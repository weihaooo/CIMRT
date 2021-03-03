/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.sessionbean;

import javax.ejb.Local;

/**
 *
 * @author Yoona
 */
@Local
public interface DataSessionBeanLocal {
    public void createRosterBasis();
        
}
