package com.ecommerce.Testdata.excel;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ExcelDataReader {



    public static String getCellData(String sheetName, String testCaseId, String columnKey ) throws FilloException {
        String cellData= null;
        Fillo fillo=new Fillo();
        String configFile = System.getProperty("user.dir") + "/Execution/RunManager.xlsx";
        Connection connection=fillo.getConnection(configFile);
        String strQuery="Select * from "+sheetName+" where TestCaseID='"+testCaseId+"'";
        Recordset recordset=connection.executeQuery(strQuery);

        while(recordset.next()){
            cellData = recordset.getField(columnKey);
            //System.out.println(recordset.getField(columnKey));
        }

        recordset.close();
        connection.close();

        return cellData;
    }
    public static boolean setCellData(String sheetName, String testCaseId, String columnKey, String Value ) throws FilloException {
        Fillo fillo=new Fillo();
        String configFile = System.getProperty("user.dir") + "/Execution/RunManager.xlsx";
        Connection connection=fillo.getConnection(configFile);
        String strQuery="Update  "+sheetName+" Set "+columnKey+"='"+Value+"' where TestCaseID='"+testCaseId+"'";
        if(connection.executeUpdate(strQuery)!=0){
            connection.close();
            return true;
        }
        connection.close();
        return false;
    }

    public static Recordset getDataRecordSet(String filepath, String query){
        Fillo fillo= new Fillo();
        Recordset recordset = null;
        Connection connection = null;
        try {
            connection = fillo.getConnection(filepath);
            recordset = connection.executeQuery(query);
        } catch (FilloException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return recordset;
    }

    // Method to read the test data from RunManager excel sheet
    public static LinkedHashMap<String, String> readTCBinding(String testCase, String dataSheet) {
        Fillo fillo=new Fillo();
        try {
            String configFile = System.getProperty("user.dir") + "/Execution/RunManager.xlsx";
            Connection connection=fillo.getConnection(configFile);
            String strQuery="Select * from " + dataSheet + " where TestCaseID='"+testCase+"'";
            Recordset recordset= connection.executeQuery(strQuery);
            LinkedHashMap<String, String> listVal = new LinkedHashMap<>();
            while(recordset.next()) {
                ArrayList<String> collection = recordset.getFieldNames();
                int size = collection.size();
                for (int i = 0; i <=(size-1); i++) {
                    String colname = collection.get(i);
                    String colval = recordset.getField(colname);
                    listVal.put(colname, colval);
                }
            }
            recordset.close();
            connection.close();
            return listVal;
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
        return null;
    }
}
