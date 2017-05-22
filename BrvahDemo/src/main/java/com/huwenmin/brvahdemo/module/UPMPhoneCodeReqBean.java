package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/22 16:20
 * <p>
 * 功能：手机登陆--获取短信验证码接口
 *
 *请求参数：
 参数名称	是否必须	类型	注释
 phone	是	String	接受验证码的手机号
 tvId	是	String	Tvid
 deviceId	是	String	设备ID
 type	是	Int	0 - 新用户注册得到验证码；
 1 - 重置密码得到验证码；
 2 - 更换手机号得到验证码；
 5 - 用户登录所使用的验证码
 请求数据格式为JSON字符串的字节码流

 */

public class UPMPhoneCodeReqBean {

    private String phone ;

    private String tvId;

    private String deviceId;

    private int type;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
