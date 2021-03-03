/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import java.io.Serializable;
import java.sql.Timestamp;
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
    private AnnouncementEntity selectedA;
    private List<AnnouncementEntity> individualList;
    private Long sessionAid;

    private List<AnnouncementEntity> filteredAnnouncement;

    @PostConstruct
    public void init() {
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        aList = announcementSessionBeanLocal.getAnnouncementList();
        individualList = announcementSessionBeanLocal.getIndividualAnnouncementList(staffId);
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

    public AnnouncementEntity getSelectedA() {
        return selectedA;
    }

    public void setSelectedA(AnnouncementEntity selectedA) {
        this.selectedA = selectedA;
    }

    public List<AnnouncementEntity> getIndividualList() {
        return individualList;
    }

    public void setIndividualList(List<AnnouncementEntity> individualList) {
        this.individualList = individualList;
    }

    public Long getSessionAid() {
        return sessionAid;
    }

    public void setSessionAid(Long sessionAid) {
        this.sessionAid = sessionAid;
    }

    public void create() {
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        Date date = new Date();
        time = new Timestamp(date.getTime());
        // StaffEntity staff = announcementSessionBeanLocal.searchStaff(staffId);
        announcementSessionBeanLocal.createAnnouncement(title, content, contentType, time, recepient, staffId);
        aList = announcementSessionBeanLocal.getAnnouncementList();
        individualList = announcementSessionBeanLocal.getIndividualAnnouncementList(staffId);
        this.title = null;
        this.contentType = null;
        this.recepient = null;
        this.content = null;
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "A new announcement has been created.",
                        ""));
    }

    public String goEdit(Long announcementId) {
        AnnouncementEntity a = announcementSessionBeanLocal.searchAnnouncement(announcementId);
        this.title = a.getTitle();
        this.contentType = a.getAnnouncementType();
        this.recepient = a.getRecepientType();
        this.content = a.getDescription();
        sessionAid = announcementId;
        return "editAnnouncement";
    }

    public String deleteAction(Long announcementId) {
        announcementSessionBeanLocal.deleteAnnouncement(announcementId);
        aList = announcementSessionBeanLocal.getAnnouncementList();
        individualList = announcementSessionBeanLocal.getIndividualAnnouncementList(staffId);
        return "viewIndividualAnnouncement?faces-redirect=true";
    }

    public void edit() {
        //Long announcementId, String newTitle, String newContent, String newType, Timestamp newDateAndTime, String newRecipient
        Date date = new Date();
        time = new Timestamp(date.getTime());
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        announcementSessionBeanLocal.createAnnouncement(title, content, contentType, time, recepient, staffId);
        //announcementSessionBeanLocal.editAnnouncement(sessionAid, title, content, contentType, time, recepient);
        this.title = null;
        this.contentType = null;
        this.recepient = null;
        this.content = null;
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Announcement has been edited.",
                        ""));

        aList = announcementSessionBeanLocal.getAnnouncementList();
        individualList = announcementSessionBeanLocal.getIndividualAnnouncementList(staffId);
    }

    public void setFilteredAnnouncement(List<AnnouncementEntity> filteredAnnouncement) {
        this.filteredAnnouncement = filteredAnnouncement;
    }

    public List<AnnouncementEntity> getFilteredAnnouncement() {
        return filteredAnnouncement;
    }
}
