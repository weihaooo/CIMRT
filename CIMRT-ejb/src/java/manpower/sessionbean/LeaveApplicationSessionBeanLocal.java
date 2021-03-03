/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.entity.StaffEntity;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Local;
import javax.servlet.http.Part;
import operations.entity.LeaveApplicationEntity;

/**
 *
 * @author zhuming
 */
@Local
public interface LeaveApplicationSessionBeanLocal {
    public LeaveApplicationEntity createLeaveApplication(String type, String decription, Date startDate, Date endDate, String staffId);
    public ArrayList<LeaveApplicationEntity> viewLeaveApplications(String staffId);
    public void approveLeaveApplications(Long leaveApplicationId, String status, String type, Date start, Date end,String applicantId,String approver);
    public ArrayList<LeaveApplicationEntity> viewOutstandingApplications(String staffId, String role, String team, String nodeCode);
    public LeaveApplicationEntity searchLeaveApplication(Long appId);
    public int getLeaveBal(String type, String staffId);
    public int getOffDays(String staffId, Date start, Date end);
}
