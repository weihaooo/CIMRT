package commoninfra.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import commoninfra.entity.MessageEntity;
import commoninfra.entity.StaffEntity;
import java.sql.Timestamp;
import javax.ejb.Local;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author FABIAN
 */
@Local
public interface MessageSessionBeanLocal {
    
    public String sendMessage(String staffId, String recId, String msgContent);
    public List<StaffEntity> getReceivers();
    public List<MessageEntity> getConversation(String staffId, String recId);
    public List<MessageEntity> getEntireConversation(String staffId);
    
}
