package edu.ncsu.csc.itrust.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.ncsu.csc.itrust.beans.FilterBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.FilterDAO;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import javafx.util.Pair;

/**
 * Used for the Edit Filter page. Can A user (an LHCP or patient/representative) can modify
 * his/her message displaying filter by modifying the following filtering criteria: (1) the
 * sender (i.e., the sender's name is exactly the same as the specified string), (2) the subject
 * (i.e., the subject is exactly the same as the specified string), (3) has the words (i.e.,
 * the subject or the message body has the specified substring), (4) doesn't have (i.e.,
 * neither the subject nor the message body has the specified substring), (5) time stamp
 * falling into the period defined by the starting date and ending date (inclusive)
 */
public class EditFilterAction {
    /** Database access methods for Filters */
    private FilterDAO filterDAO;
    private Long Mid;

    /**
     * Constructor for the action. Initializes DAO fields
     * @param factory The session's factory for DAOs
     */
    public EditFilterAction(DAOFactory factory, Long M) {
        this.filterDAO = factory.getFilterDAO();
        this.Mid = M;
    }

    /**
     * Get the current filter values.
     * @return An object containing the current filter values.
     * @throws DBException
     */
    public FilterBean pullCurrent() throws DBException{
        return filterDAO.getFilter(Mid);
    }

    /**
     * Call database access object class to add the filter values into
     * the database.
     * @param sender user inputted
     * @param subject user inputted
     * @param sub_pos user inputted
     * @param sub_neg user inputted
     * @param d_left user inputted
     * @param d_right user inputted
     * @return
     */
    public Boolean addFilter(String sender, String subject, String sub_pos, String sub_neg, Date d_left, Date d_right){
        FilterBean f = new FilterBean();
        f.setMid(Mid);
        f.setSender(sender);
        f.setSubject(subject);
        f.setSubstring_pos(sub_pos);
        f.setSubstring_neg(sub_neg);
        f.setDate_left(d_left);
        f.setDate_right(d_right);
        try {
            filterDAO.add(f);
            return true;
        }
        catch (DBException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update the existing filter values stored in the database.
     * @param sender user inputted
     * @param subject user inputted
     * @param sub_pos user inputted
     * @param sub_neg user inputted
     * @param d_left user inputted
     * @param d_right user inputted
     * @return
     * @throws DBException
     */
    public Boolean updateFilter(String sender, String subject, String sub_pos, String sub_neg, Date d_left, Date d_right) throws DBException{
        FilterBean f = new FilterBean();
        f.setMid(Mid);
        f.setSender(sender);
        f.setSubject(subject);
        f.setSubstring_pos(sub_pos);
        f.setSubstring_neg(sub_neg);
        f.setDate_left(d_left);
        f.setDate_right(d_right);
        try {
            filterDAO.update(f);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }



}
