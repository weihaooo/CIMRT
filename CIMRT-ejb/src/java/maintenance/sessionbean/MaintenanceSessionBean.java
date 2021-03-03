/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenance.sessionbean;

import commoninfra.entity.StaffEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.entity.TrainCarEntity;
import infraasset.entity.InfrastructureEntity;
import javax.ejb.Stateful;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import maintenance.entity.MaintenanceReportEntity;
import operations.entity.MaintenanceRequestEntity;

/**
 *
 * @author FABIAN
 */
@Stateful
public class MaintenanceSessionBean implements MaintenanceSessionBeanLocal {

    @PersistenceContext()
    EntityManager em;

    public MaintenanceSessionBean() {
    }

    StaffEntity staffEntity;
    AssetEntity assetEntity;
    MaintenanceRequestEntity maintenanceRequestEntity;
    MaintenanceReportEntity maintenanceReportEntity;

    @Override
    public MaintenanceReportEntity createMaintenanceReport(String rptTitle, String reportDescription, String asset1, String qty1, String maintenanceStatus,
            Timestamp time, String staffId, String submitterName, String mainReqId, String assetDetails, String qtySpoilt) {

        if (assetDetails == null) {
            return null;
        }

        String tempStatus = "";

        if (maintenanceStatus.equals("Spoilt") && asset1.equals("1")) {
            tempStatus = "Pending Replacement";
        }
        //asset is instanceof NodeAssetEntity
        if (asset1.equals("1")) {
            maintenanceReportEntity = new MaintenanceReportEntity(rptTitle, reportDescription, asset1, Integer.parseInt(qty1), tempStatus,
                    time, staffId, submitterName, Integer.parseInt(qtySpoilt));
            em.persist(maintenanceReportEntity);
            em.flush();
            maintenanceRequestEntity = searchMaintenanceRequest(mainReqId);
            Long maintenanceReportId = maintenanceReportEntity.getMaintenanceReportId();
            MaintenanceReportEntity mre = searchReport(maintenanceReportId);
            maintenanceRequestEntity.setMaintenanceReport(mre);
            em.flush();
            String[] asset = assetDetails.split(" ");
            String assetId = asset[1];
            NodeAssetEntity a = searchNodeAsset(assetId);

            if (maintenanceStatus.equals("Spoilt")) {
                a.setQuantity(a.getQuantity() - Integer.parseInt(qtySpoilt));
                em.flush();
            }

            return maintenanceReportEntity;

        } else if (!asset1.equals("1")) { //if asset is instanceof RollingStockAssetEntity

            maintenanceReportEntity = new MaintenanceReportEntity(rptTitle, reportDescription, asset1, Integer.parseInt(qty1), maintenanceStatus,
                    time, staffId, submitterName, Integer.parseInt(qtySpoilt));
            em.persist(maintenanceReportEntity);
            em.flush();
            maintenanceRequestEntity = searchMaintenanceRequest(mainReqId);
            Long maintenanceReportId = maintenanceReportEntity.getMaintenanceReportId();
            MaintenanceReportEntity mre = searchReport(maintenanceReportId);
            maintenanceRequestEntity.setMaintenanceReport(mre);
            em.flush();

            String[] asset = assetDetails.split(" ");
            String assetId = asset[1];
            RollingStockAssetEntity r = searchRollingStockAsset(assetId);

            if (maintenanceStatus.equals("Repaired") && !(r.getStorage() == 0)) {
                r.setStorage(r.getStorage() - Integer.parseInt(qtySpoilt));
                em.flush();
            } else if (maintenanceStatus.equals("Spoilt")) {
                r.setQuantity(r.getQuantity() - Integer.parseInt(qtySpoilt));
                em.flush();
            }

            return maintenanceReportEntity;
        }
        return null;
    }

    @Override
    public boolean createMaintenanceReportTrainCar(String rptTitle, String reportDescription, String maintenanceStatus, Timestamp time,
            String staffId, String submitterName, String mainReqId, String assetDetails) {

        if(assetDetails == null){
            return false;
        }

        String[] asset = assetDetails.split(" ");
        String assetId = asset[1];
        System.out.println("trainCarId: " + assetId);
        TrainCarEntity tce = searchTrainCar(assetId);
        if(tce == null){
            return false;
        }

        int counter = 0;

        maintenanceReportEntity = new MaintenanceReportEntity(rptTitle, reportDescription, null, Integer.parseInt("0"), maintenanceStatus,
                time, staffId, submitterName, Integer.parseInt("0"));
        em.persist(maintenanceReportEntity);
        em.flush();
        maintenanceRequestEntity = searchMaintenanceRequest(mainReqId);

        if(maintenanceRequestEntity == null){
            return false;
        }
        Long maintenanceReportId = maintenanceReportEntity.getMaintenanceReportId();
        MaintenanceReportEntity mre = searchReport(maintenanceReportId);
        if (mre == null){
            return false;
        }

        maintenanceRequestEntity.setMaintenanceReport(mre);
        em.flush();

        if (maintenanceStatus.equals("Repaired")) {
            tce.setStatus("Attached");
            em.flush();
            InfrastructureEntity temp = tce.getInfrastructure();
            RollingStockEntity rse = (RollingStockEntity) temp;
            List<AssetEntity> assetList = temp.getAssets();
            for (int i = 0; i < assetList.size(); i++) {
                if (assetList.get(i).getAssetId().substring(0, 2).equals("TC")) {
                    TrainCarEntity tc = (TrainCarEntity) assetList.get(i);
                    System.out.println(tc.getCarCode() + " Status: " + tc.getStatus());
                    if (tc.getStatus().equals("Attached")) {
                        counter++;
                    }
                }
            }
            if (counter == 6) {
                rse.setStatus("Available");
            }

            counter = 0;

        } else if (maintenanceStatus.equals("Spoilt")) {
            tce.setStatus("Spoilt");
            InfrastructureEntity temp = tce.getInfrastructure();
            RollingStockEntity rse = (RollingStockEntity) temp;
            List<AssetEntity> assetList = temp.getAssets();
            for (int i = 0; i < assetList.size(); i++) {
                if (assetList.get(i).getAssetId().substring(0, 2).equals("TC")) {
                    TrainCarEntity tc = (TrainCarEntity) assetList.get(i);
                    tc.setInfrastructure(null);
                    if (!tc.getAssetId().equals(tce.getAssetId())) {
                        tc.setStatus("Available");
                    }
                }
            }

            ArrayList<AssetEntity> assets = new ArrayList<AssetEntity>(rse.getAssets());
            for (int i = 0; i < assets.size(); i++) {
                if (assets.get(i).getAssetId().substring(0, 2).equals("TC")) {
                    assets.remove(assets.get(i));
                }
            }
            rse.setAssets(assets);

            ArrayList<DepotEntity> depotList = new ArrayList<DepotEntity>();
            Query q1 = em.createQuery("SELECT d FROM DepotEntity d");
            for (Object o : q1.getResultList()) {
                depotList.add((DepotEntity) o);
            }

            for (int j = 0; j < depotList.size(); j++) {
                if (depotList.get(j).getCode().equals(rse.getDepot().getCode())) {
                    ArrayList<RollingStockEntity> rollingStocks = new ArrayList<RollingStockEntity>(depotList.get(j).getRollingStocks());
                    rollingStocks.remove(rse);
                    depotList.get(j).setRollingStocks(rollingStocks);
                    rse.setDepot(null);
                    break;
                }
            }

            em.remove(rse);
            em.flush();
        }

        return true;
    }

    @Override
    public boolean updateMaintenanceReport(Long maintenanceReportId, String asset1, String qty1, String rptTitle,
            String reportDescription, String maintenanceStatus, String qtySpoilt, String assetName) {

        int counter = 0;
        String[] assetDetails = assetName.split(" ");
        String assetId = assetDetails[0];
        AssetEntity asset = searchAsset(assetId);
        if(asset == null){
            return false;
        }
        MaintenanceReportEntity mre = searchMaintenanceReport(maintenanceReportId);
        MaintenanceRequestEntity req = getMaintenanceRequest(maintenanceReportId);

        if (asset instanceof TrainCarEntity) {
            TrainCarEntity tce = searchTrainCar(assetId);
            if (maintenanceStatus.equals("Repaired")) {
                tce.setStatus("Attached");
                em.flush();
                InfrastructureEntity temp = tce.getInfrastructure();
                RollingStockEntity rse = (RollingStockEntity) temp;
                List<AssetEntity> assetList = temp.getAssets();
                for (int i = 0; i < assetList.size(); i++) {
                    if (assetList.get(i).getAssetId().substring(0, 2).equals("TC")) {
                        TrainCarEntity tc = (TrainCarEntity) assetList.get(i);
                        System.out.println(tc.getCarCode() + " Status: " + tc.getStatus());
                        if (tc.getStatus().equals("Attached")) {
                            counter++;
                        }
                    }
                }
                if (counter == 6) {
                    rse.setStatus("Available");
                }

                counter = 0;

            } else if (maintenanceStatus.equals("Spoilt")) {
                tce.setStatus("Spoilt");
                InfrastructureEntity temp = tce.getInfrastructure();
                RollingStockEntity rse = (RollingStockEntity) temp;
                List<AssetEntity> assetList = temp.getAssets();
                for (int i = 0; i < assetList.size(); i++) {
                    if (assetList.get(i).getAssetId().substring(0, 2).equals("TC")) {
                        TrainCarEntity tc = (TrainCarEntity) assetList.get(i);
                        tc.setInfrastructure(null);
                        if (!tc.getAssetId().equals(tce.getAssetId())) {
                            tc.setStatus("Available");
                        }
                    }
                }

                ArrayList<AssetEntity> assets = new ArrayList<AssetEntity>(rse.getAssets());
                for (int i = 0; i < assets.size(); i++) {
                    if (assets.get(i).getAssetId().substring(0, 2).equals("TC")) {
                        assets.remove(assets.get(i));
                    }
                }
                rse.setAssets(assets);

                ArrayList<DepotEntity> depotList = new ArrayList<DepotEntity>();
                Query q1 = em.createQuery("SELECT d FROM DepotEntity d");
                for (Object o : q1.getResultList()) {
                    depotList.add((DepotEntity) o);
                }

                for (int j = 0; j < depotList.size(); j++) {
                    if (depotList.get(j).getCode().equals(rse.getDepot().getCode())) {
                        ArrayList<RollingStockEntity> rollingStocks = new ArrayList<RollingStockEntity>(depotList.get(j).getRollingStocks());
                        rollingStocks.remove(rse);
                        depotList.get(j).setRollingStocks(rollingStocks);
                        rse.setDepot(null);
                        break;
                    }
                }

                em.remove(rse);
                em.flush();
            }
            mre.setRptTitle(rptTitle);
            mre.setReportDescription(reportDescription);
            mre.setMaintenanceStatus(maintenanceStatus);
            req.setMainReqStatus(maintenanceStatus);
            em.flush();
            return true;
        } else if (asset instanceof NodeAssetEntity) {

            NodeAssetEntity na = searchNodeAsset(assetId);

            if (maintenanceStatus.equals("Spoilt")) {
                na.setQuantity(na.getQuantity() - Integer.parseInt(qtySpoilt));
                em.flush();
                mre.setRptTitle(rptTitle);
                mre.setReportDescription(reportDescription);
                mre.setQtySpoilt(Integer.parseInt(qtySpoilt));
                mre.setMaintenanceStatus("Pending Replacement");
                req.setMainReqStatus("Pending Replacement");
                em.flush();
            } else if (maintenanceStatus.equals("Repaired")) {
                em.flush();
                mre.setRptTitle(rptTitle);
                mre.setReportDescription(reportDescription);
                mre.setMaintenanceStatus("Replaced");
                req.setMainReqStatus("Replaced");
                em.flush();
            }

            return true;
        } else if (asset instanceof RollingStockAssetEntity) {

            RollingStockAssetEntity rsae = searchRollingStockAsset(assetId);
            if (maintenanceStatus.equals("Repaired") && !(rsae.getStorage() == 0)) {
                rsae.setStorage(rsae.getStorage() - Integer.parseInt(qtySpoilt));
                em.flush();
            } else if (maintenanceStatus.equals("Spoilt")) {
                rsae.setQuantity(rsae.getQuantity() - Integer.parseInt(qtySpoilt));
                mre.setQtySpoilt(Integer.parseInt(qtySpoilt));
                em.flush();
            }

            mre.setRptTitle(rptTitle);
            mre.setReportDescription(reportDescription);
            mre.setMaintenanceStatus(maintenanceStatus);
            req.setMainReqStatus(maintenanceStatus);
            return true;
        }

        return true;
    }

    

    @Override
    public void updateRequestStatus(String mainReqId, String maintenanceStatus
    ) {
        MaintenanceRequestEntity m = searchMaintenanceRequest(mainReqId);
        m.setMainReqStatus(maintenanceStatus);
        em.flush();

    }

    @Override
    public List<MaintenanceReportEntity> getAllReports() {
        Query q = em.createQuery("SELECT m FROM MaintenanceReportEntity AS m");
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public List<MaintenanceReportEntity> getSpecificReport(Long maintenanceReportId
    ) {
        Query q = em.createQuery("SELECT m FROM MaintenanceReportEntity AS m WHERE m.maintenanceReportId=:maintenanceReportId");
        q.setParameter("maintenanceReportId", maintenanceReportId);
        List<MaintenanceReportEntity> m = q.getResultList();

        return m;
    }

    /*@Override
    public void deductQuantity(String asset1, String qty1) {
        String[] assetDetails = asset1.split(" ");
        String assetId = assetDetails[0];
        System.out.println("AssetId passed in here is :" + assetId);
        String assetName = assetDetails[1];
        AssetEntity asset = searchAsset(assetId);

        if (asset instanceof NodeAssetEntity) {
            NodeAssetEntity n = (NodeAssetEntity) asset;
            int qty = n.getQuantity();
            int updatedQty = qty - Integer.parseInt(qty1);
            n.setQuantity(updatedQty);
            em.flush();
        } else if (asset instanceof RollingStockAssetEntity) {
            RollingStockAssetEntity r = (RollingStockAssetEntity) asset;
            int qty = r.getQuantity();
            int updatedQty = qty - Integer.parseInt(qty1);
            r.setQuantity(updatedQty);
            em.flush();
        }
    }*/
    @Override
    public AssetEntity searchAsset(String assetId
    ) {
        Query q = em.createQuery("SELECT a FROM AssetEntity AS a WHERE a.assetId=:assetId");
        q.setParameter("assetId", assetId);
        List<AssetEntity> assetList = q.getResultList();
        AssetEntity ae = (AssetEntity) assetList.get(0);

        return ae;
    }

    @Override
    public NodeAssetEntity searchNodeAsset(String assetId
    ) {
        Query q = em.createQuery("SELECT n FROM NodeAssetEntity AS n WHERE n.assetId=:assetId");
        q.setParameter("assetId", assetId);
        List<NodeAssetEntity> assetList = q.getResultList();
        NodeAssetEntity na = (NodeAssetEntity) assetList.get(0);

        return na;
    }

    @Override
    public RollingStockAssetEntity searchRollingStockAsset(String assetId
    ) {
        Query q = em.createQuery("SELECT r FROM RollingStockAssetEntity AS r WHERE r.assetId=:assetId");
        q.setParameter("assetId", assetId);
        List<RollingStockAssetEntity> assetList = q.getResultList();
        RollingStockAssetEntity rsa = (RollingStockAssetEntity) assetList.get(0);

        return rsa;
    }

    @Override
    public TrainCarEntity searchTrainCar(String assetId
    ) {
        Query q = em.createQuery("SELECT t FROM TrainCarEntity AS t WHERE t.assetId=:assetId");
        q.setParameter("assetId", assetId);
        List<TrainCarEntity> temp = q.getResultList();
        TrainCarEntity c = (TrainCarEntity) temp.get(0);

        return c;
    }

    @Override
    public MaintenanceReportEntity searchReport(Long maintenanceReportId
    ) {
        Query q = em.createQuery("SELECT m FROM MaintenanceReportEntity AS m WHERE m.maintenanceReportId=:maintenanceReportId");
        q.setParameter("maintenanceReportId", maintenanceReportId);
        List<MaintenanceReportEntity> mre = q.getResultList();
        MaintenanceReportEntity mr = (MaintenanceReportEntity) mre.get(0);

        return mr;
    }

    @Override
    public MaintenanceRequestEntity searchMaintenanceRequest(String mainReqId
    ) {
        Query q = em.createQuery("SELECT m FROM MaintenanceRequestEntity AS m WHERE m.mainReqId=:mainReqId");
        q.setParameter("mainReqId", mainReqId);
        List<MaintenanceRequestEntity> mreList = q.getResultList();
        MaintenanceRequestEntity mre = (MaintenanceRequestEntity) mreList.get(0);

        return mre;
    }

    @Override
    public MaintenanceRequestEntity getMaintenanceRequest(Long maintenanceReportId
    ) {
        MaintenanceReportEntity maintenanceReport = searchMaintenanceReport(maintenanceReportId);
        Query q = em.createQuery("SELECT m FROM MaintenanceRequestEntity AS m WHERE m.maintenanceReport=:maintenanceReport");
        q.setParameter("maintenanceReport", maintenanceReport);
        List<MaintenanceRequestEntity> mreList = q.getResultList();
        MaintenanceRequestEntity mre = (MaintenanceRequestEntity) mreList.get(0);

        return mre;
    }

    @Override
    public MaintenanceReportEntity searchMaintenanceReport(Long maintenanceReportId
    ) {
        Query q = em.createQuery("SELECT m FROM MaintenanceReportEntity AS m WHERE m.maintenanceReportId=:maintenanceReportId");
        q.setParameter("maintenanceReportId", maintenanceReportId);
        List<MaintenanceReportEntity> mreList = q.getResultList();
        MaintenanceReportEntity mre = (MaintenanceReportEntity) mreList.get(0);

        return mre;
    }

    @Override
    public List<AssetEntity> getAllAssets() {
        Query q = em.createQuery("SELECT a FROM AssetEntity a");
        return q.getResultList();
    }

    @Override
    public List<AssetEntity> getAssetIdList(String assetType
    ) {
        Query q = em.createQuery("SELECT a FROM AssetEntity a.assetName=:assetType");
        return q.getResultList();
    }//end of getAssetIdList() method

}//end of class
