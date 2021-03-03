/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import routefare.entity.NodeEntity;

/**
 *
 * @author Zhirong
 */
@Entity
@XmlRootElement
@XmlType(name = "stationEntity", propOrder ={

})
public class StationEntity extends NodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public StationEntity() {

    }

    public StationEntity(String infraId, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation, double latitude, double longitude) {
        super(infraId, code, infraName, address, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
    }


}
