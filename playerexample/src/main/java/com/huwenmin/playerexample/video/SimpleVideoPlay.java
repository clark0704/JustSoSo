package com.huwenmin.playerexample.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 作者：胡文敏 on 2017/4/1 10:36
 * <p>
 * 功能：示例播放器
 */

public class SimpleVideoPlay extends WasuVideoPlayer {

    protected boolean mLockCurScreen;//锁定屏幕点击

    protected boolean mNeedLockFull;//是否需要锁定屏幕

    private boolean mThumbPlay;//是否点击封面播放

    protected ImageView mLockScreen;//锁屏

    public SimpleVideoPlay(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SimpleVideoPlay(Context context) {
        super(context);
    }

    public SimpleVideoPlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void startPlayLogic() {

    }

    /**
     *隐藏掉所有弹出状态
     */
    @Override
    protected void onClickUiToggle() {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull){
            mLockScreen.setVisibility(VISIBLE);
            return;
        }
    }
}
