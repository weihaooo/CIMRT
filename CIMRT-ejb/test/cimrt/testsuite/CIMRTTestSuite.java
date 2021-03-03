package cimrt.testsuite;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author FABIAN
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({commoninfra.sessionbean.AccountSessionBeanRemoteTest.class, commoninfra.sessionbean.MessageSessionBeanRemoteTest.class,
    commoninfra.sessionbean.SystemAdminSessionBeanRemoteTest.class, maintenance.sessionbean.MaintenanceSessionBeanRemoteTest.class,
    manpower.sessionbean.LeaveApplicationSessionBeanRemoteTest.class, manpower.sessionbean.ManpowerSessionBeanRemoteTest.class,
    manpower.sessionbean.RosterSessionBeanRemoteTest.class, operations.sessionbean.OperationsSessionBeanRemoteTest.class})
public class CIMRTTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
