package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.beans.ReminderBean;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.enums.TransactionType;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class TransactionLogBeanTest {

    @Test
    public void testBean() {

        long transactionID = 1;
        long loggedInMID = 2;
        long secondaryMID = 3;
        TransactionType transactionType = TransactionType.OFFICE_VISIT_CREATE;
        Timestamp timeLogged = Timestamp.from(Instant.now());
        String addedInfo = "blah";
        String role = "hcp";

        TransactionBean bean = new TransactionBean();
        bean.setTransactionID(transactionID);
        bean.setLoggedInMID(loggedInMID);
        bean.setSecondaryMID(secondaryMID);
        bean.setTransactionType(transactionType);
        bean.setTimeLogged(timeLogged);
        bean.setAddedInfo(addedInfo);
        bean.setRole(role);

        assertEquals(transactionID , bean.getTransactionID());
        assertEquals(loggedInMID , bean.getLoggedInMID());
        assertEquals(secondaryMID, bean.getSecondaryMID());
        assertEquals(transactionType, bean.getTransactionType());
        assertEquals(timeLogged, bean.getTimeLogged());
        assertEquals(addedInfo, bean.getAddedInfo());
        assertEquals(role, bean.getRole());
    }

    @Test
    public void testEquality() {

        long transactionID = 1;
        long loggedInMID = 2;
        long secondaryMID = 3;
        TransactionType transactionType = TransactionType.OFFICE_VISIT_CREATE;
        Timestamp timeLogged = Timestamp.from(Instant.now());
        String addedInfo = "blah";
        String role = "hcp";

        TransactionBean bean = new TransactionBean();
        bean.setTransactionID(transactionID);
        bean.setLoggedInMID(loggedInMID);
        bean.setSecondaryMID(secondaryMID);
        bean.setTransactionType(transactionType);
        bean.setTimeLogged(timeLogged);
        bean.setAddedInfo(addedInfo);
        bean.setRole(role);

        Object notBean = new String();
        assertFalse(bean.equals(notBean));

    }
}
