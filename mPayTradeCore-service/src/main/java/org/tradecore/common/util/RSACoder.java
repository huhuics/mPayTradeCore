/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author HuHui
 * @version $Id: RSACoder.java, v 0.1 2016年7月20日 下午2:56:53 HuHui Exp $
 */
public class RSACoder {

    //数字签名，密钥算法  
    public static final String  KEY_ALGORITHM       = "RSA";

    /** 
     * 数字签名 
     * 签名/验证算法 
     * */
    public static final String  SIGNATURE_ALGORITHM = "SHA1withRSA";

    /** 
     * RSA密钥长度，RSA算法的默认密钥长度是1024 
     * 密钥长度必须是64的倍数，在512到65536位之间 
     * */
    private static final int    KEY_SIZE            = 512;

    //公钥  
    private static final String PUBLIC_KEY          = "RSAPublicKey";

    //私钥  
    private static final String PRIVATE_KEY         = "RSAPrivateKey";

    /** 
     * 初始化密钥对 
     * @return Map 甲方密钥的Map 
     * */
    public static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器  
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器  
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对  
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //甲方公钥  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //甲方私钥  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中  
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }

    /** 
     * 签名 
     * @param data待签名数据 
     * @param privateKey 密钥 
     * @return byte[] 数字签名 
     * */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {

        //取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥  
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //实例化Signature  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        //初始化Signature  
        signature.initSign(priKey);
        //更新  
        signature.update(data);
        return signature.sign();
    }

    /** 
     * 校验数字签名 
     * @param data 待校验数据 
     * @param publicKey 公钥 
     * @param sign 数字签名 
     * @return boolean 校验成功返回true，失败返回false 
     * */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        //转换公钥材料  
        //实例化密钥工厂  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥  
        //密钥材料转换  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        //产生公钥  
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //实例化Signature  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        //初始化Signature  
        signature.initVerify(pubKey);
        //更新  
        signature.update(data);

        //验证  
        return signature.verify(sign);
    }

    /** 
     * 取得私钥 
     * @param keyMap 密钥map 
     * @return byte[] 私钥 
     * */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /** 
     * 取得公钥 
     * @param keyMap 密钥map 
     * @return byte[] 公钥 
     * */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /** 
     * @param args 
     * @throws Exception  
     */
    public static void main(String[] args) throws Exception {
        //初始化密钥  
        //生成密钥对  
        Map<String, Object> keyMap = RSACoder.initKey();
        //公钥  
        byte[] publicKey = RSACoder.getPublicKey(keyMap);

        //私钥  
        byte[] privateKey = RSACoder.getPrivateKey(keyMap);
        System.out.println("公钥：" + Base64.encodeBase64String(publicKey));
        System.out.println("私钥：" + Base64.encodeBase64String(privateKey));

        /* String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAleZyc1NpZSeJvm4wxAjZUICr+sI+gWieUYcFWENq1EcYsSRnbP8+JPVHGuYF1HEBvnQug1gZQ684KJ44mpTgHwIDAQABAkBUaU3f5YO/Q7GMe+6YJceCTsMJ1WJvayNkE52N44EAAhkfmbpmhwdcRgo0CnzAsiXdPeB1inynbnv1ornu/AlZAiEA/iTqVvxeYFjaYfvi38OxfNNeqpBMiPjv3XlYzEs4vR0CIQCW/qm+3Lv9YpLlqWGipBBrHGfuyv4spXxiY/mkbh4ZawIhAO14JvOSqsjSHXSS/WHipFSj2H/9h1YxbUf/3AZAf0rNAiA88cTpuIZYG3VXJSq3Tqkh0nFQvLYipxixTdDxQVD8yQIhAIgXKKDfKeEXdmx3untvAo1zh3//MhVoo3JygBsRgSYn";
         String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJXmcnNTaWUnib5uMMQI2VCAq/rCPoFonlGHBVhDatRHGLEkZ2z/PiT1RxrmBdRxAb50LoNYGUOvOCieOJqU4B8CAwEAAQ==";*/

        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
        String str = "RSA数字签名算法";
        System.out.println("原文:" + str);
        //甲方进行数据的加密  
        byte[] sign = RSACoder.sign(str.getBytes(), privateKey);
        System.out.println("产生签名：" + Base64.encodeBase64String(sign));
        //验证签名  
        boolean status = RSACoder.verify(str.getBytes(), publicKey, sign);
        System.out.println("状态：" + status);

    }
}
