/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import infraasset.entity.StationEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;
import passenger.sessionbean.CalculatorSessionBeanLocal;

/**
 *
 * @author zhuming
 */
@Named(value = "calculatorManagedBean")
@SessionScoped
public class CalculatorManagedBean implements Serializable{
    @EJB
    private CalculatorSessionBeanLocal calculatorSessionBeanLocal;
    
    private List<String> passengerTypeList;
    private String passengerType;
    private List<SelectItem> stations;
    private String startPt;
    private String endPt;
    private String startPtT;
    private String endPtT;
    private double tripFare;
    private double tripTime;

    @PostConstruct
    public void init() {
        passengerTypeList = calculatorSessionBeanLocal.getPassengerTypeList();
        stations = new ArrayList<SelectItem>();
        List<String> routesString = calculatorSessionBeanLocal.getRoutes();//NSL+EWL
        for(int i=0; i<routesString.size(); i++){//2
            SelectItemGroup g = new SelectItemGroup(routesString.get(i));
            List<StationEntity> stationsOfRoute = calculatorSessionBeanLocal.getStationsByRoute(routesString.get(i));
            SelectItem[] stationsItem = new SelectItem[stationsOfRoute.size()];
            for(int k=0; k<stationsItem.length; k++){
                stationsItem[k] = new SelectItem(stationsOfRoute.get(k).getInfraName(),stationsOfRoute.get(k).getInfraName());
            }
            g.setSelectItems(stationsItem);
            this.stations.add(g);
        }
    }

    public List<SelectItem> getStations() {
        return stations;
    }

    public void setStations(List<SelectItem> stations) {
        this.stations = stations;
    }
    
    public List<String> getPassengerTypeList() {
        passengerTypeList = calculatorSessionBeanLocal.getPassengerTypeList();
        return passengerTypeList;
    }

    public void setPassengerTypeList(List<String> passengerTypeList) {
        this.passengerTypeList = passengerTypeList;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getStartPt() {
        return startPt;
    }

    public void setStartPt(String startPt) {
        this.startPt = startPt;
    }

    public String getEndPt() {
        return endPt;
    }

    public void setEndPt(String endPt) {
        this.endPt = endPt;
    }

    public double getTripFare() {
        return tripFare;
    }

    public void setTripFare(double tripFare) {
        this.tripFare = tripFare;
    }

    public String getStartPtT() {
        return startPtT;
    }

    public void setStartPtT(String startPtT) {
        this.startPtT = startPtT;
    }

    public String getEndPtT() {
        return endPtT;
    }

    public void setEndPtT(String endPtT) {
        this.endPtT = endPtT;
    }

    public double getTripTime() {
        return tripTime;
    }

    public void setTripTime(double tripTime) {
        this.tripTime = tripTime;
    }

 
    public void calculateFare(){
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
        Date date = new Date();
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        call.set(Calendar.HOUR_OF_DAY, 0);
        call.set(Calendar.MINUTE, 0);
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        date = call.getTime();
        Timestamp date1 = new java.sql.Timestamp(date.getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        
        //double distance = calculatorSessionBeanLocal.calculateDistance(startPt, endPt);
        //System.out.println("distance = " + distance);
        
        this.tripFare = calculatorSessionBeanLocal.calculateTripFare(date1, dayOfWeek, currentTime, passengerType, startPt, endPt);
    }
    public void calculateTime(){
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
        Date date = new Date();
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        call.set(Calendar.HOUR_OF_DAY, 0);
        call.set(Calendar.MINUTE, 0);
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        date = call.getTime();
        Timestamp date1 = new java.sql.Timestamp(date.getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        
        double distance = calculatorSessionBeanLocal.calculateDistance(startPtT, endPtT);
        System.out.println("distance = " + distance);
        
        this.tripTime = calculatorSessionBeanLocal.calculateTripTime(date1, dayOfWeek, currentTime, startPtT, endPtT);
    }
}
