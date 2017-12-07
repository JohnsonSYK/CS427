package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.*;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import javafx.util.Pair;

import java.util.*;

public class ViewPrescriptionStatisticsAction {
	private int CAUSE_OF_DEATH_TARGET = 0;
	/** Database access methods for ICD codes (diagnoses) */
	private ICDCodesDAO icdDAO;
	/** Database access methods for patients information */
	private PatientDAO patientsDAO;
	/** Database access methods for office visits information */
	private OfficeVisitDAO ovDAO;
	/** Database access methods for ND codes (medication) */
	private NDCodesDAO ndDAO;
	/** Database access methods for prescription information */
	private PrescriptionsDAO pDAO;


	/**
	 * Database factory
	 */
	private DAOFactory factory;

	/**
	 * Constructor for the action. Initializes DAO fields
	 * @param factory The session's factory for DAOs
	 */
	public ViewPrescriptionStatisticsAction(DAOFactory factory) {
		this.factory = factory;
		this.icdDAO = factory.getICDCodesDAO();
		this.patientsDAO = factory.getPatientDAO();
		this.ovDAO = factory.getOfficeVisitDAO();
		this.ndDAO = factory.getNDCodesDAO();
		this.pDAO = factory.getPrescriptionsDAO();
	}

	public List<OfficeVisitBean> getFilteredOfficeVisits(String icd_code, String target_gender, Date start_date, Date end_date) throws DBException{
		List<OfficeVisitBean> officeVisits = ovDAO.getAllOfficeVisitsForDiagnosis(icd_code);
		Set<OfficeVisitBean> distinctOV = new HashSet<OfficeVisitBean>(officeVisits);

		Integer counter = 0;
		List <OfficeVisitBean> result = new ArrayList();
		for (OfficeVisitBean officeVisit : distinctOV) {
			Date date = officeVisit.getVisitDate();
			if (!(date.after(start_date) && end_date.after(date))) {
				continue;
			}

			long p_id = officeVisit.getPatientID();
			String gender = patientsDAO.getPatient(p_id).getGender().getName();
			if (!target_gender.equals("all") && !(target_gender.equals(gender))){
				continue;
			}
			result.add(officeVisit);

		}
		return result;
	}

	public HashMap<String, Integer> getPrescriptionStatistics(List<OfficeVisitBean> officeVisits) throws DBException{

		HashMap<String, Integer> result = new HashMap<>();
		for (OfficeVisitBean officeVisit : officeVisits){
			List<PrescriptionBean> curr_prescriptions = pDAO.getList(officeVisit.getVisitID());
			List<String> ndcodes = new ArrayList<>();
			for (PrescriptionBean prescription : curr_prescriptions) {
				if (!ndcodes.contains(prescription.getMedication().getNDCode())) {
					String ndcode = prescription.getMedication().getNDCode();
					result.put(ndcode, (result.containsKey(ndcode) ? result.get(ndcode) + 1 : 1));
					ndcodes.add(ndcode);
				}
			}
		}
		return result;
	}

	public HashMap<String, List <Long> > getOfficeVisitsForPrescription(List<OfficeVisitBean> officeVisits) throws DBException {
		HashMap<String, List <Long>> result = new HashMap<>();
		for (OfficeVisitBean officeVisit : officeVisits){
			List<PrescriptionBean> curr_prescriptions = pDAO.getList(officeVisit.getVisitID());
			for (PrescriptionBean prescription : curr_prescriptions) {
				String ndcode = prescription.getMedication().getNDCode();
				List<Long> currOVs = (result.containsKey(ndcode) ? result.get(ndcode): new ArrayList<>());
				if (!currOVs.contains(officeVisit.getVisitID()))
					currOVs.add(officeVisit.getVisitID());
				result.put(ndcode, currOVs);
			}
		}
		return result;
	}
}
