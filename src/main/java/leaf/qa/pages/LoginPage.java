package leaf.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.allure.annotations.Step;

public class LoginPage extends Page {
	
	//WebElements
	@FindBy(css = "#user_name")
	private WebElement userName;

	@FindBy(id = "user_password")
	private WebElement password;

	@FindBy(id = "sysverb_login")
	private WebElement loginbtn;

	public LoginPage(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
	}

	//Methods
	@Step
	public HomePage login(String uName, String passw){
		webDriver.switchTo().defaultContent();
		wait.until(ExpectedConditions
				.frameToBeAvailableAndSwitchToIt(By.id("gsft_main")));

		userName.sendKeys(uName);
		password.sendKeys(passw);
		loginbtn.click();
		return new HomePage(webDriver);
	}

}
