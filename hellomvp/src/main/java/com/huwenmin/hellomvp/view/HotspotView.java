package com.huwenmin.hellomvp.view;

import com.huwenmin.hellomvp.model.bean.AssertPageBean;

/**
 * 作者：Administrator on 2017/3/27 09:58
 * <p>
 * 功能：热点的mvp的V层
 */

public interface HotspotView extends BaseView {
    void getHotspotData(AssertPageBean pageBean);
}
