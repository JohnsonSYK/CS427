<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptRequestBean"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewApptRequestsAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Appointment Requests";
%>

<style>
	.hidden {
		display: none !important;
	}
</style>

<%@include file="/header.jsp"%>


<h1>Send Appointment Reminder</h1>

<script type="text/javascript">
$(document).ready(function() {
    $('#submitDays').click(function() {
       const val = $('#numDaysInput').val();
       if(val == "") {
           alert('You need something in the box');
           return;
	   }
	   const num = parseInt(val);
       if(isNaN(num)) {
           alert("You need to put an integer number");
           return;
	   }
        $.ajax({
            url : "/iTrust/auth/AppointmentDayServlet",
            data : {
                days : num,
                mid: <%= loggedInMID %>
            },
            success : function(e){
                $('#daysDiv').addClass('hidden');
                $('#doneDiv').removeClass('hidden');
            },
            error : function(e) {
                $('#daysDiv').addClass('hidden');
                $('#errorDiv').removeClass('hidden');
			}
        });
	});
});
</script>

<div style="border: 1px solid Gray; padding:5px;float:left;" id="daysDiv">
	<h2>Enter number of days:</h2>
	<input id="numDaysInput" style="width: 250px;" type="text" value="" />
	<button style="..." id="submitDays">Submit</button>
	<br />
</div>

<div style="..." id="doneDiv" class="hidden">
	<h2>You have completed your action!</h2>
</div>

<div style="..." id="errorDiv" class="hidden">
	<h2>Terribly sorry, we cannot complete your action at this time. Please try again later.</h2>
</div>



<%@include file="/footer.jsp"%>