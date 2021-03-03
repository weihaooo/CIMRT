/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenderModule.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author kayleytan
 */
@Entity
public class DeliveryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;
    private Timestamp dateAndTime;
    private String status;
    
    @OneToOne(cascade={CascadeType.PERSIST})
    private BidEntity bid;

    public DeliveryEntity() {
    }

    public DeliveryEntity(Timestamp dateAndTime, String status) {
        this.dateAndTime = dateAndTime;
        this.status = status;
    }

    
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Timestamp getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Timestamp dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BidEntity getBid() {
        return bid;
    }

    public void setBid(BidEntity bid) {
        this.bid = bid;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deliveryId != null ? deliveryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeliveryEntity)) {
            return false;
        }
        DeliveryEntity other = (DeliveryEntity) object;
        if ((this.deliveryId == null && other.deliveryId != null) || (this.deliveryId != null && !this.deliveryId.equals(other.deliveryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tenderModule.entity.DeliveryEntity[ id=" + deliveryId + " ]";
    }
    
}
