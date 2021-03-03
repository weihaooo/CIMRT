/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessPartner.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;
    private String title;
    @Column(length=10000)
    private String description;
    private Timestamp dateAndTime;
    private boolean checkRead;

    @ManyToOne
    private BusinessPartnerEntity businessPartner;
    
     public NotificationEntity() {
    }

    public NotificationEntity(String title, String description, Timestamp dateAndTime, Boolean checkRead) {
        this.title = title;
        this.description = description;
        this.dateAndTime = dateAndTime;
        this.checkRead = checkRead;
    }
    
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
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

    public Timestamp getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Timestamp dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public BusinessPartnerEntity getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartnerEntity businessPartner) {
        this.businessPartner = businessPartner;
    }

    public boolean isCheckRead() {
        return checkRead;
    }

    public void setCheckRead(boolean checkRead) {
        this.checkRead = checkRead;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.notificationId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NotificationEntity other = (NotificationEntity) obj;
        if (this.notificationId != other.notificationId) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "businessPartner.entity.NotificationEntity[ id=" + notificationId + " ]";
    }
    
}
