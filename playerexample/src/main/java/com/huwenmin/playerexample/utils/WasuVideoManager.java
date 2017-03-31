package com.huwenmin.playerexample.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;

import com.huwenmin.playerexample.listener.WasuMediaPlayerListener;
import com.huwenmin.playerexample.model.WasuVideoModel;
import com.huwenmin.playerexample.model.WasuVideoType;
import com.media.AbsMediaPlayer;
import com.media.IMediaPlayer;
import com.media.ffplay.ffplay;
import com.wasu.util.StringUtils;

import java.lang.ref.WeakReference;

/**
 * 作者：Administrator on 2017/3/31 09:45
 * <p>
 * 功能：媒体管理播放器
 */

public class WasuVideoManager implements IMediaPlayer.OnPreparedListener,IMediaPlayer.OnBufferingUpdateListener,IMediaPlayer.OnCompletionListener
,IMediaPlayer.OnErrorListener,IMediaPlayer.OnInfoListener,IMediaPlayer.OnSeekCompleteListener,IMediaPlayer.OnVideoSizeChangedListener{
    private final static String TAG = WasuVideoManager.class.getName();
    private volatile static WasuVideoManager instance;

    private AbsMediaPlayer mediaPlayer;
    private WeakReference<WasuMediaPlayerListener> listener;
    private Context context;

    private int videoType = WasuVideoType.IJKPLAYER;
    private int currentVideoWidth = 0; //当前播放的视频宽的高
    private int currentVideoHeight = 0; //当前播放的视屏的高

    private boolean needMute = false; //是否需要静音
    private boolean needTimeOutOther; //是否需要外部超时判断
    private int buffterPoint;//缓存点

    private HandlerThread mMediaHandlerThread;
    private MediaHandler mMediaHandler;
    private Handler mainThreadHandler;
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_SETDISPLAY = 1;
    public static final int HANDLER_RELEASE = 2;

    public synchronized static WasuVideoManager getInstance(Context context) {
        if (instance == null){
            instance = new WasuVideoManager(context);
        }
        return instance;
    }

    private WasuVideoManager(Context context) {
        this.context = context;
        mediaPlayer = new ffplay(context);
        mMediaHandlerThread = new HandlerThread(TAG);
        mMediaHandlerThread.start();
        mMediaHandler = new MediaHandler((mMediaHandlerThread.getLooper()));
        mainThreadHandler = new Handler();
    }


    public WasuMediaPlayerListener listener() {
        if (listener == null)
            return null;
        return listener.get();
    }

    public void setListener(WasuMediaPlayerListener listener) {
        if (listener == null)
            this.listener = null;
        else
            this.listener = new WeakReference<>(listener);
    }
    public int getCurrentVideoWidth() {
        return currentVideoWidth;
    }

    public int getCurrentVideoHeight() {
        return currentVideoHeight;
    }

    public AbsMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        if (listener() != null) listener().onBufferingUpdate(i);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (listener() != null) listener().onCompletion();
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {

        if (listener() != null) listener().onError(i,i1);
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        if (listener() != null) {
            listener().onInfo(i, i1);
        }
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (listener() != null) listener().onPrepared();
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        if (listener() != null) listener().onSeekComplete();
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1) {
        this.currentVideoWidth = iMediaPlayer.getVideoWidth();
        this.currentVideoHeight = iMediaPlayer.getVideoHeight();
        if (listener() != null) listener().onVideoSizeChanged();
    }

    private void initVideo(Message msg) {
        try {
            currentVideoHeight = 0;
            currentVideoHeight = 0;
            if (mediaPlayer != null) mediaPlayer.release();
            mediaPlayer = new ffplay(context);
            ((ffplay) mediaPlayer).setOption(1, "user-agent", "wasutv_player1.1");
            setNeedMute(needMute);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);

            mediaPlayer.setDataSource(context, Uri.parse(((WasuVideoModel) msg.obj).getUrl()));
            mediaPlayer.setLooping(((WasuVideoModel) msg.obj).isLooping());
            if (((WasuVideoModel) msg.obj).getSpeed() != 1 && ((WasuVideoModel) msg.obj).getSpeed() > 0) {
                ((ffplay) mediaPlayer).setSpeed(((WasuVideoModel) msg.obj).getSpeed());
            }
            mediaPlayer.prepareAsync();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 暂停播放
     */
    public  void onPause() {
        if (listener() != null) {
            listener().onVideoPause();
        }
    }
    /**
     * 恢复播放
     */
    public void onResume(){
        if (listener() != null){
            listener().onVideoResume();
        }
    }
    /**
     * 是否需要静音
     */
    public void setNeedMute(boolean needMute) {
        this.needMute = needMute;
        if (mediaPlayer != null) {
            if (needMute) {
                mediaPlayer.setVolume(0, 0);
            } else {
                mediaPlayer.setVolume(1, 1);
            }
        }
    }
    /**
     * 提供给外部的准备接口
     */
    public void prepare(String url,boolean loop,float speed){
        if (StringUtils.isEmpty(url)) return;
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        WasuVideoModel model = new WasuVideoModel(url, loop, speed);
        msg.obj = model;
        mMediaHandler.sendMessage(msg);

    }

    /**
     * 提供给外部的释放播放器的接口
     */
    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
    }

    public void setDisplay(Surface holder) {
        Message msg = new Message();
        msg.what = HANDLER_SETDISPLAY;
        msg.obj = holder;
        mMediaHandler.sendMessage(msg);
    }
   private void showDisplay(Message msg){
       if (msg.obj == null && mediaPlayer != null){
           mediaPlayer.setSurface(null);
       }else {
           Surface holder = (Surface) msg.obj;
           if (mediaPlayer != null && holder.isValid()){
               mediaPlayer.setSurface(holder);
           }
       }
   }


    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    initVideo(msg);
                    break;
                case HANDLER_SETDISPLAY:
                    showDisplay(msg);
                    break;
                case HANDLER_RELEASE:
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    setNeedMute(false);
                    buffterPoint = 0;
                    break;
            }
        }

    }
}
