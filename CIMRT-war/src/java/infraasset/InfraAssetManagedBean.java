/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infraasset;

import commoninfra.sessionbean.AccountSessionBeanLocal;
import commoninfra.sessionbean.SystemAdminSessionBeanLocal;
import commoninfra.entity.TeamEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.entity.StationEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.TrainCarEntity;
import infraasset.entity.TrackEntity;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.enterprise.context.SessionScoped;
import routefare.entity.NodeEntity;
import javax.faces.bean.ManagedProperty;
import tenderModule.entity.LeasingContractEntity;
import tenderModule.sessionbean.TenderModuleSessionBeanLocal;

/**
 *
 * @author Zhirong
 */
@Named(value = "infraAssetManagedBean")
@SessionScoped
public class InfraAssetManagedBean implements Serializable {

    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    @EJB
    private SystemAdminSessionBeanLocal systemAdminSessionBeanLocal;

    @EJB
    private TenderModuleSessionBeanLocal tenderSessionBeanLocal;

    private String staffId;
    private String firstName;
    private String lastName;
    private String department;
    private String role;
    private String team;
    private String mcEntitlement;
    private String leaveEntitlement;
    private ArrayList<String> staffDetails;

    @ManagedProperty(value = "#{param.infraId}")
    private String infraId;

    private String assetReqId;
    private String assetRequestType;
    private String assetId;
    private String trainLine;
    private String fixedTrainLine;
    private String location;
    private String type;
    private String assetName;
    private String code;
    private String remark;
    private int qty;
    private int storage;
    private double peakPeriod;
    private double nonpeakPeriod;
    private String unitNumber;
    private double floorArea;
    private String waterProvision;
    private String waterProvision1;
    private double rentalFee;
    private int leaseYear;
    private String leaseDescription;
    private int lifetimeValue; //no. of years
    @Temporal(value = TemporalType.DATE)
    private Date purchaseDate;
    @Temporal(value = TemporalType.DATE)
    private Date expiryDate;
    private String infraName;
    private double distanceToFirstStation;
    private String infraAddress;
    private String nextStation;
    private String previousStation;
    private String rsStatus;
    private String brand;
    private String selectedDepot;
    private String previousPage;
    private int carCode = 0;
    private String carStatus;
    private String storageLoc;
    private String description;
    private String availableCarList;

    private String nodeAssetType;
    private String consumableAssetType;
    private String rollingStockAssetType;
    private List<String> nodeAssetTypeList;
    private List<String> consumableAssetTypeList;
    private List<String> rollingStockAssetTypeList;
    private String assetType;
    private List<String> assetTypeList;

    private Timestamp reqDate;
    private String status;

    private ArrayList typeOfLeasing;

    private Map<String, String> types;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private List<StationEntity> stations;
    private List<String> stationList;
    private List<String> stationList1;
    private List<DepotEntity> depots;
    private List<String> depotList;
    private List<String> depotList1;
    private List<RollingStockEntity> rollingStocks;
    private List<String> rollingStocksList;
    private List<TrackEntity> tracks;
    private List<String> trackList;
    private List<String> codes;
    private List<AdvertisementSpaceEntity> adverSpaceList;
    private List<DepotEntity> depotResult;
    private List<StationEntity> stationResult;
    private List<RollingStockEntity> rollingStockResult;
    private List<TrackEntity> trackResult;
    private List<LeasingSpaceEntity> leaseSpaceList;
    private List<NodeAssetEntity> nodeAssetList;
    private List<RollingStockAssetEntity> rollingAssetList;
    private List<ConsumableAssetEntity> consumAssetList;
    private List<String> depotStationList;
    private List<String> depotStationList1;
    private List<AssetRequestEntity> assetRequestList;
    private List<AssetRequestEntity> filteredAssetRequestList;
    private List<String> nslStationList;
    private List<String> nslStationList2; //list of stations and depots without null
    private List<String> ewlStationList;
    private List<String> ewlStationList2; //list of stations and depots without null
    private List<String> result; //for route filtering

    private List<TrainCarEntity> trainCars;
    private List<DepotEntity> filteredDepots;
    private List<StationEntity> filteredStations;
    private List<RollingStockEntity> filteredRollingStocks;
    private List<TrainCarEntity> filteredTrainCars;
    private List<LeasingSpaceEntity> filteredLeasingSpaces;
    private List<AdvertisementSpaceEntity> filteredAdverSpaces;
    private List<ConsumableAssetEntity> filteredConsumAssets;
    private List<NodeAssetEntity> filteredNodeAssets;
    private List<RollingStockAssetEntity> filteredRollStockAssets;

    private List<String> locations;
    private List<String> cabinList;
    private List<String> stationVenueList;
    private List<String> assetList;
    private List<String> depotStationRollingList;

    private ArrayList<String> trainCarSet;
    private List<String> trainCarResult;
    private List<String> selectedTrainCar;

    private Map<String, String> selection;
    private Map<String, Map<String, String>> selections = new HashMap<String, Map<String, String>>();
    private Date todayDate = new Date();
    private Date deadline;

    private double latitude;
    private double longitude;
    
    private String numberCode;
    private List<String> numberCodes;

    @PostConstruct
    public void init() {

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId") != null) {
            staffId = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("staffId").toString();
            staffDetails = getStaff(staffId);
            firstName = staffDetails.get(0);
            lastName = staffDetails.get(1);
            role = staffDetails.get(14);
            if (staffDetails.size() == 19) {
                team = staffDetails.get(17);

            }
        }

        stationVenueList = new ArrayList();
        stationVenueList.add("On the Platform");
        stationVenueList.add("On the Escalator");
        stationVenueList.add("Inside the Lift");
        stationVenueList.add("Outside the Lift");
        stationVenueList.add("Outside Gantry Area");
        stationVenueList.add("Top Up Machine Area");
        stationVenueList.add("Ground Floor Area");
        stationVenueList.add("Transit Area");

        Map<String, String> map = new HashMap<String, String>();
        map.put("Digital In Train Panel", "Digital In Train Panel");
        map.put("Non Digital In Train Panel", "Non Digital In Train Panel");
        map.put("Window Sticker", "Window Sticker");
        map.put("Train Hanger", "Train Hanger");
        data.put("RollingStock", map);

        map = new HashMap<String, String>();
        map.put("Concept Train", "TConcept Train");
        data.put("Whole Train", map);

        map = new HashMap<String, String>();
        map.put("Digital Panel", "Digital Panel");
        map.put("Non Digital Panel", "Non Digital Panel");
        data.put("Ground Floor Area", map);
        data.put("Outside Gantry Area", map);
        data.put("Top Up Machine Area", map);
        data.put("Outside the Lift", map);
        data.put("Inside the Lift", map);
        data.put("Transit Area", map);

        map = new HashMap<String, String>();
        map.put("Escalator Panel", "Escalator Panel");
        map.put("Escalator Sticker", "Escalator Sticker");
        data.put("On the Escalator", map);

        map = new HashMap<String, String>();
        map.put("Digital Board on Station", "Digital Board on Station");
        map.put("Non Digital Board on Station", "Non Digital Board on Station");
        map.put("Station Sticker", "Station Sticker");
        map.put("Station Concept Space", "Station Concept Space");
        data.put("On the Platform", map);

        typeOfLeasing = new ArrayList();
        typeOfLeasing.add("Retail");
        typeOfLeasing.add("Commerical");
        typeOfLeasing.add("Food and Beverage");

        stations = getStations();
        stationList = new ArrayList<String>();
        stationList1 = new ArrayList<String>();
        stationList.add("Null");
        for (int i = 0; i < stations.size(); i++) {
            stationList.add(stations.get(i).getCode());
        }
        for (int i = 0; i < stations.size(); i++) {
            stationList1.add(stations.get(i).getCode());
        }

        depots = getDepots();
        depotList = new ArrayList<String>();
        depotList1 = new ArrayList<String>();
        depotList.add("Null");
        for (int i = 0; i < depots.size(); i++) {
            depotList.add(depots.get(i).getCode());
            depotList1.add(depots.get(i).getCode());
        }

        depotStationList = new ArrayList<String>();
        depotStationList1 = new ArrayList<String>();
        depotStationList.add("Null");
        for (int i = 0; i < depots.size(); i++) {
            depotStationList.add(depots.get(i).getCode());
            depotStationList1.add(depots.get(i).getCode());
        }
        for (int i = 0; i < stations.size(); i++) {
            depotStationList.add(stations.get(i).getCode());
            depotStationList1.add(stations.get(i).getCode());
        }

        rollingStocks = getRollingStocks();
        rollingStocksList = new ArrayList<String>();
        for (int i = 0; i < rollingStocks.size(); i++) {
            rollingStocksList.add(rollingStocks.get(i).getInfraId());
        }

        nodeAssetTypeList = new ArrayList();
        nodeAssetTypeList.add("Office Equipments");
        nodeAssetTypeList.add("Operating Equipments");
        nodeAssetTypeList.add("Audio Visual Equipments");
        nodeAssetTypeList.add("Photographic Equipments");
        nodeAssetTypeList.add("Maintenance Equipments");
        nodeAssetTypeList.add("Computer Equipments");
        nodeAssetTypeList.add("Decorations");
        nodeAssetTypeList.add("Others");

        consumableAssetTypeList = new ArrayList();
        consumableAssetTypeList.add("Office Supplies");
        consumableAssetTypeList.add("Cleaning Supplies");
        consumableAssetTypeList.add("Food & Beverage Supplies");
        consumableAssetTypeList.add("Medicine Supplies");
        consumableAssetTypeList.add("Others");

        rollingStockAssetTypeList = new ArrayList();
        rollingStockAssetTypeList.add("Audio Visual Equipments");
        rollingStockAssetTypeList.add("System Equipements");
        rollingStockAssetTypeList.add("Operating Equipments");
        rollingStockAssetTypeList.add("Medicine Supplies");
        rollingStockAssetTypeList.add("Others");

        infraAssetSessionBeanLocal.generateAssetRequest();

        getNSLList();
        getEWLList();
        tenderSessionBeanLocal.checkLeasing();
    }

    public void getNSLList() {
        //generate depot and station codes in NSL
        stations = getStations();
        depots = getDepots();
        nslStationList = new ArrayList<String>();
        nslStationList2 = new ArrayList<String>();
        nslStationList.add("Null");
        for (int i = 0; i < depots.size(); i++) {
            if (depots.get(i).getCode().substring(0, 3).equals("NSL")) {
                nslStationList.add(depots.get(i).getCode());
                nslStationList2.add(depots.get(i).getCode());
            }
        }
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getCode().substring(0, 3).equals("NSL")) {
                nslStationList.add(stations.get(i).getCode());
                nslStationList2.add(stations.get(i).getCode());
            }
        }
    }

    public void getEWLList() {
        //generate depot and station codes in EWL
        stations = getStations();
        depots = getDepots();
        ewlStationList = new ArrayList<String>();
        ewlStationList2 = new ArrayList<String>();
        ewlStationList.add("Null");
        for (int i = 0; i < depots.size(); i++) {
            if (depots.get(i).getCode().substring(0, 3).equals("EWL")) {
                ewlStationList.add(depots.get(i).getCode());
                ewlStationList2.add(depots.get(i).getCode());
            }
        }
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getCode().substring(0, 3).equals("EWL")) {
                ewlStationList.add(stations.get(i).getCode());
                ewlStationList2.add(stations.get(i).getCode());
            }
        }
    }

    public String attachedRS(int carCode) {
        this.carCode = carCode;
        TrainCarEntity temp = (TrainCarEntity) infraAssetSessionBeanLocal.searchCar(carCode);
        InfrastructureEntity infra = temp.getInfrastructure();
        RollingStockEntity rs = (RollingStockEntity) infra;
        if (rs == null) {
            String msg = "NIL";
            return msg;
        } else {
            String attachedRS = rs.getInfraId();
            return attachedRS;
        }
    }

    public String attachedDepot(int carCode) {
        this.carCode = carCode;
        String attachedDepot = "";
        TrainCarEntity temp = (TrainCarEntity) infraAssetSessionBeanLocal.searchCar(carCode);
        if (temp.getInfrastructure() instanceof NodeEntity) {
            InfrastructureEntity infra = temp.getInfrastructure();
            DepotEntity depot = (DepotEntity) infra;
            if (depot == null) {
                String msg = "NIL";
                return msg;
            } else {
                attachedDepot = depot.getCode();
            }
        }
        return attachedDepot;
    }

    //retrieve a string of train cars' codes
    public String getTrainCarList(String infraId) {
        this.infraId = infraId;
        RollingStockEntity temp = (RollingStockEntity) infraAssetSessionBeanLocal.searchInfra(infraId);
        ArrayList<AssetEntity> assets = new ArrayList<AssetEntity>(temp.getAssets());
        ArrayList<TrainCarEntity> tcList = new ArrayList<TrainCarEntity>();
        for (int i = 0; i < assets.size(); i++) {
            if (assets.get(i).getAssetId().substring(0, 2).equals("TC")) {
                TrainCarEntity tc = (TrainCarEntity) assets.get(i);
                tcList.add(tc);
            }
        }

        trainCarSet = new ArrayList<String>();
        for (int i = 0; i < tcList.size(); i++) {
            String carCode = Integer.toString(tcList.get(i).getCarCode());
            trainCarSet.add(carCode);;
        }

        String carList = "";

        if (trainCarSet.size() == 6) {
            carList = trainCarSet.get(0) + ", " + trainCarSet.get(1) + ", " + trainCarSet.get(2) + ", "
                    + trainCarSet.get(3) + ", " + trainCarSet.get(4) + ", " + trainCarSet.get(5);
        } else {
            carList = "";
        }
System.out.println("dsadsad " + carList + trainCarSet.size() + " 11dasdas1111");
        return carList;
    }

    public void findNumOfCar(String brand) {
        ArrayList<TrainCarEntity> outcome = infraAssetSessionBeanLocal.getTrainCars();
        int headCount = 0;
        int bodyCount = 0;
        for (int i = 0; i < outcome.size(); i++) {
            InfrastructureEntity temp = outcome.get(i).getInfrastructure();
            DepotEntity depot = (DepotEntity) temp;
            if ((outcome.get(i).getBrand().equals(brand)) && (depot.getCode().equals(selectedDepot))
                    && (outcome.get(i).getStatus().equals("Available"))) {
                if (outcome.get(i).getType().equals("Driving Trailer")) {
                    headCount = headCount + 1;
                } else {
                    bodyCount = bodyCount + 1;
                }
            }
        }
        availableCarList = headCount + " Driving Trailer(s), " + bodyCount + " Motor Car(s)";
        System.out.println(availableCarList);
    }

    public void onLocationChange() {
        if (assetName != null && !assetName.equals("")) {
            if (assetName.equals("RollingStock")) {
                codes = rollingStocksList;
            } else {
                codes = stationList1;
                locations = stationVenueList;
            }
        }
    }

    public void onTypeChange() {
        if (assetRequestType != null && !assetRequestType.equals("")) {
            assetList = new ArrayList();
            boolean statuss = false;
            if (assetRequestType.equals("Consumable Asset")) {
                ArrayList<ConsumableAssetEntity> consumableAssetList = infraAssetSessionBeanLocal.getConsumableAsset();
                for (int i = 0; i < consumableAssetList.size(); i++) {
                    statuss = false;
                    if (consumableAssetList.get(i).getConsumableAssetType().equals(assetType)) {
                        if (assetList.isEmpty()) {
                            assetList.add(consumableAssetList.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(consumableAssetList.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(consumableAssetList.get(i).getAssetName());
                        }
                    }
                }
            } else if (assetRequestType.equals("Node Asset")) {
                ArrayList<NodeAssetEntity> nodeAssetLists = infraAssetSessionBeanLocal.getNodeAsset();
                for (int i = 0; i < nodeAssetLists.size(); i++) {
                    statuss = false;
                    if (nodeAssetLists.get(i).getNodeAssetType().equals(assetType)) {
                        if (assetList.isEmpty()) {
                            assetList.add(nodeAssetLists.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(nodeAssetLists.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(nodeAssetLists.get(i).getAssetName());
                        }
                    }
                }
            } else {
                ArrayList<RollingStockAssetEntity> rollingStockAssetList = infraAssetSessionBeanLocal.getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList.size(); i++) {
                    statuss = false;
                    if (rollingStockAssetList.get(i).getRollingStockAssetType().equals(assetType)) {
                        if (assetList.isEmpty()) {
                            assetList.add(rollingStockAssetList.get(i).getAssetName());
                        }
                        for (int j = 0; j < assetList.size(); j++) {
                            if (assetList.get(j).equals(rollingStockAssetList.get(i).getAssetName())) {
                                statuss = true;
                                break;
                            }
                        }
                        if (statuss == false) {
                            assetList.add(rollingStockAssetList.get(i).getAssetName());
                        }
                    }
                }
            }
        }
    }

    public void onTypeChange1() {
        depotStationRollingList = null;
        if (assetRequestType != null && !assetRequestType.equals("")) {
            if (assetRequestType.equals("Consumable Asset")) {
                depotStationRollingList = depotStationList1;
            } else if (assetRequestType.equals("Node Asset")) {
                depotStationRollingList = depotStationList1;
            } else if (assetRequestType.equals("Rolling Stock Asset")) {
                depotStationRollingList = rollingStocksList;
            }
        }
    }

    public void onTypeChange2() {
        assetTypeList = null;
        if (assetRequestType != null && !assetRequestType.equals("")) {
            if (assetRequestType.equals("Consumable Asset")) {
                assetTypeList = consumableAssetTypeList;
            } else if (assetRequestType.equals("Node Asset")) {
                assetTypeList = nodeAssetTypeList;
            } else if (assetRequestType.equals("Rolling Stock Asset")) {
                assetTypeList = rollingStockAssetTypeList;
            }
        }
    }

    public void onTypeChange3() {
        assetList = new ArrayList();
        boolean statuss = false;
        ArrayList<RollingStockAssetEntity> rollingStockAssetList = infraAssetSessionBeanLocal.getRollingStockAsset();
        for (int i = 0; i < rollingStockAssetList.size(); i++) {
            statuss = false;
            if (rollingStockAssetList.get(i).getRollingStockAssetType().equals(rollingStockAssetType)) {
                if (assetList.isEmpty()) {
                    assetList.add(rollingStockAssetList.get(i).getAssetName());
                }
                for (int j = 0; j < assetList.size(); j++) {
                    if (assetList.get(j).equals(rollingStockAssetList.get(i).getAssetName())) {
                        statuss = true;
                        break;
                    }
                }
                if (statuss == false) {
                    assetList.add(rollingStockAssetList.get(i).getAssetName());
                }
            }
        }
    }

    public void onTypeChange4() {
        assetList = new ArrayList();
        boolean statuss = false;
        ArrayList<NodeAssetEntity> nodeAssetLists = infraAssetSessionBeanLocal.getNodeAsset();
        for (int i = 0; i < nodeAssetLists.size(); i++) {
            statuss = false;
            if (nodeAssetLists.get(i).getNodeAssetType().equals(nodeAssetType)) {
                if (assetList.isEmpty()) {
                    assetList.add(nodeAssetLists.get(i).getAssetName());
                }
                for (int j = 0; j < assetList.size(); j++) {
                    if (assetList.get(j).equals(nodeAssetLists.get(i).getAssetName())) {
                        statuss = true;
                        break;
                    }
                }
                if (statuss == false) {
                    assetList.add(nodeAssetLists.get(i).getAssetName());
                }
            }
        }
    }

    public void onTypeChange5() {
        assetList = new ArrayList();
        boolean statuss = false;
        ArrayList<ConsumableAssetEntity> consumableAssetList = infraAssetSessionBeanLocal.getConsumableAsset();
        for (int i = 0; i < consumableAssetList.size(); i++) {
            statuss = false;
            if (consumableAssetList.get(i).getConsumableAssetType().equals(consumableAssetType)) {
                if (assetList.isEmpty()) {
                    assetList.add(consumableAssetList.get(i).getAssetName());
                }
                for (int j = 0; j < assetList.size(); j++) {
                    if (assetList.get(j).equals(consumableAssetList.get(i).getAssetName())) {
                        statuss = true;
                        break;
                    }
                }
                if (statuss == false) {
                    assetList.add(consumableAssetList.get(i).getAssetName());
                }
            }
        }

    }

    public void onLocationChange2() {
        if (assetName != null && !assetName.equals("")) {
            if (assetName.equals("RollingStock")) {
                cabinList = new ArrayList();
                cabinList = infraAssetSessionBeanLocal.getTrainCarList(code);
                locations = cabinList;
            } else {
                locations = stationVenueList;
            }
        }
    }
    
    public void onLocationChange3() {
        numberCodes = new ArrayList<String>();
        ArrayList<String> defaultIdentifier = new ArrayList<String>();
        defaultIdentifier.add("A");
        defaultIdentifier.add("B");
        defaultIdentifier.add("C");
        ArrayList<AdvertisementSpaceEntity> advertList = infraAssetSessionBeanLocal.getAdvertisementSpace();
        if (assetName.equals("RollingStock")) {
            for (int i = 0; i < defaultIdentifier.size(); i++) {
                boolean addToList = true;
                for (int j = 0; j < advertList.size(); j++) {
                    AdvertisementSpaceEntity advert = advertList.get(j);
                    if (advert.getLocation().equals(location) && advert.getType().equals(type) && advert.getNumberCode().equals(defaultIdentifier.get(i)) && advert.getInfrastructure().getInfraId().equals(code)) {
                        addToList = false;
                        break;
                    }
                }
                if (addToList) {
                    numberCodes.add(defaultIdentifier.get(i));
                }
            }
        } else {
            NodeEntity node = infraAssetSessionBeanLocal.searchNode(code);
            for (int i = 0; i < defaultIdentifier.size(); i++) {
                boolean addToList = true;
                for (int j = 0; j < advertList.size(); j++) {
                    AdvertisementSpaceEntity advert = advertList.get(j);
                    if (advert.getLocation().equals(location) && advert.getType().equals(type) && advert.getNumberCode().equals(defaultIdentifier.get(i)) && advert.getInfrastructure().getInfraId().equals(node.getInfraId())) {
                        addToList = false;
                        break;
                    }
                }
                if (addToList) {
                    numberCodes.add(defaultIdentifier.get(i));
                }
            }

        }
    }

    public void onLocationChange1() {
        if (location != null && !location.equals("")) {
            if (assetName.equals("RollingStock")) {
                types = data.get("RollingStock");
            } else {
                types = data.get(location);
            }
        }
    }

    public void onSelectionChange() {
        if (assetName != null && !assetName.equals("")) {
            if (assetName.equals("Food and Beverage")) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Yes", "true");
                selections.put("Food and Beverage", map);
                selection = selections.get(assetName);
            } else if (assetName.equals("Retail")) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Yes", "true");
                map.put("No", "false");
                selections.put("Retail", map);
                selection = selections.get(assetName);
            } else {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Yes", "true");
                map.put("No", "false");
                selections.put("Commerical", map);
                selection = selections.get(assetName);
            }
        }
    }

    public void selectLine() {
        if (trainLine != null && !trainLine.equals("")) {
            if (trainLine.equals("NSL")) {
                result = nslStationList;
                if (nslStationList2.isEmpty()) {
                    code = "00";
                }
                if (nslStationList2.size() < 10 && nslStationList2.size() > 0) {
                    int count = nslStationList2.size();
                    code = "0" + Integer.toString(count);
                }
                if (nslStationList2.size() >= 10) {
                    int count = nslStationList2.size();
                    code = Integer.toString(count);
                }
            }
            if (trainLine.equals("EWL")) {
                result = ewlStationList;
                if (ewlStationList2.isEmpty()) {
                    code = "00";
                }
                if (ewlStationList2.size() < 10 && ewlStationList2.size() > 0) {
                    int count = ewlStationList2.size();
                    code = "0" + Integer.toString(count);
                }
                if (ewlStationList2.size() >= 10) {
                    int count = ewlStationList2.size();
                    code = Integer.toString(count);
                    /*String nodeCode = trainLine + code;
                    System.out.println(nodeCode);
                    boolean status = true;
                    while (status) {
                        if (infraAssetSessionBeanLocal.searchNode(nodeCode) == null) {
                            break;
                        }
                        count = count + 2;
                        code = Integer.toString(count);
                    }*/
                }
            }
        }
    }

    public void selectStatus(String infraId) {
        if (rsStatus.equals("Under Maintenance")) {
            trainCarResult = infraAssetSessionBeanLocal.getTrainCarList(infraId);
            rsStatus = "Under Maintenance";
        }
    }

    public boolean checkTeams(String infraId) {
        List<TeamEntity> teams = systemAdminSessionBeanLocal.getTeams();
        boolean status = true;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getNode().getInfraId().equals(infraId)) {
                return status;
            } else {
                status = false;
            }
        }
        return status;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public List<String> getNumberCodes() {
        return numberCodes;
    }

    public void setNumberCodes(List<String> numberCodes) {
        this.numberCodes = numberCodes;
    }

    
    
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<String> getSelectedTrainCar() {
        return selectedTrainCar;
    }

    public void setSelectedTrainCar(List<String> selectedTrainCar) {
        this.selectedTrainCar = selectedTrainCar;
    }

    public String getWaterProvision1() {
        return waterProvision1;
    }

    public void setWaterProvision1(String waterProvision1) {
        this.waterProvision1 = waterProvision1;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public List<String> getDepotStationRollingList() {
        return depotStationRollingList;
    }

    public void setDepotStationRollingList(List<String> depotStationRollingList) {
        this.depotStationRollingList = depotStationRollingList;
    }

    public List<String> getAssetTypeList() {
        return assetTypeList;
    }

    public void setAssetTypeList(List<String> assetTypeList) {
        this.assetTypeList = assetTypeList;
    }

    public List<String> getAssetList() {
        return assetList;
    }

    public void setAssetList(List<String> assetList) {
        this.assetList = assetList;
    }

    public String getAssetRequestType() {
        return assetRequestType;
    }

    public void setAssetRequestType(String assetRequestType) {
        this.assetRequestType = assetRequestType;
    }

    public String getNodeAssetType() {
        return nodeAssetType;
    }

    public void setNodeAssetType(String nodeAssetType) {
        this.nodeAssetType = nodeAssetType;
    }

    public String getConsumableAssetType() {
        return consumableAssetType;
    }

    public void setConsumableAssetType(String consumableAssetType) {
        this.consumableAssetType = consumableAssetType;
    }

    public String getRollingStockAssetType() {
        return rollingStockAssetType;
    }

    public void setRollingStockAssetType(String rollingStockAssetType) {
        this.rollingStockAssetType = rollingStockAssetType;
    }

    public List<DepotEntity> getFilteredDepots() {
        return filteredDepots;
    }

    public void setFilteredDepots(List<DepotEntity> filteredDepots) {
        this.filteredDepots = filteredDepots;
    }

    public List<StationEntity> getFilteredStations() {
        return filteredStations;
    }

    public void setFilteredStations(List<StationEntity> filteredStations) {
        this.filteredStations = filteredStations;
    }

    public List<RollingStockEntity> getFilteredRollingStocks() {
        return filteredRollingStocks;
    }

    public void setFilteredRollingStocks(List<RollingStockEntity> filteredRollingStocks) {
        this.filteredRollingStocks = filteredRollingStocks;
    }

    public List<TrainCarEntity> getFilteredTrainCars() {
        return filteredTrainCars;
    }

    public void setFilteredTrainCars(List<TrainCarEntity> filteredTrainCars) {
        this.filteredTrainCars = filteredTrainCars;
    }

    public List<LeasingSpaceEntity> getFilteredLeasingSpaces() {
        return filteredLeasingSpaces;
    }

    public void setFilteredLeasingSpaces(List<LeasingSpaceEntity> filteredLeasingSpaces) {
        this.filteredLeasingSpaces = filteredLeasingSpaces;
    }

    public List<AdvertisementSpaceEntity> getFilteredAdverSpaces() {
        return filteredAdverSpaces;
    }

    public void setFilteredAdverSpaces(List<AdvertisementSpaceEntity> filteredAdverSpaces) {
        this.filteredAdverSpaces = filteredAdverSpaces;
    }

    public List<ConsumableAssetEntity> getFilteredConsumAssets() {
        return filteredConsumAssets;
    }

    public void setFilteredConsumAssets(List<ConsumableAssetEntity> filteredConsumAssets) {
        this.filteredConsumAssets = filteredConsumAssets;
    }

    public List<NodeAssetEntity> getFilteredNodeAssets() {
        return filteredNodeAssets;
    }

    public void setFilteredNodeAssets(List<NodeAssetEntity> filteredNodeAssets) {
        this.filteredNodeAssets = filteredNodeAssets;
    }

    public List<RollingStockAssetEntity> getFilteredRollStockAssets() {
        return filteredRollStockAssets;
    }

    public void setFilteredRollStockAssets(List<RollingStockAssetEntity> filteredRollStockAssets) {
        this.filteredRollStockAssets = filteredRollStockAssets;
    }

    private ArrayList<String> getStaff(String staffId) {
        return accountSessionBeanLocal.viewProfile(staffId);
    }

    public ArrayList<String> getTrainCarSet() {
        return trainCarSet;
    }

    public void setTrainCarSet(ArrayList<String> trainCarSet) {
        this.trainCarSet = trainCarSet;
    }

    public List<String> getTrainCarResult() {
        return trainCarResult;
    }

    public void setTrainCarResult(List<String> trainCarResult) {
        this.trainCarResult = trainCarResult;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getMcEntitlement() {
        return mcEntitlement;
    }

    public void setMcEntitlement(String mcEntitlement) {
        this.mcEntitlement = mcEntitlement;
    }

    public String getLeaveEntitlement() {
        return leaveEntitlement;
    }

    public void setLeaveEntitlement(String leaveEntitlement) {
        this.leaveEntitlement = leaveEntitlement;
    }

    public ArrayList<String> getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(ArrayList<String> staffDetails) {
        this.staffDetails = staffDetails;
    }

    public String getInfraAddress() {
        return infraAddress;
    }

    public void setInfraAddress(String infraAddress) {
        this.infraAddress = infraAddress;
    }

    public List<TrainCarEntity> getTrainCars() {
        trainCars = infraAssetSessionBeanLocal.getTrainCars();
        return trainCars;
    }

    public void setTrainCars(List<TrainCarEntity> trainCars) {
        this.trainCars = trainCars;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getInfraId() {
        return infraId;
    }

    public void setInfraId(String infraId) {
        this.infraId = infraId;
    }

    public Map<String, String> getSelection() {
        return selection;
    }

    public void setSelection(Map<String, String> selection) {
        this.selection = selection;
    }

    public Map<String, Map<String, String>> getSelections() {
        return selections;
    }

    public void setSelections(Map<String, Map<String, String>> selections) {
        this.selections = selections;
    }

    public List<StationEntity> getStations() {
        stations = infraAssetSessionBeanLocal.getStation();
        return stations;
    }

    public void setStations(List<StationEntity> stations) {
        this.stations = stations;
    }

    public List<DepotEntity> getDepots() {
        depots = infraAssetSessionBeanLocal.getDepot();
        return depots;
    }

    public void setDepot(List<DepotEntity> depots) {
        this.depots = depots;
    }

    public List<RollingStockEntity> getRollingStocks() {
        rollingStocks = infraAssetSessionBeanLocal.getRollingStock();
        return rollingStocks;
    }

    public void setRollingStock(List<RollingStockEntity> rollingStocks) {
        this.rollingStocks = rollingStocks;
    }

    public List<TrackEntity> getTracks() {
        tracks = infraAssetSessionBeanLocal.getTrack();
        return tracks;
    }

    public void setTracks(List<TrackEntity> tracks) {
        this.tracks = tracks;
    }

    public String getFixedTrainLine() {
        return fixedTrainLine;
    }

    public void setFixedTrainLine(String fixedTrainLine) {
        this.fixedTrainLine = fixedTrainLine;
    }

    public String getInfraName() {
        return infraName;
    }

    public void setInfraName(String infraName) {
        this.infraName = infraName;
    }

    public double getDistanceToFirstStation() {
        return distanceToFirstStation;
    }

    public void setDistanceToFirstStation(double distanceToFirstStation) {
        this.distanceToFirstStation = distanceToFirstStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public String getPreviousStation() {
        return previousStation;
    }

    public void setPreviousStation(String previousStation) {
        this.previousStation = previousStation;
    }

    public String getRsStatus() {
        return rsStatus;
    }

    public void setRsStatus(String rsStatus) {
        this.rsStatus = rsStatus;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSelectedDepot() {
        return selectedDepot;
    }

    public void setSelectedDepot(String selectedDepot) {
        this.selectedDepot = selectedDepot;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrainLine() {
        return trainLine;
    }

    public void setTrainLine(String trainLine) {
        this.trainLine = trainLine;
    }

    public double getPeakPeriod() {
        return peakPeriod;
    }

    public void setPeakPeriod(double peakPeriod) {
        this.peakPeriod = peakPeriod;
    }

    public double getNonpeakPeriod() {
        return nonpeakPeriod;
    }

    public void setNonpeakPeriod(double nonpeakPeriod) {
        this.nonpeakPeriod = nonpeakPeriod;
    }

    public String getAssetReqId() {
        return assetReqId;
    }

    public void setAssetReqId(String assetReqId) {
        this.assetReqId = assetReqId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Timestamp getReqDate() {
        return reqDate;
    }

    public void setReqDate(Timestamp reqDate) {
        this.reqDate = reqDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }

    public String getWaterProvision() {
        return waterProvision;
    }

    public void setWaterProvision(String waterProvision) {
        this.waterProvision = waterProvision;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public int getLifetimeValue() {
        return lifetimeValue;
    }

    public void setLifetimeValue(int lifetimeValue) {
        this.lifetimeValue = lifetimeValue;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public Map<String, String> getTypes() {
        return types;
    }

    public void setTypes(Map<String, String> types) {
        this.types = types;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public ArrayList getTypeOfLeasing() {
        return typeOfLeasing;
    }

    public void setTypeOfLeasing(ArrayList typeOfLeasing) {
        this.typeOfLeasing = typeOfLeasing;
    }

    public List<String> getStationList() {
        return stationList;
    }

    public void setStationList(List<String> stationList) {
        this.stationList = stationList;
    }

    public List<String> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<String> depotList) {
        this.depotList = depotList;
    }

    public List<String> getNslStationList() {
        return nslStationList;
    }

    public void setNslStationList(List<String> nslStationList) {
        this.nslStationList = nslStationList;
    }

    public List<String> getNslStationList2() {
        return nslStationList2;
    }

    public void setNslStationList2(List<String> nslStationList2) {
        this.nslStationList2 = nslStationList2;
    }

    public List<String> getEwlStationList2() {
        return ewlStationList2;
    }

    public void setEwlStationList2(List<String> ewlStationList2) {
        this.ewlStationList2 = ewlStationList2;
    }

    public List<String> getEwlStationList() {
        return ewlStationList;
    }

    public void setEwlStationList(List<String> ewlStationList) {
        this.ewlStationList = ewlStationList;
    }

    public List<String> getRollingStocksList() {
        return rollingStocksList;
    }

    public void setRollingStocksList(List<String> rollingStocksList) {
        this.rollingStocksList = rollingStocksList;
    }

    public List<String> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<String> trackList) {
        this.trackList = trackList;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<AdvertisementSpaceEntity> getAdverSpaceList() {
        adverSpaceList = infraAssetSessionBeanLocal.getAdvertisementSpace();
        return adverSpaceList;
    }

    public void setAdverSpaceList(List<AdvertisementSpaceEntity> adverSpaceList) {
        this.adverSpaceList = adverSpaceList;
    }

    public List<AssetRequestEntity> getAssetRequestList() {
        assetRequestList = infraAssetSessionBeanLocal.getAssetRequest();
        return assetRequestList;
    }

    public void setAssetRequestList(List<AssetRequestEntity> assetRequestList) {
        this.assetRequestList = assetRequestList;
    }

    public List<DepotEntity> getDepotResult() {
        depotResult = infraAssetSessionBeanLocal.getDepot();
        return depotResult;
    }

    public void setDepotResult(List<DepotEntity> depotResult) {
        this.depotResult = depotResult;
    }

    public List<StationEntity> getStationResult() {
        stationResult = infraAssetSessionBeanLocal.getStation();
        return stationResult;
    }

    public void setStationResult(List<StationEntity> stationResult) {
        this.stationResult = stationResult;
    }

    public List<RollingStockEntity> getRollingStockResult() {
        rollingStockResult = infraAssetSessionBeanLocal.getRollingStock();
        return rollingStockResult;
    }

    public void setRollingStockResult(List<RollingStockEntity> rollingStockResult) {
        this.rollingStockResult = rollingStockResult;
    }

    public List<TrackEntity> getTrackResult() {
        trackResult = infraAssetSessionBeanLocal.getTrack();
        return trackResult;
    }

    public void setTrackResult(List<TrackEntity> trackResult) {
        this.trackResult = trackResult;
    }

    public List<LeasingSpaceEntity> getLeaseSpaceList() {
        leaseSpaceList = infraAssetSessionBeanLocal.getLeasingSpace();
        return leaseSpaceList;
    }

    public void setLeaseSpaceList(List<LeasingSpaceEntity> leaseSpaceList) {
        this.leaseSpaceList = leaseSpaceList;
    }

    public List<NodeAssetEntity> getNodeAssetList() {
        nodeAssetList = infraAssetSessionBeanLocal.getNodeAsset();
        return nodeAssetList;
    }

    public void setNodeAssetList(List<NodeAssetEntity> nodeAssetList) {
        this.nodeAssetList = nodeAssetList;
    }

    public List<RollingStockAssetEntity> getRollingAssetList() {
        rollingAssetList = infraAssetSessionBeanLocal.getRollingStockAsset();
        return rollingAssetList;
    }

    public void setRollingAssetList(List<RollingStockAssetEntity> rollingAssetList) {
        this.rollingAssetList = rollingAssetList;
    }

    public List<ConsumableAssetEntity> getConsumAssetList() {
        consumAssetList = infraAssetSessionBeanLocal.getConsumableAsset();
        return consumAssetList;
    }

    public void setConsumAssetList(List<ConsumableAssetEntity> consumAssetList) {
        this.consumAssetList = consumAssetList;
    }

    public List<String> getDepotStationList() {
        return depotStationList;
    }

    public void setDepotStationList(List<String> depotStationList) {
        this.depotStationList = depotStationList;
    }

    public List<String> getStationList1() {
        return stationList1;
    }

    public void setStationList1(List<String> stationList1) {
        this.stationList1 = stationList1;
    }

    public List<String> getDepotList1() {
        return depotList1;
    }

    public void setDepotList1(List<String> depotList1) {
        this.depotList1 = depotList1;
    }

    public List<String> getDepotStationList1() {
        return depotStationList1;
    }

    public void setDepotStationList1(List<String> depotStationList1) {
        this.depotStationList1 = depotStationList1;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getCabinList() {
        return cabinList;
    }

    public void setCabinList(List<String> cabinList) {
        this.cabinList = cabinList;
    }

    public List<String> getStationVenueList() {
        return stationVenueList;
    }

    public void setStationVenueList(List<String> stationVenueList) {
        this.stationVenueList = stationVenueList;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public int getCarCode() {
        return carCode;
    }

    public void setCarCode(int carCode) {
        this.carCode = carCode;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getStorageLoc() {
        return storageLoc;
    }

    public void setStorageLoc(String storageLoc) {
        this.storageLoc = storageLoc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLeaseYear() {
        return leaseYear;
    }

    public void setLeaseYear(int leaseYear) {
        this.leaseYear = leaseYear;
    }

    public String getLeaseDescription() {
        return leaseDescription;
    }

    public void setLeaseDescription(String leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public String getAvailableCarList() {
        return availableCarList;
    }

    public void setAvailableCarList(String availableCarList) {
        this.availableCarList = availableCarList;
    }

    public List<String> getNodeAssetTypeList() {
        return nodeAssetTypeList;
    }

    public void setNodeAssetTypeList(List<String> nodeAssetTypeList) {
        this.nodeAssetTypeList = nodeAssetTypeList;
    }

    public List<String> getConsumableAssetTypeList() {
        return consumableAssetTypeList;
    }

    public void setConsumableAssetTypeList(List<String> consumableAssetTypeList) {
        this.consumableAssetTypeList = consumableAssetTypeList;
    }

    public List<String> getRollingStockAssetTypeList() {
        return rollingStockAssetTypeList;
    }

    public void setRollingStockAssetTypeList(List<String> rollingStockAssetTypeList) {
        this.rollingStockAssetTypeList = rollingStockAssetTypeList;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
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

    public InfraAssetManagedBean() {
    }

    public String addDepot() {
        boolean status = infraAssetSessionBeanLocal.addDepot(trainLine, code, infraName, infraAddress, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Depot is added successfully",
                            ""));
            this.trainLine = null;
            this.code = null;
            this.infraAddress = null;
            this.infraName = null;
            this.previousStation = null;
            this.nextStation = null;
            this.distanceToFirstStation = 0;
            this.latitude = 0;
            this.longitude = 0;
            getNSLList();
            getEWLList();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a depot!", ""));
            return "depot";
        }
        return "depot";
    }

    public String addStation() {
        boolean status = infraAssetSessionBeanLocal.addStation(trainLine, code, infraName, infraAddress, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Station is added successfully",
                            ""));
            this.trainLine = null;
            this.code = null;
            this.infraAddress = null;
            this.infraName = null;
            this.previousStation = null;
            this.nextStation = null;
            this.distanceToFirstStation = 0;
            getNSLList();
            getEWLList();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a station!", ""));
            return "station";
        }
        return "station";
    }

    public String addRollingStock() {
        boolean status = infraAssetSessionBeanLocal.addRollingStock(infraName, brand, selectedDepot);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling stock is added successfully",
                            ""));
            this.infraName = null;
            this.brand = null;
            this.selectedDepot = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a rolling stock!", ""));
            return "rollingStock";
        }
        return "rollingStock";
    }

    public String addTrack() {
        boolean status = infraAssetSessionBeanLocal.addTrack(trainLine, infraName);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Track is added successfully",
                            ""));
            this.trainLine = null;
            this.infraName = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a track!", ""));
            return "track";
        }
        return "track";
    }

    public String addTrainCar() {
        boolean status = infraAssetSessionBeanLocal.addTrainCar(carCode, brand, storageLoc, type, description);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Train car is added successfully",
                            ""));
            this.brand = null;
            this.storageLoc = null;
            this.type = null;
            this.description = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add a train car!", ""));
            return "trainCar";
        }
        return "trainCar";
    }

    public String updateDepot() {
        boolean status = infraAssetSessionBeanLocal.updateDepot(infraId, fixedTrainLine, code, infraName, infraAddress, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Depot is updated successfully",
                            ""));
            this.code = null;
            this.infraAddress = null;
            this.infraName = null;
            this.previousStation = null;
            this.nextStation = null;
            this.distanceToFirstStation = 0;
            this.latitude = 0;
            this.longitude = 0;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the depot!", ""));
            return "depot";
        }
        return "depot";
    }

    public String updateStation() {
        boolean status = infraAssetSessionBeanLocal.updateStation(infraId, fixedTrainLine, code, infraName, infraAddress, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Station is updated successfully",
                            ""));
            this.code = null;
            this.infraAddress = null;
            this.infraName = null;
            this.previousStation = null;
            this.nextStation = null;
            this.distanceToFirstStation = 0;
            this.latitude = 0;
            this.longitude = 0;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the station!", ""));
            return "station";
        }
        return "station";
    }

    public String updateRollingStock() {
        boolean status = infraAssetSessionBeanLocal.updateRollingStock(infraId, rsStatus, selectedTrainCar, staffId, selectedDepot);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling stock is updated successfully",
                            ""));
            this.infraName = null;
            this.rsStatus = null;
            this.selectedTrainCar = null;
            this.selectedDepot = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the rolling stock!", ""));
            return "rollingStock";
        }
        return "rollingStock";
    }

    public String updateTrack() {
        boolean status = infraAssetSessionBeanLocal.updateTrack(infraId, infraName);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Track is updated successfully",
                            ""));
            this.trainLine = null;
            this.infraName = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the track!", ""));
            return "track";
        }
        return "track";
    }

    public String updateTrainCar() {
        boolean status = infraAssetSessionBeanLocal.updateTrainCar(assetId, description, carStatus, storageLoc);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Train car is updated successfully",
                            ""));
            this.description = null;
            this.storageLoc = null;
            this.carStatus = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update a train car!", ""));
            return "trainCar";
        }
        return "trainCar";
    }

    /*public String deleteInfra() {
        //UIViewRoot viewId = FacesContext.getCurrentInstance().getViewRoot();
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        ViewHandler handler = context.getApplication().getViewHandler();
        UIViewRoot root = handler.createView(context, viewId);

        boolean status = infraAssetSessionBeanLocal.deleteInfra(infraId);
        if (status) {
            //FacesContext.getCurrentInstance().setViewRoot(viewId);
            //FacesContext.getCurrentInstance().renderResponse();
            root.setViewId(viewId);
            context.setViewRoot(root);
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "viewIdindex.xhtml?faces-redirect=true");
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Infrastructure is deleted successfully",
                            ""));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the infrastructure!", ""));
            return "viewId?faces-redirect=true";
        }
        return "viewId?faces-redirect=true";
    }*/
    public String addAdvertisementSpace() {
        boolean sta = infraAssetSessionBeanLocal.addAdvertisementSpace(assetName, code, location, type, peakPeriod, nonpeakPeriod,numberCode,deadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Advertisement space is added successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.location = null;
            this.peakPeriod = 0;
            this.nonpeakPeriod = 0;
            this.type = null;
            this.numberCode = null;
            this.deadline = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add the advertisement space!", ""));
            return "advertSpace";
        }
        return "advertSpace";
    }

    public String updateAdvertisementSpace() {
        boolean sta = infraAssetSessionBeanLocal.updateAdvertisementSpace(assetId, assetName, code, location, type, peakPeriod, nonpeakPeriod,numberCode, deadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Advertisement space is updated successfully",
                            ""));
            this.assetId = null;
            this.assetName = null;
            this.code = null;
            this.location = null;
            this.peakPeriod = 0;
            this.nonpeakPeriod = 0;
            this.type = null;
            this.numberCode = null;
            this.deadline = null;

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the advertisement space!", ""));
            return "advertSpace";
        }
        return "advertSpace";
    }

    public String updateAdvertisementSpace1() {
        boolean sta = infraAssetSessionBeanLocal.updateAdvertisementSpace(assetId, assetName, code, location, type, peakPeriod, nonpeakPeriod,numberCode,deadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Advertisement space is updated successfully",
                            ""));
            this.assetId = null;
            this.assetName = null;
            this.code = null;
            this.location = null;
            this.peakPeriod = 0;
            this.nonpeakPeriod = 0;
            this.type = null;
            this.numberCode = null;
            this.deadline = null;

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the advertisement space!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String addLeasingSpace() {
        boolean waterProv = Boolean.parseBoolean(waterProvision);
        boolean sta = infraAssetSessionBeanLocal.addLeasingSpace(assetName, code, unitNumber, floorArea, waterProv, rentalFee, leaseYear, leaseDescription, deadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leasing space is added successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.unitNumber = null;
            this.floorArea = 0;
            this.waterProvision = null;
            this.rentalFee = 0;
            this.leaseYear = 0;
            this.leaseDescription = null;
            this.deadline = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add the leasing space!", ""));
            return "leasingSpace";
        }
        return "leasingSpace";
    }

    public String updateLeasingSpace() {
        boolean waterProv = Boolean.parseBoolean(waterProvision);
        boolean sta = infraAssetSessionBeanLocal.updateLeasingSpace(assetId, assetName, code, unitNumber, floorArea, waterProv, rentalFee, leaseYear, leaseDescription, deadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leasing space is updated successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.unitNumber = null;
            this.floorArea = 0;
            this.waterProvision = null;
            this.rentalFee = 0;
            this.leaseYear = 0;
            this.leaseDescription = null;
            this.deadline = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the leasing space!", ""));
            return "leasingSpace";
        }
        return "leasingSpace";
    }

    public String updateLeasingSpace1() {
        boolean waterProv = Boolean.parseBoolean(waterProvision);
        boolean sta = infraAssetSessionBeanLocal.updateLeasingSpace(assetId, assetName, code, unitNumber, floorArea, waterProv, rentalFee, leaseYear, leaseDescription, deadline);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leasing space is updated successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.unitNumber = null;
            this.floorArea = 0;
            this.waterProvision = null;
            this.rentalFee = 0;
            this.leaseYear = 0;
            this.leaseDescription = null;
            this.deadline = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the leasing space!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String addRollingStockAsset() {
        boolean sta = infraAssetSessionBeanLocal.addRollingStockAsset(assetName, infraId, lifetimeValue, purchaseDate, qty, storage, rollingStockAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling stock asset is added successfully",
                            ""));
            this.assetName = null;
            this.infraId = null;
            this.lifetimeValue = 0;
            this.purchaseDate = null;
            this.rollingStockAssetType = null;
            this.qty = 0;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add the rolling stock asset!", ""));
            return "rollStockAsset";
        }
        return "rollStockAsset";
    }

    public String updateRollingStockAsset() {
        boolean sta = infraAssetSessionBeanLocal.updateRollingStockAsset(assetId, assetName, infraId, lifetimeValue, purchaseDate, qty, storage, rollingStockAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling stock asset is updated successfully",
                            ""));
            this.assetName = null;
            this.infraId = null;
            this.lifetimeValue = 0;
            this.purchaseDate = null;
            this.rollingStockAssetType = null;
            this.qty = 0;
            this.storage = 0;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the rolling stock asset!", ""));
            return "rollStockAsset";
        }
        return "rollStockAsset";
    }

    public String updateRollingStockAsset1() {
        boolean sta = infraAssetSessionBeanLocal.updateRollingStockAsset(assetId, assetName, infraId, lifetimeValue, purchaseDate, qty, storage, rollingStockAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling stock asset is updated successfully",
                            ""));
            this.assetName = null;
            this.infraId = null;
            this.lifetimeValue = 0;
            this.purchaseDate = null;
            this.rollingStockAssetType = null;
            this.qty = 0;
            this.storage = 0;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the rolling stock asset!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String addConsumableAsset() {
        boolean sta = infraAssetSessionBeanLocal.addConsumableAsset(assetName, code, qty, expiryDate, consumableAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Consumable asset is added successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.qty = 0;
            this.expiryDate = null;
            this.consumableAssetType = null;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add the consumable asset!", ""));
            return "consumAsset";
        }
        return "consumAsset";
    }

    public String updateConsumableAsset() {
        boolean sta = infraAssetSessionBeanLocal.updateConsumableAsset(assetId, assetName, code, qty, expiryDate, consumableAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Consumable asset is updated successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.qty = 0;
            this.expiryDate = null;
            this.consumableAssetType = null;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the consumable asset!", ""));
            return "consumAsset";
        }
        return "consumAsset";
    }

    public String updateConsumableAsset1() {
        boolean sta = infraAssetSessionBeanLocal.updateConsumableAsset(assetId, assetName, code, qty, expiryDate, consumableAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Consumable asset is updated successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.qty = 0;
            this.expiryDate = null;
            this.consumableAssetType = null;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the consumable asset!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String addNodeAsset() {
        boolean sta = infraAssetSessionBeanLocal.addNodeAsset(assetName, code, lifetimeValue, purchaseDate, qty, nodeAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Node asset is added successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.lifetimeValue = 0;
            this.purchaseDate = null;
            this.nodeAssetType = null;
            this.qty = 0;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to add the node asset!", ""));
            return "stationDepotAsset";
        }
        return "stationDepotAsset";
    }

    public String updateNodeAsset() {
        boolean sta = infraAssetSessionBeanLocal.updateNodeAsset(assetId, assetName, code, lifetimeValue, purchaseDate, qty, nodeAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Node asset is updated successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.lifetimeValue = 0;
            this.purchaseDate = null;
            this.nodeAssetType = null;
            this.qty = 0;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the node asset!", ""));
            return "stationDepotAsset";
        }
        return "stationDepotAsset";
    }

    public String updateNodeAsset1() {
        boolean sta = infraAssetSessionBeanLocal.updateNodeAsset(assetId, assetName, code, lifetimeValue, purchaseDate, qty, nodeAssetType);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Node asset is updated successfully",
                            ""));
            this.assetName = null;
            this.code = null;
            this.lifetimeValue = 0;
            this.purchaseDate = null;
            this.nodeAssetType = null;
            this.qty = 0;
            infraAssetSessionBeanLocal.generateAssetRequest();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to update the node asset!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String submitAssetRequest() {
        boolean sta = infraAssetSessionBeanLocal.submitAssetRequest(code, assetRequestType, assetType, assetName, qty, remark);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Asset request is submitted successfully",
                            ""));
            this.assetRequestType = null;
            this.assetName = null;
            this.qty = 0;
            this.remark = null;
            this.code = null;
            this.assetType = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to submit the asset request!"));
            return "assetRequest";
        }
        return "assetRequest";
    }

    public String updateAssetRequest() {
        boolean status1 = infraAssetSessionBeanLocal.updateAssetRequest(assetReqId, assetRequestType, qty, status);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Asset request is updated successfully",
                            ""));
            this.assetReqId = null;
            this.assetRequestType = null;
            this.assetName = null;
            this.qty = 0;
            this.remark = null;
            this.status = null;
            this.code = null;
            this.reqDate = null;
            this.assetType = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the asset request!"));
            return "assetRequest";
        }
        return "assetRequest";
    }

    public String updateAssetRequest1() {
        boolean status1 = infraAssetSessionBeanLocal.updateAssetRequest(assetReqId, assetRequestType, qty, status);
        if (status1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Asset request is updated successfully",
                            ""));
            this.assetReqId = null;
            this.assetRequestType = null;
            this.assetName = null;
            this.qty = 0;
            this.remark = null;
            this.status = null;
            this.code = null;
            this.reqDate = null;
            this.assetType = null;
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR
                            + "Fail to update the asset request!"));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String goEditAS(String assetId) {
        this.assetId = assetId;
        AdvertisementSpaceEntity temp = (AdvertisementSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        this.assetName = temp.getAssetName();
        if (temp.getAssetName().equals("Station")) {
            NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getInfrastructure().getInfraId());
            this.code = temp1.getCode();
        } else {
            this.code = temp.getInfrastructure().getInfraId();
        }
        this.location = temp.getLocation();
        this.peakPeriod = temp.getPeakPeriodFee();
        this.nonpeakPeriod = temp.getNonpeakPeriodFee();
        this.type = temp.getType();
        this.numberCode = temp.getNumberCode();
        this.deadline = temp.getDeadline();
        onLocationChange();
        onLocationChange2();
        onLocationChange1();
        onLocationChange3();
        return "editAdverSpace";
    }

    public String goEditAS1(String assetId) {
        this.assetId = assetId;
        AdvertisementSpaceEntity temp = (AdvertisementSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        this.assetName = temp.getAssetName();
        if (temp.getAssetName().equals("Station")) {
            NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getInfrastructure().getInfraId());
            this.code = temp1.getCode();
        } else {
            this.code = temp.getInfrastructure().getInfraId();
        }
        this.location = temp.getLocation();
        this.peakPeriod = temp.getPeakPeriodFee();
        this.nonpeakPeriod = temp.getNonpeakPeriodFee();
        this.type = temp.getType();
        this.numberCode = temp.getNumberCode();
        this.deadline = temp.getDeadline();
         onLocationChange();
        onLocationChange2();
        onLocationChange1();
        onLocationChange3();
        return "editAdverSpace1";
    }

    public String goDeleteAS(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delAsset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Advertising Space is deleted successfully",
                            ""));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the advertising space!", ""));
            return "advertSpace";
        }
        return "advertSpace";
    }

    public String goDeleteAS1(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delAsset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Advertising Space is deleted successfully",
                            ""));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the advertising space!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String goEditDP(String infraId) {
        this.infraId = infraId;
        DepotEntity temp = (DepotEntity) infraAssetSessionBeanLocal.searchInfra(infraId);
        this.code = temp.getCode();
        fixedTrainLine = code.substring(0, 3); //need to get particular route type
        this.code = code.substring(3);
        this.infraName = temp.getInfraName();
        this.infraAddress = temp.getAddress();
        this.previousStation = temp.getPreviousStation();
        this.nextStation = temp.getNextStation();
        this.distanceToFirstStation = temp.getDistanceToFirstStation();
        this.latitude = temp.getLatitude();
        this.longitude = temp.getLongitude();
        return "editDepot";
    }

    public String goDeleteDP(String infraId) {
        this.infraId = infraId;
        boolean sta = infraAssetSessionBeanLocal.deleteInfra(infraId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Depot is deleted successfully",
                            ""));
            init();

        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the depot!", ""));
            return "depot";
        }
        return "depot";
    }

    public String goEditST(String infraId) {
        this.infraId = infraId;
        StationEntity temp = (StationEntity) infraAssetSessionBeanLocal.searchInfra(infraId);
        this.code = temp.getCode();
        fixedTrainLine = code.substring(0, 3); //need to get particular route type
        this.code = code.substring(3);
        this.infraName = temp.getInfraName();
        this.infraAddress = temp.getAddress();
        this.previousStation = temp.getPreviousStation();
        this.nextStation = temp.getNextStation();
        this.distanceToFirstStation = temp.getDistanceToFirstStation();
        this.latitude = temp.getLatitude();
        this.longitude = temp.getLongitude();

        return "editStation";
    }

    public String goDeleteST(String infraId) {
        this.infraId = infraId;
        boolean sta = infraAssetSessionBeanLocal.deleteInfra(infraId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Station is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the station!", ""));
            return "station";
        }
        return "station";
    }

    public String goEditRS(String infraId) {
        this.infraId = infraId;
        RollingStockEntity temp = (RollingStockEntity) infraAssetSessionBeanLocal.searchInfra(infraId);
        this.infraName = temp.getInfraName();
        this.brand = temp.getBrand();
        this.rsStatus = temp.getStatus();
        this.selectedDepot = temp.getDepot().getCode();
        ArrayList<AssetEntity> assets = new ArrayList<AssetEntity>(temp.getAssets());
        ArrayList<TrainCarEntity> tce = new ArrayList<TrainCarEntity>();
        for (int i = 0; i < assets.size(); i++) {
            if (assets.get(i).getAssetId().substring(0, 2).equals("TC")) {
                TrainCarEntity tc = (TrainCarEntity) assets.get(i);
                tce.add(tc);
            }
        }
        this.trainCars = tce;
        return "editRollingStock";
    }

    public String goDeleteRS(String infraId) {
        this.infraId = infraId;
        boolean sta = infraAssetSessionBeanLocal.deleteRollingStock(infraId, staffId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling stock is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the rolling stock!", ""));
            return "rollingStock";
        }
        return "rollingStock";
    }

    public String goEditTR(String infraId) {
        this.infraId = infraId;
        TrackEntity temp = (TrackEntity) infraAssetSessionBeanLocal.searchInfra(infraId);
        this.infraName = temp.getInfraName();
        return "editTrack";
    }

    public String goDeleteTR(String infraId) {
        this.infraId = infraId;
        boolean sta = infraAssetSessionBeanLocal.deleteInfra(infraId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Track is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the track!", ""));
            return "track";
        }
        return "track";
    }

    public String goAddTC() {
        //generate train car code
        if (trainCars.isEmpty()) {
            this.carCode = 1;
        } else {
            this.carCode = trainCars.get(trainCars.size() - 1).getCarCode() + 1;
        }
        return "addTrainCar";
    }

    public String goEditLS(String assetId) {
        this.assetId = assetId;
        LeasingSpaceEntity temp = (LeasingSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        NodeEntity node = (NodeEntity) infraAssetSessionBeanLocal.searchNode1(temp.getInfrastructure().getInfraId());
        this.code = node.getCode();
        this.unitNumber = temp.getUnitNumber();
        this.assetName = temp.getAssetName();
        this.floorArea = temp.getFloorArea();
        this.waterProvision = String.valueOf(temp.isWaterProvision());
        this.rentalFee = temp.getRentalFee();
        this.leaseYear = temp.getLeaseYear();
        this.leaseDescription = temp.getLeaseDescription();
        this.deadline = temp.getDeadline();
        if (assetName.equals("Food and Beverage")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Yes", "true");
            selections.put("Food and Beverage", map);
            this.selection = selections.get(assetName);

        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Yes", "true");
            map.put("No", "false");
            selections.put("Retail", map);
            selections.put("Commerical", map);
            this.selection = selections.get(assetName);
        }
        return "editLeaseSpace";
    }

    public String goEditLS1(String assetId) {
        this.assetId = assetId;
        LeasingSpaceEntity temp = (LeasingSpaceEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        NodeEntity node = (NodeEntity) infraAssetSessionBeanLocal.searchNode1(temp.getInfrastructure().getInfraId());
        this.code = node.getCode();
        this.unitNumber = temp.getUnitNumber();
        this.assetName = temp.getAssetName();
        this.floorArea = temp.getFloorArea();
        this.waterProvision = String.valueOf(temp.isWaterProvision());
        this.waterProvision = String.valueOf(temp.isWaterProvision());
        this.rentalFee = temp.getRentalFee();
        this.leaseYear = temp.getLeaseYear();
        this.leaseDescription = temp.getLeaseDescription();
        this.deadline = temp.getDeadline();
        return "editLeaseSpace1";
    }

    public String goDeleteLS(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delAsset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leasing Space is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the leasing space!", ""));
            return "leasingSpace";
        }
        return "leasingSpace";
    }

    public String goDeleteLS1(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delAsset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Leasing Space is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the leasing space!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String goEditRSA(String assetId) {
        this.assetId = assetId;
        RollingStockAssetEntity temp = (RollingStockAssetEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        this.infraId = temp.getInfrastructure().getInfraId();
        this.assetName = temp.getAssetName();
        this.lifetimeValue = temp.getLifetimeValue();
        this.purchaseDate = temp.getPurchaseDate();
        this.rollingStockAssetType = temp.getRollingStockAssetType();
        this.qty = temp.getQuantity();
        this.remark = temp.getRemarks();
        this.storage = temp.getStorage();
        return "editRollStockAsset";
    }

    public String goEditRSA1(String assetId) {
        this.assetId = assetId;
        RollingStockAssetEntity temp = (RollingStockAssetEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        this.infraId = temp.getInfrastructure().getInfraId();
        this.assetName = temp.getAssetName();
        this.lifetimeValue = temp.getLifetimeValue();
        this.purchaseDate = temp.getPurchaseDate();
        this.rollingStockAssetType = temp.getRollingStockAssetType();
        this.qty = temp.getQuantity();
        this.storage = temp.getStorage();
        return "editRollStockAsset1";
    }

    public String goDeleteRSA(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delCAasset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling Stock Asset is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the Rolling Stock Asset!", ""));
            return "rollStockAsset";
        }
        return "rollStockAsset";
    }

    public String goDeleteRSA1(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delCAasset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Rolling Stock Asset is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the Rolling Stock Asset!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String goEditNA(String assetId) {
        this.assetId = assetId;
        NodeAssetEntity temp = (NodeAssetEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getInfrastructure().getInfraId());
        this.code = temp1.getCode();
        this.assetName = temp.getAssetName();
        this.lifetimeValue = temp.getLifetimeValue();
        this.purchaseDate = temp.getPurchaseDate();
        this.nodeAssetType = temp.getNodeAssetType();
        this.qty = temp.getQuantity();
        this.remark = temp.getRemarks();
        return "editNodeAsset";
    }

    public String goEditNA1(String assetId) {
        this.assetId = assetId;
        NodeAssetEntity temp = (NodeAssetEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getInfrastructure().getInfraId());
        this.code = temp1.getCode();
        this.assetName = temp.getAssetName();
        this.lifetimeValue = temp.getLifetimeValue();
        this.purchaseDate = temp.getPurchaseDate();
        this.nodeAssetType = temp.getNodeAssetType();
        this.qty = temp.getQuantity();
        return "editNodeAsset1";
    }

    public String goDeleteNA(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delCAasset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Station / Depot Asset is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the Station / Depot Asset!", ""));
            return "stationDepotAsset";
        }
        return "stationDepotAsset";
    }

    public String goDeleteNA1(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delCAasset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Station / Depot Asset is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the Station / Depot Asset!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String goEditCA(String assetId) {
        this.assetId = assetId;
        ConsumableAssetEntity temp = (ConsumableAssetEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getInfrastructure().getInfraId());
        this.code = temp1.getCode();
        this.assetName = temp.getAssetName();
        this.qty = temp.getQuantity();
        this.expiryDate = temp.getExpiryDate();
        this.consumableAssetType = temp.getConsumableAssetType();
        return "editConsumAsset";
    }

    public String goEditCA1(String assetId) {
        this.assetId = assetId;
        ConsumableAssetEntity temp = (ConsumableAssetEntity) infraAssetSessionBeanLocal.searchAsset(assetId);
        NodeEntity temp1 = infraAssetSessionBeanLocal.searchStation(temp.getInfrastructure().getInfraId());
        this.code = temp1.getCode();
        this.assetName = temp.getAssetName();
        this.qty = temp.getQuantity();
        this.expiryDate = temp.getExpiryDate();
        this.consumableAssetType = temp.getConsumableAssetType();
        return "editConsumAsset1";
    }

    public String goDeleteCA(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delCAasset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Consumable Asset is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the Consumable Asset!", ""));
            return "consumAsset";
        }
        return "consumAsset";
    }

    public String goDeleteCA1(String assetId) {
        this.assetId = assetId;
        boolean sta = infraAssetSessionBeanLocal.delCAasset(assetId);
        if (sta) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Consumable Asset is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the Consumable Asset!", ""));
            return "assetManagement";
        }
        return "assetManagement";
    }

    public String goUpdateAR(String assetReqId) {
        this.assetReqId = assetReqId;
        AssetRequestEntity temp = (AssetRequestEntity) infraAssetSessionBeanLocal.searchAssetReq(assetReqId);
        this.assetName = temp.getAssetName();
        this.assetRequestType = temp.getAssetRequestType();
        this.qty = temp.getQty();
        this.status = temp.getStatus();
        this.remark = temp.getRemark();
        this.reqDate = temp.getReqDate();
        String type1 = temp.getAsset().getAssetId().substring(0, 2);
        if (type1.equals("RS")) {
            String id = temp.getAsset().getInfrastructure().getInfraId();
            RollingStockEntity rollingStock = infraAssetSessionBeanLocal.searchRollingStock(id);
            String codee = rollingStock.getDepot().getCode();
            codee = codee + "(" + rollingStock.getInfraId() + ")";
            this.code = codee;
        } else {
            NodeEntity node = (NodeEntity) infraAssetSessionBeanLocal.searchNode1(temp.getAsset().getInfrastructure().getInfraId());
            this.code = node.getCode();
        }
        return "updateAssetReq";
    }

    public String goUpdateAR1(String assetReqId) {
        this.assetReqId = assetReqId;
        AssetRequestEntity temp = (AssetRequestEntity) infraAssetSessionBeanLocal.searchAssetReq(assetReqId);
        this.assetName = temp.getAssetName();
        this.assetRequestType = temp.getAssetRequestType();
        this.qty = temp.getQty();
        this.status = temp.getStatus();
        this.remark = temp.getRemark();
        this.reqDate = temp.getReqDate();
        String type1 = temp.getAsset().getAssetId().substring(0, 2);
        if (type1.equals("RS")) {
            String id = temp.getAsset().getInfrastructure().getInfraId();
            RollingStockEntity rollingStock = infraAssetSessionBeanLocal.searchRollingStock(id);
            String codee = rollingStock.getDepot().getCode();
            codee = codee + "(" + rollingStock.getInfraId() + ")";
            this.code = codee;
        } else {
            NodeEntity node = (NodeEntity) infraAssetSessionBeanLocal.searchNode1(temp.getAsset().getInfrastructure().getInfraId());
            this.code = node.getCode();
        }
        return "updateAssetReq1";
    }

    public String goEditTC(String assetId) {
        this.assetId = assetId;
        TrainCarEntity temp = (TrainCarEntity) infraAssetSessionBeanLocal.searchCar(carCode);
        this.carCode = temp.getCarCode();
        this.brand = temp.getBrand();
        this.type = temp.getType();
        this.description = temp.getDescription();
        this.carStatus = temp.getStatus();
        InfrastructureEntity i = temp.getInfrastructure();
        DepotEntity depot = (DepotEntity) i;
        this.storageLoc = depot.getCode();
        return "editTrainCar";
    }

    public String goDeleteTC(int carCode) {
        this.carCode = carCode;
        boolean status = infraAssetSessionBeanLocal.deleteTrainCar(carCode);
        if (status) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Train car is deleted successfully",
                            ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to delete the train car!", ""));
        }
        return "trainCar";
    }

    public List<AssetRequestEntity> getFilteredAssetRequestList() {
        return filteredAssetRequestList;
    }

    public void setFilteredAssetRequestList(List<AssetRequestEntity> filteredAssetRequestList) {
        this.filteredAssetRequestList = filteredAssetRequestList;
    }

    public String detail(String assetRequestId) {
        AssetRequestEntity a = infraAssetSessionBeanLocal.searchAssetReq(assetRequestId);
        String id = a.getAsset().getInfrastructure().getInfraId();
        RollingStockEntity rollingStock = infraAssetSessionBeanLocal.searchRollingStock(id);
        String codee = rollingStock.getDepot().getCode();
        codee = codee + "(" + rollingStock.getInfraId() + ")";
        return codee;
    }

    public String detail1(String assetRequestId) {
        AssetRequestEntity a = infraAssetSessionBeanLocal.searchAssetReq(assetRequestId);
        String id = a.getAsset().getInfrastructure().getInfraId();
        NodeEntity node = infraAssetSessionBeanLocal.searchNode1(id);
        String codee = node.getCode();
        return codee;
    }

}
