# 第六周作业

[toc]

##2.2 基于电商交易场景，提交DDL

基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交DDL的SQL文件到Github上。

### 用户表 t_user

| 字段    | 类型         | 描述       |
| ------- | ------------ | ---------- |
| u_id    | int(11)      | 主键，自增 |
| u_name  | varchar(255) | 姓名       |
| u_phone | varchar(20)  | 手机号码   |
| u_addr  | text         | 地址       |

```mysql
-- 创建用户表
CREATE TABLE `t_user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_name` varchar(255) NOT NULL COMMENT '姓名',
  `u_phone` varchar(20) NOT NULL COMMENT '手机号码',
  `u_addr` text NOT NULL COMMENT '地址',
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表'
```

### 商品表 t_product

| 字段    | 类型          | 描述       |
| ------- | ------------- | ---------- |
| p_id    | int(11)       | 主键，自增 |
| p_name  | varchar(255)  | 商品名称   |
| p_price | decimal(11,3) | 商品价格   |

```mysql
-- 创建商品表
CREATE TABLE `t_product` (
  `p_id` int(11) NOT NULL AUTO_INCREMENT,
  `p_name` varchar(255) NOT NULL COMMENT '商品名称',
  `p_price` decimal(11,3) NOT NULL COMMENT '商品价格',
  PRIMARY KEY (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
```

### 订单表`t_order`

| 字段        | 类型         | 描述                                                         |
| ----------- | ------------ | ------------------------------------------------------------ |
| o_id        | varchar(255) | 主键                                                         |
| u_id        | int(11)      | 用户id                                                       |
| o_staus     | int(11)      | 状态（0  待支付，1 已支付待发货，2 已发货，待收货，3 已签收，订单完成，4 删除） |
| create_time | bigint       | 时间戳，创建时间                                             |
| update_time | bigint       | 时间戳，更新时间                                             |

```mysql
-- 创建订单表
CREATE TABLE `t_order` (
  `o_id` varchar(255) NOT NULL,
  `u_id` int(11) NOT NULL COMMENT '用户id',
  `o_staus` int(11) NOT NULL COMMENT '订单状态，0  待支付，1 已支付待发货，2 已发货，待收货，3 已签收，订单完成，4 删除',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`o_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
```



### 订单项表`t_order_item`

| 字段           | 类型         | 描述       |
| -------------- | ------------ | ---------- |
| oi_id          | varchar(255) | 主键       |
| p_id           | int(11)      | 商品id     |
| product_number | int(11)      | 购买的数量 |
| o_id           | varchar(255) | 订单id     |

```mysql
-- 创建订单项表
CREATE TABLE `t_order_item` (
  `oi_id` varchar(255) NOT NULL,
  `p_id` int(11) NOT NULL COMMENT '商品id',
  `product_number` int(11) NOT NULL COMMENT '购买的数量',
  `o_id` varchar(255) NOT NULL COMMENT '订单id',
  PRIMARY KEY (`o_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单项表';
```



