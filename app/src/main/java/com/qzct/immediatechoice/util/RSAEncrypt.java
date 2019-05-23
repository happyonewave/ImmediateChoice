package com.qzct.immediatechoice.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncrypt {
    private static String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJhZhlkzN5L1P8dBFM8lx56Z42wZqU1KPL3zFMLMPb7FczGPvNRbY8gj/W+zM+/ejfsQvuk27bjg7qq+57S6cUUCAwEAAQ==";
    private static String privateKey = "MIIBVwIBADANBgkqhkiG9w0BAQEFAASCAUEwggE9AgEAAkEAmFmGWTM3kvU/x0EUzyXHnpnjbBmpTUo8vfMUwsw9vsVzMY+81FtjyCP9b7Mz796N+xC+6TbtuODuqr7ntLpxRQIDAQABAkEAjxASjxT/bOsMlynQRq2thIvx+gMm7sN1wtHPfxWYLdl3RjNg1QuWUNRD4Qo5fdGVQn6YhTV4MU2BB1qoHOvVyQIhAO6MgkN1AOkYHA4AvIJCrYKu6x5Qq+iAO6d+IGCJ4fy/AiEAo36yaaN0jidQWpEbRdSUfb+waMwy8bEwPNRd+IGG3vsCIQDbcWZjp76urv8f4o2HRs9W6JJh0NRTjaNS8TPXmv/JAQIhAIJx9uWELd2xjlIETo9DkgpWo+ipa8gcyFDwW92HctV7AiEAyWjC6kfo+Qb/nGxqalE+Rfhf8OEvPp3HUKjTXLDCnDs=";

    public static String encrypt(String str) throws Exception {
        return encrypt(str, publicKey);
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey.getBytes(StandardCharsets.UTF_8));
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = new String(Base64.encodeBase64(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8))));
        return outStr;
    }

    public static String decrypt(String str) throws Exception {
        return decrypt(str, privateKey);
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey.getBytes(StandardCharsets.UTF_8));
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}