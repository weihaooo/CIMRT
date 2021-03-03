/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passenger;

import businessPartner.entity.PurchaseTransactionEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import passenger.entity.CardEntity;
import passenger.sessionbean.CardManagementSessionBeanLocal;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.faces.context.ExternalContext;
import operations.entity.TopUpTransactionEntity;
import passenger.entity.PointTransactionEntity;
import routefare.entity.FareTransactionEntity;

/**
 *
 * @author Yoona
 */
@Named(value = "cardManagedBean")
@SessionScoped
public class CardManagedBean implements Serializable {

    @EJB
    CardManagementSessionBeanLocal cardManagementSessionBeanLocal;

    private List<CardEntity> cards;
    private List<CardEntity> filteredCards;
    private String username;
    private CardEntity selectedCard;
    private int availablePoints;
    private int redeemPoints;
    private String type;
    private double amount;
    private Date startDate;
    private Date endDate;
    private Date minDate;
    private boolean extend;
    private String cardId;

    private DecimalFormat df = new DecimalFormat(".##");

    private final String clientId = "AcdGU6tZ0b-z77WZlvPW2xypwq72h_g5YCMx4XUOBTKez-oieXm40BTWw5PYc-x0rXug9xXcEAj-VXDl";
    private final String clientSecret = "EPriX9FOb4OZylUulZ-oLknSTGnOHJ7kXAwD4jPpy9i9AtWiWiFYSpWBK5qyn8RdT4Q3qxOZqLZnKg8z";

    private final APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
    private PaymentExecution paymentExecution = new PaymentExecution();
    private Payment payment = new Payment();

    private List<FareTransactionEntity> fares;
    private List<TopUpTransactionEntity> topups;
    private List<PurchaseTransactionEntity> purchases;
    private List<PointTransactionEntity> points;

    private List<FareTransactionEntity> filteredFareTrainsactions;
    private List<TopUpTransactionEntity> filteredTopUpTransactions;
    private List<PurchaseTransactionEntity> filteredPurchaseTransactions;
    private List<PointTransactionEntity> filteredPointsTransactions;

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            //Get user cards
            retrieveUserCards();
            retrievePoints();
        }

    }

    public void redirectPaypal() throws IOException {
        // Replace with your application client ID and secret
        // Set payer details
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

// Set redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://is41031718es01.southeastasia.cloudapp.azure.com:8080/PassengerSystem/myCards.xhtml;jsessionid="
                + FacesContext.getCurrentInstance().getExternalContext().getSessionId(true));
        redirectUrls.setReturnUrl("http://is41031718es01.southeastasia.cloudapp.azure.com:8080/PassengerSystem/processPayment.xhtml;jsessionid="
                + FacesContext.getCurrentInstance().getExternalContext().getSessionId(true));
        // redirectUrls.setCancelUrl("http://localhost:35436/PassengerSystem/myCards.xhtml");
        //redirectUrls.setReturnUrl("http://localhost:35436/PassengerSystem/processPayment.xhtml");

// Set payment details
        Details details = new Details();
        details.setSubtotal(amount + "");

// Payment amount
        Amount amount1 = new Amount();
        amount1.setCurrency("SGD");
// Total must be equal to sum of shipping, tax and subtotal.
        amount1.setTotal(amount + "");
        amount1.setDetails(details);

// Transaction information
        Transaction transaction = new Transaction();
        transaction.setAmount(amount1);
        transaction.setDescription("This payment is for your Topup transaction of" + " card " + selectedCard.getCardId() + " for an amount of $" + df.format(amount) + " (" + type + ")");

// Add transaction to a list
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

// Add payment details
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(transactions);

        // Create payment
        try {
            Payment createdPayment = payment.create(apiContext);

            Iterator links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = (Links) links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    // REDIRECT USER TO link.getHref()
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    externalContext.redirect(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        /*
         */
    }

    public void verifyPayment() {
        payment.setId(FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("paymentId"));

        paymentExecution.setPayerId(FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("PayerID"));
    }

    public String confirmTopup() {
        try {
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            //System.out.println(createdPayment);
            //System.out.println(createdPayment.getTransactions().get(0).getRelatedResources().get(0).getSale().getState());
            if (createdPayment.getState().equals("approved")) {
                topupCard();
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return "myCards";
    }

    public void retrieveUserCards() {
        System.out.println("Got come here?");
        cards = cardManagementSessionBeanLocal.retrieveUserCards(username);
    }

    public String goTopup(int index) {
        selectedCard = cards.get(index);
        if (selectedCard.getConcessionStart() != null && (getZeroTimeDate(selectedCard.getConcessionEnd()).after(getZeroTimeDate(new Date())) || getZeroTimeDate(selectedCard.getConcessionEnd()).equals(getZeroTimeDate(new Date())))) {
            startDate = selectedCard.getConcessionEnd();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
            extend = true;
            calculateEndDate();
        } else {
            minDate = new Date();
            extend = false;
        }
        return "topupCard?faces-redirect=true";
    }

    public static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date = calendar.getTime();

        return date;
    }

    public String goRedeem(int index) {
        selectedCard = cards.get(index);
        return "redeem";
    }

    public String goTransactionDetails(int index) {
        retrieveUserCards();
        retrievePoints();
        selectedCard = cards.get(index);
        retrievePurchaseTransactions();
        retrievePointsTransactions();
        retrieveTopupTransactions();
        retrieveFareTransactions();
        return "viewTransactions";
    }

    public void retrievePurchaseTransactions() {
        purchases = cardManagementSessionBeanLocal.retrievePurchaseTransactions(selectedCard);
    }

    public void retrievePointsTransactions() {
        points = cardManagementSessionBeanLocal.retrievePointsTransactions(selectedCard);

    }

    public void retrieveTopupTransactions() {
        topups = cardManagementSessionBeanLocal.retrieveTopupTransactions(selectedCard);

    }

    public void retrieveFareTransactions() {
        fares = cardManagementSessionBeanLocal.retrieveFareTransactions(selectedCard);

    }

    public void retrievePoints() {
        int points = cardManagementSessionBeanLocal.retrievePoints(username);
        if (points != -1) {
            availablePoints = points;
        }
    }

    public void defineAmount() {
        if (type.equals("Variable Amount")) {
            amount = 0;
        } else if (type.equals("Concession")) {
            amount = cardManagementSessionBeanLocal.retriveConcessionAmount(selectedCard);
        }
    }

    public void calculateEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, 30);
        endDate = calendar.getTime();
    }

    public void topupCard() {

        if (cardManagementSessionBeanLocal.topupCard(selectedCard, amount, type, startDate, endDate)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully top up!", ""));
            selectedCard = null;
            amount = 0;
            type = "";
            startDate = null;
            endDate = null;
            retrieveUserCards();
            retrievePoints();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Please try again!", ""));
            selectedCard = null;
            amount = 0;
            type = "";
            startDate = null;
            endDate = null;
        }
    }

    public String redeemPointsForCard() {
        if (cardManagementSessionBeanLocal.redeemPoints(selectedCard, redeemPoints)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully redeemed your points!", ""));
            retrieveUserCards();
            retrievePoints();
            redeemPoints = 0;
            return "myCards";
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Please try again!", ""));
            redeemPoints = 0;
            return "redeem";
        }
    }

    public String addCard() {
        if (cardManagementSessionBeanLocal.addCard(cardId, username)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully added Card " + cardId + "!", ""));
            cardId = "";
            retrieveUserCards();
            retrievePoints();

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! Add card failed!", ""));
            cardId = "";
            retrieveUserCards();
            retrievePoints();
        }
        return "myCards";
    }

    //Getter, Setter & Constructors
    public CardManagedBean() {
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CardEntity getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(CardEntity selectedCard) {
        this.selectedCard = selectedCard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public int getRedeemPoints() {
        return redeemPoints;
    }

    public void setRedeemPoints(int redeemPoints) {
        this.redeemPoints = redeemPoints;
    }

    public boolean isExtend() {
        return extend;
    }

    public void setExtend(boolean extend) {
        this.extend = extend;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public List<FareTransactionEntity> getFares() {
        return fares;
    }

    public void setFares(List<FareTransactionEntity> fares) {
        this.fares = fares;
    }

    public List<TopUpTransactionEntity> getTopups() {
        return topups;
    }

    public void setTopups(List<TopUpTransactionEntity> topups) {
        this.topups = topups;
    }

    public List<PurchaseTransactionEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseTransactionEntity> purchases) {
        this.purchases = purchases;
    }

    public List<PointTransactionEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointTransactionEntity> points) {
        this.points = points;
    }

    public List<CardEntity> getFilteredCards() {
        return filteredCards;
    }

    public void setFilteredCards(List<CardEntity> filteredCards) {
        this.filteredCards = filteredCards;
    }

    public List<FareTransactionEntity> getFilteredFareTrainsactions() {
        return filteredFareTrainsactions;
    }

    public void setFilteredFareTrainsactions(List<FareTransactionEntity> filteredFareTrainsactions) {
        this.filteredFareTrainsactions = filteredFareTrainsactions;
    }

    public List<TopUpTransactionEntity> getFilteredTopUpTransactions() {
        return filteredTopUpTransactions;
    }

    public void setFilteredTopUpTransactions(List<TopUpTransactionEntity> filteredTopUpTransactions) {
        this.filteredTopUpTransactions = filteredTopUpTransactions;
    }

    public List<PurchaseTransactionEntity> getFilteredPurchaseTransactions() {
        return filteredPurchaseTransactions;
    }

    public void setFilteredPurchaseTransactions(List<PurchaseTransactionEntity> filteredPurchaseTransactions) {
        this.filteredPurchaseTransactions = filteredPurchaseTransactions;
    }

    public List<PointTransactionEntity> getFilteredPointsTransactions() {
        return filteredPointsTransactions;
    }

    public void setFilteredPointsTransactions(List<PointTransactionEntity> filteredPointsTransactions) {
        this.filteredPointsTransactions = filteredPointsTransactions;
    }

}
