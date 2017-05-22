package com.huwenmin.brvahdemo.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import com.google.common.collect.Lists;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class UPMUtil {
	
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException{  
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
        keyPairGen.initialize(1024);  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        map.put("public", publicKey);  
        map.put("private", privateKey);  
        return map;  
    }  
    
    
    /**
     * 使用加密
     * @param original
     * @param publicKeyBytes
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] original, byte[] publicKeyBytes) throws Exception {
        if(original != null && original.length > 0 && publicKeyBytes != null && publicKeyBytes.length > 0) {
            RSAPublicKey publicKey = (RSAPublicKey) getPublicKeyo(publicKeyBytes);
            Cipher clipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            Cipher clipher = Cipher.getInstance("RSA");
            
//            System.out.println("original:[" + original + "], publicKey:[" + publicKey + "]");
            clipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长
            int keyLen = publicKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11
            byte[][] datas = splitArray(original, keyLen - 11);
            byte[] result = new byte[0];
            // 如果明文长度大于模长-11则要分组加密
            for (byte[] arr : datas) {
                result = ByteUtil.union(Lists.newArrayList(result, clipher.doFinal(arr)));
            }
//            result = clipher.doFinal(original);
            return result;
        } else {
            return null;
        }
    }
    
    public static byte[] encrypt(byte[] original, RSAPublicKey publicKeyBytes) throws Exception {
        if(original != null && original.length > 0 && getKeyString(publicKeyBytes).getBytes() != null && (getKeyString(publicKeyBytes).getBytes()).length > 0) {
//            RSAPublicKey publicKey = getPublicKey(publicKeyBytes);
            RSAPublicKey publicKey = getPublicKey(publicKeyBytes.getModulus().toString(),publicKeyBytes.getPublicExponent().toString());
            //Cipher clipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            Cipher clipher = Cipher.getInstance("RSA");
            
//            System.out.println("original:[" + original + "], publicKey:[" + publicKey + "]");
            clipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长
            int keyLen = publicKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11
            byte[][] datas = splitArray(original, keyLen - 11);
            byte[] result = new byte[0];
            // 如果明文长度大于模长-11则要分组加密
            for (byte[] arr : datas) {
                result = ByteUtil.union(Lists.newArrayList(result, clipher.doFinal(arr)));
            }
//            result = clipher.doFinal(original);
            return result;
        } else {
            return null;
        }
    }
    
    public static byte[] encrypt1(byte[] original, RSAPublicKey publicKey) throws Exception {
        if(original != null && original.length > 0 && publicKey != null) {
//            RSAPublicKey publicKey = getPublicKey(publicKeyBytes);
            Cipher clipher = Cipher.getInstance("RSA");
//            Cipher clipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            clipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长
            int keyLen = publicKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11
            byte[][] datas = splitArray(original, keyLen - 11);
            byte[] result = new byte[0];
            // 如果明文长度大于模长-11则要分组加密
            for ( byte[] arr : datas) {
                result = ByteUtil.union(Lists.newArrayList(result, clipher.doFinal(arr)));
            }
            return result;
        } else {
            return null;
        }
    }
    
    /**
     * 使用解密
     * @param original
     * @param publicKeyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public static byte[] decrypt(byte[] original, byte[] publicKeyBytes) throws NoSuchAlgorithmException,   InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,    BadPaddingException, IOException {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKeyo(publicKeyBytes);
        int keyLen = publicKey.getModulus().bitLength() / 8;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        // 如果密文长度大于模长则要分组解密
        byte[] result = new byte[0];
        byte[][] arrays = splitArray(original, keyLen);
        for (byte[] arr : arrays) {
            result = ByteUtil.union(Lists.newArrayList(result,
                    cipher.doFinal(arr)));
        }
        return result;
    }
    

    /**
     * 得到密钥字符串（经过base64编码）
     * @return
     */
    public static String getKeyString(java.security.Key key) throws Exception {
          byte[] keyBytes = key.getEncoded();
          String s = (new BASE64Encoder()).encode(keyBytes);
          return s;
    }
    
    public static byte[] decrypt(byte[] original,RSAPrivateKey privateKey) throws NoSuchAlgorithmException,   InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,    BadPaddingException, IOException {
//        RSAPublicKey publicKey = getPublicKey(publicKeyBytes);
        int keyLen = privateKey.getModulus().bitLength() / 8;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 如果密文长度大于模长则要分组解密
        byte[] result = new byte[0];
        byte[][] arrays = splitArray(original, keyLen);
        for (byte[] arr : arrays) {
            result = ByteUtil.union(Lists.newArrayList(result,
                    cipher.doFinal(arr)));
        }
        return result;
    }
    
    public static RSAPublicKey getPublicKeyo(byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(new String(publicKeyBytes)));
         // RSA对称加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
         // 取公钥匙对象
        RSAPublicKey publicKey  = (RSAPublicKey)keyFactory.generatePublic(bobPubKeySpec);
//        System.out.println("publicKey:" + publicKey.getModulus());
        return publicKey;
    }
    
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception{    
        try {    
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);  
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");    
//            KeyFactory keyFactory= KeyFactory.getInstance("RSA/ECB/PKCS1Padding");    
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);    
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);    
            return publicKey;
        } catch (NoSuchAlgorithmException e) {    
            throw new Exception("无此算法");    
        } catch (InvalidKeySpecException e) {    
            throw new Exception("公钥非法");    
        } catch (IOException e) {    
            throw new Exception("公钥数据内容读取错误");    
        } catch (NullPointerException e) {    
            throw new Exception("公钥数据为空");    
        }    
    }    
    public static PublicKey getPublicKey(byte[] bPubKeyInput) {
        PublicKey rsaPubKey = null;
        byte[] bX509PubKeyHeader = { 48, -127, -97, 48, 13, 6, 9, 42, -122, 72,
                -122, -9, 13, 1, 1, 1, 5, 0, 3, -127, -115, 0 };
        try {
            byte[] bPubKey = new byte[bPubKeyInput.length
                    + bX509PubKeyHeader.length];
            System.arraycopy(bX509PubKeyHeader, 0, bPubKey, 0,
                    bX509PubKeyHeader.length);
            System.arraycopy(bPubKeyInput, 0, bPubKey,
                    bX509PubKeyHeader.length, bPubKeyInput.length);

//            X509EncodedKeySpec rsaKeySpec = new X509EncodedKeySpec(bPubKey);
            X509EncodedKeySpec rsaKeySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(new String(bPubKey)));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            rsaPubKey = keyFactory.generatePublic(rsaKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsaPubKey;
    }
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static RSAPrivateKey getPrivateKey(byte[] privateKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
//        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(new String(privateKeyBytes)));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(new String(privateKeyBytes)));
        // RSA对称加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         // 取公钥匙对象
        RSAPrivateKey privateKey  = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    
    /** 
     *拆分数组  
     */  
    public static byte[][] splitArray(byte[] data,int len){  
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if(y!=0){
            z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
            if(i==x+z-1 && y!=0){
                arr = new byte[y];
                System.arraycopy(data, i*len, arr, 0, y);
            }else{
                arr = new byte[len];
                System.arraycopy(data, i*len, arr, 0, len);
            }
            arrays[i] = arr;
        }  
        return arrays;
    }
    
    public static void main(String[] age) throws Exception{
    	String v = "123456";
    	String key = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMXa8rkksFZbb0jHcghcKKVXy6XnY2iaa0RVd/sRl2drh/EvP4kHm+354vDRi3SdKgvYGZMzqINSSYzTdB1qj7MCAwEAAQ==";
    	String enStr = new String(encrypt(v.getBytes(), key.getBytes()));
    	System.out.println(enStr);
    	String deStr = new String(decrypt(enStr.getBytes(), key.getBytes()));
    	System.out.println(deStr);
    }
}
