/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import operations.entity.AnnouncementEntity;
import operations.sessionbean.AnnouncementSessionBeanLocal;

/**
 *
 * @author YuTing
 */
@Named(value = "announcementManagedBean")
@SessionScoped
public class AnnouncementManagedBean implements Serializable {

    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBeanLocal;
    private String title;
    private String contentType;
    private String recepient;
    private String content;
    private String staffId;
    private Timestamp time;
    private ArrayList<AnnouncementEntity> aList;
    private Long sessionAid;

    private List<AnnouncementEntity> filteredAnnouncement;

    @PostConstruct
    public void init() {
        aList = announcementSessionBeanLocal.getPassengerAnnouncements();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public ArrayList<AnnouncementEntity> getaList() {
        return aList;
    }

    public void setaList(ArrayList<AnnouncementEntity> aList) {
        this.aList = aList;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Long getSessionAid() {
        return sessionAid;
    }

    public void setSessionAid(Long sessionAid) {
        this.sessionAid = sessionAid;
    }

    public ArrayList<String> getLatestAnnouncements() {
        ArrayList<AnnouncementEntity> passengerAnnouncements = announcementSessionBeanLocal.getPassengerAnnouncements();
        ArrayList<String> latestAnnouncements = new ArrayList<String>();
        //String newline = System.getProperty("line.separator");
        if (passengerAnnouncements.size() > 3) {
            for (int i = 0; i < 3; i++) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateTime = dateFormat.format(passengerAnnouncements.get(i).getDateAndTime());
                System.out.println(passengerAnnouncements.get(i).getDescription());
                latestAnnouncements.add(passengerAnnouncements.get(i).getDescription() + "   [ Posted on: " + dateTime + " ]   ");
            }
        } else {
            for (int i = 0; i < passengerAnnouncements.size(); i++) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateTime = dateFormat.format(passengerAnnouncements.get(i).getDateAndTime());
                latestAnnouncements.add(passengerAnnouncements.get(i).getDescription() + "   [Posted on: " + dateTime + "]   ");
            }
        }
        return latestAnnouncements;
    }

    public void setFilteredAnnouncement(List<AnnouncementEntity> filteredAnnouncement) {
        this.filteredAnnouncement = filteredAnnouncement;
    }

    public List<AnnouncementEntity> getFilteredAnnouncement() {
        return filteredAnnouncement;
    }
}
