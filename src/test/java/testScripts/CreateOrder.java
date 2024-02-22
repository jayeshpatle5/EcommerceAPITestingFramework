package testScripts;

import java.io.IOException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ecommerce.Driver.API.customKeywords.BaseTest;

public class CreateOrder extends BaseTest{

	
	@Parameters({ "testCaseID", "dataSheet" })
    @Test
    public void createnewOrder(String testCaseID, String dataSheet) throws IOException {
		
		postEndPointUrl(testCaseID,dataSheet);
		
		assertIt(201);
	}
}
