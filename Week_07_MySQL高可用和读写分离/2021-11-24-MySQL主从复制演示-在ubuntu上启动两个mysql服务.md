# MySQL 主从复制演示——在ubuntu上启动两个mysql服务

[toc]

### （1）第一步，准备两个MySQL

```
# 创建用户组：
groupadd mysql 
#-r：创建无登录权限的账户
useradd -r -g mysql mysql

tar -zxvf mysql-5.7.35-linux-glibc2.12-x86_64.tar.gz -C /usr/local/
cd /usr/local/
mv mysql-5.7.35-linux-glibc2.12-x86_64 mysql-1
cp -r mysql-1 mysql-2
mkdir -p {mysql-1,mysql-2}/data

chown -R mysql:mysql /usr/local/{mysql-1,mysql-2}
chmod -R 755 /usr/local/{mysql-1,mysql-2}


mkdir -p /usr/local/{mysql-1,mysql-2}/{tmp,mysqld}
touch /usr/local/mysql-1/mysqld/mysqld.sock
touch /usr/local/mysql-1/mysqld/mysqld.pid

touch /usr/local/mysql-2/mysqld/mysqld.sock
touch /usr/local/mysql-2/mysqld/mysqld.pid
```

初始化数据库：

```
# 初始化第一个mysql服务
cd /usr/local/mysql-1/bin
## 下面这句，执行完之后，会在最后一行生成一个默认密码：SzypT/rra2a0
./mysqld --initialize --defaults-file=/usr/local/mysql-1/my.cnf  --user=mysql --datadir=/usr/local/mysql-1/data --basedir=/usr/local/mysql-1

./mysqld --defaults-file=/usr/local/mysql-1/my.cnf \
--basedir=/usr/local/mysql-1/ \
--datadir=/usr/local/mysql-1/data/ \
--user=mysql \
--initialize-insecure \
--ssl \
--explicit_defaults_for_timestamp \
--verbose



# 初始化第二个mysql服务
cd /usr/local/mysql-2/bin
## 下面这句，执行完之后，会在最后一行生成一个默认密码：Yh.M6GsEspe6
./mysqld --initialize  --defaults-file=/usr/local/mysql-2/my.cnf --user=mysql --datadir=/usr/local/mysql-2/data --basedir=/usr/local/mysql-2


./mysqld --defaults-file=/usr/local/mysql-2/my.cnf \
--basedir=/usr/local/mysql-2/ \
--datadir=/usr/local/mysql-2/data/ \
--user=mysql \
--initialize-insecure \
--ssl \
--explicit_defaults_for_timestamp \
--verbose



```

启动mysql并指定配置文件

`/usr/local/mysql-1/my.cnf`

```
[mysqld]
basedir=/usr/local/mysql-1/
tmpdir=/usr/local/mysql-1/tmp
datadir=/usr/local/mysql-1/data
socket=/usr/local/mysql-1/mysqld/mysqld.sock
pid-file=/usr/local/mysql-1/mysqld/mysqld.pid
port=3306
server_id = 1

sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
log_bin=mysql-bin
binlog-format=Row
```

`/usr/local/mysql-2/my.cnf`

```
[mysqld]
basedir=/usr/local/mysql-2/
tmpdir=/usr/local/mysql-2/tmp
datadir=/usr/local/mysql-2/data
socket=/usr/local/mysql-2/mysqld/mysqld.sock
pid-file=/usr/local/mysql-2/mysqld/mysqld.pid
port = 3316
server_id = 2

sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 
log_bin=mysql-bin
binlog-format=Row
```

启动mysql服务

```
# 启动第一个mysql服务
cd /usr/local/mysql-1/bin
./mysqld --defaults-file=/usr/local/mysql-1/my.cnf --user=mysql

# 启动第二个mysql服务
cd /usr/local/mysql-2/bin
./mysqld --defaults-file=/usr/local/mysql-2/my.cnf --user=mysql
```

### （2）第二步，测试连接mysql，需要配置socket

```
# 登陆第一个mysql
mysql -S /usr/local/mysql-1/mysqld/mysqld.sock -hlocalhost -P3306 -uroot -p
## 重置密码
set password=password('root');

# 登陆mysql
mysql -S /usr/local/mysql-2/mysqld/mysqld.sock -hlocalhost -P3316 -uroot -p
## 重置密码
set password=password('root');
```

### （3）第三步：配置主库3306

登陆到主节点

> mysql -S /usr/local/mysql-1/mysqld/mysqld.sock -hlocalhost -P3306 -uroot -p

```mysql
mysql> CREATE USER 'repl'@'%' IDENTIFIED BY '123456';
Query OK, 0 rows affected (0.00 sec)

mysql> GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
Query OK, 0 rows affected (0.01 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.01 sec)

mysql> show master status;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000005 |      653 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)

```



### （4） 第四步：配置从节点 3316

> mysql -S /usr/local/mysql-2/mysqld/mysqld.sock -hlocalhost -P3316 -uroot -p

```
STOP SLAVE;

CHANGE MASTER TO
    MASTER_HOST='localhost',
    MASTER_PORT = 3306,
    MASTER_USER='repl',
    MASTER_PASSWORD='123456',
    MASTER_LOG_FILE='mysql-bin.000005',
    MASTER_LOG_POS=653;

START SLAVE; 
```

### （5）第五步：验证操作

#### 5.1在主库3306上创建表，并插入几条数据

```
mysql> create schema db;
Query OK, 1 row affected (0.01 sec)

mysql> use db;
Database changed
mysql> create table t1(id int);
Query OK, 0 rows affected (0.01 sec)

mysql> insert into t1(id) values(1),(2);
Query OK, 2 rows affected (0.01 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> show tables;
+--------------+
| Tables_in_db |
+--------------+
| t1           |
+--------------+
1 row in set (0.00 sec)

mysql> select * from t1;
+------+
| id   |
+------+
|    1 |
|    2 |
+------+
2 rows in set (0.00 sec)

mysql> insert into t1(id) values(11),(22);
Query OK, 2 rows affected (0.01 sec)
Records: 2  Duplicates: 0  Warnings: 0
```

#### 5.2 在从库上查看数据同步情况

```
mysql> select * from t1;
+------+
| id   |
+------+
|    1 |
|    2 |
|   11 |
|   22 |
+------+
4 rows in set (0.00 sec)
```

