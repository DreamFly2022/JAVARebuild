package com.hef.dbsql.mysql;


import java.sql.*;

/**
 * @Date 2021/7/25
 * @Author lifei
 */
public class PostgreDemo {

    private static String URL="jdbc:postgresql://127.0.0.1:5432/mydb01";
    private static String USERNAME="demouser";
    private static String PASSWORD="demouser";

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
            statement = con.createStatement();
            String sql = "select t_id, t_name from t01";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Tdemo tdemo = new Tdemo(resultSet.getInt("t_id"), resultSet.getString("t_name"));
                System.out.println(tdemo);
            }
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
