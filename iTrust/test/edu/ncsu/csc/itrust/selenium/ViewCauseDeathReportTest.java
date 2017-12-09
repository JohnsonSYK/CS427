package edu.ncsu.csc.itrust.selenium;

import java.util.concurrent.TimeUnit;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests UC 20
 */
public class ViewCauseDeathReportTest extends iTrustSeleniumTest {
    private HtmlUnitDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
        driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    /**
     * The Test
     *  * Sets up test data
     *  * Logs in as an mid 9000000003L
     *  * Goes to the cause-of-death report page
     *  * Fills out the form, with starting + ending year
     *  * Expects Results
     * @throws Exception
     */
    @Test
    public void testGenerateReport1() throws Exception {
        driver = (HtmlUnitDriver)login("9000000003", "pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000003L, 0L, "");
        driver.findElement(By.linkText("View Cause-of-Death Trends Report")).click();
        driver.findElement(By.name("startingYear")).sendKeys("1900");
        driver.findElement(By.name("endingYear")).sendKeys("2020");
        driver.findElement(By.name("fSubmit")).click();
        assertTrue(driver.getPageSource().contains("Diabetes"));
        assertTrue(driver.getPageSource().contains("250.10"));
    }

    /**
     * The Test
     *  * Sets up test data
     *  * Logs in as an mid 9000000003L
     *  * Goes to the cause-of-death report page
     *  * Fills out the form, with starting + ending year
     *  * Expects Results
     * @throws Exception
     */
    @Test
    public void testGenerateReport2() throws Exception {
        driver = (HtmlUnitDriver)login("9000000003", "pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000003L, 0L, "");
        driver.findElement(By.linkText("View Cause-of-Death Trends Report")).click();
        driver.findElement(By.name("startingYear")).sendKeys("1900");
        driver.findElement(By.name("endingYear")).sendKeys("2000");
        driver.findElement(By.name("fSubmit")).click();
        assertFalse(driver.getPageSource().contains("Diabetes"));
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