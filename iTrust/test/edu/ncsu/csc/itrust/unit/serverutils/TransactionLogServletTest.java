package edu.ncsu.csc.itrust.unit.serverutils;

import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReminderDao;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.server.AppointmentDayServlet;
import edu.ncsu.csc.itrust.server.TransactionLogServlet;
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

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionLogServletTest {
    @Mock
    private DAOFactory factory = mock(DAOFactory.class);
    @Mock
    private PersonnelDAO personnelDao = mock(PersonnelDAO.class);
    @Mock
    private TransactionDAO transactionDAO = mock(TransactionDAO.class);
    @InjectMocks
    private TransactionLogServlet transactionLogServlet;

    @Before
    public void setUp() {

    }
    @Test
    public void doGetTest() {
        when(factory.getPersonnelDAO()).thenReturn(personnelDao);
        when(factory.getTransactionDAO()).thenReturn(transactionDAO);
        try {
            when(transactionDAO.getFilteredTransactions(anyString(), anyString(), anyObject(), anyObject(),
                    anyObject())).thenReturn(new ArrayList<>());
        } catch (Exception e) {}
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("srole")).thenReturn("hcp");
        when(req.getParameter("prole")).thenReturn("hcp");
        when(req.getParameter("transactionNum")).thenReturn("1");
        HttpServletResponse res = mock(HttpServletResponse.class);
        PrintWriter writ = mock(PrintWriter.class);
        try {
            when(res.getWriter()).thenReturn(writ);
            transactionLogServlet.service(req, res);
            verify(transactionDAO).getFilteredTransactions(anyString(), anyString(), anyObject(), anyObject(),
                    anyObject());
            verify(res).setContentType("application/json");
            verify(writ).write("");
        } catch (Exception e) {}
    }

    @Test
    public void throwsExceptionTest() {
        when(factory.getPersonnelDAO()).thenReturn(personnelDao);
        when(factory.getTransactionDAO()).thenReturn(transactionDAO);
        try {
            when(transactionDAO.getFilteredTransactions(anyString(), anyString(), anyObject(), anyObject(),
                    anyObject())).thenThrow(new IOException());
        } catch (Exception e) {}
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("srole")).thenReturn("hcp");
        when(req.getParameter("prole")).thenReturn("hcp");
        when(req.getParameter("transactionNum")).thenReturn("1");
        HttpServletResponse res = mock(HttpServletResponse.class);
        PrintWriter writ = mock(PrintWriter.class);
        try {
            when(res.getWriter()).thenReturn(writ);
            transactionLogServlet.service(req, res);
            fail("Exception should have been thrown");
        } catch (Exception e) {}
    }

    @Test
    public void doLogTest() {
        when(factory.getPersonnelDAO()).thenReturn(personnelDao);
        when(factory.getTransactionDAO()).thenReturn(transactionDAO);
        try {
            ArrayList<TransactionBean> beans = new ArrayList<>();
            TransactionBean bean = new TransactionBean();
            Date now = (new Date());
            Date later = new Date(now.getTime() + (1000 * 60 * 60 * 24 + 1));
            bean.setTimeLogged(new Timestamp(later.getTime()));
            beans.add(bean);
            when(transactionDAO.getFilteredTransactions(anyString(), anyString(), anyObject(), anyObject(),
                    anyObject())).thenReturn(beans);
        } catch (Exception e) {}
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("srole")).thenReturn("hcp");
        when(req.getParameter("prole")).thenReturn("hcp");
        HttpServletResponse res = mock(HttpServletResponse.class);
        PrintWriter writ = mock(PrintWriter.class);
        try {
            when(res.getWriter()).thenReturn(writ);
            transactionLogServlet.service(req, res);
            verify(transactionDAO).getFilteredTransactions(anyString(), anyString(), anyObject(), anyObject(),
                    anyObject());
            verify(res).setContentType("application/json");
        } catch (Exception e) {}
    }
}
