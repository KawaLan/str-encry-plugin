package com.kawa.strplugin;

public class NativeStringGuard {

    /**
     *
     * @param str
     * @return
     */
    public static String encryptStr(String str) {
        return XorUtils.xor_go(str);
    }

    /**
     *
     *
     * @param str
     * @return
     */
    public static String decryptStr(String str) {
        return XorUtils.xor_go(str);
    }

}
