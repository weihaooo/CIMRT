/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import routefare.entity.FareAlgoEntity;
import routefare.sessionbean.FarePlanningSessionBeanLocal;

/**
 *
 * @author YuTing
 */
@Named(value = "updateFareManagedBean")
@ApplicationScoped
public class UpdateFareManagedBean implements Serializable {

    @EJB
    private FarePlanningSessionBeanLocal farePlanningSessionBeanLocal;
    private String passenger;
    private String fareType;
    private double baseFare;
    private double incrementRate;
    private ArrayList<String> fareTypes;
    private boolean checkStatus;
    private String currPassenger;
    private String currFare;

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getIncrementRate() {
        return incrementRate;
    }

    public void setIncrementRate(double incrementRate) {
        this.incrementRate = incrementRate;
    }

    public String update() {
        boolean status = farePlanningSessionBeanLocal.updateFareStructure(passenger, fareType, baseFare, incrementRate);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Fare structure is updated successfully",
                            ""));
            this.baseFare = 0;
            this.fareType = null;
            this.incrementRate = 0;
            this.passenger = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the fare structure!"));

        }
        return "viewFareStructure";
    }

    private List<FareAlgoEntity> fares;
    private List<FareAlgoEntity> faresConcession;

    public List<FareAlgoEntity> getFares() {
        this.fares = farePlanningSessionBeanLocal.getFareAlgo();
        return fares;
    }

    public void setFares(List<FareAlgoEntity> fares) {
        this.fares = fares;
    }

    public List<FareAlgoEntity> getFaresConcession() {
        this.faresConcession = farePlanningSessionBeanLocal.getFareAlgoConcession();
        return faresConcession;
    }

    public void setFaresConcession(List<FareAlgoEntity> faresConcession) {
        this.faresConcession = faresConcession;
    }

    public ArrayList<String> getFareTypes() {
        return fareTypes;
    }

    public void setFareTypes(ArrayList<String> fareTypes) {
        this.fareTypes = fareTypes;
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public void onFareChange() {
        if (passenger != null) {
            fareTypes = farePlanningSessionBeanLocal.getFareType(passenger);
            currPassenger = this.passenger;
        }
    }

    public void onFareTypeChange() {
        if (fareType.equals("Concession")) {
            checkStatus = false;
        } else {
            checkStatus = true;
        }
        currFare = this.fareType;
        this.baseFare = farePlanningSessionBeanLocal.getBaseFee(currPassenger, currFare);
        this.incrementRate = farePlanningSessionBeanLocal.getIncrementalFee(currPassenger, currFare);
    }

    public String goEdit() {
        return "updateFare";
    }
}
