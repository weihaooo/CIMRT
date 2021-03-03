/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.entity;

import commoninfra.entity.StaffEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author FABIAN
 */
@Entity
public class WorkshopEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workshopId;
    private String workshopType;
    private String workshopName;
    @Column(length = 10000)
    private String description;
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    private String workshopStartTime;
    private String workshopEndTime;
    private String workshopAddress;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "WORKSHOPMMBI_STAFFMMBI")
    private List<StaffEntity> participants = new ArrayList<StaffEntity>();

    public WorkshopEntity() {
    }

    public WorkshopEntity(String description, String workshopAddress, Date startDate, Date endDate, String workshopName, String workshopStartTime, String workshopEndTime, String workshopType) {
        this.setDescription(description);
        this.setWorkshopAddress(workshopAddress);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setWorkshopName(workshopName);
        this.setWorkshopStartTime(workshopStartTime);
        this.setWorkshopEndTime(workshopEndTime);
        this.setWorkshopType(workshopType);

    }

    public List<StaffEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<StaffEntity> participants) {
        this.participants = participants;
    }

    public String getWorkshopType() {
        return workshopType;
    }

    public void setWorkshopType(String workshopType) {
        this.workshopType = workshopType;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getWorkshopStartTime() {
        return workshopStartTime;
    }

    public void setWorkshopStartTime(String workshopStartTime) {
        this.workshopStartTime = workshopStartTime;
    }

    public String getWorkshopEndTime() {
        return workshopEndTime;
    }

    public void setWorkshopEndTime(String workshopEndTime) {
        this.workshopEndTime = workshopEndTime;
    }

    public String getWorkshopAddress() {
        return workshopAddress;
    }

    public void setWorkshopAddress(String workshopAddress) {
        this.workshopAddress = workshopAddress;
    }

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setId(Long workshopId) {
        this.workshopId = workshopId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (workshopId != null ? workshopId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkshopEntity)) {
            return false;
        }
        WorkshopEntity other = (WorkshopEntity) object;
        if ((this.workshopId == null && other.workshopId != null) || (this.workshopId != null && !this.workshopId.equals(other.workshopId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "manpower.entity.WorkshopEntity[ workshopId=" + workshopId + " ]";
    }

}
