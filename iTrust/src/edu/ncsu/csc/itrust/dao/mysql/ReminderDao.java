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

public class ReminderDao {

    private transient final DAOFactory factory;
    private transient final ReminderBeanLoader rbl;

    public ReminderDao (DAOFactory factory) {
        this.factory = factory;
        this.rbl = new ReminderBeanLoader();
    }

    public void logReminder(final ReminderBean bean) throws SQLException, DBException {
        Connection conn=null;
        PreparedStatement pstring= null;

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

    public List<ReminderBean> getReminders(long mid) throws SQLException, DBException{
        Connection conn=null;
        PreparedStatement pstring= null;

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
}
