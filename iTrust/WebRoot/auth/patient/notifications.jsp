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

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Notifications";
%>

<%@include file="/header.jsp" %>
<table>
<%
    ReminderDao reminderDao = prodDAO.getReminderDAO();
    List<ReminderBean> reminderBeans = reminderDao.getReminders(loggedInMID);
    if (reminderBeans.size() == 0) { %>
        <h2>No reminders!!</h2>
    <% }

    for(ReminderBean bean : reminderBeans){
        %>
        <td><%= bean.getSenderName() %></td>
        <td><%= bean.getSubject() %></td>
        <td><%= bean.getSentTime().toString() %></td>
        <td><%= bean.getContent() %></td>
    <%
    }
%>
</table>


<%@include file="/footer.jsp" %>
