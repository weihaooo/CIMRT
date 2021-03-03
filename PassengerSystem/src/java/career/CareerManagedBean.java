/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package career;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import operations.entity.JobOfferEntity;
import operations.sessionbean.OperationsSessionBeanLocal;

/**
 *
 * @author kayleytan
 */
@Named(value = "careerManagedBean")
@SessionScoped
public class CareerManagedBean implements Serializable {

    
   @EJB
    private OperationsSessionBeanLocal OperationsSessionBeanLocal;
    
   
    private String jobId;
    private String jobTitle;
    private String jobPosition;
    private String location;
    private double jobSalary;
    private String jobType;
    private String jobDescription;
    private String jobQualifications;
    private Date postedDate;
    private Date jobDeadline;
    
    
    private String applicationId;
    private String firstName;
    private String lastName;
    private String nric;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String gender;
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
    private Date startDate;
    private Date endDate;
    private String jobIndustry;
    private String summary;

    
    private Date today = new Date();
     


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

    public double getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(double jobSalary) {
        this.jobSalary = jobSalary;
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

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }
    
    
    
    @PostConstruct
    public void init() {
        

    }
    public List<JobOfferEntity> getJobOfferList() {
    List<JobOfferEntity> jobOfferList = new ArrayList<JobOfferEntity>() ;
        ArrayList<JobOfferEntity> jobOffers = OperationsSessionBeanLocal.getJobOffer();
        ArrayList<JobOfferEntity> newjobOffers = new ArrayList<JobOfferEntity>();
         Collections.sort(jobOffers, new Comparator<JobOfferEntity>() {
            @Override
            public int compare(JobOfferEntity left, JobOfferEntity right) {
                return right.getPostedDate().compareTo(left.getPostedDate());
            }
        });
         
         for(int i=0; i<jobOffers.size(); i++){
             if(jobOffers.get(i).isJobStatus() == true){
                 newjobOffers.add(jobOffers.get(i));
             }
         }
         jobOfferList = newjobOffers;
         OperationsSessionBeanLocal.checkJob();
        return jobOfferList;
    }
    
    public String goViewJob(String jobId) {
        JobOfferEntity job = OperationsSessionBeanLocal.searchJob(jobId);
        this.jobId = job.getJobId();
        this.jobTitle = job.getJobTitle();
        this.jobPosition = job.getJobPosition();
        this.location = job.getLocation();
        this.jobSalary = job.getSalary();
        this.jobType = job.getJobType();
        this.jobDeadline = job.getJobDeadline();
        this.jobDescription = job.getJobDescription();
        this.jobQualifications = job.getJobQualifications();
        this.postedDate = job.getPostedDate();   
        return "viewJob";
    }
    
        public String applyJob() {
        boolean sta = OperationsSessionBeanLocal.createApplicant(jobId,firstName, lastName, nric, phoneNumber, emailAddress, address, gender, dateOfBirth, maritalStatus, race, nationality, religion, highestEducation, yearsOfExp, position,company, startDate, endDate,jobIndustry,summary);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Job is applied successfully",
                            ""));
            this.jobId = null;
            this.firstName = null;
            this.lastName = null;
            this.nric = null;
            this.phoneNumber = null;
            this.emailAddress = null;
            this.address = null;
            this.gender = null;
            this.dateOfBirth = null;
            this.maritalStatus = null;
            this.race = null;
            this.nationality = null;
            this.religion = null;
            this.highestEducation = null;
            this.yearsOfExp = 0;
            this.position = null;
            this.company = null;
            this.startDate = null;
            this.endDate = null;
            this.summary = null;
            this.jobIndustry = null;
            
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to apply the job!", ""));
            return "recruitment";
        }
        return "recruitment";
    }

}
