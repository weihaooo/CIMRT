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
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class StationTeamEntity extends TeamEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "stationTeam")
    private List<StationStaffEntity> staff = new ArrayList<StationStaffEntity>();

    public StationTeamEntity() {
        super();
    }

    public StationTeamEntity(ArrayList<StationStaffEntity> stationStaff, NodeEntity node) {
        super(node);
        this.staff = stationStaff;
    }

    public List<StationStaffEntity> getStaff() {
        return staff;
    }

    public void setStaff(ArrayList<StationStaffEntity> stationStaff) {
        this.staff = stationStaff;

    }

    public void addStaff(StationStaffEntity stationStaff) {
        this.staff.add(stationStaff);
    }
}
