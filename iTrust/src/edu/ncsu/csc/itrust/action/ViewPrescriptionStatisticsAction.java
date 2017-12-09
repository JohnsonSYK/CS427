package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.beans.PrescriptionStatisticsBean;
import edu.ncsu.csc.itrust.beans.loaders.PrescriptionStatisticsBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.*;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import javafx.util.Pair;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ViewPrescriptionStatisticsAction {
	private static final String all = "All";

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

	/**
	 * Filter Office visits based on user input
	 * @param icd_code user selected
	 * @param target_gender user selected
	 * @param start_date user inputted
	 * @param end_date user inputted
	 * @return A list of non-duplicated OfficeVisitBean information to be processed
	 * @throws DBException
	 */
	public List<OfficeVisitBean> getFilteredOfficeVisits(String icd_code, String target_gender, Date start_date, Date end_date) throws DBException{
		List<OfficeVisitBean> officeVisits = ovDAO.getAllOfficeVisitsForDiagnosis(icd_code);
		// Get distinct office visits based on visitID
		Set<OfficeVisitBean> distinctOV = new HashSet<OfficeVisitBean>(officeVisits);

		List <OfficeVisitBean> result = new ArrayList();
		for (OfficeVisitBean officeVisit : distinctOV) {
			Date date = officeVisit.getVisitDate();

			//Filter out based on date
			if (date.before(start_date) || end_date.before(date)) {
				continue;
			}

			long p_id = officeVisit.getPatientID();
			//Filter out based on gender
			String gender = patientsDAO.getPatient(p_id).getGender().getName();
			if (!target_gender.equals(all) && !(target_gender.equals(gender))){
				continue;
			}
			result.add(officeVisit);

		}
		return result;
	}

	/**
	 * Get Prescription information based on filtered office visits
	 * @param officeVisits office visits filtered (most likely using getFilteredOfficeVisits)
	 * @return Hashmap of (ndcode:count occurrence) pair in the filtered office visits
	 * @throws DBException
	 */
	public HashMap<String, Integer> getPrescriptionStatistics(List<OfficeVisitBean> officeVisits) throws DBException{
		HashMap<String, Integer> result = new HashMap<>();
		for (OfficeVisitBean officeVisit : officeVisits){
			//get prescription information of current office visit
			List<PrescriptionBean> curr_prescriptions = pDAO.getList(officeVisit.getVisitID());
			List<String> ndcodes = new ArrayList<>();
			for (PrescriptionBean prescription : curr_prescriptions) {
				// Filter out duplicate medication in the same office visit
				String ndcode = prescription.getMedication().getNDCode();
				if (!ndcodes.contains(ndcode)) {
					result.put(ndcode, (result.containsKey(ndcode) ? result.get(ndcode) + 1 : 1));
					ndcodes.add(ndcode);
				}
			}
		}
		return result;
	}

	/**
	 * Sets the blanks in the given prepared statements with user-provided data
	 * @param ps blank prepared statement
	 * @param icd_code user selected
	 * @param start_date user inputted
	 * @param end_date user inputted
	 * @return Filled-out prepared statement
	 * @throws SQLException
	 */
	private void setValues(PreparedStatement ps, String icd_code, Date start_date, Date end_date) throws SQLException{
		ps.setString(1, icd_code);
		ps.setDate(2, new java.sql.Date(start_date.getTime()));
		ps.setDate(3, new java.sql.Date(end_date.getTime()));
	}

	/**
	 * Get the information from user inputs to be processed and stored in table format
	 * @param icd_code user selected
	 * @param gender user selected
	 * @param start_date user inputted
	 * @param end_date user inputted
	 * @return List of PrescriptionStatisticsBeans that stores necessary information to recover the table
	 * @throws DBException
	 */
	public List<PrescriptionStatisticsBean> getTable(String icd_code, String gender, Date start_date, Date end_date) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		PrescriptionStatisticsBeanLoader ploader = new PrescriptionStatisticsBeanLoader();
		try {
			conn = factory.getConnection();
			// Execute a SQL statement based on all information given
			if (gender.equals(all)) {
				ps = conn.prepareStatement("SELECT officevisits.ID, ovdiagnosis.ICDCode, ovmedication.ID AS mID " +
					"FROM officevisits, ovdiagnosis, ovmedication, patients WHERE officevisits.ID=ovdiagnosis.VisitID " +
					"AND officevisits.ID=ovmedication.VisitID AND patients.MID=officevisits.patientID AND ovdiagnosis.ICDCode=? " +
					"AND officevisits.visitDate >= ? AND officevisits.VisitDate <= ?");

			} else {
				ps = conn.prepareStatement("SELECT officevisits.ID, ovdiagnosis.ICDCode, ovmedication.ID AS mID " +
					"FROM officevisits, ovdiagnosis, ovmedication, patients WHERE officevisits.ID=ovdiagnosis.VisitID " +
					"AND officevisits.ID=ovmedication.VisitID AND patients.MID=officevisits.patientID AND ovdiagnosis.ICDCode=? " +
					"AND officevisits.visitDate >= ? AND officevisits.VisitDate <= ? AND patients.Gender = ?");
				ps.setString(4, gender);
			}
			setValues(ps, icd_code, start_date, end_date);
			ResultSet rs = ps.executeQuery();
			List<PrescriptionStatisticsBean> loadlist = ploader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
