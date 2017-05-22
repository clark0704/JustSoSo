package com.huwenmin.brvahdemo.http;


import com.huwenmin.brvahdemo.module.UPMLoginDeviceRespBean;
import com.huwenmin.brvahdemo.module.UPMPhoneCodeRespBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceRespBean;

/**
 * 作者：Administrator on 2017/5/22 09:55
 * <p>
 * 功能：基础的UPM请求接口
 */

public interface BaseRequestListener{

    void getRegisterBean(UPMRegisterDeviceRespBean bean);

    void getLoginBean(UPMLoginDeviceRespBean bean);

    void getPhoneCode(UPMPhoneCodeRespBean bean);
}
