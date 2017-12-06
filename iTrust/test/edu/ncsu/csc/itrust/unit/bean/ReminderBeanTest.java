package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.beans.ReminderBean;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;

public class ReminderBeanTest extends TestCase {

    @Test
    public static void testBean() {

        String content = "content";
        int mid = 2;
        int rid = 4;
        String senderName = "name";
        Timestamp timestamp = Timestamp.from(Instant.now());
        String subject = "subject";

        ReminderBean bean = new ReminderBean();
        bean.setContent(content);
        bean.setMid(mid);
        bean.setReminderId(rid);
        bean.setSenderName(senderName);
        bean.setSentTime(timestamp);
        bean.setSubject(subject);

        assertEquals(content, bean.getContent());
        assertEquals(mid, bean.getMid());
        assertEquals(rid, bean.getReminderId());
        assertEquals(senderName, bean.getSenderName());
        assertEquals(timestamp, bean.getSentTime());
        assertEquals(subject, bean.getSubject());
        assertEquals(rid, bean.hashCode());
    }

    @Test
    public void testEquality() {
        String content = "content";
        int mid = 2;
        int rid = 4;
        String senderName = "name";
        Timestamp timestamp = Timestamp.from(Instant.now());
        String subject = "subject";

        ReminderBean bean = new ReminderBean();
        bean.setContent(content);
        bean.setMid(mid);
        bean.setReminderId(rid);
        bean.setSenderName(senderName);
        bean.setSentTime(timestamp);
        bean.setSubject(subject);

        Object notBean = new String();
        assertFalse(bean.equals(notBean));

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream str = new ObjectOutputStream(outputStream);
            str.writeObject(bean);
            byte[] arr = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(arr);
            ObjectInputStream inputStream1 = new ObjectInputStream(inputStream);
            ReminderBean copied = (ReminderBean)inputStream1.readObject();
            assertTrue(copied.equals(bean));
        } catch (IOException e) {
            fail("IOException");
        } catch (ClassNotFoundException e) {
            fail("Class not found exception");
        }

    }
}
