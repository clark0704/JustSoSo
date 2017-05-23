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
import com.huwenmin.brvahdemo.module.UPMPhoneCodeRespBean;
import com.huwenmin.brvahdemo.module.UPMRegisterDeviceRespBean;
import com.huwenmin.brvahdemo.utils.ACache;
import com.huwenmin.brvahdemo.utils.StringUtils;
import com.huwenmin.brvahdemo.utils.UPMUtil;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.ResponseBody;

public class Main2Activity extends AppCompatActivity implements BaseRequestListener {

    private UPMUserApiService mUPMUserApiService;
    private ACache mACache;

    String v = "0";

    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mUPMUserApiService = UPMUserApiService.getInstance();
        mUPMUserApiService.setListener(this);
        mACache = ACache.get(this);

        gson = new Gson();

        mUPMUserApiService.registerDeviceToUPM(UPMDataUtils.getRegisterReqBean(),v);

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
        if (bean != null && !StringUtils.isEmpty(bean.getDeviceId())) {

            UPMLoginDeviceReqBean loginDeviceReqBean = UPMDataUtils.getLoginReqBean(bean.getDeviceId());
            mACache.put("deviceId", bean.getDeviceId());

            Gson gson = new Gson();
            String s = gson.toJson(loginDeviceReqBean);

            if (bean.getEncryptV() != -1){
                v= bean.getEncryptV() +"";
                mACache.put("EncryptV",bean.getEncryptV());
            }

            mUPMUserApiService.loginDeviceToUPM(s,v);
        }
    }

    @Override
    public void getLoginBean(UPMLoginDeviceRespBean bean) {
        if (bean != null && !StringUtils.isEmpty(bean.getPublicKey()))
            mACache.put("publicKey", bean.getPublicKey());

        if (!StringUtils.isEmpty(bean.getEncryptV())){
            v= bean.getEncryptV() +"";
            mACache.put("EncryptV",bean.getEncryptV());
        }else {
            v= mACache.getAsString("EncryptV");
        }

        Log.e("666:",v);

        String s = gson.toJson(UPMDataUtils.getPhoneCode(mACache.getAsString("deviceId"), "18306438524", 0));
        Log.e("Main2Actiity",s);

        String publicKey = mACache.getAsString("publicKey");

        byte[] bytes = null;
        try {
            bytes = UPMUtil.encrypt(s.getBytes(), publicKey.getBytes());

            mUPMUserApiService.getPhoneCode(bytes,v);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getPhoneCode(ResponseBody bean) {
        byte[] bytes = null;
        try {
            bytes = UPMUtil.decrypt(bean.bytes(), mACache.getAsString("publicKey").getBytes());

            String s = new String(bytes);


           UPMPhoneCodeRespBean fromJson =  gson.fromJson(s,UPMPhoneCodeRespBean.class);


            Log.e("666:",s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
