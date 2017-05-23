package com.huwenmin.brvahdemo.http;

import android.util.Log;

import com.google.gson.Gson;
import com.huwenmin.brvahdemo.module.UPMLoginDeviceRespBean;
import com.huwenmin.brvahdemo.module.UPMPhoneCodeRespBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceReqBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceRespBean;

import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 作者：Administrator on 2017/5/22 09:39
 * <p>
 * 功能：
 */

public class UPMUserApiService {

    private static UPMUserApiService instance;

    private BaseRequestListener mListener;

    private Disposable mDisposable;

    public static synchronized UPMUserApiService getInstance() {
        if (instance == null) instance = new UPMUserApiService();
        return instance;
    }

    /**
     * 设置回调接口
     *
     * @param listener
     */
    public void setListener(BaseRequestListener listener) {
        mListener = listener;
    }

    /**
     * 设备注册接口
     *
     * @param bean
     */
    public void registerDeviceToUPM(UPMRegisterDeviceReqBean bean ,String v ) {
        Gson gson = new Gson();
        String s = gson.toJson(bean);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);

        UPMUserHttpUtils.getInstance().setMsg(UPMDataUtils.REGISTER_DEVICE_MSG,v).getApi().getUserRegisterMes(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UPMRegisterDeviceRespBean>() {

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(UPMRegisterDeviceRespBean upmLoginDeviceBean) {
                        mListener.getRegisterBean(upmLoginDeviceBean);
                    }
                });
    }

    /**
     * UPM设备登录接口
     */
    public void loginDeviceToUPM(String bytes , String v) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bytes);
        UPMUserHttpUtils.getInstance().setMsg(UPMDataUtils.LOGIN_DEVICE_MSG,v).getApi().getUserLoginMes(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UPMLoginDeviceRespBean>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(UPMLoginDeviceRespBean upmLoginDeviceRespBean) {
                        mListener.getLoginBean(upmLoginDeviceRespBean);
                    }
                });
    }

    public void getPhoneCode(byte[] bytes,String v) {


        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bytes);

        UPMUserHttpUtils.getInstance().setMsg(UPMDataUtils.GET_PHONE_CODE_MSG,v).getApi().getBody(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(ResponseBody value) {
                        mListener.getPhoneCode(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
