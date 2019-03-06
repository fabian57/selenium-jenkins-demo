import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SeleniumHelloWorld {
	private WebDriver driver;

	private String baseUrl;

	@BeforeClass
	public void setupClass() {
		// Notwendig wenn geckodriver nicht im Pfad ist
		System.setProperty("webdriver.gecko.driver", "lib/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
		driver = new FirefoxDriver(options);
		//baseUrl = "http://127.0.0.1/index.html";
		//baseUrl = "http://192.168.1.33/index.html";
		baseUrl = "http://10.9.40.140/index.html";

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@BeforeMethod
	public void setupMethod() {
		driver.get(baseUrl);
	}

	@Test
	public void testWorkingLink() {
		WebElement workingLink = driver.findElement(By.id("working"));
		workingLink.click();

		boolean condition = driver.getPageSource().contains("Success");
		Assert.assertTrue(condition);
	}

	// Only works on running server because the cookie is only valid on the domain
	// With the current implementation of the demo site, the cookie test happens on a different page than index
	@Test
	public void testBrokenLink() {
		WebElement brokenLink = driver.findElement(By.linkText("This link should be broken"));
		// WebElement brokenLink = driver.findElement(By.partialLinkText("be broken"));
		brokenLink.click();

		boolean condition = driver.getPageSource().contains("Success");
		Assert.assertTrue(!condition);
	}

	@Test
	public void testCookieLink() {
		Cookie cookie = new Cookie("selenium", "it worked");
		driver.manage().addCookie(cookie);

		WebElement cookieLink = driver.findElement(By.id("cookie"));
		cookieLink.click();

		String text = driver.findElement(By.cssSelector("p")).getText();

		boolean condition = text.contains("Value of selenium cookie is");
		Assert.assertTrue(condition);
		driver.manage().deleteCookie(cookie);
	}

	@Test(priority = 1)
	public void testCreateUser() {
		WebElement name = driver.findElement(By.id("Name"));
		WebElement address = driver.findElement(By.id("Address"));
		Select cities = new Select(driver.findElement(By.tagName("select")));
		WebElement age = driver.findElement(By.id("Age"));
		WebElement male = driver.findElement(By.id("Male"));
		WebElement newsletter = driver.findElement(By.id("Newsletter"));
		WebElement submit = driver.findElement(By.id("create"));

		name.sendKeys("Foo");
		address.sendKeys("Bar");
		cities.selectByVisibleText("Metz");
		age.sendKeys("42");
		male.click();
		newsletter.click();
		submit.click();
		
		String expectedRow = "Foo Bar Metz 42 Male Yes";
		
		boolean condition = driver.findElement(By.xpath("//table")).getText()
				.contains(expectedRow);
		Assert.assertTrue(condition);
	}

	@Test(priority = 2)
	public void testDeleteUser() {
		WebElement name = driver.findElement(By.id("ID_Delete"));
		WebElement submit = driver.findElement(By.id("delete"));

		name.sendKeys("0");
		submit.click();

		boolean condition = driver.findElement(By.xpath("//header")).getText().contains("User deleted successfully");
		Assert.assertTrue(condition);
	}

	@AfterMethod
	public void sleep() throws InterruptedException {
		Thread.sleep(3000);
	}

	@AfterClass
	public void teardown() throws InterruptedException {
		driver.close();
	}
}
