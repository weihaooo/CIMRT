/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import commoninfra.entity.StaffEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import operations.entity.AttendanceEntity;

/**
 *
 * @author Yoona
 */
@Local
public interface AttendanceSessionBeanLocal {
    public ArrayList<StaffEntity> getTodayStaff(String nodeCode, String teamId);

    public int createAttendance(StaffEntity staff, String attendanceType, String remark);

    public AttendanceEntity getAttendance(StaffEntity staff);

    public ArrayList<AttendanceEntity> getAttendances(ArrayList<StaffEntity> staffs);


    public AttendanceEntity getPastAttendance(Long attendanceId);

    public int editAttendance(Long attendanceId, String type, Date clockinTime, Date clockoutTime, String remarks);

    public ArrayList<StaffEntity> getTodayStandbys(String nodeCode);
    
    public List<String> getNodes();

    public boolean confirmStaffActivation(String title, String description, String nodeString, Date inTime, Date outTime, StaffEntity selectedStaff, String staffId);
    public ArrayList<StaffEntity> getTodayActivated(String nodeCode);

    public ArrayList<AttendanceEntity> retrieveAttendancesByStaff(String staffId);

    public boolean clockIn(String staffId);

    public boolean clockOut(String staffId);

    public AttendanceEntity retrieveTodayAttendance(String staffId);

    public boolean isShiftNow(String staffId);

    public boolean acknowledgeActivation(String reqId);

    public ArrayList<AttendanceEntity> retrieveAllAttendances();

}
