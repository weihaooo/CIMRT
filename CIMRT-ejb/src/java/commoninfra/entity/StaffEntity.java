/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import operations.entity.AdHocRequestEntity;
import operations.entity.AttendanceEntity;
import manpower.entity.WorkshopEntity;
import operations.entity.AnnouncementEntity;
import operations.entity.IncidentReportEntity;
import operations.entity.LeaveApplicationEntity;
import operations.entity.TopUpTransactionEntity;

@Entity
@XmlRootElement
@XmlType(name = "staffEntity", propOrder = {
    "staffId",
    "firstName",
    "lastName",
    "nric",
    "phoneNumber",
    "emailAddress",
    "password",
    "address",
    "gender",
    "dateOfBirth",
    "maritalStatus",
    "race",
    "nationality",
    "religion",
    "educationQualification",
    "salary",
    "leaveEntitlement",
    "mcEntitlement",
    "wrongPasswordCount",
    "accountLock"
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String staffId;
    private String firstName;
    private String lastName;
    private String nric;
    private String phoneNumber;
    private String emailAddress;
    private String password;
    private String address;
    private String gender;
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;
    private String maritalStatus;
    private String race;
    private String nationality;
    private String religion;
    private String educationQualification;
    private int salary;
    private int leaveEntitlement;
    private int mcEntitlement;
    private int wrongPasswordCount;
    private boolean accountLock;

    @ManyToOne
    private RoleEntity staffRole;
    @ManyToMany(cascade = {CascadeType.PERSIST}, mappedBy = "participants")
    private List<WorkshopEntity> workshops = new ArrayList<WorkshopEntity>();

    
    @OneToMany(mappedBy="staff", cascade=(CascadeType.PERSIST))
    private List<AttendanceEntity> attendances = new ArrayList<AttendanceEntity>();
    
    @OneToMany(mappedBy="requester", cascade=(CascadeType.PERSIST))
    private List<AdHocRequestEntity> sentAdHocReqs = new ArrayList<AdHocRequestEntity>();
    
    @OneToMany(mappedBy="standbyStaff", cascade=(CascadeType.PERSIST))
    private List<AdHocRequestEntity> receivedAdHocReqs = new ArrayList<AdHocRequestEntity>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy="staff")
    private List<AnnouncementEntity> announcements = new ArrayList<AnnouncementEntity>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy="applicant")
    private List<LeaveApplicationEntity> leaveApplications = new ArrayList<LeaveApplicationEntity>();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy="approver")
    private List<LeaveApplicationEntity> leaveApplicationApprovals = new ArrayList<LeaveApplicationEntity>();
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "staff")
    private List<IncidentReportEntity> incidentReports;

    @OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
    private Collection<TopUpTransactionEntity> transactions = new ArrayList<TopUpTransactionEntity>();
    
    public StaffEntity() {
    }

    public StaffEntity(String staffId, String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String password, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String educationQualification, int salary, int leaveEntitlement, int mcEntitlement, int wrongPasswordCount, boolean accountLock, RoleEntity staffRole) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nric = nric;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.maritalStatus = maritalStatus;
        this.race = race;
        this.nationality = nationality;
        this.religion = religion;
        this.educationQualification = educationQualification;
        this.salary = salary;
        this.leaveEntitlement = leaveEntitlement;
        this.mcEntitlement = mcEntitlement;
        this.wrongPasswordCount = wrongPasswordCount;
        this.accountLock = accountLock;
        this.staffRole = staffRole;
    }

    public List<IncidentReportEntity> getIncidentReports() {
        return incidentReports;
    }

    public void setIncidentReports(List<IncidentReportEntity> incidentReports) {
        this.incidentReports = incidentReports;
    }
    
    public List<WorkshopEntity> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<WorkshopEntity> workshops) {
        this.workshops = workshops;
    }
    
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String getEducationQualification() {
        return educationQualification;
    }

    public void setEducationQualification(String educationQualification) {
        this.educationQualification = educationQualification;
    }

    public int getMcEntitlement() {
        return mcEntitlement;
    }

    public void setMcEntitlement(int mcEntitlement) {
        this.mcEntitlement = mcEntitlement;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getLeaveEntitlement() {
        return leaveEntitlement;
    }

    public void setLeaveEntitlement(int leaveEntitlement) {
        this.leaveEntitlement = leaveEntitlement;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWrongPasswordCount() {
        return wrongPasswordCount;
    }

    public void setWrongPasswordCount(int wrongPasswordCount) {
        this.wrongPasswordCount = wrongPasswordCount;
    }

    public boolean getAccountLock() {
        return accountLock;
    }

    public void setAccountLock(boolean accountLock) {
        this.accountLock = accountLock;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public RoleEntity getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(RoleEntity staffRole) {
        this.staffRole = staffRole;
    }


    public List<AttendanceEntity> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendanceEntity> attendances) {
        this.attendances = attendances;
    }

    public List<LeaveApplicationEntity> getLeaveApplications() {
        return leaveApplications;
    }

    public void setLeaveApplications(List<LeaveApplicationEntity> leaveApplications) {
        this.leaveApplications = leaveApplications;
    }

    public List<LeaveApplicationEntity> getLeaveApplicationApprovals() {
        return leaveApplicationApprovals;
    }

    public void setLeaveApplicationApprovals(List<LeaveApplicationEntity> leaveApplicationApprovals) {
        this.leaveApplicationApprovals = leaveApplicationApprovals;
    }

    public List<AdHocRequestEntity> getSentAdHocReqs() {
        return sentAdHocReqs;
    }

    public void setSentAdHocReqs(List<AdHocRequestEntity> sentAdHocReqs) {
        this.sentAdHocReqs = sentAdHocReqs;
    }

    public List<AdHocRequestEntity> getReceivedAdHocReqs() {
        return receivedAdHocReqs;
    }

    public void setReceivedAdHocReqs(List<AdHocRequestEntity> receivedAdHocReqs) {
        this.receivedAdHocReqs = receivedAdHocReqs;
    }

    public List<AnnouncementEntity> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<AnnouncementEntity> announcements) {
        this.announcements = announcements;
    }

    public Collection<TopUpTransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<TopUpTransactionEntity> transactions) {
        this.transactions = transactions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaffEntity)) {
            return false;
        }
        StaffEntity other = (StaffEntity) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.StaffEntity[ id=" + staffId + " ]";
    }

}
