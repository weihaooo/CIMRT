/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.sessionbean.ws;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import passenger.sessionbean.FareTrainsactionSessionBeanLocal;
import passenger.sessionbean.TopUpSessionBeanLocal;

/**
 *
 * @author yuting
 */
@WebService(serviceName = "LoginWebService")
@Stateless()
public class LoginWebService {

    @EJB(name = "fareTrainsactionSessionBeanLocal")
    private FareTrainsactionSessionBeanLocal fareTrainsactionSessionBeanLocal;

    @EJB(name = "topUpSessionBeanLocal")
    private TopUpSessionBeanLocal topUpSessionBeanLocal;

    /**
     * This is a sample web service operation
     * @param username
     * @param password
     * @return 
     */
    @WebMethod(operationName = "login")
    public int login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        return topUpSessionBeanLocal.doLogin(username, password);
    }
    
    @WebMethod(operationName = "getConcessionEnd")
    public Date getConcessionEnd(@WebParam(name = "cardId") String cardId) {
        return topUpSessionBeanLocal.getConcessionEndDate(cardId);
    }
    
    @WebMethod(operationName = "getAmount")
    public double getAmount(@WebParam(name = "cardId") String cardId) {
        return topUpSessionBeanLocal.getAmount(cardId);
    }
    @WebMethod(operationName = "getRefundAmt")
    public String getRefundAmt(@WebParam(name = "cardId") String cardId) {
        return topUpSessionBeanLocal.getRefundAmt(cardId);
    }
    @WebMethod(operationName = "isExpired")
    public boolean isExpired(@WebParam(name = "cardId") String cardId) {
        return topUpSessionBeanLocal.isExpired(cardId);
    }
    
    @WebMethod(operationName = "createTransaction")
    public void createTransaction(@WebParam(name = "type") String type, @WebParam(name = "amount") double amount, @WebParam(name = "transDate") Date transDate, @WebParam(name = "staffId") String staffId, @WebParam(name = "start") Date start, @WebParam(name = "cardId") String cardId) {
        //String type, double amount, Date transDate, String staffId, Date start, String cardId
        topUpSessionBeanLocal.createTopUpTransaction(type, amount, transDate, staffId, start, cardId);
    }
    
    @WebMethod(operationName = "addValueTransaction")
    public void addValueTransaction(@WebParam(name = "type") String type, @WebParam(name = "amount") double amount, @WebParam(name = "transDate") Date transDate, @WebParam(name = "staffId") String staffId, @WebParam(name = "cardId") String cardId) {
        //String type, double amount, Date transDate, String staffId, Date start, String cardId
        topUpSessionBeanLocal.addValueTransaction(type, amount, transDate, staffId, cardId);
    }
    
    @WebMethod(operationName = "refund")
    public void refund(@WebParam(name = "amount") double amount, @WebParam(name = "transDate") Date transDate, @WebParam(name = "staffId") String staffId, @WebParam(name = "cardId") String cardId) {
        //String type, double amount, Date transDate, String staffId, Date start, String cardId
        topUpSessionBeanLocal.refund(amount, transDate, staffId, cardId);
    }
    
    @WebMethod(operationName = "getStations")
    public ArrayList<String> getStations() {
        //String type, double amount, Date transDate, String staffId, Date start, String cardId
        return fareTrainsactionSessionBeanLocal.getStations();
    }
    
    @WebMethod(operationName = "createFareTransaction")
    public String createFareTransaction(@WebParam(name = "cardId") String cardId, @WebParam(name = "startStation")  String station) {
        //String type, double amount, Date transDate, String staffId, Date start, String cardId
        return fareTrainsactionSessionBeanLocal.createFareTransaction(cardId, station);
    }
}
