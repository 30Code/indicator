package cn.linhome.indicator.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BannerViewPager extends ViewPager
{

    private boolean mScrollable = true;

    public BannerViewPager(Context context)
    {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return this.mScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return this.mScrollable && super.onInterceptTouchEvent(ev);
    }

    public void setScrollable(boolean scrollable)
    {
        this.mScrollable = scrollable;
    }

}
