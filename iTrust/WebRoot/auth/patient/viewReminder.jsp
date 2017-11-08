<%--
  Created by IntelliJ IDEA.
  User: mz11
  Date: 11/4/17
  Time: 6:02 PM
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
    pageTitle = "iTrust - View Message";
%>

<%@include file="/header.jsp" %>

<%
    String requestId = request.getParameter("apptId");
    if(requestId == null) {
        %>
        <h2>Error, no message specified</h2>
        <%
    }
    else {
        long id = -1;
        try {
            id = Long.parseLong(requestId);
        } catch (Exception e){
            %> <h2> Error, invalid ID </h2> <%
        }
        if (id != -1) {
            ReminderDao reminderDao = prodDAO.getReminderDAO();
            ReminderBean bean = reminderDao.getReminderById(id);
            if (bean == null) {
                %> <h2> Error, your appointment isn't found <%
            } else {
                %>
                <table>
                    <tr><td><b>To</b></td> <td><%=userName%></td></tr>
                    <tr><td><b>From</b></td> <td><%=bean.getSenderName()%></td></tr>
                    <tr><td><b>Timestamp</b></td> <td><%=bean.getSentTime().toString()%></td></tr>
                    <tr><td><b>Subject</b></td> <td><%=bean.getSubject()%></td></tr>
                    <tr><td><b>Content</b></td> <td><%=bean.getContent()%></td></tr>
                </table>
                <%
            }
        }
    }
%>


<%@include file="/footer.jsp" %>
