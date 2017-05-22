package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/22 12:58
 * <p>
 * 功能：设备登录返回的数据
 *
 * 元素名	值
 code	返回
 0：认证成功
 10：客户端参数错误。
 11：服务器端错误。
 12：TVID有错误。
 description	认证返回调试信息。认证失败会返回失败原因，用于用户提示或系统调试使用
 encryptV	加密版本（code=0时，此参数有效）
 publicKey	加密公钥（加密串认证时需要）（code=0时，此参数有效），使用base64编码过。

 */

public class UPMLoginDeviceRespBean {

    private int code;
    private String description;
    private String encryptV;
    private String publicKey;

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

    public String getEncryptV() {
        return encryptV;
    }

    public void setEncryptV(String encryptV) {
        this.encryptV = encryptV;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
