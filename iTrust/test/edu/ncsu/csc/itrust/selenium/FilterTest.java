package edu.ncsu.csc.itrust.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Test class for logging into iTrust.
 */
public class FilterTest extends iTrustSeleniumTest {
    private HtmlUnitDriver driver;
	/*
	 * The URL for iTrust, change as needed
	 */
    /**ADDRESS*/
    public static final String ADDRESS = "http://localhost:8080/iTrust/";

    /**
     * Set up for testing.
     */
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * Tear down from testing.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Helper function for clearing all filter text fields.
     */
    public void clearAll(){
        driver.findElement(By.name("subject")).clear();
        driver.findElement(By.name("sender")).clear();
        driver.findElement(By.name("sub_neg")).clear();
        driver.findElement(By.name("sub_pos")).clear();
        driver.findElement(By.name("date_left")).clear();
        driver.findElement(By.name("date_right")).clear();
    }

    /**
     * Test if a single filter outputs the correct results
     * @throws Exception
     */
    @Test
    public void testSingleFilter() throws Exception {
        driver = (HtmlUnitDriver)login("1", "pw");
        assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");

        driver.findElement(By.linkText("Edit Message Filter")).click();
        clearAll();
        driver.findElement(By.name("sub_pos")).sendKeys("Appointment");
        driver.findElement(By.name("fSubmit")).click();
        assertTrue(driver.getPageSource().contains("Appointment"));

        driver.findElement(By.linkText("Message Outbox")).click();
        assertTrue(driver.getPageSource().contains("Gandalf Stormcrow"));
        assertTrue(driver.getPageSource().contains("Kelly Doctor"));
        assertTrue(driver.getPageSource().contains("Missed Appointment"));
    }

    /**
     * Test if multiple filters together output the correct results
     * @throws Exception
     */
    @Test
    public void testMultipleFilter() throws Exception {
        driver = (HtmlUnitDriver)login("1", "pw");
        assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");

        driver.findElement(By.linkText("Edit Message Filter")).click();
        clearAll();
        driver.findElement(By.name("subject")).sendKeys("Appointment");
        driver.findElement(By.name("sender")).sendKeys("Kelly Doctor");
        driver.findElement(By.name("sub_neg")).sendKeys("next");
        driver.findElement(By.name("fSubmit")).click();
        assertTrue(driver.getPageSource().contains("Appointment"));
        assertTrue(driver.getPageSource().contains("Kelly Doctor"));

        driver.findElement(By.linkText("Message Outbox")).click();
        assertTrue(driver.getPageSource().contains("2010-02-01 09:12"));
    }

    /**
     * Test if clearing all filters and saving them will make them all empty.
     * @throws Exception
     */
    @Test
    public void testEmptyFilter() throws Exception {
        driver = (HtmlUnitDriver)login("1", "pw");
        assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");

        driver.findElement(By.linkText("Edit Message Filter")).click();
        clearAll();
        driver.findElement(By.name("fSubmit")).click();

        assertTrue(driver.findElement(By.name("subject")).getAttribute("value").isEmpty());
        assertTrue(driver.findElement(By.name("sender")).getAttribute("value").isEmpty());
        assertTrue(driver.findElement(By.name("sub_neg")).getAttribute("value").isEmpty());
    }

    /**
     * Test if two date filters output the messages within the correct date range.
     * @throws Exception
     */
    @Test
    public void testDateFilter() throws Exception {
        driver = (HtmlUnitDriver)login("1", "pw");
        assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");

        driver.findElement(By.linkText("Edit Message Filter")).click();
        clearAll();
        driver.findElement(By.name("date_left")).sendKeys("2010-1-1");
        driver.findElement(By.name("date_right")).sendKeys("2010-1-30");
        driver.findElement(By.name("fSubmit")).click();

        driver.findElement(By.linkText("Message Outbox")).click();
        assertFalse(driver.getPageSource().contains("2009"));
        assertFalse(driver.getPageSource().contains("2010-02"));
        assertTrue(driver.getPageSource().contains("2010-01-29"));
        assertTrue(driver.getPageSource().contains("2010-01-20"));
    }

    /**
     * Test if invalid date filters will output errors and will not be saved
     * @throws Exception
     */
    @Test
    public void testInvalidDate() throws Exception {
        driver = (HtmlUnitDriver)login("1", "pw");
        assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");

        driver.findElement(By.linkText("Edit Message Filter")).click();
        clearAll();
        driver.findElement(By.name("date_left")).sendKeys("sbdgbsdgn");
        driver.findElement(By.name("date_right")).sendKeys("235462356");
        driver.findElement(By.name("fSubmit")).click();

        assertFalse(driver.getPageSource().contains("sbdgbsdgn"));
        assertFalse(driver.getPageSource().contains("235462356"));
        assertTrue(driver.getPageSource().contains("Error! First date format should be"));
        assertTrue(driver.getPageSource().contains("Error! Second date format should be"));
        assertTrue(driver.getPageSource().contains("Error! Try again."));
    }

    /**
     * Test if entering new text into filters and clicking 'cancel' button
     * will not save the new text.
     * @throws Exception
     */
    @Test
    public void testCancel() throws Exception {
        driver = (HtmlUnitDriver)login("1", "pw");
        assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");

        driver.findElement(By.linkText("Edit Message Filter")).click();
        clearAll();
        driver.findElement(By.name("subject")).sendKeys("Appointment");
        driver.findElement(By.name("sender")).sendKeys("Kelly Doctor");
        driver.findElement(By.name("sub_neg")).sendKeys("next");
        driver.findElement(By.name("fSubmit")).click();

        clearAll();
        driver.findElement(By.name("date_left")).sendKeys("sbdgbsdgn");
        driver.findElement(By.name("date_right")).sendKeys("235462356");
        driver.findElement(By.name("cSubmit")).click();
        assertTrue(driver.getPageSource().contains("Appointment"));
        assertTrue(driver.getPageSource().contains("Kelly Doctor"));
    }
}
