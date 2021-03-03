/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.RollingStockEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import operations.entity.ServiceLogEntity;
import operations.entity.InterviewEntity;
import operations.entity.JobApplicationsEntity;
import operations.entity.JobOfferEntity;
import operations.entity.MaintenanceRequestEntity;
import operations.entity.StaffContractEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Zhirong
 */

@Local
public interface OperationsSessionBeanLocal {

    public boolean addSvcLog(String subject,String content,String staffId);
    public ArrayList<ServiceLogEntity> getSvcLogs(String staffId);
    public ArrayList<ServiceLogEntity> getSvcLogs1(String nodeCode);
    //public boolean updateSvcLog(String svcLogId, String subject, String content);
    public ServiceLogEntity searchLog (String svcLogId);
    public ArrayList<AdvertisementSpaceEntity> getAdvertisementSpace(String team);
    public ArrayList<LeasingSpaceEntity> getLeasingSpace(String team);
    public ArrayList<RollingStockAssetEntity> getRollingStockAsset(String team);
    public ArrayList<ConsumableAssetEntity> getConsumableAsset(String team);
    public ArrayList<NodeAssetEntity> getNodeAsset(String team);
    public ArrayList<JobOfferEntity> getJobOffer();
    public boolean createJob(String staffId,String jobTitle,String jobPosition,String location,String jobType,double salary,String jobDescription,String jobQualifications, Date postedDate, Date jobDeadline);
    public boolean updateJob(String jobId,String jobTitle,String jobPosition,String location,String jobType,double salary,String jobDescription,String jobQualifications, Date postedDate, Date jobDeadline);
    public JobOfferEntity searchJob(String jobId);
    public JobApplicationsEntity searchJobApp(String jobId);
    public boolean goJobStatus(String jobId) ;
    public ArrayList<JobApplicationsEntity> getJobApplication(String jobId,String emailAddress);
    public boolean updateJobApp(String applicationId, String appStatus,String emailAddress);
    public boolean createInterview(String applicationId,Date interviewFrom,Date interviewTo,String emailAddress);
    public ArrayList<JobApplicationsEntity> getJobApp();
    public ArrayList<InterviewEntity> getInterviewList();
    public boolean updateInterview(String applicationId,Date interviewFrom,Date interviewTo,String emailAddress);
    public boolean deleteInterview(String applicationId,String emailAddress);
    public InterviewEntity searchInterview(String interviewId);
    public ArrayList<AssetRequestEntity> getAssetRequest(String team);
    public ArrayList<AdvertisementSpaceEntity> getAdvertisementSpace();
    public ArrayList<AssetRequestEntity> getAssetRequests() ;
    public ArrayList<LeasingSpaceEntity> getLeasingSpace();
    public ArrayList<RollingStockAssetEntity> getRollingStockAsset() ;
    public ArrayList<ConsumableAssetEntity> getConsumableAsset() ;
    public ArrayList<NodeAssetEntity> getNodeAsset();
    public boolean submitAssetRequest(String teamId, String infraId,String assetRequestType, String assetType, String assetName, int qty, String remark);
    public boolean addConsumableAsset(String assetName, String code, int quantity, Date expiryDate, String consumableAssetType);
    public boolean addRollingStockAsset(String assetName, String infraId, int lifetimeValue, Date purchaseDate, int quantity, int storage,String rollingStockAssetType);
    public boolean addNodeAsset(String assetName, String code, int lifetimeValue, Date purchaseDate, int quantity, String nodeAssetType);
    public NodeEntity searchNode(String code);
    public RollingStockEntity searchRollingStock(String depot);
    public InfrastructureEntity searchInfra(String infraId);
    public ArrayList<RollingStockEntity> getRollingStock();
    public ArrayList<String> getRollingStockList();
    public List<MaintenanceRequestEntity> getMaintenanceRequestList();
    public List<MaintenanceRequestEntity> getMaintenanceRequestListByDepot(String staffId);
    public List<NodeEntity> getStationsInCharge(String StaffId);
    public List<MaintenanceRequestEntity> getMaintenanceRequestList1(String teamId);
    public List<MaintenanceRequestEntity> getMaintenanceRequestList2(String staffId);
    public boolean submitMaintenanceRequest(String teamId,String staffId,String assetRequestType, String infraId,String assetType, String assetName,String mainReqType, String remark, Date now);
    public boolean delRequest(String mainReqId);
    public MaintenanceRequestEntity searchMaintenaceRequest(String mainReqId);
    public boolean updateMaintenanceRequest(String mainReqId, String mainReqStatus);
    public TeamEntity searchTeam(String team);
    public boolean updateMaintenanceRequest1(String mainReqId, String mainReqType,String remark);
    public ArrayList<RollingStockEntity> getRollingStock(String teamId);
    public StationStaffEntity searchStationStaff(String staffId);
    public boolean createApplicant(String jobId, String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String highestEducation, int yearsOfExp, String position, String company, Date startDate, Date endDate, String jobIndustry, String summary);
    public boolean createContract(String appliciantId, Date startDate, Date endDate,String emailAddress);
    public StaffContractEntity searchStaffContract(String staffContractId) ;
    public StaffContractEntity getStaffContract(String appliciantId);
    public void checkJob();
}



