package com.ecommerce.commonUtils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {
    Logger logger = LoggerHelper.getLogger(ConfigReader.class);
    static String  path=System.getProperty("user.dir")+"\\src\\test\\resources\\config\\config.properties";


    public static String loadProperties(String property) {
        Properties prop = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(path);

            // loads a properties file

            prop.load(input);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return prop.getProperty(property);
    }

    public static Map<String, String> readPropertiesFile() {
        Properties properties = new Properties();
        Map<String, String> propertyMap = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);

            // Convert the Properties object to a Map
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                propertyMap.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyMap;
    }
    
    public static void setProperties(String property,String propertyvalue) {
    	
    	 Properties prop = new Properties();
         InputStream input;
         try {
             input = new FileInputStream(path);

             prop.load(input);
             prop.setProperty(property, propertyvalue);
             OutputStream op = new FileOutputStream(path);
     		prop.store(op, "");
         } catch (Exception e) {
             
             e.printStackTrace();
         }
    }
}
