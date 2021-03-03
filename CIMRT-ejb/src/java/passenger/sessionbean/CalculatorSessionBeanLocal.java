/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import infraasset.entity.StationEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import routefare.entity.RouteEntity;
import routefare.entity.TripStationScheduleEntity;

/**
 *
 * @author zhuming
 */
@Local
public interface CalculatorSessionBeanLocal {
    public List<String> getPassengerTypeList();
    public List<String> getRoutes();
    public List<StationEntity> getStationsByRoute(String route);
    public double calculateDistance(String startPt, String endPt);
    public double calculateTripFare(Timestamp date, String dayType, Timestamp currentTime, String passengerType, String startPt, String endPt);
    public double calculateTripTime(Timestamp date, String dayType, Timestamp currentTime, String startPt, String endPt);
    //public ArrayList<TripStationScheduleEntity> calculateNextTwoTrains(Timestamp date, String dayType, Timestamp currentTime, String routeCode, String nodeCode);    

}
