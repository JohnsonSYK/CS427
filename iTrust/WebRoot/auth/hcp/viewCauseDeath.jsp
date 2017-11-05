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
%>

<%@include file="/header.jsp" %>


<form action="viewCauseDeath.jsp" method="post">
    <input type="hidden" name="confirmAction" value="true"/>
    <input type="number" name="startingYear" min="1900" max = "2020" />
    <input type="number" name="endingYear" min="1900" max = "2020"/>
    <select name="gender">
        <option value="male">male</option>
        <option value="female">female</option>
        <option value="all">all</option>
    </select>
    <input type="submit" name="fSubmit" value="View Data">
</form>

<%
    if (request.getParameter("fSubmit") != null) {
        String param = request.getParameter("startingYear");
        String param2 = request.getParameter("endingYear");
        String gender = request.getParameter("gender");
        int startingYear = Integer.parseInt(param);
        int endingYear = Integer.parseInt(param2);

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


%>
<%@include file="/footer.jsp" %>