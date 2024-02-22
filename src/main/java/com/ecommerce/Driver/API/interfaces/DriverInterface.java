package com.ecommerce.Driver.API.interfaces;


import com.ecommerce.Driver.API.actions.*;
import com.ecommerce.commonUtils.ConfigReader;

import java.util.List;
import java.util.Map;


public interface DriverInterface {


	public Map<String, String> GetProperty= ConfigReader.readPropertiesFile();

	public void setHeader(String head, String val);

	public void setBody(String body);
	public void setBody(Object body);
	public void setFormParam(String key, String val);

	public void setQueryParam(String key, String val);

	public void setQueryParam(String queryParam);
	public String sendRequest();

	public Object assertIt(int code);

	public Object assertIt(String key, Object val, ValidatorOperation operation);

	public String extractString(String path);

	public int extractInt(String path);

	public List extractList(String path);

	public Object extractIt(String path);

	public String extractHeader(String header_name);

	public String getResponseString();

	public void printResp();

}
