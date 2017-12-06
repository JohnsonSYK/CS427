package edu.ncsu.csc.itrust.unit.dao;

import edu.ncsu.csc.itrust.beans.FilterBean;
import edu.ncsu.csc.itrust.dao.mysql.FilterDAO;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Filter;

public class FilterTest extends TestCase {
    FilterDAO filterDAO = TestDAOFactory.getTestInstance().getFilterDAO();
    private TestDataGenerator gen = new TestDataGenerator();

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
    }

    public void testAddFilterException() throws Exception {
        try {
            FilterBean testFilter = new FilterBean();
            testFilter.setMid(-1L);
            filterDAO.add(testFilter);
            fail("exception should have been thrown");
        } catch (ITrustException e) {
            assertEquals("A database exception has occurred. Please see the log in the console for stacktrace", e.getMessage());
        }
    }

    public void testAddFilterNULLsAndGet() throws Exception{
        try {
            FilterBean testFilter = new FilterBean();
            testFilter.setMid(1L);
            testFilter.setSubject("a");
            filterDAO.add(testFilter);
            FilterBean pull = filterDAO.getFilter(1L);
            assertEquals("a", pull.getSubject());
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Something went wrong");
        }
    }

    public void testAddNonNULLDateAndGet() throws Exception{
        try {
            FilterBean testFilter = new FilterBean();
            testFilter.setMid(1L);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            testFilter.setDate_left(df.parse("2012-12-12"));
            testFilter.setDate_right(df.parse("2012-12-13"));
            filterDAO.add(testFilter);
            FilterBean pull = filterDAO.getFilter(1L);
            assertEquals("2012-12-12", df.format(pull.getDate_left()));
            assertEquals("2012-12-13", df.format(pull.getDate_right()));
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Something went wrong");
        }
    }

    /*public void testUpdateException() throws Exception{
        try {
            FilterBean testFilter = new FilterBean();
            testFilter.setMid(-1L);
            filterDAO.update(testFilter);
            fail("exception should have been thrown");
        }
        catch (Exception e){
            assertEquals("A database exception has occurred. Please see the log in the console for stacktrace", e.getMessage());
        }
    }*/

    public void testUpdateFilterToNULLs() throws Exception{
        try {
            FilterBean testFilter = new FilterBean();
            testFilter.setMid(1L);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            testFilter.setDate_left(df.parse("2012-12-12"));
            testFilter.setDate_right(df.parse("2012-12-13"));
            filterDAO.add(testFilter);
            FilterBean testFilter2 = new FilterBean();
            testFilter2.setMid(1L);
            filterDAO.update(testFilter2);
            FilterBean pull = filterDAO.getFilter(1L);
            assertEquals(null, pull.getDate_left());
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Something went wrong");
        }
    }

    public void testUpdateFilterToNonNULLs() throws Exception{
        try {
            FilterBean testFilter = new FilterBean();
            testFilter.setMid(1L);
            filterDAO.add(testFilter);
            FilterBean testFilter2 = new FilterBean();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            testFilter2.setDate_left(df.parse("2012-12-12"));
            testFilter2.setDate_right(df.parse("2012-12-13"));
            testFilter2.setMid(1L);
            filterDAO.update(testFilter2);
            FilterBean pull = filterDAO.getFilter(1L);
            assertEquals("2012-12-12", df.format(pull.getDate_left()));
            assertEquals("2012-12-13", df.format(pull.getDate_right()));
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Something went wrong");
        }
    }

    /*public void testGetException(){
        try {
            FilterBean pull = filterDAO.getFilter(-1L);
            fail("exception should have been thrown");
        }
        catch (Exception e){
            assertEquals("A database exception has occurred. Please see the log in the console for stacktrace", e.getMessage());
        }
    }*/
    public void testGetNull(){
        try {
            FilterBean pull = filterDAO.getFilter(1L);
            assertEquals(null, pull);
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Something went wrong");
        }
    }
}
