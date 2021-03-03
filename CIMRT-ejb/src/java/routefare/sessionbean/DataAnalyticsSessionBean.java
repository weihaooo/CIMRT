/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.sessionbean;

import infraasset.entity.StationEntity;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import routefare.entity.FareTransactionEntity;
import routefare.entity.RouteEntity;
import routefare.entity.TrainScheduleEntity;

/**
 *
 * @author zhuming
 */
@Stateless
public class DataAnalyticsSessionBean implements DataAnalyticsSessionBeanLocal {
        
    @PersistenceContext
    private EntityManager em;
    
    public DataAnalyticsSessionBean(){
        
    }
    
    public boolean isWithinPeriod(Timestamp myTime, Timestamp periodStart, Timestamp periodEnd) {
        Date myTimeInDate = new Date(myTime.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(myTimeInDate);
        cal.set(1970, Calendar.JANUARY, 01);//keep the time while changing the date to 1970-01-01
        myTimeInDate = cal.getTime();
        Timestamp transformedMyTime = new Timestamp(myTimeInDate.getTime());
        if ((transformedMyTime.after(periodStart) || transformedMyTime.equals(periodStart)) && (transformedMyTime.before(periodEnd) || transformedMyTime.equals(periodEnd))) {
            return true;
        } else {
            return false;
        }
    }
    
    public Timestamp convertToTimestamp(String timeStr) {
        SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm");
        Timestamp time = new Timestamp(60);
        try {
            time = new Timestamp(printFormat.parse(timeStr).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(RoutePlanningSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }
    
    @Override
    public List<String> getHourList(){
        List<String> hourList = new ArrayList<String>();
    
        Query q = em.createQuery("SELECT ts FROM TrainScheduleEntity ts WHERE ts.dayType='Weekday' ORDER BY ts.periodStart");
        List<TrainScheduleEntity> weekdaySchedule = new ArrayList<TrainScheduleEntity>();
        for (Object o : q.getResultList()) {
            TrainScheduleEntity ts = (TrainScheduleEntity) o;
            weekdaySchedule.add(ts);
        }
        Timestamp weekdayStart = weekdaySchedule.get(0).getPeriodStart();
        Timestamp weekdayEnd = weekdaySchedule.get(weekdaySchedule.size()-1).getPeriodEnd();
        // find intervals
        int diffHour = (int) ((weekdayEnd.getTime()-weekdayStart.getTime())/(60*60*1000));//hours
        String result;
        String s1;
        String s2;
        for(int i=0; i<diffHour; i++){
            Timestamp weekdayLater = new Timestamp(weekdayStart.getTime() + (1000 * 60 * 60 * 1));
            s1 = new SimpleDateFormat("HH:mm").format(weekdayStart);
            s2 = new SimpleDateFormat("HH:mm").format(weekdayLater);
            result = s1 + " - " + s2;
            hourList.add(result);
            weekdayStart = weekdayLater;
        }
        
        return hourList;
    }
    
    @Override
    public List<Integer> countNumberOfPassengersPerHour(List<String> hourList) {
        List<Integer> passengers = new ArrayList<Integer>();
        for (int i = 0; i < hourList.size(); i++) {
            String[] arr = hourList.get(i).split(" - ");
            String startStr = arr[0];
            String endStr = arr[1];
            Timestamp hourStart = convertToTimestamp(startStr);
            Timestamp hourEnd = convertToTimestamp(endStr);
            
            List<FareTransactionEntity> myFT = new ArrayList<FareTransactionEntity>();
            Query q = em.createQuery("SELECT ft FROM FareTransactionEntity ft");
            for (Object o : q.getResultList()) {
                FareTransactionEntity ft = (FareTransactionEntity) o;
                if(isWithinPeriod(ft.getStartTime(),hourStart,hourEnd)){
                    myFT.add(ft);
                }
            }
            passengers.add(myFT.size());
        }
        return passengers;
    }

    @Override
    public List<String> getRoutes(){//NSL/EWL
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
    public List<String> getSortedStationCode(String route){//this route only contains the first 3 chars of the route names: NSL/EWL
        
        Query q = em.createQuery("SELECT s FROM StationEntity s");
        List<String> stationList = new ArrayList<>();
        for (Object o : q.getResultList()) {
            StationEntity s = (StationEntity) o;
            if(s.getCode().substring(0, 3).equals(route)){
                stationList.add(s.getCode());
            }
        }
        Collections.sort(stationList, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return Integer.parseInt(num);
            }
        });
        return stationList;
    }
    
    @Override
    public List<Integer> getStationXaxis(String route){//this route only contains the first 3 chars of the route names: NSL/EWL
        
        Query q = em.createQuery("SELECT s FROM StationEntity s");
        List<Integer> stationList = new ArrayList<>();
        for (Object o : q.getResultList()) {
            StationEntity s = (StationEntity) o;
            if(s.getCode().substring(0, 3).equals(route)){
                stationList.add(Integer.valueOf(s.getCode().substring(3)));
            }
        }
        Collections.sort(stationList);
        return stationList;
    }
    
    @Override
    public List<Integer> countNumberOfPassengersPerStation_tapin(List<String> stationList){
        List<Integer> passengers = new ArrayList<Integer>();
        for(int i=0; i<stationList.size(); i++){
            Query q = em.createQuery("SELECT ft FROM FareTransactionEntity ft WHERE ft.startStation.code=:stationCode");
            q.setParameter("stationCode", stationList.get(i));
            passengers.add(q.getResultList().size());
        }
        return passengers;
    }
    
    @Override
    public List<Integer> countNumberOfPassengersPerStation_tapout(List<String> stationList){
        List<Integer> passengers = new ArrayList<Integer>();
        for(int i=0; i<stationList.size(); i++){
            Query q = em.createQuery("SELECT ft FROM FareTransactionEntity ft WHERE ft.endStation.code=:stationCode");
            q.setParameter("stationCode", stationList.get(i));
            passengers.add(q.getResultList().size());
        }
        return passengers;
    }
    
    @Override
    public List<String> getPeriodXaxis() {
        List<String> periodList = new ArrayList<String>();
            Query q1 = em.createQuery("SELECT s FROM TrainScheduleEntity s WHERE s.dayType='Weekday' ORDER BY s.periodStart");

            for (Object o : q1.getResultList()) {
                TrainScheduleEntity s = (TrainScheduleEntity) o;
                String result;

                String s1 = new SimpleDateFormat("HH:mm").format(s.getPeriodStart());
                String s2 = new SimpleDateFormat("HH:mm").format(s.getPeriodEnd());

                result = s1 + " - " + s2;
                periodList.add(result);
            }
            return periodList;
        }
    
    @Override
    public List<Integer> countNumberOfPassengersPerPeriod_tapin(String route, int index, List<String> periodList, List<Integer> stationXaxis){
        List<Integer> passengers = new ArrayList<Integer>();
        String stationNum = stationXaxis.get(index).toString();
        if(stationXaxis.get(index)<10){
            stationNum = "0"+stationXaxis.get(index);
        }
        
        String stationCode = route+stationNum;
        System.out.println("tapin stationcode: "+stationCode);
        for(int k=0; k<periodList.size(); k++){
            String[] arr = periodList.get(k).split(" - ");
            String startStr = arr[0];
            String endStr = arr[1];
            Timestamp periodStart = convertToTimestamp(startStr);
            Timestamp periodEnd = convertToTimestamp(endStr);
            
            List<FareTransactionEntity> myFT = new ArrayList<FareTransactionEntity>();
            Query q = em.createQuery("SELECT ft FROM FareTransactionEntity ft WHERE ft.startStation.code=:stationCode");
            q.setParameter("stationCode", stationCode);
            for (Object o : q.getResultList()) {
                FareTransactionEntity ft = (FareTransactionEntity) o;
                if(isWithinPeriod(ft.getStartTime(),periodStart,periodEnd)){
                    myFT.add(ft);
                }
            }
            passengers.add(myFT.size());
        }
        return passengers;
    }
    
    @Override
    public List<Integer> countNumberOfPassengersPerPeriod_tapout(String route, int index, List<String> periodList, List<Integer> stationXaxis){
        List<Integer> passengers = new ArrayList<Integer>();
        String stationNum = stationXaxis.get(index).toString();
        if(stationXaxis.get(index)<10){
            stationNum = "0"+stationXaxis.get(index);
        }
        
        String stationCode = route+stationNum;
        System.out.println("tapout stationcode: "+stationCode);
        for(int k=0; k<periodList.size(); k++){
            String[] arr = periodList.get(k).split(" - ");
            String startStr = arr[0];
            String endStr = arr[1];
            Timestamp periodStart = convertToTimestamp(startStr);
            Timestamp periodEnd = convertToTimestamp(endStr);
            
            List<FareTransactionEntity> myFT = new ArrayList<FareTransactionEntity>();
            Query q = em.createQuery("SELECT ft FROM FareTransactionEntity ft WHERE ft.endStation.code=:stationCode");
            q.setParameter("stationCode", stationCode);
            for (Object o : q.getResultList()) {
                FareTransactionEntity ft = (FareTransactionEntity) o;
                if(isWithinPeriod(ft.getStartTime(),periodStart,periodEnd)){
                    myFT.add(ft);
                }
            }
            passengers.add(myFT.size());
        }
        return passengers;
    }
    
    @Override
    public double calculateFareboxRatio(double expenses){
        Query q = em.createQuery("SELECT ft FROM FareTransactionEntity ft");
        double revenue = 0;
        for (Object o : q.getResultList()) {
            FareTransactionEntity ft = (FareTransactionEntity)o;
            revenue = revenue + ft.getAmount();
        }
        
        System.out.println("revenue: "+revenue);
        System.out.println("expenses: "+expenses);
        
        return (revenue/expenses)*100;
    }
}
