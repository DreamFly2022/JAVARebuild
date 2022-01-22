package com.hef.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2022/1/8
 * @Author lifei
 */
public class ExcelContent {

    /**
     * 多个sheet页的内容
     */
    private List<ExcelSheetContent> sheetContentList = new ArrayList<>();


    public List<ExcelSheetContent> getSheetContentList() {
        return sheetContentList;
    }

    public void setSheetContentList(List<ExcelSheetContent> sheetContentList) {
        this.sheetContentList = sheetContentList;
    }
}
