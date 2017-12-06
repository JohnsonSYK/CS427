package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

import edu.ncsu.csc.itrust.beans.FilterBean;

/**
 * A loader for OfficeVisitBeans.
 *
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency.
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class FilterLoader implements BeanLoader<FilterBean> {
    public List<FilterBean> loadList(ResultSet rs) throws SQLException {
        List<FilterBean> list = new ArrayList<FilterBean>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }

    public FilterBean loadSingle(ResultSet rs) throws SQLException {
        FilterBean f = new FilterBean();
        f.setMid(rs.getLong("mid"));
        f.setSender(rs.getString("sender"));
        f.setSubject(rs.getString("subject"));
        f.setSubstring_pos(rs.getString("sub_pos"));
        f.setSubstring_neg(rs.getString("sub_neg"));
        if (rs.getDate("date_left") != null){
            f.setDate_left(new Date(rs.getDate("date_left").getTime()));
        }
        if (rs.getDate("date_right") != null){
            f.setDate_right(new Date(rs.getDate("date_right").getTime()));
        }
        return f;
    }

    public PreparedStatement loadParameters(PreparedStatement ps, FilterBean p) throws SQLException {
        throw new IllegalStateException("unimplemented!");
    }
}
