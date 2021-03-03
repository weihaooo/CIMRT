package commoninfra.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import commoninfra.entity.AccessRightsEntity;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.RoleEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Yoona
 */
@Remote
public interface SystemAdminSessionBeanRemote {

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

    public ArrayList<StaffEntity> retrieveStaffList(String nodeCode, String teamId);

    public ArrayList<String> retrieveTeamList(String nodeCode);
    
    public ArrayList<String> retrieveDTeamList(String nodeCode, StaffEntity staff);
    
    public List<String> getNodes();

    public void removeFromTeam(StaffEntity staff);


    public ArrayList<StationStaffEntity> retrieveSStaffWithoutTeamList();

    public ArrayList<DepotStaffEntity> retrieveDStaffWithoutTeamList();

    public List<String> getStations();

    public List<String> getDepots();

    public void addStaffToTeam(String sTeam,StaffEntity staff);

}
