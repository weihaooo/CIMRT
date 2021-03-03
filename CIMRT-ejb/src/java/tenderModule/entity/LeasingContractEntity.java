/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.entity;

import businessPartner.entity.BusinessPartnerEntity;
import infraasset.entity.LeasingSpaceEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class LeasingContractEntity implements Serializable {

     private static final long serialVersionUID = 1L;
    @Id
    private String leasingContractId;
    private Timestamp submittedDate;
    private Timestamp signedDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private int yearsOfContract;
    private double deposit;
    private String status;
    private boolean signingOfAgreement;
    private String fittingRequest;
    

    @ManyToOne(cascade={CascadeType.ALL})
    private BusinessPartnerEntity partner;
    
    @ManyToOne(cascade={CascadeType.ALL})
    private LeasingSpaceEntity leasingSpace;
    
    @OneToOne(cascade={CascadeType.PERSIST})
    private JointInspectionEntity jointInspection;

    public LeasingContractEntity() {
    }

    public LeasingContractEntity(String leasingContractId, Timestamp submittedDate, Timestamp signedDate, Timestamp startDate, Timestamp endDate, int yearsOfContract, double deposit, String status, boolean signingOfAgreement, String fittingRequest) {
        this.leasingContractId = leasingContractId;
        this.submittedDate = submittedDate;
        this.signedDate = signedDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.yearsOfContract = yearsOfContract;
        this.deposit = deposit;
        this.status = status;
        this.signingOfAgreement = signingOfAgreement;
        this.fittingRequest = fittingRequest;
    }
    
    

    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
    }

    public JointInspectionEntity getJointInspection() {
        return jointInspection;
    }

    public void setJointInspection(JointInspectionEntity jointInspection) {
        this.jointInspection = jointInspection;
    }      

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getLeasingContractId() {
        return leasingContractId;
    }

    public void setLeasingContractId(String leasingContractId) {
        this.leasingContractId = leasingContractId;
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

    public int getYearsOfContract() {
        return yearsOfContract;
    }

    public void setYearsOfContract(int yearsOfContract) {
        this.yearsOfContract = yearsOfContract;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BusinessPartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(BusinessPartnerEntity partner) {
        this.partner = partner;
    }

    public LeasingSpaceEntity getLeasingSpace() {
        return leasingSpace;
    }

    public void setLeasingSpace(LeasingSpaceEntity leasingSpace) {
        this.leasingSpace = leasingSpace;
    }

    public Timestamp getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Timestamp signedDate) {
        this.signedDate = signedDate;
    }

    public boolean isSigningOfAgreement() {
        return signingOfAgreement;
    }

    public void setSigningOfAgreement(boolean signingOfAgreement) {
        this.signingOfAgreement = signingOfAgreement;
    }

    public String getFittingRequest() {
        return fittingRequest;
    }

    public void setFittingRequest(String fittingRequest) {
        this.fittingRequest = fittingRequest;
    }

    
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (leasingContractId  != null ? leasingContractId .hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LeasingContractEntity)) {
            return false;
        }
        LeasingContractEntity other = (LeasingContractEntity) object;
        if ((this.leasingContractId  == null && other.leasingContractId  != null) || (this.leasingContractId  != null && !this.leasingContractId .equals(other.leasingContractId ))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tenderModule.entity.LeasingContractEntity[ id=" + leasingContractId  + " ]";
    }
    
}
