package com.huwenmin.hellomvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.huwenmin.hellomvp.fragment.BaseFragment;

/**
 * 作者：Administrator on 2017/3/17 13:48
 * <p>
 * 功能：baseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {
    //布局文件ID
    protected abstract int getLayoutId();
    //初始化view
    protected abstract void initView();
    //初始化数据
    protected abstract void initData();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        initView();

        initData();
    }
}
