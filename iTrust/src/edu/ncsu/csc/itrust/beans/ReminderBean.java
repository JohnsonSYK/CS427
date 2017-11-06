package edu.ncsu.csc.itrust.beans;

import java.io.Serializable;
import java.sql.Timestamp;

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

    @Override
    public boolean equals(Object other){
        if(! (other instanceof ReminderBean)) {
            return false;
        }
        ReminderBean oth = (ReminderBean)other;
        return this.reminderId == oth.reminderId
                && this.content.equals(oth.content)
                && this.senderName.equals(oth.senderName)
                && this.sentTime == oth.sentTime
                && this.mid == oth.mid
                && this.subject.equals(oth.subject);
    }


}
