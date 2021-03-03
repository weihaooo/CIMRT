/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.entity;

import businessPartner.entity.BusinessPartnerEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class AdvertisingContractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String advertisingId;
    private Timestamp submittedDate;
    private Timestamp signedDate;
    private boolean signingOfAgreement;
    private Timestamp startDate;
    private Timestamp endDate;
    private int yearsOfContract;
    private String status;
    private boolean installation;
    private boolean removal;
    
    @ManyToOne(cascade={CascadeType.ALL})
    private BusinessPartnerEntity partner;
    
    
    @ManyToOne(cascade={CascadeType.ALL})
    private AdvertisementSpaceEntity advertisement;

    public AdvertisingContractEntity() {
    }

    public AdvertisingContractEntity(String advertisingId, Timestamp submittedDate, Timestamp signedDate, boolean signingOfAgreement, Timestamp startDate, Timestamp endDate, int yearsOfContract, String status, boolean installation, boolean removal) {
        this.advertisingId = advertisingId;
        this.submittedDate = submittedDate;
        this.signedDate = signedDate;
        this.signingOfAgreement = signingOfAgreement;
        this.startDate = startDate;
        this.endDate = endDate;
        this.yearsOfContract = yearsOfContract;
        this.status = status;
        this.installation = installation;
        this.removal = removal;
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
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

    public boolean isInstallation() {
        return installation;
    }

    public void setInstallation(boolean installation) {
        this.installation = installation;
    }

    public boolean isRemoval() {
        return removal;
    }

    public void setRemoval(boolean removal) {
        this.removal = removal;
    }

    public AdvertisementSpaceEntity getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(AdvertisementSpaceEntity advertisement) {
        this.advertisement = advertisement;
    }

    public BusinessPartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(BusinessPartnerEntity partner) {
        this.partner = partner;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (advertisingId != null ? advertisingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdvertisingContractEntity)) {
            return false;
        }
        AdvertisingContractEntity other = (AdvertisingContractEntity) object;
        if ((this.advertisingId == null && other.advertisingId != null) || (this.advertisingId != null && !this.advertisingId.equals(other.advertisingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tenderModule.entity.AdvertisingContractEntity[ id=" + advertisingId + " ]";
    }
    
}
