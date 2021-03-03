/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import infraasset.entity.RollingStockEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.AssetEntity;
import infraasset.entity.InfrastructureEntity;
import infraasset.entity.TrainCarEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import operations.entity.IncidentReportEntity;
import operations.entity.InspectionReportEntity;
import operations.entity.MaintenanceRequestEntity;
import operations.entity.TripReportEntity;
import routefare.entity.NodeEntity;

/**
 *
 * @author Zhirong
 */
@Stateful
public class ReportSessionBean implements ReportSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    private IncidentReportEntity incidentReportEntity;
    private InspectionReportEntity inspectionReportEntity;
    private TripReportEntity tripReportEntity;

    public ReportSessionBean() {

    }

    @Override
    public boolean addInspectionReport(String reportType, Date todayDate, String rollingStock, String code, String remarks, String submitter, String driverCabinLight, String throttle,
            String brake, String reverser, String switchPanel, String horn, String microphone, String speaker, String ledDisplay, String wipers, String washers, String driverChair, String passCabinLight, String cabinBenches, String handGrips, String handRails,
            String doorSignals, String emergencyButtons, String emergencyExitDoors, String loudspeakers, String animatedTrainMaps, String windows, String trainDoors, String aircons) {
        try {
            String faulty = "";
            Query q = em.createQuery("SELECT i FROM InspectionReportEntity i");
            int spaceCount = q.getResultList().size();
            String inspectReportId = "IS" + (spaceCount + 1);
            boolean status1 = true;
            while (status1) {
                Query q1 = em.createQuery("SELECT i1 FROM InspectionReportEntity i1 WHERE i1.inspectReportId=:inspectReportId");
                q1.setParameter("inspectReportId", inspectReportId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                spaceCount = spaceCount + 2;
                inspectReportId = "IS" + spaceCount;
            }

            if (driverCabinLight.equals("Faulty")) {
                faulty = faulty + "Driver Cabin Light";
            }
            if (throttle.equals("Faulty")) {
                faulty = faulty + " || " + "Throttle";
            }
            if (brake.equals("Faulty")) {
                faulty = faulty + " || " + "Brake";
            }
            if (reverser.equals("Faulty")) {
                faulty = faulty + " || " + "Reverser";
            }
            if (switchPanel.equals("Faulty")) {
                faulty = faulty + " || " + "Swtich Panel";
            }
            if (horn.equals("Faulty")) {
                faulty = faulty + " || " + "Horn";
            }
            if (microphone.equals("Faulty")) {
                faulty = faulty + " || " + "Microphone";
            }
            if (speaker.equals("Faulty")) {
                faulty = faulty + " || " + "Speaker";
            }
            if (ledDisplay.equals("Faulty")) {
                faulty = faulty + " || " + "Led Display";
            }
            if (wipers.equals("Faulty")) {
                faulty = faulty + " || " + "Wipers";
            }
            if (washers.equals("Faulty")) {
                faulty = faulty + " || " + "Washers";
            }
            if (driverChair.equals("Faulty")) {
                faulty = faulty + " || " + "Driver Chair";
            }
            if (passCabinLight.equals("Faulty")) {
                faulty = faulty + " || " + "Pass Cabin Light";
            }
            if (cabinBenches.equals("Faulty")) {
                faulty = faulty + " || " + "Cabin Benches";
            }
            if (handGrips.equals("Faulty")) {
                faulty = faulty + " || " + "Hand Grips";
            }
            if (handRails.equals("Faulty")) {
                faulty = faulty + " || " + "Hand Rails";
            }
            if (doorSignals.equals("Faulty")) {
                faulty = faulty + " || " + "Door Signals";
            }
            if (emergencyButtons.equals("Faulty")) {
                faulty = faulty + " || " + "Emergency Buttons";
            }
            if (emergencyExitDoors.equals("Faulty")) {
                faulty = faulty + " || " + "Emergency Exit Doors";
            }
            if (loudspeakers.equals("Faulty")) {
                faulty = faulty + " || " + "Loud Speakers";
            }
            if (animatedTrainMaps.equals("Faulty")) {
                faulty = faulty + " || " + "Animated Train Maps";
            }
            if (windows.equals("Faulty")) {
                faulty = faulty + " || " + "Windows";
            }
            if (trainDoors.equals("Faulty")) {
                faulty = faulty + " || " + "Train Doors";
            }
            if (aircons.equals("Faulty")) {
                faulty = faulty + " || " + "Aircons";
            }

            Timestamp date = new Timestamp(todayDate.getTime());
            inspectionReportEntity = new InspectionReportEntity(inspectReportId, date, reportType, remarks, "Submitted", null, submitter, null, driverCabinLight, throttle, brake, reverser, switchPanel, horn, microphone, speaker, ledDisplay, wipers, washers,
                    driverChair, passCabinLight, cabinBenches, handGrips, handRails, doorSignals, emergencyButtons, emergencyExitDoors, loudspeakers, animatedTrainMaps, windows, trainDoors, aircons);
            em.persist(inspectionReportEntity);

            if (faulty.equals("")) {
            } else {
                faulty = faulty + "\n\n" + remarks;
                MaintenanceRequestEntity maintenanceStatus = submitMaintenanceRequest(todayDate, faulty, rollingStock, submitter, code);
                if (maintenanceStatus != null) {
                    inspectionReportEntity.setMaintenanceRequest(maintenanceStatus);
                }
            }

            //Get Depot Staff list and add the inspection report into the list
            DepotStaffEntity depotStaff = searchDepotStaff(submitter);
            ArrayList temp = new ArrayList<InspectionReportEntity>(depotStaff.getInspectReports());
            temp.add(inspectionReportEntity);
            depotStaff.setInspectReports(temp);
            em.persist(depotStaff);

            //set depot staff to inspectionreport 
            inspectionReportEntity.setDepotStaff(depotStaff);
            em.persist(inspectionReportEntity);

            RollingStockEntity rs = searchRollingStock(rollingStock);
            ArrayList temp2 = new ArrayList<InspectionReportEntity>(rs.getInspectReports());
            temp2.add(inspectionReportEntity);
            rs.setInspectReports(temp2);
            em.persist(rs);

            inspectionReportEntity.setRollingStock(rs);
            em.persist(inspectionReportEntity);

            em.flush();
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
    public boolean updateInspectionReport(String inspectReportId, String staffId, String signature) {
        InspectionReportEntity inspectReport = (InspectionReportEntity) searchInspectReport(inspectReportId);
        if (em.contains(inspectReport)) {
            inspectReport.setStatus("Seen");
            inspectReport.setEndorser(staffId);
            inspectReport.setSignature(signature);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<InspectionReportEntity> getInspectionReports(String staffId) {

        DepotStaffEntity depotStaff = searchDepotStaff(staffId);
        Query q = em.createQuery("SELECT i FROM InspectionReportEntity i WHERE i.depotStaff=:depotStaff");
        q.setParameter("depotStaff", depotStaff);
        List<InspectionReportEntity> report = (List<InspectionReportEntity>) q.getResultList();

        return new ArrayList<InspectionReportEntity>(report);
    }

    @Override
    public ArrayList<InspectionReportEntity> getInspectionReports1(String nodeCode) {
        ArrayList<InspectionReportEntity> inspectReports = new ArrayList<InspectionReportEntity>();
        Query q = em.createQuery("SELECT i FROM InspectionReportEntity i");
        for (Object o : q.getResultList()) {
            InspectionReportEntity ii = (InspectionReportEntity) o;
            if (ii.getRollingStock().getDepot().getCode().equals(nodeCode)) {
                inspectReports.add(ii);
            }
        }
        return inspectReports;
    }

    @Override
    public InspectionReportEntity searchInspectReport(String inspectReportId) {
        InspectionReportEntity i = new InspectionReportEntity();
        try {
            Query q = em.createQuery("SELECT i FROM InspectionReportEntity AS i WHERE i.inspectReportId=:inspectReportId");
            q.setParameter("inspectReportId", inspectReportId);
            i = (InspectionReportEntity) q.getSingleResult();
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
    public boolean addIncidentRep(Date date, String location, String subject, String content, String staffId) {
        try {
            Query q = em.createQuery("SELECT i FROM IncidentReportEntity i");
            int spaceCount = q.getResultList().size();
            String incidentRepId = "IC" + (spaceCount + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT i1 FROM IncidentReportEntity i1 WHERE i1.incidentRepId=:incidentRepId");
                q1.setParameter("incidentRepId", incidentRepId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                spaceCount = spaceCount + 2;
                incidentRepId = "IC" + spaceCount;
            }

            Timestamp datedate = new Timestamp(date.getTime());
            incidentReportEntity = new IncidentReportEntity(incidentRepId, datedate, location, subject, content, "Submitted", null, staffId, null);
            em.persist(incidentReportEntity);

            ArrayList<StaffEntity> staffList = new ArrayList<StaffEntity>();
            Query query = em.createQuery("SELECT s FROM StaffEntity s");
            for (Object o : query.getResultList()) {
                staffList.add((StaffEntity) o);
            }

            for (int i = 0; i < staffList.size(); i++) {
                if (staffList.get(i).getStaffId().equals(staffId)) {
                    ArrayList temp = new ArrayList<IncidentReportEntity>(staffList.get(i).getIncidentReports());
                    temp.add(incidentReportEntity);
                    staffList.get(i).setIncidentReports(temp);
                    incidentReportEntity.setStaff(staffList.get(i));
                }
            }
            em.flush();
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
    public boolean updateIncidentRep(String incidentRepId, Date date, String subject, String content, String location, String signature, String submitter, String staffId) {
        IncidentReportEntity incidentRep = (IncidentReportEntity) searchIncidentRep(incidentRepId);
        if (em.contains(incidentRep)) {
            incidentRep.setStatus("Seen");
            incidentRep.setEndorser(staffId);
            incidentRep.setSignature(signature);
            em.flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<IncidentReportEntity> getIncidentReports(String staffId) {
        ArrayList<IncidentReportEntity> incidentReps = new ArrayList<IncidentReportEntity>();
        Query q = em.createQuery("SELECT i FROM IncidentReportEntity i");
        for (Object o : q.getResultList()) {
            IncidentReportEntity ii = (IncidentReportEntity) o;
            if (ii.getStaff().getStaffId().equals(staffId)) {
                incidentReps.add(ii);
            }
        }
        return incidentReps;
    }

    @Override
    public ArrayList<IncidentReportEntity> getIncidentReports1(String role, String nodeCode) {
        System.out.println(role + nodeCode + " role + nodeCode");
        ArrayList<IncidentReportEntity> incidentReps = new ArrayList<IncidentReportEntity>();
        Query q = em.createQuery("SELECT i FROM IncidentReportEntity i");
        for (Object o : q.getResultList()) {
            IncidentReportEntity ii = (IncidentReportEntity) o;
            String staffId = ii.getStaff().getStaffId();
            if (role.equals("Station Manager")) {
                StationStaffEntity staff = searchStationStaff(staffId);
                String code = staff.getStationTeam().getNode().getCode();
                if (code.equals(nodeCode)) {
                    incidentReps.add(ii);
                }
            } else if (role.equals("Depot Manager")) {
        System.out.println("got come in??? " );
                DepotStaffEntity staff = searchDepotStaff(staffId);
                String code = staff.getDepotTeam().getNode().getCode();
                if (code.equals(nodeCode)) {
                    incidentReps.add(ii);
                }
            } else { //HQ OR OTHER ROLE
                incidentReps.add(ii);
            }
        }
        if (incidentReps.isEmpty()) {

            System.out.println("empty incident reps");
        }
        return incidentReps;
    }

    private DepotStaffEntity searchDepotStaff(String staffId) {
        Query q = em.createQuery("SELECT s FROM DepotStaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        ArrayList<DepotStaffEntity> result = new ArrayList<DepotStaffEntity>(q.getResultList());
        DepotStaffEntity staff;
        if (result.isEmpty()) {
            return null;
        } else {
            staff = result.get(0);
            return staff;
        }
    }

    private StationStaffEntity searchStationStaff(String staffId) {
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
    public IncidentReportEntity searchIncidentRep(String incidentRepId) {
        IncidentReportEntity i = new IncidentReportEntity();
        try {
            Query q = em.createQuery("SELECT i FROM IncidentReportEntity AS i WHERE i.incidentRepId=:incidentRepId");
            q.setParameter("incidentRepId", incidentRepId);
            i = (IncidentReportEntity) q.getSingleResult();
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
    public MaintenanceRequestEntity submitMaintenanceRequest(Date date, String faulty, String rollingStock, String staffId, String code) {
        try {
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
            faulty = "Referring To Rolling Stock: " + rollingStock + "\n" + "Faulty Components: \n" + faulty;
            Timestamp datedate = new Timestamp(date.getTime());
            MaintenanceRequestEntity mainReqEntity = new MaintenanceRequestEntity(mainReqId, datedate, "Repair", faulty, "Received", staffId);
            RollingStockEntity rs = searchRollingStock(rollingStock);
            rs.setStatus("Under Maintenance");
            em.persist(rs);
            //Get the list of cabin 
            InfrastructureEntity temp = rs;
            List<AssetEntity> assetList = temp.getAssets();
            for (int i = 0; i < assetList.size(); i++) {
                if (assetList.get(i).getAssetId().substring(0, 2).equals("TC")) {
                    TrainCarEntity tc = (TrainCarEntity) assetList.get(i);
                    if (tc.getCarCode() == (Integer.parseInt(code))) {
                        mainReqEntity.setAsset(tc);
                        tc.setStatus("Under Maintenance");
                        tc.getMaintenanceRequests().add(mainReqEntity);
                        em.persist(mainReqEntity);
                        em.persist(tc);
                    }
                }
            }
            return mainReqEntity;
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return null;
    }

    /*
     public MaintenanceRequestEntity submitMaintenanceRequest1(Date date, String remark,String code,String staffId) {
       try {
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
           Timestamp datedate = new Timestamp(date.getTime());
           MaintenanceRequestEntity mainReqEntity = new MaintenanceRequestEntity(mainReqId, datedate, "Repair", remark, "Received",staffId);
           NodeEntity i = searchNode(code); //get the nodeEntity so we can get the InfraId
           InfrastructureEntity infra = searchInfra(i.getInfraId()); //Get the infra 
            if (em.contains(infra)) {
                mainReqEntity.setInfra(infra);
                em.persist(mainReqEntity);
                return mainReqEntity;
            } else {
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
    }*/
    @Override
    public List<TripReportEntity> getTripReports1(String nodeCode) {
        ArrayList<TripReportEntity> tripReportList = new ArrayList<TripReportEntity>();
        Query query = em.createQuery("SELECT t FROM TripReportEntity AS t");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                TripReportEntity t = (TripReportEntity) o;
                String code = t.getRollingStock().getDepot().getCode();
                if (nodeCode.equals(code)) {
                    tripReportList.add(t);
                }
            }
            return tripReportList;
        }
        return tripReportList;
    }

    @Override
    public List<TripReportEntity> getTripReports(String staffId) {
        ArrayList<TripReportEntity> tripReportList = new ArrayList<TripReportEntity>();
        Query query = em.createQuery("SELECT t FROM TripReportEntity AS t");
        if ((!query.getResultList().isEmpty())) {
            for (Object o : query.getResultList()) {
                TripReportEntity t = (TripReportEntity) o;
                if (t.getDepotStaff().getStaffId().equals(staffId)) {
                    tripReportList.add(t);
                }
            }
            return tripReportList;
        }
        return tripReportList;
    }

    @Override
    public TripReportEntity searchTripReport(String tripReportId) {
        TripReportEntity t = new TripReportEntity();
        try {
            Query q = em.createQuery("SELECT t FROM TripReportEntity AS t WHERE t.tripReportId=:tripReportId");
            q.setParameter("tripReportId", tripReportId);
            t = (TripReportEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return t;
    }

    @Override
    public boolean addTripReport(String reportType, Date date, String rollingStock, String code, String remarks, String staffId, String cabinLight, String headLight, String tailLight, String reverser, String switchPanel, String horn, String microphone, String speaker, String ledDisplay, String wipers, String washers) {
        try {
            String faulty = "";
            Query q = em.createQuery("SELECT t FROM TripReportEntity AS t");
            int rows = q.getResultList().size();
            String tripReportId = "TR" + (rows + 1);
            boolean status = true;
            while (status) {
                Query q1 = em.createQuery("SELECT t1 FROM TripReportEntity AS t1 WHERE t1.tripReportId=:tripReportId");
                q1.setParameter("tripReportId", tripReportId);
                if (q1.getResultList().isEmpty()) {
                    break;
                }
                rows = rows + 2;
                tripReportId = "TR" + rows;
            }
            if (cabinLight.equals("Faulty")) {
                faulty = faulty + "Cabin Light";
            }
            if (headLight.equals("Faulty")) {
                faulty = faulty + " || " + "Head Light";
            }
            if (tailLight.equals("Faulty")) {
                faulty = faulty + " || " + "Tail Light";
            }
            if (reverser.equals("Faulty")) {
                faulty = faulty + " || " + "Reverser";
            }
            if (switchPanel.equals("Faulty")) {
                faulty = faulty + " || " + "Swtich Panel";
            }
            if (horn.equals("Faulty")) {
                faulty = faulty + " || " + "Horn";
            }
            if (microphone.equals("Faulty")) {
                faulty = faulty + " || " + "Microphone";
            }
            if (speaker.equals("Faulty")) {
                faulty = faulty + " || " + "Speaker";
            }
            if (ledDisplay.equals("Faulty")) {
                faulty = faulty + " || " + "Led Display";
            }
            if (wipers.equals("Faulty")) {
                faulty = faulty + " || " + "Wipers";
            }
            if (washers.equals("Faulty")) {
                faulty = faulty + " || " + "Washers";
            }

            Timestamp datedate = new Timestamp(date.getTime());
            tripReportEntity = new TripReportEntity(tripReportId, datedate, "Post-Trip", remarks, cabinLight, headLight, tailLight, reverser, switchPanel, horn, microphone, speaker, ledDisplay, wipers, washers);
            DepotStaffEntity depotStaff = searchStaff(staffId);
            RollingStockEntity rollingstock = searchRollingStock(rollingStock);
            if (em.contains(depotStaff)) {
                tripReportEntity.setDepotStaff(depotStaff);
                if (em.contains(rollingstock)) {
                    tripReportEntity.setRollingStock(rollingstock);
                    if (faulty.equals("")) {
                    } else {
                        faulty = faulty + "\n\n" + remarks;
                        MaintenanceRequestEntity maintenanceStatus = submitMaintenanceRequest(date, faulty, rollingStock, staffId, code);
                        if (maintenanceStatus != null) {
                            tripReportEntity.setMaintenanceRequest(maintenanceStatus);
                        }
                    }
                }
                em.persist(tripReportEntity);
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

    private DepotStaffEntity searchStaff(String staffId) {
        Query q = em.createQuery("SELECT s FROM DepotStaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        ArrayList<DepotStaffEntity> result = new ArrayList<DepotStaffEntity>(q.getResultList());
        DepotStaffEntity staff;
        if (result.isEmpty()) {
            return null;
        } else {
            staff = result.get(0);
            return staff;
        }
    }

}
