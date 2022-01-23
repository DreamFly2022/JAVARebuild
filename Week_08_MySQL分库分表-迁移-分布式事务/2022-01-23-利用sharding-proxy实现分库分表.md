# 利用sharding-proxy实现分库分表

[toc]

## 一、启动一个mysql

在上面创建两个库：`ds_shop_1`和	`ds_shop_2`，真实的环境中，应当使用两个mysql服务。

```
create schema ds_shop_1;
create schema ds_shop_2;
CREATE USER 'shoper'@'%' IDENTIFIED BY 'shoper^pw';
GRANT ALL ON ds_shop_1.* TO 'shoper'@'%';
GRANT ALL ON ds_shop_2.* TO 'shoper'@'%';
flush privileges;
```

分库分表的表结构：

```
CREATE TABLE ds_shop_1.`t_order_1` (
  `o_id`         bigint(20)  NOT NULL PRIMARY KEY  AUTO_INCREMENT  COMMENT '主键UUID',
  `p_code`       varchar(125) NOT NULL COMMENT '订单编号',
  `P_num`        int(11)      NOT NULL COMMENT '数量',
  `total_price`  decimal(7,2)   NOT NULL COMMENT '总价',
  `create_time`  bigint(20)     NOT NULL COMMENT '创建时间',
  `update_time`  bigint(20)     NOT NULL COMMENT '更新时间',
  `o_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 代付款， 1 已付款， 2 删除'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


CREATE TABLE ds_shop_1.`t_order_2` (
  `o_id`         bigint(20)  NOT NULL PRIMARY KEY  AUTO_INCREMENT  COMMENT '主键UUID',
  `p_code`       varchar(125) NOT NULL COMMENT '订单编号',
  `P_num`        int(11)      NOT NULL COMMENT '数量',
  `total_price`  decimal(7,2)   NOT NULL COMMENT '总价',
  `create_time`  bigint(20)     NOT NULL COMMENT '创建时间',
  `update_time`  bigint(20)     NOT NULL COMMENT '更新时间',
  `o_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 代付款， 1 已付款， 2 删除'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE ds_shop_1.`t_order_3` (
  `o_id`         bigint(20)  NOT NULL PRIMARY KEY  AUTO_INCREMENT  COMMENT '主键UUID',
  `p_code`       varchar(125) NOT NULL COMMENT '订单编号',
  `P_num`        int(11)      NOT NULL COMMENT '数量',
  `total_price`  decimal(7,2)   NOT NULL COMMENT '总价',
  `create_time`  bigint(20)     NOT NULL COMMENT '创建时间',
  `update_time`  bigint(20)     NOT NULL COMMENT '更新时间',
  `o_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 代付款， 1 已付款， 2 删除'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


CREATE TABLE ds_shop_2.`t_order_1` (
  `o_id`         bigint(20)  NOT NULL PRIMARY KEY  AUTO_INCREMENT  COMMENT '主键UUID',
  `p_code`       varchar(125) NOT NULL COMMENT '订单编号',
  `P_num`        int(11)      NOT NULL COMMENT '数量',
  `total_price`  decimal(7,2)   NOT NULL COMMENT '总价',
  `create_time`  bigint(20)     NOT NULL COMMENT '创建时间',
  `update_time`  bigint(20)     NOT NULL COMMENT '更新时间',
  `o_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 代付款， 1 已付款， 2 删除'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


CREATE TABLE ds_shop_2.`t_order_2` (
  `o_id`         bigint(20)  NOT NULL PRIMARY KEY  AUTO_INCREMENT  COMMENT '主键UUID',
  `p_code`       varchar(125) NOT NULL COMMENT '订单编号',
  `P_num`        int(11)      NOT NULL COMMENT '数量',
  `total_price`  decimal(7,2)   NOT NULL COMMENT '总价',
  `create_time`  bigint(20)     NOT NULL COMMENT '创建时间',
  `update_time`  bigint(20)     NOT NULL COMMENT '更新时间',
  `o_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 代付款， 1 已付款， 2 删除'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE ds_shop_2.`t_order_3` (
  `o_id`         bigint(20)  NOT NULL PRIMARY KEY  AUTO_INCREMENT  COMMENT '主键UUID',
  `p_code`       varchar(125) NOT NULL COMMENT '订单编号',
  `P_num`        int(11)      NOT NULL COMMENT '数量',
  `total_price`  decimal(7,2)   NOT NULL COMMENT '总价',
  `create_time`  bigint(20)     NOT NULL COMMENT '创建时间',
  `update_time`  bigint(20)     NOT NULL COMMENT '更新时间',
  `o_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 代付款， 1 已付款， 2 删除'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

## 二、在sharding-proxy上面配置分库、分表的规则

在sharding-proxy上面配置分库分表的规则：

```yaml
schemaName: sharding_db

dataSources:
  ds_shop_1:
    url: jdbc:mysql://127.0.0.1:3307/ds_shop_1?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: shoper
    password: shoper^pw
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    minPoolSize: 1
  ds_shop_2:
    url: jdbc:mysql://127.0.0.1:3307/ds_shop_2?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: shoper
    password: shoper^pw
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    minPoolSize: 1

rules:
- !SHARDING
  tables:
    t_order:
      actualDataNodes: ds_shop_${1..2}.t_order_${1..3}
      tableStrategy:
        standard:
          shardingColumn: o_id
          shardingAlgorithmName: t_order_inline
      keyGenerateStrategy:
        column: o_id
        keyGeneratorName: snowflake
  defaultDatabaseStrategy:
    standard:
      shardingColumn: o_id
      shardingAlgorithmName: database_inline
  defaultTableStrategy:
    none:

  shardingAlgorithms:
    database_inline:
      type: INLINE
      props:
        algorithm-expression: ds_shop_${o_id % 2 + 1}
    t_order_inline:
      type: INLINE
      props:
        algorithm-expression: t_order_${o_id % 3 + 1}
        allow-range-query-with-inline-sharding: true

  keyGenerators:
    snowflake:
      type: SNOWFLAKE
      props:
        worker-id: 123
```

## 三、启动sharding-proxy，并测试

启动sharding-proxy：

> bash bin/start.sh 3308

测试：

上面的配置中，为id配置了雪花算法，插入的时候，如果不写id值，系统会用雪花算法生成一个很大的值。

```
lifeideMacBook-Pro:shardingsphere-proxy lifei$ mysql -h127.0.0.1 -P3308 -uroot -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 1
Server version: 5.7.34-ShardingSphere-Proxy 5.0.0

Copyright (c) 2000, 2019, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show databases;
+-------------+
| schema_name |
+-------------+
| sharding_db |
+-------------+
1 row in set (0.04 sec)

mysql> use sharding_db;
eading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;
+-----------------------+------------+
| Tables_in_sharding_db | Table_type |
+-----------------------+------------+
| t_order               | BASE TABLE |
+-----------------------+------------+
1 row in set (0.00 sec)

mysql> insert into t_order(p_code,P_num,total_price,create_time,update_time,o_status) values ('A01',2,22.3,1642909338,1642909338, 0);
Query OK, 1 row affected (0.19 sec)

mysql> insert into t_order(p_code,P_num,total_price,create_time,update_time,o_status) values ('A02',1,12.3,1642909338,1642909338, 0);
Query OK, 1 row affected (0.02 sec)

mysql> insert into t_order(p_code,P_num,total_price,create_time,update_time,o_status) values ('A02',1,12.3,1642909338,1642909338, 0);
Query OK, 1 row affected (0.01 sec)

mysql> select * from sharding_db.t_order;
+--------------------+--------+-------+-------------+-------------+-------------+----------+
| o_id               | p_code | P_num | total_price | create_time | update_time | o_status |
+--------------------+--------+-------+-------------+-------------+-------------+----------+
| 691975245166784512 | A01    |     2 |       22.30 |  1642909338 |  1642909338 |        0 |
| 691975479078924289 | A02    |     1 |       12.30 |  1642909338 |  1642909338 |        0 |
| 691975708050173952 | A02    |     1 |       12.30 |  1642909338 |  1642909338 |        0 |
+--------------------+--------+-------+-------------+-------------+-------------+----------+
3 rows in set (0.11 sec)
```

查看日志：

```bash
[INFO ] 2022-01-23 11:42:34.845 [ShardingSphere-Command-2] ShardingSphere-SQL - Logic SQL: insert into t_order(p_code,P_num,total_price,create_time,update_time,o_status) values ('A01',2,22.3,1642909338,1642909338, 0)
[INFO ] 2022-01-23 11:42:34.845 [ShardingSphere-Command-2] ShardingSphere-SQL - SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
[INFO ] 2022-01-23 11:42:34.845 [ShardingSphere-Command-2] ShardingSphere-SQL - Actual SQL: ds_shop_1 ::: insert into t_order_2(p_code,P_num,total_price,create_time,update_time,o_status, o_id) values ('A01', 2, 22.3, 1642909338, 1642909338, 0, 691975245166784512)
[INFO ] 2022-01-23 11:43:30.496 [ShardingSphere-Command-2] ShardingSphere-SQL - Logic SQL: insert into t_order(p_code,P_num,total_price,create_time,update_time,o_status) values ('A02',1,12.3,1642909338,1642909338, 0)
[INFO ] 2022-01-23 11:43:30.497 [ShardingSphere-Command-2] ShardingSphere-SQL - SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
[INFO ] 2022-01-23 11:43:30.497 [ShardingSphere-Command-2] ShardingSphere-SQL - Actual SQL: ds_shop_2 ::: insert into t_order_2(p_code,P_num,total_price,create_time,update_time,o_status, o_id) values ('A02', 1, 12.3, 1642909338, 1642909338, 0, 691975479078924289)
[INFO ] 2022-01-23 11:44:25.087 [ShardingSphere-Command-2] ShardingSphere-SQL - Logic SQL: insert into t_order(p_code,P_num,total_price,create_time,update_time,o_status) values ('A02',1,12.3,1642909338,1642909338, 0)

[INFO ] 2022-01-23 11:48:15.803 [ShardingSphere-Command-3] ShardingSphere-SQL - Logic SQL: select * from sharding_db.t_order
[INFO ] 2022-01-23 11:48:15.804 [ShardingSphere-Command-3] ShardingSphere-SQL - SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
[INFO ] 2022-01-23 11:48:15.804 [ShardingSphere-Command-3] ShardingSphere-SQL - Actual SQL: ds_shop_1 ::: select * from t_order_1 ORDER BY o_id ASC
[INFO ] 2022-01-23 11:48:15.804 [ShardingSphere-Command-3] ShardingSphere-SQL - Actual SQL: ds_shop_1 ::: select * from t_order_2 ORDER BY o_id ASC
[INFO ] 2022-01-23 11:48:15.804 [ShardingSphere-Command-3] ShardingSphere-SQL - Actual SQL: ds_shop_1 ::: select * from t_order_3 ORDER BY o_id ASC
[INFO ] 2022-01-23 11:48:15.804 [ShardingSphere-Command-3] ShardingSphere-SQL - Actual SQL: ds_shop_2 ::: select * from t_order_1 ORDER BY o_id ASC
[INFO ] 2022-01-23 11:48:15.804 [ShardingSphere-Command-3] ShardingSphere-SQL - Actual SQL: ds_shop_2 ::: select * from t_order_2 ORDER BY o_id ASC
[INFO ] 2022-01-23 11:48:15.804 [ShardingSphere-Command-3] ShardingSphere-SQL - Actual SQL: ds_shop_2 ::: select * from t_order_3 ORDER BY o_id ASC
```

## 四、将用户表拆成2个库，每个库16张表

### 4.1  表结构

可以先不建立表结构，配置好shardingsphere-prox的配置，通过代理连接mysql，再执行一次这个建表语句，代理mysql会自动帮我们在两个库上分别建立16张表。

```
CREATE TABLE `t_user` (
  `u_id`         bigint(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT  COMMENT '主键，自增',
  `username`     varchar(255) NOT NULL COMMENT '用户名',
  `phone_number` char(11)     NOT NULL COMMENT '电话号码',
  `address`      varchar(255) NOT NULL COMMENT '地址',
  `create_time`  datetime     NOT NULL COMMENT '创建时间',
  `update_time`  datetime     NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 4.2 sharding-sphere-prox的配置：

```yaml
schemaName: sharding_user_db

dataSources:
  ds_shop_1:
    url: jdbc:mysql://127.0.0.1:3307/ds_shop_1?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: shoper
    password: shoper^pw
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    minPoolSize: 1
  ds_shop_2:
    url: jdbc:mysql://127.0.0.1:3307/ds_shop_2?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: shoper
    password: shoper^pw
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    minPoolSize: 1

rules:
- !SHARDING
  tables:
    t_user:
      actualDataNodes: ds_shop_${1..2}.t_user_${1..16}
      tableStrategy:
        standard:
          shardingColumn: u_id
          shardingAlgorithmName: t_user_inline
      keyGenerateStrategy:
        column: u_id
        keyGeneratorName: snowflake
  bindingTables:
    - t_user
  defaultDatabaseStrategy:
    standard:
      shardingColumn: u_id
      shardingAlgorithmName: database_inline
  defaultTableStrategy:
    none:

  shardingAlgorithms:
    database_inline:
      type: INLINE
      props:
        algorithm-expression: ds_shop_${u_id % 2 + 1}
    t_user_inline:
      type: INLINE
      props:
        algorithm-expression: t_user_${u_id % 16 + 1}
        allow-range-query-with-inline-sharding: true

  keyGenerators:
    snowflake:
      type: SNOWFLAKE
      props:
        worker-id: 123
```

## 五、利用sharding-sphere-ui 可以动态的修改配置

（待补充）

