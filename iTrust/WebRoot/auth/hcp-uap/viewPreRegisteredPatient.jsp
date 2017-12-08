<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Pre-Registered Patient Record";
%>

<%@include file="/header.jsp" %>
<%
    String switchString = "";
    if (request.getParameter("switch") != null) {
        switchString = request.getParameter("switch");
    }

    String patientString = "";
    if (request.getParameter("patient") != null) {
        patientString = request.getParameter("patient");
    }

    String pidString;
    long pid = 0;

    if (switchString.equals("true")) pidString = "";
    else if (!patientString.equals("")) {

        int patientIndex = Integer.parseInt(patientString);
        List<PatientBean> patients = (List<PatientBean>) session.getAttribute("patients");
        pid = patients.get(patientIndex).getMID();
        pidString = "" + pid;
        session.removeAttribute("patients");
        session.setAttribute("pid", pidString);
    }
    else {
        if (session.getAttribute("pid") == null) {
            pid = 0;
            pidString = "";
        } else {
            pid = (long) Long.parseLong((String) session.getAttribute("pid"));
            pidString = ""+pid;
        }
    }

    if (pidString == null || 1 > pidString.length()) {
        response.sendRedirect("../getPatientID.jsp?forward=hcp-uap/editPHR.jsp");

        return;
    }
    loggingAction.logEvent(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, loggedInMID.longValue(), pid, "");

    EditPHRAction action = new EditPHRAction(prodDAO,loggedInMID.longValue(), pidString);
    pid = action.getPid();
    String confirm = "";
    if(request.getParameter("addA") != null)
    {
        try{
            confirm = action.updateAllergies(pid,request.getParameter("description"));
            loggingAction.logEvent(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, loggedInMID.longValue(), pid, "");
        } catch(Exception e)
        {
            confirm = e.getMessage();
        }
    }

    PatientBean patient = action.getPatient();
    List<HealthRecord> records = action.getAllHealthRecords();
    HealthRecord mostRecent = records.size() > 0 ? records.get(0) : null;
%>


<%@page import="edu.ncsu.csc.itrust.exception.NoHealthRecordsException"%><script type="text/javascript">
    function showRisks(){
        document.getElementById("risks").style.display="inline";
        document.getElementById("riskButton").style.display="none";
    }
</script>

<% if (!"".equals(confirm)) {%>
<span class="iTrustError"><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span><br />
<% } %>

<br />
<div align=center>
    <div style="margin-right: 10px;">
        <table class="fTable" align="center">
            <tr>
                <th colspan="2">Patient Information</th>
            </tr>
            <tr>
                <td class="subHeaderVertical">Name:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></td>
            </tr>
            <tr>
                <td  class="subHeaderVertical">Address:</td>
                <td > <%= StringEscapeUtils.escapeHtml("" + (patient.getStreetAddress1())) %><br />
                    <%="".equals(patient.getStreetAddress2()) ? "" : patient.getStreetAddress2() + "<br />"%>
                    <%= StringEscapeUtils.escapeHtml("" + (patient.getStreetAddress3())) %><br />
                </td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Phone:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (patient.getPhone())) %></td>
            </tr>
            <tr>
                <td class="subHeaderVertical" >Email:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (patient.getEmail())) %></td>
            </tr>

            <tr>
                <th colspan="2">Insurance Information</th>
            </tr>
            <tr>
                <td class="subHeaderVertical" >Provider Name:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcName())) %></td>
            </tr>
            <tr>
                <td  class="subHeaderVertical">Address:</td>
                <td > <%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress1())) %><br />
                    <%="".equals(patient.getIcAddress2()) ? "" : patient.getIcAddress2() + "<br />"%>
                    <%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress3())) %><br />
                </td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Phone:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcPhone())) %></td>
            </tr>
            <tr>
                <th colspan="2">Basic Health Records</th>
            </tr>
            <% if (null == mostRecent) { %>
            <tr><td colspan=2>No basic health records are on file for this patient</td></tr>
            <% } else {%>
            <tr>
                <td class="subHeaderVertical">Height:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getHeight())) %>in.</td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Weight:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getWeight())) %>lbs.</td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Smoker?:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getSmokingStatus()) + " - " + (mostRecent.getSmokingStatusDesc())) %></td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Blood Pressure:</td>
                <td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getBloodPressureN())) %>/<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getBloodPressureD())) %>mmHg</td>
            </tr>
            <tr>
                <td class="subHeaderVertical">Cholesterol:</td>
                <td >
                    <table>
                        <tr>
                            <td style="text-align: right">HDL:</td>
                            <td><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolHDL())) %> mg/dL</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">LDL:</td>
                            <td><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolLDL())) %> mg/dL</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Tri:</td>
                            <td><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolTri())) %> mg/dL</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">Total:</td>
                            <td>
                                <span id="totalSpan" style="font-weight: bold;"><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getTotalCholesterol())) %> mg/dL</span>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <% } //closing for "there is a most recent record for this patient" %>
        </table>
        <br />
    </div>
</div>

<br /><br /><br />
<%@include file="/footer.jsp" %>
