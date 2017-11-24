package edu.ncsu.csc.itrust.beans.loaders;

import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.beans.ReminderBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads parameters into a prepared statement and unloads them from
 * a results set
 */
public class ReminderBeanLoader implements BeanLoader<ReminderBean> {

    /**
     * Loads each bean from a result set
     * @param rs The java.sql.ResultSet we are extracting.
     * @return ReminderBeanList
     * @throws SQLException if a bad sql statement
     */
    public List<ReminderBean> loadList(ResultSet rs) throws SQLException {
        List<ReminderBean> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }


    /**
     * Loads a param where each of the `?` params are
     * 1. mid
     * 2. subject
     * 3. sender name
     * 4. get context
     * @param ps The prepared statement to be loaded.
     * @param bean The bean containing the data to be placed.
     * @return Prepared PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement loadParameters(PreparedStatement ps, ReminderBean bean) throws SQLException {
        ps.setLong(1, bean.getMid());
        ps.setString(2, bean.getSubject());
        ps.setString(3, bean.getSenderName());
        ps.setString(4, bean.getContent());
        return ps;
    }


    /**
     * Loads a bean with a row from the result set
     * Does _not_ advance the result set
     * @param rs The java.sql.ResultSet to be loaded.
     * @return Bean from row
     * @throws SQLException if malformed sql
     */
    public ReminderBean loadSingle(ResultSet rs) throws SQLException {
        ReminderBean bean = new ReminderBean();
        bean.setReminderId(rs.getInt("id"));
        bean.setMid(rs.getInt("mid"));
        bean.setSubject(rs.getString("subject"));
        bean.setSenderName(rs.getString("sender_name"));
        bean.setSentTime(rs.getTimestamp("sent_time"));
        bean.setContent(rs.getString("content"));
        return bean;
    }
}
