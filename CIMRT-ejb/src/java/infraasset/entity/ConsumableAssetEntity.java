/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kayleytan
 */
@Entity
public class ConsumableAssetEntity extends AssetEntity implements Serializable {

 @Temporal(value = TemporalType.DATE)
 private Date expiryDate;
 private int quantity;
 private String consumableAssetType;

 public ConsumableAssetEntity(){
     
 }
    public ConsumableAssetEntity(String assetId, String assetName, int quantity,Date expiryDate, String consumableAssetType) {
        super(assetId, assetName);
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.consumableAssetType = consumableAssetType;
    }

    public String getConsumableAssetType() {
        return consumableAssetType;
    }

    public void setConsumableAssetType(String consumableAssetType) {
        this.consumableAssetType = consumableAssetType;
    }
    
    
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
