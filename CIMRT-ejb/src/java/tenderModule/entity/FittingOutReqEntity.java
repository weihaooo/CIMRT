/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class FittingOutReqEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fittingOutId;
    private String duration;
    private double deposit;
    private String status; //Submitted and Approved
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp requestedDate;
    @Column(length = 10000)
    private String remarks;
   
    @OneToOne(cascade={CascadeType.PERSIST})
    private LeasingContractEntity leasingContract;

    public FittingOutReqEntity() {
    }

    public FittingOutReqEntity(String duration, double deposit, String status, Timestamp startDate, Timestamp endDate, Timestamp requestedDate, String remarks) {
        this.duration = duration;
        this.deposit = deposit;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestedDate = requestedDate;
        this.remarks = remarks;
    }

    
   
    public Long getFittingOutId() {
        return fittingOutId;
    }

    public void setFittingOutId(Long fittingOutId) {
        this.fittingOutId = fittingOutId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
    public LeasingContractEntity getLeasingContract() {
        return leasingContract;
    }

    public void setLeasingContract(LeasingContractEntity leasingContract) {
        this.leasingContract = leasingContract;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Timestamp getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Timestamp requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fittingOutId != null ? fittingOutId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FittingOutReqEntity)) {
            return false;
        }
        FittingOutReqEntity other = (FittingOutReqEntity) object;
        if ((this.fittingOutId == null && other.fittingOutId != null) || (this.fittingOutId != null && !this.fittingOutId.equals(other.fittingOutId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tenderModule.entity.FittingOutReqEntity[ id=" + fittingOutId + " ]";
    }
    
}
