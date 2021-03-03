/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.entity;

import commoninfra.entity.TeamEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class RosterEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rosterId;
    
    @ManyToOne
    private TeamEntity team;
    
    @ManyToOne
    private ShiftEntity shift;
    
    @ManyToOne
    private NodeEntity node;

    @Temporal(TemporalType.DATE) 
    private Date date;
    
    
    public RosterEntity(){
        
    }
    
    public RosterEntity(TeamEntity team, ShiftEntity shift, NodeEntity node, Date date){
        this.team = team;
        this.shift = shift;
        this.node = node;
        this.date = date;
    }
    
    public Long getRosterId() {
        return rosterId;
    }

    public void setRosterId(Long rosterId) {
        this.rosterId = rosterId;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    public ShiftEntity getShift() {
        return shift;
    }

    public void setShift(ShiftEntity shift) {
        this.shift = shift;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rosterId != null ? rosterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RosterEntity)) {
            return false;
        }
        RosterEntity other = (RosterEntity) object;
        if ((this.rosterId == null && other.rosterId != null) || (this.rosterId != null && !this.rosterId.equals(other.rosterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "manpower.entity.RosterEntity[ id=" + rosterId + " ]";
    }
    
}
