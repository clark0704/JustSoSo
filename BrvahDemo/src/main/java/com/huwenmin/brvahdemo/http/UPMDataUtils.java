package com.huwenmin.brvahdemo.http;


import com.huwenmin.brvahdemo.module.UPMLoginDeviceReqBean;
import com.huwenmin.brvahdemo.module.UPMPhoneCodeReqBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceReqBean;

/**
 * 作者：Administrator on 2017/5/22 10:25
 * <p>
 * 功能：UPM请求数据获取的工具类
 *   1101001,1101002,1101014,1102031 免密
 */

public class UPMDataUtils {

    public static final String REGISTER_DEVICE_MSG = "1101001";
    public static final String LOGIN_DEVICE_MSG = "1101002";
    public static final String THIRD_LOGIN_MSG = "1101066";
    public static final String GET_PHONE_CODE_MSG = "1101003";
    public static final String LOGIN_PHONE_CODE_MSG = "1102026";
    public static final String LOGIN_OUT_MSG = "1102008";

    public static final String TV_ID = "AP0000PHO100231034565780";




    public static UPMRegisterDeviceReqBean getRegisterReqBean(){
        UPMRegisterDeviceReqBean bodyBean = new UPMRegisterDeviceReqBean();
        bodyBean.setAndroidV("5.0.0");
        bodyBean.setAppName("华数TV");
        bodyBean.setAppV("3.0.4");
        bodyBean.setChip("amlogic");
        bodyBean.setCpuSerial("00000000000c");
        bodyBean.setDevice("华为");
        bodyBean.setEth0Mac("A0-88-B4-AC-86-F0");
        bodyBean.setLogin(true);
        bodyBean.setManufacturer("Excenon");
        bodyBean.setMemSize(413396992);
        bodyBean.setRegisterIp("fe80::1c67:e3ff:fe79:5220%usb0");
        bodyBean.setTvId(TV_ID);
        bodyBean.setWifiMac("78-DD-08-B1-2C-AE");

        return bodyBean;
    }

    public static UPMLoginDeviceReqBean getLoginReqBean(String deviceId){
        UPMLoginDeviceReqBean bodyBean = new UPMLoginDeviceReqBean();
        bodyBean.setDeviceId(deviceId);
        bodyBean.setTvId(TV_ID);
        bodyBean.setMac("A0-88-B4-AC-86-F0");
        bodyBean.setSiteId(10086);

        return bodyBean;
    }

    public static UPMPhoneCodeReqBean getPhoneCode(String deviceId,String phone,int type){
        UPMPhoneCodeReqBean bean = new UPMPhoneCodeReqBean();
        bean.setPhone(phone);
        bean.setTvId(TV_ID);
        bean.setDeviceId(deviceId);
        bean.setType(type);

        return bean;
    }

}
