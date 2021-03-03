package commoninfra.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import commoninfra.entity.MessageEntity;
import commoninfra.entity.StaffEntity;
import javax.ejb.Stateful;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author FABIAN
 */
@Stateful
public class MessageSessionBean implements MessageSessionBeanLocal, MessageSessionBeanRemote {

    @PersistenceContext()
    EntityManager em;

    public MessageSessionBean() {
    }

    private MessageEntity messageEntity;

    @Override
    public String sendMessage(String staffId, String recId, String msgContent) {

        //Date object
        Date date = new Date();
        //getTime() returns time
        long time = date.getTime();
        //Passed the time into constructor of Timestamp class
        Timestamp messageDateTime = new Timestamp(time);
        String senderName = "";

        StaffEntity s = new StaffEntity() {
        };
        Query q = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        if (q.getResultList().isEmpty()) {
            return "fail";
        }

        for (Object o : q.getResultList()) {
            StaffEntity se = (StaffEntity) o;
            senderName = se.getFirstName() + " " + se.getLastName();
        }

        String[] receiverId = recId.split(" ");
        String recId2 = receiverId[0];
        String receiverName = receiverId[1];

        messageEntity = new MessageEntity(msgContent, messageDateTime, recId2, receiverName, staffId, senderName);
        em.persist(messageEntity);

        return "sent";
    }

    @Override
    public List<StaffEntity> getReceivers() {

        Query q = em.createQuery("SELECT s FROM StaffEntity s");
        return q.getResultList();

    }

    @Override
    public List<MessageEntity> getConversation(String staffId, String recId) {

        Query q = em.createQuery("SELECT m FROM MessageEntity AS m WHERE ((m.sender=:staffId AND m.receiver=:recId) OR (m.sender=:recId AND m.receiver=:staffId))");
        q.setParameter("staffId", staffId);
        q.setParameter("recId", recId);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public List<MessageEntity> getEntireConversation(String staffId) {

        Query q1 = em.createQuery("SELECT m FROM MessageEntity AS m WHERE m.receiver=:staffId");
        q1.setParameter("staffId", staffId);
        if (q1.getResultList().isEmpty()) {
            return null;
        } else {
            return q1.getResultList();
        }
    }

}// end of class
