package leaf.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Issue;
import ru.yandex.qatools.allure.annotations.TestCaseId;


public class TestLeaf extends TestBase {

	LoginPage loginpage;
	HomePage homePage;
	String problemNumber ="",description="";
	@Parameters({ "path" })
	@BeforeClass
	public void testInit(@Optional("/") String path) {
		// Load the page in the browser
		webDriver.get(websiteUrl + path);
		loginpage = PageFactory.initElements(webDriver, LoginPage.class);
		homePage = PageFactory.initElements(webDriver,HomePage.class);
	}

	/**
	 * Step 1,2,3,4,5,6 - Login
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	@Features("Login Page")
	@TestCaseId("642")
	@Issue("642")
	@Parameters({"username","password"})
	@Test(description = "Login with valid credentails")
	public void loginTest (@Optional("admin") String username,
						   @Optional("iaminADP@95") String password) throws Exception{
		loginpage.login(username, password);
		Thread.sleep(5000);
		Assert.assertTrue(homePage.isFilterPresent(),"Error home page not loaded");

	}
//Step 7,8 - Apply filter, click create new and store problem number
	@Test(dependsOnMethods = "loginTest", description = "Type filter and click create new")
	public void filterTest() throws Exception
	{
		Assert.assertTrue(homePage.applyFilter(),"Unable to apply filter");
		Thread.sleep(5000);
		problemNumber=homePage.getProblemNumber();
			}
//Step 9 - Choose first opened by
	@Test(dependsOnMethods = "filterTest", description = "Choose first opened by")
	public void chooseOpenedBy()  {
		Assert.assertTrue(homePage.setOpenedBy(),"Unable to set opened by");

	}

	//Step 10 - Number of windows is 1 and Select last option from Category
	@Test(dependsOnMethods = "chooseOpenedBy", description = "Number of windows is 1 and Select last option from Category")
	public void selectLastProblemState() {
		Assert.assertEquals(webDriver.getWindowHandles().size(),1,"The number of windows is greater than 1");
		Assert.assertTrue(homePage.setProblemState(),"Unable to set problem state");
	}

	//Step 11 - Select longest category
	@Test(dependsOnMethods = "chooseOpenedBy", description = "Select longest category ")
	public void selectLongestProblemState() {
		Assert.assertTrue(homePage.setLongestProblemState(),"Unable to set longest problem state");

	}

	//Step 12 - Select last assigned group
	@Test(dependsOnMethods = {"selectLongestProblemState","selectLastProblemState"},description = "Select last assigned group")
	public void selectLastAssignedGroup() {
		Assert.assertTrue(homePage.setAssignmentGroup(),"Unable to set assignment group");
	}

	//Step 13- Choose configuration item
	@Test(dependsOnMethods = "selectLastAssignedGroup",description = "Choose configuration item")
	public void chooseConfigItem() {
		Assert.assertTrue(homePage.setConfigurationitem(),"Unable to set configuration item");

	}

	//Step 14 -Type description and short description
	@Test(dependsOnMethods = "chooseConfigItem", description = "Type description and short description")
	public void addDescriptionAndShortDesc() {
		description = homePage.addDescription();
		Assert.assertTrue(description.contains("TESTLEAF CONTEST"),"Error in descritpion");

	}

	//Step 15 - Confirm Priority
	@Test(dependsOnMethods = "chooseConfigItem", description = "Confirm Priority is Default 5")
	public void confirmPriority() {
		Assert.assertEquals(homePage.getPriority(),"5 - Planning","Priorities do not match");

	}

	//Step 16 - Set Problem State
	@Test(dependsOnMethods = "chooseConfigItem", description = "Set problem state usin JS")
	public void setProblemStateViaJS() {
		Assert.assertTrue(homePage.setProblemStateViaJS(),"Problem state is not open");
	}

	//Step 17 - Type and choose assigned to
	@Test(dependsOnMethods = "chooseConfigItem", description = "Type and choose assigned to")
	public void typeAndChooseAssignedTo() throws Exception {
		Assert.assertTrue(homePage.setAssignedTo(),"Assigned to mismatch");

	}

	//Step 18,19 - Submit , search and verify the problem

	@Test(dependsOnMethods = {"chooseOpenedBy","selectLastProblemState","selectLongestProblemState","selectLastAssignedGroup","chooseConfigItem"
	,"addDescriptionAndShortDesc","confirmPriority","setProblemStateViaJS","typeAndChooseAssignedTo"},description = "Submit, search and verify the problem")
	public void submitAndVerifyProbNo() {
		Assert.assertEquals(homePage.submitAndVerifyProblemNumber(description),problemNumber,"Problem number does not match");

	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		webDriver.quit();
	}
}
