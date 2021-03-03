/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import routefare.entity.SpecialDayAlgoEntity;
import routefare.sessionbean.RoutePlanningSessionBeanLocal;

/**
 *
 * @author YuTing
 */
@Named(value = "specialDayManagedBean")
@ApplicationScoped
public class SpecialDayManagedBean implements Serializable {

    @EJB
    private RoutePlanningSessionBeanLocal routePlanningSessionBeanLocal;
    private ArrayList<SpecialDayAlgoEntity> schedules;
    private ArrayList<SpecialDayAlgoEntity> updateSchedules;
    private ArrayList<SpecialDayAlgoEntity> viewSchedules;
    private Long scheduleId;
    private String periodType;
    private Date periodStart;
    private Date periodEnd;
    private double headway;
    private double waitingTime;
    private Date day;
    private Timestamp sessionDay;
    private Long passScheduleId;
    private String viewDate;
    private Date today;
    
    @PostConstruct
    public void init() {
      today = new Date();
    }

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public ArrayList<SpecialDayAlgoEntity> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<SpecialDayAlgoEntity> schedules) {
        this.schedules = schedules;
    }

    public ArrayList<SpecialDayAlgoEntity> getViewSchedules() {
        return viewSchedules;
    }

    public void setViewSchedules(ArrayList<SpecialDayAlgoEntity> viewSchedules) {
        this.viewSchedules = viewSchedules;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public double getHeadway() {
        return headway;
    }

    public void setHeadway(double headway) {
        this.headway = headway;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Timestamp getSessionDay() {
        return sessionDay;
    }

    public void setSessionDay(Timestamp sessionDay) {
        this.sessionDay = sessionDay;
    }

    public ArrayList<SpecialDayAlgoEntity> getUpdateSchedules() {
        return updateSchedules;
    }

    public void setUpdateSchedules(ArrayList<SpecialDayAlgoEntity> updateSchedules) {
        this.updateSchedules = updateSchedules;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
        day = (Date) event.getObject();
        sessionDay = new java.sql.Timestamp(this.day.getTime());
        viewSchedules = routePlanningSessionBeanLocal.viewUpdatedSpecialDaySchedule(sessionDay);
    }

    public String addAction() {

        //System.out.println(dayType + "-" + newPeriodType + "-" + newStart + "-" + newEnd + "-" + newHeadway + "-" + newWaitingTime);
        Timestamp start = new java.sql.Timestamp(this.periodStart.getTime());
        Timestamp end = new java.sql.Timestamp(this.periodEnd.getTime());
        if(isValidPeriod(start, end)){
        routePlanningSessionBeanLocal.addSpecialDayTrainSchedule(sessionDay, periodType, start, end, headway, waitingTime);
        this.periodType = null;
        this.periodStart = null;
        this.periodEnd = null;
        this.headway = 0;
        this.waitingTime = 0;
        return "addSpecialDaySchedule";
        }
        else{
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Please re-organise the period. End time is before start time.",
                            ""));
            return "";
        }
    }

    public String goEdit(Long scheduleId) {
        SpecialDayAlgoEntity sp = routePlanningSessionBeanLocal.searchSpecialDayTrainSchedule(scheduleId);
        this.periodType = sp.getPeriodType();
        this.periodStart = sp.getPeriodStart();
        this.periodEnd = sp.getPeriodEnd();
        this.headway = sp.getHeadway();
        this.waitingTime = sp.getWaitingTime();
        passScheduleId = scheduleId;
        return "editSpecialDaySchedule";
    }

    public String editAction() {
        Timestamp tempStart = new java.sql.Timestamp(this.periodStart.getTime());
        Timestamp tempEnd = new java.sql.Timestamp(this.periodEnd.getTime());
        if(isValidPeriod(tempStart, tempEnd)){
        routePlanningSessionBeanLocal.editSpecialDayTrainSchedule(passScheduleId, periodType, tempStart, tempEnd, headway, waitingTime);
        return "addSpecialDaySchedule";
        }
        else{
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Please re-organise the period. End time is before start time.",
                            ""));
            return "";
        }
    }

    public String deleteAction(Long scheduleId) {
        routePlanningSessionBeanLocal.deleteSpecialDayTrainSchedule(scheduleId);
        return "addSpecialDaySchedule?faces-redirect=true";
    }

    public String update() {
        ArrayList<SpecialDayAlgoEntity> tempSchedules = routePlanningSessionBeanLocal.viewSpecialDaySchedule();
        viewDate = sessionDay.toString();
        viewDate = viewDate.substring(0,10);
        if (isOverlap(tempSchedules)) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Please re-organise the train schedule. Errors might be caused by empty gap or overlap between two consecutive periods.",
                            ""));
          return "";
        } else {
            routePlanningSessionBeanLocal.updateSpecialDayTrainSchedule(sessionDay);
            updateSchedules = routePlanningSessionBeanLocal.viewUpdatedSpecialDaySchedule(sessionDay);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Train Schedule Structure has been updated successfully.",
                            ""));
            
            return "viewUpdateSpecialDaySchedule";
        }
        
        
    }
    
        public String goAdjust() {
            routePlanningSessionBeanLocal.createTempSpecialDayTrainSchedule(sessionDay);
            this.schedules = routePlanningSessionBeanLocal.viewSpecialDaySchedule();
            this.day = null;
            return "addSpecialDaySchedule";
    }

    public boolean isOverlap(ArrayList<SpecialDayAlgoEntity> tempSchedules) {
        Boolean isOverlap;
        isOverlap = false;
//        if (newSchedules.size() != 0) {
//            for (int i = 0; i < newSchedules.size(); i++) {
//                Timestamp stdStart = newSchedules.get(i).getPeriodStart();
//                Timestamp stdEnd = newSchedules.get(i).getPeriodEnd();
//                if ((start.before(stdEnd) && start.after(stdStart)) || (end.before(stdEnd) && end.after(stdStart))) {
//                    isOverlap = true;
//                }
//            }
//        }
        Collections.sort(tempSchedules, new Comparator<SpecialDayAlgoEntity>() {

            public int compare(SpecialDayAlgoEntity o1, SpecialDayAlgoEntity o2) {
                if (o1.getPeriodStart() == null || o2.getPeriodStart() == null) {
                    return 0;
                }
                return o1.getPeriodStart().compareTo(o2.getPeriodStart());
            }
        }
        );
        for (int i = 1; i < tempSchedules.size(); i++) {
            if (!tempSchedules.get(i).getPeriodStart().equals(tempSchedules.get(i - 1).getPeriodEnd())) {
                isOverlap = true;
            }

        }
        return isOverlap;
    }
    
    public boolean isValidPeriod(Timestamp start, Timestamp end){
        if(end.before(start)){
            return false;
        }
        else{
            return true;
        }
    }

}
