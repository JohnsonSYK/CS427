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

	public PrescriptionStatisticsBean() {
	}


	public long getVisitID() {
		return visitID;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public long getPrescriptionID() {
		return prescriptionID;
	}

	public void setVisitID(long visitID) {
		this.visitID = visitID;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public void setPrescriptionID(long prescriptionID) {
		this.prescriptionID = prescriptionID;
	}
}
