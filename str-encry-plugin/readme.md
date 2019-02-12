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
    //加密密钥，默认[1,2,3,4,5]
    decryptKey = [1,2,6,4,5]
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
