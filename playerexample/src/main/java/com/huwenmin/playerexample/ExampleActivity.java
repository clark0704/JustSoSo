package com.huwenmin.playerexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.media.IMediaPlayer;
import com.media.IVideoView;
import com.media.WasuVideoView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExampleActivity extends AppCompatActivity {

    @BindView(R.id.wasuVideoView)
    MyVideoView mWasuVideoView;
    @BindView(R.id.imageView)
    ImageView mImageView;

    private Unbinder mBinder;

    private String exampleUrl = "http://baobab.wdjcdn.com/14564977406580.mp4";

    private Map<String ,String> heads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        mBinder = ButterKnife.bind(this);

//        heads = new LinkedHashMap<>();
//        heads.put("user-agent","wasutv_player1.1");
        prepareVideo();
    }


    private void prepareVideo() {
//        exampleUrl = "http://clientlive1-cnc.wasu.cn/hdaac_jsws/z.m3u8";
        exampleUrl = "http://apkvod-cnc.wasu.cn/201703301609/3b8242acbc428f21361740b1dc0fe52e/pcsan12/mams/vod/201703/15/13/20170315135636900652f1f03/playlist.m3u8?k=2e3d0296246c1b1fb20034386ab30951&su=Khbsg9vsDclN1GYRcLlfNg==&uid=9253adf8ba4b532eba0cd0ee35bd462b&tn=27833463&t=656c1fe15f0eff69d58e08380e43690e&src=wasu.cn&cid=22&vid=8579737&WS00001=10000&em=3";
        mWasuVideoView.setPreferPlayer(IVideoView.PreferPlayer.IJKPlayer);
        mWasuVideoView.setScaleType(IVideoView.ScaleType.fitCenter);
        mWasuVideoView.setVideoPath(exampleUrl,heads);
//        mWasuVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(IMediaPlayer iMediaPlayer) {
//                showMsg("播放结束！");
//                mImageView.setImageResource(R.drawable.play_normal_selector);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBinder.unbind();
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.imageView)
    public void onViewClicked() {
        if (mWasuVideoView.isPlaying()){
            mImageView.setImageResource(R.drawable.play_normal_selector);
            mWasuVideoView.pause();
        }else {
            mImageView.setImageResource(R.drawable.play_pause_selector);
            mWasuVideoView.start();

        }


    }
}
