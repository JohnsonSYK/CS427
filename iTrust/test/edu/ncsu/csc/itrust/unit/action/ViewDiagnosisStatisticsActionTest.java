package edu.ncsu.csc.itrust.unit.action;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.action.*;
import junit.framework.TestCase;

@SuppressWarnings("unused")
public class ViewDiagnosisStatisticsActionTest extends TestCase {

	private TestDataGenerator gen = new TestDataGenerator();
	private ViewDiagnosisStatisticsAction action;
	
	private int thisYear = Calendar.getInstance().get(Calendar.YEAR);

	
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		gen.patient_hcp_vists();
		gen.hcp_diagnosis_data();
		
		action = new ViewDiagnosisStatisticsAction(TestDAOFactory.getTestInstance());
	}
	
	public void testGetDiagnosisCodes() throws Exception {
		List<DiagnosisBean> db = action.getDiagnosisCodes();
		assertEquals(19, db.size());
	}
	
	public void testGetDiagnosisStatisticsValid() throws Exception {
		DiagnosisStatisticsBean dsBean = action.getDiagnosisStatistics("06/28/2011", "09/28/2011", "487.00", "27606-1234");
		assertEquals(2, dsBean.getZipStats());
		assertEquals(5, dsBean.getRegionStats());
	}

	public void testGetDiagnosisStatisticsRegionValid() throws Exception {
		ArrayList<DiagnosisStatisticsBean> dsBean = action.getDiagnosisStatistics_region("11/15/2017", "487.00", "27606-1234");
		assertEquals(84, dsBean.get(0).getZipStats());
		assertEquals(120, dsBean.get(0).getRegionStats());
	}

	public void testGetDiagnosisStatisticsCountryValid() throws Exception {
		ArrayList<DiagnosisStatisticsBean> dsBean = action.getDiagnosisStatistics_country("11/15/2017", "487.00", "27606-1234");
		assertEquals(84, dsBean.get(0).getZipStats());
		assertEquals(120, dsBean.get(0).getRegionStats());
	}

	public void testGetDiagnosisStatisticsStateValid() throws Exception {
		ArrayList<DiagnosisStatisticsBean> dsBean = action.getDiagnosisStatistics_state("11/15/2017", "487.00", "27606-1234");
		assertEquals(84, dsBean.get(0).getZipStats());
		assertEquals(120, dsBean.get(0).getRegionStats());
	}
	
	public void testGetDiagnosisStatisticsValidNull() throws Exception {
		DiagnosisStatisticsBean dsBean = action.getDiagnosisStatistics(null, null, "487.00", "27606");
		assertEquals(null, dsBean);
	}

	public void testGetDiagnosisStatisticsRegionValidNull() throws Exception {
		ArrayList<DiagnosisStatisticsBean> dsBean = action.getDiagnosisStatistics_region(null, "487.00", "27606");
		assertEquals(null, dsBean);
	}

	public void testGetDiagnosisStatisticsCountryValidNull() throws Exception {
		ArrayList<DiagnosisStatisticsBean> dsBean = action.getDiagnosisStatistics_country(null, "487.00", "27606");
		assertEquals(null, dsBean);
	}

	public void testGetDiagnosisStatisticsStateValidNull() throws Exception {
		ArrayList<DiagnosisStatisticsBean> dsBean = action.getDiagnosisStatistics_state(null, "487.00", "27606");
		assertEquals(null, dsBean);
	}
	
	public void testGetDiagnosisStatisticsInvalidDate() throws Exception {
		try {
			action.getDiagnosisStatistics("06-28/2011", "09/28/2011", "487.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Enter dates in MM/dd/yyyy", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsRegionInvalidDate() throws Exception {
		try {
			action.getDiagnosisStatistics_region("09-28/2011", "487.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Enter dates in MM/dd/yyyy", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsStateInvalidDate() throws Exception {
		try {
			action.getDiagnosisStatistics_state("09-28/2011", "487.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Enter dates in MM/dd/yyyy", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsCountryInvalidDate() throws Exception {
		try {
			action.getDiagnosisStatistics_country("09-28/2011", "487.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Enter dates in MM/dd/yyyy", e.getErrorList().get(0));
		}
	}
	
	public void testGetDiagnosisStatisticsReversedDates() throws Exception {
		try {
			action.getDiagnosisStatistics("09/28/2011", "06/28/2011", "487.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Start date must be before end date!", e.getErrorList().get(0));
		}
	}
	
	public void testGetDiagnosisStatisticsInvalidZip() throws Exception {
		try {
			action.getDiagnosisStatistics("06/28/2011", "09/28/2011", "487.00", "2766");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Zip Code must be 5 digits or 5 digits and the 4 digit extension!", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsRegionInvalidZip() throws Exception {
		try {
			action.getDiagnosisStatistics_region("09/28/2011", "487.00", "2766");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Zip Code must be 5 digits or 5 digits and the 4 digit extension!", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsStateInvalidZip() throws Exception {
		try {
			action.getDiagnosisStatistics_state("09/28/2011", "487.00", "2766");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Zip Code must be 5 digits or 5 digits and the 4 digit extension!", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsCountryInvalidZip() throws Exception {
		try {
			action.getDiagnosisStatistics_country("09/28/2011", "487.00", "2766");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Zip Code must be 5 digits or 5 digits and the 4 digit extension!", e.getErrorList().get(0));
		}
	}
	
	public void testGetDiagnosisStatisticsInvalidICDCode() throws Exception {
		try {
			action.getDiagnosisStatistics("06/28/2011", "09/28/2011", "11114.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("ICDCode must be valid diagnosis!", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsRegionInvalidICDCode() throws Exception {
		try {
			action.getDiagnosisStatistics_region("09/28/2011", "11114.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("ICDCode must be valid diagnosis!", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsStateInvalidICDCode() throws Exception {
		try {
			action.getDiagnosisStatistics_state("09/28/2011", "11114.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("ICDCode must be valid diagnosis!", e.getErrorList().get(0));
		}
	}

	public void testGetDiagnosisStatisticsCountryInvalidICDCode() throws Exception {
		try {
			action.getDiagnosisStatistics_country("09/28/2011", "11114.00", "27606");
			fail("Should have failed but didn't");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("ICDCode must be valid diagnosis!", e.getErrorList().get(0));
		}
	}
	
	public void testIsMalariaEpidemic() throws Exception {
		gen.malaria_epidemic();
		assertTrue(action.isMalariaEpidemic("11/02/" + thisYear, "27606", "110"));
		assertFalse(action.isMalariaEpidemic("11/16/" + thisYear, "27606", "110"));
	}
	
	public void testIsFluEpidemic() throws Exception {
		gen.influenza_epidemic();
		assertTrue(action.isFluEpidemic("11/02/" + thisYear, "27606"));
		assertFalse(action.isFluEpidemic("11/16/" + thisYear, "27606"));
	}
	
	public void testGetEpidemicStatisticsInvalidThreshold(){
		try{
			ArrayList<DiagnosisStatisticsBean> dsList = action.getEpidemicStatistics("11/02/" + thisYear, "84.50", "27606", "");
			fail("FormValidationException should have been thrown.");
		}catch(FormValidationException e){
			//This should be thrown
		} catch (DBException e) {
			fail("DB Exception thrown");
		}
	}
	
	public void testGetEpidemicStatistics() {
		try {
			ArrayList<DiagnosisStatisticsBean> dsList = 
					action.getEpidemicStatistics("11/02/2012", "487.00", 
							"00601", "5");
			assertEquals(2, dsList.size());
		} catch (FormValidationException e) {
			fail("FormValidationException");
		} catch (DBException d) {
			fail("DBException thrown");
		}
	}
}
