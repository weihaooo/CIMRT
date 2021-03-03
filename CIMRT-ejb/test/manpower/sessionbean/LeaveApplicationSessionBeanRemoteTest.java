/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.entity.StaffEntity;
import commoninfra.sessionbean.SystemAdminSessionBeanRemote;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import operations.entity.LeaveApplicationEntity;
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
public class LeaveApplicationSessionBeanRemoteTest {

    LeaveApplicationSessionBeanRemote leave = lookupEventSession();

    public LeaveApplicationSessionBeanRemoteTest() {
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
     * Test of createLeaveApplication method, of class
     * LeaveApplicationSessionBean.
     */
    @Test
    public void testCreateLeaveApplicationSuccess() throws Exception {
        System.out.println("createLeaveApplicationSuccess");
        String type = "AL";
        String description = "Holiday";
        Date startDate = new Date();
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date endDate = dateFormat.parse(inputString);
        String applicantId = "S000002";
        LeaveApplicationEntity result = leave.createLeaveApplication(type, description, startDate, endDate, applicantId);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateLeaveApplicationFail() throws Exception {
        System.out.println("createLeaveApplicationFail");
        String type = null;
        String description = null;
        Date startDate = new Date();
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date endDate = dateFormat.parse(inputString);
        String applicantId = "iloveis4103";
        LeaveApplicationEntity result = leave.createLeaveApplication(type, description, startDate, endDate, applicantId);
        assertEquals(null, result);
    }

    /**
     * Test of viewLeaveApplications method, of class
     * LeaveApplicationSessionBean.
     */
    @Test
    public void testViewLeaveApplicationsSuccess() throws Exception {
        System.out.println("viewLeaveApplicationsSuccess");
        String staffId = "S000001";;
        ArrayList<LeaveApplicationEntity> result = leave.viewLeaveApplications(staffId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testViewLeaveApplicationsFail() throws Exception {
        System.out.println("viewLeaveApplicationsFail");
        String staffId = "iloveis4103";
        ArrayList<LeaveApplicationEntity> result = leave.viewLeaveApplications(staffId);
        assertEquals(null,result);
    }
    
    /**
     * Test of viewOutstandingApplications method, of class
     * LeaveApplicationSessionBean.
     */
    @Test
    public void testViewOutstandingApplicationsSuccess() throws Exception {
        System.out.println("viewOutstandingApplicationsSuccess");
        String staffId = "HQ000003";
        String role = "I&A Staff";
        String team = "1";
        String nodeCode = "IN38";
        ArrayList<LeaveApplicationEntity> result = leave.viewOutstandingApplications(staffId, role, team, nodeCode);
        assertNotNull(result.get(0));
    }
    
    @Test
    public void testViewOutstandingApplicationsFail() throws Exception {
        ArrayList<LeaveApplicationEntity> empty = new ArrayList<LeaveApplicationEntity>();
        System.out.println("viewOutstandingApplicationsFail");
        String staffId = "HQ000003";
        String role = "I&A Staff";
        String team = "1";
        String nodeCode = null;
        ArrayList<LeaveApplicationEntity> result = leave.viewOutstandingApplications(staffId, role, team, nodeCode);
        assertEquals(empty, result);
    }

    /**
     * Test of searchLeaveApplication method, of class
     * LeaveApplicationSessionBean.
     */
    @Test
    public void testSearchLeaveApplicationFail() throws Exception {
        System.out.println("searchLeaveApplicationFail");
        Long appId = null;
        LeaveApplicationEntity result = leave.searchLeaveApplication(appId);
        assertEquals(null, result);
    }

    /**
     * Test of getLeaveBal method, of class LeaveApplicationSessionBean.
     */
    @Test
    public void testGetLeaveBalSuccess() throws Exception {
        System.out.println("getLeaveBalSuccess");
        String type = "AL";
        String staffId = "S000001";
        int result = leave.getLeaveBal(type, staffId);
        assertEquals(14,result);
    }
    
    @Test
    public void testGetLeaveBalFail() throws Exception {
        System.out.println("getLeaveBalFail");
        String type = "AL";
        String staffId = "iloveis4103";
        int result = leave.getLeaveBal(type, staffId);
        assertEquals(999,result);
    }

    /**
     * Test of getOffDays method, of class LeaveApplicationSessionBean.
     */
    @Test
    public void testGetOffDaysFail() throws Exception {
        System.out.println("getOffDaysFail");
        String staffId = "iloveis4103";
        Date start = new Date();
        String inputString = "30-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date end = dateFormat.parse(inputString);
        int result = leave.getOffDays(staffId, start, end);
        assertEquals(999, result);
    }


    private LeaveApplicationSessionBeanRemote lookupEventSession() {
        try {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (LeaveApplicationSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/LeaveApplicationSessionBean!manpower.sessionbean.LeaveApplicationSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
