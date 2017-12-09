package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about prescription statistics.
 *
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean
 * (with the exception of minor formatting such as concatenating phone numbers together).
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters.
 * to create these easily)
 */
public class PrescriptionStatisticsBean {
	private long visitID = 0L;
	private String icdCode = "";
	private long prescriptionID = 0L;

	/**
	 * PrescriptionStatisticsBean default constructor
	 * @return PrescriptoinStatisticsBean
	 */
	public PrescriptionStatisticsBean() {
	}

	/**
	 * getVisitID
	 * @return VisitID
	 */
	public long getVisitID() {
		return visitID;
	}

	/**
	 * getIcdCode
	 * @return IcdCode
	 */
	public String getIcdCode() {
		return icdCode;
	}

	/**
	 * getPrescriptionID
	 * @return PrescriptionID
	 */
	public long getPrescriptionID() {
		return prescriptionID;
	}

	/**
	 * setVisitID
	 * @param visitID visit ID
	 */
	public void setVisitID(long visitID) {
		this.visitID = visitID;
	}

	/**
	 * setIcdCode
	 * @param icdCode ICD Code
	 */
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	/**
	 * setPrescriptionID
	 * @param prescriptionID prescription ID
	 */
	public void setPrescriptionID(long prescriptionID) {
		this.prescriptionID = prescriptionID;
	}
}
