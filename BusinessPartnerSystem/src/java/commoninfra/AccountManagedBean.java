/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra;

import businessPartner.entity.BusinessPartnerEntity;
import commoninfra.sessionbean.DataSessionBeanLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import businessPartner.sessionbean.ProfileSessionBeanLocal;
import java.util.ArrayList;

/**
 *
 * @author Yoona
 */
@Named(value = "accountManagedBean")
@SessionScoped
public class AccountManagedBean implements Serializable {

    /**
     * Creates a new instance of LoginManagedBean
     */
    private String partnerId;
    private String password;
    private String password2;
    private String newPW;
    private String newPW2;
    private boolean ableCaptcha;
    private int code;
    private int otp;
    private String secretQuestion;
    private String secretAnswer;
    private String email;
    private String company;
    private int phoneNumber;
    private String address;
    private ArrayList<String> questionList;
    private boolean subscription = true;
    private boolean value1;
    private String companyProfile;
    private int faxNumber;

    @EJB
    private ProfileSessionBeanLocal profileSessionBeanLocal;
    @EJB
    private DataSessionBeanLocal dataSessionBeanLocal;

    public AccountManagedBean() {
    }

    @PostConstruct
    public void init() {

        dataSessionBeanLocal.createRosterBasis();
        questionList = new ArrayList<String>();
        questionList.add("When was your first kissed?");
        questionList.add("What is your favourite food?");
        questionList.add("Who is your childhood sports hero?");
        questionList.add("When is the last time you cry?");
        questionList.add("What was the name of your first job?");
        questionList.add("What is your company's motto?");
    }

    public String doLogin() {

        int result = profileSessionBeanLocal.doLogin(partnerId, password);

        //5 - successful login
        //>99999 - initial login
        //<5 - tries left
        //0 - account locked
        //6 - no such user
        //7 - change password
        if (result == 5) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("partnerId", partnerId);
            ableCaptcha = false;
            updatePartnerInfo(partnerId);
            return "home";
        } else if (result == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Account is locked!", "Please reset your password or contact an admin"
                    ));
            return "index";
        } else if (result < 5) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Username and Password You have " + result + " tries left!", ""));
            checkCaptcha();
            return "index";
        } else if (result == 6) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no such user!", ""));
            return "index";
        } else if (result > 99999) {
            code = result;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome! Please enter the OTP to verify your account.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("partnerId", partnerId);
            updatePartnerInfo(partnerId);
            return "verify";
        } else if (result == 7) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome! Please change your password.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("partnerId", partnerId);
            updatePartnerInfo(partnerId);
            return "changePW";
        }

        return "index";
    }

    public String doLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout Successfully! Thank you for using CIMRT Business Partner System!", ""));

        return "index";
    }

    public String retrievePartnerId() {
        boolean success = profileSessionBeanLocal.retrievePartnerId(email, secretQuestion, secretAnswer);
        if (success) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You should receive an email at your registered email address soon!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid email and/or Secret Question and/or Secret Answer!", "Also, make sure you have a stable connection!"));
            return "forgetPartnerId";
        }
        return "index";
    }

    public String resetPassword() {
        boolean success = profileSessionBeanLocal.resetPassword(partnerId, secretQuestion, secretAnswer);
        if (success) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You should receive an email at your registered email address soon!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid Partner ID and/or Secret Question and/or Secret Answer!", "Also, make sure you have a stable connection!"));
            return "forgetPassword";
        }
        return "index";
    }

    public void checkCaptcha() {
        boolean able = profileSessionBeanLocal.checkCaptcha(partnerId);

        if (able) {
            ableCaptcha = true;

        } else {
            ableCaptcha = false;
        }
    }

    public String authenticateOTP() {

        if (code == otp) {
            boolean success = profileSessionBeanLocal.verifyAccount(partnerId);
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Verified!", ""));
                return "home";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to verify!", "Please try again."));
                return "index";
            }
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to verify!", "Please try again."));
        return "index";
    }

    public String register() {

        if (!password.equals(password2)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords does not match!", "Please try again."));
            return "register";
        }

        String id = profileSessionBeanLocal.createAccount(password, company, phoneNumber, email, address, secretQuestion, secretAnswer, subscription, companyProfile, faxNumber);

        if (id.equals("")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to register! Email or Company may already exist!", "Please try again."));
            return "register";
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have registered successfully! Your Partner ID is " + id, ""));

            email = "";
            company = "";
            phoneNumber = 0;
            address = "";
            faxNumber = 0;
            companyProfile = "";
            password = "";
            password2 = "";
            secretQuestion = "";
            secretAnswer = "";
            return "index";
        }
    }

    public String changePassword() {
        if (!newPW.equals(newPW2)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Passwords does not match!", ""));
            return "changePW";
        } else if (newPW.equals(password)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Password must not be the same as current password", ""));
            return "changePW";
        }

        boolean result = profileSessionBeanLocal.changePassword(partnerId, password, newPW, newPW2);

        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed Successfully!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current Password is wrong!", ""));
            return "changePW";
        }

        return "home";
    }

    public String editProfile() {

        boolean success = profileSessionBeanLocal.editProfile(partnerId, company, phoneNumber, email, address, faxNumber, companyProfile);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated!", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Profile update has failed!", ""));
        }
        return "profile";
    }

    public String editSubscription() {

        boolean success = profileSessionBeanLocal.editSubscription(partnerId, value1);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update successfully!", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update!", ""));
        }
        updatePartnerInfo(partnerId);
        return "subscribeAlert";
    }

    private void updatePartnerInfo(String partnerId) {
        BusinessPartnerEntity partner = profileSessionBeanLocal.searchPartner(partnerId);
        email = partner.getEmailAddress();
        company = partner.getCompany();
        phoneNumber = partner.getPhoneNumber();
        address = partner.getAddress();
        faxNumber = partner.getFaxNumber();
        companyProfile = partner.getCompanyProfile();
        subscription = partner.isSubscription();
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAbleCaptcha() {
        return ableCaptcha;
    }

    public void setAbleCaptcha(boolean ableCaptcha) {
        this.ableCaptcha = ableCaptcha;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<String> questionList) {
        this.questionList = questionList;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getNewPW() {
        return newPW;
    }

    public void setNewPW(String newPW) {
        this.newPW = newPW;
    }

    public String getNewPW2() {
        return newPW2;
    }

    public void setNewPW2(String newPW2) {
        this.newPW2 = newPW2;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public boolean isValue1() {
        return value1;
    }

    public void setValue1(boolean value1) {
        this.value1 = value1;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public int getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(int faxNumber) {
        this.faxNumber = faxNumber;
    }

    public void addMessage() {
        String summary = value1 ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

}
