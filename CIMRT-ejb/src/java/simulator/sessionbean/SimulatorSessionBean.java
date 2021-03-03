/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.sessionbean;

import infraasset.entity.RollingStockEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import routefare.entity.NodeEntity;
import routefare.entity.RouteEntity;
import routefare.entity.TripStationScheduleEntity;
import simulator.entity.SimulatorTrackEntity;
import simulator.entity.SimulatorTrainEntity;

/**
 *
 * @author Yoona
 */
//@TransactionAttribute(REQUIRED)
@Stateless
public class SimulatorSessionBean implements SimulatorSessionBeanLocal {

    @PersistenceContext
    EntityManager em;
    //Train length 138m
    private List<SimulatorTrainEntity> activeTrains = new ArrayList<SimulatorTrainEntity>();
    private List<SimulatorTrainEntity> standbyTrains = new ArrayList<SimulatorTrainEntity>();
    private List<SimulatorTrainEntity> deployableTrains = new ArrayList<SimulatorTrainEntity>();
    private List<SimulatorTrackEntity> tracks = new ArrayList<SimulatorTrackEntity>();
    private boolean start = false;
    private int speed = 28;
    private int trainLength = 138;
    private double startDist;
    private long startTime;
    private long firstTrip;
    private List<TripStationScheduleEntity> trips;
    private int count;

    public void linkTrains() {
        activeTrains = new ArrayList<SimulatorTrainEntity>();
        standbyTrains = new ArrayList<SimulatorTrainEntity>();
        deployableTrains = new ArrayList<SimulatorTrainEntity>();
//Define the route
        Query q = em.createQuery("SELECT r FROM RouteEntity r WHERE r.code='NSL01-15'");
        List<RouteEntity> routes = q.getResultList();
        //System.out.println("Route Code: " + routes.get(0).getCode());
        if (!routes.isEmpty()) {
            //Retrieve the rollingstocks involved and in order
            Query q1 = em.createQuery("SELECT DISTINCT s.rollingStock FROM TripStationScheduleEntity AS s WHERE s.route=:route AND s.dayType='Weekday' AND s.arrivalTime IS NOT NULL ORDER BY s.arrivalTime");
            q1.setParameter("route", routes.get(0));
            List<RollingStockEntity> rs = q1.getResultList();

            //Retrieve Simulator Trains
            Query q2 = em.createQuery("SELECT s FROM SimulatorTrainEntity AS s ORDER BY s.trainId");
            List<SimulatorTrainEntity> trains = (List<SimulatorTrainEntity>) q2.getResultList();
            //Retrieve Tracks
            Query q3 = em.createQuery("SELECT s FROM SimulatorTrackEntity AS s ORDER BY s.distance");
            tracks = q3.getResultList();

            startDist = tracks.get(0).getDistance();
            //System.out.println("Start Distance: " + startDist);

            //Assign Rolling Stock to Simulator Trains and add into Standby Trains
            for (int i = 0; i < rs.size(); i++) {
                trains.get(i).setRs(rs.get(i));
                standbyTrains.add(trains.get(i));
            }
            Query q7 = em.createQuery("SELECT s FROM SimulatorTrainEntity AS s WHERE s.rs IS NULL ORDER BY s.trainId DESC");
            deployableTrains = (List<SimulatorTrainEntity>) q7.getResultList();

            startTime = 0;
            Query q5 = em.createQuery("SELECT n FROM NodeEntity AS n WHERE n.code='NSL00'");
            List<NodeEntity> node = q5.getResultList();
            Query q4 = em.createQuery("SELECT s FROM TripStationScheduleEntity AS s WHERE s.node=:node AND s.route=:route AND s.dayType='Weekday' AND s.departureTime IS NOT NULL ORDER BY s.departureTime");
            q4.setParameter("route", routes.get(0));
            q4.setParameter("node", node.get(0));
            trips = q4.getResultList();
            firstTrip = trips.get(0).getDepartureTime().getTime();

            //System.out.println("First trip time:" + firstTrip);
        }
    }

    @Override
    public List<TripStationScheduleEntity> retrieveTrips() {
        return trips;
    }

    @Override
    public void timer() {
        if (start == false) {
            //System.out.println("Timer event: " + new Date());
        }
        if (start == true) {
            //System.out.println("Simulating: " + new Date());
            doSimulation();
        }

    }

    @Override
    public void doSimulation() {
        startTime += 1000;

        if (startTime > trips.get(0).getDepartureTime().getTime() - firstTrip) {
            System.out.println("Next trip time: " + (trips.get(0).getDepartureTime().getTime() - firstTrip));
            Query q = em.createQuery("SELECT s FROM SimulatorTrainEntity AS s WHERE s.rs=:rs ORDER BY s.trainId");
            q.setParameter("rs", trips.get(0).getRollingStock());

            //System.out.println("Not Empty Simulator Train List for " + trips.get(0).getRollingStock().getInfraId());
            List<SimulatorTrainEntity> trains = q.getResultList();
            //System.out.println("Train rolling stock id: " + trains.get(0).getRs());
            activeTrains.add(trains.get(0));
            for (int i = 0; i < activeTrains.size(); i++) {
                System.out.println("Train number: " + activeTrains.get(i).getTrainId() + " is active Size: " + activeTrains.size());

            }
            trips.remove(trips.get(0));
            System.out.println("Next trip time: " + (trips.get(0).getDepartureTime().getTime() - firstTrip));
        }
        for (int i = 0; i < activeTrains.size(); i++) {
            System.out.println("Train: " + activeTrains.get(i).getTrainId() + " Distance: " + activeTrains.get(i).getDistanceTravelled() + " IsStop: " + activeTrains.get(i).isIsStop() + " Waiting Time: " + activeTrains.get(i).getWaitTime());

            //if train distance travelled equals 0
            if (activeTrains.get(i).getDistanceTravelled() == 0) {
                if (tracks.get(0).getStatus().equals("Yellow") || tracks.get(0).getStatus().equals("Red")) {
                    continue;
                }
                //Move the train and increase it by 60 and set train to first track and set first track to train
                //System.out.println(activeTrains.get(i).getTrainId() + " is here!");
                activeTrains.get(i).setIsStop(false);
                activeTrains.get(i).setDistanceTravelled(activeTrains.get(i).getDistanceTravelled() + speed);
                Query q3 = em.createQuery("SELECT s FROM SimulatorTrackEntity AS s WHERE s.distance<=:trainDist ORDER BY s.distance");
                q3.setParameter("trainDist", activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist));
                // q3.setParameter("trainDistEnd", activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist) - trainLength);
                //System.out.println("Train Dist: " + (activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist)));
                //System.out.println("Train Dist End: " + (activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist) - trainLength));
                List<SimulatorTrackEntity> occupyingTracks = q3.getResultList();
                if (!occupyingTracks.isEmpty()) {
                    System.out.println("Is nott Empty?!");
                    activeTrains.get(i).setOccupyingTracks(occupyingTracks);
                }
                for (int x = 0; x < occupyingTracks.size(); x++) {
                    occupyingTracks.get(x).setTrain(activeTrains.get(i));
                    occupyingTracks.get(x).setStatus("Red");
                    if (x == 0 && occupyingTracks.get(x).getPrevTrack() != null) {
                        occupyingTracks.get(x).getPrevTrack().setStatus("Yellow");
                        //System.out.println("Train " + activeTrains.get(i) + " set prev track yellow at dist=0 "
                        //       + occupyingTracks.get(x).getPrevTrack() + " is" + occupyingTracks.get(x).getPrevTrack().getStatus());

                    }
                }

                //if train status is not stop
            } else if (!activeTrains.get(i).isIsStop()) {
                //System.out.println("Train " + activeTrains.get(i) + " is moving");
                if ((activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getStatus().equals("Yellow") || activeTrains.get(i).getOccupyingTracks().get(0).isIsNode()) && ((activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist) + speed) > activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getDistance()) && activeTrains.get(i).getWaitTime() != -1) {
                    System.out.println("Train " + activeTrains.get(i) + " next track is yellow or now is node " + activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack() + " is" + activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getStatus());
                    //System.out.println("Train " + activeTrains.get(i) + " distance: " + activeTrains.get(i).getDistanceTravelled() + " - " + Math.abs(startDist) + " + " + speed + " = " + (activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist) + speed) + " is > " + activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getDistance());
                    // if (activeTrains.get(i).getOccupyingTracks().get(0).getDistance() == activeTrains.get(i).getDistanceTravelled()) {

                    // }
                    if (activeTrains.get(i).getOccupyingTracks().get(0).isIsNode() && activeTrains.get(i).getOccupyingTracks().get(0).getTrackName().equals("NSL 15 Mun Yee")) {
                        activeTrains.remove(activeTrains.get(i));
                        continue;

                    }
                    activeTrains.get(i).setDistanceTravelled(activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getDistance() + Math.abs(startDist));
                    activeTrains.get(i).setIsStop(true);
                    if (activeTrains.get(i).getOccupyingTracks().size() == 2) {
                        //System.out.println("Train " + activeTrains.get(i) + " occupied two track");
                        /*  if (!em.contains(activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack())) {
                            System.out.println("Need to merge?");
                            em.merge(activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack());
                        }
                        if (!em.contains(activeTrains.get(i).getOccupyingTracks().get(1))) {
                            System.out.println("Need to merge?1");
                            em.merge(activeTrains.get(i).getOccupyingTracks().get(1));
                        }*/
                        activeTrains.get(i).getOccupyingTracks().get(1).setTrain(null);
                        activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack().setStatus("Green");
                        System.out.println("Train " + activeTrains.get(i) + " set prev track green at moving is node "
                                + activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack() + " is" + activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack().getStatus());
                        em.merge(activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack());
                        activeTrains.get(i).getOccupyingTracks().get(1).setStatus("Yellow");
                        // System.out.println("Train " + activeTrains.get(i) + " set occupying track.get1 yellow at node more than 1 track" + activeTrains.get(i).getOccupyingTracks().get(1) + " is" + 
                        //activeTrains.get(i).getOccupyingTracks().get(1).getStatus());
                        em.merge(activeTrains.get(i).getOccupyingTracks().get(1));
                        activeTrains.get(i).getOccupyingTracks().remove(activeTrains.get(i).getOccupyingTracks().get(1));
                    }

                } else {

                    activeTrains.get(i).setWaitTime(0);
                    //add train distance by 60
                    activeTrains.get(i).setDistanceTravelled(activeTrains.get(i).getDistanceTravelled() + speed);
                    if (activeTrains.get(i).getOccupyingTracks().size() == 2) {
                        //System.out.println("Train " + activeTrains.get(i) + " occupied two track");
                        if ((activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist) - trainLength) > activeTrains.get(i).getOccupyingTracks().get(0).getDistance()) {
                            //System.out.println("Train " + activeTrains.get(i) + " distance: " + activeTrains.get(i).getDistanceTravelled() + " - " + (Math.abs(startDist) + trainLength) + " = " + (activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist) - trainLength) + " is > " + activeTrains.get(i).getOccupyingTracks().get(0).getDistance());

                            activeTrains.get(i).getOccupyingTracks().get(1).setTrain(null);

                            if (activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack() != null) {
                                activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack().setStatus("Green");
                                System.out.println("Train " + activeTrains.get(i) + " set prev track green at moving not node "
                                        + activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack() + " is" + activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack().getStatus());
                                em.merge(activeTrains.get(i).getOccupyingTracks().get(1).getPrevTrack());
                            }
                            activeTrains.get(i).getOccupyingTracks().get(1).setStatus("Yellow");
                            //System.out.println("Train " + activeTrains.get(i) + " set occupying track.get1 yellow next track not yellow "
                            //     + activeTrains.get(i).getOccupyingTracks().get(1) + " is" + activeTrains.get(i).getOccupyingTracks().get(1).getStatus());
                            em.merge(activeTrains.get(i).getOccupyingTracks().get(1));
                            activeTrains.get(i).getOccupyingTracks().remove(activeTrains.get(i).getOccupyingTracks().get(1));

                        }
                    }
                    if (activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist) > activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getDistance()) {
                        // System.out.println("Train " + activeTrains.get(i) + " distance: " + activeTrains.get(i).getDistanceTravelled() + " - " + Math.abs(startDist) + " = " + (activeTrains.get(i).getDistanceTravelled() - Math.abs(startDist)) + " is > " + activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getDistance());

                        SimulatorTrackEntity temp = activeTrains.get(i).getOccupyingTracks().get(0);
                        /*  if (!em.contains(activeTrains.get(i).getOccupyingTracks().get(0))) {
                            System.out.println("Need to merge?4");
                            em.merge(activeTrains.get(i).getOccupyingTracks().get(0));
                        }
                        if (!em.contains(temp)) {
                            System.out.println("Need to merge?5");
                            em.merge(temp);
                        }*/
                        activeTrains.get(i).getOccupyingTracks().remove(activeTrains.get(i).getOccupyingTracks().get(0));
                        activeTrains.get(i).getOccupyingTracks().add(temp.getNextTrack());
                        activeTrains.get(i).getOccupyingTracks().add(temp);
                        activeTrains.get(i).getOccupyingTracks().get(0).setStatus("Red");
                        activeTrains.get(i).getOccupyingTracks().get(0).setTrain(activeTrains.get(i));
                        em.merge(activeTrains.get(i).getOccupyingTracks().get(0));
                        em.merge(activeTrains.get(i).getOccupyingTracks().get(1));
                    }
                }
            } else { //if train status is stop
                //System.out.println("Train " + activeTrains.get(i) + " is stop");
                if (!activeTrains.get(i).getOccupyingTracks().get(0).isIsNode()) {

                    activeTrains.get(i).setIsStop(false);
                    continue;
                }
                //if train wait time is less than 30
                if (activeTrains.get(i).getWaitTime() < 30) {
                    //System.out.println("Train " + activeTrains.get(i) + " wait time < 30");
                    //add 1 sec to train wait time
                    activeTrains.get(i).setWaitTime(activeTrains.get(i).getWaitTime() + 1);
                } else {

                    if (activeTrains.get(i).getOccupyingTracks().get(0).getNextTrack().getStatus().equals("Yellow")) {
                        //System.out.println("Train " + activeTrains.get(i) + " next track is yellow");
                        continue;
                    }
                    //Set train to move and reset wait time to 0
                    activeTrains.get(i).setIsStop(false);
                    activeTrains.get(i).setWaitTime(-1);
                }
            }

            em.merge(activeTrains.get(i));

            //em.merge(activeTrains.get(i).getOccupyingTracks().get(0));
            //System.out.println("Train " + activeTrains.get(i).getTrainId() + " " + activeTrains.get(i).getOccupyingTracks().size() + " " + activeTrains.get(i).getOccupyingTracks().get(0).getTrackId());
            /*if(activeTrains.get(i).getOccupyingTracks().size()>1){
                for(int x = 0;x<activeTrains.get(i).getOccupyingTracks().size();x++){
                    System.out.print("Tracks occupied ");
                    System.out.print(activeTrains.get(i).getOccupyingTracks().get(x).getTrackId() + " ");
                }
            }*/
        }
        em.flush();
    }

    @Override
    public List<SimulatorTrainEntity> getTrain() {

        Query q = em.createQuery("SELECT s FROM SimulatorTrainEntity AS s WHERE s.rs IS NOT NULL ORDER BY s.trainId");
        List<SimulatorTrainEntity> trains = q.getResultList();

        return trains;
    }

    @Override
    public List<SimulatorTrackEntity> getTrack() {

        Query q = em.createQuery("SELECT s FROM SimulatorTrackEntity AS s ORDER BY s.distance");
        List<SimulatorTrackEntity> tracks = q.getResultList();

        return tracks;
    }

    @Override
    public long getTimeElapsed() {
        return startTime;
    }

    @Override
    public void startSimulator() {
        System.out.println("Start Simulation");
        start = true;
    }

    @Override
    public void pauseSimulator() {
        System.out.println("Pause Simulation");
        start = false;
    }

    @Override
    public void stopSimulator() {
        System.out.println("Stop simulator");
        start = false;
        Query q = em.createQuery("SELECT s FROM SimulatorTrackEntity AS s ORDER BY s.distance");
        List<SimulatorTrackEntity> tracks = q.getResultList();
        for (int i = 0; i < tracks.size(); i++) {
            tracks.get(i).setTrain(null);
            tracks.get(i).setStatus("Green");
        }

        Query q1 = em.createQuery("SELECT s FROM SimulatorTrainEntity AS s ORDER BY s.trainId");
        List<SimulatorTrainEntity> trains = q1.getResultList();
        for (int i = 0; i < trains.size(); i++) {
            trains.get(i).setOccupyingTracks(new ArrayList<SimulatorTrackEntity>());
            trains.get(i).setIsStop(true);
            trains.get(i).setDistanceTravelled(0);
            trains.get(i).setRs(null);
            trains.get(i).setWaitTime(0);
        }
        linkTrains();
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void forceDeploy() {
        //System.out.println("Train rolling stock id: " + trains.get(0).getRs());
        activeTrains.add(deployableTrains.get(count));
        count++;

    }
}
