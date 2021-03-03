/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import infraasset.entity.DepotEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.entity.StationEntity;
import infraasset.entity.TrainCarEntity;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import operations.entity.IncidentReportEntity;
import operations.entity.InspectionReportEntity;
import operations.entity.TripReportEntity;
import operations.sessionbean.ReportSessionBeanLocal;

/**
 *
 * @author Zhirong
 */
@Named(value = "reportManagedBean")
@SessionScoped
public class ReportManagedBean implements Serializable {

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
    @EJB
    private ReportSessionBeanLocal reportSessionBeanLocal;
    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    private String staffId;
    private String firstName;
    private String lastName;

    private String role;
    private String team;
    private ArrayList<String> staffDetails;
    private String warningmessage;
    private String jobId;
    private String jobTitle;
    private String jobPosition;
    private String location;
    private double jobSalary;
    private String jobType;
    private String jobDescription;
    private String jobQualifications;
    private boolean jobStatus;

    @Temporal(value = TemporalType.DATE)
    private Date date;
    private String incidentRepId;
    private String status;
    private String signature;
    private String submitter;
    private String endorser;
    private String subject;
    private String content;
    private List<IncidentReportEntity> incidentReports;
    private List<IncidentReportEntity> filteredIncidentReps;

    private List<DepotEntity> depots;
    private List<StationEntity> stations;
    private List<String> locationResult;

    private String inspectReportId;
    private String rollingStock;
    private String reportType;
    private String remarks;
    private Date todayDate;
    //list of items in the driver cabin
    private String driverCabinLight;
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
    private List<TripReportEntity> tripReports;
    private List<TripReportEntity> filteredTripReps;
    private String tripReportId;
    private Timestamp dateTime;
    private String cabinLight;
    private String passCabinLight;
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
    private String tailLight;
    private String headLight;
    private List<InspectionReportEntity> inspectionReports;
    private List<InspectionReportEntity> filteredInspectionReps;
    private List<RollingStockEntity> rollingStocks;
    private ArrayList<String> rollingStockList;
    private String nodeCode;
    
    
    private String code;
    private List<String> codes;

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            firstName = staffDetails.get(0);
            lastName = staffDetails.get(1);
            role = staffDetails.get(14);
            if (staffDetails.size() == 19) {
                team = staffDetails.get(17);
                nodeCode = staffDetails.get(18);
            }
        }

        locationResult = new ArrayList<String>();
        depots = infraAssetSessionBeanLocal.getDepot();
        stations = infraAssetSessionBeanLocal.getStation();
        for (int i = 0; i < depots.size(); i++) {
            locationResult.add(depots.get(i).getCode());
        }
        for (int i = 0; i < stations.size(); i++) {
            locationResult.add(stations.get(i).getCode());
        }

        //generate list of rolling stocks' infra IDs
        rollingStocks = infraAssetSessionBeanLocal.getRollingStock();
        rollingStockList = new ArrayList<String>();
        for (int j = 0; j < rollingStocks.size(); j++) {
            rollingStockList.add(rollingStocks.get(j).getInfraId());
        }
        todayDate = new Date();

    }

    
    
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    
    
    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public ArrayList<String> getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(ArrayList<String> staffDetails) {
        this.staffDetails = staffDetails;
    }

    public String getWarningmessage() {
        return warningmessage;
    }

    public void setWarningmessage(String warningmessage) {
        this.warningmessage = warningmessage;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public double getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(double jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobQualifications() {
        return jobQualifications;
    }

    public void setJobQualifications(String jobQualifications) {
        this.jobQualifications = jobQualifications;
    }

    public boolean isJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(boolean jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getHeadLight() {
        return headLight;
    }

    public void setHeadLight(String headLight) {
        this.headLight = headLight;
    }

    public List<InspectionReportEntity> getInspectionReports() {
        if (role.equals("Maintenance Staff")) {
            inspectionReports = reportSessionBeanLocal.getInspectionReports(staffId);
        } else {
            //For Depot Manager 
            inspectionReports = reportSessionBeanLocal.getInspectionReports1(nodeCode);
        }
        return inspectionReports;
    }

    public void setInspectionReports(List<InspectionReportEntity> inspectionReports) {
        this.inspectionReports = inspectionReports;
    }

    public List<RollingStockEntity> getRollingStocks() {
        return rollingStocks;
    }

    public void setRollingStocks(List<RollingStockEntity> rollingStocks) {
        this.rollingStocks = rollingStocks;
    }

    public ArrayList<String> getRollingStockList() {
        return rollingStockList;
    }

    public void setRollingStockList(ArrayList<String> rollingStockList) {
        this.rollingStockList = rollingStockList;
    }

    public String getRollingStock() {
        return rollingStock;
    }

    public void setRollingStock(String rollingStock) {
        this.rollingStock = rollingStock;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIncidentRepId() {
        return incidentRepId;
    }

    public void setIncidentRepId(String incidentRepId) {
        this.incidentRepId = incidentRepId;
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

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getInspectReportId() {
        return inspectReportId;
    }

    public void setInspectReportId(String inspectReportId) {
        this.inspectReportId = inspectReportId;
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

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getDriverCabinLight() {
        return driverCabinLight;
    }

    public void setDriverCabinLight(String driverCabinLight) {
        this.driverCabinLight = driverCabinLight;
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

    public String getPassCabinLight() {
        return passCabinLight;
    }

    public void setPassCabinLight(String passCabinLight) {
        this.passCabinLight = passCabinLight;
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

    public List<DepotEntity> getDepots() {
        return depots;
    }

    public void setDepots(List<DepotEntity> depots) {
        this.depots = depots;
    }

    public List<StationEntity> getStations() {
        return stations;
    }

    public void setStations(List<StationEntity> stations) {
        this.stations = stations;
    }

    public List<IncidentReportEntity> getIncidentReports() {
        if (role.equals("Maintenance Staff") || role.equals("Station Staff") || role.equals("Train Driver")) {
            incidentReports = reportSessionBeanLocal.getIncidentReports(staffId);
        } else {
            //For Depot Manager OR HQ OR Station Manager
            System.out.println(role + " " + nodeCode + "here");
            incidentReports = reportSessionBeanLocal.getIncidentReports1(role, nodeCode);
            System.out.println(role + " " + nodeCode + "here1");
        }
        return incidentReports;
    }

    public void setIncidentReports(List<IncidentReportEntity> incidentReports) {
        this.incidentReports = incidentReports;
    }

    public List<IncidentReportEntity> getFilteredIncidentReps() {
        return filteredIncidentReps;
    }

    public void setFilteredIncidentReps(List<IncidentReportEntity> filteredIncidentReps) {
        this.filteredIncidentReps = filteredIncidentReps;
    }

    public List<InspectionReportEntity> getFilteredInspectionReps() {
        return filteredInspectionReps;
    }

    public void setFilteredInspectionReps(List<InspectionReportEntity> filteredInspectionReps) {
        this.filteredInspectionReps = filteredInspectionReps;
    }

    public List<TripReportEntity> getTripReports() {
        if (role.equals("Train Driver")) {
            tripReports = reportSessionBeanLocal.getTripReports(staffId);
        } else {
            //For Depot Manager 
            tripReports = reportSessionBeanLocal.getTripReports1(nodeCode);
        }
        return tripReports;
    }

    public void setTripReports(List<TripReportEntity> tripReports) {
        this.tripReports = tripReports;
    }

    public List<TripReportEntity> getFilteredTripReps() {
        return filteredTripReps;
    }

    public void setFilteredTripReps(List<TripReportEntity> filteredTripReps) {
        this.filteredTripReps = filteredTripReps;
    }

    public List<String> getLocationResult() {
        return locationResult;
    }

    public void setLocationResult(List<String> locationResult) {
        this.locationResult = locationResult;
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

    public String getCabinLight() {
        return cabinLight;
    }

    public void setCabinLight(String cabinLight) {
        this.cabinLight = cabinLight;
    }

    public String getTailLight() {
        return tailLight;
    }

    public void setTailLight(String tailLight) {
        this.tailLight = tailLight;
    }
    
    public void onLocationChange() {
        ArrayList cabinList = new ArrayList();
        cabinList = infraAssetSessionBeanLocal.getTrainCarList(rollingStock);
        codes = cabinList;
    }

    public String attachedRS(String inspectReportId) {
        this.inspectReportId = inspectReportId;
        InspectionReportEntity temp = (InspectionReportEntity) reportSessionBeanLocal.searchInspectReport(inspectReportId);
        String attachedRS = temp.getRollingStock().getInfraId();
        return attachedRS;
    }

    public String goAddIncidentReport() {
        this.date = null;
        this.location = null;
        this.subject = null;
        this.content = null;
        return "addIncidentRep";
    }

    public String addIncidentRep() {
        boolean status = reportSessionBeanLocal.addIncidentRep(date, location, subject, content, staffId);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Incident report is added successfully",
                            ""));
            this.date = null;
            this.location = null;
            this.subject = null;
            this.content = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add an incident report!", ""));
            return "incidentReport";
        }
        return "incidentReport";
    }

    public String updateIncidentRep() {
        boolean status1 = reportSessionBeanLocal.updateIncidentRep(incidentRepId, date, subject, content, location, signature, submitter, staffId);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Incident report is updated successfully",
                            ""));
            this.status = null;
            this.signature = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the incident report!", ""));
            return "incidentReport";
        }
        return "incidentReport";
    }

    public String goUpdateICR(String incidentRepId) {
        this.incidentRepId = incidentRepId;
        IncidentReportEntity temp = (IncidentReportEntity) reportSessionBeanLocal.searchIncidentRep(incidentRepId);
        this.submitter = temp.getSubmitter();
        this.date = temp.getDate();
        this.subject = temp.getSubject();
        this.content = temp.getContent();
        this.location = temp.getLocation();
        this.status = temp.getStatus();
        this.endorser = temp.getEndorser();
        return "updateIncidentReport";
    }

    public String goViewICR(String incidentRepId) {
        this.incidentRepId = incidentRepId;
        IncidentReportEntity temp = (IncidentReportEntity) reportSessionBeanLocal.searchIncidentRep(incidentRepId);
        this.submitter = temp.getSubmitter();
        this.date = temp.getDate();
        this.subject = temp.getSubject();
        this.content = temp.getContent();
        this.location = temp.getLocation();
        this.status = temp.getStatus();
        this.endorser = temp.getEndorser();
        this.signature = temp.getSignature();
        return "viewIncidentReport";
    }

    public String goViewTripReport(String tripReportId) {
        this.tripReportId = tripReportId;
        TripReportEntity temp = reportSessionBeanLocal.searchTripReport(tripReportId);
        this.date = temp.getDateTime();
        this.reportType = temp.getReportType();
        this.remarks = temp.getRemarks();
        this.cabinLight = temp.getCabinLight();
        this.headLight = temp.getHeadLight();
        this.tailLight = temp.getTailLight();
        this.reverser = temp.getReverser();
        this.switchPanel = temp.getSwitchPanel();
        this.horn = temp.getHorn();
        this.microphone = temp.getMicrophone();
        this.speaker = temp.getSpeaker();
        this.ledDisplay = temp.getLedDisplay();
        this.wipers = temp.getWipers();
        this.washers = temp.getWashers();
        this.submitter = temp.getDepotStaff().getStaffId();
        this.location = temp.getRollingStock().getInfraId();
        if(temp.getMaintenanceRequest() != null){
             TrainCarEntity t= (TrainCarEntity) temp.getMaintenanceRequest().getAsset() ;
            this.code = Integer.toString(t.getCarCode());
        }
        else this.code = "Not Applicable";
        return "viewTripReport";
    }

    public String goAddInspectionReport() {
        this.reportType = null;
        this.rollingStock = null;
        this.code = null;
        this.driverCabinLight = "Working";
        this.throttle = "Working";
        this.brake = "Working";
        this.reverser = "Working";
        this.switchPanel = "Working";
        this.horn = "Working";
        this.microphone = "Working";
        this.speaker = "Working";
        this.ledDisplay = "Working";
        this.wipers = "Working";
        this.washers = "Working";
        this.driverChair = "Working";
        this.passCabinLight = "Working";
        this.cabinBenches = "Working";
        this.handGrips = "Working";
        this.handRails = "Working";
        this.doorSignals = "Working";
        this.emergencyButtons = "Working";
        this.emergencyExitDoors = "Working";
        this.loudspeakers = "Working";
        this.animatedTrainMaps = "Working";
        this.windows = "Working";
        this.trainDoors = "Working";
        this.aircons = "Working";
        return "addInspectionReport";
    }

    public String addInspectionReport() {
        boolean status1 = reportSessionBeanLocal.addInspectionReport(reportType, todayDate, rollingStock, code, remarks, staffId, driverCabinLight, throttle, brake, reverser, switchPanel, horn,
                microphone, speaker, ledDisplay, wipers, washers, driverChair, passCabinLight, cabinBenches, handGrips, handRails,
                doorSignals, emergencyButtons, emergencyExitDoors, loudspeakers, animatedTrainMaps, windows, trainDoors, aircons);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Inspection report is added successfully",
                            ""));
            this.reportType = null;
            this.rollingStock = null;
            this.driverCabinLight = null;
            this.throttle = null;
            this.brake = null;
            this.reverser = null;
            this.switchPanel = null;
            this.horn = null;
            this.microphone = null;
            this.speaker = null;
            this.ledDisplay = null;
            this.wipers = null;
            this.washers = null;
            this.driverChair = null;
            this.passCabinLight = null;
            this.cabinBenches = null;
            this.handGrips = null;
            this.handRails = null;
            this.doorSignals = null;
            this.emergencyButtons = null;
            this.emergencyExitDoors = null;
            this.loudspeakers = null;
            this.animatedTrainMaps = null;
            this.windows = null;
            this.trainDoors = null;
            this.aircons = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add an inspection report!", ""));
            return "inspectionReport";
        }
        return "inspectionReport";
    }

    public String updateInspectionReport() {
        boolean status1 = reportSessionBeanLocal.updateInspectionReport(inspectReportId, staffId, signature);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Inspection report is updated successfully",
                            ""));
            this.status = null;
            this.signature = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the inspection report!", ""));
            return "inspectionReport";
        }
        return "inspectionReport";
    }

    public String goUpdateISR(String inspectReportId) {
        this.inspectReportId = inspectReportId;
        InspectionReportEntity temp = (InspectionReportEntity) reportSessionBeanLocal.searchInspectReport(inspectReportId);
        this.submitter = temp.getSubmitter();
        this.reportType = temp.getReportType();
        this.rollingStock = temp.getRollingStock().getInfraId();
        this.date = temp.getDate();
        this.status = temp.getStatus();
        this.endorser = temp.getEndorser();
        this.submitter = temp.getSubmitter();
        this.driverCabinLight = temp.getdCabinLight();
        this.throttle = temp.getThrottle();
        this.brake = temp.getBrake();
        this.reverser = temp.getReverser();
        this.switchPanel = temp.getSwitchPanel();
        this.horn = temp.getHorn();
        this.microphone = temp.getMicrophone();
        this.speaker = temp.getSpeaker();
        this.ledDisplay = temp.getLedDisplay();
        this.wipers = temp.getWipers();
        this.washers = temp.getWashers();
        this.driverChair = temp.getDriverChair();
        this.passCabinLight = temp.getpCabinLight();
        this.cabinBenches = temp.getCabinBenches();
        this.handGrips = temp.getHandGrips();
        this.handRails = temp.getHandRails();
        this.doorSignals = temp.getDoorSignals();
        this.emergencyButtons = temp.getEmergencyButtons();
        this.emergencyExitDoors = temp.getEmergencyExitDoors();
        this.loudspeakers = temp.getLoudspeakers();
        this.animatedTrainMaps = temp.getAnimatedTrainMaps();
        this.windows = temp.getWindows();
        this.trainDoors = temp.getTrainDoors();
        this.aircons = temp.getAircons();
        this.remarks = temp.getRemarks();
        if(temp.getMaintenanceRequest() != null){
             TrainCarEntity t= (TrainCarEntity) temp.getMaintenanceRequest().getAsset() ;
            this.code = Integer.toString(t.getCarCode());
        }
        else this.code = "Not Applicable";
        return "updateInspectionReport";
    }

    public String goViewISR(String inspectReportId) {
        this.inspectReportId = inspectReportId;
        InspectionReportEntity temp = (InspectionReportEntity) reportSessionBeanLocal.searchInspectReport(inspectReportId);
        this.submitter = temp.getSubmitter();
        this.reportType = temp.getReportType();
        this.rollingStock = temp.getRollingStock().getInfraId();
        this.date = temp.getDate();
        this.status = temp.getStatus();
        this.endorser = temp.getEndorser();
        this.submitter = temp.getSubmitter();
        this.signature = temp.getSignature();
        this.driverCabinLight = temp.getdCabinLight();
        this.throttle = temp.getThrottle();
        this.brake = temp.getBrake();
        this.reverser = temp.getReverser();
        this.switchPanel = temp.getSwitchPanel();
        this.horn = temp.getHorn();
        this.microphone = temp.getMicrophone();
        this.speaker = temp.getSpeaker();
        this.ledDisplay = temp.getLedDisplay();
        this.wipers = temp.getWipers();
        this.washers = temp.getWashers();
        this.driverChair = temp.getDriverChair();
        this.passCabinLight = temp.getpCabinLight();
        this.cabinBenches = temp.getCabinBenches();
        this.handGrips = temp.getHandGrips();
        this.handRails = temp.getHandRails();
        this.doorSignals = temp.getDoorSignals();
        this.emergencyButtons = temp.getEmergencyButtons();
        this.emergencyExitDoors = temp.getEmergencyExitDoors();
        this.loudspeakers = temp.getLoudspeakers();
        this.animatedTrainMaps = temp.getAnimatedTrainMaps();
        this.windows = temp.getWindows();
        this.trainDoors = temp.getTrainDoors();
        this.aircons = temp.getAircons();
        this.remarks = temp.getRemarks();
        if(temp.getMaintenanceRequest() != null){
             TrainCarEntity t= (TrainCarEntity) temp.getMaintenanceRequest().getAsset() ;
            this.code = Integer.toString(t.getCarCode());
        }
        else this.code = "Not Applicable";
        return "viewInspectionReport";
    }

    public String goAddTripReport() {
        this.reportType = null;
        this.rollingStock = null;
        this.code = null;
        this.remarks = null;
        this.cabinLight = "Working";
        this.reverser = "Working";
        this.switchPanel = "Working";
        this.horn = "Working";
        this.microphone = "Working";
        this.speaker = "Working";
        this.ledDisplay = "Working";
        this.wipers = "Working";
        this.washers = "Working";
        this.headLight = "Working";
        this.tailLight = "Working";
        return "addTripReport";
    }

    public String addTripReport() {
        boolean status1 = reportSessionBeanLocal.addTripReport(reportType, todayDate, rollingStock,code, remarks, staffId, cabinLight, headLight, tailLight, reverser, switchPanel, horn,
                microphone, speaker, ledDisplay, wipers, washers);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Trip report is added successfully",
                            ""));
            this.reportType = null;
            this.rollingStock = null;
            this.remarks = null;
            this.cabinLight = null;
            this.headLight = null;
            this.tailLight = null;
            this.reverser = null;
            this.switchPanel = null;
            this.horn = null;
            this.microphone = null;
            this.speaker = null;
            this.ledDisplay = null;
            this.wipers = null;
            this.washers = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a trip report!", ""));
            return "tripReport";
        }
        return "tripReport";
    }

}
