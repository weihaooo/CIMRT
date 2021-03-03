/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author Zhirong
 */
@Entity
public class TrackEntity extends InfrastructureEntity implements Serializable {

    private String trainLine;
    private String location;

    public TrackEntity() {

    }

    public TrackEntity(String infraId, String infraName, String trainLine, String location) {
        super(infraId, infraName);
        this.trainLine = trainLine;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrainLine() {
        return trainLine;
    }

    public void setTrainLine(String trainLine) {
        this.trainLine = trainLine;
    }

}
