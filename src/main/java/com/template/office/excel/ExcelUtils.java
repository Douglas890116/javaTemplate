package com.template.office.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 创建/读取 一个Excel文件
 * 采用Apache的POI项目包
 * Created by Cloud on 2016/6/27.
 */
public class ExcelUtils {
    /*
        POI Excel工具包基础

        HSSF****用于excel 97~2007(.xls)文件的操作 poi

        XSSF****用于excel 2007 OOXML(.xlsx)文件的操作 poi-ooxml

        SXSSF***是对XSSF的一种流的扩展。目的在生成excel时候，需要生成大量的数据的时候，通过刷新的方式将excel内存信息刷新到硬盘的方式，提供写入数据的效率。

        以下用HSSF***做使用样例，XSSF和SXSSF用法基本机制

        // 创建Excel表格
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表sheet
        HSSFSheet sheet = workbook.createSheet("test_sheet");
        // 在创建的sheet中新建一行
        HSSFRow row = sheet.createRow(0);
        // 在创建的一行中新建一个单元格
        HSSFCell cell = row.createCell(0);
        // 创建单元格格式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 创建单元格内容格式
        HSSFDataFormat dataFormat = workbook.createDataFormat();
    */

    public static void readExcel(File excel) {
        XSSFWorkbook workbook = null;
        XSSFSheet sheet;
        XSSFRow row;
        XSSFCell cell;
        try {
            workbook = new XSSFWorkbook(excel);
            int sheetNum = workbook.getNumberOfSheets();
            int rowNum;
            int cellNum;
            for (int i = 0; i < sheetNum; i++) {
                sheet = workbook.getSheetAt(i);
                if (null == sheet) continue;
                System.out.println("下面开始输出【" + sheet.getSheetName() + "】工作表。");
                rowNum = sheet.getLastRowNum();
                for (int j = 0; j <= rowNum; j++) {
                    row = sheet.getRow(j);
                    if (null == row) continue;
                    System.out.print((j + 1) + "\t");
                    cellNum = row.getLastCellNum();
                    for (int k = 0; k <= cellNum; k++) {
                        cell = row.getCell(k);
                        if (null == cell) continue;
                        switch (cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_BLANK:
                                System.out.print(cell.getStringCellValue() + "\t");
                                break;
                            case XSSFCell.CELL_TYPE_BOOLEAN:
                                System.out.print(cell.getBooleanCellValue() + "\t");
                                break;
                            case XSSFCell.CELL_TYPE_ERROR:
                                System.out.print(cell.getErrorCellString() + "\t");
                                break;
                            case XSSFCell.CELL_TYPE_FORMULA:
                                System.out.print(cell.getCellFormula() + "\t");
                                break;
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                System.out.print(cell.getNumericCellValue() + "\t");
                                break;
                            case XSSFCell.CELL_TYPE_STRING:
                                System.out.print(cell.getStringCellValue() + "\t");
                                break;
                            default:
                                System.out.print("Null");
                        }
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (null != workbook)
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void main(String[] arg) {
        File excel = new File("E:\\集团表单\\漏打卡申请表-新版.xlsx");
        ExcelUtils.readExcel(excel);
    }
}