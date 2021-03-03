/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.sessionbean;

import commoninfra.entity.StaffEntity;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
public class AccountSessionBeanRemoteTest {
    
    AccountSessionBeanRemote asbr = lookupEventSession();
    
    public AccountSessionBeanRemoteTest() {
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
     * Test of doLogin method, of class AccountSessionBeanRemote.
     */
    @Test
    public void testDoLoginSuccess() {
        System.out.println("doLoginSuccess");
        String username = "D000001";
        String password = "default1";
        int result = asbr.doLogin(username, password);
        assertEquals(Integer.parseInt("5"), result);
    }
    
    @Test
    public void testDoLoginFail() {
        System.out.println("doLoginFail");
        String username = "D000001";
        String password = null;
        int result = asbr.doLogin(username, password);
        assertEquals(Integer.parseInt("6"), result);
    }

    /**
     * Test of resetPassword method, of class AccountSessionBeanRemote.
     */
    @Test
    public void testResetPasswordPass() {
        System.out.println("resetPasswordPass");
        String staffId = "HQ000001";
        String nric = "nric0";
        boolean result = asbr.resetPassword(staffId, nric);
        assertTrue(result);
    }
    
    @Test
    public void testResetPasswordFail() {
        System.out.println("resetPasswordFail");
        String staffId = "HQ000001";
        String nric = null;
        boolean result = asbr.resetPassword(staffId, nric);
        assertFalse(result);
    }

    /**
     * Test of retrieveStaffId method, of class AccountSessionBeanRemote.
     */
    @Test
    public void testRetrieveStaffIdSuccess() {
        System.out.println("retrieveStaffIdSuccess");
        String nric = "nric0";
        boolean result = asbr.retrieveStaffId(nric);
        assertTrue(result);
    }
    
    @Test
    public void testRetrieveStaffIdFail() {
        System.out.println("retrieveStaffIdFail");
        String nric = null;
        boolean result = asbr.retrieveStaffId(nric);
        assertFalse(result);
    }

    /**
     * Test of viewProfile method, of class AccountSessionBeanRemote.
     */
    @Test
    public void testViewProfileOK() {
        System.out.println("viewProfileOK");
        String staffId = "S000001";
        ArrayList<String> result = asbr.viewProfile(staffId);
        assertNotNull(result);
    }
    
    @Test
    public void testViewProfileFail() {
        System.out.println("viewProfileFail");
        String result="";
        String staffId = null;
        ArrayList<String> temp = asbr.viewProfile(staffId);
        if(temp.isEmpty()){
            result = null;
        }
        assertEquals(null,result);
    }

    /**
     * Test of editProfile method, of class AccountSessionBeanRemote.
     */
    @Test
    public void testEditProfileSuccess() {
        System.out.println("editProfileSuccess");
        ArrayList<String> details = new ArrayList<String>();
        details.add("S000002");
        details.add("91000000");
        details.add("e0002252@u.nus.edu");
        details.add("NUS Computing");
        details.add("Widowed");
        details.add("Buddhist");
        details.add("Bachelor's Degree");
        boolean result = asbr.editProfile(details);
        assertTrue(result);
    }
    
    @Test
    public void testEditProfileFail() {
        System.out.println("editProfileFail");
        ArrayList<String> details = new ArrayList<String>();
        boolean result = asbr.editProfile(details);
        assertFalse(result);
    }

    /**
     * Test of changePassword method, of class AccountSessionBeanRemote.
     */
    /*@Test
    public void testChangePasswordSuccess() {
        System.out.println("changePasswordSuccess");
        String staffId = "S000010";
        StaffEntity s = asbr.searchStaff(staffId);
        String password = s.getPassword();
        String newPW = s.getPassword() + "Test";
        String newPW2 = s.getPassword() + "Test";
        boolean result = asbr.changePassword(staffId, password, newPW, newPW2);
        assertTrue(result);
    }*/
    
    @Test
    public void testChangePasswordFail() {
        System.out.println("changePasswordFail");
        String staffId = "D000150";
        String password = null;
        String newPW = "happyboy";
        String newPW2 = "happyboy";
        boolean result = asbr.changePassword(staffId, password, newPW, newPW2);
        assertFalse(result);
    }
    
    @Test
    public void testChangePasswordNewPwNoMatch() {
        System.out.println("changePasswordNewPwNoMatch");
        String staffId = "D000150";
        String password = "default1";
        String newPW = "hello";
        String newPW2 = "ilove4103";
        boolean result = asbr.changePassword(staffId, password, newPW, newPW2);
        assertFalse(result);
    }

    /**
     * Test of checkCaptcha method, of class AccountSessionBeanRemote.
     */
    @Test
    public void testCheckCaptcha1() {
        System.out.println("checkCaptcha1");
        String staffId = null;
        boolean result = asbr.checkCaptcha(staffId);
        assertFalse(result);
    }
    
    /**
     * Test of searchStaff method, of class AccountSessionBeanRemote.
     */
    @Test
    public void testSearchStaff1() {
        System.out.println("searchStaff1");
        String staffId = "HQ000020";
        StaffEntity result = asbr.searchStaff(staffId);
        assertNotNull(result);
    }
    
    @Test
    public void testSearchStaff2() {
        System.out.println("searchStaff2");
        String staffId = null;
        StaffEntity result = asbr.searchStaff(staffId);
        assertEquals(null, result);
    }

    
     private AccountSessionBeanRemote lookupEventSession() 
    {
        try 
        {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (AccountSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/AccountSessionBean!commoninfra.sessionbean.AccountSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
