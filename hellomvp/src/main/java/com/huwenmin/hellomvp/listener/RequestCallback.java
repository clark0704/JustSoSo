package com.huwenmin.hellomvp.listener;

import io.reactivex.disposables.Disposable;

/**
 * 作者：胡文敏 on 2017/3/16 16:53
 * <p>
 * 功能：热点的view层
 */

public interface RequestCallback<T> {
    void onSuccess(T t);
    void onError(String error);
    void getDisposable(Disposable disposable);
}