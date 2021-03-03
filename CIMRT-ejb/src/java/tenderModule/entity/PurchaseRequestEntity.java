/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.entity;

import infraasset.entity.AssetRequestEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author kayleytan
 */
@Entity
public class PurchaseRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String purchaseRequestId;
    private Timestamp reqDate;
    private int qty;
    private Timestamp reqDeadline;
    private String status;
    private double maxBidAmount;
    private String category;
    private String itemName;
    @Column(length=10000)
    private String description;
    private double initalBidAmount;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="purchaseRequest")
    private Collection<AssetRequestEntity> assetRequests;
    
    public PurchaseRequestEntity() {
    }

    public PurchaseRequestEntity(String purchaseRequestId, Timestamp reqDate, int qty, Timestamp reqDeadline, String status, double maxBidAmount, String category, String itemName, String description, double initalBidAmount) {
        this.purchaseRequestId = purchaseRequestId;
        this.reqDate = reqDate;
        this.qty = qty;
        this.reqDeadline = reqDeadline;
        this.status = status;
        this.maxBidAmount = maxBidAmount;
        this.category = category;
        this.itemName = itemName;
        this.description = description;
        this.initalBidAmount = initalBidAmount;
    }

    

    public double getInitalBidAmount() {
        return initalBidAmount;
    }

    public void setInitalBidAmount(double initalBidAmount) {
        this.initalBidAmount = initalBidAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    
     public String getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(String purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public Timestamp getReqDate() {
        return reqDate;
    }

    public void setReqDate(Timestamp reqDate) {
        this.reqDate = reqDate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Timestamp getReqDeadline() {
        return reqDeadline;
    }

    public void setReqDeadline(Timestamp reqDeadline) {
        this.reqDeadline = reqDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMaxBidAmount() {
        return maxBidAmount;
    }

    public void setMaxBidAmount(double maxBidAmount) {
        this.maxBidAmount = maxBidAmount;
    }

    public Collection<AssetRequestEntity> getAssetRequests() {
        return assetRequests;
    }

    public void setAssetRequests(Collection<AssetRequestEntity> assetRequests) {
        this.assetRequests = assetRequests;
    }
       
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purchaseRequestId != null ? purchaseRequestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseRequestEntity)) {
            return false;
        }
        PurchaseRequestEntity other = (PurchaseRequestEntity) object;
        if ((this.purchaseRequestId == null && other.purchaseRequestId != null) || (this.purchaseRequestId != null && !this.purchaseRequestId.equals(other.purchaseRequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tenderModule.entity.PurchaseRequestEntity[ id=" + purchaseRequestId + " ]";
    }
    
}
