package edu.ncsu.csc.itrust.unit.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.action.ViewCauseDeathAction;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.action.ViewClaimsAction;
import edu.ncsu.csc.itrust.beans.BillingBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.BillingDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ViewCauseDeathActionTest extends TestCase{
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private TestDataGenerator gen = new TestDataGenerator();

    /**
     * Sets up defaults
     */
    @Before
    public void setUp() throws Exception {
        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * Cleans up
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * testMID
     * @throws Exception
     */
    public void testMID() throws Exception {
        ViewCauseDeathAction action = new ViewCauseDeathAction(factory);
        String result = action.getCODStatisticsMID(9000000000L, 1980, 2020, "all");
        assertEquals("ICD-9CM: 250.10; Cause of Death: Diabetes with ketoacidosis; Frequency:1\n", result);
    }

    /**
     * testAllFemale
     * @throws Exception
     */
    public void testAllFemale() throws Exception {
        ViewCauseDeathAction action = new ViewCauseDeathAction(factory);
        String result = action.getCODStatisticsAll(3L, 1980, 2020, "female");
        assertEquals("", result);
    }

    /**
     * testAllMale
     * @throws Exception
     */
    public void testAllMale() throws Exception {
        ViewCauseDeathAction action = new ViewCauseDeathAction(factory);
        String result = action.getCODStatisticsAll(3L, 1980, 2020, "male");
        assertEquals("ICD-9CM: 250.10; Cause of Death: Diabetes with ketoacidosis; Frequency:1\n", result);
    }

    /**
     * testAll
     * @throws Exception
     */
    public void testAll() throws Exception {
        ViewCauseDeathAction action = new ViewCauseDeathAction(factory);
        String result = action.getCODStatisticsAll(3L, 1980, 2020, "all");
        assertEquals("ICD-9CM: 250.10; Cause of Death: Diabetes with ketoacidosis; Frequency:1\n", result);
    }

    /**
     * testAllBadYears
     * @throws Exception
     */
    public void testAllBadYears() throws Exception {
        ViewCauseDeathAction action = new ViewCauseDeathAction(factory);
        String result = action.getCODStatisticsAll(3L, 2020, 1980, "all");
        assertEquals("", result);
    }
}
