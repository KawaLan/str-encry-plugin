# str-encry-plugin
字符串加密插件


### 此模块是字符串加密模块
<br/>

+ 1.使用之前先将本模块提交到你本地maven仓库
具体步骤：点击Gradle,选择str-encry-plugin模块，之后打开Tasks->upload->点击uploadArchives上传

+ 2.上传之后可以引入项目,引入步骤,在根目录的build.gradle引入
```
//本地仓库
maven { url uri('F:/maven/repo') }


classpath 'com.kawa.plugin:str-encry-plugin:1.0.0'
```

+ 3.使用的模块
```
apply plugin: 'StringEncry'

stringEncry {
    //加密密钥，默认[1,2,3,4,5] 项目里面默认的是异或加密
    decryptKey = [1,2,3,4,5]
    //需要加密指定目录
    stringEncryDirList = [
            "com/kawa/strencrydemo/url"
    ]
    //需要加密指定文件
    stringEncryList = [
            "TestStr",
    ]
    //解密路径
    decryptFile = "com/kawa/strencrydemo/XorUtils"
    //解密方法
    decryptMethod = "xor_go"
}
```

上面用到的加密都是简简单单的异或加密
JNI版：
```
int key[] = {1, 2, 3, 4, 5};//加密字符密钥

//异或加密
void xor_go(char *pstr, int *pkey) {
    int len = strlen(pstr);//获取长度
    for (int i = 0; i < len; i++) {
        *(pstr + i) = ((*(pstr + i)) ^ (pkey[i % 5]));
    }
}

/**
 * 执行异或加密
 */
JNIEXPORT jstring JNICALL str_go
        (JNIEnv *env, jclass, jstring str) {
    const char *buf_name = env->GetStringUTFChars(str, 0);
    if (buf_name == NULL) {
        return NULL;
    }
    int len = strlen(buf_name);
    char value[len];
    strcpy(value, buf_name);
    //2. 释放内存
    env->ReleaseStringUTFChars(str, buf_name);
    char *p = value;
    xor_go(value, key);
    return env->NewStringUTF(p);
}

```

java版：
```
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
```
如需自定义加密方法，请修改NativeStringGuard这个文件

修改class文件代码是参考了四哥的 http://www.520monkey.com/archives/1313  此插件的目的是为了更方便的加密字符串
