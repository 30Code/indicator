/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.linhome.lib.indicator.group;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.linhome.lib.indicator.adapter.PagerIndicatorAdapter;
import cn.linhome.lib.indicator.item.IPagerIndicatorItem;
import cn.linhome.lib.indicator.item.impl.ImagePagerIndicatorItem;
import cn.linhome.lib.indicator.track.IPagerIndicatorTrack;
import cn.linhome.lib.viewpager.helper.FPagerPercentChangeListener;
import cn.linhome.lib.viewpager.helper.FPagerSelectChangeListener;
import cn.linhome.lib.viewpager.helper.FViewPagerHolder;

/**
 * ViewPager指示器Group
 */
public abstract class PagerIndicatorGroup extends LinearLayout implements IPagerIndicatorGroup
{
    public PagerIndicatorGroup(Context context)
    {
        super(context);
        init();
    }

    public PagerIndicatorGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PagerIndicatorGroup(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private PagerIndicatorAdapter mAdapter;
    private IPagerIndicatorTrack mPagerIndicatorTrack;

    private void init()
    {
        //设置一个默认的Adapter
        setAdapter(new PagerIndicatorAdapter()
        {
            @Override
            public IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent)
            {
                return new ImagePagerIndicatorItem(getContext());
            }
        });
    }

    private FViewPagerHolder mViewPagerHolder = new FViewPagerHolder()
    {
        @Override
        protected void onInit(ViewPager viewPager)
        {
            mPagerSelectChangeListener.setViewPager(viewPager);
            mPagerPercentChangeListener.setViewPager(viewPager);
        }

        @Override
        protected void onRelease(ViewPager viewPager)
        {
            mPagerSelectChangeListener.setViewPager(viewPager);
            mPagerPercentChangeListener.setViewPager(viewPager);
        }
    };

    /**
     * 页面数量变化和选中监听
     */
    private FPagerSelectChangeListener mPagerSelectChangeListener = new FPagerSelectChangeListener()
    {
        @Override
        protected void onDataSetChanged()
        {
            PagerIndicatorGroup.this.onDataSetChanged(getAdapterCount());
            super.onDataSetChanged();
        }

        @Override
        protected void onSelectChanged(int index, boolean selected)
        {
            PagerIndicatorGroup.this.onSelectChanged(index, selected);
        }
    };

    /**
     * 滚动百分比监听
     */
    private FPagerPercentChangeListener mPagerPercentChangeListener = new FPagerPercentChangeListener()
    {
        @Override
        public void onShowPercent(int position, float showPercent, boolean isEnter, boolean isMoveLeft)
        {
            PagerIndicatorGroup.this.onShowPercent(position, showPercent, isEnter, isMoveLeft);
        }
    };

    @Override
    public void setViewPager(ViewPager viewPager)
    {
        mViewPagerHolder.setViewPager(viewPager);
    }

    @Override
    public ViewPager getViewPager()
    {
        return mViewPagerHolder.getViewPager();
    }

    @Override
    public void setAdapter(PagerIndicatorAdapter adapter)
    {
        if (mAdapter != null)
        {
            mAdapter.unregisterDataSetObserver(mIndicatorAdapterDataSetObserver);
        }
        mAdapter = adapter;
        if (adapter != null)
        {
            adapter.registerDataSetObserver(mIndicatorAdapterDataSetObserver);
        }
    }

    @Override
    public PagerIndicatorAdapter getAdapter()
    {
        return mAdapter;
    }

    @Override
    public void setPagerIndicatorTrack(IPagerIndicatorTrack pagerIndicatorTrack)
    {
        mPagerIndicatorTrack = pagerIndicatorTrack;
    }

    @Override
    public IPagerIndicatorTrack getPagerIndicatorTrack()
    {
        return mPagerIndicatorTrack;
    }

    @Override
    public void onDataSetChanged(int count)
    {
        if (getPagerIndicatorTrack() != null)
        {
            getPagerIndicatorTrack().onDataSetChanged(count);
        }
    }

    @Override
    public void onShowPercent(int position, float showPercent, boolean isEnter, boolean isMoveLeft)
    {
        IPagerIndicatorItem item = getPagerIndicatorItem(position);
        if (item != null)
        {
            item.onShowPercent(showPercent, isEnter, isMoveLeft);

            if (getPagerIndicatorTrack() != null)
            {
                getPagerIndicatorTrack().onShowPercent(position, showPercent, isEnter, isMoveLeft, item.getPositionData());
            }
        }
    }

    @Override
    public void onSelectChanged(int position, boolean selected)
    {
        IPagerIndicatorItem item = getPagerIndicatorItem(position);
        if (item != null)
        {
            item.onSelectChanged(selected);

            if (getPagerIndicatorTrack() != null)
            {
                getPagerIndicatorTrack().onSelectChanged(position, selected, item.getPositionData());
            }
        }
    }

    private DataSetObserver mIndicatorAdapterDataSetObserver = new DataSetObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            onDataSetChanged(mViewPagerHolder.getAdapterCount());
        }

        @Override
        public void onInvalidated()
        {
            super.onInvalidated();
        }
    };
}
