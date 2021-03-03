/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
@XmlType(name = "specialDayAlgoEntity", propOrder={
    "specialDayAlgoId",
    "date",
    "periodType",
    "periodStart",
    "periodEnd",
    "headway",
    "waitingTime"
})
public class SpecialDayAlgoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialDayAlgoId;
    private Timestamp date;
    private String periodType;
    private Timestamp periodStart;
    private Timestamp periodEnd;
    private double headway;
    private double waitingTime;
    
    @ManyToMany(cascade={CascadeType.PERSIST}, mappedBy="specialDayTrainSchedules")
    private List<RouteEntity> route = new ArrayList<RouteEntity>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "specialDayAlgo")
    private List<TripStationScheduleEntity> tripStationSchedules = new ArrayList<TripStationScheduleEntity>();
    @ManyToOne
    private FareAlgoEntity fareAlgo;
    
    public SpecialDayAlgoEntity() {
    }

    public SpecialDayAlgoEntity(Timestamp date, String periodType, Timestamp periodStart, Timestamp periodEnd, double headway, double waitingTime) {
        this.date = date;
        this.periodType = periodType;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.headway = headway;
        this.waitingTime = waitingTime;
    }

    public Long getSpecialDayAlgoId() {
        return specialDayAlgoId;
    }

    public void setSpecialDayAlgoId(Long specialDayAlgoId) {
        this.specialDayAlgoId = specialDayAlgoId;
    }


    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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

    public List<RouteEntity> getRoutes() {
        return route;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.route = routes;
    }

    public List<RouteEntity> getRoute() {
        return route;
    }

    public void setRoute(List<RouteEntity> route) {
        this.route = route;
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
        hash += (specialDayAlgoId != null ? specialDayAlgoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the specialDayAlgoId fields are not set
        if (!(object instanceof SpecialDayAlgoEntity)) {
            return false;
        }
        SpecialDayAlgoEntity other = (SpecialDayAlgoEntity) object;
        if ((this.specialDayAlgoId == null && other.specialDayAlgoId != null) || (this.specialDayAlgoId != null && !this.specialDayAlgoId.equals(other.specialDayAlgoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "routefare.entity.SpecialDayAlgoEntity[ id=" + specialDayAlgoId + " ]";
    }
    
}
