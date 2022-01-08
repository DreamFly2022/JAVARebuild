package com.hef.util;

import org.junit.Test;

/**
 * @Date 2022/1/8
 * @Author lifei
 */
public class MyExcelUtilTest {

    @Test
    public void createNewExcelWriteContentTest() {
        String excelFilePath = "/Users/lifei/Documents/opt/doc/demo01.xlsx";
        MyExcelUtil.createNewExcelWriteContent(null, excelFilePath);

    }

}
