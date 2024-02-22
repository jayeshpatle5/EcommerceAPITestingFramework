package com.ecommerce.Driver.API.customKeywords;
import com.ecommerce.Driver.API.actions.*;
import com.ecommerce.Testdata.excel.ExcelDataReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


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
			setHeader(data.get("Headers").split(":")[0].trim(), data.get("Headers").split(":")[1].trim());
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


	public String getStringFromFilePath(String path) throws IOException {
		System.out.println(path);
		Path fileName = Path.of(path);
		return Files.readString(fileName);

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

	public String extractString(String path) { return resp.jsonPath().getString(path);}
	
	public int extractInt(String path) { return resp.jsonPath().getInt(path);}

	public List<?> extractList(String path) { return resp.jsonPath().getList(path);}

	public Object extractIt(String path) { return resp.jsonPath().get(path); }

	public String extractHeader(String header_name) { return resp.header(header_name);}

	public String getResponseString() { return resp.asString();}

	public void printResp() { resp.print();}
	



}
