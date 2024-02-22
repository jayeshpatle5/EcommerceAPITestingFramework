package com.ecommerce.Testdata.excel;

//import Fillo.*;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Field;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ExcelFIlloUtils {
    private Connection connection;

    public ExcelFIlloUtils(String excelFilePath) {
        Fillo fillo = new Fillo();
        try {
            connection = fillo.getConnection(excelFilePath);
        } catch (FilloException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        connection.close();
    }

    public String readCellValue(String sheetName, String columnName, String TcId) throws FilloException {
        String query = String.format("SELECT %s FROM %s WHERE TestCaseID=%s", columnName, sheetName, TcId);
        Recordset recordset = connection.executeQuery(query);
        String cellValue = recordset.getField(columnName);
        recordset.close();
        return cellValue;
    }
    public void writeCellValue(String sheetName, String columnName, String cellValue, String TcId) throws FilloException {
        String updateQuery = String.format("UPDATE %s SET %s='%s' WHERE TestCaseID=%s", sheetName, columnName, cellValue, TcId);
        connection.executeUpdate(updateQuery);
    }


    public int getRowCount(String sheetName) throws FilloException {
        String countQuery = String.format("SELECT COUNT(TestCaseID) FROM %s", sheetName);
        Recordset recordset = connection.executeQuery(countQuery);

        int rowCount = 0;
        if (recordset.next()) {
            Field field = recordset.getField(0);
            String rowCountAsString = field.toString();
            rowCount = Integer.parseInt(rowCountAsString);
        }

        recordset.close();
        return rowCount;
    }



    public void insertNewRow(String sheetName, String TcId) throws FilloException {

        String insertQuery = String.format("INSERT INTO %s (TestCaseID) VALUES (%s)", sheetName, TcId);
        connection.executeUpdate(insertQuery);
    }

    public void deleteRow(String sheetName, String  TcId) throws FilloException {
        String deleteQuery = String.format("DELETE FROM %s WHERE TestCaseID=%s", sheetName, TcId);
        connection.executeUpdate(deleteQuery);
    }



    public void createSheet(String sheetName, String[] columnNames) throws FilloException {

/*            String createSheetQuery = String.format("CREATE TABLE %s (%s)", sheetName, String.join(",", columnNames));
            connection.executeUpdate(createSheetQuery);
            */

        connection.createTable(sheetName,columnNames);

    }




}
