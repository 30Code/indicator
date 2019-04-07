package cn.linhome.indicator.holder;

public interface HolderCreator<VH extends BannerViewHolder>
{
    VH createViewHolder();
}
