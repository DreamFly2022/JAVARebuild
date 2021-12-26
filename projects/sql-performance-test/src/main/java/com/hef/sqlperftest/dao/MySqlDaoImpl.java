package com.hef.sqlperftest.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Random;
import java.util.UUID;

/**
 * @Date 2021/12/26
 * @Author lifei
 */
public class MySqlDaoImpl implements MySqlDao{

    private static final String URL = "jdbc:mysql://127.0.0.1:3307/ds_shop?useUnicode=true&characterEncoding=utf8&useSSL=true";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";

    private static final int BATCH_INSERT_NUM = 100 * 10000;
    private static final int step = 2000;


    /**
     * 创建100 万的订单
     */
    @Override
    public void createBatchInsertSql() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String filePath = "/Users/lifei/Downloads/demo_sql/2021-12-27-order.sql";
        try (FileWriter fw = new FileWriter(filePath)) {
            for (int k = BATCH_INSERT_NUM / step; k > 0; k--) {
                sb.append("INSERT INTO t_order(o_id, p_code, p_num, total_price, create_time, update_time) values ");
                for (int i = 1; i <= step; i++) {
                    int code = random.nextInt(step);
                    int pNum = Math.max(code / 100, 1);
                    double price = Double.parseDouble(String.format("%.2f", Double.parseDouble(code / 100 + ".1" + code)));
                    sb.append(String.format("('%s', '%s', %d, %.2f, %d, %d),", UUID.randomUUID(), "p_" + code, pNum, price * pNum, System.currentTimeMillis(), System.currentTimeMillis()));
                }
                fw.write(sb.substring(0, sb.toString().length()-1)+";");
                fw.write("\n");
                sb.delete(0, sb.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testBatchInsertDataList() {
        Connection con=null;
        PreparedStatement statement=null;
        try {
            Random random = new Random();
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 建立数据库连接
            con = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            con.setAutoCommit(false);
            // 创建
            statement = con.prepareStatement("INSERT INTO t_order(o_id, p_code, p_num, total_price, create_time, update_time) values(?,?,?,?,?,?)");
            for (int k=BATCH_INSERT_NUM/step; k>0; k--) {
                for (int i=1; i<=step; i++) {
                    int code = random.nextInt(step);
                    int pNum = Math.max(code/100, 1);
                    double price = Double.parseDouble(String.format("%.2f", Double.parseDouble(code/100+".1"+code)));
                    statement.setString(1, UUID.randomUUID().toString());
                    statement.setString(2, "p_"+code);
                    statement.setInt(3, pNum);
                    statement.setDouble(4, price * pNum);
                    statement.setLong(5, System.currentTimeMillis());
                    statement.setLong(6, System.currentTimeMillis());
                    statement.addBatch();
                }
                statement.executeBatch();
            }
            con.commit();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    statement=null;
                }
            }

            if (con!=null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    con=null;
                }
            }
        }
    }

    public static void main(String[] args) {
        MySqlDao mySqlDao = new MySqlDaoImpl();
        /*long beginTime = System.currentTimeMillis();
        mySqlDao.testBatchInsertDataList();
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("beginTime: %d, endTime: %d, %d", beginTime, endTime, endTime-beginTime));*/
        mySqlDao.createBatchInsertSql();
    }
}
