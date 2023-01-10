题目名称
数字壳的传说

题目链接
链接:https://pan.baidu.com/s/1b4KY-fcL1OAjI-ysiYemGA 密码:depm

题目描述
如此简单的壳，相信你一定可以！

题目Flag
n1book{h3ckF0rfun}

Writeup
关注Nu1L Team公众号，回复4-apk-wp获取WP

### 参考：

1. 安装frida， frida tools， frida-dexdump
2. root设备， 下载frida-server并部署到设备内部启动 （https://github.com/frida/frida/releases）
3. (frida 非root方式使用)[https://nszdhd1.github.io/2021/06/15/%E9%9D%9Eroot%E7%8E%AF%E5%A2%83%E4%B8%8Bfrida%E7%9A%84%E4%B8%A4%E7%A7%8D%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F/]
4. python3.7 LIEFInjectFrida.py apkfile  outdir  libjiagu.so  -apksign 
5. (Android APK脱壳--腾讯乐固、360加固一键脱壳)[https://www.jianshu.com/p/138c9de2c987]
   
### Issues: 
1. 题目里的apk在使用LIEFInjectFrida尝试注入frida gadget so的时候会闪退， 目前不知道原因（360加固的关系？？？）
2. 在越狱中的手机中尝试脱壳失败，解析出来的dex无法打开。尝试使用模拟器进行脱壳成功

