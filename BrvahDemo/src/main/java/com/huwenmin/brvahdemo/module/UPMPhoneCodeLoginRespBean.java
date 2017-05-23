package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/23 14:43
 * <p>
 * 功能：根据短信验证码登陆接口(响应消息)
 *   参数名称	类型	注释
 code	int	返回码
 description	String	返回描述
 userKey	String	华数会员ID
 phone	String	登录用的手机号码
 token	String	登录token
 expireTime	int	多少秒如果没有任何操作是否失效
 */

public class UPMPhoneCodeLoginRespBean {

    private int code;
    private String description;
    private String userKey;
    private String phone;
    private String token;
    private int expireTime;

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

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
}
