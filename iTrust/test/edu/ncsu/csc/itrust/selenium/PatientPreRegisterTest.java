package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class PatientPreRegisterTest extends iTrustSeleniumTest {
    private WebDriver driver = null;

    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    protected void tearDown() throws Exception {
        gen.clearAllTables();
    }

    private void openPreRegisterForm() {
        driver = new Driver();
        driver.get(ADDRESS);
        driver.findElement(By.linkText("Pre-Register")).click();
    }

    /**
     * Test successful pre-registration with only the required fields
     */
    public void testSuccessRequiredOnly() throws Exception {
        openPreRegisterForm();

        driver.findElement(By.name("p_fname")).sendKeys("Test");
        driver.findElement(By.name("p_lname")).sendKeys("Test");
        driver.findElement(By.name("p_email")).sendKeys("valid@gmail.com");
        driver.findElement(By.name("p_password")).sendKeys("pw");
        driver.findElement(By.name("p_verify")).sendKeys("pw");

        driver.findElement(By.name("p_fname")).submit();

        WebElement result = driver.findElement(By.id("iTrustContent")).findElement(By.tagName("h3"));
        assertTrue(result.getText().contains("Pre-registration successful! Your MID is "));
    }

    /**
     * Test successful pre-registration with only the required fields
     */
    public void testInvalidEmailError() throws Exception {
        openPreRegisterForm();

        driver.findElement(By.name("p_fname")).sendKeys("Test");
        driver.findElement(By.name("p_lname")).sendKeys("Test");
        driver.findElement(By.name("p_email")).sendKeys("nobody@gmail.com");
        driver.findElement(By.name("p_password")).sendKeys("pw");
        driver.findElement(By.name("p_verify")).sendKeys("pw");

        driver.findElement(By.name("p_fname")).submit();

        assertEquals(1, driver.findElements(By.className("iTrustError")).size());
    }
}
