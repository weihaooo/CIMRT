/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import operations.sessionbean.OperationsSessionBeanLocal;
import routefare.entity.TripStationScheduleEntity;
import routefare.sessionbean.RoutePlanningSessionBeanLocal;

/**
 *
 * @author YuTing
 */
@Named(value = "nodeScheduleManagedBean")
@SessionScoped
public class NodeScheduleManagedBean implements Serializable {

    @EJB
    private RoutePlanningSessionBeanLocal routePlanningSessionBeanLocal;
    private String route;
    private List<String> routes;
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
    @EJB
    private OperationsSessionBeanLocal opSessionBeanLocal;

    private String staffId;
    private String firstName;
    private String lastName;
    private String role;
    private String team;
    private ArrayList<String> staffDetails;
    private String nodeCode;
    private ArrayList<TripStationScheduleEntity> viewSchedules;
    private double timeDelayed;
    private String sessionRoute;
    private String sessionNode;
    private boolean canSubmitDelayRequest;
    private boolean isDepot;
    
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
                nodeCode = opSessionBeanLocal.searchTeam(team).getNode().getCode();
            }
            if(role.equals("Depot Manager")){
                canSubmitDelayRequest = true;
                isDepot = true;
            }
            else{
                canSubmitDelayRequest = false;
                isDepot = false;
            }
            
        }
        
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public List<String> getRoutes() {
        routes = routePlanningSessionBeanLocal.getNodeRouteList(nodeCode);
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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

    public ArrayList<TripStationScheduleEntity> getViewSchedules() {
        return viewSchedules;
    }

    public void setViewSchedules(ArrayList<TripStationScheduleEntity> viewSchedules) {
        this.viewSchedules = viewSchedules;
    }

    public double getTimeDelayed() {
        return timeDelayed;
    }

    public void setTimeDelayed(double timeDelayed) {
        this.timeDelayed = timeDelayed;
    }

    public String getSessionRoute() {
        return sessionRoute;
    }

    public void setSessionRoute(String sessionRoute) {
        this.sessionRoute = sessionRoute;
    }

    public String getSessionNode() {
        return sessionNode;
    }

    public void setSessionNode(String sessionNode) {
        this.sessionNode = sessionNode;
    }

    public boolean isCanSubmitDelayRequest() {
        return canSubmitDelayRequest;
    }

    public void setCanSubmitDelayRequest(boolean canSubmitDelayRequest) {
        this.canSubmitDelayRequest = canSubmitDelayRequest;
    }

    public boolean isIsDepot() {
        return isDepot;
    }

    public void setIsDepot(boolean isDepot) {
        this.isDepot = isDepot;
    }

    public void onRouteChange() {
        String dayOfWeek;
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayOfWeek = "Sunday";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            dayOfWeek = "Saturday";
        } else {
            dayOfWeek = "Weekday";
        }

        calendar.setTime(today);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayOfWeek = "Sunday";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            dayOfWeek = "Saturday";
        } else {
            dayOfWeek = "Weekday";
        }
        
        Date date = new Date();
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        call.set(Calendar.HOUR_OF_DAY, 0);
        call.set(Calendar.MINUTE, 0);
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        date = call.getTime();
        Timestamp date1 = new java.sql.Timestamp(date.getTime());
        viewSchedules = routePlanningSessionBeanLocal.getTripStationSchedule(route, nodeCode, date1, dayOfWeek);
    }

    public String goEdit() {
        sessionRoute = route;
        sessionNode = nodeCode;
        return "editNodeSchedule";
    }

    public void update() {
        String dayOfWeek;
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayOfWeek = "Sunday";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            dayOfWeek = "Saturday";
        } else {
            dayOfWeek = "Weekday";
        }
        Date date = new Date();
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        call.set(Calendar.HOUR_OF_DAY, 0);
        call.set(Calendar.MINUTE, 0);
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        date = call.getTime();
        Timestamp date1 = new java.sql.Timestamp(date.getTime());
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        routePlanningSessionBeanLocal.createEmergencyTripStationSchedule(sessionRoute, date1, dayOfWeek, currTime, timeDelayed);
        viewSchedules = routePlanningSessionBeanLocal.getTripStationSchedule(sessionRoute, sessionNode, date1, dayOfWeek);
    }
}
