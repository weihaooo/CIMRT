/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import routefare.entity.NodeEntity;

/**
 *
 * @author damin
 */
@Named(value = "networkMap")
@SessionScoped
public class NetworkMap implements Serializable {

    private MapModel simpleModel;
    private List<NodeEntity> nodes = new ArrayList<NodeEntity>();
    private Marker marker;
    @EJB
    InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();

        nodes = infraAssetSessionBeanLocal.retrieveNodes();

        if (!nodes.isEmpty()) {
            for (int i = 0; i < nodes.size(); i++) {

                LatLng coord = new LatLng(nodes.get(i).getLatitude(), nodes.get(i).getLongitude());

                simpleModel.addOverlay(new Marker(coord, nodes.get(i).getCode() + " - " + nodes.get(i).getInfraName()));
            }
        }
        /*  //EWL
        LatLng coord1 = new LatLng(-10.510630, 105.537675);
        LatLng coord2 = new LatLng(-10.506265, 105.544373);
        LatLng coord3 = new LatLng(-10.501770, 105.549281);
        LatLng coord4 = new LatLng(-10.494829, 105.555870);
        LatLng coord5 = new LatLng(-10.480984, 105.560081);
        LatLng coord6 = new LatLng(-10.480368, 105.567342);
        LatLng coord7 = new LatLng(-10.481234, 105.574684);
        LatLng coord8 = new LatLng(-10.483798, 105.581156);
        LatLng coord9 = new LatLng(-10.485239, 105.587123);
        LatLng coord10 = new LatLng(-10.487688, 105.595998);
        LatLng coord11 = new LatLng(-10.489909, 105.604889);
        LatLng coord12 = new LatLng(-10.492858, 105.613686);
        LatLng coord13 = new LatLng(-10.494809, 105.624880);
        LatLng coord14 = new LatLng(-10.496983, 105.634964);
        LatLng coord15 = new LatLng(-10.497327, 105.645338);
        LatLng coord16 = new LatLng(-10.488727, 105.658504);
        LatLng coord17 = new LatLng(-10.479492, 105.665735);
        LatLng coord18 = new LatLng(-10.476227, 105.673893);
        LatLng coord19 = new LatLng(-10.473285, 105.680725);
        LatLng coord20 = new LatLng(-10.471495, 105.688029);
        LatLng coord21 = new LatLng(-10.470125, 105.698386);
        LatLng coord22 = new LatLng(-10.469130, 105.705022);
        
        //NSL
        LatLng coord23 = new LatLng(-10.435611, 105.672425);
        LatLng coord24 = new LatLng(-10.441225, 105.671327);
        LatLng coord25 = new LatLng(-10.447765, 105.670084);
        LatLng coord26 = new LatLng(-10.451558, 105.670567);
        LatLng coord27 = new LatLng(-10.458634, 105.667489);
        LatLng coord28 = new LatLng(-10.463436, 105.665000);
        LatLng coord29 = new LatLng(-10.479492, 105.665735);
        LatLng coord30 = new LatLng(-10.488727, 105.658504);
        LatLng coord31 = new LatLng(-10.488727, 105.658504);
        LatLng coord32 = new LatLng(-10.501633, 105.655809);
        LatLng coord33 = new LatLng(-10.510850, 105.654817);
        LatLng coord34 = new LatLng(-10.520410, 105.652593);
        LatLng coord35 = new LatLng(-10.529137, 105.653145);
        LatLng coord36 = new LatLng(-10.537637, 105.653172);
        LatLng coord37 = new LatLng(-10.547530, 105.654099);
        
        //depot
        LatLng coord38 = new LatLng(-10.431017, 105.673446);
        LatLng coord39 = new LatLng(-10.565124, 105.646623);
        LatLng coord40 = new LatLng(-10.462289, 105.704928);
        LatLng coord41 = new LatLng(-10.512096, 105.535476);
        
         
        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, "Konyaalti"));
        simpleModel.addOverlay(new Marker(coord2, "Ataturk Parki"));
        simpleModel.addOverlay(new Marker(coord3, "Karaalioglu Parki"));
        simpleModel.addOverlay(new Marker(coord4, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord5, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord6, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord7, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord8, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord9, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord10, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord11, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord12, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord13, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord14, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord15, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord16, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord17, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord18, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord19, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord20, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord21, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord22, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord23, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord24, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord25, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord26, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord27, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord28, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord29, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord30, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord31, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord32, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord33, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord34, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord35, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord36, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord37, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord38, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord39, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord40, "Kaleici"));
        simpleModel.addOverlay(new Marker(coord41, "Kaleici"));
         */
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();

        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected On Map", "Lat:" + String.valueOf(latlng.getLat()).substring(0, 10) + " Lng:" + String.valueOf(latlng.getLng()).substring(0, 10)));
    }

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        System.out.println(marker.getTitle());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Selected", marker.getTitle()));
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }



}
