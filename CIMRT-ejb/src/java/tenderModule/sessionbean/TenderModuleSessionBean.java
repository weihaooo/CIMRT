/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.sessionbean;

import businessPartner.entity.BusinessPartnerEntity;
import businessPartner.entity.NotificationEntity;
import commoninfra.sessionbean.EmailManager;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.LeasingSpaceEntity;
import static infraasset.sessionbean.InfraAssetSessionBean.addDays;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import routefare.entity.NodeEntity;
import tenderModule.entity.AdvertisingContractEntity;
import tenderModule.entity.BidEntity;
import tenderModule.entity.DeliveryEntity;
import tenderModule.entity.FittingOutReqEntity;
import tenderModule.entity.JointInspectionEntity;
import tenderModule.entity.LeasingContractEntity;
import tenderModule.entity.PurchaseRequestEntity;

/**
 *
 * @author kayleytan
 */
@Stateless
public class TenderModuleSessionBean implements TenderModuleSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private PurchaseRequestEntity purchaseRequest;

    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    @Override
    public ArrayList<PurchaseRequestEntity> getPurchaseRequestList() {
        ArrayList<PurchaseRequestEntity> purchaseRequestList = new ArrayList<PurchaseRequestEntity>();
        Query query = em.createQuery("SELECT p FROM PurchaseRequestEntity AS p");
        for (Object o : query.getResultList()) {
            PurchaseRequestEntity p = (PurchaseRequestEntity) o;
            if (!(p.getStatus().equals("Deleted"))) {
                purchaseRequestList.add(p);
            }
        }
        return purchaseRequestList;
    }

    @Override
    public ArrayList<PurchaseRequestEntity> getPurchaseRequestListBP() {
        ArrayList<PurchaseRequestEntity> purchaseRequestList = new ArrayList<PurchaseRequestEntity>();
        Query query = em.createQuery("SELECT p FROM PurchaseRequestEntity AS p");
        for (Object o : query.getResultList()) {
            PurchaseRequestEntity p = (PurchaseRequestEntity) o;
            if (p.getStatus().equals("Pending")) {
                purchaseRequestList.add(p);
            }
        }
        return purchaseRequestList;
    }

    @Override
    public void generatePurchaseRequest() {
        ArrayList<AssetRequestEntity> assetRequestList = infraAssetSessionBeanLocal.getAssetRequest();
        if (assetRequestList.isEmpty()) {
            //Do nothing
        } else {
            for (int i = 0; i < assetRequestList.size(); i++) {
                ArrayList<AssetRequestEntity> assignAR = new ArrayList<AssetRequestEntity>();
                AssetRequestEntity ar = assetRequestList.get(i);
                if (ar.getStatus().equals("Processing")) {
                    int qty = ar.getQty();
                    assignAR.add(ar);
                    if (i != (assetRequestList.size() - 1)) {
                        for (int j = i + 1; j < assetRequestList.size(); j++) {
                            AssetRequestEntity ar1 = assetRequestList.get(j);
                            if (ar.getAssetRequestType().equals(ar1.getAssetRequestType()) && ar.getAssetName().equals(ar1.getAssetName()) && ar1.getStatus().equals("Processing")) {
                                qty = qty + ar1.getQty();
                                assignAR.add(ar1);
                            }
                        }
                    }
                    PurchaseRequestEntity pr = submitPurchaseRequest(ar.getAssetName(), ar.getAssetRequestType(), qty);
                    if (pr != null) {
                        if (pr.getAssetRequests() == null) {
                            pr.setAssetRequests(assignAR);
                            em.merge(pr);
                        } else {
                            ArrayList<AssetRequestEntity> requestList = new ArrayList<AssetRequestEntity>(pr.getAssetRequests());
                            for (int b = 0; b < assignAR.size(); b++) {
                                requestList.add(assignAR.get(b));
                            }
                            pr.setAssetRequests(requestList);
                            em.merge(pr);
                        }
                        for (int a = 0; a < assignAR.size(); a++) {
                            AssetRequestEntity ar2 = assignAR.get(a);
                            ar2.setStatus("Order Sent");
                            ar2.setPurchaseRequest(pr);
                            em.merge(ar2);
                        }
                    }// end of if
                }
            }//end of for loop
        }
    }

    @Override
    public PurchaseRequestEntity submitPurchaseRequest(String assetName, String assetType, int qty) {
        try {
            Query q = em.createQuery("SELECT p FROM PurchaseRequestEntity AS p");
            int rows = q.getResultList().size();
            String purchaseRequestId = "PR" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT p1 FROM PurchaseRequestEntity AS p1 WHERE p1.purchaseRequestId=:purchaseRequestId");
                q1.setParameter("purchaseRequestId", purchaseRequestId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                purchaseRequestId = "PR" + rows;
            }
            ArrayList<PurchaseRequestEntity> purchaseRequestList = getPurchaseRequestList();
            boolean sta = true;
            for (int i = 0; i < purchaseRequestList.size(); i++) {
                PurchaseRequestEntity pr = purchaseRequestList.get(i);
                if (pr.getCategory().equals(assetType) && pr.getItemName().equals(assetName) && pr.getStatus().equals("Received")) {
                    pr.setQty(pr.getQty() + qty);
                    em.merge(pr);
                    sta = false;
                    return pr;
                }
            }
            if (sta) {
                purchaseRequest = new PurchaseRequestEntity(purchaseRequestId, null, qty, null, "Received", 0, assetType, assetName, null, 0);
                em.persist(purchaseRequest);
                return purchaseRequest;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return null;
    }

    @Override
    public PurchaseRequestEntity searchPurchaseReq(String purchaseRequestId) {
        PurchaseRequestEntity pr = new PurchaseRequestEntity();
        try {
            Query q = em.createQuery("SELECT pr FROM PurchaseRequestEntity AS pr WHERE pr.purchaseRequestId=:purchaseRequestId");
            q.setParameter("purchaseRequestId", purchaseRequestId);
            pr = (PurchaseRequestEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return pr;
    }

    @Override
    public boolean updatePurchaseRequest(String purchaseRequestId, String description, double maxBidAmount, Date reqDeadline, String emailAddress) {
        Date now = new Date();
        PurchaseRequestEntity pr = searchPurchaseReq(purchaseRequestId);
        if (em.contains(pr)) {
            EmailManager emailManager = new EmailManager();
            pr.setDescription(description);
            pr.setMaxBidAmount(maxBidAmount);
            Timestamp deadline = new Timestamp(reqDeadline.getTime());
            Timestamp today = new Timestamp(now.getTime());
            pr.setReqDeadline(deadline);
            pr.setReqDate(today);
            pr.setStatus("Pending");
            pr.setInitalBidAmount(maxBidAmount);
            em.merge(pr);

            ArrayList<BusinessPartnerEntity> bpList = getPartnerList();
            for (int i = 0; i < bpList.size(); i++) {
                BusinessPartnerEntity bb = bpList.get(i);
                if (bb.isSubscription() == true) {
                    emailManager.sendSubscriptionEmail(bb.getCompany(), pr.getItemName(), pr.getMaxBidAmount(), pr.getQty(), pr.getReqDeadline(), bb.getEmailAddress(), emailAddress);
                    String message = "We have a new listing for " + pr.getItemName() + " . ";
                    NotificationEntity no = new NotificationEntity("We have a new listing!!!", message, today, false);
                    no.setBusinessPartner(bb);
                    ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(bb.getNotifications());
                    notiList.add(no);
                    bb.setNotifications(notiList);
                    em.merge(no);
                    em.merge(bb);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean addBid(String partnerId, double currentBid, String purchaseRequestId, String remarks) {
        try {
            Query q = em.createQuery("SELECT b FROM BidEntity AS b");
            int rows = q.getResultList().size();
            String bidId = "B" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT b1 FROM BidEntity AS b1 WHERE b1.bidId=:bidId");
                q1.setParameter("bidId", bidId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                bidId = "B" + rows;
            }

            //Create a New bid
            Date now = new Date();
            Timestamp bidDate = new Timestamp(now.getTime());
            BidEntity bid = new BidEntity(bidId, bidDate, currentBid, "Submitted", remarks);
            em.persist(bid);
            //Map bid to Business Partner
            BusinessPartnerEntity partner = searchPartner(partnerId);
            if (em.contains(partner)) {
                bid.setBusinessPartner(partner);
                em.merge(bid);
            }

            //Map bid to purchase Request
            PurchaseRequestEntity pr = searchPurchaseReq(purchaseRequestId);
            if (em.contains(pr)) {
                pr.setMaxBidAmount(currentBid);
                em.merge(pr);
                bid.setPurchaseRequest(pr);
                em.merge(bid);
            }

            //Send notification to other bidders 
            ArrayList<BusinessPartnerEntity> partnerList = searchPRThroughBid(pr.getPurchaseRequestId(), partnerId);
            if (partnerList.isEmpty()) {
                //Do Nothing
            } else {
                for (int i = 0; i < partnerList.size(); i++) {
                    BusinessPartnerEntity bb = partnerList.get(i);
                    String message = "The current bid amount is $" + currentBid + " for " + pr.getItemName();
                    NotificationEntity no = new NotificationEntity("You have been outbidded!!!", message, bidDate, false);
                    ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(bb.getNotifications());
                    notiList.add(no);
                    bb.setNotifications(notiList);
                    no.setBusinessPartner(bb);
                    em.merge(no);
                    em.merge(bb);
                }
            }
            return true;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public BusinessPartnerEntity searchPartner(String partnerId) {
        BusinessPartnerEntity b = new BusinessPartnerEntity();
        try {
            Query q = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b WHERE b.partnerId=:partnerId");
            q.setParameter("partnerId", partnerId);
            b = (BusinessPartnerEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return b;
    }

    @Override
    public ArrayList<NotificationEntity> getNotifications(String partnerId) {
        ArrayList<NotificationEntity> notiList = getNotificationList();
        ArrayList<NotificationEntity> newList = new ArrayList<NotificationEntity>();
        for (int i = 0; i < notiList.size(); i++) {
            if (notiList.get(i).getBusinessPartner().getPartnerId().equals(partnerId)) {
                newList.add(notiList.get(i));
            }
        }
        Collections.sort(newList, new Comparator<NotificationEntity>() {
            @Override
            public int compare(NotificationEntity left, NotificationEntity right) {
                return right.getDateAndTime().compareTo(left.getDateAndTime());
            }
        });
        return newList;
    }

    @Override
    public boolean deleteBid(String bidId) {
        BidEntity b = searchBid(bidId);
        if (em.contains(b)) {
            b.setStatus("Withdrawn");
            String purchaseReqId = b.getPurchaseRequest().getPurchaseRequestId();
            em.merge(b);

            //Get the purchaseRequest Entity
            PurchaseRequestEntity p = searchPurchaseReq(purchaseReqId);

            //If is last bid for the PR, need to set PR amount to the inital amount or if is not last bid, need set the list to highest bid amount
            ArrayList<BidEntity> bidlist = getBidList();
            ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();

            //Get the bid list for the purchase request
            for (int i = 0; i < bidlist.size(); i++) {
                BidEntity bid = searchBid(bidlist.get(i).getBidId());
                if (!(bid.getBusinessPartner().getPartnerId().equals(b.getBusinessPartner().getPartnerId()))) {
                    if (bid.getPurchaseRequest().getPurchaseRequestId().equals(p.getPurchaseRequestId())) {
                        newbidlist.add(bid);
                    }
                }
            }

            if (newbidlist.isEmpty()) {
                p.setMaxBidAmount(p.getInitalBidAmount());
            } else {
                //from the newbidlist check
                double maxAmount = newbidlist.get(0).getBidPrice();
                for (int i = 0; i < newbidlist.size(); i++) {
                    BidEntity bid = newbidlist.get(i);
                    if (bid.getBidPrice() < maxAmount) {
                        maxAmount = bid.getBidPrice();
                    }
                }
                p.setMaxBidAmount(maxAmount);
                em.merge(p);
            }
            em.flush();

            return true;
        }
        return false;
    }

    @Override
    public BidEntity searchBid(String bidId) {
        BidEntity b = new BidEntity();
        try {
            Query q = em.createQuery("SELECT b FROM BidEntity AS b WHERE b.bidId=:bidId");
            q.setParameter("bidId", bidId);
            b = (BidEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return b;
    }

    public ArrayList<BusinessPartnerEntity> searchPRThroughBid(String PurchaseRequestId, String partnerId) {
        purchaseRequest = searchPurchaseReq(PurchaseRequestId);
        ArrayList<BusinessPartnerEntity> bidList = new ArrayList<BusinessPartnerEntity>();
        Query query = em.createQuery("SELECT DISTINCT b.businessPartner FROM BidEntity AS b WHERE b.purchaseRequest=:purchaseRequest");
        query.setParameter("purchaseRequest", purchaseRequest);
        for (Object o : query.getResultList()) {
            BusinessPartnerEntity b = (BusinessPartnerEntity) o;
            if (!(b.getPartnerId().equals(partnerId))) {
                bidList.add(b);
            }
        }
        return bidList;
    }

    @Override
    public ArrayList<BusinessPartnerEntity> getDistinctPartner(String PurchaseRequestId) {
        purchaseRequest = searchPurchaseReq(PurchaseRequestId);
        ArrayList<BusinessPartnerEntity> bidList = new ArrayList<BusinessPartnerEntity>();
        Query query = em.createQuery("SELECT DISTINCT b.businessPartner FROM BidEntity AS b WHERE b.purchaseRequest=:purchaseRequest");
        query.setParameter("purchaseRequest", purchaseRequest);
        for (Object o : query.getResultList()) {
            BusinessPartnerEntity b = (BusinessPartnerEntity) o;
            bidList.add(b);
        }
        return bidList;
    }

    //To remove exiting partner that are unsuccessful (Cater to repost)
    public ArrayList<BusinessPartnerEntity> getDistinctPartner1(String PurchaseRequestId) {
        purchaseRequest = searchPurchaseReq(PurchaseRequestId);
        ArrayList<BusinessPartnerEntity> bidList = new ArrayList<BusinessPartnerEntity>();
        Query query = em.createQuery("SELECT DISTINCT b.businessPartner FROM BidEntity AS b WHERE b.purchaseRequest=:purchaseRequest AND b.status!='Unsuccessful' ");
        query.setParameter("purchaseRequest", purchaseRequest);
        for (Object o : query.getResultList()) {
            BusinessPartnerEntity b = (BusinessPartnerEntity) o;
            bidList.add(b);
        }
        return bidList;
    }

    @Override
    public void checkPR() {
        ArrayList<PurchaseRequestEntity> purchaseList = getPurchaseRequestList();
        if (purchaseList.isEmpty()) {
            //Do Nothing
        } else {
            for (int i = 0; i < purchaseList.size(); i++) {
                PurchaseRequestEntity p = searchPurchaseReq(purchaseList.get(i).getPurchaseRequestId());
                //check the deadline
                if (em.contains(p)) {
                    if (p.getReqDeadline() != null && p.getStatus().equals("Pending")) {
                        boolean deadline = compareDate(p.getReqDeadline());
                        if (deadline == false) {

                            ArrayList<BidEntity> bidlist = getBidList();
                            ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();

                            //Get the bid list for the purchase request
                            for (int j = 0; j < bidlist.size(); j++) {
                                BidEntity bid = bidlist.get(j);
                                if (bid.getPurchaseRequest().getPurchaseRequestId().equals(p.getPurchaseRequestId()) && (bid.getStatus().equals("Submitted"))) {
                                    newbidlist.add(bid);
                                }
                            }

                            if (newbidlist.isEmpty()) {
                                p.setStatus("Not Fulfilled");
                                em.merge(p);
                            } else {
                                //from the newbidlist check
                                p.setStatus("Evaluating");
                                for (int j = 0; j < newbidlist.size(); j++) {
                                    BidEntity bid = newbidlist.get(j);
                                    bid.setStatus("Pending");
                                    em.merge(bid);
                                }
                                em.merge(p);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean compareDate(Date closeDate) {
        Date todayDate = new Date();
        if (todayDate.compareTo(closeDate) > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean goChooseBid(String bidId, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Timestamp notiDate = new Timestamp(now.getTime());
        BidEntity b = searchBid(bidId);
        if (em.contains(b)) {
            b.setStatus("Successful");
            em.persist(b);
            PurchaseRequestEntity p = searchPurchaseReq(b.getPurchaseRequest().getPurchaseRequestId());

            //Send email to inform successful Bid
            emailManager.sendBidEmail(b.getPurchaseRequest().getItemName(), b.getBusinessPartner().getCompany(), b.getBusinessPartner().getEmailAddress(), emailAddress);

            //Create a delivery Entity
            DeliveryEntity d = createDelivery(b);
            d.setBid(b);
            em.merge(d);

            String message = "Please confirm the delivery of the " + b.getPurchaseRequest().getItemName() + " ASAP!";
            NotificationEntity no = new NotificationEntity("Congratulations, you have won the bid for " + b.getPurchaseRequest().getItemName(), message, notiDate, false);
            //Map notification to business partner and vice versa
            no.setBusinessPartner(b.getBusinessPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(b.getBusinessPartner().getNotifications());
            notiList.add(no);
            b.getBusinessPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(b.getBusinessPartner());

            ArrayList<BidEntity> bidlist = getBidList();
            ArrayList<BidEntity> newbidlist = new ArrayList<BidEntity>();

            //Get the bid list for the purchase request
            for (int j = 0; j < bidlist.size(); j++) {
                BidEntity bid = bidlist.get(j);
                if (bid.getPurchaseRequest().getPurchaseRequestId().equals(p.getPurchaseRequestId()) && !bid.getStatus().equals("Withdrawn")) {
                    newbidlist.add(bid);
                }
            }

            if (!(newbidlist.isEmpty())) {
                //set the rest of the bid to unsuccessful
                for (int i = 0; i < newbidlist.size(); i++) {
                    BidEntity bid = newbidlist.get(i);
                    if (bid.getBidId().equals(bidId)) {
                        //Do nothing
                    } else {
                        String message1 = "Please be informed that your bid for " + b.getPurchaseRequest().getItemName() + " is unsuccessful.";
                        NotificationEntity no1 = new NotificationEntity("Unsuccessful bid for " + b.getPurchaseRequest().getItemName(), message1, notiDate, false);
                        bid.setStatus("Unsuccessful");
                        em.merge(bid);
                        no1.setBusinessPartner(bid.getBusinessPartner());
                        ArrayList<NotificationEntity> notificationList = new ArrayList<NotificationEntity>(bid.getBusinessPartner().getNotifications());
                        notificationList.add(no1);
                        bid.getBusinessPartner().setNotifications(notificationList);
                        em.merge(no1);
                        em.merge(bid.getBusinessPartner());

                        //Send email to inform unsuccessful Bid
                        emailManager.sendBidEmail1(b.getPurchaseRequest().getItemName(), bid.getBusinessPartner().getCompany(), bid.getBusinessPartner().getEmailAddress(), emailAddress);
                    }
                }
            }

            p.setStatus("Waiting for Delivery");
            em.merge(p);
            return true;
        }
        return false;
    }

    public ArrayList<NotificationEntity> getNotificationList() {
        ArrayList<NotificationEntity> notificationList = new ArrayList<NotificationEntity>();
        Query query = em.createQuery("SELECT n FROM NotificationEntity AS n");
        for (Object o : query.getResultList()) {
            notificationList.add((NotificationEntity) o);
        }
        return notificationList;
    }

    public DeliveryEntity createDelivery(BidEntity b) {
        try {
            DeliveryEntity d = new DeliveryEntity(null, "Not Scheduled");
            return d;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<BidEntity> getBidList() {
        ArrayList<BidEntity> bidList = new ArrayList<BidEntity>();
        Query query = em.createQuery("SELECT b FROM BidEntity AS b");
        for (Object o : query.getResultList()) {
            bidList.add((BidEntity) o);
        }
        return bidList;
    }

    @Override
    public ArrayList<BusinessPartnerEntity> getPartnerList() {
        ArrayList<BusinessPartnerEntity> partnerList = new ArrayList<BusinessPartnerEntity>();
        Query query = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b");
        for (Object o : query.getResultList()) {
            partnerList.add((BusinessPartnerEntity) o);
        }
        return partnerList;
    }

    @Override
    public boolean goDeletePR(String purchaseRequestId) {
        PurchaseRequestEntity p = searchPurchaseReq(purchaseRequestId);
        if (em.contains(p)) {
            ArrayList<AssetRequestEntity> arList = new ArrayList<AssetRequestEntity>(p.getAssetRequests());
            for (int i = 0; i < arList.size(); i++) {
                AssetRequestEntity a = arList.get(i);
                a.setStatus("Received");
                a.setPurchaseRequest(null);
                em.merge(a);
            }
            p.setStatus("Deleted");
            em.merge(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean goRejectAll(String purchaseRequestId, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Timestamp notiDate = new Timestamp(now.getTime());
        ArrayList<BidEntity> bidlist = getBidList();

        String itemname = "";
        for (int i = 0; i < bidlist.size(); i++) {
            BidEntity b = searchBid(bidlist.get(i).getBidId());
            if (b.getPurchaseRequest().getPurchaseRequestId().equals(purchaseRequestId)) {
                b.setStatus("Unsuccessful");
                em.merge(b);
                itemname = b.getPurchaseRequest().getItemName();
                String message = "Please be informed that your bid for " + itemname + " is unsuccessful.";
                NotificationEntity no = new NotificationEntity("Unsuccessful bid for " + itemname, message, notiDate, false);
                no.setBusinessPartner(b.getBusinessPartner());
                ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(b.getBusinessPartner().getNotifications());
                notiList.add(no);
                b.getBusinessPartner().setNotifications(notiList);
                em.merge(no);
                em.merge(b.getBusinessPartner());
                emailManager.sendBidEmail1(itemname, b.getBusinessPartner().getCompany(), b.getBusinessPartner().getEmailAddress(), emailAddress);

            }
        }

        PurchaseRequestEntity p = searchPurchaseReq(purchaseRequestId);
        p.setStatus("Not Fulfilled");
        p.setMaxBidAmount(p.getInitalBidAmount());
        em.merge(p);
        return true;
    }

    @Override
    public ArrayList<DeliveryEntity> getDeliveryList(String partnerId) {
        ArrayList<DeliveryEntity> deliveryList = new ArrayList<DeliveryEntity>();
        Query query = em.createQuery("SELECT d FROM DeliveryEntity AS d");
        for (Object o : query.getResultList()) {
            DeliveryEntity d = (DeliveryEntity) o;
            if (d.getBid().getBusinessPartner().getPartnerId().equals(partnerId)) {
                deliveryList.add(d);
            }
        }
        return deliveryList;
    }

    @Override
    public ArrayList<DeliveryEntity> getDeliveryList() {
        ArrayList<DeliveryEntity> deliveryList = new ArrayList<DeliveryEntity>();
        Query query = em.createQuery("SELECT d FROM DeliveryEntity AS d");
        for (Object o : query.getResultList()) {
            DeliveryEntity d = (DeliveryEntity) o;
            deliveryList.add(d);
        }
        return deliveryList;
    }

    @Override
    public boolean scheduleDelivery(Long deliveryId, Date d) {
        EmailManager emailManager = new EmailManager();
        Timestamp deliveryDate = new Timestamp(d.getTime());
        DeliveryEntity de = searchDelivery(deliveryId);
        if (em.contains(de)) {

            if (de.getDateAndTime() == null) {
                de.setDateAndTime(deliveryDate);
                de.setStatus("Scheduled");
                em.merge(de);

                //Change the status of Purchase Request
                de.getBid().getPurchaseRequest().setStatus("Delivery Scheduled");
                em.merge(de.getBid().getPurchaseRequest());

                //Change the status of Asset Request
                ArrayList<AssetRequestEntity> assetList = new ArrayList<AssetRequestEntity>(de.getBid().getPurchaseRequest().getAssetRequests());
                for (int i = 0; i < assetList.size(); i++) {
                    assetList.get(i).setStatus("Delivery Scheduled");
                    em.merge(assetList.get(i));
                }

                //Send email to inform I&A for the schedule of delivery
                emailManager.sendDeliveryEmail(de.getBid().getPurchaseRequest().getPurchaseRequestId(), de.getBid().getPurchaseRequest().getItemName(), deliveryDate, de.getBid().getBusinessPartner().getCompany(), de.getBid().getBusinessPartner().getEmailAddress(), "e0002468@u.nus.edu");
            } //Re schedule deliveryDate
            else {
                de.setDateAndTime(deliveryDate);
                em.merge(de);
                //Send email to inform I&A for the change of schedule of delivery
                emailManager.sendDeliveryEmail1(de.getBid().getPurchaseRequest().getPurchaseRequestId(), de.getBid().getPurchaseRequest().getItemName(), deliveryDate, de.getBid().getBusinessPartner().getCompany(), de.getBid().getBusinessPartner().getEmailAddress(), "e0002468@u.nus.edu");
            }
            return true;
        }
        return false;
    }

    @Override
    public DeliveryEntity searchDelivery(Long deliveryId) {
        DeliveryEntity b = new DeliveryEntity();

        try {
            Query q = em.createQuery("SELECT d FROM DeliveryEntity AS d WHERE d.deliveryId=:deliveryId");
            q.setParameter("deliveryId", deliveryId);
            b = (DeliveryEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return b;
    }

    @Override
    public boolean completePR(String purchaseRequestId, ArrayList<String> selectedAssetRequests) {
        PurchaseRequestEntity p = searchPurchaseReq(purchaseRequestId);
        int size = 0;
        if (em.contains(p)) {
            ArrayList<AssetRequestEntity> arList = new ArrayList<AssetRequestEntity>(p.getAssetRequests());
            for (int i = 0; i < arList.size(); i++) {
                if (arList.get(i).getStatus().equals("Delivery Scheduled")) {
                    size++;
                }
            }
            if (selectedAssetRequests.size() == size) {
                p.setStatus("Completed");
                ArrayList<BidEntity> bidlist = getBidList();
                for (int i = 0; i < bidlist.size(); i++) {
                    BidEntity bid = bidlist.get(i);
                    if (bid.getPurchaseRequest().getPurchaseRequestId().equals(p.getPurchaseRequestId()) && bid.getStatus().equals("Successful")) {
                        ArrayList<DeliveryEntity> deliveryList = getDeliveryList();
                        for (int j = 0; j < deliveryList.size(); j++) {
                            DeliveryEntity delivery = deliveryList.get(j);
                            if (delivery.getBid().getBidId().equals(bid.getBidId())) {
                                delivery.setStatus("Completed");
                                em.merge(delivery);
                            }
                        }

                    }
                }
                em.merge(p);
            }

            for (int i = 0; i < selectedAssetRequests.size(); i++) {
                String arId = selectedAssetRequests.get(i).substring(0, selectedAssetRequests.get(i).indexOf(" "));
                AssetRequestEntity a = infraAssetSessionBeanLocal.searchAssetReq(arId);
                boolean sta = infraAssetSessionBeanLocal.updateAssetRequest(arId, null, a.getQty(), "Completed");
                if (sta) {

                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean requestLeasing(String assetId, int leaseYear, String partnerId) {
        try {
            Query q = em.createQuery("SELECT lc FROM LeasingContractEntity AS lc");
            int rows = q.getResultList().size();
            String leasingContractId = "LC" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT lc1 FROM LeasingContractEntity AS lc1 WHERE lc1.leasingContractId=:leasingContractId");
                q1.setParameter("leasingContractId", leasingContractId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                leasingContractId = "LC" + rows;
            }

            //Create a New contract
            Date now = new Date();
            Timestamp submittedDate = new Timestamp(now.getTime());
            LeasingContractEntity lc = new LeasingContractEntity(leasingContractId, submittedDate, null, null, null, leaseYear, 0, "Submitted", false, null);
            em.persist(lc);
            //Map contract to Business Partner
            BusinessPartnerEntity partner = searchPartner(partnerId);
            if (em.contains(partner)) {
                lc.setPartner(partner);
                em.merge(lc);
            }

            //Map contract to Leasing Space
            LeasingSpaceEntity space = (LeasingSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
            if (em.contains(space)) {
                lc.setLeasingSpace(space);
                em.merge(lc);
            }

            return true;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }
    
    //FOR RENEWAL
    public boolean requestLeasing1(String assetId, int leaseYear, String partnerId) {
        try {
            Query q = em.createQuery("SELECT lc FROM LeasingContractEntity AS lc");
            int rows = q.getResultList().size();
            String leasingContractId = "LC" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT lc1 FROM LeasingContractEntity AS lc1 WHERE lc1.leasingContractId=:leasingContractId");
                q1.setParameter("leasingContractId", leasingContractId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                leasingContractId = "LC" + rows;
            }

            //Create a New contract
            Date now = new Date();
            Timestamp submittedDate = new Timestamp(now.getTime());
            LeasingContractEntity lc = new LeasingContractEntity(leasingContractId, submittedDate, null, null, null, leaseYear, 0, "Need Approval", false, null);
            em.persist(lc);
            //Map contract to Business Partner
            BusinessPartnerEntity partner = searchPartner(partnerId);
            if (em.contains(partner)) {
                lc.setPartner(partner);
                em.merge(lc);
            }

            //Map contract to Leasing Space
            LeasingSpaceEntity space = (LeasingSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
            if (em.contains(space)) {
                lc.setLeasingSpace(space);
                em.merge(lc);
            }

            return true;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<LeasingContractEntity> getLeasingContract() {
        ArrayList<LeasingContractEntity> contractList = new ArrayList<LeasingContractEntity>();
        Query query = em.createQuery("SELECT lc FROM LeasingContractEntity AS lc");
        for (Object o : query.getResultList()) {
            contractList.add((LeasingContractEntity) o);
        }
        return contractList;
    }

    @Override
    public ArrayList<LeasingContractEntity> getLeasingRequestList(String partnerId) {
        ArrayList<LeasingContractEntity> contractList = getLeasingContract();
        ArrayList<LeasingContractEntity> newList = new ArrayList<LeasingContractEntity>();

        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getPartner().getPartnerId().equals(partnerId)) {
                newList.add(contractList.get(i));
            }
        }
        return newList;
    }

    @Override
    public LeasingContractEntity searchLeasingContract(String leasingContractId) {
        LeasingContractEntity lc = new LeasingContractEntity();
        try {
            Query q = em.createQuery("SELECT l FROM LeasingContractEntity AS l WHERE l.leasingContractId=:leasingContractId");
            q.setParameter("leasingContractId", leasingContractId);
            lc = (LeasingContractEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return lc;
    }

    @Override
    public boolean goEditMyLeasing(String leasingContractId, int leaseYears) {
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            lc.setYearsOfContract(leaseYears);
            return true;
        }
        return false;
    }

    @Override
    public boolean goDeleteLC(String leasingContractId) {
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            lc.setLeasingSpace(null);
            lc.setPartner(null);
            em.remove(lc);
            em.flush();
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<LeasingContractEntity> getLeasingRequestList1(String partnerId) {
        ArrayList<LeasingContractEntity> contractList = getLeasingRequestList(partnerId);
        ArrayList<LeasingContractEntity> newList = new ArrayList<LeasingContractEntity>();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getStatus().equals("On Contract")) {
                newList.add(contractList.get(i));
            }
        }
        return newList;
    }

    @Override
    public ArrayList<LeasingContractEntity> getLeasingRequestList2(String partnerId) {
        ArrayList<LeasingContractEntity> contractList = getLeasingRequestList(partnerId);
        ArrayList<LeasingContractEntity> newList = new ArrayList<LeasingContractEntity>();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getStatus().equals("End of Contract") || contractList.get(i).getStatus().equals("Terminated")) {
                newList.add(contractList.get(i));
            }
        }
        return newList;
    }

    @Override
    public void checkLeasing() {
        //get the leasing list that got submitter --> status is submitted
        ArrayList<LeasingSpaceEntity> leasingList = getLeasingList1();
        ArrayList<LeasingSpaceEntity> newList = new ArrayList<LeasingSpaceEntity>();
        ArrayList<LeasingContractEntity> contractList = getLeasingContract();
        if (leasingList.isEmpty()) {
            //Do Nothing
        } else {

            if (!(leasingList.isEmpty())) {
                //Loop the whole leasing space list
                for (int i = 0; i < leasingList.size(); i++) {
                    LeasingSpaceEntity ls = leasingList.get(i);
                    if (ls.getDeadline() != null) {
                        boolean deadline = compareDate(ls.getDeadline());
                        if (deadline == false && ls.getStatus().equals("Available")) {
                            ls.setStatus("Pending");
                            ls.setDeadline(null);
                            em.merge(ls);
                            newList.add(ls);
                        }
                    }
                }
            }

            if (!(newList.isEmpty())) {
                //New list = the list where all the leasing space close date reached
                for (int i = 0; i < newList.size(); i++) {
                    LeasingSpaceEntity ls = newList.get(i);
                    for (int j = 0; j < contractList.size(); j++) {
                        LeasingContractEntity lc = contractList.get(j);
                        //check the contract whether got that leasing space
                        if (lc.getLeasingSpace().getAssetId().equals(ls.getAssetId()) && lc.getStatus().equals("Submitted")) {
                            lc.setStatus("Pending");
                            em.merge(lc);
                        }
                    }
                }
            }
        }

        ArrayList<LeasingSpaceEntity> wholeList = infraAssetSessionBeanLocal.getLeasingSpace();
        for (int i = 0; i < wholeList.size(); i++) {
            boolean status = true;
            //Lease List that got submitter
            for (int j = 0; j < leasingList.size(); j++) {
                if (wholeList.get(i).getAssetId().equals(leasingList.get(j).getAssetId())) {
                    status = false;
                }
            }

            //Not inside the list. (Dont have submitter)
            if (status) {
                if (wholeList.get(i).getDeadline() != null) {
                    boolean deadline = compareDate(wholeList.get(i).getDeadline());
                    if (deadline == false) {
                        wholeList.get(i).setStatus("Available");
                        wholeList.get(i).setDeadline(null);
                        em.merge(wholeList.get(i));
                    }
                }
            }
        }
    }

    //Get all the leasing space that got submitter 
    @Override
    public ArrayList<LeasingSpaceEntity> getLeasingList() {
        ArrayList<LeasingSpaceEntity> leaseList = new ArrayList<LeasingSpaceEntity>();
        Query query = em.createQuery("SELECT DISTINCT l.leasingSpace FROM LeasingContractEntity AS l ");
        for (Object o : query.getResultList()) {
            leaseList.add((LeasingSpaceEntity) o);
        }
        return leaseList;
    }

    public ArrayList<LeasingSpaceEntity> getLeasingList1() {
        ArrayList<LeasingSpaceEntity> leaseList = new ArrayList<LeasingSpaceEntity>();
        Query query = em.createQuery("SELECT DISTINCT l.leasingSpace FROM LeasingContractEntity AS l WHERE l.status='Submitted' ");
        for (Object o : query.getResultList()) {
            leaseList.add((LeasingSpaceEntity) o);
        }
        return leaseList;
    }

    @Override
    public boolean goChooseLeaseSpace(String leasingContractId, String emailAddress, Date startDate) {
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Date newDate = addDays(now, 6);
        Date newDate1 = getZeroTimeDate(newDate);
        Timestamp notiDate = new Timestamp(newDate1.getTime());
        Timestamp today = new Timestamp(now.getTime());
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            Timestamp start = new Timestamp(startDate.getTime());
            lc.setStatus("Successful");
            double amount = lc.getLeasingSpace().getRentalFee() * 3;
            lc.setDeposit(amount);
            lc.setSignedDate(notiDate);
            lc.setStartDate(start);
            int days = lc.getYearsOfContract() * 365;
            Date endDate = addDays(start, days);
            Timestamp end = new Timestamp(endDate.getTime());
            lc.setEndDate(end);
            em.merge(lc);
            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();

            //Send email to inform successful lease space
            emailManager.sendLeaseSpaceEmail(node.getCode(), lc.getLeasingSpace().getUnitNumber(), notiDate, start, amount, lc.getPartner().getCompany(), lc.getPartner().getEmailAddress(), emailAddress);

            //Send notification to inform successful lease space
            String message = "Congratulations, your request for " + node.getCode() + " : " + lc.getLeasingSpace().getUnitNumber() + " is successful.";
            NotificationEntity no = new NotificationEntity("Congratulations!", message, today, false);
            no.setBusinessPartner(lc.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(lc.getPartner().getNotifications());
            notiList.add(no);
            lc.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(lc.getPartner());

            //send email to inform unsuccessful lease space and set the status to unsuccessful
            String lsId = lc.getLeasingSpace().getAssetId();
            ArrayList<LeasingContractEntity> contractList = getLeasingContract();
            if (!(contractList.isEmpty())) {
                for (int i = 0; i < contractList.size(); i++) {
                    LeasingContractEntity lc1 = contractList.get(i);
                    if (lc1.getLeasingSpace().getAssetId().equals(lsId) && !(lc1.getLeasingContractId().equals(lc.getLeasingContractId()))) {
                        if (lc1.getStatus().equals("Pending")) {
                            lc1.setStatus("Unsuccessful");
                            if (!(lc1.getPartner().equals(lc.getPartner()))) {
                                emailManager.sendLeaseSpaceEmail1(node.getCode(), lc1.getLeasingSpace().getUnitNumber(), lc1.getPartner().getCompany(), lc1.getPartner().getEmailAddress(), emailAddress);

                                //Send notification to inform successful lease space
                                String message1 = "We are sorry to inform you that your request for " + node.getCode() + " : " + lc.getLeasingSpace().getUnitNumber() + " is unsuccessful.";
                                NotificationEntity no1 = new NotificationEntity("We are sorry to inform you", message1, today, false);
                                no1.setBusinessPartner(lc1.getPartner());
                                ArrayList<NotificationEntity> notiList1 = new ArrayList<NotificationEntity>(lc1.getPartner().getNotifications());
                                notiList1.add(no1);
                                lc1.getPartner().setNotifications(notiList1);
                                em.merge(no1);
                                em.merge(lc1.getPartner());

                            }
                            em.merge(lc1);
                        }
                    }
                }
            }

            LeasingSpaceEntity space = (LeasingSpaceEntity) infraAssetSessionBeanLocal.searchAsset(lsId);
            space.setStatus("In Progress");
            em.merge(space);

            return true;
        }
        return false;
    }

    public static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date = calendar.getTime();

        return date;
    }

    @Override
    public boolean completedSigningLeaseContract(String leasingContractId) {
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            lc.setSigningOfAgreement(true);
            lc.setStatus("On Contract");
            LeasingSpaceEntity space = (LeasingSpaceEntity) infraAssetSessionBeanLocal.searchAsset(lc.getLeasingSpace().getAssetId());
            space.setStatus("On Contract");
            em.merge(lc);
            em.merge(space);
            return true;
        }
        return false;
    }

    @Override
    public boolean editSigningLeaseContract(String leasingContractId, Date signingDate, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            Timestamp sdate = new Timestamp(signingDate.getTime());
            lc.setSignedDate(sdate);
            em.merge(lc);
            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();
            emailManager.sendSigningEmail(node.getCode(), lc.getLeasingSpace().getUnitNumber(), sdate, lc.getPartner().getCompany(), lc.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean goUpdateBid(String bidId, double currentBid, String remarks) {
        BidEntity b = searchBid(bidId);
        if (em.contains(b)) {
            PurchaseRequestEntity pr = searchPurchaseReq(b.getPurchaseRequest().getPurchaseRequestId());
            pr.setMaxBidAmount(currentBid);
            b.setBidPrice(currentBid);
            b.setRemarks(remarks);
            em.flush();
            return true;

        }
        return false;
    }

    @Override
    public boolean markRead(ArrayList<NotificationEntity> notifications) {
        for (int i = 0; i < notifications.size(); i++) {
            NotificationEntity noti = searchNotification(notifications.get(i).getNotificationId());
            if (em.contains(noti)) {
                noti.setCheckRead(true);
                em.flush();
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean markUnread(ArrayList<NotificationEntity> notifications) {
        for (int i = 0; i < notifications.size(); i++) {
            NotificationEntity noti = searchNotification(notifications.get(i).getNotificationId());
            if (em.contains(noti)) {
                noti.setCheckRead(false);
                em.flush();
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public NotificationEntity searchNotification(int notificationId) {
        NotificationEntity n = new NotificationEntity();
        try {
            Query q = em.createQuery("SELECT n FROM NotificationEntity AS n WHERE n.notificationId=:notificationId");
            q.setParameter("notificationId", notificationId);
            n = (NotificationEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return n;
    }

    @Override
    public FittingOutReqEntity searchFittingOut(String contractId) {
        LeasingContractEntity leasingContract = searchLeasingContract(contractId);
        ArrayList<FittingOutReqEntity> fittingList = new ArrayList<FittingOutReqEntity>();
        Query q = em.createQuery("SELECT n FROM FittingOutReqEntity AS n ");
        for (Object o : q.getResultList()) {
            FittingOutReqEntity f = (FittingOutReqEntity) o;
            if (f.getLeasingContract().equals(leasingContract)) {
                fittingList.add(f);
            }
        }
        if (fittingList.isEmpty()) {
            return null;
        } else {
            return fittingList.get(0);
        }
    }

    @Override
    public boolean updateFittingRequest(String decision, String duration, String contractId) {
        EmailManager emailManager = new EmailManager();
        LeasingContractEntity leasingContract = searchLeasingContract(contractId);
        if (decision.equals("No")) {
            if (em.contains(leasingContract)) {
                leasingContract.setFittingRequest("Completed");
                em.flush();
                return true;
            }
        } else {
            if (em.contains(leasingContract)) {
                double deposit = 3000;
                Date today = new Date();
                Date endDate = new Date();
                if (duration.equals("2 Weeks")) {
                    endDate = addDays(leasingContract.getStartDate(), 14);
                } else if (duration.equals("3 Weeks")) {
                    endDate = addDays(leasingContract.getStartDate(), 21);
                } else { //one month
                    endDate = addDays(leasingContract.getStartDate(), 28);
                }
                Timestamp end = new Timestamp(endDate.getTime());
                Timestamp todayDate = new Timestamp(today.getTime());
                FittingOutReqEntity fittingOut = new FittingOutReqEntity(duration, deposit, "Submitted", leasingContract.getStartDate(), end, todayDate, null);
                em.persist(fittingOut);
                fittingOut.setLeasingContract(leasingContract);
                leasingContract.setFittingRequest("Completed");
                String emailAddress = "e0002468@u.nus.edu";
                NodeEntity node = (NodeEntity) leasingContract.getLeasingSpace().getInfrastructure();

                emailManager.sendFittingEmail(leasingContract.getPartner().getCompany(), node.getCode(), leasingContract.getLeasingSpace().getUnitNumber(), leasingContract.getPartner().getEmailAddress(), emailAddress);

                em.flush();
                return true;

            }
        }
        return false;
    }

    @Override
    public boolean approveFittingOutRequest(String leasingContractId, String emailAddress) {
        Date now = new Date();
        Timestamp today = new Timestamp(now.getTime());
        EmailManager emailManager = new EmailManager();
        FittingOutReqEntity f = searchFittingOut(leasingContractId);
        LeasingContractEntity leasingContract = searchLeasingContract(leasingContractId);
        if (em.contains(f)) {
            f.setStatus("Approved");
            em.merge(f);

            //Update the start date of the contract
            Date fittingOut = f.getEndDate();
            Date newDate = addDays(fittingOut, 1);
            Timestamp newContractDate = new Timestamp(newDate.getTime());
            leasingContract.setStartDate(newContractDate);

            int days = leasingContract.getYearsOfContract() * 365;
            Date endDate = addDays(newDate, days);
            Timestamp end = new Timestamp(endDate.getTime());
            leasingContract.setEndDate(end);
            em.merge(leasingContract);
            NodeEntity node = (NodeEntity) leasingContract.getLeasingSpace().getInfrastructure();
            emailManager.sendApproveFittingEmail(leasingContract.getStartDate(), leasingContract.getEndDate(), leasingContract.getPartner().getCompany(), node.getCode(), leasingContract.getLeasingSpace().getUnitNumber(), leasingContract.getPartner().getEmailAddress(), emailAddress);

            String message = "Your fitting out request for " + node.getCode() + " : " + leasingContract.getLeasingSpace().getUnitNumber() + " is successful.";
            NotificationEntity no = new NotificationEntity("Fitting out request approved", message, today, false);
            no.setBusinessPartner(leasingContract.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(leasingContract.getPartner().getNotifications());
            notiList.add(no);
            leasingContract.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(leasingContract.getPartner());

            return true;
        }
        return false;
    }

    @Override
    public boolean rejectFittingOutRequest(String leasingContractId, String remarks, String emailAddress) {
        Date now = new Date();
        Timestamp today = new Timestamp(now.getTime());
        EmailManager emailManager = new EmailManager();
        FittingOutReqEntity f = searchFittingOut(leasingContractId);
        if (em.contains(f)) {
            f.setStatus("Rejected");
            f.setRemarks(remarks);
            em.merge(f);

            LeasingContractEntity lc = searchLeasingContract(leasingContractId);
            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();
            emailManager.sendRejectFittingEmail(remarks, lc.getPartner().getCompany(), node.getCode(), lc.getLeasingSpace().getUnitNumber(), lc.getPartner().getEmailAddress(), emailAddress);

            String message = "Sorry your fitting out request for " + node.getCode() + " : " + lc.getLeasingSpace().getUnitNumber() + " is rejected. Please check your email for more details. ";
            NotificationEntity no = new NotificationEntity("Rejected fitting out request", message, today, false);
            no.setBusinessPartner(lc.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(lc.getPartner().getNotifications());
            notiList.add(no);
            lc.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(lc.getPartner());

            return true;
        }
        return false;
    }

    @Override
    public boolean completedPreJointInspection(String leasingContractId) {
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            JointInspectionEntity j = lc.getJointInspection();
            j.setEntryStatus("Completed");
            em.merge(lc);
            em.merge(j);
            return true;
        }
        return false;
    }

    @Override
    public boolean editPreJointInspection(String leasingContractId, Date signingDate, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Timestamp today = new Timestamp(now.getTime());
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            Timestamp sdate = new Timestamp(signingDate.getTime());
            JointInspectionEntity j = lc.getJointInspection();
            j.setEntryInspection(sdate);
            em.merge(j);
            em.merge(lc);

            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();

            String message = "The date for Pre Joint Inspection for " + node.getCode() + " : " + lc.getLeasingSpace().getUnitNumber() + " has been changed";
            NotificationEntity no = new NotificationEntity("Pre Joint Inspection details changed", message, today, false);
            no.setBusinessPartner(lc.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(lc.getPartner().getNotifications());
            notiList.add(no);
            lc.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(lc.getPartner());

            emailManager.sendPreInspectionEmail(sdate, lc.getPartner().getCompany(), node.getCode(), lc.getLeasingSpace().getUnitNumber(), lc.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean schedulePreJointInspection(String leasingContractId, Date signingDate, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Timestamp today = new Timestamp(now.getTime());
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            Timestamp sdate = new Timestamp(signingDate.getTime());
            JointInspectionEntity j = new JointInspectionEntity(sdate, null, "Scheduled", null);
            em.persist(j);
            lc.setJointInspection(j);
            em.merge(lc);

            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();

            String message = "Pre Joint Inspection for " + node.getCode() + " : " + lc.getLeasingSpace().getUnitNumber() + " is scheduled.";
            NotificationEntity no = new NotificationEntity("Pre Joint Inspection is scheduled", message, today, false);
            no.setBusinessPartner(lc.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(lc.getPartner().getNotifications());
            notiList.add(no);
            lc.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(lc.getPartner());

            emailManager.sendPreInspectionEmail1(sdate, lc.getPartner().getCompany(), node.getCode(), lc.getLeasingSpace().getUnitNumber(), lc.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean schedulePostJointInspection(String leasingContractId, Date signingDate, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Timestamp today = new Timestamp(now.getTime());
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            Timestamp sdate = new Timestamp(signingDate.getTime());
            lc.getJointInspection().setExitInspection(sdate);
            lc.getJointInspection().setExitStatus("Scheduled");
            em.merge(lc);
            em.merge(lc.getJointInspection());

            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();

            String message = "Post Joint Inspection for " + node.getCode() + " : " + lc.getLeasingSpace().getUnitNumber() + " is scheduled.";
            NotificationEntity no = new NotificationEntity("Post Joint Inspection is scheduled", message, today, false);
            no.setBusinessPartner(lc.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(lc.getPartner().getNotifications());
            notiList.add(no);
            lc.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(lc.getPartner());

            emailManager.sendPostInspectionEmail1(sdate, lc.getPartner().getCompany(), node.getCode(), lc.getLeasingSpace().getUnitNumber(), lc.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean completedPostJointInspection(String leasingContractId) {
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            JointInspectionEntity j = lc.getJointInspection();
            j.setExitStatus("Completed");
            em.merge(lc);
            em.merge(j);
            return true;
        }
        return false;
    }

    @Override
    public boolean editPostJointInspection(String leasingContractId, Date signingDate, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Timestamp today = new Timestamp(now.getTime());
        LeasingContractEntity lc = searchLeasingContract(leasingContractId);
        if (em.contains(lc)) {
            Timestamp sdate = new Timestamp(signingDate.getTime());
            JointInspectionEntity j = lc.getJointInspection();
            j.setExitInspection(sdate);
            em.merge(j);
            em.merge(lc);

            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();

            String message = "The date for Post Joint Inspection for " + node.getCode() + " : " + lc.getLeasingSpace().getUnitNumber() + " has been changed";
            NotificationEntity no = new NotificationEntity("Post Joint Inspection details changed", message, today, false);
            no.setBusinessPartner(lc.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(lc.getPartner().getNotifications());
            notiList.add(no);
            lc.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(lc.getPartner());

            emailManager.sendPostInspectionEmail(sdate, lc.getPartner().getCompany(), node.getCode(), lc.getLeasingSpace().getUnitNumber(), lc.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public void completeLeaseContract(String contractId) {
        boolean status = true;
        LeasingContractEntity lc = searchLeasingContract(contractId);
        if (em.contains(lc)) {
            lc.setStatus("End of Contract");
            em.merge(lc);
            ArrayList<LeasingContractEntity> contractList = getLeasingContract();
            for (int i = 0; i < contractList.size(); i++) {
                if (contractList.get(i).getLeasingSpace().equals(lc.getLeasingSpace()) && contractList.get(i).getStatus().equals("Successful")) {
                    contractList.get(i).getLeasingSpace().setStatus("In Progress");
                    lc.getLeasingSpace().setDeadline(null);
                    status = false;
                    em.merge(lc);
                }
            }
            if (status) {
                lc.getLeasingSpace().setStatus("Available");
                lc.getLeasingSpace().setDeadline(null);
                em.merge(lc);
            }
        }
    }

    @Override
    public boolean terminateLeasingContract(String contractId, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        LeasingContractEntity lc = searchLeasingContract(contractId);
        if (em.contains(lc)) {
            lc.setStatus("Terminated");
            em.merge(lc);

            //Calculate Termination Fee (Per month is charged at $300)
            Date contractEnd = lc.getEndDate();
            Date now = new Date();
            long diff = contractEnd.getTime() - now.getTime();
            long diffMonths = (long) (diff / (60 * 60 * 1000 * 24 * 30.41666666));
            double chargeFee = diffMonths * 300;
            NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();
            emailManager.sendTerminationEmail(chargeFee, lc.getLeasingContractId(), lc.getPartner().getCompany(), node.getCode(), lc.getLeasingSpace().getUnitNumber(), lc.getPartner().getEmailAddress(), emailAddress);

            lc.getLeasingSpace().setStatus("Available");
            em.merge(lc.getLeasingSpace());
            return true;
        }
        return false;
    }

    @Override
    public boolean renewLeasingContract(LeasingContractEntity leasingContract, int leaseYear) {
        //Create a new Leasing Contract
        LeasingContractEntity lc = new LeasingContractEntity();
        requestLeasing1(leasingContract.getLeasingSpace().getAssetId(), leaseYear, leasingContract.getPartner().getPartnerId());
        ArrayList<LeasingContractEntity> contractList = getLeasingContract();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getPartner().equals(leasingContract.getPartner()) && contractList.get(i).getLeasingSpace().equals(leasingContract.getLeasingSpace()) && contractList.get(i).getStatus().equals("Submitted")) {
                lc = contractList.get(i);
                break;
            }
        }
        /*
        Date endDate = leasingContract.getEndDate();
        Date startDate = addDays(endDate, 1);
        goChooseLeaseSpace(lc.getLeasingContractId(), "e0002468@u.nus.edu", startDate);*/
        return true;
    }
    
    @Override
    public boolean approveLeasingContract(String contractId) {
        LeasingContractEntity current = new LeasingContractEntity();
        LeasingContractEntity lc = searchLeasingContract(contractId);
        
        //Find the current contract end date
        ArrayList<LeasingContractEntity> contractList = getLeasingContract();
        for (int i = 0; i < contractList.size(); i++) {
            if(contractList.get(i).getStatus().equals("On Contract") && contractList.get(i).getLeasingSpace().equals(lc.getLeasingSpace()))
                current = contractList.get(i);
        }
        
        Date endDate = current.getEndDate();
        Date startDate = addDays(endDate, 1);
        goChooseLeaseSpace(lc.getLeasingContractId(), "e0002468@u.nus.edu", startDate);
        return true;
    }
    
    @Override
    public boolean rejectLeasingContract(String contractId) {
        EmailManager emailManager = new EmailManager();
        LeasingContractEntity lc = searchLeasingContract(contractId);
        lc.setStatus("Unsuccessful");
        NodeEntity node = (NodeEntity) lc.getLeasingSpace().getInfrastructure();
        emailManager.sendLeaseSpaceEmail1(node.getCode(), lc.getLeasingSpace().getUnitNumber(), lc.getPartner().getCompany(), lc.getPartner().getEmailAddress(), "e0002468@u.nus.edu");
        return true;
    }


    @Override
    public ArrayList<AdvertisingContractEntity> getAdvertisingContract() {
        ArrayList<AdvertisingContractEntity> contractList = new ArrayList<AdvertisingContractEntity>();
        Query query = em.createQuery("SELECT ac FROM AdvertisingContractEntity AS ac");
        for (Object o : query.getResultList()) {
            contractList.add((AdvertisingContractEntity) o);
        }
        return contractList;
    }

    @Override
    public AdvertisingContractEntity searchAdvertisingContract(String advertisingId) {
        AdvertisingContractEntity ac = new AdvertisingContractEntity();
        try {
            Query q = em.createQuery("SELECT a FROM AdvertisingContractEntity AS a WHERE a.advertisingId=:advertisingId");
            q.setParameter("advertisingId", advertisingId);
            ac = (AdvertisingContractEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return ac;
    }

    @Override
    public boolean completedSigningAdvertContract(String advertisingId) {
        AdvertisingContractEntity ac = searchAdvertisingContract(advertisingId);
        if (em.contains(ac)) {
            ac.setSigningOfAgreement(true);
            ac.setStatus("On Contract");
            AdvertisementSpaceEntity space = (AdvertisementSpaceEntity) infraAssetSessionBeanLocal.searchAsset(ac.getAdvertisement().getAssetId());
            space.setStatus("On Contract");
            em.merge(ac);
            em.merge(space);
            return true;
        }
        return false;
    }

    @Override
    public boolean editSigningAdvertContract(String advertisingId, Date signingDate, String emailAddress) {
        String code;
        EmailManager emailManager = new EmailManager();
        AdvertisingContractEntity ac = searchAdvertisingContract(advertisingId);
        if (em.contains(ac)) {
            Timestamp sdate = new Timestamp(signingDate.getTime());
            ac.setSignedDate(sdate);
            em.merge(ac);
            NodeEntity node = (NodeEntity) ac.getAdvertisement().getInfrastructure();
            if (ac.getAdvertisement().getAssetName().equals("Rolling Stock")) {
                code = ac.getAdvertisement().getInfrastructure().getInfraId();
            } else {
                code = node.getCode();
            }
            emailManager.sendSigningEmail1(ac.getAdvertisement().getAssetName(), code, ac.getAdvertisement().getLocation(), ac.getAdvertisement().getType(), ac.getAdvertisement().getNumberCode(), sdate, ac.getPartner().getCompany(), ac.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean completedInstallation(String advertisingId, String emailAddress) {
        String code;
        EmailManager emailManager = new EmailManager();
        AdvertisingContractEntity ac = searchAdvertisingContract(advertisingId);
        if (em.contains(ac)) {
            ac.setInstallation(true);
            em.merge(ac);
            NodeEntity node = (NodeEntity) ac.getAdvertisement().getInfrastructure();
            if (ac.getAdvertisement().getAssetName().equals("Rolling Stock")) {
                code = ac.getAdvertisement().getInfrastructure().getInfraId();
            } else {
                code = node.getCode();
            }
            emailManager.sendInstallationEmail(ac.getStartDate(), ac.getEndDate(), ac.getAdvertisement().getAssetName(), code, ac.getAdvertisement().getLocation(), ac.getAdvertisement().getType(), ac.getAdvertisement().getNumberCode(), ac.getPartner().getCompany(), ac.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean completedRemoval(String advertisingId, String emailAddress) {
        String code;
        EmailManager emailManager = new EmailManager();
        AdvertisingContractEntity ac = searchAdvertisingContract(advertisingId);
        if (em.contains(ac)) {
            ac.setRemoval(true);
            ac.setStatus("End of Contract");
            em.merge(ac);
            NodeEntity node = (NodeEntity) ac.getAdvertisement().getInfrastructure();
            if (ac.getAdvertisement().getAssetName().equals("Rolling Stock")) {
                code = ac.getAdvertisement().getInfrastructure().getInfraId();
            } else {
                code = node.getCode();
            }
            emailManager.sendRemovalEmail(ac.getStartDate(), ac.getEndDate(), ac.getAdvertisement().getAssetName(), code, ac.getAdvertisement().getLocation(), ac.getAdvertisement().getType(), ac.getAdvertisement().getNumberCode(), ac.getPartner().getCompany(), ac.getPartner().getEmailAddress(), emailAddress);
            return true;
        }
        return false;
    }

    //Get all the advertising space that got submitter 
    @Override
    public ArrayList<AdvertisementSpaceEntity> getAdvertisingList() {
        ArrayList<AdvertisementSpaceEntity> advertList = new ArrayList<AdvertisementSpaceEntity>();
        Query query = em.createQuery("SELECT DISTINCT l.advertisement FROM AdvertisingContractEntity AS l ");
        for (Object o : query.getResultList()) {
            advertList.add((AdvertisementSpaceEntity) o);
        }
        return advertList;
    }

    @Override
    public boolean goChooseAdvertSpace(String advertisingId, String emailAddress, Date startDate) {
        String code;
        EmailManager emailManager = new EmailManager();
        Date now = new Date();
        Date newDate = addDays(now, 6);
        Date newDate1 = getZeroTimeDate(newDate);
        Timestamp notiDate = new Timestamp(newDate1.getTime());
        Timestamp today = new Timestamp(now.getTime());
        AdvertisingContractEntity ac = searchAdvertisingContract(advertisingId);
        if (em.contains(ac)) {
            Timestamp start = new Timestamp(startDate.getTime());
            ac.setStatus("Successful");
            ac.setSignedDate(notiDate);
            ac.setStartDate(start);
            int days = ac.getYearsOfContract() * 30;
            Date endDate = addDays(start, days);
            Timestamp end = new Timestamp(endDate.getTime());
            ac.setEndDate(end);
            em.merge(ac);
            NodeEntity node = (NodeEntity) ac.getAdvertisement().getInfrastructure();
            if (ac.getAdvertisement().getAssetName().equals("Rolling Stock")) {
                code = ac.getAdvertisement().getInfrastructure().getInfraId();
            } else {
                code = node.getCode();
            }

            //Send email to infor successful advert space
            emailManager.sendAdvertSpaceEmail(ac.getAdvertisement().getAssetName(), code, ac.getAdvertisement().getLocation(), ac.getAdvertisement().getType(), ac.getAdvertisement().getNumberCode(), notiDate, start, ac.getPartner().getCompany(), ac.getPartner().getEmailAddress(), emailAddress);

            //Send notification to inform successful lease space
            String message = "Congratulations, your request for " + node.getCode() + " : " + ac.getAdvertisement().getLocation() + " " + ac.getAdvertisement().getType() + " " + ac.getAdvertisement().getNumberCode() + " is successful.";
            NotificationEntity no = new NotificationEntity("Congratulations!", message, today, false);
            no.setBusinessPartner(ac.getPartner());
            ArrayList<NotificationEntity> notiList = new ArrayList<NotificationEntity>(ac.getPartner().getNotifications());
            notiList.add(no);
            ac.getPartner().setNotifications(notiList);
            em.merge(no);
            em.merge(ac.getPartner());

            //send email to inform unsuccessful advert space and set the status to unsuccessful
            String lsId = ac.getAdvertisement().getAssetId();
            ArrayList<AdvertisingContractEntity> contractList = getAdvertisingContract();
            if (!(contractList.isEmpty())) {
                for (int i = 0; i < contractList.size(); i++) {
                    AdvertisingContractEntity lc1 = contractList.get(i);
                    if (lc1.getAdvertisement().getAssetId().equals(lsId) && !(lc1.getAdvertisingId().equals(ac.getAdvertisingId()))) {
                        if (lc1.getStatus().equals("Pending")) {
                            lc1.setStatus("Unsuccessful");
                            if (!(lc1.getPartner().equals(ac.getPartner()))) {
                                emailManager.sendAdvertSpaceEmail1(ac.getAdvertisement().getAssetName(), code, ac.getAdvertisement().getLocation(), ac.getAdvertisement().getType(), ac.getAdvertisement().getNumberCode(), lc1.getPartner().getCompany(), lc1.getPartner().getEmailAddress(), emailAddress);

                                //Send notification to inform successful lease space
                                String message1 = "We are sorry to inform you that your request for " + node.getCode() + " : " + ac.getAdvertisement().getLocation() + " " + ac.getAdvertisement().getType() + " " + ac.getAdvertisement().getNumberCode() + " is unsuccessful.";
                                NotificationEntity no1 = new NotificationEntity("We are sorry to inform you", message1, today, false);
                                no1.setBusinessPartner(lc1.getPartner());
                                ArrayList<NotificationEntity> notiList1 = new ArrayList<NotificationEntity>(lc1.getPartner().getNotifications());
                                notiList1.add(no1);
                                lc1.getPartner().setNotifications(notiList1);
                                em.merge(no1);
                                em.merge(lc1.getPartner());

                            }
                            em.merge(lc1);
                        }
                    }
                }
            }

            AdvertisementSpaceEntity space = (AdvertisementSpaceEntity) infraAssetSessionBeanLocal.searchAsset(lsId);
            space.setStatus("In Progress");
            em.merge(space);

            return true;
        }
        return false;
    }

   
    @Override
    public boolean terminateAdvertisingContract(String advertisingId, String emailAddress) {
        String code;
        EmailManager emailManager = new EmailManager();
       AdvertisingContractEntity ac = searchAdvertisingContract(advertisingId);
        if (em.contains(ac)) {
            ac.setStatus("Terminated");
            em.merge(ac);

            //Calculate Termination Fee (Per month is charged at $300)
            Date contractEnd = ac.getEndDate();
            Date now = new Date();
            long diff = contractEnd.getTime() - now.getTime();
            long diffMonths = (long) (diff / (60 * 60 * 1000 * 24 * 30.41666666));
            double chargeFee = diffMonths * 300;
            NodeEntity node = (NodeEntity) ac.getAdvertisement().getInfrastructure();
            if (ac.getAdvertisement().getAssetName().equals("Rolling Stock")) {
                code = ac.getAdvertisement().getInfrastructure().getInfraId();
            } else {
                code = node.getCode();
            }
            emailManager.sendAdvertTerminationEmail(chargeFee, ac.getAdvertisingId(), ac.getPartner().getCompany(), ac.getAdvertisement().getAssetName(), code, ac.getAdvertisement().getLocation(), ac.getAdvertisement().getType(), ac.getAdvertisement().getNumberCode(), ac.getPartner().getEmailAddress(), emailAddress);

            ac.getAdvertisement().setStatus("Available");
            em.merge(ac.getAdvertisement());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean approveAdvertContract(String contractId) {
        AdvertisingContractEntity current = new AdvertisingContractEntity();
        AdvertisingContractEntity lc = searchAdvertisingContract(contractId);
        
        //Find the current contract end date
        ArrayList<AdvertisingContractEntity> contractList = getAdvertisingContract();
        for (int i = 0; i < contractList.size(); i++) {
            if(contractList.get(i).getStatus().equals("On Contract") && contractList.get(i).getAdvertisement().equals(lc.getAdvertisement()))
                current = contractList.get(i);
        }
        
        Date endDate = current.getEndDate();
        Date startDate = addDays(endDate, 1);
        goChooseAdvertSpace(lc.getAdvertisingId(), "e0002468@u.nus.edu", startDate);
        return true;
    }
    
    @Override
    public boolean rejectAdvertContract(String contractId) {
        String code;
        EmailManager emailManager = new EmailManager();
        AdvertisingContractEntity ac = searchAdvertisingContract(contractId);
        ac.setStatus("Unsuccessful");
        NodeEntity node = (NodeEntity) ac.getAdvertisement().getInfrastructure();
        if (ac.getAdvertisement().getAssetName().equals("Rolling Stock")) {
                code = ac.getAdvertisement().getInfrastructure().getInfraId();
            } else {
                code = node.getCode();
            }
       emailManager.sendAdvertSpaceEmail1(ac.getAdvertisement().getAssetName(), code, ac.getAdvertisement().getLocation(), ac.getAdvertisement().getType(), ac.getAdvertisement().getNumberCode(), ac.getPartner().getCompany(), ac.getPartner().getEmailAddress(), "e0002468@u.nus.edu");
        return true;
    }
    
     
      public ArrayList<AdvertisementSpaceEntity> getAdvertisingList1() {
        ArrayList<AdvertisementSpaceEntity> advertList = new ArrayList<AdvertisementSpaceEntity>();
        Query query = em.createQuery("SELECT DISTINCT l.advertisement FROM AdvertisingContractEntity AS l WHERE l.status='Submitted' ");
        for (Object o : query.getResultList()) {
            advertList.add((AdvertisementSpaceEntity) o);
        }
        return advertList;
    }
      
      @Override
    public void checkAdvertisingContract() {
        boolean status = true;
        ArrayList<AdvertisingContractEntity> contractList1 = getAdvertisingContract();
        for (int j = 0; j < contractList1.size(); j++) {
            AdvertisingContractEntity lc = contractList1.get(j);
            if (lc.getEndDate() != null) {
                boolean deadline = compareDate(lc.getEndDate());
                if (deadline == false && lc.getStatus().equals("On Contract")) {
                    lc.setStatus("End of Contract");
                    em.merge(lc);
                    ArrayList<AdvertisingContractEntity> contractList = getAdvertisingContract();
                    for (int i = 0; i < contractList.size(); i++) {
                        if (contractList.get(i).getAdvertisement().equals(lc.getAdvertisement()) && contractList.get(i).getStatus().equals("Successful")) {
                            contractList.get(i).getAdvertisement().setStatus("In Progress");
                            lc.getAdvertisement().setDeadline(null);
                            status = false;
                            em.merge(lc);
                        }
                    }
                    if (status) {
                        lc.getAdvertisement().setStatus("Available");
                        lc.getAdvertisement().setDeadline(null);
                        em.merge(lc);
                    }
                }
            }
        }
    }
    
     @Override
    public void checkLeasingContract() {
        boolean status = true;
        ArrayList<LeasingContractEntity> contractList1 = getLeasingContract();
        for (int j = 0; j < contractList1.size(); j++) {
            LeasingContractEntity lc = contractList1.get(j);
            if (lc.getEndDate() != null) {
                boolean deadline = compareDate(lc.getEndDate());
                if (deadline == false && lc.getStatus().equals("On Contract")) {
                    lc.setStatus("End of Contract");
                    em.merge(lc);
                    ArrayList<LeasingContractEntity> contractList = getLeasingContract();
                    for (int i = 0; i < contractList.size(); i++) {
                        if (contractList.get(i).getLeasingSpace().equals(lc.getLeasingSpace()) && contractList.get(i).getStatus().equals("Successful")) {
                            contractList.get(i).getLeasingSpace().setStatus("In Progress");
                            lc.getLeasingSpace().setDeadline(null);
                            status = false;
                            em.merge(lc);
                        }
                    }
                    if (status) {
                        lc.getLeasingSpace().setStatus("Available");
                        lc.getLeasingSpace().setDeadline(null);
                        em.merge(lc);
                    }
                }
            }
        }
    }


      
    
     @Override
    public void checkAdvertising() {
        //get the advertising list that got submitter --> status is submitted
        ArrayList<AdvertisementSpaceEntity> advertList = getAdvertisingList1();
        ArrayList<AdvertisementSpaceEntity> newList = new ArrayList<AdvertisementSpaceEntity>();
        ArrayList<AdvertisingContractEntity> contractList = getAdvertisingContract();
        if (advertList.isEmpty()) {
            //Do Nothing
        } else {

            if (!(advertList.isEmpty())) {
                //Loop the whole leasing space list
                for (int i = 0; i < advertList.size(); i++) {
                    AdvertisementSpaceEntity ls = advertList.get(i);
                    if (ls.getDeadline() != null) {
                        boolean deadline = compareDate(ls.getDeadline());
                        if (deadline == false && ls.getStatus().equals("Available")) {
                            ls.setStatus("Pending");
                            ls.setDeadline(null);
                            em.merge(ls);
                            newList.add(ls);
                        }
                    }
                }
            }

            if (!(newList.isEmpty())) {
                //New list = the list where all the advert space close date reached
                for (int i = 0; i < newList.size(); i++) {
                    AdvertisementSpaceEntity ls = newList.get(i);
                    for (int j = 0; j < contractList.size(); j++) {
                        AdvertisingContractEntity lc = contractList.get(j);
                        //check the contract whether got that advert space
                        if (lc.getAdvertisement().getAssetId().equals(ls.getAssetId()) && lc.getStatus().equals("Submitted")) {
                            lc.setStatus("Pending");
                            em.merge(lc);
                        }
                    }
                }
            }
        }

        ArrayList<AdvertisementSpaceEntity> wholeList = infraAssetSessionBeanLocal.getAdvertisementSpace();
        for (int i = 0; i < wholeList.size(); i++) {
            boolean status = true;
            //advert List that got submitter
            for (int j = 0; j < advertList.size(); j++) {
                if (wholeList.get(i).getAssetId().equals(advertList.get(j).getAssetId())) {
                    status = false;
                }
            }

            //Not inside the list. (Dont have submitter)
            if (status) {
                if (wholeList.get(i).getDeadline() != null) {
                    boolean deadline = compareDate(wholeList.get(i).getDeadline());
                    if (deadline == false) {
                        wholeList.get(i).setStatus("Available");
                        wholeList.get(i).setDeadline(null);
                        em.merge(wholeList.get(i));
                    }
                }
            }
        }
    }
    
     @Override
    public boolean requestAdvertising(String assetId, int leaseYear, String partnerId) {
        try {
            Query q = em.createQuery("SELECT ac FROM AdvertisingContractEntity AS ac");
            int rows = q.getResultList().size();
            String advertisingId = "AC" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT ac1 FROM AdvertisingContractEntity AS ac1 WHERE ac1.advertisingId=:advertisingId");
                q1.setParameter("advertisingId", advertisingId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                advertisingId = "AC" + rows;
            }

            //Create a New contract
            Date now = new Date();
            Timestamp submittedDate = new Timestamp(now.getTime());
            AdvertisingContractEntity ac = new AdvertisingContractEntity(advertisingId, submittedDate, null, false, null, null, leaseYear, "Submitted", false, false);
        
            em.persist(ac);
            //Map contract to Business Partner
            BusinessPartnerEntity partner = searchPartner(partnerId);
            if (em.contains(partner)) {
                ac.setPartner(partner);
                em.merge(ac);
            }

            //Map contract to advertising Space
            AdvertisementSpaceEntity space = (AdvertisementSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
            if (em.contains(space)) {
                ac.setAdvertisement(space);
                em.merge(ac);
            }

            return true;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }
    
    //renewal
    public boolean requestAdvertising1(String assetId, int leaseYear, String partnerId) {
        try {
            Query q = em.createQuery("SELECT ac FROM AdvertisingContractEntity AS ac");
            int rows = q.getResultList().size();
            String advertisingId = "AC" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT ac1 FROM AdvertisingContractEntity AS ac1 WHERE ac1.advertisingId=:advertisingId");
                q1.setParameter("advertisingId", advertisingId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                advertisingId = "AC" + rows;
            }

            //Create a New contract
            Date now = new Date();
            Timestamp submittedDate = new Timestamp(now.getTime());
            AdvertisingContractEntity ac = new AdvertisingContractEntity(advertisingId, submittedDate, null, false, null, null, leaseYear, "Need Approval", false, false);
        
            em.persist(ac);
            //Map contract to Business Partner
            BusinessPartnerEntity partner = searchPartner(partnerId);
            if (em.contains(partner)) {
                ac.setPartner(partner);
                em.merge(ac);
            }

            //Map contract to advertising Space
            AdvertisementSpaceEntity space = (AdvertisementSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
            if (em.contains(space)) {
                ac.setAdvertisement(space);
                em.merge(ac);
            }

            return true;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

   @Override
   public ArrayList<AdvertisingContractEntity> getAdvertisingRequestList1(String partnerId) {
        ArrayList<AdvertisingContractEntity> contractList = getAdvertisingRequestList(partnerId);
        ArrayList<AdvertisingContractEntity> newList = new ArrayList<AdvertisingContractEntity>();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getStatus().equals("On Contract")) {
                newList.add(contractList.get(i));
            }
        }
        return newList;
    }
   
    @Override
    public ArrayList<AdvertisingContractEntity> getAdvertisingRequestList2(String partnerId){
        ArrayList<AdvertisingContractEntity> contractList = getAdvertisingRequestList(partnerId);
        ArrayList<AdvertisingContractEntity> newList = new ArrayList<AdvertisingContractEntity>();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getStatus().equals("End of Contract") || contractList.get(i).getStatus().equals("Terminated")) {
                newList.add(contractList.get(i));
            }
        }
        return newList;
    }
   
   @Override
    public ArrayList<AdvertisingContractEntity> getAdvertisingRequestList(String partnerId) {
        ArrayList<AdvertisingContractEntity> contractList = getAdvertisingContract();
        ArrayList<AdvertisingContractEntity> newList = new ArrayList<AdvertisingContractEntity>();

        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getPartner().getPartnerId().equals(partnerId)) {
                newList.add(contractList.get(i));
            }
        }
        return newList;
    }
    
    @Override
    public boolean renewAdvertContract(AdvertisingContractEntity advertisingContract, int leaseYear) {
        //Create a new Advertising Contract
        AdvertisingContractEntity lc = new AdvertisingContractEntity();
        requestAdvertising1(advertisingContract.getAdvertisement().getAssetId(), leaseYear,advertisingContract.getPartner().getPartnerId());
        ArrayList<AdvertisingContractEntity> contractList = getAdvertisingContract();
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getPartner().equals(advertisingContract.getPartner()) && contractList.get(i).getAdvertisement().equals(advertisingContract.getAdvertisement()) && contractList.get(i).getStatus().equals("Submitted")) {
                lc = contractList.get(i);
                break;
            }
        }
        
        /*
        Date endDate = advertisingContract.getEndDate();
        Date startDate = addDays(endDate, 1);
        goChooseAdvertSpace(lc.getAdvertisingId(), "e0002468@u.nus.edu", startDate);*/
        return true;
    }
    
    @Override
    public boolean goEditMyAdvertising(String leasingContractId, int leaseYears) {
        AdvertisingContractEntity ac = searchAdvertisingContract(leasingContractId);
        if (em.contains(ac)) {
            ac.setYearsOfContract(leaseYears);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean goDeleteAC(String advertisingId) {
       AdvertisingContractEntity ac = searchAdvertisingContract(advertisingId);
        if (em.contains(ac)) {
            ac.setAdvertisement(null);
            ac.setPartner(null);
            em.remove(ac);
            em.flush();
            return true;
        }
        return false;
    }

}
