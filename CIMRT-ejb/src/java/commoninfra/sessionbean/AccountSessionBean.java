package commoninfra.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;

@Stateful
public class AccountSessionBean implements AccountSessionBeanLocal, AccountSessionBeanRemote {

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
    public int doLogin(String staffId, String password) {
        StaffEntity staff;
        StringBuffer sb = new StringBuffer();
        if(password == null){
            return 6;
        }
        password = "ES01" + password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }

            staff = searchStaff(staffId);

            //Login 0 - No such user or wrong password
            //      1 - Account locked
            //      2 - Successful login
            //      3 - Initial login
            if (staff == null) {
                return 6;
            } else {

                if (!(staff.getPassword().equals(sb.toString())) || staff.getAccountLock() == true) {
                    if (staff.getAccountLock() == true) {
                        return 0;
                    } else if (!(staff.getPassword().equals(sb.toString()))) {

                        staff.setWrongPasswordCount(staff.getWrongPasswordCount() + 1);
                        em.flush();
                        if (staff.getWrongPasswordCount() > 4) {
                            staff.setAccountLock(true);
                        }
                        return 5 - staff.getWrongPasswordCount();
                    }
                } else {

                    if (password.equals("ES01default")) {
                        staff.setWrongPasswordCount(0);
                        int code = generateVerificationCode(staff.getPhoneNumber());
                        return code;
                    }
                    staff.setWrongPasswordCount(0);
                    return 5;
                }
            }

        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
        return 6;
    }

    @Override
    public boolean resetPassword(String staffId, String nric) {
        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.nric=:nric AND s.staffId=:staffId");
        q.setParameter("nric", nric);
        q.setParameter("staffId", staffId);
        List<StaffEntity> result = q.getResultList();
        boolean success = true;

        if (result.isEmpty()) {
            return false;
        }

        StaffEntity staff = (StaffEntity) result.get(0);
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

            success = emailManager.emailPassword(password, 1, "e0002252@u.nus.edu", staff.getEmailAddress());

            if (success) {
                staff.setPassword(sb.toString());
                staff.setAccountLock(false);
                staff.setWrongPasswordCount(0);
                em.flush();
            }

        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }

        return success;

    }

    @Override
    public boolean retrieveStaffId(String nric) {

        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.nric=:nric");
        q.setParameter("nric", nric);
        List<StaffEntity> result = q.getResultList();

        if (result.isEmpty()) {
            return false;
        }
        StaffEntity staff = (StaffEntity) result.get(0);
        EmailManager emailManager = new EmailManager();

        /*return emailManager.emailStaffId(staff.getStaffId(), 0, "e0002252@u.nus.edu", staff.getEmailAddress());*/
        return emailManager.emailStaffId(staff.getStaffId(), 0, "e0002252@u.nus.edu", staff.getEmailAddress());

    }

    @Override
    public boolean editProfile(ArrayList<String> details) {

        if(details.isEmpty()){
            return false;
        }
        
        StaffEntity staff = searchStaff(details.get(0));
        if (staff == null) {
            return false;
        }
        staff.setPhoneNumber(details.get(1));
        staff.setEmailAddress(details.get(2));
        staff.setAddress(details.get(3));
        staff.setMaritalStatus(details.get(4));
        staff.setReligion(details.get(5));
        staff.setEducationQualification(details.get(6));

        return true;

    }

    @Override
    public ArrayList<String> viewProfile(String staffId) {

        ArrayList<String> staffDetails = new ArrayList<String>();
        StaffEntity staff = searchStaff(staffId);
        if (staff == null) {
            return staffDetails;
        }
        staffDetails.add(staff.getFirstName());
        staffDetails.add(staff.getLastName());
        staffDetails.add(staff.getNric());

        staffDetails.add(staff.getPhoneNumber());
        staffDetails.add(staff.getEmailAddress());
        staffDetails.add(staff.getAddress());
        staffDetails.add(staff.getGender());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(staff.getDateOfBirth());

        staffDetails.add(date);
        staffDetails.add(staff.getMaritalStatus());
        staffDetails.add(staff.getRace());
        staffDetails.add(staff.getNationality());
        staffDetails.add(staff.getReligion());
        staffDetails.add(staff.getEducationQualification());
        staffDetails.add(Integer.toString(staff.getSalary()));
        staffDetails.add(staff.getStaffRole().getStaffRole());
        staffDetails.add(Integer.toString(staff.getMcEntitlement()));
        staffDetails.add(Integer.toString(staff.getLeaveEntitlement()));

        Query q1 = em.createQuery("SELECT s FROM StationStaffEntity AS s WHERE s.staffId=:staffId");
        q1.setParameter("staffId", staffId);
        ArrayList<StationStaffEntity> result1 = (ArrayList<StationStaffEntity>) q1.getResultList();

        if (result1.isEmpty()) {
        } else {

            StationStaffEntity stationStaff = result1.get(0);

            if (stationStaff.getStationTeam() != null) {
                staffDetails.add(stationStaff.getStationTeam().getTeamId().toString());
                staffDetails.add(stationStaff.getStationTeam().getNode().getCode());
            }

            return staffDetails;
        }

        Query q2 = em.createQuery("SELECT s FROM DepotStaffEntity AS s WHERE s.staffId=:staffId");
        q2.setParameter("staffId", staffId);
        ArrayList<DepotStaffEntity> result2 = (ArrayList<DepotStaffEntity>) q2.getResultList();
        if (result2.isEmpty()) {
        } else {

            DepotStaffEntity depotStaff = result2.get(0);

            if (depotStaff.getDepotTeam() != null) {
                staffDetails.add(depotStaff.getDepotTeam().getTeamId().toString());
                staffDetails.add(depotStaff.getDepotTeam().getNode().getCode());
            }
        }

        return staffDetails;
    }

    @Override
    public boolean changePassword(String staffId, String password, String newPW, String newPW2) {
        StaffEntity staff;
        StringBuffer sb = new StringBuffer();
        password = "ES01" + password;
        if(!newPW.equals(newPW2)){
            return false;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());

            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }
            staff = searchStaff(staffId);
            //Login 0 - No such user or wrong password
            //      1 - Account locked
            //      2 - Successful login
            if (staff == null) {
                return false;
            } else {
                if (!(staff.getPassword().equals(sb.toString()))) {
                    return false;
                } else {
                    StringBuffer sbNew = new StringBuffer();
                    password = "ES01" + newPW;
                    MessageDigest mdNew = MessageDigest.getInstance("MD5");
                    byte[] hashSumNew = mdNew.digest(password.getBytes());

                    for (int i = 0; i < hashSumNew.length; ++i) {
                        sbNew.append(Integer.toHexString((hashSumNew[i] & 0xFF) | 0x100).substring(1, 3));
                    }
                    staff.setPassword(sbNew.toString());
                    em.persist(staff);

                    return true;
                }
            }
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
        return false;
    }

    @Override
    public boolean checkCaptcha(String staffId) {

        StaffEntity staff = searchStaff(staffId);
        if (staff == null) {
            return false;
        } else {
            if (staff.getAccountLock()) {
                return false;
            } else if (staff.getWrongPasswordCount() > 2) {
                return true;
            }
        }

        return false;
    }

    private int generateVerificationCode(String toNumber) {

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
    public StaffEntity searchStaff(String staffId) {
        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        ArrayList<StaffEntity> result = (ArrayList<StaffEntity>) q.getResultList();
        StaffEntity staff;
        if (result.isEmpty()) {
            return null;
        } else {
            staff = result.get(0);
            return staff;
        }
    }

}
