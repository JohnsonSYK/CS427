<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.EditMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="javafx.util.Pair" %>
<%@ page import="edu.ncsu.csc.itrust.action.ViewCauseDeathAction" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View cause-of-death trends report";
    loggingAction.logEvent(TransactionType.DEATH_TRENDS_VIEW, loggedInMID, 0, "");
%>

<%@include file="/header.jsp" %>


<form action="viewCauseDeath.jsp" method="post">
    <input type="hidden" name="confirmAction" value="true"/>
    Left Interval year <input type="text" name="startingYear"/><br>
    Right Interval year <input type="text" name="endingYear"/> <br>
    Gender <select name="gender">
        <option value="all">all</option>
        <option value="male">male</option>
        <option value="female">female</option>
    </select> <br>
    <input type="submit" name="fSubmit" value="View Data">
</form>

<%
    if (request.getParameter("fSubmit") != null) {
        String param = request.getParameter("startingYear");
        String param2 = request.getParameter("endingYear");
        String gender = request.getParameter("gender");
        int startingYear = Integer.parseInt(param);
        int endingYear = Integer.parseInt(param2);
        if (endingYear > 2050 || startingYear < 1900 || startingYear > endingYear){
            %>
Illegal date filter
<%
        }
        else{
            ViewCauseDeathAction CDA = new ViewCauseDeathAction(prodDAO);
            String output = CDA.getCODStatisticsMID(loggedInMID, startingYear, endingYear, gender);
            if (output == "")
                output = "Wow, no one died, good job Doc!";
%>
Cause of death statistics for your patients: <br><%=output%><br>
<%
            output = CDA.getCODStatisticsAll(startingYear, endingYear, gender);
            if (output == "")
                output = "No deaths in a whole hospital? Close the morgue, we're on a streak!";
%>
Cause of death statistics across all patients: <br><%=output%>
<%
        }
    }


%>
<%@include file="/footer.jsp" %>