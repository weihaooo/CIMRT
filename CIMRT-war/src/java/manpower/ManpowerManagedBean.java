/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.HqStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import manpower.entity.WorkshopEntity;
import manpower.sessionbean.ManpowerSessionBeanLocal;
import operations.sessionbean.AnnouncementSessionBeanLocal;
import passenger.entity.PassengerFeedbackEntity;
import passenger.sessionbean.PassengerFeedbackSessionBeanLocal;
import routefare.entity.NodeEntity;

/**
 *
 * @author FABIAN
 */
@Named(value = "manpowerManagedBean")
@SessionScoped
public class ManpowerManagedBean implements Serializable {

    @EJB
    private ManpowerSessionBeanLocal manpowerSessionBeanLocal;
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;
    @EJB
    private PassengerFeedbackSessionBeanLocal passengerFeedbackSessionBeanLocal;

    private String staffId;
    private String firstName;
    private String lastName;
    private String role;
    private String team;
    private ArrayList<String> staffDetails;

    private Timestamp time;
    private String workshopName;
    private String description;
    private String workshopType;
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    private String workshopStartTime;
    private String workshopEndTime;
    private String workshopAddress;
    private List<StaffEntity> staffList;
    private List<HqStaffEntity> hrStaffList;
    private List<String> showStaffList;
    private List<String> showHRStaffList;
    private String staff;
    private List<WorkshopEntity> workshopList;
    private List<WorkshopEntity> specificWorkshopList;
    private Long workshopId;
    private List<String> selectedStaffs;
    private List<String> selectedHRStaffs;
    private Map<String, String> types;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private List<WorkshopEntity> workshopsByStaff;
    private ArrayList<ArrayList<String>> expireSafetyList;
    private ArrayList<ArrayList<String>> expireOwnTeamSafetyList;
    private String expireStaffId;
    private String expireStaffName;
    private String safetyValidDaysLeft;
    private Date today = new Date();

    private List <StaffEntity> teamIndividualWorkshopsEnrolled;
    private ArrayList<WorkshopEntity> allWorkshopAttendedUnderSameTeam;
    
    private List<WorkshopEntity> filteredWorkshop;
    private List<WorkshopEntity> filteredWorkshopList;
    private ArrayList<ArrayList<String>> filteredExpireOwnTeamSafetyList;
    
    private List<PassengerFeedbackEntity> passengerFeedbackList;//station manager viewing his/her own stations feedback
    private List<PassengerFeedbackEntity> fullFeedbackList;//HR view all feedbacks

    @PostConstruct
    public void init() {
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        staffDetails = getStaff(staffId);
        firstName = staffDetails.get(0);
        lastName = staffDetails.get(1);
        role = staffDetails.get(14);
        if (staffDetails.size() == 18) {
            team = staffDetails.get(17);
        }

        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("1000HRS", "1000HRS");
        map.put("1100HRS", "1100HRS");
        map.put("1200HRS", "1200HRS");
        map.put("1300HRS", "1300HRS");
        map.put("1400HRS", "1400HRS");
        map.put("1500HRS", "1500HRS");
        map.put("1600HRS", "1600HRS");
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("0900HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1100HRS", "1100HRS");
        map.put("1200HRS", "1200HRS");
        map.put("1300HRS", "1300HRS");
        map.put("1400HRS", "1400HRS");
        map.put("1500HRS", "1500HRS");
        map.put("1600HRS", "1600HRS");
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1000HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1200HRS", "1200HRS");
        map.put("1300HRS", "1300HRS");
        map.put("1400HRS", "1400HRS");
        map.put("1500HRS", "1500HRS");
        map.put("1600HRS", "1600HRS");
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1100HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1300HRS", "1300HRS");
        map.put("1400HRS", "1400HRS");
        map.put("1500HRS", "1500HRS");
        map.put("1600HRS", "1600HRS");
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1200HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1400HRS", "1400HRS");
        map.put("1500HRS", "1500HRS");
        map.put("1600HRS", "1600HRS");
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1300HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1500HRS", "1500HRS");
        map.put("1600HRS", "1600HRS");
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1400HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1600HRS", "1600HRS");
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1500HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1700HRS", "1700HRS");
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1600HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1800HRS", "1800HRS");
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1700HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("1900HRS", "1900HRS");
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1800HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("2000HRS", "2000HRS");
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("1900HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("2100HRS", "2100HRS");
        map.put("2200HRS", "2200HRS");
        data.put("2000HRS", map);

        map = new LinkedHashMap<String, String>();
        map.put("2200HRS", "2200HRS");
        data.put("2100HRS", map);

    }

    public ManpowerManagedBean() {
    }

    public void onStartTimeChange() {
        if (workshopStartTime != null && !workshopStartTime.equals("")) {
            types = data.get(workshopStartTime);
        }
    }

    public ArrayList<WorkshopEntity> getAllWorkshopAttendedUnderSameTeam() {
        teamIndividualWorkshopsEnrolled = getTeamIndividualWorkshopsEnrolled();
        WorkshopEntity w = new WorkshopEntity();
        for(int i=0; i< teamIndividualWorkshopsEnrolled.size(); i++){
            StaffEntity s = accountSessionBeanLocal.searchStaff(teamIndividualWorkshopsEnrolled.get(i).getStaffId());
            //allWorkshopAttendedUnderSameTeam.add(s.getWorkshops());
        }
        return allWorkshopAttendedUnderSameTeam;
    }

    public void setAllWorkshopAttendedUnderSameTeam(ArrayList<WorkshopEntity> allWorkshopAttendedUnderSameTeam) {
        this.allWorkshopAttendedUnderSameTeam = allWorkshopAttendedUnderSameTeam;
    }

    
    
    public List<StaffEntity> getTeamIndividualWorkshopsEnrolled() {
        StaffEntity s = accountSessionBeanLocal.searchStaff(staffId);
        if(s instanceof StationStaffEntity || s instanceof DepotStaffEntity){
            teamIndividualWorkshopsEnrolled = manpowerSessionBeanLocal.getMembersWorkshopsEnrolled(staffId);
        }
        else if(s instanceof HqStaffEntity){
            teamIndividualWorkshopsEnrolled = manpowerSessionBeanLocal.getHRMembersWorkshopsEnrolled(staffId);
        }
        return teamIndividualWorkshopsEnrolled;
    }

    public void setTeamIndividualWorkshopsEnrolled(List<StaffEntity> teamIndividualWorkshopsEnrolled) {
        this.teamIndividualWorkshopsEnrolled = teamIndividualWorkshopsEnrolled;
    }

    
    
    public ArrayList<ArrayList<String>> getExpireOwnTeamSafetyList() {
        System.out.println("Staff Id here at get expire own team is: " + staffId);
        expireOwnTeamSafetyList = manpowerSessionBeanLocal.getOwnTeamExpireSafetyList(staffId);
        return expireOwnTeamSafetyList;
    }

    public void setExpireOwnTeamSafetyList(ArrayList<ArrayList<String>> expireOwnTeamSafetyList) {
        this.expireOwnTeamSafetyList = expireOwnTeamSafetyList;
    }

    public ArrayList<ArrayList<String>> getExpireSafetyList() {
        expireSafetyList = manpowerSessionBeanLocal.getExpireSafetyList();
        return expireSafetyList;
    }

    public void setExpireSafetyList(ArrayList<ArrayList<String>> expireSafetyList) {
        this.expireSafetyList = expireSafetyList;
    }

    public String updateWorkshop() {
        boolean msg = manpowerSessionBeanLocal.updateWorkshop(workshopId, workshopName, workshopType, description, workshopAddress, startDate, endDate, workshopStartTime, workshopEndTime);
        if (msg) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Workshop details is updated successfully",
                            ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the workshop!", ""));

            return "viewWorkshop?faces-redirect=true";
        }

        return "viewWorkshop?faces-redirect=true";
    }

    public String viewParticipants(Long workshopId, String workshopName) {
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.selectedStaffs = getParticipants(workshopId);

        return "viewParticipants?faces-redirect=true";
    }

    public String viewParticipantsHR(Long workshopId, String workshopName) {
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.selectedHRStaffs = getHRParticipants(workshopId);

        return "viewParticipants?faces-redirect=true";
    }

    public List<String> getParticipants(Long workshopId) {
        List<String> participants = new ArrayList<String>();
        WorkshopEntity w = manpowerSessionBeanLocal.searchWorkshop(workshopId);
        List<StaffEntity> staff = new ArrayList<StaffEntity>(w.getParticipants());
        for (int i = 0; i < staff.size(); i++) {
            if (staff.get(i) instanceof StationStaffEntity || staff.get(i) instanceof DepotStaffEntity) {
                participants.add(staff.get(i).getStaffId() + " " + staff.get(i).getFirstName() + " " + staff.get(i).getLastName());
            }
        }
        return participants;
    }

    public List<String> getHRParticipants(Long workshopId) {
        List<String> hrParticipants = new ArrayList<String>();
        WorkshopEntity w = manpowerSessionBeanLocal.searchWorkshop(workshopId);
        List<StaffEntity> staff = new ArrayList<StaffEntity>(w.getParticipants());
        for (int i = 0; i < staff.size(); i++) {
            if (staff.get(i) instanceof HqStaffEntity) {
                hrParticipants.add(staff.get(i).getStaffId() + " " + staff.get(i).getFirstName() + " " + staff.get(i).getLastName());
            }
        }
        return hrParticipants;
    }

    public String goToSignup(Long workshopId, String workshopName) {
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.selectedStaffs = getParticipants(workshopId);
        /*return "signupWorkshop?faces-redirect=true";*/
        return "signupWorkshop?faces-redirect=true";
    }

    public String goToSignupHR(Long workshopId, String workshopName) {
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.selectedHRStaffs = getHRParticipants(workshopId);
        /*return "signupWorkshop?faces-redirect=true";*/
        return "signupWorkshop?faces-redirect=true";
    }

    public String goToCreateWorkshop() {
        this.workshopName = null;
        this.workshopType = null;
        this.description = null;
        this.startDate = null;
        this.endDate = null;
        this.workshopAddress = null;
        this.workshopStartTime = null;
        this.workshopEndTime = null;
        return "createWorkshop?faces-redirect=true";
    }

    public String editWorkshop(Long workshopId) {
        this.workshopId = workshopId;
        WorkshopEntity temp = manpowerSessionBeanLocal.searchWorkshop(workshopId);
        this.workshopId = temp.getWorkshopId();
        this.workshopName = temp.getWorkshopName();
        this.workshopType = temp.getWorkshopType();
        this.description = temp.getDescription();
        this.workshopAddress = temp.getWorkshopAddress();
        this.startDate = temp.getStartDate();
        this.endDate = temp.getEndDate();
        this.workshopStartTime = temp.getWorkshopStartTime();
        this.workshopEndTime = temp.getWorkshopEndTime();
        return "editWorkshop?faces-redirect=true";
    }

    public String deleteWorkshop(Long workshopId) {
        Date date = new Date();
        time = new Timestamp(date.getTime());
        System.out.println("Workshop Id Passed in here is: " + workshopId);
        boolean msg = manpowerSessionBeanLocal.delWorkshop(workshopId);
        announcementSessionBeanLocal.createAnnouncement(workshopName + " has been cancelled", "Please be informed the following workshop has been cancelled "
                + "\nWorkshop Id: " + workshopId + "\nWorkshop Name: " + workshopName + " \n\nAll employees who are signed up for this workshop previously do not need to attend this workshop" + "\n\n Have a nice day!", "IMPT", time, "Staff Only", staffId);

        if (msg) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Workshop is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete workshop " + workshopId + "!!!", ""));
            return "viewWorkshop?faces-redirect=true";
        }
        return "viewWorkshop?faces-redirect=true";
    }

    public String createWorkshop() {
        Date date = new Date();
        time = new Timestamp(date.getTime());
        boolean id;
        id = manpowerSessionBeanLocal.createWorkshop(workshopName, workshopType, description, startDate, endDate, workshopStartTime, workshopEndTime, workshopAddress);
        announcementSessionBeanLocal.createAnnouncement(workshopName + " has been created", "Please be informed that a new workshop has been created: " + workshopName + " \nAll Managers, feel free to sign up any employees under your charge for this workshop. \n\n Have a nice day!", "Others", time, "Staff Only", staffId);

        if (id) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Workshop " + workshopName + " created! An announcement has been made to all staff",
                            ""));
            this.workshopName = null;
            this.workshopType = null;
            this.description = null;
            this.startDate = null;
            this.endDate = null;
            this.workshopAddress = null;
            this.workshopStartTime = null;
            this.workshopEndTime = null;

            return "createWorkshop?faces-redirect=true";
        }
        return "viewWorkshop?faces-redirect=true";

    }

    public String getExpireStaffId() {
        return expireStaffId;
    }

    public void setExpireStaffId(String expireStaffId) {
        this.expireStaffId = expireStaffId;
    }

    public String getExpireStaffName() {
        return expireStaffName;
    }

    public void setExpireStaffName(String expireStaffName) {
        this.expireStaffName = expireStaffName;
    }

    public String getSafetyValidDaysLeft() {
        return safetyValidDaysLeft;
    }

    public void setSafetyValidDaysLeft(String safetyValidDaysLeft) {
        this.safetyValidDaysLeft = safetyValidDaysLeft;
    }

    public Map<String, String> getTypes() {
        return types;
    }

    public void setTypes(Map<String, String> types) {
        this.types = types;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Long workshopId) {
        this.workshopId = workshopId;
    }

    public List<WorkshopEntity> getWorkshopList() {
        workshopList = manpowerSessionBeanLocal.getAllWorkshops();
        return workshopList;
    }

    public List<WorkshopEntity> getSpecificWorkshopList() {
        specificWorkshopList = manpowerSessionBeanLocal.getSpecificWorkshop(workshopId, workshopName);
        return specificWorkshopList;
    }

    public void setSpecificWorkshopList(List<WorkshopEntity> specificWorkshopList) {
        this.specificWorkshopList = specificWorkshopList;
    }

    public void setWorkshopList(List<WorkshopEntity> workshopList) {
        this.workshopList = workshopList;
    }

    public String getWorkshopType() {
        return workshopType;
    }

    public void setWorkshopType(String workshopType) {
        this.workshopType = workshopType;
    }

    public String getWorkshopAddress() {
        return workshopAddress;
    }

    public void setWorkshopAddress(String workshopAddress) {
        this.workshopAddress = workshopAddress;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<HqStaffEntity> getHrStaffList() {
        Collection<HqStaffEntity> hrStaff = new ArrayList<HqStaffEntity>();
        List<HqStaffEntity> hrList = new ArrayList<HqStaffEntity>();
        hrStaffList = manpowerSessionBeanLocal.getAllHRStaff();

        return hrStaffList;
    }

    public void setHRStaffList(List<HqStaffEntity> hrStaffList) {
        this.hrStaffList = hrStaffList;
    }

    public List<StaffEntity> getStaffList() {
        Collection<StationStaffEntity> stationStaff = new ArrayList<StationStaffEntity>();
        Collection<DepotStaffEntity> depotStaff = new ArrayList<DepotStaffEntity>();
        List<StaffEntity> staffList = new ArrayList<StaffEntity>();
        System.out.println("Staff ID here is: " + staffId);

        staffList = manpowerSessionBeanLocal.getStaffUnderSameTeam(staffId);
        if (staffList.get(0) instanceof StationStaffEntity) {
            StationStaffEntity s = (StationStaffEntity) staffList.get(0);
            stationStaff = s.getStationTeam().getStaff();
            staffList = (List) stationStaff;
            return staffList;
        } else {
            DepotStaffEntity d = (DepotStaffEntity) staffList.get(0);
            depotStaff = d.getDepotTeam().getStaff();
            staffList = (List) depotStaff;
            return staffList;
        }

    }

    public void setStaffList(List<StaffEntity> staffList) {
        this.staffList = staffList;
    }

    public List<String> getShowHRStaffList() {
        hrStaffList = getHrStaffList();
        showHRStaffList = new ArrayList<String>();
        for (int i = 0; i < hrStaffList.size(); i++) {
            /*if ((!hrStaffList.get(i).getStaffId().equals(staffId)) && if(!hrStaffList.get(i).getStaffRole().equals("HR")) {*/
                showHRStaffList.add(hrStaffList.get(i).getStaffId() + " " + hrStaffList.get(i).getFirstName() + " " + hrStaffList.get(i).getLastName());
            /*}*/
        }

        return showHRStaffList;
    }

    public void setShowHRStaffList(List<String> showHRStaffList) {
        this.showHRStaffList = showHRStaffList;
    }

    public List<String> getShowStaffList() {
        staffList = getStaffList();
        showStaffList = new ArrayList<String>();
        for (int i = 0; i < staffList.size(); i++) {
            /*if ((!staffList.get(i).getStaffId().equals(staffId)) && (!staffList.get(i).getStaffRole().equals("Manager"))) {*/
                showStaffList.add(staffList.get(i).getStaffId() + " " + staffList.get(i).getFirstName() + " " + staffList.get(i).getLastName());
            /*}*/
        }
        return showStaffList;
    }

    public void setShowStaffList(List<String> showStaffList) {
        this.showStaffList = showStaffList;
    }

    public String signupWorkshop() {

        String status = manpowerSessionBeanLocal.signupWorkshop(workshopId, workshopName, selectedStaffs, staffId);
        if (status.equals("Updated")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Signed up for Workshop " + workshopId + " successfully",
                            ""));
            return "createWorkshop";
        } else if (status.equals("Cleared")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "You did not sign up any employees for this workshop currently!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Error!"));
        }
        return "viewWorkshop?faces-redirect = true";
    }

    public String signupWorkshopHR() {

        String status = manpowerSessionBeanLocal.signupWorkshopHR(workshopId, workshopName, selectedHRStaffs, staffId);
        if (status.equals("Updated")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Signed up for Workshop " + workshopId + " successfully",
                            ""));
            return "viewWorkshop";
        } else if (status.equals("Repetitive")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Employees signed up for workshop already!"));
        } else if (status.equals("Empty")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Workshop doesn't already!"));
        } else if (status.equals("Cleared")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "You did not sign up any employees for this workshop currently!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Error!"));
        }
        return "viewWorkshop?faces-redirect = true";
    }

    public List<PassengerFeedbackEntity> getFullFeedbackList() {
        fullFeedbackList = passengerFeedbackSessionBeanLocal.getAllFeedbacks();
        return fullFeedbackList;
    }

    public void setFullFeedbackList(List<PassengerFeedbackEntity> fullFeedbackList) {
        this.fullFeedbackList = fullFeedbackList;
    }

    public List<PassengerFeedbackEntity> getPassengerFeedbackList() {
        List<StaffEntity> temp = manpowerSessionBeanLocal.getStaffUnderSameTeam(staffId);
        StationStaffEntity tempStaff = (StationStaffEntity) temp.get(0);
        NodeEntity nodeName = tempStaff.getStationTeam().getNode();
        String address = nodeName.getAddress();
        passengerFeedbackList = passengerFeedbackSessionBeanLocal.getPassengerFeedbacks(address);
        return passengerFeedbackList;
    }

    public void setPassengerFeedbackList(List<PassengerFeedbackEntity> passengerFeedbackList) {
        this.passengerFeedbackList = passengerFeedbackList;
    }

    public List<String> getSelectedHRStaffs() {
        return selectedHRStaffs;
    }

    public void setSelectedHRStaffs(List<String> selectedHRStaffs) {
        this.selectedHRStaffs = selectedHRStaffs;
    }

    public void retrieveWorkshops() {
        workshopsByStaff = manpowerSessionBeanLocal.retrieveWorkshops(staffId);
    }

    
    /*public void retrieveWorkshops(){
        workshopsByStaff = manpowerSessionBeanLocal.retrieveWorkshops(staffId);
    }*/

    public List<WorkshopEntity> getWorkshopsByStaff() {
        workshopsByStaff = manpowerSessionBeanLocal.retrieveWorkshops(staffId);
        return workshopsByStaff;
    }

    public void setWorkshopsByStaff(List<WorkshopEntity> workshopsByStaff) {
        this.workshopsByStaff = workshopsByStaff;
    }

    public List<String> getSelectedStaffs() {
        return selectedStaffs;
    }

    public void setSelectedStaffs(List<String> selectedStaffs) {
        this.selectedStaffs = selectedStaffs;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getWorkshopStartTime() {
        return workshopStartTime;
    }

    public void setWorkshopStartTime(String workshopStartTime) {
        this.workshopStartTime = workshopStartTime;
    }

    public String getWorkshopEndTime() {
        return workshopEndTime;
    }

    public void setWorkshopEndTime(String workshopEndTime) {
        this.workshopEndTime = workshopEndTime;
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

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public List<WorkshopEntity> getFilteredWorkshop() {
        return filteredWorkshop;
    }

    public void setFilteredWorkshop(List<WorkshopEntity> filteredWorkshop) {
        this.filteredWorkshop = filteredWorkshop;
    }

    public List<WorkshopEntity> getFilteredWorkshopList() {
        return filteredWorkshopList;
    }

    public void setFilteredWorkshopList(List<WorkshopEntity> filteredWorkshopList) {
        this.filteredWorkshopList = filteredWorkshopList;
    }

    public ArrayList<ArrayList<String>> getFilteredExpireOwnTeamSafetyList() {
        return filteredExpireOwnTeamSafetyList;
    }

    public void setFilteredExpireOwnTeamSafetyList(ArrayList<ArrayList<String>> filteredExpireOwnTeamSafetyList) {
        this.filteredExpireOwnTeamSafetyList = filteredExpireOwnTeamSafetyList;
    }

}
