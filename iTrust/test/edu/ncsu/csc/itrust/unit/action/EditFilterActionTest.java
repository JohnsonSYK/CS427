package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import edu.ncsu.csc.itrust.action.EditFilterAction;
import edu.ncsu.csc.itrust.beans.FilterBean;
import org.junit.Test;

import edu.ncsu.csc.itrust.action.AddExerciseEntryAction;
import edu.ncsu.csc.itrust.action.EditExerciseEntryAction;
import edu.ncsu.csc.itrust.action.ViewExerciseEntryAction;
import edu.ncsu.csc.itrust.beans.ExerciseEntryBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

/**
 * Tests different abilities for editing a exercise entry.
 *
 */
public class EditFilterActionTest extends TestCase {

    private EditFilterAction action;
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private TestDataGenerator gen;

    @Override
    protected void setUp() throws Exception {
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * Clears all of the tables.
     */
    protected void tearDown() throws Exception {
        gen.clearAllTables();
    }

    public void testAddFilter() throws Exception {
        action = new EditFilterAction(factory, 1L);
        action.addFilter("a", "b", "c", "d", null, null);
        FilterBean pull = action.pullCurrent();
        assertEquals("a", pull.getSender());
    }

    /*public void testAddFilterException() throws Exception {
        try{
            action = new EditFilterAction(factory, -1L);
            action.addFilter("a", "b", "c", "d", null, null);
            fail("exception should have been thrown");
        }
        catch (Exception e){
            assertEquals("A database exception has occurred. Please see the log in the console for stacktrace", e.getMessage());
        }
    }*/

    public void testUpdateFilter() throws Exception {
        action = new EditFilterAction(factory, 1L);
        action.addFilter("a", "b", "c", "d", null, null);
        action.updateFilter("b", null, null, null, null, null);
        FilterBean pull = action.pullCurrent();
        assertEquals("b", pull.getSender());
    }

    public void testPullCurrentNull() throws Exception {
        action = new EditFilterAction(factory, 1L);
        FilterBean pull = action.pullCurrent();
        assertEquals(null, pull);
    }

}
