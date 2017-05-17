package com.huwenmin.brvahdemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huwenmin.brvahdemo.R;
import com.huwenmin.brvahdemo.module.AssertItem;

import java.util.LinkedList;
import java.util.List;

/**
 * 作者：Administrator on 2017/5/17 10:27
 * <p>
 * 功能：轮播图适配器
 */

public class BannerPagerAdapter extends PagerAdapter {

    private List<AssertItem> data;
    private Context mContext;
    private LinkedList<View> mViewCache = null;
    private LayoutInflater mLayoutInflater = null;

    public BannerPagerAdapter(@Nullable List<AssertItem> data, Context context) {
        this.data = data;
        this.mContext = context;
        mViewCache = new LinkedList<>();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (data == null)
            return mViewCache.size();
        else
            return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder holder;
        AssertItem item = data.get(position);

        View view = null;

        if (mViewCache.size() == 0) {
            view = mLayoutInflater.inflate(R.layout.item_banner, null);
            holder = new ViewHolder(view);
            view.setTag(holder);


        } else {
            Log.e("test", "mViewCache:" + mViewCache);
            view = mViewCache.removeFirst();
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext).load(Uri.parse(item.getPic())).into(holder.mImageView);
        holder.title.setText(item.getTitle());
        holder.desc.setText("");

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mViewCache.add((View) object);
        container.removeView((View) object);
    }

    private class ViewHolder {
        ImageView mImageView;
        TextView title, desc;

        private ViewHolder(View view) {
            mImageView = (ImageView) view.findViewById(R.id.assert_image);
            title = (TextView) view.findViewById(R.id.tv_title);
            desc = (TextView) view.findViewById(R.id.tv_desc);
        }
    }
}
