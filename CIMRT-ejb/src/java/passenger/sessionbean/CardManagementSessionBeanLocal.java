/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import businessPartner.entity.PurchaseTransactionEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import operations.entity.TopUpTransactionEntity;
import passenger.entity.CardEntity;
import passenger.entity.PointTransactionEntity;
import routefare.entity.FareTransactionEntity;

/**
 *
 * @author Yoona
 */
@Local
public interface CardManagementSessionBeanLocal {
    public List<CardEntity> retrieveUserCards(String username);
    public int retrievePoints(String username);
    public double retriveConcessionAmount(CardEntity selectedCard);
    public boolean redeemPoints(CardEntity selectedCard, int redeemPoints);

    public boolean topupCard(CardEntity selectedCard, double amount, String type, Date startDate, Date endDate);

    public boolean addCard(String cardId, String username);

    public List<PurchaseTransactionEntity> retrievePurchaseTransactions(CardEntity selectedCard);

    public List<PointTransactionEntity> retrievePointsTransactions(CardEntity selectedCard);

    public List<TopUpTransactionEntity> retrieveTopupTransactions(CardEntity selectedCard);

    public List<FareTransactionEntity> retrieveFareTransactions(CardEntity selectedCard);
}
