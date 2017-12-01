package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.beans.FilterBean;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.beans.ApptRequestBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FilterBeanTest extends TestCase {

    private FilterBean filt;

    protected void setUp() throws Exception {
        filt = new FilterBean();
    }

    public void testMid() {
        assertEquals(null, filt.getMid());
        filt.setMid(1L);
        assertEquals((Long)1L, filt.getMid());
    }

    public void testSender() {
        assertEquals(null, filt.getSender());
        filt.setSender("a");
        assertEquals("a", filt.getSender());
    }

    public void testSubject() {
        assertEquals(null, filt.getSubject());
        filt.setSubject("a");
        assertEquals("a", filt.getSubject());
    }

    public void testSubstring_pos() {
        assertEquals(null, filt.getSubstring_pos());
        filt.setSubstring_pos("a");
        assertEquals("a", filt.getSubstring_pos());
    }

    public void testSubstring_neg() {
        assertEquals(null, filt.getSubstring_neg());
        filt.setSubstring_neg("a");
        assertEquals("a", filt.getSubstring_neg());
    }

    public void testDate_left() throws ParseException{
        assertEquals(null, filt.getDate_left());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        filt.setDate_left(df.parse("2012-12-12"));
        assertEquals("2012-12-12", df.format(filt.getDate_left()));
    }

    public void testDate_right() throws ParseException{
        assertEquals(null, filt.getDate_left());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        filt.setDate_right(df.parse("2012-12-12"));
        assertEquals("2012-12-12", df.format(filt.getDate_right()));
    }
}
