/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessPartner.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import passenger.entity.CardEntity;
import passenger.entity.PassengerEntity;
import passenger.entity.PointTransactionEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class PurchaseTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseTransactionId;
    private double amount;
    private Timestamp transactionDate;
    
    @ManyToOne
    private BusinessPartnerEntity bPartner;
    
    @ManyToOne
    private PassengerEntity passenger;
    
    @ManyToOne
    private CardEntity card;
    
    @OneToOne
    private PointTransactionEntity pointTrans;

    public PurchaseTransactionEntity(double amount, Timestamp transactionDate, BusinessPartnerEntity bPartner, PassengerEntity passenger, CardEntity card) {
        
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.bPartner = bPartner;
        this.passenger = passenger;
        this.card = card;
    }

    public PurchaseTransactionEntity() {
    }

    
    public Long getPurchaseTransactionId() {
        return purchaseTransactionId;
    }

    public void setPurchaseTransactionId(Long purchaseTransactionId) {
        this.purchaseTransactionId = purchaseTransactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BusinessPartnerEntity getbPartner() {
        return bPartner;
    }

    public void setbPartner(BusinessPartnerEntity bPartner) {
        this.bPartner = bPartner;
    }

    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

    public CardEntity getCard() {
        return card;
    }

    public void setCard(CardEntity card) {
        this.card = card;
    }

    public PointTransactionEntity getPointTrans() {
        return pointTrans;
    }

    public void setPointTrans(PointTransactionEntity pointTrans) {
        this.pointTrans = pointTrans;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purchaseTransactionId != null ? purchaseTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseTransactionEntity)) {
            return false;
        }
        PurchaseTransactionEntity other = (PurchaseTransactionEntity) object;
        if ((this.purchaseTransactionId == null && other.purchaseTransactionId != null) || (this.purchaseTransactionId != null && !this.purchaseTransactionId.equals(other.purchaseTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "businessPartner.entity.PurchaseTransactionEntity[ id=" + purchaseTransactionId + " ]";
    }
    
}
