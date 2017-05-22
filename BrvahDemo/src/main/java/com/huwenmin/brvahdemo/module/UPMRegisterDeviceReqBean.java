package com.huwenmin.brvahdemo.module;


/**
 * 作者：Administrator on 2017/5/18 15:19
 * <p>
 * 功能：登陆注册的请求参数（新版登陆注册）
 */

public class UPMRegisterDeviceReqBean  {

    /**
     * androidV : 4.2.2
     * appName : cn.com.wasu.main
     * appV : 3.3.0.23
     * chip : amlogic
     * cpuSerial : 000000000000000c
     * device : g18ref
     * eth0Mac : A0-88-B4-AC-86-F0
     * identification : ccbf5f7f-4f2e-41b2-938c-8fed2d8c43dc
     * jsonP : false
     * login : false
     * manufacturer : Excenon
     * memSize : 413396992
     * properties : {}
     * registerIp : fe80::1c67:e3ff:fe79:5220%usb0
     * sdkV : 1.1.0
     * sourceId : 0
     * tvId : 0302HL1640929923c
     * wifiMac : 78-DD-08-B1-2C-AE
     **/
//       参数	描述	属性格式	        是否可空
//     device	用户设备名称。	String	否
//     tvId	互联网机顶盒唯一确认串。	String	否
//     memSize	终端内存大小。	long	否
//     chip	芯片类型。	String	否
//     wifiMac	Wifi的mac地址。	String	否
//     eth0Mac	第一块网卡的mac地址。	String	否
//     cpuSerial	CPU序列号。	String	否
//     androidV	安卓版本。	String	否
//     appName	应用程序名称。	String	否
//     appV	应用程序版本。	String	否
//     sdkV	终端使用的sdk版本	String	否
//     manufacturer	生产厂家。	String	否
//     registerIp	注册时IP地址。	String	否
//     reserved	保留字符。
//     login	是否登录	boolean	默认为false。

    private String androidV;
    private String appName;
    private String appV;
    private String chip;
    private String cpuSerial;
    private String device;
    private String eth0Mac;
    private String identification;
    private boolean jsonP;
    private boolean login;
    private String manufacturer;
    private int memSize;
    private PropertiesBean properties;
    private String registerIp;
    private String sdkV;
    private int sourceId;
    private String tvId;
    private String wifiMac;

    public String getAndroidV() {
        return androidV;
    }

    public void setAndroidV(String androidV) {
        this.androidV = androidV;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppV() {
        return appV;
    }

    public void setAppV(String appV) {
        this.appV = appV;
    }

    public String getChip() {
        return chip;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public String getCpuSerial() {
        return cpuSerial;
    }

    public void setCpuSerial(String cpuSerial) {
        this.cpuSerial = cpuSerial;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getEth0Mac() {
        return eth0Mac;
    }

    public void setEth0Mac(String eth0Mac) {
        this.eth0Mac = eth0Mac;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public boolean isJsonP() {
        return jsonP;
    }

    public void setJsonP(boolean jsonP) {
        this.jsonP = jsonP;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getMemSize() {
        return memSize;
    }

    public void setMemSize(int memSize) {
        this.memSize = memSize;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getSdkV() {
        return sdkV;
    }

    public void setSdkV(String sdkV) {
        this.sdkV = sdkV;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getTvId() {
        return tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public static class PropertiesBean {
    }

    @Override
    public String toString() {
        return "UPMRegisterDeviceRespBean{" +
                "androidV='" + androidV + '\'' +
                ", appName='" + appName + '\'' +
                ", appV='" + appV + '\'' +
                ", chip='" + chip + '\'' +
                ", cpuSerial='" + cpuSerial + '\'' +
                ", device='" + device + '\'' +
                ", eth0Mac='" + eth0Mac + '\'' +
                ", identification='" + identification + '\'' +
                ", jsonP=" + jsonP +
                ", login=" + login +
                ", manufacturer='" + manufacturer + '\'' +
                ", memSize=" + memSize +
                ", properties=" + properties +
                ", registerIp='" + registerIp + '\'' +
                ", sdkV='" + sdkV + '\'' +
                ", sourceId=" + sourceId +
                ", tvId='" + tvId + '\'' +
                ", wifiMac='" + wifiMac + '\'' +
                '}';
    }
}
