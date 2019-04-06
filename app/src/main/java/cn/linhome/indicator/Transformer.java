package cn.linhome.indicator;

import android.support.v4.view.ViewPager.PageTransformer;

import cn.linhome.indicator.transformer.DefaultTransformer;
import cn.linhome.indicator.transformer.ScaleRightTransformer;
import cn.linhome.indicator.transformer.ScaleTransformer;

public class Transformer
{
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Scale = ScaleTransformer.class;
    public static Class<? extends PageTransformer> ScaleRight = ScaleRightTransformer.class;
}
