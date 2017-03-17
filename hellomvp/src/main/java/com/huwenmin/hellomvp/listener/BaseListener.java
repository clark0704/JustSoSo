package com.huwenmin.hellomvp.listener;

/**
 * 作者：胡文敏 on 2017/3/16 16:53
 * <p>
 * 功能：热点的view层
 */

public interface BaseListener<T extends Object> {
    void onSuccess(T t);
    void onError(String error);
}