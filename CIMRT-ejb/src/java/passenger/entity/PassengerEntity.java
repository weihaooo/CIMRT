/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger.entity;

import businessPartner.entity.PurchaseTransactionEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Yoona
 */
@Entity
@XmlRootElement
@XmlType(name = "passengerEntity", propOrder = {
    "username",
    "firstName",
    "lastName",
    "nric",
    "address",
    "email",
    "phoneNumber",
    "password",
    "secretQuestion",
    "secretAnswer",
    "dob",
    "accountLock",
    "wrongPasswordCount",
    "verified",
    "pwChange",
    "card",
    "gender",
    "point"
})
public class PassengerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private String nric;
    private String address;
    private String email;
    private int phoneNumber;
    private String password;
    private String secretQuestion;
    private String secretAnswer;
    @Temporal(TemporalType.DATE) 
    private Date dob;
    private boolean accountLock;
    private int wrongPasswordCount;
    private boolean verified;
    private boolean pwChange;
    @OneToMany(cascade={CascadeType.ALL},mappedBy="passenger")
    private Collection<CardEntity> cards = new ArrayList<CardEntity>();
    @OneToMany(cascade={CascadeType.ALL},mappedBy="passenger")
    private Collection<PointTransactionEntity> ptTransactions = new ArrayList<PointTransactionEntity>();
    private String gender;
    private int point;
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="passenger")
    private List<PurchaseTransactionEntity> purchaseTransactions = new ArrayList<PurchaseTransactionEntity>();
    
    public PassengerEntity(){
        
    }

    public PassengerEntity(String username, String firstName, String lastName, String nric, String address, String email, int phoneNumber, String password, String secretQuestion, String secretAnswer, Date dob,  boolean verified, String gender, int point) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nric = nric;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
        this.dob = dob;
        this.accountLock = false;
        this.wrongPasswordCount = 0;
        this.verified = verified;
        this.pwChange = false;
        this.gender = gender;
        this.point = point;
    }

    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public Collection<CardEntity> getCards() {
        return cards;
    }

    public void setCards(Collection<CardEntity> cards) {
        this.cards = cards;
    }

 

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Collection<PointTransactionEntity> getPtTransactions() {
        return ptTransactions;
    }

    public void setPtTransactions(Collection<PointTransactionEntity> ptTransactions) {
        this.ptTransactions = ptTransactions;
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
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PassengerEntity)) {
            return false;
        }
        PassengerEntity other = (PassengerEntity) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "passenger.entity.PassengerEntity[ id=" + username + " ]";
    }
    
}
