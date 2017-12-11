package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.beans.FilterBean;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;

public class OfficeVisitBeanTest extends TestCase {
	/**
	 * testDateFailure
	 */
	public void testDateFailure() {
		OfficeVisitBean ov = new OfficeVisitBean();
		ov.setVisitDateStr("bad date");
		assertNull(ov.getVisitDate());
	}
	/**
	 * testHashcodeAndEquals
	 */
	public void testHashCodeAndEquals() {
		OfficeVisitBean ov1 = new OfficeVisitBean(1);
		OfficeVisitBean ov2 = new OfficeVisitBean(1);
		OfficeVisitBean ov3 = new OfficeVisitBean(2);
		FilterBean f = new FilterBean();
		assertEquals(ov1.hashCode(), ov2.hashCode());
		assertTrue(ov1.hashCode() != ov3.hashCode());
		assertTrue(ov1.equals(ov2));
		assertFalse(ov1.equals(ov3));
		assertFalse(ov1.equals(f));
	}
}
