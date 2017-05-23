package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/23 14:46
 * <p>
 * 功能：用户登出(响应消息)
 *  参数名称	是否必须	类型	注释
 参数名称	类型	注释
 code	int	返回码
 description	String	返回描述


 */

public class UPMLoginOutRespBean {

    private int code;

    private String description;

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
}
