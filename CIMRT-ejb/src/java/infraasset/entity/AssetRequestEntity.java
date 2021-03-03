/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import tenderModule.entity.PurchaseRequestEntity;

/**
 *
 * @author Zhirong
 */
@Entity
public class AssetRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String assetRequestId;
    private String assetRequestType; 
    private String assetName;
    private int qty;
    private String status;
    @Column(length=10000)
    private String remark;
    private Timestamp reqDate;
    @ManyToOne
    private AssetEntity asset;
    @ManyToOne 
    private PurchaseRequestEntity purchaseRequest;
    
    
    public AssetRequestEntity() {

    }

    public AssetRequestEntity(String assetRequestId, String assetRequestType, String assetName, int qty, String status, String remark, Timestamp reqDate) {
        this.assetRequestId = assetRequestId;
        this.assetRequestType = assetRequestType;
        this.assetName = assetName;
        this.qty = qty;
        this.status = status;
        this.remark = remark;
        this.reqDate = reqDate;
    }
    

    public String getAssetRequestId() {
        return assetRequestId;
    }

    public void setAssetRequestId(String assetRequestId) {
        this.assetRequestId = assetRequestId;
    }

    public String getAssetRequestType() {
        return assetRequestType;
    }

    public void setAssetRequestType(String assetRequestType) {
        this.assetRequestType = assetRequestType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getReqDate() {
        return reqDate;
    }

    public void setReqDate(Timestamp reqDate) {
        this.reqDate = reqDate;
    }

    public AssetEntity getAsset() {
        return asset;
    }

    public void setAsset(AssetEntity asset) {
        this.asset = asset;
    }

    public PurchaseRequestEntity getPurchaseRequest() {
        return purchaseRequest;
    }

    public void setPurchaseRequest(PurchaseRequestEntity purchaseRequest) {
        this.purchaseRequest = purchaseRequest;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetRequestId != null ? assetRequestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssetRequestEntity)) {
            return false;
        }
        AssetRequestEntity other = (AssetRequestEntity) object;
        if ((this.assetRequestId == null && other.assetRequestId != null) || (this.assetRequestId != null && !this.assetRequestId.equals(other.assetRequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "infraasset.entity.AssetRequestEntity[ assetRequestId=" + assetRequestId + " ]";
    }

}
