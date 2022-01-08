package com.hef.domain;

/**
 * @Date 2022/1/8
 * @Author lifei
 */
public class TableContent {

    // 标题
    private String title;
    // 表格内容
    private CellContent[][] table;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CellContent[][] getTable() {
        return table;
    }

    public void setTable(CellContent[][] table) {
        this.table = table;
    }


}
