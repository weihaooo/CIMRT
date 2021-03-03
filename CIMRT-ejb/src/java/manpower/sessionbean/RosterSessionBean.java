/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.DepotTeamEntity;
import commoninfra.entity.RoleEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.StationTeamEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.StationEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import manpower.entity.RosterEntity;
import manpower.entity.ShiftEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Stateless
public class RosterSessionBean implements RosterSessionBeanLocal, RosterSessionBeanRemote {

    @PersistenceContext
    EntityManager em;

    @Override
    public void generateSSRoster(Date endDate) {

        Date startDate = getZeroTimeDate(retrieveMinDate(""));
        endDate = getZeroTimeDate(changeDate(endDate, 1));
        //Retrieve all Station
        Query q1 = em.createQuery("SELECT s FROM StationEntity AS s");
        List<StationEntity> stations = (List<StationEntity>) q1.getResultList();

        if (stations.isEmpty()) {
            System.out.println("Station list are empty");
        } else {
            //While startDate is not endDate
            while (startDate.before(endDate)) {
                for (int i = 0; i < stations.size(); i++) {

                    //Retrieve all Team from Station
                    Query q2 = em.createQuery("SELECT s FROM StationTeamEntity AS s WHERE s.node=:node");
                    q2.setParameter("node", stations.get(i));
                    List<StationTeamEntity> stationTeams = (List<StationTeamEntity>) q2.getResultList();

                    //Retrieve Station staff shifts
                    Query q3 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId LIKE :shiftId");
                    q3.setParameter("shiftId", "SS%");
                    List<ShiftEntity> shifts = (List<ShiftEntity>) q3.getResultList();

                    //Create Roster Entity for all station team and all dates
                    for (int x = 0; x < stationTeams.size(); x++) {

                        //If first time generating roster
                        if (stationTeams.get(x).getRosters().isEmpty()) {
                            createRoster(stationTeams.get(x), shifts.get(x), stations.get(i), startDate);
                        } else {
                            //Get the previous day roster for the station team
                            int todayShift = getTodayShift(startDate, stationTeams.get(x));

                            if (todayShift == -1) {
                                System.out.println("Previous day roster does not exist. " + startDate + " " + stationTeams.get(x));
                            } else {

                                createRoster(stationTeams.get(x), em.find(ShiftEntity.class, "SS" + todayShift), stations.get(i), startDate);
                            }
                        }
                    }
                }
                startDate = changeDate(startDate, 1);
            }
        }
    }

    public void createRoster(TeamEntity team, ShiftEntity shift, NodeEntity node, Date date) {

        Query q4 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.date =:date AND r.team=:team");
        q4.setParameter("date", getZeroTimeDate(date));
        q4.setParameter("team", team);
        List<RosterEntity> rosters = (List<RosterEntity>) q4.getResultList();

        if (rosters.isEmpty()) {
            RosterEntity roster = new RosterEntity(team, shift, node, getZeroTimeDate(date));
            em.persist(roster);
            team.addRoster(roster);
            node.addRoster(roster);
            em.flush();
            //  System.out.println("Roster created: " + roster.getDate() + " " + roster.getNode().getInfraId() + " " + roster.getShift().getShiftName() + " " + roster.getTeam().getTeamId());
        }
    }

    public Date changeDate(Date date, int change) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, change);
        return cal.getTime();
    }

    public int getTodayShift(Date todayDate, TeamEntity team) {
        Date ytdDate = getZeroTimeDate(changeDate(todayDate, -1));
        //Get the previous day roster for the station team
        Query q4 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.date =:date AND r.team=:team");
        q4.setParameter("date", ytdDate);
        q4.setParameter("team", team);
        List<RosterEntity> rosters = (List<RosterEntity>) q4.getResultList();

        if (rosters.isEmpty()) {
            return -1;
        } else {
            ShiftEntity shift = rosters.get(0).getShift();
            int shiftId = Integer.parseInt(shift.getShiftId().substring(2));

            if (shiftId == 4) {
                shiftId = 0;
            }
            return shiftId + 1;
        }
    }

    @Override
    public ArrayList<ArrayList<RosterEntity>> getRosterByNode(String code, Date startDate, Date endDate) {

        if (code == null) {
            return null;
        }

        String[] splitString = code.split(" ");
        Query q = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:code");
        q.setParameter("code", splitString[0]);
        List<NodeEntity> node = (List<NodeEntity>) q.getResultList();

        List<RosterEntity> rosters = new ArrayList<RosterEntity>();

        ArrayList<ArrayList<RosterEntity>> splitRosters = new ArrayList<ArrayList<RosterEntity>>();

        if (node.isEmpty()) {

        } else {
            Query q1;
            if (startDate == null) {
                startDate = new Date();
            }
            if (endDate != null) {
                endDate = getZeroTimeDate(endDate);
                q1 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date>=:startDate AND r.date<=:endDate ORDER BY r.date,r.shift");
                q1.setParameter("node", node.get(0));
                q1.setParameter("startDate", getZeroTimeDate(startDate));
                q1.setParameter("endDate", endDate);
            } else {
                q1 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date>=:startDate ORDER BY r.date,r.shift");
                q1.setParameter("node", node.get(0));
                q1.setParameter("startDate", getZeroTimeDate(startDate));
            }

            rosters = (List<RosterEntity>) q1.getResultList();

            int count = 0;

            for (int x = 0; x < rosters.size() / 4; x++) {

                ArrayList<RosterEntity> splitRoster = new ArrayList<RosterEntity>();
                for (int i = 0; i < 4; i++) {

                    splitRoster.add(rosters.get(i + count));
                }
                count += 4;
                splitRosters.add(splitRoster);
            }
        }

        return splitRosters;
    }

    public static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date = calendar.getTime();

        return date;
    }

    @Override
    public List<RosterEntity> getRoster() {
        Query q = em.createQuery("SELECT r FROM RosterEntity AS r");
        if(q.getResultList().isEmpty()){
            return null;
        }
        return (List<RosterEntity>) q.getResultList();
    }

    @Override
    public void generateMSRoster(Date endDate) {

        Date startDate = getZeroTimeDate(retrieveMinDate("Maintenance Staff"));
        endDate = getZeroTimeDate(changeDate(endDate, 1));
        //Retrieve all Depot
        Query q1 = em.createQuery("SELECT d FROM DepotEntity AS d");
        List<DepotEntity> depots = (List<DepotEntity>) q1.getResultList();

        if (depots.isEmpty()) {
            System.out.println("Depot list is empty");
        } else {
            //While startDate is not endDate
            while (startDate.before(endDate)) {

                for (int i = 0; i < depots.size(); i++) {
                    //Retrieve all Team from Station
                    Query q2 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
                    q2.setParameter("staffRole", "Maintenance Staff");

                    Query q3 = em.createQuery("SELECT d FROM DepotTeamEntity AS d WHERE d.node=:node AND d.role=:role");
                    q3.setParameter("node", depots.get(i));
                    q3.setParameter("role", q2.getResultList().get(0));
                    List<DepotTeamEntity> depotMSTeams = (List<DepotTeamEntity>) q3.getResultList();

                    //Retrieve Depot MS shifts
                    Query q4 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId LIKE :shiftId");
                    q4.setParameter("shiftId", "MS%");
                    List<ShiftEntity> shifts = (List<ShiftEntity>) q4.getResultList();

                    //Create Roster Entity for all depot team and all dates
                    for (int x = 0; x < depotMSTeams.size(); x++) {
                        //If first time generating roster
                        if (depotMSTeams.get(x).getRosters().isEmpty()) {
                            createRoster(depotMSTeams.get(x), shifts.get(x), depots.get(i), startDate);
                        } else {
                            //Get the previous day roster for the station team
                            int todayShift = getTodayShift(startDate, depotMSTeams.get(x));

                            if (todayShift == -1) {
                                System.out.println("Previous day roster does not exist. " + startDate + " " + depotMSTeams.get(x));
                            } else {

                                createRoster(depotMSTeams.get(x), em.find(ShiftEntity.class, "MS" + todayShift), depots.get(i), startDate);
                            }
                        }
                    }
                }
                startDate = changeDate(startDate, 1);
            }
        }
    }

    @Override
    public void generateTDRoster(Date endDate) {

        Date startDate = getZeroTimeDate(retrieveMinDate("Train Driver"));
        endDate = getZeroTimeDate(changeDate(endDate, 1));
        //Retrieve all Depot
        Query q1 = em.createQuery("SELECT d FROM DepotEntity AS d");
        List<DepotEntity> depots = (List<DepotEntity>) q1.getResultList();

        if (depots.isEmpty()) {
            System.out.println("Depot list is empty");
        } else {
            //While startDate is not endDate
            while (startDate.before(endDate)) {

                for (int i = 0; i < depots.size(); i++) {
                    //Retrieve all Team from Station
                    Query q2 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
                    q2.setParameter("staffRole", "Train Driver");

                    Query q3 = em.createQuery("SELECT d FROM DepotTeamEntity AS d WHERE d.node=:node AND d.role=:role");
                    q3.setParameter("node", depots.get(i));
                    q3.setParameter("role", q2.getResultList().get(0));
                    List<DepotTeamEntity> depotTDTeams = (List<DepotTeamEntity>) q3.getResultList();

                    //Retrieve Depot MS shifts
                    Query q4 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId LIKE :shiftId");
                    q4.setParameter("shiftId", "TD%");
                    List<ShiftEntity> shifts = (List<ShiftEntity>) q4.getResultList();

                    //Create Roster Entity for all depot team and all dates
                    for (int x = 0; x < depotTDTeams.size(); x++) {
                        //If first time generating roster
                        if (depotTDTeams.get(x).getRosters().isEmpty()) {
                            createRoster(depotTDTeams.get(x), shifts.get(x), depots.get(i), startDate);
                        } else {
                            //Get the previous day roster for the station team
                            int todayShift = getTodayShift(startDate, depotTDTeams.get(x));

                            if (todayShift == -1) {
                                System.out.println("Previous day roster does not exist. " + startDate + " " + depotTDTeams.get(x));
                            } else {

                                createRoster(depotTDTeams.get(x), em.find(ShiftEntity.class, "TD" + todayShift), depots.get(i), startDate);
                            }
                        }
                    }
                }
                startDate = changeDate(startDate, 1);
            }
        }
    }

    @Override
    public List<String> getNodes() {
        Query q = em.createQuery("SELECT n FROM NodeEntity AS n ORDER BY n.code");
        ArrayList<String> nodesList = new ArrayList<String>();
        List<NodeEntity> nodes = (List<NodeEntity>) q.getResultList();

        for (int i = 0; i < nodes.size(); i++) {
            nodesList.add(nodes.get(i).getCode() + " - " + nodes.get(i).getInfraName());

        }
        return nodesList;
    }

    @Override
    public List<Date> updateDateRange(String code) {
        String[] splitString = code.split(" ");

        Query q = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:code");
        q.setParameter("code", splitString[0]);
        List<NodeEntity> node = (List<NodeEntity>) q.getResultList();

        Query q1 = em.createQuery("SELECT DISTINCT r.date FROM RosterEntity AS r WHERE r.node=:node");
        q1.setParameter("node", node.get(0));
        return (List<Date>) q1.getResultList();
    }

    public Date retrieveMinDate(String role) {

        TeamEntity team;
        if (!role.equals("")) {
            Query q = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
            q.setParameter("staffRole", role);

            Query q1 = em.createQuery("SELECT d FROM DepotTeamEntity AS d WHERE d.role=:role");
            q1.setParameter("role", q.getResultList().get(0));
            List<DepotTeamEntity> depotTeams = (List<DepotTeamEntity>) q1.getResultList();
            team = depotTeams.get(0);
        } else {
            Query q2 = em.createQuery("SELECT s FROM StationTeamEntity AS s");
            List<StationTeamEntity> stationTeams = (List<StationTeamEntity>) q2.getResultList();
            team = stationTeams.get(0);
        }

        Query q3 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.team=:team ORDER BY r.date DESC");
        q3.setParameter("team", team);
        List<RosterEntity> rosters = (List<RosterEntity>) q3.getResultList();

        if (!rosters.isEmpty()) {
            return getZeroTimeDate(changeDate(rosters.get(0).getDate(), 1));
        }
        return getZeroTimeDate(new Date());
    }

    @Override
    public Date retrieveMinDate() {
        Query q = em.createQuery("SELECT r FROM RosterEntity AS r ORDER BY r.date DESC");
        List<RosterEntity> rosters = (List<RosterEntity>) q.getResultList();
        if (!rosters.isEmpty()) {
            return getZeroTimeDate(changeDate(rosters.get(0).getDate(), 1));
        }
        return getZeroTimeDate(new Date());
    }

    @Override
    public Date retrieveMaxDate() {
        Query q = em.createQuery("SELECT r FROM RosterEntity AS r ORDER BY r.date DESC");
        List<RosterEntity> rosters = (List<RosterEntity>) q.getResultList();
        if (!rosters.isEmpty()) {
            return getZeroTimeDate(rosters.get(0).getDate());
        }
        return getZeroTimeDate(new Date());
    }

    @Override
    public boolean editRoster(ArrayList<RosterEntity> rosters, String morningShift, String afternoonShift, String nightShift, String offDay) {

        Date date = rosters.get(0).getDate();
        date = changeDate(date, 1);

        if (getShiftId(date, nightShift) != null && getShiftId(date, nightShift).substring(2).equals("1")) {
            return false;
        }
        date = changeDate(date, -2);
        if (getShiftId(date, nightShift) != null && getShiftId(date, morningShift).substring(2).equals("3")) {
            return false;
        }

        if (!morningShift.equals(afternoonShift)
                && !afternoonShift.equals(nightShift) && !nightShift.equals(offDay) && !morningShift.equals(nightShift) && !morningShift.equals(offDay) && !afternoonShift.equals(offDay)) {
            Query q = em.createQuery("SELECT t FROM TeamEntity AS t WHERE t.teamId=:teamId");
            q.setParameter("teamId", Long.parseLong(morningShift));
            List<TeamEntity> team = (List<TeamEntity>) q.getResultList();

            if (!team.isEmpty()) {
                rosters.get(0).setTeam(team.get(0));
            }
            q.setParameter("teamId", Long.parseLong(afternoonShift));
            team = (List<TeamEntity>) q.getResultList();

            if (!team.isEmpty()) {
                rosters.get(1).setTeam(team.get(0));
            }
            q.setParameter("teamId", Long.parseLong(nightShift));
            team = (List<TeamEntity>) q.getResultList();

            if (!team.isEmpty()) {
                rosters.get(2).setTeam(team.get(0));
            }
            q.setParameter("teamId", Long.parseLong(offDay));
            team = (List<TeamEntity>) q.getResultList();

            if (!team.isEmpty()) {
                rosters.get(3).setTeam(team.get(0));
            }
            return true;
        }

        return false;
    }

    private String getShiftId(Date date, String teamId) {
        Query q = em.createQuery("SELECT t FROM TeamEntity AS t WHERE t.teamId=:teamId");
        q.setParameter("teamId", Long.parseLong(teamId));
        List<TeamEntity> team = (List<TeamEntity>) q.getResultList();
        Query q1 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.team=:team AND r.date=:date");
        q1.setParameter("team", team.get(0));
        q1.setParameter("date", date);
        List<RosterEntity> roster = (List<RosterEntity>) q1.getResultList();
        if (roster.isEmpty()) {
            return null;
        }

        return roster.get(0).getShift().getShiftId();
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
        if(q.getResultList().isEmpty()){
            return null;
        }
        return new ArrayList<StationStaffEntity>(q.getResultList());
    }

    @Override
    public ArrayList<DepotStaffEntity> retrieveDStaffWithoutTeamList() {
        Query q = em.createQuery("SELECT d FROM DepotStaffEntity AS d WHERE d.depotTeam IS NULL");
        if(q.getResultList().isEmpty()){
            return null;
        }
        return new ArrayList<DepotStaffEntity>(q.getResultList());
    }

    @Override
    public List<String> getStations() {
        Query q = em.createQuery("SELECT s FROM StationEntity AS s ORDER BY s.code");
        ArrayList<String> sList = new ArrayList<String>();
        List<StationEntity> s = (List<StationEntity>) q.getResultList();

        for (int i = 0; i < s.size(); i++) {
            sList.add(s.get(i).getCode() + " - " + s.get(i).getInfraName());

        }
        return sList;
    }

    @Override
    public List<String> getDepots() {
        Query q = em.createQuery("SELECT d FROM DepotEntity AS d ORDER BY d.code");
        ArrayList<String> dList = new ArrayList<String>();
        List<DepotEntity> d = (List<DepotEntity>) q.getResultList();

        for (int i = 0; i < d.size(); i++) {
            dList.add(d.get(i).getCode() + " - " + d.get(i).getInfraName());

        }
        return dList;
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
}
