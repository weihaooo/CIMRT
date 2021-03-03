/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.StationStaffEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Zhirong
 */
@Entity

public class ServiceLogEntity implements Serializable {

    @Id
    private String svcLogId;
    private Timestamp dateTime;
    private String subject;
    @Column(length=10000)
    private String content;
    @ManyToOne
    private StationStaffEntity stationStaff;
 

    public ServiceLogEntity(){
        
    }
    
    public ServiceLogEntity(String svcLogId, Timestamp dateTime, String subject, String content){
        this.svcLogId = svcLogId;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
    }

    public String getSvcLogId() {
        return svcLogId;
    }

    public void setSvcLogId(String svcLogId) {
        this.svcLogId = svcLogId;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StationStaffEntity getStationStaff() {
        return stationStaff;
    }

    public void setStationStaff(StationStaffEntity stationStaff) {
        this.stationStaff = stationStaff;
    }
    

}
