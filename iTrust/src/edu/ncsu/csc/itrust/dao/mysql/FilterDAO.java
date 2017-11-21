package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.FilterBean;
import edu.ncsu.csc.itrust.beans.loaders.FilterLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for doing tasks related to office visits. Use this for linking diagnoses to office visits, and similar
 * tasks.
 *
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 *
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 *
 *
 *
 */
public class FilterDAO {
    private DAOFactory factory;
    private FilterLoader filterLoader = new FilterLoader();

    /**
     * The typical constructor.
     * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
     */
    public FilterDAO(DAOFactory factory) {
        this.factory = factory;
    }

    /**
     * Adds an visit and return its ID
     *
     * @param f The FilterBean to be added.
     * @return A long indicating the unique ID for the office visit.
     * @throws DBException
     */
    public long add(FilterBean f) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("INSERT INTO filters (mid, sender, subject, sub_pos, sub_neg, date_left, date_right) VALUES (?,?,?,?,?,?,?)");
            setValues1(ps, f);
            ps.executeUpdate();
            ps.close();
            return DBUtil.getLastInsert(conn);
        } catch (SQLException e) {

            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }

    private void setValues1(PreparedStatement ps, FilterBean f) throws SQLException {
        ps.setLong(1, f.getMid());
        ps.setString(2, f.getSender());
        ps.setString(3, f.getSubject());
        ps.setString(4, f.getSubstring_pos());
        ps.setString(5, f.getSubstring_neg());
        if (f.getDate_left() != null){
            ps.setDate(6, new java.sql.Date(f.getDate_left().getTime()));
        }
        else{
            ps.setNull(6, Types.DATE);
        }
        if (f.getDate_right() != null){
            ps.setDate(7, new java.sql.Date(f.getDate_right().getTime()));
        }
        else{
            ps.setNull(7, Types.DATE);
        }
    }

    /**
     * Updates the information in a particular office visit.
     *
     * @param f The Filter bean representing the changes.
     * @throws DBException
     */
    public void update(FilterBean f) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("UPDATE filters SET sender=?, subject=?, sub_pos=?, "
                    + "sub_neg=?, date_left=?, date_right=? WHERE mid=?");
            setValues2(ps, f);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {

            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }

    private void setValues2(PreparedStatement ps, FilterBean f) throws SQLException {
        ps.setLong(7, f.getMid());
        ps.setString(1, f.getSender());
        ps.setString(2, f.getSubject());
        ps.setString(3, f.getSubstring_pos());
        ps.setString(4, f.getSubstring_neg());
        if (f.getDate_left() != null){
            ps.setDate(5, new java.sql.Date(f.getDate_left().getTime()));
        }
        else{
            ps.setNull(5, Types.DATE);
        }
        if (f.getDate_right() != null){
            ps.setDate(6, new java.sql.Date(f.getDate_right().getTime()));
        }
        else{
            ps.setNull(6, Types.DATE);
        }
    }

    /**
     * Returns a particular office visit given an ID
     *
     * @param mid The unique ID logged in user.
     * @return An OfficeVisitBean with the specifics for that office visit.
     * @throws DBException
     */
    public FilterBean getFilter(long mid) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("Select * From filters Where mid = ?");
            ps.setLong(1, mid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                FilterBean result = filterLoader.loadSingle(rs);
                rs.close();
                ps.close();
                return result;
            }
            else{
                rs.close();
                ps.close();
                return null;
            }
        } catch (SQLException e) {

            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }
}
