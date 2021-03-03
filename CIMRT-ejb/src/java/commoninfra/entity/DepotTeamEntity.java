/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class DepotTeamEntity extends TeamEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="depotTeam")
    private List<DepotStaffEntity> staff = new ArrayList<DepotStaffEntity>();
    @OneToOne
    private RoleEntity role;
    
    public DepotTeamEntity(){
        super();
    }
    
    public DepotTeamEntity(NodeEntity node, RoleEntity role){
        super(node);
        this.role = role;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public List<DepotStaffEntity> getStaff() {
        return staff;
    }

    public void setStaff(List<DepotStaffEntity> depotStaff) {
        this.staff = depotStaff;
    }
    public void addStaff(DepotStaffEntity depotStaff) {
        this.staff.add(depotStaff);
    }
}
