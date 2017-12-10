package edu.ncsu.csc.itrust.unit.bean;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.beans.PrescriptionStatisticsBean;
import org.easymock.classextension.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.PrescriptionStatisticsBean;
import edu.ncsu.csc.itrust.beans.loaders.PrescriptionStatisticsBeanLoader;

public class PrescriptionStatisticsBeanLoaderTest {

    private IMocksControl ctrl;
    private ResultSet rs;
    private PrescriptionStatisticsBeanLoader load;

    @Before
    /**
     * Sets up defaults
     */
    public void setUp() throws Exception {
        ctrl = createControl();
        rs = ctrl.createMock(ResultSet.class);
        load = new PrescriptionStatisticsBeanLoader();
    }

    @After
    /**
     * Tears down after execution
     */
    public void tearDown() throws Exception {
    }

    /**
     * testLoadList
     * @throws SQLException
     */
    @Test
    public void testLoadList() throws SQLException {
        List<PrescriptionStatisticsBean> l = load.loadList(rs);
        assertEquals(0, l.size());

    }

    /**
     * testLoadSingle
     */
    @Test
    public void testLoadSingle() {

        try {
            //this just sets the value for a method call (kinda hard coding I assume)
            expect(rs.getInt("ID")).andReturn(1).once();
            expect(rs.getString("ICDCode")).andReturn("250.10").once();
            expect(rs.getLong("mID")).andReturn(311L).once();
            ctrl.replay();

            PrescriptionStatisticsBean r = load.loadSingle(rs);
            assertEquals(1, r.getVisitID());
            assertEquals("250.10", r.getIcdCode());
            assertEquals(311, r.getPrescriptionID());
        } catch (SQLException e) {
            //TODO
        }
    }

}
