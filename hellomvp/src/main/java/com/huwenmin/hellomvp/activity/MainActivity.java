package com.huwenmin.hellomvp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huwenmin.hellomvp.R;
import com.huwenmin.hellomvp.bean.AssertPageBean;
import com.huwenmin.hellomvp.listener.BaseListener;
import com.huwenmin.hellomvp.presenter.HotspotPresent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity implements View.OnClickListener,BaseListener<AssertPageBean>{

    Unbinder mUnbinder;
    @BindView(R.id.tv_title)
    TextView mTextView;

    private HotspotPresent mPresent = new HotspotPresent();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void initView() {
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresent.onStop();
        mUnbinder.unbind();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(AssertPageBean pageBean) {
        mTextView.setText(pageBean.toString());
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this,error,Toast.LENGTH_LONG).show();
    }
}
