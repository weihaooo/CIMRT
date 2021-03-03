/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.entity.TeamEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.entity.StationEntity;
import infraasset.entity.TrainCarEntity;
import java.io.Serializable;
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
import operations.entity.ServiceLogEntity;
import operations.entity.JobApplicationsEntity;
import operations.entity.JobOfferEntity;
import operations.sessionbean.OperationsSessionBeanLocal;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import maintenance.entity.MaintenanceReportEntity;
import maintenance.sessionbean.MaintenanceSessionBeanLocal;
import operations.entity.MaintenanceRequestEntity;
import operations.entity.StaffContractEntity;
import org.primefaces.event.SelectEvent;
import routefare.entity.NodeEntity;


/*
 * @author Zhirong
 */
@Named(value = "operationsManagedBean")
@SessionScoped
public class OperationsManagedBean implements Serializable {

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
    @EJB
    private OperationsSessionBeanLocal OperationsSessionBeanLocal;
    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;
    @EJB
    private MaintenanceSessionBeanLocal maintenanceSessionBeanLocal;

    private String subject;
    private String content;
    private List<ServiceLogEntity> svcLogs;
    private String staffId;
    private String firstName;
    private String lastName;
    private String role;
    private String team;
    private String mcEntitlement;
    private String leaveEntitlement;
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
    private Date postedDate;
    private Date jobDeadline;

    private String applicationId;
    private String appStatus;

    private List<DepotEntity> depots;
    private List<StationEntity> stations;
    private List<LeasingSpaceEntity> leaseSpaceList;
    private List<LeasingSpaceEntity> filteredLeasingSpace;
    private List<NodeAssetEntity> nodeAssetList;
    private List<NodeAssetEntity> filteredNode;
    private List<RollingStockAssetEntity> rollingAssetList;
    private List<RollingStockAssetEntity> filteredRollingStock;
    private List<ConsumableAssetEntity> consumAssetList;
    private List<ConsumableAssetEntity> filteredConsumableAsset;
    private List<AdvertisementSpaceEntity> adverSpaceList;
    private List<AdvertisementSpaceEntity> filteredAdvertSpace;
    private List<JobOfferEntity> jobOfferList;
    private List<JobApplicationsEntity> applicationList;
    private List<ServiceLogEntity> filteredSvcLogs;
    private List<AssetRequestEntity> assetRequestList;
    private List<AssetRequestEntity> filteredAssetRequestList;

    private String assetRequestType;
    private List<String> assetList;
    private List<String> assetTypeList;
    private List<String> nodeAssetTypeList;
    private List<String> consumableAssetTypeList;
    private List<String> rollingStockAssetTypeList;
    private List<String> rollingStocksList;
    private List<String> rollingStocksList1;
    private String assetName;
    private String remark;
    private int qty;
    private String assetType;
    private String infraId;
    private List<RollingStockEntity> rollingStocks;

    private String mainReqType;
    private String mainReqId;
    private String mainReqStatus;
    private String code;
    private List<MaintenanceRequestEntity> maintenanceRequestList;
    private List<MaintenanceRequestEntity> maintenanceRequestListByDepot;
    private List<MaintenanceRequestEntity> filteredmaintenanceRequestListByDepot;
    private List<MaintenanceRequestEntity> maintenanceRequestList1;
    private List<MaintenanceRequestEntity> filteredmaintenanceRequestList1;
    private List<String> hqList;
    private List<String> depotList;
    private List<String> stationList;
    private List<String> roleNames;
    private Date todayDate;
    private String nodeCode;
    private List<MaintenanceReportEntity> specificReport;
    private Long maintenanceReportId;
    private String rptTitle;
    private String reportDescription;
    private Timestamp reportDate;
    private String asset1;
    private String qty1;
    private String maintenanceStatus;
    private String submitterId;
    private String submitterName;
    private String emailAddress;
    private String assetDetails;
    private String tempAssetType;
    private List<NodeEntity> managerStationsInCharge;
    private Date startDate;
    private Date endDate;

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            firstName = staffDetails.get(0);
            lastName = staffDetails.get(1);
            role = staffDetails.get(14);
            mcEntitlement = staffDetails.get(15);
            leaveEntitlement = staffDetails.get(16);
            emailAddress = staffDetails.get(4);
            if (staffDetails.size() == 19) {
                team = staffDetails.get(17);
                nodeCode = staffDetails.get(18);
            }
        }

        nodeAssetTypeList = new ArrayList();
        nodeAssetTypeList.add("Office Equipments");
        nodeAssetTypeList.add("Operating Equipments");
        nodeAssetTypeList.add("Audio Visual Equipments");
        nodeAssetTypeList.add("Photographic Equipments");
        nodeAssetTypeList.add("Maintenance Equipments");
        nodeAssetTypeList.add("Computer Equipments");
        nodeAssetTypeList.add("Decorations");
        nodeAssetTypeList.add("Others");

        consumableAssetTypeList = new ArrayList();
        consumableAssetTypeList.add("Office Supplies");
        consumableAssetTypeList.add("Cleaning Supplies");
        consumableAssetTypeList.add("Food & Beverage Supplies");
        consumableAssetTypeList.add("Medicine Supplies");
        consumableAssetTypeList.add("Others");

        rollingStockAssetTypeList = new ArrayList();
        rollingStockAssetTypeList.add("Audio Visual Equipments");
        rollingStockAssetTypeList.add("System Equipements");
        rollingStockAssetTypeList.add("Operating Equipments");
        rollingStockAssetTypeList.add("Medicine Supplies");
        rollingStockAssetTypeList.add("Others");

        stationList = new ArrayList<String>();
        stationList.add("Station Manager");
        stationList.add("Station Staff");

        depotList = new ArrayList<String>();
        depotList.add("Depot Manager");
        depotList.add("Train Driver");
        depotList.add("Maintenance Staff");

        hqList = new ArrayList<String>();
        hqList.add("Super Admin");
        hqList.add("System Admin");
        hqList.add("Route & Fare Manager");
        hqList.add("Data Analyst");
        hqList.add("I&A Manager");
        hqList.add("I&A Staff");
        hqList.add("Advertising Staff");
        hqList.add("Advertising Manager");
        hqList.add("Commercial Staff");
        hqList.add("Commercial Manager");
        hqList.add("HR");

        OperationsSessionBeanLocal.checkJob();

    }

    public Long getMaintenanceReportId() {
        return maintenanceReportId;
    }

    public void setMaintenanceReportId(Long maintenanceReportId) {
        this.maintenanceReportId = maintenanceReportId;
    }

    public List<MaintenanceReportEntity> getSpecificReport() {
        System.out.println("MaintenanceReport ID here is :" + maintenanceReportId);
        specificReport = maintenanceSessionBeanLocal.getSpecificReport(maintenanceReportId);
        return specificReport;
    }

    public void setSpecificReport(List<MaintenanceReportEntity> specificReport) {
        this.specificReport = specificReport;
    }

    public String goCreateMaintenanceReport(String mainReqId, String remark, String details) {
        this.mainReqId = mainReqId;
        this.remark = remark;
        this.assetDetails = details;

        String[] s = assetDetails.split(" ");
        String assetId = s[1];
        AssetEntity asset = maintenanceSessionBeanLocal.searchAsset(assetId);
        if (asset instanceof RollingStockAssetEntity) {
            this.tempAssetType = "RollingStockAssetEntity";
            return "createMaintenanceReport?faces-redirect=true";
        } else if (asset instanceof NodeAssetEntity) {
            this.tempAssetType = "NodeAssetEntity";
            return "createMaintenanceReport?faces-redirect=true";
        } else if (asset instanceof TrainCarEntity) {
            this.tempAssetType = "TrainCarEntity";
            return "createMaintenanceReport?faces-redirect=true";
        }

        return "createMaintenanceReport?faces-redirect=true";
    }

    public String viewMaintenanceReport(String mainReqId) {
        this.mainReqId = mainReqId;
        MaintenanceRequestEntity mre = maintenanceSessionBeanLocal.searchMaintenanceRequest(mainReqId);
        Long reportId = mre.getMaintenanceReport().getMaintenanceReportId();
        System.out.println("reportId passed here: " + reportId);
        this.maintenanceReportId = reportId;
        this.specificReport = getSpecificReport();
        for (int i = 0; i < specificReport.size(); i++) {
            this.maintenanceReportId = specificReport.get(i).getMaintenanceReportId();
            this.rptTitle = specificReport.get(i).getRptTitle();
            this.reportDescription = specificReport.get(i).getReportDescription();
            this.asset1 = specificReport.get(i).getAssetUsed();
            this.qty1 = Integer.toString(specificReport.get(i).getQuantityAssetUsed());
            this.maintenanceStatus = specificReport.get(i).getMaintenanceStatus();
            this.submitterId = specificReport.get(i).getSubmitterId();
            this.submitterName = specificReport.get(i).getSubmitterName();
            this.reportDate = specificReport.get(i).getReportDateTime();
            return "viewFullReport?faces-redirect=true";
        }

        return "viewFullReport?faces-redirect=true";
    }

    public List<NodeEntity> getManagerStationsInCharge() {
        managerStationsInCharge = OperationsSessionBeanLocal.getStationsInCharge(staffId);
        return managerStationsInCharge;
    }

    public void setManagerStationsInCharge(List<NodeEntity> managerStationsInCharge) {
        this.managerStationsInCharge = managerStationsInCharge;
    }

    public List<MaintenanceRequestEntity> getMaintenanceRequestListByDepot() {
        List<MaintenanceRequestEntity> temp = new ArrayList<MaintenanceRequestEntity>();
        maintenanceRequestListByDepot = new ArrayList<MaintenanceRequestEntity>();
        managerStationsInCharge = getManagerStationsInCharge();
        temp = OperationsSessionBeanLocal.getMaintenanceRequestList();
        String tempCode = "";

        for (int k = 0; k < managerStationsInCharge.size(); k++) {
            for (int i = 0; i < temp.size(); i++) {
                MaintenanceRequestEntity m = OperationsSessionBeanLocal.searchMaintenaceRequest(temp.get(i).getMainReqId());
                //System.out.println("Checking here at operation manage bean: " + m.getMainReqId() + " " + m.getAsset().getAssetId());
                if (m.getAsset().getAssetId().substring(0, 3).equals("RSA")) {
                    if (m.getAsset().getInfrastructure() instanceof RollingStockEntity) {
                        RollingStockEntity rse = (RollingStockEntity) m.getAsset().getInfrastructure();
                        tempCode = rse.getDepot().getCode();
                    }
                    //System.out.println("tempCode @ RSA: " + tempCode);
                } else if (m.getAsset().getAssetId().substring(0, 2).equals("NA")) {
                    NodeEntity na = (NodeEntity) m.getAsset().getInfrastructure();
                    tempCode = na.getCode();
                    //System.out.println("tempCode @ NA: " + tempCode);
                } else if (m.getAsset().getAssetId().substring(0, 2).equals("TC")) {
                    TrainCarEntity tc = (TrainCarEntity) m.getAsset();
                    InfrastructureEntity infra = tc.getInfrastructure();
                    NodeEntity depot = (NodeEntity) infra;
                    tempCode = depot.getCode();
                    //System.out.println("tempCode @ AS: " + tempCode);
                }
                if (tempCode.equals(managerStationsInCharge.get(k).getCode())) {
                    maintenanceRequestListByDepot.add(temp.get(i));
                }
            }
        }

        return maintenanceRequestListByDepot;
    }

    public void setMaintenanceRequestListByDepot(List<MaintenanceRequestEntity> maintenanceRequestListByDepot) {
        this.maintenanceRequestListByDepot = maintenanceRequestListByDepot;
    }

    public List<MaintenanceRequestEntity> getMaintenanceRequestList() {
        //HQ and maintenance Staff
        maintenanceRequestList = OperationsSessionBeanLocal.getMaintenanceRequestList();
        return maintenanceRequestList;
    }

    public void setMaintenanceRequestList(List<MaintenanceRequestEntity> maintenanceRequestList) {
        this.maintenanceRequestList = maintenanceRequestList;
    }

    public List<MaintenanceRequestEntity> getMaintenanceRequestList1() {
        if ((role.equals("Train Driver")) || (role.equals("Station Staff"))) {
            maintenanceRequestList1 = OperationsSessionBeanLocal.getMaintenanceRequestList2(staffId);
        } else if (role.equals("Maintenance Staff")) {
            maintenanceRequestList1 = OperationsSessionBeanLocal.getMaintenanceRequestList();
        } else {
            //view by the station/depot code which is the depot manager or station manager or HQ
            maintenanceRequestList1 = OperationsSessionBeanLocal.getMaintenanceRequestList1(nodeCode);
        }
        return maintenanceRequestList1;
    }

    public void setMaintenanceRequestList1(List<MaintenanceRequestEntity> maintenanceRequestList1) {
        this.maintenanceRequestList1 = maintenanceRequestList1;
    }

    public String getAssetDetails() {
        return assetDetails;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public String getTempAssetType() {
        return tempAssetType;
    }

    public void setTempAssetType(String tempAssetType) {
        this.tempAssetType = tempAssetType;
    }

    public String getRptTitle() {
        return rptTitle;
    }

    public void setRptTitle(String rptTitle) {
        this.rptTitle = rptTitle;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public Timestamp getReportDate() {
        return reportDate;
    }

    public void setReportDate(Timestamp reportDate) {
        this.reportDate = reportDate;
    }

    public String getAsset1() {
        return asset1;
    }

    public void setAsset1(String asset1) {
        this.asset1 = asset1;
    }

    public String getQty1() {
        return qty1;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getInfraId() {
        return infraId;
    }

    public void setInfraId(String infraId) {
        this.infraId = infraId;
    }

    public List<RollingStockEntity> getRollingStocks() {
        rollingStocks = OperationsSessionBeanLocal.getRollingStock(team);
        return rollingStocks;
    }

    public void setRollingStocks(List<RollingStockEntity> rollingStocks) {
        this.rollingStocks = rollingStocks;
    }

    public List<String> getRollingStocksList1() {
        rollingStocksList1 = OperationsSessionBeanLocal.getRollingStockList();
        return rollingStocksList1;
    }

    public void setRollingStocksList1(List<String> rollingStocksList1) {
        this.rollingStocksList1 = rollingStocksList1;
    }

    public List<String> getRollingStocksList() {
        return rollingStocksList;
    }

    public void setRollingStocksList(List<String> rollingStocksList) {
        this.rollingStocksList = rollingStocksList;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public InfraAssetSessionBeanLocal getInfraAssetSessionBeanLocal() {
        return infraAssetSessionBeanLocal;
    }

    public void setInfraAssetSessionBeanLocal(InfraAssetSessionBeanLocal infraAssetSessionBeanLocal) {
        this.infraAssetSessionBeanLocal = infraAssetSessionBeanLocal;
    }

    public String getAssetRequestType() {
        return assetRequestType;
    }

    public void setAssetRequestType(String assetRequestType) {
        this.assetRequestType = assetRequestType;
    }

    public List<String> getAssetList() {
        return assetList;
    }

    public void setAssetList(List<String> assetList) {
        this.assetList = assetList;
    }

    public List<String> getAssetTypeList() {
        return assetTypeList;
    }

    public void setAssetTypeList(List<String> assetTypeList) {
        this.assetTypeList = assetTypeList;
    }

    public List<String> getNodeAssetTypeList() {
        return nodeAssetTypeList;
    }

    public void setNodeAssetTypeList(List<String> nodeAssetTypeList) {
        this.nodeAssetTypeList = nodeAssetTypeList;
    }

    public List<String> getConsumableAssetTypeList() {
        return consumableAssetTypeList;
    }

    public void setConsumableAssetTypeList(List<String> consumableAssetTypeList) {
        this.consumableAssetTypeList = consumableAssetTypeList;
    }

    public List<String> getRollingStockAssetTypeList() {
        return rollingStockAssetTypeList;
    }

    public void setRollingStockAssetTypeList(List<String> rollingStockAssetTypeList) {
        this.rollingStockAssetTypeList = rollingStockAssetTypeList;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Date getTodayDate() {
        GregorianCalendar cal = new GregorianCalendar();
        Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DATE, 1);
        todayDate = cal.getTime();
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
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

    public List<ServiceLogEntity> getSvcLogs() {
        if (role.equals("Station Staff")) {
            svcLogs = OperationsSessionBeanLocal.getSvcLogs(staffId);
        } else {
            svcLogs = OperationsSessionBeanLocal.getSvcLogs1(nodeCode);
        }
        return svcLogs;
    }

    public void setServiceLogs(List<ServiceLogEntity> svcLogs) {
        this.svcLogs = svcLogs;
    }

    public String getWarningmessage() {
        return warningmessage;
    }

    public void setWarningmessage(String warningmessage) {
        this.warningmessage = warningmessage;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getMcEntitlement() {
        return mcEntitlement;
    }

    public void setMcEntitlement(String mcEntitlement) {
        this.mcEntitlement = mcEntitlement;
    }

    public String getLeaveEntitlement() {
        return leaveEntitlement;
    }

    public void setLeaveEntitlement(String leaveEntitlement) {
        this.leaveEntitlement = leaveEntitlement;
    }

    public ArrayList<String> getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(ArrayList<String> staffDetails) {
        this.staffDetails = staffDetails;
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(double jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Date getJobDeadline() {
        return jobDeadline;
    }

    public void setJobDeadline(Date jobDeadline) {
        this.jobDeadline = jobDeadline;
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

    public List<AssetRequestEntity> getAssetRequestList() {
        assetRequestList = OperationsSessionBeanLocal.getAssetRequest(nodeCode);
        return assetRequestList;
    }

    public void setAssetRequestList(List<AssetRequestEntity> assetRequestList) {
        this.assetRequestList = assetRequestList;
    }

    public List<LeasingSpaceEntity> getLeaseSpaceList() {
        leaseSpaceList = OperationsSessionBeanLocal.getLeasingSpace(nodeCode);
        return leaseSpaceList;
    }

    public void setLeaseSpaceList(List<LeasingSpaceEntity> leaseSpaceList) {
        this.leaseSpaceList = leaseSpaceList;
    }

    public List<NodeAssetEntity> getNodeAssetList() {
        nodeAssetList = OperationsSessionBeanLocal.getNodeAsset(nodeCode);
        return nodeAssetList;
    }

    public void setNodeAssetList(List<NodeAssetEntity> nodeAssetList) {
        this.nodeAssetList = nodeAssetList;
    }

    public List<RollingStockAssetEntity> getRollingAssetList() {
        rollingAssetList = OperationsSessionBeanLocal.getRollingStockAsset(nodeCode);
        return rollingAssetList;
    }

    public void setRollingAssetList(List<RollingStockAssetEntity> rollingAssetList) {
        this.rollingAssetList = rollingAssetList;
    }

    public List<ConsumableAssetEntity> getConsumAssetList() {
        consumAssetList = OperationsSessionBeanLocal.getConsumableAsset(nodeCode);
        return consumAssetList;
    }

    public void setConsumAssetList(List<ConsumableAssetEntity> consumAssetList) {
        this.consumAssetList = consumAssetList;
    }

    public List<AdvertisementSpaceEntity> getAdverSpaceList() {
        adverSpaceList = OperationsSessionBeanLocal.getAdvertisementSpace(nodeCode);
        return adverSpaceList;
    }

    public void setAdverSpaceList(List<AdvertisementSpaceEntity> adverSpaceList) {
        this.adverSpaceList = adverSpaceList;
    }

    public List<JobOfferEntity> getJobOfferList() {
        jobOfferList = OperationsSessionBeanLocal.getJobOffer();
        return jobOfferList;
    }

    public void setJobOfferList(List<JobOfferEntity> jobOfferList) {
        this.jobOfferList = jobOfferList;
    }

    public List<JobApplicationsEntity> getApplicationList() {
        return applicationList;
    }

    public void setApplicationList(List<JobApplicationsEntity> applicationList) {
        this.applicationList = applicationList;
    }

    public List<ServiceLogEntity> getFilteredSvcLogs() {
        return filteredSvcLogs;
    }

    public void setFilteredSvcLogs(List<ServiceLogEntity> filteredSvcLogs) {
        this.filteredSvcLogs = filteredSvcLogs;
    }

    public String getMainReqType() {
        return mainReqType;
    }

    public void setMainReqType(String mainReqType) {
        this.mainReqType = mainReqType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMainReqId() {
        return mainReqId;
    }

    public void setMainReqId(String mainReqId) {
        this.mainReqId = mainReqId;
    }

    public String getMainReqStatus() {
        return mainReqStatus;
    }

    public void setMainReqStatus(String mainReqStatus) {
        this.mainReqStatus = mainReqStatus;
    }

    public List<String> getHqList() {
        return hqList;
    }

    public void setHqList(List<String> hqList) {
        this.hqList = hqList;
    }

    public List<String> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<String> depotList) {
        this.depotList = depotList;
    }

    public List<String> getStationList() {
        return stationList;
    }

    public void setStationList(List<String> stationList) {
        this.stationList = stationList;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void onLocationChange1() {
        if (location != null && !location.equals("")) {
            if (location.equals("Station")) {
                roleNames = stationList;
            } else if (location.equals("Depot")) {
                roleNames = depotList;
            } else {
                roleNames = hqList;
            }
        }
    }

    public void onTypeChange1() {
        rollingStocks = getRollingStocks();
        rollingStocksList = new ArrayList<String>();
        for (int i = 0; i < rollingStocks.size(); i++) {
            rollingStocksList.add(rollingStocks.get(i).getInfraId());
        }
    }

    public void onTypeChange() {
        if (assetRequestType != null && !assetRequestType.equals("")) {
            assetList = new ArrayList();
            boolean statuss = false;
            if (assetRequestType.equals("Consumable Asset")) {
                ArrayList<ConsumableAssetEntity> consumableAssetList = infraAssetSessionBeanLocal.getConsumableAsset();
                for (int i = 0; i < consumableAssetList.size(); i++) {
                    statuss = false;
                    if (consumableAssetList.get(i).getConsumableAssetType().equals(assetType)) {
                        if (assetList.isEmpty()) {
                            assetList.add(consumableAssetList.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(consumableAssetList.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(consumableAssetList.get(i).getAssetName());
                        }
                    }
                }
            } else if (assetRequestType.equals("Node Asset")) {
                ArrayList<NodeAssetEntity> nodeAssetLists = infraAssetSessionBeanLocal.getNodeAsset();
                for (int i = 0; i < nodeAssetLists.size(); i++) {
                    statuss = false;
                    if (nodeAssetLists.get(i).getNodeAssetType().equals(assetType)) {
                        if (assetList.isEmpty()) {
                            assetList.add(nodeAssetLists.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(nodeAssetLists.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(nodeAssetLists.get(i).getAssetName());
                        }
                    }
                }
            } else {
                ArrayList<RollingStockAssetEntity> rollingStockAssetList = infraAssetSessionBeanLocal.getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList.size(); i++) {
                    statuss = false;
                    if (rollingStockAssetList.get(i).getRollingStockAssetType().equals(assetType)) {
                        if (assetList.isEmpty()) {
                            assetList.add(rollingStockAssetList.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(rollingStockAssetList.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(rollingStockAssetList.get(i).getAssetName());
                        }
                    }
                }
            }
        }
    }

    //get the asset based on the station/Depot
    public void onTypeChange3() {
        NodeEntity nodeId = infraAssetSessionBeanLocal.searchNode(nodeCode);
        if (assetRequestType != null && !assetRequestType.equals("")) {
            assetList = new ArrayList();
            boolean statuss = false;
            if (assetRequestType.equals("Node Asset")) {
                System.out.println("CHECKECHECKDHUFHIUSEHGOIRH");
                ArrayList<NodeAssetEntity> nodeAssetLists = infraAssetSessionBeanLocal.getNodeAsset();
                for (int i = 0; i < nodeAssetLists.size(); i++) {
                    statuss = false;
                    if (nodeAssetLists.get(i).getNodeAssetType().equals(assetType) && nodeAssetLists.get(i).getInfrastructure().getInfraId().equals(nodeId.getInfraId())) {
                        if (assetList.isEmpty()) {
                            assetList.add(nodeAssetLists.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(nodeAssetLists.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(nodeAssetLists.get(i).getAssetName());
                        }
                    }
                }
            } else {
                ArrayList<RollingStockAssetEntity> rollingStockAssetList = infraAssetSessionBeanLocal.getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList.size(); i++) {
                    statuss = false;
                    RollingStockEntity r = (RollingStockEntity) rollingStockAssetList.get(i).getInfrastructure();
                    if (rollingStockAssetList.get(i).getRollingStockAssetType().equals(assetType) && r.getDepot().getInfraId().equals(nodeId.getInfraId())) {
                        if (assetList.isEmpty()) {
                            assetList.add(rollingStockAssetList.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(rollingStockAssetList.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(rollingStockAssetList.get(i).getAssetName());
                        }
                    }
                }
            }
        }
    }

    public void onTypeChange2() {
        assetTypeList = null;
        if (assetRequestType != null && !assetRequestType.equals("")) {
            if (assetRequestType.equals("Consumable Asset")) {
                assetTypeList = consumableAssetTypeList;
            } else if (assetRequestType.equals("Node Asset")) {
                assetTypeList = nodeAssetTypeList;
            } else if (assetRequestType.equals("Rolling Stock Asset")) {
                assetTypeList = rollingStockAssetTypeList;
            }
        }
    }

    public void goRollingCode(String infraId) {
        RollingStockEntity rs = OperationsSessionBeanLocal.searchRollingStock(infraId);
        this.code = rs.getDepot().getCode();
    }

    public String submitAssetRequest() {
        boolean sta = OperationsSessionBeanLocal.submitAssetRequest(team, infraId, assetRequestType, assetType, assetName, qty, remark);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Item request is submitted successfully",
                            ""));
            this.assetRequestType = null;
            this.assetName = null;
            this.qty = 0;
            this.remark = null;
            this.assetType = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to submit the item request!"));
            return "assetManegement?faces-redirect=true";
        }
        return "assetManagement?faces-redirect=true";
    }

    public String addSvcLog() {
        boolean status = OperationsSessionBeanLocal.addSvcLog(subject, content, staffId);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Service Log is added successfully",
                            ""));
            this.subject = null;
            this.content = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a Service Log!", ""));
            return "serviceLog?faces-redirect=true";
        }
        return "serviceLog?faces-redirect=true";
    }

    public String createJob() {
        Date date = new Date();
        boolean sta = OperationsSessionBeanLocal.createJob(staffId, jobTitle, jobPosition, location, jobType, jobSalary, jobDescription, jobQualifications, date, jobDeadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Job is created successfully",
                            ""));
            this.jobTitle = null;
            this.jobPosition = null;
            this.location = null;
            this.jobType = null;
            this.jobSalary = 0;
            this.jobDescription = null;
            this.jobQualifications = null;
            this.jobDeadline = null;

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to create job!", ""));
            return "jobOffer?faces-redirect=true";
        }
        return "jobOffer?faces-redirect=true";
    }

    public String updateJob() {
        boolean sta = OperationsSessionBeanLocal.updateJob(jobId, jobTitle, jobPosition, location, jobType, jobSalary, jobDescription, jobQualifications, postedDate, jobDeadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Job is updated successfully",
                            ""));
            this.jobId = null;
            this.jobTitle = null;
            this.jobPosition = null;
            this.location = null;
            this.jobType = null;
            this.jobSalary = 0;
            this.jobDescription = null;
            this.jobQualifications = null;
            this.postedDate = null;
            this.jobDeadline = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the job!", ""));
            return "jobOffer?faces-redirect=true";
        }
        return "jobOffer?faces-redirect=true";
    }

    public String goEditJob(String jobId) {
        this.jobId = jobId;
        JobOfferEntity temp = OperationsSessionBeanLocal.searchJob(jobId);
        this.jobId = temp.getJobId();
        this.jobTitle = temp.getJobTitle();
        this.jobPosition = temp.getJobPosition();
        this.location = temp.getLocation();
        this.jobType = temp.getJobType();
        this.jobSalary = temp.getSalary();
        this.jobDescription = temp.getJobDescription();
        this.jobQualifications = temp.getJobQualifications();
        this.postedDate = temp.getPostedDate();
        this.jobDeadline = temp.getJobDeadline();
        return "editJobOffer";
    }

    public String goEditJobApp(String applicationId) {
        this.applicationId = applicationId;
        JobApplicationsEntity temp = OperationsSessionBeanLocal.searchJobApp(applicationId);
        this.appStatus = temp.getAppStatus();
        return "editJobApp";
    }

    public String goJobStatus(String jobId) {
        this.jobId = jobId;
        boolean sta = OperationsSessionBeanLocal.goJobStatus(jobId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Job status is updated successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the job status!", ""));
            return "jobOffer?faces-redirect=true";
        }
        return "jobOffer?faces-redirect=true";
    }

    public String goCreatJob() {
        this.jobId = null;
        this.jobTitle = null;
        this.jobPosition = null;
        this.location = null;
        this.jobType = null;
        this.jobSalary = 0;
        this.jobDescription = null;
        this.jobQualifications = null;
        this.postedDate = null;
        return "createJob";

    }

    public String goViewJobApplicant(String jobId) {
        ArrayList<JobApplicationsEntity> applications = OperationsSessionBeanLocal.getJobApplication(jobId, emailAddress);
        applicationList = new ArrayList();
        this.applicationList = applications;
        return "viewApplicants";
    }

    public String updateJobApp() {
        boolean sta1 = OperationsSessionBeanLocal.updateJobApp(applicationId, appStatus, emailAddress);
        JobApplicationsEntity temp = OperationsSessionBeanLocal.searchJobApp(applicationId);
        System.out.println(temp.getJobOffer().getJobId());
        ArrayList<JobApplicationsEntity> applications = OperationsSessionBeanLocal.getJobApplication(temp.getJobOffer().getJobId(), emailAddress);
        applicationList = new ArrayList();
        this.applicationList = applications;
        if (sta1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Job applicant is updated successfully",
                            ""));
            this.applicationId = null;
            this.appStatus = null;

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the job applicant!", ""));
            return "viewApplicants?faces-redirect=true";
        }
        return "viewApplicants?faces-redirect=true";
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Car Selected", ((JobOfferEntity) event.getObject()).getJobId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String submitMaintenanceRequest() {
        Date now = new Date();
        boolean sta = OperationsSessionBeanLocal.submitMaintenanceRequest(team, staffId, assetRequestType, infraId, assetType, assetName, mainReqType, remark, now);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance request is submitted successfully",
                            ""));
            this.mainReqType = null;
            this.assetRequestType = null;
            this.infraId = null;
            this.remark = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to submit the maintenance request!"));
            return "viewMaintenanceRequest1?faces-redirect=true";
        }
        return "viewMaintenanceRequest1?faces-redirect=true";
    }

    public String goDeleteMR(String mainReqId) {
        this.mainReqId = mainReqId;
        boolean sta = OperationsSessionBeanLocal.delRequest(mainReqId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance Request is deleted successfully",
                            ""));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the Maintenance Request!", ""));
            return "viewMaintenanceRequest1?faces-redirect=true";
        }
        return "viewMaintenanceRequest1?faces-redirect=true";
    }

    public String goEditRS(String mainReqId) {
        this.mainReqId = mainReqId;
        MaintenanceRequestEntity temp = OperationsSessionBeanLocal.searchMaintenaceRequest(mainReqId);
        this.mainReqStatus = temp.getMainReqStatus();
        return "editMaintenanceRequest";
    }

    public String goEditRS1(String mainReqId) {
        this.mainReqId = mainReqId;
        MaintenanceRequestEntity temp = OperationsSessionBeanLocal.searchMaintenaceRequest(mainReqId);
        this.mainReqStatus = temp.getMainReqStatus();
        this.postedDate = temp.getRequestDate();
        this.mainReqType = temp.getMainReqType();
        this.remark = temp.getRemark();
        TeamEntity teamentity = OperationsSessionBeanLocal.searchTeam(team);
        this.location = teamentity.getNode().getCode();
        this.assetName = temp.getAsset().getAssetName();
        return "editMaintenanceRequest1";
    }

    public String getDetails(String mainReqId) {
        this.mainReqId = mainReqId;
        MaintenanceRequestEntity m = OperationsSessionBeanLocal.searchMaintenaceRequest(mainReqId);
        if (m.getAsset().getAssetId().substring(0, 3).equals("RSA")) {
            RollingStockEntity rse = (RollingStockEntity) m.getAsset().getInfrastructure();
            return rse.getDepot().getCode() + " " + m.getAsset().getAssetId() + " " + m.getAsset().getAssetName();
        } else if (m.getAsset().getAssetId().substring(0, 2).equals("NA")) {
            NodeEntity nae = (NodeEntity) m.getAsset().getInfrastructure();
            return nae.getCode() + " " + m.getAsset().getAssetId() + " " + m.getAsset().getAssetName();
        } //For train car
        else {
            TrainCarEntity tc = (TrainCarEntity) m.getAsset();
            InfrastructureEntity infra = tc.getInfrastructure();
            NodeEntity depot = (NodeEntity) infra;
            return depot.getCode() + " " + tc.getAssetId() + " Car Code:  " + tc.getCarCode();
        }
    }

    public String updateMaintenanceRequest() {
        boolean sta = OperationsSessionBeanLocal.updateMaintenanceRequest(mainReqId, mainReqStatus);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance request is updated successfully",
                            ""));
            this.mainReqId = null;
            this.mainReqStatus = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the job!", ""));
            return "viewMaintenanceRequest?faces-redirect=true";
        }
        return "viewMaintenanceRequest?faces-redirect=true";
    }

    public String updateMaintenanceRequest1() {
        boolean sta = OperationsSessionBeanLocal.updateMaintenanceRequest1(mainReqId, mainReqType, remark);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance Request is updated successfully",
                            ""));
            this.mainReqId = null;
            this.mainReqStatus = null;
            this.postedDate = null;
            this.mainReqType = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the job!", ""));
            return "viewMaintenanceRequest1?faces-redirect=true";
        }
        return "viewMaintenanceRequest1?faces-redirect=true";
    }

    public String goScheduleContract(String appliciantId) {
        this.applicationId = appliciantId;
        return "staffContract";
    }

    public String getScheduleContract(String appliciantId) {
        StaffContractEntity temp = OperationsSessionBeanLocal.getStaffContract(appliciantId);
        if (temp == null) {
            return null;
        } else {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm");
            String contractStart = DATE_FORMAT.format(temp.getInterviewFrom());
            String contractEnd = DATE_FORMAT.format(temp.getInterviewTo());
            String details = contractStart + "\nto\n" + contractEnd;
            return details;
        }
    }

    public String createContract() {
        boolean sta = OperationsSessionBeanLocal.createContract(applicationId, startDate, endDate, emailAddress);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Signing of contract is scheduled successfully",
                            ""));
            this.applicationId = null;
            this.startDate = null;
            this.endDate = null;

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to schedule a date!", ""));
            return "viewApplicants";
        }
        return "viewApplicants";
    }

    public List<NodeAssetEntity> getFilteredNode() {
        return filteredNode;
    }

    public void setFilteredNode(List<NodeAssetEntity> filteredNode) {
        this.filteredNode = filteredNode;
    }

    public List<RollingStockAssetEntity> getFilteredRollingStock() {
        return filteredRollingStock;
    }

    public void setFilteredRollingStock(List<RollingStockAssetEntity> filteredRollingStock) {
        this.filteredRollingStock = filteredRollingStock;
    }

    public List<AdvertisementSpaceEntity> getFilteredAdvertSpace() {
        return filteredAdvertSpace;
    }

    public void setFilteredAdvertSpace(List<AdvertisementSpaceEntity> filteredAdvertSpace) {
        this.filteredAdvertSpace = filteredAdvertSpace;
    }

    public List<LeasingSpaceEntity> getFilteredLeasingSpace() {
        return filteredLeasingSpace;
    }

    public void setFilteredLeasingSpace(List<LeasingSpaceEntity> filteredLeasingSpace) {
        this.filteredLeasingSpace = filteredLeasingSpace;
    }

    public List<AssetRequestEntity> getFilteredAssetRequestList() {
        return filteredAssetRequestList;
    }

    public void setFilteredAssetRequestList(List<AssetRequestEntity> filteredAssetRequestList) {
        this.filteredAssetRequestList = filteredAssetRequestList;
    }

    public List<ConsumableAssetEntity> getFilteredConsumableAsset() {
        return filteredConsumableAsset;
    }

    public void setFilteredConsumableAsset(List<ConsumableAssetEntity> filteredConsumableAsset) {
        this.filteredConsumableAsset = filteredConsumableAsset;
    }

    public List<MaintenanceRequestEntity> getFilteredmaintenanceRequestListByDepot() {
        return filteredmaintenanceRequestListByDepot;
    }

    public void setFilteredmaintenanceRequestListByDepot(List<MaintenanceRequestEntity> filteredmaintenanceRequestListByDepot) {
        this.filteredmaintenanceRequestListByDepot = filteredmaintenanceRequestListByDepot;
    }

    public List<MaintenanceRequestEntity> getFilteredmaintenanceRequestList1() {
        return filteredmaintenanceRequestList1;
    }

    public void setFilteredmaintenanceRequestList1(List<MaintenanceRequestEntity> filteredmaintenanceRequestList1) {
        this.filteredmaintenanceRequestList1 = filteredmaintenanceRequestList1;
    }

}
