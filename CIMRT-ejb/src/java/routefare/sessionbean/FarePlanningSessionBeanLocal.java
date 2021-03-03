/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.sessionbean;

import java.util.ArrayList;
import javax.ejb.Local;
import routefare.entity.FareAlgoEntity;

/**
 *
 * @author zhuming
 */
@Local
public interface FarePlanningSessionBeanLocal {
    public boolean updateFareStructure(String passengerType, String fareType, double newBaseFee, double newIncrementRate);
    public ArrayList<FareAlgoEntity> getFareAlgo();
    public ArrayList<String> getFareType(String passengerType);
    public ArrayList<FareAlgoEntity> getFareAlgoConcession();
    public double getBaseFee(String pType, String fType);
    public double getIncrementalFee(String pType, String fType);
}
