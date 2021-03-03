/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance.sessionbean;

import infraasset.entity.AssetEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.TrainCarEntity;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import maintenance.entity.MaintenanceReportEntity;
import operations.entity.MaintenanceRequestEntity;

/**
 *
 * @author FABIAN
 */
@Remote
public interface MaintenanceSessionBeanRemote {
    /*public void createInspectionRequest(String staffId, String requestDescription, String assetId, String urgnecyLvl);*/
    /*public void createInspectionRequest(String staffId, String requestDescription, String assetId, String urgnecyLvl);*/
    public List<AssetEntity> getAssetIdList(String assetType);
    public List<AssetEntity> getAllAssets();
    public AssetEntity searchAsset(String assetId);
    public MaintenanceRequestEntity searchMaintenanceRequest(String mainReqId);
    public MaintenanceReportEntity searchMaintenanceReport(Long maintenanceReportId);
    public NodeAssetEntity searchNodeAsset(String assetId);
    public RollingStockAssetEntity searchRollingStockAsset(String assetId);
    //public void deductQuantity(String asset1, String qty1);
    public void updateRequestStatus(String mainReqId, String maintenanceStatus);
    public List<MaintenanceReportEntity> getAllReports();
    public MaintenanceRequestEntity getMaintenanceRequest(Long maintenanceReportId);
    public List<MaintenanceReportEntity> getSpecificReport(Long maintenanceReportId);
    public TrainCarEntity searchTrainCar(String trainCarId);
    public boolean updateMaintenanceReport(Long maintenanceReportId, String asset1, String qty1, 
              String rptTitle, String reportDescription, String maintenanceStatus, String qtySpoilt, String assetName);
    public MaintenanceReportEntity createMaintenanceReport(String rptTitle, String reportDescription, String asset1, String qty1, String maintenanceStatus, Timestamp time, String staffId,
            String submitterName, String mainReqId, String assetDetails, String qtySpoilt);
    public boolean createMaintenanceReportTrainCar(String rptTitle, String reportDescription, String maintenanceStatus, Timestamp time, String staffId, String submitterName, String mainReqId, String assetDetails);
   /* public List<InspectionRequestEntity> getAllInspectionRequest();
    public InspectionRequestEntity getOneInspectionRequest(String inspectionRequestId);
    public boolean updateInspectionRequest(String inspectionRequestId, String assetType, String assetId, String requestDescription, String urgencyLvl);
    public boolean deleteInspectionRequest(String inspectionRequestId);*/
    public MaintenanceReportEntity searchReport(Long maintenanceReportId);
}
