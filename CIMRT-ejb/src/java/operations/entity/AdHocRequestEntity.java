/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.StaffEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class AdHocRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reqId;
    private String description;
    private String title;
    private Timestamp inTime;
    private Timestamp outTime;
    private String reqType;
    private boolean acknowledged;
    
    @ManyToOne
    private StaffEntity requester;

    @ManyToOne
    private StaffEntity standbyStaff;
    
    @ManyToOne
    private NodeEntity node;

    public AdHocRequestEntity(String description, String title, Timestamp inTime, Timestamp outTime, String reqType, StaffEntity requester, StaffEntity standbyStaff, NodeEntity node) {
        this.description = description;
        this.title = title;
        this.inTime = inTime;
        this.outTime = outTime;
        this.reqType = reqType;
        this.requester = requester;
        this.standbyStaff = standbyStaff;
        this.node = node;
        this.acknowledged = false;
    }
    
    public AdHocRequestEntity(){
        
    }

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public StaffEntity getRequester() {
        return requester;
    }

    public void setRequester(StaffEntity requester) {
        this.requester = requester;
    }

    public StaffEntity getStandbyStaff() {
        return standbyStaff;
    }

    public void setStandbyStaff(StaffEntity standbyStaff) {
        this.standbyStaff = standbyStaff;
    }

    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reqId != null ? reqId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdHocRequestEntity)) {
            return false;
        }
        AdHocRequestEntity other = (AdHocRequestEntity) object;
        if ((this.reqId == null && other.reqId != null) || (this.reqId != null && !this.reqId.equals(other.reqId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.AdHocRequestEntity[ id=" + reqId + " ]";
    }

}
