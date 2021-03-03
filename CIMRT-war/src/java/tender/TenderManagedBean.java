/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tender;

import businessPartner.entity.BusinessPartnerEntity;
import businessPartner.sessionbean.ProfileSessionBeanLocal;
import commoninfra.sessionbean.AccountSessionBeanLocal;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.RollingStockEntity;
import static infraasset.sessionbean.InfraAssetSessionBean.addDays;
import static infraasset.sessionbean.InfraAssetSessionBean.subtractDays;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import routefare.entity.NodeEntity;
import tenderModule.entity.AdvertisingContractEntity;
import tenderModule.entity.BidEntity;
import tenderModule.entity.DeliveryEntity;
import tenderModule.entity.FittingOutReqEntity;
import tenderModule.entity.LeasingContractEntity;
import tenderModule.entity.PurchaseRequestEntity;
import tenderModule.sessionbean.TenderModuleSessionBeanLocal;

/**
 *
 * @author kayleytan
 */
@Named(value = "tenderManagedBean")
@SessionScoped
public class TenderManagedBean implements Serializable {

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    @EJB
    private TenderModuleSessionBeanLocal tenderSessionBeanLocal;

    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    @EJB
    private ProfileSessionBeanLocal profileSessionBeanLocal;

    private String staffId;
    private String role;
    private ArrayList<String> staffDetails;

    private String purchaseRequestId;
    private Date reqDate;
    private int qty;
    private Date reqDeadline;
    private String status;
    private double minBidAmount;
    private double maxBidAmount;
    private String bidId;
    private Timestamp bidDate;
    private double bidPrice;
    private String assetRequestId;
    private String assetRequestType;
    private String assetName;
    private String description;
    private Date todayDate = new Date();
    private String emailAddress;
    private String assetId;
    private int index;
    private int menu;
    private LeasingContractEntity leasingContract;
    private String leasingContractId;
    private String email;
    private String company;
    private int phoneNumber;
    private String address;
    private String companyProfile;
    private int faxNumber;
    private boolean checkReject = false;
    private boolean canEdit = false;
    private Date signingDate;
    private Date startDate;
    private StreamedContent file;
    private FittingOutReqEntity fittingOut;

    private List<PurchaseRequestEntity> purchaseRequestList;
    private List<PurchaseRequestEntity> filteredPurchaseRequestList;
    private List<BidEntity> bidList;
    private List<BidEntity> filteredBidList;
    private ArrayList<String> selectedAssetRequests;
    private List<String> assetRequestsList;
    private ArrayList<LeasingSpaceEntity> leasingList;
    private ArrayList<LeasingSpaceEntity> filteredLeasingSpace;
    private ArrayList<LeasingContractEntity> contractList;
    private ArrayList<LeasingContractEntity> filteredLeasingSpaceContract;
    private String remarks;
    private AdvertisingContractEntity advertisingContract;
    private ArrayList<AdvertisementSpaceEntity> advertisingList;
    private ArrayList<AdvertisingContractEntity> advertContractList;
    private ArrayList<AdvertisingContractEntity> filteredAdvertisingContract;
    private String advertisingId;
   

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    
    
    
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ArrayList<String> getSelectedAssetRequests() {
        return selectedAssetRequests;
    }

    public void setSelectedAssetRequests(ArrayList<String> selectedAssetRequests) {
        this.selectedAssetRequests = selectedAssetRequests;
    }

    public List<String> getAssetRequestsList() {
        return assetRequestsList;
    }

    public void setAssetRequestsList(List<String> assetRequestsList) {
        this.assetRequestsList = assetRequestsList;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(String purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<String> getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(ArrayList<String> staffDetails) {
        this.staffDetails = staffDetails;
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Date getReqDeadline() {
        return reqDeadline;
    }

    public void setReqDeadline(Date reqDeadline) {
        this.reqDeadline = reqDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMinBidAmount() {
        return minBidAmount;
    }

    public void setMinBidAmount(double minBidAmount) {
        this.minBidAmount = minBidAmount;
    }

    public double getMaxBidAmount() {
        return maxBidAmount;
    }

    public void setMaxBidAmount(double maxBidAmount) {
        this.maxBidAmount = maxBidAmount;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public Timestamp getBidDate() {
        return bidDate;
    }

    public void setBidDate(Timestamp bidDate) {
        this.bidDate = bidDate;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getAssetRequestId() {
        return assetRequestId;
    }

    public void setAssetRequestId(String assetRequestId) {
        this.assetRequestId = assetRequestId;
    }

    public String getAssetRequestType() {
        return assetRequestType;
    }

    public void setAssetRequestType(String assetRequestType) {
        this.assetRequestType = assetRequestType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PurchaseRequestEntity> getPurchaseRequestList() {
        return purchaseRequestList;
    }

    public void setPurchaseRequestList(List<PurchaseRequestEntity> purchaseRequestList) {
        this.purchaseRequestList = purchaseRequestList;
    }

    public List<BidEntity> getBidList() {
        return bidList;
    }

    public void setBidList(List<BidEntity> bidList) {
        this.bidList = bidList;
    }

    public ArrayList<LeasingSpaceEntity> getLeasingList() {
        init();
        leasingList = tenderSessionBeanLocal.getLeasingList();
        return leasingList;
    }

    public void setLeasingList(ArrayList<LeasingSpaceEntity> leasingList) {
        this.leasingList = leasingList;
    }

    public ArrayList<AdvertisementSpaceEntity> getAdvertisingList() {
        init();
        advertisingList = tenderSessionBeanLocal.getAdvertisingList();
        return advertisingList;
    }

    public void setAdvertisingList(ArrayList<AdvertisementSpaceEntity> advertisingList) {
        this.advertisingList = advertisingList;
    }
   
    public ArrayList<LeasingContractEntity> getContractList() {
        return contractList;
    }

    public void setContractList(ArrayList<LeasingContractEntity> contractList) {
        this.contractList = contractList;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LeasingContractEntity getLeasingContract() {
        return leasingContract;
    }

    public void setLeasingContract(LeasingContractEntity leasingContract) {
        this.leasingContract = leasingContract;
    }

    public String getLeasingContractId() {
        return leasingContractId;
    }

    public void setLeasingContractId(String leasingContractId) {
        this.leasingContractId = leasingContractId;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public int getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(int faxNumber) {
        this.faxNumber = faxNumber;
    }

    public boolean isCheckReject() {
        return checkReject;
    }

    public void setCheckReject(boolean checkReject) {
        this.checkReject = checkReject;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Date getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(Date signingDate) {
        this.signingDate = signingDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public FittingOutReqEntity getFittingOut() {
        return fittingOut;
    }

    public void setFittingOut(FittingOutReqEntity fittingOut) {
        this.fittingOut = fittingOut;
    }

    public AdvertisingContractEntity getAdvertisingContract() {
        return advertisingContract;
    }

    public void setAdvertisingContract(AdvertisingContractEntity advertisingContract) {
        this.advertisingContract = advertisingContract;
    }

    public ArrayList<AdvertisingContractEntity> getAdvertContractList() {
        return advertContractList;
    }

    public void setAdvertContractList(ArrayList<AdvertisingContractEntity> advertContractList) {
        this.advertContractList = advertContractList;
    }
    
    

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            role = staffDetails.get(14);
            emailAddress = staffDetails.get(4);
        }

        tenderSessionBeanLocal.generatePurchaseRequest();
        tenderSessionBeanLocal.checkPR();
        tenderSessionBeanLocal.checkLeasing();
        tenderSessionBeanLocal.checkAdvertising();
        tenderSessionBeanLocal.checkAdvertisingContract();
        tenderSessionBeanLocal.checkLeasingContract();

        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/tenancyAgreement.pdf");
        file = new DefaultStreamedContent(stream, "application/pdf", "tenancyAgreement.pdf");

    }

    public String goPostPR(String purchaseReqId) {
        this.purchaseRequestId = purchaseReqId;
        PurchaseRequestEntity temp = (PurchaseRequestEntity) tenderSessionBeanLocal.searchPurchaseReq(purchaseReqId);
        this.qty = temp.getQty();
        this.assetRequestType = temp.getCategory();
        this.assetName = temp.getItemName();
        return "updatePurchaseRequest";
    }

    public List<PurchaseRequestEntity> getPRList() {
        init();
        List<PurchaseRequestEntity> purchaseRequestList1 = new ArrayList<PurchaseRequestEntity>();
        purchaseRequestList1 = tenderSessionBeanLocal.getPurchaseRequestList();
        return purchaseRequestList1;
    }

    public String detail(String assetRequestId) {
        AssetRequestEntity a = infraAssetSessionBeanLocal.searchAssetReq(assetRequestId);
        String id = a.getAsset().getInfrastructure().getInfraId();
        RollingStockEntity rollingStock = infraAssetSessionBeanLocal.searchRollingStock(id);
        String codee = rollingStock.getDepot().getCode();
        codee = codee + "(" + rollingStock.getInfraId() + ")";
        return codee;
    }

    public String detail1(String assetRequestId) {
        AssetRequestEntity a = infraAssetSessionBeanLocal.searchAssetReq(assetRequestId);
        String id = a.getAsset().getInfrastructure().getInfraId();
        NodeEntity node = infraAssetSessionBeanLocal.searchNode1(id);
        String codee = node.getCode();
        return codee;
    }

    public String goUpdatePR(String purchaseReqId) {
        this.purchaseRequestId = purchaseReqId;
        PurchaseRequestEntity temp = (PurchaseRequestEntity) tenderSessionBeanLocal.searchPurchaseReq(purchaseReqId);
        this.qty = temp.getQty();
        this.assetRequestType = temp.getCategory();
        this.assetName = temp.getItemName();
        ArrayList<String> newlist = new ArrayList<String>();
        ArrayList<AssetRequestEntity> list = new ArrayList<AssetRequestEntity>(temp.getAssetRequests());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus().equals("Completed")) {
                //Dont add into the list
            } else {
                if (list.get(i).getAsset().getAssetId().substring(0, 3).equals("RSA")) {
                    String code = detail(list.get(i).getAssetRequestId());
                    newlist.add(list.get(i).getAssetRequestId() + " " + code);
                } else {
                    String code = detail1(list.get(i).getAssetRequestId());
                    newlist.add(list.get(i).getAssetRequestId() + " " + code);
                }
            }
        }
        this.assetRequestsList = newlist;
        return "updatePurchaseRequest1";
    }

    public String deliveryLocation(String purchaseRequestId) {
        String details = "";
        PurchaseRequestEntity temp = (PurchaseRequestEntity) tenderSessionBeanLocal.searchPurchaseReq(purchaseRequestId);
        ArrayList<AssetRequestEntity> requestList = new ArrayList<AssetRequestEntity>(temp.getAssetRequests());
        for (int i = 0; i < requestList.size(); i++) {
            AssetRequestEntity a = requestList.get(i);
            if (a.getAsset().getAssetId().substring(0, 3).equals("RSA")) {
                String infraId = a.getAsset().getInfrastructure().getInfraId();
                RollingStockEntity rs = (RollingStockEntity) infraAssetSessionBeanLocal.searchInfra(infraId);
                String code = rs.getDepot().getCode();
                details = details + " " + code + "(" + rs.getInfraId() + ")" + ": " + a.getQty() + "qty";
            } else {
                String infraId = a.getAsset().getInfrastructure().getInfraId();
                NodeEntity node = infraAssetSessionBeanLocal.searchNode1(infraId);
                details = details + " " + node.getCode() + ": " + a.getQty() + "qty";
            }

        }
        return details;
    }

    public String updatePurchaseRequest() {
        boolean status1 = tenderSessionBeanLocal.updatePurchaseRequest(purchaseRequestId, description, maxBidAmount, reqDeadline, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Purchase request is updated successfully",
                            ""));
            this.purchaseRequestId = null;
            this.qty = 0;
            this.assetRequestType = null;
            this.assetName = null;
            this.description = null;
            this.maxBidAmount = 0;
            this.reqDeadline = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the purchase request!"));
            return "viewPurchaseRequest";
        }
        return "viewPurchaseRequest";
    }

    public String goViewBids(String purchaseReqId) {
        checkReject = false;
        this.purchaseRequestId = purchaseReqId;
        PurchaseRequestEntity temp = tenderSessionBeanLocal.searchPurchaseReq(purchaseReqId);

        ArrayList<BidEntity> bidlist = tenderSessionBeanLocal.getBidList();
        ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();

        if (!(bidlist.isEmpty())) {
            //Get the bid list for the purchase request
            for (int j = 0; j < bidlist.size(); j++) {
                BidEntity bid = bidlist.get(j);
                if (bid.getPurchaseRequest().getPurchaseRequestId().equals(temp.getPurchaseRequestId()) && (!(bid.getStatus().equals("Withdrawn")))) {
                    newbidlist.add(bid);
                }
                if (bid.getStatus().equals("Pending")) {
                    checkReject = true;
                }
            }
        }

        this.bidList = newbidlist;
        return "viewBids";
    }

    public String goChooseBid(String bidId) {
        boolean status1 = tenderSessionBeanLocal.goChooseBid(bidId, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "The winner for the particular request is selected",
                            ""));
            goViewBids(purchaseRequestId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to select the winner!"));
            return "viewBids";
        }
        return "viewBids";
    }

    public String goRejectAll() {
        boolean status1 = tenderSessionBeanLocal.goRejectAll(purchaseRequestId, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "All bidders are rejected!",
                            ""));
            goViewBids(purchaseRequestId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to reject all!"));
            return "viewBids";
        }
        return "viewBids";
    }

    public String godeletePR(String prID) {
        boolean status1 = tenderSessionBeanLocal.goDeletePR(prID);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Purchase Request is deleted successfully",
                            ""));
            tenderSessionBeanLocal.checkPR();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to delete purchase request!"));
            return "viewPurchaseRequest";
        }
        return "viewPurchaseRequest";
    }

    public String completePurchaseRequest() {
        boolean status1 = tenderSessionBeanLocal.completePR(purchaseRequestId, selectedAssetRequests);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Purchase Request is updated successfully",
                            ""));
            tenderSessionBeanLocal.checkPR();
            this.selectedAssetRequests = null;
            this.purchaseRequestId = null;

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update purchase request!"));
            return "viewPurchaseRequest";
        }
        return "viewPurchaseRequest";
    }

    public String getDelivery(String purchaseRequestId) {
        PurchaseRequestEntity temp = tenderSessionBeanLocal.searchPurchaseReq(purchaseRequestId);
        String bid = "";
        if (temp.getStatus().equals("Delivery Scheduled") || temp.getStatus().equals("Completed")) {
            ArrayList<BidEntity> bids = tenderSessionBeanLocal.getBidList();
            for (int i = 0; i < bids.size(); i++) {
                if (bids.get(i).getStatus().equals("Successful") && bids.get(i).getPurchaseRequest().getPurchaseRequestId().equals(purchaseRequestId)) {
                    bid = bids.get(i).getBidId();
                }
            }
            ArrayList<DeliveryEntity> deliveryList = tenderSessionBeanLocal.getDeliveryList();
            for (int i = 0; i < deliveryList.size(); i++) {
                if (deliveryList.get(i).getBid().getBidId().equals(bid)) {
                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    String datee = DATE_FORMAT.format(deliveryList.get(i).getDateAndTime());
                    return datee;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    public String getDelivery1(String assetReqId, String purchaseRequestId) {
        AssetRequestEntity ar = infraAssetSessionBeanLocal.searchAssetReq(assetReqId);
        if (ar.getPurchaseRequest() != null) {
            PurchaseRequestEntity temp = tenderSessionBeanLocal.searchPurchaseReq(purchaseRequestId);
            String bid = "";
            if (temp.getStatus().equals("Delivery Scheduled") || temp.getStatus().equals("Completed")) {
                ArrayList<BidEntity> bids = tenderSessionBeanLocal.getBidList();
                for (int i = 0; i < bids.size(); i++) {
                    if (bids.get(i).getStatus().equals("Successful") && bids.get(i).getPurchaseRequest().getPurchaseRequestId().equals(purchaseRequestId)) {
                        bid = bids.get(i).getBidId();
                    }
                }
                ArrayList<DeliveryEntity> deliveryList = tenderSessionBeanLocal.getDeliveryList();
                for (int i = 0; i < deliveryList.size(); i++) {
                    if (deliveryList.get(i).getBid().getBidId().equals(bid)) {
                        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        String datee = DATE_FORMAT.format(deliveryList.get(i).getDateAndTime());
                        return datee;
                    }
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public String goViewLeaseSubmitter(String assetId) {
        this.assetId = assetId;
        contractList = new ArrayList<LeasingContractEntity>();
        ArrayList<LeasingContractEntity> list = tenderSessionBeanLocal.getLeasingContract();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLeasingSpace().getAssetId().equals(assetId) && !list.get(i).getStatus().equals("Need Approval")) {
                contractList.add(list.get(i));
            }
        }
        return "viewLeaseSpaceRequest";
    }

    public List<PurchaseRequestEntity> getFilteredPurchaseRequestList() {
        return filteredPurchaseRequestList;
    }

    public void setFilteredPurchaseRequestList(List<PurchaseRequestEntity> filteredPurchaseRequestList) {
        this.filteredPurchaseRequestList = filteredPurchaseRequestList;
    }

    public List<BidEntity> getFilteredBidList() {
        return filteredBidList;
    }

    public void setFilteredBidList(List<BidEntity> filteredBidList) {
        this.filteredBidList = filteredBidList;
    }

    public String goChooseLeaseSpace1(String leasingContractId) {
        this.leasingContractId = leasingContractId;
        return "scheduleContract";
    }

    public String goChooseLeaseSpace(String leasingContractId) {
        boolean status1 = tenderSessionBeanLocal.goChooseLeaseSpace(leasingContractId, emailAddress, startDate);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "The winner for the particular lease space is selected",
                            ""));
            this.startDate = null;
            goViewLeaseSubmitter(assetId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to select the winner!"));
            return "viewLeaseSpaceRequest";
        }
        return "viewLeaseSpaceRequest";
    }

    //View from the list of leasing space
    public String goViewLeaseContract(String assetId) {
        ArrayList<LeasingContractEntity> contractlist = tenderSessionBeanLocal.getLeasingContract();
        for (int i = 0; i < contractlist.size(); i++) {
            if (contractlist.get(i).getLeasingSpace().getAssetId().equals(assetId) && (contractlist.get(i).getStatus().equals("On Contract"))) {
                leasingContract = contractlist.get(i);
            }
        }

        fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        //Check is at which step
        //First step is siging of tenancy agreement

        if (fittingOut != null) {
            if (leasingContract.isSigningOfAgreement() == false) {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            } else if ((leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest() == null) || (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && !(fittingOut.getStatus().equals("Approved")))) {
                this.menu = 1;
                this.index = 1;
                this.canEdit = false;
            } else if (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && fittingOut.getStatus().equals("Approved") && (leasingContract.getJointInspection() == null || (leasingContract.getJointInspection().getEntryStatus().equals("Scheduled")))) {
                this.menu = 2;
                this.index = 2;
            } else if (leasingContract.getJointInspection().getEntryStatus().equals("Completed") && !leasingContract.getStatus().equals("End of Contract") && (leasingContract.getJointInspection().getExitInspection() == null || leasingContract.getJointInspection().getExitStatus().equals("Scheduled") || leasingContract.getJointInspection().getExitStatus().equals("Completed"))) {
                this.menu = 3;
                this.index = 3;
                this.canEdit = false;
            } else if ((contractEnd(leasingContract.getLeasingContractId()) == true  || leasingContract.getStatus().equals("End of Contract")) && leasingContract.getJointInspection().getExitStatus().equals("Completed")) {
                this.menu = 4;
                this.index = 4;
                this.canEdit = false;
            } else {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            }
        } else {
            if (leasingContract.isSigningOfAgreement() == false) {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            } else if ((leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest() == null && leasingContract.getJointInspection() == null)) {
                this.menu = 1;
                this.index = 1;
                this.canEdit = false;
            } else if (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && (leasingContract.getJointInspection() == null || leasingContract.getJointInspection().getEntryStatus().equals("Scheduled"))) {
                this.menu = 2;
                this.index = 2;
            } else if (leasingContract.getJointInspection().getEntryStatus().equals("Completed") && !leasingContract.getStatus().equals("End of Contract") && (leasingContract.getJointInspection().getExitInspection() == null || leasingContract.getJointInspection().getExitStatus().equals("Scheduled") || leasingContract.getJointInspection().getExitStatus().equals("Completed"))) {
                this.menu = 3;
                this.index = 3;
                this.canEdit = false;
            } else if ((contractEnd(leasingContract.getLeasingContractId()) == true || leasingContract.getStatus().equals("End of Contract")) && leasingContract.getJointInspection().getExitStatus().equals("Completed")) {
                this.menu = 4;
                this.index = 4;
                this.canEdit = false;
            } else {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            }
        }
        return "viewLeaseContract";
    }
    
    //View from the list of leasing request 
    public String goViewLeaseContract1(String contractId) {
        LeasingContractEntity contract = tenderSessionBeanLocal.searchLeasingContract(contractId);
        leasingContract = contract;

        fittingOut = tenderSessionBeanLocal.searchFittingOut(contractId);

         if (fittingOut != null) {
            if (leasingContract.isSigningOfAgreement() == false) {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            } else if ((leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest() == null) || (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && !(fittingOut.getStatus().equals("Approved")))) {
                this.menu = 1;
                this.index = 1;
                this.canEdit = false;
            } else if (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && fittingOut.getStatus().equals("Approved") && (leasingContract.getJointInspection() == null || (leasingContract.getJointInspection().getEntryStatus().equals("Scheduled")))) {
                this.menu = 2;
                this.index = 2;
                 this.canEdit = false;
            } else if (leasingContract.getJointInspection().getEntryStatus().equals("Completed") && !leasingContract.getStatus().equals("End of Contract") && (leasingContract.getJointInspection().getExitInspection() == null || leasingContract.getJointInspection().getExitStatus().equals("Scheduled") || leasingContract.getJointInspection().getExitStatus().equals("Completed"))) {
                this.menu = 3;
                this.index = 3;
                this.canEdit = false;
            } else if ((contractEnd(leasingContract.getLeasingContractId()) == true || leasingContract.getStatus().equals("End of Contract")) && leasingContract.getJointInspection().getExitStatus().equals("Completed")) {
                this.menu = 4;
                this.index = 4;
                this.canEdit = false;
            } else {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            }
        } else {
            if (leasingContract.isSigningOfAgreement() == false) {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            } else if ((leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest() == null && leasingContract.getJointInspection() == null)) {
                this.menu = 1;
                this.index = 1;
                this.canEdit = false;
            } else if (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && (leasingContract.getJointInspection() == null || leasingContract.getJointInspection().getEntryStatus().equals("Scheduled"))) {
                this.menu = 2;
                this.index = 2;
                 this.canEdit = false;
            } else if (leasingContract.getJointInspection().getEntryStatus().equals("Completed") && !leasingContract.getStatus().equals("End of Contract") && (leasingContract.getJointInspection().getExitInspection() == null || leasingContract.getJointInspection().getExitStatus().equals("Scheduled") || leasingContract.getJointInspection().getExitStatus().equals("Completed"))) {
                this.menu = 3;
                this.index = 3;
                this.canEdit = false;
            } else if ((contractEnd(leasingContract.getLeasingContractId()) == true || leasingContract.getStatus().equals("End of Contract") ) && leasingContract.getJointInspection().getExitStatus().equals("Completed")) {
                this.menu = 4;
                this.index = 4;
                this.canEdit = false;
            } else {
                this.canEdit = false;
                this.menu = 0;
                this.index = 0;
            }
        }
        return "viewLeaseContract";
    }
    
    public boolean contractEnd (String contractId) {
         LeasingContractEntity contract = tenderSessionBeanLocal.searchLeasingContract(contractId);
         Date contractEnd = contract.getEndDate();
            if (todayDate.compareTo(contractEnd) > 0) {
                tenderSessionBeanLocal.completeLeaseContract(contractId);
                return true;
            } else {
                return false;  
            }
    }

    public String completedSigningLeaseContract(String Id) {
        boolean status1 = tenderSessionBeanLocal.completedSigningLeaseContract(Id);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Signing of Tenancy Agreement is completed",
                            ""));
            this.menu = 1;
            this.index = 1;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the status of contract!"));
            return "viewLeaseContract";
        }
        return "viewLeaseContract";
    }

    public String goIndex(int number) {
        this.canEdit = false;
        this.index = number;
        return "viewLeaseContract.xhtml?faces-redirect=true";
    }
    
    public String goIndex1(int number) {
        this.canEdit = false;
        this.index = number;
        return "viewAdvertContract.xhtml?faces-redirect=true";
    }

    public String editSigningLeaseContract() {
        this.canEdit = true;
        return "viewLeaseContract.xhtml?faces-redirect=true";
    }

    public String editSigningLC(String Id) {
        boolean status1 = tenderSessionBeanLocal.editSigningLeaseContract(Id, signingDate, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Contract Date is updated successfully",
                            ""));
            this.canEdit = false;
            this.signingDate = null;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the date of contract!"));
            return "viewLeaseContract.xhtml";
        }
        return "viewLeaseContract.xhtml";
    }

    public String goViewBusinessPartner(String bidId) {
        this.bidId = bidId;
        BidEntity bid = tenderSessionBeanLocal.searchBid(bidId);
        BusinessPartnerEntity partner = profileSessionBeanLocal.searchPartner(bid.getBusinessPartner().getPartnerId());
        email = partner.getEmailAddress();
        company = partner.getCompany();
        phoneNumber = partner.getPhoneNumber();
        address = partner.getAddress();
        faxNumber = partner.getFaxNumber();
        companyProfile = partner.getCompanyProfile();
        return "viewPartnerProfile";
    }

    public String goViewBusinessPartner1(BusinessPartnerEntity partner) {
        email = partner.getEmailAddress();
        company = partner.getCompany();
        phoneNumber = partner.getPhoneNumber();
        address = partner.getAddress();
        faxNumber = partner.getFaxNumber();
        companyProfile = partner.getCompanyProfile();
        return "viewPartnerProfile";
    }

    public Date calculateDate() {
        Date today = new Date();
        Date newDate = addDays(today, 90);
        return newDate;
    }

    public String approveFittingOutRequest(String Id) {
        boolean status1 = tenderSessionBeanLocal.approveFittingOutRequest(Id, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Fitting Out Request is approved",
                            ""));
            this.menu = 2;
            this.index = 2;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the status of fitting out request!"));
            return "viewLeaseContract";
        }
        return "viewLeaseContract";
    }

    public String rejectFittingOutRequest() {
        this.canEdit = true;
        return "viewLeaseContract.xhtml?faces-redirect=true";
    }

    public String rejectFitting(String Id) {
        boolean status1 = tenderSessionBeanLocal.rejectFittingOutRequest(Id, remarks, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Fitting out request is rejected successfully",
                            ""));
            this.canEdit = false;
            this.remarks = null;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to reject fitting out request!"));
            return "viewLeaseContract.xhtml";
        }
        return "viewLeaseContract.xhtml";
    }

    public String completedPreJointInspection(String Id) {
        boolean status1 = tenderSessionBeanLocal.completedPreJointInspection(Id);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Pre Joint Inspection is completed",
                            ""));
            this.menu = 3;
            this.index = 3;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the status of Pre Joint Inspection!"));
            return "viewLeaseContract";
        }
        return "viewLeaseContract";
    }

    public String editPreJointInspection() {
        this.canEdit = true;
        return "viewLeaseContract.xhtml?faces-redirect=true";
    }

    public String editPreJointInspection(String Id) {
        boolean status1 = tenderSessionBeanLocal.editPreJointInspection(Id, signingDate, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Pre Joint Inspection Date is updated successfully",
                            ""));
            this.canEdit = false;
            this.signingDate = null;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the date of Pre Inspection Date!"));
            return "viewLeaseContract.xhtml";
        }
        return "viewLeaseContract.xhtml";
    }

    public Date checkMin(String Id) {
        leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
        Date start = leasingContract.getStartDate();
        Date newDate = subtractDays(start, 7);
        return newDate;
    }

    public Date checkMin1(String Id) {
        leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
        Date end = leasingContract.getEndDate();
        Date newDate = subtractDays(end, 7);
        return newDate;
    }

    public Date checkMax(String Id) {
        leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
        Date start = leasingContract.getStartDate();
        Date newDate = subtractDays(start, 1);
        return newDate;
    }

    public String schedulePreJointInspection(String Id) {
        boolean status1 = tenderSessionBeanLocal.schedulePreJointInspection(Id, signingDate, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Pre Joint Inspection Date is scheduled successfully",
                            ""));
            this.canEdit = false;
            this.signingDate = null;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to scheduled the date of Pre Inspection Date!"));
            return "viewLeaseContract.xhtml";
        }
        return "viewLeaseContract.xhtml";
    }

    public String schedulePostJointInspection(String Id) {
        boolean status1 = tenderSessionBeanLocal.schedulePostJointInspection(Id, signingDate, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Post Joint Inspection Date is scheduled successfully",
                            ""));
            this.canEdit = false;
            this.signingDate = null;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to scheduled the date of Post Inspection Date!"));
            return "viewLeaseContract.xhtml";
        }
        return "viewLeaseContract.xhtml";
    }

    public String completedPostJointInspection(String Id) {
        boolean status1 = tenderSessionBeanLocal.completedPostJointInspection(Id);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Post Joint Inspection is completed",
                            ""));
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the status of Post Joint Inspection!"));
            return "viewLeaseContract";
        }
        return "viewLeaseContract";
    }

    public String editPostJointInspection() {
        this.canEdit = true;
        return "viewLeaseContract.xhtml?faces-redirect=true";
    }

    public String editPostJointInspection(String Id) {
        boolean status1 = tenderSessionBeanLocal.editPostJointInspection(Id, signingDate, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Post Joint Inspection Date is updated successfully",
                            ""));
            this.canEdit = false;
            this.signingDate = null;
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(Id);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the date of Post Inspection Date!"));
            return "viewLeaseContract.xhtml";
        }
        return "viewLeaseContract.xhtml";
    }
    
    
    public String  goTerminateLeasingContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.terminateLeasingContract(contractId,emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Contract is terminated successfully",
                            ""));
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(contractId);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to terminate the contract!"));
            return "viewAllLeaseContract";
        }
        return "viewAllLeaseContract";
    }
    
  public ArrayList<LeasingContractEntity> getAllLeasingContract(){
      ArrayList<LeasingContractEntity> leasingContractList = tenderSessionBeanLocal.getLeasingContract();
      ArrayList<LeasingContractEntity> newList = new ArrayList<LeasingContractEntity>();
      for(int i=0; i<leasingContractList.size(); i++){
          if(leasingContractList.get(i).getStatus().equals("On Contract")) {
              newList.add(leasingContractList.get(i));
          }
      }
      return newList;
  }
  
  public String  goEndContract(String contractId) {
        tenderSessionBeanLocal.completeLeaseContract(contractId);
         boolean status1 = true;
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "End of the contract",
                            ""));
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(contractId);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to end the contract!"));
            return "viewLeaseSpaceRequest";
        }
        return "viewLeaseSpaceRequest";
    }
  
  public boolean checkSubmitter(String assetId){
      ArrayList<LeasingContractEntity> contractlist = tenderSessionBeanLocal.getLeasingContract();
        for (int i = 0; i < contractlist.size(); i++) {
            if (contractlist.get(i).getLeasingSpace().getAssetId().equals(assetId) && (contractlist.get(i).getStatus().equals("Submitted"))) {
                return true;
            }
        }
        return false;
  }
  
  public boolean checkContract(String assetId){
      ArrayList<LeasingContractEntity> contractlist = tenderSessionBeanLocal.getLeasingContract();
        for (int i = 0; i < contractlist.size(); i++) {
            if (contractlist.get(i).getLeasingSpace().getAssetId().equals(assetId) && (contractlist.get(i).getStatus().equals("On Contract"))) {
                return true;
            }
        }
        return false;
  }
  
  public boolean checkAdvertContract(String assetId){
      ArrayList<AdvertisingContractEntity> contractlist = tenderSessionBeanLocal.getAdvertisingContract();
        for (int i = 0; i < contractlist.size(); i++) {
            if (contractlist.get(i).getAdvertisement().getAssetId().equals(assetId) && (contractlist.get(i).getStatus().equals("On Contract"))) {
                return true;
            }
        }
        return false;
  }
  
  public boolean checkAdverSubmitter(String assetId){
        ArrayList<AdvertisingContractEntity> contractlist = tenderSessionBeanLocal.getAdvertisingContract();
        for (int i = 0; i < contractlist.size(); i++) {
            if (contractlist.get(i).getAdvertisement().getAssetId().equals(assetId) && (contractlist.get(i).getStatus().equals("Submitted"))) {
                return true;
            }
        }
        return false;
  }
  
    public String goViewAdvertContract(String assetId) {
        ArrayList<AdvertisingContractEntity> contractlist = tenderSessionBeanLocal.getAdvertisingContract();
        for (int i = 0; i < contractlist.size(); i++) {
            if (contractlist.get(i).getAdvertisement().getAssetId().equals(assetId) && (contractlist.get(i).getStatus().equals("On Contract"))) {
                advertisingContract = contractlist.get(i);
            }
        }
        if (advertisingContract.isSigningOfAgreement() == false) {
            this.canEdit = false;
            this.menu = 0;
            this.index = 0;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == false) {
            this.menu = 1;
            this.index = 1;
            this.canEdit = false;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == true & advertisingContract.isRemoval() == false) {
            this.menu = 2;
            this.index = 2;
            this.canEdit = false;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == true & advertisingContract.isRemoval() == true) {
            this.menu = 3;
            this.index = 3;
            this.canEdit = false;
        } 
        return "viewAdvertContract";
    }
    
    //View from the list of advertising request 
    public String goViewAdvertContract1(String contractId) {
        AdvertisingContractEntity contract = tenderSessionBeanLocal.searchAdvertisingContract(contractId);
        advertisingContract = contract;
        if (advertisingContract.isSigningOfAgreement() == false) {
            this.canEdit = false;
            this.menu = 0;
            this.index = 0;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == false) {
            this.menu = 1;
            this.index = 1;
            this.canEdit = false;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == true & advertisingContract.isRemoval() == false) {
            this.menu = 2;
            this.index = 2;
            this.canEdit = false;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == true & advertisingContract.isRemoval() == true) {
            this.menu = 3;
            this.index = 3;
            this.canEdit = false;
        }
        return "viewAdvertContract";
    }

    
    public String completedSigningAdvertContract(String Id) {
        boolean status1 = tenderSessionBeanLocal.completedSigningAdvertContract(Id);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Signing of Tenancy Agreement is completed",
                            ""));
            this.menu = 1;
            this.index = 1;
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(Id);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the status of contract!"));
            return "viewAdvertContract";
        }
        return "viewAdvertContract";
    }

    public String editSigningAdvertContract() {
        this.canEdit = true;
        return "viewAdvertContract.xhtml?faces-redirect=true";
    }

    public String editSigningAC(String Id) {
        boolean status1 = tenderSessionBeanLocal.editSigningAdvertContract(Id, signingDate, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Contract Date is updated successfully",
                            ""));
            this.canEdit = false;
            this.signingDate = null;
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(Id);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the date of contract!"));
            return "viewAdvertContract.xhtml";
        }
        return "viewAdvertContract.xhtml";
    }
    
    public String completedInstallation(String Id) {
        boolean status1 = tenderSessionBeanLocal.completedInstallation(Id, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Installation of Advertisement is completed",
                            ""));
            this.menu = 2;
            this.index = 2;
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(Id);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the status of installation!"));
            return "viewAdvertContract";
        }
        return "viewAdvertContract";
    }
    
    public String completedRemoval(String Id) {
        boolean status1 = tenderSessionBeanLocal.completedRemoval(Id, emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Removal of Advertisement is completed",
                            ""));
            this.menu = 3;
            this.index = 3;
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(Id);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the status of removal!"));
            return "viewAdvertContract";
        }
        return "viewAdvertContract";
    }
    
     public String goViewAdvertSubmitter(String assetId) {
        this.assetId = assetId;
        advertContractList = new ArrayList<AdvertisingContractEntity>();
        ArrayList<AdvertisingContractEntity> list = tenderSessionBeanLocal.getAdvertisingContract();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAdvertisement().getAssetId().equals(assetId) && !list.get(i).getStatus().equals("Need Approval")) {
                advertContractList.add(list.get(i));
            }
        }
        return "viewAdvertSubmitter";
    }
    
     public String goChooseAdvertSpace1(String advertisingId) {
        this.advertisingId = advertisingId;
        return "scheduleContract1";
    }

    public String goChooseAdvertSpace(String advertisingId) {
        boolean status1 = tenderSessionBeanLocal.goChooseAdvertSpace(advertisingId, emailAddress, startDate);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "The winner for the particular advert space is selected",
                            ""));
            this.startDate = null;
            goViewAdvertSubmitter(assetId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to select the winner!"));
            return "viewAdvertSubmitter";
        }
        return "viewAdvertSubmitter";
    }

   
    
    public ArrayList<AdvertisingContractEntity> getAllAdvertisingContract(){
      ArrayList<AdvertisingContractEntity> list = tenderSessionBeanLocal.getAdvertisingContract();
      ArrayList<AdvertisingContractEntity> newList = new ArrayList<AdvertisingContractEntity>();
      for(int i=0; i<list.size(); i++){
          if(list.get(i).getStatus().equals("On Contract")) {
              newList.add(list.get(i));
          }
      }
      return newList;
  }
    
      public String  goTerminateAdvertisingContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.terminateAdvertisingContract(contractId,emailAddress);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Contract is terminated successfully",
                            ""));
           advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(advertisingId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to terminate the contract!"));
            return "viewAllAdvertContract";
        }
        return "viewAlladvertContract";
    }
    
      //Need approval for lease contract (renew)
      public ArrayList<LeasingContractEntity> getApproveLeaseRequest() {
        ArrayList<LeasingContractEntity> list = tenderSessionBeanLocal.getLeasingContract();
         ArrayList<LeasingContractEntity> newlist = new ArrayList<LeasingContractEntity>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus().equals("Need Approval")) {
                newlist.add(list.get(i));
            }
        }
        return newlist;
    }
     
      //Need approval for adver contract (renew)
      public ArrayList<AdvertisingContractEntity> getApproveAdvertRequest() {
        ArrayList<AdvertisingContractEntity> list = tenderSessionBeanLocal.getAdvertisingContract();
         ArrayList<AdvertisingContractEntity> newlist = new ArrayList<AdvertisingContractEntity>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus().equals("Need Approval")) {
                newlist.add(list.get(i));
            }
        }
        return newlist;
    }
      
     
      
      
      public String  goApproveLeaseContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.approveLeasingContract(contractId);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Renewal of Contract is approved successfully",
                            ""));
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(contractId);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to renew the contract!"));
            return "viewApproveLeaseContract";
        }
        return "viewApproveLeaseContract";
    }
      
      public String  goRejectLeaseContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.rejectLeasingContract(contractId);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Renewal of Contract is rejected successfully",
                            ""));
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(contractId);
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to reject renewal of the contract!"));
            return "viewApproveLeaseContract";
        }
        return "viewApproveLeaseContract";
    }
      
       
      public String  goApproveAdvertContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.approveAdvertContract(contractId);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Renewal of Contract is approved successfully",
                            ""));
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(advertisingId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to renew the contract!"));
            return "viewApproveAdvertContract";
        }
        return "viewApproveAdvertContract";
    }
      
      public String  goRejectAdvertContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.rejectAdvertContract(contractId);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Renewal of Contract is rejected successfully",
                            ""));
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(advertisingId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to reject renewal of the contract!"));
            return "viewAllLeaseContract";
        }
        return "viewAllLeaseContract";
    }

    public ArrayList<AdvertisingContractEntity> getFilteredAdvertisingContract() {
        return filteredAdvertisingContract;
    }

    public void setFilteredAdvertisingContract(ArrayList<AdvertisingContractEntity> filteredAdvertisingContract) {
        this.filteredAdvertisingContract = filteredAdvertisingContract;
    }

    public ArrayList<LeasingSpaceEntity> getFilteredLeasingSpace() {
        return filteredLeasingSpace;
    }

    public void setFilteredLeasingSpace(ArrayList<LeasingSpaceEntity> filteredLeasingSpace) {
        this.filteredLeasingSpace = filteredLeasingSpace;
    }

    public ArrayList<LeasingContractEntity> getFilteredLeasingSpaceContract() {
        return filteredLeasingSpaceContract;
    }

    public void setFilteredLeasingSpaceContract(ArrayList<LeasingContractEntity> filteredLeasingSpaceContract) {
        this.filteredLeasingSpaceContract = filteredLeasingSpaceContract;
    }


}
