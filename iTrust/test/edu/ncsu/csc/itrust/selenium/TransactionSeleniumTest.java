package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TransactionSeleniumTest extends iTrustSeleniumTest {

    public void testDisplayRowsEmpty() {
        try {

            HtmlUnitDriver doctorDriver = (HtmlUnitDriver) iTrustSeleniumTest.login("90000000001", "pw");
            doctorDriver.get("/iTrust/auth/admin/viewLogs.jsp");
            assertEquals("iTrust - View Transaction Logs", doctorDriver.getTitle());
            doctorDriver.findElementById("startDate").sendKeys("11/11/2011");
            doctorDriver.findElementById("endDate").sendKeys("11/11/2016");
            doctorDriver.findElementById("submitRow").click();
            assertEquals("", doctorDriver.findElementById("tableDiv").getAttribute("class"));
            assertEquals("hidden", doctorDriver.findElementById("barDiv").getAttribute("class"));
            assertEquals("hidden", doctorDriver.findElementById("formDiv").getAttribute("class"));
            List<WebElement> rows = doctorDriver.findElementsByCssSelector("#rowTable tr");
            assertEquals(1, rows.size());
            doctorDriver.close();


        } catch (Exception e) {

        }
    }

    public void testNoCharts() {
        try {

            HtmlUnitDriver doctorDriver = (HtmlUnitDriver) iTrustSeleniumTest.login("90000000001", "pw");
            doctorDriver.get("/iTrust/auth/admin/viewLogs.jsp");
            assertEquals("iTrust - View Transaction Logs", doctorDriver.getTitle());
            doctorDriver.findElementById("startDate").sendKeys("11/11/2011");
            doctorDriver.findElementById("endDate").sendKeys("11/11/2016");
            doctorDriver.findElementById("submitRow").click();
            assertEquals("", doctorDriver.findElementById("barDiv").getAttribute("class"));
            assertEquals("hidden", doctorDriver.findElementById("tableDiv").getAttribute("class"));
            assertEquals("hidden", doctorDriver.findElementById("formDiv").getAttribute("class"));
            List<WebElement> rows = doctorDriver.findElementsByCssSelector(".chart");
            assertEquals(4, rows.size());
            doctorDriver.close();


        } catch (Exception e) {

        }
    }

}
