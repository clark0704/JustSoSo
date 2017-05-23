package com.huwenmin.brvahdemo.module;

/**
 * 作者：Administrator on 2017/5/19 16:49
 * <p>
 * 功能：三方服务器--登录接口
 */

public class UPMRegisterDeviceRespBean {

    /**
     * code : 0
     * deviceId : b98420d88b72d86dde1b0cc53f5ee22a
     * encryptV : 1
     * identification : 9b315674-6fbc-443f-8115-df400843f9fb
     * jsonP : false
     * properties : {}
     * publicKey : MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMXa8rkksFZbb0jHcghcKKVXy6XnY2iaa0RVd/sRl2drh/EvP4kHm+354vDRi3SdKgvYGZMzqINSSYzTdB1qj7MCAwEAAQ==
     * sourceId : 0
     */

    private int code;
    private String deviceId;
    private int encryptV = -1;
    private String identification;
    private boolean jsonP;
    private PropertiesBean properties;
    private String publicKey;
    private int sourceId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getEncryptV() {
        return encryptV;
    }

    public void setEncryptV(int encryptV) {
        this.encryptV = encryptV;
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

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public static class PropertiesBean {
    }
}
