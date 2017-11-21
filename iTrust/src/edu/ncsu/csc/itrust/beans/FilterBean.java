package edu.ncsu.csc.itrust.beans;
import java.util.*;

/**
 * A bean for storing data about a visit with an HCP.
 *
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean
 * (with the exception of minor formatting such as concatenating phone numbers together).
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters.
 * to create these easily)
 */
public class FilterBean {
    private Long mid;
    private String Sender;
    private String Subject;

    private String Substring_pos;
    private String Substring_neg;

    private Date Date_left;
    private Date Date_right;

    public FilterBean() {
        mid = -1L;
        Sender = null;
        Subject = null;
        Substring_pos = null;
        Substring_neg = null;
        Date_left = null;
        Date_right = null;
    }


    public void setMid(Long m) {
        mid = m;
    }
    public Long getMid() {
        return mid;
    }

    public void setSender(String s) {
        Sender = s;
    }
    public String getSender() {return Sender;}

    public void setSubject(String s) {
        Subject = s;
    }
    public String getSubject() {return Subject;}

    public void setSubstring_pos(String s) {
        Substring_pos = s;
    }
    public String getSubstring_pos() {return Substring_pos;}

    public void setSubstring_neg(String s) {
        Substring_neg = s;
    }
    public String getSubstring_neg() {return Substring_neg;}

    public void setDate_left(Date d) {Date_left = d;}
    public Date getDate_left() {return Date_left;}

    public void setDate_right(Date d) {Date_right = d;}
    public Date getDate_right() {return Date_right;}
}
