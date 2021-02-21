# 第一周作业内容

[toc]

## 第01次作业

### （1）自己写一个简单的Hello.java，里面需要涉及基本类型，四则运算，if和for，然后自己分析一下字节码，有问题群里讨论。

```
javac MyHello.java
java MyHello
javap -c MyHello
```

### （2）自定义一个ClassLoader，加载一个Hello.xlass文件，执行hello方法，此文件内容是一个Hello.class 文件所有字节码（x=255-x）处理后的文件，文件群里提供。

```
继承ClassLoader，重写 findClass方法
```

### （3）画一张图，展示Xmx、Xms、Xmn、Meta、DirectMemory、Xss这些内存参数的关系。

[JVM内存参数关系图.png](https://github.com/hefrankeleyn/JAVA-000/blob/main/Week_01/JVM%E5%86%85%E5%AD%98%E5%8F%82%E6%95%B0%E5%85%B3%E7%B3%BB%E5%9B%BE.png)

- Xmn 设置新生代的大小
- Xss 为每个线程栈的字节数

### （4）检查一下自己维护的业务系统的JVM参数配置，用jstat和jstack、jmap查看一下详情，并自己独立分析一下大概情况，思考有没有不合理的地方，如何改进。

> 如果没有线上系统，可以自己run一个web/java项目。



