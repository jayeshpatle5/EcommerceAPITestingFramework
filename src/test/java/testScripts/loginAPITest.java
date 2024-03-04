package testScripts;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ecommerce.Driver.API.actions.ValidatorOperation;
import com.ecommerce.Driver.API.customKeywords.BaseTest;
import com.ecommerce.commonUtils.ConfigReader;
import com.ecommerce.commonUtils.ReadjsonData;
import com.ecommerce.commonUtils.Utilities;

public class loginAPITest extends BaseTest {
	
	@Parameters({ "testCaseID", "dataSheet" })
    @Test
    public void loginAPI(String testCaseID, String dataSheet) throws IOException {
		
		postEndPointUrl(testCaseID,dataSheet);
		ConfigReader.setProperties("token",extractString("token"));
		ConfigReader.setProperties("userId",extractString("userId"));
		
		
		
		assertIt(200);
		saveResponseinfile(new Object(){}.getClass().getEnclosingMethod().getName());
		assertEquals(isbodyContainsKey("src\\test\\resources\\jsonResponses\\loginAPIResp.json", "userId"),true);
		assertIt("userId","6454a713568c3e9fb16442f2",ValidatorOperation.EQUALS);
		
	}
}
