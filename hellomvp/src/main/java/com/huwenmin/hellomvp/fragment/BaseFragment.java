package com.huwenmin.hellomvp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huwenmin.hellomvp.activity.BaseActivity;


/**
 * 作者：胡文敏 on 2017/3/17 14:01
 * <p>
 * 功能：BaseFragment
 */

public abstract class BaseFragment extends Fragment {

    //获取布局文件ID
    protected abstract int getLayoutId();

    //获取宿主Activity
    protected abstract BaseActivity getHoldingActivity() ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            return inflater.inflate(getLayoutId(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        destroyViewAndThing();
        super.onDestroy();
    }

    protected abstract void destroyViewAndThing();

    /**
     *
     * @param clz 新的activity类
     * @param isCloseCurrentActivity
     * @param ex
     */
    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(getHoldingActivity(), clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            getHoldingActivity().finish();
        }
    }
}
