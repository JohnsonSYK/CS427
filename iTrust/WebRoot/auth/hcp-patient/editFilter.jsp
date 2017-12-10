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
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Edit Filter";
    EditFilterAction efa = new EditFilterAction(prodDAO, loggedInMID);
    FilterBean cur = efa.pullCurrent();
    Boolean isNull = (cur == null);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

%>
<%@include file="/header.jsp" %>
<form action="editFilter.jsp" method="post" name="FilterForm">
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
            if (!isNull && cur.getDate_left() != null) {
        %>
                          value="<%=sdf.format(cur.getDate_left())%>"<%}%>/>
    <input type=button value="Select Date" onclick="displayDatePicker('date_left');"> <br>


    Ending Date: <input type="text" name="date_right"
        <%
            if (!isNull && cur.getDate_right() != null) {
        %>
                        value="<%=sdf.format(cur.getDate_right())%>"<%}%>/>
    <input type=button value="Select Date" onclick="displayDatePicker('date_right');"> <br>
    <input type="submit" name="cSubmit" value="Cancel">
    <input type="submit" name="fSubmit" value="Save Filter">
    <input type="submit" name="tSubmit" value="Test Search">
</form>

<%
    if (request.getParameter("cSubmit") != null){
        response.sendRedirect("editFilter.jsp");
    }
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
                String[] s1 = d_left.split("/",0);
                String dl = d_left;
                if(s1.length==3) {
                    dl = s1[2] + "-" + s1[0] + "-" + s1[1];
                }
                date_left = df.parse(dl);
            } catch (ParseException e) {
                allGood = false;
%> Error! First date format should be "yyyy-mm-dd"<br><%
        }

    }

    String d_right = request.getParameter("date_right");
    Date date_right = null;
    if (!d_right.equals("")){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            String[] s2 = d_right.split("/",0);
            String dr = d_right;
            if(s2.length==3){
                dr = s2[2]+"-"+s2[0]+"-"+s2[1];
            }
            date_right = df.parse(dr);
        } catch (ParseException e) {
            allGood = false;
%> Error! Second date format should be "yyyy-mm-dd"<br><%
        }

    }

    if(date_left!=null && date_right!=null){
        if(date_left.compareTo(date_right)>0){
            allGood=false;
%> Error! First date is after second date <br><%

        }
    }

    if(date_left!=null){
        String inputString = "1970-01-01";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date early = dateFormat.parse(inputString);
        if(date_left.compareTo(early)<0){
        allGood=false;
%> Error! First date cannot be that early <br><%
        }
    }

    if(date_right!=null && date_right.compareTo(new Date())>0){
        allGood=false;
%> Error! Second date cannot be after today <br><%
    }

    if (isNull && allGood){
        Boolean success = efa.addFilter(sender, subject, sub_pos, sub_neg, date_left, date_right);
        if (success){
%> Success! <%
    response.sendRedirect("editFilter.jsp");
}
else{
%> Error while adding a filter! Try again. <%
    }
}
else if (allGood){
    Boolean success = efa.updateFilter(sender, subject, sub_pos, sub_neg, date_left, date_right);
    if (success){
%> Success <%
    response.sendRedirect("editFilter.jsp");
}
else{
%> Error while updating the filter! Try again. <%
    }
}
else {
%> Error! Try again. <%
    }
}
else if (request.getParameter("tSubmit") != null){
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

    if (allGood){
        FilterBean testFilter = new FilterBean();
        testFilter.setMid(loggedInMID);
        testFilter.setSender(sender);
        testFilter.setSubject(subject);
        testFilter.setSubstring_pos(sub_pos);
        testFilter.setSubstring_neg(sub_neg);
        testFilter.setDate_left(date_left);
        testFilter.setDate_right(date_right);
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
        $("#mailbox").dataTable( {
            "aaColumns": [ [2,'dsc'] ],
            "aoColumns": [ { "sType": "lname" }, null, null, {"bSortable": false} ],
            "sPaginationType": "full_numbers"
        });
    });
</script>
<style type="text/css" title="currentStyle">
    @import "/iTrust/DataTables/media/css/demo_table.css";
</style>
<%
    PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
    PatientDAO patientDAO = new PatientDAO(prodDAO);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());

    List<MessageBean> messages = action.getAllMyMessages();
    session.setAttribute("messages", messages);
    FilterBean filter = testFilter;
    AuthDAO aDAO = new AuthDAO(prodDAO);
    if (filter.getMid() == -1L)
        filter = null;
    if(messages.size() > 0) { %>
<table id="mailbox" class="display fTable">
    <thead>
    <tr>
        <th><%= "Sender" %></th>
        <th>Subject</th>
        <th><%= "Received" %></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <%
        int index=-1;
        for(MessageBean message : messages) {
            Boolean good = true;
            Date msgDate = new Date(message.getSentDate().getTime());
            if (filter != null && ((filter.getSender() != null && !aDAO.getUserName(message.getFrom()).equals(filter.getSender())) ||
                    (filter.getSubject() != null && !message.getSubject().equals(filter.getSubject())) ||
                    (filter.getSubstring_pos() != null && !message.getSubject().contains(filter.getSubstring_pos()) && !message.getBody().contains(filter.getSubstring_pos())) ||
                    (filter.getSubstring_neg() != null && (message.getSubject().contains(filter.getSubstring_neg()) || message.getBody().contains(filter.getSubstring_neg()))) ||
                    (filter.getDate_left() != null && msgDate.before(filter.getDate_left())) ||
                    (filter.getDate_right() != null && msgDate.after(filter.getDate_right()))
            )){
                good = false;
            }
            index ++;
            if (good){
                String style = "";
                if(message.getRead() == 0) {
                    style = "style=\"font-weight: bold;\"";
                }
                String primaryName = action.getName(message.getFrom());
                List<MessageBean> ccs = action.getCCdMessages(message.getMessageId());
                String ccNames = "";
                int ccCount = 0;
                for(MessageBean cc:ccs){
                    ccCount++;
                    long ccMID = cc.getTo();
                    ccNames += action.getPersonnelName(ccMID) + ", ";
                }
                ccNames = ccNames.length() > 0?ccNames.substring(0, ccNames.length()-2):ccNames;
                String toString = primaryName;
                if(ccCount>0){
                    String ccNameParts[] = ccNames.split(",");
                    toString = toString + " (CC'd: ";
                    for(int i = 0; i < ccNameParts.length-1; i++) {
                        toString += ccNameParts[i] + ", ";
                    }
                    toString += ccNameParts[ccNameParts.length - 1] + ")";
                }
    %>
    <tr <%=style%>>
        <td><%= StringEscapeUtils.escapeHtml("" + ( toString)) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + ( message.getSubject() )) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + ( dateFormat.format(message.getSentDate()) )) %></td>
        <td><a href="<%= "viewMessageInbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
    </tr>
    <%
                }
            }
        }
    %>
    </tbody>
</table>
<%      if (messages.size() == 0) { %>
<div>
    <i>You have no messages</i>
</div>
<%	    }
}
}
%>
<%@include file="/footer.jsp" %>
