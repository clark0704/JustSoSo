package com.huwenmin.playerexample.listener;

/**
 * 作者：Administrator on 2017/3/31 09:33
 * <p>
 * 功能：媒体播放器监听
 */

public interface WasuMediaPlayerListener {
    //准备好
    void onPrepared();
    //自动完成
    void onAutoCompletion();
    //完成
    void onCompletion();

    void onBufferingUpdate(int percent);

    void onSeekComplete();
    //出错
    void onError(int what, int extra);
    //信息
    void onInfo(int what, int extra);
    //视频size变化监听
    void onVideoSizeChanged();
    //退出全屏
    void onBackFullscreen();
    //视频暂停
    void onVideoPause();
    //视频继续
    void onVideoResume();
}
