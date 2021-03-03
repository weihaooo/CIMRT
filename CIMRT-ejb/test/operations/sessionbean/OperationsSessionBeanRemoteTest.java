/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.RollingStockEntity;
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
import manpower.sessionbean.RosterSessionBeanRemote;
import operations.entity.InterviewEntity;
import operations.entity.JobApplicationsEntity;
import operations.entity.JobOfferEntity;
import operations.entity.MaintenanceRequestEntity;
import operations.entity.ServiceLogEntity;
import operations.entity.StaffContractEntity;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import routefare.entity.NodeEntity;

/**
 *
 * @author FABIAN
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OperationsSessionBeanRemoteTest {

    OperationsSessionBeanRemote osbr = lookupEventSession();

    public OperationsSessionBeanRemoteTest() {
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
     * Test of addSvcLog method, of class OperationsSessionBean.
     */
    @Test
    public void testAddSvcLogOK() throws Exception {
        System.out.println("addSvcLogOK");
        String subject = "Testing Service Log";
        String content = "Passnger Fainted";
        String staffId = "S000003";
        boolean result = osbr.addSvcLog(subject, content, staffId);
        assertTrue(result);
    }

    /**
     * Test of getSvcLogs method, of class OperationsSessionBean.
     */
    @Test
    public void testGetSvcLogs() throws Exception {
        System.out.println("getSvcLogs");
        String staffId = "S000001";
        ArrayList<ServiceLogEntity> result = osbr.getSvcLogs(staffId);
        assertNotNull(result);
    }

    /**
     * Test of getSvcLogs1 method, of class OperationsSessionBean.
     */
    @Test
    public void testGetSvcLogs1() throws Exception {
        System.out.println("getSvcLogs1");
        String nodeCode = "NSL01";
        ArrayList<ServiceLogEntity> result = osbr.getSvcLogs1(nodeCode);
        assertTrue(result.isEmpty());

    }

    /**
     * Test of searchStationStaff method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchStationStaff1() throws Exception {
        System.out.println("searchStationStaff1");
        String staffId = "S000004";
        StationStaffEntity result = osbr.searchStationStaff(staffId);
        assertEquals("Fabian", result.getFirstName());
    }

    @Test
    public void testSearchStationStaff2() throws Exception {
        System.out.println("searchStationStaff2");
        String staffId = "iloveis4103";
        StationStaffEntity result = osbr.searchStationStaff(staffId);
        assertEquals(null, result);
    }

    /**
     * Test of searchLog method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchLog() throws Exception {
        System.out.println("searchLog");
        String svcLogId = "Testing";
        try {
            ServiceLogEntity result = osbr.searchLog(svcLogId);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of getLeasingSpace method, of class OperationsSessionBean.
     */
    @Test
    public void testGetLeasingSpace() throws Exception {
        System.out.println("getLeasingSpace");
        String nodeCode = "EWL02";
        ArrayList<LeasingSpaceEntity> result = osbr.getLeasingSpace(nodeCode);
        assertNotNull(result);
    }

    @Test
    public void testGetLeasingSpace1() throws Exception {
        System.out.println("getLeasingSpace1");
        String nodeCode = "Testing";
        try {
            ArrayList<LeasingSpaceEntity> result = osbr.getLeasingSpace(nodeCode);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of getRollingStock method, of class OperationsSessionBean.
     */
    @Test
    public void testGetRollingStock() throws Exception {
        System.out.println("getRollingStock");
        ArrayList<RollingStockEntity> result = osbr.getRollingStock();
        assertNotNull(result.get(0));
    }

    /**
     * Test of getRollingStock method, of class OperationsSessionBean.
     */
    @Test
    public void testGetRollingStockBasedOnTeamId1() throws Exception {
        System.out.println("getRollingStockBasedOnTeamId1");
        String teamId = "3";
        ArrayList<RollingStockEntity> result = osbr.getRollingStock(teamId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRollingStockBasedOnTeamId2() throws Exception {
        System.out.println("getRollingStockBasedOnTeamId2");
        String teamId = "9999";
        ArrayList<RollingStockEntity> result = osbr.getRollingStock(teamId);
        assertEquals(null, result);
    }

    /**
     * Test of getRollingStockAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testGetRollingStockAssetByNodeCode1() throws Exception {
        System.out.println("getRollingStockAssetByNodeCode1");
        String nodeCode = "EWL01";
        ArrayList<RollingStockAssetEntity> result = osbr.getRollingStockAsset(nodeCode);
        assertNotNull(result);
    }

    @Test
    public void testGetRollingStockAssetByNodeCode2() throws Exception {
        System.out.println("getRollingStockAssetByNodeCode2");
        String nodeCode = "";
        ArrayList<RollingStockAssetEntity> result = osbr.getRollingStockAsset(nodeCode);
        assertEquals(null, result);
    }

    /**
     * Test of getConsumableAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testGetConsumableAsset1() throws Exception {
        System.out.println("getConsumableAsset1");
        String nodeCode = "NSL02";
        ArrayList<ConsumableAssetEntity> result = osbr.getConsumableAsset(nodeCode);
        assertNotNull(result);
    }

    @Test
    public void testGetConsumableAsset2() throws Exception {
        System.out.println("getConsumableAsset2");
        String nodeCode = "EWL100";
        try {
            ArrayList<ConsumableAssetEntity> result = osbr.getConsumableAsset(nodeCode);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }


    /**
     * Test of getLeasingSpace method, of class OperationsSessionBean.
     */
    @Test
    public void testGetLeasingSpace_0args() throws Exception {
        System.out.println("getLeasingSpace");
        ArrayList<LeasingSpaceEntity> result = osbr.getLeasingSpace();
        assertNotNull(result.get(0));
    }

    /**
     * Test of getRollingStockAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testGetRollingStockAsset() throws Exception {
        System.out.println("getRollingStockAsset");
        ArrayList<RollingStockAssetEntity> result = osbr.getRollingStockAsset();
        assertTrue(result.isEmpty());
    }

    /**
     * Test of getConsumableAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testGetConsumableAsset() throws Exception {
        System.out.println("getConsumableAsset");
        ArrayList<ConsumableAssetEntity> result = osbr.getConsumableAsset();
        assertFalse(result.isEmpty());
    }

    /**
     * Test of getNodeAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testGetNodeAsset() throws Exception {
        System.out.println("getNodeAsset");
        ArrayList<NodeAssetEntity> result = osbr.getNodeAsset();
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of searchNode method, of class OperationsSessionBean.
     */
    @Test
        public void testSearchNode1() throws Exception {
        System.out.println("searchNode1");
        String code = "EWL13";
        NodeEntity result = osbr.searchNode(code);
        assertEquals("EWL13", result.getCode());
    }

    @Test
    public void testSearchNode2() throws Exception {
        System.out.println("searchNode2");
        String code = "EWLIS4103";
        try {
            NodeEntity result = osbr.searchNode(code);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of searchInfra method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchInfra1() throws Exception {
        System.out.println("searchInfra1");
        String infraId = "IN14";
        InfrastructureEntity result = osbr.searchInfra(infraId);
        assertEquals("IN14", result.getInfraId());
    }

    @Test
    public void testSearchInfra2() throws Exception {
        System.out.println("searchInfra2");
        String infraId = "INIS4103";
        try {
            InfrastructureEntity result = osbr.searchInfra(infraId);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of searchRollingStock method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchRollingStock1() throws Exception {
        System.out.println("searchRollingStock1");
        String infraId = "IN44";
        RollingStockEntity result = osbr.searchRollingStock(infraId);
        assertEquals("Kawasaki", result.getBrand());
    }

    @Test
    public void testSearchRollingStock2() throws Exception {
        System.out.println("searchRollingStock2");
        String infraId = "INIS4103";
        try {
            RollingStockEntity result = osbr.searchRollingStock(infraId);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of addConsumableAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testAddConsumableAsset1() throws Exception {
        System.out.println("addConsumableAsset1");
        String assetName = "Printer HP Laser";
        String code = "EWL08";
        int quantity = 70;
        String inputString = "24-11-2019";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expiryDate = dateFormat.parse(inputString);
        String consumableAssetType = "Office Supplies";
        boolean result = osbr.addConsumableAsset(assetName, code, quantity, expiryDate, consumableAssetType);
        assertTrue(result);
    }

    @Test
    public void testAddConsumableAsset2() throws Exception {
        System.out.println("addConsumableAsset2");
        String assetName = "Printer HP Laser";
        String code = "EWLis4103";
        int quantity = 70;
        String inputString = "24-11-2019";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expiryDate = dateFormat.parse(inputString);
        String consumableAssetType = "Office Supplies";
        try {
            boolean result = osbr.addConsumableAsset(assetName, code, quantity, expiryDate, consumableAssetType);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of addNodeAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testAddNodeAsset1() throws Exception {
        System.out.println("addNodeAsset1");
        String assetName = "Samsung Ticketing Machine";
        String code = "NSL08";
        int lifetimeValue = 7;
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date purchaseDate = dateFormat.parse(inputString);
        int quantity = 80;
        String nodeAssetType = "Operating Equipment";
        boolean result = osbr.addNodeAsset(assetName, code, lifetimeValue, purchaseDate, quantity, nodeAssetType);
        assertTrue(result);
    }

    @Test
    public void testAddNodeAsset2() throws Exception {
        System.out.println("addNodeAsset2");
        String assetName = "Samsung Ticketing Machine";
        String code = "NSLIS4103";
        int lifetimeValue = 7;
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date purchaseDate = dateFormat.parse(inputString);
        int quantity = 80;
        String nodeAssetType = "Operating Equipment";
        try {
            boolean result = osbr.addNodeAsset(assetName, code, lifetimeValue, purchaseDate, quantity, nodeAssetType);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of searchTeam method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchTeam1() throws Exception {
        System.out.println("searchTeam1");
        String team = "1";
        TeamEntity result = osbr.searchTeam(team);
        assertNotNull(result);
    }

    @Test
    public void testSearchTeam2() throws Exception {
        System.out.println("searchTeam2");
        String team = "9999";
        TeamEntity result = osbr.searchTeam(team);
        assertEquals(null, result);
    }

    /**
     * Test of getAssetRequest method, of class OperationsSessionBean.
     */
    @Test
    public void testGetAssetRequest1() throws Exception {
        System.out.println("getAssetRequest1");
        String nodeCode = "EWL02";
        ArrayList<AssetRequestEntity> result = osbr.getAssetRequest(nodeCode);
        assertNotNull(result);
    }

    @Test
    public void testGetAssetRequest2() throws Exception {
        System.out.println("getAssetRequest2");
        String nodeCode = "IS4103";
        try {
            ArrayList<AssetRequestEntity> result = osbr.getAssetRequest(nodeCode);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of getNodeAsset method, of class OperationsSessionBean.
     */
    @Test
    public void testGetNodeAsset1() throws Exception {
        System.out.println("getNodeAsset1");
        String nodeCode = "EWL05";
        ArrayList<NodeAssetEntity> result = osbr.getNodeAsset(nodeCode);
        assertNotNull(result);
    }

    @Test
    public void testGetNodeAsset2() throws Exception {
        System.out.println("getNodeAsset2");
        String nodeCode = "IS4103";
        try {
            ArrayList<NodeAssetEntity> result = osbr.getNodeAsset(nodeCode);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of getJobOffer method, of class OperationsSessionBean.
     */
    @Test
    public void testGetJobOffer() throws Exception {
        System.out.println("getJobOffer");
        ArrayList<JobOfferEntity> result = osbr.getJobOffer();
        assertNotNull(result);
    }

    /**
     * Test of createJob method, of class OperationsSessionBean.
     */
    @Test
    public void testCreateJob() throws Exception {
        System.out.println("createJob");
        String staffId = "HQ000002";
        String jobTitle = "Testing";
        String jobPosition = "Accountant";
        String location = "Finance";
        String jobType = "Contract";
        double salary = 2600.0;
        String jobDescription = "Have to be enthusiastic";
        String jobQualifications = "Bachelor's Degree";
        String inputString = "13-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date postedDate = dateFormat.parse(inputString);

        String inputString2 = "30-11-2017";
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date jobDeadline = dateFormat2.parse(inputString2);

        boolean result = osbr.createJob(staffId, jobTitle, jobPosition, location, jobType, salary, jobDescription, jobQualifications, postedDate, jobDeadline);
        assertTrue(result);
    }

    /**
     * Test of updateJob method, of class OperationsSessionBean.
     */
    @Test
    public void testUpdateJob1() throws Exception {
        System.out.println("updateJob1");
        String jobId = "J9";
        String jobTitle = "Full-Time Needed";
        String jobPosition = "Software Developer";
        String location = "IT";
        String jobType = "Full-Time";
        double salary = 4000.0;
        String jobDescription = "Python and Java";
        String jobQualifications = "Bachelor's Degree";
        String inputString = "13-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date postedDate = dateFormat.parse(inputString);

        String inputString2 = "30-11-2017";
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date jobDeadline = dateFormat2.parse(inputString2);

        boolean result = osbr.updateJob(jobId, jobTitle, jobPosition, location, jobType, salary, jobDescription, jobQualifications, postedDate, jobDeadline);
        assertTrue(result);
    }

    @Test
    public void testUpdateJob2() throws Exception {
        System.out.println("updateJob2");
        String jobId = "is4103";
        String jobTitle = "Full-Time Needed";
        String jobPosition = "Software Developer";
        String location = "IT";
        String jobType = "Full-Time";
        double salary = 4000.0;
        String jobDescription = "Python and Java";
        String jobQualifications = "Bachelor's Degree";
        String inputString = "13-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date postedDate = dateFormat.parse(inputString);

        String inputString2 = "30-11-2017";
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date jobDeadline = dateFormat2.parse(inputString2);

        try {
            boolean result = osbr.updateJob(jobId, jobTitle, jobPosition, location, jobType, salary, jobDescription, jobQualifications, postedDate, jobDeadline);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of searchJob method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchJob1() throws Exception {
        System.out.println("searchJob1");
        String jobId = "J7";
        JobOfferEntity result = osbr.searchJob(jobId);
        assertEquals("Train Captain", result.getJobPosition());
    }

    @Test
    public void testSearchJob2() throws Exception {
        System.out.println("searchJob2");
        String jobId = "is4103";
        try {
            JobOfferEntity result = osbr.searchJob(jobId);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of searchJobApp method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchJobApp1() throws Exception {
        System.out.println("searchJobApp1");
        String applicationId = "A9";
        JobApplicationsEntity result = osbr.searchJobApp(applicationId);
        assertEquals("Boon Lay", result.getAddress());
    }

    @Test
    public void testSearchJobApp2() throws Exception {
        System.out.println("searchJobApp2");
        String applicationId = "is4103";
        try {
            JobApplicationsEntity result = osbr.searchJobApp(applicationId);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of goJobStatus method, of class OperationsSessionBean.
     */
    @Test
    public void testGoJobStatus1() throws Exception {
        System.out.println("goJobStatus1");
        String jobId = "J7";
        boolean result = osbr.goJobStatus(jobId);
        assertTrue(result);
    }

    /**
     * Test of getJobApp method, of class OperationsSessionBean.
     */
    @Test
    public void testGetJobApp() throws Exception {
        System.out.println("getJobApp");
        ArrayList<JobApplicationsEntity> result = osbr.getJobApp();
        assertNotNull(result.get(0));
    }

    /**
     * Test of updateJobApp method, of class OperationsSessionBean.
     */
    @Test
    public void testUpdateJobApp1() throws Exception {
        System.out.println("updateJobApp1");
        String applicationId = "A5";
        String appStatus = "Rejected";
        String emailAddress = "e0002468@u.nus.edu";
        boolean result = osbr.updateJobApp(applicationId, appStatus, emailAddress);
        assertTrue(result);
    }

    @Test
    public void testUpdateJobApp2() throws Exception {
        System.out.println("updateJobApp2");
        String applicationId = "is4103";
        String appStatus = "Rejected";
        String emailAddress = "e0002468@u.nus.edu";
        try {
            boolean result = osbr.updateJobApp(applicationId, appStatus, emailAddress);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of createInterview method, of class OperationsSessionBean.
     */
    @Test
    public void testCreateInterview1() throws Exception {
        System.out.println("createInterview1");
        String applicationId = "A9";
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date interviewFrom = dateFormat.parse(inputString);

        String inputString2 = "24-11-2017";
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date interviewTo = dateFormat2.parse(inputString2);
        String emailAddress = "e0002468@u.nus.edu";

        boolean result = osbr.createInterview(applicationId, interviewFrom, interviewTo, emailAddress);
        assertTrue(result);
    }

    @Test
    public void testCreateInterview2() throws Exception {
        System.out.println("createInterview2");
        String applicationId = "AAAA";
        String inputString = "24-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date interviewFrom = dateFormat.parse(inputString);

        String inputString2 = "24-11-2017";
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date interviewTo = dateFormat2.parse(inputString2);
        String emailAddress = "e0002468@u.nus.edu";

        try {
            boolean result = osbr.createInterview(applicationId, interviewFrom, interviewTo, emailAddress);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of getInterviewList method, of class OperationsSessionBean.
     */
    @Test
    public void testGetInterviewList() throws Exception {
        System.out.println("getInterviewList");
        ArrayList<InterviewEntity> result = osbr.getInterviewList();
        assertNotNull(result.get(0).getInterviewId());
    }

    /**
     * Test of searchInterview method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchInterview() throws Exception {
        System.out.println("searchInterview");
        String interviewId = "is4103";
        try {
            InterviewEntity result = osbr.searchInterview(interviewId);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }

    }

    /**
     * Test of deleteInterview method, of class OperationsSessionBean.
     */
    @Test
    public void testDeleteInterview() throws Exception {
        System.out.println("deleteInterview");
        String applicationId = null;
        String emailAddress = null;
        boolean result = osbr.deleteInterview(applicationId, emailAddress);
        assertFalse(result);
    }

    /**
     * Test of getAssetRequests method, of class OperationsSessionBean.
     */
    @Test
    public void testGetAssetRequests() throws Exception {
        System.out.println("getAssetRequests");
        ArrayList<AssetRequestEntity> result = osbr.getAssetRequests();
        assertNotNull(result);
    }

    /**
     * Test of submitMaintenanceRequest method, of class OperationsSessionBean.
     */
    @Test
    public void testSubmitMaintenanceRequest() throws Exception {
        System.out.println("submitMaintenanceRequest");
        String teamId = "1";
        String staffId = "S000002";
        String assetRequestType = "Operating Equipment";
        String infraId = "IN42";
        String assetType = "Operating Equipment";
        String assetName = "Ticketing Machine";
        String mainReqType = "Repair";
        String remark = "Check";
        Date now = new Date();
        boolean result = osbr.submitMaintenanceRequest(teamId, staffId, assetRequestType, infraId, assetType, assetName, mainReqType, remark, now);
        assertTrue(result);
    }

    /**
     * Test of getRollingStockList method, of class OperationsSessionBean.
     */
    @Test
    public void testGetRollingStockList() throws Exception {
        System.out.println("getRollingStockList");
        ArrayList<String> result = osbr.getRollingStockList();
        assertNotNull(result);
    }

    /**
     * Test of getMaintenanceRequestList method, of class OperationsSessionBean.
     */
    @Test
    public void testGetMaintenanceRequestList() throws Exception {
        System.out.println("getMaintenanceRequestList");
        List<MaintenanceRequestEntity> result = osbr.getMaintenanceRequestList();
        assertNotNull(result);
    }

    /**
     * Test of getStationsInCharge method, of class OperationsSessionBean.
     */
    @Test
    public void testGetStationsInCharge() throws Exception {
        System.out.println("getStationsInCharge");
        String staffId = "D000001";
        List<NodeEntity> result = osbr.getStationsInCharge(staffId);
        assertEquals("EWL00", result.get(2).getCode());
    }

    /**
     * Test of getMaintenanceRequestListByDepot method, of class
     * OperationsSessionBean.
     */
    @Test
    public void testGetMaintenanceRequestListByDepot() throws Exception {
        System.out.println("getMaintenanceRequestListByDepot");
        String staffId = "D000001";
        List<MaintenanceRequestEntity> result = osbr.getMaintenanceRequestListByDepot(staffId);
        assertNotNull(result);
    }

    /**
     * Test of getMaintenanceRequestList1 method, of class
     * OperationsSessionBean.
     */
    @Test
    public void testGetMaintenanceRequestList1() throws Exception {
        System.out.println("getMaintenanceRequestList1");
        String nodeCode = null;
        List<MaintenanceRequestEntity> result = osbr.getMaintenanceRequestList1(nodeCode);
        assertEquals(null, result);
    }

    /**
     * Test of getMaintenanceRequestList2 method, of class
     * OperationsSessionBean.
     */
    @Test
    public void testGetMaintenanceRequestList2() throws Exception {
        System.out.println("getMaintenanceRequestList2");
        String staffId = "D000001";
        List<MaintenanceRequestEntity> result = osbr.getMaintenanceRequestList2(staffId);
        assertNotNull(result);
    }

    /**
     * Test of searchMaintenaceRequest method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchMaintenaceRequest() throws Exception {
        System.out.println("searchMaintenaceRequest");
        String mainReqId = "MR1";
        MaintenanceRequestEntity result = osbr.searchMaintenaceRequest(mainReqId);
        assertEquals("Repair", result.getMainReqType());
    }

    /**
     * Test of updateMaintenanceRequest method, of class OperationsSessionBean.
     */
    @Test
    public void testUpdateMaintenanceRequest() throws Exception {
        System.out.println("updateMaintenanceRequest");
        String mainReqId = "MR1";
        String mainReqStatus = "Replaced";
        boolean result = osbr.updateMaintenanceRequest(mainReqId, mainReqStatus);
        assertTrue(result);
    }

    /**
     * Test of createContract method, of class OperationsSessionBean.
     */
    @Test
    public void testCreateContract() throws Exception {
        System.out.println("createContract");
        String appliciantId = "A2";
        String inputString = "20-11-2017";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = dateFormat.parse(inputString);
        String inputString2 = "20-11-2019";
        Date endDate = dateFormat.parse(inputString2);
        String emailAddress = "e0002468@u.nus.edu";
        boolean result = osbr.createContract(appliciantId, startDate, endDate, emailAddress);
        assertTrue(result);
    }

    /**
     * Test of searchStaffContract method, of class OperationsSessionBean.
     */
    @Test
    public void testSearchStaffContract() throws Exception {
        System.out.println("searchStaffContract");
        String staffContractId = "testing";
        try {
            StaffContractEntity result = osbr.searchStaffContract(staffContractId);
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
    }

    /**
     * Test of getStaffContract method, of class OperationsSessionBean.
     */
    @Test
    public void testGetStaffContract() throws Exception {
        System.out.println("getStaffContract");
        String appliciantId = null;
        StaffContractEntity result = osbr.getStaffContract(appliciantId);
        assertEquals(null, result);
    }

    private OperationsSessionBeanRemote lookupEventSession() {
        try {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (OperationsSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/OperationsSessionBean!operations.sessionbean.OperationsSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
