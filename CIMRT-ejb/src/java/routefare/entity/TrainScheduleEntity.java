/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author zhuming
 */
@Entity
@XmlRootElement
@XmlType(name = "trainScheduleEntity", propOrder = {
    "trainScheduleId",
    "dayType",
    "periodType",
    "periodStart",
    "periodEnd",
    "headway",
    "waitingTime",
    "updateVersion"
})


public class TrainScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainScheduleId;
    private String dayType;
    private String periodType;
    private Timestamp periodStart;
    private Timestamp periodEnd;
    private double headway;
    private double waitingTime;
    private String updateVersion;
    @ManyToMany(cascade = {CascadeType.PERSIST}, mappedBy = "trainSchedules")
    private List<RouteEntity> routes = new ArrayList<RouteEntity>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "trainSchedule")
    private List<TripStationScheduleEntity> tripStationSchedules = new ArrayList<TripStationScheduleEntity>();
    @ManyToOne
    private FareAlgoEntity fareAlgo;
            
    public TrainScheduleEntity() {
    }

    public TrainScheduleEntity(String dayType, String periodType, Timestamp periodStart, Timestamp periodEnd, double headway, double waitingTime, String updateVersion) {
        this.dayType = dayType;
        this.periodType = periodType;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.headway = headway;
        this.waitingTime = waitingTime;
        this.updateVersion = updateVersion;
    }

    public Long getTrainScheduleId() {
        return trainScheduleId;
    }

    public void setTrainScheduleId(Long trainScheduleId) {
        this.trainScheduleId = trainScheduleId;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Timestamp getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Timestamp periodStart) {
        this.periodStart = periodStart;
    }

    public Timestamp getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Timestamp periodEnd) {
        this.periodEnd = periodEnd;
    }

    public double getHeadway() {
        return headway;
    }

    public void setHeadway(double headway) {
        this.headway = headway;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }

    public List<RouteEntity> getRoutes() {
        Collections.sort(routes, new Comparator<RouteEntity>() {
            public int compare(RouteEntity o1, RouteEntity o2) {
                if (o1.getCode() == null || o2.getCode() == null) {
                    return 0;
                }
                return o1.getCode().compareTo(o2.getCode());
            }
        }
        );
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public List<TripStationScheduleEntity> getTripStationSchedules() {
        return tripStationSchedules;
    }

    public void setTripStationSchedules(List<TripStationScheduleEntity> tripStationSchedules) {
        this.tripStationSchedules = tripStationSchedules;
    }

    public FareAlgoEntity getFareAlgo() {
        return fareAlgo;
    }

    public void setFareAlgo(FareAlgoEntity fareAlgo) {
        this.fareAlgo = fareAlgo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trainScheduleId != null ? trainScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the trainScheduleId fields are not set
        if (!(object instanceof TrainScheduleEntity)) {
            return false;
        }
        TrainScheduleEntity other = (TrainScheduleEntity) object;
        if ((this.trainScheduleId == null && other.trainScheduleId != null) || (this.trainScheduleId != null && !this.trainScheduleId.equals(other.trainScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "routefare.sessionbean.TrainScheduleEntity[ id=" + trainScheduleId + " ]";
    }

}
