package com.huwenmin.brvahdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.huwenmin.brvahdemo.adapter.BannerPagerAdapter;
import com.huwenmin.brvahdemo.adapter.HotspotAdapter;
import com.huwenmin.brvahdemo.http.RetrofitHelper;
import com.huwenmin.brvahdemo.module.AssertItem;
import com.huwenmin.brvahdemo.module.AssertPageBean;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnRefreshListener,RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Unbinder mUnbinder;

    private Disposable mDisposable;

    private Context context;

    private HotspotAdapter mAdapter;

    private final static int LIMIT = 8;

    private View bannerView;

    private UltraViewPager mUltraViewPager;
    private BannerPagerAdapter mBannerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        context = this;

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        initAdapter();

        initBestData();

        initBanners();
    }

    private void initBanners() {
        RetrofitHelper.getInstance().getApi().getHotLbt()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<List<AssertItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<AssertItem> value) {

                       if (value != null)initViewPager(value);

                        mAdapter.addHeaderView(bannerView);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initViewPager(List<AssertItem> mAssertItems) {
        bannerView = LayoutInflater.from(context).inflate(R.layout.layout_banner, null);
        mUltraViewPager = (UltraViewPager) bannerView.findViewById(R.id.ultraViewPager);
        //设置左右滑动
        mUltraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        mBannerPagerAdapter = new BannerPagerAdapter(mAssertItems, context);
        mUltraViewPager.setAdapter(mBannerPagerAdapter);

        //内置indicator初始化
        mUltraViewPager.initIndicator();
        //设置indicator样式
        mUltraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusResId(R.mipmap.dot_selected)
                .setNormalResId(R.mipmap.dot_none)
                .setGravity(Gravity.RIGHT | Gravity.BOTTOM )
//                .setIndicatorPadding(10)
//                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics()))
                .build();
        //设定页面循环播放
        mUltraViewPager.setInfiniteLoop(true);
        //设定页面自动切换  间隔2秒
        mUltraViewPager.setAutoScroll(5000);
    }

    private long historyTime = System.currentTimeMillis() / 1000;
    private int p = 1;
    private List<AssertItem> hotBests;
    private int total_count;
    private boolean isError = false;

    private void initData() {

        RetrofitHelper.getInstance().getApi().getHistoryHot(historyTime, p, LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<AssertPageBean>() {

                    private  List<AssertItem> mList;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(AssertPageBean value) {

                        if (value != null && value.getDatas() != null) {
                            total_count = value.getPage_count();
                            if (value.getDatas().size() > 0) {

                                mList = new ArrayList<AssertItem>();

                                for (AssertItem item : value.getDatas()) {
                                    item.setItemType(AssertItem.NORMAL);
                                    mList.add(item);
                                }
                            }
                        }

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(getClass().getSimpleName(), e.getMessage());
                        isError = true ;

                    }
                    @Override
                    public void onComplete() {
                        /**
                         * 每间隔四个位置添加精选大图
                         * 没有数据不用管
                         */
                        if (mList.size() > 3 && hotBests.size() > 0) {
                            mList.add(4, hotBests.get(0));
                            hotBests.remove(0);
                        }
                        if (mList.size() > 8 && hotBests.size() > 0) {
                            mList.add(9, hotBests.get(0));
                            hotBests.remove(0);
                        }
                        mAdapter.addData(mList);

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initBestData() {
        RetrofitHelper.getInstance().getApi().getHotBest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<List<AssertItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(List<AssertItem> value) {
                        if (value != null) {

                            if (value.size() > 0) {
                                hotBests = new ArrayList<AssertItem>();
                                for (AssertItem item : value) {
                                    item.setItemType(AssertItem.BIG_IMG);
                                    hotBests.add(item);
                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getClass().getSimpleName(), e.getMessage());
                        initData();
                    }

                    @Override
                    public void onComplete() {
                        initData();
                    }
                });
    }

    private void initAdapter() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //防止下拉刷新和recyclerView 冲突导致崩溃
        mRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
        mAdapter = new HotspotAdapter(null);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.openLoadAnimation();

        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onRefresh() {
      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              mAdapter.setEnableLoadMore(false);
              p = 1;
              mSwipeRefreshLayout.setRefreshing(false);
              mAdapter.setEnableLoadMore(true);

              historyTime = System.currentTimeMillis() / 1000;

              initBestData();
              mAdapter.setNewData(null);
          }
      },1000);
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        if (mAdapter.getData().size() < LIMIT) {
            mAdapter.disableLoadMoreIfNotFullPage();
        } else {
            if (mAdapter.getData().size() >= total_count) {
                mAdapter.loadMoreEnd();
            } else {
                if (isError) {
                    isError = false;
                    mAdapter.loadMoreFail();
                } else {
                    p++;
                    initData();
                    mAdapter.loadMoreComplete();
                }
            }
        }
        mSwipeRefreshLayout.setEnabled(true);
    }
}
