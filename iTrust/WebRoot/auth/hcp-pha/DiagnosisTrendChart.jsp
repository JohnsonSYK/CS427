<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.charts.DiagnosisTrendData" %>

<%

DiagnosisStatisticsBean avgBean = null;


%>

<!-- Use this tag to specify the location of the dataset for the chart -->
<jsp:useBean id="DSchart" class="edu.ncsu.csc.itrust.charts.DiagnosisTrendData"/>

<%

// This calls the class from the useBean tag and initializes the Adverse Event list and pres/immu name



if ( view.equalsIgnoreCase("trends") ) {
	
	DSchart.initializeDiagnosisStatistics(dsBean, view);
	
} else  {
	
	return;
	
}

String chartTitle = "Diagnosis Cases Chart";

%>


