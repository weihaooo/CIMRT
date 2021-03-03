/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import manpower.entity.RosterEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TeamEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @ManyToOne
    private NodeEntity node = new NodeEntity();
    
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="team")
    private List<RosterEntity> rosters = new ArrayList<RosterEntity>();
    
    public TeamEntity(){
        
    }
    
    public TeamEntity(NodeEntity node){
        this.node = node;
    }
    
    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public List<RosterEntity> getRosters() {
        return rosters;
    }

    public void setRosters(List<RosterEntity> rosters) {
        this.rosters = rosters;
    }
    
    public void addRoster(RosterEntity roster){
        this.rosters.add(roster);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (teamId != null ? teamId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeamEntity)) {
            return false;
        }
        TeamEntity other = (TeamEntity) object;
        if ((this.teamId == null && other.teamId != null) || (this.teamId != null && !this.teamId.equals(other.teamId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TeamEntity[ id=" + teamId + " ]";
    }

}
