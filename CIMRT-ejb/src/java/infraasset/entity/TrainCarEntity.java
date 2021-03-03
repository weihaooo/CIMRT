/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import operations.entity.MaintenanceRequestEntity;

/**
 *
 * @author Zhirong
 */
@Entity
public class TrainCarEntity extends AssetEntity implements Serializable {

    private String status;
    private String brand;
    private int carCode;
    private String type;
    @Column(length = 10000)
    private String description;

    public TrainCarEntity() {

    }

    public TrainCarEntity(String assetId, String assetName, int carCode, String brand, String status, String type, String description) {
        super(assetId, assetName);
        this.carCode = carCode;
        this.brand = brand;
        this.status = status;
        this.type = type;
        this.description = description;
    }

    public int getCarCode() {
        return carCode;
    }

    public void setCarCode(int carCode) {
        this.carCode = carCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
