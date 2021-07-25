package com.hef.dbsql.mysql;


import java.sql.*;

/**
 * @Date 2021/7/25
 * @Author lifei
 */
public class PostgreDemo {

    private static String URL="jdbc:postgresql://127.0.0.1:5432/homework";
    private static String USERNAME="demouser";
    private static String PASSWORD="demouser";
    private static int INSERT_NUM = 100_0000;

    public static void main(String[] args) {
        PostgreDemo postgreDemo = new PostgreDemo();
        postgreDemo.execPostgre();
    }

    public void execPostgre() {
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            long begin = System.currentTimeMillis();
            /*statement = con.createStatement();
            String sql = "select t_id, t_name from t01";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Tdemo tdemo = new Tdemo(resultSet.getInt("t_id"), resultSet.getString("t_name"));
                System.out.println(tdemo);
            }*/


            String insert_sql = "insert into tb_order_item(oi_id, o_id, p_id, p_number, create_time) values (?, ?, ?, ?, '2021-07-25 10:21:21')";
            PreparedStatement preparedStatement = con.prepareStatement(insert_sql);
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
            if (resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {

                }finally {
                    resultSet=null;
                }
            }
        }
    }
}
