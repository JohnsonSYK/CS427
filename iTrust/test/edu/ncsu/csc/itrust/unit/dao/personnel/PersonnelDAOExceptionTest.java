package edu.ncsu.csc.itrust.unit.dao.personnel;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonnelDAOExceptionTest extends TestCase {
	private PersonnelDAO evilDAO = EvilDAOFactory.getEvilInstance().getPersonnelDAO();

	@Override
	protected void setUp() throws Exception {
	}

	public void testAddEmptyPersonnelException() throws Exception {
		try {
			evilDAO.addEmptyPersonnel(Role.HCP);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testCheckPersonnelExistsException() throws Exception {
		try {
			evilDAO.checkPersonnelExists(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testEditPersonnelException() throws Exception {
		try {
			evilDAO.editPersonnel(new PersonnelBean());
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testGetHospitalsException() throws Exception {
		try {
			evilDAO.getHospitals(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
	
	public void testGetUAPHospitalsException() throws Exception {
		try {
			evilDAO.getUAPHospitals(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
	
	public void testGetNameException() throws Exception {
		try {
			evilDAO.getName(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testPersonnelException() throws Exception {
		try {
			evilDAO.getPersonnel(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testGetAllUserRole() throws Exception {
		DAOFactory factory = mock(DAOFactory.class);
		Connection conn = mock(Connection.class);
		ResultSet rs = mock(ResultSet.class);
		when(factory.getConnection()).thenReturn(conn);
		PreparedStatement ps = mock(PreparedStatement.class);
		when(conn.prepareStatement(anyString())).thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(false);
		PersonnelDAO personnelDAO = new PersonnelDAO(factory);

		List<String> roles = personnelDAO.getAllUserRoles();

		assertEquals(0, roles.size());
	}

	public void testException() throws Exception {
		DAOFactory factory = mock(DAOFactory.class);
		when(factory.getConnection()).thenThrow(new SQLException());

		PersonnelDAO personnelDAO = new PersonnelDAO(factory);

		try {
			personnelDAO.getAllUserRoles();
			fail("Should throw exception");
		} catch (Exception e ) {}

	}
}
