/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kayleytan
 */
@Entity
public class JobApplicationsEntity implements Serializable {

   private static final long serialVersionUID = 1L;
    @Id
    private String applicationId;
    private String firstName;
    private String lastName;
    private String nric;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String gender;
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;
    private String maritalStatus;
    private String race;
    private String nationality;
    private String religion;
    private String highestEducation;
    private int yearsOfExp;
    
    /*Lastet Work experience*/
    private String position;
    private String company;
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    private String jobIndustry;
    @Column(length=10000)
    private String summary;
    
    private String appStatus;
    
    @OneToOne(cascade={CascadeType.PERSIST})
    private InterviewEntity interview;
    
    @ManyToOne
    private JobOfferEntity jobOffer;

    public JobApplicationsEntity() {
    }

    public JobApplicationsEntity(String applicationId, String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String highestEducation, int yearsOfExp, String position, String company, Date startDate, Date endDate, String jobIndustry, String summary, String appStatus) {
        this.applicationId = applicationId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nric = nric;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.maritalStatus = maritalStatus;
        this.race = race;
        this.nationality = nationality;
        this.religion = religion;
        this.highestEducation = highestEducation;
        this.yearsOfExp = yearsOfExp;
        this.position = position;
        this.company = company;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobIndustry = jobIndustry;
        this.summary = summary;
        this.appStatus = appStatus;
    }

    
    
    
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public int getYearsOfExp() {
        return yearsOfExp;
    }

    public void setYearsOfExp(int yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getJobIndustry() {
        return jobIndustry;
    }

    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public InterviewEntity getInterview() {
        return interview;
    }

    public void setInterview(InterviewEntity interview) {
        this.interview = interview;
    }

    public JobOfferEntity getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(JobOfferEntity jobOffer) {
        this.jobOffer = jobOffer;
    }

    
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (applicationId != null ? applicationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobApplicationsEntity)) {
            return false;
        }
        JobApplicationsEntity other = (JobApplicationsEntity) object;
        if ((this.applicationId == null && other.applicationId != null) || (this.applicationId != null && !this.applicationId.equals(other.applicationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.JobApplicationsEntity[ id=" + applicationId + " ]";
    }
    
}
