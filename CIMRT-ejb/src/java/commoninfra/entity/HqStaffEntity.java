/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import operations.entity.JobOfferEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class HqStaffEntity extends StaffEntity implements Serializable {

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<JobOfferEntity> jobs;

    private static final long serialVersionUID = 1L;

    public List<JobOfferEntity> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobOfferEntity> jobs) {
        this.jobs = jobs;
    }

    public HqStaffEntity() {

    }

    public HqStaffEntity(String staffId, String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String password, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String educationQualification, int salary, int leaveEntitlement, int mcEntitlement, int wrongPasswordCount, boolean accountLock, RoleEntity staffRole) {
        super(staffId, firstName, lastName, nric, phoneNumber, emailAddress, password, address, gender, dateOfBirth, maritalStatus, race, nationality, religion, educationQualification, salary, leaveEntitlement, mcEntitlement, wrongPasswordCount, accountLock, staffRole);
    }
}
