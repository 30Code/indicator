package cn.linhome.indicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.linhome.indicator.holder.BannerViewHolder;

public class CustomViewHolder implements BannerViewHolder<CustomData>
{
    private ImageView iv;

    @Override
    public View createView(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
        iv = (ImageView) view.findViewById(R.id.iv);
        return view;
    }

    @Override
    public void onBind(Context context, int position, CustomData data)
    {
        // 数据绑定
        Glide.with(context).load(data.getUrl()).into(iv);
    }
}
