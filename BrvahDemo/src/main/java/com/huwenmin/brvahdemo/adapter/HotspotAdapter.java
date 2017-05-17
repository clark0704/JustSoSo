package com.huwenmin.brvahdemo.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huwenmin.brvahdemo.R;
import com.huwenmin.brvahdemo.module.AssertItem;
import com.huwenmin.brvahdemo.utils.TimeTools;

import java.util.List;

/**
 * 作者：huwenmin on 2017/5/16 15:31
 * <p>
 * 功能：适配器
 */

public class HotspotAdapter extends BaseMultiItemQuickAdapter<AssertItem, BaseViewHolder> {

    public HotspotAdapter(List<AssertItem> data) {
        super(data);
        addItemType(AssertItem.NORMAL, R.layout.item_hotspot);
        addItemType(AssertItem.BIG_IMG, R.layout.item_hotspot_iv);
//        addItemType(AssertItem.BANNER,R.layout.item_hotspot);
    }

    @Override
    protected void convert(BaseViewHolder holder, AssertItem assertItem) {
        ImageView imageView;
        switch (holder.getItemViewType()) {
            case AssertItem.NORMAL:
                holder.setText(R.id.tv_title, assertItem.getTitle())
                        .setText(R.id.tv_pop, assertItem.getPop())
                        .setText(R.id.tv_length, setDurationStr(Integer.parseInt(assertItem.getDuration())))
                        .setText(R.id.tv_tag, assertItem.getClassX());

                imageView = holder.getView(R.id.sdv_img);
                Glide.with(mContext).load(assertItem.getPic()).into(imageView);
                break;
            case AssertItem.BIG_IMG:
                imageView = holder.getView(R.id.hotspot_image);
                Glide.with(mContext).load(assertItem.getPic()).into(imageView);
                holder.setText(R.id.hotspot_title,assertItem.getTitle());
                break;

            case AssertItem.BANNER:
                break;
        }
    }
    /**
     * 获取时间 （格式:00:00 ）
     *
     * @param str
     * @return
     */
    private String setDurationStr(int str) {
        String duration = null;
        if (str > 0) {
            duration = TimeTools.stringForTime(str * 1000);
            if (duration.startsWith("00:")) duration = duration.replace("00:", "");
            if (duration.length() < 3) duration = "00:" + duration;
        }
        return duration;
    }
}
