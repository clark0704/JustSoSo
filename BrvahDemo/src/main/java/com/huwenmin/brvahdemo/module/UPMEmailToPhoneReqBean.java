package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/23 13:42
 * <p>
 * 功能：用户强转接口-- QQ、微博转手机账号
 *          参数名称	是否必须	类型	注释
 mail	是	String(32)	邮箱
 name	是	String(32)	昵称
 passwd	是	String(32)	密码
 phone	是	String(32)	强转手机号
 mac	是	String(32)	Mac地址
 siteId	是	String(8)	站点ID
 tvId	是	String(32)	tvid
 deviceId	是	String(32)	设备序列号
 cilentIp	是	String(32)	客户端IP
 */

public class UPMEmailToPhoneReqBean {

    private String mail;
    private String name;
    private String passwd;
    private String phone;
    private String mac;
    private String siteId;
    private String clientIp;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
