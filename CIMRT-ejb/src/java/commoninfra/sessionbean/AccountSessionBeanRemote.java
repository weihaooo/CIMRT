package commoninfra.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import commoninfra.entity.StaffEntity;
import java.util.ArrayList;
import javax.ejb.Remote;

@Remote
public interface AccountSessionBeanRemote {
    public int doLogin(String username, String password);
    public boolean resetPassword(String staffId, String nric);
    public boolean retrieveStaffId(String nric);
    public ArrayList<String> viewProfile(String staffId);
    public boolean editProfile(ArrayList<String> details);
    public boolean changePassword(String staffId, String password, String newPW, String newPW2);
    public boolean checkCaptcha(String staffId);
    public StaffEntity searchStaff(String staffId);

}
