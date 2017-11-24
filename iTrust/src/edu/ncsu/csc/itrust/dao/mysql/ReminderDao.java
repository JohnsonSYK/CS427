package edu.ncsu.csc.itrust.dao.mysql;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ReminderBean;
import edu.ncsu.csc.itrust.beans.loaders.ReminderBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Used for managing all static information related to a reminder. For other information related to all aspects
 * of patient care, see the other DAOs.
 *
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 *
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 *
 */
public class ReminderDao {

    private transient final DAOFactory factory;
    private transient final ReminderBeanLoader rbl;

    /**
     * Typical Constructor
     * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
     */
    public ReminderDao (DAOFactory factory) {
        this.factory = factory;
        this.rbl = new ReminderBeanLoader();
    }

    /**
     * Persists a reminder bean in the database, does not update the bean's ReminderID
     * @param bean, reminder to store
     * @throws SQLException, when there is a SQL error in the query
     * @throws DBException, when the database cannot be contacted
     */
    public void logReminder(final ReminderBean bean) throws SQLException, DBException {
        Connection conn = null;
        PreparedStatement pstring = null;

        try{
            conn = factory.getConnection();

            pstring = conn.prepareStatement("INSERT INTO reminders (mid, subject, " +
                    "sender_name, content) VALUES (?, ?, ?, ?)");
            this.rbl.loadParameters(pstring, bean);
            pstring.executeUpdate();
            pstring.close();
        }catch (SQLException e){
            throw new DBException(e);
        }finally {
            DBUtil.closeConnection(conn,pstring);
        }

    }

    /**
     * Grabs all reminders given a MID
     * @param mid for which to grab all reminders
     * @return List of reminders
     * @throws SQLException, on malformed sql
     * @throws DBException, on no DB connection
     */
    public List<ReminderBean> getReminders(long mid) throws SQLException, DBException{
        Connection conn = null;
        PreparedStatement pstring = null;

        try{
            conn = factory.getConnection();

            pstring = conn.prepareStatement("SELECT * FROM reminders WHERE mid = ?");
            pstring.setLong(1, mid);
            final ResultSet results = pstring.executeQuery();
            final List<ReminderBean> rbList = this.rbl.loadList(results);
            results.close();
            pstring.close();
            return rbList;
        }catch (SQLException e){
            throw new DBException(e);
        }finally {
            DBUtil.closeConnection(conn,pstring);
        }
    }

    /**
     * Grabs a reminder from an AppointmentID
     * @param apptId, appointment to grab reminders
     * @return ReminderBean or null of the most recent reminder
     * @throws SQLException, on malformed sql syntax
     * @throws DBException, on no DB connection
     */
    public ReminderBean getReminderById(long apptId) throws SQLException, DBException {
        Connection conn = null;
        PreparedStatement pstring = null;

        try{
            conn = factory.getConnection();

            pstring = conn.prepareStatement("SELECT * FROM reminders WHERE id = ?");
            pstring.setLong(1, apptId);
            final ResultSet results = pstring.executeQuery();
            final List<ReminderBean> rbList = this.rbl.loadList(results);
            if(rbList.size() == 0) {
                return null;
            } else {
                return rbList.get(0);
            }
        }catch (SQLException e){
            throw new DBException(e);
        }finally {
            DBUtil.closeConnection(conn,pstring);
        }
    }

}
