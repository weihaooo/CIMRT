/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author FABIAN
 */
@Entity
public class PassengerFeedbackEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;
    private String description;
    private Timestamp feedbackDateTime;
    private String submitter;
    private String subject;//Title
    private int mobileNo;
    private String address;//feedback pertaining to which specific station address (Node Adress)

    public PassengerFeedbackEntity() {

    }

    public PassengerFeedbackEntity(String description, Timestamp feedbackDateTime, String submitter, String subject, int mobileNo, String address) {
        this.description = description;
        this.feedbackDateTime = feedbackDateTime;
        this.submitter = submitter;
        this.subject = subject;
        this.mobileNo = mobileNo;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feedbackId != null ? feedbackId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PassengerFeedbackEntity)) {
            return false;
        }
        PassengerFeedbackEntity other = (PassengerFeedbackEntity) object;
        if ((this.feedbackId == null && other.feedbackId != null) || (this.feedbackId != null && !this.feedbackId.equals(other.feedbackId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "passenger.entity.PassengerFeedbackEntity[ feedbackId=" + feedbackId + " ]";
    }

}
