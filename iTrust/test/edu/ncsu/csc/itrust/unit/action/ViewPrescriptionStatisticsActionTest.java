package edu.ncsu.csc.itrust.unit.action;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import edu.ncsu.csc.itrust.action.ViewPrescriptionStatisticsAction;
import edu.ncsu.csc.itrust.beans.*;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.action.ViewPrescriptionRecordsAction;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ViewPrescriptionStatisticsActionTest extends TestCase {

    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private ViewPrescriptionStatisticsAction action;
    private TestDataGenerator gen;

    @Override
    /**
     * Sets up defaults
     */
    protected void setUp() throws Exception {
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * testGetOVsMale
     * @throws Exception
     */
    public void testGetOVsMale() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/1980");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List <OfficeVisitBean> ovbl = action.getFilteredOfficeVisits("250.10", "Male", left, right);
        assertEquals(2, ovbl.size());
        System.out.println(ovbl.get(0).getID());
        System.out.println(ovbl.get(1).getID());
    }

    /**
     * testGetOVsFemale
     * @throws Exception
     */
    public void testGetOVsFemale() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/1980");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List <OfficeVisitBean> ovbl = action.getFilteredOfficeVisits("250.10", "Female", left, right);
        assertEquals(0, ovbl.size());
    }

    /**
     * testGetOVsAll
     * @throws Exception
     */
    public void testGetOVsAll() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/1980");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List <OfficeVisitBean> ovbl = action.getFilteredOfficeVisits("250.10", "All", left, right);
        assertEquals(2, ovbl.size());
    }

    /**
     * testGetOVsBadDates
     * @throws Exception
     */
    public void testGetOVsBadDates() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List <OfficeVisitBean> ovbl = action.getFilteredOfficeVisits("250.10", "All", left, right);
        assertEquals(0, ovbl.size());
    }

    /**
     * testGetOVsBadICD
     * @throws Exception
     */
    public void testGetOVsBadICD() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/1980");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List <OfficeVisitBean> ovbl = action.getFilteredOfficeVisits("garbo", "All", left, right);
        assertEquals(0, ovbl.size());
    }

    /**
     * testGetPrescriptions
     * @throws Exception
     */
    public void testGetPrescriptions() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/1980");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List <OfficeVisitBean> ovbl = action.getFilteredOfficeVisits("250.10", "Male", left, right);
        assertEquals(2, ovbl.size());
        HashMap<String, Integer> parsed = action.getPrescriptionStatistics(ovbl);
        assertEquals(2, parsed.size());
    }

    /**
     * testGetTableMale
     * @throws Exception
     */
    public void testGetTableMale() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/1980");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List<PrescriptionStatisticsBean> table = action.getTable("250.10", "Male", left, right);
        assertEquals(3, table.size());
    }

    /**
     * testGetTableAll
     * @throws Exception
     */
    public void testGetTableAll() throws Exception {
        action = new ViewPrescriptionStatisticsAction(factory);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        Date left = null;
        try {
            left = df.parse("01/01/1980");
        } catch (ParseException e) {
        }
        Date right = null;
        try {
            right = df.parse("01/01/2020");
        } catch (ParseException e) {
        }
        List<PrescriptionStatisticsBean> table = action.getTable("250.10", "All", left, right);
        assertEquals(3, table.size());
    }
}