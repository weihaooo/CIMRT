/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Yoona
 */
@Entity
public class ShiftEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String shiftId;
    private String shiftName;
    private Timestamp startTime;
    private Timestamp endTime;
    
    public ShiftEntity(){
        
    }
    
    public ShiftEntity(String shiftId, String shiftName, Timestamp startTime, Timestamp endTime){
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shiftId != null ? shiftId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShiftEntity)) {
            return false;
        }
        ShiftEntity other = (ShiftEntity) object;
        if ((this.shiftId == null && other.shiftId != null) || (this.shiftId != null && !this.shiftId.equals(other.shiftId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "manpower.entity.ShiftEntity[ id=" + shiftId + " ]";
    }
    
}
