/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import infraasset.entity.AssetEntity;
import maintenance.entity.MaintenanceReportEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class MaintenanceRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String mainReqId;
    
    private Timestamp requestDate;
    private String mainReqType;
    @Column(length=10000)
    private String remark;
    private String mainReqStatus;
    private String submitter;
  
    @OneToOne(cascade={CascadeType.PERSIST})
    private MaintenanceReportEntity maintenanceReport;
    
    @ManyToOne(cascade={CascadeType.PERSIST})
    private AssetEntity asset;

    public MaintenanceRequestEntity() {
    }

    public MaintenanceRequestEntity(String mainReqId, Timestamp requestDate, String mainReqType, String remark, String mainReqStatus, String submitter) {
        this.mainReqId = mainReqId;
        this.requestDate = requestDate;
        this.mainReqType = mainReqType;
        this.remark = remark;
        this.mainReqStatus = mainReqStatus;
        this.submitter = submitter;
    }
  
   public MaintenanceReportEntity getMaintenanceReport(){ 
       return maintenanceReport;
   }

    public AssetEntity getAsset() {
        return asset;
    }

    public void setAsset(AssetEntity asset) {
        this.asset = asset;
    }
   
    public String getMainReqId() {
        return mainReqId;
    }

    public void setMainReqId(String mainReqId) {
        this.mainReqId = mainReqId;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

   

    public String getMainReqType() {
        return mainReqType;
    }

    public void setMainReqType(String mainReqType) {
        this.mainReqType = mainReqType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMainReqStatus() {
        return mainReqStatus;
    }

    public void setMainReqStatus(String mainReqStatus) {
        this.mainReqStatus = mainReqStatus;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public void setMaintenanceReport(MaintenanceReportEntity maintenanceReport) {
        this.maintenanceReport = maintenanceReport;
    }
    
    
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mainReqId != null ? mainReqId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaintenanceRequestEntity)) {
            return false;
        }
        MaintenanceRequestEntity other = (MaintenanceRequestEntity) object;
        if ((this.mainReqId == null && other.mainReqId != null) || (this.mainReqId != null && !this.mainReqId.equals(other.mainReqId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "operations.entity.MaintenanceRequestEntity[ id=" + mainReqId + " ]";
    }
    
}
