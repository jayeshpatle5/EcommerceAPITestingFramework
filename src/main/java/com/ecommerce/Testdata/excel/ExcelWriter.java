package com.ecommerce.Testdata.excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {

    String excelPath;
    Workbook workbook;
    FileOutputStream outputStream;
    Sheet sheet;

    public ExcelWriter(String excelPath, String sheetName) throws IOException {
        this.excelPath = excelPath;
        this.workbook = new XSSFWorkbook(new FileInputStream(excelPath));
        this.outputStream = new FileOutputStream(excelPath);
        this.sheet = workbook.getSheet(sheetName);
    }

    public boolean writeTOCell(String colomnName, String testCaseID, String value) {
        try {
            int column = findValueInExcel(colomnName).getColumn();
            int row = findValueInExcel(testCaseID).getRow();
            writeData(column, row, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void writeData(int column, int line, String value) {
        Row row = sheet.getRow(line);
        Cell cell = row.getCell(column);
        cell.setCellValue(value);

        try {
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the workbook
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public CellAddress findValueInExcel(String targetValue) {

        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.equals(targetValue)) {
                        return cell.getAddress();
                    }
                }
            }
        }
        return null; // Value not found

    }
}
