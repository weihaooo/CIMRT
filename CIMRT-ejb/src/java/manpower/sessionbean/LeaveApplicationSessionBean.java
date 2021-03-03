/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.sessionbean.EmailManager;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.DepotTeamEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.StationTeamEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Part;
import manpower.entity.RosterEntity;
import manpower.entity.ShiftEntity;
import operations.entity.LeaveApplicationEntity;

/**
 *
 * @author zhuming
 */
@Stateless
public class LeaveApplicationSessionBean implements LeaveApplicationSessionBeanLocal, LeaveApplicationSessionBeanRemote {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    public LeaveApplicationSessionBean() {
    }

    @Override
    public LeaveApplicationEntity createLeaveApplication(String type, String description, Date startDate, Date endDate, String applicantId) {
        //System.out.println(type + "" + applicantId);
        StaffEntity s = searchStaff(applicantId);
        if(s == null){
            return null;
        }
        LeaveApplicationEntity leaveApplication = new LeaveApplicationEntity(type, description, "Pending", startDate, endDate);
        leaveApplication.setApplicant(searchStaff(applicantId));
        em.persist(leaveApplication);
        return leaveApplication;
    }

    public StaffEntity searchStaff(String staffId) {

        Query q = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        List<StaffEntity> result = q.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public ArrayList<LeaveApplicationEntity> viewLeaveApplications(String staffId) {

        ArrayList<LeaveApplicationEntity> lists = new ArrayList<LeaveApplicationEntity>();

        StaffEntity applicant = searchStaff(staffId);
        if(applicant == null){
            return null;
        }

        Query q = em.createQuery("SELECT la FROM LeaveApplicationEntity la WHERE la.applicant=:applicant");
        q.setParameter("applicant", applicant);
        for (Object o : q.getResultList()) {
            LeaveApplicationEntity la = (LeaveApplicationEntity) o;
            lists.add(la);
        }
        return lists;

    }

    @Override
    public ArrayList<LeaveApplicationEntity> viewOutstandingApplications(String staffId, String role, String team, String nodeCode) {

        ArrayList<LeaveApplicationEntity> lists = new ArrayList<LeaveApplicationEntity>();
        ArrayList<LeaveApplicationEntity> temp = new ArrayList<LeaveApplicationEntity>();
        StaffEntity applicant = searchStaff(staffId);
        Query q = em.createQuery("SELECT la FROM LeaveApplicationEntity la WHERE la.status='Pending' AND la.applicant!=:applicant");
        q.setParameter("applicant", applicant);
        for (Object o : q.getResultList()) {
            LeaveApplicationEntity la = (LeaveApplicationEntity) o;
            temp.add(la);
        }
        if (nodeCode == null) { //HQ staff
            for (int i = 0; i < temp.size(); i++) {
                LeaveApplicationEntity leave = temp.get(i);
                StaffEntity a = leave.getApplicant();
                String id = a.getStaffId();
                ArrayList<String> staffDetails = accountSessionBeanLocal.viewProfile(id);
                String node = "";
                if (staffDetails.size() == 19) {
                    node = staffDetails.get(18);
                }
                if (node == "") {
                    lists.add(leave);
                } else if (staffDetails.get(14).equals("Station Manager") || staffDetails.get(14).equals("Depot Manager")) {
                    lists.add(leave);
                }
            }
        } else {
            for (int i = 0; i < temp.size(); i++) {
                LeaveApplicationEntity leave = temp.get(i);
                StaffEntity a = leave.getApplicant();
                String id = a.getStaffId();
                ArrayList<String> staffDetails = accountSessionBeanLocal.viewProfile(id);
                String aTeam = null;
                String aNode = null;
                if (staffDetails.size() == 19) {
                    aTeam = staffDetails.get(17);
                    aNode = staffDetails.get(18);
                }
                String aRole = staffDetails.get(14);
                if (team.equals(aTeam) || (aRole.equals("Train Driver") && aNode.equals(nodeCode))) {
                    lists.add(leave);
                }
            }
        }
        return lists;
    }

    @Override
    public LeaveApplicationEntity searchLeaveApplication(Long appId) {
        LeaveApplicationEntity app = new LeaveApplicationEntity();
        Query q = em.createQuery("  SELECT a FROM LeaveApplicationEntity AS a WHERE a.leaveApplicationId=:leaveApplicationId");
        q.setParameter("leaveApplicationId", appId);
        if(q.getResultList().isEmpty()){
            return null;
        }
        List<LeaveApplicationEntity> result = q.getResultList();
        app = result.get(0);
        return app;
    }

    @Override
    public void approveLeaveApplications(Long leaveApplicationId, String status, String type, Date start, Date end, String applicantId, String approver) {
        
        LeaveApplicationEntity app = new LeaveApplicationEntity();
        Query q = em.createQuery("SELECT a FROM LeaveApplicationEntity AS a WHERE a.leaveApplicationId=:leaveApplicationId");
        q.setParameter("leaveApplicationId", leaveApplicationId);

        List<LeaveApplicationEntity> result = q.getResultList();
        app = result.get(0);
        //System.out.println("get application"+ app.getLeaveApplicationId());
        app.setStatus(status);
        //System.out.println("status "+ status);
        app.setApprover(searchStaff(approver));
        //System.out.println("approver "+ approver);
        em.flush();
        //System.out.println("enteredddddd");
        Long diff = end.getTime() - start.getTime();
        diff = diff / 1000 / 3600 / 24;
        Integer i = (int) (long) diff + 1;

        //find out whether the applicant is HQ or station/depot staff
        ArrayList<String> applicantDetails = accountSessionBeanLocal.viewProfile(applicantId);
        String applicantTeam = null;
        if (applicantDetails.size() == 19) {
            applicantTeam = applicantDetails.get(17);
        }
        if (applicantTeam == null) { //HQ
            int numOfWeekend = noOfWeekend(start, end);
            i = i - numOfWeekend;
        } else { //station/depot staff
            int noOfOff = getOffDays(applicantId, start, end);
            i = i - noOfOff;
        }

        //System.out.println("day diff " + i);
        //System.out.println("applicant "+ applicantId);
        //System.out.println("Mc bal"+searchStaff(applicantId).getMcEntitlement());
        //System.out.println("AL bal"+searchStaff(applicantId).getLeaveEntitlement());
        int leaveBal = 0;
        StaffEntity sender = searchStaff(approver);
        StaffEntity applicant = searchStaff(applicantId);

        if (status.equals("Approve")) {
            if (type.equals("MC")) {
                leaveBal = searchStaff(applicantId).getMcEntitlement() - i;
                searchStaff(applicantId).setMcEntitlement(leaveBal);
                //System.out.println("leave bal MC "+ leaveBal);
                em.flush();
            } else if (type.equals("AL")) {
                leaveBal = searchStaff(applicantId).getLeaveEntitlement() - i;
                searchStaff(applicantId).setLeaveEntitlement(leaveBal);
                //System.out.println("leave bal AL "+ leaveBal);
                em.flush();
            }

        }

        EmailManager emailManager = new EmailManager();
        emailManager.sendLeaveAppResult(applicant.getFirstName(), applicant.getLastName(), app.getType(), app.getStatus(), app.getLeaveApplicationId(), app.getDescription(), app.getStartDate(), app.getEndDate(), sender.getFirstName(), sender.getLastName(), sender.getEmailAddress(), applicant.getEmailAddress());
    }

    public int noOfWeekend(Date start, Date end) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(start);
        cal2.setTime(end);

        int numberOfDays = 0;
        while (cal1.before(cal2)) {
            if ((Calendar.SATURDAY == cal1.get(Calendar.DAY_OF_WEEK))
                    || (Calendar.SUNDAY == cal1.get(Calendar.DAY_OF_WEEK))) {
                numberOfDays++;
            }
            cal1.add(Calendar.DATE, 1);
        }
        return numberOfDays;
    }

    @Override
    public int getLeaveBal(String type, String staffId) {
        StaffEntity staff = searchStaff(staffId);
        if(staff == null){
            return 999;
        }
        int leaveBal = 0;
        if (type.equals("MC")) {
            leaveBal = staff.getMcEntitlement();
        } else if (type.equals("AL")) {
            leaveBal = staff.getLeaveEntitlement();
        }
        return leaveBal;
    }

    @Override
    public int getOffDays(String staffId, Date start, Date end) {
        Query q4 = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q4.setParameter("staffId", staffId);
        List<StaffEntity> result = q4.getResultList();
        if(result.isEmpty()){
            return 999;
        }
        StaffEntity staff;
        staff = result.get(0);
        start = getZeroTimeDate(start);
        end = getZeroTimeDate(end);
        Query q = em.createQuery("SELECT r FROM RosterEntity AS r WHERE r.team=:team AND r.date>=:start AND r.date<=:end AND r.shift=:shift");
        System.out.println("start" + start);
        System.out.println("end" + end);
        if (staff instanceof StationStaffEntity) {
            StationStaffEntity stationStaff = (StationStaffEntity) staff;
            StationTeamEntity stationTeam = stationStaff.getStationTeam();
            q.setParameter("team", stationTeam);
            Query q1 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId='SS4'");
            List<ShiftEntity> shift = q1.getResultList();
            q.setParameter("shift", shift.get(0));
            System.out.println("shift" + shift.get(0).getShiftId());
            System.out.println("team" + stationTeam);

        } else {
            DepotStaffEntity depotStaff = (DepotStaffEntity) staff;
            DepotTeamEntity depotTeam = depotStaff.getDepotTeam();
            q.setParameter("team", depotTeam);

            System.out.println("team" + depotTeam);
            if (depotStaff.getStaffRole().getStaffRole().equals("Train Driver")) {

                Query q1 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId='TD4'");
                List<ShiftEntity> shift = q1.getResultList();
                q.setParameter("shift", shift.get(0));
            } else {

                Query q1 = em.createQuery("SELECT s FROM ShiftEntity AS s WHERE s.shiftId='MS4'");
                List<ShiftEntity> shift = q1.getResultList();
                q.setParameter("shift", shift.get(0));
            }

        }
        q.setParameter("start", start);
        q.setParameter("end", end);

        List<RosterEntity> rosters = q.getResultList();

        return rosters.size();

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
}
