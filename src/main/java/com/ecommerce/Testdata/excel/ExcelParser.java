package com.ecommerce.Testdata.excel;


import com.codoid.products.utils.FilenameUtils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for parsing an excel document, supports files of type ".xls" and ".xlsx".
 * Add methods as needed
 *
 * @Author Raju G
 */
public class ExcelParser {
    private FileInputStream fin;
    public File file;
    public Workbook workbook;
    public Sheet sheet;
    private int sheetIndex;
    private String fileExtension;
    private static final String xls = "xls";
    private static final String xlsx = "xlsx";

    public Cell cell;
    public Row row;
    public int rows;
    HashMap<String, Integer> columnData = new LinkedHashMap<>();

    /**
     * Constructor for ExcelParser
     * File extension determines how to instantiate the workbook
     *
     *
     */
    public ExcelParser() {

    }
    public ExcelParser(File file) {
        fileExtension = FilenameUtils.getExtension(file.getPath());
        try {
            fin = new FileInputStream(file);
            if (fileExtension.equals(xls)) {
                workbook = new HSSFWorkbook(fin);
            } else if (fileExtension.equals(xlsx)) {
                workbook = new XSSFWorkbook(fin);
            } else {
                throw new IOException("File is not of valid type: must be of type " + xls + " or " + xlsx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = workbook.getSheetAt(0);
    }
    public ExcelParser(String filePath) {
         file = new File(filePath);

        sheet = workbook.getSheetAt(0);
    }

    /**
     * stores column header name in HashMap and returns total rows in the given sheet name
     * @param file
     * @param sheetName
     * @return
     * @Author Srirama Murala
     */
    public int getRowNumAndHeaderDataFromExcel(File file, String sheetName){

        fileExtension = FilenameUtils.getExtension(file.getPath());
        try {
            fin = new FileInputStream(file);
            if (fileExtension.equals(xls)) {
                workbook = new HSSFWorkbook(fin);
            } else if (fileExtension.equals(xlsx)) {
                workbook = new XSSFWorkbook(fin);
            } else {
                throw new IOException("File is not of valid type: must be of type " + xls + " or " + xlsx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = workbook.getSheet(sheetName);

        rows = sheet.getLastRowNum();
        System.out.println("Number of rows in sheet: "+rows);
        sheet.getRow(0).forEach(cell->{
            columnData.put(cell.getStringCellValue(), cell.getColumnIndex());

        });

        return rows;
    }

    /**
     * Converts any type of cell data to String
     * @param rowNum
     * @param colNum
     * @return cellData
     * @Author Srirama Murala
     */
    public String getCellData(int rowNum, int colNum){
        cell = sheet.getRow(rowNum).getCell(colNum);
        String cellData = null;
        switch(cell.getCellTypeEnum()){
            case STRING:
                cellData = cell.getStringCellValue();
                break;
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    cellData = String.valueOf(cell.getDateCellValue());
                }else{
                    cellData = String.valueOf((long)cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                cellData = Boolean.toString(cell.getBooleanCellValue());
                break;
            case BLANK:
                cellData = "null";
                break;
        }
        return cellData;
    }

    /**
     * Used for getting cell data under a column or
     * the same method can be used to convert cell data type to String
     * @param ColName
     * @param rowNum
     * @return String type cell data for given column name in HashMap
     */
    public String getCellData(String ColName, int rowNum){
        return getCellData(rowNum, columnData.get(ColName) );
    }

    /**
     * Writes given message in the cell
     * @param fileName Excel template file object
     * @param msg
     * @return
     */

    public boolean setCelldata(File fileName, String ColName, int rowNum, String msg){

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            int colIndex = columnData.get(ColName);
            Row row = sheet.getRow(rowNum);
            CellStyle style = workbook.createCellStyle();

            Cell cell = row.createCell(colIndex);
            cell.setCellValue(msg);
            cell.setCellStyle(style);
            workbook.write(fos);
            fos.close();
            return true;
        }catch(IOException e){e.getMessage();
            return false;
        }
    }

    /**
     * Changes the current excel sheet
     *
     * @param sheetIndex Sheet to go to
     * @return the ExcelParser for chaining
     */
    public ExcelParser changeSheet(int sheetIndex) {
        this.sheetIndex = sheetIndex;
        sheet = workbook.getSheet("sheetIndex");
        return this;
    }

    /**
     * @return total number of sheets.
     */
    public int getTotalSheetNumber() {
        return workbook.getNumberOfSheets();
    }

    /**
     * @return the sheet's name at specific index
     */
    public String getSheetName(int sheetIndex) {
        return workbook.getSheetName(sheetIndex);
    }

    /**
     * Check if every non-empty cell in a column is a hyperlink
     *
     * @param col column index
     * @return true if every cell in column is a hyperlink
     */
    public boolean isColumnLinked(int col) {
        Iterator<Row> rowIterator = sheet.rowIterator();

        rowIterator.next(); //skip column header
        while (rowIterator.hasNext()) {
            Cell cell = rowIterator.next().getCell(col);
            if (cell != null && !cell.getStringCellValue().isEmpty() && cell.getHyperlink() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * check if every non-empty cell in a column is of certain color
     *
     * @param color
     * @param col
     * @return
     */
    public boolean isColumnDisplayedInColor(IndexedColors color, int col) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next(); //skip column header
        while (rowIterator.hasNext()) {
            Cell cell = rowIterator.next().getCell(col);
            if (cell != null && !cell.getStringCellValue().isEmpty()) {
                XSSFColor actualColor = ((XSSFCellStyle) cell.getCellStyle()).getFont().getXSSFColor();
                if (!(actualColor.getIndexed() == color.getIndex())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Gets the column index of a column name
     *
     * @param columnName Name of column to get index of
     * @return the column index
     */

    public int getColumn(String columnName) {
        Row firstRow = sheet.getRow(0);
        for (Cell cell : firstRow) {
            if (cell.getStringCellValue().equals(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    /**
     * @return true if value is displayed in specific column
     */
    public boolean isValueDisplayedInCol(String value, int col) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next(); //skip column header
        while (rowIterator.hasNext()) {
            Cell cell = rowIterator.next().getCell(col);
            if (cell != null && cell.getStringCellValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return list of column names of current sheet
     */
    public List<String> getAllColumnLabels() {
        List<String> rc = new ArrayList<>();
        Row firstRow = sheet.getRow(0);
        for (Cell cell : firstRow) {
            rc.add(cell.getStringCellValue());
        }

        return rc;
    }

    /**
     * get all the row values from certain column
     *
     * @param col
     * @return
     */
    public List<String> getAllRowValueForColumn(int col) {
        List<String> rc = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next(); //skip column header
        while (rowIterator.hasNext()) {
            Cell cell = rowIterator.next().getCell(col);
            rc.add(cell.getStringCellValue());
        }
        return rc;
    }
/**
 public int getDuplicateValuesCountFromColRow(int colIndex, int rowIndex){
 List<String> rc = new ArrayList<>();
 int counter = 0;
 Iterator<Row> rowIterator = sheet.rowIterator();
 rowIterator.next(); //skip column header
 while (rowIterator.hasNext()) {
 rc.add(rowIterator.next().getCell(colIndex).getStringCellValue());
 if(rc.contains(rowIterator.next().getCell(colIndex).getStringCellValue())){
 counter++; } else { }

 }
 return counter;
 }
 **/

    /**
     * Gets the cell at row, col (indexed at 0)
     *
     * @param row row index
     * @param col coulumn index
     * @return the cell
     */

    public Cell getCell(int row, int col) {
        return sheet.getRow(row).getCell(col);
    }

    /**
     * Gets the url from a hyperlink within a cell at row, col (indexed at 0)
     *
     * @param row row index
     * @param col column index
     * @return the url
     */
    public String getUrl(int row, int col) {
        return getCell(row, col).getHyperlink().getAddress();
    }

    /**
     * Gets the cell at col Width
     *
     * @param colNumber column index
     * @return the int
     */
    public int getColumnWidth(int colNumber) {
        return sheet.getColumnWidth(colNumber);
    }

    /**
     * Sets the cell at col Width
     *
     * @param colNumber column index
     * @param width     width size
     * @return the int
     */
    public void setColumnWidth(int colNumber, int width) {
        sheet.setColumnWidth(colNumber, width);
    }

    /**
     * get all the row values from certain column
     *
     * @param colName,CellValue
     * @return
     */
    public int getRowNumber(String colName, String CellValue) {
        int colNum = getColumn(colName);
        List<String> allRowValue = getAllRowValueForColumn(colNum);
        for (int i = 0; i < allRowValue.size(); i++) {
            if (allRowValue.get(i).equals(CellValue)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Get the extension of the file
     *
     * @param file
     */
    public String getFileExtension(File file) {
        final String fileExtension = FilenameUtils.getExtension(file.getPath());
        return fileExtension;
    }
    public void close() throws IOException {
        workbook.close();
        if(fin!=null) {
            fin.close();
        }
    }
}