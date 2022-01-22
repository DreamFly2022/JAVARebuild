# 在Linux系统上安装Python3

[toc]

## 一、第一步：先安装openssl

下载：[openssl-OpenSSL_1_1_1.tar.gz](https://github.com/openssl/openssl/releases/tag/OpenSSL_1_1_1)

```shell
tar -zxvf softs/openssl-OpenSSL_1_1_1.tar.gz -C servers/
sudo mkdir -p /usr/local/openssl
cd servers/openssl-OpenSSL_1_1_1
sudo ./config --prefix=/usr/local/openssl shared zlib
sudo make && sudo make install
```

参考：[centos 解决python3.7 安装时No module named _ssl](https://www.jianshu.com/p/3ec24f563b81)。

## 二、第二步：安装python3

```shell
tar -zxvf softs/Python-3.9.1.tgz -C servers/
sudo mkdir -p /usr/local/python3
```

修改`Modules/Setup`：

```shell
cd servers/Python-3.9.1
vi Modules/Setup

SSL=/usr/local/openssl
_ssl _ssl.c \
       -DUSE_SSL -I$(SSL)/include -I$(SSL)/include/openssl \
       -L$(SSL)/lib -lssl -lcrypto
   
sudo ln -s /usr/local/openssl/lib/libssl.so.1.1 /usr/lib64/libssl.so.1.1
sudo ln -s /usr/local/openssl/lib/libcrypto.so.1.1 /usr/lib64/libcrypto.so.1.1
```

执行python安装命令：

```shell
sudo ./configure --prefix=/usr/local/python3 --with-openssl=/usr/local/openssl

sudo make && sudo make install
```

然后执行下面命令：

```
sudo rm -rf /usr/bin/python3
sudo rm -rf /usr/bin/pip3
sudo ln -s /usr/local/python3/bin/python3.9 /usr/bin/python3
sudo ln -s /usr/local/python3/bin/pip3.9 /usr/bin/pip3
```

## 三、第三步：验证安装情况

```shell
$ python3
>>> import _ssl
>>>
```





