package leaf.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class HomePage extends Page {
	@FindBy(id="filter")
	private WebElement filter;

	@FindBy(xpath = "//*[@target='gsft_main' and contains(@href,'problem.do') and @data-placement='right']")
	private WebElement a_CreateNew;

	@FindBy(xpath="//h1[@class='form_header navbar-title navbar-title-twoline']")
	private WebElement newRecord;

	@FindBy(id = "problem.number")
	private WebElement probNumber;

	@FindBy(id = "lookup.problem.opened_by")
	private WebElement openedBy;

	@FindBy(id = "sys_display.problem.assignment_group")
	private WebElement assignmentGroup_input;

	@FindBy(id = "lookup.problem.assignment_group")
	private WebElement assignmentGroup;

	@FindBy(id = "lookup.problem.cmdb_ci")
	private WebElement configurationItem;

	@FindBy(xpath="//a[@role='button' and @class='glide_ref_item_link']")
	private List<WebElement> selectName;

	@FindBy(xpath = "//div[@class='nav navbar-right text-align-right']//input[@data-original-title='Skip to row']")
	private WebElement search_rows;

	@FindBy(id = "problem.description")
	private WebElement description;

	@FindBy(id = "problem.short_description")
	private WebElement short_description;

	@FindBy(xpath = "//select[@id='problem.priority']/option[@selected='SELECTED']")
	private WebElement priority;

	@FindBy(id="sys_display.problem.assigned_to")
	private WebElement assignedTo;

	@FindBy(id = "sysverb_insert_bottom")
	private WebElement submit;

	@FindBy(xpath = "//div[@class='input-group']//select")
	private WebElement selectMainPage;

	@FindBy(xpath = "//table[@id='problem_table']//td[3]/a")
	private WebElement td_probNum;

	String parentWinHandle="";

	public HomePage(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		parentWinHandle = webDriver.getWindowHandle();
	}

	public boolean isFilterPresent() {
		return filter.isDisplayed();
	}

	public boolean applyFilter() throws Exception{
		filter.sendKeys("Problem");
		a_CreateNew.click();
		return true;
	}

	public String getProblemNumber() {
		switchToParentWindow();
		switchToIframe();
		return probNumber.getAttribute("value");
	}
	public boolean setOpenedBy() {

		openedBy.click();
		switchToChildWindow();
		selectName.get(0).click();
		return true;
	}

	public boolean setProblemState() {
		switchToParentWindow();
		switchToIframe();
		Select sel = new Select(webDriver.findElement(By.id("problem.problem_state")));
		List<WebElement> options = webDriver.findElements(By.xpath("//*[@id=\"problem.problem_state\"]/option"));
		sel.selectByIndex(options.size()-1);
		return true;
	}

	public boolean setLongestProblemState() {
		Actions action = new Actions(webDriver);
		WebElement optionsList = webDriver.findElement(By.xpath("//*[@id=\"problem.problem_state\"]"));
		action.moveToElement(optionsList);
		List<WebElement> options = webDriver.findElements(By.xpath("//*[@id=\"problem.problem_state\"]/option"));
		String longestString = "";
		for(int i=0;i<options.size();i++)
		{
			if(longestString.length()<options.get(i).getText().length())
				longestString=options.get(i).getText();
		}
		System.out.println("Longest string is "+longestString);
		List<WebElement> options1 = webDriver.findElements(By.xpath("//*[@id=\"problem.problem_state\"]/option"));
		for(WebElement option : options1) {
			if (option.getText().equals(longestString)) {
				option.click();
			}
		}
		return true;
	}

	public boolean setAssignmentGroup() {

		assignmentGroup.click();
		switchToChildWindow();
		String text = selectName.get(selectName.size()-1).getText();
		selectName.get(selectName.size()-1).click();
		switchToParentWindow();
		switchToIframe();

		return true;
		//return text.equals(assignmentGroup_input.getText());
	}

	public boolean setConfigurationitem() {

		configurationItem.click();
		switchToChildWindow();
		String rows = webDriver.findElement(By.xpath("//span[@class=' list_row_number_input ']//span[contains(@id,'total_rows')]")).getText();
		rows=rows.replaceAll(",","");
		int ro = Integer.parseInt(rows);

		Random rand = new Random(100);
		int randdoo =rand.nextInt(ro);
		search_rows.sendKeys(""+randdoo+""+Keys.ENTER);//selectName.get(0).click();
		selectName.get(0).click();
		return true;
	}

	public String addDescription() {
		switchToParentWindow();
		switchToIframe();
		Calendar calendar = Calendar.getInstance();
		String descriptionText = "TESTLEAF CONTEST "+calendar.getTime().toString();
		description.sendKeys(descriptionText);
		short_description.sendKeys(descriptionText);
		return  short_description.getAttribute("value");
	}

	public String getPriority() {
		System.out.println("priority is "+priority.getText());
		return  priority.getText();
	}

	public boolean setProblemStateViaJS() {
		WebElement select = webDriver.findElement(By.id("problem.problem_state"));

		((JavascriptExecutor)webDriver).executeScript("var select = arguments[0]; for(var i = 0; i < select.options.length; i++){ if(select.options[i].text == arguments[1]){ select.options[i].selected = true; } }", select, "Open");
		return true;
	}

	public boolean setAssignedTo() throws Exception{
		assignedTo.sendKeys("Be"+Keys.ARROW_DOWN+Keys.ENTER);
		Thread.sleep(2000);
		return true;
	}

	public String submitAndVerifyProblemNumber(String desc) {
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", submit);
		submit.click();
		Select sel = new Select(selectMainPage);

		sel.selectByVisibleText("Short description");
		webDriver.findElement(By.xpath("//div[@class='input-group']//input")).sendKeys(desc+Keys.ENTER);
		System.out.println("Prob num "+td_probNum.getText());
		return td_probNum.getText();
	}

	public void switchToChildWindow() {
		Set<String> winHandles = webDriver.getWindowHandles();
		// Loop through all handles
		for(String handle: winHandles) {
			if (!handle.equals(parentWinHandle)) {
				webDriver.switchTo().window(handle);
			}
		}
	}

	public void switchToParentWindow() {

		webDriver.switchTo().window(parentWinHandle);
		webDriver.switchTo().defaultContent();

	}

	public void switchToIframe() {
		wait.until(ExpectedConditions
				.frameToBeAvailableAndSwitchToIt(By.id("gsft_main")));
	}


}
