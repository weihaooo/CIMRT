/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.sessionbean;

import java.util.List;
import javax.ejb.Local;
import routefare.entity.TripStationScheduleEntity;
import simulator.entity.SimulatorTrackEntity;
import simulator.entity.SimulatorTrainEntity;

/**
 *
 * @author Yoona
 */
@Local
public interface SimulatorSessionBeanLocal {
    
    public List<SimulatorTrackEntity> getTrack();
    public List<SimulatorTrainEntity> getTrain();
    public long getTimeElapsed();
    public void doSimulation();
    public void timer();
    public void startSimulator();
    public void stopSimulator();
    public void pauseSimulator();
    public void linkTrains();
    public void setSpeed(int speed);
    public List<TripStationScheduleEntity> retrieveTrips();

    public void forceDeploy();
}
