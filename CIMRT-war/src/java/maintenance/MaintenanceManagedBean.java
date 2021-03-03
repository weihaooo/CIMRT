/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import infraasset.entity.AssetEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.TrainCarEntity;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import maintenance.entity.MaintenanceReportEntity;
import maintenance.sessionbean.MaintenanceSessionBeanLocal;
import operations.entity.MaintenanceRequestEntity;

/**
 *
 * @author FABIAN
 */
@Named(value = "maintenanceManagedBean")
@SessionScoped
public class MaintenanceManagedBean implements Serializable {

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
    @EJB
    private MaintenanceSessionBeanLocal maintenanceSessionBeanLocal;
    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    private String staffId;
    private String firstName;
    private String lastName;
    private String nric;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String maritalStatus;
    private String race;
    private String nationality;
    private String religion;
    private String educationQualification;
    private String salary;
    private String department;
    private String role;
    private String team;
    private String mcEntitlement;
    private String leaveEntitlement;
    private ArrayList<String> staffDetails;

    private String staffName;
    private String urgencyLvl;
    private String mainReqId;
    private String rptTitle;
    private String reportDescription;
    private String asset1;
    private String asset2;
    private String asset3;
    private String qty1;
    private String qty2;
    private String qty3;
    private String maintenanceStatus;
    private List<AssetEntity> assets;
    private List<String> assetList;
    private List<MaintenanceReportEntity> reportList;
    private List<MaintenanceReportEntity> filteredreportList;
    private List<MaintenanceReportEntity> specificReport;
    private Long maintenanceReportId;
    private String submitterId;
    private String submitterName;
    private Timestamp reportDate;
    private String remark;
    private String qtySpoilt;
    private String assetType;
    private String assetName;
    private String assetDetails;
    private boolean appear;

    public MaintenanceManagedBean() {
    }

    @PostConstruct
    public void init() {
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        staffDetails = getStaff(staffId);
        firstName = staffDetails.get(0);
        lastName = staffDetails.get(1);
        nric = staffDetails.get(2);
        phoneNumber = staffDetails.get(3);
        emailAddress = staffDetails.get(4);
        address = staffDetails.get(5);
        gender = staffDetails.get(6);
        dateOfBirth = staffDetails.get(7);
        maritalStatus = staffDetails.get(8);
        race = staffDetails.get(9);
        nationality = staffDetails.get(10);
        religion = staffDetails.get(11);
        educationQualification = staffDetails.get(12);
        salary = staffDetails.get(13);
        role = staffDetails.get(14);
        mcEntitlement = staffDetails.get(15);
        leaveEntitlement = staffDetails.get(16);
        if (staffDetails.size() == 19) {
            team = staffDetails.get(17);
        }
    }

    public String goEditMaintenanceReport(String mainReqId, String remark, String details) {

        this.mainReqId = mainReqId;
        this.remark = remark;
        /*this.assetDetails = details;

        String[] s = assetDetails.split(" ");
        String assetId = s[0];
        AssetEntity asset = maintenanceSessionBeanLocal.searchAsset(assetId);*/

        MaintenanceRequestEntity mre = maintenanceSessionBeanLocal.searchMaintenanceRequest(mainReqId);
        AssetEntity tempAsset = mre.getAsset();
        this.assetName = tempAsset.getAssetId() + " " + tempAsset.getAssetName();
        if (tempAsset instanceof NodeAssetEntity) {
            this.assetType = "NodeAssetEntity";
        } else if (tempAsset instanceof RollingStockAssetEntity) {
            this.assetType = "RollingStockAssetEntity";
        } else if (tempAsset instanceof TrainCarEntity) {
            this.assetType = "TrainCarEntity";
        }

        Long reportId = mre.getMaintenanceReport().getMaintenanceReportId();
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
        }

        return "editMaintenanceReport?faces-redirect=true";
    }
    
    public void onSelectionChange(){
        if(maintenanceStatus.equals("Processing") || maintenanceStatus.equals("Repaired")){
            this.qtySpoilt = "0";
            this.appear = true;
        } else {
            this.appear = false;
        }
               
    }
    
    public String goUpdateMaintenanceReport(String mainReqId, String remark, String details) {

        this.mainReqId = mainReqId;
        this.remark = remark;
        /*this.assetDetails = details;

        String[] s = assetDetails.split(" ");
        String assetId = s[0];
        AssetEntity asset = maintenanceSessionBeanLocal.searchAsset(assetId);*/

        MaintenanceRequestEntity mre = maintenanceSessionBeanLocal.searchMaintenanceRequest(mainReqId);
        AssetEntity tempAsset = mre.getAsset();
        this.assetName = tempAsset.getAssetId() + " " + tempAsset.getAssetName();
        if (tempAsset instanceof NodeAssetEntity) {
            this.assetType = "NodeAssetEntity";
        } else if (tempAsset instanceof RollingStockAssetEntity) {
            this.assetType = "RollingStockAssetEntity";
        } else if (tempAsset instanceof TrainCarEntity) {
            this.assetType = "TrainCarEntity";
        }

        Long reportId = mre.getMaintenanceReport().getMaintenanceReportId();
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
            this.qtySpoilt = Integer.toString(specificReport.get(i).getQtySpoilt());
        }

        return "updateStatusMaintenanceReport?faces-redirect=true";
    }
    
   
    public String goToMaintenanceReport(String mainReqId) {
        MaintenanceRequestEntity mre = maintenanceSessionBeanLocal.searchMaintenanceRequest(mainReqId);
        MaintenanceReportEntity report = mre.getMaintenanceReport();
        Long reportId = report.getMaintenanceReportId();
        String s = goFullReport(reportId);
        return s;

    }

    public String goFullReport(Long maintenanceReportId) {
        this.maintenanceReportId = maintenanceReportId;
        this.specificReport = getSpecificReport();
        MaintenanceRequestEntity mre = maintenanceSessionBeanLocal.getMaintenanceRequest(maintenanceReportId);
        AssetEntity tempAsset = mre.getAsset();
        this.assetName = tempAsset.getAssetId() + " " + tempAsset.getAssetName();
        if (tempAsset instanceof NodeAssetEntity) {
            this.assetType = "NodeAssetEntity";
        } else if (tempAsset instanceof RollingStockAssetEntity) {
            this.assetType = "RollingStockAssetEntity";
        } else if (tempAsset instanceof TrainCarEntity) {
            this.assetType = "TrainCarEntity";
        }

        for (int i = 0; i < specificReport.size(); i++) {
            this.maintenanceReportId = specificReport.get(i).getMaintenanceReportId();
            this.rptTitle = specificReport.get(i).getRptTitle();
            this.reportDescription = specificReport.get(i).getReportDescription();
            this.asset1 = specificReport.get(i).getAssetUsed();
            this.qty1 = Integer.toString(specificReport.get(i).getQuantityAssetUsed());
            this.qtySpoilt = Integer.toString(specificReport.get(i).getQtySpoilt());
            this.maintenanceStatus = specificReport.get(i).getMaintenanceStatus();
            this.submitterId = specificReport.get(i).getSubmitterId();
            this.submitterName = specificReport.get(i).getSubmitterName();
            this.reportDate = specificReport.get(i).getReportDateTime();

        }
        return "viewFullReport?faces-redirect=true";
    }

    public String updateMaintenanceReport(Long maintenanceReportId,
            String rptTitle, String reportDescription, String maintenanceStatus, String qtySpoilt, String assetName) {

       
        MaintenanceReportEntity mre = maintenanceSessionBeanLocal.searchMaintenanceReport(maintenanceReportId);
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        boolean status;
        String[] asset = assetName.split(" ");
        String assetId = asset[0];
        AssetEntity a = maintenanceSessionBeanLocal.searchAsset(assetId);

        status = maintenanceSessionBeanLocal.updateMaintenanceReport(maintenanceReportId, "", "0",
                rptTitle, reportDescription, maintenanceStatus, qtySpoilt, assetName);

         MaintenanceReportEntity report = maintenanceSessionBeanLocal.searchReport(maintenanceReportId);
        if (status) {
            if (a instanceof NodeAssetEntity && maintenanceStatus.equals("Spoilt")) {
                infraAssetSessionBeanLocal.submitNodeAssetRequest(assetName, qtySpoilt,report);
            }
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance Report updated successfully",
                            ""));

            this.rptTitle = null;
            this.reportDescription = null;
            this.maintenanceStatus = null;
            this.qtySpoilt = null;
            return "viewMaintenanceReportList?faces-redirect=true";
        }
        return "viewMaintenanceReportList?faces-redirect=true";
    }
    
     public String updateMaintenanceReport1(Long maintenanceReportId,
            String rptTitle, String reportDescription, String maintenanceStatus, String qtySpoilt, String assetName) {

       
        MaintenanceReportEntity mre = maintenanceSessionBeanLocal.searchMaintenanceReport(maintenanceReportId);
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        boolean status;
        String[] asset = assetName.split(" ");
        String assetId = asset[0];
        AssetEntity a = maintenanceSessionBeanLocal.searchAsset(assetId);

        status = maintenanceSessionBeanLocal.updateMaintenanceReport(maintenanceReportId, "", "0",
                rptTitle, reportDescription, maintenanceStatus, qtySpoilt, assetName);

         MaintenanceReportEntity report = maintenanceSessionBeanLocal.searchReport(maintenanceReportId);
        if (status) {
            if (a instanceof NodeAssetEntity && maintenanceStatus.equals("Spoilt")) {
                infraAssetSessionBeanLocal.submitNodeAssetRequest1(assetName, qtySpoilt,report);
            }
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance Report updated successfully",
                            ""));

            this.rptTitle = null;
            this.reportDescription = null;
            this.maintenanceStatus = null;
            this.qtySpoilt = null;
            return "viewMaintenanceReportList?faces-redirect=true";
        }
        return "viewMaintenanceReportList?faces-redirect=true";
    }

    public String updateMaintenanceReportTrainCar(Long maintenanceReportId,
            String rptTitle, String reportDescription, String maintenanceStatus, String assetName) {

        MaintenanceReportEntity mre = maintenanceSessionBeanLocal.searchMaintenanceReport(maintenanceReportId);
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        boolean status;
        status = maintenanceSessionBeanLocal.updateMaintenanceReport(maintenanceReportId, "", "0",
                rptTitle, reportDescription, maintenanceStatus, "0", assetName);

        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance Report updated successfully",
                            ""));

            this.rptTitle = null;
            this.reportDescription = null;
            this.maintenanceStatus = null;
            return "viewMaintenanceReportList?faces-redirect=true";
        }
        return "viewMaintenanceReportList?faces-redirect=true";
    }

    public String getAssetDetails() {
        return assetDetails;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Timestamp getReportDate() {
        return reportDate;
    }

    public void setReportDate(Timestamp reportDate) {
        this.reportDate = reportDate;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public List<MaintenanceReportEntity> getSpecificReport() {
        System.out.println("MaintenanceReport ID here is :" + maintenanceReportId);
        specificReport = maintenanceSessionBeanLocal.getSpecificReport(maintenanceReportId);
        return specificReport;
    }

    public void setSpecificReport(List<MaintenanceReportEntity> specificReport) {
        this.specificReport = specificReport;
    }

    public Long getMaintenanceReportId() {
        return maintenanceReportId;
    }

    public void setMaintenanceReportId(Long maintenanceReportId) {
        this.maintenanceReportId = maintenanceReportId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<MaintenanceReportEntity> getReportList() {
        reportList = maintenanceSessionBeanLocal.getAllReports();
        return reportList;
    }

    public void setReportList(List<MaintenanceReportEntity> reportList) {
        this.reportList = reportList;
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

    public boolean isAppear() {
        return appear;
    }

    public void setAppear(boolean appear) {
        this.appear = appear;
    }
    
    

    public String createMaintenanceReport(String mainReqId, String assetDetails) {
        MaintenanceReportEntity report = new MaintenanceReportEntity();
        MaintenanceRequestEntity mre =  maintenanceSessionBeanLocal.searchMaintenanceRequest(mainReqId);
      
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        boolean status = true;
        String[] asset = assetDetails.split(" ");
        String assetId = asset[1];
        AssetEntity a = maintenanceSessionBeanLocal.searchAsset(assetId);
        String tempStatus = "";

        if (a instanceof NodeAssetEntity) {
           report  = maintenanceSessionBeanLocal.createMaintenanceReport(rptTitle, reportDescription, "1", "0", maintenanceStatus, time, staffId, firstName,
                    mainReqId, assetDetails, qtySpoilt);
        } else if (a instanceof RollingStockAssetEntity) {
            report = maintenanceSessionBeanLocal.createMaintenanceReport(rptTitle, reportDescription, "2", "0", maintenanceStatus, time, staffId, firstName,
                    mainReqId, assetDetails, qtySpoilt);
        }

        if (report != null) {
            //maintenanceSessionBeanLocal.deductQuantity(asset1, qty1);
            if (a instanceof NodeAssetEntity) {
                if (Integer.parseInt(qtySpoilt) != 0) {
                    infraAssetSessionBeanLocal.submitNodeAssetRequest(assetDetails, qtySpoilt, report);
                    tempStatus = "Pending Replacement";
                    maintenanceSessionBeanLocal.updateRequestStatus(mainReqId, tempStatus);
                } else {
                    if (maintenanceStatus.equals("Repaired")) {
                        maintenanceSessionBeanLocal.updateRequestStatus(mainReqId, "Repaired");
                    } else {
                        maintenanceSessionBeanLocal.updateRequestStatus(mainReqId, "Processing");
                    }
                }
            } else if (a instanceof RollingStockAssetEntity) {
                maintenanceSessionBeanLocal.updateRequestStatus(mainReqId, maintenanceStatus);
            }
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance Report submitted successfully",
                            ""));
            this.asset1 = null;
            this.asset2 = null;
            this.qty1 = null;
            this.qty2 = null;
            this.rptTitle = null;
            this.reportDescription = null;
            this.maintenanceStatus = null;
            this.appear = true;
            return "viewMaintenanceRequest?faces-redirect=true";
        }
        return "viewMaintenanceRequest?faces-redirect=true";
    }

    public String createMaintenanceReportTrainCar(String mainReqId, String maintenaceStatus, String assetDetails) {
        MaintenanceRequestEntity mre = new MaintenanceRequestEntity();
        mre = maintenanceSessionBeanLocal.searchMaintenanceRequest(mainReqId);

        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        boolean status;

        status = maintenanceSessionBeanLocal.createMaintenanceReportTrainCar(rptTitle, reportDescription, maintenanceStatus, time, staffId, firstName, mainReqId, assetDetails);
        System.out.println("Checking boolean Status here: " + status);
        if (status) {
            maintenanceSessionBeanLocal.updateRequestStatus(mainReqId, maintenanceStatus);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Maintenance Report submitted successfully",
                            ""));
            this.asset1 = null;
            this.asset2 = null;
            this.qty1 = null;
            this.qty2 = null;
            this.rptTitle = null;
            this.reportDescription = null;
            this.maintenanceStatus = null;
            return "createMaintenanceReport?faces-redirect=true";
        }
        return "createMaintenanceReport?faces-redirect=true";
    }
    
    public boolean checkAssetRequestStatus(String mainReqId){
        MaintenanceRequestEntity mre = new MaintenanceRequestEntity();
        mre = maintenanceSessionBeanLocal.searchMaintenanceRequest(mainReqId);
        MaintenanceReportEntity report = mre.getMaintenanceReport();
        AssetRequestEntity req = report.getAssetRequest();
        if(req !=null){
            if(req.getStatus().equals("Completed"))
                return true;
        }
        return false;  
    }

    public String getQtySpoilt() {
        return qtySpoilt;
    }

    public void setQtySpoilt(String qtySpoilt) {
        this.qtySpoilt = qtySpoilt;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public List<AssetEntity> getAssets() {
        assets = maintenanceSessionBeanLocal.getAllAssets();
        return assets;
    }

    public void setAssets(List<AssetEntity> assets) {
        this.assets = assets;
    }

    public List<String> getAssetList() {
        assets = getAssets();
        assetList = new ArrayList<String>();
        for (int i = 0; i < assets.size(); i++) {
            if (assets.get(i) instanceof NodeAssetEntity || assets.get(i) instanceof RollingStockAssetEntity) {
                assetList.add(assets.get(i).getAssetId() + " " + assets.get(i).getAssetName());
            }
        }
        return assetList;
    }

    public void setAssetList(List<String> assetList) {
        this.assetList = assetList;
    }

    public String getAsset1() {
        return asset1;
    }

    public void setAsset1(String asset1) {
        this.asset1 = asset1;
    }

    public String getAsset2() {
        return asset2;
    }

    public void setAsset2(String asset2) {
        this.asset2 = asset2;
    }

    public String getAsset3() {
        return asset3;
    }

    public void setAsset3(String asset3) {
        this.asset3 = asset3;
    }

    public String getQty1() {
        return qty1;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getQty2() {
        return qty2;
    }

    public void setQty2(String qty2) {
        this.qty2 = qty2;
    }

    public String getQty3() {
        return qty3;
    }

    public void setQty3(String qty3) {
        this.qty3 = qty3;
    }

    public String getMainReqId() {
        return mainReqId;
    }

    public void setMainReqId(String mainReqId) {
        this.mainReqId = mainReqId;
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

    public String getUrgencyLvl() {
        return urgencyLvl;
    }

    public void setUrgencyLvl(String urgencyLvl) {
        this.urgencyLvl = urgencyLvl;
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
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

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getEducationQualification() {
        return educationQualification;
    }

    public void setEducationQualification(String educationQualification) {
        this.educationQualification = educationQualification;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public List<MaintenanceReportEntity> getFilteredreportList() {
        return filteredreportList;
    }

    public void setFilteredreportList(List<MaintenanceReportEntity> filteredreportList) {
        this.filteredreportList = filteredreportList;
    }
    

}
