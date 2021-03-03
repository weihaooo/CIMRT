/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tender;

import businessPartner.entity.NotificationEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.entity.StationEntity;
import static infraasset.sessionbean.InfraAssetSessionBean.addDays;
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
@Named(value = "bpTenderManagedBean")
@SessionScoped
public class BpTenderManagedBean implements Serializable {

    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    @EJB
    private TenderModuleSessionBeanLocal tenderSessionBeanLocal;

    private String partnerId;
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
    private String timeLeft;
    private String location;
    private double currentBid;
    private int bidCount;
    private Date deliveryDate;
    private Long deliveryId;
    private Date now = new Date();
    private boolean showDatatable = false;
    private boolean showOption = false;
    private boolean canRequest = true;

    private List<NotificationEntity> notificationList;
    private List<PurchaseRequestEntity> purchaseRequestList;
    private List<PurchaseRequestEntity> filteredAvailablePurchaseRequest;
    private List<BidEntity> bidList1;
    private List<BidEntity> filteredCurrentBid;
    private List<BidEntity> filteredLeasingHistory;
    private List<BidEntity> filteredBidHistory;
    private List<NotificationEntity> filteredNotification;
    private List<DeliveryEntity> deliveryList;
    private List<DeliveryEntity> filteredViewDelivery;
    private ArrayList<LeasingSpaceEntity> leasingList;
    private ArrayList<LeasingSpaceEntity> filteredLeasingSpace;
    private List<String> stationList;
    private List<StationEntity> stations;
    private List<NotificationEntity> selectedNotification;

    private String assetId;
    private String unitNumber;
    private double floorArea;
    private String waterProvision;
    private String waterProvision1;
    private String code;
    private double rentalFee;
    private int leaseYear;
    private String leaseDescription;
    private String leasingContractId;
    private String remarks;
    private int notificationCount;
    private int index;
    private int menu;
    private LeasingContractEntity leasingContract;
    private StreamedContent file;
    private StreamedContent tenderSubmission;
    private String decision;
    private String duration;
    private FittingOutReqEntity fittingOut;
    private double deposit;
    private StreamedContent fittingRequest;
    private StreamedContent jointInspection;
    private StreamedContent conditions;
    private StreamedContent tenderSubmissionForm;
    private StreamedContent tenderFAQ;
    private StreamedContent tenderNotesExample;
    private StreamedContent tenderApproveTrades;
    private StreamedContent shortForm;
    private StreamedContent longForm;

    //Filter Function
    private List<LeasingContractEntity> filteredCurrentLeasing;
    private List<LeasingContractEntity> filteredLeasingRequest;

    private AdvertisingContractEntity advertisingContract;
    private ArrayList<AdvertisementSpaceEntity> advertisingList;
    private ArrayList<AdvertisingContractEntity> advertContractList;
    private ArrayList<AdvertisementSpaceEntity> filteredAdvertSpace;
    private ArrayList<AdvertisingContractEntity> filteredAdvertContract;
    private String advertisingId;
    private double peakPeriod;
    private double nonpeakPeriod;
    private Date deadline;
    private String numberCode;
    private String type;
    private List<String> images;

    public double getPeakPeriod() {
        return peakPeriod;
    }

    public void setPeakPeriod(double peakPeriod) {
        this.peakPeriod = peakPeriod;
    }

    public double getNonpeakPeriod() {
        return nonpeakPeriod;
    }

    public void setNonpeakPeriod(double nonpeakPeriod) {
        this.nonpeakPeriod = nonpeakPeriod;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
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

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public List<PurchaseRequestEntity> getPurchaseRequestList() {
        return purchaseRequestList;
    }

    public void setPurchaseRequestList(List<PurchaseRequestEntity> purchaseRequestList) {
        this.purchaseRequestList = purchaseRequestList;
    }

    public List<BidEntity> getBidList1() {
        return bidList1;
    }

    public void setBidList1(List<BidEntity> BidList1) {
        this.bidList1 = BidList1;
    }

    public List<DeliveryEntity> getDeliveryList() {
        deliveryList = tenderSessionBeanLocal.getDeliveryList(partnerId);
        return deliveryList;
    }

    public void setDeliveryList(List<DeliveryEntity> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public int getBidCount() {
        return bidCount;
    }

    public void setBidCount(int bidCount) {
        this.bidCount = bidCount;
    }

    public List<NotificationEntity> getNotificationList() {
        notificationList = tenderSessionBeanLocal.getNotifications(partnerId);
        return notificationList;
    }

    public boolean isShowDatatable() {
        return showDatatable;
    }

    public void setShowDatatable(boolean showDatatable) {
        this.showDatatable = showDatatable;
    }

    public void setNotificationList(List<NotificationEntity> notificationList) {
        this.notificationList = notificationList;
    }

    public List<String> getStationList() {
        return stationList;
    }

    public void setStationList(List<String> stationList) {
        this.stationList = stationList;
    }

    public List<StationEntity> getStations() {
        return stations;
    }

    public void setStations(List<StationEntity> stations) {
        this.stations = stations;
    }

    public ArrayList<LeasingSpaceEntity> getLeasingList() {
        return leasingList;
    }

    public void setLeasingList(ArrayList<LeasingSpaceEntity> leasingList) {
        this.leasingList = leasingList;
    }

    public InfraAssetSessionBeanLocal getInfraAssetSessionBeanLocal() {
        return infraAssetSessionBeanLocal;
    }

    public void setInfraAssetSessionBeanLocal(InfraAssetSessionBeanLocal infraAssetSessionBeanLocal) {
        this.infraAssetSessionBeanLocal = infraAssetSessionBeanLocal;
    }

    public TenderModuleSessionBeanLocal getTenderSessionBeanLocal() {
        return tenderSessionBeanLocal;
    }

    public void setTenderSessionBeanLocal(TenderModuleSessionBeanLocal tenderSessionBeanLocal) {
        this.tenderSessionBeanLocal = tenderSessionBeanLocal;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }

    public String getWaterProvision() {
        return waterProvision;
    }

    public void setWaterProvision(String waterProvision) {
        this.waterProvision = waterProvision;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public int getLeaseYear() {
        return leaseYear;
    }

    public void setLeaseYear(int leaseYear) {
        this.leaseYear = leaseYear;
    }

    public String getLeaseDescription() {
        return leaseDescription;
    }

    public void setLeaseDescription(String leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public String getWaterProvision1() {
        return waterProvision1;
    }

    public void setWaterProvision1(String waterProvision1) {
        this.waterProvision1 = waterProvision1;
    }

    public String getLeasingContractId() {
        return leasingContractId;
    }

    public void setLeasingContractId(String leasingContractId) {
        this.leasingContractId = leasingContractId;
    }

    public boolean isCanRequest() {
        return canRequest;
    }

    public void setCanRequest(boolean canRequest) {
        this.canRequest = canRequest;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<PurchaseRequestEntity> getFilteredAvailablePurchaseRequest() {
        return filteredAvailablePurchaseRequest;
    }

    public void setFilteredAvailablePurchaseRequest(List<PurchaseRequestEntity> filteredAvailablePurchaseRequest) {
        this.filteredAvailablePurchaseRequest = filteredAvailablePurchaseRequest;
    }

    public List<BidEntity> getFilteredCurrentBid() {
        return filteredCurrentBid;
    }

    public void setFilteredCurrentBid(List<BidEntity> filteredCurrentBid) {
        this.filteredCurrentBid = filteredCurrentBid;
    }

    public List<BidEntity> getFilteredLeasingHistory() {
        return filteredLeasingHistory;
    }

    public void setFilteredLeasingHistory(List<BidEntity> filteredLeasingHistory) {
        this.filteredLeasingHistory = filteredLeasingHistory;
    }

    public List<DeliveryEntity> getFilteredViewDelivery() {
        return filteredViewDelivery;
    }

    public void setFilteredViewDelivery(List<DeliveryEntity> filteredViewDelivery) {
        this.filteredViewDelivery = filteredViewDelivery;
    }

    public ArrayList<LeasingSpaceEntity> getFilteredLeasingSpace() {
        return filteredLeasingSpace;
    }

    public void setFilteredLeasingSpace(ArrayList<LeasingSpaceEntity> filteredLeasingSpace) {
        this.filteredLeasingSpace = filteredLeasingSpace;
    }

    public List<LeasingContractEntity> getFilteredCurrentLeasing() {
        return filteredCurrentLeasing;
    }

    public void setFilteredCurrentLeasing(List<LeasingContractEntity> filteredCurrentLeasing) {
        this.filteredCurrentLeasing = filteredCurrentLeasing;
    }

    public List<LeasingContractEntity> getFilteredLeasingRequest() {
        return filteredLeasingRequest;
    }

    public void setFilteredLeasingRequest(List<LeasingContractEntity> filteredLeasingRequest) {
        this.filteredLeasingRequest = filteredLeasingRequest;
    }

    public List<BidEntity> getFilteredBidHistory() {
        return filteredBidHistory;
    }

    public void setFilteredBidHistory(List<BidEntity> filteredBidHistory) {
        this.filteredBidHistory = filteredBidHistory;
    }

    public List<NotificationEntity> getFilteredNotification() {
        return filteredNotification;
    }

    public void setFilteredNotification(List<NotificationEntity> filteredNotification) {
        this.filteredNotification = filteredNotification;
    }

    public List<NotificationEntity> getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotification(List<NotificationEntity> selectedNotification) {
        this.selectedNotification = selectedNotification;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public LeasingContractEntity getLeasingContract() {
        return leasingContract;
    }

    public void setLeasingContract(LeasingContractEntity leasingContract) {
        this.leasingContract = leasingContract;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public StreamedContent getTenderSubmission() {
        return tenderSubmission;
    }

    public void setTenderSubmission(StreamedContent tenderSubmission) {
        this.tenderSubmission = tenderSubmission;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public FittingOutReqEntity getFittingOut() {
        return fittingOut;
    }

    public void setFittingOut(FittingOutReqEntity fittingOut) {
        this.fittingOut = fittingOut;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public StreamedContent getFittingRequest() {
        return fittingRequest;
    }

    public void setFittingRequest(StreamedContent fittingRequest) {
        this.fittingRequest = fittingRequest;
    }

    public StreamedContent getJointInspection() {
        return jointInspection;
    }

    public void setJointInspection(StreamedContent jointInspection) {
        this.jointInspection = jointInspection;
    }

    public StreamedContent getConditions() {
        return conditions;
    }

    public void setConditions(StreamedContent conditions) {
        this.conditions = conditions;
    }

    public StreamedContent getTenderSubmissionForm() {
        return tenderSubmissionForm;
    }

    public void setTenderSubmissionForm(StreamedContent tenderSubmissionForm) {
        this.tenderSubmissionForm = tenderSubmissionForm;
    }

    public StreamedContent getTenderFAQ() {
        return tenderFAQ;
    }

    public void setTenderFAQ(StreamedContent tenderFAQ) {
        this.tenderFAQ = tenderFAQ;
    }

    public StreamedContent getTenderNotesExample() {
        return tenderNotesExample;
    }

    public void setTenderNotesExample(StreamedContent tenderNotesExample) {
        this.tenderNotesExample = tenderNotesExample;
    }

    public StreamedContent getTenderApproveTrades() {
        return tenderApproveTrades;
    }

    public void setTenderApproveTrades(StreamedContent tenderApproveTrades) {
        this.tenderApproveTrades = tenderApproveTrades;
    }

    public StreamedContent getShortForm() {
        return shortForm;
    }

    public void setShortForm(StreamedContent shortForm) {
        this.shortForm = shortForm;
    }

    public StreamedContent getLongForm() {
        return longForm;
    }

    public void setLongForm(StreamedContent longForm) {
        this.longForm = longForm;
    }

    public AdvertisingContractEntity getAdvertisingContract() {
        return advertisingContract;
    }

    public void setAdvertisingContract(AdvertisingContractEntity advertisingContract) {
        this.advertisingContract = advertisingContract;
    }

    public ArrayList<AdvertisementSpaceEntity> getAdvertisingList() {
        return advertisingList;
    }

    public void setAdvertisingList(ArrayList<AdvertisementSpaceEntity> advertisingList) {
        this.advertisingList = advertisingList;
    }

    public ArrayList<AdvertisingContractEntity> getAdvertContractList() {
        return advertContractList;
    }

    public void setAdvertContractList(ArrayList<AdvertisingContractEntity> advertContractList) {
        this.advertContractList = advertContractList;
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    public boolean isShowOption() {
        return showOption;
    }

    public void setShowOption(boolean showOption) {
        this.showOption = showOption;
    }

    @PostConstruct
    public void init() {
        partnerId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("partnerId").toString();
        checkPR();
        checkLeasing();
        checkAdvertising();

        stations = infraAssetSessionBeanLocal.getStation();
        stationList = new ArrayList<String>();
        for (int i = 0; i < stations.size(); i++) {
            stationList.add(stations.get(i).getCode());
        }

        calculateNotification();
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/tenancyAgreement.pdf");
        file = new DefaultStreamedContent(stream, "application/pdf", "tenancyAgreement.pdf");

        InputStream stream1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/TenderSubmissionForm.pdf");
        tenderSubmission = new DefaultStreamedContent(stream1, "application/pdf", "TenderSubmissionForm.pdf");

        InputStream stream2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/FittingOut.pdf");
        fittingRequest = new DefaultStreamedContent(stream2, "application/pdf", "FittingOut.pdf");

        InputStream stream3 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/jointInspection.pdf");
        jointInspection = new DefaultStreamedContent(stream3, "application/pdf", "JointInspection.pdf");

        InputStream stream4 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/ApprovedTrades.pdf");
        tenderApproveTrades = new DefaultStreamedContent(stream4, "application/pdf", "TenderApprovedTrades.pdf");

        InputStream stream5 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/ConditionsTender.pdf");
        conditions = new DefaultStreamedContent(stream5, "application/pdf", "TenderConditions.pdf");

        InputStream stream6 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/FAQ.pdf");
        tenderFAQ = new DefaultStreamedContent(stream6, "application/pdf", "FrequentlyAskQuestions.pdf");

        InputStream stream7 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/LongFormAgreement.pdf");
        longForm = new DefaultStreamedContent(stream7, "application/pdf", "LongFormAgreement.pdf");

        InputStream stream8 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/NotesExamples.pdf");
        tenderNotesExample = new DefaultStreamedContent(stream8, "application/pdf", "TenderNotesExample.pdf");

        InputStream stream9 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/ShortForm.pdf");
        shortForm = new DefaultStreamedContent(stream9, "application/pdf", "ShortFormAgreement.pdf");

        images = new ArrayList<String>();
        for (int i = 1; i <= 13; i++) {
            images.add("Escalator_Bulkhead.jpg");
            images.add("GroundFloor.jpg");
            images.add("On_Track.jpg");
            images.add("Platform.jpg");
            images.add("Transit_Area.jpg");
            images.add("Digital_InTrain.jpg");
            images.add("Escalator.jpg");
            images.add("Non_Digital_InTrain_Panel.jpg");
            images.add("Outside_Gantry.jpg");
            images.add("Lift.jpg");
            images.add("Topup_Machine.jpg");
            images.add("Train_Hanger.jpg");
            images.add("Window_Sticker.jpg");
        }
    }

    public List<String> getImages() {
        return images;

    }

    public void checkPR() {
        tenderSessionBeanLocal.checkPR();
    }

    public void checkLeasing() {
        tenderSessionBeanLocal.checkLeasing();
    }

    public void checkAdvertising() {
        tenderSessionBeanLocal.checkAdvertising();
    }

    public void onSelectionChange() {
        if (location != null && !location.equals("")) {
            leasingList = new ArrayList<LeasingSpaceEntity>();
            ArrayList<LeasingSpaceEntity> leasingList1 = infraAssetSessionBeanLocal.getLeasingSpace();
            for (int i = 0; i < leasingList1.size(); i++) {
                LeasingSpaceEntity e = leasingList1.get(i);
                NodeEntity infra = (NodeEntity) e.getInfrastructure();
                if (e.getStatus().equals("Available") && infra.getCode().equals(location) && e.getDeadline() != null) {
                    leasingList.add(e);
                }
            }
            showDatatable = true;
        }
    }

    public String onSelectionChange1() {
        this.showOption = false;
        advertisingList = new ArrayList<AdvertisementSpaceEntity>();
        if (assetName.equals("Station")) {
            this.showOption = true;
            return "viewAvailableAdvertisement";
        } else {
            ArrayList<AdvertisementSpaceEntity> advertList = infraAssetSessionBeanLocal.getAdvertisementSpace();
            for (int i = 0; i < advertList.size(); i++) {
                AdvertisementSpaceEntity a = advertList.get(i);
                if (a.getStatus().equals("Available") && a.getAssetName().equals("Rolling Stock") && a.getDeadline() != null) {
                    advertisingList.add(a);
                }
            }
            showDatatable = true;
            return "viewAvailableAdvertisement";
        }
    }

    public void onSelectionChange2() {
        if (location != null && !location.equals("")) {
            advertisingList = new ArrayList<AdvertisementSpaceEntity>();
            ArrayList<AdvertisementSpaceEntity> advertList = infraAssetSessionBeanLocal.getAdvertisementSpace();
            for (int i = 0; i < advertList.size(); i++) {
                AdvertisementSpaceEntity a = advertList.get(i);
                NodeEntity infra = (NodeEntity) a.getInfrastructure();
                if (a.getStatus().equals("Available") && a.getAssetName().equals("Station") && infra.getCode().equals(location) && a.getDeadline() != null) {
                    advertisingList.add(a);
                }
            }
            showDatatable = true;
        }
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

    public String calculateBidNumber(String purchaseRequestId) {
        PurchaseRequestEntity temp = tenderSessionBeanLocal.searchPurchaseReq(purchaseRequestId);

        ArrayList<BidEntity> bidlist = tenderSessionBeanLocal.getBidList();
        ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();
        ArrayList<BidEntity> newbidlist1 = new ArrayList<BidEntity>();

        //Get the bid list for the purchase request
        for (int j = 0; j < bidlist.size(); j++) {
            BidEntity bid = bidlist.get(j);
            if (bid.getPurchaseRequest() != null) {
                if (bid.getPurchaseRequest().getPurchaseRequestId().equals(temp.getPurchaseRequestId())) {
                    newbidlist.add(bid);
                }
            }
        }

        if (newbidlist.isEmpty()) {
            return "0 bids";
        } else {
            for (int i = 0; i < newbidlist.size(); i++) {
                if (newbidlist.get(i).getStatus().equals("Submitted")) {
                    newbidlist1.add(newbidlist.get(i));
                }
            }
            this.bidCount = newbidlist1.size();
            return newbidlist1.size() + " bids";
        }
    }

    public String calculateLeaseNumber(String assetId) {
        ArrayList<LeasingContractEntity> temp = tenderSessionBeanLocal.getLeasingContract();
        ArrayList<LeasingContractEntity> newList = new ArrayList<LeasingContractEntity>();

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getLeasingSpace().getAssetId().equals(assetId) && temp.get(i).getStatus().equals("Submitted")) {
                newList.add(temp.get(i));
            }
        }

        if (newList.isEmpty()) {
            return "0 submitter";
        } else {
            this.bidCount = newList.size();
            return newList.size() + " submitters";
        }
    }

    public String goBidPR(String purchaseReqId) {
        this.canRequest = true;
        this.purchaseRequestId = purchaseReqId;
        PurchaseRequestEntity temp = (PurchaseRequestEntity) tenderSessionBeanLocal.searchPurchaseReq(purchaseReqId);
        this.assetRequestType = temp.getCategory();
        this.assetName = temp.getItemName();
        this.qty = temp.getQty();
        this.location = deliveryLocation(purchaseReqId);
        this.maxBidAmount = temp.getMaxBidAmount();
        this.description = temp.getDescription();
        this.reqDate = temp.getReqDate();
        this.reqDeadline = temp.getReqDeadline();
        String S = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(temp.getReqDeadline());
        this.timeLeft = printDifference(todayDate, reqDeadline) + "(" + S + ")";
        ArrayList<BidEntity> bidList = tenderSessionBeanLocal.getBidList();
        for (int i = 0; i < bidList.size(); i++) {
            if (bidList.get(i).getBusinessPartner().getPartnerId().equals(partnerId) && bidList.get(i).getPurchaseRequest().equals(temp) && (!(bidList.get(i).getStatus().equals("Withdrawn") || bidList.get(i).getStatus().equals("Unsuccessful")))) {
                this.canRequest = false;
                break;
            }
        }
        return "submitBid";
    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        return elapsedDays + "days " + elapsedHours + "hours " + elapsedMinutes + "minutes ";
    }

    public String addBid() {
        boolean sta = tenderSessionBeanLocal.addBid(partnerId, currentBid, purchaseRequestId, remarks);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Bid is added successfully",
                            ""));
            this.maxBidAmount = currentBid;
            this.currentBid = 0;
            this.remarks = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a Bid!", ""));
            return "viewMyTransactions";
        }
        return "viewMyTransactions";
    }

    public ArrayList<BidEntity> getBidList(String purchaseRequestId) {
        PurchaseRequestEntity temp = (PurchaseRequestEntity) tenderSessionBeanLocal.searchPurchaseReq(purchaseRequestId);

        ArrayList<BidEntity> bidlist = tenderSessionBeanLocal.getBidList();
        ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();
        ArrayList<BidEntity> newbidlist1 = new ArrayList<BidEntity>();

        //Get the bid list for the purchase request
        for (int j = 0; j < bidlist.size(); j++) {
            BidEntity bid = bidlist.get(j);
            if (bid.getPurchaseRequest().getPurchaseRequestId().equals(temp.getPurchaseRequestId())) {
                newbidlist.add(bid);
            }
        }

        if (newbidlist.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < newbidlist.size(); i++) {
                if (newbidlist.get(i).getStatus().equals("Submitted")) {
                    newbidlist1.add(newbidlist.get(i));
                }
            }
            return newbidlist1;
        }
    }

    public ArrayList<BidEntity> getBidList() {
        ArrayList<BidEntity> bidList = tenderSessionBeanLocal.getBidList();
        ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();
        if (bidList.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < bidList.size(); i++) {
                if (bidList.get(i).getBusinessPartner().getPartnerId().equals(partnerId) && (bidList.get(i).getStatus().equals("Submitted") || bidList.get(i).getStatus().equals("Pending"))) {
                    newbidlist.add(bidList.get(i));
                }

            }
            return newbidlist;
        }
    }

    //Bid that are either fulfillied or not fulfillied or withdraw
    public ArrayList<BidEntity> getBidList11() {
        ArrayList<BidEntity> bidList = tenderSessionBeanLocal.getBidList();
        ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();
        if (bidList.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < bidList.size(); i++) {
                if (bidList.get(i).getBusinessPartner().getPartnerId().equals(partnerId) && (!(bidList.get(i).getStatus().equals("Submitted") || bidList.get(i).getStatus().equals("Pending")))) {
                    newbidlist.add(bidList.get(i));
                }

            }
            return newbidlist;
        }
    }

    public String goDeleteBid(String bidId) {
        boolean sta = tenderSessionBeanLocal.deleteBid(bidId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Bid is deleted successfully",
                            ""));
            init();
            this.canRequest = true;
            tenderSessionBeanLocal.checkPR();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the bid!", ""));
            return "viewMyTransactions";
        }
        return "viewMyTransactions";
    }

    public String goSchedule() {
        boolean sta = tenderSessionBeanLocal.scheduleDelivery(deliveryId, deliveryDate);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Delivery is scheduled successfully",
                            ""));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to schedule a delivery!", ""));
            return "viewMyDelivery";
        }
        return "viewMyDelivery";
    }

    public String goSchedule1(Long deliveryId) {
        this.deliveryId = deliveryId;
        DeliveryEntity temp = (DeliveryEntity) tenderSessionBeanLocal.searchDelivery(deliveryId);
        this.deliveryDate = temp.getDateAndTime();
        return "scheduleDelivery";
    }

    public boolean checkDelivery(Date deliveryDate) {
        if (deliveryDate == null) {
            return true;
        } else {
            Date today = new Date();
            Date newDate = addDays(today, 7);
            if (newDate.compareTo(deliveryDate) > 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public String goRequestLeasing(String assetId) {
        this.canRequest = true;
        this.assetId = assetId;
        LeasingSpaceEntity temp = (LeasingSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        NodeEntity node = (NodeEntity) infraAssetSessionBeanLocal.searchNode1(temp.getInfrastructure().getInfraId());
        this.code = node.getCode();
        this.unitNumber = temp.getUnitNumber();
        this.assetName = temp.getAssetName();
        this.floorArea = temp.getFloorArea();
        this.waterProvision = String.valueOf(temp.isWaterProvision());
        this.rentalFee = temp.getRentalFee();
        this.leaseDescription = temp.getLeaseDescription();
        String S = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(temp.getDeadline());
        this.timeLeft = printDifference(todayDate, temp.getDeadline()) + "(" + S + ")";
        ArrayList<LeasingContractEntity> contractList = tenderSessionBeanLocal.getLeasingContract();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getPartner().getPartnerId().equals(partnerId) && contractList.get(i).getLeasingSpace().getAssetId().equals(assetId) && !contractList.get(i).getStatus().equals("Terminated") && !contractList.get(i).getStatus().equals("End of Contract")) {
                this.canRequest = false;
                break;
            }
        }
        return "submitLeasingRequest";
    }

    public String requestLeasing() {
        boolean sta = tenderSessionBeanLocal.requestLeasing(assetId, leaseYear, partnerId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leasing space is requested successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.unitNumber = null;
            this.floorArea = 0;
            this.waterProvision = null;
            this.rentalFee = 0;
            this.leaseYear = 0;
            this.leaseDescription = null;
            this.showDatatable = false;
            this.location = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to request for the leasing space!", ""));
            return "viewMyLeasing";
        }
        return "viewMyLeasing";
    }

    public ArrayList<LeasingContractEntity> getLeasingList(String assetId) {
        ArrayList<LeasingContractEntity> temp = tenderSessionBeanLocal.getLeasingContract();
        ArrayList<LeasingContractEntity> newList = new ArrayList<LeasingContractEntity>();

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getLeasingSpace().getAssetId().equals(assetId) && temp.get(i).getStatus().equals("Submitted")) {
                newList.add(temp.get(i));
            }
        }
        return newList;
    }

    public String goEditLC(String leasingContractId) {
        LeasingContractEntity temp = tenderSessionBeanLocal.searchLeasingContract(leasingContractId);
        this.leasingContractId = temp.getLeasingContractId();
        this.leaseYear = temp.getYearsOfContract();
        this.reqDate = temp.getSubmittedDate();
        NodeEntity node = (NodeEntity) temp.getLeasingSpace().getInfrastructure();
        this.location = node.getCode();
        this.unitNumber = temp.getLeasingSpace().getUnitNumber();
        return "editMyLeasing";
    }

    public String editMyLeasing() {
        boolean sta = tenderSessionBeanLocal.goEditMyLeasing(leasingContractId, leaseYear);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Request is edited successfully",
                            ""));
            this.leasingContractId = null;
            this.leaseYear = 0;
            this.reqDate = null;
            this.location = null;
            this.unitNumber = null;
            this.showDatatable = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update request!", ""));
            return "viewMyLeasing";
        }
        return "viewMyLeasing";
    }

    public String goDeleteLC(String leasingContractId) {
        boolean sta = tenderSessionBeanLocal.goDeleteLC(leasingContractId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Request is deleted successfully",
                            ""));
            this.canRequest = true;
            this.showDatatable = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete request!", ""));
            return "viewMyLeasing";
        }
        return "viewMyLeasing";
    }

    public String goEditBid(String bidId) {
        BidEntity bid = tenderSessionBeanLocal.searchBid(bidId);
        PurchaseRequestEntity temp = (PurchaseRequestEntity) tenderSessionBeanLocal.searchPurchaseReq(bid.getPurchaseRequest().getPurchaseRequestId());
        this.assetRequestType = temp.getCategory();
        this.assetName = temp.getItemName();
        this.qty = temp.getQty();
        this.location = deliveryLocation(temp.getPurchaseRequestId());
        this.maxBidAmount = temp.getMaxBidAmount();
        this.description = temp.getDescription();
        this.reqDate = temp.getReqDate();
        this.reqDeadline = temp.getReqDeadline();
        String S = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(temp.getReqDeadline());
        this.timeLeft = printDifference(todayDate, reqDeadline) + "(" + S + ")";
        this.currentBid = bid.getBidPrice();
        this.remarks = bid.getRemarks();
        this.bidId = bid.getBidId();
        return "editBid";
    }

    public String updateBid() {
        boolean sta = tenderSessionBeanLocal.goUpdateBid(bidId, currentBid, remarks);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Bid is updated successfully",
                            ""));
            this.bidId = null;
            this.currentBid = 0;
            this.remarks = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update Bid!", ""));
            return "viewMyTransactions";
        }
        return "viewMyTransactions";
    }

    public String goViewPR(String category) {
        ArrayList<PurchaseRequestEntity> purchaseRequestList1 = new ArrayList<PurchaseRequestEntity>();
        ArrayList<PurchaseRequestEntity> list = tenderSessionBeanLocal.getPurchaseRequestListBP();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCategory().equals(category)) {
                purchaseRequestList1.add(list.get(i));
            }
        }
        if (!(purchaseRequestList1.isEmpty())) {
            this.purchaseRequestList = purchaseRequestList1;
        } else {
            this.purchaseRequestList = null;
        }
        return "viewAvailablePurchaseRequest";
    }

    public String markRead(ArrayList<NotificationEntity> notifications) {
        if (notifications.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Please select a notifcation to mark as read!",
                            ""));
            init();
        } else {

            boolean sta = tenderSessionBeanLocal.markRead(notifications);
            if (sta) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Notifications is marked as read successfully",
                                ""));
                init();
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to mark notifications as read!", ""));
                return "viewMyNotifications";
            }
        }
        return "viewMyNotifications";

    }

    public String markUnread(ArrayList<NotificationEntity> notifications) {
        if (notifications.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Please select a notifcation to mark as uread!",
                            ""));
            init();
        } else {

            boolean sta = tenderSessionBeanLocal.markUnread(notifications);
            if (sta) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Notifications is marked as unread successfully",
                                ""));
                init();
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to mark notifications as unread!", ""));
                return "viewMyNotifications";
            }
        }
        return "viewMyNotifications";
    }

    public void calculateNotification() {
        int count = 0;
        ArrayList<NotificationEntity> temp = tenderSessionBeanLocal.getNotifications(partnerId);
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).isCheckRead() == false) {
                count++;
            }
        }
        this.notificationCount = count;
    }

    public String goViewLeaseContract(String contractId) {
        LeasingContractEntity contract = tenderSessionBeanLocal.searchLeasingContract(contractId);
        leasingContract = contract;

        fittingOut = tenderSessionBeanLocal.searchFittingOut(contractId);

        //Check is at which step
        //First step is siging of tenancy agreement
        if (fittingOut != null) {
            if (leasingContract.isSigningOfAgreement() == false) {
                this.menu = 0;
                this.index = 0;
            } else if ((leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest() == null) || (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && !(fittingOut.getStatus().equals("Approved")))) {
                this.menu = 1;
                this.index = 1;
                this.decision = null;
            } else if ((leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && fittingOut.getStatus().equals("Approved")) && (leasingContract.getJointInspection() == null || leasingContract.getJointInspection().getEntryStatus().equals("Scheduled"))) {
                this.menu = 2;
                this.index = 2;
            } else if (leasingContract.getJointInspection().getEntryStatus().equals("Completed") && !leasingContract.getStatus().equals("End of Contract") && (leasingContract.getJointInspection().getExitInspection() == null || leasingContract.getJointInspection().getExitStatus().equals("Scheduled") || leasingContract.getJointInspection().getExitStatus().equals("Completed"))) {
                this.menu = 3;
                this.index = 3;
            } else if ((contractEnd(leasingContract.getLeasingContractId()) == true || leasingContract.getStatus().equals("End of Contract")) && leasingContract.getJointInspection().getExitStatus().equals("Completed")) {
                this.menu = 4;
                this.index = 4;
            } else {
                this.menu = 0;
                this.index = 0;
            }
        } else {
            if (leasingContract.isSigningOfAgreement() == false) {
                this.menu = 0;
                this.index = 0;
            } else if ((leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest() == null) && leasingContract.getJointInspection() == null) {
                this.menu = 1;
                this.index = 1;
                this.decision = null;
            } else if (leasingContract.isSigningOfAgreement() == true && leasingContract.getFittingRequest().equals("Completed") && (leasingContract.getJointInspection() == null || leasingContract.getJointInspection().getEntryStatus().equals("Scheduled"))) {
                this.menu = 2;
                this.index = 2;
            } else if (leasingContract.getJointInspection().getEntryStatus().equals("Completed") && !leasingContract.getStatus().equals("End of Contract") && (leasingContract.getJointInspection().getExitInspection() == null || leasingContract.getJointInspection().getExitStatus().equals("Scheduled") || leasingContract.getJointInspection().getExitStatus().equals("Completed"))) {
                this.menu = 3;
                this.index = 3;
            } else if ((contractEnd(leasingContract.getLeasingContractId()) == true || leasingContract.getStatus().equals("End of Contract")) && leasingContract.getJointInspection().getExitStatus().equals("Completed")) {
                this.menu = 4;
                this.index = 4;
            } else {
                this.menu = 0;
                this.index = 0;
            }
        }
        return "viewMyLeaseContract";
    }

    public String goViewAdvertContract(String contractId) {
        AdvertisingContractEntity contract = tenderSessionBeanLocal.searchAdvertisingContract(contractId);
        advertisingContract = contract;
        if (advertisingContract.isSigningOfAgreement() == false) {
            this.menu = 0;
            this.index = 0;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == false) {
            this.menu = 1;
            this.index = 1;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == true & advertisingContract.isRemoval() == false) {
            this.menu = 2;
            this.index = 2;
        } else if (advertisingContract.isSigningOfAgreement() == true && advertisingContract.isInstallation() == true & advertisingContract.isRemoval() == true) {
            this.menu = 3;
            this.index = 3;
        }
        return "viewMyAdvertContract";
    }

    public boolean contractEnd(String contractId) {
        LeasingContractEntity contract = tenderSessionBeanLocal.searchLeasingContract(contractId);
        Date contractEnd = contract.getEndDate();
        if (now.compareTo(contractEnd) > 0) {
            tenderSessionBeanLocal.completeLeaseContract(contractId);
            return true;
        } else {
            return false;
        }
    }

    public String goIndex(int number) {
        if (number == 1) {
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        }
        this.index = number;
        return "viewMyLeaseContract.xhtml?faces-redirect=true";
    }

    public String goIndex1(int number) {
        this.index = number;
        return "viewMyAdvertContract.xhtml?faces-redirect=true";
    }

    public String confirmFittingRequest(String leasingContractId) {
        if (decision.equals("No")) {
            updateFittingRequest("No", null, leasingContractId);
            return "viewMyLeaseContract.xhtml?faces-redirect=true";
        } else {
            this.decision = "Yes";
            return "viewMyLeaseContract.xhtml?faces-redirect=true";
        }
    }

    public void confirmFittingRequest1(String leasingContractId) {
        updateFittingRequest("Yes", duration, leasingContractId);
    }

    public String updateFittingRequest(String decision, String duration, String leasingContractId) {
        boolean sta = tenderSessionBeanLocal.updateFittingRequest(decision, duration, leasingContractId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Fitting Request is submitted successfully",
                            ""));
            init();
            LeasingContractEntity contract = tenderSessionBeanLocal.searchLeasingContract(leasingContractId);
            leasingContract = contract;
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContractId);
            if (decision.equals("No")) {
                this.menu = 2;
                this.index = 2;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to submit fitting request!", ""));
            return "viewMyLeaseContract.xhtml?faces-redirect=true";
        }
        return "viewMyLeaseContract.xhtml?faces-redirect=true";
    }

    public List<LeasingContractEntity> getLRList() {
        List<LeasingContractEntity> leasingRequestList = tenderSessionBeanLocal.getLeasingRequestList(partnerId);
        List<LeasingContractEntity> newleasingRequestList = new ArrayList<LeasingContractEntity>();
        for (int i = 0; i < leasingRequestList.size(); i++) {
            if (!(leasingRequestList.get(i).getStatus().equals("On Contract") || leasingRequestList.get(i).getStatus().equals("End of Contract") || leasingRequestList.get(i).getStatus().equals("Terminated"))) {
                newleasingRequestList.add(leasingRequestList.get(i));
            }
        }
        return newleasingRequestList;
    }

    public List<LeasingContractEntity> getLeasingRequestList1() {
        List<LeasingContractEntity> leasingRequestList1 = tenderSessionBeanLocal.getLeasingRequestList1(partnerId);
        return leasingRequestList1;
    }

    public List<LeasingContractEntity> getLeasingRequestList2() {
        List<LeasingContractEntity> leasingRequestList2 = tenderSessionBeanLocal.getLeasingRequestList2(partnerId);
        return leasingRequestList2;
    }

    public String goTerminateLeasingContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.terminateLeasingContract(contractId, "e0002468@u.nus.edu");
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
            return "viewMyLeasing";
        }
        return "viewMyLeasing";
    }

    public String goRenewLeaseContract(String contractId) {
        LeasingContractEntity contract = tenderSessionBeanLocal.searchLeasingContract(contractId);
        leasingContract = contract;
        return "renewLeaseContract";
    }

    public String renewLeaseContract() {
        boolean status1 = tenderSessionBeanLocal.renewLeasingContract(leasingContract, leaseYear);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Renewal of contract is successfully",
                            ""));
            leasingContract = tenderSessionBeanLocal.searchLeasingContract(leasingContract.getLeasingContractId());
            fittingOut = tenderSessionBeanLocal.searchFittingOut(leasingContract.getLeasingContractId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to renew the contract!"));
            return "viewMyLeasing";
        }
        return "viewMyLeasing";
    }

    public String goRequestAdvertising(String assetId) {
        this.canRequest = true;
        this.assetId = assetId;
        AdvertisementSpaceEntity temp = (AdvertisementSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        this.assetName = temp.getAssetName();
        if (temp.getAssetName().equals("Station")) {
            NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getInfrastructure().getInfraId());
            this.code = temp1.getCode();
        } else {
            this.code = temp.getInfrastructure().getInfraId();
        }
        this.location = temp.getLocation();
        this.peakPeriod = temp.getPeakPeriodFee();
        this.nonpeakPeriod = temp.getNonpeakPeriodFee();
        this.type = temp.getType();
        this.numberCode = temp.getNumberCode();
        this.deadline = temp.getDeadline();

        String S = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(temp.getDeadline());
        this.timeLeft = printDifference(todayDate, temp.getDeadline()) + "(" + S + ")";
        ArrayList<AdvertisingContractEntity> contractList = tenderSessionBeanLocal.getAdvertisingContract();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getPartner().getPartnerId().equals(partnerId) && contractList.get(i).getAdvertisement().getAssetId().equals(assetId) && !contractList.get(i).getStatus().equals("Terminated") && !contractList.get(i).getStatus().equals("End of Contract")) {
                this.canRequest = false;
                break;
            }
        }
        return "submitAdvertisingRequest";
    }

    public String requestAdvertising() {
        boolean sta = tenderSessionBeanLocal.requestAdvertising(assetId, leaseYear, partnerId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Advertising space is requested successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.location = null;
            this.peakPeriod = 0;
            this.nonpeakPeriod = 0;
            this.type = null;
            this.numberCode = null;
            this.deadline = null;
            this.leaseYear = 0;
            this.showDatatable = false;
            this.location = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to request for the leasing space!", ""));
            return "viewMyAdvertising";
        }
        return "viewMyAdvertising";
    }

    public String calculateAdvertNumber(String assetId) {
        ArrayList<AdvertisingContractEntity> temp = tenderSessionBeanLocal.getAdvertisingContract();
        ArrayList<AdvertisingContractEntity> newList = new ArrayList<AdvertisingContractEntity>();

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getAdvertisement().getAssetId().equals(assetId) && temp.get(i).getStatus().equals("Submitted")) {
                newList.add(temp.get(i));
            }
        }

        if (newList.isEmpty()) {
            return "0 submitter";
        } else {
            this.bidCount = newList.size();
            return newList.size() + " submitters";
        }
    }

    public List<AdvertisingContractEntity> getAdvertisingRequestList1() {
        List<AdvertisingContractEntity> advertisingRequestList1 = tenderSessionBeanLocal.getAdvertisingRequestList1(partnerId);
        return advertisingRequestList1;
    }

    public List<AdvertisingContractEntity> getAdvertisingRequestList2() {
        List<AdvertisingContractEntity> advertisingRequestList2 = tenderSessionBeanLocal.getAdvertisingRequestList2(partnerId);
        return advertisingRequestList2;
    }

    public List<AdvertisingContractEntity> getARList() {
        ArrayList<AdvertisingContractEntity> contractList = tenderSessionBeanLocal.getAdvertisingRequestList(partnerId);
        ArrayList<AdvertisingContractEntity> newList = new ArrayList<AdvertisingContractEntity>();
        for (int i = 0; i < contractList.size(); i++) {
            if (!(contractList.get(i).getStatus().equals("On Contract") || contractList.get(i).getStatus().equals("End of Contract") || contractList.get(i).getStatus().equals("Terminated"))) {
                newList.add(contractList.get(i));
            }
        }
        return newList;
    }

    public String goRenewAdvertContract(String contractId) {
        AdvertisingContractEntity contract = tenderSessionBeanLocal.searchAdvertisingContract(contractId);
        advertisingContract = contract;
        return "renewAdvertContract";
    }

    public String renewAdvertContract() {
        boolean status1 = tenderSessionBeanLocal.renewAdvertContract(advertisingContract, leaseYear);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Renewal of contract is successfully",
                            ""));
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(advertisingContract.getAdvertisingId());
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to renew the contract!"));
            return "viewMyAdvertising";
        }
        return "viewMyAdvertising";
    }

    public String goTerminateAdvertContract(String contractId) {
        boolean status1 = tenderSessionBeanLocal.terminateAdvertisingContract(contractId, "e0002468@u.nus.edu");
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Contract is terminated successfully",
                            ""));
            advertisingContract = tenderSessionBeanLocal.searchAdvertisingContract(contractId);
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to terminate the contract!"));
            return "viewMyAdvertising";
        }
        return "viewMyAdvertising";
    }

    public String goEditAC(String contractId) {
        AdvertisingContractEntity temp = tenderSessionBeanLocal.searchAdvertisingContract(contractId);
        this.leasingContractId = temp.getAdvertisingId();
        this.leaseYear = temp.getYearsOfContract();
        this.reqDate = temp.getSubmittedDate();
        if (temp.getAdvertisement().getAssetName().equals("Station")) {
            NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getAdvertisement().getInfrastructure().getInfraId());
            this.code = temp1.getCode();
        } else {
            this.code = temp.getAdvertisement().getInfrastructure().getInfraId();
        }
        this.location = temp.getAdvertisement().getLocation();
        this.type = temp.getAdvertisement().getType();
        this.numberCode = temp.getAdvertisement().getNumberCode();
        return "editMyAdvertising";
    }

    public String editMyAdvertising() {
        boolean sta = tenderSessionBeanLocal.goEditMyAdvertising(leasingContractId, leaseYear);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Request is edited successfully",
                            ""));
            this.leasingContractId = null;
            this.leaseYear = 0;
            this.reqDate = null;
            this.code = null;
            this.location = null;
            this.type = null;
            this.numberCode = null;
            this.showDatatable = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update request!", ""));
            return "viewMyAdvertising";
        }
        return "viewMyAdvertising";
    }

    public String goDeleteAC(String advertisingId) {
        boolean sta = tenderSessionBeanLocal.goDeleteAC(advertisingId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Request is deleted successfully",
                            ""));
            this.canRequest = true;
            this.showDatatable = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete request!", ""));
            return "viewMyAdvertising";
        }
        return "viewMyAdvertising";
    }

    public boolean checkAdvertTerminate(String advertisingId) {
        AdvertisingContractEntity ac = tenderSessionBeanLocal.searchAdvertisingContract(advertisingId);
        ArrayList<AdvertisingContractEntity> list = tenderSessionBeanLocal.getAdvertisingContract();
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).getStatus().equals("Need Approval") || list.get(i).getStatus().equals("Successful") || list.get(i).getStatus().equals("Unsuccessful")) && ac.getAdvertisement().equals(list.get(i).getAdvertisement())) {
                return false;
            }
        }
        return true;
    }

    public boolean checkLeaseTerminate(String contractId) {
        ArrayList<LeasingContractEntity> list = tenderSessionBeanLocal.getLeasingContract();
        LeasingContractEntity lc = tenderSessionBeanLocal.searchLeasingContract(contractId);
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).getStatus().equals("Need Approval") || list.get(i).getStatus().equals("Successful") || list.get(i).getStatus().equals("Unsuccessful")) && lc.getLeasingSpace().equals(list.get(i).getLeasingSpace())) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<AdvertisementSpaceEntity> getFilteredAdvertSpace() {
        return filteredAdvertSpace;
    }

    public void setFilteredAdvertSpace(ArrayList<AdvertisementSpaceEntity> filteredAdvertSpace) {
        this.filteredAdvertSpace = filteredAdvertSpace;
    }

    public ArrayList<AdvertisingContractEntity> getFilteredAdvertContract() {
        return filteredAdvertContract;
    }

    public void setFilteredAdvertContract(ArrayList<AdvertisingContractEntity> filteredAdvertContract) {
        this.filteredAdvertContract = filteredAdvertContract;
    }

}
