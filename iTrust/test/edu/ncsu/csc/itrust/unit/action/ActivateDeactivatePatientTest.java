package edu.ncsu.csc.itrust.unit.action;

import edu.ncsu.csc.itrust.action.EditPatientAction;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ActivateDeactivatePatientTest extends TestCase{
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private TestDataGenerator gen = new TestDataGenerator();
    private EditPatientAction action;
    private long loggedInMID = 9000000000L;

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
        gen.patient2();
    }

    public void testActivateRegularPatient() throws Exception {
        PatientBean p = factory.getPatientDAO().getPatient(2);

        action = new EditPatientAction(factory, loggedInMID, String.valueOf(p.getMID()));
        action.activate();

        assertActivated(p.getMID());
    }

    public void testActivatePreRegisteredPatient() throws Exception {
        long pid = addPreRegisteredPatient("Test", "Test", "nobody@gmail.com");

        action = new EditPatientAction(factory, loggedInMID, String.valueOf(pid));
        action.activate();

        assertActivated(pid);
    }

    public void testDeactivateRegularPatient() throws Exception {
        PatientBean p = factory.getPatientDAO().getPatient(2);

        action = new EditPatientAction(factory, loggedInMID, String.valueOf(p.getMID()));
        action.deactivate();

        assertDeactivated(p.getMID());
    }

    public void testDeactivatePreRegisteredPatient() throws Exception {
        long pid = addPreRegisteredPatient("Test", "Test", "nobody2@gmail.com");

        action = new EditPatientAction(factory, loggedInMID, String.valueOf(pid));
        action.deactivate();

        assertDeactivated(pid);
    }

    public void testActivateDeactivateChangesGetPreRegistered() throws Exception {
        long p1 = addPreRegisteredPatient("Test", "Test", "test1@gmail.com");
        long p2 = addPreRegisteredPatient("Test", "Test", "test2@gmail.com");
        long p3 = addPreRegisteredPatient("Test", "Test", "test3@gmail.com");

        assertEquals(factory.getPatientDAO().getPreRegisteredPatients().size(), 3);

        (new EditPatientAction(factory, loggedInMID, String.valueOf(p1))).activate();
        assertActivated(p1);
        assertEquals(factory.getPatientDAO().getPreRegisteredPatients().size(), 2);

        (new EditPatientAction(factory, loggedInMID, String.valueOf(p2))).deactivate();
        assertDeactivated(p2);
        assertEquals(factory.getPatientDAO().getPreRegisteredPatients().size(), 1);

        (new EditPatientAction(factory, loggedInMID, String.valueOf(p3))).activate();
        assertActivated(p3);
        assertEquals(factory.getPatientDAO().getPreRegisteredPatients().size(), 0);
    }

    private long addPreRegisteredPatient(String first, String last, String email) throws Exception {
        PatientDAO patientDAO = factory.getPatientDAO();
        long pid = patientDAO.addEmptyPatient();
        PatientBean p = patientDAO.getPatient(pid);
        p.setFirstName(first);
        p.setLastName(last);
        p.setEmail(email);
        p.setPreRegister(true);
        patientDAO.editPatient(p, pid);

        return pid;
    }

    private void assertActivated(long pid) throws Exception {
        PatientBean p = factory.getPatientDAO().getPatient(pid);
        assertFalse(p.getPreRegister());
        assertTrue(p.getDateOfDeactivationStr().compareTo("") == 0);
    }

    private void assertDeactivated(long pid) throws Exception {
        PatientBean p = factory.getPatientDAO().getPatient(pid);
        assertTrue(p.getDateOfDeactivationStr().compareTo("") != 0);
    }
}
