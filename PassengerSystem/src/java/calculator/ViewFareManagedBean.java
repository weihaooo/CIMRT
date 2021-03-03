/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import routefare.entity.FareAlgoEntity;
import routefare.sessionbean.FarePlanningSessionBeanLocal;

/**
 *
 * @author Yuting
 */
@Named(value = "viewFareManagedBean")
@SessionScoped
public class ViewFareManagedBean implements Serializable{
    
 @EJB
    private FarePlanningSessionBeanLocal farePlanningSessionBeanLocal;
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

}

