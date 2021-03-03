/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import passenger.entity.PassengerEntity;
import passenger.entity.PassengerFeedbackEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author FABIAN
 */
@Stateless
public class PassengerFeedbackSessionBean implements PassengerFeedbackSessionBeanLocal {

    @PersistenceContext()
    EntityManager em;
    
    PassengerFeedbackEntity passengerFeedback;
    NodeEntity nodeEntity;
    
    public PassengerFeedbackSessionBean() {
    }

    @Override
    public boolean submitFeedback(String description, String submitter, String subject, int mobileNo, String selectedStation) {
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        passengerFeedback = new PassengerFeedbackEntity(description, time ,submitter, subject, mobileNo, selectedStation);
        em.persist(passengerFeedback);
        em.flush();
        return true;
    }
    
    @Override
    public List<NodeEntity> getStations(){
        Query q = em.createQuery("SELECT n FROM NodeEntity AS n");
        q.getResultList();
        List<NodeEntity> temp = new ArrayList<NodeEntity>();
        temp = q.getResultList();
        
        return temp;
    }
    
    @Override
    public PassengerEntity searchPassenger(String username){
        Query q = em.createQuery("SELECT p FROM PassengerEntity AS p WHERE p.username=:username");
        q.setParameter("username",username);
        List<PassengerEntity> tempList = q.getResultList();
        PassengerEntity p = tempList.get(0);
        
        return p;
    }
    
    @Override
    public List<PassengerFeedbackEntity> getPassengerFeedbacks(String address){
        Query q = em.createQuery("SELECT p FROM PassengerFeedbackEntity AS p WHERE p.address=:address");
        q.setParameter("address",address);
        List<PassengerFeedbackEntity> temp = q.getResultList();
        
        return temp;
    }
    
    @Override
    public List<PassengerFeedbackEntity> getAllFeedbacks(){
        Query q = em.createQuery("SELECT p FROM PassengerFeedbackEntity AS p");
        List<PassengerFeedbackEntity> temp = q.getResultList();
        
        return temp;
    }
    
}//end of class
