/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import infraasset.entity.StationEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import routefare.entity.NodeEntity;
import routefare.entity.RouteEntity;
import routefare.entity.SpecialDayAlgoEntity;
import routefare.entity.TripStationScheduleEntity;
import routefare.sessionbean.RoutePlanningSessionBeanLocal;

/**
 *
 * @author zhuming
 */
@Stateless
public class TrainScheduleSessionBean implements TrainScheduleSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private RoutePlanningSessionBeanLocal routePlanningSessionBeanLocal;
    @EJB
    private CalculatorSessionBeanLocal calculatorSessionBeanLocal;
    
    public StationEntity searchStationByCode(String code) {
        Query q = em.createQuery("SELECT s FROM StationEntity s");
        StationEntity s = new StationEntity();
        for (Object o : q.getResultList()) {
            s = (StationEntity) o;
            if (s.getCode().equals(code)) {
                break;
            }
        }
        return s;
    }
    
    public String getTerminatingStation (RouteEntity route){
        ArrayList<String> stations = routePlanningSessionBeanLocal.getNodes(route.getCode());
        NodeEntity terminatingStation = routePlanningSessionBeanLocal.searchNode(stations.get(stations.size()-2));
        return terminatingStation.getInfraName();
    }
    
    @Override
    public List<String> getRoutesFromStation(String code){
        StationEntity theStation= searchStationByCode(code);
        List<RouteEntity> routeEntities = new ArrayList<RouteEntity>(theStation.getRoutes());
        List<String> routes= new ArrayList<String>();
        for(int i=0; i<routeEntities.size(); i++){
            routes.add(routeEntities.get(i).getCode()+" "+getTerminatingStation(routeEntities.get(i)));
        }
        return routes;
    }
    
    @Override
    public ArrayList<TripStationScheduleEntity> calculateNextTwoTrains(Timestamp date, String dayType, Timestamp currentTime, String routeCode, String nodeCode){    
        Query q1 = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE (s.date=:date OR s.dayType=:dayType) AND s.status='emergency'");
        q1.setParameter("date", date);
        q1.setParameter("dayType", dayType);
        ArrayList<TripStationScheduleEntity> tripStationSchedules = new ArrayList<>();
        if (q1.getResultList().isEmpty()) {//no emergency
            Query q = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.route.code=:routeCode AND s.node.code=:nodeCode AND s.status='normal' AND (s.date=:date OR s.dayType=:dayType)");
            q.setParameter("routeCode", routeCode);
            q.setParameter("nodeCode", nodeCode);
            q.setParameter("date", date);
            q.setParameter("dayType", dayType);
            for (Object o : q.getResultList()) {
                TripStationScheduleEntity ts = (TripStationScheduleEntity) o;
                if(routePlanningSessionBeanLocal.isAfterCurrentTime(currentTime, ts.getArrivalTime())){
                tripStationSchedules.add(ts);
            }
            }
        } else {
            Query q = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.route.code=:routeCode AND s.node.code=:nodeCode AND s.status='emergency' AND s.date=:date ORDER BY s.arrivalTime ASC");
            q.setParameter("routeCode", routeCode);
            q.setParameter("nodeCode", nodeCode);
            q.setParameter("date", date);
            for (Object o : q.getResultList()) {
                TripStationScheduleEntity ts = (TripStationScheduleEntity) o;
                if(routePlanningSessionBeanLocal.isAfterCurrentTime(currentTime, ts.getArrivalTime())){
                tripStationSchedules.add(ts);
            }
            }
        }
        ArrayList<TripStationScheduleEntity> nextTwoTrains = new ArrayList<>();
        nextTwoTrains.add(tripStationSchedules.get(0));
        nextTwoTrains.add(tripStationSchedules.get(1));
        
        return nextTwoTrains;
    }
    
    @Override
    public List<TripStationScheduleEntity> printFirstTrain(String stationCode, Timestamp today){
        List<TripStationScheduleEntity> firstTrain = new ArrayList<TripStationScheduleEntity>();
        
        //get routes of the station
        StationEntity theStation= searchStationByCode(stationCode);
        List<RouteEntity> routeEntities = new ArrayList<RouteEntity>(theStation.getRoutes());
        List<String> routes= new ArrayList<String>();
        for(int i=0; i<routeEntities.size(); i++){
            routes.add(routeEntities.get(i).getCode());
        }
        //regular days
        ArrayList<String> dayTypes = new ArrayList<String>() {
                {
                    add("Weekday");
                    add("Saturday");
                    add("Sunday");
                }
            };
        //special days
        Query q = em.createQuery("SELECT DISTINCT s.date FROM SpecialDayAlgoEntity s WHERE s.date>=:today");
        q.setParameter("today", today);
        ArrayList<Timestamp> specialDays = new ArrayList<Timestamp>(q.getResultList());
//        /* Create set of String from List */
//        /* Set will store only unique values from listString */
//        Set<Timestamp> setString = new HashSet<Timestamp>(specialDays);
//        /* Clear the listString */
//        specialDays.clear();
//        /* Add setString(Unique) values back to listString */
//        specialDays.addAll(setString);
        System.out.println("how many special days: "+specialDays.size());
        for(int i=0;i<dayTypes.size();i++){
            for(int r=0; r<routes.size(); r++){
                Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss WHERE tss.node.code=:stationCode AND tss.route.code=:routeCode AND tss.dayType=:dayType ORDER BY tss.arrivalTime ASC");
                query.setParameter("stationCode", stationCode);
                query.setParameter("routeCode", routes.get(r));
                query.setParameter("dayType", dayTypes.get(i));
                firstTrain.add((TripStationScheduleEntity)query.getResultList().get(0));
        }
        }
        System.out.println("first trains of regular days: "+firstTrain.size());
        for(int k=0; k<specialDays.size();k++){
            for(int r=0; r<specialDays.size(); r++){
                Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss WHERE tss.node.code=:stationCode AND tss.route.code=:routeCode AND tss.date=:date ORDER BY tss.arrivalTime ASC");
                query.setParameter("stationCode", stationCode);
                query.setParameter("routeCode", routes.get(r));
                query.setParameter("date", specialDays.get(k));
                firstTrain.add((TripStationScheduleEntity)query.getResultList().get(0));
        }
        }
            return firstTrain;
    }

    @Override
    public List<TripStationScheduleEntity> printLastTrain(String stationCode, Timestamp today){
        List<TripStationScheduleEntity> lastTrain = new ArrayList<TripStationScheduleEntity>();
        
        //get routes of the station
        StationEntity theStation= searchStationByCode(stationCode);
        List<RouteEntity> routeEntities = new ArrayList<RouteEntity>(theStation.getRoutes());
        List<String> routes= new ArrayList<String>();
        for(int i=0; i<routeEntities.size(); i++){
            routes.add(routeEntities.get(i).getCode());
        }
        //regular days
        ArrayList<String> dayTypes = new ArrayList<String>() {
                {
                    add("Weekday");
                    add("Saturday");
                    add("Sunday");
                }
            };
        //special days
        Query q = em.createQuery("SELECT DISTINCT s.date FROM SpecialDayAlgoEntity s WHERE s.date>=:today");
        q.setParameter("today", today);
        ArrayList<Timestamp> specialDays = new ArrayList<Timestamp>(q.getResultList());
//        //special days
//        Query q = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s WHERE s.date>=:today");
//        ArrayList<Timestamp> specialDays = new ArrayList<Timestamp>();
//        for (Object o : q.getResultList()) {
//            SpecialDayAlgoEntity s = (SpecialDayAlgoEntity) o;
//            specialDays.add(s.getDate());
//        }
//        /* Create set of String from List */
//        /* Set will store only unique values from listString */
//        Set<Timestamp> setString = new HashSet<Timestamp>(specialDays);
//        /* Clear the listString */
//        specialDays.clear();
//        /* Add setString(Unique) values back to listString */
//        specialDays.addAll(setString);
        
        for(int i=0;i<dayTypes.size();i++){
            for(int r=0; r<routes.size(); r++){
                Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss WHERE tss.node.code=:stationCode AND tss.route.code=:routeCode AND tss.dayType=:dayType ORDER BY tss.arrivalTime DESC");
                query.setParameter("stationCode", stationCode);
                query.setParameter("routeCode", routes.get(r));
                query.setParameter("dayType", dayTypes.get(i));
                lastTrain.add((TripStationScheduleEntity)query.getResultList().get(0));
        }
        }
        for(int k=0; k<specialDays.size();k++){
            for(int r=0; r<specialDays.size(); r++){
                Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss WHERE tss.node.code=:stationCode AND tss.route.code=:routeCode AND tss.date=:date ORDER BY tss.arrivalTime DESC");
                query.setParameter("stationCode", stationCode);
                query.setParameter("routeCode", routes.get(r));
                query.setParameter("date", specialDays.get(k));
                lastTrain.add((TripStationScheduleEntity)query.getResultList().get(0));
        }
        }
            return lastTrain;
    }
    
}
