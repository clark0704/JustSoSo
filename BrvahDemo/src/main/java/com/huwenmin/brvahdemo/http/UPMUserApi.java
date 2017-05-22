package com.huwenmin.brvahdemo.http;


import com.huwenmin.brvahdemo.module.UPMLoginDeviceRespBean;
import com.huwenmin.brvahdemo.module.UPMPhoneCodeRespBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceRespBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 作者：Administrator on 2017/5/19 17:03
 * <p>
 * 功能： UPM的相关接口
 */

public interface UPMUserApi {
    /**
     * 设备注册
     * @param bean
     */
    @POST("pcum")
    Observable<UPMRegisterDeviceRespBean> getUserRegisterMes(@Body RequestBody bean);
    /**
     *  设备登录
     * @param bean
     */
    @POST("pcum")
    Observable<UPMLoginDeviceRespBean> getUserLoginMes(@Body RequestBody bean);
    /**
     *
     * 获取手机验证码
     * @param bean
     */
    @Headers({"v:1"})
    @POST("pcum")
    Observable<UPMPhoneCodeRespBean> getPhoneCode(@Body RequestBody bean);
}
