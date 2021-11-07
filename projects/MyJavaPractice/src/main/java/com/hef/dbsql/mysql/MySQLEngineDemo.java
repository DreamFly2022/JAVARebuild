package com.hef.dbsql.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Date 2021/8/1
 * @Author lifei
 */
public class MySQLEngineDemo {

    private static final String URL = "jdbc:mysql://127.0.0.1:3307/mydb01?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final int TOTAL = 100_0000;
    private static final int STEP = 1000;





    public static void main(String[] args) {

        Random random = new Random(1);
        double res = random.nextDouble();
        System.out.println(res);
        System.out.println(toTwoPointPercent(res));
        System.out.println(toTwoPointPercent(random.nextDouble()));


//        MySQLEngineDemo engineDemo = new MySQLEngineDemo();
//        List<MySQLEngineItem> itemList = new ArrayList<>();
//        StringBuilder sb = new StringBuilder();
//        sb.append("insert into tb_engine_myisam(t_name, t_num, t_phone_type, t_phone_version) values");
//        for (int i=1; i<=TOTAL; i++) {
//            sb.append("(?,?,?,?),");
//            itemList.add(new MySQLEngineItem.Builder()
//                    .name("demo"+(i%STEP))
//                    .num()
//                    .builder());
//            if (i%STEP==0) {
//                sb.deleteCharAt(sb.length()-1);
//                engineDemo.insertDemo(sb.toString(), itemList);
//            }
//        }
    }

    private static double toTwoPointPercent(double v) {
        return Double.parseDouble(String.format(".2f", v*100));
    }

    public void insertDemo(String sql, List<MySQLEngineItem> itemList) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql);) {
//                statement.
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
