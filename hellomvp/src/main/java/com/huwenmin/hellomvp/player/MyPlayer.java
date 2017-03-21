package com.huwenmin.hellomvp.player;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.GSYVideoPlayer;

/**
 * 作者：胡文敏 on 2017/3/21 14:53
 * <p>
 * 功能：我的自定义播放器
 */

public class MyPlayer extends GSYVideoPlayer {

    public MyPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyPlayer(Context context) {
        super(context);
    }

    public MyPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void startPlayLogic() {

    }
}
