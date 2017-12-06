package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.action.EventLoggingAction;
import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.beans.ReminderBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReminderDao;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Logs and returns reminders
 */
public class AppointmentDayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DAOFactory factory;
    private ReminderDao reminderDao;
    private PersonnelDAO personnelDAO;
    private static final String senderName = "System Reminder";
    private static final DateFormat hmsFormatter = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat dmyFormatter = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * Default Constructor
     */
    public AppointmentDayServlet () {
        super();
        factory = DAOFactory.getProductionInstance();
        reminderDao = factory.getReminderDAO();
    }

    /**
     * Dependency Injection Constructor
     * @param factory
     */
    public AppointmentDayServlet (DAOFactory factory) {
        super();
        this.factory = factory;
        reminderDao = factory.getReminderDAO();
        personnelDAO = this.factory.getPersonnelDAO();
    }

    /**
     * Services the request
     * @param request, needs "days" param and "mid" param
     * @param response, to be sent back
     * @throws ServletException, if invalid request
     * @throws IOException, if unable to write the request back
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("days");
        Integer days = Integer.parseInt(param);
        Long mid = Long.parseLong(request.getParameter("mid"));
        List<ApptBean> res;
        String names;
        try {
            res = factory.getApptDAO().getApptsFor(mid);
            names = personnelDAO.getName(mid);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        Date now = (new Date());
        Date later = new Date(now.getTime() + (1000 * 60 * 60 * 24 * days + 1));
        List<ApptBean> filtered = new ArrayList<>();

        for(ApptBean bean : res) {
            if(bean.getDate().before(later)) {
                filtered.add(bean);
            }
        }

        for(ApptBean bean : filtered) {
            sendNotification(mid, names, now, bean);
        }
        PrintWriter resp = response.getWriter();
        resp.write("Success!");
    }

    /**
     * sends a notification from "mid" named "names' from "now" and "bean
     * @param mid
     * @param names
     * @param now
     * @param bean
     */
    private void sendNotification(Long mid, String names, Date now, ApptBean bean) {
        ReminderBean rb = new ReminderBean();
        Date apptTime = bean.getDate();
        int dayDiff = (int)((apptTime.getTime() - now.getTime()) / (1000*60*60*24));
        rb.setMid((int)(bean.getPatient()));
        rb.setSenderName(senderName);
        rb.setSubject(String.format("Reminder: upcoming appointment in %d day(s)", dayDiff));
        rb.setContent(String.format("You have an appointment on %s, %s with Dr. %s",
                hmsFormatter.format(apptTime),
                dmyFormatter.format(apptTime),
                names));
        try {
            reminderDao.logReminder(rb);
            EventLoggingAction loggingAction = new EventLoggingAction(this.factory);
            loggingAction.logEvent(TransactionType.MESSAGE_SEND, mid, bean.getPatient() ,
                    "Sent appointment reminder with appt_id="+bean.getApptID());
        } catch (SQLException | DBException e) {

        }
    }
}
