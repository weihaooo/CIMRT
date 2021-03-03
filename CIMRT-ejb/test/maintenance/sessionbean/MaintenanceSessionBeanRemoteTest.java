/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance.sessionbean;

import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.TrainCarEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import maintenance.entity.MaintenanceReportEntity;
import operations.entity.MaintenanceRequestEntity;
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

public class MaintenanceSessionBeanRemoteTest {
    
    MaintenanceSessionBeanRemote maintenanceSessionBeanRemote = lookupEventSession();
    
    public MaintenanceSessionBeanRemoteTest() {
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
     * Test of createMaintenanceReport method, of class MaintenanceSessionBean.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreateMaintenanceReport1() throws Exception {
        System.out.println("createMaintenanceReport1");
        String rptTitle = "Testing";
        String reportDescription = "Report Description Testing 1";
        String asset1 = "1";
        String qty1 = "0";
        String maintenanceStatus = "Processing";
        Timestamp time = Timestamp.valueOf("2017-11-07 11:30:00");
        String staffId = "D000002";
        String submitterName = "Roger";
        String mainReqId = "MR1";
        String assetDetails = "NA40 NA40 NA40";
        String qtySpoilt = "1";
        MaintenanceReportEntity result = maintenanceSessionBeanRemote.createMaintenanceReport(rptTitle, reportDescription, asset1, qty1, maintenanceStatus, time, staffId, submitterName, mainReqId, assetDetails, qtySpoilt);
        assertNotNull(result);

    }
    
     @Test
    public void testCreateMaintenanceReport2() throws Exception {
        System.out.println("createMaintenanceReport2");
        String rptTitle = "Testing 2";
        String reportDescription = "Report Description Testing 1";
        String asset1 = "2";
        String qty1 = "0";
        String maintenanceStatus = "Processing";
        Timestamp time = Timestamp.valueOf("2017-11-08 11:30:00");
        String staffId = "D000002";
        String submitterName = "Roger";
        String mainReqId = null;
        String assetDetails = null;
        String qtySpoilt = "1";
        MaintenanceReportEntity result = maintenanceSessionBeanRemote.createMaintenanceReport(rptTitle, reportDescription, asset1, qty1, maintenanceStatus, time, staffId, submitterName, mainReqId, assetDetails, qtySpoilt);
        assertEquals(null, result);     
    }

    /**
     * Test of createMaintenanceReportTrainCar method, of class MaintenanceSessionBean.
     */
    @Test
    public void testCreateMaintenanceReportTrainCar1() throws Exception {
        System.out.println("createMaintenanceReportTrainCar1");
        String rptTitle = "Testing";
        String reportDescription = "Faulty brakes";
        String maintenanceStatus = "Repaired";
        Timestamp time = Timestamp.valueOf("2017-11-08 10:30:00");
        String staffId = "D000003";
        String submitterName = "WeiHao";
        String mainReqId = "MR4";
        String assetDetails = "AS2653 AS2563 AS2563";
        boolean result = maintenanceSessionBeanRemote.createMaintenanceReportTrainCar(rptTitle, reportDescription, maintenanceStatus, time, staffId, submitterName, mainReqId, assetDetails);
        assertTrue(result);
    }
    
    @Test
        public void testCreateMaintenanceReportTrainCar2() throws Exception {
        System.out.println("createMaintenanceReportTrainCar2");
        String rptTitle = "Testing";
        String reportDescription = "Faulty brakes";
        String maintenanceStatus = "Repaired";
        Timestamp time = Timestamp.valueOf("2017-11-08 10:30:00");
        String staffId = "D000003";
        String submitterName = "WeiHao";
        String mainReqId = null;
        String assetDetails = null;
        boolean result = maintenanceSessionBeanRemote.createMaintenanceReportTrainCar(rptTitle, reportDescription, maintenanceStatus, time, staffId, submitterName, mainReqId, assetDetails);
        assertFalse(result);
    }
    

    /**
     * Test of updateMaintenanceReport method, of class MaintenanceSessionBean.
     */
    @Test
    public void testUpdateMaintenanceReport1() throws Exception {
        System.out.println("updateMaintenanceReport1");
        Long maintenanceReportId = Long.valueOf("2");
        String asset1 = "1";
        String qty1 = "0";
        String rptTitle = "Testing";
        String reportDescription = "Change To Updated One";
        String maintenanceStatus = "Repaired";
        String qtySpoilt = "0";
        String assetName = "NA40 NA40 NA40";
        boolean result = maintenanceSessionBeanRemote.updateMaintenanceReport(maintenanceReportId, asset1, qty1, rptTitle, reportDescription, maintenanceStatus, qtySpoilt, assetName);
        assertTrue(result);

    }

    @Test
    public void testUpdateMaintenanceReport2() throws Exception {
        System.out.println("updateMaintenanceReport2");
        Long maintenanceReportId = Long.valueOf("2");
        String asset1 = "1";
        String qty1 = "0";
        String rptTitle = "Testing";
        String reportDescription = "Change To Updated One";
        String maintenanceStatus = "Repaired";
        String qtySpoilt = "0";
        String assetName = null;
        boolean result = maintenanceSessionBeanRemote.updateMaintenanceReport(maintenanceReportId, asset1, qty1, rptTitle, reportDescription, maintenanceStatus, qtySpoilt, assetName);
        assertFalse(result);

    }
    
    
    /**
     * Test of getAllReports method, of class MaintenanceSessionBean.
     */
    @Test
    public void testGetAllReports() throws Exception {
        System.out.println("getAllReports");
        List<MaintenanceReportEntity> expResult = null;
        List<MaintenanceReportEntity> result = maintenanceSessionBeanRemote.getAllReports();
        if(result.isEmpty()){
            assertEquals(null,result);
        }else{
            assertNotNull(result);
        }
    }

    /**
     * Test of getSpecificReport method, of class MaintenanceSessionBean.
     */
    @Test
    public void testGetSpecificReport1() throws Exception {
        System.out.println("getSpecificReport1");
        Long maintenanceReportId = null;
        List<MaintenanceReportEntity> result = maintenanceSessionBeanRemote.getSpecificReport(maintenanceReportId);
        assertEquals(null, result);
    }
    
    @Test
    public void testGetSpecificReport2() throws Exception {
        System.out.println("getSpecificReport2");
        Long maintenanceReportId = Long.valueOf("1");
        List<MaintenanceReportEntity> result = maintenanceSessionBeanRemote.getSpecificReport(maintenanceReportId);
        assertNotNull(result);
    }

    /**
     * Test of searchAsset method, of class MaintenanceSessionBean.
     */
    @Test
    public void testSearchAsset1() throws Exception {
        System.out.println("searchAsset1");
        String assetId = "AS500";
        AssetEntity result = maintenanceSessionBeanRemote.searchAsset(assetId);
        assertTrue(result instanceof AdvertisementSpaceEntity);
        assertFalse(result instanceof RollingStockAssetEntity);
        assertFalse(result instanceof NodeAssetEntity);
        assertFalse(result instanceof TrainCarEntity);
        assertFalse(result instanceof LeasingSpaceEntity);
        assertFalse(result instanceof ConsumableAssetEntity);
        assertNotNull(result);
        assertEquals("Station", result.getAssetName());
    }
    
    @Test
    public void testSearchAsset2() throws Exception {
        System.out.println("searchAsset2");
        String assetId = null;
        AssetEntity result = maintenanceSessionBeanRemote.searchAsset(assetId);
        assertFalse(result instanceof AdvertisementSpaceEntity);
        assertFalse(result instanceof RollingStockAssetEntity);
        assertFalse(result instanceof NodeAssetEntity);
        assertFalse(result instanceof TrainCarEntity);
        assertFalse(result instanceof LeasingSpaceEntity);
        assertFalse(result instanceof ConsumableAssetEntity);
        assertEquals(null, result);
    }
    
    
    /**
     * Test of searchNodeAsset method, of class MaintenanceSessionBean.
     */
    @Test
    public void testSearchNodeAsset1() throws Exception {
        System.out.println("searchNodeAsset1");
        String assetId = "NA40";
        NodeAssetEntity result = maintenanceSessionBeanRemote.searchNodeAsset(assetId);
        assertTrue(result instanceof NodeAssetEntity);
        assertEquals("Ticketing Machine", result.getAssetName());
    }
    
    @Test
    public void testSearchNodeAsset2() throws Exception {
        System.out.println("searchNodeAsset2");
        String assetId = "AS40";
        NodeAssetEntity result = maintenanceSessionBeanRemote.searchNodeAsset(assetId);
        assertFalse(result instanceof NodeAssetEntity);
    }

    /**
     * Test of searchTrainCar method, of class MaintenanceSessionBean.
     */
    @Test
    public void testSearchTrainCar1() throws Exception {
        System.out.println("searchTrainCar1");
        String assetId = "AS2564";
        TrainCarEntity result = maintenanceSessionBeanRemote.searchTrainCar(assetId);
        assertTrue(result instanceof TrainCarEntity);
        assertNotNull(result);      
    }
    
    @Test
    public void testSearchTrainCar2() throws Exception {
        System.out.println("searchTrainCar2");
        String assetId = "NA00";
        TrainCarEntity result = maintenanceSessionBeanRemote.searchTrainCar(assetId);
        assertEquals(null, result);      
    }

    /**
     * Test of searchReport method, of class MaintenanceSessionBean.
     */
    
    @Test
    public void testSearchReport1() throws Exception {
        System.out.println("searchReport1");
        Long maintenanceReportId = Long.valueOf("1");
        MaintenanceReportEntity result = maintenanceSessionBeanRemote.searchReport(maintenanceReportId);
        assertNotNull(result);
    }
    
    @Test
    public void testSearchReport2() throws Exception {
        System.out.println("searchReport2");
        Long maintenanceReportId = null;
        MaintenanceReportEntity result = maintenanceSessionBeanRemote.searchReport(maintenanceReportId);
        assertEquals(null, result);
    }

    /**
     * Test of searchMaintenanceRequest method, of class MaintenanceSessionBean.
     */
    @Test
    public void testSearchMaintenanceRequest1() throws Exception {
        System.out.println("searchMaintenanceRequest1");
        String mainReqId = "MR1";
        MaintenanceRequestEntity result = maintenanceSessionBeanRemote.searchMaintenanceRequest(mainReqId);
        assertNotNull(result);
    }
    
    @Test
    public void testSearchMaintenanceRequest2() throws Exception {
        System.out.println("searchMaintenanceRequest2");
        String mainReqId = "POLTest";
        MaintenanceRequestEntity result = maintenanceSessionBeanRemote.searchMaintenanceRequest(mainReqId);
        assertEquals(null, result);
    }

    /**
     * Test of getAllAssets method, of class MaintenanceSessionBean.
     */
    @Test
    public void testGetAllAssets() throws Exception {
        System.out.println("getAllAssets");
        List<AssetEntity> result = maintenanceSessionBeanRemote.getAllAssets();
        assertNotNull(result);
    }
    
    private MaintenanceSessionBeanRemote lookupEventSession() 
    {
        try 
        {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (MaintenanceSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBean!maintenance.sessionbean.MaintenanceSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
