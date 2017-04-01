package com.huwenmin.playerexample.video;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 作者：胡文敏 on 2017/4/1 10:36
 * <p>
 * 功能：示例播放器
 */

public class SimpleVideoPlay extends WasuVideoPlayer {
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
}
