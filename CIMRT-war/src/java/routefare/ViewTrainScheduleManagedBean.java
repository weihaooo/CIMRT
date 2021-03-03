/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare;

import commoninfra.sessionbean.DataSessionBeanLocal;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import routefare.sessionbean.RoutePlanningSessionBeanLocal;

/**
 *
 * @author YuTing
 */
@Named(value = "viewTrainScheduleManagedBean")
@SessionScoped
public class ViewTrainScheduleManagedBean implements Serializable {

    @EJB
    private RoutePlanningSessionBeanLocal routePlanningSessionBeanLocal;
    @EJB
    private DataSessionBeanLocal dataSessionBean;    
    private Date date;
    private List<String> routes;

    private List<String> hourList;
    private String dayOfWeek;
    private String hour;
    private String route;
    private Date dateSelect;
    private ArrayList<ArrayList<String>> tripSchedules;
    private Date newStart;
    private Date newEnd;
    private double headway;
    private ArrayList<ArrayList<String>> editedTripSchedules;
    //  private Timestamp[][] tripSchedules;
    @PostConstruct
    public void init() {
      
      //routePlanningSessionBeanLocal.storeSpecialDayTripStationSchedule();
      //routePlanningSessionBeanLocal.testStoreRegularDayTripStationSchedule();
      //routes = routePlanningSessionBeanLocal.getRouteList();
      //routePlanningSessionBeanLocal.testStoreRegularDayTripStationScheduleWithRS();
      }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
        dateSelect = (Date) event.getObject();
    }

    public List<String> getHourList() {
        if (dateSelect == null) {

        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateSelect);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dayOfWeek = "Sunday";
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                dayOfWeek = "Saturday";
            } else {
                dayOfWeek = "Weekday";
            }
            Timestamp date = new java.sql.Timestamp(this.dateSelect.getTime());
            hourList = routePlanningSessionBeanLocal.getPeriodList(date, dayOfWeek);
        }
        return hourList;
    }

    public void setHourList(List<String> hourList) {
        this.hourList = hourList;
    }

    public List<String> getRoutes() {
        routes = routePlanningSessionBeanLocal.getRouteList();
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public String displayTable() {
        Timestamp date = new java.sql.Timestamp(this.dateSelect.getTime());
        //tripSchedules = routePlanningSessionBeanLocal.getCentralTrainScheduleTableStr(route, date, dayOfWeek, hour);
        if(routePlanningSessionBeanLocal.isSpecialDay(date)){
            tripSchedules = routePlanningSessionBeanLocal.getSpecialDaySchedule(date,route, dayOfWeek, hour);
        }
        else{
            //tripSchedules = routePlanningSessionBeanLocal.collateWholeDaySchedule(route, dayOfWeek);
            //tripSchedules = routePlanningSessionBeanLocal.assignRollingStock(route, dayOfWeek);
            tripSchedules = routePlanningSessionBeanLocal.getTrainSchedule(route, dayOfWeek, hour);
        }
        this.route = null;
        this.date = null;
        this.hour = null;
        return "viewTrainPeriodSchedule";
    }

    public ArrayList<ArrayList<String>> getTripSchedules() {
        return tripSchedules;
    }

    public void setTripSchedules(ArrayList<ArrayList<String>> tripSchedules) {
        this.tripSchedules = tripSchedules;
    }

    public Date getNewStart() {
        return newStart;
    }

    public void setNewStart(Date newStart) {
        this.newStart = newStart;
    }

    public Date getNewEnd() {
        return newEnd;
    }

    public void setNewEnd(Date newEnd) {
        this.newEnd = newEnd;
    }

    public double getHeadway() {
        return headway;
    }

    public void setHeadway(double headway) {
        this.headway = headway;
    }

    public ArrayList<ArrayList<String>> getEditedTripSchedules() {
        return editedTripSchedules;
    }

    public void setEditedTripSchedules(ArrayList<ArrayList<String>> editedTripSchedules) {
        this.editedTripSchedules = editedTripSchedules;
    }


}
