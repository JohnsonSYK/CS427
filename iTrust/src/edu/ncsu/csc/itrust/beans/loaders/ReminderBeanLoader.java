package edu.ncsu.csc.itrust.beans.loaders;

import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.beans.ReminderBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReminderBeanLoader implements BeanLoader<ReminderBean> {
    public List<ReminderBean> loadList(ResultSet rs) throws SQLException {
        List<ReminderBean> list = new ArrayList<>();
        while (rs.next())
            list.add(loadSingle(rs));
        return list;
    }


    public PreparedStatement loadParameters(PreparedStatement ps, ReminderBean bean) throws SQLException {
        ps.setLong(1, bean.getMid());
        ps.setString(2, bean.getSubject());
        ps.setString(3, bean.getSenderName());
        ps.setString(4, bean.getContent());
        return ps;
    }


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
