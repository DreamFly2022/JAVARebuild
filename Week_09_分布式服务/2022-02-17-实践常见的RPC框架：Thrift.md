# 实践常见的RPC框架：Thrift

[toc]

## 一、Thrift介绍

维基百科上对[Thrift的介绍](https://zh.wikipedia.org/wiki/Thrift)如下：

> **Thrift**是一种[接口描述语言](https://zh.wikipedia.org/wiki/接口描述语言)和二进制通讯协议，[[1\]](https://zh.wikipedia.org/wiki/Thrift#cite_note-1)它被用来定义和创建跨语言的服务。[[2\]](https://zh.wikipedia.org/wiki/Thrift#cite_note-2)它被当作一个[远程过程调用](https://zh.wikipedia.org/wiki/远程过程调用)（RPC）框架来使用，是由[Facebook](https://zh.wikipedia.org/wiki/Facebook)为“大规模跨语言服务开发”而开发的。它通过一个代码生成引擎联合了一个软件栈，来创建不同程度的、无缝的[跨平台](https://zh.wikipedia.org/wiki/跨平台)高效服务，可以使用[C#](https://zh.wikipedia.org/wiki/C♯)、[C++](https://zh.wikipedia.org/wiki/C%2B%2B)（基于[POSIX](https://zh.wikipedia.org/wiki/POSIX)兼容系统[[3\]](https://zh.wikipedia.org/wiki/Thrift#cite_note-3)）、Cappuccino、[[4\]](https://zh.wikipedia.org/wiki/Thrift#cite_note-4)[Cocoa](https://zh.wikipedia.org/wiki/Cocoa)、[Delphi](https://zh.wikipedia.org/wiki/Delphi)、[Erlang](https://zh.wikipedia.org/wiki/Erlang)、[Go](https://zh.wikipedia.org/wiki/Go)、[Haskell](https://zh.wikipedia.org/wiki/Haskell)、[Java](https://zh.wikipedia.org/wiki/Java)、[Node.js](https://zh.wikipedia.org/wiki/Node.js)、[OCaml](https://zh.wikipedia.org/wiki/OCaml)、[Perl](https://zh.wikipedia.org/wiki/Perl)、[PHP](https://zh.wikipedia.org/wiki/PHP)、[Python](https://zh.wikipedia.org/wiki/Python)、[Ruby](https://zh.wikipedia.org/wiki/Ruby)和[Smalltalk](https://zh.wikipedia.org/wiki/Smalltalk)。[[5\]](https://zh.wikipedia.org/wiki/Thrift#cite_note-5)虽然它以前是由Facebook开发的，但它现在是[Apache软件基金会](https://zh.wikipedia.org/wiki/Apache软件基金会)的[开源](https://zh.wikipedia.org/wiki/开源)项目了。该实现被描述在2007年4月的一篇由Facebook发表的技术论文中，该论文现由Apache掌管。[[6\]](https://zh.wikipedia.org/wiki/Thrift#cite_note-6)

需要安装thrift编译器，将Thrift的接口定义文件编译成对应的技术栈的代码。

## 二、Thrift使用示例

参考：

- [由浅入深了解Thrift](https://blog.csdn.net/houjixin/article/details/42778335)
- [Thrift入门](https://juejin.cn/post/6844903622380093447)

- [thrift单端口多服务](https://www.cnblogs.com/luckygxf/p/9393618.html)

### 2.1 安装Thrift编译器

macbook下：

> brew install thrift

可以在IDEA中添加Thrift support插件。

### 2.2 编写Thrift的IDL文件

参考官方的IDL文件示例：https://git-wip-us.apache.org/repos/asf?p=thrift.git;a=blob_plain;f=test/ThriftTest.thrift;hb=HEAD

`myrpc.thrift`内容如下：

```
namespace java com.hef.demo.thrift.api

struct User {
  1: i32 id,
  2: string name
}

service UserService {
   User findUser(1 : i32 id)
}

struct Order {
  1: i32 id,
  2: string name,
  3: double amount
}

service OrderService {
   Order findOrder(1 : i32 id)
}
```

运行thrift命令编译上面的文件： `thrift -r --gen java myrpc.thrift` , 之后就产生了四个java文件：User、UserService、Order、OrderService。

### 2.3 通过Thrift实现RPC功能

[action-rpcfx-demo](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo)项目中三个模块 ：[ `demo-thrift-api`](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo/demo-thrift-api)、[`demo-thrift-client`](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo/demo-thrift-client)、[`demo-thrift-server`](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo/demo-thrift-server)。

#### (1) demo-thrift-api

User、UserService、Order、OrderService 四个java文件都是通过Thirft命令生成的。

> thrift -r --gen java myrpc.thrift

#### (2) demo-thrift-server 服务端代码

- UserServiceImpl 和 OrderServiceImpl是服务接口的具体实现
- [DemoThriftServerApplication]()  注册服务：

```java
    public static void main(String[] args) {
        try {
            TServerSocket tServerSocket = new TServerSocket(ServerConfig.SERVER_PORT);

            // 只注册一个服务
//            UserService.Processor<UserService.Iface> processor = new UserService.Processor<>(new UserServiceImpl());
            // 注册多个服务
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            processor.registerProcessor("userService", new UserService.Processor<>(new UserServiceImpl()));
            processor.registerProcessor("orderService", new OrderService.Processor<>(new OrderServiceImpl()));

            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(tServerSocket);
            tArgs.processor(processor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            tArgs.transportFactory(new TTransportFactory());

            TSimpleServer server = new TSimpleServer(tArgs);
            System.out.println("Running Simple server");
            server.serve();

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }
```

#### (3) demo-thrift-client  客户端代码

- DemoThriftClientApplication 调用服务

```java
    public static void main(String[] args) {
        TTransport transport = null;
        try {
            transport = new TSocket(new TConfiguration(), ServerConfig.SERVER_HOST,
                    ServerConfig.SERVER_PORT, ServerConfig.TIMEOUT);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            // 当注册了一个服务当时候，获取方法
            /*UserService.Client userService = new UserService.Client(protocol);
            User user = userService.findUser(2);
            System.out.println(user);*/

            // 当注册多个服务当时候，获取方法
            UserService.Client userService = new UserService.Client(new TMultiplexedProtocol(protocol, "userService"));
            User user = userService.findUser(2);
            System.out.println(user);

            OrderService.Client orderService = new OrderService.Client(new TMultiplexedProtocol(protocol, "orderService"));
            Order order = orderService.findOrder(3);
            System.out.println(order);


        } catch (TException e) {
            throw new RuntimeException(e);
        } finally {
            if (transport!=null) {
                transport.close();
            }
        }
    }
```







