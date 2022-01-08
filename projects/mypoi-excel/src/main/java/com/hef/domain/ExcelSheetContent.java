package com.hef.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2022/1/8
 * @Author lifei
 */
public class ExcelSheetContent {

    private String sheetName;
    // 一个sheet页里面有多个table
    private List<TableContent> tableContentList = new ArrayList<>();

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<TableContent> getTableContentList() {
        return tableContentList;
    }

    public void setTableContentList(List<TableContent> tableContentList) {
        this.tableContentList = tableContentList;
    }

    @Override
    public String toString() {
        return "ExcelSheetContent{" +
                "sheetName='" + sheetName + '\'' +
                ", tableContentList=" + tableContentList +
                '}';
    }
}
