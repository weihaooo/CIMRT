/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class StaffContractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String staffContractId;
    
     private Timestamp interviewFrom;
    
    private Timestamp interviewTo;

    @OneToOne(cascade={CascadeType.PERSIST})
    private JobApplicationsEntity jobApplication;

    public StaffContractEntity() {
    }

    public StaffContractEntity(String staffContractId, Timestamp interviewFrom, Timestamp interviewTo) {
        this.staffContractId = staffContractId;
        this.interviewFrom = interviewFrom;
        this.interviewTo = interviewTo;
    }

    public String getStaffContractId() {
        return staffContractId;
    }

    public void setStaffContractId(String staffContractId) {
        this.staffContractId = staffContractId;
    }

    public Timestamp getInterviewFrom() {
        return interviewFrom;
    }

    public void setInterviewFrom(Timestamp interviewFrom) {
        this.interviewFrom = interviewFrom;
    }

    public Timestamp getInterviewTo() {
        return interviewTo;
    }

    public void setInterviewTo(Timestamp interviewTo) {
        this.interviewTo = interviewTo;
    }

    public JobApplicationsEntity getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplicationsEntity jobApplication) {
        this.jobApplication = jobApplication;
    }
    
    

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffContractId != null ? staffContractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaffContractEntity)) {
            return false;
        }
        StaffContractEntity other = (StaffContractEntity) object;
        if ((this.staffContractId == null && other.staffContractId != null) || (this.staffContractId != null && !this.staffContractId.equals(other.staffContractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.StaffContractEntity[ id=" + staffContractId + " ]";
    }
    
}
