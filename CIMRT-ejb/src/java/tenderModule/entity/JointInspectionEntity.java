/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author kayleytan
 */
@Entity
public class JointInspectionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jointInspectId;
    private Timestamp entryInspection;
    private Timestamp exitInspection;
    private String entryStatus;
    private String exitStatus;

    public JointInspectionEntity() {
    }

    public JointInspectionEntity(Timestamp entryInspection,Timestamp exitInspection, String entryStatus, String exitStatus) {
        this.entryInspection = entryInspection;
        this.exitInspection = exitInspection;
        this.entryStatus = entryStatus;
        this.exitStatus = exitStatus;
    }

    
    
    public Long getJointInspectId() {
        return jointInspectId;
    }

    public void setJointInspectId(Long jointInspectId) {
        this.jointInspectId = jointInspectId;
    }

    public Timestamp getEntryInspection() {
        return entryInspection;
    }

    public void setEntryInspection(Timestamp entryInspection) {
        this.entryInspection = entryInspection;
    }

   

    public Timestamp getExitInspection() {
        return exitInspection;
    }

    public void setExitInspection(Timestamp exitInspection) {
        this.exitInspection = exitInspection;
    }

  

    public String getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
        this.entryStatus = entryStatus;
    }

    public String getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(String exitStatus) {
        this.exitStatus = exitStatus;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jointInspectId != null ? jointInspectId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JointInspectionEntity)) {
            return false;
        }
        JointInspectionEntity other = (JointInspectionEntity) object;
        if ((this.jointInspectId == null && other.jointInspectId != null) || (this.jointInspectId != null && !this.jointInspectId.equals(other.jointInspectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tenderModule.entity.JointInspectionEntity[ id=" + jointInspectId + " ]";
    }
    
}
