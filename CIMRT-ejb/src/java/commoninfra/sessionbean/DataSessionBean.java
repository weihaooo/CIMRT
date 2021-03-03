/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commoninfra.sessionbean;

import businessPartner.entity.BusinessPartnerEntity;
import commoninfra.entity.AccessRightsEntity;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.DepotTeamEntity;
import commoninfra.entity.HqStaffEntity;
import commoninfra.entity.MessageEntity;
import commoninfra.entity.RoleEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import commoninfra.entity.StationTeamEntity;
import commoninfra.entity.TeamEntity;
import infraasset.entity.AdvertisementSpaceEntity;
import infraasset.entity.DepotEntity;
import infraasset.entity.LeasingSpaceEntity;
import infraasset.entity.NodeAssetEntity;
import infraasset.entity.StationEntity;
import infraasset.entity.TrainCarEntity;
import infraasset.sessionbean.InfraAssetSessionBeanLocal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import manpower.entity.ShiftEntity;
import manpower.entity.WorkshopEntity;
import operations.entity.IncidentReportEntity;
import operations.entity.JobApplicationsEntity;
import operations.entity.JobOfferEntity;
import operations.sessionbean.OperationsSessionBeanLocal;
import operations.sessionbean.ReportSessionBeanLocal;
import routefare.entity.FareAlgoEntity;
import routefare.entity.NodeEntity;
import routefare.entity.RouteEntity;
import routefare.entity.TrainScheduleEntity;
import routefare.sessionbean.RoutePlanningSessionBean;
import routefare.sessionbean.RoutePlanningSessionBeanLocal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import businessPartner.sessionbean.ProfileSessionBeanLocal;
import java.util.GregorianCalendar;
import java.util.Random;
import passenger.entity.CardEntity;
import passenger.entity.PassengerEntity;
import passenger.sessionbean.AcctSessionBeanLocal;
import passenger.sessionbean.CalculatorSessionBeanLocal;
import routefare.entity.FareTransactionEntity;
import simulator.entity.SimulatorTrainEntity;
import simulator.entity.SimulatorTrackEntity;

/**
 *
 * @author Yoona
 */
@Stateless
public class DataSessionBean implements DataSessionBeanLocal {

    @PersistenceContext
    EntityManager em;

    @EJB
    SystemAdminSessionBeanLocal systemAdminSessionBeanLocal;

    @EJB
    InfraAssetSessionBeanLocal infraAssetSessionBeanLocal;

    @EJB
    ReportSessionBeanLocal reportSessionBeanLocal;

    @EJB
    OperationsSessionBeanLocal OperationsSessionBeanLocal;

    @EJB
    RoutePlanningSessionBeanLocal routePlanningSessionBeanLocal;

    @EJB
    ProfileSessionBeanLocal profileSessionBeanLocal;

    @EJB
    AcctSessionBeanLocal acctSessionBeanLocal;
    
    @EJB
    CalculatorSessionBeanLocal calculatorSessionBeanLocal;

    final java.util.Random rand = new java.util.Random();

    @Asynchronous
    @Override
    public void createRosterBasis() {

 
        createRoles();
        addAccessRight();
        addTrainSchedule();
        addRoute();
        addFareStructure();
        addDummyStation();
        addDummyDepot();
        addDummyShift();
        createAdverSpace();
        createLeasingSpace();
        createConsumAsset();
        createNodeAsset();
        addDummyWorkshop();
        //addDummyMessage();
        addDummyTrainCar();
        //addDummyJobOffer();
        //addDummyJobApplications();
        addPartners();
        addPassengers();
       addSimulator();
       addDummyFareTransaction();
       //addDummyTripReport();
       //addDummyServiceLog();
        //    addDummyInspectionReport();
        //    addDummyIncidentReport();
        //routePlanningSessionBeanLocal.storeRegularDayTripStationSchedule();
        //routePlanningSessionBeanLocal.storeRegularDayTripStationScheduleWithRS();
        routePlanningSessionBeanLocal.testStoreRegularDayTripStationScheduleWithRS_NSL0115();
        //routePlanningSessionBeanLocal.testStoreRegularDayTripStationScheduleWithRS_NSL1501();

        Query query = em.createQuery("SELECT t FROM TeamEntity AS t");
        List<TeamEntity> team = (List<TeamEntity>) query.getResultList();

        if (team.isEmpty()) {
            //Create 148 station team, 4 for each of the 37 station
            for (int i = 0; i < 148; i++) {
                StationTeamEntity stationTeam = new StationTeamEntity(null, null);
                em.persist(stationTeam);
                em.flush();

            }

            //Retrieve all StationTeam
            Query q = em.createQuery("SELECT s FROM StationTeamEntity AS s ORDER BY s.teamId");
            List<StationTeamEntity> result = (List<StationTeamEntity>) q.getResultList();

            //Retrieve all Station
            Query q1 = em.createQuery("SELECT s FROM StationEntity AS s ORDER BY s.code");
            List<StationEntity> result1 = (List<StationEntity>) q1.getResultList();

            //Add 4 teams to all the 37 station
            for (int i = 0; i < 37; i++) {
                int count = i * 4;
                for (int x = 0; x < 4; x++) {
                    result1.get(i).addTeams(result.get(count));
                    result.get(count).setNode(result1.get(i));
                    count++;
                }
            }
            
            //Retrieve TrainDriver Role
            Query q2 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
            q2.setParameter("staffRole", "Train Driver");
            List<RoleEntity> result2 = q2.getResultList();

            //Retrieve all Depot
            Query q3 = em.createQuery("SELECT d FROM DepotEntity AS d ORDER BY d.code");
            List<DepotEntity> result3 = (List<DepotEntity>) q3.getResultList();

            //Create 16 team of Train Drivers
            for (int i = 0; i < 16; i++) {
                DepotTeamEntity depotTeam = new DepotTeamEntity(null, (RoleEntity) result2.get(0));
                em.persist(depotTeam);
                em.flush();
            }

            //Retrieve all DepotTeam
            Query q4 = em.createQuery("SELECT d FROM DepotTeamEntity AS d ORDER BY d.teamId");
            List<DepotTeamEntity> result4 = (List<DepotTeamEntity>) q4.getResultList();

            
            //Add 4 teams of Train Drivers to each of the 4 Depot
            for (int i = 0; i < 4; i++) {
                int count = i * 4;
                for (int x = 0; x < 4; x++) {
                    result3.get(i).addTeams(result4.get(count));
                    result4.get(count).setNode(result3.get(i));
                    count++;
                }
            }

            //Retrieve Maintenance Role
            Query q6 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
            q6.setParameter("staffRole", "Maintenance Staff");
            List<RoleEntity> result6 = q6.getResultList();

            //Create 16 teams of Maintenance Staff
            for (int i = 0; i < 16; i++) {
                DepotTeamEntity depotTeam = new DepotTeamEntity(null, (RoleEntity) result6.get(0));
                em.persist(depotTeam);
                em.flush();
            }

            //Retrieve all Maintenance Team
            Query q5 = em.createQuery("SELECT d FROM DepotTeamEntity as d WHERE d.role=:role");
            q5.setParameter("role", (RoleEntity) q6.getResultList().get(0));
            List<DepotTeamEntity> result5 = (List<DepotTeamEntity>) q5.getResultList();

            //Add 4 teams of Maintenance Staff to each of the 4 Depot
            for (int i = 0; i < 4; i++) {
                int count = i * 4;
                for (int x = 0; x < 4; x++) {
                    result3.get(i).addTeams(result5.get(count));
                    result5.get(count).setNode(result3.get(i));
                    count++;
                }
            }
        }
        createStaff();

        em.flush();
    }

    public void addDummyStation() {

        Query q = em.createQuery("SELECT s FROM StationEntity AS s");
        List<StationEntity> result = (List<StationEntity>) q.getResultList();

        if (result.isEmpty()) {
            StationEntity s1 = new StationEntity("IN1", "NSL01", "Tang Min", "Tang Min CIMRT", "NSL00", "NSL02", 0.00, -10.435611, 105.672425);
            StationEntity s2 = new StationEntity("IN2", "NSL02", "Greenfall", "Greenfall CIMRT", "NSL01", "NSL03", 2200.00, -10.441225, 105.671327);
            StationEntity s3 = new StationEntity("IN3", "NSL03", "Crystal chris", "Crystal chris CIMRT", "NSL02", "NSL04", 4600.00, -10.447765, 105.670084);
            StationEntity s4 = new StationEntity("IN4", "NSL04", "Fire Star", "Fire Star CIMRT", "NSL03", "NSL05", 6400.00, -10.455205, 105.669017);
            StationEntity s5 = new StationEntity("IN5", "NSL05", "Hsuyi Tin", "Hsuyi Tin CIMRT", "NSL04", "NSL06", 8400.00, -10.466036, 105.664855);
            StationEntity s6 = new StationEntity("IN6", "NSL06", "Toema", "Toema CIMRT", "NSL05", "NSL07", 10800.00, -10.471833, 105.662408);
            StationEntity s7 = new StationEntity("IN7", "NSL07", "Coral Plaza", "Coral Plaza CIMRT", "NSL06", "NSL08", 12000.00, -10.479492, 105.665665);
            StationEntity s8 = new StationEntity("IN8", "NSL08", "Justice Screen Centre", "Justice Screen Centre CIMRT", "NSL07", "NSL09", 13800.00, -10.482312, 105.658963);
            StationEntity s9 = new StationEntity("IN9", "NSL09", "Highland University", "Highland University CIMRT", "NSL08", "NSL10", 15000.00, -10.488727, 105.658504);
            StationEntity s10 = new StationEntity("IN10", "NSL10", "Cycle Square", "Cycle Square CIMRT", "NSL09", "NSL11", 17000.00, -10.501633, 105.655809);
            StationEntity s11 = new StationEntity("IN11", "NSL11", "Informal Sky", "Informal Sky CIMRT", "NSL10", "NSL12", 18800.00, -10.510850, 105.654817);
            StationEntity s12 = new StationEntity("IN12", "NSL12", "Salty Breeze", "Salty Breeze CIMRT", "NSL11", "NSL13", 20000.00, -10.520410, 105.652593);
            StationEntity s13 = new StationEntity("IN13", "NSL13", "Song Dolly", "Song Dolly CIMRT", "NSL12", "NSL14", 21400.00, -10.529137, 105.653145);
            StationEntity s14 = new StationEntity("IN14", "NSL14", "Joo Kia", "Joo Kia CIMRT", "NSL13", "NSL15", 22800.00, -10.537637, 105.653172);
            StationEntity s15 = new StationEntity("IN15", "NSL15", "Mun Yee", "Mun Yee CIMRT", "NSL14", "NSL16", 24000.00, -10.548149, 105.650831);

            StationEntity s16 = new StationEntity("IN16", "EWL01", "Fong Sheng", "Fong Sheng CIMRT", "EWL00", "EWL02", 0.00, -10.510630, 105.537675);
            StationEntity s17 = new StationEntity("IN17", "EWL02", "Caramel Stoll", "Caramel Stoll CIMRT", "EWL01", "EWL03", 2200.00, -10.506265, 105.544373);
            StationEntity s18 = new StationEntity("IN18", "EWL03", "Geping", "Geping CIMRT", "EWL02", "EWL04", 4600.00, -10.501770, 105.549281);
            StationEntity s19 = new StationEntity("IN19", "EWL04", "Fabio", "Fabio CIMRT", "EWL03", "EWL05", 7000.00, -10.494829, 105.555870);
            StationEntity s20 = new StationEntity("IN20", "EWL05", "Tang Stars", "Tang Stars CIMRT", "EWL04", "EWL06", 9000.00, -10.480984, 105.560081);
            StationEntity s21 = new StationEntity("IN21", "EWL06", "Hong Xing", "Hong Xing CIMRT", "EWL05", "EWL07", 11000.00, -10.480368, 105.567342);
            StationEntity s22 = new StationEntity("IN22", "EWL07", "Sideway high", "Sideway high CIMRT", "EWL06", "EWL08", 13000.00, -10.481234, 105.574684);
            StationEntity s23 = new StationEntity("IN23", "EWL08", "Velvet Red", "Velvet Red CIMRT", "EWL07", "EWL09", 14600.00, -10.483798, 105.581156);
            StationEntity s24 = new StationEntity("IN24", "EWL09", "Holland D", "Holland D CIMRT", "EWL08", "EWL10", 16200.00, -10.485239, 105.587123);
            StationEntity s25 = new StationEntity("IN25", "EWL10", "Western Garden", "Western Garden CIMRT", "EWL09", "EWL11", 17800.00, -10.487688, 105.595998);
            StationEntity s26 = new StationEntity("IN26", "EWL11", "Geoffrey Gray", "Geoffrey Gray CIMRT", "EWL10", "EWL12", 19200.00, -10.489909, 105.604889);
            StationEntity s27 = new StationEntity("IN27", "EWL12", "Chua Zhu Kang", "Chua Zhu Kang CIMRT", "EWL11", "EWL13", 20800.00, -10.492858, 105.613686);
            StationEntity s28 = new StationEntity("IN28", "EWL13", "Binggol", "Binggol CIMRT", "EWL12", "EWL14", 23000.00, -10.494809, 105.624880);
            StationEntity s29 = new StationEntity("IN29", "EWL14", "Lorang Chao", "Lorang Chao CIMRT", "EWL13", "EWL15", 25000.00, -10.496983, 105.634964);
            StationEntity s30 = new StationEntity("IN30", "EWL15", "Little Chinese", "Little Chinese CIMRT", "EWL14", "EWL16", 27000.00, -10.497327, 105.645338);
            StationEntity s31 = new StationEntity("IN31", "EWL16", "Justice Screen Centre", "Justice Screen Centre CIMRT", "EWL15", "EWL17", 29000.00, -10.482607, 105.659907);
            StationEntity s32 = new StationEntity("IN32", "EWL17", "Coral Plaza", "Coral Plaza CIMRT", "EWL16", "EWL18", 31400.00, -10.479492, 105.665735);
            StationEntity s33 = new StationEntity("IN33", "EWL18", "Old Langgey", "Old Langgey CIMRT", "EWL17", "EWL19", 33800.00, -10.476227, 105.673893);
            StationEntity s34 = new StationEntity("IN34", "EWL19", "Seng Mo", "Seng Mo CIMRT", "EWL18", "EWL20", 35200.00, -10.473285, 105.680725);
            StationEntity s35 = new StationEntity("IN35", "EWL20", "Kampong Square", "Kampong Square CIMRT", "EWL19", "EWL21", 36800.00, -10.471495, 105.688029);
            StationEntity s36 = new StationEntity("IN36", "EWL21", "Eastern Wuli", "Eastern Wuli CIMRT", "EWL20", "EWL22", 38000.00, -10.470125, 105.698386);
            StationEntity s37 = new StationEntity("IN37", "EWL22", "Coral Airport", "Coral Airport CIMRT", "EWL21", "EWL23", 40000.00, -10.469130, 105.705022);
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(s4);
            em.persist(s5);
            em.persist(s6);
            em.persist(s7);
            em.persist(s8);
            em.persist(s9);
            em.persist(s10);
            em.persist(s11);
            em.persist(s12);
            em.persist(s13);
            em.persist(s14);
            em.persist(s15);
            em.persist(s16);
            em.persist(s17);
            em.persist(s18);
            em.persist(s19);
            em.persist(s20);
            em.persist(s21);
            em.persist(s22);
            em.persist(s23);
            em.persist(s24);
            em.persist(s25);
            em.persist(s26);
            em.persist(s27);
            em.persist(s28);
            em.persist(s29);
            em.persist(s30);
            em.persist(s31);
            em.persist(s32);
            em.persist(s33);
            em.persist(s34);
            em.persist(s35);
            em.persist(s36);
            em.persist(s37);

            Query q2 = em.createQuery("SELECT s FROM StationEntity AS s");
            StationEntity s = new StationEntity();
            for (Object o : q2.getResultList()) {
                s = (StationEntity) o;
                String routeType = s.getCode().substring(0, 3);
                Set<RouteEntity> routeSet = new HashSet<RouteEntity>();
                Query query = em.createQuery("SELECT r FROM RouteEntity r");
                for (Object o1 : query.getResultList()) {
                    routeSet.add((RouteEntity) o1);
                }

                Iterator<RouteEntity> route = routeSet.iterator();
                while (route.hasNext()) {
                    RouteEntity r = route.next();
                    if (r.getCode().substring(0, 3).equals(routeType)) {

                        ArrayList<NodeEntity> nodes = new ArrayList<NodeEntity>(r.getNodes());
                        nodes.add(s);
                        r.setNodes(nodes);
                    }
                }
            }
        }
    }

    public void addDummyDepot() {

        Query q = em.createQuery("SELECT d FROM DepotEntity AS d");
        List<DepotEntity> result = (List<DepotEntity>) q.getResultList();

        if (result.isEmpty()) {
            DepotEntity d1 = new DepotEntity("IN38", "EWL00", "Rurong West", "Rurong West CIMRT", "EWL01", "EWL01", 3000.00, -10.512096, 105.535476);
            DepotEntity d2 = new DepotEntity("IN39", "EWL23", "East Coast", "East Coast CIMRT", "EWL22", "EWL22", 43400.00, -10.462289, 105.704928);
            DepotEntity d3 = new DepotEntity("IN40", "NSL00", "Flying Fish Cove", "Flying Fish Cove CIMRT", "NSL01", "NSL01", 3000.00, -10.431017, 105.673446);
            DepotEntity d4 = new DepotEntity("IN41", "NSL16", "Marina Bay", "Marina Bay CIMRT", "NSL15", "NSL15", 27200.00, -10.565124, 105.646623);
            em.persist(d1);
            em.persist(d2);
            em.persist(d3);
            em.persist(d4);
            Query q2 = em.createQuery("SELECT d FROM DepotEntity AS d");
            DepotEntity d = new DepotEntity();
            for (Object o : q2.getResultList()) {
                d = (DepotEntity) o;
                String routeType = d.getCode().substring(0, 3);
                Set<RouteEntity> routeSet = new HashSet<RouteEntity>();
                Query query = em.createQuery("SELECT r FROM RouteEntity r");
                for (Object o1 : query.getResultList()) {
                    routeSet.add((RouteEntity) o1);
                }

                Iterator<RouteEntity> route = routeSet.iterator();
                while (route.hasNext()) {
                    RouteEntity r = route.next();
                    if (r.getCode().substring(0, 3).equals(routeType)) {

                        ArrayList<NodeEntity> nodes = new ArrayList<NodeEntity>(r.getNodes());
                        nodes.add(d);
                        r.setNodes(nodes);
                    }
                }
            }
        }
    }

    public void addDummyShift() {

        Query q = em.createQuery("SELECT s FROM ShiftEntity AS s");
        List<ShiftEntity> result = (List<ShiftEntity>) q.getResultList();

        if (result.isEmpty()) {
            ShiftEntity s1 = new ShiftEntity("SS1", "Station Staff Morning Shift", convertToTimestamp("04:00"), convertToTimestamp("11:00"));
            ShiftEntity s2 = new ShiftEntity("SS2", "Station Staff Afternoon Shift", convertToTimestamp("11:00"), convertToTimestamp("18:00"));
            ShiftEntity s3 = new ShiftEntity("SS3", "Station Staff Evening Shift", convertToTimestamp("18:00"), convertToTimestamp("01:00"));
            ShiftEntity s4 = new ShiftEntity("SS4", "Station Staff Off Day", null, null);

            ShiftEntity s5 = new ShiftEntity("TD1", "Train Driver Morning Shift", convertToTimestamp("04:00"), convertToTimestamp("11:00"));
            ShiftEntity s6 = new ShiftEntity("TD2", "Train Driver Afternoon Shift", convertToTimestamp("11:00"), convertToTimestamp("18:00"));
            ShiftEntity s7 = new ShiftEntity("TD3", "Train Driver Evening Shift", convertToTimestamp("18:00"), convertToTimestamp("01:00"));
            ShiftEntity s8 = new ShiftEntity("TD4", "Train Driver Off Day", null, null);

            ShiftEntity s9 = new ShiftEntity("MS1", "Maintenance Staff Morning Shift", convertToTimestamp("05:00"), convertToTimestamp("13:00"));
            ShiftEntity s10 = new ShiftEntity("MS2", "Maintenance Staff Afternoon Shift", convertToTimestamp("13:00"), convertToTimestamp("21:00"));
            ShiftEntity s11 = new ShiftEntity("MS3", "Maintenance Staff Evening Shift", convertToTimestamp("21:00"), convertToTimestamp("05:00"));
            ShiftEntity s12 = new ShiftEntity("MS4", "Maintenance Staff Off Day", null, null);

            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(s4);
            em.persist(s5);
            em.persist(s6);
            em.persist(s7);
            em.persist(s8);
            em.persist(s9);
            em.persist(s10);
            em.persist(s11);
            em.persist(s12);
        }

    }

    public Timestamp convertToTimestamp(String timeStr) {
        SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm");
        Timestamp time = new Timestamp(60);
        try {
            time = new Timestamp(printFormat.parse(timeStr).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(RoutePlanningSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }

    public void createRoles() {
        Query q = em.createQuery("SELECT r FROM RoleEntity AS r");
        List<RoleEntity> result = (List<RoleEntity>) q.getResultList();

        if (result.isEmpty()) {
            RoleEntity r1 = new RoleEntity("Super Admin", "HQ");
            RoleEntity r2 = new RoleEntity("System Admin", "HQ");
            RoleEntity r3 = new RoleEntity("Route & Fare Manager", "HQ");
            RoleEntity r4 = new RoleEntity("Data Analyst", "HQ");
            RoleEntity r5 = new RoleEntity("I&A Manager", "HQ");
            RoleEntity r6 = new RoleEntity("I&A Staff", "HQ");
            RoleEntity r7 = new RoleEntity("Advertising Staff", "HQ");
            RoleEntity r8 = new RoleEntity("Advertising Manager", "HQ");
            RoleEntity r9 = new RoleEntity("Commercial Staff", "HQ");
            RoleEntity r10 = new RoleEntity("Commercial Manager", "HQ");
            RoleEntity r11 = new RoleEntity("HR", "HQ");
            RoleEntity r12 = new RoleEntity("Station Manager", "Station");
            RoleEntity r13 = new RoleEntity("Depot Manager", "Depot");
            RoleEntity r14 = new RoleEntity("Station Staff", "Station");
            RoleEntity r15 = new RoleEntity("Train Driver", "Depot");
            RoleEntity r16 = new RoleEntity("Maintenance Staff", "Depot");
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.persist(r4);
            em.persist(r5);
            em.persist(r6);
            em.persist(r7);
            em.persist(r8);
            em.persist(r9);
            em.persist(r10);
            em.persist(r11);
            em.persist(r12);
            em.persist(r13);
            em.persist(r14);
            em.persist(r15);
            em.persist(r16);
        }
    }

    public void addAccessRight() {
        Query query = em.createQuery("SELECT a FROM AccessRightsEntity a");
        if (query.getResultList().isEmpty()) {
            AccessRightsEntity a1 = new AccessRightsEntity("User Workspace");
            AccessRightsEntity a2 = new AccessRightsEntity("Account Management");
            AccessRightsEntity a3 = new AccessRightsEntity("Internal Messaging");
            AccessRightsEntity a4 = new AccessRightsEntity("Route Management");
            AccessRightsEntity a5 = new AccessRightsEntity("Fare Management");
            AccessRightsEntity a6 = new AccessRightsEntity("Data Analysis");
            AccessRightsEntity a7 = new AccessRightsEntity("Service Request");
            AccessRightsEntity a8 = new AccessRightsEntity("Inspection Log Management");
            AccessRightsEntity a9 = new AccessRightsEntity("Maintenance Report");
            AccessRightsEntity a10 = new AccessRightsEntity("I&A Management");
            AccessRightsEntity a11 = new AccessRightsEntity("Advertising Management");
            AccessRightsEntity a12 = new AccessRightsEntity("Leasing Management");
            AccessRightsEntity a13 = new AccessRightsEntity("Recruitment Management");
            AccessRightsEntity a14 = new AccessRightsEntity("Employee Management");
            AccessRightsEntity a15 = new AccessRightsEntity("Entitlement Management");
            AccessRightsEntity a16 = new AccessRightsEntity("Workshop Management");
            AccessRightsEntity a17 = new AccessRightsEntity("Service Management");
            AccessRightsEntity a18 = new AccessRightsEntity("Asset Management");
            AccessRightsEntity a19 = new AccessRightsEntity("Report Management");
            AccessRightsEntity a20 = new AccessRightsEntity("Attendance Management");
            AccessRightsEntity a21 = new AccessRightsEntity("Activation");
            AccessRightsEntity a22 = new AccessRightsEntity("Train Schedule Management");
            AccessRightsEntity a23 = new AccessRightsEntity("Announcement Management");
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.persist(a4);
            em.persist(a5);
            em.persist(a6);
            em.persist(a7);
            em.persist(a8);
            em.persist(a9);
            em.persist(a10);
            em.persist(a11);
            em.persist(a12);
            em.persist(a13);
            em.persist(a14);
            em.persist(a15);
            em.persist(a16);
            em.persist(a17);
            em.persist(a18);
            em.persist(a19);
            em.persist(a20);
            em.persist(a21);
            em.persist(a22);
            em.persist(a23);
        }
    }

    public void createStaff() {

        String[] name = {"John", "Marcus", "Susan", "Henry", "Wei Hao", "Kayley", "Hui Ming", "Jaden", "Zhi Rong", "Fabian", "Damin", "Elaine", "Zhu Ming", "Yu Ting", "En Hao", "Jay", "Jun Xing"};

        int nric = 0;

        Query query = em.createQuery("SELECT s FROM StaffEntity AS s");
        List<StaffEntity> staff = (List<StaffEntity>) query.getResultList();

        if (staff.isEmpty()) {

            Query q = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole!='Train Driver' AND r.staffRole!='Maintenance Staff' AND r.staffRole!='Depot Manager' AND r.staffRole!='Station Manager' AND r.staffRole!='Station Staff'");
            List<RoleEntity> result = (List<RoleEntity>) q.getResultList();

            Query q1 = em.createQuery("SELECT s FROM StationEntity AS s ORDER BY s.code");
            List<StationEntity> result1 = (List<StationEntity>) q1.getResultList();

            Query q2 = em.createQuery("SELECT s FROM StationTeamEntity AS s ORDER BY s.teamId");
            List<StationTeamEntity> result2 = (List<StationTeamEntity>) q2.getResultList();

            Query q3 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole='Station Manager'");
            List<RoleEntity> result3 = (List<RoleEntity>) q3.getResultList();

            Query q4 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole='Station Staff'");
            List<RoleEntity> result4 = (List<RoleEntity>) q4.getResultList();

            Query q100 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole='Super Admin'");
            List<RoleEntity> result100 = (List<RoleEntity>) q100.getResultList();
            Query q101 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole='HR'");
            List<RoleEntity> result101 = (List<RoleEntity>) q101.getResultList();
            Query q102 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole='I&A Staff'");
            List<RoleEntity> result102 = (List<RoleEntity>) q102.getResultList();

            systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "HQ", result100.get(0).getStaffRole(), 1000, "");
            nric++;
            systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "HQ", result101.get(0).getStaffRole(), 1000, "");
            nric++;
            systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "HQ", result102.get(0).getStaffRole(), 1000, "");
            nric++;
            for (int i = 0; i < 30; i++) {
                systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "HQ", result.get(rand.nextInt(11)).getStaffRole(), 1000, "");
                nric++;
            }
            String id;
            for (int i = 0; i < result2.size(); i++) {
                for (int x = 0; x < 7; x++) {
                    if (x == 0) {
                        id = systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "Station", result3.get(0).getStaffRole(), 1000, result2.get(i).getTeamId().toString());

                    } else {
                        id = systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "Station", result4.get(0).getStaffRole(), 1000, result2.get(i).getTeamId().toString());
                    }
                    Query q5 = em.createQuery("SELECT s FROM StationStaffEntity AS s WHERE s.staffId=:staffId");
                    q5.setParameter("staffId", id);
                    List<StationStaffEntity> result5 = (List<StationStaffEntity>) q5.getResultList();
                    if (!result5.isEmpty()) {
                        result2.get(i).addStaff(result5.get(0));
                    }
                    nric++;
                }
            }
            Query q6 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
            q6.setParameter("staffRole", "Train Driver");
            List<RoleEntity> result6 = (List<RoleEntity>) q6.getResultList();

            Query q10 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
            q10.setParameter("staffRole", "Depot Manager");
            List<RoleEntity> result10 = (List<RoleEntity>) q10.getResultList();

            Query q8 = em.createQuery("SELECT r FROM RoleEntity AS r WHERE r.staffRole=:staffRole");
            q8.setParameter("staffRole", "Maintenance Staff");
            List<RoleEntity> result8 = (List<RoleEntity>) q8.getResultList();

            Query q7 = em.createQuery("SELECT d FROM DepotTeamEntity as d WHERE d.role=:role");
            q7.setParameter("role", result6.get(0));
            List<DepotTeamEntity> result7 = (List<DepotTeamEntity>) q7.getResultList();

            Query q9 = em.createQuery("SELECT d FROM DepotTeamEntity as d WHERE d.role=:role");
            q9.setParameter("role", result8.get(0));
            List<DepotTeamEntity> result9 = (List<DepotTeamEntity>) q9.getResultList();

            for (int i = 0; i < result9.size(); i++) {
                for (int x = 0; x < 21; x++) {

                    if (x == 0) {
                        id = systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "Depot", result10.get(0).getStaffRole(), 1000, result9.get(i).getTeamId().toString());

                    } else {
                        id = systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "Depot", result8.get(0).getStaffRole(), 1000, result9.get(i).getTeamId().toString());

                    }
                    Query q5 = em.createQuery("SELECT d FROM DepotStaffEntity AS d WHERE d.staffId=:staffId");
                    q5.setParameter("staffId", id);
                    List<DepotStaffEntity> result5 = (List<DepotStaffEntity>) q5.getResultList();
                    if (!result5.isEmpty()) {
                        result9.get(i).addStaff(result5.get(0));
                    }
                    nric++;
                }
            }
            for (int i = 0; i < result7.size(); i++) {
                for (int x = 0; x < 30; x++) {
                    id = systemAdminSessionBeanLocal.createAccount(name[rand.nextInt(17)], "", "nric" + nric, "96743264", "e0002252@u.nus.edu", "", "M", new Date(), "", "", "", "", "", "Depot", result6.get(0).getStaffRole(), 1000, result7.get(i).getTeamId().toString());

                    Query q5 = em.createQuery("SELECT d FROM DepotStaffEntity AS d WHERE d.staffId=:staffId");
                    q5.setParameter("staffId", id);
                    List<DepotStaffEntity> result5 = (List<DepotStaffEntity>) q5.getResultList();
                    if (!result5.isEmpty()) {
                        result7.get(i).addStaff(result5.get(0));
                    }
                    nric++;
                }
            }
        }
    }

    public void createAdverSpace() {

        String[] location = {"On the Platform", "On the Escalator", "Inside the Lift", "Outside the Lift", "Outside Gantry Area", "Top Up Machine Area", "Ground Floor Area", "Transit Area"};

        String[] platformType = {"Digital Board on Station", "Non Digital Board on Station", "Station Sticker", "Station Concept Space"};

        String[] escalatorType = {"Escalator Panel", "Escalator Sticker"};
        String[] liftAndGroundType = {"Digital Panel", "Non Digital Panel"};
        String[] letter = {"A","B","C"};

        Query query = em.createQuery("SELECT a FROM AdvertisementSpaceEntity AS a");
        List<AdvertisementSpaceEntity> adSpace = (List<AdvertisementSpaceEntity>) query.getResultList();

        if (adSpace.isEmpty()) {

            Query q1 = em.createQuery("SELECT s FROM StationEntity AS s");
            List<StationEntity> result1 = (List<StationEntity>) q1.getResultList();

            for (int i = 0; i < result1.size(); i++) {
                for (int x = 0; x < 4; x++) {
                    for(int a=0;a<3;a++){
                    infraAssetSessionBeanLocal.addAdvertisementSpace("Station", result1.get(i).getCode(), location[0], platformType[x], Double.parseDouble("20000"), Double.parseDouble("10000"),letter[a],dateAndTimeConvertToTimestamp("2017-12-30 23:59:00.0"));
                    }
                }
                for (int y = 0; y < 2; y++) {
                    
                    for(int a=0;a<3;a++){
                    infraAssetSessionBeanLocal.addAdvertisementSpace("Station", result1.get(i).getCode(), location[1], escalatorType[y], Double.parseDouble("10000"), Double.parseDouble("5000"),letter[a],dateAndTimeConvertToTimestamp("2017-12-30 23:59:00.0"));
                    }
                }
                for (int z = 0; z < 6; z++) {
                    for(int a=0;a<3;a++){
                    infraAssetSessionBeanLocal.addAdvertisementSpace("Station", result1.get(i).getCode(), location[z + 2], liftAndGroundType[0], Double.parseDouble("10000"), Double.parseDouble("5000"),letter[a],dateAndTimeConvertToTimestamp("2017-12-30 23:59:00.0"));
                    infraAssetSessionBeanLocal.addAdvertisementSpace("Station", result1.get(i).getCode(), location[z + 2], liftAndGroundType[1], Double.parseDouble("10000"), Double.parseDouble("5000"),letter[a],dateAndTimeConvertToTimestamp("2017-12-30 23:59:00.0"));
                
                    }}
            }
        }

    }

    public void createLeasingSpace() {

        String[] assetName = {"Retail", "Commercial", "Food and Beverage"};

        Query query = em.createQuery("SELECT l FROM LeasingSpaceEntity AS l");
        List<LeasingSpaceEntity> lSpace = (List<LeasingSpaceEntity>) query.getResultList();

        if (lSpace.isEmpty()) {

            Query q1 = em.createQuery("SELECT s FROM StationEntity AS s");
            List<StationEntity> result1 = (List<StationEntity>) q1.getResultList();

            for (int i = 0; i < result1.size(); i++) {
                for (int x = 0; x < 9; x++) {
                    infraAssetSessionBeanLocal.addLeasingSpace(assetName[rand.nextInt(3)], result1.get(i).getCode(), "#01-0" + (x + 1) + "A", Double.parseDouble("200"), true, Double.parseDouble("3000"), 99, "", dateAndTimeConvertToTimestamp("2017-12-30 23:59:00.0"));
                }
            }
        }

    }

    public void createNodeAsset() {

        String[] assetType = {"Office Equipments", "Operating Equipments", "Audio Visual Equipments", "Photographic Equipments", "Computer Equipments", "Decorations", "Others", "Maintenance Equipments"};
        String[] nodeOperatingEq = {"Ticketing Machine", "Escalator", "Lift", "Ticketing Gantry", "Platform Doors"};
        String[] nodeMaintenanceEq = {"Infrared Thermal Imaging Tool", "Vibration Analysis Tool", "Oil Analysis Tool", "Track Surfacing Machine", "Train Surface Polishing Machine", "Torque wrench", "Hammer", "Drilling Machine", "Philips and Flathead Screwdrivers"};
        String[] nodeAudioVisualEq = {"Digital Panel", "Announcement Mic", "Portable Projection Screens", "Bluetooth Speakers"};
        String[] nodePhotographicEq = {"DSL85 Camera", "Tripod", "Handheld Quick Camera", "GoPro", "GoPro Accessories"};
        String[] nodeOfficeEq = {"Water Boiler", "Extension Cord", "Chair", "Table", "Printer", "Office Phones"};
        String[] nodeDeco = {"Warning Signs", "Duct Tape", "White Tape", "Bulletin Board"};
        String[] nodeComEq = {"Desktop", "Laptops", "Server Racks", "Servers", "Internet Modem", "Routers"};

        Query query = em.createQuery("SELECT n FROM NodeAssetEntity AS n");
        List<NodeAssetEntity> nAsset = (List<NodeAssetEntity>) query.getResultList();

        if (nAsset.isEmpty()) {

            Query q1 = em.createQuery("SELECT n FROM NodeEntity AS n");
            List<NodeEntity> result1 = (List<NodeEntity>) q1.getResultList();

            for (int i = 0; i < result1.size(); i++) {
                infraAssetSessionBeanLocal.addNodeAsset(nodeOperatingEq[0], result1.get(i).getCode(), 99, new Date(), 4, assetType[1]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOperatingEq[1], result1.get(i).getCode(), 99, new Date(), 4, assetType[1]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOperatingEq[2], result1.get(i).getCode(), 99, new Date(), 4, assetType[1]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOperatingEq[3], result1.get(i).getCode(), 99, new Date(), 14, assetType[1]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOperatingEq[4], result1.get(i).getCode(), 99, new Date(), 24, assetType[1]);

                if (result1.get(i) instanceof DepotEntity) {
                    for (int x = 0; x < 9; x++) {
                        infraAssetSessionBeanLocal.addNodeAsset(nodeMaintenanceEq[x], result1.get(i).getCode(), 10, new Date(), 4, assetType[7]);
                    }
                }
                for (int y = 0; y < 4; y++) {
                    infraAssetSessionBeanLocal.addNodeAsset(nodeAudioVisualEq[y], result1.get(i).getCode(), 5, new Date(), 4, assetType[2]);
                }
                for (int y = 0; y < 5; y++) {
                    infraAssetSessionBeanLocal.addNodeAsset(nodePhotographicEq[y], result1.get(i).getCode(), 5, new Date(), 4, assetType[3]);
                }

                infraAssetSessionBeanLocal.addNodeAsset(nodeOfficeEq[0], result1.get(i).getCode(), 8, new Date(), 4, assetType[0]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOfficeEq[1], result1.get(i).getCode(), 8, new Date(), 10, assetType[0]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOfficeEq[2], result1.get(i).getCode(), 8, new Date(), 10, assetType[0]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOfficeEq[3], result1.get(i).getCode(), 8, new Date(), 5, assetType[0]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOfficeEq[4], result1.get(i).getCode(), 8, new Date(), 2, assetType[0]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeOfficeEq[5], result1.get(i).getCode(), 8, new Date(), 4, assetType[0]);

                infraAssetSessionBeanLocal.addNodeAsset(nodeDeco[0], result1.get(i).getCode(), 20, new Date(), 10, assetType[5]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeDeco[1], result1.get(i).getCode(), 20, new Date(), 30, assetType[5]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeDeco[2], result1.get(i).getCode(), 20, new Date(), 30, assetType[5]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeDeco[3], result1.get(i).getCode(), 20, new Date(), 10, assetType[5]);

                infraAssetSessionBeanLocal.addNodeAsset(nodeComEq[0], result1.get(i).getCode(), 5, new Date(), 5, assetType[4]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeComEq[1], result1.get(i).getCode(), 5, new Date(), 3, assetType[4]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeComEq[2], result1.get(i).getCode(), 10, new Date(), 2, assetType[4]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeComEq[3], result1.get(i).getCode(), 10, new Date(), 2, assetType[4]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeComEq[4], result1.get(i).getCode(), 10, new Date(), 2, assetType[4]);
                infraAssetSessionBeanLocal.addNodeAsset(nodeComEq[5], result1.get(i).getCode(), 10, new Date(), 4, assetType[4]);
            }
        }

    }

    public void createConsumAsset() {

        String[] assetType = {"Office Supplies", "Cleaning Supplies", "Food & Beverage Supplies", "Medicine Supplies", "Operating Supplies", "Maintenance Supplies", "Others"};
        String[] firstAid = {"First Aid Kit (Expiry  3-5 years)"};
        String[] foodSupplies = {"Almonds Nuts Carton (24pax)",
            "Cereal Carton (24pax)",
            "Protein Bar Carton (24pax)",
            "Wasabi Peas Carton (24pax)",
            "Peanut Carton (24pax)",
            "Nestle Milo Carton (36pax)",
            "Nestle Holicks Carton (36pax)",
            "Coke Carton (24pax)",
            "Mug Carton (24pax)",
            "Ice Lemon Tea Carton (24pax)",
            "Green Tea (24pax)",
            "Mineral Water Bottle (32pax)"};
        String[] officeSupplies = {"Toner cartridges"};
        String[] cleaningSupplies = {"Broom", "Mop", "Dustpan", "Bucket", "Vacuum Cleaner", "Sponge", "Scrub Brush", "Microfiber Cloths", "Rubber Gloves", "Air Freshener", "Dishwashing Mama Lemon", "Garbage Bags", "Toilet Rolls"};

        Query query = em.createQuery("SELECT n FROM NodeAssetEntity AS n");
        List<NodeAssetEntity> nAsset = (List<NodeAssetEntity>) query.getResultList();

        if (nAsset.isEmpty()) {

            Query q1 = em.createQuery("SELECT n FROM NodeEntity AS n");
            List<NodeEntity> result1 = (List<NodeEntity>) q1.getResultList();

            for (int i = 0; i < result1.size() - 30; i++) {

                infraAssetSessionBeanLocal.addConsumableAsset(firstAid[0], result1.get(i).getCode(), 5, changeYear(new Date(), 3), assetType[3]);
                for (int y = 0; y < 12; y++) {
                    infraAssetSessionBeanLocal.addConsumableAsset(foodSupplies[y], result1.get(i).getCode(), 10, changeYear(new Date(), 3), assetType[2]);
                }
                infraAssetSessionBeanLocal.addConsumableAsset(officeSupplies[0], result1.get(i).getCode(), 20, changeYear(new Date(), 3), assetType[0]);
                for (int y = 0; y < 13; y++) {
                    infraAssetSessionBeanLocal.addConsumableAsset(cleaningSupplies[y], result1.get(i).getCode(), 10, changeYear(new Date(), 5), assetType[1]);
                }

            }

        }
    }

    public Date changeYear(Date date, int change) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, change);
        return cal.getTime();
    }

    public void addRoute() {
        Query q = em.createQuery("SELECT r FROM RouteEntity AS r");
        List<RouteEntity> result = (List<RouteEntity>) q.getResultList();

        if (result.isEmpty()) {
            RouteEntity route1 = new RouteEntity();
            route1.setCode("NSL01-15");
            RouteEntity route2 = new RouteEntity();
            route2.setCode("NSL15-01");
            RouteEntity route3 = new RouteEntity();
            route3.setCode("EWL01-22");
            RouteEntity route4 = new RouteEntity();
            route4.setCode("EWL22-01");
            em.persist(route1);
            em.persist(route2);
            em.persist(route3);
            em.persist(route4);

            List<TrainScheduleEntity> tsList = new ArrayList<>();
            Query query = em.createQuery("SELECT ts FROM TrainScheduleEntity ts");
            for (Object o : query.getResultList()) {
                TrainScheduleEntity ts = (TrainScheduleEntity) o;
                tsList.add(ts);
            }
            route1.setTrainSchedules(tsList);
            route2.setTrainSchedules(tsList);
            route3.setTrainSchedules(tsList);
            route4.setTrainSchedules(tsList);
        }
    }

    public void addFareStructure() {
        Query q = em.createQuery("SELECT f FROM FareAlgoEntity AS f");
        List<FareAlgoEntity> result = (List<FareAlgoEntity>) q.getResultList();

        if (result.isEmpty()) {
            FareAlgoEntity f1 = new FareAlgoEntity("Adult", "Off Peak", 0.77, 0.10);
            FareAlgoEntity f2 = new FareAlgoEntity("Adult", "Peak", 0.97, 0.10);
            FareAlgoEntity f3 = new FareAlgoEntity("Adult", "Concession", 80, 0);

            FareAlgoEntity f4 = new FareAlgoEntity("Student", "Off Peak", 0.37, 0.05);
            FareAlgoEntity f5 = new FareAlgoEntity("Student", "Concession", 45, 0);

            FareAlgoEntity f6 = new FareAlgoEntity("Senior Citizen", "Off Peak", 0.54, 0.05);
            FareAlgoEntity f7 = new FareAlgoEntity("Senior Citizen", "Concession", 40, 0);

            FareAlgoEntity f8 = new FareAlgoEntity("Short-term Visitor", "Concession", 25, 0);
            em.persist(f1);
            em.persist(f2);
            em.persist(f3);
            em.persist(f4);
            em.persist(f5);
            em.persist(f6);
            em.persist(f7);
            em.persist(f8);

            List<TrainScheduleEntity> tsList = new ArrayList<>();
            Query query = em.createQuery("SELECT ts FROM TrainScheduleEntity ts");
            for (Object o : query.getResultList()) {
                TrainScheduleEntity ts = (TrainScheduleEntity) o;
                tsList.add(ts);
            }
            f1.setTrainSchedules(tsList);
            f2.setTrainSchedules(tsList);
            f3.setTrainSchedules(tsList);
            f4.setTrainSchedules(tsList);
            f5.setTrainSchedules(tsList);
            f6.setTrainSchedules(tsList);
            f7.setTrainSchedules(tsList);
            f8.setTrainSchedules(tsList);
        }
    }

    public void addTrainSchedule() {
        Query q = em.createQuery("SELECT ts FROM TrainScheduleEntity AS ts");
        List<TrainScheduleEntity> result = (List<TrainScheduleEntity>) q.getResultList();

        if (result.isEmpty()) {
            TrainScheduleEntity ts1 = new TrainScheduleEntity("Weekday", "Off Peak", dateAndTimeConvertToTimestamp("1970-01-01 05:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 07:30:00.0"), 5, 30, "new");
            TrainScheduleEntity ts2 = new TrainScheduleEntity("Weekday", "Peak", dateAndTimeConvertToTimestamp("1970-01-01 07:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 09:30:00.0"), 2, 30, "new");
            TrainScheduleEntity ts3 = new TrainScheduleEntity("Weekday", "Off Peak", dateAndTimeConvertToTimestamp("1970-01-01 09:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 17:30:00.0"), 5, 30, "new");
            TrainScheduleEntity ts4 = new TrainScheduleEntity("Weekday", "Peak", dateAndTimeConvertToTimestamp("1970-01-01 17:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 19:30:00.0"), 2, 30, "new");
            TrainScheduleEntity ts5 = new TrainScheduleEntity("Weekday", "Off Peak", dateAndTimeConvertToTimestamp("1970-01-01 19:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 22:30:00.0"), 5, 30, "new");
            TrainScheduleEntity ts6 = new TrainScheduleEntity("Weekday", "Last Hour", dateAndTimeConvertToTimestamp("1970-01-01 22:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 23:30:00.0"), 8, 30, "new");
            TrainScheduleEntity ts7 = new TrainScheduleEntity("Saturday", "Off Peak", dateAndTimeConvertToTimestamp("1970-01-01 05:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 22:30:00.0"), 3, 30, "new");
            TrainScheduleEntity ts8 = new TrainScheduleEntity("Saturday", "Last Hour", dateAndTimeConvertToTimestamp("1970-01-01 22:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 23:30:00.0"), 6, 30, "new");
            TrainScheduleEntity ts9 = new TrainScheduleEntity("Sunday", "Off Peak", dateAndTimeConvertToTimestamp("1970-01-01 05:50:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 22:30:00.0"), 3, 30, "new");
            TrainScheduleEntity ts10 = new TrainScheduleEntity("Sunday", "Last Hour", dateAndTimeConvertToTimestamp("1970-01-01 22:30:00.0"), dateAndTimeConvertToTimestamp("1970-01-01 23:30:00.0"), 6, 30, "new");
            em.persist(ts1);
            em.persist(ts2);
            em.persist(ts3);
            em.persist(ts4);
            em.persist(ts5);
            em.persist(ts6);
            em.persist(ts7);
            em.persist(ts8);
            em.persist(ts9);
            em.persist(ts10);
        }
    }

    public Timestamp dateAndTimeConvertToTimestamp(String timeStr) {
        SimpleDateFormat printFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
        Timestamp time = new Timestamp(60);
        try {
            time = new Timestamp(printFormat.parse(timeStr).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(RoutePlanningSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }

    public void addDummyWorkshop() {

        Query q = em.createQuery("SELECT w FROM WorkshopEntity AS w");
        List<WorkshopEntity> result = (List<WorkshopEntity>) q.getResultList();
        Date date = new Date();
        Timestamp messageDate = new Timestamp(date.getTime());
        Date date2 = new Date();
        Timestamp workshopStartDate = new Timestamp(date2.getTime());
        Date date3 = new Date(117, 12, 12);
        Timestamp workshopEndDate = new Timestamp(date3.getTime());
        Date recentDate = new Date(116, 1, 1);
        Date recentDate2 = new Date(116, 2, 1);
        Date recentEndWorkshop = new Date(117, 10, 10);

        if (result.isEmpty()) {
            WorkshopEntity w1 = new WorkshopEntity("Java EE1", "Java EE", workshopStartDate, workshopEndDate, "Java EE Workshop", "1100HRS", "1300HRS", "External");
            WorkshopEntity w2 = new WorkshopEntity("Java EE2", "Java EE2", workshopStartDate, workshopEndDate, "Java EE2 Workshop", "1300HRS", "1500HRS", "External");
            WorkshopEntity w3 = new WorkshopEntity("Safety1", "Safety1", recentDate, recentDate2, "Java EE3 Workshop", "1700HRS", "2000HRS", "Safety");
            WorkshopEntity w4 = new WorkshopEntity("Safety2", "Safety2", workshopStartDate, recentEndWorkshop, "Java EE4 Workshop", "1400HRS", "1600HRS", "Safety");

            em.persist(w1);
            em.persist(w2);
            em.persist(w3);
            em.persist(w4);

        }

    }

    public void addDummyMessage() {
        Query q = em.createQuery("SELECT m FROM MessageEntity AS m");
        List<MessageEntity> result = (List<MessageEntity>) q.getResultList();
        Date date = new Date();
        Timestamp messageDate = new Timestamp(date.getTime());
        Date date2 = new Date();
        Timestamp workshopStartDate = new Timestamp(date2.getTime());
        Date date3 = new Date(117, 12, 12);
        Timestamp workshopEndDate = new Timestamp(date3.getTime());

        if (result.isEmpty()) {
            MessageEntity m1 = new MessageEntity("Hello There", messageDate, "HQ000001", "John", "HQ000003", "Yu Ting");
            MessageEntity m2 = new MessageEntity("Hello There", messageDate, "HQ000001", "John", "HQ000004", "Zhi Rong");
            MessageEntity m3 = new MessageEntity("Hello There Again My Second Message", messageDate, "HQ000001", "John", "HQ000003", "Yu Ting");

            em.persist(m1);
            em.persist(m2);
            em.persist(m3);

        }
    }

    public void addDummyJobOffer() {
        Query q = em.createQuery("SELECT j FROM JobOfferEntity AS j");
        List<JobOfferEntity> result = (List<JobOfferEntity>) q.getResultList();

        if (result.isEmpty()) {
            Date postedDate = new Date(117, 4, 5);
            Date deadline = new Date(117, 11, 12);

            JobOfferEntity j1 = new JobOfferEntity("J1", "Full stack senior developer", "Full stack senior developer", "HQ", "Contract", 4500, "Work with SCB Solution architects to develop  solutions based Micro Services/ ReST oriented architecture", "Understanding of the Java technology stack.Experienced in atleast two of the follow Database Technologies   - Cassandra, Postgres SQL, Dynamo db and MongoDB.Experienced the Linux Operating System with Basic scripting.", true, postedDate, deadline);
            RoleEntity r1 = new RoleEntity("Full Stack Senior Developer", "HQ");
            JobOfferEntity j2 = new JobOfferEntity("J2", "Analytics Manager", "Analytics Manager", "HQ", "Part Time", 2000, "Start-to-end transformation of Data into Insights and Analytics, useful for Business Decisioning.  This would involve understanding and validating data source, developing an analytics framework.", "Degree in Quantitative field, with 8 – 10 years of relevant experience.SAS / SQL knowledge and skill-set is a must", true, postedDate, deadline);
            RoleEntity r2 = new RoleEntity("Analytics Manager", "HQ");
            JobOfferEntity j3 = new JobOfferEntity("J3", "Assistant Manager", "Assistant Manager", "HQ", "Full Time", 5000, "Understand the user requirements and documenting those requirements.To take part in software and architectural development activities", "Java based Web services,Java based rest services,\n"
                    + "Object Oriented Analysis and Design", true, postedDate, deadline);
            RoleEntity r3 = new RoleEntity("Assistant", "Manager");
            JobOfferEntity j4 = new JobOfferEntity("J4", "Solution Advisor", "Solution Advisor", "HQ", "Internship", 1000, "Define business and sales plays execution plan to prioritize initiatives across the region.Ensure programs to drive 4x un-weighted cover, < 30% E/F and < 50% stalled pipe.", "Bachelor’s Degree MBA or Masters.10-15 years of professional experience; demonstrated knowledge/expertise over different aspects of technology solutions", true, postedDate, deadline);
            RoleEntity r4 = new RoleEntity("Solution Advisor", "HQ");
            JobOfferEntity j5 = new JobOfferEntity("J5", "Accountant", "Accountant", "HQ", "Full Time", 8000, "As part of the Global Finance team, you will be responsible for managing the inventory accounting activities for the trading activities, ensuring compliance in terms of statutory, US GAAP, SOX, transfer pricing and direct tax reporting.", "5 to 6 years experience in a Big 4 accounting firm together with some commercial experience.SAP knowledge is preferred", true, postedDate, deadline);
            RoleEntity r5 = new RoleEntity("Accountant", "HQ");
            JobOfferEntity j6 = new JobOfferEntity("J6", "Train Driver", "Train Driver", "Depot", "Part Time", 1200, "Operate the CIMRT EWL Line trains to ensure that our customers have a dafe,reliable and comfortable journey", "NITEC in an Engineering discipline. Working experieence are preferred.You will be required to perform rotating and split shfts, including duties on weekends and public holidays", true, postedDate, deadline);
            JobOfferEntity j7 = new JobOfferEntity("J7", "Train Driver", "Train Driver", "Depot", "Full Time", 2000, "Operate the CIMRT NSL Line trains to ensure that our customers have a dafe,reliable and comfortable journey", "NITEC in an Engineering discipline. Working experieence are preferred.You will be required to perform rotating and split shfts, including duties on weekends and public holidays", true, postedDate, deadline);
            JobOfferEntity j8 = new JobOfferEntity("J8", "Station Manager", "Station Manager", "Station", "Full Time", 2000, "Be responsible and fully in-charge of the detailed daily operation of all facilities and services in a station in accordance with work instruction and procedures. Be required to respond to sttaion,train and track related incidents to ensure the safety and security of our passengers.", "Diploma in an Engineering discipline with a genuine passion, energy and enthusiasm for a frontline customer service job", true, postedDate, deadline);
            JobOfferEntity j9 = new JobOfferEntity("J9", "Track Access Controller", "Track Access Controller", "Depot", "Full Time", 2800, "Execute promptly and accurately the processing of track access activities on the appropraite zone of stations. Monitor that PM/PIC access to the correct bound via appropriate the headwall. Inform manager if PM/PIC request for extension of time on the track and monitor the late book out time", "Diploma in Engineering field. Able to work on permanet 3rd shift. Preferably with at least 3 years of experience in the railway industry", true, postedDate, deadline);
            RoleEntity r6 = new RoleEntity("Track Access Controller", "Depot");
            em.persist(j1);
            em.persist(j2);
            em.persist(j3);
            em.persist(j4);
            em.persist(j5);
            em.persist(j6);
            em.persist(j7);
            em.persist(j8);
            em.persist(j9);
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.persist(r4);
            em.persist(r5);
            em.persist(r6);

            String staffId = "HQ000002";
            Query q1 = em.createQuery("SELECT s FROM HqStaffEntity AS s WHERE s.staffId=:staffId");
            q1.setParameter("staffId", staffId);
            ArrayList<HqStaffEntity> result1 = (ArrayList<HqStaffEntity>) q1.getResultList();
            HqStaffEntity staff;
            if (result1.isEmpty()) {
            } else {
                staff = result1.get(0);
                ArrayList<JobOfferEntity> jobList = new ArrayList<JobOfferEntity>(staff.getJobs());
                jobList.add(j1);
                jobList.add(j2);
                jobList.add(j3);
                jobList.add(j4);
                jobList.add(j5);
                jobList.add(j6);
                jobList.add(j7);
                jobList.add(j8);
                jobList.add(j9);
                staff.setJobs(jobList);
                em.persist(staff);
            }
        }
    }

    public void addDummyJobApplications() {

        Query q = em.createQuery("SELECT j FROM JobApplicationsEntity AS j");
        List<JobApplicationsEntity> result = (List<JobApplicationsEntity>) q.getResultList();
        if (result.isEmpty()) {
            Date dob = new Date(95, 12, 20);
            Date startDate = new Date(116, 2, 4);
            Date endDate = new Date(117, 2, 4);
            JobApplicationsEntity j1 = new JobApplicationsEntity("A1", "Kayley", "Tan", "S12345678", "83837881", "e0002468@u.nus.edu", "Woodlands", "F", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in IS", 10, "Admin", "OCBC", startDate, endDate, "Bank", "Help to process invoices", "Received");
            JobApplicationsEntity j2 = new JobApplicationsEntity("A2", "James", "Lim", "S12345678", "83837881", "e0002468@u.nus.edu", "Yishun", "M", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in CS", 8, "Programmer", "Standard Chartered", startDate, endDate, "Bank", "Create Website for department", "Received");
            JobApplicationsEntity j3 = new JobApplicationsEntity("A3", "Mikki", "Yee", "S12345678", "83837881", "e0002468@u.nus.edu", "Kallang", "F", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in Accountancy", 10, "Accountant", "UOB", startDate, endDate, "Bank", "Help to process debit and credit payment", "Received");
            JobApplicationsEntity j4 = new JobApplicationsEntity("A4", "John", "Chua", "S12345678", "83837881", "e0002468@u.nus.edu", "Serangoon", "M", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in Psyhcology", 5, "It analyst", "Websparks", startDate, endDate, "StartUp", "Help to analysis on the hardware and software", "Received");
            JobApplicationsEntity j5 = new JobApplicationsEntity("A5", "Kayden", "Ng", "S12345678", "83837881", "e0002468@u.nus.edu", "Jurong East", "M", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in Real Estate", 20, "Real Estate Analyst", "HDB", startDate, endDate, "Real Estate", "providing direct support with project management, in-depth research, document development, correspondence, financial analyses and modeling, along with other activities directly related with corporate real estate advisory and location strategy", "Received");
            JobApplicationsEntity j6 = new JobApplicationsEntity("A6", "John", "Chua", "S12345678", "83837881", "e0002468@u.nus.edu", "Serangoon", "M", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in Psyhcology", 5, "It analyst", "Websparks", startDate, endDate, "StartUp", "Help to analysis on the hardware and software", "Received");
            JobApplicationsEntity j7 = new JobApplicationsEntity("A7", "Kenny", "Ong", "S12345678", "83837881", "e0002468@u.nus.edu", "Toa Payoh", "M", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in Math", 7, "Mathematicians", "AMS", startDate, endDate, "Math", "Teach Math", "Received");
            JobApplicationsEntity j8 = new JobApplicationsEntity("A8", "Damin", "Leow", "S12345678", "83837881", "e0002468@u.nus.edu", "Bukit Timah", "M", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in Business Studies", 3, "Business analyst", "OCBC", startDate, endDate, "Education", "Perform system testing and improvement", "Received");
            JobApplicationsEntity j9 = new JobApplicationsEntity("A9", "Fabian", "Sng", "S12345678", "83837881", "e0002468@u.nus.edu", "Boon Lay", "M", dob, "Single", "Chinese", "Singaporean", "Buddhism", "Degree in Engineering", 30, "Customer Acceptance Engineer", "JTC", startDate, endDate, "Engineer", "Driver improvement through failure and corrective actions", "Received");

            em.persist(j1);
            em.persist(j2);
            em.persist(j3);
            em.persist(j4);
            em.persist(j5);
            em.persist(j6);
            em.persist(j7);
            em.persist(j8);
            em.persist(j9);

            JobOfferEntity job = searchJob("J1");
            ArrayList<JobApplicationsEntity> jobList = new ArrayList<JobApplicationsEntity>(job.getJobApplications());
            jobList.add(j1);
            job.setJobApplications(jobList);
            j1.setJobOffer(job);
            em.persist(j1);
            em.persist(job);

            JobOfferEntity job1 = searchJob("J2");
            ArrayList<JobApplicationsEntity> jobList1 = new ArrayList<JobApplicationsEntity>(job1.getJobApplications());
            jobList1.add(j2);
            job1.setJobApplications(jobList1);
            j2.setJobOffer(job1);
            em.persist(j2);
            em.persist(job1);

            JobOfferEntity job2 = searchJob("J3");
            ArrayList<JobApplicationsEntity> jobList2 = new ArrayList<JobApplicationsEntity>(job2.getJobApplications());
            jobList2.add(j3);
            job2.setJobApplications(jobList2);
            j3.setJobOffer(job2);
            em.persist(j3);
            em.persist(job2);

            JobOfferEntity job3 = searchJob("J4");
            ArrayList<JobApplicationsEntity> jobList3 = new ArrayList<JobApplicationsEntity>(job3.getJobApplications());
            jobList3.add(j4);
            job3.setJobApplications(jobList3);
            j4.setJobOffer(job3);
            em.persist(j4);
            em.persist(job3);

            JobOfferEntity job4 = searchJob("J5");
            ArrayList<JobApplicationsEntity> jobList4 = new ArrayList<JobApplicationsEntity>(job4.getJobApplications());
            jobList4.add(j5);
            job4.setJobApplications(jobList4);
            j5.setJobOffer(job4);
            em.persist(j5);
            em.persist(job4);

            JobOfferEntity job5 = searchJob("J6");
            ArrayList<JobApplicationsEntity> jobList5 = new ArrayList<JobApplicationsEntity>(job5.getJobApplications());
            jobList5.add(j6);
            job5.setJobApplications(jobList5);
            j6.setJobOffer(job5);
            em.persist(j6);
            em.persist(job5);

            JobOfferEntity job6 = searchJob("J7");
            ArrayList<JobApplicationsEntity> jobList6 = new ArrayList<JobApplicationsEntity>(job6.getJobApplications());
            jobList6.add(j7);
            job6.setJobApplications(jobList6);
            j7.setJobOffer(job6);
            em.persist(j7);
            em.persist(job6);

            JobOfferEntity job7 = searchJob("J8");
            ArrayList<JobApplicationsEntity> jobList7 = new ArrayList<JobApplicationsEntity>(job7.getJobApplications());
            jobList7.add(j8);
            job7.setJobApplications(jobList7);
            j8.setJobOffer(job7);
            em.persist(j8);
            em.persist(job7);

            JobOfferEntity job8 = searchJob("J9");
            ArrayList<JobApplicationsEntity> jobList8 = new ArrayList<JobApplicationsEntity>(job8.getJobApplications());
            jobList8.add(j9);
            job8.setJobApplications(jobList8);
            j9.setJobOffer(job8);
            em.persist(j8);
            em.persist(job8);

        }
    }

    public JobOfferEntity searchJob(String jobId) {
        JobOfferEntity job = new JobOfferEntity();
        try {
            Query q = em.createQuery("SELECT j FROM JobOfferEntity AS j WHERE j.jobId=:jobId");
            q.setParameter("jobId", jobId);
            job = (JobOfferEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        }
        return job;
    }

    /*  public void addDummyTripReport() {
        Date date = new Date();
        Timestamp datedate = new Timestamp(date.getTime());
        Query query = em.createQuery("SELECT t FROM TripReportEntity AS t");
        List<TripReportEntity> trips = (List<TripReportEntity>) query.getResultList();

        //Map to rolling stock and Depot Staff(TrainDriver)
        if (trips.isEmpty()) {
            reportSessionBeanLocal.addTripReport("Post-Trip", datedate, "IN67", "NIL", "D000337", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
            reportSessionBeanLocal.addTripReport("Post-Trip", datedate, "IN67", "Cabin 2", "D000337", "Working", "Working", "Faulty", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
            reportSessionBeanLocal.addTripReport("Post-Trip", datedate, "IN67", "NIL", "D000337", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
            reportSessionBeanLocal.addTripReport("Post-Trip", datedate, "IN68", "NIL", "D000457", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
            reportSessionBeanLocal.addTripReport("Post-Trip", datedate, "IN68", "NIL", "D000457", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
        }
    } */

 /* public void addDummyServiceLog() {
        Date date = new Date();
        Timestamp datedate = new Timestamp(date.getTime());
        Query query = em.createQuery("SELECT s FROM ServiceLogEntity AS s");
        List<ServiceLogEntity> logs = (List<ServiceLogEntity>) query.getResultList();

        if (logs.isEmpty()) {
            ServiceLogEntity sl1 = new ServiceLogEntity("SL1", datedate, "Passenger fainted in the lift", "This morning (11.45am), we were notified that a passenger named Mdm Siti collasped suddenly in the station's lift. Melvin and I went down to attend to the lady immediately while Jane contacted the ambulance. Mdm Siti was evacuated to NUH subsequently. A follow-up will be done on Mdm Siti to find out the situation back then.");
            ServiceLogEntity sl2 = new ServiceLogEntity("SL2", datedate, "Boy injured by platform screen door", "A 12-years old boy named Raymond Du was injured by the platform screen door when he attempted to rush into the train as the train door was closing. I was in the control station and witnessed the event via CCTV, and quickly released the particular screen door. The boy was later sent to NUH to examine for any possible injury. A follow-up will be made.");
            ServiceLogEntity sl3 = new ServiceLogEntity("SL3", datedate, "Fine Imposement - Food Consumption", "A fine was imposed to Mr. Wong Teng Kiat for his food consumption at the train platform on 10 October 2017. The collection of fine ($300) was processed by me and witnessed by station manager Fabio.");
            ServiceLogEntity sl4 = new ServiceLogEntity("SL4", datedate, "Fine Imposement - Overstay in MRT station", "A fine was imposed to Mr. Tan Seng Seng for his overstay at the station on 12 October 2017. He was found to have loitered in the station for 3 hours. A fine $10 was collected.");
            ServiceLogEntity sl5 = new ServiceLogEntity("SL5", datedate, "Passenger requested for medical attention", "A passenger named Ms. Beverly Soh was feeling unwell during her train trip. Her friend, Ms. Novita Lam, approached us for help after they alighted at our station. Nas and I brought her to our first-aid room to rest, with some water provided. She rested for 30 mins and left after her conditions got better.");
            em.persist(sl1);
            em.persist(sl2);
            em.persist(sl3);
            em.persist(sl4);
            em.persist(sl5);

            //Map to Station Staff
            StationStaffEntity staff = OperationsSessionBeanLocal.searchStationStaff("S000002");
            sl1.setStationStaff(staff);
            sl2.setStationStaff(staff);
            sl3.setStationStaff(staff);
            StationStaffEntity staff2 = OperationsSessionBeanLocal.searchStationStaff("S000030");
            sl4.setStationStaff(staff2);
            sl5.setStationStaff(staff2);
            em.flush();

        }
    }
     
    public void addDummyInspectionReport() {
        Date date = new Date();
        Timestamp datedate = new Timestamp(date.getTime());
        Query query = em.createQuery("SELECT t FROM InspectionReportEntity AS t");
        List<InspectionReportEntity> inspections = (List<InspectionReportEntity>) query.getResultList();

        //Map to rolling stock and Depot Staff(TrainDriver)
        if (inspections.isEmpty()) {
            reportSessionBeanLocal.addInspectionReport("Pre-Departure", datedate, "IN67", "Cabin 2 and 6", "D000337", "Working", "Faulty", "Working", "Working", "Working", "Working", "Faulty", "Working", "Working", "Working", "Working", "Faulty", "Working", "Working", "Working", "Working", "Working", "Faulty", "Working", "Working", "Working", "Working", "Faulty", "Working");
            reportSessionBeanLocal.addInspectionReport("End of Day", datedate, "IN67", "NIL", "D000337", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
            reportSessionBeanLocal.addInspectionReport("Pre-Departure", datedate, "IN67", "NIL", "D000337", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
            reportSessionBeanLocal.addInspectionReport("Pre-Departure", datedate, "IN68", "NIL", "D000457", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
            reportSessionBeanLocal.addInspectionReport("End of Day", datedate, "IN68", "NIL", "D000457", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working", "Working");
        }
    }*/
    public void addDummyIncidentReport() {
        Date date = new Date();
        Timestamp datedate = new Timestamp(date.getTime());
        Query query = em.createQuery("SELECT t FROM IncidentReportEntity AS t");
        List<IncidentReportEntity> incidents = (List<IncidentReportEntity>) query.getResultList();

        //Map to station staff or maintenance staff and Station/depot code
        if (incidents.isEmpty()) {
            reportSessionBeanLocal.addIncidentRep(datedate, "D000002", "Signalling fault at Depot", "Signalling fault at EWL00, which causes train delays on the North-South Line.", "EWL00");
            reportSessionBeanLocal.addIncidentRep(datedate, "S000002", "Lift Breakdown at EWL01", "The lift (IN43) keeps breaking down about four times a month. Frequent compliant from the customer about the lift plaguing issues", "EWL01");
            reportSessionBeanLocal.addIncidentRep(datedate, "S000002", "One of the gantry door cannot open at EWL01", "The sensor at the gantry door (Serial number XXX) is not working, and it will close after 2 secs when passenger tap in.", "EWL01");
            reportSessionBeanLocal.addIncidentRep(datedate, "S000030", "Platform screen door at EWL02 malfunction", "Sceen door at EWL09 Cabin 6 cannot be open automatically, and it cannot be resolved even after using the key to open the door", "EWL02");
            reportSessionBeanLocal.addIncidentRep(datedate, "S000030", "Faulty lights at platform", "Lights at the platform were not working even though they were switched on. It started at around 8pm, causing some inconvenience to the passengers since it was night time. ", "EWL02");
        }
    }

    public void addDummyTrainCar() {

        Query q = em.createQuery("SELECT t FROM TrainCarEntity AS t");
        List<TrainCarEntity> result = (List<TrainCarEntity>) q.getResultList();

        if (result.isEmpty()) {

            int carCode = 0;

            //EWL00 Motor Car X 107
            for (int i = 0; i < 107; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "EWL00", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            }

            //EWL00 Driving Trailer X 53
            for (int i = 0; i < 53; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "EWL00", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            }

            //EWL23 Motor Car X 104
            for (int i = 0; i < 104; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "EWL23", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            }

            //EWL23 Driving Trailer X 52
            for (int i = 0; i < 52; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "EWL23", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            }

            //NSL00 Motor Car X 72
            for (int i = 0; i < 72; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "NSL00", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            }

            //NSL00 Driving Trailer X 36
            for (int i = 0; i < 36; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "NSL00", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            }

            //NSL16 Motor Car X 72
            for (int i = 0; i < 72; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "NSL16", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            }

            //NSL16 Driving Trailer X 36
            for (int i = 0; i < 36; i++) {
                carCode = carCode + 1;
                infraAssetSessionBeanLocal.addTrainCar(carCode, "Kawasaki", "NSL16", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            }

            int count = 0;

            //Form rolling stocks at EWL00
            for (int i = 0; i < 26; i++) {
                count = count + 1;
                String name = "RS" + count;
                infraAssetSessionBeanLocal.addRollingStock(name, "Kawasaki", "EWL00");
            }

            //Form rolling stocks at EWL23
            for (int i = 0; i < 26; i++) {
                count = count + 1;
                String name = "RS" + count;
                infraAssetSessionBeanLocal.addRollingStock(name, "Kawasaki", "EWL23");
            }

            //Form rolling stocks at NSL00
            for (int i = 0; i < 18; i++) {
                count = count + 1;
                String name = "RS" + count;
                infraAssetSessionBeanLocal.addRollingStock(name, "Kawasaki", "NSL00");
            }

            //Form rolling stocks at NSL16
            for (int i = 0; i < 18; i++) {
                count = count + 1;
                String name = "RS" + count;
                infraAssetSessionBeanLocal.addRollingStock(name, "Kawasaki", "NSL16");
            }

            /*infraAssetSessionBeanLocal.addTrainCar(1, "Kawasaki", "EWL00", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(2, "Kawasaki", "EWL00", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(3, "Kawasaki", "EWL00", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(4, "Kawasaki", "EWL00", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(5, "Kawasaki", "EWL00", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(6, "Kawasaki", "EWL00", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(7, "Siemens", "EWL00", "Driving Trailer", "54-seater cabin with 46 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(8, "Siemens", "EWL00", "Driving Trailer", "54-seater cabin with 46 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(9, "Siemens", "EWL00", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(10, "Siemens", "EWL00", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(11, "Siemens", "EWL00", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(12, "Siemens", "EWL00", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(13, "Alstom", "NSL00", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");

            infraAssetSessionBeanLocal.addTrainCar(14, "Siemens", "EWL23", "Driving Trailer", "54-seater cabin with 46 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(15, "Siemens", "EWL23", "Driving Trailer", "54-seater cabin with 46 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(16, "Siemens", "EWL23", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(17, "Siemens", "EWL23", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(18, "Siemens", "EWL23", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(19, "Siemens", "EWL23", "Motor Car", "58-seater cabin with 50 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(20, "Alstom", "EWL23", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(21, "Alstom", "EWL23", "Driving Trailer", "60-seater cabin with 52 handgrips and handrails. The driver cabin is included.");
            infraAssetSessionBeanLocal.addTrainCar(22, "Alstom", "EWL23", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(23, "Alstom", "EWL23", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(24, "Alstom", "EWL23", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");
            infraAssetSessionBeanLocal.addTrainCar(25, "Alstom", "EWL23", "Motor Car", "64-seater cabin with 56 handgrips and handrails.");*/

 /*infraAssetSessionBeanLocal.addRollingStock("RS1", "Kawasaki", "EWL00");
            infraAssetSessionBeanLocal.addRollingStock("RS2", "Siemens", "EWL23");*/
        }
    }

    public void addPartners() {
        Query q = em.createQuery("SELECT b FROM BusinessPartnerEntity AS b");
        List<BusinessPartnerEntity> result = (List<BusinessPartnerEntity>) q.getResultList();
        String[] company = {"Google", "Facebook", "Apple", "Xiaomi", "JTC"};
        String password;
        try {
            //Hashing the password
            password = "ES01" + "default1";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }
            if (result.isEmpty()) {
                BusinessPartnerEntity partner = new BusinessPartnerEntity("BP000001", 96743264, "e0002468@u.nus.edu", sb.toString(), "Google", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "Google Inc. is an American multinational technology company that specializes in Internet-related services and products. These include online advertising technologies, search, cloud computing, software, and hardware. Google was founded in 1998 by Larry Page and Sergey Brin while they were Ph.D. students at Stanford University, in California.", 65657365);
                BusinessPartnerEntity partner1 = new BusinessPartnerEntity("BP000002", 96743264, "e0002468@u.nus.edu", sb.toString(), "Facebook", "Computing", "What is your favourite food?", "Chicken Rice", true, true, "Facebook is an American for-profit corporation and an online social media and social networking service based in Menlo Park, California. The Facebook website was launched on February 4, 2004, by Mark Zuckerberg, along with fellow Harvard College students and roommates, Eduardo Saverin, Andrew McCollum, Dustin Moskovitz, and Chris Hughes.", 65657365);
                BusinessPartnerEntity partner2 = new BusinessPartnerEntity("BP000003", 96743264, "e0002468@u.nus.edu", sb.toString(), "Apple", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "Apple Inc. is an American multinational technology company headquartered in Cupertino, California that designs, develops, and sells consumer electronics, computer software, and online services. The company's hardware products include the iPhone smartphone, the iPad tablet computer, the Mac personal computer, the iPod portable media player, the Apple Watch smartwatch, the Apple TV digital media player, and the HomePod smart speaker.", 65657365);
                BusinessPartnerEntity partner3 = new BusinessPartnerEntity("BP000004", 96743264, "e0002252@u.nus.edu", sb.toString(), "Xiaomi", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "Xiaomi Inc., Chinese: 小米 科技; pinyin: Xiǎomǐ Kējì, is a privately owned Chinese electronics and software company headquartered in Beijing. It is the world's 5th largest smartphone maker in 2017. Xiaomi designs, develops, and sells smartphones, mobile apps, laptops, and related consumer electronics.", 65657365);
                BusinessPartnerEntity partner4 = new BusinessPartnerEntity("BP000005", 96743264, "e0002252@u.nus.edu", sb.toString(), "IBM", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "IBM (International Business Machines Corporation) is an American multinational technology company headquartered in Armonk, New York, United States, with operations in over 170 countries. The company originated in 1911 as the Computing-Tabulating-Recording Company (CTR) and was renamed \"International Business Machines\" in 1924.", 65657365);
                BusinessPartnerEntity partner5 = new BusinessPartnerEntity("BP000006", 96743264, "e0002252@u.nus.edu", sb.toString(), "Samsung", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "Samsung Group is a South Korean multinational conglomerate headquartered in Samsung Town, Seoul. It comprises numerous affiliated businesses, most of them united under the Samsung brand, and is the largest South Korean chaebol (business conglomerate).\n" + "Samsung was founded by Lee Byung-chul in 1938 as a trading company. Over the next three decades, the group diversified into areas including food processing, textiles, insurance, securities and retail. Samsung entered the electronics industry in the late 1960s and the construction and shipbuilding industries in the mid-1970s; these areas would drive its subsequent growth. ", 65657365);
                BusinessPartnerEntity partner6 = new BusinessPartnerEntity("BP000007", 96743264, "e0002252@u.nus.edu", sb.toString(), "Nokia", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "Nokia Corporation, stylised as NOKIA, is a Finnish multinational communications, information technology and consumer electronics company, founded in 1865. Nokia's headquarters are in Espoo, Uusimaa, in the greater Helsinki metropolitan area. In 2016, Nokia employed approximately 101,000 people across over 100 countries, did business in more than 130 countries, and reported annual revenues of around €23.6 billion", 65657365);
                BusinessPartnerEntity partner7 = new BusinessPartnerEntity("BP000008", 96743264, "e0002252@u.nus.edu", sb.toString(), "Telemax", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "Telemax is a SME that established 20 years ago and has continue to grow its brand as reknowned telecommunication company. It has worked with mutiple large corperations around Singapore such as Gain City, America Country Club, Singtel, Starhub and etc. They have now venture to other electronic market such as CCTV and biometric door access so as to expand its company to be the leading contractor in supporting clients in many areas.", 65657365);
                BusinessPartnerEntity partner8 = new BusinessPartnerEntity("BP000009", 96743264, "e0002252@u.nus.edu", sb.toString(), "ASUS", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "AsusTek Computer Inc. (stylised as ASUSTeK or ΛSUS) is a Taiwanese multinational computer and phone hardware and electronics company headquartered in Beitou District, Taipei, Taiwan. Its products include desktops, laptops, netbooks, mobile phones, networking equipment, monitors, WIFI routers, projectors, motherboards, graphics cards, optical storage, multimedia products, peripherals, wearables, servers, workstations, and tablet PCs. The company is also an original equipment manufacturer (OEM).", 65657365);
                BusinessPartnerEntity partner9 = new BusinessPartnerEntity("BP000010", 96743264, "e0002252@u.nus.edu", sb.toString(), "Honda", "Computing", "What is your favourite food?", "Chicken Rice", true, false, "Honda has been the world's largest motorcycle manufacturer since 1959, as well as the world's largest manufacturer of internal combustion engines measured by volume, producing more than 14 million internal combustion engines each year. Honda became the second-largest Japanese automobile manufacturer in 2001. Honda was the eighth largest automobile manufacturer in the world behind Toyota, Volkswagen Group, Hyundai Motor Group, General Motors, Ford, Nissan, and Fiat Chrysler Automobiles in 2015.", 65657365);
                em.persist(partner);
                em.persist(partner1);
                em.persist(partner2);
                em.persist(partner3);
                em.persist(partner4);
                em.persist(partner5);
                em.persist(partner6);
                em.persist(partner7);
                em.persist(partner8);
                em.persist(partner9);

            }
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }

    }

    public void addPassengers() {
        Query q = em.createQuery("SELECT p FROM PassengerEntity AS p");
        List<PassengerEntity> result = (List<PassengerEntity>) q.getResultList();
        String[] company = {"Google", "Facebook", "Apple", "Xiaomi", "JTC"};
        String password;
        String[] name = {"John", "Marcus", "Susan", "Henry", "Wei Hao", "Kayley", "Hui Ming", "Jaden", "Zhi Rong", "Fabian", "Damin", "Elaine", "Zhu Ming", "Yu Ting", "En Hao", "Jay", "Jun Xing"};

        try {
            //Hashing the password
            password = "ES01" + "default1";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashSum = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashSum.length; ++i) {
                sb.append(Integer.toHexString((hashSum[i] & 0xFF) | 0x100).substring(1, 3));
            }
            if (result.isEmpty()) {
                PassengerEntity p = new PassengerEntity("P000001", name[rand.nextInt(17)], "Soh", "S9336802I", "address", "e0003894@u.nus.edu", 96743264, sb.toString(), "What is your favourite food?", "Chicken Rice", new Date(), true, "M",0);
                PassengerEntity p1 = new PassengerEntity("P000002", name[rand.nextInt(17)], "Soh", "S9336803I", "address", "e0002252@u.nus.edu", 96743264, sb.toString(), "What is your favourite food?", "Chicken Rice", new Date(), true, "M",0);
                PassengerEntity p2 = new PassengerEntity("P000003", name[rand.nextInt(17)], "Soh", "S9336804I", "address", "e0002399@u.nus.edu", 96743264, sb.toString(), "What is your favourite food?", "Chicken Rice", new Date(), true, "M",0);
                PassengerEntity p3 = new PassengerEntity("P000004", name[rand.nextInt(17)], "Soh", "S9336805I", "address", "e0003942@u.nus.edu", 96743264, sb.toString(), "What is your favourite food?", "Chicken Rice", new Date(), true, "M",0);
                PassengerEntity p4 = new PassengerEntity("P000005", name[rand.nextInt(17)], "Soh", "S9336806I", "address", "e0002468@u.nus.edu", 96743264, sb.toString(), "What is your favourite food?", "Chicken Rice", new Date(), true, "M",0);
                CardEntity c1 = new CardEntity("2234A75D9000","Student", 0, new Date(), new Date(), new Date());
                ArrayList<CardEntity> cards1 = new ArrayList<CardEntity>();
                cards1.add(c1);
                Calendar date = new GregorianCalendar(2018, 12, 30);
                CardEntity c2 = new CardEntity("92CEA65D9000","Adult", 0, new Date(), new Date(), date.getTime());
                //CardEntity c3 = new CardEntity("92CEA65D9001","Adult", 0, new Date(), new Date(), date.getTime());
                ArrayList<CardEntity> cards2 = new ArrayList<CardEntity>();
                cards2.add(c2);
                c1.setPassenger(p);
                c2.setPassenger(p1);
                p.setCards(cards1);
                p1.setCards(cards2);
                em.persist(p);
                em.persist(p1);
                em.persist(p2);
                em.persist(p3);
                em.persist(p4);
                em.persist(c1);
                em.persist(c2);
                //em.persist(c3);

            }
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }

    }

    public void addSimulator() {
        Query q = em.createQuery("SELECT s FROM SimulatorTrackEntity AS s");
        List<SimulatorTrackEntity> result = (List<SimulatorTrackEntity>) q.getResultList();

        Query q1 = em.createQuery("SELECT s FROM SimulatorTrainEntity AS s");
        List<SimulatorTrainEntity> result1 = (List<SimulatorTrainEntity>) q1.getResultList();

        if (result.isEmpty()) {
            SimulatorTrackEntity t = new SimulatorTrackEntity("Green", "NSL 16 Marina Bay", true, 27200, null);
            SimulatorTrackEntity t1 = new SimulatorTrackEntity("Green", "Track", false, 27000, t);
            SimulatorTrackEntity t2 = new SimulatorTrackEntity("Green", "Track", false, 26800, t1);
            SimulatorTrackEntity t3 = new SimulatorTrackEntity("Green", "Track", false, 26600, t2);
            SimulatorTrackEntity t4 = new SimulatorTrackEntity("Green", "Track", false, 26400, t3);
            SimulatorTrackEntity t5 = new SimulatorTrackEntity("Green", "Track", false, 26200, t4);
            SimulatorTrackEntity t6 = new SimulatorTrackEntity("Green", "Track", false, 26000, t5);
            SimulatorTrackEntity t7 = new SimulatorTrackEntity("Green", "Track", false, 25800, t6);
            SimulatorTrackEntity t8 = new SimulatorTrackEntity("Green", "Track", false, 25600, t7);
            SimulatorTrackEntity t9 = new SimulatorTrackEntity("Green", "Track", false, 25400, t8);
            SimulatorTrackEntity t10 = new SimulatorTrackEntity("Green", "Track", false, 25200, t9);
            SimulatorTrackEntity t11 = new SimulatorTrackEntity("Green", "Track", false, 25000, t10);
            SimulatorTrackEntity t12 = new SimulatorTrackEntity("Green", "Track", false, 24800, t11);
            SimulatorTrackEntity t13 = new SimulatorTrackEntity("Green", "Track", false, 24600, t12);
            SimulatorTrackEntity t14 = new SimulatorTrackEntity("Green", "Track", false, 24400, t13);
            SimulatorTrackEntity t15 = new SimulatorTrackEntity("Green", "Track", false, 24200, t14);
            SimulatorTrackEntity t16 = new SimulatorTrackEntity("Green", "NSL 15 Mun Yee", true, 24000, t15);
            SimulatorTrackEntity t17 = new SimulatorTrackEntity("Green", "Track", false, 23800, t16);
            SimulatorTrackEntity t18 = new SimulatorTrackEntity("Green", "Track", false, 23600, t17);
            SimulatorTrackEntity t19 = new SimulatorTrackEntity("Green", "Track", false, 23400, t18);
            SimulatorTrackEntity t20 = new SimulatorTrackEntity("Green", "Track", false, 23200, t19);
            SimulatorTrackEntity t21 = new SimulatorTrackEntity("Green", "Track", false, 23000, t20);
            SimulatorTrackEntity t22 = new SimulatorTrackEntity("Green", "NSL 14 Joo Kia", true, 22800, t21);
            SimulatorTrackEntity t23 = new SimulatorTrackEntity("Green", "Track", false, 22600, t22);
            SimulatorTrackEntity t24 = new SimulatorTrackEntity("Green", "Track", false, 22400, t23);
            SimulatorTrackEntity t25 = new SimulatorTrackEntity("Green", "Track", false, 22200, t24);
            SimulatorTrackEntity t26 = new SimulatorTrackEntity("Green", "Track", false, 22000, t25);
            SimulatorTrackEntity t27 = new SimulatorTrackEntity("Green", "Track", false, 21800, t26);
            SimulatorTrackEntity t28 = new SimulatorTrackEntity("Green", "Track", false, 21600, t27);
            SimulatorTrackEntity t29 = new SimulatorTrackEntity("Green", "NSL 13 Song Dolly", true, 21400, t28);
            SimulatorTrackEntity t30 = new SimulatorTrackEntity("Green", "Track", false, 21200, t29);
            SimulatorTrackEntity t31 = new SimulatorTrackEntity("Green", "Track", false, 21000, t30);
            SimulatorTrackEntity t32 = new SimulatorTrackEntity("Green", "Track", false, 20800, t31);
            SimulatorTrackEntity t33 = new SimulatorTrackEntity("Green", "Track", false, 20600, t32);
            SimulatorTrackEntity t34 = new SimulatorTrackEntity("Green", "Track", false, 20400, t33);
            SimulatorTrackEntity t35 = new SimulatorTrackEntity("Green", "Track", false, 20200, t34);
            SimulatorTrackEntity t36 = new SimulatorTrackEntity("Green", "NSL 12 Salty Breeze", true, 20000, t35);
            SimulatorTrackEntity t37 = new SimulatorTrackEntity("Green", "Track", false, 19800, t36);
            SimulatorTrackEntity t38 = new SimulatorTrackEntity("Green", "Track", false, 19600, t37);
            SimulatorTrackEntity t39 = new SimulatorTrackEntity("Green", "Track", false, 19400, t38);
            SimulatorTrackEntity t40 = new SimulatorTrackEntity("Green", "Track", false, 19200, t39);
            SimulatorTrackEntity t41 = new SimulatorTrackEntity("Green", "Track", false, 19000, t40);
            SimulatorTrackEntity t42 = new SimulatorTrackEntity("Green", "NSL 11 Informal Sky", true, 18800, t41);
            SimulatorTrackEntity t43 = new SimulatorTrackEntity("Green", "Track", false, 18600, t42);
            SimulatorTrackEntity t44 = new SimulatorTrackEntity("Green", "Track", false, 18400, t43);
            SimulatorTrackEntity t45 = new SimulatorTrackEntity("Green", "Track", false, 18200, t44);
            SimulatorTrackEntity t46 = new SimulatorTrackEntity("Green", "Track", false, 18000, t45);
            SimulatorTrackEntity t47 = new SimulatorTrackEntity("Green", "Track", false, 17800, t46);
            SimulatorTrackEntity t48 = new SimulatorTrackEntity("Green", "Track", false, 17600, t47);
            SimulatorTrackEntity t49 = new SimulatorTrackEntity("Green", "Track", false, 17400, t48);
            SimulatorTrackEntity t50 = new SimulatorTrackEntity("Green", "Track", false, 17200, t49);
            SimulatorTrackEntity t51 = new SimulatorTrackEntity("Green", "NSL 10 Cycle Square", true, 17000, t50);
            SimulatorTrackEntity t52 = new SimulatorTrackEntity("Green", "Track", false, 16800, t51);
            SimulatorTrackEntity t53 = new SimulatorTrackEntity("Green", "Track", false, 16600, t52);
            SimulatorTrackEntity t54 = new SimulatorTrackEntity("Green", "Track", false, 16400, t53);
            SimulatorTrackEntity t55 = new SimulatorTrackEntity("Green", "Track", false, 16200, t54);
            SimulatorTrackEntity t56 = new SimulatorTrackEntity("Green", "Track", false, 16000, t55);
            SimulatorTrackEntity t57 = new SimulatorTrackEntity("Green", "Track", false, 15800, t56);
            SimulatorTrackEntity t58 = new SimulatorTrackEntity("Green", "Track", false, 15600, t57);
            SimulatorTrackEntity t59 = new SimulatorTrackEntity("Green", "Track", false, 15400, t58);
            SimulatorTrackEntity t60 = new SimulatorTrackEntity("Green", "Track", false, 15200, t59);
            SimulatorTrackEntity t61 = new SimulatorTrackEntity("Green", "NSL 09 Highland University", true, 15000, t60);
            SimulatorTrackEntity t62 = new SimulatorTrackEntity("Green", "Track", false, 14800, t61);
            SimulatorTrackEntity t63 = new SimulatorTrackEntity("Green", "Track", false, 14600, t62);
            SimulatorTrackEntity t64 = new SimulatorTrackEntity("Green", "Track", false, 14400, t63);
            SimulatorTrackEntity t65 = new SimulatorTrackEntity("Green", "Track", false, 14200, t64);
            SimulatorTrackEntity t66 = new SimulatorTrackEntity("Green", "Track", false, 14000, t65);
            SimulatorTrackEntity t67 = new SimulatorTrackEntity("Green", "NSL 08 Justice Screen Center", true, 13800, t66);
            SimulatorTrackEntity t68 = new SimulatorTrackEntity("Green", "Track", false, 13600, t67);
            SimulatorTrackEntity t69 = new SimulatorTrackEntity("Green", "Track", false, 13400, t68);
            SimulatorTrackEntity t70 = new SimulatorTrackEntity("Green", "Track", false, 13200, t69);
            SimulatorTrackEntity t71 = new SimulatorTrackEntity("Green", "Track", false, 13000, t70);
            SimulatorTrackEntity t72 = new SimulatorTrackEntity("Green", "Track", false, 12800, t71);
            SimulatorTrackEntity t73 = new SimulatorTrackEntity("Green", "Track", false, 12600, t72);
            SimulatorTrackEntity t74 = new SimulatorTrackEntity("Green", "Track", false, 12400, t73);
            SimulatorTrackEntity t75 = new SimulatorTrackEntity("Green", "Track", false, 12200, t74);
            SimulatorTrackEntity t76 = new SimulatorTrackEntity("Green", "NSL 07 Coral Plaza", true, 12000, t75);
            SimulatorTrackEntity t77 = new SimulatorTrackEntity("Green", "Track", false, 11800, t76);
            SimulatorTrackEntity t78 = new SimulatorTrackEntity("Green", "Track", false, 11600, t77);
            SimulatorTrackEntity t79 = new SimulatorTrackEntity("Green", "Track", false, 11400, t78);
            SimulatorTrackEntity t80 = new SimulatorTrackEntity("Green", "Track", false, 11200, t79);
            SimulatorTrackEntity t81 = new SimulatorTrackEntity("Green", "Track", false, 11000, t80);
            SimulatorTrackEntity t82 = new SimulatorTrackEntity("Green", "NSL 06 Toema", true, 10800, t81);
            SimulatorTrackEntity t83 = new SimulatorTrackEntity("Green", "Track", false, 10600, t82);
            SimulatorTrackEntity t84 = new SimulatorTrackEntity("Green", "Track", false, 10400, t83);
            SimulatorTrackEntity t85 = new SimulatorTrackEntity("Green", "Track", false, 10200, t84);
            SimulatorTrackEntity t86 = new SimulatorTrackEntity("Green", "Track", false, 10000, t85);
            SimulatorTrackEntity t87 = new SimulatorTrackEntity("Green", "Track", false, 9800, t86);
            SimulatorTrackEntity t88 = new SimulatorTrackEntity("Green", "Track", false, 9600, t87);
            SimulatorTrackEntity t89 = new SimulatorTrackEntity("Green", "Track", false, 9400, t88);
            SimulatorTrackEntity t90 = new SimulatorTrackEntity("Green", "Track", false, 9200, t89);
            SimulatorTrackEntity t91 = new SimulatorTrackEntity("Green", "Track", false, 9000, t90);
            SimulatorTrackEntity t92 = new SimulatorTrackEntity("Green", "Track", false, 8800, t91);
            SimulatorTrackEntity t93 = new SimulatorTrackEntity("Green", "Track", false, 8600, t92);
            SimulatorTrackEntity t94 = new SimulatorTrackEntity("Green", "NSL 05 Hsuyi Tin", true, 8400, t93);
            SimulatorTrackEntity t95 = new SimulatorTrackEntity("Green", "Track", false, 8200, t94);
            SimulatorTrackEntity t96 = new SimulatorTrackEntity("Green", "Track", false, 8000, t95);
            SimulatorTrackEntity t97 = new SimulatorTrackEntity("Green", "Track", false, 7800, t96);
            SimulatorTrackEntity t98 = new SimulatorTrackEntity("Green", "Track", false, 7600, t97);
            SimulatorTrackEntity t99 = new SimulatorTrackEntity("Green", "Track", false, 7400, t98);
            SimulatorTrackEntity t100 = new SimulatorTrackEntity("Green", "Track", false, 7200, t99);
            SimulatorTrackEntity t101 = new SimulatorTrackEntity("Green", "Track", false, 7000, t100);
            SimulatorTrackEntity t102 = new SimulatorTrackEntity("Green", "Track", false, 6800, t101);
            SimulatorTrackEntity t103 = new SimulatorTrackEntity("Green", "Track", false, 6600, t102);
            SimulatorTrackEntity t104 = new SimulatorTrackEntity("Green", "NSL 04 Fire Star", true, 6400, t103);
            SimulatorTrackEntity t105 = new SimulatorTrackEntity("Green", "Track", false, 6200, t104);
            SimulatorTrackEntity t106 = new SimulatorTrackEntity("Green", "Track", false, 6000, t105);
            SimulatorTrackEntity t107 = new SimulatorTrackEntity("Green", "Track", false, 5800, t106);
            SimulatorTrackEntity t108 = new SimulatorTrackEntity("Green", "Track", false, 5600, t107);
            SimulatorTrackEntity t109 = new SimulatorTrackEntity("Green", "Track", false, 5400, t108);
            SimulatorTrackEntity t110 = new SimulatorTrackEntity("Green", "Track", false, 5200, t109);
            SimulatorTrackEntity t111 = new SimulatorTrackEntity("Green", "Track", false, 5000, t110);
            SimulatorTrackEntity t112 = new SimulatorTrackEntity("Green", "Track", false, 4800, t111);
            SimulatorTrackEntity t113 = new SimulatorTrackEntity("Green", "NSL 03 Crystal Chris", true, 4600, t112);
            SimulatorTrackEntity t114 = new SimulatorTrackEntity("Green", "Track", false, 4400, t113);
            SimulatorTrackEntity t115 = new SimulatorTrackEntity("Green", "Track", false, 4200, t114);
            SimulatorTrackEntity t116 = new SimulatorTrackEntity("Green", "Track", false, 4000, t115);
            SimulatorTrackEntity t117 = new SimulatorTrackEntity("Green", "Track", false, 3800, t116);
            SimulatorTrackEntity t118 = new SimulatorTrackEntity("Green", "Track", false, 3600, t117);
            SimulatorTrackEntity t119 = new SimulatorTrackEntity("Green", "Track", false, 3400, t118);
            SimulatorTrackEntity t120 = new SimulatorTrackEntity("Green", "Track", false, 3200, t119);
            SimulatorTrackEntity t121 = new SimulatorTrackEntity("Green", "Track", false, 3000, t120);
            SimulatorTrackEntity t122 = new SimulatorTrackEntity("Green", "Track", false, 2800, t121);
            SimulatorTrackEntity t123 = new SimulatorTrackEntity("Green", "Track", false, 2600, t122);
            SimulatorTrackEntity t124 = new SimulatorTrackEntity("Green", "Track", false, 2400, t123);
            SimulatorTrackEntity t125 = new SimulatorTrackEntity("Green", "NSL 02 Greenfall", true, 2200, t124);
            SimulatorTrackEntity t126 = new SimulatorTrackEntity("Green", "Track", false, 2000, t125);
            SimulatorTrackEntity t127 = new SimulatorTrackEntity("Green", "Track", false, 1800, t126);
            SimulatorTrackEntity t128 = new SimulatorTrackEntity("Green", "Track", false, 1600, t127);
            SimulatorTrackEntity t129 = new SimulatorTrackEntity("Green", "Track", false, 1400, t128);
            SimulatorTrackEntity t130 = new SimulatorTrackEntity("Green", "Track", false, 1200, t129);
            SimulatorTrackEntity t131 = new SimulatorTrackEntity("Green", "Track", false, 1000, t130);
            SimulatorTrackEntity t132 = new SimulatorTrackEntity("Green", "Track", false, 800, t131);
            SimulatorTrackEntity t133 = new SimulatorTrackEntity("Green", "Track", false, 600, t132);
            SimulatorTrackEntity t134 = new SimulatorTrackEntity("Green", "Track", false, 400, t133);
            SimulatorTrackEntity t135 = new SimulatorTrackEntity("Green", "Track", false, 200, t134);
            SimulatorTrackEntity t136 = new SimulatorTrackEntity("Green", "NSL 01 Tang Min", true, 0, t135);
            SimulatorTrackEntity t137 = new SimulatorTrackEntity("Green", "Track", false, -200, t136);
            SimulatorTrackEntity t138 = new SimulatorTrackEntity("Green", "Track", false, -400, t137);
            SimulatorTrackEntity t139 = new SimulatorTrackEntity("Green", "Track", false, -600, t138);
            SimulatorTrackEntity t140 = new SimulatorTrackEntity("Green", "Track", false, -800, t139);
            SimulatorTrackEntity t141 = new SimulatorTrackEntity("Green", "Track", false, -1000, t140);
            SimulatorTrackEntity t142 = new SimulatorTrackEntity("Green", "Track", false, -1200, t141);
            SimulatorTrackEntity t143 = new SimulatorTrackEntity("Green", "Track", false, -1400, t142);
            SimulatorTrackEntity t144 = new SimulatorTrackEntity("Green", "Track", false, -1600, t143);
            SimulatorTrackEntity t145 = new SimulatorTrackEntity("Green", "Track", false, -1800, t144);
            SimulatorTrackEntity t146 = new SimulatorTrackEntity("Green", "Track", false, -2000, t145);
            SimulatorTrackEntity t147 = new SimulatorTrackEntity("Green", "Track", false, -2200, t146);
            SimulatorTrackEntity t148 = new SimulatorTrackEntity("Green", "Track", false, -2400, t147);
            SimulatorTrackEntity t149 = new SimulatorTrackEntity("Green", "Track", false, -2600, t148);
            SimulatorTrackEntity t150 = new SimulatorTrackEntity("Green", "Track", false, -2800, t149);
            SimulatorTrackEntity t151 = new SimulatorTrackEntity("Green", "NSL 00 Flying Fish Cove", false, -3000, t150);
            em.persist(t);
            em.persist(t1);
            em.persist(t2);
            em.persist(t3);
            em.persist(t4);
            em.persist(t5);
            em.persist(t6);
            em.persist(t7);
            em.persist(t8);
            em.persist(t9);
            em.persist(t10);
            em.persist(t11);
            em.persist(t12);
            em.persist(t13);
            em.persist(t14);
            em.persist(t15);
            em.persist(t16);
            em.persist(t17);
            em.persist(t18);
            em.persist(t19);
            em.persist(t20);
            em.persist(t21);
            em.persist(t22);
            em.persist(t23);
            em.persist(t24);
            em.persist(t25);
            em.persist(t26);
            em.persist(t27);
            em.persist(t28);
            em.persist(t29);
            em.persist(t30);
            em.persist(t31);
            em.persist(t32);
            em.persist(t33);
            em.persist(t34);
            em.persist(t35);
            em.persist(t36);
            em.persist(t37);
            em.persist(t38);
            em.persist(t39);
            em.persist(t40);
            em.persist(t41);
            em.persist(t42);
            em.persist(t43);
            em.persist(t44);
            em.persist(t45);
            em.persist(t46);
            em.persist(t47);
            em.persist(t48);
            em.persist(t49);
            em.persist(t50);
            em.persist(t51);
            em.persist(t52);
            em.persist(t53);
            em.persist(t54);
            em.persist(t55);
            em.persist(t56);
            em.persist(t57);
            em.persist(t58);
            em.persist(t59);
            em.persist(t60);
            em.persist(t61);
            em.persist(t62);
            em.persist(t63);
            em.persist(t64);
            em.persist(t65);
            em.persist(t66);
            em.persist(t67);
            em.persist(t68);
            em.persist(t69);
            em.persist(t70);
            em.persist(t71);
            em.persist(t72);
            em.persist(t73);
            em.persist(t74);
            em.persist(t75);
            em.persist(t76);
            em.persist(t77);
            em.persist(t78);
            em.persist(t79);
            em.persist(t80);
            em.persist(t81);
            em.persist(t82);
            em.persist(t83);
            em.persist(t84);
            em.persist(t85);
            em.persist(t86);
            em.persist(t87);
            em.persist(t88);
            em.persist(t89);
            em.persist(t90);
            em.persist(t91);
            em.persist(t92);
            em.persist(t93);
            em.persist(t94);
            em.persist(t95);
            em.persist(t96);
            em.persist(t97);
            em.persist(t98);
            em.persist(t99);
            em.persist(t100);
            em.persist(t101);
            em.persist(t102);
            em.persist(t103);
            em.persist(t104);
            em.persist(t105);
            em.persist(t106);
            em.persist(t107);
            em.persist(t108);
            em.persist(t109);
            em.persist(t110);
            em.persist(t111);
            em.persist(t112);
            em.persist(t113);
            em.persist(t114);
            em.persist(t115);
            em.persist(t116);
            em.persist(t117);
            em.persist(t118);
            em.persist(t119);
            em.persist(t120);
            em.persist(t121);
            em.persist(t122);
            em.persist(t123);
            em.persist(t124);
            em.persist(t125);
            em.persist(t126);
            em.persist(t127);
            em.persist(t128);
            em.persist(t129);
            em.persist(t130);
            em.persist(t131);
            em.persist(t132);
            em.persist(t133);
            em.persist(t134);
            em.persist(t135);
            em.persist(t136);
            em.persist(t137);
            em.persist(t138);
            em.persist(t139);
            em.persist(t140);
            em.persist(t141);
            em.persist(t142);
            em.persist(t143);
            em.persist(t144);
            em.persist(t145);
            em.persist(t146);
            em.persist(t147);
            em.persist(t148);
            em.persist(t149);
            em.persist(t150);
            em.persist(t151);
        }
        if (result1.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                SimulatorTrainEntity t = new SimulatorTrainEntity(0, 60, true);
                em.persist(t);
            }
        }
        em.flush();
        
        Query q5 = em.createQuery("SELECT s FROM SimulatorTrackEntity AS s ORDER BY s.distance");
        List<SimulatorTrackEntity> tracks = q5.getResultList();
        
        for(int i = 1; i< tracks.size();i++){
            tracks.get(i).setPrevTrack(tracks.get(i-1));
            
        }
        
    }
    
    public double randomAmt(StationEntity start, StationEntity end){
         return calculatorSessionBeanLocal.calculateTripFare(randomTime(), "Weekday", randomTime(), "Adult", start.getInfraName(), end.getInfraName());
    }
    
    public int randomInt() {
        int leftLimit = 1;
        int rightLimit = 37;
        int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
        return generatedInteger;
    }
    
    public StationEntity randomStation(int num){
        String infraId = "IN"+num;
        Query q = em.createQuery("SELECT s FROM StationEntity AS s WHERE s.infraId=:infraId");
        q.setParameter("infraId", infraId);
        return (StationEntity) q.getResultList().get(0);
    }
    
    public Timestamp randomTime() {
        long offset = Timestamp.valueOf("2017-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2018-01-01 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp rand = new Timestamp(offset + (long) (Math.random() * diff));
        return rand;
    }
    
    public void addDummyFareTransaction(){
        Query q = em.createQuery("SELECT ft FROM FareTransactionEntity AS ft");
        if(q.getResultList().isEmpty()){
            for(int i=0; i<1000; i++){
                FareTransactionEntity ft = new FareTransactionEntity(randomAmt(randomStation(randomInt()),randomStation(randomInt())), randomStation(randomInt()),randomStation(randomInt()), randomTime(), randomTime());
                ft.setCard(null);
                ft.setSpecialDayAlgo(null);
                ft.setTrainSchedule(null);
                em.persist(ft);
            }
        }
    }
}
