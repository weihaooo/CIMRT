/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import routefare.entity.TripStationScheduleEntity;
import simulator.entity.SimulatorTrackEntity;
import simulator.entity.SimulatorTrainEntity;
import simulator.sessionbean.SimulatorSessionBeanLocal;

/**
 *
 * @author Yoona
 */
@Named(value = "simulatorManagedBean")
@SessionScoped
public class SimulatorManagedBean implements Serializable {

    @EJB
    SimulatorSessionBeanLocal simulatorSessionBeanLocal;

    private List<SimulatorTrainEntity> trains;
    private List<SimulatorTrackEntity> tracks;
    private Timestamp timeElapsed;
    private String simulatorStatus;
    private int speed;
    private List<TripStationScheduleEntity> trips;

    @PostConstruct
    public void init() {
        simulatorSessionBeanLocal.linkTrains();
        retrieveTrains();
        retrieveTracks();
        retrieveTrips();
    }

    public SimulatorManagedBean() {
    }

    public List<SimulatorTrainEntity> getTrains() {
        return trains;
    }

    public void setTrains(List<SimulatorTrainEntity> trains) {
        this.trains = trains;
    }

    public List<SimulatorTrackEntity> getTracks() {
        return tracks;
    }

    public void setTracks(List<SimulatorTrackEntity> tracks) {
        this.tracks = tracks;
    }

    public void retrieveTrains() {
        trains = simulatorSessionBeanLocal.getTrain();
    }

    public void retrieveTracks() {
        tracks = simulatorSessionBeanLocal.getTrack();
    }

    public void startSimulator() {
        simulatorSessionBeanLocal.startSimulator();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Simulator is running.", ""));
    }

    public void stopSimulator() {

        simulatorSessionBeanLocal.stopSimulator();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Simulator stopped.", ""));
    }

    public void pauseSimulator() {

        simulatorSessionBeanLocal.pauseSimulator();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Simulator paused", ""));
    }

    public void retrieveTime() {
        timeElapsed = new Timestamp(simulatorSessionBeanLocal.getTimeElapsed());
        //System.out.println("Time elapsed "+simulatorSessionBeanLocal.getTimeElapsed()  + " 2 " + timeElapsed);
        simulatorSessionBeanLocal.timer();
    }

    public void setSimulatorSpeed() {
        simulatorSessionBeanLocal.setSpeed(speed);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Speed set as " + speed, ""));
    }

    public void retrieveTrips() {
        simulatorSessionBeanLocal.retrieveTrips();

    }

    public void forceDeploy() {
        simulatorSessionBeanLocal.forceDeploy();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Force Deploy ", ""));
    }

    public Timestamp getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(Timestamp timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getSimulatorStatus() {
        return simulatorStatus;
    }

    public void setSimulatorStatus(String simulatorStatus) {
        this.simulatorStatus = simulatorStatus;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public List<TripStationScheduleEntity> getTrips() {
        return trips;
    }

    public void setTrips(List<TripStationScheduleEntity> trips) {
        this.trips = trips;
    }

}
