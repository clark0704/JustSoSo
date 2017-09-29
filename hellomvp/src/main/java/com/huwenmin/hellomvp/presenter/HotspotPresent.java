package com.huwenmin.hellomvp.presenter;

import android.content.Context;
import android.content.Intent;

import com.huwenmin.hellomvp.listener.RequestCallback;
import com.huwenmin.hellomvp.model.bean.AssertPageBean;
import com.huwenmin.hellomvp.request.RetrofitService;
import com.huwenmin.hellomvp.view.HotspotView;


import io.reactivex.disposables.Disposable;

/**
 * 作者：Administrator on 2017/3/16 17:38
 * <p>
 * 功能：热点的P
 */

public class HotspotPresent implements BasePresenter {
    private Disposable mDisposable;
    private HotspotView mHotspotView;

    public HotspotPresent(HotspotView hotspotView) {
        mHotspotView = hotspotView;
    }

    @Override
    public void onStop() {
       if (mDisposable.isDisposed())mDisposable.dispose();
    }

    public void requestHotspotData(int p, long time, int limit ){
        RetrofitService.getInstance().getHotspotData(p, time, limit, new RequestCallback<AssertPageBean>() {
            @Override
            public void onSuccess(AssertPageBean assertPageBean) {
                if (assertPageBean != null  ){
                    mHotspotView.getHotspotData(assertPageBean);
                }else {
                    mHotspotView.showError("暂无数据");
                }
            }

            @Override
            public void onError(String error) {
                mHotspotView.showError(error);
            }

            @Override
            public void getDisposable(Disposable disposable) {
                mDisposable = disposable;
            }
        }).getObservable();
    }

}
