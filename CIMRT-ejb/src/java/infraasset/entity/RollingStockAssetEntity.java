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
 * @author Zhirong
 */
@Entity
public class RollingStockAssetEntity extends AssetEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private int lifetimeValue; //no. of years
    @Temporal(value = TemporalType.DATE)
    private Date purchaseDate;
    private int quantity;
    private int storage;
    private String rollingStockAssetType;
    @Column(length=10000)
    private String remarks;
    
    
    
    public RollingStockAssetEntity(){
        
    }
    
    public RollingStockAssetEntity(String assetId, String assetName, int lifetimeValue, Date purchaseDate, int quantity, int storage,String rollingStockAssetType,String remarks) {
        super(assetId, assetName);
        this.lifetimeValue = lifetimeValue;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.storage = storage;
        this.rollingStockAssetType = rollingStockAssetType;
        this.remarks = remarks;
    }
    
    

    public String getRollingStockAssetType() {
        return rollingStockAssetType;
    }

    public void setRollingStockAssetType(String rollingStockAssetType) {
        this.rollingStockAssetType = rollingStockAssetType;
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

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }
    
    
    
    
    
}
