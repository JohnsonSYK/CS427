package edu.ncsu.csc.itrust.selenium;

import junit.framework.TestCase;
import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class ReminderSeleniumTest extends TestCase {

    @Test
    public void sendNotificationTest() {
        try {
            HtmlUnitDriver doctorDriver = (HtmlUnitDriver) iTrustSeleniumTest.login("90000000000", "pw");
            doctorDriver.get("/iTrust/auth/hcp/sendApptReminder.jsp");
            assertEquals("iTrust - View My Appointment Requests", doctorDriver.getTitle());
            doctorDriver.findElementById("numDaysInput").sendKeys("3");
            doctorDriver.findElementById("submitDays").click();
            assertEquals("", doctorDriver.findElementById("doneDiv").getAttribute("class"));
            doctorDriver.close();

            HtmlUnitDriver driver = (HtmlUnitDriver) iTrustSeleniumTest.login("1", "pw");
            assertTrue(driver.getTitle().equals("iTrust - Patient Home"));
        } catch (Exception e) {
        }
    }
}
