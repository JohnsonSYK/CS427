<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>

<%@page import="java.util.ArrayList" %>

<%@page import="edu.ncsu.csc.itrust.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@page import="edu.ncsu.csc.itrust.dao.mysql.HospitalsDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.WardBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.WardRoomBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.WardDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>

<!-- %@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%-->



<%@include file="/global.jsp" %>

<style>
    .hidden {
        display: none !important;
    }
</style>

<%
    pageTitle = "iTrust - Manage Wards";
%>

<%@include file="/header.jsp" %>

<%
    DAOFactory factory = DAOFactory.getProductionInstance();
    PersonnelDAO personnelDAO = factory.getPersonnelDAO();
    List<String> roles = personnelDAO.getAllUserRoles();
    String selectAllString = "all";
%>
<script type = "text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script>
    $(document).ready(function (){
        var state = "form"; //Can be "form", "row", or "bar"
        function getParams() {
            const prole = $('#primarySelect').val();
            const srole = $('#secondarySelect').val();
            const transactionNum = $('#transactionSelect').val();
            const startDate = $('#startDate').val();
            const endDate = $('#endDate').val();
            const obj = {prole: prole, srole: srole,
                transactionNum: transactionNum, startDate: startDate, endDate: endDate};
            if (transactionNum === "<%= selectAllString%>") {
                delete obj.transactionNum;
            }
            console.log(obj);
            return obj;
        }

        function sendRequest(obj) {
            $.ajax({
                url : "/iTrust/auth/TransactionLogServlet",
                data : obj,
                success : function(e) {
                    console.log(e);
                    $('#formDiv').addClass('hidden');
                    if(state === "row") {
                        showTable(e);
                    } else {
                        showCharts(e);
                    }
                },

                error : function(e) {
                    state = "form";
                    console.log("error");
                    console.log(e);
                }
            })
        }

        function showTable(data) {
            const tableDiv = $('#tableDiv');
            tableDiv.empty();

            if(data.length === 0){
                tableDiv.append("<h2>No Results!</h2>")
            } else {

                const keys = Object.keys(data[0]);
                const header = "<tr><th>" + keys.join("</th><th>") + "</th></tr>";
                tableDiv.append("<table id='rowTable'><tbody>"+header+"</tbody></table>");
                for(var i = 0; i < data.length; ++i) {
                    var obj = data[i];
                    var row = "<tr>"
                    for(var j = 0; j < keys.length; ++j) {
                        row += "<td>" + obj[keys[j]] + "</td>";
                    }
                    row += "</tr>"
                    $('#rowTable > tbody:last').append(row);
                }
            }

            tableDiv.removeClass('hidden');
        }

        function showCharts(data) {
            google.charts.load('current',{'packages':['bar']});
            google.charts.setOnLoadCallback(drawChart(data));
        }

        function drawChart(data) {
            return function() {
                const loggedFreq = data.map(function(e) { return e.loggedInMID; });
                drawSingleChart('loggedInMID', 'Frequency', loggedFreq, 'loggedInChart');
                $('#barDiv').removeClass('hidden');
            }
        }

        function getCounts(array) {
            var counts = {};
            for(var i = 0 ; i < array.length; ++i) {
                var obj = array[i];
                counts[obj] = counts[obj] ? counts[obj] +1 : 1;
            }
            return counts;
        }

        function drawSingleChart(xlabel, ylabel, counts, target) {
            const countsArray = getCounts(counts);
            const dataTable = Object.keys(countsArray).map(function(key){ return [key, countsArray[key]]});
            dataTable.unshift([xlabel, ylabel]);
            var data = google.visualization.arrayToDataTable(dataTable);
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

            var chart = new google.charts.Bar(document.getElementById(target));

            chart.draw(data,google.charts.Bar.convertOptions(options));
        }

        $("#submitRow").click(function(){
            const params = getParams();
            state = "row";
            sendRequest(params);
        });

        $("#submitSummary").click(function() {
            const params = getParams();
            state = "bar";
            sendRequest(params);
        })
    });
</script>

<div id="formDiv">

    <div id="primaryMIDDiv">
        <h2>Primary Role</h2>
        <select id="primarySelect">
            <%
                for (String role : roles) {
            %>
            <option value="<%=role %>"><%= role %></option> <%
            }
        %>
        </select>
    </div>

    <div id="secondaryMIDDiv">
        <h2>Secondary Role</h2>
        <select id="secondarySelect">
            <%
                for (String role : roles) {
            %>
            <option value="<%=role %>"><%= role %></option> <%
            }
        %>
        </select>
    </div>

    <div id="transactionTypeDiv">
        <h2>Transaction Type</h2>
        <select id="transactionSelect" >
            <option value="<%= selectAllString %>">Select All</option>
            <%
                for (TransactionType t : TransactionType.values()) {
            %>
            <option value="<%=t.getCode() %>"><%= t.getActionPhrase() %></option> <%
                }
            %>
        </select>
    </div>

    <div id="startDateDiv">
        <input name="startDate" id="startDate" value="" size="10">
        <input type=button value="Select Date" onclick="displayDatePicker('startDate');">
    </div>

    <div id="endDateDiv">
        <input name="endDate" id="endDate" value="" size="10">
        <input type=button value="Select Date" onclick="displayDatePicker('endDate');">
    </div>

    <button id="submitRow">Get Rows</button>

    <button id="submitSummary">Get Summary</button>

</div>

<div id="tableDiv" class="hidden">
    I am a table!
</div>

<div id="barDiv" class="hidden">
    <div id="loggedInChart" style="..."></div>
</div>
<%@include file="/footer.jsp" %>