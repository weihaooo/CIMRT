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
public class LeasingSpaceEntity extends AssetEntity implements Serializable {

    private String unitNumber;
    private double floorArea;
    private boolean waterProvision;
    private double rentalFee;
    private int leaseYear;
    private String leaseDescription;
    private String status;
    private Timestamp deadline;

    public LeasingSpaceEntity(){
        
    }
    
    /* assetName: preferred Usage (retail/commerical f&b)*/

    public LeasingSpaceEntity(String assetId, String assetName,String unitNumber, double floorArea, boolean waterProvision, double rentalFee, int leaseYear, String leaseDescription, String status, Timestamp deadline) {
        super(assetId, assetName);
        this.unitNumber = unitNumber;
        this.floorArea = floorArea;
        this.waterProvision = waterProvision;
        this.rentalFee = rentalFee;
        this.leaseYear = leaseYear;
        this.leaseDescription = leaseDescription;
        this.status = status;
        this.deadline = deadline;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }
   
    

  
    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }

    public boolean isWaterProvision() {
        return waterProvision;
    }

    public void setWaterProvision(boolean waterProvision) {
        this.waterProvision = waterProvision;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public int getLeaseYear() {
        return leaseYear;
    }

    public void setLeaseYear(int leaseYear) {
        this.leaseYear = leaseYear;
    }

    public String getLeaseDescription() {
        return leaseDescription;
    }

    public void setLeaseDescription(String leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
    
}
