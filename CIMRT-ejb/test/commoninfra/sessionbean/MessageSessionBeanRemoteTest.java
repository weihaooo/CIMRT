/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.sessionbean;

import commoninfra.entity.MessageEntity;
import commoninfra.entity.StaffEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
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
public class MessageSessionBeanRemoteTest {
    
    MessageSessionBeanRemote msbr = lookupEventSession();
    
    public MessageSessionBeanRemoteTest() {
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
     * Test of sendMessage method, of class MessageSessionBean.
     */
    @Test
    public void testSendMessageSuccess() throws Exception {
        System.out.println("sendMessageSuccess");
        String staffId = "D000001";
        String recId = "S000001 Fabian";
        String msgContent = "Hello testing";
        String result = msbr.sendMessage(staffId, recId, msgContent);
        assertEquals("sent", result);
    }
    
    @Test
    public void testSendMessageFail() throws Exception {
        System.out.println("sendMessageFail");
        String staffId = "iloveis4103";
        String recId = "D000001 ZhuMing";
        String msgContent = "Hello testing";
        String result = msbr.sendMessage(staffId, recId, msgContent);
        assertEquals("fail", result);
    }

    /**
     * Test of getReceivers method, of class MessageSessionBean.
     */
    /*@Test
    public void testGetReceivers() throws Exception {
        System.out.println("getReceivers");
        List<StaffEntity> result = msbr.getReceivers();
        assertNotNull(result);
    }*/

    /**
     * Test of getConversation method, of class MessageSessionBean.
     */
    @Test
    public void testGetConversationSuccess() throws Exception {
        System.out.println("getConversationSuccess");
        String staffId = "S000001";
        String recId = "D000001";
        List<MessageEntity> result = msbr.getConversation(staffId, recId);
        assertNotNull(result);
    }
    
    @Test
    public void testGetConversationFail() throws Exception {
        System.out.println("getConversationFail");
        String staffId = "iloveis4103";
        String recId = "D000001";
        List<MessageEntity> result = msbr.getConversation(staffId, recId);
        assertEquals(null,result);
    }

    /**
     * Test of getEntireConversation method, of class MessageSessionBean.
     */
    @Test
    public void testGetEntireConversationSuccess() throws Exception {
        System.out.println("getEntireConversationSuccess");
        String staffId = "D000001";
        List<MessageEntity> result = msbr.getEntireConversation(staffId);
        assertNotNull(result);
    }
    
    @Test
    public void testGetEntireConversationFail() throws Exception {
        System.out.println("getEntireConversationFail");
        String staffId = "iloveis4103";
        List<MessageEntity> result = msbr.getEntireConversation(staffId);
        assertEquals(null,result);
    }
    
    private MessageSessionBeanRemote lookupEventSession() {
        try {
            Context c = new InitialContext();
            //return (MaintenanceSessionBeanRemoteRemote) c.lookup("java:global/CIMRT-ejb/MaintenanceSessionBeanRemote");
            return (MessageSessionBeanRemote) c.lookup("java:global/CIMRT-ejb/MessageSessionBean!commoninfra.sessionbean.MessageSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
