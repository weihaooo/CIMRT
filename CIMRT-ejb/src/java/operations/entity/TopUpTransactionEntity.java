/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.entity;

import commoninfra.entity.StaffEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import passenger.entity.CardEntity;
import passenger.entity.PointTransactionEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Yuting
 */
@Entity
@XmlRootElement
@XmlType(name = "topUpTransactionEntity", propOrder = {
    "transId",
    "type",
    "amount",
    "transDate",
    "card",
    "node",
    "staff"
})
public class TopUpTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transId;
    private String type;
    private double amount;
    private Timestamp transDate;
    @ManyToOne
    private CardEntity card;
    @ManyToOne
    private NodeEntity node;
    @ManyToOne
    private StaffEntity staff;

    @OneToOne
    private PointTransactionEntity pointTrans;

    public TopUpTransactionEntity() {
    }

    public TopUpTransactionEntity(String type, double amount, Timestamp transDate, StaffEntity staff, CardEntity card, NodeEntity node) {
        this.type = type;
        this.amount = amount;
        this.transDate = transDate;
        this.staff = staff;
        this.card = card;
        this.node = node;
    }

    public TopUpTransactionEntity(String type, double amount, Timestamp transDate, CardEntity card) {
        this.type = type;
        this.amount = amount;
        this.transDate = transDate;
        this.card = card;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
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

    public Timestamp getTransDate() {
        return transDate;
    }

    public void setTransDate(Timestamp transDate) {
        this.transDate = transDate;
    }

    public CardEntity getCard() {
        return card;
    }

    public void setCard(CardEntity card) {
        this.card = card;
    }

    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public PointTransactionEntity getPointTrans() {
        return pointTrans;
    }

    public void setPointTrans(PointTransactionEntity pointTrans) {
        this.pointTrans = pointTrans;
    }

}
