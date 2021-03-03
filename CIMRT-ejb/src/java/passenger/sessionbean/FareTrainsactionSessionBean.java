/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import infraasset.entity.StationEntity;
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
import routefare.entity.FareTransactionEntity;
import routefare.entity.TrainScheduleEntity;

/**
 *
 * @author Yuting
 */
@Stateless
public class FareTrainsactionSessionBean implements FareTrainsactionSessionBeanLocal {

    @PersistenceContext(unitName = "CIMRT-ejbPU")
    private EntityManager em;

    @EJB
    private CalculatorSessionBeanLocal calculatorSessionBeanLocal;

    @Override
    public ArrayList<String> getStations() {
        ArrayList<String> stations = new ArrayList<String>();
        List<String> routesString = calculatorSessionBeanLocal.getRoutes();
        for (int i = 0; i < routesString.size(); i++) {
            List<StationEntity> stationsOfRoute = calculatorSessionBeanLocal.getStationsByRoute(routesString.get(i));
            String route = routesString.get(i);
            for (int j = 0; j < stationsOfRoute.size(); j++) {
                String station = route + " - " + stationsOfRoute.get(j).getInfraName();
                stations.add(station);
            }
        }
        return stations;
    }

    @Override
    public String createFareTransaction(String cardId, String station) {
        CardEntity card = new CardEntity();
        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        ArrayList<CardEntity> result = (ArrayList<CardEntity>) q.getResultList();
        if (!result.isEmpty()) {
            card = result.get(0);
        }

        Query q1 = em.createQuery("SELECT f FROM FareTransactionEntity AS f WHERE f.card=:card");
        q1.setParameter("card", card);
        List<FareTransactionEntity> result1 = (List<FareTransactionEntity>) q1.getResultList();

        boolean status = false;
        int index = 0;

        for (int i = 0; i < result1.size(); i++) {
            if (result1.get(i).getEndStation() == null) {
                status = true;
                index = i;
                break;
            }
        }

        StationEntity start = new StationEntity();
        StationEntity end = new StationEntity();
        if (status) { // have not tap out
            FareTransactionEntity tapOut = result1.get(index);
            //find end station
            Query q2 = em.createQuery("SELECT s FROM StationEntity AS s WHERE s.infraName=:station");
            q2.setParameter("station", station);
            List<StationEntity> result2 = (List<StationEntity>) q2.getResultList();
            if (!result2.isEmpty()) {
                end = result2.get(0);
            }
            //tap out time
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            //day type & date
            String dayOfWeek;
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dayOfWeek = "Sunday";
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                dayOfWeek = "Saturday";
            } else {
                dayOfWeek = "Weekday";
            }
            Calendar call = Calendar.getInstance();
            call.setTime(today);
            call.set(Calendar.HOUR_OF_DAY, 0);
            call.set(Calendar.MINUTE, 0);
            call.set(Calendar.SECOND, 0);
            call.set(Calendar.MILLISECOND, 0);
            today = call.getTime();
            Timestamp date1 = new java.sql.Timestamp(today.getTime());

            //passenger type
            String passenger = card.getType();
            double tripFare = calculatorSessionBeanLocal.calculateTripFare(date1, dayOfWeek, currentTime, passenger, tapOut.getStartStation().getInfraName(), station);
            System.out.println("trip amount = " + tripFare);
            tapOut.setAmount(tripFare);
            tapOut.setEndTime(currentTime);
            tapOut.setEndStation(end);
            double preAmt = card.getAmount();
            double currAmt = preAmt - tripFare;
            card.setAmount(currAmt);
//            
//            Query query = em.createQuery("SELECT ts FROM TrainScheduleEntity AS ts WHERE ts.dayType=:dayOfWeek");
//            query.setParameter("dayOfWeek", dayOfWeek);
//            List<TrainScheduleEntity> result3 = (List<TrainScheduleEntity>)query.getResultList();
//            int trainScheduleIndex=0;
//            if (!result3.isEmpty()) {
//                for(int k = 0; k<result3.size();k++){
//                    Timestamp pStart = result3.get(k).getPeriodStart();
//                    Timestamp pEnd = result3.get(k).getPeriodEnd();
//                    int compareStart = compareTimes(tapOut.getStartTime(),pStart);
//                    int compareEnd = compareTimes(tapOut.getEndTime(),pEnd);
//                    if(compareStart>0 && compareEnd<0){
//                    trainScheduleIndex=k;
//                    break;
//                }
//                }
//            }
//            
//            //get train schedule entity
//            TrainScheduleEntity ts = result3.get(trainScheduleIndex);
//            //get period type
//            String periodType = ts.getPeriodType();

            em.merge(card);
            em.merge(tapOut);
            em.flush();
            return String.valueOf(tripFare);
        } else { //create new transaction
            if (card.getAmount() < 5) {
                return "Insufficient amount";
            } else {
                Query q2 = em.createQuery("SELECT s FROM StationEntity AS s WHERE s.infraName=:station");
                q2.setParameter("station", station);
                List<StationEntity> result2 = (List<StationEntity>) q2.getResultList();
                if (!result2.isEmpty()) {
                    start = result2.get(0);
                }
                Timestamp date = new Timestamp(System.currentTimeMillis());
                FareTransactionEntity tapIn = new FareTransactionEntity(0, start, null, date, null);
                em.persist(tapIn);
                tapIn.setCard(card);
                card.getFareTransactions().add(tapIn);
                em.flush();
                return "Tap in";
            }
        }
    }

    public int compareTimes(Timestamp d1, Timestamp d2) {
        int t1;
        int t2;

        t1 = (int) (d1.getTime() % (24 * 60 * 60 * 1000L));
        t2 = (int) (d2.getTime() % (24 * 60 * 60 * 1000L));
        return (t1 - t2);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
