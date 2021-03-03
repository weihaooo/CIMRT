/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class InterviewEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String interviewId;
   
    private Timestamp interviewFrom;
    
    private Timestamp interviewTo;

    @OneToOne(mappedBy="interview")
    private JobApplicationsEntity jobApplication;

    public InterviewEntity() {
    }

    public InterviewEntity(String interviewId, Timestamp interviewFrom, Timestamp interviewTo) {
        this.interviewId = interviewId;
        this.interviewFrom = interviewFrom;
        this.interviewTo = interviewTo;
    }
    
    public String getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(String interviewId) {
        this.interviewId = interviewId;
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
        hash += (interviewId != null ? interviewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InterviewEntity)) {
            return false;
        }
        InterviewEntity other = (InterviewEntity) object;
        if ((this.interviewId == null && other.interviewId != null) || (this.interviewId != null && !this.interviewId.equals(other.interviewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.InterviewEntity[ id=" + interviewId + " ]";
    }
    
}
