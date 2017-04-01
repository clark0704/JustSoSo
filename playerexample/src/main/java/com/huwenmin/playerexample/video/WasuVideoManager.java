package com.huwenmin.playerexample.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.huwenmin.playerexample.listener.WasuMediaPlayerListener;
import com.huwenmin.playerexample.model.WasuVideoModel;
import com.huwenmin.playerexample.model.WasuVideoType;
import com.huwenmin.playerexample.utils.CommonUtil;
import com.huwenmin.playerexample.utils.FileUtils;
import com.huwenmin.playerexample.utils.LogUtil;
import com.huwenmin.playerexample.utils.StorageUtils;
import com.media.AbsMediaPlayer;
import com.media.IMediaPlayer;
import com.media.ffplay.ffplay;
import com.wasu.util.StringUtils;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 作者：Administrator on 2017/3/31 09:45
 * <p>
 * 功能：媒体管理播放器
 */

public class WasuVideoManager implements IMediaPlayer.OnPreparedListener,IMediaPlayer.OnBufferingUpdateListener,IMediaPlayer.OnCompletionListener
,IMediaPlayer.OnErrorListener,IMediaPlayer.OnInfoListener,IMediaPlayer.OnSeekCompleteListener,IMediaPlayer.OnVideoSizeChangedListener,CacheListener{
    private final static String TAG = WasuVideoManager.class.getName();
    private volatile static WasuVideoManager instance;

    private AbsMediaPlayer mediaPlayer;
    private WeakReference<WasuMediaPlayerListener> listener;
    private WeakReference<WasuMediaPlayerListener> lastListener;
    private Context context;

    private int videoType = WasuVideoType.IJKPLAYER;
    private int currentVideoWidth = 0; //当前播放的视频宽的高
    private int currentVideoHeight = 0; //当前播放的视屏的高

    private boolean needMute = false; //是否需要静音

    private int lastState;//当前视频的最后状态

    private HandlerThread mMediaHandlerThread;
    private MediaHandler mMediaHandler;
    private Handler mainThreadHandler;
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_SETDISPLAY = 1;
    public static final int HANDLER_RELEASE = 2;

    private HttpProxyCacheServer proxy; //视频代理

    private File cacheFile;

    private String playTag = ""; //播放的tag，防止错位置，因为普通的url也可能重复
    private int playPosition = -22; //播放的tag，防止错位置，因为普通的url也可能重复

    private int buffterPoint; //缓存点

    private int timeOut = 8 * 1000;

    private boolean needTimeOutOther; //是否需要外部超时判断

    public static final int BUFFER_TIME_OUT_ERROR = -192;//外部超时错误码

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
    public WasuMediaPlayerListener lastListener() {
        if (lastListener == null)
            return null;
        return lastListener.get();
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

    public int getLastState() {
        return lastState;
    }

    public void setLastListener(WasuMediaPlayerListener lastListener) {
        if (lastListener == null)
            this.lastListener = null;
        else
            this.lastListener = new WeakReference<>(lastListener);
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
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, final int percent) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    if (percent > buffterPoint) {
                        listener().onBufferingUpdate(percent);
                    } else {
                        listener().onBufferingUpdate(buffterPoint);
                    }
                }
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (needTimeOutOther) {
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        startTimeOutBuffer();
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        cancelTimeOutBuffer();
                    }
                }
                if (listener != null) {
                    listener().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onPrepared();
                }
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onSeekComplete();
                }
            }
        });
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1) {
        this.currentVideoWidth = iMediaPlayer.getVideoWidth();
        this.currentVideoHeight = iMediaPlayer.getVideoHeight();
       mainThreadHandler.post(new Runnable() {
           @Override
           public void run() {
               if (listener() != null) listener().onVideoSizeChanged();
           }
       }) ;
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

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        buffterPoint = percentsAvailable;
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
    /**
     * 获取缓存代理服务,带文件目录的
     */
    public static HttpProxyCacheServer getProxy(Context context, File file) {

        //如果为空，返回默认的
        if (file == null) {
            return getProxy(context);
        }

        //如果已经有缓存文件路径，那么判断缓存文件路径是否一致
        if (getInstance(context).cacheFile != null
                && !getInstance(context).cacheFile.getAbsolutePath().equals(file.getAbsolutePath())) {
            //不一致先关了旧的
            HttpProxyCacheServer proxy = getInstance(context).proxy;

            if (proxy != null) {
                proxy.shutdown();
            }
            //开启新的
            return (getInstance(context).proxy =
                    getInstance(context).newProxy(context, file));
        } else {
            //还没有缓存文件的或者一致的，返回原来
            HttpProxyCacheServer proxy = getInstance(context).proxy;

            return proxy == null ? (getInstance(context).proxy =
                    getInstance(context).newProxy(context, file)) : proxy;
        }
    }
    /**
     * 创建缓存代理服务,带文件目录的.
     */
    private HttpProxyCacheServer newProxy(Context context, File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        HttpProxyCacheServer.Builder builder = new HttpProxyCacheServer.Builder(context);
        builder.cacheDirectory(file);
        cacheFile = file;
        return builder.build();
    }
    /**
     * 创建缓存代理服务
     */
    private HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer(context.getApplicationContext());
    }
    /**
     * 获取缓存代理服务
     */
    public static HttpProxyCacheServer getProxy(Context context) {
        HttpProxyCacheServer proxy = getInstance(context).proxy;
        return proxy == null ? (getInstance(context).proxy =
                getInstance(context).newProxy(context)) : proxy;
    }

    /**
     * 删除默认所有缓存文件
     */
    public static void clearAllDefaultCache(Context context) {
        String path = StorageUtils.getIndividualCacheDirectory
                (context.getApplicationContext()).getAbsolutePath();
        FileUtils.deleteFiles(new File(path));
    }

    /**
     * 删除url对应默认缓存文件
     */
    public static void clearDefaultCache(Context context, String url) {
        Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
        String name = md5FileNameGenerator.generate(url);
        String pathTmp = StorageUtils.getIndividualCacheDirectory
                (context.getApplicationContext()).getAbsolutePath()
                + File.separator + name + ".download";
        String path = StorageUtils.getIndividualCacheDirectory
                (context.getApplicationContext()).getAbsolutePath()
                + File.separator + name;
        CommonUtil.deleteFile(pathTmp);
        CommonUtil.deleteFile(path);

    }
    /**
     * 启动十秒的定时器进行 缓存操作
     */
    private void startTimeOutBuffer() {
        // 启动定时
        LogUtil.e("startTimeOutBuffer");
        mainThreadHandler.postDelayed(mTimeOutRunnable, timeOut);

    }

    /**
     * 取消 十秒的定时器进行 缓存操作
     */
    private void cancelTimeOutBuffer() {
        LogUtil.e("cancelTimeOutBuffer");
        // 取消定时
        if (needTimeOutOther)
            mainThreadHandler.removeCallbacks(mTimeOutRunnable);
    }


    private Runnable mTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null) {
                LogUtil.e("time out for error listener");
                listener().onError(BUFFER_TIME_OUT_ERROR, BUFFER_TIME_OUT_ERROR);
            }
        }
    };

    public String getPlayTag() {
        return playTag;
    }

    public void setPlayTag(String playTag) {
        this.playTag = playTag;
    }

    public int getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }
    public void setCurrentVideoHeight(int currentVideoHeight) {
        this.currentVideoHeight = currentVideoHeight;
    }

    public void setCurrentVideoWidth(int currentVideoWidth) {
        this.currentVideoWidth = currentVideoWidth;
    }
    /**
     * 是否需要在buffer缓冲时，增加外部超时判断
     *
     * 超时后会走onError接口，播放器通过onPlayError回调出
     *
     * 错误码为 ： BUFFER_TIME_OUT_ERROR = -192
     *
     * 由于onError之后执行GSYVideoPlayer的OnError，如果不想触发错误，
     * 可以重载onError，在super之前拦截处理。
     *
     * public void onError(int what, int extra){
     *     do you want before super and return;
     *     super.onError(what, extra)
     * }
     *
     * @param timeOut          超时时间，毫秒 默认8000
     * @param needTimeOutOther 是否需要延时设置，默认关闭
     */
    public void setTimeOut(int timeOut, boolean needTimeOutOther) {
        this.timeOut = timeOut;
        this.needTimeOutOther = needTimeOutOther;
    }
}
