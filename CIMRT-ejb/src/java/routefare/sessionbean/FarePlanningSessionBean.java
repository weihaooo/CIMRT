/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.sessionbean;

import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import routefare.entity.FareAlgoEntity;

/**
 *
 * @author zhuming
 */
@Stateless
public class FarePlanningSessionBean implements FarePlanningSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    public FarePlanningSessionBean() {
    }

    @Override
    public boolean updateFareStructure(String passengerType, String fareType, double newBaseFee, double newIncrementRate) {
        Query q = em.createQuery("SELECT f FROM FareAlgoEntity f WHERE f.passengerType=:passengerType AND f.fareType=:fareType");
        q.setParameter("passengerType", passengerType);
        q.setParameter("fareType", fareType);
        FareAlgoEntity fs = (FareAlgoEntity) q.getResultList().get(0);
        if (fs == null) {
            return false;
        } else {
            fs.setBaseFee(newBaseFee);
            fs.setIncrementRate(newIncrementRate);
            em.flush();
            return true;
        }

    }

    @Override
    public ArrayList<FareAlgoEntity> getFareAlgo() {
        ArrayList<FareAlgoEntity> fareList = new ArrayList<FareAlgoEntity>();
        Query query = em.createQuery("SELECT a FROM FareAlgoEntity a WHERE a.fareType != 'Concession' ORDER BY a.fareType ASC");
        for (Object o : query.getResultList()) {
            fareList.add((FareAlgoEntity) o);
        }
        return fareList;
    }

    @Override
    public ArrayList<FareAlgoEntity> getFareAlgoConcession() {
        ArrayList<FareAlgoEntity> fareList = new ArrayList<FareAlgoEntity>();
        Query query = em.createQuery("SELECT a FROM FareAlgoEntity a WHERE a.fareType = 'Concession' ORDER BY a.fareAlgoId ASC");
        for (Object o : query.getResultList()) {
            fareList.add((FareAlgoEntity) o);
        }
        return fareList;
    }

    @Override
    public ArrayList<String> getFareType(String passengerType) {
        ArrayList<String> fares = new ArrayList<String>();
        if (passengerType.equals("Adult") || passengerType.equals("Short-term Visitor")) {
            fares.add("Peak");
            fares.add("Off Peak");
            fares.add("Concession");
        } else if (passengerType.equals("Student") || passengerType.equals("Senior Citizen")) {
            fares.add("Off Peak");
            fares.add("Concession");
        }
        return fares;
    }
    @Override
    public double getBaseFee(String pType, String fType){
        Query q = em.createQuery("SELECT f FROM FareAlgoEntity f WHERE f.passengerType=:passengerType AND f.fareType=:fareType");
        q.setParameter("passengerType", pType);
        q.setParameter("fareType", fType);
        FareAlgoEntity fs = (FareAlgoEntity) q.getResultList().get(0);
        double baseFee;
        baseFee = fs.getBaseFee();
        return baseFee;
    }
    @Override
    public double getIncrementalFee(String pType, String fType){
        Query q = em.createQuery("SELECT f FROM FareAlgoEntity f WHERE f.passengerType=:passengerType AND f.fareType=:fareType");
        q.setParameter("passengerType", pType);
        q.setParameter("fareType", fType);
        FareAlgoEntity fs = (FareAlgoEntity) q.getResultList().get(0);
        double iFee;
        iFee = fs.getIncrementRate();
        return iFee;
    }
}
