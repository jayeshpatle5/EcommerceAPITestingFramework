package testScripts;

import java.io.IOException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ecommerce.Driver.API.customKeywords.BaseTest;
import com.ecommerce.commonUtils.ConfigReader;

public class loginAPITest extends BaseTest {
	
	@Parameters({ "testCaseID", "dataSheet" })
    @Test
    public void loginAPI(String testCaseID, String dataSheet) throws IOException {
		
		postEndPointUrl(testCaseID,dataSheet);
		ConfigReader.setProperties("token",extractString("tokean"));
		ConfigReader.setProperties("userId",extractString("userId"));
		
		
		
		assertIt(200);
	}
}
