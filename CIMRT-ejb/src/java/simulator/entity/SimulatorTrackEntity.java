/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import routefare.entity.RouteEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class SimulatorTrackEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;
    private String status;
    private String trackName;
    private boolean isNode;
    private double distance;
    
    @OneToOne
    private RouteEntity route;
    
    @OneToOne
    private SimulatorTrackEntity nextTrack;
    
    @OneToOne
    private SimulatorTrackEntity prevTrack;
    
    @ManyToOne
    private SimulatorTrainEntity train;

    public SimulatorTrackEntity() {
    }

    public SimulatorTrackEntity(String status, String trackName, boolean isNode, double distance, SimulatorTrackEntity nextTrack) {
        this.status = status;
        this.trackName = trackName;
        this.isNode = isNode;
        this.distance = distance;
        this.nextTrack = nextTrack;
    }
    
    public SimulatorTrackEntity(String status, String trackName, boolean isNode, double distance, SimulatorTrackEntity nextTrack, SimulatorTrackEntity prevTrack, RouteEntity route) {
        this.status = status;
        this.trackName = trackName;
        this.isNode = isNode;
        this.distance = distance;
        this.nextTrack = nextTrack;
        this.prevTrack = prevTrack;
        this.route = route;
    }
    

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public boolean isIsNode() {
        return isNode;
    }

    public void setIsNode(boolean isNode) {
        this.isNode = isNode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public SimulatorTrackEntity getNextTrack() {
        return nextTrack;
    }

    public void setNextTrack(SimulatorTrackEntity nextTrack) {
        this.nextTrack = nextTrack;
    }

    public SimulatorTrainEntity getTrain() {
        return train;
    }

    public void setTrain(SimulatorTrainEntity train) {
        this.train = train;
    }

    public SimulatorTrackEntity getPrevTrack() {
        return prevTrack;
    }

    public void setPrevTrack(SimulatorTrackEntity prevTrack) {
        this.prevTrack = prevTrack;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trackId != null ? trackId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SimulatorTrackEntity)) {
            return false;
        }
        SimulatorTrackEntity other = (SimulatorTrackEntity) object;
        if ((this.trackId == null && other.trackId != null) || (this.trackId != null && !this.trackId.equals(other.trackId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "simulator.SimulatorTrackEntity[ id=" + trackId + " ]";
    }
    
}
