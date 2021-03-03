/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.entity;

import businessPartner.entity.BusinessPartnerEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class BidEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String bidId;
    private Timestamp bidDate;
    private double bidPrice;
    private String status;
    @Column(length = 10000)
    private String remarks;

    @ManyToOne(cascade={CascadeType.ALL})
    private PurchaseRequestEntity purchaseRequest;
    
   @ManyToOne(cascade={CascadeType.ALL})
    private BusinessPartnerEntity businessPartner;

    public BidEntity() {
    }

    public BidEntity(String bidId, Timestamp bidDate, double bidPrice, String status, String remarks) {
        this.bidId = bidId;
        this.bidDate = bidDate;
        this.bidPrice = bidPrice;
        this.status = status;
        this.remarks = remarks;
    }
        
    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public Timestamp getBidDate() {
        return bidDate;
    }

    public void setBidDate(Timestamp bidDate) {
        this.bidDate = bidDate;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PurchaseRequestEntity getPurchaseRequest() {
        return purchaseRequest;
    }

    public void setPurchaseRequest(PurchaseRequestEntity purchaseRequest) {
        this.purchaseRequest = purchaseRequest;
    }   

    public BusinessPartnerEntity getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartnerEntity businessPartner) {
        this.businessPartner = businessPartner;
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
        hash += (bidId != null ? bidId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BidEntity)) {
            return false;
        }
        BidEntity other = (BidEntity) object;
        if ((this.bidId == null && other.bidId != null) || (this.bidId != null && !this.bidId.equals(other.bidId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tenderModule.entity.BidEntity[ id=" + bidId + " ]";
    }
    
}
