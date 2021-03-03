/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra;

import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.sessionbean.DataSessionBeanLocal;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Yoona
 */
@Named(value = "loginManagedBean")
@SessionScoped
public class LoginManagedBean implements Serializable {

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    @EJB
    private DataSessionBeanLocal dataSessionBeanLocal;
    private String staffId;
    private String password;
    private String nric;
    private boolean ableCaptcha;
    private Long otp;
    private int code;
    private String firstName;
    private String lastName;
    private String role;
    private String nodeCode;
    private Long team;

    @PostConstruct
    public void init() {

        dataSessionBeanLocal.createRosterBasis();
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public boolean isAbleCaptcha() {
        return ableCaptcha;
    }

    public void setAbleCaptcha(boolean ableCaptcha) {
        this.ableCaptcha = ableCaptcha;
    }

    public Long getOtp() {
        return otp;
    }

    public void setOtp(Long otp) {
        this.otp = otp;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public Long getTeam() {
        return team;
    }

    public void setTeam(Long team) {
        this.team = team;
    }

    public LoginManagedBean() {
    }

    public String doLogin() {

        int result = accountSessionBeanLocal.doLogin(staffId, password);

        //5 - successful login
        //>99999 - initial login
        //<5 - tries left
        //0 - account locked
        //6 - no such user
        if (result == 5) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("staffId", staffId);
            ableCaptcha = false;
            updateStaffInfo(staffId);
            return "home";
        } else if (result == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Account is locked!", "Please contact admin to reset account"
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome! Please enter the OTP.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("staffId", staffId);
            updateStaffInfo(staffId);
            return "enter2FA";
        }

        return "index";
    }

    public String doLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout Successfully!", ""));

        return "index";
    }

    public String retrieveStaffId() {
        boolean success = accountSessionBeanLocal.retrieveStaffId(nric);
        if (success) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You should receive an email at your registered email address soon!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid NRIC!", "Also, make sure you have a stable connection!"));
            return "forgetStaffId";
        }
        return "index";
    }

    public String resetPassword() {
        boolean success = accountSessionBeanLocal.resetPassword(staffId, nric);
        if (success) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You should receive an email at your registered email address soon!", ""));
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid Staff ID and/or NRIC!", "Also, make sure you have a stable connection!"));
            return "forgetPassword";
        }
        return "index";
    }

    public void checkCaptcha() {
        boolean able = accountSessionBeanLocal.checkCaptcha(staffId);

        if (able) {
            ableCaptcha = true;

        } else {
            ableCaptcha = false;
        }
    }

    public String authenticateOTP() {

        //  boolean success = accountSessionBeanLocal.authenticateOTP(otp);
        // System.out.println(success);
        if (code == otp) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please change your password!", ""));
            return "changePassword";
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed Login!", "Please try again."));
        return "index";
    }

    private void updateStaffInfo(String staffId) {
        StaffEntity staff = accountSessionBeanLocal.searchStaff(staffId);
        this.firstName = staff.getFirstName();
        this.lastName = staff.getLastName();
        this.role = staff.getStaffRole().getStaffRole();
        if (staff instanceof StationStaffEntity) {
            StationStaffEntity stationStaff = (StationStaffEntity) staff;
            this.nodeCode = stationStaff.getStationTeam().getNode().getCode();

            this.team = stationStaff.getStationTeam().getTeamId();

        } else if (staff instanceof DepotStaffEntity) {
            DepotStaffEntity depotStaff = (DepotStaffEntity) staff;

            this.nodeCode = depotStaff.getDepotTeam().getNode().getCode();
            this.team = depotStaff.getDepotTeam().getTeamId();
        }
    }
}
