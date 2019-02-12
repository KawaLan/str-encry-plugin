package com.kawa.strplugin.strproguard

/**
 * 用于build.gradle中的参数传递
 */
public class SettingParams {

    public Iterable<Integer> decryptKey
    /**
     * 解密文件路径
     */
    public String decryptFile
    /**
     * 解密方法
     */
    public String decryptMethod

    /**
     * 需要加密字符串的目录
     */
    public Iterable<String> stringEncryDirList

    /**
     * 需要手动添加的包
     */
    public Iterable<String> stringEncryList

    public SettingParams() {
        decryptKey = []
        stringEncryDirList = []
        stringEncryList = []
        decryptFile = null
        decryptMethod = null
    }

    Iterable<Integer> getDecryptKey(){
        return decryptKey;
    }

    String getDecryptFile() {
        return decryptFile
    }

    String getDecryptMethod() {
        return decryptMethod
    }

    Iterable<String> getStringEncryList() {
        return stringEncryList
    }

    Iterable<String> getStringEncryDirList() {
        return stringEncryDirList
    }
}