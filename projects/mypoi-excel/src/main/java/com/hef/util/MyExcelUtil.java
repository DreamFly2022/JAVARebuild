package com.hef.util;

import com.hef.domain.ExcelContent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @Date 2022/1/8
 * @Author lifei
 */
public class MyExcelUtil {

    /**
     * 创建一个新的excel， 并向里面写入内容
     * @param excelContent
     * @param excelFilePath
     */
    public static void createNewExcelWriteContent(ExcelContent excelContent, String excelFilePath) {
        try {
            MyExcelTypeEnum myExcelType = judgeExcelFileType(excelFilePath);
            Workbook wb;
            if (myExcelType==MyExcelTypeEnum.XLS) {
                wb = new HSSFWorkbook();
            }else {
                wb = new XSSFWorkbook();
            }

            if (excelContent!=null && !CollectionUtils.isEmpty(excelContent.getSheetContentList())) {

            }
            try (OutputStream outputStream = new FileOutputStream(excelFilePath)) {
                wb.write(outputStream);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断excel文件类型
     * @param excelFilePath
     * @return
     */
    private static MyExcelTypeEnum judgeExcelFileType(String excelFilePath) {
        if (StringUtils.isBlank(excelFilePath)) {
            throw new IllegalArgumentException("excel路径不能为空");
        }
        int i = excelFilePath.lastIndexOf(".");
        if (i<0) throw new IllegalArgumentException("文件名称没有后缀，后缀必须为: .xls 或 .xlsx");
        String suffix = excelFilePath.substring(i+1);
        MyExcelTypeEnum myExcelType = MyExcelTypeEnum.findExcelSuffixEnum(suffix);
        if (myExcelType==null) throw new IllegalArgumentException("文件后缀必须为： .xls 或 .xlsx");
        File file  = new File(excelFilePath);
        if (file.exists() && file.isFile()) {
            throw new IllegalArgumentException("文件已存在，需要把已存在的文件删除");
        }
        String parent = file.getParent();
        File parentFile = new File(parent);
        if (!parentFile.exists() || !parentFile.isDirectory()) {
            throw new IllegalArgumentException("文件路径不存在：" +parentFile.getAbsolutePath());
        }
        return myExcelType;
    }
}
