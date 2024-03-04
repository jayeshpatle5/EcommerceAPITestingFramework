package com.ecommerce.commonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ecommerce.Driver.API.customKeywords.APIDriverKeywords;

public class ReadjsonData {

	public static void getJsonValue(JSONObject jsonobject, String key) {

		boolean exists=jsonobject.has(key);
		Iterator<?> keys;
		String nextkeys;

		if(!exists) {
			keys=jsonobject.keys();

			while(keys.hasNext()) {
				nextkeys=(String)keys.next();

				try {
					if(jsonobject.get(nextkeys) instanceof JSONObject) {

						if(exists== false) {

							getJsonValue(jsonobject.getJSONObject(nextkeys),key);
						}
					}else if(jsonobject.get(nextkeys) instanceof JSONArray){

						JSONArray jsonarray=jsonobject.getJSONArray(nextkeys);
						for(int i=0;i<jsonarray.length();i++) {
							String jsonstring = jsonarray.get(i).toString();
							JSONObject innerjson=new JSONObject(jsonstring);

							if(exists==false) {
								getJsonValue(innerjson, key);
							}
						}
					}
				}catch(Exception e) {

				}
			}
		}else {

			System.out.println(jsonobject.get(key).toString());
		}
		//return "Value for given key is not available";
	}



	public static void main(String args[]){

		String json = "";
		try {
			json = APIDriverKeywords.getStringFromFilePath(System.getProperty("user.dir")+""+"\\src\\test\\resources\\jsonData\\accountssample.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject	jsonobject=new JSONObject(json);
		System.out.println(bodyContainsKey(jsonobject, "firstName"));
		System.out.println(getJsonValue1(jsonobject, "firstName"));
	}

	public static Collection<Object> getJsonValue1(JSONObject jsonobject, String key) {

		boolean exists=jsonobject.has(key);
		Iterator<?> keys;
		String nextkeys;
		Collection<Object> list= new ArrayList<>();

		if(!jsonobject.isEmpty()) {
			keys=jsonobject.keys();

			while(keys.hasNext()) {
				String nextkey=(String)keys.next();

				if(jsonobject.has(key) && jsonobject.get(nextkey) instanceof String) {
					if(key.equals(nextkey)) {
						list.add(jsonobject.getString(nextkey));
					}
				}else if(jsonobject.get(nextkey) instanceof JSONObject) {

					if(key.equals(nextkey)) {

						list.add(jsonobject.get(nextkey));
					}else {
						list.addAll(getJsonValue1(jsonobject.getJSONObject(nextkey), key));
					}

				}else if(jsonobject.get(nextkey) instanceof JSONArray) {

					if(key.equals(nextkey)) {
						list.add(jsonobject.get(nextkey));
					}else {

						JSONArray jsonarray=jsonobject.getJSONArray(nextkey);
						for(int i=0;i<jsonarray.length();i++) {
							String jsonstring = jsonarray.get(i).toString();
							JSONObject innerjson=new JSONObject(jsonstring);

							list.addAll(getJsonValue1(innerjson, key));
						}
					}
				}
			}
		}
		return list;

	}	

	public static boolean bodyContainsKey(JSONObject jsonobject,String key) {

		Iterator<?> iterator;
		if(!jsonobject.has(key)) {
			iterator=jsonobject.keys();

			while(iterator.hasNext()) {
				String obj=(String)iterator.next();
				try {
					if(jsonobject.get(obj) instanceof JSONObject) {

						if(bodyContainsKey(jsonobject.getJSONObject(obj), key)) {
							return true;
						}
					}else if(jsonobject.get(obj) instanceof JSONArray) {
						JSONArray jsonarr=jsonobject.getJSONArray(obj);
						for(int i=0;i<jsonarr.length();i++) {
							String json=jsonarr.get(i).toString();
							JSONObject innerjson=new JSONObject(json);
							if(bodyContainsKey(innerjson, key)) {
								return true;
							}
						}
					}
				}catch(Exception e) {

				}
			}
		}
		return jsonobject.has(key);
	}
}
