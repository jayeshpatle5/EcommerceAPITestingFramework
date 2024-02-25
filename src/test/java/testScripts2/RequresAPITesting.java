package testScripts2;

import java.io.IOException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ecommerce.Driver.API.customKeywords.BaseTest;
import com.ecommerce.commonUtils.ConfigReader;

public class RequresAPITesting extends BaseTest{
	
	@Parameters({ "testCaseID", "dataSheet" })
    @Test
    public void loginAPI(String testCaseID, String dataSheet) throws IOException {
		
		
		
		
		
		assertIt(200);
	}

}
