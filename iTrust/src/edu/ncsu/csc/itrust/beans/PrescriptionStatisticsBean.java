package edu.ncsu.csc.itrust.beans;

public class PrescriptionStatisticsBean {
	private long visitID = 0L;
	private String icdCode = "";
	private long prescriptionID = 0L;

	public PrescriptionStatisticsBean() {
	}

	public PrescriptionStatisticsBean(long visitID, String icdCode, long prescriptionID) {
		this.visitID = visitID;
		this.icdCode = icdCode;
		this.prescriptionID = prescriptionID;
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
