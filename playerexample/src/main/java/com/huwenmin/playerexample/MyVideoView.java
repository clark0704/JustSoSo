package com.huwenmin.playerexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.media.IMediaPlayer;
import com.media.IVideoView;
import com.media.exoplayer.WasuExoPlayer;
import com.media.ffplay.ffplay;
import com.media.mtkplayer.MTKPlayer;
import com.media.systemplayer.SystemPlayer;

import java.io.IOException;
import java.util.Map;

/**
 * 作者：Administrator on 2017/3/29 16:07
 * <p>
 * 功能：
 */

public class MyVideoView extends SurfaceView implements IVideoView {
    private String TAG = "VideoView";
    private Uri mUri;
    private Map<String, String> mHeaders;
    private PreferPlayer mPreferPlayer;
    private ScaleType mScaleType;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private int mCurrentState;
    private int mTargetState;
    private SurfaceHolder mSurfaceHolder;
    private IMediaPlayer mMediaPlayer;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private int mCurrentBufferPercentage;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private int mSeekWhenPrepared;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    IMediaPlayer.OnPreparedListener mPreparedListener;
    private IMediaPlayer.OnSeekCompleteListener mSeekListener;
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    private IMediaPlayer.OnInfoListener mInfoListener;
    private IMediaPlayer.OnErrorListener mErrorListener;
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    SurfaceHolder.Callback mSHCallback;

    public MyVideoView(Context var1) {
        super(var1);
        this.mPreferPlayer = PreferPlayer.DEFAULT;
        this.mScaleType = ScaleType.fitXY;
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer var1, int var2, int var3) {
                MyVideoView.this.mVideoWidth = var1.getVideoWidth();
                MyVideoView.this.mVideoHeight = var1.getVideoHeight();
                if(MyVideoView.this.mVideoWidth != 0 && MyVideoView.this.mVideoHeight != 0) {
                    MyVideoView.this.getHolder().setFixedSize(MyVideoView.this.mVideoWidth, MyVideoView.this.mVideoHeight);
                    MyVideoView.this.requestLayout();
                }

            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer var1) {
                MyVideoView.this.mCurrentState = 2;
                MyVideoView.this.mCanPause = MyVideoView.this.mCanSeekBack = MyVideoView.this.mCanSeekForward = true;
                if(MyVideoView.this.mOnPreparedListener != null) {
                    MyVideoView.this.mOnPreparedListener.onPrepared(MyVideoView.this.mMediaPlayer);
                }

                MyVideoView.this.mVideoWidth = var1.getVideoWidth();
                MyVideoView.this.mVideoHeight = var1.getVideoHeight();
                int var2 = MyVideoView.this.mSeekWhenPrepared;
                if(var2 != 0) {
                    MyVideoView.this.seekTo(var2);
                }

                if(MyVideoView.this.mVideoWidth != 0 && MyVideoView.this.mVideoHeight != 0) {
                    MyVideoView.this.getHolder().setFixedSize(MyVideoView.this.mVideoWidth, MyVideoView.this.mVideoHeight);
                    if(MyVideoView.this.mSurfaceWidth == MyVideoView.this.mVideoWidth && MyVideoView.this.mSurfaceHeight == MyVideoView.this.mVideoHeight) {
                        if(MyVideoView.this.mTargetState == 3) {
                            MyVideoView.this.start();
                        } else if(!MyVideoView.this.isPlaying() && var2 == 0 && MyVideoView.this.getCurrentPosition() > 0) {
                            ;
                        }
                    }
                } else if(MyVideoView.this.mTargetState == 3) {
                    MyVideoView.this.start();
                }

            }
        };
        this.mSeekListener = new IMediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(IMediaPlayer var1) {
                if(MyVideoView.this.mOnSeekCompleteListener != null) {
                    MyVideoView.this.mOnSeekCompleteListener.onSeekComplete(var1);
                }

            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer var1) {
                MyVideoView.this.mCurrentState = 5;
                MyVideoView.this.mTargetState = 5;
                if(MyVideoView.this.mOnCompletionListener != null) {
                    MyVideoView.this.mOnCompletionListener.onCompletion(MyVideoView.this.mMediaPlayer);
                }

            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer var1, int var2, int var3) {
                if(MyVideoView.this.mOnInfoListener != null) {
                    MyVideoView.this.mOnInfoListener.onInfo(var1, var2, var3);
                }

                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer var1, int var2, int var3) {
                Log.d(MyVideoView.this.TAG, "Error: " + var2 + "," + var3);
                MyVideoView.this.mCurrentState = -1;
                MyVideoView.this.mTargetState = -1;
                return MyVideoView.this.mOnErrorListener != null && MyVideoView.this.mOnErrorListener.onError(MyVideoView.this.mMediaPlayer, var2, var3)?true:true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer var1, int var2) {
                MyVideoView.this.mCurrentBufferPercentage = var2;
            }
        };
        this.mSHCallback = new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
                Log.d(MyVideoView.this.TAG, "surfaceChanged:" + MyVideoView.this);
                MyVideoView.this.mSurfaceWidth = var3;
                MyVideoView.this.mSurfaceHeight = var4;
                boolean var5 = MyVideoView.this.mTargetState == 3;
                boolean var6 = MyVideoView.this.mVideoWidth == var3 && MyVideoView.this.mVideoHeight == var4;
                if(MyVideoView.this.mMediaPlayer != null && var5 && var6) {
                    if(MyVideoView.this.mSeekWhenPrepared != 0) {
                        MyVideoView.this.seekTo(MyVideoView.this.mSeekWhenPrepared);
                    }

                    MyVideoView.this.start();
                }

            }

            public void surfaceCreated(SurfaceHolder var1) {
                Log.d(MyVideoView.this.TAG, "surfaceCreated:" + MyVideoView.this);
                MyVideoView.this.mSurfaceHolder = var1;
                MyVideoView.this.openVideo();
            }

            public void surfaceDestroyed(SurfaceHolder var1) {
                Log.d(MyVideoView.this.TAG, "surfaceDestroyed: " + MyVideoView.this);
                MyVideoView.this.mSurfaceHolder = null;
                if(MyVideoView.this.mMediaPlayer != null && (MyVideoView.this.mTargetState == 3 || MyVideoView.this.mTargetState == 4)) {
                    MyVideoView.this.mSeekWhenPrepared = (int)MyVideoView.this.mMediaPlayer.getCurrentPosition();
                }

                MyVideoView.this.release(false);
            }
        };
        this.initVideoView();
    }

    public MyVideoView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.mPreferPlayer = PreferPlayer.DEFAULT;
        this.mScaleType = ScaleType.fitXY;
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer var1, int var2, int var3) {
                MyVideoView.this.mVideoWidth = var1.getVideoWidth();
                MyVideoView.this.mVideoHeight = var1.getVideoHeight();
                if(MyVideoView.this.mVideoWidth != 0 && MyVideoView.this.mVideoHeight != 0) {
                    MyVideoView.this.getHolder().setFixedSize(MyVideoView.this.mVideoWidth, MyVideoView.this.mVideoHeight);
                    MyVideoView.this.requestLayout();
                }

            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer var1) {
                MyVideoView.this.mCurrentState = 2;
                MyVideoView.this.mCanPause = MyVideoView.this.mCanSeekBack = MyVideoView.this.mCanSeekForward = true;
                if(MyVideoView.this.mOnPreparedListener != null) {
                    MyVideoView.this.mOnPreparedListener.onPrepared(MyVideoView.this.mMediaPlayer);
                }

                MyVideoView.this.mVideoWidth = var1.getVideoWidth();
                MyVideoView.this.mVideoHeight = var1.getVideoHeight();
                int var2 = MyVideoView.this.mSeekWhenPrepared;
                if(var2 != 0) {
                    MyVideoView.this.seekTo(var2);
                }

                if(MyVideoView.this.mVideoWidth != 0 && MyVideoView.this.mVideoHeight != 0) {
                    MyVideoView.this.getHolder().setFixedSize(MyVideoView.this.mVideoWidth, MyVideoView.this.mVideoHeight);
                    if(MyVideoView.this.mSurfaceWidth == MyVideoView.this.mVideoWidth && MyVideoView.this.mSurfaceHeight == MyVideoView.this.mVideoHeight) {
                        if(MyVideoView.this.mTargetState == 3) {
                            MyVideoView.this.start();
                        } else if(!MyVideoView.this.isPlaying() && var2 == 0 && MyVideoView.this.getCurrentPosition() > 0) {
                            ;
                        }
                    }
                } else if(MyVideoView.this.mTargetState == 3) {
                    MyVideoView.this.start();
                }

            }
        };
        this.mSeekListener = new IMediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(IMediaPlayer var1) {
                if(MyVideoView.this.mOnSeekCompleteListener != null) {
                    MyVideoView.this.mOnSeekCompleteListener.onSeekComplete(var1);
                }

            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer var1) {
                MyVideoView.this.mCurrentState = 5;
                MyVideoView.this.mTargetState = 5;
                if(MyVideoView.this.mOnCompletionListener != null) {
                    MyVideoView.this.mOnCompletionListener.onCompletion(MyVideoView.this.mMediaPlayer);
                }

            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer var1, int var2, int var3) {
                if(MyVideoView.this.mOnInfoListener != null) {
                    MyVideoView.this.mOnInfoListener.onInfo(var1, var2, var3);
                }

                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer var1, int var2, int var3) {
                Log.d(MyVideoView.this.TAG, "Error: " + var2 + "," + var3);
                MyVideoView.this.mCurrentState = -1;
                MyVideoView.this.mTargetState = -1;
                return MyVideoView.this.mOnErrorListener != null && MyVideoView.this.mOnErrorListener.onError(MyVideoView.this.mMediaPlayer, var2, var3)?true:true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer var1, int var2) {
                MyVideoView.this.mCurrentBufferPercentage = var2;
            }
        };
        this.mSHCallback = new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
                Log.d(MyVideoView.this.TAG, "surfaceChanged:" + MyVideoView.this);
                MyVideoView.this.mSurfaceWidth = var3;
                MyVideoView.this.mSurfaceHeight = var4;
                boolean var5 = MyVideoView.this.mTargetState == 3;
                boolean var6 = MyVideoView.this.mVideoWidth == var3 && MyVideoView.this.mVideoHeight == var4;
                if(MyVideoView.this.mMediaPlayer != null && var5 && var6) {
                    if(MyVideoView.this.mSeekWhenPrepared != 0) {
                        MyVideoView.this.seekTo(MyVideoView.this.mSeekWhenPrepared);
                    }

                    MyVideoView.this.start();
                }

            }

            public void surfaceCreated(SurfaceHolder var1) {
                Log.d(MyVideoView.this.TAG, "surfaceCreated:" + MyVideoView.this);
                MyVideoView.this.mSurfaceHolder = var1;
                MyVideoView.this.openVideo();
            }

            public void surfaceDestroyed(SurfaceHolder var1) {
                Log.d(MyVideoView.this.TAG, "surfaceDestroyed: " + MyVideoView.this);
                MyVideoView.this.mSurfaceHolder = null;
                if(MyVideoView.this.mMediaPlayer != null && (MyVideoView.this.mTargetState == 3 || MyVideoView.this.mTargetState == 4)) {
                    MyVideoView.this.mSeekWhenPrepared = (int)MyVideoView.this.mMediaPlayer.getCurrentPosition();
                }

                MyVideoView.this.release(false);
            }
        };
        this.initVideoView();
    }

    public MyVideoView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.mPreferPlayer = PreferPlayer.DEFAULT;
        this.mScaleType = ScaleType.fitXY;
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer var1, int var2, int var3) {
                MyVideoView.this.mVideoWidth = var1.getVideoWidth();
                MyVideoView.this.mVideoHeight = var1.getVideoHeight();
                if(MyVideoView.this.mVideoWidth != 0 && MyVideoView.this.mVideoHeight != 0) {
                    MyVideoView.this.getHolder().setFixedSize(MyVideoView.this.mVideoWidth, MyVideoView.this.mVideoHeight);
                    MyVideoView.this.requestLayout();
                }

            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer var1) {
                MyVideoView.this.mCurrentState = 2;
                MyVideoView.this.mCanPause = MyVideoView.this.mCanSeekBack = MyVideoView.this.mCanSeekForward = true;
                if(MyVideoView.this.mOnPreparedListener != null) {
                    MyVideoView.this.mOnPreparedListener.onPrepared(MyVideoView.this.mMediaPlayer);
                }

                MyVideoView.this.mVideoWidth = var1.getVideoWidth();
                MyVideoView.this.mVideoHeight = var1.getVideoHeight();
                int var2 = MyVideoView.this.mSeekWhenPrepared;
                if(var2 != 0) {
                    MyVideoView.this.seekTo(var2);
                }

                if(MyVideoView.this.mVideoWidth != 0 && MyVideoView.this.mVideoHeight != 0) {
                    MyVideoView.this.getHolder().setFixedSize(MyVideoView.this.mVideoWidth, MyVideoView.this.mVideoHeight);
                    if(MyVideoView.this.mSurfaceWidth == MyVideoView.this.mVideoWidth && MyVideoView.this.mSurfaceHeight == MyVideoView.this.mVideoHeight) {
                        if(MyVideoView.this.mTargetState == 3) {
                            MyVideoView.this.start();
                        } else if(!MyVideoView.this.isPlaying() && var2 == 0 && MyVideoView.this.getCurrentPosition() > 0) {
                            ;
                        }
                    }
                } else if(MyVideoView.this.mTargetState == 3) {
                    MyVideoView.this.start();
                }

            }
        };
        this.mSeekListener = new IMediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(IMediaPlayer var1) {
                if(MyVideoView.this.mOnSeekCompleteListener != null) {
                    MyVideoView.this.mOnSeekCompleteListener.onSeekComplete(var1);
                }

            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer var1) {
                MyVideoView.this.mCurrentState = 5;
                MyVideoView.this.mTargetState = 5;
                if(MyVideoView.this.mOnCompletionListener != null) {
                    MyVideoView.this.mOnCompletionListener.onCompletion(MyVideoView.this.mMediaPlayer);
                }

            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer var1, int var2, int var3) {
                if(MyVideoView.this.mOnInfoListener != null) {
                    MyVideoView.this.mOnInfoListener.onInfo(var1, var2, var3);
                }

                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer var1, int var2, int var3) {
                Log.d(MyVideoView.this.TAG, "Error: " + var2 + "," + var3);
                MyVideoView.this.mCurrentState = -1;
                MyVideoView.this.mTargetState = -1;
                return MyVideoView.this.mOnErrorListener != null && MyVideoView.this.mOnErrorListener.onError(MyVideoView.this.mMediaPlayer, var2, var3)?true:true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer var1, int var2) {
                MyVideoView.this.mCurrentBufferPercentage = var2;
            }
        };
        this.mSHCallback = new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
                Log.d(MyVideoView.this.TAG, "surfaceChanged:" + MyVideoView.this);
                MyVideoView.this.mSurfaceWidth = var3;
                MyVideoView.this.mSurfaceHeight = var4;
                boolean var5 = MyVideoView.this.mTargetState == 3;
                boolean var6 = MyVideoView.this.mVideoWidth == var3 && MyVideoView.this.mVideoHeight == var4;
                if(MyVideoView.this.mMediaPlayer != null && var5 && var6) {
                    if(MyVideoView.this.mSeekWhenPrepared != 0) {
                        MyVideoView.this.seekTo(MyVideoView.this.mSeekWhenPrepared);
                    }

                    MyVideoView.this.start();
                }

            }

            public void surfaceCreated(SurfaceHolder var1) {
                Log.d(MyVideoView.this.TAG, "surfaceCreated:" + MyVideoView.this);
                MyVideoView.this.mSurfaceHolder = var1;
                MyVideoView.this.openVideo();
            }

            public void surfaceDestroyed(SurfaceHolder var1) {
                Log.d(MyVideoView.this.TAG, "surfaceDestroyed: " + MyVideoView.this);
                MyVideoView.this.mSurfaceHolder = null;
                if(MyVideoView.this.mMediaPlayer != null && (MyVideoView.this.mTargetState == 3 || MyVideoView.this.mTargetState == 4)) {
                    MyVideoView.this.mSeekWhenPrepared = (int)MyVideoView.this.mMediaPlayer.getCurrentPosition();
                }

                MyVideoView.this.release(false);
            }
        };
        throw new UnsupportedOperationException("not support create in xml");
    }

    protected void onMeasure(int var1, int var2) {
        int var3 = getDefaultSize(this.mVideoWidth, var1);
        int var4 = getDefaultSize(this.mVideoHeight, var2);
        if(this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            int var5 = MeasureSpec.getMode(var1);
            int var6 = MeasureSpec.getSize(var1);
            int var7 = MeasureSpec.getMode(var2);
            int var8 = MeasureSpec.getSize(var2);
            if(this.mScaleType == ScaleType.fitXY) {
                this.setMeasuredDimension(var1, var2);
                return;
            }

            if(this.mScaleType == ScaleType.normal) {
                this.setMeasuredDimension(this.mVideoWidth, this.mVideoHeight);
                return;
            }

            if(var5 == 1073741824 && var7 == 1073741824) {
                var3 = var6;
                var4 = var8;
                if(this.mVideoWidth * var8 < var6 * this.mVideoHeight) {
                    var3 = var8 * this.mVideoWidth / this.mVideoHeight;
                } else if(this.mVideoWidth * var8 > var6 * this.mVideoHeight) {
                    var4 = var6 * this.mVideoHeight / this.mVideoWidth;
                }
            } else if(var5 == 1073741824) {
                var3 = var6;
                var4 = var6 * this.mVideoHeight / this.mVideoWidth;
                if(var7 == -2147483648 && var4 > var8) {
                    var4 = var8;
                }
            } else if(var7 == 1073741824) {
                var4 = var8;
                var3 = var8 * this.mVideoWidth / this.mVideoHeight;
                if(var5 == -2147483648 && var3 > var6) {
                    var3 = var6;
                }
            } else {
                var3 = this.mVideoWidth;
                var4 = this.mVideoHeight;
                if(var7 == -2147483648 && var4 > var8) {
                    var4 = var8;
                    var3 = var8 * this.mVideoWidth / this.mVideoHeight;
                }

                if(var5 == -2147483648 && var3 > var6) {
                    var3 = var6;
                    var4 = var6 * this.mVideoHeight / this.mVideoWidth;
                }
            }
        }

        this.setMeasuredDimension(var3, var4);
    }

    private void initVideoView() {
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.getHolder().addCallback(this.mSHCallback);
        this.getHolder().setType(3);
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    public void setScaleType(ScaleType var1) {
        this.mScaleType = var1;
        this.invalidate();
    }

    public void setVideoPath(String var1, Map<String, String> var2) {
        this.setVideoURI(Uri.parse(var1), var2);
    }

    private void setVideoURI(Uri var1, Map<String, String> var2) {
        this.mUri = var1;
        this.mHeaders = var2;
        this.mSeekWhenPrepared = 0;
        this.mTargetState = 1;
        this.openVideo();
        this.requestLayout();
        this.invalidate();
    }

    public void stopPlayback() {
        if(this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.release();
            } catch (IllegalStateException var6) {
                var6.printStackTrace();
            } catch (IllegalArgumentException var7) {
                var7.printStackTrace();
            } finally {
                this.mMediaPlayer = null;
                this.mCurrentState = 0;
                this.mTargetState = 0;
                this.mUri = null;
            }
        }

    }

    private void openVideo() {
        if(this.mUri != null && this.mSurfaceHolder != null && this.mTargetState != 0 && this.mTargetState != -1) {
            Log.d(this.TAG, "openVideo: " + this.mUri.toString());
            Context var1 = this.getContext();
            Intent var2 = new Intent("com.android.music.musicservicecommand");
            var2.putExtra("command", "pause");
            var1.sendBroadcast(var2);
            this.release(false);

            try {
                if(this.mPreferPlayer == PreferPlayer.SYSTEM) {
                    this.mMediaPlayer = new SystemPlayer(this.getContext());
                } else if(this.mPreferPlayer == PreferPlayer.MTKPlayer) {
                    this.mMediaPlayer = new MTKPlayer(this.getContext());
                } else if(this.mPreferPlayer == PreferPlayer.IJKPlayer) {
                    this.mMediaPlayer = new ffplay(this.getContext());
                } else if(this.mPreferPlayer == PreferPlayer.EXOPlayer) {
                    this.mMediaPlayer = new WasuExoPlayer(this.getContext());
                } else if(ffplay.isMediaCodecSurpported("video/avc")) {
                    this.mMediaPlayer = new ffplay(this.getContext());
                } else {
                    this.mMediaPlayer = new SystemPlayer(this.getContext());
                }

                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mCurrentBufferPercentage = 0;
                if (mMediaPlayer instanceof ffplay){
                    ((ffplay)mMediaPlayer).setOption(1,"user-agent","wasutv_player1.1");
                }
                this.mMediaPlayer.setDataSource(var1, this.mUri,mHeaders);

                this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 1;
            } catch (IOException var4) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, var4);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException var5) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, var5);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener var1) {
        this.mOnPreparedListener = var1;
    }

    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener var1) {
        this.mOnSeekCompleteListener = var1;
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener var1) {
        this.mOnCompletionListener = var1;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener var1) {
        this.mOnErrorListener = var1;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener var1) {
        this.mOnInfoListener = var1;
    }

    private void release(boolean var1) {
        if(this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.reset();
                this.mMediaPlayer.release();
            } catch (IllegalStateException var7) {
                var7.printStackTrace();
            } catch (IllegalArgumentException var8) {
                var8.printStackTrace();
            } finally {
                this.mMediaPlayer = null;
                this.mCurrentState = 0;
                if(var1) {
                    this.mTargetState = 0;
                }

            }
        }

    }

    public void start() {
        if(this.isInPlaybackState()) {
            Log.d(this.TAG, "mMediaPlayer.start()");
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }

        this.mTargetState = 3;
    }

    public void pause() {
        if(this.isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }

        this.mTargetState = 4;
    }

    public void suspend() {
        this.release(false);
    }

    public void resume() {
        this.openVideo();
    }

    public int getDuration() {
        return this.isInPlaybackState()?(int)this.mMediaPlayer.getDuration():-1;
    }

    public int getCurrentPosition() {
        return this.isInPlaybackState()?(int)this.mMediaPlayer.getCurrentPosition():0;
    }

    public void seekTo(int var1) {
        if(this.isInPlaybackState()) {
            this.mMediaPlayer.seekTo((long)var1);
            this.mSeekWhenPrepared = 0;
        } else {
            this.mSeekWhenPrepared = var1;
        }

    }

    public boolean isPlaying() {
        return this.isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return this.mMediaPlayer != null?this.mCurrentBufferPercentage:0;
    }

    private boolean isInPlaybackState() {
        return this.mMediaPlayer != null && this.mCurrentState != -1 && this.mCurrentState != 0 && this.mCurrentState != 1;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public void setPreferPlayer(PreferPlayer var1) {
        this.mPreferPlayer = var1;
    }

    public View getView() {
        return this;
    }

    public int setConfig(String var1, Object var2) {
        return 0;
    }

    public Object getConfig(String var1) {
        return null;
    }
}
