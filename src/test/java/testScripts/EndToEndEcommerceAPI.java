package testScripts;

import static io.restassured.RestAssured.given;
//file
import java.io.File;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class EndToEndEcommerceAPI {
	static String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NDU0YTcxMzU2OGMzZTlmYjE2NDQyZjIiLCJ1c2VyRW1haWwiOiJqYXllc2hAZ21haWwuY29tIiwidXNlck1vYmlsZSI6MTIzNDU2Nzg5MCwidXNlclJvbGUiOiJjdXN0b21lciIsImlhdCI6MTY5NTcwODY1MiwiZXhwIjoxNzI3MjY2MjUyfQ.t6G6hr2eGPHbkJQxsBkDHgCvwbXeJHkJS7ybvrJJxaA";
	static String userId = "6454a713568c3e9fb16442f2";
	static String productId = "651276c77244490f95b9ccd1";
	static String orderId= "";

	static RequestSpecification reqspecfication;

	// Register Page =https://rahulshettyacademy.com/client
	public static RequestSpecification reqspecificationOb() {
		reqspecfication = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		return reqspecfication;
	}
	public static void loginRequest() {
		//		String str = given().header("Content-Type", "application/json")
		//				.body("{\r\n" + "    \"userEmail\": \"jayesh@gmail.com\",\r\n"
		//						+ "    \"userPassword\": \"Jayesh123#\"\r\n" + "}")
		//				.when().post("/api/ecom/auth/login").then().extract().response().asPrettyString();

		String str = given().spec(reqspecificationOb()).body("{\r\n" + "    \"userEmail\": \"jayesh@gmail.com\",\r\n"
				+ "    \"userPassword\": \"Jayesh123#\"\r\n" + "}")
				.when().post("/api/ecom/auth/login").then().extract().response().asPrettyString();
		JsonPath js = new JsonPath(str);
		token = js.getString("token");
		userId = js.getString("userId");
		System.out.println(token);
		System.out.println(userId);
	}

	public static void createProduct() {

		String str = given().header("Content-Type", "multipart/form-data; boundary=<calculated when request is sent>")
				.header("Authorization", token).multiPart("productName", "qwerty")
				.multiPart("productAddedBy", userId).multiPart("productCategory", "fashion")
				.multiPart("productSubCategory", "shirts").multiPart("productPrice", "11500")
				.multiPart("productDescription", "Addias Originals").multiPart("productFor", "women")
				.multiPart("productImage", new File("D:\\Downloads\\smartphone.jpg")).when()
				.post("/api/ecom/product/add-product")
				.then().extract().response().asPrettyString();

		JsonPath js = new JsonPath(str);
		productId = js.getString("productId");
		System.out.println(str);

	}

	public static void createOrder() {
		String str = given().spec(reqspecificationOb()).header("Authorization", token)
				.body("{\r\n" + "    \"orders\": [\r\n" + "        {\r\n" + "            \"country\": \"India\",\r\n"
						+ "            \"productOrderedId\": \"" + productId + "\"\r\n" + "        }\r\n" + "    ]\r\n"
						+ "}")
				.when().post("/api/ecom/order/create-order").then().extract().response().asPrettyString();
		JsonPath js=new JsonPath(str);
		//js.get()
		System.out.println(str);
	}

	public static void deleteProduct() {

		String str = given().spec(reqspecificationOb()).header("Authorization", token).when()
				.delete("/api/ecom/product/delete-product/" + productId).then().extract().response().asPrettyString();

		System.out.println(str);
	}

	public static String getAllProductPayload() {
		return "{\r\n" + "  \"productName\": \"\",\r\n" + "  \"minPrice\": null,\r\n" + "  \"maxPrice\": null,\r\n"
				+ "  \"productCategory\": [],\r\n" + "  \"productSubCategory\": [],\r\n" + "  \"productFor\": []\r\n"
				+ "}";
	}
	public static void getAllProduct() {

		String str = given().spec(reqspecificationOb()).header("Authorization", token)
				.when().post("/api/ecom/product/get-all-products").then()
				.extract().response().asPrettyString();

		System.out.println(str);
	}

	public static void deleteOrder() {
		
	String res=	given().spec(reqspecificationOb()).header("Authorization", token)
		        .when().delete("/api/ecom/order/delete-order/651277827244490f95b9ce24")
		        .then().extract().response().asPrettyString();
	System.out.println(res);
	}
	public static void getOrderDetails() {
		
	String res=	given().spec(reqspecificationOb()).header("Authorization", token)
		       .when().get("/api/ecom/order/get-orders-details?id=651278537244490f95b9cf53")
		       .then().extract().response().asPrettyString();
		System.out.println(res);
	}

	public static void main(String[] args) {

			RestAssured.baseURI = "https://rahulshettyacademy.com";


		//loginRequest();
		// getAllProduct();
		//createOrder();
		//deleteOrder();
		//createProduct();
		//		deleteProduct();
			getOrderDetails();
	}

}
