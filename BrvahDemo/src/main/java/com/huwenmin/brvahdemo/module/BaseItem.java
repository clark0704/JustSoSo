package com.huwenmin.brvahdemo.module;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 作者：Administrator on 2017/5/17 09:12
 * <p>
 * 功能：
 */

public abstract class BaseItem implements MultiItemEntity {

    public static final int NORMAL = 1;
    public static final int BIG_IMG = 2;
    public static final int BANNER = 3;

    @Override
    public int getItemType() {
        return 0;
    }
}
