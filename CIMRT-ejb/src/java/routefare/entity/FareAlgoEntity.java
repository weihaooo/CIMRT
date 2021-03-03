/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author zhuming
 */
@Entity
@XmlRootElement
@XmlType(name = "fareAlgoEntity", propOrder ={
    
    "fareAlgoId",
    "passengerType",
    "fareType",
    "baseFee",
    "incrementRate"
    
})

public class FareAlgoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fareAlgoId;
    private String passengerType;
    private String fareType;
    private double baseFee;
    private double incrementRate;
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="fareAlgo")
    private List<TrainScheduleEntity> trainSchedules = new ArrayList<TrainScheduleEntity>();
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="fareAlgo")
    private List<SpecialDayAlgoEntity> specialDayAlgos = new ArrayList<SpecialDayAlgoEntity>();
    
    public FareAlgoEntity() {
    }

    public FareAlgoEntity(String passengerType, String fareType, double baseFee, double incrementRate) {
        this.passengerType = passengerType;
        this.fareType = fareType;
        this.baseFee = baseFee;
        this.incrementRate = incrementRate;
    }

    public Long getFareAlgoId() {
        return fareAlgoId;
    }

    public void setFareAlgoId(Long id) {
        this.fareAlgoId = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public String getFareType() {
        return fareType;
    }

    public double getBaseFee() {
        return baseFee;
    }

    public double getIncrementRate() {
        return incrementRate;
    }

    public void setPassengerType(String PassengerType) {
        this.passengerType = PassengerType;
    }

    public void setFareType(String FareType) {
        this.fareType = FareType;
    }

    public void setBaseFee(double baseFee) {
        this.baseFee = baseFee;
    }

    public void setIncrementRate(double incrementRate) {
        this.incrementRate = incrementRate;
    }

    public List<TrainScheduleEntity> getTrainSchedules() {
        return trainSchedules;
    }

    public void setTrainSchedules(List<TrainScheduleEntity> trainSchedules) {
        this.trainSchedules = trainSchedules;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareAlgoId != null ? fareAlgoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FareAlgoEntity)) {
            return false;
        }
        FareAlgoEntity other = (FareAlgoEntity) object;
        if ((this.fareAlgoId == null && other.fareAlgoId != null) || (this.fareAlgoId != null && !this.fareAlgoId.equals(other.fareAlgoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FareEntity[ id=" + fareAlgoId + " ]";
    }
    
}
