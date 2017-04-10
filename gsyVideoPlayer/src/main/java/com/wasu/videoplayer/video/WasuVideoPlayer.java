package com.wasu.videoplayer.video;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wasu.videoplayer.R;

import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.wasu.videoplayer.utils.CommonUtil.hideNavKey;

/**
 * 作者：胡文敏 on 2017/4/10 15:24
 * <p>
 * 功能：华数播放器
 */

public class WasuVideoPlayer extends VideoPlayer{

    protected ImageView mLockScreen; //锁屏按钮
    protected TextView mTitle; //标题

    protected boolean mLockCurScreen;//锁定屏幕点击
    protected boolean mNeedLockFull;//是否需要锁定屏幕

    protected Timer mDismissControlViewTimer;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;


    public WasuVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public WasuVideoPlayer(Context context) {
        super(context);
    }

    public WasuVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.player_simple_video_land;
        }
        return R.layout.player_simple_video;
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        mLockScreen = (ImageView) findViewById(R.id.lock_screen);
        mLockScreen.setVisibility(GONE);
        mLockScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE ||
                        mCurrentState == CURRENT_STATE_ERROR) {
                    return;
                }
                lockTouchLogic();
            }
        });

            mTitle =(TextView)findViewById(R.id.title);
        }


    @Override
    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
//                changeUiToNormal();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PREPAREING:
//                changeUiToPrepareingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING:
//                changeUiToPlayingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PAUSE:
//                changeUiToPauseShow();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_ERROR:
//                changeUiToError();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
//                changeUiToCompleteShow();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
//                changeUiToPlayingBufferingShow();
                break;
        }
    }

    @Override
    protected void onClickUiToggle() {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            mLockScreen.setVisibility(VISIBLE);
            return;
        }
        switch (mCurrentState){
            case CURRENT_STATE_PREPAREING:

                break;
            case CURRENT_STATE_PLAYING:

                break;
            case CURRENT_STATE_PAUSE:

                break;
            case CURRENT_STATE_AUTO_COMPLETE:

                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:

                break;

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
//                    if (mChangePosition) {
//                        int duration = getDuration();
//                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
//                        mBottomProgressBar.setProgress(progress);
//                    }
                    if (!mChangePosition && !mChangeVolume && !mBrightness) {
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
            }
        }

        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            return true;
        }


        return super.onTouch(v, event);
    }

    /**
     * 计时器，用来计时touch事件
     */
    private void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        mDismissControlViewTimer = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        mDismissControlViewTimer.schedule(mDismissControlViewTimerTask, 2500);
    }

    private void cancelDismissControlViewTimer() {
        if (mDismissControlViewTimer != null) {
            mDismissControlViewTimer.cancel();
            mDismissControlViewTimer = null;
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
            mDismissControlViewTimerTask = null;
        }
    }

    protected class DismissControlViewTimerTask extends TimerTask {

        @Override
        public void run() {
            if (mCurrentState != CURRENT_STATE_NORMAL
                    && mCurrentState != CURRENT_STATE_ERROR
                    && mCurrentState != CURRENT_STATE_AUTO_COMPLETE) {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideAllWidget();
                            mLockScreen.setVisibility(GONE);
                            if (mHideKey && mIfCurrentIsFullscreen && mShowVKey) {
                                hideNavKey(mContext);
                            }
                        }
                    });
                }
            }
        }
    }
    @Override
        public void startPlayLogic () {
            prepareVideo();
        }

        @Override
        protected void showVolumeDialog ( float deltaY, int volumePercent){
            super.showVolumeDialog(deltaY, volumePercent);
        }

        /**
         * 处理锁屏屏幕触摸逻辑
         */

    private void lockTouchLogic() {
        if (mLockCurScreen) {
            mLockScreen.setImageResource(R.drawable.player_unlock_selector);
            mLockCurScreen = false;
            if (mOrientationUtils != null)
                mOrientationUtils.setEnable(mRotateViewAuto);
        } else {
            mLockScreen.setImageResource(R.drawable.player_lock_selector);
            mLockCurScreen = true;
            if (mOrientationUtils != null)
                mOrientationUtils.setEnable(false);
            hideAllWidget();
        }
    }

    /**
     * 隐藏所有控件
     */
    protected void hideAllWidget() {
//        mTopContainer.setVisibility(INVISIBLE);
//        mBottomContainer.setVisibility(INVISIBLE);
//        mLockScreen.setVisibility(INVISIBLE);

    }

    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object... objects) {
        return super.setUp(url, cacheWithPlay, cachePath, mapHeadData, objects);
    }
}
