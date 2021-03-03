/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessPartner.sessionbean;

import businessPartner.entity.BusinessPartnerEntity;
import businessPartner.entity.PurchaseTransactionEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import passenger.entity.CardEntity;
import passenger.entity.PassengerEntity;
import passenger.entity.PointTransactionEntity;

/**
 *
 * @author Yoona
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    @PersistenceContext
    EntityManager em;

    @Override
    public boolean submitTransaction(String partnerId, String type, String nric, String cardId, double amount) {
        BusinessPartnerEntity partner = searchPartner(partnerId);
        PassengerEntity passenger = searchPassenger(nric);
        CardEntity card = searchCard(cardId);
        if (partner != null) {
            if (type.equals("Card") && card != null) {
                PurchaseTransactionEntity purchase = new PurchaseTransactionEntity(amount, new Timestamp(new Date().getTime()), partner, null, card);
                em.persist(purchase);
                PointTransactionEntity point = new PointTransactionEntity(new Timestamp(new Date().getTime()), (int) Math.floor(amount), card.getPassenger(), null, purchase, null);
                em.persist(point);
                purchase.setPointTrans(point);
                card.getPassenger().setPoint(card.getPassenger().getPoint() + (int) Math.floor(amount));
                card.getPurchaseTransactions().add(purchase);
                card.getPointTransactions().add(point);
                partner.getPurchaseTransactions().add(purchase);
                em.flush();

                return true;

            } else if (type.equals("Others") && passenger != null) {
                PurchaseTransactionEntity purchase = new PurchaseTransactionEntity(amount, new Timestamp(new Date().getTime()), partner, passenger, null);
                em.persist(purchase);
                PointTransactionEntity point = new PointTransactionEntity(new Timestamp(new Date().getTime()), (int) Math.floor(amount), passenger, null, purchase, null);
                em.persist(point);
                purchase.setPointTrans(point);
                passenger.setPoint(passenger.getPoint() + (int) Math.floor(amount));
                passenger.getPtTransactions().add(point);
                passenger.getPurchaseTransactions().add(purchase);
                partner.getPurchaseTransactions().add(purchase);
                em.flush();

                return true;
            }

        }
        return false;
    }

    private BusinessPartnerEntity searchPartner(String partnerId) {

        Query q = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b WHERE b.partnerId=:partnerId");
        q.setParameter("partnerId", partnerId);
        List<BusinessPartnerEntity> result = (List<BusinessPartnerEntity>) q.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {

            return result.get(0);
        }
    }

    private PassengerEntity searchPassenger(String nric) {

        Query q = em.createQuery("SELECT p FROM PassengerEntity AS p WHERE p.nric=:nric");
        q.setParameter("nric", nric);
        List<PassengerEntity> result = (List<PassengerEntity>) q.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {

            return result.get(0);
        }
    }

    private CardEntity searchCard(String cardId) {

        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        List<CardEntity> result = (List<CardEntity>) q.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {

            return result.get(0);
        }
    }
}
