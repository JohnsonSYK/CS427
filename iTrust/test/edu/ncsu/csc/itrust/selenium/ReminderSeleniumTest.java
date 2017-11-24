package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.sql.Timestamp;
import java.util.Date;

public class ReminderSeleniumTest extends iTrustSeleniumTest {

    @Test
    public void sendNotificationTest() {
        try {
            DAOFactory factory = DAOFactory.getProductionInstance();
            ApptDAO apptDAO = factory.getApptDAO();
            ApptBean bean = new ApptBean();
            bean.setApptType("void");
            bean.setDate(new Timestamp((new Date()).getTime()));
            bean.setComment("Comment");
            bean.setHcp(90000000000L);
            apptDAO.scheduleAppt();
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
