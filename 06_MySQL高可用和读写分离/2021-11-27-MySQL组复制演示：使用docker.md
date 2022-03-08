#  MySQL 组复制演示

[toc]

## 一、MySQL组复制——MGR

基于协议保证数据的一致性。

## 二、准备

### 2.1 使用docker启动三个mysql服务

```
# 查看docker 镜像
~$ sudo docker image ls
REPOSITORY            TAG       IMAGE ID       CREATED         SIZE
mysql                 5.7       8b43c6af2ad0   9 days ago      448MB
siomiz/softethervpn   latest    57268e3cc5b1   3 months ago    550MB
hello-world           latest    d1165f221234   8 months ago    13.3kB
mritd/shadowsocks     latest    8641e04be393   10 months ago   44.7MB
mobtitude/vpn-pptp    latest    4ce95136b93f   3 years ago     153MB

# 启动三个容器
sudo docker container run -p 3308:3306 --name mgr-1 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
sudo docker container run -p 3318:3306 --name mgr-2 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
sudo docker container run -p 3328:3306 --name mgr-3 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7

~$ sudo docker container ps
CONTAINER ID   IMAGE       COMMAND                  CREATED          STATUS          PORTS                                                  NAMES
4874145a4596   mysql:5.7   "docker-entrypoint.s…"   12 seconds ago   Up 10 seconds   33060/tcp, 0.0.0.0:3328->3306/tcp, :::3328->3306/tcp   mgr-3
e03e1f87c21e   mysql:5.7   "docker-entrypoint.s…"   19 seconds ago   Up 18 seconds   33060/tcp, 0.0.0.0:3318->3306/tcp, :::3318->3306/tcp   mgr-2
614d19a2d069   mysql:5.7   "docker-entrypoint.s…"   50 seconds ago   Up 49 seconds   33060/tcp, 0.0.0.0:3308->3306/tcp, :::3308->3306/tcp   mgr-1
```

### 2.2 测试进入三台mysql服务

```
mysql -h172.25.227.137 -P3308 -uroot -p

mysql -h172.25.227.137 -P3318 -uroot -p

mysql -h172.25.227.137 -P3328 -uroot -p
```

补充log_bin 和 sql_log_bin的区别：

参考：[MySQL的log_bin和sql_log_bin 的区别](https://www.cnblogs.com/sdadx/p/7685351.html)

- log_bin:二进制日志

- 在 mysql 启动时，通过命令行或配置文件决定是否开启 binlog，而 log_bin 这个变量仅仅是报告当前 binlog 系统的状态（打开与否）

- 若你想要关闭 binlog，你可以通过修改 sql_log_bin 并把原来的连接 kill 掉，也可以修改 log_bin，然后重启 mysql，**后者更彻底，缺点就是需要重启**

- sql_log_bin 是一个动态变量，修改该变量时，可以只对当前会话生效（Session），也可以是全局的（Global），当全局修改这个变量时，只会对新的会话生效（这意味当对当前会话也不会生效），因此一般全局修改了这个变量后，都要把原来的所有连接 kill 掉。

  > 当还原数据库的时候，如果不关闭二进制日志，那么你还原的过程仍然会记录在二进制日志里面，不仅浪费资源，那么增加了磁盘的容量，还没有必要（特别是利用二进制还原数据库的时候）所以一般还原的时候会选择关闭二进制日志，可以通过修改配置文件，重启关闭二进制日志。也可以动态命令关闭sql_log_bin,然后导入数据库。

### 2.3 修改`my.cnf` 配置文件

参考：

- [mgr-安装部署（多主）](https://blog.csdn.net/weixin_30666753/article/details/99999641)

修改mgr-1:

> 修改配置之后，重启容器：sudo docker container restart mgr-1

> apt-get update
>
> apt install iputils-ping
>
> apt install net-tools
>
> apt install telnet
>
> apt-get update
> apt-get install vim

```
[mysqld]
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql
log-error = error.log

server_id = 111
sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin = mysql-bin
binlog-format = Row

# mgr
gtid_mode = ON
enforce-gtid-consistency = ON
log-bin
log-slave-updates = 1
master_info_repository = TABLE
relay_log_info_repository = TABLE
binlog_checksum = NONE
# 指示Server必须为每个事务收集写集合，并使用XXHASH64哈希算法将其编码为散列
transaction_write_set_extraction = XXHASH64
# 表示将加入或者创建的复制组命名为， 可自定义(通过cat /proc/sys/kernel/random/uuid)
loose-group_replication_group_name = "d3ad3595-9126-4b04-9b63-5f67bb0c56fe"
# 设置为Server启动时不自动启动组复制
loose-group_replication_start_on_boot = off
loose-group_replication_local_address = "172.17.0.2:3306"
# 服务器进行配置。本设置可以不是全部的组成员服务地址
loose-group_replication_group_seeds ="172.17.0.2:3306,172.17.0.3:3306,172.17.0.4:3306"
loose-group_replication_local_address = "172.25.227.137:3308"
loose-group_replication_group_seeds = "172.25.227.137:3308,172.25.227.137:3318,172.25.227.137:3328"

#配置是否自动引导组
loose-group_replication_bootstrap_group = off
loose-group_replication_single_primary_mode = FALSE
loose-group_replication_enforce_update_everywhere_checks = TRUE
plugin-load=group_replication.so
# 允许加入组复制的客户机来源的ip白名单
loose-group_replication_ip_whitelist = "172.25.227.0/24,127.0.0.1/8,172.25.227.0/255"    


symbolic-links=0
```

mgr-2 的my.cnf 配置：

> 配置修改完之后需要重启服务：sudo docker container restart mgr-2

```
[mysqld]
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql
log-error = error.log

server_id = 222
sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin = mysql-bin
binlog-format = Row

gtid_mode = ON
enforce-gtid-consistency = ON
log-bin
log-slave-updates = 1
master_info_repository = TABLE
relay_log_info_repository = TABLE
binlog_checksum = NONE
transaction_write_set_extraction = XXHASH64

loose-group_replication_group_name = "23bbcde4-4198-41a1-b24c-09f2affa9a23"
loose-group_replication_start_on_boot = off
#loose-group_replication_local_address = "172.17.0.3:3306"
#loose-group_replication_group_seeds ="172.17.0.2:3306,172.17.0.3:3306,172.17.0.4:3306"
loose-group_replication_local_address = "172.25.227.137:3318"
loose-group_replication_group_seeds = "172.25.227.137:3308,172.25.227.137:3318,172.25.227.137:3328"
loose-group_replication_bootstrap_group = off
loose-group_replication_single_primary_mode = FALSE
loose-group_replication_enforce_update_everywhere_checks = TRUE
loose-group_replication_ip_whitelist = "172.25.227.0/24,127.0.0.1/8,172.25.227.0/255"

plugin-load=group_replication.so

symbolic-links=0
```

mgr-3 的my.cnf 配置：

> 配置修改完之后需要重启服务：sudo docker container restart mgr-3

```
[mysqld]
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql
log-error = error.log

server_id = 333
sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin = mysql-bin
binlog-format = Row

gtid_mode = ON
enforce-gtid-consistency = ON
log-bin
log-slave-updates = 1
master_info_repository = TABLE
relay_log_info_repository = TABLE
binlog_checksum = NONE
transaction_write_set_extraction = XXHASH64

loose-group_replication_group_name = "bdc3518a-b663-4853-a5e5-f05f35d7925b"
loose-group_replication_start_on_boot = off
#loose-group_replication_local_address = "172.17.0.4:3306"
#loose-group_replication_group_seeds ="172.17.0.2:3306,172.17.0.3:3306,172.17.0.4:3306"
loose-group_replication_local_address = "172.25.227.137:3328"
loose-group_replication_group_seeds = "172.25.227.137:3308,172.25.227.137:3318,172.25.227.137:3328"
loose-group_replication_bootstrap_group = off
loose-group_replication_single_primary_mode = FALSE
loose-group_replication_enforce_update_everywhere_checks = TRUE
loose-group_replication_ip_whitelist = "172.25.227.0/24,127.0.0.1/8,172.25.227.0/255"

plugin-load=group_replication.so

symbolic-links=0
```

```
172.17.0.2  614d19a2d069
172.17.0.3  e03e1f87c21e
172.17.0.4  4874145a4596
```



## 三、配置主节点mgr-1

> mysql -h172.25.227.137 -P3308 -uroot -p

```
# 关闭binlog
mysql> set sql_log_bin = 0;
Query OK, 0 rows affected (0.00 sec)

# 查看master 的状态
mysql> show master status;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000001 |      150 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)

# 创建复制用户
mysql> create user repl@'%' identified by '123456';
Query OK, 0 rows affected (0.00 sec)

mysql> grant replication slave on *.* to repl@'%';
Query OK, 0 rows affected (0.00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.00 sec)

# 打开binlog
mysql> set sql_log_bin = 1;
Query OK, 0 rows affected (0.00 sec)

# 开始构建group replication集群，通常操作命令 在mgr-1、mgr-2、mgr-3上依次执行
mysql> change master to master_user='repl', master_password='123456' for channel 'group_replication_recovery';
Query OK, 0 rows affected, 1 warning (0.04 sec)

# 安装复制插件
install plugin group_replication soname 'group_replication.so';

# 设置group_replication_bootstrap_group为ON是为了标示以后加入集群的服务器以这台服务器为基准（只要在mgr1上执行就可以了），以后加入的就不需要设置。 
set global group_replication_bootstrap_group = on;
start group_replication;


grant replication slave,replication client on *.* to repl@'localhost' identified by 'repl';
grant replication slave,replication client on *.* to repl@'127.0.0.1' identified by 'repl';
grant replication slave,replication client on *.* to repl@'172.18.42.%' identified by 'repl';
```

## 四、配置mgr-2

```
mysql -h172.25.227.137 -P3318 -uroot -p
```

## 五、配置mgr-3

> mysql -h172.25.227.137 -P3328 -uroot -p

```
mysql> set sql_log_bin = 0;
Query OK, 0 rows affected (0.00 sec)

mysql> create user repl@'%' identified by '123456';
Query OK, 0 rows affected (0.00 sec)

mysql> grant replication slave on *.* to repl@'%';
Query OK, 0 rows affected (0.00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.01 sec)

mysql> set sql_log_bin = 1;
Query OK, 0 rows affected (0.00 sec)

mysql> change master to master_user='repl', master_password='123456' for channel 'group_replication_recovery';
Query OK, 0 rows affected, 1 warning (0.04 sec)

mysql> install plugin group_replication soname 'group_replication.so';
```





```
port=3306
character-set-server=utf8mb4
bind-address = 0.0.0.0
max_connections=3000
max_connect_errors=100000
table_open_cache=512
external-locking=FALSE
max_allowed_packet=32M
sort_buffer_size=2M
join_buffer_size=2M
thread_cache_size=51
query_cache_size=32M
transaction_isolation=REPEATABLE-READ
tmp_table_size=96M
max_heap_table_size=96M
log_timestamps=SYSTEM

###***slowqueryparameters
long_query_time=1
slow_query_log = 1

###***binlogparameters
log-bin=mysql-bin
binlog_cache_size=4M
max_binlog_cache_size=4096M
max_binlog_size=1024M
binlog_format=row
expire_logs_days=7

###***relay-logparameters
gtid_mode = ON
enforce_gtid_consistency = ON
master_info_repository = TABLE
relay_log_info_repository = TABLE
binlog_checksum = NONE
log_slave_updates = ON
transaction_write_set_extraction = XXHASH64
loose-group_replication_group_name = "d3ad3595-9126-4b04-9b63-5f67bb0c56fe"
loose-group_replication_start_on_boot = off
loose-group_replication_local_address = "172.17.0.2:33061"
loose-group_replication_group_seeds ="172.17.0.2:33061,172.17.0.3:33062,172.17.0.4:33063"
loose-group_replication_bootstrap_group = off
#loose-group_replication_single_primary_mode = FALSE
#loose-group_replication_enforce_update_everywhere_checks = TRUE

plugin-load=group_replication.so

#***MyISAMparameters
key_buffer_size=16M
read_buffer_size=1M
read_rnd_buffer_size=16M
bulk_insert_buffer_size=1M
#skip-name-resolve

###***master-slavereplicationparameters
server-id=111
#slave-skip-errors=all

#***Innodbstorageengineparameters
innodb_buffer_pool_size=730M
innodb_data_file_path=ibdata1:10M:autoextend
innodb_thread_concurrency=16
innodb_flush_log_at_trx_commit=1
innodb_log_buffer_size=16M
innodb_log_file_size=512M
innodb_log_files_in_group=2
innodb_max_dirty_pages_pct=75
innodb_buffer_pool_dump_pct=50
innodb_lock_wait_timeout=50
innodb_file_per_table=on
```



```
port=3306
character-set-server=utf8mb4
bind-address = 0.0.0.0
max_connections=3000
max_connect_errors=100000
table_open_cache=512
external-locking=FALSE
max_allowed_packet=32M
sort_buffer_size=2M
join_buffer_size=2M
thread_cache_size=51
query_cache_size=32M
transaction_isolation=REPEATABLE-READ
tmp_table_size=96M
max_heap_table_size=96M
log_timestamps=SYSTEM

###***slowqueryparameters
long_query_time=1
slow_query_log = 1

###***binlogparameters
log-bin=mysql-bin
binlog_cache_size=4M
max_binlog_cache_size=4096M
max_binlog_size=1024M
binlog_format=row
expire_logs_days=7

###***relay-logparameters
gtid_mode = ON
enforce_gtid_consistency = ON
master_info_repository = TABLE
relay_log_info_repository = TABLE
binlog_checksum = NONE
log_slave_updates = ON
transaction_write_set_extraction = XXHASH64
loose-group_replication_group_name = "23bbcde4-4198-41a1-b24c-09f2affa9a23"
loose-group_replication_start_on_boot = off
loose-group_replication_local_address = "172.17.0.3:33062"
loose-group_replication_group_seeds ="172.17.0.2:33061,172.17.0.3:33062,172.17.0.4:33063"
loose-group_replication_bootstrap_group = off
#loose-group_replication_single_primary_mode = FALSE
#loose-group_replication_enforce_update_everywhere_checks = TRUE

plugin-load=group_replication.so

#***MyISAMparameters
key_buffer_size=16M
read_buffer_size=1M
read_rnd_buffer_size=16M
bulk_insert_buffer_size=1M
#skip-name-resolve

###***master-slavereplicationparameters
server-id=222
#slave-skip-errors=all

#***Innodbstorageengineparameters
innodb_buffer_pool_size=730M
innodb_data_file_path=ibdata1:10M:autoextend
innodb_thread_concurrency=16
innodb_flush_log_at_trx_commit=1
innodb_log_buffer_size=16M
innodb_log_file_size=512M
innodb_log_files_in_group=2
innodb_max_dirty_pages_pct=75
innodb_buffer_pool_dump_pct=50
innodb_lock_wait_timeout=50
innodb_file_per_table=on
```



```
port=3306
character-set-server=utf8mb4
bind-address = 0.0.0.0
max_connections=3000
max_connect_errors=100000
table_open_cache=512
external-locking=FALSE
max_allowed_packet=32M
sort_buffer_size=2M
join_buffer_size=2M
thread_cache_size=51
query_cache_size=32M
transaction_isolation=REPEATABLE-READ
tmp_table_size=96M
max_heap_table_size=96M
log_timestamps=SYSTEM

###***slowqueryparameters
long_query_time=1
slow_query_log = 1

###***binlogparameters
log-bin=mysql-bin
binlog_cache_size=4M
max_binlog_cache_size=4096M
max_binlog_size=1024M
binlog_format=row
expire_logs_days=7

###***relay-logparameters
gtid_mode = ON
enforce_gtid_consistency = ON
master_info_repository = TABLE
relay_log_info_repository = TABLE
binlog_checksum = NONE
log_slave_updates = ON
transaction_write_set_extraction = XXHASH64
loose-group_replication_group_name = "bdc3518a-b663-4853-a5e5-f05f35d7925b"
loose-group_replication_start_on_boot = off
loose-group_replication_local_address = "172.17.0.4:33063"
loose-group_replication_group_seeds = "172.17.0.2:33061,172.17.0.3:33062,172.17.0.4:33063"
loose-group_replication_bootstrap_group = off
#loose-group_replication_single_primary_mode = FALSE
#loose-group_replication_enforce_update_everywhere_checks = TRUE

plugin-load=group_replication.so

#***MyISAMparameters
key_buffer_size=16M
read_buffer_size=1M
read_rnd_buffer_size=16M
bulk_insert_buffer_size=1M
#skip-name-resolve

###***master-slavereplicationparameters
server-id=333
#slave-skip-errors=all

#***Innodbstorageengineparameters
innodb_buffer_pool_size=730M
innodb_data_file_path=ibdata1:10M:autoextend
innodb_thread_concurrency=16
innodb_flush_log_at_trx_commit=1
innodb_log_buffer_size=16M
innodb_log_file_size=512M
innodb_log_files_in_group=2
innodb_max_dirty_pages_pct=75
innodb_buffer_pool_dump_pct=50
innodb_lock_wait_timeout=50
innodb_file_per_table=on
```

> https://www.jianshu.com/p/43c80b5e8764?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation



> https://www.cnblogs.com/jimmy-share/p/11108967.html
