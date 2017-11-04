<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%@ page import="org.apache.commons.codec.digest.DigestUtils" %>
<%@ page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%@ page import="edu.ncsu.csc.itrust.beans.HealthRecord" %>
<%@ page import="edu.ncsu.csc.itrust.enums.Role" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Pre-Register as Patient";
%>

<%@include file="/header.jsp" %>

<h1>Pre-Register as Patient</h1>

<%
    String firstName = request.getParameter("p_fname");
    String lastName = request.getParameter("p_lname");
    String email = request.getParameter("p_email");
    String password = request.getParameter("p_password");
    String pwVerify = request.getParameter("p_verify");

    String addr1 = request.getParameter("p_address1");
    String addr2 = request.getParameter("p_address2");
    String city = request.getParameter("p_city");
    String state = request.getParameter("p_state");
    String zipcode = request.getParameter("p_zipcode");
    String phone = request.getParameter("p_phone");

    String icAddr1 = request.getParameter("p_icAddress1");
    String icAddr2 = request.getParameter("p_icAddress2");
    String icCity = request.getParameter("p_icCity");
    String icState = request.getParameter("p_icState");
    String icZipcode = request.getParameter("p_icZipcode");
    String icPhone = request.getParameter("p_icPhone");

    String height = request.getParameter("p_height");
    String weight = request.getParameter("p_weight");
    String smoker = request.getParameter("p_smoker");

    if (firstName != null && "".equals(firstName)) {
        String msg = "Please enter your first name";
%>
<div style="align: center; margin-bottom: 10px;">
    <span class="iTrustError" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (msg)) %></span>
</div>
<%
    } else if (lastName != null && "".equals(lastName)) {
        String msg = "Please enter your last name";
%>
<div style="align: center; margin-bottom: 10px;">
    <span class="iTrustError" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (msg)) %></span>
</div>
<%
    } else if (email != null && !prodDAO.getPatientDAO().validPatientEmail(email)) {
        String msg = "This email is already being used";
%>
<div style="align: center; margin-bottom: 10px;">
    <span class="iTrustError" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (msg)) %></span>
</div>
<%
    } else if (email != null && "".equals(email)) {
        String msg = "Please enter an email address";
%>
<div style="align: center; margin-bottom: 10px;">
    <span class="iTrustError" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (msg)) %></span>
</div>
<%
    } else if (password != null && pwVerify != null && ("".equals(password) || "".equals(pwVerify))) {
        String msg = "Please enter a password and confirm it";
%>
<div style="align: center; margin-bottom: 10px;">
    <span class="iTrustError" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (msg)) %></span>
</div>
<%
    } else if (password != null && pwVerify != null && !password.equals(pwVerify)) {
        String msg = "Password and verification don't match";
%>
<div style="align: center; margin-bottom: 10px;">
    <span class="iTrustError" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (msg)) %></span>
</div>
<%
    } else if (firstName != null && lastName != null && email != null && password != null && pwVerify != null) {
        long patientMID = prodDAO.getPatientDAO().addEmptyPatient();
        PatientBean patientBean = new PatientBean();
        patientBean.setMID(patientMID);
        patientBean.setFirstName(firstName);
        patientBean.setLastName(lastName);
        patientBean.setEmail(email);
        patientBean.setPassword(password);

        patientBean.setStreetAddress1(addr1);
        patientBean.setStreetAddress2(addr2);
        patientBean.setCity(city);
        if (!state.equals(""))
            patientBean.setState(state);
        patientBean.setZip(zipcode);
        patientBean.setPhone(phone);

        patientBean.setIcAddress1(icAddr1);
        patientBean.setIcAddress2(icAddr2);
        patientBean.setIcCity(icCity);
        if (!icState.equals(""))
            patientBean.setIcState(icState);
        patientBean.setIcZip(icZipcode);
        patientBean.setIcPhone(icPhone);

        HealthRecord healthRecord = new HealthRecord();
        if ("".equals(height)) height = "0";
        if ("".equals(weight)) weight = "0";
        healthRecord.setHeight(Double.parseDouble(height));
        healthRecord.setWeight(Double.parseDouble(weight));
        healthRecord.setPatientID(patientMID);
        if (smoker == null) smoker = "off";
        healthRecord.setSmoker(smoker.compareTo("off"));

        prodDAO.getPatientDAO().editPatient(patientBean, -1);
        prodDAO.getHealthRecordsDAO().add(healthRecord);
        prodDAO.getAuthDAO().addUser(patientMID, Role.PATIENT, password);

%>
<div><%= StringEscapeUtils.escapeHtml("Your MID is " + patientMID) %></div>
<%
    }
    // show form
%>
<form method="post" action="/iTrust/preRegisterForm.jsp">
    First Name<br />
    <input type="text" id="p_fname" name="p_fname" style="width: 97%;"><br />
    Last Name<br />
    <input type="text" id="p_lname" name="p_lname" style="width: 97%;"><br />
    Email<br />
    <input type="text" id="p_email" name="p_email" style="width: 97%;"><br />
    Password<br />
    <input type="password" maxlength="20" id="p_password" name="p_password" style="width: 97%;"><br />
    Verify Password<br />
    <input type="password" maxlength="20" id="p_verify" name="p_verify" style="width: 97%;"><br />

    Address 1<br />
    <input type="text" id="p_address1" name="p_address1" style="width: 97%;"><br />
    Address 2<br />
    <input type="text" id="p_address2" name="p_address2" style="width: 97%;"><br />
    City<br />
    <input type="text" id="p_city" name="p_city" style="width: 97%;"><br />
    State<br />
    <input type="text" id="p_state" name="p_state" style="width: 97%;"><br />
    Zipcode<br />
    <input type="text" id="p_zipcode" name="p_zipcode" style="width: 97%;"><br />

    Phone<br />
    <input type="text" id="p_phone" name="p_phone" style="width: 97%;"><br />

    <h3>Insurance Provider</h3>
    Address 1<br />
    <input type="text" id="p_icAddress1" name="p_icAddress1" style="width: 97%;"><br />
    Address 2<br />
    <input type="text" id="p_icAddress2" name="p_icAddress2" style="width: 97%;"><br />
    City<br />
    <input type="text" id="p_icCity" name="p_icCity" style="width: 97%;"><br />
    State<br />
    <input type="text" id="p_icState" name="p_icState" style="width: 97%;"><br />
    Zipcode<br />
    <input type="text" id="p_icZipcode" name="p_icZipcode" style="width: 97%;"><br />

    Phone<br />
    <input type="text" id="p_icPhone" name="p_icPhone" style="width: 97%;"><br />

    Height<br />
    <input type="text" id="p_height" name="p_height" style="width: 97%;"><br />
    Weight<br />
    <input type="text" id="p_weight" name="p_weight" style="width: 97%;"><br />
    Smoker<br />
    <input type="checkbox" id="p_smoker" name="p_smoker"><br />


    <br />
    <input type="submit" value="Pre-Register">

    <br /><br />

</form>



<%@include file="/footer.jsp" %>