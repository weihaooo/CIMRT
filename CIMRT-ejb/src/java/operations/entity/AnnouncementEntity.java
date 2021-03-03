/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.StaffEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author zhuming
 */
@Entity
public class AnnouncementEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementId;
    private String title;
    @Column(length=10000)
    private String description;
    private String announcementType;
    private Timestamp dateAndTime;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private StaffEntity staff;
    private String recepientType;

    public AnnouncementEntity() {
    }

    public AnnouncementEntity(String title, String description, String announcementType, Timestamp dateAndTime, String recepientType) {
        this.title = title;
        this.description = description;
        this.announcementType = announcementType;
        this.dateAndTime = dateAndTime;
        this.recepientType = recepientType;
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

    public String getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(String announcementType) {
        this.announcementType = announcementType;
    }

    public Timestamp getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Timestamp dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public String getRecepientType() {
        return recepientType;
    }

    public void setRecepientType(String recepientType) {
        this.recepientType = recepientType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (announcementId != null ? announcementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the announcementId fields are not set
        if (!(object instanceof AnnouncementEntity)) {
            return false;
        }
        AnnouncementEntity other = (AnnouncementEntity) object;
        if ((this.announcementId == null && other.announcementId != null) || (this.announcementId != null && !this.announcementId.equals(other.announcementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operation.entity.AnnouncementEntity[ id=" + announcementId + " ]";
    }

}
