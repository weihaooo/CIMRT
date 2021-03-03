/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare.sessionbean;

import infraasset.entity.RollingStockEntity;
import static java.lang.Math.abs;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import routefare.entity.NodeEntity;
import routefare.entity.RouteEntity;
import routefare.entity.SpecialDayAlgoEntity;
import routefare.entity.TrainScheduleEntity;
import routefare.entity.TripStationScheduleEntity;

/**
 *
 * @author zhuming
 */
@Stateless
public class RoutePlanningSessionBean implements RoutePlanningSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private ArrayList<TrainScheduleEntity> tempSchedule = new ArrayList<TrainScheduleEntity>();
    private ArrayList<SpecialDayAlgoEntity> specialDaySchedule = new ArrayList<SpecialDayAlgoEntity>();
    private int tripNumberDepot = 1;
    private int tripNumberStation = 1;
    private int previousMinRS = 1;
    private int thisMinRS = 1;
    List<RollingStockEntity> availableRS = new ArrayList<RollingStockEntity>();
    List<RollingStockEntity> availableRS_opp = new ArrayList<RollingStockEntity>();

    public RoutePlanningSessionBean() {
    }

    ////////////////////generate train schedule tables////////////////////
    @Override
    public List<String> getRouteList() {
        Query q = em.createQuery("SELECT r FROM RouteEntity r ORDER BY r.code");
        List routeList = new ArrayList();
        for (Object o : q.getResultList()) {
            RouteEntity r = (RouteEntity) o;
            routeList.add(r.getCode());
        }
        return routeList;
    }

    @Override
    public boolean isSpecialDay(Timestamp date) {
        Query q = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s WHERE s.date=:date");
        q.setParameter("date", date);
        if (q.getResultList().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<String> getPeriodList(Timestamp date, String dayType) {
        List periodList = new ArrayList();
        Query q = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s WHERE s.date=:date");
        q.setParameter("date", date);
        if (q.getResultList().isEmpty()) {
            Query q1 = em.createQuery("SELECT s FROM TrainScheduleEntity s WHERE s.dayType=:dayType AND s.updateVersion='new' ORDER BY s.periodStart");
            q1.setParameter("dayType", dayType);

            for (Object o : q1.getResultList()) {
                TrainScheduleEntity s = (TrainScheduleEntity) o;
                String result;

                String s1 = new SimpleDateFormat("hh:mm a").format(s.getPeriodStart());
                String s2 = new SimpleDateFormat("hh:mm a").format(s.getPeriodEnd());

                result = s1 + " - " + s2;
                periodList.add(result);
            }
        } else {
            Query q2 = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s WHERE s.date=:date ORDER BY s.periodStart");
            q2.setParameter("date", date);

            for (Object o : q2.getResultList()) {
                SpecialDayAlgoEntity s = (SpecialDayAlgoEntity) o;
                String result;

                String s1 = new SimpleDateFormat("hh:mm a").format(s.getPeriodStart());
                String s2 = new SimpleDateFormat("hh:mm a").format(s.getPeriodEnd());

                result = s1 + " - " + s2;
                periodList.add(result);
            }
        }
        return periodList;
    }

    @Override
    public NodeEntity searchNode(String code) {
        Query q = em.createQuery("SELECT s FROM NodeEntity s");
        NodeEntity s = new NodeEntity();
        for (Object o : q.getResultList()) {
            s = (NodeEntity) o;
            if (s.getCode().equals(code)) {
                break;
            }
        }
        return s;
    }

    @Override
    public RouteEntity searchRoute(String code) {
        Query q = em.createQuery("SELECT r FROM RouteEntity r");
        RouteEntity r = new RouteEntity();
        for (Object o : q.getResultList()) {
            r = (RouteEntity) o;
            if (r.getCode().equals(code)) {
                break;
            }
        }
        return r;
    }

    @Override
    public ArrayList<String> getNodes(String routeCode) {
        ArrayList<String> nodesInRoute = new ArrayList<String>();
        RouteEntity route = searchRoute(routeCode);
        ArrayList<NodeEntity> nodeArr = new ArrayList<NodeEntity>(route.getNodes());
        for (int i = 0; i < nodeArr.size(); i++) {
            nodesInRoute.add(nodeArr.get(i).getCode());
        }
        Collections.sort(nodesInRoute, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return Integer.parseInt(num);
            }
        });
        if (route.getCode().charAt(3) != '0') {
            Collections.reverse(nodesInRoute);
        }
        return nodesInRoute;
    }

    @Override
    public double calculateTimeToNextStation(String code) {
        NodeEntity s1 = searchNode(code);
        double distance1 = s1.getDistanceToFirstStation();
        String codeNext = s1.getNextStation();
        NodeEntity s2 = searchNode(codeNext);
        double distance2 = s2.getDistanceToFirstStation();
        return abs((distance2 - distance1) / 60000);
    }

    @Override
    public double calculateTimeToPreviousStation(String code) {
        NodeEntity s1 = searchNode(code);
        double distance1 = s1.getDistanceToFirstStation();
        String codeNext = s1.getPreviousStation();
        NodeEntity s2 = searchNode(codeNext);
        double distance2 = s2.getDistanceToFirstStation();
        return abs((distance1 - distance2) / 60000);
    }

    public Timestamp convertToTimestamp(String timeStr) {
        SimpleDateFormat printFormat = new SimpleDateFormat("hh:mm a");
        Timestamp time = new Timestamp(60);
        try {
            time = new Timestamp(printFormat.parse(timeStr).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(RoutePlanningSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }

    public Timestamp strConvertToTimestamp(String timeStr) {
        SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
        Timestamp time = new Timestamp(60);
        try {
            time = new Timestamp(printFormat.parse(timeStr).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(RoutePlanningSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }

    public Timestamp dateConvertToTimestamp(String timeStr) {
        SimpleDateFormat printFormat = new SimpleDateFormat("yyyy-mm-dd");
        Timestamp time = new Timestamp(60);
        try {
            time = new Timestamp(printFormat.parse(timeStr).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(RoutePlanningSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
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

    @Override
    public String[] getFirstRowOfScheduleTable(String routeCode) {
        ArrayList<String> nodesInRoute = getNodes(routeCode);
        int col = nodesInRoute.size() * 2 - 2;
        String[] nodes = new String[col];
        nodes[0] = nodesInRoute.get(0) + " Departure Time";
        for (int i = 1; i < col - 2; i = i + 2) {
            nodes[i] = nodesInRoute.get((i + 1) / 2) + " Arrival Time";
            nodes[i + 1] = nodesInRoute.get((i + 1) / 2) + " Departure Time";
        }
        nodes[col - 1] = nodesInRoute.get(nodesInRoute.size() - 1) + " Arrival Time";
        return nodes;
    }

    @Override
    public Timestamp[][] getScheduleTable(String routeCode, String dayType, String time) {
        String[] arr = time.split(" - ");
        String startStr = arr[0];
        String endStr = arr[1];
        Timestamp periodStart = convertToTimestamp(startStr);
        Timestamp periodEnd = convertToTimestamp(endStr);
        Query q = em.createQuery("SELECT s FROM TrainScheduleEntity s WHERE s.dayType=:dayType AND s.periodStart=:periodStart AND s.periodEnd=:periodEnd AND s.updateVersion='new'");
        q.setParameter("dayType", dayType);
        q.setParameter("periodStart", periodStart);
        q.setParameter("periodEnd", periodEnd);
        TrainScheduleEntity ts = (TrainScheduleEntity) q.getResultList().get(0);

        int freq = (int) ts.getHeadway();
        double tsHeadway = ts.getHeadway();
        long milliseconds1 = periodStart.getTime();
        long milliseconds2 = periodEnd.getTime();
        double diff = milliseconds2 - milliseconds1;
        double diffHours = diff / (60 * 1000);
        int duration = abs((int) Math.ceil(diffHours));
        int row = duration / freq;

        ArrayList<String> nodesInRoute = getNodes(routeCode);
        int col = nodesInRoute.size() * 2 - 2;
        String[] nodes = getFirstRowOfScheduleTable(routeCode);

        Timestamp[][] scheduleTable = new Timestamp[row][col];
        Timestamp firstStationArrTime = periodStart;
        double timeFromDepotToFirstStation = 0;
        Timestamp depotDepartTime = new Timestamp(1000);

        if (nodes[0].substring(3, 5).equals("00")) {//for route XXX01-15
            timeFromDepotToFirstStation = ((searchNode(nodes[0].substring(0, 5)).getDistanceToFirstStation()) / 60000 * 3600);//in seconds
            depotDepartTime = new Timestamp(firstStationArrTime.getTime() - (long) (timeFromDepotToFirstStation * 1000));

            for (int i = 0; i < row; i++) {
                scheduleTable[i][0] = new Timestamp(depotDepartTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                scheduleTable[i][1] = new Timestamp(firstStationArrTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                for (int k = 1; k < col - 1; k = k + 2) {
                    scheduleTable[i][k + 1] = new Timestamp(scheduleTable[i][k].getTime() + (long) (1000 * ts.getWaitingTime()));
                    scheduleTable[i][k + 2] = new Timestamp(scheduleTable[i][k + 1].getTime() + (long) (1000 * 60 * 60 * calculateTimeToNextStation(nodes[k].substring(0, 5))));
                }
            }
        } else {//for route XXX15-01
            timeFromDepotToFirstStation = calculateTimeToPreviousStation(nodes[0].substring(0, 5)) * 3600;//in seconds
            depotDepartTime = new Timestamp(firstStationArrTime.getTime() - (long) (timeFromDepotToFirstStation * 1000));
            for (int i = 0; i < row; i++) {
                scheduleTable[i][0] = new Timestamp(depotDepartTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                scheduleTable[i][1] = new Timestamp(firstStationArrTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                for (int k = 1; k < col - 1; k = k + 2) {
                    scheduleTable[i][k + 1] = new Timestamp(scheduleTable[i][k].getTime() + (long) (1000 * ts.getWaitingTime()));
                    scheduleTable[i][k + 2] = new Timestamp(scheduleTable[i][k + 1].getTime() + (long) (1000 * 60 * 60 * calculateTimeToPreviousStation(nodes[k].substring(0, 5))));
                }
            }
        }

        return scheduleTable;
    }

    @Override
    public ArrayList<ArrayList<String>> getTrainSchedule(String routeCode, String dayType, String time) {
        Timestamp[][] scheduleInTimestamp = getScheduleTable(routeCode, dayType, time);
        ArrayList<ArrayList<String>> schedule = new ArrayList<ArrayList<String>>();

        //firstRow
        ArrayList<String> firstRow = new ArrayList<String>();
        String[] nodeName = getFirstRowOfScheduleTable(routeCode);
        firstRow.add("Trip Number");
        for (int i = 0; i < nodeName.length; i++) {
            firstRow.add(nodeName[i]);
        }
        schedule.add(firstRow);
        //System.out.println(firstRow.toString());

        for (int i = 0; i < scheduleInTimestamp.length; i++) {
            ArrayList<String> oneRow = new ArrayList<String>();
            oneRow.add(String.valueOf(i + 1));
            for (int k = 0; k < scheduleInTimestamp[0].length; k++) {
                oneRow.add(new SimpleDateFormat("HH:mm:ss").format(scheduleInTimestamp[i][k]));
            }
            schedule.add(oneRow);
        }

        return schedule;
    }

    ////////////////////update train schedule structure for special days////////////////////
    @Override
    public void createTempSpecialDayTrainSchedule(Timestamp date) {
        if (!specialDaySchedule.isEmpty()) {
            specialDaySchedule.clear();
        }
        Query query = em.createQuery("SELECT ts FROM SpecialDayAlgoEntity ts WHERE ts.date=:date");
        query.setParameter("date", date);
        if (!query.getResultList().isEmpty()) {
            for (Object o : query.getResultList()) {
                SpecialDayAlgoEntity ts = (SpecialDayAlgoEntity) o;
                SpecialDayAlgoEntity tempTs = new SpecialDayAlgoEntity(date, ts.getPeriodType(), ts.getPeriodStart(), ts.getPeriodEnd(), ts.getHeadway(), ts.getWaitingTime());
                specialDaySchedule.add(tempTs);
            }
            for (int i = 0; i < specialDaySchedule.size(); i++) {
                Long id = new Long(i);
                specialDaySchedule.get(i).setSpecialDayAlgoId(id);
            }
        }
    }

    @Override
    public void addSpecialDayTrainSchedule(Timestamp date, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime) {
        SpecialDayAlgoEntity specialDayAlgo = new SpecialDayAlgoEntity(date, newPeriodType,
                newPeriodStart, newPeriodEnd, newHeadway, newWaitingTime);
        specialDaySchedule.add(specialDayAlgo);
        for (int i = 0; i < specialDaySchedule.size(); i++) {
            Long id = new Long(i);
            specialDaySchedule.get(i).setSpecialDayAlgoId(id);
        }
    }

    @Override
    public SpecialDayAlgoEntity searchSpecialDayTrainSchedule(Long id) {
        SpecialDayAlgoEntity ts = new SpecialDayAlgoEntity();
        for (int i = 0; i < specialDaySchedule.size(); i++) {
            if (specialDaySchedule.get(i).getSpecialDayAlgoId() == id) {
                ts = specialDaySchedule.get(i);
            }
        }
        return ts;
    }

    @Override
    public void editSpecialDayTrainSchedule(Long id, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime) {
        for (int i = 0; i < specialDaySchedule.size(); i++) {
            SpecialDayAlgoEntity ts = specialDaySchedule.get(i);
            if (ts.getSpecialDayAlgoId() == id) {
                ts.setPeriodType(newPeriodType);
                ts.setPeriodStart(newPeriodStart);
                ts.setPeriodEnd(newPeriodEnd);
                ts.setHeadway(newHeadway);
                ts.setWaitingTime(newWaitingTime);
            }
        }
    }

    @Override
    public void deleteSpecialDayTrainSchedule(Long id) {
        for (int i = 0; i < specialDaySchedule.size(); i++) {
            if (specialDaySchedule.get(i).getSpecialDayAlgoId() == id) {
                specialDaySchedule.remove(i);
            }
        }
    }

    @Override
    public void updateSpecialDayTrainSchedule(Timestamp date) {
        Query query = em.createQuery("SELECT ts FROM SpecialDayAlgoEntity ts WHERE ts.date=:date");
        query.setParameter("date", date);
        if (!query.getResultList().isEmpty()) {
            for (Object o : query.getResultList()) {
                SpecialDayAlgoEntity spd = (SpecialDayAlgoEntity) o;
                em.remove(spd);
            }
        }
        for (int i = 0; i < specialDaySchedule.size(); i++) {
            specialDaySchedule.get(i).setSpecialDayAlgoId(null);
            em.persist(specialDaySchedule.get(i));
        }
        Query q = em.createQuery("SELECT r FROM RouteEntity r");
        List routeList = new ArrayList();
        for (Object o : q.getResultList()) {
            RouteEntity r = (RouteEntity) o;
            r.setSpecialDayTrainSchedules(specialDaySchedule);
        }
    }

    @Override
    public ArrayList<SpecialDayAlgoEntity> viewSpecialDaySchedule() {
        return specialDaySchedule;
    }

    @Override
    public ArrayList<SpecialDayAlgoEntity> viewUpdatedSpecialDaySchedule(Timestamp date) {
        ArrayList<SpecialDayAlgoEntity> specialDayAlgo= new ArrayList<SpecialDayAlgoEntity>();
        Query query = em.createQuery("SELECT ts FROM SpecialDayAlgoEntity ts WHERE ts.date=:date");
        query.setParameter("date", date);
        for (Object o : query.getResultList()) {
            specialDayAlgo.add((SpecialDayAlgoEntity) o);
        }
        return specialDayAlgo;
    }

    @Override
    public Timestamp[][] getSpecialDayScheduleTable(String routeCode, Timestamp date, Timestamp periodStart, Timestamp periodEnd) {
        Query q = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s WHERE s.date=:date AND s.periodStart=:periodStart AND s.periodEnd=:periodEnd");
        q.setParameter("date", date);
        q.setParameter("periodStart", periodStart);
        q.setParameter("periodEnd", periodEnd);
        SpecialDayAlgoEntity ts = (SpecialDayAlgoEntity) q.getResultList().get(0);

        int freq = (int) ts.getHeadway();
        double tsHeadway = ts.getHeadway();
        long milliseconds1 = periodStart.getTime();
        long milliseconds2 = periodEnd.getTime();
        double diff = milliseconds2 - milliseconds1;
        double diffHours = diff / (60 * 1000);
        int duration = abs((int) Math.ceil(diffHours));
        int row = duration / freq;

        ArrayList<String> nodesInRoute = getNodes(routeCode);
        int col = nodesInRoute.size() * 2 - 2;
        String[] nodes = getFirstRowOfScheduleTable(routeCode);

        Timestamp[][] scheduleTable = new Timestamp[row][col];
        Timestamp firstStationArrTime = periodStart;
        double timeFromDepotToFirstStation = 0;//in seconds
        Timestamp depotDepartTime = new Timestamp(1000);

        if (nodes[0].substring(3, 5).equals("00")) {//for route XXX01-15
            timeFromDepotToFirstStation = ((searchNode(nodes[0].substring(0, 5)).getDistanceToFirstStation()) / 60000 * 3600);//in seconds
            depotDepartTime = new Timestamp(firstStationArrTime.getTime() - (long) (timeFromDepotToFirstStation * 1000));

            for (int i = 0; i < row; i++) {
                scheduleTable[i][0] = new Timestamp(depotDepartTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                scheduleTable[i][1] = new Timestamp(firstStationArrTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                for (int k = 1; k < col - 1; k = k + 2) {
                    scheduleTable[i][k + 1] = new Timestamp(scheduleTable[i][k].getTime() + (long) (1000 * ts.getWaitingTime()));
                    scheduleTable[i][k + 2] = new Timestamp(scheduleTable[i][k].getTime() + (long) (1000 * 60 * 60 * calculateTimeToNextStation(nodes[k].substring(0, 5))));
                }
            }
        } else {//for route XXX15-01
            timeFromDepotToFirstStation = calculateTimeToPreviousStation(nodes[0].substring(0, 5)) * 3600;//in seconds
            depotDepartTime = new Timestamp(firstStationArrTime.getTime() - (long) (timeFromDepotToFirstStation * 1000));
            for (int i = 0; i < row; i++) {
                scheduleTable[i][0] = new Timestamp(depotDepartTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                scheduleTable[i][1] = new Timestamp(firstStationArrTime.getTime() + (long) (1000 * 60 * tsHeadway * i));
                for (int k = 1; k < col - 1; k = k + 2) {
                    scheduleTable[i][k + 1] = new Timestamp(scheduleTable[i][k].getTime() + (long) (1000 * ts.getWaitingTime()));
                    scheduleTable[i][k + 2] = new Timestamp(scheduleTable[i][k].getTime() + (long) (1000 * 60 * 60 * calculateTimeToPreviousStation(nodes[k].substring(0, 5))));
                }
            }
        }

        return scheduleTable;
    }

    @Override
    public ArrayList<ArrayList<String>> getSpecialDaySchedule(Timestamp date, String routeCode, String dayType, String time) {
        ArrayList<ArrayList<String>> resultTable = new ArrayList<ArrayList<String>>();
        String[] arr = time.split(" - ");
        String startStr = arr[0];
        String endStr = arr[1];
        Timestamp periodStart = convertToTimestamp(startStr);
        Timestamp periodEnd = convertToTimestamp(endStr);
        Timestamp[][] specialDayScheduleInTimestamp = getSpecialDayScheduleTable(routeCode, date, periodStart, periodEnd);

        //firstRow
        ArrayList<String> firstRow = new ArrayList<String>();
        String[] nodeName = getFirstRowOfScheduleTable(routeCode);
        firstRow.add("Trip Number");
        for (int i = 0; i < nodeName.length; i++) {
            firstRow.add(nodeName[i]);
        }
        resultTable.add(firstRow);

        for (int i = 0; i < specialDayScheduleInTimestamp.length; i++) {
            ArrayList<String> oneRow = new ArrayList<String>();
            oneRow.add(String.valueOf(i + 1));
            for (int k = 0; k < specialDayScheduleInTimestamp[0].length; k++) {
                oneRow.add(new SimpleDateFormat("HH:mm:ss").format(specialDayScheduleInTimestamp[i][k]));
            }
            resultTable.add(oneRow);
        }

        return resultTable;

    }

    ////////////////////update train schedule structure////////////////////
    @Override
    public ArrayList<TrainScheduleEntity> viewTrainSchedule(String dayType) {
        ArrayList<TrainScheduleEntity> trainSchedule = new ArrayList<TrainScheduleEntity>();
        Query query = em.createQuery("SELECT ts FROM TrainScheduleEntity ts WHERE ts.dayType=:dayType AND ts.updateVersion='new'");
        query.setParameter("dayType", dayType);
        for (Object o : query.getResultList()) {
            trainSchedule.add((TrainScheduleEntity) o);
        }
        return trainSchedule;
    }

    @Override
    public void createTempTrainSchedule(String dayType) {
        if (!tempSchedule.isEmpty()) {
            tempSchedule.clear();
        }
        Query query = em.createQuery("SELECT ts FROM TrainScheduleEntity ts WHERE ts.dayType=:dayType AND ts.updateVersion='new'");
        query.setParameter("dayType", dayType);
        for (Object o : query.getResultList()) {
            TrainScheduleEntity ts = (TrainScheduleEntity) o;
            TrainScheduleEntity tempTs = new TrainScheduleEntity(ts.getDayType(), ts.getPeriodType(), ts.getPeriodStart(), ts.getPeriodEnd(), ts.getHeadway(), ts.getWaitingTime(), ts.getUpdateVersion());
            tempSchedule.add(tempTs);
        }
        for (int i = 0; i < tempSchedule.size(); i++) {
            Long id = new Long(i);
            tempSchedule.get(i).setTrainScheduleId(id);
        }
    }

    @Override
    public TrainScheduleEntity searchTempTrainSchedule(Long trainScheduleId) {
        TrainScheduleEntity ts = new TrainScheduleEntity();
        for (int i = 0; i < tempSchedule.size(); i++) {
            if (tempSchedule.get(i).getTrainScheduleId() == trainScheduleId) {
                ts = tempSchedule.get(i);
            }
        }
        return ts;
    }

    @Override
    public void editTempTrainSchedule(Long trainScheduleId, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime) {
        for (int i = 0; i < tempSchedule.size(); i++) {
            TrainScheduleEntity ts = tempSchedule.get(i);
            if (ts.getTrainScheduleId() == trainScheduleId) {
                ts.setPeriodType(newPeriodType);
                ts.setPeriodStart(newPeriodStart);
                ts.setPeriodEnd(newPeriodEnd);
                ts.setHeadway(newHeadway);
                ts.setWaitingTime(newWaitingTime);
            }
        }
    }

    @Override
    public void deleteTempTrainSchedule(Long trainScheduleId) {
        for (int i = 0; i < tempSchedule.size(); i++) {
            if (tempSchedule.get(i).getTrainScheduleId() == trainScheduleId) {
                tempSchedule.remove(i);
            }
        }
    }

    @Override
    public void addTempTrainSchedule(String dayType, String newPeriodType, Timestamp newPeriodStart, Timestamp newPeriodEnd, double newHeadway, double newWaitingTime) {
        TrainScheduleEntity ts = new TrainScheduleEntity(dayType, newPeriodType, newPeriodStart, newPeriodEnd, newHeadway, newWaitingTime, "new");
        Long id = new Long(tempSchedule.size());
        ts.setTrainScheduleId(id);
        tempSchedule.add(ts);
    }

    @Override
    public ArrayList<TrainScheduleEntity> getTempTrainSchedule() {
        return tempSchedule;
    }

    @Override
    public void updateTrainSchedule(String dayType) {
        Query q = em.createQuery("SELECT s FROM TrainScheduleEntity s WHERE s.dayType=:dayType AND s.updateVersion='new'");
        q.setParameter("dayType", dayType);
        for (Object o : q.getResultList()) {
            TrainScheduleEntity ts = (TrainScheduleEntity) o;
            ts.setUpdateVersion(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        }

        for (int i = 0; i < tempSchedule.size(); i++) {
            tempSchedule.get(i).setUpdateVersion("new");
            tempSchedule.get(i).setTrainScheduleId(null);
            em.persist(tempSchedule.get(i));
        }
        tempSchedule.clear();
    }

    ////////////////////view train schedule for operation staff////////////////////
    @Override
    public ArrayList<String> getNodeRouteList(String code) {
        //System.out.println("enter");
        NodeEntity node = searchNode(code);
        ArrayList<String> routeList = new ArrayList<String>();
        Set<RouteEntity> routeSet = node.getRoutes();
        Iterator<RouteEntity> route = routeSet.iterator();
        while (route.hasNext()) {
            RouteEntity r = route.next();
            routeList.add(r.getCode());
        }
        return routeList;
    }

    @Override
    public void storeSpecialDayTripStationSchedule() {
        Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss");
        if (query.getResultList().isEmpty()) {
            Query q = em.createQuery("SELECT s FROM SpecialDayAlgoEntity s ORDER BY s.periodStart ASC");
            ArrayList<SpecialDayAlgoEntity> allSpecialDaySchedules = new ArrayList<>();
            for (Object o : q.getResultList()) {
                SpecialDayAlgoEntity ts = (SpecialDayAlgoEntity) o;
                allSpecialDaySchedules.add(ts);
            }
//            Collections.sort(allSpecialDaySchedules, new Comparator<SpecialDayAlgoEntity>() {
//                public int compare(SpecialDayAlgoEntity o1, SpecialDayAlgoEntity o2) {
//                    if (o1.getPeriodStart() == null || o2.getPeriodStart() == null) {
//                        return 0;
//                    }
//                    return o1.getPeriodStart().compareTo(o2.getPeriodStart());
//                }
//            }
//            );
            //String[] nodes = getFirstRowOfScheduleTable(routeCode);
            for (int i = 0; i < allSpecialDaySchedules.size(); i++) {//i = number of special day periods
                SpecialDayAlgoEntity oneSpecialDayPeriod = allSpecialDaySchedules.get(i);
                for (int h = 0; h < oneSpecialDayPeriod.getRoutes().size(); h++) {//h = number of routes
                    String routeCode = oneSpecialDayPeriod.getRoutes().get(h).getCode();
                    Timestamp[][] specialDayTrainSchedule = getSpecialDayScheduleTable(routeCode, oneSpecialDayPeriod.getDate(), oneSpecialDayPeriod.getPeriodStart(), oneSpecialDayPeriod.getPeriodEnd());
                    //for depots
                    tripNumberDepot = 1;
                    for (int a = 0; a < specialDayTrainSchedule.length; a++) {//a = number of rows of the 2D array
                        int numberOfNodes = getNodes(routeCode).size();
                        //first depot
                        TripStationScheduleEntity tss1 = new TripStationScheduleEntity(oneSpecialDayPeriod.getDate(), null, tripNumberDepot, null, specialDayTrainSchedule[a][0], "normal");
                        tss1.setRoute(searchRoute(routeCode));
                        tss1.setNode(searchNode(getNodes(routeCode).get(0)));
                        tss1.setSpecialDayAlgo(oneSpecialDayPeriod);
                        tss1.setTrainSchedule(null);
                        //last depot
                        TripStationScheduleEntity tss2 = new TripStationScheduleEntity(oneSpecialDayPeriod.getDate(), null, tripNumberDepot, specialDayTrainSchedule[a][specialDayTrainSchedule[0].length - 1], null, "normal");
                        tss2.setRoute(searchRoute(routeCode));
                        tss2.setNode(searchNode(getNodes(routeCode).get(numberOfNodes - 1)));
                        tss2.setSpecialDayAlgo(oneSpecialDayPeriod);
                        tss2.setTrainSchedule(null);
                        em.persist(tss1);
                        em.persist(tss2);
                        tripNumberDepot++;
                    }
                    //for stations
                    String[] nodes = getFirstRowOfScheduleTable(routeCode);
                    for (int k = 1; k < specialDayTrainSchedule[0].length - 1; k = k + 2) {//columns
                        tripNumberStation = 1;
                        for (int j = 0; j < specialDayTrainSchedule.length; j++) {//rows
                            TripStationScheduleEntity tss = new TripStationScheduleEntity(oneSpecialDayPeriod.getDate(), null, tripNumberStation, specialDayTrainSchedule[j][k], specialDayTrainSchedule[j][k + 1], "normal");
                            tss.setRoute(searchRoute(routeCode));
                            tss.setNode(searchNode(nodes[k].substring(0, 5)));
                            tss.setSpecialDayAlgo(oneSpecialDayPeriod);
                            tss.setTrainSchedule(null);
                            em.persist(tss);
                            tripNumberStation++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void storeRegularDayTripStationSchedule() {
        Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss");
        if (query.getResultList().isEmpty()) {
            Query q = em.createQuery("SELECT s FROM TrainScheduleEntity s ORDER BY s.dayType,s.periodStart ASC");
            ArrayList<TrainScheduleEntity> allRegularDaySchedules = new ArrayList<>();
            for (Object o : q.getResultList()) {
                TrainScheduleEntity ts = (TrainScheduleEntity) o;
                allRegularDaySchedules.add(ts);
            }
//            Collections.sort(allRegularDaySchedules, new Comparator<TrainScheduleEntity>() {
//                public int compare(TrainScheduleEntity o1, TrainScheduleEntity o2) {
//                    if (o1.getPeriodStart() == null || o2.getPeriodStart() == null) {
//                        return 0;
//                    }
//                    return o1.getPeriodStart().compareTo(o2.getPeriodStart());
//                }
//            }
//            );

            Query q1 = em.createQuery("SELECT rs FROM RollingStockEntity rs WHERE rs.status='Available'");
            ArrayList<RollingStockEntity> availableRollingStocks = new ArrayList<>();
            for (Object o : q1.getResultList()) {
                RollingStockEntity rs = (RollingStockEntity) o;
                availableRollingStocks.add(rs);
            }
            //String[] nodes = getFirstRowOfScheduleTable(routeCode);
            for (int i = 0; i < allRegularDaySchedules.size(); i++) {//i = number of periods
                System.out.println(allRegularDaySchedules.size() + " i" + i + " " + new Date());
                TrainScheduleEntity oneRegularDayPeriod = allRegularDaySchedules.get(i);
                for (int h = 0; h < oneRegularDayPeriod.getRoutes().size(); h++) {//h = number of routes

                    System.out.println(oneRegularDayPeriod.getRoutes().size() + " h" + h + " " + new Date());
                    String routeCode = oneRegularDayPeriod.getRoutes().get(h).getCode();
                    String period;
                    String s1 = new SimpleDateFormat("hh:mm a").format(oneRegularDayPeriod.getPeriodStart());
                    String s2 = new SimpleDateFormat("hh:mm a").format(oneRegularDayPeriod.getPeriodEnd());
                    period = s1 + " - " + s2;
                    Timestamp[][] regularDayTrainSchedule = getScheduleTable(routeCode, oneRegularDayPeriod.getDayType(), period);
                    //for depots
                    tripNumberDepot = 1;
                    for (int a = 0; a < regularDayTrainSchedule.length; a++) {//a = number of rows of the 2D array
                        int numberOfNodes = getNodes(routeCode).size();
                        //first depot
                        TripStationScheduleEntity tss1 = new TripStationScheduleEntity(null, oneRegularDayPeriod.getDayType(), tripNumberDepot, null, regularDayTrainSchedule[a][0], "normal");
                        tss1.setRoute(searchRoute(routeCode));
                        tss1.setNode(searchNode(getNodes(routeCode).get(0)));
                        tss1.setSpecialDayAlgo(null);
                        tss1.setTrainSchedule(oneRegularDayPeriod);
                        tss1.setRollingStock(availableRollingStocks.get(0));

                        //last depot
                        TripStationScheduleEntity tss2 = new TripStationScheduleEntity(null, oneRegularDayPeriod.getDayType(), tripNumberDepot, regularDayTrainSchedule[a][regularDayTrainSchedule[0].length - 1], null, "normal");
                        tss2.setRoute(searchRoute(routeCode));
                        tss2.setNode(searchNode(getNodes(routeCode).get(numberOfNodes - 1)));
                        tss2.setSpecialDayAlgo(null);
                        tss2.setTrainSchedule(oneRegularDayPeriod);
                        tss2.setRollingStock(availableRollingStocks.get(1));
                        em.persist(tss1);
                        em.persist(tss2);
                        tripNumberDepot++;
                    }

                    //for stations
                    String[] nodes = getFirstRowOfScheduleTable(routeCode);
                    for (int k = 1; k < regularDayTrainSchedule[0].length - 1; k = k + 2) {//columns
                        tripNumberStation = 1;
                        for (int j = 0; j < regularDayTrainSchedule.length; j++) {//rows
                            TripStationScheduleEntity tss = new TripStationScheduleEntity(null, oneRegularDayPeriod.getDayType(), tripNumberStation, regularDayTrainSchedule[j][k], regularDayTrainSchedule[j][k + 1], "normal");
                            tss.setRoute(searchRoute(routeCode));
                            tss.setNode(searchNode(nodes[k].substring(0, 5)));
                            tss.setSpecialDayAlgo(null);
                            tss.setTrainSchedule(oneRegularDayPeriod);
                            tss.setRollingStock(availableRollingStocks.get(0));
                            em.persist(tss);
                            tripNumberStation++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void testStoreRegularDayTripStationSchedule() {
        Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss");
        if (query.getResultList().isEmpty()) {

            Query q = em.createQuery("SELECT s FROM TrainScheduleEntity s WHERE s.dayType='Weekday' ORDER BY s.periodStart ASC");
            ArrayList<TrainScheduleEntity> allRegularDaySchedules = new ArrayList<>();
            for (Object o : q.getResultList()) {
                TrainScheduleEntity ts = (TrainScheduleEntity) o;
                allRegularDaySchedules.add(ts);
            }

            Query q1 = em.createQuery("SELECT rs FROM RollingStockEntity rs WHERE rs.status='Available'");
            ArrayList<RollingStockEntity> availableRollingStocks = new ArrayList<>();
            for (Object o : q1.getResultList()) {
                RollingStockEntity rs = (RollingStockEntity) o;
                availableRollingStocks.add(rs);
            }

            //String[] nodes = getFirstRowOfScheduleTable(routeCode);
            //for (int i = 0; i < allRegularDaySchedules.size(); i++) {//i = number of periods
            System.out.println("reach here le ma?i" + "0");
            TrainScheduleEntity oneRegularDayPeriod = allRegularDaySchedules.get(0);
            //for (int i = 0; i < allRegularDaySchedules.size(); i++) {//i = number of periods
            //TrainScheduleEntity oneRegularDayPeriod = allRegularDaySchedules.get(1);
            for (int h = 0; h < oneRegularDayPeriod.getRoutes().size(); h++) {//h = number of routes

                System.out.println("reach here le ma?h" + h + "i" + 0);
                String routeCode = oneRegularDayPeriod.getRoutes().get(h).getCode();
                String period;
                String s1 = new SimpleDateFormat("hh:mm a").format(oneRegularDayPeriod.getPeriodStart());
                String s2 = new SimpleDateFormat("hh:mm a").format(oneRegularDayPeriod.getPeriodEnd());
                period = s1 + " - " + s2;
                Timestamp[][] regularDayTrainSchedule = getScheduleTable(routeCode, oneRegularDayPeriod.getDayType(), period);
                //for depots
                tripNumberDepot = 1;
                for (int a = 0; a < regularDayTrainSchedule.length; a++) {//a = number of rows of the 2D array

                    int numberOfNodes = getNodes(routeCode).size();
                    //first depot
                    TripStationScheduleEntity tss1 = new TripStationScheduleEntity(null, oneRegularDayPeriod.getDayType(), tripNumberDepot, null, regularDayTrainSchedule[a][0], "normal");
                    tss1.setRoute(searchRoute(routeCode));
                    tss1.setNode(searchNode(getNodes(routeCode).get(0)));
                    tss1.setSpecialDayAlgo(null);
                    tss1.setTrainSchedule(oneRegularDayPeriod);
                    tss1.setRollingStock(availableRollingStocks.get(0));
                    //last depot
                    TripStationScheduleEntity tss2 = new TripStationScheduleEntity(null, oneRegularDayPeriod.getDayType(), tripNumberDepot, regularDayTrainSchedule[a][regularDayTrainSchedule[0].length - 1], null, "normal");
                    tss2.setRoute(searchRoute(routeCode));
                    tss2.setNode(searchNode(getNodes(routeCode).get(numberOfNodes - 1)));
                    tss2.setSpecialDayAlgo(null);
                    tss2.setTrainSchedule(oneRegularDayPeriod);
                    tss2.setRollingStock(availableRollingStocks.get(1));
                    em.persist(tss1);
                    em.persist(tss2);
                    tripNumberDepot++;
                }

                //for stations
                String[] nodes = getFirstRowOfScheduleTable(routeCode);
                for (int k = 1; k < regularDayTrainSchedule[0].length - 1; k = k + 2) {//columns
                    tripNumberStation = 1;
                    for (int j = 0; j < regularDayTrainSchedule.length; j++) {//rows
                        TripStationScheduleEntity tss = new TripStationScheduleEntity(null, oneRegularDayPeriod.getDayType(), tripNumberStation, regularDayTrainSchedule[j][k], regularDayTrainSchedule[j][k + 1], "normal");
                        tss.setRoute(searchRoute(routeCode));
                        tss.setNode(searchNode(nodes[k].substring(0, 5)));
                        tss.setSpecialDayAlgo(null);
                        tss.setTrainSchedule(oneRegularDayPeriod);
                        tss.setRollingStock(availableRollingStocks.get(0));
                        em.persist(tss);
                        tripNumberStation++;
                    }
                }

            }

        }

    }

    @Override
    public ArrayList<TripStationScheduleEntity> getTripStationSchedule(String routeCode, String nodeCode, Timestamp date, String dayType) {
        System.out.println(routeCode + "_" + nodeCode + "_" + date + "_" + dayType);
        Query q1 = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.route.code=:routeCode AND (s.date=:date OR s.dayType=:dayType) AND s.status='emergency'");
        q1.setParameter("routeCode", routeCode);
        q1.setParameter("date", date);
        q1.setParameter("dayType", dayType);
        ArrayList<TripStationScheduleEntity> tripStationSchedules = new ArrayList<>();
        if (q1.getResultList().isEmpty()) {//no emergency
            Query q = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.route.code=:routeCode AND s.node.code=:nodeCode AND s.status='normal' AND (s.date=:date OR s.dayType=:dayType)");
            q.setParameter("routeCode", routeCode);
            q.setParameter("nodeCode", nodeCode);
            q.setParameter("date", date);
            q.setParameter("dayType", dayType);
            //ArrayList<TripStationScheduleEntity> tripStationSchedules = new ArrayList<>();
            for (Object o : q.getResultList()) {
                TripStationScheduleEntity ts = (TripStationScheduleEntity) o;
                tripStationSchedules.add(ts);
            }
        } else {
            Query q = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.route.code=:routeCode AND s.node.code=:nodeCode AND s.status='emergency' AND s.date=:date ORDER BY s.arrivalTime ASC");
            q.setParameter("routeCode", routeCode);
            q.setParameter("nodeCode", nodeCode);
            q.setParameter("date", date);
            //ArrayList<TripStationScheduleEntity> tripStationSchedules = new ArrayList<>();
            for (Object o : q.getResultList()) {
                TripStationScheduleEntity ts = (TripStationScheduleEntity) o;
                tripStationSchedules.add(ts);
            }
        }
        return tripStationSchedules;
    }

    ////////////////////handle emergency////////////////////
    @Override
    public boolean isAfterCurrentTime(Timestamp currentTime, Timestamp oneTime) {
        Date currentTimeInDate = new Date(currentTime.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTimeInDate);
        cal.set(1970, Calendar.JANUARY, 01);//keep the time while changing the date to 1970-01-01
        currentTimeInDate = cal.getTime();
        Timestamp transformedCurrentTime = new Timestamp(currentTimeInDate.getTime());
        return oneTime.after(transformedCurrentTime);
    }

    @Override
    public void createEmergencyTripStationSchedule(String routeCode, Timestamp currentDate, String currentDayType, Timestamp currentTime, double delay) {
        Query q = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.route.code=:routeCode AND s.status='normal' AND (s.date=:date OR s.dayType=:dayType) ORDER BY s.arrivalTime ASC");
        q.setParameter("date", currentDate);
        q.setParameter("dayType", currentDayType);
        System.out.println("tss" + q.getResultList().size());

        ArrayList<TripStationScheduleEntity> emergentTripStationSchedules = new ArrayList<>();
        for (Object o : q.getResultList()) {
            TripStationScheduleEntity ts = (TripStationScheduleEntity) o;
            if (ts.getArrivalTime() == null) {//first depot
                if (isAfterCurrentTime(currentTime, ts.getDepartureTime())) {
                    emergentTripStationSchedules.add(ts);
                }
            } else if (ts.getDepartureTime() == null) {//last depot
                if (isAfterCurrentTime(currentTime, ts.getArrivalTime())) {
                    emergentTripStationSchedules.add(ts);
                }
            } else {//stations
                if (isAfterCurrentTime(currentTime, ts.getArrivalTime())) {
                    emergentTripStationSchedules.add(ts);
                }
            }
        }
        ArrayList<TripStationScheduleEntity> tempEmergency = new ArrayList<>();
        for (int i = 0; i < emergentTripStationSchedules.size(); i++) {
            TripStationScheduleEntity emergency = emergentTripStationSchedules.get(i);//from database
            TripStationScheduleEntity copy = new TripStationScheduleEntity(currentDate, currentDayType, emergency.getTripNum(), emergency.getArrivalTime(), emergency.getDepartureTime(), "emergency");//copy  instances
            copy.setRoute(emergency.getRoute());
            copy.setNode(emergency.getNode());
            copy.setSpecialDayAlgo(emergency.getSpecialDayAlgo());
            copy.setTrainSchedule(emergency.getTrainSchedule());
            tempEmergency.add(copy);
        }

        for (int k = 0; k < tempEmergency.size(); k++) {
            TripStationScheduleEntity emergencyCopy = tempEmergency.get(k);
            Timestamp delayedDeparture = new Timestamp(10);
            Timestamp delayedArrival = new Timestamp(10);
            if (emergencyCopy.getArrivalTime() == null) {//first depot
                delayedDeparture = new Timestamp(emergencyCopy.getDepartureTime().getTime() + (long) delay * 60 * 1000);
                emergencyCopy.setDepartureTime(delayedDeparture);
            } else if (emergencyCopy.getDepartureTime() == null) {//last depot
                delayedArrival = new Timestamp(emergencyCopy.getArrivalTime().getTime() + (long) delay * 60 * 1000);
                emergencyCopy.setArrivalTime(delayedArrival);
            } else {
                delayedDeparture = new Timestamp(emergencyCopy.getDepartureTime().getTime() + (long) delay * 60 * 1000);
                delayedArrival = new Timestamp(emergencyCopy.getArrivalTime().getTime() + (long) delay * 60 * 1000);
                emergencyCopy.setArrivalTime(delayedArrival);
                emergencyCopy.setDepartureTime(delayedDeparture);
            }
            em.persist(emergencyCopy);
        }
        Query q1 = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.status='normal'");
        Query q2 = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.status='emergency'");
        System.out.println("normal :" + q1.getResultList().size());
        System.out.println("emergency :" + q2.getResultList().size());
        System.out.println("end");
    }

    public ArrayList<ArrayList<TripStationScheduleEntity>> getCentralTrainScheduleTable(String routeCode, Timestamp date, String dayType, String time) {
        String[] arr = time.split(" - ");
        String startStr = arr[0];
        String endStr = arr[1];
        Timestamp periodStart = convertToTimestamp(startStr);
        Timestamp periodEnd = convertToTimestamp(endStr);
        Query q = em.createQuery("SELECT s FROM TripStationScheduleEntity s WHERE s.route.code=:routeCode AND s.status='normal' AND (s.date=:date OR s.dayType=:dayType) AND s.trainSchedule.periodStart=:periodStart AND s.trainSchedule.periodEnd=:periodEnd ORDER BY s.node.code, s.tripNum");
        q.setParameter("routeCode", routeCode);
        q.setParameter("date", date);
        q.setParameter("dayType", dayType);
        q.setParameter("periodStart", periodStart);
        q.setParameter("periodEnd", periodEnd);
        ArrayList<TripStationScheduleEntity> allNodesSchedule = new ArrayList<>();
        ArrayList<TripStationScheduleEntity> oneNodeSchedule = new ArrayList<>();
        ArrayList<ArrayList<TripStationScheduleEntity>> table = new ArrayList<ArrayList<TripStationScheduleEntity>>();

        for (Object o : q.getResultList()) {
            TripStationScheduleEntity ts = (TripStationScheduleEntity) o;
            allNodesSchedule.add(ts);
        }
        ArrayList<String> nodes = getNodes(routeCode);

        for (int k = 0; k < nodes.size(); k++) {
            for (int i = 0; i < allNodesSchedule.size(); i++) {
                if ((allNodesSchedule.get(i).getNode().getCode()).equals(nodes.get(k))) {
                    oneNodeSchedule.add(allNodesSchedule.get(i));
                }
            }
            table.add(oneNodeSchedule);
        }
        return table;
    }

    ////////////////////assign rolling stock for each trip////////////////////
    @Override
    public ArrayList<ArrayList<String>> collateWholeDaySchedule(String routeCode, String dayType) {
        ArrayList<ArrayList<String>> wholeDaySchedule = new ArrayList<ArrayList<String>>();

        Query q = em.createQuery("SELECT s FROM TrainScheduleEntity s WHERE s.dayType=:dayType ORDER BY s.periodStart ASC");
        q.setParameter("dayType", dayType);
        ArrayList<TrainScheduleEntity> oneRegularDaySchedules = new ArrayList<>();
        for (Object o : q.getResultList()) {
            TrainScheduleEntity ts = (TrainScheduleEntity) o;
            oneRegularDaySchedules.add(ts);
        }
        ArrayList<String> firstRow = new ArrayList<String>();
        String[] nodeName = getFirstRowOfScheduleTable(routeCode);
        firstRow.add("Trip Number");
        for (int i = 0; i < nodeName.length; i++) {
            firstRow.add(nodeName[i]);
        }
        firstRow.add("Rolling Stock Assigned");
        wholeDaySchedule.add(firstRow);
        for (int p = 0; p < oneRegularDaySchedules.size(); p++) {//p: number of periods within the particular day type
            TrainScheduleEntity onePeriod = oneRegularDaySchedules.get(p);
            String period;
            String s1 = new SimpleDateFormat("hh:mm a").format(onePeriod.getPeriodStart());
            String s2 = new SimpleDateFormat("hh:mm a").format(onePeriod.getPeriodEnd());
            period = s1 + " - " + s2;
            ArrayList<ArrayList<String>> onePeriodSchedule = getTrainSchedule(routeCode, dayType, period);
            for (int row = 1; row < onePeriodSchedule.size(); row++) {
                wholeDaySchedule.add(onePeriodSchedule.get(row));
            }
        }
        for (int i = 1; i < wholeDaySchedule.size(); i++) {
            wholeDaySchedule.get(i).add("empty");
        }
        return wholeDaySchedule;

    }

//    @Override
//    public ArrayList<ArrayList<String>> assignRollingStock_selectedColumns(String routeCode, String dayType) {
//        ArrayList<ArrayList<String>> table = assignRollingStock(routeCode, dayType);
//        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
//        for(int i=0; i<table.size(); i++){
//            ArrayList<String> row = new ArrayList<String>();
//            row.add(table.get(i).get(0));
//            row.add(table.get(i).get(1));
//            row.add(table.get(i).get(2));
//            row.add(table.get(i).get(table.get(0).size() - 3));
//            row.add(table.get(i).get(table.get(0).size() - 2));
//            row.add(table.get(i).get(table.get(0).size() - 1));
//            result.add(row);
//        }
//        
//        return result;
//    }
    @Override
    public ArrayList<ArrayList<String>> assignRollingStock(String routeCode, String dayType) {
        String routeCode_opp = findOppositeRoute(routeCode);
        ArrayList<ArrayList<String>> table = collateWholeDaySchedule(routeCode, dayType);
        ArrayList<ArrayList<String>> table_opp = collateWholeDaySchedule(routeCode_opp, dayType);

        //get the available rolling stocks
        Query q1 = em.createQuery("SELECT rs FROM RollingStockEntity rs WHERE rs.status='Available' AND rs.depot=:depot ORDER BY rs.infraId");
        q1.setParameter("depot", searchNode(getNodes(routeCode).get(0)));
        Query q2 = em.createQuery("SELECT rs FROM RollingStockEntity rs WHERE rs.status='Available' AND rs.depot=:depotOpp ORDER BY rs.infraId");
        q2.setParameter("depotOpp", searchNode(getNodes(routeCode_opp).get(0)));
        ArrayList<RollingStockEntity> allAvailableRS = new ArrayList<>();
        for (Object o : q1.getResultList()) {
            RollingStockEntity rs = (RollingStockEntity) o;
            allAvailableRS.add(rs);
        }
        ArrayList<RollingStockEntity> allAvailableRS_opp = new ArrayList<>();
        for (Object o : q2.getResultList()) {
            RollingStockEntity rs = (RollingStockEntity) o;
            allAvailableRS_opp.add(rs);
        }
        //the columns that need special attention
        int firstStationCol = 2;
        int lastStationCol = table.get(0).size() - 3;
        int rollingStockCol = table.get(0).size() - 1;
        //assign rolling stocks in table and table_opp simultaneously
        for (int rs = 0; rs < allAvailableRS.size(); rs++) {
            String assignedRS = allAvailableRS.get(rs).getInfraId();
            String assignedRS_opp = allAvailableRS_opp.get(rs).getInfraId();
            //System.out.println("assigning rolling stock: " + assignedRS + " " + assignedRS_opp);
            //the flag variables that will be updated each time assigning the rolling stock
            int assignedRow = findFirstRowWithoutRS(table);
            String flag = table.get(assignedRow).get(lastStationCol);
            table.get(assignedRow).set(rollingStockCol, assignedRS);
            table_opp.get(assignedRow).set(rollingStockCol, assignedRS_opp);
            //System.out.println("first time: " + assignedRow + " " + flag);
            //update flag variables
            assignedRow = findNextNearestTime(assignedRow, flag, table);//NSL15-01
            flag = table.get(assignedRow).get(lastStationCol);
            //System.out.println("second time: " + assignedRow + " " + flag);

            while (!hasCompletedAssigningTheRS(flag, table_opp) || !hasCompletedAssigningTheRS(flag, table)) {//NSL01-15

                table_opp.get(assignedRow).set(rollingStockCol, assignedRS);
                //table_opp.get(assignedRow).set(1,"");
                table.get(assignedRow).set(rollingStockCol, assignedRS_opp);
                //table.get(assignedRow).set(1,"");
                //update flag variables
                assignedRow = findNextNearestTime(assignedRow, flag, table);//NSL15-01
                flag = table.get(assignedRow).get(lastStationCol);
                //System.out.println("in the loop: " + assignedRow + " " + flag);

                table.get(assignedRow).set(rollingStockCol, assignedRS);
                //table.get(assignedRow).set(1,"");
                table_opp.get(assignedRow).set(rollingStockCol, assignedRS_opp);
                //table_opp.get(assignedRow).set(1,"");
                //update flag variables
                assignedRow = findNextNearestTime(assignedRow, flag, table_opp);//NSL01-15
                flag = table.get(assignedRow).get(lastStationCol);
                //System.out.println("in the loop: " + assignedRow + " " + flag);
            }

//            while (!hasCompletedAssigningTheRS(flag, table_opp) || !hasCompletedAssigningTheRS(flag, table)) {//NSL01-15
//                table.get(assignedRow).remove(rollingStockCol);
//                table.get(assignedRow).add(assignedRS);
//                table_opp.get(assignedRow).remove(rollingStockCol);
//                table_opp.get(assignedRow).add(assignedRS_opp);
//                //update flag variables
//                assignedRow = findNextNearestTime(assignedRow, flag, table_opp);//NSL01-15
//                flag = table.get(assignedRow).get(lastStationCol);
//                
//                table_opp.get(assignedRow).remove(rollingStockCol);
//                table_opp.get(assignedRow).add(assignedRS);
//                table.get(assignedRow).remove(rollingStockCol);
//                table.get(assignedRow).add(assignedRS_opp);
//                //update flag variables
//                assignedRow = findNextNearestTime(assignedRow, flag, table);//NSL15-01
//                flag = table.get(assignedRow).get(lastStationCol);
//                }
        }
        return table;
    }

    public String findOppositeRoute(String routeCode) {
        Query q = em.createQuery("SELECT r FROM RouteEntity r");
        RouteEntity r = new RouteEntity();
        for (Object o : q.getResultList()) {
            r = (RouteEntity) o;
            if ((r.getCode().substring(0, 3).equals(routeCode.substring(0, 3))) && (!r.getCode().substring(3).equals(routeCode.substring(3)))) {
                break;
            }
        }
        return r.getCode();
    }

    public int findNextNearestTime(int assignedRow, String flag, ArrayList<ArrayList<String>> table_opp) {
        int rowNum = assignedRow;
        int lastColumn = table_opp.get(0).size() - 1;
        for (int i = assignedRow; i < table_opp.size(); i++) {
            rowNum = i;
            if (strConvertToTimestamp(table_opp.get(i).get(2)).after(strConvertToTimestamp(flag)) && table_opp.get(i).get(lastColumn).equals("empty")) {
                break;
            }
        }
        return rowNum;
    }

    public int findFirstRowWithoutRS(ArrayList<ArrayList<String>> table) {
        int firstRow = 0;
        int lastColumn = table.get(0).size() - 1;

        for (int i = 0; i < table.size(); i++) {
            firstRow = i;
            if (table.get(i).get(lastColumn).equals("empty")) {
                break;
            }
        }
        return firstRow;
    }

    public boolean hasCompletedAssigningTheRS(String flag, ArrayList<ArrayList<String>> table_opp) {
        int lastRow = table_opp.size() - 1;
        if (flag.substring(0, 2).equals("00")) {
            return true;
        }
        if (!strConvertToTimestamp(table_opp.get(lastRow).get(2)).after(strConvertToTimestamp(flag))) {
            return true;
        } else {
            return false;
        }
    }

    public RollingStockEntity searchRS(String infraId) {
        Query q = em.createQuery("SELECT r FROM RollingStockEntity r WHERE r.infraId=:infraId");
        q.setParameter("infraId", infraId);
        return (RollingStockEntity) q.getResultList().get(0);
    }

    @Override
    public void testStoreRegularDayTripStationScheduleWithRS_NSL0115() {
        Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss");
        if (query.getResultList().isEmpty()) {
            String routeCode = "NSL01-15";
            String dayType = "Saturday";
            System.out.println("Store trip station schedule for: " + routeCode + " on " + dayType);
            ArrayList<ArrayList<String>> table = assignRollingStock(routeCode, dayType);
            System.out.println("collating and assigning are done!");
            for (int row = 1; row < table.size(); row++) {
                System.out.println("run dao le: " + row);
                int numberOfNodes = getNodes(routeCode).size();
                //first depot
                TripStationScheduleEntity tss1 = new TripStationScheduleEntity(null, dayType, Integer.parseInt(table.get(row).get(0)), null, strConvertToTimestamp(table.get(row).get(1)), "normal");
                tss1.setRoute(searchRoute(routeCode));
                tss1.setNode(searchNode(getNodes(routeCode).get(0)));
                tss1.setSpecialDayAlgo(null);
                tss1.setTrainSchedule(null);
                tss1.setRollingStock(searchRS(table.get(row).get(table.get(0).size() - 1)));
                //last depot
                TripStationScheduleEntity tss2 = new TripStationScheduleEntity(null, dayType, Integer.parseInt(table.get(row).get(0)), strConvertToTimestamp(table.get(row).get(table.get(0).size() - 2)), null, "normal");
                tss2.setRoute(searchRoute(routeCode));
                tss2.setNode(searchNode(getNodes(routeCode).get(numberOfNodes - 1)));
                tss2.setSpecialDayAlgo(null);
                tss2.setTrainSchedule(null);
                tss2.setRollingStock(searchRS(table.get(row).get(table.get(0).size() - 1)));
                em.persist(tss1);
                em.persist(tss2);
            }
            //for stations
            String[] nodes = getFirstRowOfScheduleTable(routeCode);
            for (int k = 2; k < table.get(0).size() - 2; k = k + 2) {//columns
                for (int j = 1; j < table.size(); j++) {//rows
                    System.out.println("run dao le: " + k + " " + j);
                    TripStationScheduleEntity tss = new TripStationScheduleEntity(null, dayType, Integer.parseInt(table.get(j).get(0)), strConvertToTimestamp(table.get(j).get(k)), strConvertToTimestamp(table.get(j).get(k + 1)), "normal");
                    tss.setRoute(searchRoute(routeCode));
                    tss.setNode(searchNode(nodes[k - 1].substring(0, 5)));
                    tss.setSpecialDayAlgo(null);
                    tss.setTrainSchedule(null);
                    tss.setRollingStock(searchRS(table.get(j).get(table.get(0).size() - 1)));
                    em.persist(tss);
                }
            }
        }
    }

    @Override
    public void storeRegularDayTripStationScheduleWithRS() {
        Query query = em.createQuery("SELECT tss FROM TripStationScheduleEntity tss WHERE tss.dayType IS NOT NULL");
        if (query.getResultList().isEmpty()) {
            ArrayList<String> dayTypes = new ArrayList<String>() {
                {
                    add("Weekday");
                    add("Saturday");
                    add("Sunday");
                }
            };
            List<String> routes = getRouteList();
            for (int r = 0; r < routes.size(); r++) {
                System.out.println("r" + routes.size() + " " + r + new Date());
                String routeCode = routes.get(r);
                for (int d = 0; d < dayTypes.size(); d++) {
                    String dayType = dayTypes.get(d);
                    System.out.println("Store trip station schedule for: " + routeCode + " on " + dayType);
                    System.out.println("r" + routes.size() + " " + r + " d" + dayTypes.size() + " " + d + new Date());
                    ArrayList<ArrayList<String>> table = assignRollingStock(routeCode, dayType);
                    for (int row = 1; row < table.size(); row++) {
                        int numberOfNodes = getNodes(routeCode).size();
                        //first depot
                        TripStationScheduleEntity tss1 = new TripStationScheduleEntity(null, dayType, Integer.parseInt(table.get(row).get(0)), null, strConvertToTimestamp(table.get(row).get(1)), "normal");
                        tss1.setRoute(searchRoute(routeCode));
                        tss1.setNode(searchNode(getNodes(routeCode).get(0)));
                        tss1.setSpecialDayAlgo(null);
                        tss1.setTrainSchedule(null);
                        tss1.setRollingStock(searchRS(table.get(row).get(table.get(0).size() - 1)));
                        //last depot
                        TripStationScheduleEntity tss2 = new TripStationScheduleEntity(null, dayType, Integer.parseInt(table.get(row).get(0)), strConvertToTimestamp(table.get(row).get(table.get(0).size() - 2)), null, "normal");
                        tss2.setRoute(searchRoute(routeCode));
                        tss2.setNode(searchNode(getNodes(routeCode).get(numberOfNodes - 1)));
                        tss2.setSpecialDayAlgo(null);
                        tss2.setTrainSchedule(null);
                        tss2.setRollingStock(searchRS(table.get(row).get(table.get(0).size() - 1)));
                        em.persist(tss1);
                        em.persist(tss2);
                    }

                    //for stations
                    String[] nodes = getFirstRowOfScheduleTable(routeCode);
                    for (int k = 2; k < table.get(0).size() - 2; k = k + 2) {//columns
                        for (int j = 1; j < table.size(); j++) {//rows
                            TripStationScheduleEntity tss = new TripStationScheduleEntity(null, dayType, Integer.parseInt(table.get(j).get(0)), strConvertToTimestamp(table.get(j).get(k)), strConvertToTimestamp(table.get(j).get(k + 1)), "normal");
                            tss.setRoute(searchRoute(routeCode));
                            tss.setNode(searchNode(nodes[k - 1].substring(0, 5)));
                            tss.setSpecialDayAlgo(null);
                            tss.setTrainSchedule(null);
                            tss.setRollingStock(searchRS(table.get(j).get(table.get(0).size() - 1)));
                            em.persist(tss);
                        }
                    }
                }
            }
        }
    }
    
}
