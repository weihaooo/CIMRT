package commoninfra.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import businessPartner.entity.BusinessPartnerEntity;
import commoninfra.entity.AccessRightsEntity;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.DepotTeamEntity;
import commoninfra.entity.HqStaffEntity;
import commoninfra.entity.RoleEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.StationTeamEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.StationEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import passenger.entity.PassengerEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author kayleytan
 */
@Stateful
public class SystemAdminSessionBean implements SystemAdminSessionBeanLocal, SystemAdminSessionBeanRemote {

    @PersistenceContext
    EntityManager em;
    
    StaffEntity staff;
    HqStaffEntity hqStaff;
    StationStaffEntity stationStaff;
    DepotStaffEntity depotStaff;
    RoleEntity staffRole;
    StationTeamEntity stationTeam;
    DepotTeamEntity depotTeam;

    @Override
    public String createAccount(String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String educationQualification, String department, String role, int salary, String team) {

        //Check if there is any staff with the same nric
        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.nric=:nric");
        q.setParameter("nric", nric);

        //Retrieve the selected role
        Query q1 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:role");
        q1.setParameter("role", role);
        ArrayList<RoleEntity> result = (ArrayList<RoleEntity>) q1.getResultList();

        //If there is no staff with the same nric
        if (q.getResultList().isEmpty()) {

            //Pass the selected role to staffRole
            staffRole = (RoleEntity) result.get(0);

            //Retrieve the list of hq staff (default)
            Query q2 = em.createQuery("SELECT hq FROM HqStaffEntity AS hq");

            //If department is Station, retrieve list of station staff 
            if (department.equals("Station")) {
                q2 = em.createQuery("SELECT s FROM StationStaffEntity AS s");
                //Else if department is Depot, retrieve list of depot staff 
            } else if (department.equals("Depot")) {
                q2 = em.createQuery("SELECT d FROM DepotStaffEntity AS d");
            }

            //Count the size of staff from the particular department
            int count = q2.getResultList().size();
            count = count + 1;
            String id;
            //Get the length of count
            int length = String.valueOf(count).length();

            //If team is not null
            try {
                if (!team.equals("")) {
                    //Retrieve the team base on team ID
                    Query q3 = em.createQuery("SELECT t FROM TeamEntity AS t WHERE t.teamId=:team");
                    q3.setParameter("team", Long.parseLong(team));

                    //If the list is not empty
                    if (!q3.getResultList().isEmpty()) {
                        ArrayList<TeamEntity> result3 = (ArrayList<TeamEntity>) q3.getResultList();

                        //If the team is a stationTeam
                        if (result3.get(0) instanceof StationTeamEntity) {
                            stationTeam = (StationTeamEntity) result3.get(0);
                            //If the team is a depotTeam
                        } else if (result3.get(0) instanceof DepotTeamEntity) {
                            depotTeam = (DepotTeamEntity) result3.get(0);
                        }
                    }
                }
            } catch (NumberFormatException nfe) {

            }

            try {
                //Hashing the password
                String password = "ES01" + "default1";
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hashSum = md.digest(password.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < hashSum.length; ++i) {
                    sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
                }

                //If department is HQ
                if (department.equals("HQ")) {
                    id = "HQ";
                    for (int i = 0; i < 6 - length; i++) {
                        id = id + "0";
                    }
                    id = id + String.valueOf(count);
                    hqStaff = new HqStaffEntity(id, firstName, lastName, nric, phoneNumber, emailAddress, sb.toString(), address, gender, dateOfBirth, maritalStatus, race, nationality, religion, educationQualification, salary, 14, 14, 0, false, staffRole);
                    em.persist(hqStaff);
                    return id;

                    //If department is Station
                } else if (department.equals("Station")) {
                    id = "S";
                    for (int i = 0; i < 6 - length; i++) {
                        id = id + "0";
                    }
                    id = id + String.valueOf(count);
                    stationStaff = new StationStaffEntity(id, firstName, lastName, nric, phoneNumber, emailAddress, sb.toString(), address, gender, dateOfBirth, maritalStatus, race, nationality, religion, educationQualification, salary, 14, 14, 0, false, staffRole, stationTeam);
                    em.persist(stationStaff);
                    return id;

                    //If department is Depot
                } else if (department.equals("Depot")) {
                    id = "D";
                    for (int i = 0; i < 6 - length; i++) {
                        id = id + "0";
                    }
                    id = id + String.valueOf(count);
                    depotStaff = new DepotStaffEntity(id, firstName, lastName, nric, phoneNumber, emailAddress, sb.toString(), address, gender, dateOfBirth, maritalStatus, race, nationality, religion, educationQualification, salary, 14, 14, 0, false, staffRole, depotTeam);
                    em.persist(depotStaff);
                    return id;
                }
            } catch (NoSuchAlgorithmException nsae) {
                System.out.println(nsae);
            }
        }
        return "";
    }

    @Override
    public Long createTeam(String department) {
        Long teamId = null;
        if (department.equals("Station")) {
            stationTeam = new StationTeamEntity(null, null);
            em.persist(stationTeam);
            em.flush();
            teamId = stationTeam.getTeamId();
            System.out.println("TeamID " + teamId);
        } else if (department.equals("Depot")) {
            depotTeam = new DepotTeamEntity(null, null);
            em.persist(depotTeam);
            em.flush();
            teamId = depotTeam.getTeamId();

            System.out.println("TeamID " + depotTeam.getTeamId());
        }

        return teamId;
    }

    @Override
    public List<TeamEntity> getTeams(String role) {
        List<TeamEntity> team = new ArrayList<TeamEntity>();
        if (role.equals("Station Staff") || role.equals("Station Manager")) {
            Query query = em.createQuery("SELECT st FROM StationTeamEntity st");
            team = query.getResultList();
        } else if (role.equals("Maintenance Staff") || role.equals("Depot Manager")) {

            Query query = em.createQuery("SELECT dt FROM DepotTeamEntity dt WHERE dt.role=:role");
            Query q1 = em.createQuery("SELECT r FROM RoleEntity r WHERE r.staffRole='Maintenance Staff'");
            query.setParameter("role", q1.getResultList().get(0));
            team = query.getResultList();
        } else if (role.equals("Train Driver")) {

            Query query = em.createQuery("SELECT dt FROM DepotTeamEntity dt WHERE dt.role=:role");
            Query q1 = em.createQuery("SELECT r FROM RoleEntity r WHERE r.staffRole='Train Driver'");
            query.setParameter("role", q1.getResultList().get(0));
            team = query.getResultList();
        }
        return team;
    }

    @Override
    public List<TeamEntity> getTeams() {
        Query query = em.createQuery("SELECT t FROM TeamEntity t");
        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            return query.getResultList();
        }
    }

    @Override
    public String createRole(String role, String department) {

        Query query = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:role ");
        query.setParameter("role", role);
        ArrayList<RoleEntity> result = (ArrayList<RoleEntity>) query.getResultList();
        String roleName = "";

        if (result.isEmpty()) {
            RoleEntity roleEntity = new RoleEntity(role, department);
            em.persist(roleEntity);
            em.flush();
            roleName = roleEntity.getStaffRole();
        }

        return roleName;
    }

    @Override
    public List<RoleEntity> getRoles() {

        Query query = em.createQuery("SELECT r FROM RoleEntity r");
        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            return query.getResultList();
        }
    }

    @Override
    public List<RoleEntity> getRolesAccess() {
        List<RoleEntity> roles = getRoles();
        List<RoleEntity> newroles = new ArrayList();
        for (int i = 0; i < roles.size(); i++) {
            if (!(roles.get(i).getAccessrights().isEmpty())) {
                newroles.add(roles.get(i));
            }
        }
        return newroles;
    }

    @Override
    public List<AccessRightsEntity> getAccess() {

        Query query = em.createQuery("SELECT a FROM AccessRightsEntity a");
        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            return query.getResultList();
        }
    }

    @Override
    public boolean createAccess(String role, List<String> selectedAccessRights) {
        try {
            RoleEntity roleE = searchRole(role);
            List<AccessRightsEntity> accessrights = new ArrayList<AccessRightsEntity>();
            if (em.contains(roleE)) {
                if (roleE.getAccessrights().isEmpty()) {
                    for (int i = 0; i < selectedAccessRights.size(); i++) {
                        AccessRightsEntity access = searchAccess(selectedAccessRights.get(i));
                        if (em.contains(access)) {
                            accessrights.add(access);
                            access.getRoles().add(roleE);
                        } else {
                            return false;
                        }
                    }
                    roleE.setAccessrights(accessrights);
                    em.persist(roleE);
                    return true;
                }
                return false;
            } else {
                return false;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public RoleEntity searchRole(String role) {
        RoleEntity r = new RoleEntity();
        try {
            Query q = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:role");
            q.setParameter("role", role);
            r = (RoleEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return r;
    }

    private AccessRightsEntity searchAccess(String accessName) {
        AccessRightsEntity a = new AccessRightsEntity();
        try {
            Query q = em.createQuery("SELECT a FROM AccessRightsEntity AS a WHERE a.accessName=:accessName");
            q.setParameter("accessName", accessName);
            a = (AccessRightsEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return a;
    }

    @Override
    public boolean updateAccess(String role, List<String> selectedAccessRights) {
        RoleEntity roleE = searchRole(role);
        List<AccessRightsEntity> accessrights = new ArrayList<AccessRightsEntity>();
        if (em.contains(roleE)) {
            roleE.setAccessrights(null);
            for (int i = 0; i < selectedAccessRights.size(); i++) {
                AccessRightsEntity access = searchAccess(selectedAccessRights.get(i));
                if (em.contains(access)) {
                    accessrights.add(access);
                    access.getRoles().add(roleE);
                } else {
                    return false;
                }
            }
            roleE.setAccessrights(accessrights);
            em.persist(roleE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<StaffEntity> getStaffs() {
        Query query = em.createQuery("SELECT s FROM StaffEntity s");
        return query.getResultList();
    }

    @Override
    public boolean deleteStaff(String staffId) {

        Query query = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        query.setParameter("staffId", staffId);
        if(query.getResultList().isEmpty()){
            return false;
        }
        ArrayList<StaffEntity> result = (ArrayList<StaffEntity>) query.getResultList();

        if (!result.isEmpty()) {
            em.remove((StaffEntity) result.get(0));
            em.flush();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public ArrayList<StaffEntity> retrieveStaffList(String nodeCode, String teamId) {

        if (nodeCode.equals("") || teamId.equals("")) {
            return null;
        } else {
            String[] splitString = nodeCode.split(" ");
            String[] splitString1 = teamId.split(" ");
            Query query = em.createQuery("SELECT n FROM NodeEntity n WHERE n.code=:nodeCode");
            query.setParameter("nodeCode", splitString[0]);

            if (!query.getResultList().isEmpty() && query.getResultList().get(0) instanceof StationEntity) {
                Query q2 = em.createQuery("SELECT s FROM StationTeamEntity AS s WHERE s.teamId=:teamId");
                q2.setParameter("teamId", Long.parseLong(splitString1[0]));

                Query q3 = em.createQuery("SELECT s FROM StationStaffEntity AS s WHERE s.stationTeam=:team");
                q3.setParameter("team", q2.getResultList().get(0));
                return new ArrayList<StaffEntity>(q3.getResultList());
            } else {
                Query q2 = em.createQuery("SELECT d FROM DepotTeamEntity AS d WHERE d.teamId=:teamId");
                q2.setParameter("teamId", Long.parseLong(splitString1[0]));

                Query q3 = em.createQuery("SELECT d FROM DepotStaffEntity AS d WHERE d.depotTeam=:team");
                q3.setParameter("team", q2.getResultList().get(0));
                return new ArrayList<StaffEntity>(q3.getResultList());
            }

        }
    }

    @Override
    public ArrayList<String> retrieveTeamList(String nodeCode) {
        ArrayList<String> teamList = new ArrayList<String>();

        if (nodeCode.equals("")) {
            return null;
        } else {
            String[] splitString = nodeCode.split(" ");
            Query query = em.createQuery("SELECT n FROM NodeEntity n WHERE n.code=:nodeCode");
            query.setParameter("nodeCode", splitString[0]);

            if (!query.getResultList().isEmpty() && query.getResultList().get(0) instanceof StationEntity) {
                Query q2 = em.createQuery("SELECT s FROM StationTeamEntity AS s WHERE s.node=:node");
                q2.setParameter("node", query.getResultList().get(0));
                List<StationTeamEntity> team = (List<StationTeamEntity>) q2.getResultList();
                for (int i = 0; i < team.size(); i++) {
                    teamList.add(team.get(i).getTeamId().toString());
                }

            } else {
                Query q2 = em.createQuery("SELECT d FROM DepotTeamEntity AS d WHERE d.node=:node");
                q2.setParameter("node", query.getResultList().get(0));
                List<DepotTeamEntity> team = (List<DepotTeamEntity>) q2.getResultList();
                for (int i = 0; i < team.size(); i++) {
                    teamList.add(team.get(i).getTeamId().toString() + " - " + team.get(i).getRole().getStaffRole());
                }
            }
        }
        return teamList;
    }

    @Override
    public List<String> getNodes() {
        Query q = em.createQuery("SELECT n FROM NodeEntity AS n ORDER BY n.code");
        ArrayList<String> nodesList = new ArrayList<String>();
        List<NodeEntity> nodes = (List<NodeEntity>) q.getResultList();
        if (nodes.isEmpty()) {
            return null;
        } else {

            for (int i = 0; i < nodes.size(); i++) {
                nodesList.add(nodes.get(i).getCode() + " - " + nodes.get(i).getInfraName());

            }
            return nodesList;
        }
    }

    @Override
    public void removeFromTeam(StaffEntity staff) {

        if (staff instanceof StationStaffEntity) {
            StationStaffEntity stationStaff = (StationStaffEntity) staff;
            StationTeamEntity team = stationStaff.getStationTeam();
            stationStaff.setStationTeam(null);
            team.getStaff().remove(stationStaff);
            em.merge(team);
            em.merge(stationStaff);
        } else {

            DepotStaffEntity depotStaff = (DepotStaffEntity) staff;
            DepotTeamEntity team = depotStaff.getDepotTeam();
            depotStaff.setDepotTeam(null);
            team.getStaff().remove(depotStaff);
            em.merge(team);
            em.merge(depotStaff);
        }

        em.flush();
    }

    @Override
    public ArrayList<StationStaffEntity> retrieveSStaffWithoutTeamList() {

        Query q = em.createQuery("SELECT s FROM StationStaffEntity AS s WHERE s.stationTeam IS NULL");
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return new ArrayList<StationStaffEntity>(q.getResultList());
        }
    }

    @Override
    public ArrayList<DepotStaffEntity> retrieveDStaffWithoutTeamList() {
        Query q = em.createQuery("SELECT d FROM DepotStaffEntity AS d WHERE d.depotTeam IS NULL");
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return new ArrayList<DepotStaffEntity>(q.getResultList());
        }
    }

    @Override
    public List<String> getStations() {
        Query q = em.createQuery("SELECT s FROM StationEntity AS s ORDER BY s.code");
        ArrayList<String> sList = new ArrayList<String>();
        List<StationEntity> s = (List<StationEntity>) q.getResultList();
        if (q.getResultList().isEmpty()) {
            return null;
        } else {

            for (int i = 0; i < s.size(); i++) {
                sList.add(s.get(i).getCode() + " - " + s.get(i).getInfraName());

            }
            return sList;
        }
    }

    @Override
    public List<String> getDepots() {
        Query q = em.createQuery("SELECT d FROM DepotEntity AS d ORDER BY d.code");
        ArrayList<String> dList = new ArrayList<String>();
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            List<DepotEntity> d = (List<DepotEntity>) q.getResultList();

            for (int i = 0; i < d.size(); i++) {
                dList.add(d.get(i).getCode() + " - " + d.get(i).getInfraName());

            }
            return dList;
        }
    }

    @Override
    public void addStaffToTeam(String sTeam, StaffEntity staff) {
        String[] splitString = sTeam.split(" ");
        if (staff instanceof StationStaffEntity) {

            Query q = em.createQuery("SELECT s FROM StationTeamEntity AS s WHERE s.teamId=:teamId");
            q.setParameter("teamId", Long.parseLong(splitString[0]));
            StationStaffEntity stationStaff = (StationStaffEntity) staff;
            StationTeamEntity stationTeam = (StationTeamEntity) q.getResultList().get(0);
            stationStaff.setStationTeam(stationTeam);
            stationTeam.getStaff().add(stationStaff);
            em.merge(stationTeam);
            em.merge(stationStaff);
        } else {
            Query q = em.createQuery("SELECT d FROM DepotTeamEntity AS d WHERE d.teamId=:teamId");
            q.setParameter("teamId", Long.parseLong(splitString[0]));
            DepotStaffEntity depotStaff = (DepotStaffEntity) staff;
            DepotTeamEntity depotTeam = (DepotTeamEntity) q.getResultList().get(0);
            depotStaff.setDepotTeam(depotTeam);
            depotTeam.getStaff().remove(depotStaff);
            em.merge(depotTeam);
            em.merge(depotStaff);
        }

        em.flush();
    }

    @Override
    public ArrayList<String> retrieveDTeamList(String nodeCode, StaffEntity staff) {

        ArrayList<String> teamList = new ArrayList<String>();

        if (nodeCode.equals("")) {
            return null;
        } else {
            String[] splitString = nodeCode.split(" ");
            Query query = em.createQuery("SELECT n FROM NodeEntity n WHERE n.code=:nodeCode");
            query.setParameter("nodeCode", splitString[0]);

            DepotStaffEntity depotStaff = (DepotStaffEntity) staff;
            RoleEntity role = depotStaff.getStaffRole();
            Query q2 = em.createQuery("SELECT d FROM DepotTeamEntity AS d WHERE d.node=:node AND d.role=:role");
            if (depotStaff.getStaffRole().getStaffRole().equals("Depot Manager")) {
                Query q1 = em.createQuery("SELECT r FROM RoleEntity r WHERE r.staffRole='Maintenance Staff'");
                role = (RoleEntity) q1.getResultList().get(0);
            }
            q2.setParameter("node", query.getResultList().get(0));
            q2.setParameter("role", role);
            List<DepotTeamEntity> team = (List<DepotTeamEntity>) q2.getResultList();
            for (int i = 0; i < team.size(); i++) {
                teamList.add(team.get(i).getTeamId().toString() + " - " + team.get(i).getRole().getStaffRole());
            }

        }
        return teamList;
    }

    @Override
    public List<PassengerEntity> retrievePassengers() {
        Query query = em.createQuery("SELECT p FROM PassengerEntity p");
        return query.getResultList();
    }

    @Override
    public boolean deletePassenger(String username) {
        
        Query query = em.createQuery("SELECT p FROM PassengerEntity p WHERE p.username=:username");
        query.setParameter("username", username);
        if(query.getResultList().isEmpty()){
            return false;
        }
        List<PassengerEntity> result = (List<PassengerEntity>) query.getResultList();

        if (!result.isEmpty()) {
            em.remove(result.get(0));
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<BusinessPartnerEntity> retrievePartners() {
        Query query = em.createQuery("SELECT b FROM BusinessPartnerEntity b");
        return query.getResultList();
    }

    @Override
    public boolean deletePartner(String partnerId) {
        Query query = em.createQuery("SELECT b FROM BusinessPartnerEntity b WHERE b.partnerId=:partnerId");
        query.setParameter("partnerId", partnerId);
        if(query.getResultList().isEmpty()){
            return false;
        }
        List<BusinessPartnerEntity> result = (List<BusinessPartnerEntity>) query.getResultList();

        if (!result.isEmpty()) {
            em.remove(result.get(0));
            em.flush();
            return true;
        } else {
            return false;
        }
    }

}
