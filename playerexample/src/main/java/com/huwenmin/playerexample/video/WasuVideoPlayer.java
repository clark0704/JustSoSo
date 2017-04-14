package com.huwenmin.playerexample.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wasu.videoplayer.R;
import com.wasu.videoplayer.listener.LockClickListener;
import com.wasu.videoplayer.listener.StandardVideoAllCallBack;
import com.wasu.videoplayer.utils.NetworkUtils;
import com.wasu.videoplayer.video.BaseVideoPlayer;
import com.wasu.videoplayer.video.VideoPlayer;

import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.wasu.videoplayer.utils.CommonUtil.hideNavKey;
import static com.wasu.videoplayer.utils.CommonUtil.hideSupportActionBar;

/**
 * 作者：胡文敏 on 2017/4/10 15:24
 * <p>
 * 功能：华数播放器
 */

public class WasuVideoPlayer extends VideoPlayer {

    protected ImageView mLockScreen; //锁屏按钮
    protected TextView mTitle; //标题

    protected ViewGroup mRightContainer;//右边的父布局（横屏）
    protected Button mBtnEpisodeLand, mBtnDownloadLand, mBtnCollectLand;
    protected View mLineTop;

    protected LinearLayout mTopRight; //竖屛右上角布局
    protected ImageButton mBtnDownload, mBtnCollect, mBtnShare;//竖屛右上角的子控件

    protected LinearLayout mTopRightLand;//横屏右上角布局
    protected ImageButton mBtnShareLand; //横屏分享
    protected Button mDefinitionLand;//清晰度

    protected ImageButton mBtnPlayNext; //播放下一集

    protected ImageView mThumb;//封面

    protected View mCenterContainer;//中间缓冲的布局
    protected ProgressBar mProgressBarLoading;//缓冲进度圈
    protected TextView mPlayMsg;//缓冲时候的信息

    protected boolean mLockCurScreen;//锁定屏幕点击
    protected boolean mNeedLockFull;//是否需要锁定屏幕

    protected Timer mDismissControlViewTimer;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;

    private StandardVideoAllCallBack mStandardVideoAllCallBack;
    protected LockClickListener mLockClickListener;//点击锁屏的回调

    //声音,亮度,进度控制
    protected Dialog mBrightnessDialog;//亮度布局
    protected Dialog mVolumeDialog;
    RelativeLayout player_overlay_info;
    FrameLayout mVerticalBar;
    View  mVerticalBarProgress;
    TextView tvInfo;
    ImageView imgvOoverlay;

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
        return R.layout.player_simple_video;
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        mTitle = (TextView) findViewById(R.id.title);

        mCenterContainer = findViewById(R.id.center_container);
        mProgressBarLoading = (ProgressBar) findViewById(R.id.pbLoading);
        mPlayMsg = (TextView) findViewById(R.id.tvPlayMsg);

        mRightContainer = (ViewGroup) findViewById(R.id.layout_right);
        mBtnDownloadLand = (Button) findViewById(R.id.btn_download_land);
        mBtnCollectLand = (Button) findViewById(R.id.btn_collect_land);
        mBtnEpisodeLand = (Button) findViewById(R.id.btn_episodes);
        mBtnEpisodeLand.setVisibility(GONE);
        mLineTop = findViewById(R.id.viewTop);
        mLineTop.setVisibility(GONE);

        mTopRight = (LinearLayout) findViewById(R.id.ll_top_right);
        mBtnCollect = (ImageButton) findViewById(R.id.btn_collect);
        mBtnDownload = (ImageButton) findViewById(R.id.btn_download);
        mBtnShare = (ImageButton) findViewById(R.id.btn_share);

        mTopRightLand = (LinearLayout) findViewById(R.id.ll_top_right_land);
        mDefinitionLand = (Button) findViewById(R.id.btn_land_definition);
        mBtnShareLand = (ImageButton) findViewById(R.id.btn_share_land);

        mBtnPlayNext = (ImageButton) findViewById(R.id.btnPlayNext);

        mThumb = (ImageView) findViewById(R.id.thumb);

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
                if (mLockClickListener != null) {
                    mLockClickListener.onClick(v, mLockCurScreen);
                }
            }
        });
        /**
         * 当前是横屏
         */
        if (isLandscape) {
            this.setIfCurrentIsFullscreen(true);
            hideSupportActionBar(getContext(), true, true);
            if (mHideKey) hideNavKey(getContext());
            mOrientationUtils.setEnable(false);
        }
    }
    /**
     * 初始化为正常状态
     */
    public void initUIState() {
        setStateAndUi(CURRENT_STATE_NORMAL);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    @Override
    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                changeUiToNormal();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PREPAREING:
                changeUiToPreparingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING:
                changeUiToPlayingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PAUSE:
                changeUiToPauseShow();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_ERROR:
                changeUiToErrorShow();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                Log.e("Complete:","true");
                changeUiToCompleteShow();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                changeUiToPlayingBufferingShow();
                break;
        }
    }


    //        percent = Math.min(Math.max(percent,0),100);
//        String strText = String.format(getContext().getString(R.string.player_format_buffer_msg),
//                percent)
//                .toString();
//        mPlayMsg.setText(strText);
//        if (percent >= 95) {
//            mCenterContainer.setVisibility(View.GONE);
//        } else {
//            mCenterContainer.setVisibility(View.VISIBLE);
//        }

    /**
     * 在开始缓冲状态下显示的UI
     */
    private void changeUiToPlayingBufferingShow() {
        changeUiToNormal();
        mLockScreen.setVisibility(GONE);
        mThumb.setVisibility(GONE);
        mCenterContainer.setVisibility(VISIBLE);
        mProgressBarLoading.setVisibility(VISIBLE);
        mPlayMsg.setText(R.string.player_loading_msg);

    }

    /**
     * 完成状态下显示的UI
     */
    private void changeUiToCompleteShow() {
        Log.e("changeUiToCompleteShow:","true");
        changeUiToNormal();
        mThumb.setVisibility(VISIBLE);
    }

    /**
     * 出错状态下显示的UI
     */
    private void changeUiToErrorShow() {
        changeUiToNormal();
        mThumb.setVisibility(INVISIBLE);
    }

    /**
     * 暂停状态下显示的UI
     */
    private void changeUiToPauseShow() {
        changeUiToNormal();
        mThumb.setVisibility(INVISIBLE);
    }

    /**
     * 播放状态下显示的UI
     */
    private void changeUiToPlayingShow() {
        changeUiToNormal();
        mThumb.setVisibility(INVISIBLE);
        mCenterContainer.setVisibility(INVISIBLE);
    }

    /**
     * 准备状态下显示的UI
     */
    private void changeUiToPreparingShow() {
        changeUiToNormal();
        mThumb.setVisibility(INVISIBLE);
        mCenterContainer.setVisibility(VISIBLE);
        mProgressBarLoading.setVisibility(VISIBLE);
        mPlayMsg.setText(R.string.player_url_link_msg);
    }

    /**
     * 正常状态下的UI显示状况
     */
    private void changeUiToNormal() {
        mTopContainer.setVisibility(VISIBLE);
        mBottomContainer.setVisibility(VISIBLE);
        mRightContainer.setVisibility(VISIBLE);
        mCenterContainer.setVisibility(INVISIBLE);
        mThumb.setVisibility(View.VISIBLE);
        showUIIsFullscreen();
        updateStartImage();
    }

    /**
     * 横竖屏显示UI
     */
    protected void showUIIsFullscreen() {

        if (mIfCurrentIsFullscreen && mNeedLockFull) {
            mTopRight.setVisibility(GONE);
            mTopRightLand.setVisibility(VISIBLE);
            mRightContainer.setVisibility(VISIBLE);
            mBtnPlayNext.setVisibility(VISIBLE);
            mLockScreen.setVisibility(VISIBLE);
            mFullscreenButton.setVisibility(GONE);

        } else {
            mTopRight.setVisibility(VISIBLE);
            mTopRightLand.setVisibility(GONE);
            mRightContainer.setVisibility(GONE);
            mBtnPlayNext.setVisibility(GONE);
            mLockScreen.setVisibility(GONE);
            mFullscreenButton.setVisibility(VISIBLE);
        }
        //在黑屏或者锁屏状态下启动，如果是锁屏的话，会回到继续锁屏的状态
        if (mLockCurScreen) hideAllWidget();
    }

    /**
     * 是否显示剧集控件
     *
     * @param isShow 是否显示剧集控件
     */
    public void isShowEpisode(boolean isShow) {
        mBtnEpisodeLand.setVisibility(GONE);
        if (isShow) mBtnEpisodeLand.setVisibility(VISIBLE);
    }

    protected void updateStartImage() {
        ImageButton imageButton = (ImageButton) mStartButton;
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            imageButton.setImageResource(R.drawable.play_pause_selector);
        } else {
            imageButton.setImageResource(R.drawable.play_normal_selector);
        }
    }

    @Override
    protected void onClickUiToggle() {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            mLockScreen.setVisibility(VISIBLE);
            return;
        }
        switch (mCurrentState) {
            case CURRENT_STATE_PREPAREING:
                if (mBottomContainer.getVisibility() == View.VISIBLE) {
                    changeUiToPreparingClear();
                } else {
                    changeUiToPreparingShow();
                }
                break;
            case CURRENT_STATE_PLAYING:
                if (mBottomContainer.getVisibility() == View.VISIBLE) {
                    changeUiToPlayingClear();
                } else {
                    changeUiToPlayingShow();
                }
                break;
            case CURRENT_STATE_PAUSE:
                if (mBottomContainer.getVisibility() == View.VISIBLE) {
                    changeUiToPauseClear();
                } else {
                    changeUiToPauseShow();
                }
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                if (mBottomContainer.getVisibility() == View.VISIBLE) {
                    changeUiToCompleteClear();
                } else {
                    changeUiToCompleteShow();
                }
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                if (mBottomContainer.getVisibility() == View.VISIBLE) {
                    changeUiToPlayingBufferingClear();
                } else {
                    changeUiToPlayingBufferingShow();
                }
                break;

        }
    }

    /**
     * 缓冲状态下隐藏控件
     */
    private void changeUiToPlayingBufferingClear() {
        changeUiToPreparingClear();
    }

    /**
     * 完成状态下隐藏控件
     */
    private void changeUiToCompleteClear() {
        changeUiToPreparingClear();
        mThumb.setVisibility(VISIBLE);
    }

    /**
     * 暂停状态下隐藏控件
     */
    private void changeUiToPauseClear() {
        changeUiToPreparingClear();
    }

    /**
     * 播放状态下隐藏控件
     */
    private void changeUiToPlayingClear() {
        changeUiToPreparingClear();
    }

    /**
     * 准备状态下隐藏控件
     */
    private void changeUiToPreparingClear() {
        mTopContainer.setVisibility(INVISIBLE);
        mBottomContainer.setVisibility(INVISIBLE);
        mRightContainer.setVisibility(INVISIBLE);
        mThumb.setVisibility(View.INVISIBLE);
        mLockScreen.setVisibility(GONE);
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
    public void startPlayLogic() {
        prepareVideo();
        startDismissControlViewTimer();
    }

    /**
     * 处理锁屏屏幕触摸逻辑
     */

    private void lockTouchLogic() {
        if (mLockCurScreen) {
            mLockScreen.setBackgroundResource(R.drawable.player_unlock_selector);
            mLockCurScreen = false;
            Log.e("isLandscape:", isLandscape + "");
            if (mOrientationUtils != null) mOrientationUtils.setEnable(mRotateViewAuto);
        } else {
            mLockScreen.setBackgroundResource(R.drawable.player_lock_selector);
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
        mTopContainer.setVisibility(INVISIBLE);
        mBottomContainer.setVisibility(INVISIBLE);
        mRightContainer.setVisibility(INVISIBLE);
        mLockScreen.setVisibility(INVISIBLE);


    }

    /**
     * 锁屏点击
     */
    public void setLockClickListener(LockClickListener lockClickListener) {
        this.mLockClickListener = lockClickListener;
    }

    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object... objects) {
        return super.setUp(url, cacheWithPlay, cachePath, mapHeadData, objects);
    }

    @Override
    public void onBackFullscreen() {
        clearFullscreenLayout();
    }

    @Override
    public void clearFullscreenLayout() {
        super.clearFullscreenLayout();
        showUIIsFullscreen();

    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        if (mLockCurScreen) {
            lockTouchLogic();
            mLockScreen.setVisibility(GONE);
        }
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
        if (mLockCurScreen) {
            lockTouchLogic();
            mLockScreen.setVisibility(GONE);
        }
    }

    @Override
    public void onVideoPause() {
        super.onVideoPause();
    }

    @Override
    public void onVideoResume() {
        super.onVideoResume();
    }

    /**
     * 开始全屏播放
     *
     * @param context
     * @param actionBar 是否有actionBar，有的话需要隐藏
     * @param statusBar 是否有状态bar，有的话需要隐藏
     * @return
     */
    @Override
    public BaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        BaseVideoPlayer baseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        if (baseVideoPlayer != null) {
            WasuVideoPlayer wasuVideoPlayer = (WasuVideoPlayer) baseVideoPlayer;
            wasuVideoPlayer.setStandardVideoAllCallBack(mStandardVideoAllCallBack);
            wasuVideoPlayer.setLockClickListener(mLockClickListener);
            wasuVideoPlayer.setNeedLockFull(mNeedLockFull);
            wasuVideoPlayer.showUIIsFullscreen();//横竖屏显示UI
        }
        return baseVideoPlayer;
    }
    @Override
    public BaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
        BaseVideoPlayer baseVideoPlayer = super.showSmallVideo(size, actionBar, statusBar);
        if (baseVideoPlayer != null) {
            WasuVideoPlayer gsyVideoPlayer = (WasuVideoPlayer) baseVideoPlayer;
            gsyVideoPlayer.setStandardVideoAllCallBack(mStandardVideoAllCallBack);
        }
        return baseVideoPlayer;
    }
    /**
     * 设置锁屏
     *
     * @param needLockFull 是否需要锁屏功能
     */
    public void setNeedLockFull(boolean needLockFull) {
        this.mNeedLockFull = needLockFull;
    }

    /**
     * 标题
     */
    public TextView getTitleTextView() {
        return mTitle;
    }


    @Override
    protected void showWifiDialog() {
        super.showWifiDialog();
        if (!NetworkUtils.isAvailable(getContext())) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_net), Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startPlayLogic();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void setStandardVideoAllCallBack(StandardVideoAllCallBack standardVideoAllCallBack) {
        this.mStandardVideoAllCallBack = standardVideoAllCallBack;
        setVideoAllCallBack(standardVideoAllCallBack);
    }

    /**
     *
     * @param deltaX
     * @param seekTime
     * @param seekTimePosition
     * @param totalTime
     * @param totalTimeDuration
     */
    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);

    }

    /**
     * 显示亮度
     *
     * @param percent 亮度百分比
     */
    @Override
    protected void showBrightnessDialog(float percent) {
        if (mBrightnessDialog == null){
            View localView = initBrightAndVolumeView(percent);
            tvInfo.setText("亮度");
            imgvOoverlay.setImageResource(R.drawable.player_overlay_brightness);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mVerticalBarProgress.getLayoutParams();
            float brightness = Math.min(Math.max(percent, 0.01f), 1f);
            brightness = Math.round(brightness * 100);
            layoutParams.weight = brightness;
            mVerticalBarProgress.setLayoutParams(layoutParams);
            mVerticalBar.setVisibility(View.VISIBLE);

            mBrightnessDialog = new Dialog(getContext(), R.style.video_style_dialog_progress);
            mBrightnessDialog.setContentView(localView);
            mBrightnessDialog.getWindow().addFlags(8);
            mBrightnessDialog.getWindow().addFlags(32);
            mBrightnessDialog.getWindow().addFlags(16);
            mBrightnessDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mBrightnessDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.TOP | Gravity.CENTER;
            localLayoutParams.width = getWidth();
            localLayoutParams.height = getHeight();
            int location[] = new int[2];
            getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            mBrightnessDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mBrightnessDialog.isShowing()) {
            mBrightnessDialog.show();
        }
    }

    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {
        if (mVolumeDialog == null){
            View localView = initBrightAndVolumeView(volumePercent);
            tvInfo.setText("音量");
            if (volumePercent == 0) {
                imgvOoverlay.setImageResource(R.drawable.player_overlay_mute);
            } else {
                imgvOoverlay.setImageResource(R.drawable.player_overlay_sound);
            }

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mVerticalBarProgress.getLayoutParams();
            layoutParams.weight = volumePercent;
            mVerticalBarProgress.setLayoutParams(layoutParams);
            mVerticalBar.setVisibility(View.VISIBLE);

            mVolumeDialog = new Dialog(getContext(), R.style.video_style_dialog_progress);
            mVolumeDialog.setContentView(localView);
            mVolumeDialog.getWindow().addFlags(8);
            mVolumeDialog.getWindow().addFlags(32);
            mVolumeDialog.getWindow().addFlags(16);
            mVolumeDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mVolumeDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.TOP | Gravity.CENTER;
            localLayoutParams.width = getWidth();
            localLayoutParams.height = getHeight();
            int location[] = new int[2];
            getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            mVolumeDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mVolumeDialog.isShowing()){
            mVolumeDialog.show();
        }
    }


    @Override
    protected void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog != null){
            mVolumeDialog.dismiss();
            mVolumeDialog =null;
        }
    }

    @Override
    protected void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        if (mBrightnessDialog != null) {
            mBrightnessDialog.dismiss();
            mBrightnessDialog = null;
        }
    }

    private View initBrightAndVolumeView(float percent){

        View localView = LayoutInflater.from(getContext()).inflate(R.layout.wasu_video_brightness,null);
        player_overlay_info = (RelativeLayout) localView.findViewById(R.id.player_overlay_info);
        mVerticalBar = (FrameLayout) localView.findViewById(R.id.verticalbar);
        tvInfo = (TextView) localView.findViewById(R.id.player_overlay_textinfo);
        imgvOoverlay = (ImageView) localView.findViewById(R.id.player_overlay_image);
        mVerticalBarProgress = localView.findViewById(R.id.verticalbar_progress);

        return localView;
    }


}
