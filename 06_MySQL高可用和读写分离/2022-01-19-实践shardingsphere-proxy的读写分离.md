# 实践shardingsphere-proxy实现读写分离

[toc]

## 一、shardingsphere-proxy 介绍

shardingsphere-proxy是一个中间件，可直接当做 MySQL/PostgreSQL 使用。

## 二、使用shardingsphere-proxy

### 2.1 下载

[shardingsphere-proxy二进制包下载](https://www.apache.org/dyn/closer.cgi/shardingsphere/5.0.0/apache-shardingsphere-5.0.0-shardingsphere-proxy-bin.tar.gz)。

### 2.2 添加mysql驱动

参考：[规则配置](https://shardingsphere.apache.org/document/current/cn/quick-start/shardingsphere-proxy-quick-start/#%E8%A7%84%E5%88%99%E9%85%8D%E7%BD%AE)

因为后端使用mysql，所以需要下载[mysql-connector-java-5.1.47.jar](https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.47/mysql-connector-java-5.1.47.jar) 驱动放到`%SHARDINGSPHERE_PROXY_HOME%/lib` 目录目录。

> %SHARDINGSPHERE_PROXY_HOME% 为 Proxy 解压后的路径，例：/opt/shardingsphere-proxy-bin/

### 2.3 配置`config-xxx.yaml`和`server.yaml`

参考：[读写分离配置](https://shardingsphere.apache.org/document/legacy/4.x/document/cn/manual/sharding-proxy/configuration/#%E8%AF%BB%E5%86%99%E5%88%86%E7%A6%BB)。

一定要配置`server.yaml`：

users下面配置的root和sharding用户，将作为登陆sharding-proxy的用户。

```yaml
rules:
  - !AUTHORITY
    users:
      - root@%:root
      - sharding@:sharding
    provider:
      type: ALL_PRIVILEGES_PERMITTED

props:
  sql-show: true
```

配置`config-readwrite-splitting-performance.yaml`:

配置读写分离

```yaml
schemaName: readwrite_splitting_db

dataSources:
  write_ds:
    url: jdbc:mysql://192.168.0.104:3316/performance?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: performance
    password: performance^pw
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    minPoolSize: 1
  read_ds_0:
    url: jdbc:mysql://192.168.0.104:3326/performance?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: performance
    password: performance^pw
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    minPoolSize: 1
  read_ds_1:
    url: jdbc:mysql://192.168.0.104:3336/performance?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: performance
    password: performance^pw
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    minPoolSize: 1

rules:
- !READWRITE_SPLITTING
  dataSources:
    pr_ds:
      writeDataSourceName: write_ds
      readDataSourceNames:
        - read_ds_0
        - read_ds_1
```

然后就是启动：

```mysql
bash bin/start.sh


$ mysql -h192.168.0.104 -P3307 -uroot -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 1
Server version: 5.7.34-log-ShardingSphere-Proxy 5.0.0

Copyright (c) 2000, 2019, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show databases;
+------------------------+
| schema_name            |
+------------------------+
| readwrite_splitting_db |
+------------------------+
1 row in set (0.05 sec)

mysql> select * from t_model_info;
+------+--------------+--------------+--------------+
| m_id | model_type   | model_name   | model_status |
+------+--------------+--------------+--------------+
|    1 | dynamic-type | 动态类型     |            0 |
+------+--------------+--------------+--------------+
1 row in set (0.06 sec)

mysql> insert into t_model_info(model_type,model_name, model_status) values('static-type', '静态模型', 0);
Query OK, 1 row affected (0.06 sec)

mysql> select * from t_model_info;
+------+--------------+--------------+--------------+
| m_id | model_type   | model_name   | model_status |
+------+--------------+--------------+--------------+
|    1 | dynamic-type | 动态类型     |            0 |
|    2 | static-type  | 静态模型     |            0 |
+------+--------------+--------------+--------------+
2 rows in set (0.02 sec)
```

通过日志，可以看出来，已经显示了读写分离：

```txt
[INFO ] 2022-01-20 21:27:51.577 [ShardingSphere-Command-0] ShardingSphere-SQL - Actual SQL: read_ds_0 ::: select * from t_model_info
[INFO ] 2022-01-20 21:29:34.489 [ShardingSphere-Command-1] ShardingSphere-SQL - Logic SQL: insert into t_model_info(model_type,model_name, model_status) values('static-type', '静态模型', 0)
[INFO ] 2022-01-20 21:29:34.489 [ShardingSphere-Command-1] ShardingSphere-SQL - SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
[INFO ] 2022-01-20 21:29:34.490 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: write_ds ::: insert into t_model_info(model_type,model_name, model_status) values('static-type', '静态模型', 0)
[INFO ] 2022-01-20 21:29:49.662 [ShardingSphere-Command-1] ShardingSphere-SQL - Logic SQL: select * from t_model_info
[INFO ] 2022-01-20 21:29:49.662 [ShardingSphere-Command-1] ShardingSphere-SQL - SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
[INFO ] 2022-01-20 21:29:49.662 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: read_ds_1 ::: select * from t_model_info
```







