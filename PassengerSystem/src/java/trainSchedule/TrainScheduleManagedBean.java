/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trainSchedule;

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
import javax.faces.bean.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;
import passenger.sessionbean.CalculatorSessionBeanLocal;
import passenger.sessionbean.TrainScheduleSessionBeanLocal;
import routefare.entity.TripStationScheduleEntity;

/**
 *
 * @author zhuming
 */
@Named(value = "trainScheduleManagedBean")
@SessionScoped
public class TrainScheduleManagedBean implements Serializable {

    @EJB
    private CalculatorSessionBeanLocal calculatorSessionBeanLocal;
    @EJB
    private TrainScheduleSessionBeanLocal trainScheduleSessionBeanLocal;
    private List<SelectItem> stations;
    private String theStation;
    private String theStationForOT;
    private List<String> routeList;
    private String theRoute;
    private List<TripStationScheduleEntity> nextTwoTrainSchedules;
    private List<TripStationScheduleEntity> firstTrain;
    private List<TripStationScheduleEntity> lastTrain;

    @PostConstruct
    public void init() {
        stations = new ArrayList<SelectItem>();
        List<String> routesString = calculatorSessionBeanLocal.getRoutes();//NSL+EWL
        for(int i=0; i<routesString.size(); i++){//2
            SelectItemGroup g = new SelectItemGroup(routesString.get(i));
            List<StationEntity> stationsOfRoute = calculatorSessionBeanLocal.getStationsByRoute(routesString.get(i));
            SelectItem[] stationsItem = new SelectItem[stationsOfRoute.size()];
            for(int k=0; k<stationsItem.length; k++){
                stationsItem[k] = new SelectItem(stationsOfRoute.get(k).getCode(),stationsOfRoute.get(k).getInfraName());
            }
            g.setSelectItems(stationsItem);
            this.stations.add(g);
        }

    }

    public String getTheStation() {
        return theStation;
    }

    public void setTheStation(String theStation) {
        this.theStation = theStation;
    }

    public String getTheStationForOT() {
        return theStationForOT;
    }

    public void setTheStationForOT(String theStationForOT) {
        this.theStationForOT = theStationForOT;
    }

    public List<String> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<String> routeList) {
        this.routeList = routeList;
    }

    public List<TripStationScheduleEntity> getNextTwoTrainSchedules() {
        return nextTwoTrainSchedules;
    }

    public void setNextTwoTrainSchedules(List<TripStationScheduleEntity> nextTwoTrainSchedules) {
        this.nextTwoTrainSchedules = nextTwoTrainSchedules;
    }

    public List<SelectItem> getStations() {
        return stations;
    }

    public void setStations(List<SelectItem> stations) {
        this.stations = stations;
    }

    public String getTheRoute() {
        return theRoute;
    }

    public void setTheRoute(String theRoute) {
        this.theRoute = theRoute;
    }

    public List<TripStationScheduleEntity> getFirstTrain() {
        return firstTrain;
    }

    public void setFirstTrain(List<TripStationScheduleEntity> firstTrain) {
        this.firstTrain = firstTrain;
    }

    public List<TripStationScheduleEntity> getLastTrain() {
        return lastTrain;
    }

    public void setLastTrain(List<TripStationScheduleEntity> lastTrain) {
        this.lastTrain = lastTrain;
    }

    public void getNextTwoTrainSchedule() {
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

        this.nextTwoTrainSchedules = trainScheduleSessionBeanLocal.calculateNextTwoTrains(date1, dayOfWeek, currentTime, theRoute.substring(0, 8), theStation);
    }

    public void onStationChange() {
        System.out.println("Station Change");
        this.routeList = trainScheduleSessionBeanLocal.getRoutesFromStation(theStation);
    }
    
    public void getFirstAndLastTrainTime() {
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
        
        this.firstTrain = trainScheduleSessionBeanLocal.printFirstTrain(theStationForOT, date1);
        this.lastTrain = trainScheduleSessionBeanLocal.printLastTrain(theStationForOT, date1);
    }
    
}
