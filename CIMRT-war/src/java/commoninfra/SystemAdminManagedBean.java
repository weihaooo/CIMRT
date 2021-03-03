/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra;

import businessPartner.entity.BusinessPartnerEntity;
import commoninfra.sessionbean.DataSessionBeanLocal;
import commoninfra.sessionbean.SystemAdminSessionBeanLocal;
import commoninfra.entity.AccessRightsEntity;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.DepotTeamEntity;
import commoninfra.entity.RoleEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import manpower.sessionbean.RosterSessionBeanLocal;
import passenger.entity.PassengerEntity;

/**
 *
 * @author Yoona
 */
@Named(value = "systemAdminManagedBean")
@SessionScoped
public class SystemAdminManagedBean implements Serializable {

    @EJB
    private SystemAdminSessionBeanLocal systemAdminSessionBeanLocal;
    @EJB
    private RosterSessionBeanLocal rosterSessionBeanLocal;
    @EJB
    private DataSessionBeanLocal dataSessionBeanLocal;
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
    private String educationQualification;
    private int salary;
    private String department;
    private String role;
    private String team;
    private List<TeamEntity> teams;
    private List<String> teamNumbers;
    private List<RoleEntity> roles;
    private List<String> roleNames;
    private List<String> roleNames1;
    private List<StaffEntity> staffs;
    private List<String> hqList;
    private List<String> depotList;
    private List<String> stationList;

    private List<AccessRightsEntity> access;
    private List<String> accessRightsList;
    private List<String> selectedAccessRights;
    private List<RoleEntity> currentAccessRights;

    private String nodeCode;
    private List<String> nodeList;
    private ArrayList<String> teamList;
    private ArrayList<StaffEntity> staffList;
    private String teamId;
    private ArrayList<StationStaffEntity> sStaffWithoutTeamList;
    private ArrayList<DepotStaffEntity> dStaffWithoutTeamList;
    private String station;
    private String depot;
    private List<String> sList;
    private List<String> dList;
    private String sTeam;
    private String dTeam;
    private List<String> sTeamList;
    private List<String> dTeamList;
    
    private List<PassengerEntity> passengers;
    private List<BusinessPartnerEntity> partners;

    public SystemAdminManagedBean() {
    }

    @PostConstruct
    public void init() {

        dataSessionBeanLocal.createRosterBasis();

        nodeList = rosterSessionBeanLocal.getNodes();
        sList = rosterSessionBeanLocal.getStations();
        dList = rosterSessionBeanLocal.getDepots();
        retrieveStaffWithoutTeam();
        roles = getRoles();
        roleNames1 = new ArrayList<String>();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getAccessrights().isEmpty()) {
                roleNames1.add(roles.get(i).getStaffRole());
            }
        }

        roles = getRoles();
        roleNames = new ArrayList<String>();
        for (int i = 0; i < roles.size(); i++) {
            roleNames.add(roles.get(i).getStaffRole());
        }

        staffs = retrieveStaffs();
        partners = retrievePartners();
        passengers = retrievePassengers();
        access = getAccess();
        accessRightsList = new ArrayList<String>();
        for (int i = 0; i < access.size(); i++) {
            accessRightsList.add(access.get(i).getAccessName());
        }
        
        stationList = new ArrayList<String>();
        depotList = new ArrayList<String>();
        hqList = new ArrayList<String>();
        List<RoleEntity> rolelist = systemAdminSessionBeanLocal.getRoles();  
        for(int i=0; i< rolelist.size(); i++){
            if(rolelist.get(i).getDepartment().equals("Station")) {
                stationList.add(rolelist.get(i).getStaffRole());
            } else if (rolelist.get(i).getDepartment().equals("Depot")){
                depotList.add(rolelist.get(i).getStaffRole());
            } else {
               hqList.add(rolelist.get(i).getStaffRole()); 
            }     
        }    
    }

    public void onLocationChange1() {
        if (department != null && !department.equals("")) {
            if (department.equals("Station")) {
                roleNames = stationList;
            } else if (department.equals("Depot")) {
                roleNames = depotList;
            } else {
                roleNames = hqList;
            }
        }
    }

    public List<String> getRoleNames1() {
        return roleNames1;
    }

    public void setRoleNames1(List<String> roleNames1) {
        this.roleNames1 = roleNames1;
    }

    public List<String> getHqList() {
        return hqList;
    }

    public void setHqList(List<String> hqList) {
        this.hqList = hqList;
    }

    public List<String> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<String> depotList) {
        this.depotList = depotList;
    }

    public List<String> getStationList() {
        return stationList;
    }

    public void setStationList(List<String> stationList) {
        this.stationList = stationList;
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

    public String getEducationQualification() {
        return educationQualification;
    }

    public void setEducationQualification(String educationQualification) {
        this.educationQualification = educationQualification;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<RoleEntity> getRoles() {
        roles = systemAdminSessionBeanLocal.getRoles();
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<String> getSelectedAccessRights() {
        return selectedAccessRights;
    }

    public void setSelectedAccessRights(List<String> selectedAccessRights) {
        this.selectedAccessRights = selectedAccessRights;
    }

    public List<AccessRightsEntity> getAccess() {
        access = systemAdminSessionBeanLocal.getAccess();
        return access;
    }

    public void setAccess(List<AccessRightsEntity> access) {
        this.access = access;
    }

    public List<String> getAccessRightsList() {
        return accessRightsList;
    }

    public void setAccessRightsList(List<String> accessRightsList) {
        this.accessRightsList = accessRightsList;
    }

    public List<RoleEntity> getCurrentAccessRights() {
        currentAccessRights = systemAdminSessionBeanLocal.getRolesAccess();
        return currentAccessRights;
    }

    public void setCurrentAccessRights(List<RoleEntity> currentAccessRights) {
        this.currentAccessRights = currentAccessRights;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public List<TeamEntity> getTeams() {

        teams = systemAdminSessionBeanLocal.getTeams(role);
        teamNumbers = new ArrayList<String>();

        for (int i = 0; i < teams.size(); i++) {
            teamNumbers.add(teams.get(i).getNode().getCode() + " - " + teams.get(i).getTeamId().toString());
        }
        return teams;
    }

    public void setTeams(List<TeamEntity> teams) {
        this.teams = teams;
    }

    public List<String> getTeamNumbers() {
        return teamNumbers;
    }

    public void setTeamNumbers(List<String> teamNumbers) {
        this.teamNumbers = teamNumbers;
    }

    public List<StaffEntity> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<StaffEntity> staffs) {
        this.staffs = staffs;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public List<String> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<String> nodeList) {
        this.nodeList = nodeList;
    }

    public ArrayList<String> getTeamList() {
        return teamList;
    }

    public void setTeamList(ArrayList<String> teamList) {
        this.teamList = teamList;
    }

    public ArrayList<StaffEntity> getStaffList() {
        return staffList;
    }

    public void setStaffList(ArrayList<StaffEntity> staffList) {
        this.staffList = staffList;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public ArrayList<StationStaffEntity> getsStaffWithoutTeamList() {
        return sStaffWithoutTeamList;
    }

    public void setsStaffWithoutTeamList(ArrayList<StationStaffEntity> sStaffWithoutTeamList) {
        this.sStaffWithoutTeamList = sStaffWithoutTeamList;
    }

    public ArrayList<DepotStaffEntity> getdStaffWithoutTeamList() {
        return dStaffWithoutTeamList;
    }

    public void setdStaffWithoutTeamList(ArrayList<DepotStaffEntity> dStaffWithoutTeamList) {
        this.dStaffWithoutTeamList = dStaffWithoutTeamList;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public List<String> getsList() {
        return sList;
    }

    public void setsList(List<String> sList) {
        this.sList = sList;
    }

    public List<String> getdList() {
        return dList;
    }

    public void setdList(List<String> dList) {
        this.dList = dList;
    }

    public String getsTeam() {
        return sTeam;
    }

    public void setsTeam(String sTeam) {
        this.sTeam = sTeam;
    }

    public String getdTeam() {
        return dTeam;
    }

    public void setdTeam(String dTeam) {
        this.dTeam = dTeam;
    }

    public List<String> getsTeamList() {
        return sTeamList;
    }

    public void setsTeamList(List<String> sTeamList) {
        this.sTeamList = sTeamList;
    }

    public List<String> getdTeamList() {
        return dTeamList;
    }

    public void setdTeamList(List<String> dTeamList) {
        this.dTeamList = dTeamList;
    }

    public List<PassengerEntity> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerEntity> passengers) {
        this.passengers = passengers;
    }

    public List<BusinessPartnerEntity> getPartners() {
        return partners;
    }

    public void setPartners(List<BusinessPartnerEntity> partners) {
        this.partners = partners;
    }

    public String createAccount() {
        String id = "";
        if (team == null) {
            team = "";
        }

        id = systemAdminSessionBeanLocal.createAccount(firstName, lastName, nric, phoneNumber, emailAddress, address, gender, dateOfBirth, maritalStatus, race, nationality, religion, educationQualification, department, role, salary, team);

        if (id.equals("")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Fail to create user!",
                            "Please enter a unique NRIC."));
            return "createStaff";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Staff " + id + " created!",
                            ""));
        }
        return "createStaff";
    }

    public String createTeam() {
        Long teamId;
        teamId = systemAdminSessionBeanLocal.createTeam(department);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Team " + teamId + " created", ""));

        return "createTeam?faces-redirect = true";
    }

    public String createRole() {
        String roleName;
        roleName = systemAdminSessionBeanLocal.createRole(role,department);
        if (roleName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Role " + roleName + " is already created", ""));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Role " + roleName + " created", ""));
        }
        return "createRole?faces-redirect = true";
    }

    public String createAccessRights() {
        boolean status = systemAdminSessionBeanLocal.createAccess(role, selectedAccessRights);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Configuration is created successfully",
                            ""));
            this.role = null;
            this.selectedAccessRights = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to create configuration!"));
        }
        return "accessRights?faces-redirect=true";
    }

    private List<StaffEntity> retrieveStaffs() {
        return systemAdminSessionBeanLocal.getStaffs();
    }

    public String deleteStaff(String staffId) {

        boolean success = systemAdminSessionBeanLocal.deleteStaff(staffId);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            staffId + " is deleted.", ""));
            staffs = retrieveStaffs();

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to delete " + staffId + "!", ""));
        }
        return "viewStaff";
    }

    public List<String> getAccess(String staffRole) {
        List<String> accessList = new ArrayList<String>();
        RoleEntity role = systemAdminSessionBeanLocal.searchRole(staffRole);
        ArrayList<AccessRightsEntity> access = new ArrayList<AccessRightsEntity>(role.getAccessrights());
        for (int i = 0; i < access.size(); i++) {
            accessList.add(access.get(i).getAccessName());
        }
        return accessList;

    }

    public String goEditAS(String staffRole) {
        this.role = staffRole;
        this.selectedAccessRights = getAccess(staffRole);
        return "editAccessRight";
    }

    public String editAccessRights() {
        boolean status = systemAdminSessionBeanLocal.updateAccess(role, selectedAccessRights);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Configuration is updated successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update Configuration!"));
            return "accessRights";
        }
        return "accessRights";
    }

    public void retrieveStaffList() {
        staffList = rosterSessionBeanLocal.retrieveStaffList(nodeCode, teamId);
    }

    public void retrieveTeamList() {
        teamList = rosterSessionBeanLocal.retrieveTeamList(nodeCode);
    }

    public void removeFromTeam(StaffEntity staff) {
        rosterSessionBeanLocal.removeFromTeam(staff);
        FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            staff.getStaffId() + " - " + staff.getFirstName() + " " +staff.getLastName()+" is successfully removed from Team!",""));
        retrieveStaffList();
        retrieveStaffWithoutTeam();
    }

    public void retrieveStaffWithoutTeam() {
        dStaffWithoutTeamList = rosterSessionBeanLocal.retrieveDStaffWithoutTeamList();
        sStaffWithoutTeamList = rosterSessionBeanLocal.retrieveSStaffWithoutTeamList();

    }

    public void retrieveSTeamList() {
        sTeamList = rosterSessionBeanLocal.retrieveTeamList(station);
    }

    public void retrieveDTeamList(StaffEntity staff) {
        dTeamList = rosterSessionBeanLocal.retrieveDTeamList(depot,staff);
    }

    public void onRowEdit(StaffEntity staff) {
        if (staff instanceof StationStaffEntity) {
            rosterSessionBeanLocal.addStaffToTeam(sTeam, staff);
        } else {
            rosterSessionBeanLocal.addStaffToTeam(dTeam, staff);
        }
        FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            staff.getStaffId() + " - " + staff.getFirstName() + " " +staff.getLastName()+" is successfully added to Team!",""));
        station = "";
        sTeam = "";
        depot = "";
        dTeam = "";
        retrieveStaffList();
        retrieveStaffWithoutTeam();

    }

    public void onRowCancel() {
        station = "";
        sTeam = "";
        depot = "";
        dTeam = "";
    }
    
    private List<PassengerEntity> retrievePassengers() {
        return systemAdminSessionBeanLocal.retrievePassengers();
    }

    public String deletePassenger(String username) {

        boolean success = systemAdminSessionBeanLocal.deletePassenger(username);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            username + " is deleted.", ""));
            passengers = retrievePassengers();

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to delete " + username + "!", ""));
        }
        return "viewPassenger";
    }
    
    private List<BusinessPartnerEntity> retrievePartners() {
        return systemAdminSessionBeanLocal.retrievePartners();
    }

    public String deletePartner(String partnerId) {

        boolean success = systemAdminSessionBeanLocal.deletePartner(partnerId);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            partnerId + " is deleted.", ""));
            partners = retrievePartners();

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to delete " + partnerId + "!", ""));
        }
        return "viewPartner";
    }
}
