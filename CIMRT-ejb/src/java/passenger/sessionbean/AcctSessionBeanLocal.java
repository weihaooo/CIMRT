/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Local;
import passenger.entity.PassengerEntity;

/**
 *
 * @author Yoona
 */
@Local
public interface AcctSessionBeanLocal {

    public int doLogin(String username, String password);

    public boolean resetPassword(String username, String secretQuestion, String secretAnswer);

    public boolean retrieveUsername(String email, String secretQuestion, String secretAnswer);

    public boolean checkCaptcha(String username);

    public boolean changePassword(String username, String password, String newPW, String newPW2);

    public boolean editProfile(String username, String firstName, String lastName, int phoneNumber, String email, String address);

    public boolean createAccount(String username, String password, String firstName, String lastName, String nric, int phoneNumber, String emailAddress, String address, Date dob, String secretQuestion, String secretAnswer,String gender);

    public PassengerEntity searchPassenger(String username);

    public boolean verifyAccount(String username);
}
