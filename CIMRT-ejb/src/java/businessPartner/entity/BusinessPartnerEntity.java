/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessPartner.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import passenger.entity.PointTransactionEntity;

/**
 *
 * @author Yoona
 */
@Entity
public class BusinessPartnerEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String partnerId;
    private int phoneNumber;
    private String emailAddress;
    private String password;
    private String company;
    private String address;
    private boolean accountLock;
    private int wrongPasswordCount;
    private String secretQuestion;
    private String secretAnswer;
    private boolean verified;
    private boolean pwChange;
    private boolean subscription;
    @Column(length = 10000)
    private String companyProfile;
    private int faxNumber;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="businessPartner")
    private List<NotificationEntity> notifications = new ArrayList<NotificationEntity>();
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="bPartner")
    private List<PurchaseTransactionEntity> purchaseTransactions = new ArrayList<PurchaseTransactionEntity>();
    
    public BusinessPartnerEntity(){
        
    }
    public BusinessPartnerEntity(String partnerId, int phoneNumber, String emailAddress, String password, String company, String address, String secretQuestion, String secretAnswer, boolean verified, boolean subscription, String companyProfile, int faxNumber){
        this.partnerId = partnerId;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
        this.company = company;
        this.address = address;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
        this.wrongPasswordCount = 0;
        this.accountLock = false;
        this.pwChange = false;
        this.verified = verified;
        this.subscription = subscription;
        this.companyProfile = companyProfile;
        this.faxNumber = faxNumber;
    }
    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getAccountLock() {
        return accountLock;
    }

    public void setAccountLock(boolean accountLock) {
        this.accountLock = accountLock;
    }

    public int getWrongPasswordCount() {
        return wrongPasswordCount;
    }

    public void setWrongPasswordCount(int wrongPasswordCount) {
        this.wrongPasswordCount = wrongPasswordCount;
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

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean getPwChange() {
        return pwChange;
    }

    public void setPwChange(boolean pwChange) {
        this.pwChange = pwChange;
    }

    public List<NotificationEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
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

    public List<PurchaseTransactionEntity> getPurchaseTransactions() {
        return purchaseTransactions;
    }

    public void setPurchaseTransactions(List<PurchaseTransactionEntity> purchaseTransactions) {
        this.purchaseTransactions = purchaseTransactions;
    }

    
     
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BusinessPartnerEntity)) {
            return false;
        }
        BusinessPartnerEntity other = (BusinessPartnerEntity) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "businessPartner.entity.BusinessPartnerEntity[ id=" + partnerId + " ]";
    }
    
}
