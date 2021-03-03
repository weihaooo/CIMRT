/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import manpower.entity.RosterEntity;
import manpower.entity.ShiftEntity;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runners.MethodSorters;
import routefare.entity.NodeEntity;

/**
 *
 * @author FABIAN
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RosterSessionBeanRemoteTest {
    
    RosterSessionBeanRemote rsbr = lookupEventSession();
    
    public RosterSessionBeanRemoteTest() {
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
     * Test of getTodayShift method, of class RosterSessionBean.
     */
    /**
     * Test of getRosterByNode method, of class RosterSessionBean.
     */
    @Test
    public void testGetRosterByNode() throws Exception {
        System.out.println("getRosterByNode");
        String code = "IN14";
        Date startDate = new Date();
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date endDate = dateFormat.parse(inputString);
        ArrayList<ArrayList<RosterEntity>> result = rsbr.getRosterByNode(code, startDate, endDate);
        assertNotNull(result);
    }


    /**
     * Test of getRoster method, of class RosterSessionBean.
     */
    @Test
    public void testGetRoster() throws Exception {
        System.out.println("getRoster");
        List<RosterEntity> result = rsbr.getRoster();
        assertEquals(null, result);
    }

    
    /**
     * Test of getNodes method, of class RosterSessionBean.
     */
    @Test
    public void testGetNodes() throws Exception {
        System.out.println("getNodes");
        List<String> result = rsbr.getNodes();
        assertNotNull(result.get(0));
    }

    /**
     * Test of updateDateRange method, of class RosterSessionBean.
     */
    @Test
    public void testUpdateDateRange() throws Exception {
        System.out.println("updateDateRange");
        List<Date> expResult = new ArrayList<Date>();
        String code = "NSL04";
        List<Date> result = rsbr.updateDateRange(code);
        assertEquals(expResult, result);
    }

   

    /**
     * Test of retrieveMinDate method, of class RosterSessionBean.
     */
    @Test
    public void testRetrieveMinDate() throws Exception {
        System.out.println("retrieveMinDate");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date expResult = calendar.getTime();
        Date result = rsbr.retrieveMinDate();
        assertEquals(expResult, result);

    }

    /**
     * Test of retrieveMaxDate method, of class RosterSessionBean.
     */
    @Test
    public void testRetrieveMaxDate() throws Exception {
        System.out.println("retrieveMaxDate");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date expResult = calendar.getTime();
        Date result = rsbr.retrieveMaxDate();
        assertEquals(expResult, result);
    }

    @Test
    public void testRetrieveStaffList() throws Exception {
        System.out.println("retrieveStaffListNOTOK");
        String nodeCode = "";
        String teamId = "";
        ArrayList<StaffEntity> result = rsbr.retrieveStaffList(nodeCode, teamId);
        assertEquals(null, result);
    }
    
    @Test
    public void testRetrieveTeamList() throws Exception {
        System.out.println("retrieveTeamListNOTOK");
        String nodeCode = "";
        ArrayList<String> result = rsbr.retrieveTeamList(nodeCode);
        assertEquals(null,result);
    }
    
    /**
     * Test of retrieveSStaffWithoutTeamList method, of class RosterSessionBean.
     */
    @Test
    public void testRetrieveSStaffWithoutTeamList() throws Exception {
        System.out.println("retrieveSStaffWithoutTeamList");
        ArrayList<StationStaffEntity> result = rsbr.retrieveSStaffWithoutTeamList();
        assertEquals(null, result);
    }

    /**
     * Test of retrieveDStaffWithoutTeamList method, of class RosterSessionBean.
     */
    @Test
    public void testRetrieveDStaffWithoutTeamList() throws Exception {
        System.out.println("retrieveDStaffWithoutTeamList");
        ArrayList<DepotStaffEntity> result = rsbr.retrieveDStaffWithoutTeamList();
        assertEquals(null, result);
    }

    /**
     * Test of getStations method, of class RosterSessionBean.
     */
    @Test
    public void testGetStations() throws Exception {
        System.out.println("getStations");
        List<String> result = rsbr.getStations();
        assertNotNull(result.get(0));
    }

    /**
     * Test of getDepots method, of class RosterSessionBean.
     */
    @Test
    public void testGetDepots() throws Exception {
        System.out.println("getDepots");
        List<String> result = rsbr.getDepots();
        assertNotNull(result.get(0));
    }
    
    private RosterSessionBeanRemote lookupEventSession() {
        try {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (RosterSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/RosterSessionBean!manpower.sessionbean.RosterSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
