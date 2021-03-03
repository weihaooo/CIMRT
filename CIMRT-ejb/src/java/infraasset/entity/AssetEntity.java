/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import operations.entity.MaintenanceRequestEntity;

/**
 *
 * @author Zhirong
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AssetEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String assetId;
    private String assetName;
    @ManyToOne
    private InfrastructureEntity infrastructure;
    
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "asset")
    private List<MaintenanceRequestEntity> maintenanceRequests = new ArrayList<MaintenanceRequestEntity>();
 

    public AssetEntity(){
        
    }
    
    public AssetEntity(String assetId, String assetName){
        this.assetId = assetId;
        this.assetName = assetName;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public InfrastructureEntity getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(InfrastructureEntity infrastructure) {
        this.infrastructure = infrastructure;
    }

    public List<MaintenanceRequestEntity> getMaintenanceRequests() {
        return maintenanceRequests;
    }

    public void setMaintenanceRequests(List<MaintenanceRequestEntity> maintenanceRequests) {
        this.maintenanceRequests = maintenanceRequests;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetId != null ? assetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssetEntity)) {
            return false;
        }
        AssetEntity other = (AssetEntity) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.RideEntity[ userID =" + assetId + " ]";
    }
}
