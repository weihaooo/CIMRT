/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author zhuming
 */
@Entity
@XmlRootElement
@XmlType(name = "routeEntity", propOrder={
    "routeId",
    "code"

})
public class RouteEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;
    private String code;
    
    @ManyToMany(cascade={CascadeType.PERSIST})
    private List<NodeEntity> nodes = new ArrayList<NodeEntity>();
    @ManyToMany(cascade={CascadeType.PERSIST})
    private List<TrainScheduleEntity> trainSchedules = new ArrayList<TrainScheduleEntity>();
    @ManyToMany(cascade={CascadeType.PERSIST})
    private List<SpecialDayAlgoEntity> specialDayTrainSchedules = new ArrayList<SpecialDayAlgoEntity>();
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="route")
    private List<TripStationScheduleEntity> tripStationSchedules = new ArrayList<TripStationScheduleEntity>();

    public RouteEntity() {
    }

    public RouteEntity(String code) {
        this.code = code;
    }
    
    public Long getRouteId() {
        return routeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<NodeEntity> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeEntity> nodes) {
        this.nodes = nodes;
    }

    public List<TrainScheduleEntity> getTrainSchedules() {
        return trainSchedules;
    }

    public void setTrainSchedules(List<TrainScheduleEntity> trainSchedules) {
        this.trainSchedules = trainSchedules;
    }

    public List<SpecialDayAlgoEntity> getSpecialDayTrainSchedules() {
        return specialDayTrainSchedules;
    }

    public void setSpecialDayTrainSchedules(List<SpecialDayAlgoEntity> specialDayTrainSchedules) {
        this.specialDayTrainSchedules = specialDayTrainSchedules;
    }

    public List<TripStationScheduleEntity> getTripStationSchedules() {
        return tripStationSchedules;
    }

    public void setTripStationSchedules(List<TripStationScheduleEntity> tripStationSchedules) {
        this.tripStationSchedules = tripStationSchedules;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (routeId != null ? routeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RouteEntity)) {
            return false;
        }
        RouteEntity other = (RouteEntity) object;
        if ((this.routeId == null && other.routeId != null) || (this.routeId != null && !this.routeId.equals(other.routeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "routefare.sessionbean.RouteEntity[ id=" + routeId + " ]";
    }
    
}
