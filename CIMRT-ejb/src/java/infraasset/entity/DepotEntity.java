/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import routefare.entity.NodeEntity;

/**
 *
 * @author Zhirong
 */
@Entity
public class DepotEntity extends NodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "depot")
    private List<RollingStockEntity> rollingStocks;

    public DepotEntity() {

    }

    public DepotEntity(String infraId, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation, double latitude, double longitude) {
        super(infraId, code, infraName, address, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
    }

    public List<RollingStockEntity> getRollingStocks() {
        return rollingStocks;
    }

    public void setRollingStocks(List<RollingStockEntity> rollingStocks) {
        this.rollingStocks = rollingStocks;
    }

}
