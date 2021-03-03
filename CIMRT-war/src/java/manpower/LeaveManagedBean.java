/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import manpower.sessionbean.LeaveApplicationSessionBeanLocal;
import operations.entity.LeaveApplicationEntity;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author YuTing
 */
@Named(value = "leaveManagedBean")
@SessionScoped
public class LeaveManagedBean implements Serializable {

    @EJB
    private LeaveApplicationSessionBeanLocal leaveApplicationSessionBeanLocal;
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
    private String type;
    private Date start;
    private Date end;
    private String description;
    private Date today;
    private Date dateSelect;
    private String content;
    private String applicantId;
    private UploadedFile file;
    private String staffId;
    private ArrayList<LeaveApplicationEntity> applications;
    private List<LeaveApplicationEntity> filteredLeave;
    private ArrayList<LeaveApplicationEntity> pendingList;
    private String role;
    private String approver;
    private String firstName;
    private ArrayList<String> staffDetails;
    private Long applicationId;
    private String team;
    private String decision;

    private String supDocName;
    private String applicantTeam;
    private String applicantRole;
    private String nodeCode;

    @PostConstruct
    public void init() {
        today = new Date();
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        if (staffId != null) {
            staffDetails = getStaff(staffId);
            firstName = staffDetails.get(0);
            role = staffDetails.get(14);
            if (staffDetails.size() == 19) {
            team = staffDetails.get(17);
            nodeCode = staffDetails.get(18);
            }
        }
        applications = leaveApplicationSessionBeanLocal.viewLeaveApplications(staffId);
        ArrayList<LeaveApplicationEntity> tempList = null;
        if(role.equals("HR") || role.equals("Super Admin") || role.equals("Depot Manager") || role.equals("Station Manager")){
            tempList = leaveApplicationSessionBeanLocal.viewOutstandingApplications(staffId,role,team,nodeCode);
        }
        if(tempList != null){
            pendingList = tempList;
        }
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getDateSelect() {
        return dateSelect;
    }

    public void setDateSelect(Date dateSelect) {
        this.dateSelect = dateSelect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ArrayList<LeaveApplicationEntity> getApplications() {
        return applications;
    }

    public void setApplications(ArrayList<LeaveApplicationEntity> applications) {
        this.applications = applications;
    }

    public ArrayList<LeaveApplicationEntity> getPendingList() {
        return pendingList;
    }

    public void setPendingList(ArrayList<LeaveApplicationEntity> pendingList) {
        this.pendingList = pendingList;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getSupDocName() {
        return supDocName;
    }

    public void setSupDocName(String supDocName) {
        this.supDocName = supDocName;
    }

    public String getApplicantTeam() {
        return applicantTeam;
    }

    public void setApplicantTeam(String applicantTeam) {
        this.applicantTeam = applicantTeam;
    }

    public String getApplicantRole() {
        return applicantRole;
    }

    public void setApplicantRole(String applicantRole) {
        this.applicantRole = applicantRole;
    }

    public void handleFileUpload() throws FileNotFoundException, IOException {
        //FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        ArrayList<LeaveApplicationEntity> lists = leaveApplicationSessionBeanLocal.viewLeaveApplications(staffId);
        double leaveBal;
        leaveBal = leaveApplicationSessionBeanLocal.getLeaveBal(type, staffId);
        Long diff = end.getTime() - start.getTime();
        diff = diff / 1000 / 3600 / 24;
        Integer noOfDays = (int) (long) diff + 1;
        //find out whether the applicant is HQ or station/depot staff
        ArrayList<String> applicantDetails = getStaff(staffId);
        String applicantTeam = null;
        if (applicantDetails.size() == 19) {
            applicantTeam = applicantDetails.get(17);
        }
        if(applicantTeam==null){ //HQ
            int numOfWeekend = noOfWeekend(start,end);
            noOfDays = noOfDays - numOfWeekend;
            System.out.println("number of weekend = "+numOfWeekend);
        }
        else{ //station/depot staff
            int noOfOff = leaveApplicationSessionBeanLocal.getOffDays(staffId, start, end);
            noOfDays = noOfDays - noOfOff;
            System.out.println("number of off = "+noOfOff);
        }
        System.out.println("number of days = "+noOfDays);
        if (isOverlap(this.start, this.end, lists)) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "The leave period is overlapped with past leave applications!",
                            ""));
        } 
        else if(leaveBal<noOfDays){
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Your leave balance is not sufficient!",
                            ""));
        }
        else {
            String suffix = "";
            String filePath = "";
            String name = "";

            name = file.getFileName();
            //FacesContext.getCurrentInstance().addMessage(null, message);'
            if (!name.equals("")) {
                int index = name.indexOf(".");
                suffix = name.substring(index, name.length());
                filePath = System.getProperty("user.dir").substring(0, System.getProperty("user.dir").length() - 6) + "docroot" + System.getProperty("file.separator");
               
            }

            LeaveApplicationEntity leave = leaveApplicationSessionBeanLocal.createLeaveApplication(this.type, content, start, end, staffId);

            if (!name.equals("") && suffix.equals(".png")) {
                String fileName = staffId + "_" + leave.getLeaveApplicationId() + suffix;
                File newFile = new File(filePath + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int a;
                int BUFFER_SIZE = 8192;
                byte[] buffer = new byte[BUFFER_SIZE];

                InputStream inputStream = file.getInputstream();
                while (true) {
                    a = inputStream.read(buffer);
                    if (a < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, a);
                    fileOutputStream.flush();

                }
                inputStream.close();
                fileOutputStream.close();
                
                FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leave application has been submitted.",
                            ""));
            }
            else if(!name.equals("") && !suffix.equals(".png")){
                FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "The supporting format is .png only!",
                            ""));
            }
            else if(name.equals("")){
                FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leave application has been submitted.",
                            ""));
            }

            
        }
        applications = leaveApplicationSessionBeanLocal.viewLeaveApplications(staffId);
        //pendingList = leaveApplicationSessionBeanLocal.viewOutstandingApplications(staffId);
        this.content = null;
        this.type = null;
        this.start = null;
        this.end = null;
    }

    public int noOfWeekend(Date start, Date end) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(start);
        cal2.setTime(end);
        System.out.println("lalalal start - " + start);
        System.out.println("lalalal end - " + end);
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
    public boolean isOverlap(Date start, Date end, ArrayList<LeaveApplicationEntity> leaves) {
        Boolean isOverlap;
        isOverlap = false;
        for (int i = 0; i < leaves.size(); i++) {
            if (start.after(leaves.get(i).getStartDate()) && start.before(leaves.get(i).getEndDate())) {
                isOverlap = true;
                break;
            } else if (end.after(leaves.get(i).getStartDate()) && end.before(leaves.get(i).getEndDate())) {
                isOverlap = true;
                break;
            } else if (start.equals(leaves.get(i).getEndDate()) || start.equals(leaves.get(i).getStartDate()) || end.equals(leaves.get(i).getStartDate()) || end.equals(leaves.get(i).getEndDate())) {
                isOverlap = true;
                break;
            }
        }
        return isOverlap;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
        dateSelect = (Date) event.getObject();
    }

    public void onTypeChange() {
        if (this.type.equals("MC")) {
            this.today = null;
        } else {
            this.today = new Date();
        }
    }

    public String goEdit(Long appId) {
        this.applicationId = appId;
        LeaveApplicationEntity leaveApp = leaveApplicationSessionBeanLocal.searchLeaveApplication(appId);
        this.applicantId = leaveApp.getApplicant().getStaffId();
        this.firstName = leaveApp.getApplicant().getFirstName();
        staffDetails = getStaff(applicantId);
        this.type = leaveApp.getType();
        this.applicantRole = staffDetails.get(14);
        this.applicantTeam = staffDetails.get(17);
        this.start = leaveApp.getStartDate();
        this.end = leaveApp.getEndDate();
        this.description = leaveApp.getDescription();
        this.supDocName = applicantId + "_" + applicationId + ".png";
        return "leaveApplicationDetails";
    }

    public String update() {
        staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
        staffDetails = getStaff(staffId);
        role = staffDetails.get(14);
        team = staffDetails.get(17);
        nodeCode = staffDetails.get(18);
        leaveApplicationSessionBeanLocal.approveLeaveApplications(applicationId, decision, type, start, end, applicantId, staffId);
        pendingList = leaveApplicationSessionBeanLocal.viewOutstandingApplications(staffId,role,team,nodeCode);
        return "leaveApproval";
    }

    public List<LeaveApplicationEntity> getFilteredLeave() {
        return filteredLeave;
    }

    public void setFilteredLeave(List<LeaveApplicationEntity> filteredLeave) {
        this.filteredLeave = filteredLeave;
    }

}
