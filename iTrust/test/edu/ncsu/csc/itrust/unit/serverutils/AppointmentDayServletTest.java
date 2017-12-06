package edu.ncsu.csc.itrust.unit.serverutils;

import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReminderDao;
import edu.ncsu.csc.itrust.server.AppointmentDayServlet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.io.HttpResponseWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentDayServletTest {
    @Mock
    private DAOFactory factory = mock(DAOFactory.class);
    @Mock
    private ReminderDao reminderDao = mock(ReminderDao.class);
    @Mock
    private ApptDAO apptDAO = mock(ApptDAO.class);
    @InjectMocks
    private AppointmentDayServlet appointmentDayServlet;

    @Before
    public void setUp() {

    }
    @Test
    public void doGetTest() {
        when(factory.getReminderDAO()).thenReturn(reminderDao);
        when(factory.getApptDAO()).thenReturn(apptDAO);
        try {
            when(apptDAO.getApptsFor(anyLong())).thenReturn(new ArrayList<>());
        } catch (Exception e) {}
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("days")).thenReturn("100");
        when(req.getParameter("mid")).thenReturn("9000000000");
        when(req.getParameter("name")).thenReturn("asfd");
        HttpServletResponse res = mock(HttpServletResponse.class);
        PrintWriter writ = mock(PrintWriter.class);
        try {
            when(res.getWriter()).thenReturn(writ);
            appointmentDayServlet.service(req, res);
            verify(writ).write("Success!");
        } catch (Exception e) {}
    }
    @Test
    public void doLogTest() {
        when(factory.getReminderDAO()).thenReturn(reminderDao);
        when(factory.getApptDAO()).thenReturn(apptDAO);
        try {
            ArrayList<ApptBean> beans = new ArrayList<>();
            ApptBean bean = new ApptBean();
            Date now = (new Date());
            Date later = new Date(now.getTime() + (1000 * 60 * 60 * 24 + 1));
            bean.setDate(new Timestamp(later.getTime()));
            beans.add(bean);
            when(apptDAO.getApptsFor(anyLong())).thenReturn(beans);
        } catch (Exception e) {}
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("days")).thenReturn("100");
        when(req.getParameter("mid")).thenReturn("9000000000");
        when(req.getParameter("name")).thenReturn("asfd");
        HttpServletResponse res = mock(HttpServletResponse.class);
        PrintWriter writ = mock(PrintWriter.class);
        try {
            when(res.getWriter()).thenReturn(writ);
            appointmentDayServlet.service(req, res);
            verify(reminderDao).logReminder(anyObject());
            verify(writ).write("Success!");
        } catch (Exception e) {}
    }
}
