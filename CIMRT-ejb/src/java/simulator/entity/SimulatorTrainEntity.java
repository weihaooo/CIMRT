/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.entity;

import infraasset.entity.RollingStockEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Yoona
 */
@Entity
public class SimulatorTrainEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainId;
    private double distanceTravelled;
    private double maxSpeed;
    private boolean isStop;
    private int waitTime;
    @OneToMany
    private List<SimulatorTrackEntity> occupyingTracks = new ArrayList<SimulatorTrackEntity>();
    @OneToOne
    private RollingStockEntity rs;

    public SimulatorTrainEntity() {
    }

    public SimulatorTrainEntity(double distanceTravelled, double maxSpeed, boolean isStop) {
        this.distanceTravelled = distanceTravelled;
        this.maxSpeed = maxSpeed;
        this.isStop = isStop;
        this.waitTime = 0;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isIsStop() {
        return isStop;
    }

    public void setIsStop(boolean isStop) {
        this.isStop = isStop;
    }

    public List<SimulatorTrackEntity> getOccupyingTracks() {
        return occupyingTracks;
    }

    public void setOccupyingTracks(List<SimulatorTrackEntity> occupyingTracks) {
        this.occupyingTracks = occupyingTracks;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public RollingStockEntity getRs() {
        return rs;
    }

    public void setRs(RollingStockEntity rs) {
        this.rs = rs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trainId != null ? trainId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SimulatorTrainEntity)) {
            return false;
        }
        SimulatorTrainEntity other = (SimulatorTrainEntity) object;
        if ((this.trainId == null && other.trainId != null) || (this.trainId != null && !this.trainId.equals(other.trainId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "simulator.SimulatorTrainEntity[ id=" + trainId + " ]";
    }
    
}
