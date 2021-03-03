/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import commoninfra.sessionbean.EmailManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import passenger.entity.PassengerEntity;

/**
 *
 * @author Yoona
 */
@Stateless
public class AcctSessionBean implements AcctSessionBeanLocal {

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
    public int doLogin(String username, String password) {
        PassengerEntity passenger;
        StringBuffer sb = new StringBuffer();
        password = "ES01" + password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }

            passenger = searchPassenger(username);

            //Login 6 - No such user
            //      0 - Account Locked
            //      1 to 4 - Wrong password
            //      5 - Successful login
            //      7 - Need to change pw
            //      8 - Need to verify
            if (passenger == null) {
                return 6;
            } else {

                if (!(passenger.getPassword().equals(sb.toString())) || passenger.getAccountLock() == true) {
                    if (passenger.getAccountLock() == true) {
                        return 0;
                    } else if (!(passenger.getPassword().equals(sb.toString()))) {

                        passenger.setWrongPasswordCount(passenger.getWrongPasswordCount() + 1);
                        em.flush();
                        if (passenger.getWrongPasswordCount() > 4) {
                            passenger.setAccountLock(true);
                        }
                        return 5 - passenger.getWrongPasswordCount();
                    }
                } else {

                    if (passenger.getPwChange() == true) {
                        passenger.setWrongPasswordCount(0);
                        return 7;
                    }
                    if (passenger.getVerified() == false) {
                        passenger.setWrongPasswordCount(0);
                        return generateVerificationCode(passenger.getPhoneNumber());
                    }
                    passenger.setWrongPasswordCount(0);
                    return 5;
                }
            }

        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
        return 6;
    }

    @Override
    public boolean resetPassword(String username, String secretQuestion, String secretAnswer) {
        Query q = em.createQuery("SELECT p FROM PassengerEntity AS p WHERE p.username=:username AND p.secretQuestion=:secretQuestion AND p.secretAnswer=:secretAnswer");
        q.setParameter("secretQuestion", secretQuestion);
        q.setParameter("secretAnswer", secretAnswer);
        q.setParameter("username", username);
        List<PassengerEntity> result = q.getResultList();
        boolean success = true;

        if (result.isEmpty()) {
            return false;
        }

        PassengerEntity passenger = (PassengerEntity) result.get(0);
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

            success = emailManager.emailPassword(password, 12, "e0002252@u.nus.edu", passenger.getEmail());

            if (success) {
                passenger.setPassword(sb.toString());
                passenger.setAccountLock(false);
                passenger.setWrongPasswordCount(0);
                passenger.setPwChange(true);
                em.flush();
            }

        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }

        return success;

    }

    @Override
    public boolean retrieveUsername(String email, String secretQuestion, String secretAnswer) {

        Query q = em.createQuery("SELECT p FROM PassengerEntity AS p WHERE p.email=:email AND p.secretQuestion=:secretQuestion AND p.secretAnswer=:secretAnswer");
        q.setParameter("secretQuestion", secretQuestion);
        q.setParameter("secretAnswer", secretAnswer);
        q.setParameter("email", email);
        List<PassengerEntity> result = q.getResultList();

        if (result.isEmpty()) {
            return false;
        }
        PassengerEntity passenger = (PassengerEntity) result.get(0);
        EmailManager emailManager = new EmailManager();

        return emailManager.emailPartnerId(passenger.getUsername(), "e0002252@u.nus.edu", passenger.getEmail());

    }

    @Override
    public boolean editProfile(String username,String firstName, String lastName, int phoneNumber, String email, String address) {

        PassengerEntity passenger = searchPassenger(username);
        if (passenger == null) {
            return false;
        }
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setPhoneNumber(phoneNumber);
        passenger.setEmail(email);
        passenger.setAddress(address);
        em.flush();
        return true;

    }

    @Override
    public boolean changePassword(String username, String password, String newPW, String newPW2) {

        if (!newPW.equals(newPW2)) {

            return false;
        }

        PassengerEntity passenger;
        StringBuffer sb = new StringBuffer();
        password = "ES01" + password;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }
            passenger = searchPassenger(username);
            //Login 0 - No such user or wrong password
            //      1 - Account locked
            //      2 - Successful login
            if (passenger == null) {
                return false;
            } else {
                if (!(passenger.getPassword().equals(sb.toString()))) {
                    return false;
                } else {
                    StringBuffer sbNew = new StringBuffer();
                    password = "ES01" + newPW;
                    MessageDigest mdNew = MessageDigest.getInstance("MD5");
                    byte[] hashSumNew = mdNew.digest(password.getBytes());

                    for (int i = 0; i < hashSumNew.length; ++i) {
                        sbNew.append(Integer.toHexString((hashSumNew[i] & 0xFF) | 0x100).substring(1, 3));
                    }
                    passenger.setPassword(sbNew.toString());
                    passenger.setPwChange(false);
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
    public boolean checkCaptcha(String username) {

        PassengerEntity passenger = searchPassenger(username);
        if (passenger == null) {
            return false;
        } else {
            if (passenger.getAccountLock()) {
                return false;
            } else if (passenger.getWrongPasswordCount() > 2) {
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
    public PassengerEntity searchPassenger(String username) {
        Query q = em.createQuery("SELECT p FROM PassengerEntity AS p WHERE p.username=:username");
        q.setParameter("username", username);
        ArrayList<PassengerEntity> result = (ArrayList<PassengerEntity>) q.getResultList();
        PassengerEntity passenger;
        if (result.isEmpty()) {
            return null;
        } else {
            passenger = result.get(0);
            return passenger;
        }
    }

    @Override
    public boolean createAccount(String username, String password, String firstName, String lastName, String nric, int phoneNumber, String email, String address,Date dob, String secretQuestion, String secretAnswer, String gender) {

        Query q = em.createQuery("SELECT p FROM PassengerEntity AS p WHERE p.email=:email OR p.nric=:nric OR p.username=:username");
        q.setParameter("email", email);
        q.setParameter("nric", nric);
        q.setParameter("username", username);
        ArrayList<PassengerEntity> result = new ArrayList<>(q.getResultList());
        PassengerEntity passenger;

        if (result.isEmpty()) {

            try {
                //Hashing the password
                password = "ES01" + password;
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hashSum = md.digest(password.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < hashSum.length; ++i) {
                    sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
                }

                passenger = new PassengerEntity(username, firstName, lastName, nric, address, email, phoneNumber, sb.toString(), secretQuestion, secretAnswer, dob, false, gender,0);
                em.persist(passenger);
                em.flush();
                return true;

            } catch (NoSuchAlgorithmException nsae) {
                System.out.println(nsae);
            }
        }
        return false;
    }

    @Override
    public boolean verifyAccount(String username) {

        PassengerEntity passenger = searchPassenger(username);
        if (passenger == null) {
            return false;
        } else {
            passenger.setVerified(true);
            return true;
        }
    }
}
