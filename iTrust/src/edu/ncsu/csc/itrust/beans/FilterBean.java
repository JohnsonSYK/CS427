package edu.ncsu.csc.itrust.beans;
import java.util.*;

/**
 * A bean for storing data about filter values.
 *
 */
public class FilterBean {
    /** The mid of a user */
    private Long mid;
    /** The sender filter value inputted by a user.*/
    private String Sender;
    /** The subject filter value inputted by a user.*/
    private String Subject;
    /** The "contains" filter value inputted by a user.*/
    private String Substring_pos;
    /** The "excludes" filter value inputted by a user.*/
    private String Substring_neg;
    /** The starting date filter value inputted by a user.*/
    private Date Date_left;
    /** The ending date filter value inputted by a user.*/
    private Date Date_right;

    /**
     * Constructor for a filter bean. Initialize a default filter bean.
     */
    public FilterBean() {
        mid = null;
        Sender = null;
        Subject = null;
        Substring_pos = null;
        Substring_neg = null;
        Date_left = null;
        Date_right = null;
    }

    /**
     * Sets the mid
     * @param m
     */
    public void setMid(Long m) {
        mid = m;
    }

    /**
     * Returns the mid
     * @return
     */
    public Long getMid() {
        return mid;
    }
    /**
     * Sets the sender
     * @param s
     */
    public void setSender(String s) {
        Sender = s;
    }

    /**
     * Returns the sender
     * @return
     */
    public String getSender() {return Sender;}

    /**
     * Sets the subject
     * @param s
     */
    public void setSubject(String s) {
        Subject = s;
    }

    /**
     * Returns the subject
     * @return
     */
    public String getSubject() {return Subject;}

    /**
     * Sets the contained substring
     * @param s
     */
    public void setSubstring_pos(String s) {
        Substring_pos = s;
    }

    /**
     * Returns the contained substring
     * @return
     */
    public String getSubstring_pos() {return Substring_pos;}

    /**
     * Sets the excluded substring
     * @param s
     */
    public void setSubstring_neg(String s) {
        Substring_neg = s;
    }

    /**
     * Returns the excluded substring
     * @return
     */
    public String getSubstring_neg() {return Substring_neg;}

    /**
     * Sets the starting date
     * @param d
     */
    public void setDate_left(Date d) {Date_left = d;}

    /**
     * Returns the starting date
     * @return
     */
    public Date getDate_left() {return Date_left;}

    /**
     * Sets the ending date
     * @param d
     */
    public void setDate_right(Date d) {Date_right = d;}

    /**
     * Returns the ending_date
     * @return
     */
    public Date getDate_right() {return Date_right;}
}
