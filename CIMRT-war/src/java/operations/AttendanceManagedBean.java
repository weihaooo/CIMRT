/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.entity.StaffEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import manpower.entity.RosterEntity;
import manpower.sessionbean.RosterSessionBeanLocal;
import operations.entity.AttendanceEntity;
import operations.sessionbean.AttendanceSessionBeanLocal;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Named(value = "attendanceManagedBean")
@SessionScoped
public class AttendanceManagedBean implements Serializable {

    @EJB
    private RosterSessionBeanLocal rosterSessionBeanLocal;

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    @EJB
    private AttendanceSessionBeanLocal attendanceSessionBeanLocal;

    private String staffId;
    private String firstName;
    private String lastName;
    private String role;
    private String team;
    private ArrayList<String> staffDetails;
    private String nodeCode;
    private RosterEntity roster;
    private ArrayList<StaffEntity> staffs;
    private NodeEntity node;
    private String todayString;
    private String[] remarks;
    private String[] attendanceType;
    private Date[] clockinTime;
    private Date[] clockoutTime;
    private int minHour;
    private int minMinute;

    private ArrayList<AttendanceEntity> attendances;
    private ArrayList<AttendanceEntity> filteredattendances;
    private ArrayList<AttendanceEntity> attendancesByStaff;
    private boolean clockedIn = false;
    private boolean clockedOut = false;
    private AttendanceEntity todayAttendance;
    private boolean shiftNow;

    private ArrayList<StaffEntity> standbyStaffs;
    private ArrayList<StaffEntity> activatedStaffs;
    private ArrayList<StaffEntity> filteredStandbyStaffs;
    private ArrayList<StaffEntity> filteredActivatedStaffs;
    private List<Date> activatedTimeList;
    private List<Date> expectedEndTimeList;
    private List<String> acknowledgeList;
    private StaffEntity selectedStaff;
    private List<String> nodes;
    private Date inTime;
    private Date outTime;
    private String title;
    private String description;
    private String nodeString;

    private Date today = new Date();

    public AttendanceManagedBean() {
    }

    @PostConstruct
    public void init() {

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            firstName = staffDetails.get(0);
            lastName = staffDetails.get(1);
            role = staffDetails.get(14);
            minHour = 0;
            minMinute = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            todayString = sdf.format(new Date());
            if (staffDetails.size() == 19) {
                team = staffDetails.get(17);
                nodeCode = staffDetails.get(18);
                staffs = getTodayStaff();
                if (staffs != null) {
                    remarks = new String[staffs.size()];
                    attendanceType = new String[staffs.size()];
                    clockinTime = new Date[staffs.size()];
                    clockoutTime = new Date[staffs.size()];
                    updateAttendance();
                }
                standbyStaffs = getTodayStandbys();
                activatedStaffs = getTodayActivated();
                nodes = getNodes();

                for (int i = 0; i < nodes.size(); i++) {
                    if (nodeCode.equals(nodes.get(i).substring(0, 5))) {
                        nodeString = nodes.get(i);
                    }
                }
            }
            retrieveAttendancesByStaff();
            getTodayAttendance();
        }
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    private ArrayList<StaffEntity> getTodayStaff() {
        return attendanceSessionBeanLocal.getTodayStaff(nodeCode, team);
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

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public RosterEntity getRoster() {
        return roster;
    }

    public void setRoster(RosterEntity roster) {
        this.roster = roster;
    }

    public ArrayList<StaffEntity> getStaffs() {
        return staffs;
    }

    public void setStaffs(ArrayList<StaffEntity> staffs) {
        this.staffs = staffs;
    }

    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }

    public String getTodayString() {
        return todayString;
    }

    public void setTodayString(String today) {
        this.todayString = today;
    }

    public String[] getRemarks() {
        return remarks;
    }

    public void setRemarks(String[] remarks) {
        this.remarks = remarks;
    }

    public String[] getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String[] attendanceType) {
        this.attendanceType = attendanceType;
    }

    public Date[] getClockinTime() {
        return clockinTime;
    }

    public void setClockinTime(Date[] clockinTime) {
        this.clockinTime = clockinTime;
    }

    public Date[] getClockoutTime() {
        return clockoutTime;
    }

    public void setClockoutTime(Date[] clockoutTime) {
        this.clockoutTime = clockoutTime;
    }

    public int getMinHour() {
        return minHour;
    }

    public void setMinHour(int minHour) {
        this.minHour = minHour;
    }

    public int getMinMinute() {
        return minMinute;
    }

    public void setMinMinute(int minMinute) {
        this.minMinute = minMinute;
    }

    public void setMinTime(int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(clockinTime[index]);
        this.minHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minMinute = calendar.get(Calendar.MINUTE);

    }

    public ArrayList<AttendanceEntity> getAttendances() {
        attendances = attendanceSessionBeanLocal.retrieveAllAttendances();
        return attendances;
    }

    public void setAttendances(ArrayList<AttendanceEntity> attendances) {
        this.attendances = attendances;
    }

    public ArrayList<StaffEntity> getStandbyStaffs() {
        return standbyStaffs;
    }

    public void setStandbyStaffs(ArrayList<StaffEntity> standbyStaffs) {
        this.standbyStaffs = standbyStaffs;
    }

    public StaffEntity getSelectedStaff() {
        return selectedStaff;
    }

    public void setSelectedStaff(StaffEntity selectedStaff) {
        this.selectedStaff = selectedStaff;
    }

    public List<String> getNodes() {
        return attendanceSessionBeanLocal.getNodes();
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNodeString() {
        return nodeString;
    }

    public void setNodeString(String nodeString) {
        this.nodeString = nodeString;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public ArrayList<StaffEntity> getActivatedStaffs() {
        return activatedStaffs;
    }

    public void setActivatedStaffs(ArrayList<StaffEntity> activatedStaffs) {
        this.activatedStaffs = activatedStaffs;
    }

    public ArrayList<AttendanceEntity> getAttendancesByStaff() {
        return attendancesByStaff;
    }

    public void setAttendancesByStaff(ArrayList<AttendanceEntity> attendancesByStaff) {
        this.attendancesByStaff = attendancesByStaff;
    }

    public void retrieveAttendancesByStaff() {
        attendancesByStaff = attendanceSessionBeanLocal.retrieveAttendancesByStaff(staffId);
    }

    public boolean isClockedIn() {
        return clockedIn;
    }

    public void setClockedIn(boolean clockedIn) {
        this.clockedIn = clockedIn;
    }

    public boolean isClockedOut() {
        return clockedOut;
    }

    public boolean isShiftNow() {
        shiftNow = attendanceSessionBeanLocal.isShiftNow(staffId);
        return shiftNow;
    }

    public void setShiftNow(boolean shiftNow) {
        this.shiftNow = shiftNow;
    }

    public void setClockedOut(boolean clockedOut) {
        this.clockedOut = clockedOut;
    }

    public List<Date> getActivatedTimeList() {
        return activatedTimeList;
    }

    public void setActivatedTimeList(List<Date> activatedTimeList) {
        this.activatedTimeList = activatedTimeList;
    }

    public List<Date> getExpectedEndTimeList() {
        return expectedEndTimeList;
    }

    public void setExpectedEndTimeList(List<Date> expectedEndTimeList) {
        this.expectedEndTimeList = expectedEndTimeList;
    }

    public List<String> getAcknowledgeList() {
        return acknowledgeList;
    }

    public void setAcknowledgeList(List<String> acknowledgeList) {
        this.acknowledgeList = acknowledgeList;
    }

    public void onRowEdit(int index) {

        int result;
        if ((attendanceType[index].equals("Others") || attendanceType[index].equals("MC")) && (remarks[index].equals(""))) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Please enter a remark!",
                            ""));
            this.remarks[index] = null;
            this.attendanceType[index] = null;
            this.clockinTime[index] = null;
            this.clockoutTime[index] = null;
        } else {
            result = attendanceSessionBeanLocal.createAttendance(staffs.get(index), attendanceType[index], remarks[index]);

            if (result == 2) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "You have successfully create an attendance for " + staffs.get(index).getStaffId() + " - " + staffs.get(index).getFirstName() + " " + staffs.get(index).getLastName() + "!",
                                ""));
            } else if (result == 1) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "You have successfully added a remarks for " + staffs.get(index).getStaffId() + " - " + staffs.get(index).getFirstName() + " " + staffs.get(index).getLastName() + "!",
                                ""));
            }
        }
        updateAttendance();
        retrieveAttendancesByStaff();

    }

    public void onRowCancel(int index) {
        AttendanceEntity att = attendanceSessionBeanLocal.getAttendance(staffs.get(index));
        if (att != null) {
            this.remarks[index] = att.getRemarks();
            this.attendanceType[index] = att.getType();
            this.clockinTime[index] = att.getClockinTime();
            this.clockoutTime[index] = att.getClockoutTime();
        } else {
            this.remarks[index] = null;
            this.attendanceType[index] = null;
            this.clockinTime[index] = null;
            this.clockoutTime[index] = null;
        }
    }

    public AttendanceEntity getTodayAttendance() {
        todayAttendance = attendanceSessionBeanLocal.retrieveTodayAttendance(staffId);
        if (todayAttendance == null) {
            clockedIn = false;
            clockedOut = false;
        } else if ((todayAttendance.getType().equals("On Time") || todayAttendance.getType().equals("Late") || todayAttendance.getType().equals("Activated")) && todayAttendance.getClockoutTime() == null) {
            clockedIn = true;
            clockedOut = false;
        } else if ((todayAttendance.getType().equals("On Time") || todayAttendance.getType().equals("Late") || todayAttendance.getType().equals("Activated")) && todayAttendance.getClockoutTime() != null) {
            clockedIn = true;
            clockedOut = true;
        }

        return todayAttendance;
    }

    public void setTodayAttendance(AttendanceEntity todayAttendance) {
        this.todayAttendance = todayAttendance;
    }

    public void clockIn() {
        clockedIn = attendanceSessionBeanLocal.clockIn(staffId);
        retrieveAttendancesByStaff();
        updateAttendance();
    }

    public void clockOut() {
        clockedOut = attendanceSessionBeanLocal.clockOut(staffId);
        retrieveAttendancesByStaff();
        updateAttendance();
    }

    public void updateAttendance() {
        for (int i = 0; i < staffs.size(); i++) {
            AttendanceEntity att = attendanceSessionBeanLocal.getAttendance(staffs.get(i));
            if (att != null) {
                this.remarks[i] = att.getRemarks();
                this.attendanceType[i] = att.getType();
                this.clockinTime[i] = att.getClockinTime();
                this.clockoutTime[i] = att.getClockoutTime();
            }
        }
    }

    private ArrayList<StaffEntity> getTodayStandbys() {
        standbyStaffs = attendanceSessionBeanLocal.getTodayStandbys(nodeCode);
        return attendanceSessionBeanLocal.getTodayStandbys(nodeCode);
    }

    private ArrayList<StaffEntity> getTodayActivated() {
        activatedStaffs = attendanceSessionBeanLocal.getTodayActivated(nodeCode);
        if (activatedStaffs != null) {

            activatedTimeList = new ArrayList<Date>();
            expectedEndTimeList = new ArrayList<Date>();
            acknowledgeList = new ArrayList<String>();
            for (int i = 0; i < activatedStaffs.size(); i++) {
                if (!activatedStaffs.get(i).getReceivedAdHocReqs().isEmpty()) {
                    activatedTimeList.add(activatedStaffs.get(i).getReceivedAdHocReqs().get(activatedStaffs.get(i).getReceivedAdHocReqs().size() - 1).getInTime());
                    expectedEndTimeList.add(activatedStaffs.get(i).getReceivedAdHocReqs().get(activatedStaffs.get(i).getReceivedAdHocReqs().size() - 1).getOutTime());
                    if (activatedStaffs.get(i).getReceivedAdHocReqs().get(activatedStaffs.get(i).getReceivedAdHocReqs().size() - 1).isAcknowledged()) {
                        acknowledgeList.add("Yes");
                    } else {
                        acknowledgeList.add("No");
                    }

                }
            }
        }
        return attendanceSessionBeanLocal.getTodayActivated(nodeCode);

    }

    public String goActivateStaff(int index) {
        selectedStaff = standbyStaffs.get(index);
        return "confirmActivation";
    }

    public String confirmStaffActivation() {
        boolean success = attendanceSessionBeanLocal.confirmStaffActivation(title, description, nodeString, inTime, outTime, selectedStaff, staffId);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "You have successfully activated " + selectedStaff.getStaffId() + " - " + selectedStaff.getFirstName() + selectedStaff.getLastName() + "!",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "You have failed to activate " + selectedStaff.getStaffId() + " - " + selectedStaff.getFirstName() + selectedStaff.getLastName() + "!",
                            ""));
        }
        selectedStaff = null;
        title = "";
        description = "";
        inTime = null;
        outTime = null;
        standbyStaffs = getTodayStandbys();
        activatedStaffs = getTodayActivated();

        return "activateStaff";
    }

    public String acknowledgeActivation() {
        boolean success = attendanceSessionBeanLocal.acknowledgeActivation(FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("reqId"));
        if (success) {
            return "acknowledge.xhtml?faces-redirect=true&amp;success=true";
        }
        return "acknowledge.xhtml?faces-redirect=true&amp;success=false";
    }

    public ArrayList<StaffEntity> getFilteredStandbyStaffs() {
        return filteredStandbyStaffs;
    }

    public void setFilteredStandbyStaffs(ArrayList<StaffEntity> filteredStandbyStaffs) {
        this.filteredStandbyStaffs = filteredStandbyStaffs;
    }

    public ArrayList<StaffEntity> getFilteredActivatedStaffs() {
        return filteredActivatedStaffs;
    }

    public void setFilteredActivatedStaffs(ArrayList<StaffEntity> filteredActivatedStaffs) {
        this.filteredActivatedStaffs = filteredActivatedStaffs;
    }

    public ArrayList<AttendanceEntity> getFilteredattendances() {
        return filteredattendances;
    }

    public void setFilteredattendances(ArrayList<AttendanceEntity> filteredattendances) {
        this.filteredattendances = filteredattendances;
    }

}
