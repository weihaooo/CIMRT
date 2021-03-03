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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import operations.entity.ServiceLogEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class StationStaffEntity extends StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private StationTeamEntity stationTeam;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "stationStaff")
    private List<ServiceLogEntity> svcLogs;

    public StationStaffEntity() {

    }

    public StationStaffEntity(String staffId, String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String password, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String educationQualification, int salary, int leaveEntitlement, int mcEntitlement, int wrongPasswordCount, boolean accountLock, RoleEntity staffRole, StationTeamEntity stationTeam) {
        super(staffId, firstName, lastName, nric, phoneNumber, emailAddress, password, address, gender, dateOfBirth, maritalStatus, race, nationality, religion, educationQualification, salary, leaveEntitlement, mcEntitlement, wrongPasswordCount, accountLock, staffRole);
        this.stationTeam = stationTeam;
    }

    public StationTeamEntity getStationTeam() {
        return stationTeam;
    }

    public void setStationTeam(StationTeamEntity stationTeam) {
        this.stationTeam = stationTeam;
    }

    public List<ServiceLogEntity> getSvcLogs() {
        return svcLogs;
    }

    public void setSvcLogs(List<ServiceLogEntity> svcLogs) {
        this.svcLogs = svcLogs;
    }

}
