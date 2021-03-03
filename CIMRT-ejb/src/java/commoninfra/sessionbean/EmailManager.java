/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.sessionbean;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class EmailManager 
{
    private final String emailServerName = "mailauth.comp.nus.edu.sg";     
    private final String mailer = "JavaMailer";
    private String smtpAuthUser = "e0002252@u.nus.edu";
    private String smtpAuthPassword = "YoonAx3WH";
    
    
    
    public EmailManager()
    {
    }

    
    
    public EmailManager(String smtpAuthUser, String smtpAuthPassword)
    {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }
    
    
    
    public Boolean emailStaffId(String staffId, int type, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        
        emailBody += "Your Staff ID is " + staffId;
        
        return emailProcess(emailBody,type,fromEmailAddress,toEmailAddress);
    }
    
    public Boolean emailPartnerId(String partnerId, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        
        emailBody += "Your Partner ID is " + partnerId;
        
        return emailProcess(emailBody,9,fromEmailAddress,toEmailAddress);
    }
    
    public Boolean emailUsername(String username, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        
        emailBody += "Your Username is " + username;
        
        return emailProcess(emailBody,11,fromEmailAddress,toEmailAddress);
    }
    
    public Boolean sendWorkshopEmail(String staffName, Long workshopId, String workshopName,String description, Date startDate, Date endDate,
            String workshopStartTime, String workshopEndTime, String workshopAddress, int type, String fromEmailAddress, String toEmailAddress)
    {
        
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE dd MMMMM yyyy");
        /*String date = DATE_FORMAT.format(workshopDate);*/
        
        String emailBody = "";
        emailBody += "Dear " + staffName + ",";
        
        emailBody += "\n\nYou have been sign up for the following: ";
        emailBody += "\nWorkshop Id: " + workshopId;
        emailBody += "\nWorkshop Name: " + workshopName;
        emailBody += "\nDescription: " + description;
        emailBody += "\nWorkshop Start Date: " + DATE_FORMAT.format(startDate);
        emailBody += "\nWorkshop End Date: " + DATE_FORMAT.format(endDate);
        emailBody += "\nWorkshop Starting Time: " + workshopStartTime;
        emailBody += "\nWorkshop Ending Time: " + workshopEndTime;
        emailBody += "\nWorkshop Address: " + workshopAddress;
        

        return emailProcess(emailBody,type,fromEmailAddress,toEmailAddress);
    }
    
    public Boolean sendWorkshopCancel(String staffName, Long workshopId, String workshopName, 
            int type, String fromEmailAddress, String toEmailAddress)
    {
        
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE dd MMMMM yyyy");
        /*String date = DATE_FORMAT.format(workshopDate);*/
        
        String emailBody = "";
        emailBody += "Dear " + staffName + ",";
        
        emailBody += "\n\nPlease be informed that the following workshop has been cancelled: ";
        emailBody += "\nWorkshop Id: " + workshopId;
        emailBody += "\nWorkshop Name: " + workshopName;
        emailBody += "\n\nYou are not no longer required to attend the above mentioned workshop. For any further clarifications" +
                ", please look for for respective managers or superior.";
        emailBody += "\n\nThank you and have a nice day!";
        emailBody += "\n\nWarmest Regards,";
        emailBody += "\nCIMRT HQ";
        

        return emailProcess(emailBody,type,fromEmailAddress,toEmailAddress);
    }
    
    public Boolean sendWithdrawEmail(String staffName, Long workshopId, String workshopName, int type, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        emailBody += "Dear " + staffName + ",";
        
        emailBody += "\n\nYou have been WITHDRAWN from the following: ";
        emailBody += "\nWorkshop Id: " + workshopId;
        emailBody += "\nWorkshop Name: " + workshopName;
  
        

        return emailProcess(emailBody,type,fromEmailAddress,toEmailAddress);
    }
    
    public Boolean sendInterviewEmail(Timestamp interviewTo, Timestamp interviewFrom, String emailAddress, String name, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nCongratulations, we are pleased to inform you that CIMRT has shortlisted you for an interview. ";
        emailBody += "\nThe interview details are as follows: ";
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewFrom) + " - " + sdf1.format(interviewTo);
        emailBody += "\nVenue: " + "131 Rifle Range Road CIMRT Building #02-345";
        emailBody += "\n\nThank you, have a great day ahead! ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";
       
        return emailProcess(emailBody,4,staffEmail,emailAddress);
    }
    
     public Boolean sendUpdateInterviewEmail(Timestamp interviewTo, Timestamp interviewFrom, String emailAddress, String name, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe interview details are changed as follows: ";
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewFrom) + " - " + sdf1.format(interviewTo);
        emailBody += "\nVenue: " + "131 Rifle Range Road CIMRT Building #02-345";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";
       
       
        return emailProcess(emailBody,5,staffEmail,emailAddress);
    }
     
     public Boolean sendLeaveAppResult(String rFirstName, String rLastName, String leaveType, String status, Long appId, String description, Date start, Date end, String sFirstName, String sLastName, String sEmail, String rEmail
     )
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String emailBody = "";
        emailBody += "Dear " + rFirstName + " " +rLastName+ ",";
        
        emailBody += "\n\nYour request for " + leaveType+" has been "+ status + ".\n" + "The details of your application are as below: ";
        emailBody += "\nApplication ID: " + appId;
        emailBody += "\nLeave Type: " + leaveType;
        emailBody += "\nDescription: " + description;
        emailBody += "\nTime: " + sdf.format(start) + " - " + sdf.format(end);
        emailBody += "\nStatus: " + status;
        emailBody += "\nApprover: " + sFirstName + " "+sLastName;
        emailBody += "\nThis email is auto-generated. Please do not reply.";
        emailBody += "\nFor any further queries, please contact +65 96743264 or email hr_support@cimrt.com.";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";
       
       
        return emailProcess(emailBody,8,"e0002252@u.nus.edu",rEmail);
    }
     
      public Boolean sendDeleteInterviewEmail(String emailAddress, String name, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe interview has been cancelled. ";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";
       
        return emailProcess(emailBody,6,staffEmail,emailAddress);
    }
      
      
       public Boolean sendRejectionEmail(String name,String emailAddress, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThank you very much for taking the time to interview with us. We appreciate your interest in the company and the job.\n"
                + "\n"
                + "I am writing to let you know that we have selected the candidate whom we believe most closely matches the job requirements of the position.\n"
                + "\n"
                + "We do appreciate you taking the time to interview with us and encourage you to apply for other openings at the company in the future.\n"
                + "\n"
                + "Again, thank you for your time.";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";

        return emailProcess(emailBody, 17, staffEmail,emailAddress);
    }
       
        public Boolean sendAcceptanceEmail(String name, String emailAddress, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nWe are pleased to inform you that you are shortlisted! Your skills and experience will be an ideal fit for our company."
                + "\n\n If you choose to reject this job offer, please drop me an email. Else, if you choose to accept this job offer, please sign the second copy of this letter and return it to me at your earliest convenience. When your acknowledgment is received, "
                + "our HR staff will contact you shortly for signing of staff contract!\n"
                + "We look forward to welcoming you to our CIMRT big family!\n"
                + "\n"
                + "";
        emailBody += "Thank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";
        emailBody += "\n"
                + "\n"
                + "I hereby accept the position offered to me.\n"
                + "\n"
                + "\n"
                + "_____________________________\n"
                + "Signature\n"
                + "\n"
                + "_____________________________\n"
                + "Date"; 
        return emailProcess(emailBody,18,staffEmail,emailAddress);
    }
      
      //Successful bid
      public Boolean sendBidEmail(String itemName,String companyName, String emailAddress, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear " + companyName + ",";
        
        emailBody += "\n\nCongratulations, you have won the bid for " + itemName + ".";
        emailBody += "\n\nPlease schedule the delivery of the items asap.";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,13,staffEmail,emailAddress);
    }
    
      //Unsuccessful Bid
    public Boolean sendBidEmail1(String itemName,String companyName, String emailAddress, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear " + companyName + ",";
        
        emailBody += "\n\nPlease be informed that your bid for " + itemName + " is unsuccessful.";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,14,staffEmail,emailAddress);
    }
    
    
      //Delivery details
    public Boolean sendDeliveryEmail(String purchaseRequest,String itemName,Timestamp deliverydetails,String companyName, String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear I&A team,";
        
        emailBody += "\n\nPlease be informed that the delivery date for " + itemName + " has been scheduled.";
        emailBody += "\n\nThe delivery details are as follows: ";
        emailBody += "\nCompany: " + companyName;
        emailBody += "\nPurchase Request: " + purchaseRequest;
        emailBody += "\nDate: " + sdf.format(deliverydetails);
        emailBody += "\nTime: " + sdf1.format(deliverydetails);

       
        return emailProcess(emailBody,15,emailAddress,staffEmail);
    }
    
    //Change of Delivery details
    public Boolean sendDeliveryEmail1(String purchaseRequest,String itemName,Timestamp deliverydetails,String companyName, String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear I&A team,";
        
        emailBody += "\n\nPlease be informed that the delivery date for " + itemName + " has been changed.";
        emailBody += "\n\nThe delivery details are changed as follows: ";
        emailBody += "\nCompany: " + companyName;
        emailBody += "\nPurchase Request: " + purchaseRequest;
        emailBody += "\nDate: " + sdf.format(deliverydetails);
        emailBody += "\nTime: " + sdf1.format(deliverydetails);
       
        return emailProcess(emailBody,16,emailAddress,staffEmail);
    }
      
      
     public Boolean sendSubscriptionEmail(String name, String itemname,double amount ,int qty,Timestamp deadline,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nWe have a new lisiting. Please log in to check it out!";
        emailBody += "\nThe listing details are as follows: ";
        emailBody += "\nItem Name: " + itemname;
         emailBody += "\nQuantity: " + qty;
        emailBody += "\nItem Price: " + amount;
        emailBody += "\nClosing Date: " + sdf.format(deadline);
        emailBody += "\nClosing Time: " + sdf1.format(deadline);
       
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,19,staffEmail,emailAddress);
    }
     
       public Boolean sendLeaseSpaceEmail(String station,String unitNumber,Timestamp notiDate,Timestamp start,double amount,String name, String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nWe are pleased to inform you that your request is accepted!";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;
        emailBody += "\n\nThe details for signing of Tenancy agreement are as follows: ";
        emailBody += "\nDate: " + sdf.format(notiDate);
        emailBody += "\nTime: " + sdf1.format(notiDate);
        emailBody += "\nVenue: " + "131 Rifle Range Road CIMRT Building #02-345";
        emailBody += "\nDeposit: $" + amount;
        emailBody += "\nContract Start Date: " + sdf.format(start);
        emailBody += "\n\nIf you have any queries, you can contact the following person\n" +
                        "Name: Low Xuan Zhi\n" +
                        "Contact No: 98763213\n" +
                        "Contact Email: cimrtcoralIsland@cimrt.com";
        emailBody += "\n\nThank you, have a great day ahead! ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,20,staffEmail,emailAddress);
    }
       
     public Boolean sendSigningEmail(String station,String unitNumber,Timestamp interviewTo,String name,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe details for signing of Tenancy agreement are changed as follows: ";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewTo);
        emailBody += "\nVenue: " + "131 Rifle Range Road CIMRT Building #02-345";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
       
        return emailProcess(emailBody,24,staffEmail,emailAddress);
    }
     
     
       
         public Boolean sendLeaseSpaceEmail1(String station,String unitNumber,String name, String emailAddress, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nPlease be informed that your request is unsuccessful.";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";  
        return emailProcess(emailBody,21,staffEmail,emailAddress);
    }
         
          public Boolean sendApplicantEmail(String name, String emailAddress, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nWe acknowledge receipt of your application for a position at CIMRT and sincerely appreciate your interest in our company. ";
         emailBody += "\n\nWe will screen all applicants and select candidates whose qualifications seem to meet our needs. We will carefully consider your application during the initial screening and will contact"
                 + " you if you are selected to continue in the recruitment process. We wish you every success.";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";
       
        return emailProcess(emailBody,22,staffEmail,emailAddress);
    }
    
     public Boolean sendContractEmail(Timestamp contractFrom, Timestamp contractTo, String name, String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
       
        emailBody += "\nThe signing of contract details are as follows: ";
        emailBody += "\nDate: " + sdf.format(contractTo);
        emailBody += "\nTime: " + sdf1.format(contractFrom) + " - " + sdf1.format(contractTo);
        emailBody += "\nVenue: " + "131 Rifle Range Road CIMRT Building #02-345";
        emailBody += "\n\nThank you, have a great day ahead! ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT HR Team";
       
        return emailProcess(emailBody,23,staffEmail,emailAddress);
    }
     
      public Boolean sendApproveFittingEmail(Timestamp startdate, Timestamp endDate,String name,String station, String unitNumber, String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nWe are pleased to inform you that your fitting out request is accepted!";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;  
        emailBody += "\n\nPlease be informed that your contract has changed to:";
        emailBody += "\nStart Date: " + sdf.format(startdate);
        emailBody += "\nEnd Date: " + sdf.format(endDate);
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,20,staffEmail,emailAddress);
    }
      
       public Boolean sendRejectFittingEmail(String remarks,String name,String station, String unitNumber,String emailAddress, String staffEmail)
    {
      
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nPlease be informed that your fitting out request is rejected.";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;   
        emailBody += "\n\nRemarks: ";
        emailBody += remarks;
        emailBody += "\n Please make amendments and mail it to CIMRT Pte.Ltd, CIMRT Headquarters, "
                + "Reception Block A, 251 North Bridge Road Singapore 179102 within one week from this email's stipulated date.";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";  
       
        return emailProcess(emailBody,21,staffEmail,emailAddress);
    }
       
    public Boolean sendPreInspectionEmail(Timestamp interviewTo,String name,String station, String unitNumber,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe details for Pre Joint Inspection are changed as follows: ";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;   
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewTo);
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
       
        return emailProcess(emailBody,25,staffEmail,emailAddress);
    }
    
    public Boolean sendPreInspectionEmail1(Timestamp interviewTo,String name,String station, String unitNumber,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe details for Pre Joint Inspection are as follows: ";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;   
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewTo);
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
       
        return emailProcess(emailBody,26,staffEmail,emailAddress);
    }
    
     public Boolean sendFittingEmail(String name,String station,String unitNumber, String emailAddress, String staffEmail)
    {
        String emailBody = "";
        emailBody += "Dear I&A team,";
        
        emailBody += "\n\nPlease be informed that" + name + " has submitted a fitting out request";
        emailBody += "\n\nThe leasing space details are as follows: ";
        emailBody += "\nCompany: " + name;
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;    
        return emailProcess(emailBody,27,emailAddress,staffEmail);
    }
     
     public Boolean sendPostInspectionEmail1(Timestamp interviewTo,String name,String station, String unitNumber,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe details for Post Joint Inspection are as follows: ";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;   
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewTo);
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
       
        return emailProcess(emailBody,29,staffEmail,emailAddress);
    }
     
      public Boolean sendPostInspectionEmail(Timestamp interviewTo,String name,String station, String unitNumber,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe details for Post Joint Inspection are changed as follows: ";
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber;   
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewTo);
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
       
        return emailProcess(emailBody,28,staffEmail,emailAddress);
    }
      
      public Boolean sendTerminationEmail(double chargeFee, String contractId,String name,String station, String unitNumber, String emailAddress, String staffEmail)
    {
       
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\nDo take note that you will be charged an early termination fee for the following contract:";
        emailBody += "\nContract Id: " + contractId;
        emailBody += "\nStation: " + station;
        emailBody += "\nUnit Number: " + unitNumber; 
        emailBody += "\nCharge Fee: $" + chargeFee; 
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,30,staffEmail,emailAddress);
    }
      
      //Signing of tenancy agreement for advertisement
    public Boolean sendSigningEmail1(String venue, String code,String location, String type, String numberCode,Timestamp interviewTo,String name,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nThe details for signing of Tenancy agreement are changed as follows: ";
        emailBody += "\nVenue: " + venue;
         emailBody += "\nCode: " + code;
        emailBody += "\nLocation: " + location;
        emailBody += "\nType: " + type;
        emailBody += "\nIdentifier: " + numberCode;
        emailBody += "\nDate: " + sdf.format(interviewTo);
        emailBody += "\nTime: " + sdf1.format(interviewTo);
        emailBody += "\nVenue: " + "131 Rifle Range Road CIMRT Building #02-345";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
       
        return emailProcess(emailBody,24,staffEmail,emailAddress);
    }
      
    public Boolean sendInstallationEmail(Timestamp startDate, Timestamp endDate,String venue,String code,String location, String type, String numberCode,String name,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nPlease be informed that your advertisement is installed successfully at: ";
        emailBody += "\nVenue: " + venue;
         emailBody += "\nCode: " + code;
        emailBody += "\nLocation: " + location;
        emailBody += "\nType: " + type;
        emailBody += "\nIdentifier: " + numberCode;
        emailBody += "\n\nYour contract will starts on " + sdf.format(startDate) + " and ends on " +sdf.format(endDate);
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,31,staffEmail,emailAddress);
    }
    
    public Boolean sendRemovalEmail(Timestamp startDate, Timestamp endDate,String venue, String code,String location, String type, String numberCode,String name,String emailAddress, String staffEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nPlease be informed that your advertisement is removed successfully at: ";
        emailBody += "\nVenue: " + venue;
         emailBody += "\nCode: " + code;
        emailBody += "\nLocation: " + location;
        emailBody += "\nType: " + type;
        emailBody += "\nIdentifier: " + numberCode;
        emailBody += "\n\nYour contract will ends on " +sdf.format(endDate);
        emailBody += "\n  Please come by the location where your advertising are, to collect your advertising materials on the following day. Any items materials not collected\n" +
"                            will be send back to CIMRT's HQ and be kept in the warehouse for 2 weeks. \n" +
"                            CIMRT Pte.Ltd, CIMRT Headquarters, Reception Block A, 251 North Bridge Road Singapore 179102 within one week from this email's stipulated date.";
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,32,staffEmail,emailAddress);
    }
    
    public Boolean sendAdvertSpaceEmail(String venue, String code,String location, String type,String numberCode,Timestamp notiDate,Timestamp start,String name, String emailAddress, String staffEmail)
    {
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH.mm");
        String emailBody = "";
        emailBody += "Dear " + name + ",";
        
        emailBody += "\n\nWe are pleased to inform you that your request is accepted!";
        emailBody += "\nVenue: " + venue;
         emailBody += "\nCode: " + code;
        emailBody += "\nLocation: " + location;
        emailBody += "\nType: " + type;
        emailBody += "\nIdentifier: " + numberCode;
        emailBody += "\n\nThe details for signing of Tenancy agreement are as follows: ";
        emailBody += "\nDate: " + sdf.format(notiDate);
        emailBody += "\nTime: " + sdf1.format(notiDate);
        emailBody += "\nVenue: " + "131 Rifle Range Road CIMRT Building #02-345";
        emailBody += "\nContract Start Date: " + sdf.format(start);
        emailBody += "\n\nIf you have any queries, you can contact the following person\n"
                + "Name: Low Xuan Zhi\n"
                + "Contact No: 98763213\n"
                + "Contact Email: cimrtcoralIsland@cimrt.com";
        emailBody += "\n\nThank you, have a great day ahead! ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";

        return emailProcess(emailBody, 20, staffEmail, emailAddress);
    }

    public Boolean sendAdvertSpaceEmail1(String venue, String code, String location, String type, String numberCode, String name, String emailAddress, String staffEmail) {
        String emailBody = "";
        emailBody += "Dear " + name + ",";

        emailBody += "\n\nPlease be informed that your request is unsuccessful.";
       emailBody += "\nVenue: " + venue;
         emailBody += "\nCode: " + code;
        emailBody += "\nLocation: " + location;
        emailBody += "\nType: " + type;
        emailBody += "\nIdentifier: " + numberCode;
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
        return emailProcess(emailBody, 21, staffEmail, emailAddress);
    }
         
    
     public Boolean sendAdvertTerminationEmail(double chargeFee, String contractId,String name,String venue, String code, String location, String type, String numberCode, String emailAddress, String staffEmail)
    {
       
        String emailBody = "";
        emailBody += "Dear " + name + ",";

        emailBody += "\nDo take note that you will be charged an early termination fee for the following contract:";
        emailBody += "\nContract Id: " + contractId;
        emailBody += "\nVenue: " + venue;
        emailBody += "\nCode: " + code;
        emailBody += "\nLocation: " + location;
        emailBody += "\nType: " + type;
        emailBody += "\nIdentifier: " + numberCode;
        emailBody += "\nCharge Fee: $" + chargeFee;
        emailBody += "\n\nThank you. ";
        emailBody += "\nWarmest Regards,";
        emailBody += "\nCIMRT I&A Team";
       
        return emailProcess(emailBody,30,staffEmail,emailAddress);
    }
    
    public Boolean emailPassword(String password, int type, String fromEmailAddress, String toEmailAddress)
    {
        String emailBody = "";
        
        emailBody += "Your password is reseted to " + password;
        
        emailBody += "\nPlease change your password immediately after you have logged in.";
        
        return emailProcess(emailBody,type,fromEmailAddress,toEmailAddress);
    }
    
    
    public boolean emailProcess(String emailBody,int type,String fromEmailAddress, String toEmailAddress ){
        try 
        {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");            
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);            
            Message msg = new MimeMessage(session);
            String subject="";
            
            if(type==0){
                subject = "[CIMRT Internal Staff System] Retrieving your Staff ID";
            } else if(type ==1){
                subject = "[CIMRT Internal Staff System] Resetting your Password";
            }else if(type ==2){
                subject = "[CIMRT Internal Staff System] You are required to attend this Workshop";
            }
            else if(type ==3){
                subject = "[CIMRT Internal Staff System] !!You are WITHDRAWN from a Workshop!!";
            }
            else if(type ==4){
                subject = "Selection for Interview";
            }
            else if(type ==5){
                subject = "Interview Details has been changed";
            }
            else if(type ==6){
                subject = "Interview has been cancelled";
            }
            else if(type ==7){
                subject = "Workshop Cancelled";
            }
            else if(type ==8){
                subject = "Leave Application Outcome";
            }
            else if(type ==9){
                subject = "[CIMRT Business Partner System] Retrieving your Partner ID";
            }
            else if(type ==10){
                subject = "[CIMRT Business Partner System] Resetting your Password";
            }
            else if(type ==11){
                subject = "[CIMRT Passenger Information System] Retrieving your Username";
            }
            else if(type ==12){
                subject = "[CIMRT Passenger Information System] Resetting your Password";
            }
            else if(type ==13){
                subject = "Congratulations, you have won the bid!";
            }
            else if(type ==14){
                subject = "We are sorry to inform you that bid is unsuccessful";
            }
            else if(type ==15){
                subject = "Delivery is scheduled";
            }
            else if(type ==16){
                subject = "Delivery is rescheduled";
            }
            else if(type ==17){
                subject = "Sorry, you are not shortlised";
            }
            else if(type ==18){
                subject = "Congratulations, you are shortlisted";
            }
            else if(type ==19){
                subject = "We have a new listing.";
            }
            else if(type ==20){
                subject = "Congratulations, your request is accepted";
            }
            else if(type ==21){
                subject = "We are sorry to inform you that your request is unsuccessful";
            }
            else if(type ==22){
                subject = "Thank you for your interest at CIMRT";
            }
             else if(type ==23){
                subject = "Important Notice";
            }
            else if(type ==24){
                subject = "Signing of Tenancy Agreenment Details has been changed";
            }
            else if(type ==25){
                subject = "Pre Joint Inspection Details has been changed";
            }
            else if(type ==26){
                subject = "Pre Joint Inspection has been Scheduled";
            }
             else if(type ==27){
                subject = "Tenant has submitted a fitting out request";
            }
            else if(type ==28){
                subject = "Post Joint Inspection Details has been changed";
            }
            else if(type ==29){
                subject = "Post Joint Inspection has been Scheduled";
            }
            else if(type ==30){
                subject = "Contract has been terminated";
            }
            else if(type ==31){
                subject = "Installation of Advertisement is Completed";
            }
            else if(type ==32){
                subject = "Removal of Advertisement is Completed";
            }
            if (msg != null)
            {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject(subject);
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);
                
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                
                Transport.send(msg);
                
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            
            return false;
        }
    }
}
