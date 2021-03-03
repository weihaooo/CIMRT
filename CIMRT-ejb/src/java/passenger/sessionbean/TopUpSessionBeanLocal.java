/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Yuting
 */
@Local
public interface TopUpSessionBeanLocal {

    public int doLogin(String staffId, String password);
    public Date getConcessionEndDate(String cardId);
    public void createTopUpTransaction(String type, double amount, Date transDate, String staffId, Date start, String cardId);
    public double getAmount(String cardId);
    public boolean isExpired(String cardId);
    public String getRefundAmt(String cardId);
    public void addValueTransaction(String type, double amount, Date transDate, String staffId, String cardId);
    public void refund(double amount, Date transDate, String staffId, String cardId);
    
}
