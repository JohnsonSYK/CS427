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
 * Used for the View Cause of Death Statistics page. Can return top-2 causes-of-death
 * for a logged in HCP and all HCPs filtered by gender and year of death
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

    public FilterBean pullCurrent() throws DBException{
        return filterDAO.getFilter(Mid);
    }

    public Boolean addFilter(String sender, String subject, String sub_pos, String sub_neg, Date d_left, Date d_right) throws DBException{
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
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

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
