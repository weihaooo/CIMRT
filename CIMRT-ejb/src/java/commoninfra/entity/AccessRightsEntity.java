/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author Yoona
 */
@Entity
public class AccessRightsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessId;
    private String accessName;

    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "accessrights")
    private Collection<RoleEntity> roles = new ArrayList<RoleEntity>();

    public AccessRightsEntity() {
    }

    public AccessRightsEntity(String accessName) {
        this.accessName = accessName;
    }

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accessId != null ? accessId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessRightsEntity)) {
            return false;
        }
        AccessRightsEntity other = (AccessRightsEntity) object;
        if ((this.accessId == null && other.accessId != null) || (this.accessId != null && !this.accessId.equals(other.accessId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccessRightsEntity[ id=" + accessId + " ]";
    }
    
}
