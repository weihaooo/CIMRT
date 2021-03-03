/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

/**
 *
 * @author kayleytan
 */
import commoninfra.sessionbean.AccountSessionBeanLocal;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import operations.entity.InterviewEntity;
import operations.entity.JobApplicationsEntity;
import operations.sessionbean.OperationsSessionBeanLocal;
 
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
 
@ManagedBean
@ViewScoped
public class ScheduleView implements Serializable {
    
    
    @EJB
    private OperationsSessionBeanLocal OperationsSessionBeanLocal;
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
 
    private ScheduleModel eventModel;
    
    private ScheduleEvent event = new DefaultScheduleEvent();
    
    private List<JobApplicationsEntity> applicationList;
    
    private String applicantID;
    
    private List appList;
    
    private boolean status = true;
    
    private Date today = new Date();
    
    private String staffId;
   
    private String emailAddress;
    
     private ArrayList<String> staffDetails;
  
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date interviewFrom;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date interviewTo;

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }
 
    public List<JobApplicationsEntity> getApplicationList() {
        applicationList = OperationsSessionBeanLocal.getJobApp();
        return applicationList;
    }

    public void setApplicationList(List<JobApplicationsEntity> applicationList) {
        this.applicationList = applicationList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    
    public Date getInterviewFrom() {
        return interviewFrom;
    }

    public void setInterviewFrom(Date interviewFrom) {
        this.interviewFrom = interviewFrom;
    }

    public Date getInterviewTo() {
        return interviewTo;
    }

    public void setInterviewTo(Date interviewTo) {
        this.interviewTo = interviewTo;
    }

  
    
    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public List getAppList() {
        return appList;
    }

    public void setAppList(List appList) {
        this.appList = appList;
    }
    
    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ArrayList<String> getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(ArrayList<String> staffDetails) {
        this.staffDetails = staffDetails;
    }
    
    
  
    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            emailAddress = staffDetails.get(4);
        }
        
       eventModel = new DefaultScheduleModel();
       
       List<InterviewEntity> interviewList = OperationsSessionBeanLocal.getInterviewList();
       for(int j=0;j<interviewList.size();j++){
           if(interviewList.get(j).getJobApplication().getAppStatus().equals("Pending") || interviewList.get(j).getJobApplication().getAppStatus().equals("Scheduled"))
           
               eventModel.addEvent(new DefaultScheduleEvent(interviewList.get(j).getJobApplication().getApplicationId(), interviewList.get(j).getInterviewFrom(), interviewList.get(j).getInterviewTo())); 
       }
        
        appList = new ArrayList();
        List<JobApplicationsEntity> applicationList1 = getApplicationList();
        for(int i=0; i<applicationList1.size();i++){
            JobApplicationsEntity jobApp = applicationList1.get(i);
            //status: Pending/Received
            if(jobApp.getInterview()==null && (!(jobApp.getAppStatus().equals("Rejected"))) && (!(jobApp.getAppStatus().equals("Accepted")))  )
            appList.add(jobApp.getApplicationId());
        }
    }
         
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
     
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    public String addEvent() {
        if(event.getId() == null) {
        boolean sta = OperationsSessionBeanLocal.createInterview(applicantID,interviewFrom,interviewTo,emailAddress);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Interview is created successfully",
                            ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to create interview!", ""));
            return "applicantSchedule?faces-redirect=true";
        }
        return "applicantSchedule?faces-redirect=true";
        } else {
        boolean sta1 = OperationsSessionBeanLocal.updateInterview(applicantID,interviewFrom, interviewTo,emailAddress);
        if (sta1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Interview is updated successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the interview!", ""));
            return "applicantSchedule?faces-redirect=true";
        }
         event = new DefaultScheduleEvent();
         return "applicantSchedule?faces-redirect=true";
        }
    }
     
    public String deleteEvent() {
        boolean sta = OperationsSessionBeanLocal.deleteInterview(applicantID,emailAddress);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Interview is created successfully",
                            ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to create interview!", ""));
            return "applicantSchedule?faces-redirect=true";
        }
        return "applicantSchedule?faces-redirect=true";
        } 
    
    
      public String interviewDetails(String applicationId) {
        JobApplicationsEntity temp = OperationsSessionBeanLocal.searchJobApp(applicationId);
        if(temp.getInterview()==null){
            return "N.A";
        } else{
        String interviewId = temp.getInterview().getInterviewId();
        InterviewEntity inter = OperationsSessionBeanLocal.searchInterview(interviewId);
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm");
        String interviewStart = DATE_FORMAT.format(inter.getInterviewFrom());
        String interviewEnd = DATE_FORMAT.format(inter.getInterviewTo());
        String details = interviewStart + "\nto\n" + interviewEnd;
        return details;
        }
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        status = false;
        event = (ScheduleEvent) selectEvent.getObject();
        this.applicantID = event.getTitle();
        this.interviewFrom = event.getStartDate();
        this.interviewTo = event.getEndDate();     
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        status = true;
        Date now = new Date();
         if (now.compareTo((Date) selectEvent.getObject()) > 0) {
             this.interviewFrom = now;
         } else {
              this.interviewFrom = (Date) selectEvent.getObject();
         }
          if (now.compareTo((Date) selectEvent.getObject()) > 0) {
              this.interviewTo = now;
          } else {
               this.interviewTo = (Date) selectEvent.getObject();
          }
        event = new DefaultScheduleEvent("", interviewFrom, interviewTo);
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());   
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
