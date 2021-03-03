/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import manpower.entity.RosterEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Local
public interface RosterSessionBeanLocal {
    public void generateSSRoster(Date endDate);
    public void generateMSRoster(Date endDate);
    public void generateTDRoster(Date endDate);
    public ArrayList<ArrayList<RosterEntity>> getRosterByNode(String nodeId,Date startDate,Date endDate);
    public List<RosterEntity> getRoster();
    public List<String> getNodes();
    public List<Date> updateDateRange(String nodeId);
    public Date retrieveMinDate();
    public Date retrieveMaxDate();
    public boolean editRoster(ArrayList<RosterEntity> rosters, String morningShift, String afternoonShift, String nightShift, String offDay);
    
    
    public ArrayList<StaffEntity> retrieveStaffList(String nodeCode, String teamId);

    public ArrayList<String> retrieveTeamList(String nodeCode);
    
    public ArrayList<String> retrieveDTeamList(String nodeCode, StaffEntity staff);

    public void removeFromTeam(StaffEntity staff);

    public ArrayList<StationStaffEntity> retrieveSStaffWithoutTeamList();

    public ArrayList<DepotStaffEntity> retrieveDStaffWithoutTeamList();

    public List<String> getStations();

    public List<String> getDepots();

    public void addStaffToTeam(String sTeam,StaffEntity staff);

}
