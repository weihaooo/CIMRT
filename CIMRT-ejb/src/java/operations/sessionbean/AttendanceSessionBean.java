/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import static commoninfra.sessionbean.AccountSessionBean.ACCOUNT_SID;
import static commoninfra.sessionbean.AccountSessionBean.AUTH_TOKEN;
import static commoninfra.sessionbean.AccountSessionBean.FROM_NUMBER;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.DepotTeamEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.StationTeamEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.StationEntity;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import static manpower.sessionbean.RosterSessionBean.getZeroTimeDate;
import operations.entity.AdHocRequestEntity;
import operations.entity.AttendanceEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Stateless
public class AttendanceSessionBean implements AttendanceSessionBeanLocal {

    @PersistenceContext
    EntityManager em;

    @Override
    public ArrayList<StaffEntity> getTodayStaff(String nodeCode, String teamId) {

        ArrayList<StaffEntity> staffs = new ArrayList<StaffEntity>();
        Date date = getZeroTimeDate(new Date());

        Query q = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:code");
        q.setParameter("code", nodeCode);
        List<NodeEntity> node = (List<NodeEntity>) q.getResultList();

        Query q1 = em.createQuery("SELECT t FROM TeamEntity AS t WHERE t.teamId=:teamId");
        q1.setParameter("teamId", Long.parseLong(teamId));
        List<TeamEntity> teams = (List<TeamEntity>) q1.getResultList();

        if (node.isEmpty() || teams.isEmpty()) {
            return null;
        }

        Query query = em.createQuery("SELECT a FROM AdHocRequestEntity AS a WHERE a.node=:node AND a.inTime > :date AND a.acknowledged= TRUE");
        query.setParameter("date", date);
        query.setParameter("node", node.get(0));

        if (teams.get(0) instanceof StationTeamEntity) {

            StationTeamEntity team = (StationTeamEntity) teams.get(0);
            List<StationStaffEntity> stationStaff = (List<StationStaffEntity>) team.getStaff();

            for (int x = 0; x < stationStaff.size(); x++) {
                staffs.add(stationStaff.get(x));
            }
            if (!query.getResultList().isEmpty()) {
                AdHocRequestEntity req = (AdHocRequestEntity) query.getResultList().get(0);
                staffs.add(req.getStandbyStaff());
            }
            return staffs;
        }

        Query q2 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date=:date AND r.team=:team");
        q2.setParameter("node", node.get(0));
        q2.setParameter("date", getZeroTimeDate(new Date()));
        q2.setParameter("team", teams.get(0));
        List<RosterEntity> roster = (List<RosterEntity>) q2.getResultList();

        if (roster.isEmpty()) {
            return null;
        }

        ShiftEntity shift = roster.get(0).getShift();
        String shiftNo = shift.getShiftId().substring(2);
        String tdShift = "TD" + shiftNo;
        String msShift = "MS" + shiftNo;

        Query q4 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId=:shift OR s.shiftId=:shift1");
        q4.setParameter("shift", tdShift);
        q4.setParameter("shift1", msShift);
        List<ShiftEntity> shifts = (List<ShiftEntity>) q4.getResultList();

        Query q3 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date=:date AND (r.shift=:shift OR r.shift=:shift1)");
        q3.setParameter("node", node.get(0));
        q3.setParameter("date", getZeroTimeDate(new Date()));
        q3.setParameter("shift", shifts.get(0));
        q3.setParameter("shift1", shifts.get(1));
        roster = (List<RosterEntity>) q3.getResultList();

        for (int i = 0; i < roster.size(); i++) {

            DepotTeamEntity team = (DepotTeamEntity) roster.get(i).getTeam();
            List<DepotStaffEntity> depotStaff = (List<DepotStaffEntity>) team.getStaff();
            for (int x = 0; x < depotStaff.size(); x++) {
                staffs.add(depotStaff.get(x));
            }

        }
        if (!query.getResultList().isEmpty()) {
            for (int i = 0; i < query.getResultList().size(); i++) {
                AdHocRequestEntity req = (AdHocRequestEntity) query.getResultList().get(i);
                staffs.add(req.getStandbyStaff());
            }
        }
        return staffs;

    }

    @Override
    public int createAttendance(StaffEntity staff, String attendanceType, String remark) {

        Date date = getZeroTimeDate(new Date());

        Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND a.workDate=:date");
        q.setParameter("staff", staff);
        q.setParameter("date", date);
        List<AttendanceEntity> att = (List<AttendanceEntity>) q.getResultList();

        if (att.isEmpty()) {
            AttendanceEntity attendance = new AttendanceEntity(attendanceType, remark, null, null, staff, date);
            em.persist(attendance);
            em.flush();
            return 2;
        } else {
            att.get(0).setRemarks(remark);
            att.get(0).setType(attendanceType);
            return 1;
        }

    }

    @Override
    public AttendanceEntity getAttendance(StaffEntity staff) {

        Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND a.workDate=:date ORDER BY a.attendanceId DESC");
        q.setParameter("staff", staff);
        q.setParameter("date", getZeroTimeDate(new Date()));
        List<AttendanceEntity> att = (List<AttendanceEntity>) q.getResultList();

        if (att.isEmpty()) {
            Query q1 = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND (a.type='On Time' OR a.type='Late' OR a.type='Activated') AND a.clockoutTime IS NULL ORDER BY a.attendanceId DESC");
            q1.setParameter("staff", staff);
            List<AttendanceEntity> attendance = (List<AttendanceEntity>) q1.getResultList();
            if (attendance.isEmpty()) {
                return null;
            } else {
                return attendance.get(0);
            }

        } else {
            return att.get(0);
        }
    }

    @Override
    public ArrayList<AttendanceEntity> getAttendances(ArrayList<StaffEntity> staffs) {
        ArrayList<AttendanceEntity> att = new ArrayList<AttendanceEntity>();
        if (staffs.isEmpty()) {
        } else {

            Date today = getZeroTimeDate(new Date());
            Date pastDate = changeDate(getZeroTimeDate(new Date()), -3);
            while (!pastDate.equals(today)) {
                for (int i = 0; i < staffs.size(); i++) {
                    Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND a.workDate=:today ORDER BY a.attendanceId DESC");
                    q.setParameter("staff", staffs.get(i));
                    q.setParameter("today", today);
                    List<AttendanceEntity> attendances = (List<AttendanceEntity>) q.getResultList();
                    for (int x = 0; x < attendances.size(); x++) {
                        att.add(attendances.get(x));
                    }
                }

                today = changeDate(today, -1);
            }
        }
        return att;
    }

    public Date changeDate(Date date, int change) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, change);
        return cal.getTime();
    }

    @Override
    public AttendanceEntity getPastAttendance(Long attendanceId) {
        Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.attendanceId=:attendanceId");
        q.setParameter("attendanceId", attendanceId);
        List<AttendanceEntity> att = (List<AttendanceEntity>) q.getResultList();

        if (att.isEmpty()) {
            return null;
        } else {
            return att.get(0);
        }
    }

    @Override
    public int editAttendance(Long attendanceId, String type, Date clockinTime, Date clockoutTime, String remarks) {
        Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.attendanceId=:attendanceId");
        q.setParameter("attendanceId", attendanceId);
        List<AttendanceEntity> att = (List<AttendanceEntity>) q.getResultList();

        if (att.isEmpty()) {

            return 0;
        } else {
            att.get(0).setType(type);
            if (clockinTime != null) {
                att.get(0).setClockinTime(new Timestamp(clockinTime.getTime()));
            } else {
                att.get(0).setClockinTime(null);
            }
            if (clockoutTime != null) {
                att.get(0).setClockoutTime(new Timestamp(clockoutTime.getTime()));
            } else {
                att.get(0).setClockoutTime(null);
            }
            att.get(0).setRemarks(remarks);
            em.flush();
            return 1;
        }
    }

    @Override
    public ArrayList<StaffEntity> getTodayStandbys(String nodeCode) {
        ArrayList<StaffEntity> staffs = new ArrayList<StaffEntity>();
        Date today = getZeroTimeDate(new Date());

        Query q = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:code");
        q.setParameter("code", nodeCode);
        List<NodeEntity> node = (List<NodeEntity>) q.getResultList();

        if (node.isEmpty()) {
            return null;
        }

        if (node.get(0) instanceof StationEntity) {
            Query q1 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId='SS4'");
            List<ShiftEntity> shift = (List<ShiftEntity>) q1.getResultList();
            Query q2 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date=:date AND r.shift=:shift");
            q2.setParameter("node", node.get(0));
            q2.setParameter("date", getZeroTimeDate(new Date()));
            q2.setParameter("shift", shift.get(0));
            List<RosterEntity> roster = (List<RosterEntity>) q2.getResultList();
            if (roster.isEmpty()) {
                return null;
            }

            for (int i = 0; i < roster.size(); i++) {
                StationTeamEntity stationTeam = (StationTeamEntity) roster.get(i).getTeam();
                for (int x = 0; x < stationTeam.getStaff().size(); x++) {
                    List<StaffEntity> s = new ArrayList(stationTeam.getStaff());
                    List<AdHocRequestEntity> requests = s.get(x).getReceivedAdHocReqs();
                    if (requests.isEmpty()) {
                        staffs.add(s.get(x));
                    } else {
                        boolean success = true;
                        for (int y = 0; y < requests.size(); y++) {
                            Date reqDate = getZeroTimeDate(requests.get(y).getInTime());

                            if (reqDate.equals(today)) {

                                success = false;
                                break;
                            }
                        }
                        if (success) {
                            staffs.add(s.get(x));
                        }
                    }
                }
            }
        } else {
            Query q1 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId='TD4' OR s.shiftId='MS4'");
            List<ShiftEntity> shift = (List<ShiftEntity>) q1.getResultList();
            Query q2 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date=:date AND (r.shift=:shift OR r.shift=:shift1)");
            q2.setParameter("node", node.get(0));
            q2.setParameter("date", getZeroTimeDate(new Date()));
            q2.setParameter("shift", shift.get(0));
            q2.setParameter("shift1", shift.get(1));

            List<RosterEntity> roster = (List<RosterEntity>) q2.getResultList();
            if (roster.isEmpty()) {
                return null;
            }

            for (int i = 0; i < roster.size(); i++) {
                DepotTeamEntity depotTeam = (DepotTeamEntity) roster.get(i).getTeam();
                for (int x = 0; x < depotTeam.getStaff().size(); x++) {
                    List<StaffEntity> s = new ArrayList(depotTeam.getStaff());
                    List<AdHocRequestEntity> requests = s.get(x).getReceivedAdHocReqs();
                    if (requests.isEmpty()) {
                        staffs.add(s.get(x));
                    } else {
                        boolean success = true;
                        for (int y = 0; y < requests.size(); y++) {
                            Date reqDate = getZeroTimeDate(requests.get(y).getInTime());

                            if (reqDate.equals(today)) {
                                success = false;
                                break;
                            }
                        }
                        if (success) {
                            staffs.add(s.get(x));
                        }
                    }
                }
            }
        }

        return staffs;
    }

    @Override
    public List<String> getNodes() {
        Query q = em.createQuery("SELECT n FROM NodeEntity AS n ORDER BY n.infraId");
        ArrayList<String> nodesList = new ArrayList<String>();
        List<NodeEntity> nodes = (List<NodeEntity>) q.getResultList();

        for (int i = 0; i < nodes.size(); i++) {
            nodesList.add(nodes.get(i).getCode() + " - " + nodes.get(i).getInfraName());

        }
        return nodesList;
    }

    @Override
    public boolean confirmStaffActivation(String title, String description,
            String nodeString, Date inTime,
            Date outTime, StaffEntity selectedStaff,
            String staffId
    ) {
        if (inTime != null && outTime != null) {
            if (inTime.before(outTime) && selectedStaff != null && !nodeString.equals("")) {
                String[] splitString = nodeString.split(" ");
                Query q = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:code");
                q.setParameter("code", splitString[0]);
                List<NodeEntity> node = (List<NodeEntity>) q.getResultList();

                if (node.isEmpty()) {
                    return false;
                }

                Query q1 = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
                q1.setParameter("staffId", staffId);
                List<StaffEntity> requester = (List<StaffEntity>) q1.getResultList();

                if (requester.isEmpty()) {
                    return false;
                }

                AdHocRequestEntity req = new AdHocRequestEntity(description, title, new Timestamp(inTime.getTime()), new Timestamp(outTime.getTime()), "Staff", requester.get(0), selectedStaff, node.get(0));

                selectedStaff.getReceivedAdHocReqs().add(req);
                requester.get(0).getSentAdHocReqs().add(req);
                em.persist(req);
                em.merge(selectedStaff);
                em.flush();
                String tempTitle = title.replace(" ", "%20");
                String tempVenue = node.get(0).getInfraName().replace(" ", "%20");
                SimpleDateFormat dt = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                String startTime = dt.format(inTime).replace(" ", "%20");
                String endTime = dt.format(outTime).replace(" ", "%20");
                String url = "http://is41031718es01.southeastasia.cloudapp.azure.com:8080/CIMRT-war/acknowledge.xhtml?" + "reqId=" + req.getReqId() + "&title=" + tempTitle + "&venue=" + tempVenue + "&startTime=" + startTime + "&endTime=" + endTime;
                String smsMessage = "Dear " + selectedStaff.getFirstName() + selectedStaff.getLastName() + " - " + selectedStaff.getStaffId() + ",";

                smsMessage += "\n\nYou have been activated by CIMRT.";
                smsMessage += "\n\nTitle: " + title;
                smsMessage += "\nDescription: " + description;
                smsMessage += "\nReporting Venue: " + node.get(0).getCode() + " - " + node.get(0).getInfraName();
                smsMessage += "\nRequested Time: " + dt.format(inTime);
                smsMessage += "\nExpected End Time: " + dt.format(outTime);
                smsMessage += "\nAcknowledge here: " + url;

                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                com.twilio.rest.api.v2010.account.Message tMessage;
                tMessage = com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber("+65" + selectedStaff.getPhoneNumber()), new PhoneNumber(FROM_NUMBER), smsMessage).create();

                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<StaffEntity> getTodayActivated(String nodeCode) {
        ArrayList<StaffEntity> staffs = new ArrayList<StaffEntity>();
        Date today = getZeroTimeDate(new Date());

        Query q = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code=:code");
        q.setParameter("code", nodeCode);
        List<NodeEntity> node = (List<NodeEntity>) q.getResultList();

        if (node.isEmpty()) {
            return null;
        }

        if (node.get(0) instanceof StationEntity) {
            Query q1 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId='SS4'");
            List<ShiftEntity> shift = (List<ShiftEntity>) q1.getResultList();
            Query q2 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date=:date AND r.shift=:shift");
            q2.setParameter("node", node.get(0));
            q2.setParameter("date", getZeroTimeDate(new Date()));
            q2.setParameter("shift", shift.get(0));
            List<RosterEntity> roster = (List<RosterEntity>) q2.getResultList();
            if (roster.isEmpty()) {
                return null;
            }

            for (int i = 0; i < roster.size(); i++) {
                StationTeamEntity stationTeam = (StationTeamEntity) roster.get(i).getTeam();
                for (int x = 0; x < stationTeam.getStaff().size(); x++) {
                    List<StaffEntity> s = new ArrayList(stationTeam.getStaff());
                    List<AdHocRequestEntity> requests = s.get(x).getReceivedAdHocReqs();
                    if (requests.isEmpty()) {
                    } else {
                        boolean success = false;
                        for (int y = 0; y < requests.size(); y++) {
                            Date reqDate = getZeroTimeDate(requests.get(y).getInTime());

                            if (reqDate.equals(today)) {
                                success = true;
                                break;
                            }
                        }
                        if (success) {
                            staffs.add(s.get(x));
                        }
                    }
                }
            }
        } else {
            Query q1 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId='TD4' OR s.shiftId='MS4'");
            List<ShiftEntity> shift = (List<ShiftEntity>) q1.getResultList();
            Query q2 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.node=:node AND r.date=:date AND (r.shift=:shift OR r.shift=:shift1)");
            q2.setParameter("node", node.get(0));
            q2.setParameter("date", getZeroTimeDate(new Date()));
            q2.setParameter("shift", shift.get(0));
            q2.setParameter("shift1", shift.get(1));

            List<RosterEntity> roster = (List<RosterEntity>) q2.getResultList();
            if (roster.isEmpty()) {
                return null;
            }

            for (int i = 0; i < roster.size(); i++) {
                DepotTeamEntity depotTeam = (DepotTeamEntity) roster.get(i).getTeam();
                for (int x = 0; x < depotTeam.getStaff().size(); x++) {
                    List<StaffEntity> s = new ArrayList(depotTeam.getStaff());
                    List<AdHocRequestEntity> requests = s.get(x).getReceivedAdHocReqs();
                    if (requests.isEmpty()) {
                    } else {
                        boolean success = false;
                        for (int y = 0; y < requests.size(); y++) {
                            Date reqDate = getZeroTimeDate(requests.get(y).getInTime());

                            if (reqDate.equals(today)) {
                                success = true;
                                break;
                            }
                        }
                        if (success) {
                            staffs.add(s.get(x));
                        }
                    }
                }
            }
        }

        return staffs;
    }

    @Override
    public ArrayList<AttendanceEntity> retrieveAttendancesByStaff(String staffId) {
        Query q1 = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        List<StaffEntity> staff = (List<StaffEntity>) q1.getResultList();
        if (staff.isEmpty()) {
        } else {
            Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff ORDER BY a.attendanceId DESC");
            q.setParameter("staff", staff.get(0));
            List<AttendanceEntity> attendances = (List<AttendanceEntity>) q.getResultList();

            return new ArrayList<AttendanceEntity>(attendances);
        }
        return null;
    }

    @Override
    public boolean clockIn(String staffId) {
        Date date = getZeroTimeDate(new Date());
        Timestamp currTime = new Timestamp((new Date()).getTime());
        String attendanceType = "On Time";

        Query q1 = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        List<StaffEntity> staffs = (List<StaffEntity>) q1.getResultList();

        Query q3 = em.createQuery("SELECT a FROM AdHocRequestEntity a WHERE a.standbyStaff=:staff AND a.inTime > :date AND a.acknowledged=TRUE");
        q3.setParameter("staff", staffs.get(0));
        q3.setParameter("date", date);

        if (!q3.getResultList().isEmpty()) {
            AttendanceEntity attendance = new AttendanceEntity("Activated", "Activated", currTime, null, staffs.get(0), date);
            em.persist(attendance);
            em.flush();
            return true;
        }

        Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND a.workDate=:date");
        q.setParameter("staff", staffs.get(0));
        q.setParameter("date", date);
        List<AttendanceEntity> att = (List<AttendanceEntity>) q.getResultList();
        Query q2 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.team=:team AND r.date=:date");
        if (staffs.get(0) instanceof StationStaffEntity) {
            StationStaffEntity staff = (StationStaffEntity) staffs.get(0);
            q2.setParameter("team", staff.getStationTeam());
        } else {
            DepotStaffEntity staff = (DepotStaffEntity) staffs.get(0);
            q2.setParameter("team", staff.getDepotTeam());
        }
        q2.setParameter("date", date);
        List<RosterEntity> roster = q2.getResultList();

        Timestamp lateTime = roster.get(0).getShift().getStartTime();

        if (compareTwoTimestamps(currTime, lateTime, 15) > 0) {
            attendanceType = "Late";
        }
        if (att.isEmpty()) {

            AttendanceEntity attendance = new AttendanceEntity(attendanceType, "", currTime, null, staffs.get(0), date);
            em.persist(attendance);
            em.flush();
            return true;

        }
        return false;

    }

    public long compareTwoTimestamps(java.sql.Timestamp currentTime, java.sql.Timestamp lateTime, int adjust) {
        Calendar oldCal = Calendar.getInstance();
        oldCal.setTime(lateTime);
        oldCal.add(Calendar.MINUTE, adjust);
        long min1 = oldCal.get(Calendar.HOUR_OF_DAY) * 60 + oldCal.get(Calendar.MINUTE);
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentTime);
        long min2 = currentCal.get(Calendar.HOUR_OF_DAY) * 60 + currentCal.get(Calendar.MINUTE);
        System.out.println(min2 + " " + currentTime +  " " + min1 + " " + lateTime);
        return min2 - min1;
    }

    @Override
    public boolean clockOut(String staffId) {
        Date date = getZeroTimeDate(new Date());
        Query q1 = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        List<StaffEntity> staffs = (List<StaffEntity>) q1.getResultList();
        Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND a.clockoutTime IS NULL ORDER BY a.attendanceId DESC");
        q.setParameter("staff", staffs.get(0));
        List<AttendanceEntity> att = (List<AttendanceEntity>) q.getResultList();

        Timestamp currTime = new Timestamp((new Date()).getTime());

        if (!att.isEmpty()) {
            att.get(0).setClockoutTime(currTime);
            em.flush();
            return true;

        }
        return false;
    }

    @Override
    public AttendanceEntity retrieveTodayAttendance(String staffId) {
        Date date = getZeroTimeDate(new Date());
        Query q1 = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        List<StaffEntity> staffs = (List<StaffEntity>) q1.getResultList();
        Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND a.workDate=:date");
        q.setParameter("staff", staffs.get(0));
        q.setParameter("date", date);
        List<AttendanceEntity> att = (List<AttendanceEntity>) q.getResultList();

        if (att.isEmpty()) {
            Query q2 = em.createQuery("SELECT a FROM AttendanceEntity AS a WHERE a.staff=:staff AND a.clockoutTime IS NULL ORDER BY a.attendanceId DESC");
            q2.setParameter("staff", staffs.get(0));
            List<AttendanceEntity> att1 = (List<AttendanceEntity>) q2.getResultList();
            if (att1.isEmpty()) {
                return null;
            } else {
                return att1.get(0);
            }
        }

        return att.get(0);
    }

    @Override
    public boolean isShiftNow(String staffId) {

        Date date = getZeroTimeDate(new Date());
        Query q1 = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        List<StaffEntity> staffs = (List<StaffEntity>) q1.getResultList();

        Query q = em.createQuery("SELECT a FROM AdHocRequestEntity a WHERE a.standbyStaff=:staff AND a.inTime > :date AND a.acknowledged=TRUE");
        q.setParameter("staff", staffs.get(0));
        q.setParameter("date", date);

        if (!q.getResultList().isEmpty()) {
            return true;
        }

        Query q2 = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.team=:team AND r.date=:date");
        if (staffs.get(0) instanceof StationStaffEntity) {
            StationStaffEntity staff = (StationStaffEntity) staffs.get(0);
            q2.setParameter("team", staff.getStationTeam());
        } else {
            DepotStaffEntity staff = (DepotStaffEntity) staffs.get(0);
            q2.setParameter("team", staff.getDepotTeam());
        }
        q2.setParameter("date", date);
        List<RosterEntity> roster = q2.getResultList();

        Timestamp currTime = new Timestamp((new Date()).getTime());
        if (roster.isEmpty() || roster.get(0).getShift().getStartTime() == null) {
            return false;
        }
        Timestamp startTime = roster.get(0).getShift().getStartTime();
        if (compareTwoTimestamps(currTime, startTime, -15) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean acknowledgeActivation(String reqId) {
        Query q = em.createQuery("SELECT a FROM AdHocRequestEntity a WHERE a.reqId=:reqId");
        q.setParameter("reqId", Long.parseLong(reqId));
        List<AdHocRequestEntity> req = (List<AdHocRequestEntity>) q.getResultList();

        if (req.isEmpty()) {
            return false;
        } else {
            AdHocRequestEntity request = req.get(0);
            request.setAcknowledged(true);
            em.flush();
            return true;
        }

    }

    @Override
    public ArrayList<AttendanceEntity> retrieveAllAttendances() {
        
            Query q = em.createQuery("SELECT a FROM AttendanceEntity AS a");
            return new ArrayList<AttendanceEntity>(q.getResultList());
    }
}
