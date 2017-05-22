package com.huwenmin.brvahdemo.http;


import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Administrator on 2017/5/19 16:53
 * <p>
 * 功能：UPM登陆注册接口
 */

public class UPMUserHttpUtils<T extends Object> {
    /**
     * 请求地址
     */
    private static String BASEURL = "http://test.itf.upm.wasu.tv/";//"http://223.202.16.247:18018/";// https://phoneapi.wasu.cn/";
//    private static String BASEURL = "http://itf.upm.wasu.tv/pcum";
    /**
     * 请求数据Timeout时间，30秒
     */
    private static int TIMEOUT_READ = 30000;
    /**
     * 连接数据Timeout时间，30秒
     */
    private static int TIMEOUT_CONNECTION = 30000;

    private static UPMUserApi mWasuDataApi;
    private static OkHttpClient mOkHttpClient;
    private static Converter.Factory mGsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory mRxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    private static UPMUserHttpUtils instance;

    private  String msgCode;

    public static synchronized UPMUserHttpUtils getInstance(){
        if (instance == null){
           instance = new UPMUserHttpUtils();
        }
        return instance;
    }

    public UPMUserHttpUtils setMsg(String msg){
        msgCode = msg;

        return instance;
    }
    /**
     * 请求api方法
     *
     * @return
     */
    public UPMUserApi getApi() {

        if (mWasuDataApi == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(BASEURL)
                    .addConverterFactory(mGsonConverterFactory)
                    .addCallAdapterFactory(mRxJavaCallAdapterFactory)
                    .build();

            mWasuDataApi = retrofit.create(UPMUserApi.class);
        }
        return mWasuDataApi;
    }

    /**
     * OK http 设置
     *
     * @return
     */
    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            //新建log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    Log.e("RetrofitLog","retrofitBack = "+message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type","application/json; charset=UTF-8")
                            .addHeader("msg",msgCode)
                            .addHeader("v","0")
                            .build();

                    return chain.proceed(request);
                }
            };

            mOkHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(interceptor)
                    //失败重连
                    .retryOnConnectionFailure(true)
                    //time out
                    .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                    .build();
        }

        return mOkHttpClient;
    }
}
