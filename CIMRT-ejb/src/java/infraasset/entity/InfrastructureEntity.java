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
import javax.persistence.OneToMany;

/**
 *
 * @author zhuming
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class InfrastructureEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String infraId;
    private String infraName;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "infrastructure")
    private List<AssetEntity> assets = new ArrayList<AssetEntity>();

    

    public InfrastructureEntity() {
    }

    public InfrastructureEntity(String infraId, String infraName) {
        this.infraId = infraId;
        this.infraName = infraName;
    }

    public String getInfraId() {
        return infraId;
    }

    public void setInfraId(String infraId) {
        this.infraId = infraId;
    }

    public String getInfraName() {
        return infraName;
    }

    public void setInfraName(String infraName) {
        this.infraName = infraName;
    }

    public List<AssetEntity> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetEntity> assets) {
        this.assets = assets;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (infraId != null ? infraId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InfrastructureEntity)) {
            return false;
        }
        InfrastructureEntity other = (InfrastructureEntity) object;
        if ((this.infraId == null && other.infraId != null) || (this.infraId != null && !this.infraId.equals(other.infraId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InfrastructureEntity[ id=" + infraId + " ]";
    }

}
