/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import infraasset.entity.StationEntity;
import static java.lang.Math.abs;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import routefare.entity.FareAlgoEntity;
import routefare.entity.RouteEntity;
import routefare.entity.SpecialDayAlgoEntity;
import routefare.entity.TrainScheduleEntity;
import routefare.sessionbean.RoutePlanningSessionBeanLocal;

/**
 *
 * @author zhuming
 */
@Stateless
public class CalculatorSessionBean implements CalculatorSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private RoutePlanningSessionBeanLocal routePlanningSessionBeanLocal;

    public CalculatorSessionBean() {

    }
    
    @Override
    public List<String> getPassengerTypeList(){
        List<String> passengerTypes = new ArrayList<>();
        passengerTypes.add("Adult");
        passengerTypes.add("Student");
        passengerTypes.add("Senior Citizen");
        
        return passengerTypes;
    }
    
    @Override
    public List<String> getRoutes(){
        Query q = em.createQuery("SELECT r FROM RouteEntity r");
        RouteEntity r = new RouteEntity();
        List<String> routes = new ArrayList<String>();
        for (Object o : q.getResultList()) {
            r = (RouteEntity) o;
            routes.add(r.getCode().substring(0, 3));
        }
        /* Create set of String from List */
        /* Set will store only unique values from listString */
        Set<String> setString = new HashSet<String>(routes);
        /* Clear the listString */
        routes.clear();
        /* Add setString(Unique) values back to listString */
        routes.addAll(setString);
        return routes;
    }
    
    @Override
    public List<StationEntity> getStationsByRoute(String route){//this route only contains the first 3 chars of the route names: NSL/EWL
        
        Query q = em.createQuery("SELECT s FROM StationEntity s ORDER BY s.infraName ASC");
        List<StationEntity> stationList = new ArrayList<>();
        for (Object o : q.getResultList()) {
            StationEntity s = (StationEntity) o;
            if(s.getCode().substring(0, 3).equals(route)){
                stationList.add(s);
            }
        }
        return stationList;
    }
    
    public List<StationEntity> searchStationByName(String stationName) {
        Query q = em.createQuery("SELECT s FROM StationEntity s WHERE s.infraName=:infraName ORDER BY s.code");
        q.setParameter("infraName", stationName);
        List<StationEntity> stations = new ArrayList<StationEntity>();
        for (Object o : q.getResultList()) {
            stations.add((StationEntity) o);
        }
        return stations;
    }
    
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
      
    //find the nearest interchange station from start point
    public String findFirstIntersection(StationEntity startNode){
        StationEntity myStation = startNode;
        boolean loop = true;
        while(loop && myStation.getNextStation()!=null){
            myStation = searchStationByCode(myStation.getNextStation());
            if(searchStationByName(myStation.getInfraName()).size()>1){//is intersection station
                loop = false;
            }
        }
        if(loop == true){//didn't find the interchange station
            myStation = startNode;
            while(loop && myStation.getPreviousStation()!=null){
                myStation = searchStationByCode(myStation.getPreviousStation());
                if(searchStationByName(myStation.getInfraName()).size()>1){//is intersection station
                loop = false;
            }
            }
        }
        return myStation.getInfraName();
    }
    
    @Override
    public double calculateDistance(String startPt, String endPt) {
        List<StationEntity> startNodeList = searchStationByName(startPt);
        List<StationEntity> endNodeList = searchStationByName(endPt);
        StationEntity startNode = new StationEntity();
        StationEntity endNode = new StationEntity();
        double distance = 0;

        if (startNodeList.size() == 1 && startNodeList.size() == endNodeList.size()) {//both of start and end stations are NOT intersections
            startNode = startNodeList.get(0);
            endNode = endNodeList.get(0);
            if ((startNode.getCode().substring(0, 3)).equals(endNode.getCode().substring(0, 3))) {//same route: NSL/EWL
                distance = abs(startNode.getDistanceToFirstStation() - endNode.getDistanceToFirstStation());
            } else {//different route
                List<StationEntity> intersectionNodeList = searchStationByName(findFirstIntersection(startNode));
                StationEntity intersectionNode1 = new StationEntity();
                StationEntity intersectionNode2 = new StationEntity();
                for (int i = 0; i < intersectionNodeList.size(); i++) {
                    if (intersectionNodeList.get(i).getCode().substring(0, 3).equals(startNode.getCode().substring(0, 3))) {
                        intersectionNode1 = intersectionNodeList.get(i);
                    }else if(intersectionNodeList.get(i).getCode().substring(0, 3).equals(endNode.getCode().substring(0, 3))){
                        intersectionNode2 = intersectionNodeList.get(i);
                    }
                }
                double distance1 = abs(startNode.getDistanceToFirstStation() - intersectionNode1.getDistanceToFirstStation());
                double distance2 = abs(intersectionNode2.getDistanceToFirstStation() - endNode.getDistanceToFirstStation());
                distance = distance1+distance2;
            }
        } else if (startNodeList.size() == 2 && startNodeList.size() == endNodeList.size()) {//both of start and end stations are intersections
            for (int i = 0; i < startNodeList.size(); i++) {
                if (startNodeList.get(i).getCode().substring(0, 3).equals(endNodeList.get(i).getCode().substring(0, 3))) {
                    startNode = startNodeList.get(i);
                    endNode = endNodeList.get(i);
                }
            }
            distance = abs(startNode.getDistanceToFirstStation() - endNode.getDistanceToFirstStation());
        } else if (startNodeList.size() == 1 && endNodeList.size() == 2) {//non intersection to intersection
            startNode = startNodeList.get(0);
            for (int i = 0; i < endNodeList.size(); i++) {
                if (endNodeList.get(i).getCode().substring(0, 3).equals(startNode.getCode().substring(0, 3))) {
                    endNode = endNodeList.get(i);
                }
            }
            distance = abs(startNode.getDistanceToFirstStation() - endNode.getDistanceToFirstStation());
        } else if (startNodeList.size() == 2 && endNodeList.size() == 1) {//intersection to non intersection
            endNode = endNodeList.get(0);
            for (int i = 0; i < startNodeList.size(); i++) {
                if (startNodeList.get(i).getCode().substring(0, 3).equals(endNode.getCode().substring(0, 3))) {
                    startNode = startNodeList.get(i);
                }
            }
            distance = abs(startNode.getDistanceToFirstStation() - endNode.getDistanceToFirstStation());
        }
        return distance;
    }

    public boolean isPeak(Timestamp currentTime, Timestamp peakStart, Timestamp peakEnd) {
        Date currentTimeInDate = new Date(currentTime.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTimeInDate);
        cal.set(1970, Calendar.JANUARY, 01);//keep the time while changing the date to 1970-01-01
        currentTimeInDate = cal.getTime();
        Timestamp transformedCurrentTime = new Timestamp(currentTimeInDate.getTime());
        if ((transformedCurrentTime.after(peakStart) || transformedCurrentTime.equals(peakStart)) && (transformedCurrentTime.before(peakEnd) || transformedCurrentTime.equals(peakEnd))) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public double calculateTripFare(Timestamp date, String dayType, Timestamp currentTime, String passengerType, String startPt, String endPt) {
        
        double distance = calculateDistance(startPt, endPt);
        if (distance < 3200) {
            distance = 3200;
        }
        double ultimateFee = 0;

        if (passengerType.equals("Adult")) {
            Query q = em.createQuery("SELECT f FROM FareAlgoEntity f WHERE f.passengerType='Adult' ORDER BY f.fareType");
            List<FareAlgoEntity> fares = new ArrayList<>();
            for (Object o : q.getResultList()) {
                FareAlgoEntity s = (FareAlgoEntity) o;
                fares.add(s);
            }//0:Concession 1:Off Peak 2:Peak

            Query q1 = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s WHERE s.date=:date");
            q1.setParameter("date", date);
            Query q2 = em.createQuery("SELECT ts FROM TrainScheduleEntity ts WHERE ts.dayType=:dayType");
            q2.setParameter("dayType", dayType);
            if (!q1.getResultList().isEmpty()) {//the day is a special day
                List<SpecialDayAlgoEntity> specialDays = new ArrayList<>();
                for (Object o : q1.getResultList()) {
                    SpecialDayAlgoEntity sd = (SpecialDayAlgoEntity) o;
                    specialDays.add(sd);
                }
                for (int a = 0; a < specialDays.size(); a++) {
                    if (isPeak(currentTime, specialDays.get(a).getPeriodStart(), specialDays.get(a).getPeriodEnd()) && specialDays.get(a).getPeriodType().equals("peak")) {
                        ultimateFee = fares.get(2).getBaseFee() + (distance - 3200)/1000 * fares.get(2).getIncrementRate();
                    } else if (isPeak(currentTime, specialDays.get(a).getPeriodStart(), specialDays.get(a).getPeriodEnd()) == false) {
                        ultimateFee = fares.get(1).getBaseFee() + (distance - 3200)/1000 * fares.get(1).getIncrementRate();
                    }
                }

            } else if (!q2.getResultList().isEmpty()) {//the day is a regular day
                List<TrainScheduleEntity> regularDays = new ArrayList<>();
                for (Object o : q2.getResultList()) {
                    TrainScheduleEntity rd = (TrainScheduleEntity) o;
                    regularDays.add(rd);
                }
                for (int a = 0; a < regularDays.size(); a++) {
                    if (isPeak(currentTime, regularDays.get(a).getPeriodStart(), regularDays.get(a).getPeriodEnd()) && regularDays.get(a).getPeriodType().equals("peak")) {
                        ultimateFee = fares.get(2).getBaseFee() + (distance - 3200)/1000 * fares.get(2).getIncrementRate();
                    } else if (isPeak(currentTime, regularDays.get(a).getPeriodStart(), regularDays.get(a).getPeriodEnd()) == false) {
                        ultimateFee = fares.get(1).getBaseFee() + (distance - 3200)/1000 * fares.get(1).getIncrementRate();
                    }
                }
            }
        } else if (passengerType.equals("Student")) {
            Query q = em.createQuery("SELECT f FROM FareAlgoEntity f WHERE f.passengerType=:passengerType AND f.fareType='Off Peak'");
            q.setParameter("passengerType", passengerType);
            FareAlgoEntity f = (FareAlgoEntity) q.getResultList().get(0);
            ultimateFee = f.getBaseFee() + (distance - 3200)/1000 * f.getIncrementRate();
            if(ultimateFee>0.58){
                ultimateFee = 0.58;
            }
        } else if (passengerType.equals("Senior Citizen")) {
            Query q = em.createQuery("SELECT f FROM FareAlgoEntity f WHERE f.passengerType=:passengerType AND f.fareType='Off Peak'");
            q.setParameter("passengerType", passengerType);
            FareAlgoEntity f = (FareAlgoEntity) q.getResultList().get(0);
            ultimateFee = f.getBaseFee() + (distance - 3200)/1000 * f.getIncrementRate();
            if(ultimateFee>0.87){
                ultimateFee = 0.87;
            }
        }
        return ultimateFee;
    }
    
    public int countNumberOfStations(String startPt, String endPt){
        List<StationEntity> startNodeList = searchStationByName(startPt);
        List<StationEntity> endNodeList = searchStationByName(endPt);
        StationEntity startNode = new StationEntity();
        StationEntity endNode = new StationEntity();
        int num = 0;

        if (startNodeList.size() == 1 && startNodeList.size() == endNodeList.size()) {//both of start and end stations are NOT intersections
            startNode = startNodeList.get(0);
            endNode = endNodeList.get(0);
            if ((startNode.getCode().substring(0, 3)).equals(endNode.getCode().substring(0, 3))) {//same route: NSL/EWL
                num = abs(Integer.parseInt(endNode.getCode().substring(3))-Integer.parseInt(startNode.getCode().substring(3)))-1;
            }else {//different route
                List<StationEntity> intersectionNodeList = searchStationByName(findFirstIntersection(startNode));
                StationEntity intersectionNode1 = new StationEntity();
                StationEntity intersectionNode2 = new StationEntity();
                for (int i = 0; i < intersectionNodeList.size(); i++) {
                    if (intersectionNodeList.get(i).getCode().substring(0, 3).equals(startNode.getCode().substring(0, 3))) {
                        intersectionNode1 = intersectionNodeList.get(i);
                    }else if(intersectionNodeList.get(i).getCode().substring(0, 3).equals(endNode.getCode().substring(0, 3))){
                        intersectionNode2 = intersectionNodeList.get(i);
                    }
                }
                int num1 = abs(Integer.parseInt(intersectionNode1.getCode().substring(3))-Integer.parseInt(startNode.getCode().substring(3)))-1;
                int num2 = abs(Integer.parseInt(endNode.getCode().substring(3))-Integer.parseInt(intersectionNode2.getCode().substring(3)))-1;
                num = num1+num2;
            }
        }else if (startNodeList.size() == 2 && startNodeList.size() == endNodeList.size()) {//both of start and end stations are intersections
            for (int i = 0; i < startNodeList.size(); i++) {
                if (startNodeList.get(i).getCode().substring(0, 3).equals(endNodeList.get(i).getCode().substring(0, 3))) {
                    startNode = startNodeList.get(i);
                    endNode = endNodeList.get(i);
                }
            }
                num = abs(Integer.parseInt(endNode.getCode().substring(3))-Integer.parseInt(startNode.getCode().substring(3)))-1;
        } else if (startNodeList.size() == 1 && endNodeList.size() == 2) {//non intersection to intersection
            startNode = startNodeList.get(0);
            for (int i = 0; i < endNodeList.size(); i++) {
                if (endNodeList.get(i).getCode().substring(0, 3).equals(startNode.getCode().substring(0, 3))) {
                    endNode = endNodeList.get(i);
                }
            }
                num = abs(Integer.parseInt(endNode.getCode().substring(3))-Integer.parseInt(startNode.getCode().substring(3)))-1;
        } else if (startNodeList.size() == 2 && endNodeList.size() == 1) {//intersection to non intersection
            endNode = endNodeList.get(0);
            for (int i = 0; i < startNodeList.size(); i++) {
                if (startNodeList.get(i).getCode().substring(0, 3).equals(endNode.getCode().substring(0, 3))) {
                    startNode = startNodeList.get(i);
                }
            }
                num = abs(Integer.parseInt(endNode.getCode().substring(3))-Integer.parseInt(startNode.getCode().substring(3)))-1;
        }
        return num;
    }
    
    public boolean isWithinPeriod(Timestamp currentTime, Timestamp periodStart, Timestamp periodEnd) {
        Date currentTimeInDate = new Date(currentTime.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTimeInDate);
        cal.set(1970, Calendar.JANUARY, 01);//keep the time while changing the date to 1970-01-01
        currentTimeInDate = cal.getTime();
        Timestamp transformedCurrentTime = new Timestamp(currentTimeInDate.getTime());
        if ((transformedCurrentTime.after(periodStart) || transformedCurrentTime.equals(periodStart)) && (transformedCurrentTime.before(periodEnd) || transformedCurrentTime.equals(periodEnd))) {
            return true;
        } else {
            return false;
        }
    }

    //calculate trip time
    @Override
    public double calculateTripTime(Timestamp date, String dayType, Timestamp currentTime, String startPt, String endPt){ 
        double distance = calculateDistance(startPt, endPt);
        int numberOfStations = countNumberOfStations(startPt, endPt);
        double waitingTime = 0;
        
        Query q1 = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s WHERE s.date=:date");
        q1.setParameter("date", date);
        Query q2 = em.createQuery("SELECT ts FROM TrainScheduleEntity ts WHERE ts.dayType=:dayType");
        q2.setParameter("dayType", dayType);
        
        if (!q1.getResultList().isEmpty()) {//the day is a special day
                List<SpecialDayAlgoEntity> specialDays = new ArrayList<>();
                for (Object o : q1.getResultList()) {
                    SpecialDayAlgoEntity sd = (SpecialDayAlgoEntity) o;
                    specialDays.add(sd);
                }
                for (int a = 0; a < specialDays.size(); a++) {
                    if ( isWithinPeriod(currentTime, specialDays.get(a).getPeriodStart(), specialDays.get(a).getPeriodEnd()) ) {
                        waitingTime = specialDays.get(a).getWaitingTime();
                    }
                }
        }else if (!q2.getResultList().isEmpty()) {//the day is a regular day
                List<TrainScheduleEntity> regularDays = new ArrayList<>();
                for (Object o : q2.getResultList()) {
                    TrainScheduleEntity rd = (TrainScheduleEntity) o;
                    regularDays.add(rd);
                }
                for (int a = 0; a < regularDays.size(); a++) {
                    if (isWithinPeriod(currentTime, regularDays.get(a).getPeriodStart(), regularDays.get(a).getPeriodEnd()) ) {
                        waitingTime = regularDays.get(a).getWaitingTime();
                    }
                }
            }
        return distance / 60000 * 60 + (numberOfStations * waitingTime)/60;
    }
    
    public String recommendedPath(String startPt, String endPt){
        List<StationEntity> startNodeList = searchStationByName(startPt);
        List<StationEntity> endNodeList = searchStationByName(endPt);
        StationEntity startNode = new StationEntity();
        StationEntity endNode = new StationEntity();
        String path = "";

        if (startNodeList.size() == 1 && startNodeList.size() == endNodeList.size()) {//both of start and end stations are NOT intersections
            startNode = startNodeList.get(0);
            endNode = endNodeList.get(0);
            if (!(startNode.getCode().substring(0, 3)).equals(endNode.getCode().substring(0, 3))) {//different route
                List<StationEntity> intersectionNodeList = searchStationByName(findFirstIntersection(startNode));
                StationEntity intersectionNode1 = new StationEntity();
                StationEntity intersectionNode2 = new StationEntity();
                for (int i = 0; i < intersectionNodeList.size(); i++) {
                    if (intersectionNodeList.get(i).getCode().substring(0, 3).equals(startNode.getCode().substring(0, 3))) {
                        intersectionNode1 = intersectionNodeList.get(i);
                    }else if(intersectionNodeList.get(i).getCode().substring(0, 3).equals(endNode.getCode().substring(0, 3))){
                        intersectionNode2 = intersectionNodeList.get(i);
                    }
                }
                path = startPt+" -> "+intersectionNode1.getInfraName()+ " -> "+endPt;
            }
        }else{
            path = startPt+" -> "+endPt;
        }  
        return path;
    }
    
}
