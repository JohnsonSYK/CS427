package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

public class ViewPrescriptionTrendsTest extends iTrustSeleniumTest {
	private HtmlUnitDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * The Test
	 *  * Sets up test data
	 *  * Logs in as an mid 9000000003L
	 *  * Goes to the prescription trends page
	 *  * Fills out the form, with all necessary fields
	 *  * Expects Normal Results
	 * @throws Exception
	 */
	@Test
	public void testGenerateReport1() throws Exception {
		driver = (HtmlUnitDriver)login("9000000003", "pw");
		driver.findElement(By.linkText("View Prescription Trends Report")).click();
		driver.findElement(By.name("startDate")).sendKeys("01/01/1980");
		driver.findElement(By.name("endDate")).sendKeys("10/10/2020");
		new Select(driver.findElement(By.name("icdCode"))).selectByVisibleText("250.10 - Diabetes with ketoacidosis");
		new Select(driver.findElement(By.name("gender"))).selectByValue("Male");
		driver.findElement(By.name("fSubmit")).click();
		assertTrue(driver.getPageSource().contains("5 units"));
		assertTrue(driver.getPageSource().contains("Tetracycline"));
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("Take twice daily"));
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertFalse(driver.getPageSource().contains("Eat like"));
	}

	/**
	 * The Test
	 *  * Sets up test data
	 *  * Logs in as an mid 9000000003L
	 *  * Goes to the prescription trends page
	 *  * Fills out the form, with all necessary fields (except start and end dates flipped)
	 *  * Expects Negative Results
	 * @throws Exception
	 */
	@Test
	public void testGenerateReport2() throws Exception {
		driver = (HtmlUnitDriver)login("9000000003", "pw");
		driver.findElement(By.linkText("View Prescription Trends Report")).click();
		driver.findElement(By.name("startDate")).sendKeys("01/01/2020");
		driver.findElement(By.name("endDate")).sendKeys("10/10/1999");
		new Select(driver.findElement(By.name("icdCode"))).selectByVisibleText("250.10 - Diabetes with ketoacidosis");
		new Select(driver.findElement(By.name("gender"))).selectByValue("Male");
		driver.findElement(By.name("fSubmit")).click();
		assertFalse(driver.getPageSource().contains("units"));
		assertFalse(driver.getPageSource().contains("Kelly Doctor"));
		assertFalse(driver.getPageSource().contains("Take twice daily"));
	}


	/**
	 * The Test
	 *  * Sets up test data
	 *  * Logs in as an mid 9000000003L
	 *  * Goes to the prescription trends page
	 *  * Fills out the form, with all necessary fields
	 *  * Expects Normal Results
	 * @throws Exception
	 */
	@Test
	public void testGenerateReport3() throws Exception {
		driver = (HtmlUnitDriver)login("9000000003", "pw");
		driver.findElement(By.linkText("View Prescription Trends Report")).click();
		driver.findElement(By.name("startDate")).sendKeys("11/11/1983");
		driver.findElement(By.name("endDate")).sendKeys("5/5/2018");
		new Select(driver.findElement(By.name("icdCode"))).selectByVisibleText("493.00 -");
		new Select(driver.findElement(By.name("gender"))).selectByValue("All");
		driver.findElement(By.name("fSubmit")).click();
		assertTrue(driver.getPageSource().contains("99"));
		assertTrue(driver.getPageSource().contains("Tetracycline"));
		assertTrue(driver.getPageSource().contains("Aspirin"));
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("Take twice daily"));
		assertTrue(driver.getPageSource().contains("Eat like "));
		assertFalse(driver.getPageSource().contains("Andy Programmer"));
	}


	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
