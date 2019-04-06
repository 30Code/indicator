package cn.linhome.indicator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.linhome.indicator.holder.BannerViewHolder;
import cn.linhome.indicator.holder.HolderCreator;
import cn.linhome.indicator.view.Banner;

public class MainActivity extends AppCompatActivity
{
    private Banner banner1;
    private List<CustomData> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner1 = (Banner) findViewById(R.id.banner1);

        mList = new ArrayList<>();
        mList.add(new CustomData("http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg", "CustomLayout", false));
        mList.add(new CustomData("http://img.zcool.cn/community/01996b57a7f6020000018c1bedef97.jpg", "Transformer", false));
        mList.add(new CustomData("http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg", "Viewpager", false));

        banner1.setAutoPlay(true)
                .setPages(mList, new HolderCreator<BannerViewHolder>()
                {
                    @Override
                    public BannerViewHolder createViewHolder()
                    {
                        return new CustomViewHolder();
                    }
                })
                .setBannerAnimation(Transformer.ScaleRight)
                .start();
    }
}
