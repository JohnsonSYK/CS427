<%--
  Created by IntelliJ IDEA.
  User: mz11
  Date: 11/4/17
  Time: 5:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReportRequestBean"%>

<%@page import="java.util.List"%>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.ReminderDao" %>
<%@ page import="edu.ncsu.csc.itrust.beans.ReminderBean" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Collections" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Notifications";
%>

<%@include file="/header.jsp" %>

<%
    ReminderDao reminderDao = prodDAO.getReminderDAO();
    List<ReminderBean> reminderBeans = reminderDao.getReminders(loggedInMID);
    if (reminderBeans.size() == 0) { %>
        <h2>No reminders!!</h2>
    <% }
    else {
        %> <table>
        <tr><th>Subject</th> <th>Username</th> <th>Timestamp</th> <th>Link</th></tr>
    <%
            reminderBeans.sort(new Comparator<ReminderBean>() {
                @Override
                public int compare(ReminderBean o1, ReminderBean o2) {
                    return o1.getSentTime().compareTo(o2.getSentTime());
                }
            });
            Collections.reverse(reminderBeans);
            for(ReminderBean bean : reminderBeans){
                %>
                <tr>
                <td><%= bean.getSubject() %></td>
                <td><%= userName %></td>
                <td><%= bean.getSentTime().toString() %></td>
                <td><a href="/iTrust/auth/patient/viewReminder.jsp?apptId=<%= bean.getReminderId()%>">Link</a></td>
                </tr>
                        <%
            }
            %> </table> <%

        }
%>



<%@include file="/footer.jsp" %>
