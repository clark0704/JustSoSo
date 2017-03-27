package com.huwenmin.hellomvp.request;

import com.huwenmin.hellomvp.model.bean.AssertPageBean;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 作者：胡文敏 on 2017/3/16 16:35
 * <p>
 * 功能： 网络请求接口
 */

public interface RetrofitApi {

    @GET("Phone/todayhot/p/{p}/time/{time}/limit/{limit}")
    Observable<AssertPageBean> getTodayHot(@Path("p") int p,@Path("time") long time,@Path("limit") int limit);
}
