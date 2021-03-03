/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.sessionbean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zhuming
 */
@Local
public interface DataAnalyticsSessionBeanLocal {
    //passenger volume by hour
    public List<String> getHourList();
    public List<Integer> countNumberOfPassengersPerHour(List<String> hourList);
    //passenger volume by station
    public List<String> getRoutes();
    public List<String> getSortedStationCode(String route);
    public List<Integer> getStationXaxis(String route);
    public List<Integer> countNumberOfPassengersPerStation_tapin(List<String> stationList);
    public List<Integer> countNumberOfPassengersPerStation_tapout(List<String> stationList);
    public List<String> getPeriodXaxis();
    public List<Integer> countNumberOfPassengersPerPeriod_tapin(String route, int index, List<String> periodList, List<Integer> stationXaxis);
    public List<Integer> countNumberOfPassengersPerPeriod_tapout(String route, int index, List<String> periodList, List<Integer> stationXaxis);
    //farebox recovery ratio
    public double calculateFareboxRatio(double expenses);
}
