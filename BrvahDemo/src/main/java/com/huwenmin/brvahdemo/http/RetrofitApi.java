package com.huwenmin.brvahdemo.http;

import com.huwenmin.brvahdemo.module.AssertItem;
import com.huwenmin.brvahdemo.module.AssertPageBean;

import java.util.List;

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
    Observable<AssertPageBean> getTodayHot(@Path("p") int p, @Path("time") long time, @Path("limit") int limit);
    /**
     * 2、热点频道精品推荐
     * 测试地址：http://clientapi.cs.wasu.cn/Phone/hotbest
     */
    @GET("Phone/hotbest")
    Observable<List<AssertItem>> getHotBest();
    /**
     * 1、热点频道轮播图
     *  测试地址（hosts绑定）：
     *  http://clientapi.cs.wasu.cn/Phone/hotlbt
     */
    @GET("Phone/hotlbt")
    Observable<List<AssertItem>> getHotLbt();
    /**
     * 4、热点视频
     *    历史数据地址：
     *     http://clientapi.cs.wasu.cn/Phone/todayhot/p/1/time/1487232637/limit/8
     */
    @GET("Phone/todayhot/p/{p}/time/{time}/limit/{limit}")
    Observable<AssertPageBean> getHistoryHot(@Path("time") long time,@Path("p") int p,@Path("limit") int limit);
}
