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
	public List<PrescriptionStatisticsBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<PrescriptionStatisticsBean> list = new ArrayList<>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PrescriptionStatisticsBean loadSingle(ResultSet rs) throws SQLException {
		PrescriptionStatisticsBean pres = new PrescriptionStatisticsBean();
		pres.setVisitID(rs.getInt("ID"));
		pres.setIcdCode(rs.getString("ICDCode"));
		pres.setPrescriptionID(rs.getLong("mID"));
		return pres;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, PrescriptionStatisticsBean pres) throws SQLException {
		ps.setLong(1, pres.getVisitID());
		ps.setString(2, pres.getIcdCode());
		ps.setLong(3, pres.getPrescriptionID());
		return ps;
	}
}
