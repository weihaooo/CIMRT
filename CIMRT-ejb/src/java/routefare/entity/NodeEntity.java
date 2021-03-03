/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.entity;

import commoninfra.entity.TeamEntity;
import infraasset.entity.InfrastructureEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import manpower.entity.RosterEntity;
import operations.entity.TopUpTransactionEntity;

/**
 *
 * @author zhuming
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class NodeEntity extends InfrastructureEntity implements Serializable {

    
    private String address;
    private String code;
    private String previousStation;
    private String nextStation;
    private double distanceToFirstStation;
    private double latitude;
    private double longitude;
    @ManyToMany(cascade = {CascadeType.PERSIST}, mappedBy = "nodes")
    private Set<RouteEntity> routes = new HashSet<RouteEntity>();
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy = "node")
    private List<TeamEntity> teams = new ArrayList<TeamEntity>();
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy = "node")
    private List<RosterEntity> rosters = new ArrayList<RosterEntity>();

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "node")
    private List<TripStationScheduleEntity> tripStationSchedules = new ArrayList<TripStationScheduleEntity>(); 
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="node")
    private Collection<TopUpTransactionEntity> transactions = new ArrayList<TopUpTransactionEntity>();
            
    public NodeEntity() {
    }

    public NodeEntity(String infraId, String code, String infraName, String address,String previousStation, String nextStation, double distanceToFirstStation, double latitude, double longitude) {
        super(infraId, infraName);
        this.address = address;
        this.code = code;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distanceToFirstStation = distanceToFirstStation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistanceToFirstStation() {
        return distanceToFirstStation;
    }

    public void setDistanceToFirstStation(double distanceToFirstStation) {
        this.distanceToFirstStation = distanceToFirstStation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreviousStation() {
        return previousStation;
    }

    public void setPreviousStation(String previousStation) {
        this.previousStation = previousStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public Set<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<RouteEntity> routes) {
        this.routes = routes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TeamEntity> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamEntity> teams) {
        this.teams = teams;
    }
    
    public void addTeams(TeamEntity team) {
        this.teams.add(team);
    }

    public List<RosterEntity> getRosters() {
        return rosters;
    }

    public void setRosters(List<RosterEntity> rosters) {
        this.rosters = rosters;
    }
    public void addRoster(RosterEntity roster){
        this.rosters.add(roster);
    }

    public List<TripStationScheduleEntity> getTripStationSchedules() {
        return tripStationSchedules;
    }

    public void setTripStationSchedules(List<TripStationScheduleEntity> tripStationSchedules) {
        this.tripStationSchedules = tripStationSchedules;
    }

    public Collection<TopUpTransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<TopUpTransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    
    
}
