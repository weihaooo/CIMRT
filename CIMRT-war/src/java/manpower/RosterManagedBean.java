/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import manpower.entity.RosterEntity;
import manpower.sessionbean.RosterSessionBeanLocal;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Named(value = "rosterManagedBean")
@SessionScoped
public class RosterManagedBean implements Serializable {

    @EJB
    private RosterSessionBeanLocal rosterSessionBeanLocal;

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    private ArrayList<ArrayList<RosterEntity>> rosterList;
    private NodeEntity node;
    private List<String> nodes;
    private String nodeId;
    private List<Date> dateRange;
    private Date endDate;
    private Date minDate;
    private Date startDate;
    private Date lastDate;
    private Date todayStart;
    private Date todayLast;
    private Date maxDate;
    private Date maxDateStart;
    private ArrayList<RosterEntity> rosters;
    private ArrayList<Long> shiftOrder;
    private String morningShift;
    private String afternoonShift;
    private String nightShift;
    private String offDay;

    private String staffId;
    private String firstName;
    private String lastName;
    private String role;
    private String team;
    private ArrayList<String> staffDetails;

    private ArrayList<String> shiftList;
    private ArrayList<Date> dateList;

    @PostConstruct
    public void init() {

        retrieveMinDate();
        retrieveMaxDate();
        todayStart = new Date();
        todayLast = new Date();

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            firstName = staffDetails.get(0);
            lastName = staffDetails.get(1);
            role = staffDetails.get(14);
            if (staffDetails.size() == 19) {
                team = staffDetails.get(17);
                nodeId = staffDetails.get(18);
                updateRosterByNode();
            }

        }
    }

    public RosterManagedBean() {
    }

    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }

    public List<String> getNodes() {
        return rosterSessionBeanLocal.getNodes();
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void updateTable() {
        updateRosterByNode();
        updateDateRange();
        if (startDate != null) {
            todayLast = startDate;
        }
        if (lastDate != null) {
            maxDateStart = lastDate;
        }
    }

    public void updateRosterByNode() {
        this.rosterList = rosterSessionBeanLocal.getRosterByNode(nodeId, startDate, lastDate);
        if (role.equals("Station Staff") || role.equals("Maintenance Staff") || role.equals("Train Driver")) {
            shiftList = new ArrayList<String>();
            dateList = new ArrayList<Date>();
            for (int i = 0; i < rosterList.size(); i++) {
                for (int x = 0; x < rosterList.get(i).size(); x++) {
                    if (rosterList.get(i).get(x).getTeam().getTeamId() == Long.parseLong(team)) {
                        shiftList.add(rosterList.get(i).get(x).getShift().getShiftName());
                    }
                }
            }
            for (int i = 0; i < rosterList.size(); i++) {
                dateList.add(rosterList.get(i).get(0).getDate());

            }
        }

    }

    public void updateDateRange() {
        this.dateRange = rosterSessionBeanLocal.updateDateRange(nodeId);
    }

    public List<Date> getDateRange() {
        return dateRange;
    }

    public void setDateRange(List<Date> dateRange) {
        this.dateRange = dateRange;
    }

    public ArrayList<ArrayList<RosterEntity>> getRosterList() {
        return rosterList;
    }

    public void setRosterList(ArrayList<ArrayList<RosterEntity>> rosterList) {
        this.rosterList = rosterList;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public Date getTodayStart() {
        return todayStart;
    }

    public void setTodayStart(Date todayStart) {
        this.todayStart = todayStart;
    }

    public Date getTodayLast() {
        return todayLast;
    }

    public void setTodayLast(Date todayLast) {
        this.todayLast = todayLast;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Date getMaxDateStart() {
        return maxDateStart;
    }

    public void setMaxDateStart(Date maxDateStart) {
        this.maxDateStart = maxDateStart;
    }

    public void retrieveMaxDate() {
        this.maxDate = rosterSessionBeanLocal.retrieveMaxDate();
        this.maxDateStart = rosterSessionBeanLocal.retrieveMaxDate();
    }

    public void retrieveMinDate() {
        this.minDate = rosterSessionBeanLocal.retrieveMinDate();
    }

    public ArrayList<RosterEntity> getRosters() {
        return rosters;
    }

    public void setRosters(ArrayList<RosterEntity> rosters) {
        this.rosters = rosters;
    }

    public ArrayList<Long> getShiftOrder() {
        return shiftOrder;
    }

    public void setShiftOrder(ArrayList<Long> shiftOrder) {
        this.shiftOrder = shiftOrder;
    }

    public String getMorningShift() {
        return morningShift;
    }

    public void setMorningShift(String morningShift) {
        this.morningShift = morningShift;
    }

    public String getAfternoonShift() {
        return afternoonShift;
    }

    public void setAfternoonShift(String afternoonShift) {
        this.afternoonShift = afternoonShift;
    }

    public String getNightShift() {
        return nightShift;
    }

    public void setNightShift(String nightShift) {
        this.nightShift = nightShift;
    }

    public String getOffDay() {
        return offDay;
    }

    public void setOffDay(String offDay) {
        this.offDay = offDay;
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

    public ArrayList<String> getShiftList() {
        return shiftList;
    }

    public void setShiftList(ArrayList<String> shiftList) {
        this.shiftList = shiftList;
    }

    public ArrayList<Date> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<Date> dateList) {
        this.dateList = dateList;
    }

    public void generateRoster() {
        rosterSessionBeanLocal.generateMSRoster(endDate);
        rosterSessionBeanLocal.generateSSRoster(endDate);
        rosterSessionBeanLocal.generateTDRoster(endDate);
        retrieveMinDate();
        retrieveMaxDate();
        updateRosterByNode();
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Generate successfully!",
                        ""));

    }

    public String goEditRoster(ArrayList<RosterEntity> rosters) {
        this.rosters = rosters;
        shiftOrder = new ArrayList<Long>();
        shiftOrder.add(rosters.get(0).getTeam().getTeamId());
        shiftOrder.add(rosters.get(1).getTeam().getTeamId());
        shiftOrder.add(rosters.get(2).getTeam().getTeamId());
        shiftOrder.add(rosters.get(3).getTeam().getTeamId());
        morningShift = shiftOrder.get(0).toString();
        afternoonShift = shiftOrder.get(1).toString();
        nightShift = shiftOrder.get(2).toString();
        offDay = shiftOrder.get(3).toString();
        return "editRoster";
    }

    public String editRoster() {
        boolean success = rosterSessionBeanLocal.editRoster(rosters, morningShift, afternoonShift, nightShift, offDay);
        if (success) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Edit successfully!",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Edit entry fails!",
                            "Duplicate Entries or Consecutive Working Shift! "));
        }
        return "roster";
    }

    public void validateRosterValue() {
        if (!morningShift.equals(afternoonShift) && !afternoonShift.equals(nightShift) && !nightShift.equals(offDay) && !morningShift.equals(nightShift) && !morningShift.equals(offDay) && !afternoonShift.equals(offDay)) {
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Duplicate Entries!",
                            ""));
        }
    }
}
