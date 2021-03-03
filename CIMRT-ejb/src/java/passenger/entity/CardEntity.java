/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.entity;

import operations.entity.TopUpTransactionEntity;
import businessPartner.entity.PurchaseTransactionEntity;
import infraasset.entity.StationEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import routefare.entity.FareTransactionEntity;

/**
 *
 * @author Yuting
 */
@Entity
@XmlRootElement
@XmlType(name = "cardEntity", propOrder = {
    "cardId",
    "type",
    "amount",
    "concessionStart",
    "concessionEnd",
    "passenger",
    "transactions"
})
public class CardEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String cardId;
    private String type;
    private double amount;
    @Temporal(TemporalType.DATE) 
    private Date concessionStart;
    @Temporal(TemporalType.DATE) 
    private Date concessionEnd;
    @Temporal(TemporalType.DATE) 
    private Date expiryDate;
    @ManyToOne
    private PassengerEntity passenger;
    @OneToMany(cascade={CascadeType.ALL},mappedBy="card")
    private Collection<TopUpTransactionEntity> topUpTransactions = new ArrayList<TopUpTransactionEntity>();
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="card")
    private Collection<FareTransactionEntity> fareTransactions = new ArrayList<FareTransactionEntity>();
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="card")
    private List<PurchaseTransactionEntity> purchaseTransactions = new ArrayList<PurchaseTransactionEntity>();
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="card")
    private List<PointTransactionEntity> pointTransactions = new ArrayList<PointTransactionEntity>();
            
    public CardEntity() {
    }

    public CardEntity(String cardId, String type, double amount, Date start, Date end, Date expiryDate) {
        this.cardId = cardId;
        this.type = type;
        this.amount = amount;
        this.concessionStart = start;
        this.concessionEnd = end;
        this.expiryDate = expiryDate;
    }
    
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getConcessionStart() {
        return concessionStart;
    }

    public void setConcessionStart(Date concessionStart) {
        this.concessionStart = concessionStart;
    }

    public Date getConcessionEnd() {
        return concessionEnd;
    }

    public void setConcessionEnd(Date concessionEnd) {
        this.concessionEnd = concessionEnd;
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

    public Collection<TopUpTransactionEntity> getTopUpTransactions() {
        return topUpTransactions;
    }

    public void setTopUpTransactions(Collection<TopUpTransactionEntity> topUpTransactions) {
        this.topUpTransactions = topUpTransactions;
    }

    public Collection<FareTransactionEntity> getFareTransactions() {
        return fareTransactions;
    }

    public void setFareTransactions(Collection<FareTransactionEntity> fareTransactions) {
        this.fareTransactions = fareTransactions;
    }

    public List<PurchaseTransactionEntity> getPurchaseTransactions() {
        return purchaseTransactions;
    }

    public void setPurchaseTransactions(List<PurchaseTransactionEntity> purchaseTransactions) {
        this.purchaseTransactions = purchaseTransactions;
    }

    public List<PointTransactionEntity> getPointTransactions() {
        return pointTransactions;
    }

    public void setPointTransactions(List<PointTransactionEntity> pointTransactions) {
        this.pointTransactions = pointTransactions;
    }
    
}
