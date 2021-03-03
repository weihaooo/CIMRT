/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.DepotStaffEntity;
import infraasset.entity.RollingStockEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Zhirong
 */
@Entity
public class TripReportEntity implements Serializable {

    @Id
    private String tripReportId;

    private Timestamp dateTime;
    private String reportType;
    @Column(length=10000)
    private String remarks;
    //list of items in the driver cabin
    private String cabinLight;
    private String headLight;
    private String tailLight;
    private String reverser;
    private String switchPanel;
    private String horn;
    private String microphone;
    private String speaker;
    private String ledDisplay;
    private String wipers;
    private String washers;
    @ManyToOne
    private RollingStockEntity rollingStock;
    @ManyToOne
    private DepotStaffEntity depotStaff;
    
    @OneToOne(cascade={CascadeType.PERSIST})
    private MaintenanceRequestEntity maintenanceRequest;
     

    public TripReportEntity() {

    }

    public TripReportEntity(String tripReportId, Timestamp dateTime, String reportType, String remarks, String cabinLight, String headLight, String tailLight, String reverser, String switchPanel, String horn, String microphone, String speaker, String ledDisplay, String wipers, String washers) {
        this.tripReportId = tripReportId;
        this.dateTime = dateTime;
        this.reportType = reportType;
        this.remarks = remarks;
        this.cabinLight = cabinLight;
        this.headLight = headLight;
        this.tailLight = tailLight;
        this.reverser = reverser;
        this.switchPanel = switchPanel;
        this.horn = horn;
        this.microphone = microphone;
        this.speaker = speaker;
        this.ledDisplay = ledDisplay;
        this.wipers = wipers;
        this.washers = washers;
    }

    
    
    
    public String getHeadLight() {
        return headLight;
    }

    public void setHeadLight(String headLight) {
        this.headLight = headLight;
    }

    public String getTailLight() {
        return tailLight;
    }

    public void setTailLight(String tailLight) {
        this.tailLight = tailLight;
    }  
      
    
    public String getTripReportId() {
        return tripReportId;
    }

    public void setTripReportId(String tripReportId) {
        this.tripReportId = tripReportId;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

   
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCabinLight() {
        return cabinLight;
    }

    public void setCabinLight(String cabinLight) {
        this.cabinLight = cabinLight;
    }

    
    public String getReverser() {
        return reverser;
    }

    public void setReverser(String reverser) {
        this.reverser = reverser;
    }

    public String getSwitchPanel() {
        return switchPanel;
    }

    public void setSwitchPanel(String switchPanel) {
        this.switchPanel = switchPanel;
    }

    public String getHorn() {
        return horn;
    }

    public void setHorn(String horn) {
        this.horn = horn;
    }

    public String getMicrophone() {
        return microphone;
    }

    public void setMicrophone(String microphone) {
        this.microphone = microphone;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getLedDisplay() {
        return ledDisplay;
    }

    public void setLedDisplay(String ledDisplay) {
        this.ledDisplay = ledDisplay;
    }

    public String getWipers() {
        return wipers;
    }

    public void setWipers(String wipers) {
        this.wipers = wipers;
    }

    public String getWashers() {
        return washers;
    }

    public void setWashers(String washers) {
        this.washers = washers;
    }

    public RollingStockEntity getRollingStock() {
        return rollingStock;
    }

    public void setRollingStock(RollingStockEntity rollingStock) {
        this.rollingStock = rollingStock;
    }

    public DepotStaffEntity getDepotStaff() {
        return depotStaff;
    }

    public void setDepotStaff(DepotStaffEntity depotStaff) {
        this.depotStaff = depotStaff;
    }

    public MaintenanceRequestEntity getMaintenanceRequest() {
        return maintenanceRequest;
    }

    public void setMaintenanceRequest(MaintenanceRequestEntity maintenanceRequest) {
        this.maintenanceRequest = maintenanceRequest;
    }

  
    
    
}
