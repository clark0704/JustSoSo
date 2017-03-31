package com.huwenmin.playerexample.model;

/**
 * 作者：Administrator on 2017/3/31 14:22
 * <p>
 * 功能：视频内部接受数据model
 */

public class WasuVideoModel {
    String url;

    float speed = 1;

    boolean looping;

    public WasuVideoModel(String url, boolean loop, float speed) {
        this.url = url;
        this.looping = loop;
        this.speed = speed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
