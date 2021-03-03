/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.entity;

import infraasset.entity.RollingStockEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author zhuming
 */
@Entity
public class TripStationScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripStationScheduleId;
    private Timestamp date;
    private String dayType;
    private int tripNum;
    private Timestamp arrivalTime;
    private Timestamp departureTime;
    private String status;
    
    @ManyToOne(cascade={CascadeType.PERSIST})
    private NodeEntity node = new NodeEntity();
    @ManyToOne(cascade={CascadeType.PERSIST})
    private RouteEntity route = new RouteEntity();
    @ManyToOne(cascade={CascadeType.PERSIST})
    private TrainScheduleEntity trainSchedule = new TrainScheduleEntity();
    @ManyToOne(cascade={CascadeType.PERSIST})
    private SpecialDayAlgoEntity specialDayAlgo = new SpecialDayAlgoEntity();
    @ManyToOne(cascade={CascadeType.PERSIST})
    private RollingStockEntity rollingStock = new RollingStockEntity();
    
    public TripStationScheduleEntity() {
    }

    public TripStationScheduleEntity(Timestamp date, String dayType, int tripNum, Timestamp arrivalTime, Timestamp departureTime, String status) {
        this.date = date;
        this.dayType = dayType;
        this.tripNum = tripNum;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = status;
    }

    public Long getTripStationScheduleId() {
        return tripStationScheduleId;
    }

    public void setTripStationScheduleId(Long tripStationScheduleId) {
        this.tripStationScheduleId = tripStationScheduleId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public int getTripNum() {
        return tripNum;
    }

    public void setTripNum(int tripNum) {
        this.tripNum = tripNum;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public TrainScheduleEntity getTrainSchedule() {
        return trainSchedule;
    }

    public void setTrainSchedule(TrainScheduleEntity trainSchedule) {
        this.trainSchedule = trainSchedule;
    }

    public SpecialDayAlgoEntity getSpecialDayAlgo() {
        return specialDayAlgo;
    }

    public void setSpecialDayAlgo(SpecialDayAlgoEntity specialDayAlgo) {
        this.specialDayAlgo = specialDayAlgo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RollingStockEntity getRollingStock() {
        return rollingStock;
    }

    public void setRollingStock(RollingStockEntity rollingStock) {
        this.rollingStock = rollingStock;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tripStationScheduleId != null ? tripStationScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the tripStationScheduleId fields are not set
        if (!(object instanceof TripStationScheduleEntity)) {
            return false;
        }
        TripStationScheduleEntity other = (TripStationScheduleEntity) object;
        if ((this.tripStationScheduleId == null && other.tripStationScheduleId != null) || (this.tripStationScheduleId != null && !this.tripStationScheduleId.equals(other.tripStationScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "routefare.sessionbean.TripScheduleEntity[ id=" + tripStationScheduleId + " ]";
    }
    
}
