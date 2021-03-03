/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra;

import commoninfra.sessionbean.DataSessionBeanLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import passenger.entity.PassengerEntity;
import passenger.sessionbean.AcctSessionBeanLocal;

/**
 *
 * @author Yoona
 */
@Named(value = "accountManagedBean")
@SessionScoped
public class AcctManagedBean implements Serializable {

    /**
     * Creates a new instance of LoginManagedBean
     */
    private String username;
    private String firstName;
    private String lastName;
    private String nric;
    private Date dob;
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
    private int phoneNumber;
    private String address;
    private ArrayList<String> questionList;
    private String gender;

    @EJB
    private AcctSessionBeanLocal acctSessionBeanLocal;
    @EJB
    private DataSessionBeanLocal dataSessionBeanLocal;

    public AcctManagedBean() {
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

        int result = acctSessionBeanLocal.doLogin(username, password);

        //5 - successful login
        //>99999 - initial login
        //<5 - tries left
        //0 - account locked
        //6 - no such user
        //7 - change password
        if (result == 5) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);
            ableCaptcha = false;
            updateUserInfo(username);
            return "index";
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
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("partnerId", username);
            updateUserInfo(username);
            return "verify";
        } else if (result == 7) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome! Please change your password.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("partnerId", username);
            updateUserInfo(username);
            return "changePW";
        }

        return "index";
    }

    public String doLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout Successfully!", ""));

        return "index";
    }

    public String retrieveUsername() {
        boolean success = acctSessionBeanLocal.retrieveUsername(email, secretQuestion, secretAnswer);
        if (success) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You should receive an email at your registered email address soon!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid email and/or Secret Question and/or Secret Answer!", "Also, make sure you have a stable connection!"));
            return "forgetUserId";
        }
        return "index";
    }

    public String resetPassword() {
        boolean success = acctSessionBeanLocal.resetPassword(username, secretQuestion, secretAnswer);
        if (success) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You should receive an email at your registered email address soon!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid Username and/or Secret Question and/or Secret Answer!", "Also, make sure you have a stable connection!"));
            return "forgetPassword";
        }
        return "index";
    }

    public void checkCaptcha() {
        boolean able = acctSessionBeanLocal.checkCaptcha(username);

        if (able) {
            ableCaptcha = true;

        } else {
            ableCaptcha = false;
        }
    }

    public String authenticateOTP() {

        if (code == otp) {
            boolean success = acctSessionBeanLocal.verifyAccount(username);
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Verified!", ""));
                return "index";
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

        boolean success = acctSessionBeanLocal.createAccount(username, password, firstName, lastName, nric, phoneNumber, email, address, dob, secretQuestion, secretAnswer, gender);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have registered successfully!", ""));
            firstName = "";
            lastName = "";
            email = "";
            password = "";
            username="";
            secretQuestion="";
            secretAnswer="";
            phoneNumber = 0;
            address = "";
            dob = null;
            nric = "";
            gender = "";
            return "index";

        } else {

            FacesContext.getCurrentInstance().addMessage("message1", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to register! Email/Username/NRIC has been used", "Please try again."));
            return "register";
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

        boolean result = acctSessionBeanLocal.changePassword(username, password, newPW, newPW2);

        if (result) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed Successfully!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current Password is wrong!", ""));
            return "changePW";
        }

        return "index";
    }

    public String editProfile() {

        boolean success = acctSessionBeanLocal.editProfile(username, firstName, lastName, phoneNumber, email, address);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated!", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Profile update has failed!", ""));
        }
        return "myProfile?faces-redirect=true";
    }

    private void updateUserInfo(String username) {
        PassengerEntity passenger = acctSessionBeanLocal.searchPassenger(username);
        firstName = passenger.getFirstName();
        lastName = passenger.getLastName();
        email = passenger.getEmail();
        phoneNumber = passenger.getPhoneNumber();
        address = passenger.getAddress();
        dob = passenger.getDob();
        nric = passenger.getNric();
        gender = passenger.getGender();
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
