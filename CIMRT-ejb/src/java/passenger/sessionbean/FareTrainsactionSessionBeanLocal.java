/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import java.sql.Timestamp;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author Yuting
 */
@Local
public interface FareTrainsactionSessionBeanLocal {

    public ArrayList<String> getStations();
    public String createFareTransaction(String cardId, String station);
    
}
