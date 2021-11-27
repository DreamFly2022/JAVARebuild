# MySQL主从复制：设置GTID复制

[toc]

## 一、GTID

是为了让所有 relay log，有个像链表一样的顺序；

GTID的机制是：每次发送的一条事务，都会带一个，我下一个ID是啥。这样我的从库在做relay的时候，就会知道我这些事务相互之间的关系。

基于[MySQL主从复制演示-在ubuntu服务器上利用docker启动两个mysql服务](./2021-11-25-MySQL主从复制演示-在ubuntu服务器上利用docker启动两个mysql服务.md) ，在其之上进行改进。

## 二、设置GTID复制

设置之前，查询主库mysql-1 的状态：

> mysql -h172.25.227.137 -P3307 -uroot -p

```
mysql> show master status;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000002 |      154 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)
```

### （1）修改主库的配置文件

> sudo docker exec -it mysql-1 bash

`my.cnf`

```
[mysqld]
pid-file        = /var/run/mysqld/mysqld.pid
socket          = /var/run/mysqld/mysqld.sock
datadir         = /var/lib/mysql

server_id = 1

# master-slave v.1 add
sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin = mysql-bin
binlog-format = Row

# GTID v.2 add
gtid_mode = ON
enforce-gtid-consistency
log-bin
log-slave-updates = 1


symbolic-links=0
```

重启容器：

> sudo docker container ps

进行一次事务操作：

```mysql
mysql> show master status;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000003 |      154 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)

mysql> use db;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;
+--------------+
| Tables_in_db |
+--------------+
| t1           |
+--------------+
1 row in set (0.00 sec)

mysql> insert t1(id) values(221),(331);
Query OK, 2 rows affected (0.00 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> show master status;
+------------------+----------+--------------+------------------+----------------------------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set                      |
+------------------+----------+--------------+------------------+----------------------------------------+
| mysql-bin.000003 |      408 |              |                  | ab1b5eab-4e03-11ec-af72-0242ac110002:1 |
+------------------+----------+--------------+------------------+----------------------------------------+
1 row in set (0.00 sec)
```



### (2) 修改从库的配置文件

> sudo docker exec -it mysql-2 bash

```
[mysqld]
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql

server_id = 2
sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin = mysql-bin
binlog-format = Row

gtid_mode = ON
enforce-gtid-consistency
log-bin
log-slave-updates = 1



symbolic-links=0
```

重启服务：

> sudo docker container restart mysql-2

登陆从库：

> mysql -h172.25.227.137 -P3317 -uroot -p

在从库上执行DTID的复制

```
CHANGE MASTER TO
MASTER_HOST = host,
MASTER_PORT = port,
MASTER_USER = user,
MASTER_PASSWORD = password,
MASTER_AUTO_POSITION = 1;

CHANGE MASTER TO
    MASTER_HOST='172.25.227.137',  
    MASTER_PORT = 3307,
    MASTER_USER='repl',      
    MASTER_PASSWORD='123456',   
    MASTER_LOG_FILE='mysql-bin.000003',
    MASTER_LOG_POS=408;
```





