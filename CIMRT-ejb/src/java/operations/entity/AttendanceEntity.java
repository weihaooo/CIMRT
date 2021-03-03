/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.StaffEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Yoona
 */
@Entity
public class AttendanceEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;
    private String type;
    private String remarks;
    private Timestamp clockinTime;
    private Timestamp clockoutTime;
    @Temporal(TemporalType.DATE)
    private Date workDate;

    @ManyToOne
    private StaffEntity staff;

    public AttendanceEntity() {

    }

    public AttendanceEntity(String type, String remarks, Timestamp clockinTime, Timestamp clockoutTime, StaffEntity staff, Date date) {
        this.type = type;
        this.remarks = remarks;
        this.clockinTime = clockinTime;
        this.clockoutTime = clockoutTime;
        this.staff = staff;
        this.workDate = date;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Timestamp getClockinTime() {
        return clockinTime;
    }

    public void setClockinTime(Timestamp clockinTime) {
        this.clockinTime = clockinTime;
    }

    public Timestamp getClockoutTime() {
        return clockoutTime;
    }

    public void setClockoutTime(Timestamp clockoutTime) {
        this.clockoutTime = clockoutTime;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attendanceId != null ? attendanceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttendanceEntity)) {
            return false;
        }
        AttendanceEntity other = (AttendanceEntity) object;
        if ((this.attendanceId == null && other.attendanceId != null) || (this.attendanceId != null && !this.attendanceId.equals(other.attendanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.AttendanceEntity[ id=" + attendanceId + " ]";
    }

}
