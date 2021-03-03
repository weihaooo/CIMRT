/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.sessionbean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import routefare.entity.NodeEntity;
import routefare.entity.RouteEntity;
import routefare.entity.SpecialDayAlgoEntity;
import routefare.entity.TrainScheduleEntity;
import routefare.entity.TripStationScheduleEntity;

/**
 *
 * @author zhuming
 */
@Local
public interface RoutePlanningSessionBeanLocal {
    
    //generate train schedule tables
    public List<String> getRouteList();
    public boolean isSpecialDay(Timestamp date);
    public List<String> getPeriodList(Timestamp date, String dayType);
    public NodeEntity searchNode(String code);
    public RouteEntity searchRoute(String code);
    public ArrayList<String> getNodes(String routeCode);
    public double calculateTimeToNextStation(String code);
    public double calculateTimeToPreviousStation(String code);
    public String[] getFirstRowOfScheduleTable(String routeCode);
    public Timestamp[][] getScheduleTable(String routeCode,String dayType, String time);
    public ArrayList<ArrayList<String>> getTrainSchedule(String routeCode, String dayType, String time);

    //update train schedule structure for special days
    public void createTempSpecialDayTrainSchedule(Timestamp date);
    public void addSpecialDayTrainSchedule (Timestamp date, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime);
    public SpecialDayAlgoEntity searchSpecialDayTrainSchedule(Long id);
    public void editSpecialDayTrainSchedule(Long id, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime);
    public void deleteSpecialDayTrainSchedule(Long id);
    public void updateSpecialDayTrainSchedule(Timestamp date);
    public ArrayList<SpecialDayAlgoEntity> viewSpecialDaySchedule();
    public ArrayList<SpecialDayAlgoEntity> viewUpdatedSpecialDaySchedule(Timestamp date);
    public Timestamp[][] getSpecialDayScheduleTable(String routeCode, Timestamp date, Timestamp periodStart, Timestamp periodEnd);
    public ArrayList<ArrayList<String>> getSpecialDaySchedule(Timestamp date, String routeCode, String dayType, String time);
    
    
    //update train schedule structure
    public ArrayList<TrainScheduleEntity> viewTrainSchedule(String dayType);
    public void createTempTrainSchedule(String dayType);
    public TrainScheduleEntity searchTempTrainSchedule(Long trainScheduleId);
    public void editTempTrainSchedule(Long trainScheduleId, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime);
    public void deleteTempTrainSchedule(Long trainScheduleId);
    public void addTempTrainSchedule(String dayType, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime);
    public ArrayList<TrainScheduleEntity> getTempTrainSchedule();
    public void updateTrainSchedule(String dayType);
    
    ////////////////////view train schedule for operation staff////////////////////
    public ArrayList<String> getNodeRouteList(String code);
    public void storeSpecialDayTripStationSchedule();
    public void storeRegularDayTripStationSchedule();
    public void testStoreRegularDayTripStationSchedule();
    public ArrayList<TripStationScheduleEntity> getTripStationSchedule(String routeCode, String nodeCode, Timestamp date, String dayType);
    //handle emergency
    public boolean isAfterCurrentTime(Timestamp currentTime, Timestamp oneTime);
    public void createEmergencyTripStationSchedule(String routeCode, Timestamp currentDate, String currentDayType, Timestamp currentTime, double delay);
    
    ///////////////////assign rolling stock for each trip////////////////////
    public ArrayList<ArrayList<String>> collateWholeDaySchedule(String routeCode, String dayType);
    public ArrayList<ArrayList<String>> assignRollingStock(String routeCode, String dayType);
    //public ArrayList<ArrayList<String>> assignRollingStock_selectedColumns(String routeCode, String dayType);
    public void storeRegularDayTripStationScheduleWithRS();
    public void testStoreRegularDayTripStationScheduleWithRS_NSL0115();
    
}

