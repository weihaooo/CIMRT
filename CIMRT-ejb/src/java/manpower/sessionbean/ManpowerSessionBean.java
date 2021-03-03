/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpower.sessionbean;

import commoninfra.sessionbean.EmailManager;
import commoninfra.entity.DepotStaffEntity;
import commoninfra.entity.HqStaffEntity;
import commoninfra.entity.StaffEntity;
import commoninfra.entity.StationStaffEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import manpower.entity.WorkshopEntity;

/**
 *
 * @author FABIAN
 */
@Stateless
public class ManpowerSessionBean implements ManpowerSessionBeanLocal, ManpowerSessionBeanRemote {

    @PersistenceContext()
    EntityManager em;

    public ManpowerSessionBean() {
    }

    private WorkshopEntity workshopEntity;

    @Override
    public boolean createWorkshop(String workshopName, String workshopType, String description, Date startDate, Date endDate, String workshopStartTime, String workshopEndTime, String workshopAddress) {

        if (workshopName == null || workshopType == null || description == null || startDate == null || endDate == null || workshopStartTime == null || workshopEndTime == null || workshopAddress == null) {
            return false;
        }
        workshopEntity = new WorkshopEntity(description, workshopAddress, startDate, endDate, workshopName, workshopStartTime, workshopEndTime, workshopType);
        em.persist(workshopEntity);
        em.flush();
        return true;
    }

    @Override
    public ArrayList<ArrayList<String>> getExpireSafetyList() {
        String workshopType = "Safety";
        ArrayList<ArrayList<String>> allStaffValidities = new ArrayList<ArrayList<String>>();
        List<StaffEntity> allStaff = new ArrayList<StaffEntity>();
        List<WorkshopEntity> workshops;
        Date recentDate = new Date(116, 1, 1);
        Date recentDate2 = new Date(116, 1, 1);
        Date today = new Date();

        Query q = em.createQuery("SELECT s FROM StaffEntity AS s");
        allStaff = q.getResultList();

        for (int i = 0; i < allStaff.size(); i++) {
            workshops = new ArrayList<WorkshopEntity>();
            workshops = allStaff.get(i).getWorkshops();
            if (!workshops.isEmpty()) {
                for (int k = 0; k < workshops.size(); k++) {
                    if (workshops.get(k).getWorkshopType().equals("Safety")) {
                        if (workshops.get(k).getStartDate().after(recentDate)) {
                            recentDate = workshops.get(k).getStartDate();

                        }
                    }
                }

                ArrayList<String> allStaffValidity = new ArrayList<String>();
                if (!recentDate.equals(recentDate2)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(recentDate);
                    c.add(Calendar.DATE, 365); //number of days to add
                    recentDate = c.getTime();
                    int daysToExpiration = (int) ((recentDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
                    allStaffValidity.add(allStaff.get(i).getStaffId());
                    allStaffValidity.add(allStaff.get(i).getFirstName());
                    allStaffValidity.add(Integer.toString(daysToExpiration));

                    allStaffValidities.add(allStaffValidity);
                    recentDate = recentDate2;
                }
            }
        }
        return allStaffValidities;
    }

    @Override
    public ArrayList<ArrayList<String>> getOwnTeamExpireSafetyList(String staffId) {
        String workshopType = "Safety";
        ArrayList<ArrayList<String>> allStaffValidities = new ArrayList<ArrayList<String>>();
        Collection<StationStaffEntity> stationStaff = new ArrayList<StationStaffEntity>();
        Collection<DepotStaffEntity> depotStaff = new ArrayList<DepotStaffEntity>();
        List<StaffEntity> allStaff = new ArrayList<StaffEntity>();
        List<WorkshopEntity> workshops;
        Date recentDate = new Date(116, 1, 1);
        Date recentDate2 = new Date(116, 1, 1);
        Date today = new Date();

        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        allStaff = q.getResultList();
        if (allStaff.isEmpty()) {
            return null;
        }

        //1) - see if manager belongs to station or depot team
        //2) - get all staff that is under his/her team 
        if (allStaff.get(0) instanceof StationStaffEntity) {
            StationStaffEntity s = (StationStaffEntity) allStaff.get(0);
            stationStaff = s.getStationTeam().getStaff();
            allStaff = (List) stationStaff;
        } else {
            DepotStaffEntity d = (DepotStaffEntity) allStaff.get(0);
            depotStaff = d.getDepotTeam().getStaff();
            allStaff = (List) depotStaff;
        }

        for (int i = 0; i < allStaff.size(); i++) {
            workshops = new ArrayList<WorkshopEntity>();
            workshops = allStaff.get(i).getWorkshops();
            if (!workshops.isEmpty()) {
                for (int k = 0; k < workshops.size(); k++) {
                    if (workshops.get(k).getWorkshopType().equals("Safety")) {
                        if (workshops.get(k).getStartDate().after(recentDate)) {
                            recentDate = workshops.get(k).getStartDate();

                        }
                    }
                }

                ArrayList<String> allStaffValidity = new ArrayList<String>();
                if (!recentDate.equals(recentDate2)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(recentDate);
                    c.add(Calendar.DATE, 365); //number of days to add
                    recentDate = c.getTime();
                    int daysToExpiration = (int) ((recentDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
                    allStaffValidity.add(allStaff.get(i).getStaffId());
                    allStaffValidity.add(allStaff.get(i).getFirstName());
                    allStaffValidity.add(Integer.toString(daysToExpiration));

                    allStaffValidities.add(allStaffValidity);
                    recentDate = recentDate2;
                }
            }
        }
        return allStaffValidities;
    }

    @Override
    public String signupWorkshop(Long workshopId, String workshopName, List<String> selectedStaffs, String staffId) {
        try {
            EmailManager emailManager = new EmailManager();
            List<StaffEntity> staffList = new ArrayList<StaffEntity>();
            List<StaffEntity> participants = new ArrayList<StaffEntity>();
            List<StaffEntity> removedParticipants = new ArrayList<StaffEntity>();
            List<StaffEntity> newList = new ArrayList<StaffEntity>();
            WorkshopEntity w = searchWorkshop(workshopId);

            Query q = em.createQuery("SELECT w FROM WorkshopEntity AS w WHERE w.workshopId=:workshopId");
            q.setParameter("workshopId", workshopId);
            List<WorkshopEntity> result = q.getResultList();

            StaffEntity manager = searchStaff(staffId);

            boolean success = true;
            List<StaffEntity> previousList = result.get(0).getParticipants();
            List<StaffEntity> previousListSameTeam = new ArrayList<StaffEntity>();

            for (int i = 0; i < previousList.size(); i++) {
                System.out.println("Check database participants: " + previousList.get(i).getStaffId());
            }

            for (int i = 0; i < previousList.size(); i++) {
                if (previousList.get(i) instanceof StationStaffEntity) {
                    StationStaffEntity s = (StationStaffEntity) previousList.get(i);
                    StationStaffEntity m = (StationStaffEntity) manager;
                    if (s.getStationTeam().equals(m.getStationTeam())) {
                        System.out.println("Testing 1");
                        previousListSameTeam.add(previousList.get(i));
                    }
                }
            }

            for (int i = 0; i < previousListSameTeam.size(); i++) {
                System.out.println("Those in the same team as manager are: " + previousListSameTeam.get(i).getStaffId());
            }

            for (int i = 0; i < previousList.size(); i++) {
                if (previousList.get(i) instanceof DepotStaffEntity) {
                    DepotStaffEntity p = (DepotStaffEntity) previousList.get(i);
                    DepotStaffEntity m = (DepotStaffEntity) manager;
                    if (p.getDepotTeam().equals(m.getDepotTeam())) {
                        previousListSameTeam.add(previousList.get(i));
                    }
                }
            }

            //this method is for post updating (newly added staff will receive email to notify them to go for the workshop
            for (int j = 0; j < selectedStaffs.size(); j++) {
                String allDetails = selectedStaffs.get(j);
                String[] s = allDetails.split(" ");
                String staffId2 = s[0];
                StaffEntity staffs = searchStaff(staffId2);
                staffList.add(staffs);
            }

            for (int m = 0; m < staffList.size(); m++) {
                if (!previousListSameTeam.contains(staffList.get(m))) {
                    success = emailManager.sendWorkshopEmail(staffList.get(m).getFirstName() + " " + staffList.get(m).getLastName(), workshopId, w.getWorkshopName(), w.getDescription(),
                            w.getStartDate(), w.getEndDate(), w.getWorkshopStartTime(), w.getWorkshopEndTime(),
                            w.getWorkshopAddress(), 2, "e0002252@u.nus.edu", staffList.get(m).getEmailAddress());
                }
            }

            for (int k = 0; k < previousListSameTeam.size(); k++) {
                if (!staffList.contains(previousListSameTeam.get(k))) {
                    success = emailManager.sendWithdrawEmail(previousListSameTeam.get(k).getFirstName() + " " + previousListSameTeam.get(k).getLastName(), workshopId, w.getWorkshopName(),
                            3, "e0002252@u.nus.edu", previousListSameTeam.get(k).getEmailAddress());
                }

            }
            /*w.setParticipants(participants);*/

            //this method is to add in new participants into database
            if (!staffList.isEmpty()) {
                if (!w.getParticipants().isEmpty()) {//if the participants is not empty
                    for (int i = 0; i < staffList.size(); i++) {
                        if (!previousListSameTeam.contains(staffList.get(i))) { //repetative registration not allowed
                            w.getParticipants().add(staffList.get(i));
                        }
                    }

                } else {
                    for (int i = 0; i < staffList.size(); i++) {
                        w.getParticipants().add(staffList.get(i));
                    }
                }

            } else {
                w.setParticipants(participants);
            }

            //this method is to remove participants
            if (!staffList.isEmpty()) {
                if (!w.getParticipants().isEmpty()) {//if the participants is not empty
                    for (int i = 0; i < previousListSameTeam.size(); i++) {
                        String details = previousListSameTeam.get(i).getStaffId();
                        String[] temp = details.split(" ");
                        String staffId5 = temp[0];
                        StaffEntity staff = searchStaff(staffId5);
                        if (!staffList.contains(staff)) { //repetative registration not allowed
                            removedParticipants.add(staff);
                        }
                    }

                    if (!removedParticipants.isEmpty()) {
                        Query q2 = em.createQuery("SELECT w FROM WorkshopEntity AS w WHERE w.workshopId=:workshopId");
                        q2.setParameter("workshopId", workshopId);
                        result = q2.getResultList();
                        previousList = result.get(0).getParticipants();
                        w = searchWorkshop(workshopId);
                        w.setParticipants(participants);

                        for (int i = 0; i < previousList.size(); i++) {
                            if (!removedParticipants.contains(previousList.get(i))) {
                                newList.add(previousList.get(i));
                            }
                        }

                        for (int i = 0; i < newList.size(); i++) {
                            w.getParticipants().add(newList.get(i));
                        }

                        return "Updated";
                    }
                } else {
                    for (int i = 0; i < staffList.size(); i++) {
                        w.getParticipants().add(staffList.get(i));
                    }
                    return "Updated";
                }

            } else {
                w.setParticipants(participants);
                return "Cleared";
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return "Updated";
    }

    @Override
    public String signupWorkshopHR(Long workshopId, String workshopName, List<String> selectedHRStaffs, String staffId) {
        try {
            EmailManager emailManager = new EmailManager();
            List<StaffEntity> staffList = new ArrayList<StaffEntity>();
            List<StaffEntity> participants = new ArrayList<StaffEntity>();
            List<StaffEntity> removedParticipants = new ArrayList<StaffEntity>();
            List<StaffEntity> newList = new ArrayList<StaffEntity>();
            WorkshopEntity w = searchWorkshop(workshopId);
            Query q = em.createQuery("SELECT w FROM WorkshopEntity AS w WHERE w.workshopId=:workshopId");
            q.setParameter("workshopId", workshopId);
            List<WorkshopEntity> result = q.getResultList();
            if (result.isEmpty()) {
                return null;
            }

            boolean success = true;
            List<StaffEntity> previousList = result.get(0).getParticipants();
            List<StaffEntity> sameHRType = new ArrayList<StaffEntity>();

            for (int i = 0; i < previousList.size(); i++) {
                if (previousList.get(i) instanceof HqStaffEntity) {
                    sameHRType.add(previousList.get(i));
                }
            }

            //this method is for post updating (newly added staff will receive email to notify them to go for the workshop
            for (int j = 0; j < selectedHRStaffs.size(); j++) {
                String allDetails = selectedHRStaffs.get(j);
                String[] s = allDetails.split(" ");
                String staffId4 = s[0];
                StaffEntity staffs = searchStaff(staffId4);
                staffList.add(staffs);
            }

            for (int m = 0; m < staffList.size(); m++) {
                if (!sameHRType.contains(staffList.get(m))) {
                    success = emailManager.sendWorkshopEmail(staffList.get(m).getFirstName() + " " + staffList.get(m).getLastName(), workshopId, w.getWorkshopName(), w.getDescription(),
                            w.getStartDate(), w.getEndDate(), w.getWorkshopStartTime(), w.getWorkshopEndTime(),
                            w.getWorkshopAddress(), 2, "e0002252@u.nus.edu", staffList.get(m).getEmailAddress());
                }
            }

            for (int k = 0; k < sameHRType.size(); k++) {
                if (!staffList.contains(sameHRType.get(k))) {
                    success = emailManager.sendWithdrawEmail(sameHRType.get(k).getFirstName() + " " + sameHRType.get(k).getLastName(), workshopId, w.getWorkshopName(),
                            3, "e0002252@u.nus.edu", sameHRType.get(k).getEmailAddress());
                }

            }
            /*w.setParticipants(participants);*/

            //this method is to add in new participants into database
            if (!staffList.isEmpty()) {
                if (!w.getParticipants().isEmpty()) {//if the participants is not empty
                    for (int i = 0; i < staffList.size(); i++) {
                        if (!sameHRType.contains(staffList.get(i))) { //repetative registration not allowed
                            w.getParticipants().add(staffList.get(i));
                        }
                    }

                } else {
                    for (int i = 0; i < staffList.size(); i++) {
                        w.getParticipants().add(staffList.get(i));
                    }
                }

            } else {
                w.setParticipants(participants);
            }

            //this method is to remove participants
            if (!staffList.isEmpty()) {
                if (!w.getParticipants().isEmpty()) {//if the participants is not empty
                    for (int i = 0; i < sameHRType.size(); i++) {
                        String details = sameHRType.get(i).getStaffId();
                        String[] temp = details.split(" ");
                        String staffId5 = temp[0];
                        StaffEntity staff = searchStaff(staffId5);
                        if (!staffList.contains(staff)) { //repetative registration not allowed
                            removedParticipants.add(staff);
                        }
                    }

                    if (!removedParticipants.isEmpty()) {
                        Query q2 = em.createQuery("SELECT w FROM WorkshopEntity AS w WHERE w.workshopId=:workshopId");
                        q2.setParameter("workshopId", workshopId);
                        result = q2.getResultList();
                        previousList = result.get(0).getParticipants();
                        w = searchWorkshop(workshopId);
                        w.setParticipants(participants);

                        for (int i = 0; i < previousList.size(); i++) {
                            if (!removedParticipants.contains(previousList.get(i))) {
                                newList.add(previousList.get(i));
                            }
                        }

                        for (int i = 0; i < newList.size(); i++) {
                            w.getParticipants().add(newList.get(i));
                        }

                        return "Updated";
                    }
                } else {
                    for (int i = 0; i < staffList.size(); i++) {
                        w.getParticipants().add(staffList.get(i));
                    }
                    return "Updated";
                }

            } else {
                w.setParticipants(participants);
                return "Cleared";
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return "Updated";
    }

    private StaffEntity searchStaff(String staffId) {

        Query q = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        List<StaffEntity> result = q.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<StaffEntity> getStaffUnderSameTeam(String staffId) {
        Query q = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        if (q.getResultList().isEmpty()) {
            return null;
        }
        return q.getResultList();
    }

    @Override
    public List<HqStaffEntity> getAllHRStaff() {
        Query q = em.createQuery("SELECT h FROM HqStaffEntity h");
        if (q.getResultList().isEmpty()) {
            return null;
        }
        return q.getResultList();
    }

    @Override
    public List<WorkshopEntity> getAllWorkshops() {
        Query q = em.createQuery("SELECT w FROM WorkshopEntity AS w");
        if (q.getResultList().isEmpty()) {
            return null;
        }
        return q.getResultList();
    }

    @Override
    public List<WorkshopEntity> getSpecificWorkshop(Long workshopId, String workshopName) {
        Query q = em.createQuery("SELECT w FROM WorkshopEntity AS w WHERE w.workshopId=:workshopId");
        q.setParameter("workshopId", workshopId);
        if (q.getResultList().isEmpty()) {
            return null;
        }
        return q.getResultList();
    }

    @Override
    public boolean updateWorkshop(Long workshopId, String workshopName, String workshopType, String description, String workshopAddress, Date startDate, Date endDate, String workshopStartTime, String workshopEndTime) {
        WorkshopEntity workshop = searchWorkshop(workshopId);
        if (em.contains(workshop)) {
            workshop.setWorkshopName(workshopName);
            workshop.setWorkshopType(workshopType);
            workshop.setDescription(description);
            workshop.setWorkshopAddress(workshopAddress);
            workshop.setStartDate(startDate);
            workshop.setEndDate(endDate);
            workshop.setWorkshopStartTime(workshopStartTime);
            workshop.setWorkshopEndTime(workshopEndTime);
            em.flush();
            return true;

        } else {
            return false;
        }
    }

    @Override
    public WorkshopEntity searchWorkshop(Long workshopId) {
        WorkshopEntity workshop = new WorkshopEntity();
        try {
            Query q = em.createQuery("SELECT w FROM WorkshopEntity AS w WHERE w.workshopId=:workshopId");
            q.setParameter("workshopId", workshopId);
            workshop = (WorkshopEntity) q.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon Unique Result error:" + nure.getMessage());
        } catch (NoResultException nre) {
            System.out.println("\nNo Result error:" + nre.getMessage());
        }
        return workshop;
    }

    @Override
    public boolean delWorkshop(Long workshopId) {
        boolean success = true;
        EmailManager emailManager = new EmailManager();
        List<StaffEntity> staff = new ArrayList<StaffEntity>();
        List<StaffEntity> participants = new ArrayList<StaffEntity>();
        WorkshopEntity workshop = new WorkshopEntity();
        Query q = em.createQuery("SELECT w FROM WorkshopEntity AS w WHERE w.workshopId=:workshopId");
        q.setParameter("workshopId", workshopId);
        if(q.getResultList().isEmpty()){
            return false;
        }
        workshop = (WorkshopEntity) q.getSingleResult();

        staff = workshop.getParticipants();

        for (int i = 0; i < staff.size(); i++) {
            success = emailManager.sendWorkshopCancel(staff.get(i).getFirstName() + " " + staff.get(i).getLastName(), workshopId, workshop.getWorkshopName(),
                    7, "e0002252@u.nus.edu", staff.get(i).getEmailAddress());
        }

        workshop.setParticipants(participants);

        if (em.contains(workshop)) {
            em.remove(workshop);
            em.flush();
            return true;
        } else {
            return false;
        }
    }//end of delWorkshop method

    @Override
    public List<WorkshopEntity> retrieveWorkshops(String staffId) {

        List<StaffEntity> staffList = new ArrayList<StaffEntity>();
        List<WorkshopEntity> workshop = new ArrayList<WorkshopEntity>();
        List<WorkshopEntity> myWorkshops = new ArrayList<WorkshopEntity>();

        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        staffList = q.getResultList();
        if(staffList.get(0)== null){
            return null;
        }

        StaffEntity staff = staffList.get(0);

        Query q1 = em.createQuery("SELECT w FROM WorkshopEntity AS w");
        workshop = q1.getResultList();
        if(workshop.get(0) == null){
            return null;
        }

        for (int i = 0; i < workshop.size(); i++) {
            if (workshop.get(i).getParticipants().contains(staff)) {
                myWorkshops.add(workshop.get(i));
            }
        }
        return myWorkshops;

    }

    @Override
    public List<StaffEntity> getMembersWorkshopsEnrolled(String staffId) {
        List<StaffEntity> allStaff = new ArrayList<StaffEntity>();
        List<StaffEntity> membersSameTeam = new ArrayList<StaffEntity>();
        StaffEntity s = searchStaff(staffId);
        Query q = em.createQuery("SELECT s FROM StaffEntity AS s");
        allStaff = q.getResultList();
        if(allStaff.isEmpty()){
            return null;
        }

        for (int i = 0; i < allStaff.size(); i++) {
            if (allStaff.get(i) instanceof StationStaffEntity && s instanceof StationStaffEntity) {
                StationStaffEntity stationStaff = (StationStaffEntity) allStaff.get(i);
                StationStaffEntity manager = (StationStaffEntity) s;
                if (stationStaff.getStationTeam().equals(manager.getStationTeam()) && !stationStaff.equals(manager)) {
                    membersSameTeam.add(stationStaff);
                }
            }
            if (allStaff.get(i) instanceof DepotStaffEntity && s instanceof DepotStaffEntity) {
                DepotStaffEntity depotStaff = (DepotStaffEntity) allStaff.get(i);
                DepotStaffEntity manager = (DepotStaffEntity) s;
                if (depotStaff.getDepotTeam().equals(manager.getDepotTeam()) && !depotStaff.equals(manager)) {
                    membersSameTeam.add(depotStaff);
                }
            }
        }
        return membersSameTeam;

    }

    @Override
    public List<StaffEntity> getHRMembersWorkshopsEnrolled(String staffId) {
        List<StaffEntity> allHRStaff = new ArrayList<StaffEntity>();
        List<StaffEntity> hrTeam = new ArrayList<StaffEntity>();
        StaffEntity s = searchStaff(staffId);
        Query q = em.createQuery("SELECT h FROM HqStaffEntity AS h");
        allHRStaff = q.getResultList();
        if(allHRStaff.isEmpty()){
            return null;
        }

        for (int i = 0; i < allHRStaff.size(); i++) {
            if (!allHRStaff.get(i).equals(s)) {
                hrTeam.add(allHRStaff.get(i));
            }
        }
        return hrTeam;
    }

}
