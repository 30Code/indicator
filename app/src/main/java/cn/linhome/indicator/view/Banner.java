package cn.linhome.indicator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.linhome.indicator.BannerScroller;
import cn.linhome.indicator.R;
import cn.linhome.indicator.holder.BannerViewHolder;
import cn.linhome.indicator.holder.HolderCreator;
import cn.linhome.indicator.listener.OnBannerClickListener;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static android.support.v4.view.ViewPager.PageTransformer;

public class Banner extends FrameLayout implements OnPageChangeListener
{
    private boolean isScroll = true;
    private boolean isLoop = true;
    private int count = 0;
    private int currentItem = -1;
    private int lastPosition;
    private List mDatas;
    private HolderCreator<BannerViewHolder> creator;
    private BannerViewPager viewPager;
    private BannerPagerAdapter adapter;
    private OnPageChangeListener mOnPageChangeListener;
    private OnBannerClickListener listener;
    private int mPageLeftMargin;
    private int mPageRightMargin;
    private static final int NUM = 5000;

    public Banner(Context context)
    {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mDatas = new ArrayList<>();
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs)
    {
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.banner, this, true);
        viewPager = (BannerViewPager) view.findViewById(R.id.bannerViewPager);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.leftMargin = mPageLeftMargin;
        params.rightMargin = mPageRightMargin;
        viewPager.setLayoutParams(params);
        initViewPagerScroll();
    }

    private void handleTypedArray(Context context, AttributeSet attrs)
    {
        if (attrs == null)
        {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        mPageLeftMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_page_left_margin, 0);
        mPageRightMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_page_right_margin, 0);
        typedArray.recycle();
    }

    private void initViewPagerScroll()
    {
        try
        {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            BannerScroller scroller = new BannerScroller(viewPager.getContext());
//            scroller.setDuration(scrollTime);
            mField.set(viewPager, scroller);
        } catch (Exception e)
        {

        }
    }

    public Banner setAutoPlay(boolean isAutoPlay)
    {
        return this;
    }

    public Banner setLoop(boolean isLoop)
    {
        this.isLoop = isLoop;
        return this;
    }

    public Banner setDelayTime(int delayTime)
    {
//        this.delayTime = delayTime;
        return this;
    }

    public Banner setBannerAnimation(Class<? extends PageTransformer> transformer)
    {
        try
        {
            viewPager.setPageTransformer(true, transformer.newInstance());
        } catch (Exception e)
        {

        }
        return this;
    }

    public Banner setOffscreenPageLimit(int limit)
    {
        if (viewPager != null)
        {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    public Banner setPages(List<?> datas, HolderCreator<BannerViewHolder> creator)
    {
        this.mDatas = datas;
        this.creator = creator;
        this.count = datas.size();
        return this;
    }

    public Banner start()
    {
        if (count > 0)
        {
            setData();
        }
        return this;
    }

    private void setData()
    {
        if (isLoop)
        {
            if (currentItem == -1)
            {
                currentItem = NUM / 2 - ((NUM / 2) % count) + 1;
            }
            lastPosition = 1;
        } else
        {
            if (currentItem == -1)
            {
                currentItem = 0;
            }
            lastPosition = 0;
        }
        if (adapter == null)
        {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.setOffscreenPageLimit(count);
        if (isScroll && count > 1)
        {
            viewPager.setScrollable(true);
        } else
        {
            viewPager.setScrollable(false);
        }
        startAutoPlay();
    }

    public void startAutoPlay()
    {

    }

    public void stopAutoPlay()
    {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                startAutoPlay();
                break;
            case MotionEvent.ACTION_DOWN:
                float downX = ev.getX();
                if (mPageLeftMargin != 0 || mPageRightMargin != 0)
                {
                    if (downX > mPageLeftMargin && downX < getWidth() - mPageRightMargin)
                    {
                        stopAutoPlay();
                    }
                } else
                {
                    stopAutoPlay();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int toRealPosition(int position)
    {
        //int realPosition = (position - 1) % count;
        int realPosition;
        if (isLoop)
        {
            realPosition = (position - 1 + count) % count;
        } else
        {
            realPosition = (position + count) % count;
        }
        if (realPosition < 0)
            realPosition += count;
        return realPosition;
    }

    private class BannerPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            if (mDatas.size() == 1)
            {
                return mDatas.size();
            } else if (mDatas.size() < 1)
            {
                return 0;
            } else
            {
                if (isLoop)
                    //return mDatas.size() + 2;
                    return NUM;
                else
                    return mDatas.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            if (creator == null)
            {
                throw new RuntimeException("[Banner] --> The layout is not specified,please set holder");
            }
            BannerViewHolder holder = creator.createViewHolder();

            View view = holder.createView(container.getContext());
            container.addView(view);

            if (mDatas != null && mDatas.size() > 0)
            {
                holder.onBind(container.getContext(), toRealPosition(position), mDatas.get(toRealPosition(position)));
            }
            if (listener != null)
            {
                view.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listener.onBannerClick(toRealPosition(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        if (mOnPageChangeListener != null)
        {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        if (!isLoop)
            return;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        if (mOnPageChangeListener != null)
        {
            mOnPageChangeListener.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position)
    {
        currentItem = position;
        if (mOnPageChangeListener != null)
        {
            mOnPageChangeListener.onPageSelected(toRealPosition(position));
        }
    }

    public Banner setOnBannerClickListener(OnBannerClickListener listener)
    {
        this.listener = listener;
        return this;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener)
    {
        mOnPageChangeListener = onPageChangeListener;
    }
}
