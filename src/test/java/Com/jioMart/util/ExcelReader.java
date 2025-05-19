package Com.jioMart.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.h2.result.Row;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;

//import org.apache.poi.ss.usermodel.*; // Import necessary classes
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.*;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
    public class ExcelReader {

        public List<String> readExcel(String filePath) throws Exception {
            List<String> queryList = new ArrayList<>();
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);  // Read first sheet

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Skip header row
                    continue;
                }
                queryList.add(row.getCell(0).getStringCellValue());
            }

            workbook.close();
            fis.close();
            return queryList;
        }
    }

