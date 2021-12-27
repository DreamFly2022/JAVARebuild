package com.hef.sqlperftest.dao;

public interface MySqlDao {

    void testBatchInsertDataList();

    /**
     * 创建批量插入的SQL
     */
    void createBatchInsertSql();

    void testSelectSql();
}
