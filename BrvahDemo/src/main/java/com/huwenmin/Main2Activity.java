package com.huwenmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.huwenmin.brvahdemo.R;
import com.huwenmin.brvahdemo.http.BaseRequestListener;
import com.huwenmin.brvahdemo.http.UPMDataUtils;
import com.huwenmin.brvahdemo.http.UPMUserApiService;
import com.huwenmin.brvahdemo.module.UPMLoginDeviceReqBean;
import com.huwenmin.brvahdemo.module.UPMLoginDeviceRespBean;
import com.huwenmin.brvahdemo.module.UPMPhoneCodeReqBean;
import com.huwenmin.brvahdemo.module.UPMPhoneCodeRespBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceRespBean;
import com.huwenmin.brvahdemo.utils.ACache;
import com.huwenmin.brvahdemo.utils.StringUtils;
import com.huwenmin.brvahdemo.utils.UPMUtil;

import java.security.interfaces.RSAPublicKey;

import Decoder.BASE64Encoder;

public class Main2Activity extends AppCompatActivity implements BaseRequestListener{

    private UPMUserApiService mUPMUserApiService;
    private ACache mACache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mUPMUserApiService = UPMUserApiService.getInstance();
        mUPMUserApiService.setListener(this);
        mACache = ACache.get(this);

        mUPMUserApiService.registerDeviceToUPM(UPMDataUtils.getRegisterReqBean());

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUPMUserApiService.unSubscribe();
    }

    @Override
    public void getRegisterBean(UPMRegisterDeviceRespBean bean) {
        /**
         * UPM登录设备
         */
        if (bean!=null && !StringUtils.isEmpty(bean.getDeviceId())) {

            UPMLoginDeviceReqBean loginDeviceReqBean = UPMDataUtils.getLoginReqBean(bean.getDeviceId());
            mACache.put("deviceId",bean.getDeviceId());

            Gson gson = new Gson();
            String s = gson.toJson(loginDeviceReqBean);

            String publicKey = null;
            if (StringUtils.isEmpty(bean.getPublicKey())){
                publicKey = mACache.getAsString("publicKey");
            }else {
                publicKey = bean.getPublicKey();
                mACache.put("publicKey",publicKey);
            }
//            byte[] bytes = null;
            try {
//                RSAPublicKey key  = UPMUtil.loadPublicKey(publicKey);
//                bytes = UPMUtil.encrypt(s.getBytes(),key);

                mUPMUserApiService.loginDeviceToUPM(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getLoginBean(UPMLoginDeviceRespBean bean) {
       if (bean != null && !StringUtils.isEmpty(bean.getPublicKey()))mACache.put("publicKey",bean.getPublicKey());

        Gson gson = new Gson();

        String s = gson.toJson(UPMDataUtils.getPhoneCode(mACache.getAsString("deviceId"),"15858241815",0));

        String publicKey = mACache.getAsString("publicKey");

                    byte[] bytes = null;
        try {
                RSAPublicKey key  = UPMUtil.loadPublicKey(publicKey);
                bytes = UPMUtil.encrypt(s.getBytes(),key);



            mUPMUserApiService.getPhoneCode(new BASE64Encoder().encode(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    public void getPhoneCode(UPMPhoneCodeRespBean bean) {
    }
}
