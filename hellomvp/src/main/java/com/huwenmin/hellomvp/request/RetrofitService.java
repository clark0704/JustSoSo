package com.huwenmin.hellomvp.request;

import com.huwenmin.hellomvp.listener.RequestCallback;
import com.huwenmin.hellomvp.model.bean.AssertPageBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：胡文敏 on 2017/3/27 09:37
 * <p>
 * 功能：网络请求（其实这个相当于MVP中的M）
 */

public class RetrofitService {
    private static AssertPageBean mPageBean;

    public static void getHotspotData(int p, long time, int limit, final RequestCallback<AssertPageBean> callback) {
        RetrofitHelper.getInstance().getApi().getTodayHot(p, time, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AssertPageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        callback.getDisposable(d);
                    }

                    @Override
                    public void onNext(AssertPageBean value) {
                        mPageBean = value;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onError("获取数据失败");
                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(mPageBean);
                    }
                });
    }
}
