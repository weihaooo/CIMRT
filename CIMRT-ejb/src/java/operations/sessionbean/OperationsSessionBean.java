/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import commoninfra.sessionbean.EmailManager;
import commoninfra.sessionbean.SystemAdminSessionBeanLocal;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.DepotTeamEntity;
import commoninfra.entity.HqStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.AssetRequestEntity;
import infraasset.entity.ConsumableAssetEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.RollingStockAssetEntity;
import infraasset.entity.RollingStockEntity;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import operations.entity.ServiceLogEntity;
import operations.entity.InterviewEntity;
import operations.entity.JobApplicationsEntity;
import operations.entity.JobOfferEntity;
import operations.entity.MaintenanceRequestEntity;
import routefare.entity.NodeEntity;
import infraasset.entity.TrainCarEntity;
import operations.entity.StaffContractEntity;

/**
 *
 * @author Zhirong
 */
@Stateful
public class OperationsSessionBean implements OperationsSessionBeanLocal, OperationsSessionBeanRemote {

    @EJB
    private InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    @EJB
    private SystemAdminSessionBeanLocal systemAdminSessionBean;

    @PersistenceContext
    EntityManager em;

    private ServiceLogEntity serviceLogEntity;
    private JobOfferEntity jobOfferEntity;
    private InterviewEntity interviewEntity;
    private AssetRequestEntity assetRequest;
    private RollingStockAssetEntity rollingStockAsset;
    private ConsumableAssetEntity consumableAsset;
    private NodeAssetEntity nodeAsset;
    private MaintenanceRequestEntity mainReqEntity;

    public OperationsSessionBean() {

    }

    @Override
    public boolean addSvcLog(String subject, String content, String staffId) {
        try {
            Query q = em.createQuery("SELECT sl FROM ServiceLogEntity sl");
            int spaceCount = q.getResultList().size();
            String svcLogId = "SL" + (spaceCount + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT sl1 FROM ServiceLogEntity sl1 WHERE sl1.svcLogId=:svcLogId");
                q1.setParameter("svcLogId", svcLogId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                spaceCount = spaceCount + 2;
                svcLogId = "SL" + spaceCount;
            }

            Date date = new Date();
            long time = date.getTime();
            Timestamp dateTime = new Timestamp(time);

            serviceLogEntity = new ServiceLogEntity(svcLogId, dateTime, subject, content);
            StationStaffEntity staff = searchStationStaff(staffId);
            serviceLogEntity.setStationStaff(staff);
            em.persist(serviceLogEntity);
            return true;
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
    public ArrayList<ServiceLogEntity> getSvcLogs(String staffId) {
        ArrayList<ServiceLogEntity> svcLogList = new ArrayList<ServiceLogEntity>();
        Query q = em.createQuery("SELECT sl FROM ServiceLogEntity sl");
        if(q.getResultList().isEmpty()){
            return null;
        }
        for (Object o : q.getResultList()) {
            ServiceLogEntity s = (ServiceLogEntity) o;
            if (s.getStationStaff().getStaffId().equals(staffId)) {
                svcLogList.add(s);
            }
        }
        return svcLogList;
    }

    @Override
    public ArrayList<ServiceLogEntity> getSvcLogs1(String nodeCode) {
        ArrayList<ServiceLogEntity> svcLogList = new ArrayList<ServiceLogEntity>();
        Query q = em.createQuery("SELECT sl FROM ServiceLogEntity sl");
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            for (Object o : q.getResultList()) {
                ServiceLogEntity s = (ServiceLogEntity) o;
                String staffId = s.getStationStaff().getStaffId();
                StationStaffEntity staff = searchStationStaff(staffId);
                if (staff != null) {
                    String code = staff.getStationTeam().getNode().getCode();
                    if (code.equals(nodeCode)) {
                        svcLogList.add(s);
                    }
                }
            }
        }
        return svcLogList;
    }

    @Override
    public StationStaffEntity searchStationStaff(String staffId) {
        Query q = em.createQuery("SELECT s FROM StationStaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        ArrayList<StationStaffEntity> result = new ArrayList<StationStaffEntity>(q.getResultList());
        StationStaffEntity staff;
        if (result.isEmpty()) {
            return null;
        } else {
            staff = result.get(0);
            return staff;
        }
    }

    @Override
    public ServiceLogEntity searchLog(String svcLogId) {
        ServiceLogEntity sl = new ServiceLogEntity();
        try {
            Query q = em.createQuery("SELECT sl FROM ServiceLogEntity AS sl WHERE sl.svcLogId=:svcLogId");
            q.setParameter("svcLogId", svcLogId);
            sl = (ServiceLogEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return sl;
    }

    @Override
    public ArrayList<AdvertisementSpaceEntity> getAdvertisementSpace(String nodeCode) {
        ArrayList<AdvertisementSpaceEntity> advertisementSpaceList = new ArrayList<AdvertisementSpaceEntity>();
        NodeEntity node = searchNode(nodeCode);
        String infraId = node.getInfraId();
        Query query = em.createQuery("SELECT a FROM AdvertisementSpaceEntity AS a");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                AdvertisementSpaceEntity a = (AdvertisementSpaceEntity) o;
                if (a.getAssetName().equals("Station")) {
                    if (a.getInfrastructure().getInfraId().equals(infraId)) {
                        advertisementSpaceList.add(a);
                    }
                } else {
                    RollingStockEntity rolling = searchRolling(a.getInfrastructure().getInfraId());
                    if (rolling.getDepot().equals(nodeCode)) {
                        advertisementSpaceList.add(a);
                    }
                }
            }
            return advertisementSpaceList;
        }
        return advertisementSpaceList;
    }

    @Override
    public ArrayList<LeasingSpaceEntity> getLeasingSpace(String nodeCode) {
        ArrayList<LeasingSpaceEntity> leasingSpaceList = new ArrayList<LeasingSpaceEntity>();
        NodeEntity node = searchNode(nodeCode);
        String infraId = node.getInfraId();
        Query query = em.createQuery("SELECT l FROM LeasingSpaceEntity l");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                LeasingSpaceEntity l = (LeasingSpaceEntity) o;
                if (l.getInfrastructure().getInfraId().equals(infraId)) {
                    leasingSpaceList.add(l);
                }
            }
            return leasingSpaceList;
        }
        return leasingSpaceList;
    }

    @Override
    public ArrayList<RollingStockEntity> getRollingStock() {
        ArrayList<RollingStockEntity> rollingStockList = new ArrayList<RollingStockEntity>();
        Query q = em.createQuery("SELECT r FROM RollingStockEntity AS r");
        if(q.getResultList().isEmpty()){
            return null;
        }
        for (Object o : q.getResultList()) {
            rollingStockList.add((RollingStockEntity) o);
        }
        return rollingStockList;
    }

    @Override
    public ArrayList<RollingStockEntity> getRollingStock(String teamId) {
        ArrayList<RollingStockEntity> rollingStockList = new ArrayList<RollingStockEntity>();
        TeamEntity teamentity = searchTeam(teamId);
        if (teamentity == null) {
            return null;
        } else {
            String code = teamentity.getNode().getCode();
            Query q = em.createQuery("SELECT r FROM RollingStockEntity r");
            if ((!q.getResultList().isEmpty())) {
                for (Object o : q.getResultList()) {
                    RollingStockEntity r = (RollingStockEntity) o;
                    if (r.getDepot().equals(code)) {
                        rollingStockList.add(r);
                    }
                }
                return rollingStockList;
            }
        }
        return rollingStockList;
    }

    @Override
    public ArrayList<RollingStockAssetEntity> getRollingStockAsset(String nodeCode) {
        ArrayList<RollingStockAssetEntity> rollingStockAssetList = new ArrayList<RollingStockAssetEntity>();
        if(nodeCode == ""){
            return null;
        }
        
        Query query = em.createQuery("SELECT rs FROM RollingStockAssetEntity rs");
        for (Object o : query.getResultList()) {
            RollingStockAssetEntity r = (RollingStockAssetEntity) o; //infraId: RollingStock
            RollingStockEntity rolling = searchRolling(r.getInfrastructure().getInfraId());
            if (rolling.getDepot().equals(nodeCode)) {
                rollingStockAssetList.add(r);
            }
        }
        return rollingStockAssetList;
    }

    public RollingStockEntity searchRolling(String infraId) {
        try {
            RollingStockEntity i;
            Query q = em.createQuery("SELECT i FROM RollingStockEntity AS i WHERE i.infraId=:infraId");
            q.setParameter("infraId", infraId);
            i = (RollingStockEntity) q.getSingleResult();
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
    public ArrayList<ConsumableAssetEntity> getConsumableAsset(String nodeCode) {
        ArrayList<ConsumableAssetEntity> consumableAssetList = new ArrayList<ConsumableAssetEntity>();
        NodeEntity node = searchNode(nodeCode);
        String infraId = node.getInfraId();
        Query query = em.createQuery("SELECT ca FROM ConsumableAssetEntity ca");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                ConsumableAssetEntity c = (ConsumableAssetEntity) o;
                if (c.getInfrastructure().getInfraId().equals(infraId)) {
                    consumableAssetList.add(c);
                }
            }
        }
        return consumableAssetList;
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
    public RollingStockEntity searchRollingStock(String infraId) {
        RollingStockEntity rollingStock = new RollingStockEntity();
        try {
            Query q = em.createQuery("SELECT r FROM RollingStockEntity AS r WHERE r.infraId=:infraId");
            q.setParameter("infraId", infraId);
            rollingStock = (RollingStockEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return rollingStock;
    }

    @Override
    public boolean addConsumableAsset(String assetName, String code, int quantity, Date expiryDate, String consumableAssetType) {
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
    public boolean addNodeAsset(String assetName, String code, int lifetimeValue, Date purchaseDate, int quantity, String nodeAssetType) {
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
    public boolean addRollingStockAsset(String assetName, String infraId, int lifetimeValue, Date purchaseDate, int quantity, int storage, String rollingStockAssetType) {
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
    public boolean submitAssetRequest(String teamId, String infraId2, String assetRequestType, String assetType, String assetName, int qty, String remark) {
        try {
            TeamEntity teamentity = searchTeam(teamId);
            String infraId = teamentity.getNode().getInfraId();
            String code = teamentity.getNode().getCode();
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
            assetRequest = new AssetRequestEntity(assetRequestId, assetRequestType, assetName, qty, "Received", remark, reqDate);
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
                    if (rollingStockAssetList.get(i).getAssetName().equals(assetName) && rollingStockAssetList.get(i).getInfrastructure().getInfraId().equals(infraId2)) {
                        statuss = true;
                    } //the assetName is inside database
                }
                if (statuss == false) {
                    boolean added = addRollingStockAsset(assetName, infraId2, 0, null, 0, 0, assetType);
                    if (added == false) {
                        return false;
                    }
                }
                ArrayList<RollingStockAssetEntity> rollingStockAssetList1 = getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList1.size(); i++) {
                    if (rollingStockAssetList1.get(i).getAssetName().equals(assetName) && rollingStockAssetList1.get(i).getInfrastructure().getInfraId().equals(infraId2)) {
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

    @Override
    public TeamEntity searchTeam(String team) {
        long teamId = Long.parseLong(team);
        Query q = em.createQuery("SELECT t FROM TeamEntity AS t WHERE t.teamId=:teamId");
        q.setParameter("teamId", teamId);
        ArrayList<TeamEntity> result = new ArrayList<TeamEntity>(q.getResultList());
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public ArrayList<AssetRequestEntity> getAssetRequest(String nodeCode) {
        ArrayList<AssetRequestEntity> assetRequestList = new ArrayList<AssetRequestEntity>();
        NodeEntity node = searchNode(nodeCode);
        String infraId = node.getInfraId();
        Query query = em.createQuery("SELECT a FROM AssetRequestEntity AS a");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                AssetRequestEntity a = (AssetRequestEntity) o;
                if (a.getAsset().getInfrastructure().getInfraId().equals(infraId)) {
                    assetRequestList.add(a);
                }
            }
            ArrayList<RollingStockEntity> rollingStockList = getRollingStock();

            for (Object o : query.getResultList()) {
                AssetRequestEntity a = (AssetRequestEntity) o;
                for (int i = 0; i < rollingStockList.size(); i++) {
                    if (a.getAsset().getInfrastructure().getInfraId().equals(rollingStockList.get(i).getInfraId())) {
                        assetRequestList.add(a);
                    }
                }
            }
            return assetRequestList;
        }
        return null;
    }

    @Override
    public ArrayList<NodeAssetEntity> getNodeAsset(String nodeCode) {
        ArrayList<NodeAssetEntity> nodeAssetList = new ArrayList<NodeAssetEntity>();
        NodeEntity node = searchNode(nodeCode);
        String infraId = node.getInfraId();
        Query query = em.createQuery("SELECT na FROM NodeAssetEntity na");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                NodeAssetEntity n = (NodeAssetEntity) o;
                if (n.getInfrastructure().getInfraId().equals(infraId)) {
                    nodeAssetList.add(n);
                }
            }
            return nodeAssetList;
        }
        return nodeAssetList;
    }

    @Override
    public ArrayList<JobOfferEntity> getJobOffer() {
        ArrayList<JobOfferEntity> jobOfferList = new ArrayList<JobOfferEntity>();
        Query query = em.createQuery("SELECT j FROM JobOfferEntity j");
        for (Object o : query.getResultList()) {
            JobOfferEntity joboffer = (JobOfferEntity) o;
            jobOfferList.add(joboffer);
        }
        return jobOfferList;
    }

    private StaffEntity searchStaff(String staffId) {
        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        ArrayList<StaffEntity> result = new ArrayList<StaffEntity>(q.getResultList());
        StaffEntity staff;
        if (result.isEmpty()) {
            return null;
        } else {
            staff = result.get(0);
            return staff;
        }
    }

    @Override
    public boolean createJob(String staffId, String jobTitle, String jobPosition, String location, String jobType, double salary, String jobDescription, String jobQualifications, Date postedDate, Date jobDeadline) {
        try {
            Query q = em.createQuery("SELECT j FROM JobOfferEntity AS j");
            int rows = q.getResultList().size();
            String jobId = "J" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT j1 FROM JobOfferEntity AS j1 WHERE j1.jobId=:jobId");
                q1.setParameter("jobId", jobId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                jobId = "J" + rows;
            }
            //Create the new role into the database
            systemAdminSessionBean.createRole(jobPosition, location);
            jobOfferEntity = new JobOfferEntity(jobId, jobTitle, jobPosition, location, jobType, salary, jobDescription, jobQualifications, true, postedDate, jobDeadline);
            HqStaffEntity hq = (HqStaffEntity) searchStaff(staffId);
            if (em.contains(hq)) {
                hq.getJobs().add(jobOfferEntity);
                em.persist(hq);
                em.persist(jobOfferEntity);
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
    public boolean updateJob(String jobId, String jobTitle, String jobPosition, String location, String jobType, double salary, String jobDescription, String jobQualifications, Date postedDate, Date jobDeadline) {
        JobOfferEntity job = searchJob(jobId);
        if (em.contains(job)) {
            job.setJobPosition(jobPosition);
            job.setJobDescription(jobDescription);
            job.setJobTitle(jobTitle);
            job.setLocation(location);
            job.setJobType(jobType);
            job.setSalary(salary);
            job.setJobQualifications(jobQualifications);
            job.setPostedDate(postedDate);
            job.setJobDeadline(jobDeadline);
            Date now = new Date();
            if (now.compareTo(jobDeadline) > 0) {
                job.setJobStatus(false);
            } else {
                job.setJobStatus(true);
            }
            em.flush();
            return true;

        } else {
            return false;
        }
    }

    @Override
    public void checkJob() {
        Date now = new Date();
        ArrayList<JobOfferEntity> jobList = getJobOffer();
        for (int i = 0; i < jobList.size(); i++) {
            JobOfferEntity job = searchJob(jobList.get(i).getJobId());
            if (em.contains(job)) {
                if (job.isJobStatus() == true && job.getJobDeadline() != null) {
                    if (now.compareTo(job.getJobDeadline()) > 0) {
                        job.setJobStatus(false);
                    } else {
                        job.setJobStatus(true);
                    }
                }
            }
            em.flush();
        }
    }

    @Override
    public JobOfferEntity searchJob(String jobId) {
        JobOfferEntity job = new JobOfferEntity();
        try {
            Query q = em.createQuery("SELECT j FROM JobOfferEntity AS j WHERE j.jobId=:jobId");
            q.setParameter("jobId", jobId);
            job = (JobOfferEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return job;
    }

    @Override
    public JobApplicationsEntity searchJobApp(String applicationId) {
        JobApplicationsEntity job = new JobApplicationsEntity();
        try {
            Query q = em.createQuery("SELECT j FROM JobApplicationsEntity AS j WHERE j.applicationId=:applicationId");
            q.setParameter("applicationId", applicationId);
            job = (JobApplicationsEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return job;
    }

    @Override
    public boolean goJobStatus(String jobId) {
        JobOfferEntity joboffer = searchJob(jobId);
        if (em.contains(joboffer)) {
            if (joboffer.isJobStatus() == true) {
                joboffer.setJobStatus(false);
            } else {
                joboffer.setJobStatus(true);
                joboffer.setJobDeadline(null);
            }
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<JobApplicationsEntity> getJobApplication(String jobId, String emailAddress) {
        Date now = new Date();
        JobOfferEntity job = new JobOfferEntity();
        ArrayList<JobApplicationsEntity> jobAppList = new ArrayList<JobApplicationsEntity>();
        job = searchJob(jobId);
        if (job != null) {
            Collection<JobApplicationsEntity> applications = job.getJobApplications();
            for (JobApplicationsEntity application : applications) {
                if (application.getInterview() != null && application.getAppStatus().equals("Scheduled")) {
                    Date interTo = application.getInterview().getInterviewTo();
                    if (now.compareTo(interTo) > 0) {
                        updateJobApp(application.getApplicationId(), "Pending", emailAddress);
                    }
                }
                jobAppList.add(application);
            }
            return jobAppList;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<JobApplicationsEntity> getJobApp() {
        ArrayList<JobApplicationsEntity> jobAppList = new ArrayList<JobApplicationsEntity>();
        Query query = em.createQuery("SELECT j FROM JobApplicationsEntity j");
        for (Object o : query.getResultList()) {
            jobAppList.add((JobApplicationsEntity) o);
        }
        return jobAppList;
    }

    @Override
    public boolean updateJobApp(String applicationId, String appStatus, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        JobApplicationsEntity jobApp = searchJobApp(applicationId);
        if (em.contains(jobApp)) {
            jobApp.setAppStatus(appStatus);
            em.flush();

            if (appStatus.equals("Rejected")) {
                emailManager.sendRejectionEmail(jobApp.getFirstName() + " " + jobApp.getLastName(), jobApp.getEmailAddress(), emailAddress);
            } else if (appStatus.equals("Accepted")) {
                emailManager.sendAcceptanceEmail(jobApp.getFirstName() + " " + jobApp.getLastName(), jobApp.getEmailAddress(), emailAddress);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createInterview(String applicationId, Date interviewFrom, Date interviewTo, String emailAddress) {
        try {
            EmailManager emailManager = new EmailManager();
            Query q = em.createQuery("SELECT i FROM InterviewEntity AS i");
            int rows = q.getResultList().size();
            String interviewId = "Interview" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT i1 FROM InterviewEntity AS i1 WHERE i1.interviewId=:interviewId");
                q1.setParameter("interviewId", interviewId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                interviewId = "Interview" + rows;
            }
            Timestamp interTo = new Timestamp(interviewTo.getTime());
            Timestamp interFrom = new Timestamp(interviewFrom.getTime());
            interviewEntity = new InterviewEntity(interviewId, interFrom, interTo);
            JobApplicationsEntity jobApp = searchJobApp(applicationId);
            if (em.contains(jobApp)) {
                jobApp.setInterview(interviewEntity);
                jobApp.setAppStatus("Scheduled");
                interviewEntity.setJobApplication(jobApp);
                em.persist(jobApp);
                em.persist(interviewEntity);
                emailManager.sendInterviewEmail(interTo, interFrom, jobApp.getEmailAddress(), jobApp.getFirstName() + " " + jobApp.getLastName(), emailAddress);
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
    public ArrayList<InterviewEntity> getInterviewList() {
        ArrayList<InterviewEntity> interviewList = new ArrayList<InterviewEntity>();
        Query query = em.createQuery("SELECT i FROM InterviewEntity i");
        for (Object o : query.getResultList()) {
            interviewList.add((InterviewEntity) o);
        }
        return interviewList;
    }

    @Override
    public InterviewEntity searchInterview(String interviewId) {
        InterviewEntity interview = new InterviewEntity();
        try {
            Query q = em.createQuery("SELECT i FROM InterviewEntity AS i WHERE i.interviewId=:interviewId");
            q.setParameter("interviewId", interviewId);
            interview = (InterviewEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return interview;
    }

    @Override
    public boolean updateInterview(String applicationId, Date interviewFrom, Date interviewTo, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        JobApplicationsEntity jobApp = searchJobApp(applicationId);
        String interviewID = jobApp.getInterview().getInterviewId();
        InterviewEntity interview = searchInterview(interviewID);
        if (em.contains(interview)) {
            Timestamp interTo = new Timestamp(interviewTo.getTime());
            Timestamp interFrom = new Timestamp(interviewFrom.getTime());
            interview.setInterviewFrom(interFrom);
            interview.setInterviewTo(interTo);
            jobApp.setAppStatus("Scheduled");
            em.persist(jobApp);
            em.persist(interview);
            em.flush();
            emailManager.sendUpdateInterviewEmail(interTo, interFrom, jobApp.getEmailAddress(), jobApp.getFirstName() + " " + jobApp.getLastName(), emailAddress);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteInterview(String applicationId, String emailAddress) {
        EmailManager emailManager = new EmailManager();
        if(applicationId==null){
            return false;
        }
        JobApplicationsEntity jobApp = searchJobApp(applicationId);
        
        String interviewID = jobApp.getInterview().getInterviewId();
        InterviewEntity interview = searchInterview(interviewID);
        if (em.contains(interview)) {
            interview.setJobApplication(null);
            jobApp.setInterview(null);
            jobApp.setAppStatus("Rejected");
            em.remove(interview);
            em.flush();
            emailManager.sendDeleteInterviewEmail(jobApp.getEmailAddress(), jobApp.getFirstName() + " " + jobApp.getLastName(), emailAddress);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<AssetRequestEntity> getAssetRequests() {
        ArrayList<AssetRequestEntity> assetRequestList = new ArrayList<AssetRequestEntity>();
        Query query = em.createQuery("SELECT a FROM AssetRequestEntity AS a");
        for (Object o : query.getResultList()) {
            assetRequestList.add((AssetRequestEntity) o);
        }
        return assetRequestList;
    }

    @Override
    public boolean submitMaintenanceRequest(String teamId, String staffId, String assetRequestType, String infraId, String assetType, String assetName, String mainReqType, String remark, Date now) {
        try {
            TeamEntity teamentity = searchTeam(teamId);
            String infraId1 = teamentity.getNode().getInfraId();
            InfrastructureEntity infra = searchInfra(infraId1); //Get the submitter depot/Station

            if (assetRequestType.equals("Rolling Stock")) {
                remark = "Referring To Rolling Stock: " + infraId + '\n' + remark;
                RollingStockEntity rs = searchRollingStock(infraId);
                rs.setStatus("Under Maintenance");
                em.persist(rs);
            }

            Query q = em.createQuery("SELECT mr FROM MaintenanceRequestEntity AS mr");
            int rows = q.getResultList().size();
            String mainReqId = "MR" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT mr1 FROM MaintenanceRequestEntity AS mr1 WHERE mr1.mainReqId=:mainReqId");
                q1.setParameter("mainReqId", mainReqId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                mainReqId = "MR" + rows;
            }
            Timestamp date = new Timestamp(now.getTime());
            mainReqEntity = new MaintenanceRequestEntity(mainReqId, date, mainReqType, remark, "Received", staffId);

            if (assetRequestType.equals("Node Asset")) {
                ArrayList<NodeAssetEntity> nodeAssetLists = infraAssetSessionBeanLocal.getNodeAsset();
                for (int i = 0; i < nodeAssetLists.size(); i++) {
                    if (nodeAssetLists.get(i).getNodeAssetType().equals(assetType) && nodeAssetLists.get(i).getAssetName().equals(assetName) && nodeAssetLists.get(i).getInfrastructure().getInfraName().equals(infra.getInfraName())) {
                        mainReqEntity.setAsset(nodeAssetLists.get(i));
                        em.persist(mainReqEntity);
                    }
                }
            } else {
                ArrayList<RollingStockAssetEntity> rollingStockAssetList = infraAssetSessionBeanLocal.getRollingStockAsset();
                for (int i = 0; i < rollingStockAssetList.size(); i++) {
                    RollingStockEntity r = (RollingStockEntity) rollingStockAssetList.get(i).getInfrastructure();
                    if (rollingStockAssetList.get(i).getRollingStockAssetType().equals(assetType) && rollingStockAssetList.get(i).getAssetName().equals(assetName) && r.getDepot().getInfraName().equals(infra.getInfraName())) {
                        mainReqEntity.setAsset(rollingStockAssetList.get(i));
                        em.persist(mainReqEntity);
                    }
                }
            }
            return true;
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
    public ArrayList<String> getRollingStockList() {
        ArrayList<String> rollingStockList = new ArrayList<String>();
        Query q = em.createQuery("SELECT r FROM RollingStockEntity r");
        for (Object o : q.getResultList()) {
            RollingStockEntity r = (RollingStockEntity) o;
            rollingStockList.add(r.getInfraId());
        }
        return rollingStockList;
    }

    @Override
    public List<MaintenanceRequestEntity> getMaintenanceRequestList() {
        ArrayList<MaintenanceRequestEntity> mainReqList = new ArrayList<MaintenanceRequestEntity>();
        Query query = em.createQuery("SELECT m FROM MaintenanceRequestEntity AS m");
        for (Object o : query.getResultList()) {
            mainReqList.add((MaintenanceRequestEntity) o);
        }
        return mainReqList;
    }

    @Override
    public List<NodeEntity> getStationsInCharge(String staffId) {
        List<NodeEntity> ewlList = new ArrayList<NodeEntity>();
        List<NodeEntity> ewlListFinal = new ArrayList<NodeEntity>();
        List<NodeEntity> nslList = new ArrayList<NodeEntity>();
        List<NodeEntity> nslListFinal = new ArrayList<NodeEntity>();
        List<NodeEntity> empty = new ArrayList<NodeEntity>();

        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        List<StaffEntity> temp = q.getResultList();
        if (temp.get(0) instanceof DepotStaffEntity) {
            DepotStaffEntity manager = (DepotStaffEntity) temp.get(0);
            NodeEntity node = manager.getDepotTeam().getNode();
            String managerStation = node.getCode();
            String code = "";

            if (managerStation.contains("EWL")) {
                code = "EWL%";
                System.out.println("Here at EWL");
                Query q1 = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code LIKE :code ORDER BY n.distanceToFirstStation");
                q1.setParameter("code", code);
                ewlList = q1.getResultList();
                for (int i = 0; i < ewlList.size(); i++) {
                    if (managerStation.equals("EWL00")) {
                        if (ewlList.get(i).getDistanceToFirstStation() <= 19200.0) {//first 11 stations belong to DepotManager @ EWL00
                            ewlListFinal.add(ewlList.get(i));
                        }
                    } else if (managerStation.equals("EWL23")) {
                        if (ewlList.get(i).getDistanceToFirstStation() > 19200.0 && ewlList.get(i).getDistanceToFirstStation() <= 43400.0) {
                            ewlListFinal.add(ewlList.get(i));
                        }
                    }

                }

                /*for (int k = 0; k < ewlListFinal.size(); k++) {
                System.out.println(ewlListFinal.get(k).getInfraId() + " " + ewlListFinal.get(k).getCode());
            }*/
                return ewlListFinal;

            } else if (managerStation.contains("NSL")) {
                code = "NSL%";
                Query q2 = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code LIKE :code ORDER BY n.distanceToFirstStation");
                q2.setParameter("code", code);
                nslList = q2.getResultList();
                for (int i = 0; i < nslList.size(); i++) {
                    if (managerStation.equals("NSL00")) {
                        if (nslList.get(i).getDistanceToFirstStation() <= 13800.0) {
                            nslListFinal.add(nslList.get(i));
                        }
                    } else if (managerStation.equals("NSL16")) {
                        if (nslList.get(i).getDistanceToFirstStation() > 13800.0 && nslList.get(i).getDistanceToFirstStation() <= 27200.0) {
                            nslListFinal.add(nslList.get(i));
                        }
                    }

                }
                /*for (int k = 0; k < nslListFinal.size(); k++) {
                System.out.println(nslListFinal.get(k).getInfraId() + " " + nslListFinal.get(k).getCode());
            }*/
                return nslListFinal;
            }
        }

        return empty;
    }

    @Override
    public List<MaintenanceRequestEntity> getMaintenanceRequestListByDepot(String staffId) {

        ArrayList<MaintenanceRequestEntity> mainReqList = new ArrayList<MaintenanceRequestEntity>();
        Query query = em.createQuery("SELECT m FROM MaintenanceRequestEntity AS m");
        for (Object o : query.getResultList()) {
            mainReqList.add((MaintenanceRequestEntity) o);
        }
        return mainReqList;
    }

    @Override
    public List<MaintenanceRequestEntity> getMaintenanceRequestList1(String nodeCode) {
        ArrayList<MaintenanceRequestEntity> mainReqList = new ArrayList<MaintenanceRequestEntity>();
        if(nodeCode == null){
            return null;
        }
        NodeEntity n = searchNode(nodeCode);
        Query query = em.createQuery("SELECT m FROM MaintenanceRequestEntity AS m");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                MaintenanceRequestEntity m = (MaintenanceRequestEntity) o;
                if (m.getAsset().getAssetId().substring(0, 3).equals("RSA")) {
                    RollingStockEntity rs = searchRollingStock(m.getAsset().getInfrastructure().getInfraId());
                    if (rs.getDepot().getCode().equals(nodeCode)) {
                        mainReqList.add(m);
                    }
                } else if (m.getAsset().getAssetId().substring(0, 2).equals("NA")) {
                    if (m.getAsset().getInfrastructure().getInfraId().equals(n.getInfraId())) {
                        mainReqList.add(m);
                    }
                } //For train car
                else {
                    TrainCarEntity tc = (TrainCarEntity) m.getAsset();
                    InfrastructureEntity temp = tc.getInfrastructure();
                    NodeEntity depot = (NodeEntity) temp;
                    if (depot.getCode().equals(nodeCode)) {
                        mainReqList.add(m);
                    }
                }
            }
            return mainReqList;
        }
        return mainReqList;
    }

    @Override
    public List<MaintenanceRequestEntity> getMaintenanceRequestList2(String staffId) {
        ArrayList<MaintenanceRequestEntity> mainReqList = new ArrayList<MaintenanceRequestEntity>();
        Query query = em.createQuery("SELECT m FROM MaintenanceRequestEntity AS m");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                MaintenanceRequestEntity m = (MaintenanceRequestEntity) o;
                if (m.getSubmitter().equals(staffId)) {
                    mainReqList.add(m);
                }
            }
            return mainReqList;
        }
        return mainReqList;
    }

    @Override
    public boolean delRequest(String mainReqId) {
        MaintenanceRequestEntity mr = searchMaintenaceRequest(mainReqId);
        if (em.contains(mr)) {
            if (mr.getMaintenanceReport() == null) {
                mr.setAsset(null);
                em.remove(mr);
                em.flush();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public MaintenanceRequestEntity searchMaintenaceRequest(String mainReqId) {
        MaintenanceRequestEntity m = new MaintenanceRequestEntity();
        try {
            Query q = em.createQuery("SELECT m FROM MaintenanceRequestEntity AS m WHERE m.mainReqId=:mainReqId");
            q.setParameter("mainReqId", mainReqId);
            m = (MaintenanceRequestEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return m;
    }

    //Update the status of the maintenance request
    @Override
    public boolean updateMaintenanceRequest(String mainReqId, String mainReqStatus) {
        MaintenanceRequestEntity m = searchMaintenaceRequest(mainReqId);
        if (em.contains(m)) {
            m.setMainReqStatus(mainReqStatus);

            //If the asset is train car 
            //mainReqId.
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    //Station staff/train driver when they edit their maintenance request
    @Override
    public boolean updateMaintenanceRequest1(String mainReqId, String mainReqType, String remark) {
        MaintenanceRequestEntity m = searchMaintenaceRequest(mainReqId);
        if (em.contains(m)) {
            m.setMainReqType(mainReqType);
            m.setRemark(remark);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createApplicant(String jobId, String firstName, String lastName, String nric, String phoneNumber, String emailAddress, String address, String gender, Date dateOfBirth, String maritalStatus, String race, String nationality, String religion, String highestEducation, int yearsOfExp, String position, String company, Date startDate, Date endDate, String jobIndustry, String summary) {
        try {
            //Same person cannot resubmit again
            ArrayList<JobApplicationsEntity> jobAppList = getJobApp();
            for (int i = 0; i < jobAppList.size(); i++) {
                if (jobAppList.get(i).getJobOffer().getJobId().equals(jobId) && jobAppList.get(i).getNric().equals(nric) && (jobAppList.get(i).getAppStatus().equals("Received") || jobAppList.get(i).getAppStatus().equals("Pending"))) {
                    return false;
                }
            }

            Query q = em.createQuery("SELECT a FROM JobApplicationsEntity a");
            int spaceCount = q.getResultList().size();
            String applicationId = "A" + (spaceCount + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT a1 FROM JobApplicationsEntity a1 WHERE a1.applicationId=:applicationId");
                q1.setParameter("applicationId", applicationId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                spaceCount = spaceCount + 2;
                applicationId = "A" + spaceCount;
            }

            JobApplicationsEntity applicant = new JobApplicationsEntity(applicationId, firstName, lastName, nric, phoneNumber, emailAddress, address, gender, dateOfBirth, maritalStatus, race, nationality, religion, highestEducation, yearsOfExp, position, company, startDate, endDate, jobIndustry, summary, "Received");
            em.persist(applicant);
            JobOfferEntity job = searchJob(jobId);
            if (em.contains(job)) {
                applicant.setJobOffer(job);
                em.flush();
            }
            ArrayList<JobApplicationsEntity> appList = new ArrayList<JobApplicationsEntity>(job.getJobApplications());
            appList.add(applicant);
            job.setJobApplications(appList);
            em.flush();

            EmailManager emailManager = new EmailManager();
            String staffEmail = "e0002252@u.nus.edu";
            emailManager.sendApplicantEmail(applicant.getFirstName() + " " + applicant.getLastName(), applicant.getEmailAddress(), staffEmail);

            return true;
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
    public boolean createContract(String appliciantId, Date startDate, Date endDate, String emailAddress) {
        try {
            Query q = em.createQuery("SELECT sc FROM StaffContractEntity sc");
            int spaceCount = q.getResultList().size();
            String staffContractId = "SC" + (spaceCount + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT a1 FROM StaffContractEntity a1 WHERE a1.staffContractId=:staffContractId");
                q1.setParameter("staffContractId", staffContractId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                spaceCount = spaceCount + 2;
                staffContractId = "SC" + spaceCount;
            }
            Timestamp start = new Timestamp(startDate.getTime());
            Timestamp end = new Timestamp(endDate.getTime());

            StaffContractEntity contract = new StaffContractEntity(staffContractId, start, end);
            em.persist(contract);
            JobApplicationsEntity jobApp = searchJobApp(appliciantId);
            if (em.contains(jobApp)) {
                contract.setJobApplication(jobApp);
                em.flush();
            }

            EmailManager emailManager = new EmailManager();
            emailManager.sendContractEmail(start, end, jobApp.getFirstName() + " " + jobApp.getLastName(), jobApp.getEmailAddress(), emailAddress);

            return true;
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
    public StaffContractEntity searchStaffContract(String staffContractId) {
        StaffContractEntity s = new StaffContractEntity();
        try {
            Query q = em.createQuery("SELECT s FROM StaffContractEntity AS s WHERE s.staffContractId=:staffContractId");
            q.setParameter("staffContractId", staffContractId);
            if (q.getSingleResult() == null) {
                return null;
            }
            s = (StaffContractEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return s;
    }

    @Override
    public StaffContractEntity getStaffContract(String appliciantId) {
        Query query = em.createQuery("SELECT m FROM StaffContractEntity AS m");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                StaffContractEntity m = (StaffContractEntity) o;
                if (m.getJobApplication().getApplicationId().equals(appliciantId)) {
                    return m;
                }
            }
        }
        return null;
    }

}
