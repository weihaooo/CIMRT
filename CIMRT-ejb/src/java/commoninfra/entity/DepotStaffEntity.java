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
import operations.entity.InspectionReportEntity;
import operations.entity.TripReportEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class DepotStaffEntity extends StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private DepotTeamEntity depotTeam;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "depotStaff")
    private List<InspectionReportEntity> inspectReports;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "depotStaff")
    private List<TripReportEntity> tripReports;

    public DepotStaffEntity() {

    }

    public DepotStaffEntity(String staffId, String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String password, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String educationQualification, int salary, int leaveEntitlement, int mcEntitlement, int wrongPasswordCount, boolean accountLock, RoleEntity staffRole, DepotTeamEntity depotTeam) {
        super(staffId, firstName, lastName, nric, phoneNumber, emailAddress, password, address, gender, dateOfBirth, maritalStatus, race, nationality, religion, educationQualification, salary, leaveEntitlement, mcEntitlement, wrongPasswordCount, accountLock, staffRole);
        this.depotTeam = depotTeam;
    }

    public DepotTeamEntity getDepotTeam() {
        return depotTeam;
    }

    public void setDepotTeam(DepotTeamEntity depotTeam) {
        this.depotTeam = depotTeam;
    }

    public List<InspectionReportEntity> getInspectReports() {
        return inspectReports;
    }

    public void setInspectReports(List<InspectionReportEntity> inspectReports) {
        this.inspectReports = inspectReports;
    }

    public List<TripReportEntity> getTripReports() {
        return tripReports;
    }

    public void setTripReports(List<TripReportEntity> tripReports) {
        this.tripReports = tripReports;
    }

}
