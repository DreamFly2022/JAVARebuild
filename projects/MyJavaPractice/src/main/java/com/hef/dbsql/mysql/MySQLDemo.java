package com.hef.dbsql.mysql;


import java.sql.*;

/**
 * @Date 2021/7/24
 * @Author lifei
 */
public class MySQLDemo {

    public static void main(String[] args) {
        MySQLDemo demo = new MySQLDemo();
        demo.mysqlExec();
    }

    private static String JDBC_URL="jdbc:mysql://127.0.0.1:3307/mydb01?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
    private static String USERNAME = "root";
    private static String PASSWORD = "root";


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
            statement = connection.createStatement();
            String sql = "select t_id, t_name from t01";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int tId = resultSet.getInt("t_id");
                String tName = resultSet.getString("t_name");
                Tdemo tdemo = new Tdemo(tId, tName);
                System.out.println(tdemo);

            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            if (resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {

                }
            }
            if (statement!=null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {

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
