package testScripts;

import java.io.IOException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ecommerce.Driver.API.actions.ValidatorOperation;
import com.ecommerce.Driver.API.customKeywords.BaseTest;
import com.ecommerce.commonUtils.ConfigReader;
import com.ecommerce.commonUtils.Utilities;

public class loginAPITest extends BaseTest {
	
	@Parameters({ "testCaseID", "dataSheet" })
    @Test
    public void loginAPI(String testCaseID, String dataSheet) throws IOException {
		
		postEndPointUrl(testCaseID,dataSheet);
		ConfigReader.setProperties("token",extractString("token"));
		ConfigReader.setProperties("userId",extractString("userId"));
		
		
		
		assertIt(200);
		assertIt("userId","6454a713568c3e9fb16442f2",ValidatorOperation.EQUALS);
		saveResponseinfile(new Object(){}.getClass().getEnclosingMethod().getName());
	}
}
