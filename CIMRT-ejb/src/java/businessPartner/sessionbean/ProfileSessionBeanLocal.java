/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessPartner.sessionbean;

import businessPartner.entity.BusinessPartnerEntity;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author Yoona
 */
@Local
public interface ProfileSessionBeanLocal {

    public int doLogin(String partnerId, String password);

    public boolean resetPassword(String partnerId, String secretQuestion, String secretAnswer);

    public boolean retrievePartnerId(String email, String secretQuestion, String secretAnswer);

    public boolean checkCaptcha(String partnerId);

    public boolean changePassword(String partnerId, String password, String newPW, String newPW2);


    public boolean editProfile(String partnerId, String company, int phoneNumber, String emailAddress, String address, int faxNumber, String companyProfile);
    
    public boolean editSubscription(String partnerId, boolean subscription);

    public String createAccount(String password, String company, int phoneNumber, String emailAddress, String address, String secretQuestion, String secretAnswer,boolean subscription, String companyProfile, int faxNumber);
    
    public BusinessPartnerEntity searchPartner(String partnerId);

    public boolean verifyAccount(String partnerId);

}
