package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/23 13:42
 * <p>
 * 功能：用户强转接口-- QQ、微博转手机账号
 *        参数名称	类型	注释
 code	Int(11)	返回码
 description	String(256)	返回描述
 token	String(64)	用户登录之后的token，当result=0可用。
 expireTime	String(32)	Token失效时间
 userKey	String(32)	用户userkey

 *
 */

public class UPMQQOrEmailToPhoneRespBean {
    private int code;
    private String description;
    private String token;
    private String expireTime;
    private String userKey;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
