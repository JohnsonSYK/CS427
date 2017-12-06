package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.beans.loaders.*;
import junit.framework.TestCase;

public class BeanLoaderTest extends TestCase {
	
	public void testLoadParameters() throws Exception {
		
		try {
			new AllergyBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new DiagnosisBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new FamilyBeanLoader("self").loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new DiagnosisBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new HospitalBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new LabProcedureBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new LOINCBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new MedicationBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new OfficeVisitLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new OperationalProfileLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new PrescriptionReportBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new RemoteMonitoringListsBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new ReportRequestBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new SurveyLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new SurveyResultBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new TransactionBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
		try {
			new VisitReminderReturnFormLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new FilterLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}
		
	}

}
