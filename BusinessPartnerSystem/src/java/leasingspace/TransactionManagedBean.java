/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leasingspace;

import businessPartner.sessionbean.TransactionSessionBeanLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Yoona
 */
@Named(value = "transactionManagedBean")
@SessionScoped
public class TransactionManagedBean implements Serializable {

    /**
     * Creates a new instance of transactionManagedBean
     */
    @EJB
    TransactionSessionBeanLocal transactionSessionBeanLocal;
    
    private String partnerId;
    private String type;
    private String nric;
    private String cardId;
    private double amount;

    public TransactionManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("partnerId") != null) {
            partnerId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("partnerId").toString();
        }
    }
    
    public String submitTransaction(){
        if(transactionSessionBeanLocal.submitTransaction(partnerId,type,nric,cardId,amount)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction has successfully created!", ""));
            
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Please try again.", ""));
        
    }
        type = "";
        nric = "";
        cardId = "";
        amount = 0;
        return "transaction";
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
