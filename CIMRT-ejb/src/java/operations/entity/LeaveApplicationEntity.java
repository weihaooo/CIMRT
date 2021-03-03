/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.StaffEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.Part;

/**
 *
 * @author Yoona
 */
@Entity
public class LeaveApplicationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveApplicationId;
    private String type;
    @Column(length=10000)
    private String description;
    private String status;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private StaffEntity applicant;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private StaffEntity approver;

    public LeaveApplicationEntity() {
    }

    public LeaveApplicationEntity(String type, String description, String status, Date startDate, Date endDate){//), StaffEntity applicant) {
        this.type = type;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        //this.applicant = applicant;
    }
    
    public Long getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(Long leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public StaffEntity getApplicant() {
        return applicant;
    }

    public void setApplicant(StaffEntity applicant) {
        this.applicant = applicant;
    }

    public StaffEntity getApprover() {
        return approver;
    }

    public void setApprover(StaffEntity approver) {
        this.approver = approver;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (leaveApplicationId != null ? leaveApplicationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LeaveApplicationEntity)) {
            return false;
        }
        LeaveApplicationEntity other = (LeaveApplicationEntity) object;
        if ((this.leaveApplicationId == null && other.leaveApplicationId != null) || (this.leaveApplicationId != null && !this.leaveApplicationId.equals(other.leaveApplicationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.LeaveApplicationEntity[ id=" + leaveApplicationId + " ]";
    }

}
