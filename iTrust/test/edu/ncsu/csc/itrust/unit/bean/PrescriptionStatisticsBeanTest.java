package edu.ncsu.csc.itrust.unit.bean;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.beans.PrescriptionStatisticsBean;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.OverrideReasonBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;

/**
 */
public class PrescriptionStatisticsBeanTest extends TestCase {
    /**
     * Sets up defaults
     */
    @Override
    protected void setUp() throws Exception {
    }

    /**
     * testPSB
     * @throws Exception
     */
    public void testPSB() throws Exception {
        PrescriptionStatisticsBean p1 = new PrescriptionStatisticsBean();
        assertEquals(0L, p1.getVisitID());
        assertEquals("", p1.getIcdCode());
        assertEquals(0L, p1.getPrescriptionID());
        p1.setVisitID(1L);
        p1.setIcdCode("250.10");
        p1.setPrescriptionID(1L);
        assertEquals(1L, p1.getVisitID());
        assertEquals("250.10", p1.getIcdCode());
        assertEquals(1L, p1.getPrescriptionID());
    }
}
