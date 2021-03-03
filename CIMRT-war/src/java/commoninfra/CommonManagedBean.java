/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.sessionbean.MessageSessionBeanLocal;
import commoninfra.entity.MessageEntity;
import commoninfra.entity.StaffEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Yoona
 */
@Named(value = "commonManagedBean")
@SessionScoped
public class CommonManagedBean implements Serializable {

    /**
     * Creates a new instance of CommonManagedBean
     */
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;
    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    private String staffId;
    private String firstName;
    private String lastName;
    private String nric;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String maritalStatus;
    private String race;
    private String nationality;
    private String religion;
    private String educationQualification;
    private String salary;
    private String department;
    private String role;
    private String team;
    private String mcEntitlement;
    private String leaveEntitlement;
    private ArrayList<String> staffDetails;

    private String password;
    private String newPW;
    private String newPW2;

    private List<StaffEntity> receivers;
    private String receiver;
    private List<String> receiverList;
    private String msgContent;
    private Timestamp msgDateTime;
    private List<MessageEntity> conversationList;
    private List<MessageEntity> entireConversation;
    private List<String> convoList;
    private String conversation;
    private String nodeCode;

    public CommonManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            firstName = staffDetails.get(0);
            lastName = staffDetails.get(1);
            nric = staffDetails.get(2);
            phoneNumber = staffDetails.get(3);
            emailAddress = staffDetails.get(4);
            address = staffDetails.get(5);
            gender = staffDetails.get(6);
            dateOfBirth = staffDetails.get(7);
            maritalStatus = staffDetails.get(8);
            race = staffDetails.get(9);
            nationality = staffDetails.get(10);
            religion = staffDetails.get(11);
            educationQualification = staffDetails.get(12);
            salary = staffDetails.get(13);
            role = staffDetails.get(14);
            mcEntitlement = staffDetails.get(15);
            leaveEntitlement = staffDetails.get(16);
            if (staffDetails.size() == 19) {
                team = staffDetails.get(17);
                nodeCode = staffDetails.get(18);
            }
        }
        conversationList = getEntireConversation();
        

    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Timestamp getMsgDateTime() {
        return msgDateTime;
    }

    public void setMsgDateTime(Timestamp msgDateTime) {
        this.msgDateTime = msgDateTime;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public String clearMessage() {
        msgContent = "";
        return "sendMessages?faces-redirect=true";
    }

    public List<String> getConvoList() {
        return convoList;
    }

    public void setConvoList(List<String> convoList) {
        this.convoList = convoList;
    }

    public List<MessageEntity> getConversationList() {
        return conversationList;
    }

    public List<MessageEntity> getEntireConversation(){
        entireConversation = messageSessionBeanLocal.getEntireConversation(staffId);
        return entireConversation;
    }
    
    public void setConversationList(List<MessageEntity> conversationList) {
        this.conversationList = conversationList;
    }

    public String updateConversationList() {
        String[] receiverId = receiver.split(" ");
        String recId = receiverId[0];
        conversationList = messageSessionBeanLocal.getConversation(staffId, recId);
        convoList = new ArrayList<String>();
        if (conversationList.isEmpty()) {
            System.out.println("No Messages Retrieved");
        } else {
            for (int i = 0; i < conversationList.size(); i++) {
                convoList.add("Date & Time: " + conversationList.get(i).getMessageDate().toString() + " "
                        + conversationList.get(i).getSenderName() + " " + conversationList.get(i).getReceiverName() + " " + conversationList.get(i).getMsgContent());

            }
        }

        return "sendMessages";
    }

    public List<StaffEntity> getReceivers() {
        receivers = messageSessionBeanLocal.getReceivers();
        return receivers;
    }

    public void setReceiver(List<StaffEntity> receiver) {
        this.receivers = receiver;
    }

    public List<String> getReceiverList() {
        receivers = getReceivers();
        receiverList = new ArrayList<String>();
        for (int i = 0; i < receivers.size(); i++) {
            if (!(receivers.get(i).getStaffId().equals(staffId))) {
                receiverList.add(receivers.get(i).getStaffId() + " " + receivers.get(i).getFirstName() + " " + receivers.get(i).getLastName());

            }
        }

        return receiverList;
    }

    public void setReceiverList(List<String> receiverList) {
        this.receiverList = receiverList;
    }

    public String sendMessage() {
        /*String[] receiverId = receiver.split(" ");
        String recId = receiverId[0];
        String sendStatus = messageSessionBeanLocal.sendMessage(staffId, recId, msgContent);*/
        String sendStatus = messageSessionBeanLocal.sendMessage(staffId, receiver, msgContent);

        if (sendStatus.equals("sent")) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Message sent to " + receiver + " successfully.",
                            ""));
            updateConversationList();
            return "sendMessages";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Message NOT sent to " + receiver + ".",
                            "Please try sending again"));
            return "sendMessages";
        }
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getEducationQualification() {
        return educationQualification;
    }

    public void setEducationQualification(String educationQualification) {
        this.educationQualification = educationQualification;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getMcEntitlement() {
        return mcEntitlement;
    }

    public void setMcEntitlement(String mcEntitlement) {
        this.mcEntitlement = mcEntitlement;
    }

    public String getLeaveEntitlement() {
        return leaveEntitlement;
    }

    public void setLeaveEntitlement(String leaveEntitlement) {
        this.leaveEntitlement = leaveEntitlement;
    }

    public ArrayList<String> getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(ArrayList<String> staffDetails) {
        this.staffDetails = staffDetails;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPW() {
        return newPW;
    }

    public void setNewPW(String newPW) {
        this.newPW = newPW;
    }

    public String getNewPW2() {
        return newPW2;
    }

    public void setNewPW2(String newPW2) {
        this.newPW2 = newPW2;
    }

    public String editProfile() {
        ArrayList<String> editedList = new ArrayList<String>();
        editedList.add(staffId);
        editedList.add(phoneNumber);
        editedList.add(emailAddress);
        editedList.add(address);
        editedList.add(maritalStatus);
        editedList.add(religion);
        editedList.add(educationQualification);

        boolean success = accountSessionBeanLocal.editProfile(editedList);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated!", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Profile update has failed!", ""));
        }
        return "profile";
    }

    public String changePassword() {
        if (!newPW.equals(newPW2)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Passwords does not match!", ""));
            return "changePassword";
        } else if (newPW.equals(password)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Passwords must not be the same as current passwords", ""));
            return "changePassword";
        }

        boolean result = accountSessionBeanLocal.changePassword(staffId, password, newPW, newPW2);

        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed Successfully!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current Password is wrong!", ""));
            return "changePassword";
        }

        return "home";
    }

}
