package edu.ncsu.csc.itrust.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import javafx.util.Pair;

/**
 * Used for the View Cause of Death Statistics page. Can return top-2 causes-of-death
 * for a logged in HCP and all HCPs filtered by gender and year of death
 */
public class ViewCauseDeathAction {
    /** Database access methods for ICD codes (diagnoses) */
    private ICDCodesDAO icdDAO;
    /** Database access methods for patients information */
    private PatientDAO patientsDAO;
    /** Database access methods for office visits information */
    private OfficeVisitDAO ovDAO;

    /**
     * Constructor for the action. Initializes DAO fields
     * @param factory The session's factory for DAOs
     */
    public ViewCauseDeathAction(DAOFactory factory) {
        this.icdDAO = factory.getICDCodesDAO();
        this.patientsDAO = factory.getPatientDAO();
        this.ovDAO = factory.getOfficeVisitDAO();
    }

    public String getCODStatisticsMID(Long MID, int startingYear, int endingYear, String gender) throws DBException{
        List<OfficeVisitBean> officeVisits = this.ovDAO.getAllOfficeVisitsForLHCP(MID);
        List<Long> patients = new ArrayList<>();
        for (OfficeVisitBean visit : officeVisits) {
            patients.add(visit.getPatientID());
        }
        Set<Long> unique_patients = new HashSet<Long>(patients);
        List<String> causeDeathList = new ArrayList<>();
        for (Long patient : unique_patients) {
            PatientBean currentPatient = patientsDAO.getPatient(patient);
            String causeDeath = currentPatient.getCauseOfDeath();
            if (causeDeath.equals("")) {
                continue;
            }

            filteredAdd(startingYear, endingYear, gender, causeDeathList, currentPatient);
        }
        return formatCauseOfDeaths(causeDeathList);
    }

    public String getCODStatisticsAll(int startingYear, int endingYear, String gender) throws DBException{
        List <PatientBean> all_patients = patientsDAO.getAllPatients();
        List<String> causeDeathList = new ArrayList<>();
        for (PatientBean currentPatient : all_patients) {
            String causeDeath = currentPatient.getCauseOfDeath();
            if (causeDeath.equals("")) {
                continue;
            }

            filteredAdd(startingYear, endingYear, gender, causeDeathList, currentPatient);
        }
        return formatCauseOfDeaths(causeDeathList);
    }

    private void filteredAdd(int startingYear, int endingYear, String gender, List<String> causeDeathList, PatientBean currentPatient) {
        String causeDeath = currentPatient.getCauseOfDeath();
        Gender currentGender = currentPatient.getGender();
        String dateDeath = currentPatient.getDateOfDeathStr();
        int yearDeath = Integer.parseInt(dateDeath.split("/")[2]);
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

    private String formatCauseOfDeaths(List<String> causeDeathList) throws DBException {
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
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 2 && i < freqList.size(); i++) {
            Pair<Long, String> freqCause = freqList.get(i);
            output.append("ICD-9CM: ");
            output.append(freqCause.getValue());
            output.append("; Cause of Death: ");
            output.append((icdDAO.getICDCode((String) freqCause.getValue())).getDescription());
            output.append("; Frequency:");
            output.append(String.valueOf((Long) freqCause.getKey()));
            output.append("\n");
        }
        return output.toString();
    }
}
