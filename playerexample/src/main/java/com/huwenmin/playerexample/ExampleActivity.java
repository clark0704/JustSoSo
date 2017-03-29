package com.huwenmin.playerexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.media.IVideoView;
import com.media.WasuVideoView;

import java.util.HashMap;
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

    private String exampleUrl = "http://video.jiecao.fm/8/16/%E8%B7%B3%E8%88%9E.mp4";
    private Map<String,String> heads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        mBinder = ButterKnife.bind(this);

        heads = new HashMap<>();
        heads.put("User-Agent","wasutv_player1.1");
        mWasuVideoView.setPreferPlayer(IVideoView.PreferPlayer.IJKPlayer);
        mWasuVideoView.setScaleType(IVideoView.ScaleType.fitCenter);

    }


    private void playVideo() {
        exampleUrl = Tools.getPlayUrl(ExampleActivity.this,exampleUrl,0,false,false,null,null,false);
        mWasuVideoView.setVideoPath(exampleUrl, heads);
        mWasuVideoView.start();
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
        mImageView.setVisibility(View.GONE);
        playVideo();
    }
}
