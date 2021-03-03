/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.sessionbean;

import businessPartner.entity.BusinessPartnerEntity;
import businessPartner.entity.NotificationEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.LeasingSpaceEntity;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Local;
import tenderModule.entity.AdvertisingContractEntity;
import tenderModule.entity.BidEntity;
import tenderModule.entity.DeliveryEntity;
import tenderModule.entity.FittingOutReqEntity;
import tenderModule.entity.LeasingContractEntity;
import tenderModule.entity.PurchaseRequestEntity;

/**
 *
 * @author kayleytan
 */
@Local
public interface TenderModuleSessionBeanLocal {

    public ArrayList<PurchaseRequestEntity> getPurchaseRequestList();

    public void generatePurchaseRequest();

    public PurchaseRequestEntity submitPurchaseRequest(String assetName, String assetType, int qty);

    public PurchaseRequestEntity searchPurchaseReq(String purchaseRequestId);

    public boolean updatePurchaseRequest(String purchaseRequestId, String description, double minBidAmount, Date reqDeadline, String emailAddress);

    ArrayList<PurchaseRequestEntity> getPurchaseRequestListBP();

    public boolean addBid(String partnerId, double currentBid, String purchaseRequestId, String remarks);

    public ArrayList<NotificationEntity> getNotifications(String partnerId);

    public BusinessPartnerEntity searchPartner(String partnerId);

    public boolean deleteBid(String bidId);

    public void checkPR();

    public void checkLeasing();
    
    public void checkAdvertising();

    public boolean goChooseBid(String bidId, String emailAddress);

    public ArrayList<BidEntity> getBidList();

    public ArrayList<BusinessPartnerEntity> getDistinctPartner(String PurchaseRequestId);

    public boolean goDeletePR(String purchaseRequestId);

    public boolean goRejectAll(String purchaseRequestId, String emailAddress);

    public ArrayList<DeliveryEntity> getDeliveryList(String partnerId);

    public ArrayList<DeliveryEntity> getDeliveryList();

    public boolean scheduleDelivery(Long deliveryId, Date date);

    public boolean completePR(String purchaseRequestId, ArrayList<String> selectedAssetRequests);

    public DeliveryEntity searchDelivery(Long deliveryId);

    public boolean requestLeasing(String assetId, int leaseYear, String partnerId);

    public ArrayList<LeasingContractEntity> getLeasingContract();

    public ArrayList<LeasingContractEntity> getLeasingRequestList(String partnerId);

    public ArrayList<BusinessPartnerEntity> getPartnerList();

    public LeasingContractEntity searchLeasingContract(String leasingContractId);

    public boolean goEditMyLeasing(String leasingContractId, int leaseYears);

    public boolean goDeleteLC(String leasingContractId);

    public ArrayList<LeasingContractEntity> getLeasingRequestList1(String partnerId);

    public ArrayList<LeasingContractEntity> getLeasingRequestList2(String partnerId);

    public ArrayList<LeasingSpaceEntity> getLeasingList();

    public boolean goChooseLeaseSpace(String leasingContractId, String emailAddress, Date startDate);

    public boolean completedSigningLeaseContract(String leasingContractId);

    public BidEntity searchBid(String bidId);

    public boolean goUpdateBid(String bidId, double currentBid, String remarks);

    public boolean markRead(ArrayList<NotificationEntity> notfications);

    public NotificationEntity searchNotification(int notficationId);

    public boolean markUnread(ArrayList<NotificationEntity> notifications);

    public boolean editSigningLeaseContract(String leasingContractId, Date signingDate, String emailAddress);

    public FittingOutReqEntity searchFittingOut(String contractId);

    public boolean updateFittingRequest(String decision, String duration, String leasingContractId);

    public boolean approveFittingOutRequest(String leasingContractId, String emailAddress);

    public boolean rejectFittingOutRequest(String leasingContractId, String remarks, String emailAddress);

    public boolean completedPreJointInspection(String leasingContractId);

    public boolean editPreJointInspection(String leasingContractId, Date signingDate, String emailAddress);

    public boolean schedulePreJointInspection(String leasingContractId, Date signingDate, String emailAddress);

    public boolean completedPostJointInspection(String leasingContractId);

    public boolean editPostJointInspection(String leasingContractId, Date signingDate, String emailAddress);

    public boolean schedulePostJointInspection(String Id, Date signingDate, String emailAddress);
    
    public void completeLeaseContract(String contractId);
    
    public boolean terminateLeasingContract(String contractId,String emailAddress);
    
    public boolean renewLeasingContract(LeasingContractEntity leasingContract, int leaseYear);
    
    public ArrayList<AdvertisingContractEntity> getAdvertisingContract();
    
    public AdvertisingContractEntity searchAdvertisingContract(String advertisingId);
    
    public boolean completedSigningAdvertContract(String advertisingId);
    
    public boolean editSigningAdvertContract(String advertisingId, Date signingDate, String emailAddress);
    
    public boolean completedInstallation(String advertisingId, String emailAddress);
    
    public boolean completedRemoval(String advertisingId, String emailAddress);
    
    public ArrayList<AdvertisementSpaceEntity> getAdvertisingList();
    
    public boolean goChooseAdvertSpace(String advertisingId, String emailAddress, Date startDate);

   
    public boolean terminateAdvertisingContract(String contractId,String emailAddress);
    
    public boolean requestAdvertising(String assetId, int leaseYear, String partnerId);
    
    public ArrayList<AdvertisingContractEntity> getAdvertisingRequestList1(String partnerId);

    public ArrayList<AdvertisingContractEntity> getAdvertisingRequestList2(String partnerId);
    
    public ArrayList<AdvertisingContractEntity> getAdvertisingRequestList(String partnerId);
    
    public boolean renewAdvertContract(AdvertisingContractEntity advertisingContract, int leaseYear);
    
     public boolean goEditMyAdvertising(String leasingContractId, int leaseYears);
     
     public boolean goDeleteAC(String advertisingId);
     
     public boolean approveLeasingContract(String contractId);
     
     public boolean rejectLeasingContract(String contractId);
     
     public boolean approveAdvertContract(String contractId);
     
     public boolean rejectAdvertContract(String contractId);
     
      public void checkAdvertisingContract();
      
      public void checkLeasingContract();
    
}
