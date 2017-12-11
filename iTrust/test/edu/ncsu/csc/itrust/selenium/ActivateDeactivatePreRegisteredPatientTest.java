package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ActivateDeactivatePreRegisteredPatientTest extends iTrustSeleniumTest {
    private WebDriver driver = null;

    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    protected void tearDown() throws Exception {
        gen.clearAllTables();
    }

    public void testActivatePreRegisteredPatient() throws Exception {
        try {
            long pid = preRegisterPatient("Test", "Test", "test1@gmail.com", "pw");
            driver = login("9000000000", "pw");
            driver.findElement(By.linkText("Pre-Registered Patients")).click();
            List<WebElement> rows = driver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
            assertTrue(rows.size() > 0);

            int initialNumRows = rows.size();
            boolean patientFound = false;
            for (WebElement we : rows) {
                List<WebElement> cells = we.findElements(By.tagName("td"));
                assertTrue(cells.size() > 0);

                if (cells.get(0).getText().equals("Test Test")) {
                    patientFound = true;
                    cells.get(1).findElement(By.tagName("button")).click();
                    rows = driver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
                    assertTrue(rows.size() == initialNumRows - 1);
                    assertNotNull(driver.findElement(By.linkText("Edit patient")));
                }
            }

            if (!patientFound)
                fail("Pre-registered patient was not found in list");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail("Exception thrown");
        }
    }

    public void testDeactivatePreRegisteredPatient() throws Exception {
        try {
            long pid = preRegisterPatient("Test", "Test", "test2@gmail.com", "pw");
            driver = login("9000000000", "pw");
            driver.findElement(By.linkText("Pre-Registered Patients")).click();
            List<WebElement> rows = driver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
            assertTrue(rows.size() > 0);

            int initialNumRows = rows.size();
            boolean patientFound = false;
            for (WebElement we : rows) {
                List<WebElement> cells = we.findElements(By.tagName("td"));
                assertTrue(cells.size() > 0);

                if (cells.get(0).getText().equals("Test Test")) {
                    patientFound = true;
                    cells.get(2).findElement(By.tagName("button")).click();
                    rows = driver.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
                    assertTrue(rows.size() == initialNumRows - 1);
                }
            }

            if (!patientFound)
                fail("Pre-registered patient was not found in list");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail("Exception thrown");
        }
    }

    private long preRegisterPatient(String first, String last, String email, String password) throws Exception {
        driver = new Driver();
        driver.get(ADDRESS);
        driver.findElement(By.linkText("Pre-Register")).click();

        driver.findElement(By.name("p_fname")).sendKeys(first);
        driver.findElement(By.name("p_lname")).sendKeys(last);
        driver.findElement(By.name("p_email")).sendKeys(email);
        driver.findElement(By.name("p_password")).sendKeys(password);
        driver.findElement(By.name("p_verify")).sendKeys(password);

        driver.findElement(By.name("p_fname")).submit();

        WebElement result = driver.findElement(By.id("iTrustContent")).findElement(By.tagName("h3"));
        assertTrue(result.getText().contains("Pre-registration successful! Your MID is "));

        return Long.parseLong(result.getText().substring(result.getText().length()-3));
    }
}
