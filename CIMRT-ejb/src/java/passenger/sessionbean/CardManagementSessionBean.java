/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import businessPartner.entity.PurchaseTransactionEntity;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import passenger.entity.CardEntity;
import passenger.entity.PassengerEntity;
import passenger.entity.PointTransactionEntity;
import operations.entity.TopUpTransactionEntity;
import routefare.entity.FareAlgoEntity;
import java.util.List;
import routefare.entity.FareTransactionEntity;

/**
 *
 * @author Yoona
 */
@Stateless
public class CardManagementSessionBean implements CardManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CardEntity> retrieveUserCards(String username) {
        PassengerEntity passenger = searchPassenger(username);

        if (passenger != null) {
            Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.passenger=:passenger");
            q.setParameter("passenger", passenger);
            List<CardEntity> cards = q.getResultList();
            return cards;
        }
        return null;
    }

    @Override
    public int retrievePoints(String username) {
        PassengerEntity passenger = searchPassenger(username);

        if (passenger != null) {
            return passenger.getPoint();
        } else {
            return -1;
        }
    }

    private PassengerEntity searchPassenger(String username) {

        Query query = em.createQuery("SELECT p FROM PassengerEntity AS p WHERE p.username=:username");
        query.setParameter("username", username);
        List<PassengerEntity> passengers = query.getResultList();

        if (passengers.isEmpty()) {
            return null;
        } else {
            return passengers.get(0);
        }
    }

    @Override
    public double retriveConcessionAmount(CardEntity selectedCard) {
        Query query = em.createQuery("SELECT f FROM FareAlgoEntity AS f WHERE f.passengerType=:pType AND f.fareType='Concession'");
        query.setParameter("pType", selectedCard.getType());

        List<FareAlgoEntity> fare = query.getResultList();

        if (fare.isEmpty()) {
            return -1;
        } else {
            return fare.get(0).getBaseFee();
        }
    }

    @Override
    public boolean redeemPoints(CardEntity selectedCard, int redeemPoints) {
        PassengerEntity passenger = selectedCard.getPassenger();
        if (redeemPoints <= passenger.getPoint() && redeemPoints > 0) {
            int pointsLeft = passenger.getPoint() - redeemPoints;
            passenger.setPoint(pointsLeft);

            double cardValue = selectedCard.getAmount();
            cardValue += ((double) redeemPoints) / 1000;
            selectedCard.setAmount(cardValue);
            PointTransactionEntity pointTrans = new PointTransactionEntity(new Timestamp(new Date().getTime()), -redeemPoints, passenger, null, null, selectedCard);
            selectedCard.getPointTransactions().add(pointTrans);
            passenger.getPtTransactions().add(pointTrans);
            em.persist(pointTrans);
            em.merge(passenger);
            em.merge(selectedCard);
            em.flush();

            return true;
        }
        return false;
    }

    @Override
    public boolean topupCard(CardEntity selectedCard, double amount, String type, Date startDate, Date endDate) {

        if (type.equals("Variable Amount")) {
            double value = selectedCard.getAmount();
            value = value + amount;
            selectedCard.setAmount(value);
            TopUpTransactionEntity topup = new TopUpTransactionEntity(type, amount, new Timestamp(new Date().getTime()), selectedCard);
            em.persist(topup);
            if (amount >= 10) {
                PointTransactionEntity point = new PointTransactionEntity(new Timestamp(new Date().getTime()), (int) Math.floor(amount), selectedCard.getPassenger(), topup, null, null);
                em.persist(point);
                topup.setPointTrans(point);
                selectedCard.getPassenger().setPoint(selectedCard.getPassenger().getPoint() + (int) Math.floor(amount));
            }
            selectedCard.getTopUpTransactions().add(topup);

            em.flush();
            em.merge(selectedCard);
            em.merge(selectedCard.getPassenger());
            return true;

        } else if (type.equals("Concession")) {

            Date currEnd = getZeroTimeDate(selectedCard.getConcessionEnd());

            if (currEnd.before(getZeroTimeDate(new Date()))) {
                selectedCard.setConcessionStart(startDate);
                selectedCard.setConcessionEnd(endDate);
            } else {
                selectedCard.setConcessionEnd(endDate);
            }

            TopUpTransactionEntity topup = new TopUpTransactionEntity(type, amount, new Timestamp(new Date().getTime()), selectedCard);
            em.persist(topup);
            if (amount >= 10) {
                PointTransactionEntity point = new PointTransactionEntity(new Timestamp(new Date().getTime()), (int) Math.floor(amount), selectedCard.getPassenger(), topup, null, null);
                em.persist(point);
                topup.setPointTrans(point);
                selectedCard.getPassenger().setPoint(selectedCard.getPassenger().getPoint() + (int) Math.floor(amount));
            }
            selectedCard.getTopUpTransactions().add(topup);
            em.flush();
            em.merge(selectedCard);
            em.merge(selectedCard.getPassenger());
            return true;
        }
        return false;
    }

    public static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date = calendar.getTime();

        return date;
    }

    @Override
    public boolean addCard(String cardId, String username) {

        Query q = em.createQuery("SELECT c FROM CardEntity AS c WHERE c.cardId=:cardId");
        q.setParameter("cardId", cardId);
        List<CardEntity> cards = q.getResultList();

        if (cards.isEmpty()) {
            System.out.println("no such card");
            return false;
        }
        CardEntity card = cards.get(0);
        System.out.println("got card");
        if (card.getPassenger() == null) {
            System.out.println("no passenger");
            PassengerEntity passenger = searchPassenger(username);
            passenger.getCards().add(card);
            card.setPassenger(passenger);
            em.merge(passenger);
            em.merge(card);
            return true;
        }

        return false;
    }

    @Override
    public List<PurchaseTransactionEntity> retrievePurchaseTransactions(CardEntity selectedCard) {
        
        Query q = em.createQuery("SELECT p FROM PurchaseTransactionEntity AS p WHERE p.card=:card ORDER BY p.transactionDate DESC");
        q.setParameter("card", selectedCard);
        List<PurchaseTransactionEntity> purchaseTrans = q.getResultList();
        
        return purchaseTrans;
    }

    @Override
    public List<PointTransactionEntity> retrievePointsTransactions(CardEntity selectedCard) {
        Query q = em.createQuery("SELECT p FROM PointTransactionEntity AS p WHERE p.card=:card ORDER BY p.transDateTime DESC");
        q.setParameter("card", selectedCard);
        List<PointTransactionEntity> pointTrans = q.getResultList();
        
        return pointTrans;
    }

    @Override
    public List<TopUpTransactionEntity> retrieveTopupTransactions(CardEntity selectedCard) {
        Query q = em.createQuery("SELECT t FROM TopUpTransactionEntity AS t WHERE t.card=:card ORDER BY t.transDate DESC");
        q.setParameter("card", selectedCard);
        List<TopUpTransactionEntity> topupTrans = q.getResultList();
        
        return topupTrans;
    }

    @Override
    public List<FareTransactionEntity> retrieveFareTransactions(CardEntity selectedCard) {
        Query q = em.createQuery("SELECT f FROM FareTransactionEntity AS f WHERE f.card=:card ORDER BY f.startTime DESC");
        q.setParameter("card", selectedCard);
        List<FareTransactionEntity> fareTrans = q.getResultList();
        
        return fareTrans;
    }
}
