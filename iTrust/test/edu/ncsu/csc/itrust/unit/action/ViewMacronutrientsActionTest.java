package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.action.ViewFoodEntryAction;
import edu.ncsu.csc.itrust.action.ViewMacronutrientsAction;
import edu.ncsu.csc.itrust.beans.FoodEntryBean;
import edu.ncsu.csc.itrust.beans.MacronutrientsBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * Tests for viewing a patient's food diary
 *
 */
public class ViewMacronutrientsActionTest {
	private ViewMacronutrientsAction msj;
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen;
	
	/**
	 * Clears all of the tables, gets the standard data, 
	 * and includes the data for use cases 68 and 69
	 */
	@Before
	public void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Clears all of the tables
	 */
	@After
	public void tearDown() throws Exception {
		gen.clearAllTables();
	}
	

	@Test
	public void testGetPatientMsj() {
		msj = new ViewMacronutrientsAction(factory, 341L);
		try {
			
			
	    	MacronutrientsBean msjData = msj.getMsjData(341);
	    	
	    	Assert.assertEquals(40, msjData.getYears());
	    	Assert.assertEquals(112.5, msjData.getWeight(), .001);
	    	Assert.assertEquals(65.0, msjData.getHeight(), .001);
	    	Assert.assertEquals(1336.25, msjData.getMsj(), .001);
	    	Assert.assertEquals(341L, msjData.getPatientID());
			
		} catch (ITrustException e) {
			Assert.fail(e.getMessage());
		}
			
	}
	
	@Test
	public void testGetBadMsj() {
		msj = new ViewMacronutrientsAction(factory, 341L);
		MacronutrientsBean msjData = null;
		try {
			
			msjData = msj.getMsjData(343);
	    	
	    	Assert.fail("You do not have permission");
			
		} catch (ITrustException e) {
			Assert.assertEquals("You do not have permission"
					+ " to view Macronutrients data!", e.getMessage());
			Assert.assertNull(msjData);
		}
		
			
	}
	
	@Test
	public void testGetIncorrectMsj() {
		msj = new ViewMacronutrientsAction(factory, 336L);
		MacronutrientsBean msjData = null;
		try {
			
			msjData = msj.getMsjData(336);
	    	
	    	Assert.fail("Patient does not have complete information");
			
		} catch (ITrustException e) {
			Assert.assertEquals("Patient does not have complete information to calculate"
					+ " Macronutrients.", e.getMessage());
			Assert.assertNull(msjData);
		}
		
			
	}
	
	@Test
	public void testGetPatientMacronutrients() {
		msj = new ViewMacronutrientsAction(factory, 342L);
		ViewFoodEntryAction action = new ViewFoodEntryAction(factory, 342L);
		try {
			
			
	    	MacronutrientsBean msjData = msj.getMsjData(342);
	    	
	    	Assert.assertEquals(60, msjData.getYears());
	    	Assert.assertEquals(112.5, msjData.getWeight(), .001);
	    	Assert.assertEquals(65.0, msjData.getHeight(), .001);
	    	Assert.assertEquals(1236.25, msjData.getMsj(), .001);
	    	Assert.assertEquals(342L, msjData.getPatientID());
	    	
	    	List<FoodEntryBean> foodentry = action.getDiaryTotals(342);
	    	
	    	foodentry = msj.reverse(foodentry);
	    	
	    	FoodEntryBean single = foodentry.get(0);
	    	
	    	Assert.assertEquals(5, foodentry.size());
	    	Assert.assertEquals(728.0, single.getCarbGrams() * single.getServings() * 4, .001);
			
		} catch (ITrustException e) {
			Assert.fail(e.getMessage());
		}
			
	}
	

}
