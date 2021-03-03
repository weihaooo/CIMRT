/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.entity.HqStaffEntity;
import commoninfra.entity.StaffEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import manpower.entity.WorkshopEntity;

/**
 *
 * @author FABIAN
 */
@Local
public interface ManpowerSessionBeanLocal {
    public boolean createWorkshop(String workshopName, String workshopType, String description, Date startDate,Date endDate, String workshopStartTime, String workshopEndTime, String workshopAddress);
    public List <WorkshopEntity> getAllWorkshops();
    public List <WorkshopEntity> getSpecificWorkshop(Long workshopId, String workshopName);
    public boolean delWorkshop(Long workshopId);
    public boolean updateWorkshop(Long workshopId, String workshopName, String workshopType, String description, String workshopAddress, Date startDate,Date endDate, String workshopStartTime, String workshopEndTime);
    public WorkshopEntity searchWorkshop(Long workshopId);
    public List <StaffEntity> getStaffUnderSameTeam(String staffId);

    public List<WorkshopEntity> retrieveWorkshops(String staffId);
    public List <HqStaffEntity> getAllHRStaff();
    public String signupWorkshop(Long workshopId, String workshopName, List<String> selectedStaffs, String staffId);
    public String signupWorkshopHR(Long workshopId, String workshopName, List<String> selectedHRStaffs, String staffId);
    public ArrayList<ArrayList<String>> getExpireSafetyList();
    public ArrayList<ArrayList<String>> getOwnTeamExpireSafetyList(String staffId);
    public List<StaffEntity> getMembersWorkshopsEnrolled(String staffId);
    public List<StaffEntity> getHRMembersWorkshopsEnrolled(String staffId);
}
