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

        String returnedMID = result.getText().substring(result.getText().length()-3);
        driver = login(returnedMID, "pw");

        WebElement loginMessage = driver.findElement(By.id("iTrustContent")).findElement(By.tagName("h2"));
        assertTrue(loginMessage.getText().compareTo("Welcome Test Test! As a pre-registered patient you will need a HCP to activate your account in order to use iTrust") == 0);
    }

    /**
     * Test failed pre-registration due to email already being used
     */
    public void testUsedEmailError() throws Exception {
        openPreRegisterForm();

        driver.findElement(By.name("p_fname")).sendKeys("Test");
        driver.findElement(By.name("p_lname")).sendKeys("Test");
        driver.findElement(By.name("p_email")).sendKeys("nobody@gmail.com");
        driver.findElement(By.name("p_password")).sendKeys("pw");
        driver.findElement(By.name("p_verify")).sendKeys("pw");

        driver.findElement(By.name("p_fname")).submit();

        assertTrue(driver.findElements(By.className("iTrustError")).size() > 0);
    }

    /**
     * Test failed pre-registration due to invalid email input
     */
    public void testInvalidEmailError() throws Exception {
        openPreRegisterForm();

        driver.findElement(By.name("p_fname")).sendKeys("Test");
        driver.findElement(By.name("p_lname")).sendKeys("Test");
        driver.findElement(By.name("p_email")).sendKeys("nobody");
        driver.findElement(By.name("p_password")).sendKeys("pw");
        driver.findElement(By.name("p_verify")).sendKeys("pw");

        driver.findElement(By.name("p_fname")).submit();

        assertTrue(driver.findElements(By.className("iTrustError")).size() > 0);
    }

    /**
     * Test failed pre-registration due to password verification mismatch
     */
    public void testPasswordError() throws Exception {
        openPreRegisterForm();

        driver.findElement(By.name("p_fname")).sendKeys("Test");
        driver.findElement(By.name("p_lname")).sendKeys("Test");
        driver.findElement(By.name("p_email")).sendKeys("nobody@gmail.com");
        driver.findElement(By.name("p_password")).sendKeys("pw");
        driver.findElement(By.name("p_verify")).sendKeys("blah");

        driver.findElement(By.name("p_fname")).submit();

        assertTrue(driver.findElements(By.className("iTrustError")).size() > 0);
    }

    /**
     * Test failed pre-registration due to missing inputs
     */
    public void testEmptyFieldsError() throws Exception {
        openPreRegisterForm();

        driver.findElement(By.name("p_fname")).sendKeys("Test");
        driver.findElement(By.name("p_lname")).sendKeys("Test");
        driver.findElement(By.name("p_email")).sendKeys("nobody@gmail.com");
        driver.findElement(By.name("p_password")).sendKeys("pw");
        driver.findElement(By.name("p_verify")).sendKeys("pw");

        driver.findElement(By.name("p_fname")).submit();

        assertTrue(driver.findElements(By.className("iTrustError")).size() > 0);
    }
}
