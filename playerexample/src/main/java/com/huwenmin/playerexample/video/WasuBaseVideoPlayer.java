package com.huwenmin.playerexample.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huwenmin.playerexample.R;
import com.huwenmin.playerexample.listener.SmallVideoTouch;
import com.huwenmin.playerexample.listener.VideoAllCallBack;
import com.huwenmin.playerexample.listener.WasuMediaPlayerListener;
import com.huwenmin.playerexample.utils.CommonUtil;
import com.huwenmin.playerexample.utils.LogUtil;
import com.huwenmin.playerexample.utils.OrientationUtils;
import com.huwenmin.playerexample.widget.WasuTextureView;
import com.media.ffplay.ffplay;
import com.transitionseverywhere.TransitionManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static com.huwenmin.playerexample.utils.CommonUtil.getActionBarHeight;
import static com.huwenmin.playerexample.utils.CommonUtil.getStatusBarHeight;
import static com.huwenmin.playerexample.utils.CommonUtil.hideNavKey;
import static com.huwenmin.playerexample.utils.CommonUtil.hideSupportActionBar;
import static com.huwenmin.playerexample.utils.CommonUtil.showNavKey;
import static com.huwenmin.playerexample.utils.CommonUtil.showSupportActionBar;


/**
 * Created by shuyu on 2016/11/17.
 */

public abstract class WasuBaseVideoPlayer extends FrameLayout implements WasuMediaPlayerListener {

    private Context context;
    private WasuVideoManager wasuVideoManger;

    public static final int SMALL_ID = R.id.tiny_screen_id;

    protected static final int FULLSCREEN_ID = R.id.fullscreen_id;

    protected static long CLICK_QUIT_FULLSCREEN_TIME = 0;

    protected boolean mActionBar = false;//是否需要在利用window实现全屏幕的时候隐藏actionbar

    protected boolean mStatusBar = false;//是否需要在利用window实现全屏幕的时候隐藏statusbar

    protected boolean mHideKey = true;//是否隐藏虚拟按键

    protected boolean mCache = false;//是否播边边缓冲

    private boolean mShowFullAnimation = true;//是否使用全屏动画效果

    protected boolean mNeedShowWifiTip = true; //是否需要显示流量提示

    protected int[] mListItemRect;//当前item框的屏幕位置

    protected int[] mListItemSize;//当前item的大小

    protected int mCurrentState = -1; //当前的播放状态

    protected int mRotate = 0; //针对某些视频的旋转信息做了旋转处理

    protected int mShrinkImageRes = -1; //退出全屏显示的案件图片

    protected int mEnlargeImageRes = -1; //全屏显示的案件图片

    private int mSystemUiVisibility;

    protected float mSpeed = 1;//播放速度，只支持6.0以上

    protected boolean mRotateVieWasuto = true; //是否自动旋转

    protected boolean mIfCurrentIsFullscreen = false;//当前是否全屏

    protected boolean mLockLand = false;//当前全屏是否锁定全屏

    protected boolean mLooping = false;//循环

    protected boolean mHadPlay = false;//是否播放过

    protected boolean mCacheFile = false; //是否是缓存的文件

    protected Context mContext;

    protected String mOriginUrl; //原来的url

    protected String mUrl; //转化后的URL

    protected Object[] mObjects;

    protected File mCachePath;

    protected ViewGroup mTextureViewContainer; //渲染控件父类

    protected View mSmallClose; //小窗口关闭按键

    protected VideoAllCallBack mVideoAllCallBack;

    protected Map<String, String> mMapHeadData = new HashMap<>();

    protected WasuTextureView mTextureView;

    protected ImageView mCoverImageView; //内部使用，请勿操作哟~

    protected View mStartButton;

    protected SeekBar mProgressBar;

    protected ImageView mFullscreenButton;

    protected TextView mCurrentTimeTextView, mTotalTimeTextView;

    protected ViewGroup mTopContainer, mBottomContainer;

    protected ImageView mBackButton;

    protected Bitmap mFullPauseBitmap = null;//暂停时的全屏图片；

    protected OrientationUtils mOrientationUtils; //旋转工具类

    private Handler mHandler = new Handler();

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public WasuBaseVideoPlayer(Context context, Boolean fullFlag) {
        super(context);
        mIfCurrentIsFullscreen = fullFlag;
    }

    public WasuBaseVideoPlayer(Context context) {
        super(context);
        this.context = context;
       wasuVideoManger =  WasuVideoManager.getInstance(context);
    }

    public WasuBaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        wasuVideoManger =  WasuVideoManager.getInstance(context);
    }

    public WasuBaseVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        wasuVideoManger =  WasuVideoManager.getInstance(context);
    }


    private ViewGroup getViewGroup() {
        return (ViewGroup) (CommonUtil.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
    }

    /**
     * 移除没用的
     */
    private void removeVideo(ViewGroup vp, int id) {
        View old = vp.findViewById(id);
        if (old != null) {
            if (old.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) old.getParent();
                vp.removeView(viewGroup);
            }
        }
    }

    /**
     * 保存大小和状态
     */
    private void saveLocationStatus(Context context, boolean statusBar, boolean actionBar) {
        getLocationOnScreen(mListItemRect);
        int statusBarH = getStatusBarHeight(context);
        int actionBerH = getActionBarHeight((Activity) context);
        if (statusBar) {
            mListItemRect[1] = mListItemRect[1] - statusBarH;
        }
        if (actionBar) {
            mListItemRect[1] = mListItemRect[1] - actionBerH;
        }
        mListItemSize[0] = getWidth();
        mListItemSize[1] = getHeight();
    }

    /**
     * 全屏
     */
    private void resolveFullVideoShow(Context context, final WasuBaseVideoPlayer WasuVideoPlayer, final FrameLayout frameLayout) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) WasuVideoPlayer.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        WasuVideoPlayer.setLayoutParams(lp);
        WasuVideoPlayer.setIfCurrentIsFullscreen(true);
        mOrientationUtils = new OrientationUtils((Activity) context, WasuVideoPlayer);
        mOrientationUtils.setEnable(mRotateVieWasuto);
        WasuVideoPlayer.mOrientationUtils = mOrientationUtils;

        if (isShowFullAnimation()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLockLand && mOrientationUtils.getIsLand() != 1) {
                        mOrientationUtils.resolveByClick();
                    }
                    WasuVideoPlayer.setVisibility(VISIBLE);
                    frameLayout.setVisibility(VISIBLE);
                }
            }, 300);
        } else {
            if (mLockLand) {
                mOrientationUtils.resolveByClick();
            }
            WasuVideoPlayer.setVisibility(VISIBLE);
            frameLayout.setVisibility(VISIBLE);
        }


        if (mVideoAllCallBack != null) {
            LogUtil.e("onEnterFullscreen");
            mVideoAllCallBack.onEnterFullscreen(mUrl, mObjects);
        }
        mIfCurrentIsFullscreen = true;
    }

    /**
     * 恢复
     */
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, WasuVideoPlayer WasuVideoPlayer) {

        if (oldF != null && oldF.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) oldF.getParent();
            vp.removeView(viewGroup);
        }
        mCurrentState = wasuVideoManger.getLastState();
        if (WasuVideoPlayer != null) {
            mCurrentState = WasuVideoPlayer.getCurrentState();
        }
        wasuVideoManger.setListener(wasuVideoManger.lastListener());
        wasuVideoManger.setLastListener(null);
        setStateAndUi(mCurrentState);
        addTextureView();
        CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        if (mVideoAllCallBack != null) {
            LogUtil.e("onQuitFullscreen");
            mVideoAllCallBack.onQuitFullscreen(mUrl, mObjects);
        }
        mIfCurrentIsFullscreen = false;
        if (mHideKey) {
            showNavKey(mContext, mSystemUiVisibility);
        }
        showSupportActionBar(mContext, mActionBar, mStatusBar);
        getFullscreenButton().setImageResource(getEnlargeImageRes());
    }

    /**
     * 利用window层播放全屏效果
     *
     * @param context
     * @param actionBar 是否有actionBar，有的话需要隐藏
     * @param statusBar 是否有状态bar，有的话需要隐藏
     */
    public WasuBaseVideoPlayer startWindowFullscreen(final Context context, final boolean actionBar, final boolean statusBar) {

        mSystemUiVisibility = ((Activity) context).getWindow().getDecorView().getSystemUiVisibility();

        hideSupportActionBar(context, actionBar, statusBar);

        if (mHideKey) {
            hideNavKey(context);
        }

        this.mActionBar = actionBar;

        this.mStatusBar = statusBar;

        mListItemRect = new int[2];

        mListItemSize = new int[2];

        final ViewGroup vp = getViewGroup();

        removeVideo(vp, FULLSCREEN_ID);

        //处理暂停的逻辑
        pauseFullCoverLogic();

        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }


        saveLocationStatus(context, statusBar, actionBar);

        boolean hadNewConstructor = true;

        try {
            WasuBaseVideoPlayer.this.getClass().getConstructor(Context.class, Boolean.class);
        } catch (Exception e) {
            hadNewConstructor = false;
        }

        try {
            //通过被重载的不同构造器来选择
            Constructor<WasuBaseVideoPlayer> constructor;
            final WasuBaseVideoPlayer WasuVideoPlayer;
            if (!hadNewConstructor) {
                constructor = (Constructor<WasuBaseVideoPlayer>) WasuBaseVideoPlayer.this.getClass().getConstructor(Context.class);
                WasuVideoPlayer = constructor.newInstance(getContext());
            } else {
                constructor = (Constructor<WasuBaseVideoPlayer>) WasuBaseVideoPlayer.this.getClass().getConstructor(Context.class, Boolean.class);
                WasuVideoPlayer = constructor.newInstance(getContext(), true);
            }

            WasuVideoPlayer.setId(FULLSCREEN_ID);
            WasuVideoPlayer.setIfCurrentIsFullscreen(true);
            WasuVideoPlayer.setVideoAllCallBack(mVideoAllCallBack);
            WasuVideoPlayer.setLooping(isLooping());
            WasuVideoPlayer.setSpeed(getSpeed());
            final FrameLayout.LayoutParams lpParent = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setBackgroundColor(Color.BLACK);

            if (mShowFullAnimation) {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getWidth(), getHeight());
                lp.setMargins(mListItemRect[0], mListItemRect[1], 0, 0);
                frameLayout.addView(WasuVideoPlayer, lp);
                vp.addView(frameLayout, lpParent);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TransitionManager.beginDelayedTransition(vp);
                        resolveFullVideoShow(context, WasuVideoPlayer, frameLayout);
                    }
                }, 300);
            } else {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getWidth(), getHeight());
                frameLayout.addView(WasuVideoPlayer, lp);
                vp.addView(frameLayout, lpParent);
                WasuVideoPlayer.setVisibility(INVISIBLE);
                frameLayout.setVisibility(INVISIBLE);
                resolveFullVideoShow(context, WasuVideoPlayer, frameLayout);
            }
            WasuVideoPlayer.mHadPlay = mHadPlay;
            WasuVideoPlayer.mCacheFile = mCacheFile;
            WasuVideoPlayer.mFullPauseBitmap = mFullPauseBitmap;
            WasuVideoPlayer.mNeedShowWifiTip = mNeedShowWifiTip;
            WasuVideoPlayer.mShrinkImageRes = mShrinkImageRes;
            WasuVideoPlayer.mEnlargeImageRes = mEnlargeImageRes;
            WasuVideoPlayer.setUp(mOriginUrl, mCache, mCachePath, mMapHeadData, mObjects);
            WasuVideoPlayer.setStateAndUi(mCurrentState);
            WasuVideoPlayer.addTextureView();

            WasuVideoPlayer.getFullscreenButton().setImageResource(getShrinkImageRes());
            WasuVideoPlayer.getFullscreenButton().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearFullscreenLayout();
                }
            });

            WasuVideoPlayer.getBackButton().setVisibility(VISIBLE);
            WasuVideoPlayer.getBackButton().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearFullscreenLayout();
                }
            });

            wasuVideoManger.setLastListener(this);
            wasuVideoManger.setListener(WasuVideoPlayer);
            return WasuVideoPlayer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 退出window层播放全屏效果
     */
    public void clearFullscreenLayout() {
        mIfCurrentIsFullscreen = false;
        int delay = 0;
        if (mOrientationUtils != null) {
            delay = mOrientationUtils.backToProtVideo();
            mOrientationUtils.setEnable(false);
            if (mOrientationUtils != null) {
                mOrientationUtils.releaseListener();
                mOrientationUtils = null;
            }
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                backToNormal();
            }
        }, delay);

    }

    /**
     * 回到正常效果
     */
    private void backToNormal() {

        final ViewGroup vp = getViewGroup();

        final View oldF = vp.findViewById(FULLSCREEN_ID);
        final WasuVideoPlayer WasuVideoPlayer;
        if (oldF != null) {
            WasuVideoPlayer = (WasuVideoPlayer) oldF;
            //如果暂停了
            pauseFullBackCoverLogic(WasuVideoPlayer);
            if (mShowFullAnimation) {
                TransitionManager.beginDelayedTransition(vp);

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) WasuVideoPlayer.getLayoutParams();
                lp.setMargins(mListItemRect[0], mListItemRect[1], 0, 0);
                lp.width = mListItemSize[0];
                lp.height = mListItemSize[1];
                //注意配置回来，不然动画效果会不对
                lp.gravity = Gravity.NO_GRAVITY;
                WasuVideoPlayer.setLayoutParams(lp);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resolveNormalVideoShow(oldF, vp, WasuVideoPlayer);
                    }
                }, 400);
            } else {
                resolveNormalVideoShow(oldF, vp, WasuVideoPlayer);
            }

        } else {
            resolveNormalVideoShow(null, vp, null);
        }
    }

    /**
     * 全屏的暂停的时候返回页面不黑色
     */
    private void pauseFullCoverLogic() {
        if (mCurrentState == WasuVideoPlayer.CURRENT_STATE_PAUSE && mTextureView != null
                && (mFullPauseBitmap == null || mFullPauseBitmap.isRecycled())) {
            try {
                mFullPauseBitmap = mTextureView.getBitmap(mTextureView.getSizeW(), mTextureView.getSizeH());
            } catch (Exception e) {
                e.printStackTrace();
                mFullPauseBitmap = null;
            }
        }
    }

    /**
     * 全屏的暂停返回的时候返回页面不黑色
     */
    private void pauseFullBackCoverLogic(WasuBaseVideoPlayer wasuVideoPlayer) {
        //如果是暂停状态
        if (wasuVideoPlayer.mCurrentState == WasuVideoPlayer.CURRENT_STATE_PAUSE
                && wasuVideoPlayer.mTextureView != null) {
            //全屏的位图还在，说明没播放，直接用原来的
            if (wasuVideoPlayer.mFullPauseBitmap != null
                    && !wasuVideoPlayer.mFullPauseBitmap.isRecycled()) {
                mFullPauseBitmap = wasuVideoPlayer.mFullPauseBitmap;
            } else {
                //不在了说明已经播放过，还是暂停的话，我们拿回来就好
                try {
                    mFullPauseBitmap = mTextureView.getBitmap(mTextureView.getSizeW(), mTextureView.getSizeH());
                } catch (Exception e) {
                    e.printStackTrace();
                    mFullPauseBitmap = null;
                }
            }
        }
    }


    /**
     * 显示小窗口
     */
    public WasuBaseVideoPlayer showSmallVideo(Point size, final boolean actionBar, final boolean statusBar) {

        final ViewGroup vp = getViewGroup();

        removeVideo(vp, SMALL_ID);

        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        try {
            Constructor<WasuBaseVideoPlayer> constructor = (Constructor<WasuBaseVideoPlayer>) WasuBaseVideoPlayer.this.getClass().getConstructor(Context.class);
            WasuBaseVideoPlayer WasuVideoPlayer = constructor.newInstance(getContext());
            WasuVideoPlayer.setId(SMALL_ID);

            FrameLayout.LayoutParams lpParent = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            FrameLayout frameLayout = new FrameLayout(mContext);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size.x, size.y);
            int marginLeft = CommonUtil.getScreenWidth(mContext) - size.x;
            int marginTop = CommonUtil.getScreenHeight(mContext) - size.y;

            if (actionBar) {
                marginTop = marginTop - getActionBarHeight((Activity) mContext);
            }

            if (statusBar) {
                marginTop = marginTop - getStatusBarHeight(mContext);
            }

            lp.setMargins(marginLeft, marginTop, 0, 0);
            frameLayout.addView(WasuVideoPlayer, lp);

            vp.addView(frameLayout, lpParent);
            WasuVideoPlayer.mHadPlay = mHadPlay;
            WasuVideoPlayer.setUp(mOriginUrl, mCache, mCachePath, mMapHeadData, mObjects);
            WasuVideoPlayer.setStateAndUi(mCurrentState);
            WasuVideoPlayer.addTextureView();
            //隐藏掉所有的弹出状态哟
            WasuVideoPlayer.onClickUiToggle();
            WasuVideoPlayer.setVideoAllCallBack(mVideoAllCallBack);
            WasuVideoPlayer.setLooping(isLooping());
            WasuVideoPlayer.setSpeed(getSpeed());
            WasuVideoPlayer.setSmallVideoTextureView(new SmallVideoTouch(WasuVideoPlayer, marginLeft, marginTop));

            wasuVideoManger.setLastListener(this);
            wasuVideoManger.setListener(WasuVideoPlayer);

            if (mVideoAllCallBack != null) {
                LogUtil.e("onEnterSmallWidget");
                mVideoAllCallBack.onEnterSmallWidget(mUrl, mObjects);
            }

            return WasuVideoPlayer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 隐藏小窗口
     */
    public void hideSmallVideo() {
        final ViewGroup vp = getViewGroup();
        WasuVideoPlayer WasuVideoPlayer = (WasuVideoPlayer) vp.findViewById(SMALL_ID);
        removeVideo(vp, SMALL_ID);
        mCurrentState = wasuVideoManger.getLastState();
        if (WasuVideoPlayer != null) {
            mCurrentState = WasuVideoPlayer.getCurrentState();
        }
        wasuVideoManger.setListener(wasuVideoManger.lastListener());
        wasuVideoManger.setLastListener(null);
        setStateAndUi(mCurrentState);
        addTextureView();
        CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        if (mVideoAllCallBack != null) {
            LogUtil.e("onQuitSmallWidget");
            mVideoAllCallBack.onQuitSmallWidget(mUrl, mObjects);
        }
    }


    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects
     * @return
     */
    public abstract boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object... objects);

    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param mapHeadData
     * @param objects
     * @return
     */

    public abstract boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object... objects);

    /**
     * 设置播放显示状态
     *
     * @param state
     */
    protected abstract void setStateAndUi(int state);

    /**
     * 添加播放的view
     */
    protected abstract void addTextureView();

    /**
     * 小窗口
     **/
    protected abstract void setSmallVideoTextureView(View.OnTouchListener onTouchListener);


    protected abstract void onClickUiToggle();

    /**
     * 获取全屏按键
     */
    public abstract ImageView getFullscreenButton();

    /**
     * 获取返回按键
     */
    public abstract ImageView getBackButton();


    public boolean isIfCurrentIsFullscreen() {
        return mIfCurrentIsFullscreen;
    }

    public void setIfCurrentIsFullscreen(boolean ifCurrentIsFullscreen) {
        this.mIfCurrentIsFullscreen = ifCurrentIsFullscreen;
    }


    public boolean isShowFullAnimation() {
        return mShowFullAnimation;
    }

    /**
     * 全屏动画
     *
     * @param showFullAnimation 是否使用全屏动画效果
     */
    public void setShowFullAnimation(boolean showFullAnimation) {
        this.mShowFullAnimation = showFullAnimation;
    }


    public boolean isLooping() {
        return mLooping;
    }

    /**
     * 设置循环
     */
    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }


    /**
     * 设置播放过程中的回调
     *
     * @param mVideoAllCallBack
     */
    public void setVideoAllCallBack(VideoAllCallBack mVideoAllCallBack) {
        this.mVideoAllCallBack = mVideoAllCallBack;
    }


    public boolean isRotateVieWasuto() {
        return mRotateVieWasuto;
    }

    /**
     * 是否开启自动旋转
     */
    public void setRotateVieWasuto(boolean rotateVieWasuto) {
        this.mRotateVieWasuto = rotateVieWasuto;
        if (mOrientationUtils != null) {
            mOrientationUtils.setEnable(rotateVieWasuto);
        }
    }

    public boolean isLockLand() {
        return mLockLand;
    }

    /**
     * 一全屏就锁屏横屏，默认false竖屏，可配合setRotateVieWasuto使用
     */
    public void setLockLand(boolean lockLand) {
        this.mLockLand = lockLand;
    }


    public float getSpeed() {
        return mSpeed;
    }

    /**
     * 播放速度
     */
    public void setSpeed(float speed) {
        this.mSpeed = speed;
        if (wasuVideoManger.getMediaPlayer() != null
                && wasuVideoManger.getMediaPlayer() instanceof ffplay) {
            if (speed != 1 && speed > 0) {
                ((ffplay) wasuVideoManger.getMediaPlayer()).setSpeed(speed);
            }
        }
    }

    public boolean isHideKey() {
        return mHideKey;
    }

    /**
     * 全屏隐藏虚拟按键，默认打开
     */
    public void setHideKey(boolean hideKey) {
        this.mHideKey = hideKey;
    }

    public boolean isNeedShowWifiTip() {
        return mNeedShowWifiTip;
    }

    /**
     * 是否需要显示流量提示,默认true
     */
    public void setNeedShowWifiTip(boolean needShowWifiTip) {
        this.mNeedShowWifiTip = needShowWifiTip;
    }

    public int getEnlargeImageRes() {
        if (mShrinkImageRes == -1) {
            return R.drawable.player_btn_full_selector;
        }
        return mEnlargeImageRes;
    }

    /**
     * 设置右下角 显示切换到全屏 的按键资源
     * 必须在setUp之前设置
     * 不设置使用默认
     */
    public void setEnlargeImageRes(int mEnlargeImageRes) {
        this.mEnlargeImageRes = mEnlargeImageRes;
    }

    public int getShrinkImageRes() {
        if (mShrinkImageRes == -1) {
            return R.drawable.video_shrink;
        }
        return mShrinkImageRes;
    }

    /**
     * 设置右下角 显示退出全屏 的按键资源
     * 必须在setUp之前设置
     * 不设置使用默认
     */
    public void setShrinkImageRes(int mShrinkImageRes) {
        this.mShrinkImageRes = mShrinkImageRes;
    }
}
