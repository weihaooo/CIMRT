/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.StaffEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Zhirong
 */
@Entity
public class IncidentReportEntity implements Serializable {

    @Id
    private String incidentRepId;
   
    private Timestamp date;
    private String location; //station code, depot code or rolling stock code
    private String subject;
    @Column(length = 10000)
    private String content;
    private String status;
    @Column(length = 10000)
    private String signature;
    private String submitter;
    private String endorser;
    @ManyToOne
    private StaffEntity staff;
 
    
    public IncidentReportEntity() {

    }

    public IncidentReportEntity(String incidentRepId, Timestamp date, String location, String subject, String content, String status, String signature, String submitter, String endorser) {
        this.incidentRepId = incidentRepId;
        this.date = date;
        this.location = location;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.signature = signature;
        this.submitter = submitter;
        this.endorser = endorser;
    }

    public String getIncidentRepId() {
        return incidentRepId;
    }

    public void setIncidentRepId(String incidentRepId) {
        this.incidentRepId = incidentRepId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignature() {
        return signature;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getEndorser() {
        return endorser;
    }

    public void setEndorser(String endorser) {
        this.endorser = endorser;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }
    
}
    

   
