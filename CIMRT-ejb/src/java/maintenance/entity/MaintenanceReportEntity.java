/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance.entity;

import infraasset.entity.AssetRequestEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import operations.entity.MaintenanceRequestEntity;
import tenderModule.entity.BidEntity;

/**
 *
 * @author FABIAN
 */
@Entity
public class MaintenanceReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceReportId;
    private String rptTitle;
    private String reportDescription;
    private String assetUsed;
    private int quantityAssetUsed;
    private String maintenanceStatus;
    private Timestamp reportDateTime;
    private int qtySpoilt;
    private String submitterId;
    private String submitterName;
    
    @OneToOne(cascade={CascadeType.PERSIST}, mappedBy="maintenanceReport")
    private MaintenanceRequestEntity maintenanceRequest;
    
     @OneToOne(cascade={CascadeType.PERSIST})
    private AssetRequestEntity assetRequest;
    
    public MaintenanceReportEntity() {
    }
    
    public MaintenanceReportEntity(String rptTitle, String reportDescription, String assetUsed, int quantityAssetUsed, String maintenanceStatus , 
            Timestamp reportDateTime, String submitterId, String submitterName, int qtySpoilt) {
        this.rptTitle = rptTitle;
        this.reportDescription = reportDescription;
        this.assetUsed = assetUsed;
        this.quantityAssetUsed = quantityAssetUsed; 
        this.maintenanceStatus = maintenanceStatus;
        this.reportDateTime = reportDateTime;
        this.submitterId = submitterId;
        this.submitterName = submitterName;
        this.qtySpoilt = qtySpoilt;
    } 

    public MaintenanceRequestEntity getMaintenanceRequest() {
        return maintenanceRequest;
    }

    public void setMaintenanceRequest(MaintenanceRequestEntity maintenanceRequest) {
        this.maintenanceRequest = maintenanceRequest;
    }

    public int getQtySpoilt() {
        return qtySpoilt;
    }

    public void setQtySpoilt(int qtySpoilt) {
        this.qtySpoilt = qtySpoilt;
    }
    
    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }
    
    

    public String getRptTitle() {
        return rptTitle;
    }

    public void setRptTitle(String rptTitle) {
        this.rptTitle = rptTitle;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getAssetUsed() {
        return assetUsed;
    }

    public void setAssetUsed(String assetUsed) {
        this.assetUsed = assetUsed;
    }

    public int getQuantityAssetUsed() {
        return quantityAssetUsed;
    }

    public void setQuantityAssetUsed(int quantityAssetUsed) {
        this.quantityAssetUsed = quantityAssetUsed;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }
        
    public Long getMaintenanceReportId() {
        return maintenanceReportId;
    }
        
    public void setMaintenanceReportId(Long maintenanceReportId) {
        this.maintenanceReportId = maintenanceReportId;
    }

    public Timestamp getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(Timestamp reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public AssetRequestEntity getAssetRequest() {
        return assetRequest;
    }

    public void setAssetRequest(AssetRequestEntity assetRequest) {
        this.assetRequest = assetRequest;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maintenanceReportId != null ? maintenanceReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaintenanceReportEntity)) {
            return false;
        }
        MaintenanceReportEntity other = (MaintenanceReportEntity) object;
        if ((this.maintenanceReportId == null && other.maintenanceReportId != null) || (this.maintenanceReportId != null && !this.maintenanceReportId.equals(other.maintenanceReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MaintenanceReportEntity[ maintenanceReportId=" + maintenanceReportId + " ]";
    }
    
}
