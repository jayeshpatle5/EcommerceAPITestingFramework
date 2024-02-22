package com.ecommerce.Executor;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.ecommerce.Testdata.excel.ExcelDataReader;

import java.io.IOException;

public class TestSuiteCreator extends ExecutionHelper {

    public static void main(String[] args) throws FilloException, IOException {
        System.out.println("----- Test Suite Creation started -----");
        Recordset result = null;
        String filePath = System.getProperty("user.dir") + "/Execution/RunManager.xlsx";

        String queryDataSheet = ("select * from MasterTestExecutor where Run='YES'");
        System.out.println(queryDataSheet);
        result = ExcelDataReader.getDataRecordSet(filePath, queryDataSheet);
//        writer = new FileWriter(new File(xmlPath));
//        bw = new BufferedWriter(writer);
        while (result.next()) {
            String sheetName = result.getField("Platform");
            //String Tagname = result.getField("Tags");
            System.setProperty("dataSheet",result.getField("MappingSheet"));
            String query = "select * from "+sheetName+" where Run='YES'";
            Recordset rs= ExcelDataReader.getDataRecordSet(filePath, query);
            ExecutionHelper.createSuites(rs,sheetName);
        }
        System.out.println("----- Test Suite Creation Completed -----");
    }
}
