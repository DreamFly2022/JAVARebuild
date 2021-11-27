#MySQL主从复制演示-在ubuntu服务器上利用docker启动两个mysql服务

[toc]

## 一、安装docker

### 1.1 配置docker仓库

```
# 第一步：卸载旧版本
sudo apt-get remove docker docker-engine docker.io containerd runc

# 第二步：安装依赖
## 更新
sudo apt-get update
## 安装依赖
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
# 第三步：
 curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# 第四步：
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

### 1.2 安装docker

```
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
```

### 1.3 配置阿里云镜像

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://k8yv8apb.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

docker的几个命令：

```
sudo systemctl start docker
sudo systemctl stop docker
sudo systemctl restart docker
# 查看docker的版本
sudo docker version
sudo docker info
```

## 二、利用docker启动两个mysql服务

```
# 拉取mysql 镜像
sudo docker pull mysql:5.7
# 查看镜像
sudo docker image ls
#  运行第一个容器
sudo docker container run -p 3307:3306 --name mysql-1 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
#  运行第二个mysql容器
sudo docker container run -p 3317:3306 --name mysql-2 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7
```

连接mysql，必须使用ip

```
mysql -h172.25.227.137 -P3307 -uroot -p

mysql -h172.25.227.137 -P3317 -uroot -p
```

进入mysql-1容器：

**注意：mysqld 配置 的等号 左右两边要有空格，否则容器会无法启动**

```
sudo docker exec -it mysql-1 bash

# 安装 vim 命令
apt-get update
apt-get install vim

# 修改配置文件
oot@8b1b7f89cda3:/etc/mysql/mysql.conf.d# cat mysqld.cnf | grep -v '#'


[mysqld]
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql

server_id = 1


sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin = mysql-bin
binlog-format = Row

symbolic-links=0

# 重启容器
sudo docker container restart mysql-1
sudo docker container start 8b1b7f89cda3
```

进入mysql-2容器：

**注意：mysqld 配置 的等号 左右两边要有空格，否则容器会无法启动**

```
sudo docker exec -it mysql-2 bash

# 安装 vim 命令
apt-get update
apt-get install vim

#  配置mysql
root@8df74d9a3cd7:/etc/mysql/mysql.conf.d# vi mysqld.cnf
root@8df74d9a3cd7:/etc/mysql/mysql.conf.d# pwd
/etc/mysql/mysql.conf.d
root@8df74d9a3cd7:/etc/mysql/mysql.conf.d# cat mysqld.cnf | grep -v '#'


[mysqld]
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql

server_id = 2
sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin = mysql-bin
binlog-format = Row



symbolic-links=0
# 重启mysql-2
sudo docker container restart mysql-2
```



## 三、演示主从复制

[参考：mysql复制--主从复制配置](https://blog.csdn.net/daicooper/article/details/79905660)

### (1) 登陆mysql-1 ，主库

> mysql -h172.25.227.137 -P3307 -uroot -p

```
mysql> CREATE USER 'repl'@'%' IDENTIFIED BY '123456';
Query OK, 0 rows affected (0.11 sec)

mysql> GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
Query OK, 0 rows affected (0.12 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.10 sec)

mysql> show master status;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000001 |      682 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)

```

### (2) 登陆mysql-2 ，配置从节点

> mysql -h172.25.227.137 -P3317 -uroot -p

```
CHANGE MASTER TO
    MASTER_HOST='172.25.227.137',  
    MASTER_PORT = 3307,
    MASTER_USER='repl',      
    MASTER_PASSWORD='123456',   
    MASTER_LOG_FILE='mysql-bin.000001',
    MASTER_LOG_POS=682;
    
show slave status\G

## 启动复制，这一步骤很重要
START SLAVE;
```

### (3) 在主库mysql-1上操作

```
mysql> create schema db;
Query OK, 1 row affected (0.00 sec)

mysql> use db1;
Database changed

mysql> show tables;
Empty set (0.00 sec)

mysql> create table t1(id int);
Query OK, 0 rows affected (0.01 sec)

mysql> insert into t1(id) values(1),(2);
Query OK, 2 rows affected (0.00 sec)
Records: 2  Duplicates: 0  Warnings: 0
```

### (4) 在从库mysql-2 上查看同步情况

```
mysql> show schemas;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| db                 |
| db1                |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
6 rows in set (0.01 sec)

mysql> use db1;
Database changed
mysql> select * from t1;
+------+
| id   |
+------+
|    1 |
|    2 |
+------+
2 rows in set (0.00 sec)
```

补充SQL命令：

```
show global variables like '%port%';
```

