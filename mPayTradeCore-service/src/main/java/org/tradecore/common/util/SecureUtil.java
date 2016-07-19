/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import org.apache.commons.codec.binary.Base64;

/**
 * 安全工具类
 * @author HuHui
 * @version $Id: SecureUtil.java, v 0.1 2016年7月19日 下午8:42:34 HuHui Exp $
 */
public class SecureUtil {

    private static final String TYPE      = "X.509";

    private static final String ALGORITHM = "SHA1withRSA";

    /**
     * 验签
     * @param cert          证书内容
     * @param paraStr       待验签的字符串，即参数字符串
     * @param oriSign       收单机构传过来的签名内容
     * @return              true表示验签通过，false表示验签失败
     * @throws Exception 
     */
    public static boolean verifySign(byte[] cert, String paraStr, String oriSign) throws Exception {

        byte[] binaryData = Base64.decodeBase64(oriSign.getBytes(StandardCharsets.UTF_8));

        //获取公钥
        PublicKey publicKey = getPublicKey(cert);
        AssertUtil.assertNotNull(publicKey, "获取公钥失败");

        //验签
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(paraStr.getBytes(StandardCharsets.UTF_8));

        return signature.verify(binaryData);
    }

    private static PublicKey getPublicKey(byte[] cert) throws Exception {
        InputStream is = new ByteArrayInputStream(cert);
        CertificateFactory cf = CertificateFactory.getInstance(TYPE);
        Certificate certifecate = cf.generateCertificate(is);
        if (is != null) {
            is.close();
            is = null;
        }
        return certifecate.getPublicKey();
    }

}
