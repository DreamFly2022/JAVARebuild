# 利用shardingsphere-scaling实现数据库迁移

[toc]

## 一、演示将一个2*6的表数据，迁移到一个主从复制的集群

### 1.1 t_user 被分成2个库，每个库6张表

[利用sharding-proxy实现分库分表](https://github.com/hefrankeleyn/JAVARebuild/blob/main/Week_08_MySQL%E5%88%86%E5%BA%93%E5%88%86%E8%A1%A8-%E8%BF%81%E7%A7%BB-%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/2022-01-23-%E5%88%A9%E7%94%A8sharding-proxy%E5%AE%9E%E7%8E%B0%E5%88%86%E5%BA%93%E5%88%86%E8%A1%A8.md)。

```mysql
$ mysql -h127.0.0.1 -P3308 -uroot -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 2
Server version: 5.7.34-ShardingSphere-Proxy 5.0.0

Copyright (c) 2000, 2019, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> use sharding_user_db;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed

mysql> select * from t_user;


```

实际执行的语句为：

```
Logic SQL: select * from t_user
SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
Actual SQL: ds_shop_1 ::: select * from t_user_1 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_2 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_3 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_4 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_5 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_6 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_7 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_8 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_9 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_10 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_11 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_12 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_13 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_14 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_15 ORDER BY u_id ASC
Actual SQL: ds_shop_1 ::: select * from t_user_16 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_1 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_2 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_3 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_4 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_5 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_6 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_7 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_8 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_9 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_10 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_11 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_12 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_13 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_14 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_15 ORDER BY u_id ASC
Actual SQL: ds_shop_2 ::: select * from t_user_16 ORDER BY u_id ASC
```

### 1.2 准备一个主从复制的集群

[mysql半同步复制搭建_一主两从](https://github.com/hefrankeleyn/JAVARebuild/blob/main/Week_07_MySQL%E9%AB%98%E5%8F%AF%E7%94%A8%E5%92%8C%E8%AF%BB%E5%86%99%E5%88%86%E7%A6%BB/2022-01-15-mysql%E5%8D%8A%E5%90%8C%E6%AD%A5%E5%A4%8D%E5%88%B6%E6%90%AD%E5%BB%BA_%E4%B8%80%E4%B8%BB%E4%B8%A4%E4%BB%8E.md) 。

在主节点mysql-1上创建要复制的库和表：

```mysql
create database ds_shop;
CREATE TABLE ds_shop.`t_user` (
  `u_id`         bigint(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT  COMMENT '主键，自增',
  `username`     varchar(255) NOT NULL COMMENT '用户名',
  `phone_number` char(11)     NOT NULL COMMENT '电话号码',
  `address`      varchar(255) NOT NULL COMMENT '地址',
  `create_time`  datetime     NOT NULL COMMENT '创建时间',
  `update_time`  datetime     NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


GRANT ALL ON ds_shop.* TO 'performance'@'%';
flush privileges;

GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'shoper'@'%';
```

### 1.3  启动shardingsphere-scaling-bin

在本地启动一个zookeeper：

```
sudo docker pull zookeeper
docker run -d -p 2181:2181 --name shop-zookeeper  zookeeper:latest
```

配置`shardingsphere-scaling-bin/conf/server.yaml`：

```
scaling:
  port: 8888
  blockQueueSize: 10000
  workerThread: 30

governance:
  name: governance_ds
  registryCenter:
    type: ZooKeeper
    serverLists: 127.0.0.1:2181
```

启动`shardingsphere-scaling-bin`:

```
bash bin/server_start.sh

ShardingSphere-Scaling is server on http://127.0.0.1:8888/

-- 验证
curl -X GET http://127.0.0.1:8888/scaling/job/list
```

### 1.4 编写同步数据的配置文件

[scaling.txt](https://github.com/hefrankeleyn/JAVARebuild/blob/main/Week_08_MySQL%E5%88%86%E5%BA%93%E5%88%86%E8%A1%A8-%E8%BF%81%E7%A7%BB-%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/scaling.txt)

### 1.5 执行复制命令

```bash
bash scaling.txt
```

### 1.6 报了下面的错误

报了错，暂时不知道如何解决。

```
[ERROR] 21:47:33.964 [nioEventLoopGroup-3-2] o.a.s.scaling.web.HttpServerHandler - Http request handle occur error:
java.lang.NullPointerException: null
	at org.apache.shardingsphere.scaling.core.utils.SyncConfigurationUtil.createDumperConfiguration(SyncConfigurationUtil.java:174)
	at org.apache.shardingsphere.scaling.core.utils.SyncConfigurationUtil.toSyncConfigurations(SyncConfigurationUtil.java:74)
	at org.apache.shardingsphere.scaling.web.HttpServerHandler.startJob(HttpServerHandler.java:98)
	at org.apache.shardingsphere.scaling.web.HttpServerHandler.channelRead0(HttpServerHandler.java:73)
	at org.apache.shardingsphere.scaling.web.HttpServerHandler.channelRead0(HttpServerHandler.java:52)
	at io.netty.channel.SimpleChannelInboundHandler.channelRead(SimpleChannelInboundHandler.java:99)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:355)
	at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:102)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:355)
	at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436)
	at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:321)
	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:295)
	at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:355)
	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:163)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)
```



