/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessPartner.sessionbean;

import businessPartner.entity.BusinessPartnerEntity;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import commoninfra.sessionbean.EmailManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yoona
 */
@Stateless
public class ProfileSessionBean implements ProfileSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private static SecureRandom random = new SecureRandom();

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

    public static final String ACCOUNT_SID = "AC3a4248d9b807f430a58b892383604776";
    public static final String AUTH_TOKEN = "c272a9c5ca820c800eebc93d7a7a6d0c";
    public static final String FROM_NUMBER = "+1 302-404-2101 ";

    @Override
    public int doLogin(String partnerId, String password) {
        BusinessPartnerEntity partner;
        StringBuffer sb = new StringBuffer();
        password = "ES01" + password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }

            partner = searchPartner(partnerId);

            //Login 6 - No such user
            //      0 - Account Locked
            //      1 to 4 - Wrong password
            //      5 - Successful login
            //      7 - Need to change pw
            //      8 - Need to verify
            if (partner == null) {
                return 6;
            } else {

                if (!(partner.getPassword().equals(sb.toString())) || partner.getAccountLock() == true) {
                    if (partner.getAccountLock() == true) {
                        return 0;
                    } else if (!(partner.getPassword().equals(sb.toString()))) {

                        partner.setWrongPasswordCount(partner.getWrongPasswordCount() + 1);
                        em.flush();
                        if (partner.getWrongPasswordCount() > 4) {
                            partner.setAccountLock(true);
                        }
                        return 5 - partner.getWrongPasswordCount();
                    }
                } else {

                    if (partner.getPwChange() == true) {
                        partner.setWrongPasswordCount(0);
                        return 7;
                    }
                    if (partner.getVerified() == false) {
                        partner.setWrongPasswordCount(0);
                        return generateVerificationCode(partner.getPhoneNumber());
                    }
                    partner.setWrongPasswordCount(0);
                    return 5;
                }
            }

        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
        return 6;
    }

    @Override
    public boolean resetPassword(String partnerId, String secretQuestion, String secretAnswer) {
        Query q = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b WHERE b.partnerId=:partnerId AND b.secretQuestion=:secretQuestion AND b.secretAnswer=:secretAnswer");
        q.setParameter("secretQuestion", secretQuestion);
        q.setParameter("secretAnswer", secretAnswer);
        q.setParameter("partnerId", partnerId);
        List<BusinessPartnerEntity> result = q.getResultList();
        boolean success = true;

        if (result.isEmpty()) {
            return false;
        }

        BusinessPartnerEntity partner = (BusinessPartnerEntity) result.get(0);
        EmailManager emailManager = new EmailManager();

        String saltedPassword = "ES01";
        String password = "";
        String randomString = ALPHA_CAPS + ALPHA + SPECIAL_CHARS + NUMERIC;
        for (int i = 0; i < 13; i++) {
            int index = random.nextInt(randomString.length());
            password += randomString.charAt(index);
        }

        StringBuffer sb = new StringBuffer();
        saltedPassword += password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(saltedPassword.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }

            success = emailManager.emailPassword(password, 10, "e0002252@u.nus.edu", partner.getEmailAddress());

            if (success) {
                partner.setPassword(sb.toString());
                partner.setAccountLock(false);
                partner.setWrongPasswordCount(0);
                partner.setPwChange(true);
                em.flush();
            }

        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }

        return success;

    }

    @Override
    public boolean retrievePartnerId(String email, String secretQuestion, String secretAnswer) {

        Query q = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b WHERE b.emailAddress=:email AND b.secretQuestion=:secretQuestion AND b.secretAnswer=:secretAnswer");
        q.setParameter("secretQuestion", secretQuestion);
        q.setParameter("secretAnswer", secretAnswer);
        q.setParameter("email", email);
        List<BusinessPartnerEntity> result = q.getResultList();

        if (result.isEmpty()) {
            return false;
        }
        BusinessPartnerEntity partner = (BusinessPartnerEntity) result.get(0);
        EmailManager emailManager = new EmailManager();

        return emailManager.emailPartnerId(partner.getPartnerId(), "e0002252@u.nus.edu", partner.getEmailAddress());

    }

    @Override
    public boolean editProfile(String partnerId, String company, int phoneNumber, String emailAddress, String address, int faxNumber, String companyProfile) {

        BusinessPartnerEntity partner = searchPartner(partnerId);
        if (partner == null) {
            return false;
        }
        partner.setCompany(company);
        partner.setPhoneNumber(phoneNumber);
        partner.setEmailAddress(emailAddress);
        partner.setAddress(address);
        partner.setCompanyProfile(companyProfile);
        partner.setFaxNumber(faxNumber);
        em.flush();
        return true;

    }

    @Override
    public boolean editSubscription(String partnerId, boolean subscription) {
        BusinessPartnerEntity partner = searchPartner(partnerId);
        if (partner == null) {
            return false;
        }
        partner.setSubscription(subscription);
        em.flush();
        return true;
    }

    @Override
    public boolean changePassword(String partnerId, String password, String newPW, String newPW2) {

        if (!newPW.equals(newPW2)) {

            return false;
        }

        BusinessPartnerEntity partner;
        StringBuffer sb = new StringBuffer();
        password = "ES01" + password;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }
            partner = searchPartner(partnerId);
            //Login 0 - No such user or wrong password
            //      1 - Account locked
            //      2 - Successful login
            if (partner == null) {
                return false;
            } else {
                if (!(partner.getPassword().equals(sb.toString()))) {
                    return false;
                } else {
                    StringBuffer sbNew = new StringBuffer();
                    password = "ES01" + newPW;
                    MessageDigest mdNew = MessageDigest.getInstance("MD5");
                    byte[] hashSumNew = mdNew.digest(password.getBytes());

                    for (int i = 0; i < hashSumNew.length; ++i) {
                        sbNew.append(Integer.toHexString((hashSumNew[i] & 0xFF) | 0x100).substring(1, 3));
                    }
                    partner.setPassword(sbNew.toString());
                    partner.setPwChange(false);
                    em.flush();

                    return true;
                }
            }
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
        return false;
    }

    @Override
    public boolean checkCaptcha(String partnerId) {

        BusinessPartnerEntity partner = searchPartner(partnerId);
        if (partner == null) {
            return false;
        } else {
            if (partner.getAccountLock()) {
                return false;
            } else if (partner.getWrongPasswordCount() > 2) {
                return true;
            }
        }

        return false;
    }

    private int generateVerificationCode(int toNumber) {

        Random random = new Random();
        Integer code = random.nextInt(999999
                - 100000 + 1) + 100000;

        String smsMessage = "Your OTP is: " + code;

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        com.twilio.rest.api.v2010.account.Message tMessage;
        tMessage = com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber("+65" + toNumber), new PhoneNumber(FROM_NUMBER), smsMessage).create();
        System.err.println("Sent: " + tMessage.getSid());

        return code;

    }

    @Override
    public BusinessPartnerEntity searchPartner(String partnerId) {
        Query q = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b WHERE b.partnerId=:partnerId");
        q.setParameter("partnerId", partnerId);
        ArrayList<BusinessPartnerEntity> result = (ArrayList<BusinessPartnerEntity>) q.getResultList();
        BusinessPartnerEntity partner;
        if (result.isEmpty()) {
            return null;
        } else {
            partner = result.get(0);
            return partner;
        }
    }

    @Override
    public String createAccount(String password, String company, int phoneNumber, String email, String address, String secretQuestion, String secretAnswer, boolean subscription, String companyProfile, int faxNumber) {

        Query q = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b WHERE b.emailAddress=:email");
        q.setParameter("email", email);
        ArrayList<BusinessPartnerEntity> result = new ArrayList<BusinessPartnerEntity>(q.getResultList());
        BusinessPartnerEntity partner;

        //If there is no partner with the same email
        if (result.isEmpty()) {
            Query q1 = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b");
            //Count the size of staff from the particular department
            int count = q1.getResultList().size();
            count = count + 1;
            String id;
            //Get the length of count
            int length;
            try {
                //Hashing the password
                password = "ES01" + password;
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hashSum = md.digest(password.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < hashSum.length; ++i) {
                    sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
                }

                while (true) {
                    id = "BP";
                    length = String.valueOf(count).length();
                    for (int i = 0; i < 6 - length; i++) {
                        id = id + "0";
                    }
                    id = id + String.valueOf(count);
                    Query q2 = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b WHERE b.partnerId=:partnerId");
                    q2.setParameter("partnerId", id);
                    if (!q2.getResultList().isEmpty()) {
                        count *= 2;
                    } else {
                        break;
                    }
                }
                partner = new BusinessPartnerEntity(id, phoneNumber, email, sb.toString(), company, address, secretQuestion, secretAnswer, false, subscription, companyProfile, faxNumber);
                em.persist(partner);
                em.flush();
                return id;

            } catch (NoSuchAlgorithmException nsae) {
                System.out.println(nsae);
            }
        }
        return "";
    }

    @Override
    public boolean verifyAccount(String partnerId) {

        BusinessPartnerEntity partner = searchPartner(partnerId);
        if (partner == null) {
            return false;
        } else {
            partner.setVerified(true);
            return true;
        }
    }
    
}
