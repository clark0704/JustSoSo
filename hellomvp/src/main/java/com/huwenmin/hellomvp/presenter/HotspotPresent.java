package com.huwenmin.hellomvp.presenter;

import android.content.Intent;

import com.huwenmin.hellomvp.bean.AssertPageBean;
import com.huwenmin.hellomvp.listener.BaseListener;
import com.huwenmin.hellomvp.request.RetrofitHelper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：Administrator on 2017/3/16 17:38
 * <p>
 * 功能：热点的P
 */

public class HotspotPresent implements BasePresenter {
    private Disposable mDisposable;
    private BaseListener mListener;
    private AssertPageBean mPageBean;

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        if (mDisposable != null && mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void pause() {

    }
    @Override
    public void setListener(BaseListener listener) {
        mListener = listener;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void getHotspotData() {
        RetrofitHelper.getInstance().getService().getTodayHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AssertPageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(AssertPageBean value) {
                        mPageBean = value;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                      if (mListener != null) mListener.onError("请求失败！！");
                    }

                    @Override
                    public void onComplete() {
                        if(mListener != null){
                            mListener.onSuccess(mPageBean);
                        }
                    }
                });
    }
}
