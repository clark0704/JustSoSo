package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/22 16:34
 * <p>
 * 功能：手机登陆--获取短信验证码类
 *  参数名称	类型	注释
 code	int	返回码
 description	String	返回描述
 resendInterval	String	多少时间之后能重发验证码
 expireTime	int	多少秒如果没有任何操作是否失效

 */

public class UPMPhoneCodeRespBean {

    private int code;
    private String description;
    private String resendInterval;
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

    public String getResendInterval() {
        return resendInterval;
    }

    public void setResendInterval(String resendInterval) {
        this.resendInterval = resendInterval;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
}
