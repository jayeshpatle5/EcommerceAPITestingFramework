package com.ecommerce.Executor;

import com.codoid.products.fillo.Recordset;
import org.testng.xml.*;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExecutionHelper {

	/*static FileWriter writer=null;
	static BufferedWriter bw = null;
	static String xmlPath = System.getProperty("user.dir") + "/Execution/TestNG_Execution.xml";*/

	static String filename;

	public static String createSuites(Recordset recordset, String sheetName) {
		XmlSuite suite = new XmlSuite();
		//XmlGroups xmlGroups
		ArrayList<XmlTest> alXmlTest = new ArrayList<XmlTest>();
		XmlTest xmlTest = new XmlTest();
		try {
			System.out.println("----- *********** -----");
			while (recordset.next()) {
				String includedMethods = "";
				includedMethods = recordset.getField("TestMethod");
				String appName = recordset.getField("Platform");
				String tags = recordset.getField("Tags");
				String testClassName = recordset.getField("TestClassName");
				String TestCaseID = recordset.getField("TestCaseID");
				suite.setName(appName);
				suite.setVerbose(2);
			    System.out.println("Sheet Name ----> "+sheetName);
				//System.out.println("TestModule Name : "+testModule);
				System.out.println("TestClass Name : " + testClassName);
				System.out.println("Included Method : " + includedMethods);
				System.out.println("Testcase Number : " + TestCaseID);
				System.out.println("----- *********** -----");

				xmlTest = getTest(suite,tags,
						testClassName,
						includedMethods,
						TestCaseID,sheetName);
				alXmlTest.add(xmlTest);
			}

			//List<XmlTest> disntictList = alXmlTest.stream().distinct().collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Exception Occurred while creating the xml file => "+e.getMessage());
		} finally {
			recordset.close();
		}
		return writeTestNGFile(suite);
	}

	public static XmlTest getTest(XmlSuite suite, String... strings) throws FileNotFoundException {
		List<String> listValue = Arrays.asList(strings);
		String tags = listValue.get(0);
		String testClassName = listValue.get(1);
		String includedMethods = listValue.get(2);
		String TestcaseID = listValue.get(3);
		String sheetName = listValue.get(4);
		/*Test tag*/
		XmlTest test1 = new XmlTest(suite);
		//------------Arun
		//Setting the xTestNG xml Test name and Parameters
		HashMap<String, String> hmParam = new HashMap<String, String>();
		hmParam.put("testCaseID",TestcaseID);
		hmParam.put("dataSheet",System.getProperty("dataSheet"));
		test1.setParameters(hmParam);
		//------------Arun
		/*Class tag*/
		List<XmlClass> lstClasses = new ArrayList<XmlClass>();
		XmlClass xmlClass = new XmlClass();
		xmlClass.setName(getClassReference(System.getProperty("user.dir")+"\\src\\test\\java\\testScripts",testClassName));
		/*if(sheetName.equals("Online"))
		{
			xmlClass.setName(getClassReference(System.getProperty("user.dir")+"\\src\\test\\java\\com\\parexel\\automation",testClassName));
		}
		else if(sheetName.equals("DB"))
		{
			xmlClass.setName(getClassReference(System.getProperty("user.dir")+"\\src\\test\\java\\com\\parexel\\automation",testClassName));
		}else if(sheetName.equals("API"))
		{
			xmlClass.setName(getClassReference(System.getProperty("user.dir")+"\\src\\test\\java\\com\\parexel\\automation",testClassName));
		}*/
		/*Exclude tag*/
		/*List<String> lstxmlExclude = new ArrayList<String>();
        for (String str : excludedMethods.split("!:!")) {
            lstxmlExclude.add(str);
        }
        xmlClass.setExcludedMethods(lstxmlExclude);*/
		/*Include tag*/
		List<XmlInclude> lstxmlInclude = new ArrayList<XmlInclude>();
		XmlInclude xmlInclude = null;
		for (String str : includedMethods.split("!:!")) {
			xmlInclude = new XmlInclude(str);
			lstxmlInclude.add(xmlInclude);
		}
		xmlClass.setIncludedMethods(lstxmlInclude);
		lstClasses.add(xmlClass);
		test1.setClasses(lstClasses);
		return test1;
	}

	public static String writeTestNGFile(XmlSuite suite) {
		String xmlPath = System.getProperty("user.dir") + "/testng.xml";
		try {
			FileWriter writer = new FileWriter(xmlPath);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(suite.toXml());
			bw.close();
		} catch (IOException e) {
			System.out.println("Exception Occurred while creating the xml file => "+e.getMessage());
		}
		return xmlPath;
	}

	public static String getClassReference(String path, String className) throws FileNotFoundException {

		File directory = new File(path);

		// Check if the directory exists
		if (directory.exists() && directory.isDirectory()) {
			File[] filesAndFolders = directory.listFiles();

			// Iterate through the list
			for (File file : filesAndFolders) {
				if (file.isFile()) {
					 String name= file.getAbsolutePath().replace("\\",".").split("java.")[1];

					if(name.endsWith(className+".java")){
						//System.out.println(filename);
						filename = name.split(".java")[0];
						return filename;
					}
				} else if (file.isDirectory()) {
					  filename= getClassReference(file.getAbsolutePath(), className);
				}
			}
		} else {
			throw new FileNotFoundException();
		}
		return filename;
	}

}