package edu.ncsu.csc.itrust.unit.dao.referral;

import edu.ncsu.csc.itrust.beans.ReminderBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ReminderDao;
import edu.ncsu.csc.itrust.exception.DBException;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.*;
import java.time.Instant;

import static org.mockito.Mockito.*;

public class ReminderDAOTest extends TestCase {

    private ReminderBean getTestBean() {
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
        return bean;
    }
    @Test
    public void testLogReminder() {
        DAOFactory factory = mock(DAOFactory.class);
        Connection conn = mock(Connection.class);
        PreparedStatement state = mock(PreparedStatement.class);
        try {
            when(factory.getConnection()).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenReturn(state);
        } catch (SQLException e) {}
        ReminderDao dao = new ReminderDao(factory);

        ReminderBean bean = getTestBean();
        try {
            dao.logReminder(bean);
            verify(state).setLong(1, bean.getMid());
            verify(state).setString(2, bean.getSubject());
            verify(state).setString(3, bean.getSenderName());
            verify(state).setString(4, bean.getContent());
            verify(state).executeUpdate();
            verify(state, atLeastOnce()).close();
        } catch (DBException | SQLException e) { fail("Should not throw a DBexception"); }

    }
    @Test
    public void testGetReminderByMID() {
        DAOFactory factory = mock(DAOFactory.class);
        Connection conn = mock(Connection.class);
        PreparedStatement state = mock(PreparedStatement.class);
        ResultSet set = mock(ResultSet.class);
        try {
            when(factory.getConnection()).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenReturn(state);
            when(state.executeQuery()).thenReturn(set);
        } catch (SQLException e) {}
        ReminderDao dao = new ReminderDao(factory);
        ReminderBean bean = getTestBean();
        try {
            dao.getReminders(bean.getMid());
            verify(state).setLong(1, bean.getMid());
            verify(state, atLeastOnce()).executeQuery();
        } catch (DBException | SQLException e) { fail("Should not throw a DBexception"); }

    }
    @Test
    public void testGetReminderRID() {
        DAOFactory factory = mock(DAOFactory.class);
        Connection conn = mock(Connection.class);
        PreparedStatement state = mock(PreparedStatement.class);
        ResultSet set = mock(ResultSet.class);
        try {
            when(factory.getConnection()).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenReturn(state);
            when(state.executeQuery()).thenReturn(set);
        } catch (SQLException e) {}
        ReminderDao dao = new ReminderDao(factory);
        ReminderBean bean = getTestBean();
        try {
            dao.getReminderById(bean.getReminderId());
            verify(state).setLong(1, bean.getReminderId());
            verify(state, atLeastOnce()).executeQuery();
        } catch (DBException | SQLException e) { fail("Should not throw a DBexception"); }

    }

    @Test
    public void testExceptionReminder() {
        DAOFactory factory = mock(DAOFactory.class);
        Connection conn = mock(Connection.class);
        PreparedStatement state = mock(PreparedStatement.class);
        try {
            when(factory.getConnection()).thenThrow(new SQLException());
            when(conn.prepareStatement(anyString())).thenReturn(state);
        } catch (SQLException e) {}
        ReminderDao dao = new ReminderDao(factory);

       try {
           dao.logReminder(null);
           fail("Exception should have been thrown");
       } catch (Exception e) {

       }

       try {
           dao.getReminderById(1);
           fail("Exception should have been thrown");
       } catch (Exception e) {

       }

       try {
           dao.getReminders(1);
           fail("Exception should have been thrown");
       } catch (Exception e) {

       }

    }
}
