/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import passenger.entity.PassengerEntity;
import passenger.sessionbean.AcctSessionBeanLocal;
import passenger.sessionbean.PassengerFeedbackSessionBeanLocal;
import routefare.entity.NodeEntity;

/**
 *
 * @author FABIAN
 */
@Named(value = "passengerFeedbackManagedBean")
@SessionScoped
public class PassengerFeedbackManagedBean implements Serializable {

    @EJB
    AcctSessionBeanLocal acctSessionBeanLocal;
    @EJB
    PassengerFeedbackSessionBeanLocal passengerFeedbackSessionBeanLocal;

    private String username;
    private String firstName;
    private String lastName;
    private PassengerEntity passenger;

    private String description;
    private Timestamp feedbackDateTime;
    private String submitter;
    private boolean anonymous;
    private String subject;
    private int mobileNo;
    private List<NodeEntity> stationList;
    private List<String> stationNames;
    private String selectedStation;
    private String publicName;

    public PassengerFeedbackManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            passenger = acctSessionBeanLocal.searchPassenger(username);
            firstName = passenger.getFirstName();
            lastName = passenger.getLastName();

        }
        anonymous = false;
    }

    public String submitFeedback() {
        boolean temp = true;

        if (username != null && anonymous != true) {
            PassengerEntity pe = passengerFeedbackSessionBeanLocal.searchPassenger(username);
            submitter = pe.getUsername() + " " + pe.getFirstName() + " " + pe.getLastName();
            temp = passengerFeedbackSessionBeanLocal.submitFeedback(description, submitter, subject, pe.getPhoneNumber(), selectedStation);
        } else if (username != null && anonymous == true) {
            submitter = "Anonymous";
            temp = passengerFeedbackSessionBeanLocal.submitFeedback(description, submitter, subject, 0, selectedStation);
        } else if (username == null && anonymous == true) {
            submitter = "Anonymous";
            temp = passengerFeedbackSessionBeanLocal.submitFeedback(description, submitter, subject, 0, selectedStation);
        } else if (username == null && anonymous != true) {
            temp = passengerFeedbackSessionBeanLocal.submitFeedback(description, publicName, subject, mobileNo, selectedStation);
        }

        if (temp) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Your feedback has been submitted successfully",
                            ""));

            this.description = null;
            this.subject = null;
            this.anonymous = false;
            this.publicName = null;
            this.mobileNo = 0;
            this.selectedStation = null;
            return "submitFeedback";
        }
        return "submitFeedback";
    }

    public String getSelectedStation() {
        return selectedStation;
    }

    public void setSelectedStation(String selectedStation) {
        this.selectedStation = selectedStation;
    }

    public List<String> getStationNames() {
        stationList = getStationList();
        stationNames = new ArrayList<String>();
        for (int i = 0; i < stationList.size(); i++) {
            stationNames.add(stationList.get(i).getAddress());
        }

        return stationNames;
    }

    public void setStationNames(List<String> stationNames) {
        this.stationNames = stationNames;
    }

    public List<NodeEntity> getStationList() {
        stationList = passengerFeedbackSessionBeanLocal.getStations();
        return stationList;
    }

    public void setStationList(List<NodeEntity> stationList) {
        this.stationList = stationList;
    }

    public void addMessage() {

        String summary = anonymous ? "Submit as Anonymous." : "Please fill in your Name and Contact Number.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(int mobileNo) {
        this.mobileNo = mobileNo;
    }

    public boolean isAnonymous() {
        return anonymous;
    }


    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getFeedbackDateTime() {
        return feedbackDateTime;
    }

    public void setFeedbackDateTime(Timestamp feedbackDateTime) {
        this.feedbackDateTime = feedbackDateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

}
