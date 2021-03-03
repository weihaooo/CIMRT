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
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Zhirong
 */
@Entity

public class InspectionReportEntity implements Serializable {

    @Id
    private String inspectReportId;
   
    private Timestamp date;
    private String reportType;
    @Column(length = 10000)
    private String remarks;
    private String status;
    @Column(length = 10000)
    private String signature;
    private String submitter;
    private String endorser;
    //list of items in the driver cabin
    private String dCabinLight;
    private String throttle;
    private String brake;
    private String reverser;
    private String switchPanel;
    private String horn;
    private String microphone;
    private String speaker;
    private String ledDisplay;
    private String wipers;
    private String washers;
    private String driverChair;
    //list of items in the passenger cabin
    private String pCabinLight;
    private String cabinBenches;
    private String handGrips;
    private String handRails;
    private String doorSignals;
    private String emergencyButtons;
    private String emergencyExitDoors;
    private String loudspeakers;
    private String animatedTrainMaps;
    private String windows;
    private String trainDoors;
    private String aircons;
    @ManyToOne
    private RollingStockEntity rollingStock;
    @ManyToOne
    private DepotStaffEntity depotStaff;
    
    @OneToOne(cascade={CascadeType.PERSIST})
    private MaintenanceRequestEntity maintenanceRequest;

    public InspectionReportEntity() {

    }

    public InspectionReportEntity(String inspectReportId, Timestamp date, String reportType, String remarks, String status, String signature, String submitter, String endorser, String dCabinLight, String throttle,
            String brake, String reverser, String switchPanel, String horn, String microphone, String speaker, String ledDisplay, String wipers, String washers, String driverChair, String pCabinLight, String cabinBenches, String handGrips, String handRails,
            String doorSignals, String emergencyButtons, String emergencyExitDoors, String loudspeakers, String animatedTrainMaps, String windows, String trainDoors, String aircons) {
        this.inspectReportId = inspectReportId;
        this.date = date;
        this.reportType = reportType;
        this.remarks = remarks;
        this.status = status;
        this.signature = signature;
        this.submitter = submitter;
        this.endorser = endorser;
        this.dCabinLight = dCabinLight;
        this.throttle = throttle;
        this.brake = brake;
        this.reverser = reverser;
        this.switchPanel = switchPanel;
        this.horn = horn;
        this.microphone = microphone;
        this.speaker = speaker;
        this.ledDisplay = ledDisplay;
        this.wipers = wipers;
        this.washers = washers;
        this.driverChair = driverChair;
        this.pCabinLight = pCabinLight;
        this.cabinBenches = cabinBenches;
        this.handGrips = handGrips;
        this.handRails = handRails;
        this.doorSignals = doorSignals;
        this.emergencyButtons = emergencyButtons;
        this.emergencyExitDoors = emergencyExitDoors;
        this.loudspeakers = loudspeakers;
        this.animatedTrainMaps = animatedTrainMaps;
        this.windows = windows;
        this.trainDoors = trainDoors;
        this.aircons = aircons;
    }

    public String getInspectReportId() {
        return inspectReportId;
    }

    public void setInspecReportId(String inspectReportId) {
        this.inspectReportId = inspectReportId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getEndorser() {
        return endorser;
    }

    public void setEndorser(String endorser) {
        this.endorser = endorser;
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

    public String getdCabinLight() {
        return dCabinLight;
    }

    public void setdCabinLight(String dCabinLight) {
        this.dCabinLight = dCabinLight;
    }

    public String getThrottle() {
        return throttle;
    }

    public void setThrottle(String throttle) {
        this.throttle = throttle;
    }

    public String getBrake() {
        return brake;
    }

    public void setBrake(String brake) {
        this.brake = brake;
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

    public String getDriverChair() {
        return driverChair;
    }

    public void setDriverChair(String driverChair) {
        this.driverChair = driverChair;
    }

    public String getpCabinLight() {
        return pCabinLight;
    }

    public void setpCabinLight(String pCabinLight) {
        this.pCabinLight = pCabinLight;
    }

    public String getCabinBenches() {
        return cabinBenches;
    }

    public void setCabinBenches(String cabinBenches) {
        this.cabinBenches = cabinBenches;
    }

    public String getHandGrips() {
        return handGrips;
    }

    public void setHandGrips(String handGrips) {
        this.handGrips = handGrips;
    }

    public String getHandRails() {
        return handRails;
    }

    public void setHandRails(String handRails) {
        this.handRails = handRails;
    }

    public String getDoorSignals() {
        return doorSignals;
    }

    public void setDoorSignals(String doorSignals) {
        this.doorSignals = doorSignals;
    }

    public String getEmergencyButtons() {
        return emergencyButtons;
    }

    public void setEmergencyButtons(String emergencyButtons) {
        this.emergencyButtons = emergencyButtons;
    }

    public String getEmergencyExitDoors() {
        return emergencyExitDoors;
    }

    public void setEmergencyExitDoors(String emergencyExitDoors) {
        this.emergencyExitDoors = emergencyExitDoors;
    }

    public String getLoudspeakers() {
        return loudspeakers;
    }

    public void setLoudspeakers(String loudspeakers) {
        this.loudspeakers = loudspeakers;
    }

    public String getAnimatedTrainMaps() {
        return animatedTrainMaps;
    }

    public void setAnimatedTrainMaps(String animatedTrainMaps) {
        this.animatedTrainMaps = animatedTrainMaps;
    }

    public String getWindows() {
        return windows;
    }

    public void setWindows(String windows) {
        this.windows = windows;
    }

    public String getTrainDoors() {
        return trainDoors;
    }

    public void setTrainDoors(String trainDoors) {
        this.trainDoors = trainDoors;
    }

    public String getAircons() {
        return aircons;
    }

    public void setAircons(String aircons) {
        this.aircons = aircons;
    }

    public MaintenanceRequestEntity getMaintenanceRequest() {
        return maintenanceRequest;
    }

    public void setMaintenanceRequest(MaintenanceRequestEntity maintenanceRequest) {
        this.maintenanceRequest = maintenanceRequest;
    }
    
    
    
}
