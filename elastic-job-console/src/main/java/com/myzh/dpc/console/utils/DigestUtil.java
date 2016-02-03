package com.myzh.dpc.console.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

public class DigestUtil {
    /**
     * 生成SHA256
     *
     * @param className 类名, 如street, merchant, post等
     * @return SHA256
     */
    public static String sha256(String className) {
        return DigestUtils.sha256Hex(className + System.currentTimeMillis() + RandomStringUtils.random(6));
    }


    public static String sha1(String s) {
        return DigestUtils.sha1Hex(s);
    }
    public static String sha1(String s, String salt) {
        return DigestUtils.sha1Hex(s + salt);
    }
    public static String md5(String s) {
        return DigestUtils.md5Hex(s);
    }
}
