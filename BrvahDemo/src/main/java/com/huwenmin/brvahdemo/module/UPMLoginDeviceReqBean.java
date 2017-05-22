package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/22 11:22
 * <p>
 * 功能：设备登录参数
 */

public class UPMLoginDeviceReqBean {

    private String deviceId;
    private String tvId;
    private String mac;
    private int siteId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTvId() {
        return tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }
}
