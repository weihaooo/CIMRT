/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessPartner.sessionbean;

import javax.ejb.Local;

/**
 *
 * @author Yoona
 */
@Local
public interface TransactionSessionBeanLocal {

    public boolean submitTransaction(String partnerId, String type, String nric, String cardId, double amount);
    
}
