<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="javafx.util.Pair" %>
<%@ page import="edu.ncsu.csc.itrust.action.*" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.*" %>
<%@ page import="edu.ncsu.csc.itrust.beans.*" %>
<%@ page import="java.text.ParsePosition" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View prescription trends report";
%>

<%@include file="/header.jsp" %>

<%
    ICDCodesDAO diagnoses = prodDAO.getICDCodesDAO();
    NDCodesDAO ndDiagnoses = prodDAO.getNDCodesDAO();
    PatientDAO patientDAO = prodDAO.getPatientDAO();
    OfficeVisitDAO ovDAO = prodDAO.getOfficeVisitDAO();
    PrescriptionsDAO preDAO = prodDAO.getPrescriptionsDAO();
    PersonnelDAO personDAO = prodDAO.getPersonnelDAO();

    Boolean allGood = true;
    String icdCode = request.getParameter("icdCode");
    String icdError = "";
    if (request.getParameter("fSubmit") != null && (icdCode == null || icdCode.equals(""))){
        allGood = false;
        icdError = "Error! Select diagnosis";
    }
    String gender = request.getParameter("gender");

    String dleft_error = "";
    String d_left = request.getParameter("startDate");
    Date date_left = null;
    if (d_left != null && !d_left.equals("")){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        ParsePosition pos = new ParsePosition(0);
        date_left = df.parse(d_left, pos);
        if (pos.getIndex() < d_left.length()){
            date_left = null;
            allGood = false;
            dleft_error = "Error! First date format should be 'MM/dd/yyyy'";
        }
    }
    String dright_error = "";
    String d_right = request.getParameter("endDate");
    Date date_right = null;
    if (d_right != null && !d_right.equals("")){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        ParsePosition pos = new ParsePosition(0);
        date_right = df.parse(d_right, pos);
        if (pos.getIndex() < d_right.length()){
            date_right = null;
            allGood = false;
            dright_error = "Error! Second date format should be 'MM/dd/yyyy'";
        }
    }
    String date_order_error = "";
    if (date_right != null && date_left != null && date_right.before(date_left)) {
        date_order_error =  "Error! Your dates are out of order";
        allGood = false;
    }

%>


<br />
<form action="viewPrescriptionTrends.jsp" method="post" id="formMain">
    <input type="hidden" name="viewSelect" value="trends" />
                Diagnosis:
                <select name="icdCode" >
                    <option value="">-- None Selected --</option>
                    <%for(DiagnosisBean diag : diagnoses.getAllICDCodes()) { %>
                    <%if (diag.getICDCode().equals(icdCode)) { %>
                    <option selected="selected" value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
                        - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
                    <% } else { %>
                    <option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
                        - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
                    <% } %>
                    <%}%>
                </select>
                <%= icdError %>
                <br><br> Gender:
                <select name="gender">
                    <option value="All">All</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>

                 <br><br> Start Date:
                <% if (d_left != null){
                    %> <input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (d_left)) %>" size="10"> <%
                }
                else{
                    %> <input name="startDate"  size="10"> <%
                } %>
                <input type=button value="Select Date" onclick="displayDatePicker('startDate');">
                <%= dleft_error %>
                <br><br> End  Date:
                <% if (d_right != null){
                %> <input name="endDate" value="<%= StringEscapeUtils.escapeHtml("" + (d_right)) %>" size="10"> <%
                }
                else{
                %> <input name="endDate"  size="10"> <%
                    } %>
                    <input type=button value="Select Date" onclick="displayDatePicker('endDate');">
                <%= dright_error %>
                <br><br>
                <input type="submit" name ='fSubmit' value = "View Data">
                <br><br>
</form>
<%=date_order_error%>
<% if (request.getParameter("fSubmit") != null && allGood){
    ViewPrescriptionStatisticsAction vsa = new ViewPrescriptionStatisticsAction(prodDAO);
    List<OfficeVisitBean> officeVisits = vsa.getFilteredOfficeVisits(icdCode, gender, date_left, date_right);
    HashMap<String, Integer> stats = vsa.getPrescriptionStatistics(officeVisits);
    List<PrescriptionStatisticsBean> results = vsa.getTable(icdCode, gender, date_left, date_right);%>

    <style>
        .google-visualization-table-td {
            text-align: center !important;
        }
    </style>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <%

    if (results != null && results.size() > 0 && stats != null && stats.size() > 0){ %>
        <script type="text/javascript">
                google.charts.load('current', {'packages':['table']});
        google.charts.setOnLoadCallback(drawTable);
        var table_data = [];
        <%
        for (PrescriptionStatisticsBean result : results) {
            long pre_id = result.getPrescriptionID();
            PrescriptionBean prescript = preDAO.getByID(pre_id);
            OfficeVisitBean officeVisit = ovDAO.getOfficeVisit(result.getVisitID());
            MedicationBean medBean = prescript.getMedication();
            long HCP_id = officeVisit.getHcpID();
            long P_id = officeVisit.getPatientID();
            Date ov_date = officeVisit.getVisitDate();
            Calendar ov_cal = Calendar.getInstance();
            ov_cal.setTime(ov_date);
            PersonnelBean personnel = personDAO.getPersonnel(HCP_id);
            String HCP_name = personnel.getFirstName()+' '+personnel.getLastName();
            PatientBean patient = patientDAO.getPatient(P_id);
            String patient_name = patient.getFirstName()+' '+patient.getLastName();
            String patient_gender = patient.getGender().getName();
            String diagnosis  = diagnoses.getICDCode(result.getIcdCode()).getDescription();
            MedicationBean medication = prescript.getMedication();
            String medicationCode = medication.getNDCode();
            String medicationName = medication.getDescription();
            Date prescriptionStart = prescript.getStartDate();
            Calendar start_cal = Calendar.getInstance();
            ov_cal.setTime(prescriptionStart);
            Date prescriptionEnd = prescript.getEndDate();
            Calendar end_cal = Calendar.getInstance();
            end_cal.setTime(prescriptionEnd);
            int dosage = prescript.getDosage();
            String instructions = prescript.getInstructions();
        %>
            table_data.push([ <%= result.getVisitID() %> ,new Date (<%= ov_cal.get(Calendar.YEAR)%>, <%=ov_cal.get(Calendar.MONTH)%>, <%=ov_cal.get(Calendar.DAY_OF_MONTH) %>),
            <%= HCP_id %> ,"<%= HCP_name %>" ,
            <%= P_id %> ,"<%= patient_name %>","<%= patient_gender %>" ,"<%= result.getIcdCode() %>", "<%= (diagnosis==null || diagnosis.equals(""))? "Description not found" : diagnosis %>" ,
            "<%= medicationCode %>" , "<%= medicationName %>" ,new Date (<%= start_cal.get(Calendar.YEAR)%>, <%=start_cal.get(Calendar.MONTH)%>, <%=start_cal.get(Calendar.DAY_OF_MONTH) %>) ,
            new Date (<%= end_cal.get(Calendar.YEAR)%>, <%=end_cal.get(Calendar.MONTH)%>, <%=end_cal.get(Calendar.DAY_OF_MONTH) %>) ,
            {v:<%= dosage %>,f:"<%=dosage%> units"},"<%= instructions %>"]);
            <% }
            %>
        function drawTable() {
        var data = new google.visualization.DataTable();
        data.addColumn('number', 'Office Visit ID');
        data.addColumn('date', 'Office Visit Date');
        data.addColumn('number', 'HCP ID');
        data.addColumn('string', 'HCP Name');
        data.addColumn('number', 'Patient ID');
        data.addColumn('string', 'Patient Name');
        data.addColumn('string', 'Patient Gender');
        data.addColumn('string', 'ICD Code');
        data.addColumn('string', 'Diagnosis Name');
        data.addColumn('string', 'ND Code');
        data.addColumn('string', 'Medication Name');
        data.addColumn('date', 'Medication Start Date');
        data.addColumn('date', 'Medication End Date');
        data.addColumn('number', 'Medication Dosage');
        data.addColumn('string', 'Medication Instructions');
        console.log(table_data);
        data.addRows(table_data);

        var table = new google.visualization.Table(document.getElementById('table_div'));

        table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
        }
        </script> <div id="table_div" class = "google-visualization-table-td"></div>

        <script type = "text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        var stats_array = [['Medication', 'Frequency of prescriptions for given diagnosis', {role: 'style'}]];
        <% List<String> colors = new ArrayList<>();
        colors.add("#0303ec");
        colors.add("#e90911");
        colors.add("#fff60b");
        colors.add("#19e42e");
        colors.add("#8624d1");
        int counter = 0;
        for (String stat : stats.keySet()){
            String icd = ndDiagnoses.getNDCode(stat).getDescription();%>
        stats_array.push(["<%= stat + ": " + ((icd==null || icd.equals("")) ? "Description not found" : icd) %>",<%= 1.0*stats.get(stat)/officeVisits.size() %>, "<%= colors.get(counter % 5)%>"]);
        <% counter ++;
        }%>
        function drawChart() {

            var data = google.visualization.arrayToDataTable(stats_array);

            var options = {
                title: "Prescription Statistics",
                chartArea: {width: "50%", height: "70%"},
                legend: {position: "none"}
            };

            var chart = new google.visualization.BarChart(document.getElementById('barchart'));
            console.log('something happened');
            chart.draw(data, options);
        }
    </script> <div id="barchart" style="width: 900px; height: 500px;"></div>
    <%}
    else{
        %> No results were found and the charts were not generated<%
    }%>

    <%
}

%>


<%@include file="/footer.jsp" %>