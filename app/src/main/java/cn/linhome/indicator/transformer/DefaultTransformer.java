package cn.linhome.indicator.transformer;

import android.view.View;

import cn.linhome.indicator.transformer.ABaseTransformer;

public class DefaultTransformer extends ABaseTransformer
{

    @Override
    protected void onTransform(View view, float position)
    {
    }

    @Override
    public boolean isPagingEnabled()
    {
        return true;
    }

}
