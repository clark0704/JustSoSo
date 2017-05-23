package com.huwenmin.brvahdemo.http;


import com.huwenmin.brvahdemo.module.UPMLoginDeviceRespBean;
import com.huwenmin.brvahdemo.module.UPMPhoneCodeRespBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceRespBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
     * 获取手机验证码、用戶登出、根據驗證碼登錄、用户强转接口-- 昵称、邮箱转手机账号
     * 用户强转接口-- QQ、微博转手机账号
     * @param bean
     */
    @POST("pcum")
    Observable<ResponseBody> getBody(@Body RequestBody bean);
}
