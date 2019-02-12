package com.kawa.strencrydemo;

import java.nio.charset.Charset;

/****
 * <pre>
 *  Project_Name:    Bookkeeping
 *  Created:         Kawa on 2018/12/29 13:49.
 *  E-mail:          958129971@qq.com
 *  Desc:            异或加密解密
 * </pre> 
 ****/
public class XorUtils {

    public static byte[] keyBytes = {1, 2, 3, 4, 5};

    /**
     * 异或加密解密
     *
     * @param enc
     * @return
     */
    public static String xor_go(String enc) {
        byte[] b = enc.getBytes(Charset.forName("UTF-8"));
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (b[i] ^ (keyBytes[i % keyBytes.length]));
        }
        return new String(b);
    }
}
