package com.huwenmin.hellomvp.request;

import com.huwenmin.hellomvp.bean.AssertPageBean;


import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 作者：胡文敏 on 2017/3/16 16:35
 * <p>
 * 功能： 网络请求接口
 */

public interface RetrofitApiService {

    @GET("Phone/todayhot")
    Observable<AssertPageBean> getTodayHot();
}
