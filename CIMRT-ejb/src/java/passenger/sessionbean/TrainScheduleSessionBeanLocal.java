/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import routefare.entity.TripStationScheduleEntity;

/**
 *
 * @author zhuming
 */
@Local
public interface TrainScheduleSessionBeanLocal {
    public ArrayList<TripStationScheduleEntity> calculateNextTwoTrains(Timestamp date, String dayType, Timestamp currentTime, String routeCode, String nodeCode);    
    public List<String> getRoutesFromStation(String code);
    public List<TripStationScheduleEntity> printFirstTrain(String stationCode, Timestamp today);
    public List<TripStationScheduleEntity> printLastTrain(String stationCode, Timestamp today);
}
