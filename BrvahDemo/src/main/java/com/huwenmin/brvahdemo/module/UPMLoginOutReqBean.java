package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/23 14:46
 * <p>
 * 功能：用户登出(请求消息  MsgCode：1102008)
 *  参数名称	是否必须	类型	注释
 mac	是	String	当前设备的mac地址
 userKey	是	String	用户key
 tvId	是	String	Tvid
 deviceId	是	String	设备ID
 token	是	String	登录token

 */

public class UPMLoginOutReqBean {

    private String mac;
    private String userKey;
    private String tvId;
    private String deviceId;
    private String token;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getTvId() {
        return tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
