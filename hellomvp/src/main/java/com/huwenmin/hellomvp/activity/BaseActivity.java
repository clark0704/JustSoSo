package com.huwenmin.hellomvp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huwenmin.hellomvp.R;
import com.huwenmin.hellomvp.fragment.BaseFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(false);//true  将布局置顶，文字覆盖状态栏    false文字在状态栏的下面
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);//设置栏状态颜色，true默认使用setStatusBarTintResource颜色，否则为透明色
//            tintManager.setStatusBarTintResource(R.color.colorPrimary);//通知栏所需颜色
        }
        setContentView(getLayoutId());

        initView();

        initData();
    }
    /*注：setTranslucentStatus
    *沉浸式状态栏都设置为true
    * 1.true    布局覆盖状态栏
    * setStatusBarTintEnabled为true状态栏使用setStatusBarTintResource所设置的颜色
    * setStatusBarTintEnabled为false状态栏使用透明色；及无颜色值沉浸式状态栏
    *
    * 2.false   布局在状态栏下面
    *setStatusBarTintEnabled为true状态栏使用styles中colorPrimaryDark属性的颜色
    * setStatusBarTintEnabled为false状态栏使用styles中colorPrimaryDark属性的颜色
    */

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public BaseQuickAdapter mAdapter;
}
