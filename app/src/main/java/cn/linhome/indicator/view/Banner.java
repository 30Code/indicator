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
    private boolean mIsScroll = true;
    private boolean mIsLoop = true;
    private int mCount = 0;
    private int mCurrentItem = -1;
    private int mLastPosition;
    private List mDatas;
    private HolderCreator<BannerViewHolder> mCreator;
    private BannerViewPager mViewPager;
    private BannerPagerAdapter mPagerAdapter;
    private OnPageChangeListener mOnPageChangeListener;
    private OnBannerClickListener mListener;
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
        mViewPager = (BannerViewPager) view.findViewById(R.id.bannerViewPager);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.leftMargin = mPageLeftMargin;
        params.rightMargin = mPageRightMargin;
        mViewPager.setLayoutParams(params);
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
            BannerScroller scroller = new BannerScroller(mViewPager.getContext());
//            scroller.setDuration(scrollTime);
            mField.set(mViewPager, scroller);
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
        this.mIsLoop = isLoop;
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
            mViewPager.setPageTransformer(true, transformer.newInstance());
        } catch (Exception e)
        {

        }
        return this;
    }

    public Banner setOffscreenPageLimit(int limit)
    {
        if (mViewPager != null)
        {
            mViewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    public Banner setPages(List<?> datas, HolderCreator<BannerViewHolder> creator)
    {
        this.mDatas = datas;
        this.mCreator = creator;
        this.mCount = datas.size();
        return this;
    }

    public Banner start()
    {
        if (mCount > 0)
        {
            setData();
        }
        return this;
    }

    private void setData()
    {
        if (mIsLoop)
        {
            if (mCurrentItem == -1)
            {
                mCurrentItem = NUM / 2 - ((NUM / 2) % mCount) + 1;
            }
            mLastPosition = 1;
        } else
        {
            if (mCurrentItem == -1)
            {
                mCurrentItem = 0;
            }
            mLastPosition = 0;
        }
        if (mPagerAdapter == null)
        {
            mPagerAdapter = new BannerPagerAdapter();
            mViewPager.addOnPageChangeListener(this);
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentItem);
        mViewPager.setOffscreenPageLimit(mCount);
        if (mIsScroll && mCount > 1)
        {
            mViewPager.setScrollable(true);
        } else
        {
            mViewPager.setScrollable(false);
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
        int realPosition;
        if (mIsLoop)
        {
            realPosition = (position - 1 + mCount) % mCount;
        } else
        {
            realPosition = (position + mCount) % mCount;
        }
        if (realPosition < 0)
        {
            realPosition += mCount;
        }
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
                if (mIsLoop)
                {
                    return NUM;
                }
                else
                {
                    return mDatas.size();
                }
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
            if (mCreator == null)
            {
                throw new RuntimeException("[Banner] --> The layout is not specified,please set holder");
            }
            BannerViewHolder holder = mCreator.createViewHolder();

            View view = holder.createView(container.getContext());
            container.addView(view);

            if (mDatas != null && mDatas.size() > 0)
            {
                holder.onBind(container.getContext(), toRealPosition(position), mDatas.get(toRealPosition(position)));
            }
            if (mListener != null)
            {
                view.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mListener.onBannerClick(toRealPosition(position));
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
        if (!mIsLoop)
        {
            return;
        }
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
        mCurrentItem = position;
        if (mOnPageChangeListener != null)
        {
            mOnPageChangeListener.onPageSelected(toRealPosition(position));
        }
    }

    public Banner setOnBannerClickListener(OnBannerClickListener listener)
    {
        this.mListener = listener;
        return this;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener)
    {
        mOnPageChangeListener = onPageChangeListener;
    }
}
