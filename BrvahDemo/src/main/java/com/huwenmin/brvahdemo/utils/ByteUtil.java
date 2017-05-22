/**
 * cn.com.sand.pack4j.util.ByteUtil.java
 */
package com.huwenmin.brvahdemo.utils;

import java.util.List;

/**
 * 项目：pack4j
 * <br>
 * 描述：
 * byte工具类
 * 
 * @author 朱伟
 * @since 2006-10-25 13:05:25
 */
public class ByteUtil {
	
	/**
     * 合并两个byte数组。合并后的数组将bs1放在在bs2的前面。
     * @param bs1
     * @param bs2
     * @return byte[]
     */
    public static byte[] union(byte[]bs1,byte[]bs2){
        byte[]bs=new byte[bs1.length+bs2.length];
        for(int i=0;i<bs1.length;i++)
            bs[i]=bs1[i];
        for(int i=0;i<bs2.length;i++)
            bs[bs1.length+i]=bs2[i];
        return bs;
    }
    
    public static byte[] union(List<byte[]> byteList) {
        int size = 0;
        for (byte[] bs : byteList) {
            size += bs.length;
        }
        byte[] ret = new byte[size];
        int pos = 0;
        for (byte[] bs : byteList) {
            System.arraycopy(bs, 0, ret, pos, bs.length);
            pos += bs.length;
        }
        return ret;
    }
}
