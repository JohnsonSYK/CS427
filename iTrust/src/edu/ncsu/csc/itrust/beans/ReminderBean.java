package edu.ncsu.csc.itrust.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a reminder object
 */
public class ReminderBean implements Serializable {

    private static final long serialVersionUID = -1965874529780021183L;
    private int reminderId;
    private int mid;
    private String subject;
    private String senderName;
    private Timestamp sentTime;
    private String content;

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    @Override
    public int hashCode () { return reminderId; }

    /**
     * Returns true if both objects represent the exact same
     * entity in the database (including ids)
     * @param other, another object
     * @return true if equal false otherwise
     */
    public boolean equals(Object other){
        if(! (other instanceof ReminderBean)) {
            return false;
        }
        ReminderBean oth = (ReminderBean)other;
        return this.reminderId == oth.reminderId;
    }


}
