<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.EditMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="javafx.util.Pair" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View cause-of-death trends report";
%>

<%@include file="/header.jsp" %>


<form action="viewCauseDeath.jsp" method="post">
    <input type="hidden" name="confirmAction" value="true"/>
    <input type="number" name="startingYear" min="1900" max = "2020" />
    <input type="number" name="endingYear" min="1900" max = "2020"/>
    <select name="gender">
        <option value="male">male</option>
        <option value="female">female</option>
        <option value="all">all</option>
    </select>
    <input type="submit" name="fSubmit" value="View Data">
</form>

<%
    if (request.getParameter("fSubmit") != null) {
        String param = request.getParameter("startingYear");
        String param2 = request.getParameter("endingYear");
        String gender = request.getParameter("gender");
        int startingYear = Integer.parseInt(param);
        int endingYear = Integer.parseInt(param2);

        OfficeVisitDAO ovdao = new OfficeVisitDAO(prodDAO);
        List<OfficeVisitBean> officeVisits = ovdao.getAllOfficeVisitsForLHCP(loggedInMID);
        List<Long> patients = new ArrayList<>();
        for (OfficeVisitBean visit : officeVisits) {
            patients.add(visit.getPatientID());
        }
        Set<Long> unique_patients = new HashSet<Long>(patients);

        PatientDAO pdao = new PatientDAO(prodDAO);
        List<String> causeDeathList = new ArrayList<>();
        for (Long patient : unique_patients) {
            PatientBean currentPatient = pdao.getPatient(patient);
            Gender currentGender = currentPatient.getGender();
            String causeDeath = currentPatient.getCauseOfDeath();
            if (causeDeath.equals("")) {
            	continue;
            }
            String dateDeath = currentPatient.getDateOfDeathStr();
            int yearDeath = Integer.parseInt(dateDeath.split("/")[2]);
            System.out.println(yearDeath);
            if (yearDeath >= startingYear && yearDeath <= endingYear) {
                if (gender.equals("all")) {
                    causeDeathList.add(causeDeath);
                } else if (gender.equals("male") && currentGender.getName().equals("Male")) {
                    causeDeathList.add(causeDeath);
                } else if (gender.equals("female") && currentGender.getName().equals("Female")) {
                    causeDeathList.add(causeDeath);
                }
            }
        }
        //Map<String, Long> counts =
        //        causeDeathList.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        Map<String, Long> counts = new HashMap<>();
        for (String causeDeath : causeDeathList) {
            Long count = counts.get(causeDeath);
            counts.put(causeDeath, ((count == null) ? 1 : count + 1));
        }
        List<Pair<Long, String>> freqList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            freqList.add(new Pair<Long, String>(entry.getValue(), entry.getKey()));
        }
        Collections.sort(freqList, new Comparator<Pair<Long, String>>() {
            @Override
            public int compare(final Pair<Long, String> p1, final Pair<Long, String> p2) {
                Long freq1 = p1.getKey();
                Long freq2 = p2.getKey();
                return freq2.compareTo(freq1);
            }
        });
        System.out.println(freqList.size());
        ICDCodesDAO codeBase = new ICDCodesDAO(prodDAO);
        for (int i = 0; i < 2 && i < freqList.size(); i++) {
            Pair<Long, String> freqCause = freqList.get(i);
            System.out.println("ICD-9CM: "+(String) freqCause.getValue()
                    + "; Cause of Death " + (codeBase.getICDCode((String) freqCause.getValue())).getDescription()
                    + "; Frequency :" + String.valueOf((Long) freqCause.getKey())
                    );
        }
        List<PatientBean> all_patients = pdao.getAllPatients();
        /*List<String>*/ causeDeathList = new ArrayList<>();
        for (PatientBean currentPatient : all_patients) {
            Gender currentGender = currentPatient.getGender();
            String causeDeath = currentPatient.getCauseOfDeath();
            if (causeDeath.equals("")) {
                continue;
            }
            String dateDeath = currentPatient.getDateOfDeathStr();
            int yearDeath = Integer.parseInt(dateDeath.split("/")[2]);
            System.out.println(yearDeath);
            if (yearDeath >= startingYear && yearDeath <= endingYear) {
                if (gender.equals("all")) {
                    causeDeathList.add(causeDeath);
                } else if (gender.equals("male") && currentGender.getName().equals("Male")) {
                    causeDeathList.add(causeDeath);
                } else if (gender.equals("female") && currentGender.getName().equals("Female")) {
                    causeDeathList.add(causeDeath);
                }
            }
        }
        /*Map<String, Long>*/ counts = new HashMap<>();
        for (String causeDeath : causeDeathList) {
            Long count = counts.get(causeDeath);
            counts.put(causeDeath, ((count == null) ? 1 : count + 1));
        }
        /*List<Pair<Long, String>>*/ freqList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            freqList.add(new Pair<Long, String>(entry.getValue(), entry.getKey()));
        }
        Collections.sort(freqList, new Comparator<Pair<Long, String>>() {
            @Override
            public int compare(final Pair<Long, String> p1, final Pair<Long, String> p2) {
                Long freq1 = p1.getKey();
                Long freq2 = p2.getKey();
                return freq2.compareTo(freq1);
            }
        });
        System.out.println(freqList.size());
        /*ICDCodesDAO*/ codeBase = new ICDCodesDAO(prodDAO);
        for (int i = 0; i < 2 && i < freqList.size(); i++) {
            Pair<Long, String> freqCause = freqList.get(i);
            System.out.println("ICD-9CM: "+(String) freqCause.getValue()
                    + "; Cause of Death " + (codeBase.getICDCode((String) freqCause.getValue())).getDescription()
                    + "; Frequency :" + String.valueOf((Long) freqCause.getKey())
            );
        }
    }


%>
<%@include file="/footer.jsp" %>