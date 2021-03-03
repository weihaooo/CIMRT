/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import java.util.List;
import javax.ejb.Local;
import passenger.entity.PassengerEntity;
import passenger.entity.PassengerFeedbackEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author FABIAN
 */
@Local
public interface PassengerFeedbackSessionBeanLocal {
    public boolean submitFeedback(String description, String submitter, String subject, int mobileNo, String selectedStation);
    public PassengerEntity searchPassenger(String username);
    public List<NodeEntity> getStations();
    public List<PassengerFeedbackEntity> getPassengerFeedbacks(String address);
    public List<PassengerFeedbackEntity> getAllFeedbacks();

}
