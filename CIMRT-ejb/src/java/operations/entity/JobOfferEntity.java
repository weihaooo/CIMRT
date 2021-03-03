/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kayleytan
 */
@Entity
public class JobOfferEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String jobId;
    private String jobTitle;
    private String jobPosition;
    private String location;
    private double salary;
    private String jobType;
    @Column(length=10000)
    private String jobDescription;
    @Column(length=10000)
    private String jobQualifications;
    private boolean jobStatus;
    @Temporal(value = TemporalType.DATE)
    private Date postedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date jobDeadline;

    @OneToMany(cascade={CascadeType.ALL}, mappedBy="jobOffer")
    private List<JobApplicationsEntity> jobApplications;

    public JobOfferEntity() {
    }

    public JobOfferEntity(String jobId, String jobTitle, String jobPosition, String location,String jobType, double salary, String jobDescription, String jobQualifications, boolean jobStatus, Date postedDate, Date jobDeadline) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobPosition = jobPosition;
        this.location = location;
        this.salary = salary;
        this.jobType = jobType;
        this.jobDescription = jobDescription;
        this.jobQualifications = jobQualifications;
        this.jobStatus = jobStatus;
        this.postedDate = postedDate;
        this.jobDeadline = jobDeadline;
    }

    public List<JobApplicationsEntity> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(List<JobApplicationsEntity> jobApplications) {
        this.jobApplications = jobApplications;
    }

    
    
    
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobQualifications() {
        return jobQualifications;
    }

    public void setJobQualifications(String jobQualifications) {
        this.jobQualifications = jobQualifications;
    }

    public boolean isJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(boolean jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Date getJobDeadline() {
        return jobDeadline;
    }

    public void setJobDeadline(Date jobDeadline) {
        this.jobDeadline = jobDeadline;
    }
    
    
    
  
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobId != null ? jobId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobOfferEntity)) {
            return false;
        }
        JobOfferEntity other = (JobOfferEntity) object;
        if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.JobOfferEntity[ id=" + jobId + " ]";
    }
    
}
