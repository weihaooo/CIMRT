package commoninfra.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import businessPartner.entity.BusinessPartnerEntity;
import commoninfra.entity.AccessRightsEntity;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.RoleEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import passenger.entity.PassengerEntity;

/**
 *
 * @author Yoona
 */
@Local
public interface SystemAdminSessionBeanLocal {

    public String createAccount(String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String educationQualification, String Department, String role, int salary, String team);

    public Long createTeam(String department);

    public List<TeamEntity> getTeams(String role);

    public List<TeamEntity> getTeams();

    public String createRole(String role, String department);

    public List<RoleEntity> getRoles();

    public List<StaffEntity> getStaffs();

    public boolean deleteStaff(String staffId);

    public boolean createAccess(String role, List<String> selectedAccessRights);

    public List<AccessRightsEntity> getAccess();

    public List<RoleEntity> getRolesAccess();

    public RoleEntity searchRole(String role);

    public boolean updateAccess(String role, List<String> selectedAccessRights);

    public List<PassengerEntity> retrievePassengers();

    public boolean deletePassenger(String username);

    public List<BusinessPartnerEntity> retrievePartners();

    public boolean deletePartner(String partnerId);


}
