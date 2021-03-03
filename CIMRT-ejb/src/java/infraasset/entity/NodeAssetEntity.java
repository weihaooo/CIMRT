/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kayleytan
 */
@Entity
public class NodeAssetEntity extends AssetEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private int lifetimeValue; //no. of years
    @Temporal(value = TemporalType.DATE)
    private Date purchaseDate;
    private int quantity;
    private String nodeAssetType;
    @Column(length=10000)
    private String remarks;

    public NodeAssetEntity() {
    }

    public NodeAssetEntity(String assetId, String assetName,int lifetimeValue, Date purchaseDate, int quantity, String nodeAssetType, String remarks) {
        super(assetId, assetName);
        this.lifetimeValue = lifetimeValue;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.nodeAssetType = nodeAssetType;
        this.remarks = remarks;
    }

    public String getNodeAssetType() {
        return nodeAssetType;
    }

    public void setNodeAssetType(String nodeAssetType) {
        this.nodeAssetType = nodeAssetType;
    }

    public int getLifetimeValue() {
        return lifetimeValue;
    }

    public void setLifetimeValue(int lifetimeValue) {
        this.lifetimeValue = lifetimeValue;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
}
