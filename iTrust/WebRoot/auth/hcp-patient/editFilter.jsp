<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.FilterDAO"%>
<%@ page import="edu.ncsu.csc.itrust.action.EditFilterAction" %>
<%@ page import="edu.ncsu.csc.itrust.beans.FilterBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.ParseException" %>

<%@include file="/global.jsp" %>
<%
    pageTitle = "iTrust - Edit Filter";
    EditFilterAction efa = new EditFilterAction(prodDAO, loggedInMID);
    FilterBean cur = efa.pullCurrent();
    Boolean isNull = (cur.getMid()==-1L);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>
<%@include file="/header.jsp" %>
<form action="editFilter.jsp" method="post" onsubmit="setTimeout(function (){window.location.reload();}, 100)">
    Sender/Receiver: <input type="text" name="sender"
        <%
            if (!isNull  && cur.getSender() != null && !cur.getSender().equals("")){
                %>
                value="<%=cur.getSender()%>"<%
            }

%>/> <br>
    Subject: <input type="text" name="subject"
        <%
            if (!isNull  && cur.getSubject() != null && !cur.getSubject().equals("")){
        %>
                    value="<%=cur.getSubject()%>"<%
    }

%>/> <br>
    Includes: <input type="text" name="sub_pos"
        <%
            if (!isNull && cur.getSubstring_pos() != null && !cur.getSubstring_pos().equals("")){
        %>
                     value="<%=cur.getSubstring_pos()%>"<%
    }

%>/> <br>
    Excludes: <input type="text" name="sub_neg"
        <%
            if (!isNull && cur.getSubstring_neg() != null && !cur.getSubstring_neg().equals("")){
        %>
                     value="<%=cur.getSubstring_neg()%>"<%
    }

%>/> <br>
    Starting Date: <input type="text" name="date_left"
        <%
            if (!isNull && cur.getDate_left() != null){
        %>
                          value="<%=sdf.format(cur.getDate_left())%>"<%
    }

%>/> <br>
    Ending Date: <input type="text" name="date_right"
        <%
            if (!isNull && cur.getDate_right() != null){
        %>
                        value="<%=sdf.format(cur.getDate_right())%>"<%
    }

%>/> <br>
    <input type="submit" name="cSubmit" value="Cancel" onClick="window.location.reload();"> <br>
    <input type="submit" name="fSubmit" value="Save Filter"> <br>
</form>

<%
    if (request.getParameter("fSubmit") != null){
        Boolean allGood = true;
        String sender = request.getParameter("sender");
        if (sender.equals(""))
            sender = null;
        String subject = request.getParameter("subject");
        if (subject.equals(""))
            subject = null;
        String sub_pos = request.getParameter("sub_pos");
        if (sub_pos.equals(""))
            sub_pos = null;
        String sub_neg = request.getParameter("sub_neg");
        if (sub_neg.equals(""))
            sub_neg = null;
        String d_left = request.getParameter("date_left");
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

        String d_right = request.getParameter("date_right");
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
        if (isNull && allGood){
            Boolean success = efa.addFilter(sender, subject, sub_pos, sub_neg, date_left, date_right);
            if (success){
                %> Success! <%
            }
            else{
                %> Error while adding a filter! Try again. <%
            }
        }
        else if (allGood){
            Boolean success = efa.updateFilter(sender, subject, sub_pos, sub_neg, date_left, date_right);
            if (success){
                %> Success <%
            }
            else{
                %> Error while updating the filter! Try again. <%
            }
        }
        else {
%> Error! Try again. <%
            }
        response.sendRedirect("editFilter.jsp");
    }
%>
<%@include file="/footer.jsp" %>
