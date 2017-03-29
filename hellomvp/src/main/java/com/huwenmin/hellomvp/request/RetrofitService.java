package com.huwenmin.hellomvp.request;

import com.huwenmin.hellomvp.listener.RequestCallback;
import com.huwenmin.hellomvp.model.bean.AssertPageBean;

import io.reactivex.Observable;
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
    private static Observable sObservable;
    private static Observer sObserver;

    private static volatile RetrofitService instance;

    private RetrofitService() {
    }

    public static RetrofitService getInstance() {
        if (instance == null) {
          synchronized (RetrofitService.class) {
             if (instance == null)instance = new RetrofitService();
          }
        }
        return instance;
    }

    public RetrofitService getObservable() {
        sObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sObserver);

        return getInstance();
    }

    public RetrofitService getHotspotData(int p, long time, int limit, final RequestCallback<AssertPageBean> callback) {
        sObservable = RetrofitHelper.getInstance().getApi().getTodayHot(p, time, limit);
        sObserver = new Observer<AssertPageBean>() {
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
        };

        return getInstance();
    }


}
