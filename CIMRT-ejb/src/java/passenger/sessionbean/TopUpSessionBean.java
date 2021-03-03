/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import passenger.entity.CardEntity;
import passenger.entity.PassengerEntity;
import operations.entity.TopUpTransactionEntity;
import routefare.entity.FareAlgoEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author yuting
 */
@Stateless
public class TopUpSessionBean implements TopUpSessionBeanLocal {

    @PersistenceContext(unitName = "CIMRT-ejbPU")
    private EntityManager em;
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    @Override
    public int doLogin(String staffId, String password) {
        StationStaffEntity staff;
        StringBuffer sb = new StringBuffer();
        password = "ES01" + password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }

            staff = searchStaff(staffId);

            //Login 6: No such station user 
            //      0: Account locked
            //    1-4: Wrong password
            //      5: login successful
            if (staff == null) {
                return 6;
            } else {

                if (!(staff.getPassword().equals(sb.toString())) || staff.getAccountLock() == true) {
                    if (staff.getAccountLock() == true) {
                        return 0;
                    } else if (!(staff.getPassword().equals(sb.toString()))) {

                        staff.setWrongPasswordCount(staff.getWrongPasswordCount() + 1);
                        em.flush();
                        if (staff.getWrongPasswordCount() > 4) {
                            staff.setAccountLock(true);
                        }
                        return 5 - staff.getWrongPasswordCount();
                    }
                } else {

                    staff.setWrongPasswordCount(0);
                    return 5;
                }
            }

        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
        return 6;
    }

    public StationStaffEntity searchStaff(String staffId) {
        Query q = em.createQuery("SELECT s FROM StationStaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        ArrayList<StationStaffEntity> result = (ArrayList<StationStaffEntity>) q.getResultList();
        StationStaffEntity staff;
        if (result.isEmpty()) {
            return null;
        } else {
            staff = result.get(0);
            return staff;
        }
    }

    @Override
    public Date getConcessionEndDate(String cardId) {
        Date end = new Date();
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            end = result.get(0).getConcessionEnd();
        }
        return end;
    }
    
    @Override
    public boolean isExpired(String cardId){
        boolean isExpired = false;
        Date today = new Date();
        Date expiryDate;
        double amount = 0;
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (result.isEmpty()) {
            return true;
        } else {
            expiryDate = result.get(0).getExpiryDate();
        }
        if(expiryDate.before(today)){
            isExpired = true;
        }
        return isExpired;
    }
    
    @Override
    public double getAmount(String cardId){
       
        String type;
        double amount = 0;
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (result.isEmpty()) {
            return 0;
        } else {
            type = result.get(0).getType();
        }
        
        //retrieve amount from FareAlgo
        Query q1 = em.createQuery("SELECT f FROM FareAlgoEntity AS f WHERE f.passengerType=:type AND f.fareType='Concession'");
        q1.setParameter("type", type);
        List<FareAlgoEntity> result1 = q1.getResultList();
        if(result.isEmpty()){
            return 0;
        }
        else{
            amount = result1.get(0).getBaseFee();
        }
        return amount;
    }
    @Override
    public void createTopUpTransaction(String type, double amount, Date transDate, String staffId, Date start, String cardId) {
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        CardEntity card;
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (!result.isEmpty()) {
            card = result.get(0);
            if (start != null) {
                card.setConcessionStart(start);
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                cal.add(Calendar.MONTH, 1);
                card.setConcessionEnd(cal.getTime());
                em.flush();
            }
        } else {
            card = null;
        }
        Query q1 = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        StaffEntity staff;
        ArrayList<StaffEntity> result1 = (ArrayList<StaffEntity>) q1.getResultList();
        if (!result1.isEmpty()) {
            staff = result1.get(0);
        } else {
            staff = null;
        }
        ArrayList<String> staffDetails = accountSessionBeanLocal.viewProfile(staffId);
        String nodeCode;
        if (staffDetails.size() == 19) {
            nodeCode = staffDetails.get(18);
        } else {
            nodeCode = null;
        }
        Query q2 = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:nodeCode");
        q2.setParameter("nodeCode", nodeCode);
        NodeEntity node;
        List<NodeEntity> result2 = q2.getResultList();
        if (!result2.isEmpty()) {
            node = result2.get(0);
        } else {
            node = null;
        }
        TopUpTransactionEntity trans = new TopUpTransactionEntity(type, amount, new Timestamp(transDate.getTime()), staff, card, node);
        em.persist(trans);
    }
    
    @Override
    public void addValueTransaction(String type, double amount, Date transDate, String staffId, String cardId){
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        CardEntity card;
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (!result.isEmpty()) {
            card = result.get(0);
            card.setAmount(amount);
            em.persist(card);
        } else {
            card = null;
        }
        Query q1 = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        StaffEntity staff;
        ArrayList<StaffEntity> result1 = (ArrayList<StaffEntity>) q1.getResultList();
        if (!result1.isEmpty()) {
            staff = result1.get(0);
        } else {
            staff = null;
        }
        ArrayList<String> staffDetails = accountSessionBeanLocal.viewProfile(staffId);
        String nodeCode;
        if (staffDetails.size() == 19) {
            nodeCode = staffDetails.get(18);
        } else {
            nodeCode = null;
        }
        Query q2 = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:nodeCode");
        q2.setParameter("nodeCode", nodeCode);
        NodeEntity node;
        List<NodeEntity> result2 = q2.getResultList();
        if (!result2.isEmpty()) {
            node = result2.get(0);
        } else {
            node = null;
        }
        TopUpTransactionEntity trans = new TopUpTransactionEntity(type, amount, new Timestamp(transDate.getTime()), staff, card, node);
        em.persist(trans);
    }
    
    @Override
    public String getRefundAmt(String cardId){
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (result.isEmpty()) {
            return "";
        } else {
            return String.valueOf(result.get(0).getAmount());
        }
    }
    
    @Override
    public void refund(double amount, Date transDate, String staffId, String cardId){
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        CardEntity card;
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (!result.isEmpty()) {
            card = result.get(0);
            card.setAmount(0);
            card.setPassenger(null);
            em.persist(card);
        } else {
            card = null;
        }
        Query q1 = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        StaffEntity staff;
        ArrayList<StaffEntity> result1 = (ArrayList<StaffEntity>) q1.getResultList();
        if (!result1.isEmpty()) {
            staff = result1.get(0);
        } else {
            staff = null;
        }
        ArrayList<String> staffDetails = accountSessionBeanLocal.viewProfile(staffId);
        String nodeCode;
        if (staffDetails.size() == 19) {
            nodeCode = staffDetails.get(18);
        } else {
            nodeCode = null;
        }
        Query q2 = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:nodeCode");
        q2.setParameter("nodeCode", nodeCode);
        NodeEntity node;
        List<NodeEntity> result2 = q2.getResultList();
        if (!result2.isEmpty()) {
            node = result2.get(0);
        } else {
            node = null;
        }
        TopUpTransactionEntity trans = new TopUpTransactionEntity("Refund", amount*(-1), new Timestamp(transDate.getTime()), staff, card, node);
        em.persist(trans);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
