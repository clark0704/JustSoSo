package com.huwenmin.brvahdemo.http;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Administrator on 2017/3/16 16:55
 * <p>
 * 功能：Retrofit相关配置
 */

public class RetrofitHelper {
    /**
     * 请求地址
     */
    private static String BASEURL = "http://clientapi.wasu.cn/";
    /**
     * 请求数据Timeout时间，30秒
     */
    private static int TIMEOUT_READ = 30000;
    /**
     * 连接数据Timeout时间，30秒
     */
    private static int TIMEOUT_CONNECTION = 30000;
    OkHttpClient client= null;
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;

    public static RetrofitHelper getInstance() {

        if (instance == null) instance = new RetrofitHelper();
        return instance;
    }

    private Retrofit getRetrofit(OkHttpClient client){
        if (mRetrofit == null){
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .client(client)
                    .addConverterFactory(factory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return  mRetrofit;
    }

    public RetrofitApi getApi(){
        return getRetrofit(getClient()).create(RetrofitApi.class);
    }

    private OkHttpClient getClient() {
        if (client == null){
            //新建log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    Log.e("RetrofitLog","retrofitBack = "+message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT_CONNECTION,TimeUnit.SECONDS)
                    .build();

        }
        return client;
    }
}
