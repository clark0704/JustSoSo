package com.huwenmin.hellomvp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huwenmin.hellomvp.R;
import com.huwenmin.hellomvp.model.bean.AssertPageBean;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;

import java.util.List;

/**
 * 作者：Administrator on 2017/3/21 15:04
 * <p>
 * 功能：首页适配器
 */

public class MainAdapter extends BaseQuickAdapter<AssertPageBean.DatasBean,BaseViewHolder> {

    public static final String TAG = "MainAdapter";
    private Context mContext;
    private ListVideoUtil listVideoUtil;

    public MainAdapter(List<AssertPageBean.DatasBean> data, Context context) {
        super(R.layout.item_main,data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, AssertPageBean.DatasBean item) {
        helper.setText(R.id.tv_content,item.getTitle());
        ImageView imageView = helper.getView(R.id.imageView);
        Glide.with(mContext).load(item.getPic()).into(imageView);

        ImageView listItemBtn = helper.getView(R.id.iv_btn);
        RelativeLayout relativeLayout = helper.getView(R.id.framerLayout);
        listVideoUtil.addVideoPlayer(helper.getLayoutPosition(), imageView, TAG, relativeLayout, listItemBtn);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                //listVideoUtil.setLoop(true);
                listVideoUtil.setPlayPositionAndTag(helper.getLayoutPosition(), TAG);
                final String url = "http://baobab.wdjcdn.com/14564977406580.mp4";
                //listVideoUtil.setCachePath(new File(FileUtils.getPath()));
                listVideoUtil.startPlay(url);
            }
        });
    }

    public void setListVideoUtil(ListVideoUtil listVideoUtil) {
        this.listVideoUtil = listVideoUtil;
    }
}
