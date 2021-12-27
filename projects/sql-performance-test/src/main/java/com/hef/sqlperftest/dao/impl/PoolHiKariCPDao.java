package com.hef.sqlperftest.dao.impl;

import com.hef.sqlperftest.dao.MySqlDao;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @Date 2021/12/27
 * @Author lifei
 */
public class PoolHiKariCPDao implements MySqlDao {

    private static final String URL = "jdbc:mysql://127.0.0.1:3307/ds_shop?useUnicode=true&characterEncoding=utf8&useSSL=false";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";


    @Override
    public void testBatchInsertDataList() {

    }

    @Override
    public void createBatchInsertSql() {

    }

    @Override
    public void testSelectSql() {
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            DataSource dataSource = setupDataSource(URL);
            con = dataSource.getConnection();
            statement = con.createStatement();
            resultSet = statement.executeQuery("select count(1) from t_order where p_code='p_12'");
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getObject(i)+"\t");
                }
                System.out.println();
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            if (resultSet!=null) {try{resultSet.close();}catch (Exception e){}}
            if (statement!=null) {try{statement.close();}catch (Exception e){}}
            if (con!=null) {try{con.close();}catch (Exception e){}}
        }
    }

    /**
     * 创建数据源
     * @param url
     * @return
     */
    private DataSource setupDataSource(String url) {
        try {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl(url);
            dataSource.setUsername(USER_NAME);
            dataSource.setPassword(PASSWORD);
            return dataSource;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        PoolHiKariCPDao poolDBCPDao = new PoolHiKariCPDao();
        poolDBCPDao.testSelectSql();
    }
}
