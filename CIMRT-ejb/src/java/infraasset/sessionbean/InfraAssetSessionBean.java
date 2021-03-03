package infraasset.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import commoninfra.entity.TeamEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.StationEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.entity.TrackEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.TrainCarEntity;
import java.sql.Timestamp;
import routefare.entity.RouteEntity;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import routefare.entity.NodeEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import maintenance.entity.MaintenanceReportEntity;
import maintenance.sessionbean.MaintenanceSessionBeanLocal;
import operations.entity.MaintenanceRequestEntity;
import operations.sessionbean.ReportSessionBeanLocal;

/**
 *
 * @author Zhirong
 */
@Stateful

public class InfraAssetSessionBean implements InfraAssetSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private DepotEntity depotEntity;
    private StationEntity stationEntity;
    private RollingStockEntity rollingStockEntity;
    private TrackEntity trackEntity;
    private AdvertisementSpaceEntity advertSpace;
    private LeasingSpaceEntity leaseSpace;
    private RollingStockAssetEntity rollingStockAsset;
    private ConsumableAssetEntity consumableAsset;
    private NodeAssetEntity nodeAsset;
    private AssetRequestEntity assetRequest;
    private TrainCarEntity trainCarEntity;
    ArrayList<AssetEntity> assets;
    ArrayList<NodeEntity> nodes;
    ArrayList<TrainCarEntity> trainCars;

    @EJB
    private ReportSessionBeanLocal reportSessionBeanLocal;

    @EJB
    private MaintenanceSessionBeanLocal maintenanceSessionBeanLocal;

    public InfraAssetSessionBean() {

    }

    @Override
    public boolean addDepot(String trainLine, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation, double latitude, double longitude) {
        try {
            String depotCode = trainLine + code;
            InfrastructureEntity temp = searchNode(depotCode);
            if (em.contains(temp)) {
                return false;
            } else {
                Query q = em.createQuery("SELECT i FROM InfrastructureEntity i");
                int spaceCount = q.getResultList().size();
                String infraId = "IN" + (spaceCount + 1);
                boolean status = true;
                while (status) {
                    Query q1 = em.createQuery("SELECT i1 FROM InfrastructureEntity i1 WHERE i1.infraId=:infraId");
                    q1.setParameter("infraId", infraId);
                    if (q1.getResultList().isEmpty()) {
                        break;
                    }
                    spaceCount = spaceCount + 2;
                    infraId = "IN" + spaceCount;
                }
                depotEntity = new DepotEntity(infraId, depotCode, infraName, address, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
                em.persist(depotEntity);

                String routeType = depotCode.substring(0, 3);
                Set<RouteEntity> routeSet = new HashSet<RouteEntity>();
                Query query = em.createQuery("SELECT r FROM RouteEntity r");
                for (Object o : query.getResultList()) {
                    routeSet.add((RouteEntity) o);
                }

                Iterator<RouteEntity> route = routeSet.iterator();
                while (route.hasNext()) {
                    RouteEntity r = route.next();
                    if (r.getCode().substring(0, 3).equals(routeType)) {

                        nodes = new ArrayList<NodeEntity>(r.getNodes());
                        nodes.add(depotEntity);
                        r.setNodes(nodes);
                    }
                }
                return true;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean addStation(String trainLine, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation, double latitude, double longitude) {
        try {
            String stationCode = trainLine + code;
            InfrastructureEntity temp = searchNode(stationCode);
            if (em.contains(temp)) {
                return false;
            } else {
                Query q = em.createQuery("SELECT i FROM InfrastructureEntity i");
                int spaceCount = q.getResultList().size();
                String infraId = "IN" + (spaceCount + 1);
                boolean status = true;
                while (status) {
                    Query q1 = em.createQuery("SELECT i1 FROM InfrastructureEntity i1 WHERE i1.infraId=:infraId");
                    q1.setParameter("infraId", infraId);
                    if (q1.getResultList().isEmpty()) {
                        break;
                    }
                    spaceCount = spaceCount + 2;
                    infraId = "IN" + spaceCount;
                }

                stationEntity = new StationEntity(infraId, stationCode, infraName, address, previousStation, nextStation, distanceToFirstStation, latitude, longitude);
                em.persist(stationEntity);

                String routeType = stationCode.substring(0, 3);
                ArrayList<RouteEntity> routeSet = new ArrayList<RouteEntity>();
                Query query = em.createQuery("SELECT r FROM RouteEntity r");
                for (Object o : query.getResultList()) {
                    routeSet.add((RouteEntity) o);
                }

                Iterator<RouteEntity> route = routeSet.iterator();
                while (route.hasNext()) {
                    RouteEntity r = route.next();
                    if (r.getCode().substring(0, 3).equals(routeType)) {
                        nodes = new ArrayList<NodeEntity>(r.getNodes());
                        nodes.add(stationEntity);
                        r.setNodes(nodes);
                    }
                }
                return true;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean addTrainCar(int carCode, String brand, String location, String type, String description) {
        try {
            AssetEntity temp = searchCar(carCode);
            if (em.contains(temp)) {
                return false;
            } else {
                Query q = em.createQuery("SELECT a FROM AssetEntity a");
                int spaceCount = q.getResultList().size();
                String assetId = "TC" + (spaceCount + 1);
                while (true) {
                    Query q1 = em.createQuery("SELECT a1 FROM AssetEntity a1 WHERE a1.assetId=:assetId");
                    q1.setParameter("assetId", assetId);
                    if (q1.getResultList().isEmpty()) {
                        break;
                    }
                    spaceCount = spaceCount + 2;
                    assetId = "TC" + spaceCount;
                }

                trainCarEntity = new TrainCarEntity(assetId, "Train Car", carCode, brand, "Available", type, description);

                InfrastructureEntity infra = searchNode(location);
                if (em.contains(infra)) {
                    if (infra instanceof NodeEntity) {
                        trainCarEntity.setInfrastructure(infra);
                        em.persist(trainCarEntity);
                        infra.getAssets().add(trainCarEntity);
                        em.flush();
                        return true;
                    }
                } else {
                    return false;
                }

                /*ArrayList<InfrastructureEntity> infraList = new ArrayList<InfrastructureEntity>();
                Query q1 = em.createQuery("SELECT i FROM InfrastructureEntity i");
                for (Object o : q1.getResultList()) {
                    infraList.add((DepotEntity) o);
                }

                for (int j = 0; j < infraList.size(); j++) {
                     if (infraList.get(j).get().equals(location)) {
                        //ArrayList<TrainCarEntity> trainCars = new ArrayList<TrainCarEntity>(depotList.get(j).getTrainCars());
                        trainCars.add(trainCarEntity);
                        depotList.get(j).setTrainCars(trainCars);
                        trainCarEntity.setDepot(depotList.get(j));
                    }
                }*/
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean addRollingStock(String infraName, String brand, String selectedDepot) {

        Query q = em.createQuery("SELECT i FROM InfrastructureEntity i");
        int spaceCount = q.getResultList().size();
        String infraId = "IN" + (spaceCount + 1);
        while (true) {
            Query q1 = em.createQuery("SELECT i1 FROM InfrastructureEntity i1 WHERE i1.infraId=:infraId");
            q1.setParameter("infraId", infraId);
            if (q1.getResultList().isEmpty()) {
                break;
            }
            spaceCount = spaceCount + 2;
            infraId = "IN" + spaceCount;
        }

        //Get train cars to form a rolling stock
        ArrayList<TrainCarEntity> availableDrivingTrailers = new ArrayList<TrainCarEntity>();
        ArrayList<TrainCarEntity> availableMotorCars = new ArrayList<TrainCarEntity>();
        Query query = em.createQuery("SELECT t FROM TrainCarEntity t WHERE t.brand=:brand AND t.status='Available' AND t.type='Driving Trailer'");
        query.setParameter("brand", brand);

        Query query1 = em.createQuery("SELECT t FROM TrainCarEntity t WHERE t.brand=:brand AND t.status='Available' AND t.type='Motor Car'");
        query1.setParameter("brand", brand);

        availableDrivingTrailers = new ArrayList<TrainCarEntity>(query.getResultList());
        availableMotorCars = new ArrayList<TrainCarEntity>(query1.getResultList());

        if (availableDrivingTrailers.size() < 2 || availableMotorCars.size() < 4) {
            return false;
        }

        rollingStockEntity = new RollingStockEntity(infraId, infraName, brand, "Available");

        //Map to Depot
        Query q1 = em.createQuery("SELECT d FROM DepotEntity d WHERE d.code=:code");
        q1.setParameter("code", selectedDepot);

        DepotEntity depot = (DepotEntity) q1.getResultList().get(0);
        depot.getRollingStocks().add(rollingStockEntity);
        rollingStockEntity.setDepot(depot);

        for (int i = 0; i < 2; i++) {
            availableDrivingTrailers.get(i).setInfrastructure(rollingStockEntity);
            rollingStockEntity.getAssets().add(availableDrivingTrailers.get(i));
            availableDrivingTrailers.get(i).setStatus("Attached");
        }

        for (int i = 0; i < 4; i++) {
            availableMotorCars.get(i).setInfrastructure(rollingStockEntity);
            rollingStockEntity.getAssets().add(availableMotorCars.get(i));
            availableMotorCars.get(i).setStatus("Attached");
        }

        em.persist(rollingStockEntity);

        return true;
    }

    @Override
    public boolean addTrack(String trainLine, String infraName) {
        Query q = em.createQuery("SELECT i FROM InfrastructureEntity i");
        int spaceCount = q.getResultList().size();
        String infraId = "IN" + (spaceCount + 1);
        while (true) {
            Query q1 = em.createQuery("SELECT i1 FROM InfrastructureEntity i1 WHERE i1.infraId=:infraId");
            q1.setParameter("infraId", infraId);
            if (q1.getResultList().isEmpty()) {
                break;
            }
            spaceCount = spaceCount + 2;
            infraId = "IN" + spaceCount;
        }

        trackEntity = new TrackEntity(infraId, infraName, trainLine, null);
        em.persist(trackEntity);
        return true;
    }

    @Override
    public boolean updateDepot(String infraId, String fixedTrainLine,
            String code,
            String infraName,
            String address, String previousStation,
            String nextStation, double distanceToFirstStation, double latitude, double longitude
    ) {

        InfrastructureEntity i;
        Query q = em.createQuery("SELECT i FROM InfrastructureEntity AS i WHERE i.infraId=:infraId");
        q.setParameter("infraId", infraId);

        if (q.getResultList().isEmpty()) {
            return false;
        } else {
            DepotEntity depot = (DepotEntity) q.getResultList().get(0);
            String depotCode = fixedTrainLine + code;
            depot.setDistanceToFirstStation(distanceToFirstStation);
            depot.setAddress(address);
            depot.setCode(depotCode);
            depot.setNextStation(nextStation);
            depot.setInfraName(infraName);
            depot.setLatitude(latitude);
            depot.setLongitude(longitude);
            em.flush();
            return true;
        }

    }

    @Override
    public boolean updateStation(String infraId, String fixedTrainLine,
            String code,
            String infraName,
            String address,
            String previousStation,
            String nextStation,
            double distanceToFirstStation, double latitude, double longitude
    ) {

        InfrastructureEntity i;
        Query q = em.createQuery("SELECT i FROM InfrastructureEntity AS i WHERE i.infraId=:infraId");
        q.setParameter("infraId", infraId);
        if (q.getResultList().isEmpty()) {
            return false;
        } else {
            StationEntity station = (StationEntity) q.getResultList().get(0);
            String stationCode = fixedTrainLine + code;
            station.setPreviousStation(previousStation);
            station.setDistanceToFirstStation(distanceToFirstStation);
            station.setAddress(address);
            station.setCode(stationCode);
            station.setNextStation(nextStation);
            station.setInfraName(infraName);
            station.setLatitude(latitude);
            station.setLongitude(longitude);
            em.flush();

            return true;
        }
    }

    @Override
    public boolean updateRollingStock(String infraId, String rsStatus,
            List<String> selectedTrainCar, String staffId,
            String selectedDepot
    ) {
        Date date = new Date();
        RollingStockEntity rollingStock = (RollingStockEntity) searchInfra(infraId);
        if (em.contains(rollingStock)) {
            rollingStock.setStatus(rsStatus);
            if (!rollingStock.getDepot().getCode().equals(selectedDepot)) {
                ArrayList<DepotEntity> depotList = new ArrayList<DepotEntity>();
                Query q1 = em.createQuery("SELECT d FROM DepotEntity d");
                for (Object o : q1.getResultList()) {
                    depotList.add((DepotEntity) o);
                }

                for (int j = 0; j < depotList.size(); j++) {
                    if (depotList.get(j).getCode().equals(selectedDepot)) {
                        ArrayList<RollingStockEntity> rollingStocks = new ArrayList<RollingStockEntity>(depotList.get(j).getRollingStocks());
                        rollingStocks.add(rollingStock);
                        depotList.get(j).setRollingStocks(rollingStocks);

                        DepotEntity depot = rollingStock.getDepot();
                        ArrayList<RollingStockEntity> depotRSs = new ArrayList<RollingStockEntity>(depot.getRollingStocks());
                        for (int k = 0; k < depotRSs.size(); k++) {
                            if (depotRSs.get(k).getInfraId().equals(rollingStock.getInfraId())) {
                                depotRSs.remove(k);
                                depot.setRollingStocks(rollingStocks);
                                break;
                            }
                        }
                        rollingStock.setDepot(depotList.get(j));
                    }
                }
            }

            if (rollingStock.getStatus().equals("Under Maintenance") && selectedTrainCar.isEmpty()) {
            } else if (rollingStock.getStatus().equals("Under Maintenance") && !selectedTrainCar.isEmpty()) {
                for (int i = 0; i < selectedTrainCar.size(); i++) {
                    String faulty = "Train Car " + selectedTrainCar.get(i) + " ";
                    TrainCarEntity tc = searchCar(Integer.parseInt(selectedTrainCar.get(i)));
                    tc.setStatus("Under Maintenance");
                    MaintenanceRequestEntity maintenanceStatus = reportSessionBeanLocal.submitMaintenanceRequest(date, faulty, rollingStock.getInfraId(), staffId, selectedTrainCar.get(i));
                    if (maintenanceStatus != null) {
                        tc.getMaintenanceRequests().add(maintenanceStatus);
                    }
                }
                /*  MaintenanceRequestEntity maintenanceStatus = reportSessionBeanLocal.submitMaintenanceRequest(date, faulty, infraId, staffId);
                if (maintenanceStatus != null) {
                    rollingStock.getMaintenanceRequests().add(maintenanceStatus);
                }*/
            }
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateTrack(String infraId, String infraName
    ) {
        TrackEntity track = (TrackEntity) searchInfra(infraId);
        if (em.contains(track)) {
            track.setInfraName(infraName);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateTrainCar(String assetId, String description,
            String status, String location
    ) {
        AssetEntity a;
        Query q = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
        q.setParameter("assetId", assetId);
        if (q.getResultList().isEmpty()) {
            return false;
        } else {
            TrainCarEntity car = (TrainCarEntity) q.getResultList().get(0);
            car.setDescription(description);
            car.setStatus(status);
            InfrastructureEntity infra = car.getInfrastructure();
            NodeEntity depot = searchNode(infra.getInfraId());
            if (!depot.getCode().equals(location)) {
                ArrayList<DepotEntity> depotList = new ArrayList<DepotEntity>();
                Query q1 = em.createQuery("SELECT d FROM DepotEntity d");
                for (Object o : q1.getResultList()) {
                    depotList.add((DepotEntity) o);
                }

                for (int j = 0; j < depotList.size(); j++) {
                    if (depotList.get(j).getCode().equals(location)) {
                        InfrastructureEntity newDepot = depotList.get(j);
                        assets = new ArrayList<AssetEntity>(newDepot.getAssets());
                        assets.add(car);
                        newDepot.setAssets(assets);
                    }

                    ArrayList<AssetEntity> oldDepotAssets = new ArrayList<AssetEntity>(infra.getAssets());
                    for (int k = 0; k < oldDepotAssets.size(); k++) {
                        if (oldDepotAssets.get(k).getAssetId().equals(car.getAssetId())) {
                            oldDepotAssets.remove(k);
                            infra.setAssets(oldDepotAssets);
                            break;
                        }
                    }
                    car.setInfrastructure(depotList.get(j));
                }
            }
        }
        em.flush();
        return true;
    }

    @Override
    public ArrayList<DepotEntity> getDepot() {
        ArrayList<DepotEntity> depotList = new ArrayList<DepotEntity>();
        Query q = em.createQuery("SELECT d FROM DepotEntity AS d");
        for (Object o : q.getResultList()) {
            depotList.add((DepotEntity) o);
        }
        return depotList;
    }

    @Override
    public ArrayList<StationEntity> getStation() {
        ArrayList<StationEntity> stationList = new ArrayList<StationEntity>();
        Query q = em.createQuery("SELECT s FROM StationEntity AS s");
        for (Object o : q.getResultList()) {
            stationList.add((StationEntity) o);
        }
        return stationList;
    }

    @Override
    public ArrayList<RollingStockEntity> getRollingStock() {
        ArrayList<RollingStockEntity> rollingStockList = new ArrayList<RollingStockEntity>();
        Query q = em.createQuery("SELECT r FROM RollingStockEntity AS r");
        for (Object o : q.getResultList()) {
            rollingStockList.add((RollingStockEntity) o);
        }
        return rollingStockList;
    }

    @Override
    public ArrayList<TrackEntity> getTrack() {
        ArrayList<TrackEntity> trackList = new ArrayList<TrackEntity>();
        Query q = em.createQuery("SELECT t FROM TrackEntity AS t");
        for (Object o : q.getResultList()) {
            trackList.add((TrackEntity) o);
        }
        return trackList;
    }

    @Override
    public ArrayList<TrainCarEntity> getTrainCars() {
        ArrayList<TrainCarEntity> trainCarList = new ArrayList<TrainCarEntity>();
        Query q = em.createQuery("SELECT tc FROM TrainCarEntity AS tc");
        for (Object o : q.getResultList()) {
            trainCarList.add((TrainCarEntity) o);
        }
        return trainCarList;
    }

    @Override
    public ArrayList<String> getTrainCarList(String code
    ) {
        ArrayList<String> carlist = new ArrayList<String>();
        ArrayList<TrainCarEntity> trainCarList = new ArrayList<TrainCarEntity>();
        trainCarList = getTrainCars();
        //System.out.println(trainCarList.get(2).getCarCode());
        for (int i = 0; i < trainCarList.size(); i++) {
            if (trainCarList.get(i).getInfrastructure() != null) {
                if (trainCarList.get(i).getInfrastructure().getInfraId().equals(code)) {
                    carlist.add(Integer.toString(trainCarList.get(i).getCarCode()));
                }
            }
        }
        return carlist;
    }

    @Override
    public boolean deleteInfra(String infraId
    ) {
        InfrastructureEntity temp = searchInfra(infraId);
        if (em.contains(temp)) {
            em.remove(temp);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTrainCar(int carCode
    ) {
        boolean status = true;
        TrainCarEntity tc = searchCar(carCode);
        AssetEntity temp = searchAsset(tc.getAssetId());
        if (em.contains(tc)) {
            if (tc.getStatus().equals("Available") || tc.getStatus().equals("Spoiled")) {
                ArrayList<DepotEntity> depotList = new ArrayList<DepotEntity>();
                Query q1 = em.createQuery("SELECT d FROM DepotEntity d");
                for (Object o : q1.getResultList()) {
                    depotList.add((DepotEntity) o);
                }

                InfrastructureEntity infra = tc.getInfrastructure();
                NodeEntity attachedDepot = searchNode(infra.getInfraId());

                for (int j = 0; j < depotList.size(); j++) {
                    if (depotList.get(j).getCode().equals(attachedDepot.getCode())) {
                        assets = new ArrayList<AssetEntity>(infra.getAssets());
                        assets.remove(tc);
                        infra.setAssets(assets);
                        temp.setInfrastructure(null);
                        break;
                    }
                }
                em.remove(temp);
                em.flush();
            } else {
                status = false;
            }
        } else {
            status = false;
        }
        return status;
    }

    @Override
    public boolean deleteRollingStock(String infraId, String staffId
    ) {
        RollingStockEntity rs = searchRollingStock(infraId);
        InfrastructureEntity temp = searchInfra(rs.getInfraId());
        Date date = new Date();
        if (em.contains(rs)) {
            assets = new ArrayList(temp.getAssets());
            for (int i = 0; i < assets.size(); i++) {
                if (assets.get(i).getAssetId().substring(0, 2).equals("TC")) {
                    TrainCarEntity trainCar = (TrainCarEntity) assets.get(i);
                    trainCar.setStatus("To Be Inspected");
                    trainCar.setInfrastructure(rs.getDepot());
                    String faulty = "Car " + trainCar.getCarCode() + " ";

                    MaintenanceRequestEntity maintenanceStatus = reportSessionBeanLocal.submitMaintenanceRequest(date, faulty, "-", staffId, trainCar.getCarCode() + "");
                    if (maintenanceStatus != null) {
                        List<MaintenanceRequestEntity> mqList = assets.get(i).getMaintenanceRequests();
                        mqList.add(maintenanceStatus);
                        assets.get(i).setMaintenanceRequests(mqList);
                    }
                } else {
                    assets.get(i).setInfrastructure(rs.getDepot());
                }
            }
            rs.setAssets(null);

            ArrayList<DepotEntity> depotList = new ArrayList<DepotEntity>();
            Query q1 = em.createQuery("SELECT d FROM DepotEntity d");
            for (Object o : q1.getResultList()) {
                depotList.add((DepotEntity) o);
            }

            for (int j = 0; j < depotList.size(); j++) {
                if (depotList.get(j).getCode().equals(rs.getDepot().getCode())) {
                    ArrayList<RollingStockEntity> rollingStocks = new ArrayList<RollingStockEntity>(depotList.get(j).getRollingStocks());
                    rollingStocks.remove(rs);
                    depotList.get(j).setRollingStocks(rollingStocks);
                    rs.setDepot(null);
                    break;
                }
            }
            em.remove(temp);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAdvertisementSpace(String assetName, String code,
            String location, String type,
            double peakPeriod, double nonpeakPeriod, String numberCode, Date deadline
    ) {
        try {
             Timestamp date = new Timestamp(deadline.getTime());
            ArrayList<AdvertisementSpaceEntity> advertList = getAdvertisementSpace();
            if (assetName.equals("RollingStock")) {
                for (int j = 0; j < advertList.size(); j++) {
                    AdvertisementSpaceEntity advert = advertList.get(j);
                    if (advert.getLocation().equals(location) && advert.getType().equals(type) && advert.getNumberCode().equals(numberCode) && advert.getInfrastructure().getInfraId().equals(code)) {
                        return false;
                    }
                }
            } else {
                NodeEntity node = searchNode(code);
                for (int j = 0; j < advertList.size(); j++) {
                    AdvertisementSpaceEntity advert = advertList.get(j);
                    if (advert.getLocation().equals(location) && advert.getType().equals(type) && advert.getNumberCode().equals(numberCode) && advert.getInfrastructure().getInfraId().equals(node.getInfraId())) {
                        return false;
                    }
                }
            }

            Query q = em.createQuery("SELECT a FROM AdvertisementSpaceEntity AS a");
            int rows = q.getResultList().size();
            String assetId = "AS" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT a1 FROM AdvertisementSpaceEntity AS a1 WHERE a1.assetId=:assetId");
                q1.setParameter("assetId", assetId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                assetId = "AS" + rows;
            }

            advertSpace = new AdvertisementSpaceEntity(assetId, assetName, location, type, peakPeriod, nonpeakPeriod, "Available", date, numberCode);
            if (assetName.equals("RollingStock")) {
                InfrastructureEntity infra = searchInfra(code);
                if (em.contains(infra)) {
                    advertSpace.setInfrastructure(infra);
                    em.persist(advertSpace);
                    infra.getAssets().add(advertSpace);
                    em.flush();
                    return true;
                }
            } else {
                InfrastructureEntity infra = searchNode(code);
                if (em.contains(infra)) {
                    advertSpace.setInfrastructure(infra);
                    em.persist(advertSpace);
                    infra.getAssets().add(advertSpace);
                    em.flush();
                    return true;
                }
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean addLeasingSpace(String assetName, String code,
            String unitNumber, double floorArea, boolean waterProvision, double rentalFee, int leaseYear, String leaseDescription,
            Date deadline
    ) {
        try {
            Timestamp date = new Timestamp(deadline.getTime());
            Query q = em.createQuery("SELECT l FROM LeasingSpaceEntity AS l");
            int rows = q.getResultList().size();
            String assetId = "LS" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT l1 FROM LeasingSpaceEntity AS l1 WHERE l1.assetId=:assetId");
                q1.setParameter("assetId", assetId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                assetId = "LS" + rows;
            }
            leaseSpace = new LeasingSpaceEntity(assetId, assetName, unitNumber, floorArea, waterProvision, rentalFee, leaseYear, leaseDescription, "Available", date);
            InfrastructureEntity infra = searchNode(code);
            if (em.contains(infra)) {
                leaseSpace.setInfrastructure(infra);
                em.persist(leaseSpace);
                infra.getAssets().add(leaseSpace);
                em.flush();
                return true;
            } else {
                return false;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean addRollingStockAsset(String assetName, String infraId,
            int lifetimeValue, Date purchaseDate,
            int quantity, int storage, String rollingStockAssetType
    ) {
        try {
            Query q = em.createQuery("SELECT r FROM RollingStockAssetEntity AS r");
            int rows = q.getResultList().size();
            String assetId = "RSA" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT r1 FROM RollingStockAssetEntity AS r1 WHERE r1.assetId=:assetId");
                q1.setParameter("assetId", assetId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                assetId = "RSA" + rows;
            }
            rollingStockAsset = new RollingStockAssetEntity(assetId, assetName, lifetimeValue, purchaseDate, quantity, storage, rollingStockAssetType, "NIL");
            InfrastructureEntity infra = searchInfra(infraId);
            if (em.contains(infra)) {
                rollingStockAsset.setInfrastructure(infra);
                em.persist(rollingStockAsset);
                return true;
            } else {
                return false;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean addConsumableAsset(String assetName, String code,
            int quantity, Date expiryDate,
            String consumableAssetType
    ) {
        try {
            Query q = em.createQuery("SELECT c FROM ConsumableAssetEntity AS c");
            int rows = q.getResultList().size();
            String assetId = "CSA" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT c1 FROM ConsumableAssetEntity AS c1 WHERE c1.assetId=:assetId");
                q1.setParameter("assetId", assetId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                assetId = "CSA" + rows;
            }
            consumableAsset = new ConsumableAssetEntity(assetId, assetName, quantity, expiryDate, consumableAssetType);
            InfrastructureEntity infra = searchNode(code);
            if (em.contains(infra)) {
                consumableAsset.setInfrastructure(infra);
                em.persist(consumableAsset);
                infra.getAssets().add(consumableAsset);
                return true;
            } else {
                return false;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean addNodeAsset(String assetName, String code,
            int lifetimeValue, Date purchaseDate,
            int quantity, String nodeAssetType
    ) {
        try {
            Query q = em.createQuery("SELECT na FROM NodeAssetEntity AS na");
            int rows = q.getResultList().size();
            String assetId = "NA" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT na1 FROM NodeAssetEntity AS na1 WHERE na1.assetId=:assetId");
                q1.setParameter("assetId", assetId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                assetId = "NA" + rows;
            }
            nodeAsset = new NodeAssetEntity(assetId, assetName, lifetimeValue, purchaseDate, quantity, nodeAssetType, "NIL");
            InfrastructureEntity infra = searchNode(code);
            if (em.contains(infra)) {
                nodeAsset.setInfrastructure(infra);
                em.persist(nodeAsset);
                infra.getAssets().add(nodeAsset);
                return true;
            } else {
                return false;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateAdvertisementSpace(String assetId, String assetName,
            String code, String location,
            String type, double peakPeriod, double nonpeakPeriod, String numberCode, Date deadline
    ) {
        AdvertisementSpaceEntity as = (AdvertisementSpaceEntity) searchAsset(assetId);
        if (em.contains(as)) {
            if (assetName.equals("RollingStock")) {
                InfrastructureEntity infra = searchInfra(code);
                if (em.contains(infra)) {
                    as.setInfrastructure(null);
                    em.persist(as);
                    as.setAssetName(assetName);
                    as.setLocation(location);
                    as.setPeakPeriodFee(peakPeriod);
                    as.setNonpeakPeriodFee(nonpeakPeriod);
                    as.setInfrastructure(infra);
                    as.setType(type);
                    as.setNumberCode(numberCode);
                     Timestamp date = new Timestamp(deadline.getTime());
                    as.setDeadline(date);
                    em.flush();
                    return true;
                }
            } else {
                InfrastructureEntity infra = searchNode(code);
                if (em.contains(infra)) {
                    as.setInfrastructure(null);
                    as.setAssetName(assetName);
                    as.setLocation(location);
                    as.setPeakPeriodFee(peakPeriod);
                    as.setNonpeakPeriodFee(nonpeakPeriod);
                    as.setInfrastructure(infra);
                    as.setType(type);
                    as.setNumberCode(numberCode);
                     Timestamp date = new Timestamp(deadline.getTime());
                    as.setDeadline(date);
                    em.flush();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateLeasingSpace(String assetId, String assetName,
            String code, String unitNumber,
            double floorArea, boolean waterProvision, double rentalFee, int leaseYear, String leaseDescription,
            Date deadline
    ) {
        LeasingSpaceEntity ls = (LeasingSpaceEntity) searchAsset(assetId);
        if (em.contains(ls)) {
            InfrastructureEntity infra = searchNode(code);
            if (em.contains(infra)) {
                ls.setInfrastructure(null);
                ls.setAssetName(assetName);
                ls.setUnitNumber(unitNumber);
                ls.setFloorArea(floorArea);
                ls.setWaterProvision(waterProvision);
                ls.setRentalFee(rentalFee);
                ls.setInfrastructure(infra);
                ls.setLeaseYear(leaseYear);
                ls.setLeaseDescription(leaseDescription);
                Timestamp date = new Timestamp(deadline.getTime());
                ls.setDeadline(date);
                em.merge(ls);
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public boolean updateRollingStockAsset(String assetId, String assetName,
            String infraId, int lifetimeValue, Date purchaseDate,
            int quantity, int storage, String rollingStockAssetType
    ) {
        RollingStockAssetEntity rs = (RollingStockAssetEntity) searchAsset(assetId);
        if (em.contains(rs)) {
            InfrastructureEntity infra = searchInfra(infraId);
            if (em.contains(infra)) {
                rs.setInfrastructure(null);
                rs.setAssetName(assetName);
                rs.setLifetimeValue(lifetimeValue);
                rs.setPurchaseDate(purchaseDate);
                rs.setInfrastructure(infra);
                rs.setQuantity(quantity);
                rs.setRollingStockAssetType(rollingStockAssetType);
                rs.setStorage(storage);
                em.flush();
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public boolean updateConsumableAsset(String assetId, String assetName,
            String code, int quantity, Date expiryDate,
            String consumableAssetType
    ) {
        ConsumableAssetEntity ca = (ConsumableAssetEntity) searchAsset(assetId);
        if (em.contains(ca)) {
            InfrastructureEntity infra = searchNode(code);
            if (em.contains(infra)) {
                ca.setInfrastructure(null);
                ca.setAssetName(assetName);
                ca.setExpiryDate(expiryDate);
                ca.setQuantity(quantity);
                ca.setInfrastructure(infra);
                ca.setConsumableAssetType(consumableAssetType);
                em.flush();
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public boolean updateNodeAsset(String assetId, String assetName,
            String code, int lifetimeValue, Date purchaseDate,
            int quantity, String nodeAssetType
    ) {
        NodeAssetEntity na = (NodeAssetEntity) searchAsset(assetId);
        if (em.contains(na)) {
            InfrastructureEntity infra = searchNode(code);
            if (em.contains(infra)) {
                na.setInfrastructure(null);
                na.setAssetName(assetName);
                na.setLifetimeValue(lifetimeValue);
                na.setPurchaseDate(purchaseDate);
                na.setInfrastructure(infra);
                na.setQuantity(quantity);
                na.setNodeAssetType(nodeAssetType);
                em.flush();
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public void submitNodeAssetRequest(String assetDetails, String qtySpoit,
            MaintenanceReportEntity report
    ) {

        String[] asset = assetDetails.split(" ");
        String assetId = asset[1];
        AssetEntity a = searchAsset(assetId);
        NodeAssetEntity n = (NodeAssetEntity) a;
        String assetType = n.getNodeAssetType();
        String assetName = a.getAssetName();
        NodeEntity ne = (NodeEntity) a.getInfrastructure();
        String code = ne.getCode();
        AssetRequestEntity assetReq = submitAssetRequest1(code, "Node Asset", assetType, assetName,
                Integer.parseInt(qtySpoit), "Request made as this asset is spoilt and could not be repaired");

        MaintenanceReportEntity temp = maintenanceSessionBeanLocal.searchReport(report.getMaintenanceReportId());
        if (em.contains(temp)) {
            temp.setAssetRequest(assetReq);
            em.merge(temp);
        }
    }

    @Override
    public void submitNodeAssetRequest1(String assetDetails, String qtySpoit,
            MaintenanceReportEntity report
    ) {

        String[] asset = assetDetails.split(" ");
        String assetId = asset[0];
        AssetEntity a = searchAsset(assetId);
        NodeAssetEntity n = (NodeAssetEntity) a;
        String assetType = n.getNodeAssetType();
        String assetName = a.getAssetName();
        NodeEntity ne = (NodeEntity) a.getInfrastructure();
        String code = ne.getCode();
        AssetRequestEntity assetReq = submitAssetRequest1(code, "Node Asset", assetType, assetName,
                Integer.parseInt(qtySpoit), "Request made as this asset is spoilt and could not be repaired");

        MaintenanceReportEntity temp = maintenanceSessionBeanLocal.searchReport(report.getMaintenanceReportId());
        if (em.contains(temp)) {
            temp.setAssetRequest(assetReq);
            em.merge(temp);
        }
    }

    @Override
    public boolean submitAssetRequest(String code, String assetRequestType,
            String assetType, String assetName,
            int qty, String remark
    ) {
        try {
            InfrastructureEntity infra = searchNode(code);
            String infraId = infra.getInfraId();
            Query q = em.createQuery("SELECT aq FROM AssetRequestEntity AS aq");
            int rows = q.getResultList().size();
            String assetRequestId = "AR" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT aq1 FROM AssetRequestEntity AS aq1 WHERE aq1.assetRequestId=:assetRequestId");
                q1.setParameter("assetRequestId", assetRequestId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                assetRequestId = "AR" + rows;
            }
            Timestamp reqDate = new Timestamp(System.currentTimeMillis());
            assetRequest = new AssetRequestEntity(assetRequestId, assetType, assetName, qty, "Received", remark, reqDate);
            if (assetRequestType.equals("Consumable Asset")) {
                boolean statuss = false;
                ArrayList<ConsumableAssetEntity> consumableAssetList = getConsumableAsset();
                for (int i = 0; i < consumableAssetList.size(); i++) {
                    if (consumableAssetList.get(i).getAssetName().equals(assetName) && consumableAssetList.get(i).getInfrastructure().getInfraId().equals(infraId)) {
                        statuss = true;
                    } //the assetName is inside database
                }
                if (statuss == false) {
                    boolean added = addConsumableAsset(assetName, code, 0, null, assetType);
                    if (added == false) {
                        return false;
                    }
                }
                ArrayList<ConsumableAssetEntity> consumableAssetList1 = getConsumableAsset();
                for (int i = 0; i < consumableAssetList1.size(); i++) {
                    if (consumableAssetList1.get(i).getAssetName().equals(assetName) && consumableAssetList1.get(i).getInfrastructure().getInfraId().equals(infraId)) {
                        String assetId = consumableAssetList1.get(i).getAssetId();
                        Query q1 = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
                        q1.setParameter("assetId", assetId);
                        if (q1.getResultList().isEmpty()) {
                        } else {
                            AssetEntity asset = (AssetEntity) q1.getResultList().get(0);
                            if (asset != null) {
                                assetRequest.setAsset(asset);
                                em.persist(assetRequest);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                return false;
            } else if (assetRequestType.equals("Node Asset")) {
                boolean statuss = false;
                ArrayList<NodeAssetEntity> nodeAssetList = getNodeAsset();
                for (int i = 0; i < nodeAssetList.size(); i++) {
                    if (nodeAssetList.get(i).getAssetName().equals(assetName) && nodeAssetList.get(i).getInfrastructure().getInfraId().equals(infraId)) {
                        statuss = true;
                    } //the assetName is inside database
                }
                if (statuss == false) {
                    boolean added = addNodeAsset(assetName, code, 0, null, 0, assetType);
                    if (added == false) {
                        return false;
                    }
                }
                ArrayList<NodeAssetEntity> nodeAssetList1 = getNodeAsset();
                for (int i = 0; i < nodeAssetList1.size(); i++) {
                    if (nodeAssetList1.get(i).getAssetName().equals(assetName) && nodeAssetList1.get(i).getInfrastructure().getInfraId().equals(infraId)) {
                        String assetId = nodeAssetList1.get(i).getAssetId();
                        Query q1 = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
                        q1.setParameter("assetId", assetId);
                        if (q1.getResultList().isEmpty()) {
                        } else {
                            AssetEntity asset = (AssetEntity) q1.getResultList().get(0);
                            if (asset != null) {
                                assetRequest.setAsset(asset);
                                em.persist(assetRequest);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                return false;

            } else {
                boolean statuss = false;
                ArrayList<RollingStockAssetEntity> rollingStockAssetList = getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList.size(); i++) {
                    if (rollingStockAssetList.get(i).getAssetName().equals(assetName) && rollingStockAssetList.get(i).getInfrastructure().getInfraId().equals(code)) {
                        statuss = true;
                    } //the assetName is inside database
                }
                if (statuss == false) {
                    boolean added = addRollingStockAsset(assetName, code, 0, null, 0, 0, assetType);
                    if (added == false) {
                        return false;
                    }
                }
                ArrayList<RollingStockAssetEntity> rollingStockAssetList1 = getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList1.size(); i++) {
                    if (rollingStockAssetList1.get(i).getAssetName().equals(assetName) && rollingStockAssetList1.get(i).getInfrastructure().getInfraId().equals(code)) {
                        String assetId = rollingStockAssetList1.get(i).getAssetId();
                        Query q1 = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
                        q1.setParameter("assetId", assetId);
                        if (q1.getResultList().isEmpty()) {
                        } else {
                            AssetEntity asset = (AssetEntity) q1.getResultList().get(0);
                            if (asset != null) {
                                assetRequest.setAsset(asset);
                                em.persist(assetRequest);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                return false;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return false;
    }

    public AssetRequestEntity submitAssetRequest1(String code, String assetRequestType,
            String assetType, String assetName,
            int qty, String remark
    ) {
        try {
            InfrastructureEntity infra = searchNode(code);
            String infraId = infra.getInfraId();
            Query q = em.createQuery("SELECT aq FROM AssetRequestEntity AS aq");
            int rows = q.getResultList().size();
            String assetRequestId = "AR" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT aq1 FROM AssetRequestEntity AS aq1 WHERE aq1.assetRequestId=:assetRequestId");
                q1.setParameter("assetRequestId", assetRequestId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                assetRequestId = "AR" + rows;
            }
            Timestamp reqDate = new Timestamp(System.currentTimeMillis());
            assetRequest = new AssetRequestEntity(assetRequestId, assetType, assetName, qty, "Received", remark, reqDate);
            if (assetRequestType.equals("Node Asset")) {
                boolean statuss = false;
                ArrayList<NodeAssetEntity> nodeAssetList = getNodeAsset();
                for (int i = 0; i < nodeAssetList.size(); i++) {
                    if (nodeAssetList.get(i).getAssetName().equals(assetName) && nodeAssetList.get(i).getInfrastructure().getInfraId().equals(infraId)) {
                        statuss = true;
                    } //the assetName is inside database
                }
                if (statuss == false) {
                    boolean added = addNodeAsset(assetName, code, 0, null, 0, assetType);
                    if (added == false) {
                        return null;
                    }
                }
                ArrayList<NodeAssetEntity> nodeAssetList1 = getNodeAsset();
                for (int i = 0; i < nodeAssetList1.size(); i++) {
                    if (nodeAssetList1.get(i).getAssetName().equals(assetName) && nodeAssetList1.get(i).getInfrastructure().getInfraId().equals(infraId)) {
                        String assetId = nodeAssetList1.get(i).getAssetId();
                        Query q1 = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
                        q1.setParameter("assetId", assetId);
                        if (q1.getResultList().isEmpty()) {
                        } else {
                            AssetEntity asset = (AssetEntity) q1.getResultList().get(0);
                            if (asset != null) {
                                assetRequest.setAsset(asset);
                                em.persist(assetRequest);
                                return assetRequest;
                            } else {
                                return null;
                            }
                        }
                    }
                }
                return null;

            } else {
                boolean statuss = false;
                ArrayList<RollingStockAssetEntity> rollingStockAssetList = getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList.size(); i++) {
                    if (rollingStockAssetList.get(i).getAssetName().equals(assetName) && rollingStockAssetList.get(i).getInfrastructure().getInfraId().equals(code)) {
                        statuss = true;
                    } //the assetName is inside database
                }
                if (statuss == false) {
                    boolean added = addRollingStockAsset(assetName, code, 0, null, 0, 0, assetType);
                    if (added == false) {
                        return null;
                    }
                }
                ArrayList<RollingStockAssetEntity> rollingStockAssetList1 = getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList1.size(); i++) {
                    if (rollingStockAssetList1.get(i).getAssetName().equals(assetName) && rollingStockAssetList1.get(i).getInfrastructure().getInfraId().equals(code)) {
                        String assetId = rollingStockAssetList1.get(i).getAssetId();
                        Query q1 = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
                        q1.setParameter("assetId", assetId);
                        if (q1.getResultList().isEmpty()) {
                        } else {
                            AssetEntity asset = (AssetEntity) q1.getResultList().get(0);
                            if (asset != null) {
                                assetRequest.setAsset(asset);
                                em.persist(assetRequest);
                                return assetRequest;
                            } else {
                                return null;
                            }
                        }
                    }
                }
                return null;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return null;
    }

    public TeamEntity searchTeam(String team) {
        long teamId = Long.parseLong(team);
        Query q = em.createQuery("SELECT t FROM TeamEntity AS t WHERE t.teamId=:teamId");
        q.setParameter("teamId", teamId);
        ArrayList<TeamEntity> result = (ArrayList<TeamEntity>) q.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public boolean updateAssetRequest(String assetReqId, String assetRequestType, int qty, String status) {
        AssetRequestEntity aq = searchAssetReq(assetReqId);
        if (em.contains(aq)) {
            aq.setStatus(status);
            aq.setQty(qty);
            String assetId = aq.getAsset().getAssetId();
            if (status.equals("Completed")) {
                if (aq.getAsset().getAssetId().substring(0, 3).equals("CSA")) {
                    ConsumableAssetEntity ca = (ConsumableAssetEntity) searchAsset(assetId);
                    int quantity = ca.getQuantity();
                    quantity = quantity + qty;
                    ca.setQuantity(quantity);
                    ca.setExpiryDate(null);
                } else if (aq.getAsset().getAssetId().substring(0, 2).equals("NA")) {
                    NodeAssetEntity na = (NodeAssetEntity) searchAsset(assetId);
                    int quantity = na.getQuantity();
                    quantity = quantity + qty;
                    na.setQuantity(quantity);
                    Date now = new Date();
                    na.setPurchaseDate(now);
                } else {
                    RollingStockAssetEntity ra = (RollingStockAssetEntity) searchAsset(assetId);
                    int quantity = ra.getStorage();
                    quantity = quantity + qty;
                    ra.setStorage(quantity);
                    Date now = new Date();
                    ra.setPurchaseDate(now);
                }
            } else if (status.equals("Cancelled")) {
                if (aq.getAsset().getAssetId().substring(0, 3).equals("CSA")) {
                    ConsumableAssetEntity ca = (ConsumableAssetEntity) searchAsset(assetId);
                    if (ca.getExpiryDate() == null && ca.getQuantity() == 0) {
                        delCAasset(assetId);
                    }
                } else if (aq.getAsset().getAssetId().substring(0, 2).equals("NA")) {
                    NodeAssetEntity na = (NodeAssetEntity) searchAsset(assetId);
                    if (na.getPurchaseDate() == null && na.getQuantity() == 0) {
                        delCAasset(assetId);
                    }
                } else {
                    RollingStockAssetEntity ra = (RollingStockAssetEntity) searchAsset(assetId);
                    if (ra.getPurchaseDate() == null && ra.getStorage() == 0 && ra.getQuantity() == 0) {
                        delCAasset(assetId);
                    }
                }
            }
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<AdvertisementSpaceEntity> getAdvertisementSpace() {
        ArrayList<AdvertisementSpaceEntity> advertisementSpaceList = new ArrayList<AdvertisementSpaceEntity>();
        Query query = em.createQuery("SELECT a FROM AdvertisementSpaceEntity AS a");
        for (Object o : query.getResultList()) {
            advertisementSpaceList.add((AdvertisementSpaceEntity) o);
        }
        return advertisementSpaceList;
    }

    @Override
    public ArrayList<AssetRequestEntity> getAssetRequest() {
        ArrayList<AssetRequestEntity> assetRequestList = new ArrayList<AssetRequestEntity>();
        Query query = em.createQuery("SELECT a FROM AssetRequestEntity AS a");
        for (Object o : query.getResultList()) {
            assetRequestList.add((AssetRequestEntity) o);
        }
        return assetRequestList;

    }

    @Override
    public ArrayList<LeasingSpaceEntity> getLeasingSpace() {
        ArrayList<LeasingSpaceEntity> leasingSpaceList = new ArrayList<LeasingSpaceEntity>();
        Query query = em.createQuery("SELECT l FROM LeasingSpaceEntity l");
        for (Object o : query.getResultList()) {
            leasingSpaceList.add((LeasingSpaceEntity) o);
        }
        return leasingSpaceList;
    }

    @Override
    public ArrayList<RollingStockAssetEntity> getRollingStockAsset() {
        ArrayList<RollingStockAssetEntity> rollingStockAssetList = new ArrayList<RollingStockAssetEntity>();
        Query query = em.createQuery("SELECT rs FROM RollingStockAssetEntity rs");
        for (Object o : query.getResultList()) {
            rollingStockAssetList.add((RollingStockAssetEntity) o);
        }
        return rollingStockAssetList;
    }

    @Override
    public ArrayList<ConsumableAssetEntity> getConsumableAsset() {
        ArrayList<ConsumableAssetEntity> consumableAssetList = new ArrayList<ConsumableAssetEntity>();
        Query query = em.createQuery("SELECT ca FROM ConsumableAssetEntity ca");
        for (Object o : query.getResultList()) {
            consumableAssetList.add((ConsumableAssetEntity) o);
        }
        return consumableAssetList;
    }

    @Override
    public ArrayList<NodeAssetEntity> getNodeAsset() {
        ArrayList<NodeAssetEntity> nodeAssetList = new ArrayList<NodeAssetEntity>();
        Query query = em.createQuery("SELECT na FROM NodeAssetEntity na");
        for (Object o : query.getResultList()) {
            nodeAssetList.add((NodeAssetEntity) o);
        }
        return nodeAssetList;
    }

    @Override
    public NodeEntity searchNode(String code) {
        NodeEntity i = new NodeEntity();
        try {
            Query q = em.createQuery("SELECT i FROM NodeEntity AS i WHERE i.code=:code");
            q.setParameter("code", code);
            i = (NodeEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return i;
    }

    @Override
    public NodeEntity searchNode1(String infraId) {
        NodeEntity i = new NodeEntity();
        try {
            Query q = em.createQuery("SELECT i FROM NodeEntity AS i WHERE i.infraId=:infraId");
            q.setParameter("infraId", infraId);
            i = (NodeEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return i;
    }

    @Override
    public NodeEntity searchStation(String infraId) {
        NodeEntity i = new NodeEntity();
        try {
            Query q = em.createQuery("SELECT i FROM NodeEntity AS i WHERE i.infraId=:infraId");
            q.setParameter("infraId", infraId);
            i = (NodeEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return i;
    }

    @Override
    public InfrastructureEntity searchInfra(String infraId) {

        try {
            InfrastructureEntity i;
            Query q = em.createQuery("SELECT i FROM InfrastructureEntity AS i WHERE i.infraId=:infraId");
            q.setParameter("infraId", infraId);
            i = (InfrastructureEntity) q.getSingleResult();

            return i;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return null;
    }

    @Override
    public AssetEntity searchAsset(String assetId) {

        AssetEntity a;
        try {
            Query q = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
            q.setParameter("assetId", assetId);
            a = (AssetEntity) q.getSingleResult();
            return a;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return null;
    }

    @Override
    public AssetRequestEntity searchAssetReq(String assetReqId) {
        AssetRequestEntity aq = new AssetRequestEntity();
        try {
            Query q = em.createQuery("SELECT aq FROM AssetRequestEntity AS aq WHERE aq.assetRequestId=:assetReqId");
            q.setParameter("assetReqId", assetReqId);
            aq = (AssetRequestEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return aq;
    }

    @Override
    public TrainCarEntity searchCar(int carCode) {
        TrainCarEntity tc = new TrainCarEntity();
        try {
            Query q = em.createQuery("SELECT tc FROM TrainCarEntity AS tc WHERE tc.carCode=:carCode");
            q.setParameter("carCode", carCode);
            tc = (TrainCarEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return tc;
    }

    @Override
    public RollingStockEntity searchRollingStock(String infraId) {
        RollingStockEntity rs = new RollingStockEntity();
        try {
            Query q = em.createQuery("SELECT rs FROM RollingStockEntity AS rs WHERE rs.infraId=:infraId");
            q.setParameter("infraId", infraId);
            rs = (RollingStockEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return rs;
    }

    @Override
    public boolean delAsset(String assetId) {
        AssetEntity as = searchAsset(assetId);
        if (em.contains(as)) {
            as.setInfrastructure(null);
            em.remove(as);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    public boolean delAssetRequest(String assetReqId) {
        AssetRequestEntity aq = searchAssetReq(assetReqId);
        if (em.contains(aq)) {
            em.remove(aq);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delCAasset(String assetId) {
        AssetEntity as = searchAsset(assetId);
        boolean status = true;
        if (em.contains(as)) {
            Query q = em.createQuery("SELECT aq FROM AssetRequestEntity AS aq");
            for (Object o : q.getResultList()) {
                AssetRequestEntity request = (AssetRequestEntity) o;
                if (request.getAsset().getAssetId().equals(assetId)) {
                    if (request.getStatus().equals("Delivered") || request.getStatus().equals("Cancelled")) {
                        delAssetRequest(request.getAssetRequestId());
                    } else {
                        status = false;
                    }
                }
            }

            if (status) {
                as.setInfrastructure(null);
                em.remove(as);
                em.flush();
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Asynchronous
    @Override
    public void generateAssetRequest() {
        Date now = new Date();
        Date newDate = subtractDays(now, 30);
        ArrayList<ConsumableAssetEntity> consumableAssetList = getConsumableAsset();
        if (consumableAssetList.isEmpty()) {
        } else {
            for (int i = 0; i < consumableAssetList.size(); i++) {
                ConsumableAssetEntity ca = new ConsumableAssetEntity();
                ca = consumableAssetList.get(i);
                if (ca.getExpiryDate() != null) {
                    if (ca.getQuantity() < 10 || newDate.compareTo(ca.getExpiryDate()) > 0) {
                        ArrayList<AssetRequestEntity> assetRequestList = getAssetRequest();
                        boolean sta = false;
                        if (!(assetRequestList.isEmpty())) {
                            for (int j = 0; j < assetRequestList.size(); j++) {
                                AssetRequestEntity aa = new AssetRequestEntity();
                                aa = assetRequestList.get(j);
                                if (aa.getAssetName().equals(ca.getAssetName()) && aa.getAsset().getInfrastructure().getInfraId().equals(ca.getInfrastructure().getInfraId()) && ((!(aa.getStatus().equals("Delivered"))) && (!(aa.getStatus().equals("Cancelled"))))) {
                                    sta = true;
                                    break;
                                    //Means assetRequest submitted 
                                }
                            } //AssetRequest not sumbitted 
                            if (sta == false) {
                                NodeEntity node = searchNode1(ca.getInfrastructure().getInfraId());
                                boolean submitRequest = submitAssetRequest(node.getCode(), "Consumable Asset", ca.getConsumableAssetType(), ca.getAssetName(), 0, "Automatic Generated Order");
                                if (submitRequest) {
                                }
                            }//done submitting request
                        }//done for loop for assetRequest
                        else {
                            NodeEntity node = searchNode1(ca.getInfrastructure().getInfraId());
                            boolean submitRequest = submitAssetRequest(node.getCode(), "Consumable Asset", ca.getConsumableAssetType(), ca.getAssetName(), 0, "Automatic Generated Order");
                            if (submitRequest) {
                            }
                        }
                    }//checech whether the assetList is empty
                } // end of if (quantity and expiry date)
            }// end of if (expiryDate !=null)
        }//end of checking consumable asset

        ArrayList<NodeAssetEntity> nodeAssetList = getNodeAsset();
        if (nodeAssetList.isEmpty()) {
        } else {
            for (int i = 0; i < nodeAssetList.size(); i++) {
                NodeAssetEntity na = new NodeAssetEntity();
                na = nodeAssetList.get(i);
                if (na.getPurchaseDate() != null) {
                    Date purchaseDate = na.getPurchaseDate();
                    int days = (na.getLifetimeValue() * 365);
                    Date newDate1 = addDays(purchaseDate, days); //date going expire
                    if (na.getQuantity() < 10 || newDate.compareTo(newDate1) > 0) {
                        ArrayList<AssetRequestEntity> assetRequestList = getAssetRequest();
                        boolean sta = false;
                        if (!(assetRequestList.isEmpty())) {
                            for (int j = 0; j < assetRequestList.size(); j++) {
                                AssetRequestEntity aa = new AssetRequestEntity();
                                aa = assetRequestList.get(j);
                                if (aa.getAssetName().equals(na.getAssetName()) && aa.getAsset().getInfrastructure().getInfraId().equals(na.getInfrastructure().getInfraId()) && ((!(aa.getStatus().equals("Delivered"))) && (!(aa.getStatus().equals("Cancelled"))))) {
                                    sta = true;
                                    break;
                                } //Means assetRequest submitted 
                            }
                            //AssetRequest not sumbitted 
                            if (sta == false) {
                                NodeEntity node = searchNode1(na.getInfrastructure().getInfraId());
                                boolean submitRequest = submitAssetRequest(node.getCode(), "Node Asset", na.getNodeAssetType(), na.getAssetName(), 0, "Automatic Generated Order");
                                if (submitRequest) {
                                }
                            }//done submitting request
                        }//done for loop for assetRequest
                        else {
                            NodeEntity node = searchNode1(na.getInfrastructure().getInfraId());
                            boolean submitRequest = submitAssetRequest(node.getCode(), "Node Asset", na.getNodeAssetType(), na.getAssetName(), 0, "Automatic Generated Order");
                            if (submitRequest) {
                            }
                        }
                    }//checech whether the assetList is empty
                } // end of if (quantity and expiry date)
            }// end of if (expiryDate !=null)
        }//end of checking nodeasset

        ArrayList<RollingStockAssetEntity> rollingStockAssetList = getRollingStockAsset();
        if (rollingStockAssetList.isEmpty()) {
        } else {
            for (int i = 0; i < rollingStockAssetList.size(); i++) {
                RollingStockAssetEntity ra = new RollingStockAssetEntity();
                ra = rollingStockAssetList.get(i);
                if (ra.getPurchaseDate() != null) {
                    Date purchaseDate = ra.getPurchaseDate();
                    int days = (ra.getLifetimeValue() * 365);
                    Date newDate1 = addDays(purchaseDate, days); //date going expire  
                    if (ra.getStorage() < 10 || newDate.compareTo(newDate1) > 0) {
                        ArrayList<AssetRequestEntity> assetRequestList = getAssetRequest();
                        boolean sta = false;
                        if (!(assetRequestList.isEmpty())) {
                            for (int j = 0; j < assetRequestList.size(); j++) {
                                AssetRequestEntity aa = new AssetRequestEntity();
                                aa = assetRequestList.get(j);
                                if (aa.getAssetName().equals(ra.getAssetName()) && aa.getAsset().getInfrastructure().getInfraId().equals(ra.getInfrastructure().getInfraId()) && ((!(aa.getStatus().equals("Delivered"))) && (!(aa.getStatus().equals("Cancelled"))))) {
                                    sta = true;
                                    break;
                                }  //Means assetRequest submitted 
                            }
                            //AssetRequest not sumbitted 
                            if (sta == false) {
                                boolean submitRequest = submitAssetRequest(ra.getInfrastructure().getInfraId(), "Rolling Stock Asset", ra.getRollingStockAssetType(), ra.getAssetName(), 0, "Automatic Generated Order");
                                if (submitRequest) {
                                }
                            }//done submitting request
                        }//checking whether the assetList is empty
                        else {
                            boolean submitRequest = submitAssetRequest(ra.getInfrastructure().getInfraId(), "Rolling Stock Asset", ra.getRollingStockAssetType(), ra.getAssetName(), 0, "Automatic Generated Order");
                            if (submitRequest) {
                            }
                        }
                    }//checech whether the assetList is empty
                } // end of if (quantity and expiry date)
            }// end of if (expiryDate !=null)
        }//end of checking nodeasset         
    }

    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    @Override
    public ArrayList<NodeEntity> retrieveNodes() {
        Query q = em.createQuery("SELECT i FROM NodeEntity AS i");
        List<NodeEntity> nodes = (List<NodeEntity>) q.getResultList();

        return new ArrayList<NodeEntity>(nodes);
    }

}
