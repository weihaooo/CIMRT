/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.entity.HqStaffEntity;
import commoninfra.entity.StaffEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityNotFoundException;
import manpower.entity.WorkshopEntity;
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
public class ManpowerSessionBeanRemoteTest {
    
    ManpowerSessionBeanRemote msbr = lookupEventSession();
    
    public ManpowerSessionBeanRemoteTest() {
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
     * Test of createWorkshop method, of class ManpowerSessionBean.
     */
    @Test
    public void testCreateWorkshopSuccess() throws Exception {
        System.out.println("createWorkshopSuccess");
        String workshopName = "JAVA Hello World";
        String workshopType = "External";
        String description = "Basics of Java";
        Date startDate = new Date();
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date endDate = dateFormat.parse(inputString);
        String workshopStartTime = "1300HRS";
        String workshopEndTime = "1500HRS";
        String workshopAddress = "Choa Chu Kang";
        boolean result = msbr.createWorkshop(workshopName, workshopType, description, startDate, endDate, workshopStartTime, workshopEndTime, workshopAddress);
        assertTrue(result);
    }
    
    @Test
    public void testCreateWorkshopFail() throws Exception {
        System.out.println("createWorkshopFail");
        String workshopName = null;
        String workshopType = "External";
        String description = "Basics of Java";
        Date startDate = new Date();
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date endDate = dateFormat.parse(inputString);
        String workshopStartTime = "1300HRS";
        String workshopEndTime = "1500HRS";
        String workshopAddress = "Choa Chu Kang";
        boolean result = msbr.createWorkshop(workshopName, workshopType, description, startDate, endDate, workshopStartTime, workshopEndTime, workshopAddress);
        assertFalse(result);
    }

    /**
     * Test of getExpireSafetyList method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetExpireSafetyList() throws Exception {
        System.out.println("getExpireSafetyList");
        ArrayList<ArrayList<String>> result = msbr.getExpireSafetyList();
        assertNotNull(result);
    }

    /**
     * Test of getOwnTeamExpireSafetyList method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetOwnTeamExpireSafetyListSuccess() throws Exception {
        System.out.println("getOwnTeamExpireSafetyListSuccess");
        String staffId = "S000001";
        ArrayList<ArrayList<String>> result = msbr.getOwnTeamExpireSafetyList(staffId);
        assertNotNull(result);
    }
    
    @Test
    public void testGetOwnTeamExpireSafetyListFail() throws Exception {
        System.out.println("getOwnTeamExpireSafetyListFail");
        String staffId = "iloveis4103";
        ArrayList<ArrayList<String>> result = msbr.getOwnTeamExpireSafetyList(staffId);
        assertEquals(null,result);
    }

    /**
     * Test of signupWorkshopHR method, of class ManpowerSessionBean.
     */
    @Test
    public void testSignupWorkshopHR() throws Exception {
        System.out.println("signupWorkshopHR");
        Long workshopId = Long.valueOf("1");
        String workshopName = "Java EE Workshop";
        List<String> selectedHRStaffs = new ArrayList<String>();
        selectedHRStaffs.add("HQ000003 Marcus");
        String staffId = "HQ000002";
        String result = msbr.signupWorkshopHR(workshopId, workshopName, selectedHRStaffs, staffId);
        assertEquals("Updated", result);
    }

    /**
     * Test of getStaffUnderSameTeam method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetStaffUnderSameTeamSucess() throws Exception {
        System.out.println("getStaffUnderSameTeamSuccess");
        String staffId = "D000001";
        List<StaffEntity> result = msbr.getStaffUnderSameTeam(staffId);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testGetStaffUnderSameTeamFail() throws Exception {
        System.out.println("getStaffUnderSameTeamFail");
        String staffId = "testing";
        List<StaffEntity> result = msbr.getStaffUnderSameTeam(staffId);
        assertEquals(null, result);
    }

    /**
     * Test of getAllHRStaff method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetAllHRStaffSuccess() throws Exception {
        System.out.println("getAllHRStaffSuccess");
        List<HqStaffEntity> result = msbr.getAllHRStaff();
        assertFalse(result.get(0).getStaffId() == null);
    }
    
    /**
     * Test of getAllWorkshops method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetAllWorkshops() throws Exception {
        System.out.println("getAllWorkshops");
        List<WorkshopEntity> result = msbr.getAllWorkshops();
        assertNotNull(result.get(0).getWorkshopId());
    }

    /**
     * Test of getSpecificWorkshop method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetSpecificWorkshopOK() throws Exception {
        System.out.println("getSpecificWorkshopOK");
        Long workshopId = Long.valueOf("2");
        String workshopName = "Java EE3 Workshop";
        List<WorkshopEntity> result = msbr.getSpecificWorkshop(workshopId, workshopName);
        assertNotNull(result.get(0).getWorkshopId());
    }
    
    @Test
    public void testGetSpecificWorkshopNOTOK() throws Exception {
        System.out.println("getSpecificWorkshopNOTOK");
        Long workshopId = null;
        String workshopName = null;
        List<WorkshopEntity> result = msbr.getSpecificWorkshop(workshopId, workshopName);
        assertEquals(null, result);
    }

    /**
     * Test of updateWorkshop method, of class ManpowerSessionBean.
     */
    @Test
    public void testUpdateWorkshopOK() throws Exception {
        System.out.println("updateWorkshopOK");
        Long workshopId = Long.valueOf("1");
        String workshopName = "New WorkshopName: Python Workshop";
        String workshopType = "External";
        String description = "[Update]: Basics of Python will be taught here";
        String workshopAddress = "Katong Mall #09-009";
        String inputString = "20-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = dateFormat.parse(inputString);
        String inputString2 = "30-11-2017";
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date endDate = dateFormat2.parse(inputString2);
        
        String workshopStartTime = "1500HRS";
        String workshopEndTime = "1600HRS";
        boolean result = msbr.updateWorkshop(workshopId, workshopName, workshopType, description, workshopAddress, startDate, endDate, workshopStartTime, workshopEndTime);
        assertTrue(result);
    }
    
    @Test
    public void testUpdateWorkshopNOTOK() throws Exception {
        System.out.println("updateWorkshopNOTOK");
        Long workshopId = null;
        String workshopName = "New WorkshopName: Python Workshop";
        String workshopType = "External";
        String description = "[Update]: Basics of Python will be taught here";
        String workshopAddress = "Katong Mall #09-009";
        String inputString = "20-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = dateFormat.parse(inputString);
        String inputString2 = "30-11-2017";
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date endDate = dateFormat2.parse(inputString2);
        
        String workshopStartTime = "1500HRS";
        String workshopEndTime = "1600HRS";
        boolean result = msbr.updateWorkshop(workshopId, workshopName, workshopType, description, workshopAddress, startDate, endDate, workshopStartTime, workshopEndTime);
        assertFalse(result);
    }

    /**
     * Test of searchWorkshop method, of class ManpowerSessionBean.
     */
    @Test
    public void testSearchWorkshopOK() throws Exception {
        System.out.println("searchWorkshopOK");
        Long workshopId = Long.valueOf("3");
        WorkshopEntity result = msbr.searchWorkshop(workshopId);
        assertNotNull(result);
    }
    
    @Test
    public void testSearchWorkshopNOTOK(){
        System.out.println("searchWorkshopOK");
        Long workshopId = Long.valueOf("999999");
        try{
            WorkshopEntity result = msbr.searchWorkshop(workshopId);
        }catch (EntityNotFoundException enfe){
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of delWorkshop method, of class ManpowerSessionBean.
     */
    @Test
    public void testDelWorkshopFail() throws Exception {
        System.out.println("delWorkshopFail");
        Long workshopId = null;
        boolean result = msbr.delWorkshop(workshopId);
        assertFalse(result);

    }

    /**
     * Test of retrieveWorkshops method, of class ManpowerSessionBean.
     */
    @Test
    public void testRetrieveWorkshops() throws Exception {
        System.out.println("retrieveWorkshops");
        String staffId = "S000001";
        List<WorkshopEntity> result = msbr.retrieveWorkshops(staffId);
        assertNotNull(result);
    }

    /**
     * Test of getMembersWorkshopsEnrolled method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetMembersWorkshopsEnrolled() throws Exception {
        System.out.println("getMembersWorkshopsEnrolled");
        String staffId = "S000001";
        List<StaffEntity> result = msbr.getMembersWorkshopsEnrolled(staffId);
        assertNotNull(result);
    }

    /**
     * Test of getHRMembersWorkshopsEnrolled method, of class ManpowerSessionBean.
     */
    @Test
    public void testGetHRMembersWorkshopsEnrolled() throws Exception {
        System.out.println("getHRMembersWorkshopsEnrolled");
        String staffId = "HQ000002";
        List<StaffEntity> result = msbr.getHRMembersWorkshopsEnrolled(staffId);
        assertNotNull(result);
    }
    
    private ManpowerSessionBeanRemote lookupEventSession() {
        try {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (ManpowerSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/ManpowerSessionBean!manpower.sessionbean.ManpowerSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
