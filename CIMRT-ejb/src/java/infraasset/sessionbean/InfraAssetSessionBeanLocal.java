package infraasset.sessionbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.entity.StationEntity;
import infraasset.entity.TrackEntity;
import infraasset.entity.TrainCarEntity;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import maintenance.entity.MaintenanceReportEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Zhirong
 */
@Local
public interface InfraAssetSessionBeanLocal {

    public boolean addDepot(String trainLine, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation,double latitude,double longitude);
    public boolean addStation(String trainLine, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation,double latitude,double longitude);
    public boolean addRollingStock(String infraName, String brand, String selectedDepot);
    public boolean addTrack(String trainLine, String infraName);
    public boolean updateDepot(String infraId, String fixedTrainLine, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation,double latitude,double longitude);
    public boolean updateStation(String infraId, String fixedTrainLine, String code, String infraName, String address, String previousStation, String nextStation, double distanceToFirstStation,double latitude,double longitude);
    public boolean updateRollingStock(String infraId, String status, List<String> selectedTrainCar, String staffId, String selectedDepot);
    public boolean updateTrack(String infraId, String infraName);
    public boolean deleteInfra(String infraId);
    public boolean addAdvertisementSpace(String assetName, String code, String location, String type, double peakPeriod, double nonpeakPeriod, String numberCode, Date deadline);
    public boolean updateAdvertisementSpace(String assetId, String assetName,String code,String location, String type, double peakPeriod, double nonpeakPeriod, String numberCode, Date deadline);
    public boolean addLeasingSpace(String assetName, String code, String unitNumber, double floorArea, boolean waterProvision, double rentalFee, int leaseYear,String leaseDescription, Date deadline);
    public boolean updateLeasingSpace(String assetId, String assetName, String code, String unitNumber, double floorArea, boolean waterProvision, double rentalFee, int leaseYear,String leaseDescription, Date timestamp);
    public boolean addRollingStockAsset(String assetName, String code, int lifetimeValue, Date purchaseDate,int quantity,int storage,String rollingStockAssetType);
    public boolean updateRollingStockAsset(String assetId, String assetName,String code, int lifetimeValue, Date purchaseDate,int quantity,int storage,String rollingStockAssetType);
    public boolean addConsumableAsset(String assetName, String code, int quantity, Date expiryDate, String consumableAssetType);
    public boolean updateConsumableAsset(String assetId, String assetName,String code, int quantity, Date expiryDate, String consumableAssetType);
    public boolean addNodeAsset(String assetName, String code, int lifetimeValue, Date purchaseDate,int quantity, String nodeAssetType);
    public boolean updateNodeAsset(String assetId, String assetName,String code, int lifetimeValue, Date purchaseDate,int quantity, String nodeAssetType) ;  
    public boolean delAsset(String assetId) ;
    public boolean delCAasset(String assetId) ;
    public ArrayList<StationEntity> getStation() ;
    public ArrayList<DepotEntity> getDepot();
    public ArrayList<RollingStockEntity> getRollingStock();
    public ArrayList<TrackEntity> getTrack();
    public ArrayList<AdvertisementSpaceEntity> getAdvertisementSpace();
    public AssetEntity searchAsset(String assetId);
    public InfrastructureEntity searchInfra(String infraId);
    public NodeEntity searchStation(String infraId);
    public ArrayList<LeasingSpaceEntity> getLeasingSpace();
    public ArrayList<RollingStockAssetEntity> getRollingStockAsset();
    public ArrayList<ConsumableAssetEntity> getConsumableAsset();
    public ArrayList<NodeAssetEntity> getNodeAsset();
    public boolean submitAssetRequest(String code,String assetRequestType,String assetType, String assetName,int qty,String remark);
    public AssetRequestEntity searchAssetReq(String assetReqId);
    public boolean updateAssetRequest(String assetReqId, String assetRequestType,int qty,String status);
    public ArrayList<AssetRequestEntity> getAssetRequest();
    public boolean addTrainCar(int carCode, String brand, String location, String type, String description);
    public TrainCarEntity searchCar(int carCode);
    public boolean updateTrainCar(String assetId, String description, String status, String location);
    public ArrayList<TrainCarEntity> getTrainCars();
    public NodeEntity searchNode(String code);
    public NodeEntity searchNode1(String infraId);
    public ArrayList<String> getTrainCarList(String code);
    public void generateAssetRequest();
    public boolean deleteTrainCar(int carCode);
    public RollingStockEntity searchRollingStock(String infraId);
    public ArrayList<NodeEntity> retrieveNodes();
    public boolean deleteRollingStock(String infraId, String staffId);
    public void submitNodeAssetRequest(String assetDetails, String qtySpoilt, MaintenanceReportEntity report);
    public void submitNodeAssetRequest1(String assetDetails, String qtySpoilt, MaintenanceReportEntity report);
}
