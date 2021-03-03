/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.CascadeType;

/**
 *
 * @author Yoona
 */
@Entity
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String staffRole;
    private String department;
    
    
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="role_accessrights") 
    private List<AccessRightsEntity> accessrights = new ArrayList<AccessRightsEntity>();

    public RoleEntity(){
        
    }

    public RoleEntity(String staffRole, String department) {
        this.staffRole = staffRole;
        this.department = department;
    }
    
    
    public void create(String staffRole, String department) {
        this.setStaffRole(staffRole);
        this.setDepartment(department);
    }
    
    public RoleEntity(String role) {
        this.staffRole = role;
    }
    

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public List<AccessRightsEntity> getAccessrights() {
        return accessrights;
    }

    public void setAccessrights(List<AccessRightsEntity> accessrights) {
        this.accessrights = accessrights;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }  

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffRole != null ? staffRole.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoleEntity)) {
            return false;
        }
        RoleEntity other = (RoleEntity) object;
        if ((this.staffRole == null && other.staffRole != null) || (this.staffRole != null && !this.staffRole.equals(other.staffRole))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.RoleEntity[ id=" + staffRole + " ]";
    }
    
}
