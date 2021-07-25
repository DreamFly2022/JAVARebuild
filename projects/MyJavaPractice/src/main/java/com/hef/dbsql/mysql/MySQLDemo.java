package com.hef.dbsql.mysql;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date 2021/7/24
 * @Author lifei
 */
public class MySQLDemo {

    public static void main(String[] args) {
        MySQLDemo demo = new MySQLDemo();
        demo.mysqlExec();
        /*long begin = System.currentTimeMillis();
        Connection con = demo.createConnect();
        for (int i = 0; i < INSERT_NUM; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOiId((long)i);
            orderItem.setOid("aa-bb-cc-dd-123");
            orderItem.setPid((long)i%7);
            orderItem.setPnum(100%21);
//                preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
            orderItem.setCreateTime(new Date());
            demo.insertDemo(con, orderItem);
        }
        long end = System.currentTimeMillis();
        System.out.println("本次执行消耗时间：" + (end-begin));*/
    }

    private static String JDBC_URL="jdbc:mysql://127.0.0.1:3307/homework?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
    private static String USERNAME = "root";
    private static String PASSWORD = "root";
    private static int INSERT_NUM = 100_0000;

    private void insertDemo(Connection con, OrderItem orderItem) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Statement statement = null;
        try {
            String insert_sql = String.format("insert into tb_order_item(oi_id, o_id, p_id, p_number, create_time) " +
                            "values (%s, '%s', %s, %s, '%s')", orderItem.getOiId(), "aa-bb-cc-dd-"+100/7, orderItem.getPid(), orderItem.getPnum(), format.format(orderItem.getCreateTime()));
            statement = con.createStatement();
            statement.execute(insert_sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (statement!=null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {

                }finally {
                    statement=null;
                }
            }
        }

    }

    private Connection createConnect() {
        Connection connection=null;
        // 1。 注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. 创建连接
            connection= DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }


    public void mysqlExec() {
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        // 1. 加载驱动

        try {
            // 1。 注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2. 创建连接
            connection= DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            // 3。执行查询

            long begin = System.currentTimeMillis();
            /*
             *
            // 测试一： 执行查询测试
            statement = connection.createStatement();
            String sql = "select t_id, t_name from t01";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int tId = resultSet.getInt("t_id");
                String tName = resultSet.getString("t_name");
                Tdemo tdemo = new Tdemo(tId, tName);
                System.out.println(tdemo);
            }
            */
            // 本次执行消耗时间：1581453
            String insert_sql = "insert into tb_order_item(oi_id, o_id, p_id, p_number, create_time) values (?, ?, ?, ?, '2021-07-25 10:21:21')";
            PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
            // 第二次测试: 插入100万条数据
            for (int i = 0; i < INSERT_NUM; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "aa-bb-cc-dd-"+i%233);
                preparedStatement.setInt(3, i/7);
                preparedStatement.setInt(4, 12);
//                preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            }


            long end = System.currentTimeMillis();
            System.out.println("本次执行消耗时间：" + (end-begin));

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            if (resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {

                }finally {
                    resultSet=null;
                }
            }
            if (statement!=null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {

                }finally {
                    statement=null;
                }
            }
            if (connection!=null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {

                } finally {
                    connection=null;
                }
            }

        }
    }
}
