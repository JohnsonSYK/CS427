package edu.ncsu.csc.itrust.beans.loaders;

import edu.ncsu.csc.itrust.beans.PrescriptionStatisticsBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A loader for PrescriptionStatisticsBeans.
 *
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency.
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class PrescriptionStatisticsBeanLoader {
	/**
	 * Returns list of prescription statistics information based on executed query result
	 * @param rs The java.sql.ResultSet we are extracting.
	 * @return List of PrescriptionStatisticsBean that are extracted
	 * @throws SQLException
	 */

	public List<PrescriptionStatisticsBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<PrescriptionStatisticsBean> list = new ArrayList<>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * Return prescription information based on executed query result
	 * @param rs The java.sql.ResultSet to be loaded.
	 * @return A single PrescriptionStatisticsBean extracted
	 * @throws SQLException
	 */

	public PrescriptionStatisticsBean loadSingle(ResultSet rs) throws SQLException {
		PrescriptionStatisticsBean pres = new PrescriptionStatisticsBean();
		pres.setVisitID(rs.getInt("ID"));
		pres.setIcdCode(rs.getString("ICDCode"));
		pres.setPrescriptionID(rs.getLong("mID"));
		return pres;
	}
}
