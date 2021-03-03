/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.sessionbean;

import commoninfra.entity.AccessRightsEntity;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.RoleEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author FABIAN
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemAdminSessionBeanRemoteTest {

    SystemAdminSessionBeanRemote sas = lookupEventSession();

    public SystemAdminSessionBeanRemoteTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createTeam method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testCreateTeamStation() {
        System.out.println("createTeamStation");
        String department = "Station";
        Long result = sas.createTeam(department);
        assertNotNull(result);
    }

    @Test
    public void testCreateTeamDepot() {
        System.out.println("createTeamDepot");
        String department = "Depot";
        Long result = sas.createTeam(department);
        assertNotNull(result);
    }

    @Test
    public void testCreateTeamFail() {
        System.out.println("createTeamFail");
        String department = "Testing";
        Long result = sas.createTeam(department);
        assertEquals(null, result);
    }

    /**
     * Test of getTeams method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetTeamsListTeamEntityPass() {
        System.out.println("getTeamsListTeamEntityPass");
        String role = "Station Manager";
        List<TeamEntity> result = sas.getTeams(role);
        assertNotNull(result);
    }

    @Test
    public void testGetTeamsListTeamEntityFail() {
        System.out.println("getTeamsListTeamEntityFail");
        String role = "Testing";
        String result = "";
        List<TeamEntity> temp = sas.getTeams(role);
        if (temp.isEmpty()) {
            result = null;
            assertEquals(null, result);
        }
    }

    /**
     * Test of getTeams method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetAllTeamsPass() {
        System.out.println("getAllTeamsPass");
        List<TeamEntity> result = sas.getTeams();
        assertNotNull(result);
    }
    /**
     * Test of getRoles method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetRolesSuccess() {
        System.out.println("getRolesSuccess");
        List<RoleEntity> result = sas.getRoles();
        assertNotNull(result);
    }


    /**
     * Test of getStaffs method, of class SystemAdminSessionBeanRemote.
     */
    /*@Test
    public void testGetStaffsSuccess() {
        System.out.println("getStaffsSuccess");
        List<StaffEntity> result = sas.getStaffs();
        assertFalse(result.isEmpty());
    }*/

    @Test
    public void testDeleteStaffFail() {
        System.out.println("deleteStaffFail");
        String staffId = "iloveis4103";
        boolean result = sas.deleteStaff(staffId);
        assertFalse(result);
    }

    /**
     * Test of getAccess method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetAccessSuccess() {
        System.out.println("getAccessSuccess");
        List<AccessRightsEntity> result = sas.getAccess();
        assertNotNull(result);
    }


    /**
     * Test of getRolesAccess method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetRolesAccess() {
        System.out.println("getRolesAccess");
        List<RoleEntity> result = sas.getRolesAccess();
        assertNotNull(result);
    }

    /**
     * Test of retrieveStaffList method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testRetrieveStaffListSuccess() {
        System.out.println("retrieveStaffListSuccess");
        String nodeCode = "NSL13";
        String teamId = "1";
        ArrayList<StaffEntity> result = sas.retrieveStaffList(nodeCode, teamId);
        assertNotNull(result);
    }

    @Test
    public void testRetrieveStaffListFail() {
        System.out.println("retrieveStaffListFail");
        String nodeCode = "";
        String teamId = "";
        ArrayList<StaffEntity> result = sas.retrieveStaffList(nodeCode, teamId);
        assertEquals(null, result);
    }

    /**
     * Test of retrieveTeamList method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testRetrieveTeamListSuccess() {
        System.out.println("retrieveTeamListSuccess");
        String nodeCode = "NSL14";
        ArrayList<String> result = sas.retrieveTeamList(nodeCode);
        assertNotNull(result);
    }

    @Test
    public void testRetrieveTeamListFail() {
        System.out.println("retrieveTeamListFail");
        String nodeCode = "";
        ArrayList<String> result = sas.retrieveTeamList(nodeCode);
        assertEquals(null, result);
    }

    /**
     * Test of retrieveDTeamList method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testRetrieveDTeamListFail() {
        System.out.println("retrieveDTeamListFail");
        String nodeCode = "";
        StaffEntity staff = null;
        ArrayList<String> result = sas.retrieveDTeamList(nodeCode, staff);
        assertEquals(null, result);
    }

    /**
     * Test of getNodes method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetNodes() {
        System.out.println("getNodes");
        List<String> result = sas.getNodes();
        assertNotNull(result);
    }

    /**
     * Test of getStations method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetStations() {
        System.out.println("getStations");
        List<String> result = sas.getStations();
        assertNotNull(result);
    }

    /**
     * Test of getDepots method, of class SystemAdminSessionBeanRemote.
     */
    @Test
    public void testGetDepots() {
        System.out.println("getDepots");
        List<String> result = sas.getDepots();
        assertNotNull(result);
    }


    private SystemAdminSessionBeanRemote lookupEventSession() {
        try {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (SystemAdminSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/SystemAdminSessionBean!commoninfra.sessionbean.SystemAdminSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
