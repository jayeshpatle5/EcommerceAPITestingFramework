package com.ecommerce.Driver.API.customKeywords;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.ecommerce.Driver.API.actions.HttpOperation;
import com.ecommerce.Driver.API.actions.ValidatorOperation;
import com.ecommerce.Testdata.excel.ExcelDataReader;
import com.ecommerce.commonUtils.ConfigReader;
import com.ecommerce.commonUtils.ReadjsonData;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public abstract class APIDriverKeywords extends ExcelDataReader {

	ExcelDataReader dataReader = new ExcelDataReader();
	LinkedHashMap<String, String> data ;
	RequestSpecification reqSpec;
	HttpOperation method;
	String url;
	Response resp;




	public void initEndPointURL(HttpOperation method, String baseUrl, String url) {

		try {
			RestAssured.baseURI = baseUrl;
			this.url = url;
			this.method = method;
			reqSpec = given();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public String postEndPointUrl( String tcID, String dataSheet) throws IOException {
		data= dataReader.readTCBinding(tcID,dataSheet);
		initEndPointURL( HttpOperation.POST,data.get("BaseUrl"),data.get("ResourceUrl")); 	// Reading base URI and initialising Given()

		if(!(data.get("PathParams").equalsIgnoreCase(null) || data.get("PathParams").equalsIgnoreCase(""))){
			this.url = this.url+data.get("PathParams");
		}
		
		if(!(data.get("Headers").equalsIgnoreCase(null) || data.get("Headers").equalsIgnoreCase(""))) {
			String[] arr= data.get("Headers").split(";");
			for(int i=0;i<arr.length;i++) {
				String temp=arr[i].split(":")[0].trim();
				if(temp.equals("Authorization")) {
					setHeader(temp,ConfigReader.loadProperties("token"));
				}else {
			setHeader(temp, arr[i].split(":")[1].trim());
				}
			}
		}
		if(!(data.get("PayloadPath").equalsIgnoreCase(null) || data.get("PayloadPath").equalsIgnoreCase(""))) {
			setBody(getStringFromFilePath(System.getProperty("user.dir") + "\\" + data.get("PayloadPath")));
		}

		return sendRequest();
	}
	public String putEndPointUrl( String tcID, String dataSheet) throws IOException {
		data= dataReader.readTCBinding(tcID,dataSheet);
		initEndPointURL( HttpOperation.PUT,data.get("BaseUrl"),data.get("ResourceUrl")); 	// Reading base URI and initialising Given()

		if(!(data.get("PathParams").equalsIgnoreCase(null) || data.get("PathParams").equalsIgnoreCase(""))){
			this.url = this.url+data.get("PathParams");
		}
		if(!(data.get("Headers").equalsIgnoreCase(null) || data.get("Headers").equalsIgnoreCase(""))) {
			setHeader(data.get("Headers").split(":")[0].trim(), data.get("Headers").split(":")[1].trim());
		}
		if(!(data.get("PayloadPath").equalsIgnoreCase(null) || data.get("PayloadPath").equalsIgnoreCase(""))) {
			setBody(getStringFromFilePath(System.getProperty("user.dir") + "\\" + data.get("PayloadPath")));
			//setBody(getjsonDataFromFilePath(System.getProperty("user.dir") + "\\" + data.get("PayloadPath")));
		}

		return sendRequest();
	}

	public String getEndPointUrl( String tcID, String dataSheet){
		data= dataReader.readTCBinding(tcID,dataSheet);
		initEndPointURL( HttpOperation.GET,data.get("BaseUrl"),data.get("ResourceUrl")); 	// Reading base URI and initialising Given()

		if(!(data.get("PathParams").equalsIgnoreCase(null) || data.get("PathParams").equalsIgnoreCase(""))){
			this.url = this.url+data.get("PathParams");
		}
		if(!(data.get("QueryParams").equalsIgnoreCase(null) || data.get("QueryParams").equalsIgnoreCase(""))) {
			setQueryParam(data.get("QueryParams"));
		}
		return sendRequest();

	}

	public String deleteEndPointUrl( String tcID, String dataSheet){
		data= dataReader.readTCBinding(tcID,dataSheet);
		initEndPointURL( HttpOperation.DELETE,data.get("BaseUrl"),data.get("ResourceUrl")); 	// Reading base URI and initialising Given()

		if(!(data.get("PathParams").equalsIgnoreCase(null) || data.get("PathParams").equalsIgnoreCase(""))){
			this.url = this.url+data.get("PathParams");
		}
		if(!(data.get("QueryParams").equalsIgnoreCase(null) || data.get("QueryParams").equalsIgnoreCase(""))) {
			setQueryParam(data.get("QueryParams"));
		}
		return sendRequest();

	}


	public static String getStringFromFilePath(String path) throws IOException {
		System.out.println(path);
		Path fileName = Path.of(path);
		return Files.readString(fileName);

	}

    public String getjsonDataFromFilePath(String path) throws IOException {
    	
    	Path fileName=Path.of(path);
		return Files.readString(fileName);
    }
    
    public Collection<Object> getjsonvalueusingkey(String path, String key) throws IOException {
    	String source=getStringFromFilePath(System.getProperty("user.dir")+"\\");
        JSONObject json=new JSONObject(source);
    	
		return ReadjsonData.getJsonValue1(json,key);
    }
    
    public boolean isbodyContainsKey(String path, String key) throws IOException {
    	//String s=System.getProperty("user.dir");
    	String source=getStringFromFilePath("D:\\eclipse workspace\\ECommerceAPI1\\"+path);
    	System.out.println(source);
        JSONObject json=new JSONObject(source);
        return ReadjsonData.bodyContainsKey(json, key);
    }
	public void setHeader(String head, String val) { reqSpec.header(head, val);}

	public void setBody(String body) {
		reqSpec.body(body);
	}



	public void setBody(Object body) { reqSpec.body(body); }


	public void setFormParam(String key, String val) { reqSpec.formParam(key, val);}



	public void setPathParam(String key, String val) { reqSpec.pathParam(key, val);}

	public void setQueryParam(String key, String val) { reqSpec.queryParam(key, val);}

	public void setQueryParam(String queryParam) {
		//username={{login}}&password={{password}}&grant_type=password&client=dnb-connector
		//reqSpec.queryParam(queryParam.split("=")[0].trim(), queryParam.split("=")[1].trim());
		String[] params = queryParam.split("&");
		Map<String, String> map = new HashMap<>();

		/*if(queryParam.contains("&")){
			params = queryParam.split("&");
		}
		else{
			params[0] = queryParam;
		}*/

		for(int i=0;i<params.length;i++){
			map.put(params[i].split("=")[0].trim(), params[i].split("=")[1].trim());

		}
		reqSpec.queryParam(map.toString());


	}

	public String sendRequest() { //By now URL body header is added in reqSpec and sends the request
		if (method.toString().equalsIgnoreCase("get")) {
			resp = reqSpec.get(url);
			return resp.asString();
		} else if (method.toString().equalsIgnoreCase("post")) {
			resp = reqSpec.post(url);
			return resp.asString();
		} else if (method.toString().equalsIgnoreCase("patch")) {
			resp = reqSpec.patch(url);
			return resp.asString();
		} else if (method.toString().equalsIgnoreCase("put")) {
			resp = reqSpec.put(url);
			return resp.asString();
		} else if (method.toString().equalsIgnoreCase("delete")) {
			resp = reqSpec.delete(url);
			return resp.asString();
		}
		return "invalid method set for API";
	}

	public APIDriverKeywords assertIt(String key, Object val, ValidatorOperation operation) {

		switch (operation.toString()) {
		case "EQUALS":
			resp.then().body(key, equalTo(val));
			break;

		case "KEY_PRESENTS":
			resp.then().body(key, hasKey(key));
			break;

		case "HAS_ALL":
			break;

		case "NOT_EQUALS":
			resp.then().body(key, not(equalTo(val)));
			break;

		case "EMPTY":
			resp.then().body(key, empty());
			break;

		case "NOT_EMPTY":
			resp.then().body(key, not(emptyArray()));
			break;

		case "NOT_NULL":
			resp.then().body(key, notNullValue());
			break;

		case "HAS_STRING":
	        resp.then().body(key, containsString((String)val));
	        break;

		case "SIZE":
	        resp.then().body(key, hasSize((int)val));
	        break;
		}

		return this;
	}



	public APIDriverKeywords assertIt(int code) {

		resp.then().statusCode(code);

		return this;
	}
    public void saveResponseinfile(String methodname) throws StreamWriteException, DatabindException, IOException {
    	ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(new File(System.getProperty("user.dir")+ConfigReader.loadProperties("JsonResFilepath")+methodname+"Resp.json"),resp.asString());
    }
    
	public String extractString(String path) { return resp.jsonPath().getString(path);}
	
	public int extractInt(String path) { return resp.jsonPath().getInt(path);}

	public List<?> extractList(String path) { return resp.jsonPath().getList(path);}

	public Object extractIt(String path) { return resp.jsonPath().get(path); }

	public String extractHeader(String header_name) { return resp.header(header_name);}

	public String getResponseString() { return resp.asString();}

	public void printResp() { resp.print();}
	



}
