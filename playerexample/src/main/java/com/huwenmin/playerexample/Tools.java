package com.huwenmin.playerexample;

import android.content.Context;
import android.util.Log;

import com.wasu.cores.WasuCore;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

/**
 * 作者：Administrator on 2017/3/29 15:36
 * <p>
 * 功能：
 */

public class Tools {

    /**
     * 取得播放地址
     *
     * @param url       播放地址
     * @param timestamp 时间戳
     * @param isLive    是否直播,
     */
    public static String getPlayUrl(Context context, String url, long timestamp, boolean isLive, boolean isP2P, String cid, String vid, boolean isDownload) {
        String playUrl = "";
        String timestampStr = null;

        if (!StringUtils.isEmpty(url)) {
//            url = url.replace("apkvod-cnc.wasu.cn","apkvod-bj.wasu.cn");
            try {
                if (!isDownload && url.endsWith(".mp4")) {
                    url = url.substring(0, url.lastIndexOf(".")) + "/playlist.m3u8";//替换成m3u8
                }

                URL uurl = new URL(url);
                //判断是否为过滤域名,如果是则直接播放,否则进行加密
//                String filter_domain = OnlineConfigAgent.getInstance().getConfigParams(context, "PLAY_URL_FILTER_DOMAIN");

                String key = "liveWASU1234@#&*";
                if (isLive) {
                    key = "liveWASU12#$56&*";
                }

                //是否能获取到服务器时间,如果不能则获取系统时间
                if (timestamp > 0) {

                    timestampStr = TimeTools.getDateCompleteStr(timestamp);


                } else {
                    if (isLive) {//旧域名下则用明码
                        timestampStr = TimeTools.getDateCompleteStr(System.currentTimeMillis());
                    } else {
                        timestampStr = Long.toHexString(System.currentTimeMillis() / 1000);
                    }
                }

                String temp = url.substring(url.indexOf(uurl.getHost()) + uurl.getHost().length());


                //先获取参数，之后再加时间戳
                try {
                    playUrl = WasuCore.getPlayUrlParams(context, url, "wasutv_player1.1", timestampStr);
                } catch (NullPointerException nex) {
                    nex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String md5 = key + timestampStr + temp;
                playUrl = playUrl.replace(uurl.getHost(), uurl.getHost() + "/" + timestampStr + "/" + Tools.MD5(md5));

                playUrl += "&src=" + "wasu.cn";
                playUrl += "&cid=" + cid;
                playUrl += "&vid=" + vid;
                playUrl += "&WS00001=" + 10000;
                playUrl += "&em=3";

                Log.e("Tools:", "播放地址:" + playUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return playUrl;
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
