<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="javafx.util.Pair" %>
<%@ page import="edu.ncsu.csc.itrust.beans.DiagnosisBean" %>
<%@ page import="edu.ncsu.csc.itrust.action.*" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View prescription trends report";
%>

<%@include file="/header.jsp" %>
<%
    String icdCode = request.getParameter("icdCode");
    String gender = request.getParameter("gender");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Boolean allGood = true;
    String d_left = request.getParameter("startDate");
    Date date_left = null;
    if (!d_left.equals("")){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            date_left = df.parse(d_left);
        } catch (ParseException e) {
            allGood = false;
        %> Error! First date format should be "yyyy-mm-dd" <br><%
        }
    }

    String d_right = request.getParameter("endDate");
    Date date_right = null;
    if (!d_right.equals("")){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            date_right = df.parse(d_right);
        } catch (ParseException e) {
            allGood = false;
        %> Error! Second date format should be "yyyy-mm-dd" <br><%
        }
    }
    if (date_right.after(date_left)) {
        %> Error! Start Date must be before End Date <br><%
    }

    ViewPrescriptionStatisticsAction vsa = new ViewPrescriptionStatisticsAction(prodDAO);
    List<OfficeVisitBean> officeVisits = vsa.getFilteredOfficeVisits(icdCode, gender, date_left, date_right);
    HashMap<String, Integer> stats = vsa.getPrescriptionStatistics(officeVisits);
    HashMap<String, List <Long>> ovDict = vsa.getOfficeVisitsForPrescription(officeVisits);

%>



<br />
<form action="viewDiagnosisStatistics.jsp" method="post" id="formMain">
    <input type="hidden" name="viewSelect" value="trends" />
                Diagnosis:
                <select name="icdCode" >
                    <option value="">-- None Selected --</option>
                    <%for(DiagnosisBean diag : diagnoses.getDiagnosisCodes()) { %>
                    <%if (diag.getICDCode().equals(icdCode)) { %>
                    <option selected="selected" value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
                        - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
                    <% } else { %>
                    <option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
                        - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
                    <% } %>
                    <%}%>
                </select>

                <br><br> Gender:
                <select name="gender">
                    <option value="all">all</option>
                    <option value="male">male</option>
                    <option value="female">female</option>
                </select>

                 <br><br> Start Date:
                <% if (d_left != null){
                    %> <input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (d_left)) %>" size="10"> <%
                }
                else{
                    %> <input name="startDate"  size="10"> <%
                } %>
                <input type=button value="Select Date" onclick="displayDatePicker('startDate');">

                <br><br> End  Date:
                <% if (d_right != null){
                %> <input name="endDate" value="<%= StringEscapeUtils.escapeHtml("" + (d_right)) %>" size="10"> <%
                }
                else{
                %> <input name="endDate"  size="10"> <%
                    } %>
                    <input type=button value="Select Date" onclick="displayDatePicker('endDate');">

</form>

<script type = "text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type = "text/javascript">
    var region_array=[];
    var state_array=[];
    var country_array=[];

    <% for (int i=0;i<region_ds_bean.size();i++){%>
    region_array.push("<%= region_ds_bean.get(i).getRegionStats()%>");
    state_array.push("<%= state_ds_bean.get(i).getRegionStats()%>");
    country_array.push("<%= country_ds_bean.get(i).getRegionStats()%>");
    <%}%>

    var week_1=[];
    week_1.push('Week -1');
    week_1.push(parseInt(region_array[0]));
    week_1.push(parseInt(state_array[0]));
    week_1.push(parseInt(country_array[0]));

    var week_2=[];
    week_2.push('Week -2');
    week_2.push(parseInt(region_array[1]));
    week_2.push(parseInt(state_array[1]));
    week_2.push(parseInt(country_array[1]));

    var week_3=[];
    week_3.push('Week -3');
    week_3.push(parseInt(region_array[2]));
    week_3.push(parseInt(state_array[2]));
    week_3.push(parseInt(country_array[2]));

    var week_4=[];
    week_4.push('Week -4');
    week_4.push(parseInt(region_array[3]));
    week_4.push(parseInt(state_array[3]));
    week_4.push(parseInt(country_array[3]));

    var week_5=[];
    week_5.push('Week -5');
    week_5.push(parseInt(region_array[4]));
    week_5.push(parseInt(state_array[4]));
    week_5.push(parseInt(country_array[4]));

    var week_6=[];
    week_6.push('Week -6');
    week_6.push(parseInt(region_array[5]));
    week_6.push(parseInt(state_array[5]));
    week_6.push(parseInt(country_array[5]));

    var week_7=[];
    week_7.push('Week -7');
    week_7.push(parseInt(region_array[6]));
    week_7.push(parseInt(state_array[6]));
    week_7.push(parseInt(country_array[6]));

    var week_8=[];
    week_8.push('Week -8');
    week_8.push(parseInt(region_array[7]));
    week_8.push(parseInt(state_array[7]));
    week_8.push(parseInt(country_array[7]));

    google.charts.load('current',{'packages':['bar']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ['Week','Region Count','State Count','Country Count'],
            week_8,
            week_7,
            week_6,
            week_5,
            week_4,
            week_3,
            week_2,
            week_1
        ]);
        var options={
            chart: {
                title: 'Trend Report',
                subtitle: 'Regional Count, State Count and Country Count'
            },
            vAxis: {
                minValue:0,
                viewWindow: {
                    min: 0
                }
            },
            bars:'vertical',
            height: 400
        };

        var chart= new google.charts.Bar(document.getElementById('barchart_material'));

        chart.draw(data,google.charts.Bar.convertOptions(options));
    }

</script>

<%@include file="/footer.jsp" %>