package com.huwenmin.hellomvp.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huwenmin.hellomvp.R;
import com.huwenmin.hellomvp.adapter.MainAdapter;
import com.huwenmin.hellomvp.listener.BaseListener;
import com.huwenmin.hellomvp.listener.SampleListener;
import com.huwenmin.hellomvp.model.bean.AssertPageBean;
import com.huwenmin.hellomvp.presenter.HotspotPresent;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity implements View.OnClickListener, BaseListener<AssertPageBean> {

    Unbinder mUnbinder;
    RecyclerView mRecyclerView;
    ListVideoUtil mListVideoUtil;
    FrameLayout mVideoFullContainer;

    LinearLayoutManager linearLayoutManager;
    int lastVisibleItem;
    int firstVisibleItem;

    private HotspotPresent mPresent = new HotspotPresent();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mVideoFullContainer = (FrameLayout) findViewById(R.id.video_full_container);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mListVideoUtil = new ListVideoUtil(this);
        mListVideoUtil.setFullViewContainer(mVideoFullContainer);
        mListVideoUtil.setHideStatusBar(true);
    }

    @Override
    protected void initData() {
        mPresent.getHotspotData();
        mPresent.setListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);

        initAdapter();

        mListVideoUtil.setHideActionBar(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Debuger.printfLog("firstVisibleItem " + firstVisibleItem +" lastVisibleItem " + lastVisibleItem);
                //大于0说明有播放,//对应的播放列表TAG
                if (mListVideoUtil.getPlayPosition() >= 0 && mListVideoUtil.getPlayTAG().equals(MainAdapter.TAG)) {
                    //当前播放的位置
                    int position = mListVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!mListVideoUtil.isSmall() && !mListVideoUtil.isFull()) {
                            //小窗口
                            int size_x = CommonUtil.dip2px(MainActivity.this, 160);
                            int size_y = CommonUtil.dip2px(MainActivity.this, 100);
                            //actionbar为true才不会掉下面去
                            mListVideoUtil.showSmallVideo(new Point(size_x, size_y), true, true);
                        }
                    } else {
                        if (mListVideoUtil.isSmall()) {
                            mListVideoUtil.smallVideoToNormal();
                        }
                    }
                }
            }
        });

        //小窗口关闭被点击的时候回调处理回复页面
        mListVideoUtil.setVideoAllCallBack(new SampleListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("Duration " + mListVideoUtil.getDuration() + " CurrentPosition " + mListVideoUtil.getCurrentPositionWhenPlaying());
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                super.onQuitSmallWidget(url, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (mListVideoUtil.getPlayPosition() >= 0 && mListVideoUtil.getPlayTAG().equals(MainAdapter.TAG)) {
                    //当前播放的位置
                    int position = mListVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        mListVideoUtil.releaseVideoPlayer();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (mListVideoUtil.backFromFull()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresent.onStop();
        mUnbinder.unbind();
        mListVideoUtil.releaseVideoPlayer();
        GSYVideoPlayer.releaseAllVideos();


        mAdapter = null;

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(AssertPageBean pageBean) {
        mAdapter.addData(pageBean.getDatas());
        mAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        mAdapter = new MainAdapter(null, this);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.setEmptyView(emptyView);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        ((MainAdapter) mAdapter).setListVideoUtil(mListVideoUtil);

    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
    }


}
