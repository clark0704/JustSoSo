package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/23 13:42
 * <p>
 * 功能：用户强转接口-- QQ、微博转手机账号
 *          参数名称	是否必须	类型	注释
 account	是	String(32)	账号信息：QQ账号、微博账号
 accountType	是	int(2)	 会员类型：QQ注册： 9 微博注册：11
 passwd	是	String(32)	密码
 phone	是	String(12)	强转手机号
 mac	是	String(32)	Mac地址
 siteId	是	String(8)	站点ID
 tvId	是	String(32)	tvid
 deviceId	是	String(32)	设备序列号

 */

public class UPMQQToPhoneReqBean {

    private String account;
    private int accountType;
    private String password;
    private String phone;
    private String mac;
    private String siteId;
    private String tvId;
    private String deviceId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
}
