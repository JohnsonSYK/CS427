<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Pre-Registered Patients";
%>

<%@include file="/header.jsp" %>

<%
    PatientDAO patientDAO = prodDAO.getPatientDAO();

    EditPatientAction action;
    String prevAction = "";
    boolean activatedPatient = false;
    if (request.getParameter("activateButton") != null) {
        String clickedPatientMID = request.getParameter("activateButton");
        action = new EditPatientAction(prodDAO, loggedInMID.longValue(), clickedPatientMID);
        action.activate();
        activatedPatient = true;
        PatientBean patient = patientDAO.getPatient(Long.parseLong(clickedPatientMID));
        prevAction = "Successfully activated patient " + patient.getFirstName() + " " + patient.getLastName();
        session.setAttribute("pid", clickedPatientMID);
        loggingAction.logEvent(TransactionType.PATIENT_PREREGISTER_ACTIVATE, loggedInMID, 0, "");
    } else if (request.getParameter("deactivateButton") != null) {
        String clickedPatientMID = request.getParameter("deactivateButton");
        action = new EditPatientAction(prodDAO, loggedInMID.longValue(), clickedPatientMID);
        action.deactivate();
        PatientBean patient = patientDAO.getPatient(Long.parseLong(clickedPatientMID));
        prevAction = "Successfully deactivated patient " + patient.getFirstName() + " " + patient.getLastName();
        session.setAttribute("pid", clickedPatientMID);
        loggingAction.logEvent(TransactionType.PATIENT_PREREGISTER_DEACTIVATE, loggedInMID, 0, "");
    }

    List<PatientBean> preRegisteredPatients = patientDAO.getPreRegisteredPatients();
%>
<script src="/iTrust/DataTables/media/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script type="text/javascript">
    jQuery.fn.dataTableExt.oSort['lname-asc']  = function(x,y) {
        var a = x.split(" ");
        var b = y.split(" ");
        return ((a[1] < b[1]) ? -1 : ((a[1] > b[1]) ?  1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['lname-desc']  = function(x,y) {
        var a = x.split(" ");
        var b = y.split(" ");
        return ((a[1] < b[1]) ? 1 : ((a[1] > b[1]) ?  -1 : 0));
    };
</script>
<script type="text/javascript">
    $(document).ready(function() {
        $("#patientList").dataTable( {
            "aaColumns": [ [2,'dsc'] ],
            "aoColumns": [ { "sType": "lname" }, null, null],
            "bStateSave": true,
            "sPaginationType": "full_numbers"
        });
    });
</script>
<style type="text/css" title="currentStyle">
    @import "/iTrust/DataTables/media/css/demo_table.css";
</style>

<br />
<h2>Pre-Registered Patients</h2>
<p><%=prevAction%></p>
<%
    if (activatedPatient) {
%>
<a href="editPatient.jsp">Edit patient</a>
<%
    }
%>
<form action="viewPreRegisteredPatients.jsp" method="post" name="myform">
    <table class="display fTable" id="patientList" align="center">
        <thead>
        <tr class="">
            <th>Patient</th>
            <th>Activate</th>
            <th>Deactivate</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<PatientBean> patients = preRegisteredPatients;
            int index = 0;
            for (PatientBean patient : preRegisteredPatients) {
        %>
        <tr>
            <td >
                <a href="viewPreRegisteredPatient.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index))%>">
                    <%= StringEscapeUtils.escapeHtml("" + (patient.getFirstName() + " " + patient.getLastName())) %>
                </a>
            </td>
            <td ><button type="submit" name="activateButton" value="<%=patient.getMID()%>">Activate</button></td>
            <td ><button type="submit" name="deactivateButton" value="<%=patient.getMID()%>">Deactivate</button></td>
        </tr>
        <%
                index++;
            }
            session.setAttribute("patients", patients);
        %>
        </tbody>
    </table>
</form>
<br />
<br />

<%@include file="/footer.jsp" %>
