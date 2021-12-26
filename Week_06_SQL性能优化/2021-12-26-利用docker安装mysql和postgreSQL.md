# 利用docker 安装mysql、postgreSQL

[toc]

## 一、启动mysql

```
sudo docker container run -p 3307:3306 --name ds-stop-mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
```

连接mysql：

```
mysql -h127.0.0.1 -P3307 -uroot -p
```

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

CREATE TABLE ds_shop.`t_product` (
  `p_id`         bigint(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT  COMMENT '主键，自增',
  `p_name`       varchar(255) NOT NULL COMMENT '产品名称',
  `p_code`       varchar(125) NOT NULL UNIQUE COMMENT '商品编号',
  `p_price`      decimal(7,2)  NOT NULL COMMENT '价格',
  `create_time`  datetime     NOT NULL COMMENT '创建时间',
  `update_time`  datetime     NOT NULL COMMENT '更新时间',
  `p_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 启用， 1 删除'  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE ds_shop.`t_order` (
  `o_id`         char(36)     NOT NULL PRIMARY KEY   COMMENT '主键UUID',
  `p_code`       varchar(125) NOT NULL COMMENT '订单编号',
  `P_num`        int(11)      NOT NULL COMMENT '数量',
  `total_price`  decimal(7,2)   NOT NULL COMMENT '总价',
  `create_time`  bigint(20)     NOT NULL COMMENT '创建时间',
  `update_time`  bigint(20)     NOT NULL COMMENT '更新时间',
  `o_status`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '0 代付款， 1 已付款， 2 删除'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

测试增删查改：

```mysql
--- 插入用户数据
insert into t_user(username, phone_number, address, create_time, update_time) 
values
('aa', '18822229999', '天津市西青区', '2021-12-26 16:22:30', '2021-12-26 16:22:30'),
('bb', '18821129999', '天津市南开区', '2021-12-26 16:22:30', '2021-12-26 16:22:30'),
('cc', '18822266999', '天津市北辰区', '2021-12-26 16:22:30', '2021-12-26 16:22:30');
-- 查询用户数据
select * from t_user;
-- 删除一条用户数据
delete from t_user where username='cc';
-- 修改用户数据
update t_user set address='天津市武清区', update_time='2021-12-26 16:25:03' where u_id=2;


```



## 二、启动postgreSQL

```
sudo docker container run -p 5433:5432 --name ds-stop-postgres -e POSTGRES_PASSWORD=postgres -d postgres:latest
```

连接postgres:

```
psql -h 127.0.0.1 -p 5433 -U postgres -W
```

常用语法：

```sql
-- 查询已存在的数据库：
\l
-- 创建数据库
create database ds_shop;
-- 进入数据库
\c ds_shop

-- 查看表的列表
\d

-- 查看表结构
\d t_user

-- 创建表
CREATE TABLE t_user (
  u_id         serial  NOT NULL PRIMARY KEY,
  username     varchar(255) NOT NULL,
  phone_number char(11)     NOT NULL,
  address      varchar(255) NOT NULL,
  create_time  timestamp     NOT NULL,
  update_time  timestamp     NOT NULL
);


CREATE TABLE t_product (
  p_id         serial   NOT NULL PRIMARY KEY,
  p_name       varchar(255) NOT NULL,
  p_code       varchar(125) NOT NULL UNIQUE,
  p_price      decimal(7,2)  NOT NULL,
  create_time  timestamp     NOT NULL,
  update_time  timestamp     NOT NULL,
  p_status     smallint   NOT NULL DEFAULT 0 
);

CREATE TABLE t_order (
  o_id         char(36)     NOT NULL PRIMARY KEY,
  p_code       varchar(125) NOT NULL,
  P_num        int      NOT NULL,
  total_price  decimal(7,2)   NOT NULL,
  create_time  bigint     NOT NULL,
  update_time  bigint     NOT NULL,
  o_status     smallint   NOT NULL DEFAULT 0
);
```

测试增删查改：

> psql -h 127.0.0.1 -p 5433 -U postgres -W -d ds_shop

```


--- 插入用户数据
insert into t_user(username, phone_number, address, create_time, update_time) 
values
('aa', '18822229999', '天津市西青区', '2021-12-26 16:22:30', '2021-12-26 16:22:30'),
('bb', '18821129999', '天津市南开区', '2021-12-26 16:22:30', '2021-12-26 16:22:30'),
('cc', '18822266999', '天津市北辰区', '2021-12-26 16:22:30', '2021-12-26 16:22:30');
-- 查询用户数据
select * from t_user;
-- 删除一条用户数据
delete from t_user where username='cc';
-- 修改用户数据
update t_user set address='天津市武清区', update_time='2021-12-26 16:25:03' where u_id=2;

```

