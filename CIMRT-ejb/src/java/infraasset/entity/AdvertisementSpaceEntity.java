/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;

/**
 *
 * @author kayleytan
 */
@Entity 
public class AdvertisementSpaceEntity extends AssetEntity implements Serializable {

   private String location;
   private String type;
   private double peakPeriodFee;
   private double nonpeakPeriodFee;
   private String status;
   private Timestamp deadline;
   private String numberCode;

   
   public AdvertisementSpaceEntity(){
       
   }

   /* assetName: rolling stock or station
      location: Cabin what or Location at station
      type: in-train/window stickers/concept train*/

    public AdvertisementSpaceEntity(String assetId, String assetName,String location, String type, double peakPeriodFee, double nonpeakPeriodFee, String status, Timestamp deadline,String numberCode) {
        super(assetId, assetName);
        this.location = location;
        this.type = type;
        this.peakPeriodFee = peakPeriodFee;
        this.nonpeakPeriodFee = nonpeakPeriodFee;
        this.status = status;
        this.deadline = deadline;
        this.numberCode = numberCode;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    } 
   
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPeakPeriodFee() {
        return peakPeriodFee;
    }

    public void setPeakPeriodFee(double peakPeriodFee) {
        this.peakPeriodFee = peakPeriodFee;
    }

    public double getNonpeakPeriodFee() {
        return nonpeakPeriodFee;
    }

    public void setNonpeakPeriodFee(double nonpeakPeriodFee) {
        this.nonpeakPeriodFee = nonpeakPeriodFee;
    }

   
 
   
 
   
}
