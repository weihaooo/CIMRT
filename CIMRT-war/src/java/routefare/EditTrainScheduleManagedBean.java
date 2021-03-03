/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import routefare.entity.TrainScheduleEntity;
import routefare.sessionbean.RoutePlanningSessionBeanLocal;

/**
 *
 * @author YuTing
 */
@Named(value = "editTrainScheduleManagedBean")
@ApplicationScoped
public class EditTrainScheduleManagedBean implements Serializable {

    @EJB
    private RoutePlanningSessionBeanLocal routePlanningSessionBeanLocal;
    private String dayType;
    private ArrayList<TrainScheduleEntity> schedules;
    private ArrayList<TrainScheduleEntity> newSchedules;
    private ArrayList<TrainScheduleEntity> updatedSchedules;
    private Long scheduleId;
    private String periodType;
    private Timestamp periodStart;
    private Timestamp periodEnd;
    private double headway;
    private double waitingTime;

    private String newPeriodType;
    private Date newPeriodStart;
    private Date newPeriodEnd;
    private Timestamp newStart;
    private Timestamp newEnd;
    private double newHeadway;
    private double newWaitingTime;

    private String sessionDayType;
    private Long passScheduleId;

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public ArrayList<TrainScheduleEntity> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<TrainScheduleEntity> schedules) {
        this.schedules = schedules;
    }

    public void onDayTypeChange() {
        ArrayList<TrainScheduleEntity> retrivedSchedules = routePlanningSessionBeanLocal.viewTrainSchedule(dayType);
        this.schedules = retrivedSchedules;
    }

    public String goDelete() {
        sessionDayType = this.dayType;
        routePlanningSessionBeanLocal.createTempTrainSchedule(sessionDayType);
        this.newSchedules = routePlanningSessionBeanLocal.getTempTrainSchedule();
        this.dayType = null;
        this.schedules = null;
        return "updateTrainScheduleStructure";
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

    public Timestamp getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Timestamp periodStart) {
        this.periodStart = periodStart;
    }

    public Timestamp getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Timestamp periodEnd) {
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

    public ArrayList<TrainScheduleEntity> getNewSchedules() {
        return newSchedules;
    }

    public void setNewSchedules(ArrayList<TrainScheduleEntity> newSchedules) {
        this.newSchedules = newSchedules;
    }

    public String getNewPeriodType() {
        return newPeriodType;
    }

    public void setNewPeriodType(String newPeriodType) {
        this.newPeriodType = newPeriodType;
    }

    public Date getNewPeriodStart() {

        return newPeriodStart;
    }

    public void setNewPeriodStart(Date newPeriodStart) {
        this.newPeriodStart = newPeriodStart;
    }

    public Date getNewPeriodEnd() {
        return newPeriodEnd;
    }

    public void setNewPeriodEnd(Date newPeriodEnd) {
        this.newPeriodEnd = newPeriodEnd;
    }

    public double getNewHeadway() {
        return newHeadway;
    }

    public void setNewHeadway(double newHeadway) {
        this.newHeadway = newHeadway;
    }

    public double getNewWaitingTime() {
        return newWaitingTime;
    }

    public String getSessionDayType() {
        return sessionDayType;
    }

    public void setSessionDayType(String sessionDayType) {
        this.sessionDayType = sessionDayType;
    }

    public void setNewWaitingTime(double newWaitingTime) {
        this.newWaitingTime = newWaitingTime;
    }

    public ArrayList<TrainScheduleEntity> getUpdatedSchedules() {
        return updatedSchedules;
    }

    public void setUpdatedSchedules(ArrayList<TrainScheduleEntity> updatedSchedules) {
        this.updatedSchedules = updatedSchedules;
    }

    public String addAction() {
        newStart = new java.sql.Timestamp(this.newPeriodStart.getTime());
        newEnd = new java.sql.Timestamp(this.newPeriodEnd.getTime());
        if(isValidPeriod(newStart, newEnd)){
        //System.out.println(dayType + "-" + newPeriodType + "-" + newStart + "-" + newEnd + "-" + newHeadway + "-" + newWaitingTime);
        routePlanningSessionBeanLocal.addTempTrainSchedule(sessionDayType, this.newPeriodType, this.newStart, this.newEnd, this.newHeadway, this.newWaitingTime);
        //System.out.println("after");
        this.newSchedules = routePlanningSessionBeanLocal.getTempTrainSchedule();
        this.newPeriodType = null;
        this.newPeriodStart = null;
        this.newPeriodEnd = null;
        this.newHeadway = 0;
        this.newWaitingTime = 0;
        return "updateTrainScheduleStructure";
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

    public String editAction() {
        Timestamp tempStart = new java.sql.Timestamp(this.newPeriodStart.getTime());
        Timestamp tempEnd = new java.sql.Timestamp(this.newPeriodEnd.getTime());
        //System.out.println(scheduleId + "-" + newPeriodType + "-" + tempStart + "-" + tempEnd + "-" + newHeadway + "-" + newWaitingTime);
        if(isValidPeriod(tempStart, tempEnd)){
        routePlanningSessionBeanLocal.editTempTrainSchedule(passScheduleId, newPeriodType, tempStart, tempEnd, newHeadway, newWaitingTime);
        return "updateTrainScheduleStructure";
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

    public boolean isValidPeriod(Timestamp start, Timestamp end) {
        if (end.before(start)) {
            return false;
        } else {
            return true;
        }
    }

    public String deleteAction(Long scheduleId) {
        //this.scheduleId = scheduleId;
        //System.out.println("oooooo" + scheduleId);
        routePlanningSessionBeanLocal.deleteTempTrainSchedule(scheduleId);
        return "updateTrainScheduleStructure?faces-redirect=true";
    }

    public String goEdit(Long scheduleId) {
        TrainScheduleEntity ts = routePlanningSessionBeanLocal.searchTempTrainSchedule(scheduleId);
        this.newPeriodType = ts.getPeriodType();
        this.newPeriodStart = ts.getPeriodStart();
        this.newPeriodEnd = ts.getPeriodEnd();
        this.newHeadway = ts.getHeadway();
        this.newWaitingTime = ts.getWaitingTime();
        passScheduleId = scheduleId;
        //System.out.println("entered");
        return "editTrainScheduleStructure";
    }

    public String update() {
        ArrayList<TrainScheduleEntity> tempSchedules = routePlanningSessionBeanLocal.getTempTrainSchedule();
        if (isOverlap(tempSchedules)) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Please re-organise the train schedule. Errors might be caused by empty gap or overlap between two consecutive periods.",
                            ""));
            return "";
        } else {
            routePlanningSessionBeanLocal.updateTrainSchedule(sessionDayType);
            updatedSchedules = routePlanningSessionBeanLocal.viewTrainSchedule(sessionDayType);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Train Schedule Structure has been updated successfully.",
                            ""));
            return "viewUpdateTrainScheduleResult";
        }

    }

    public boolean isOverlap(ArrayList<TrainScheduleEntity> tempSchedules) {
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
        Collections.sort(tempSchedules, new Comparator<TrainScheduleEntity>() {

            public int compare(TrainScheduleEntity o1, TrainScheduleEntity o2) {
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

}
