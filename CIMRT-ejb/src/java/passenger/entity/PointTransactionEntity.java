/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.entity;

import operations.entity.TopUpTransactionEntity;
import businessPartner.entity.BusinessPartnerEntity;
import businessPartner.entity.PurchaseTransactionEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Yuting
 */
@Entity
public class PointTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointTransId;
    private Timestamp transDateTime;
    private int amount;
    
    @ManyToOne
    private PassengerEntity passenger; 
    
    @OneToOne(mappedBy="pointTrans")
    private TopUpTransactionEntity topUpTrans;
    
    @OneToOne(mappedBy="pointTrans")
    private PurchaseTransactionEntity purchaseTrans;
    
    @ManyToOne
    private CardEntity card;
    
    public PointTransactionEntity() {
    }

    public PointTransactionEntity(Timestamp transDateTime, int amount, PassengerEntity passenger, TopUpTransactionEntity topUpTrans, PurchaseTransactionEntity purchaseTrans, CardEntity card) {
        
        this.transDateTime = transDateTime;
        this.amount = amount;
        this.passenger = passenger;
        this.topUpTrans = topUpTrans;
        this.purchaseTrans = purchaseTrans;
        this.card = card;
    }

    public Long getPointTransId() {
        return pointTransId;
    }

    public void setPointTransId(Long pointTransId) {
        this.pointTransId = pointTransId;
    }

    public Timestamp getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(Timestamp transDateTime) {
        this.transDateTime = transDateTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

    public TopUpTransactionEntity getTopUpTrans() {
        return topUpTrans;
    }

    public void setTopUpTrans(TopUpTransactionEntity topUpTrans) {
        this.topUpTrans = topUpTrans;
    }

    public PurchaseTransactionEntity getPurchaseTrans() {
        return purchaseTrans;
    }

    public void setPurchaseTrans(PurchaseTransactionEntity purchaseTrans) {
        this.purchaseTrans = purchaseTrans;
    }

    public CardEntity getCard() {
        return card;
    }

    public void setCard(CardEntity card) {
        this.card = card;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pointTransId != null ? pointTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PointTransactionEntity)) {
            return false;
        }
        PointTransactionEntity other = (PointTransactionEntity) object;
        if ((this.pointTransId == null && other.pointTransId != null) || (this.pointTransId != null && !this.pointTransId.equals(other.pointTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "passenger.entity.PointTransaction[ id=" + pointTransId + " ]";
    }
    
}
