/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.entity;

import infraasset.entity.StationEntity;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import passenger.entity.CardEntity;
/**
 *
 * @author YuTing
 */
@Entity
@XmlRootElement
@XmlType(name = "fareTransactionEntity", propOrder = {
    "transactionId",
    "amount",
    "startStation",
    "endStation",
    "startTime",
    "endTime",
    "card",
    "trainSchedule",
    "specialDayAlgo"
})
public class FareTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private double amount;
    @OneToOne(cascade={CascadeType.PERSIST})
    private StationEntity startStation;
    @OneToOne(cascade={CascadeType.PERSIST})
    private StationEntity endStation;
    private Timestamp startTime;
    private Timestamp endTime;
    @ManyToOne
    private CardEntity card;
    @ManyToOne(cascade={CascadeType.ALL})
    private TrainScheduleEntity trainSchedule;
    @ManyToOne(cascade={CascadeType.ALL})
    private SpecialDayAlgoEntity specialDayAlgo;

    public FareTransactionEntity() {
    }

    public FareTransactionEntity(double amount, StationEntity startStation, StationEntity endStation, Timestamp startTime, Timestamp endTime) {
        this.amount = amount;
        this.startStation = startStation;
        this.endStation = endStation;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public StationEntity getStartStation() {
        return startStation;
    }

    public void setStartStation(StationEntity startStation) {
        this.startStation = startStation;
    }

    public StationEntity getEndStation() {
        return endStation;
    }

    public void setEndStation(StationEntity endStation) {
        this.endStation = endStation;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public CardEntity getCard() {
        return card;
    }

    public void setCard(CardEntity card) {
        this.card = card;
    }

    public TrainScheduleEntity getTrainSchedule() {
        return trainSchedule;
    }

    public void setTrainSchedule(TrainScheduleEntity trainSchedule) {
        this.trainSchedule = trainSchedule;
    }

    public SpecialDayAlgoEntity getSpecialDayAlgo() {
        return specialDayAlgo;
    }

    public void setSpecialDayAlgo(SpecialDayAlgoEntity specialDayAlgo) {
        this.specialDayAlgo = specialDayAlgo;
    }

}
