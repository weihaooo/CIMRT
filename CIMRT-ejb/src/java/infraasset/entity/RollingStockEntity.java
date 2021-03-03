/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import operations.entity.InspectionReportEntity;
import operations.entity.TripReportEntity;
import routefare.entity.TripStationScheduleEntity;

/**
 *
 * @author Zhirong
 */
@Entity
public class RollingStockEntity extends InfrastructureEntity implements Serializable {

    private String status;
    private String brand;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "rollingStock")
    private List<InspectionReportEntity> inspectReports;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "rollingStock")
    private List<TripReportEntity> tripReports;
    @ManyToOne
    private DepotEntity depot;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "rollingStock")
    private List<TripStationScheduleEntity> tripStationSchedules = new ArrayList<TripStationScheduleEntity>();
    
    public RollingStockEntity() {

    }

    public RollingStockEntity(String infraId, String infraName, String brand, String status ) {
        super(infraId, infraName);
        this.brand = brand;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DepotEntity getDepot() {
        return depot;
    }

    public void setDepot(DepotEntity depot) {
        this.depot = depot;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public List<InspectionReportEntity> getInspectReports() {
        return inspectReports;
    }

    public void setInspectReports(List<InspectionReportEntity> inspectReports) {
        this.inspectReports = inspectReports;
    }

    public List<TripReportEntity> getTripReports() {
        return tripReports;
    }

    public void setTripReports(List<TripReportEntity> tripReports) {
        this.tripReports = tripReports;
    }

    public List<TripStationScheduleEntity> getTripStationSchedules() {
        return tripStationSchedules;
    }

    public void setTripStationSchedules(List<TripStationScheduleEntity> tripStationSchedules) {
        this.tripStationSchedules = tripStationSchedules;
    }
    
}
