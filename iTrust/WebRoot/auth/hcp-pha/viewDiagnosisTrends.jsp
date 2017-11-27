<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>


<% 
	//log the page view
	loggingAction.logEvent(TransactionType.DIAGNOSIS_TRENDS_VIEW, loggedInMID.longValue(), 0, "");

	ViewDiagnosisStatisticsAction diagnoses = new ViewDiagnosisStatisticsAction(prodDAO);
	ArrayList<DiagnosisStatisticsBean> region_ds_bean=new ArrayList<>();
	ArrayList<DiagnosisStatisticsBean> state_ds_bean=new ArrayList<>();
	ArrayList<DiagnosisStatisticsBean> country_ds_bean=new ArrayList<>();
	//get form data
	String startDate = request.getParameter("startDate");
	
	String zipCode = request.getParameter("zipCode");
	if (zipCode == null)
		zipCode = "";
	
	String icdCode = request.getParameter("icdCode");
	
	//try to get the statistics. If there's an error, print it. If null is returned, it's the first page load
	try{
		region_ds_bean=diagnoses.getDiagnosisStatistics_region(startDate,icdCode,zipCode);
		state_ds_bean=diagnoses.getDiagnosisStatistics_state(startDate,icdCode,zipCode);
		country_ds_bean=diagnoses.getDiagnosisStatistics_country(startDate,icdCode,zipCode);


	} catch(FormValidationException e){
		e.printHTML(pageContext.getOut());
	}
	
	if (startDate == null)
		startDate = "";
	if (icdCode == null)
		icdCode = "";
	
%>
<br />
<form action="viewDiagnosisStatistics.jsp" method="post" id="formMain">
<input type="hidden" name="viewSelect" value="trends" />
<table class="fTable" align="center" id="diagnosisStatisticsSelectionTable">
	<tr>
		<th colspan="4">Diagnosis Statistics</th>
	</tr>
	<tr class="subHeader">
		<td>Diagnosis:</td>
		<td>
			<select name="icdCode" style="font-size:10" >
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
		</td>
		<td>Zip Code:</td>
		<td ><input name="zipCode" value="<%= StringEscapeUtils.escapeHtml(zipCode) %>" /></td>
	</tr>
	<tr class="subHeader">
		<td>Date: </td>
		<td>
			<input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (startDate)) %>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
		</td>

		<td style=display:none>End Date:</td>
		<td></td><td></td>

	</tr>
	<tr>
		<td colspan="4" style="text-align: center;"><input type="submit" id="select_diagnosis" value="View Statistics"></td>
	</tr>
</table>	

</form>

<br />

<%  if (region_ds_bean != null) { %>

<br />
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

<body>
	<div id="barchart_material" style="width:900px; height:500px;"></div>
</body>
<% } %>


<br />
<br />
