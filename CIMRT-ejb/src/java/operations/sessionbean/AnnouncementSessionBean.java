/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import commoninfra.entity.StaffEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import operations.entity.AnnouncementEntity;

/**
 *
 * @author zhuming
 */
@Stateless
public class AnnouncementSessionBean implements AnnouncementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    public AnnouncementSessionBean() {
    }

    @Override
    public void createAnnouncement(String title, String content, String type, Timestamp dateAndTime, String recepientType, String staffId) {

        AnnouncementEntity a = new AnnouncementEntity(title, content, type, dateAndTime, recepientType);
        a.setStaff(searchStaff(staffId));
        em.persist(a);
    }

    @Override
    public void editAnnouncement(Long announcementId, String newTitle, String newContent, String newType, Timestamp newDateAndTime, String newRecipient) {
        
        AnnouncementEntity myA = searchAnnouncement(announcementId);
        
        myA.setTitle(newTitle);
        myA.setDescription(newContent);
        myA.setAnnouncementType(newType);
        myA.setDateAndTime(newDateAndTime);
        myA.setRecepientType(newRecipient);
        em.flush();
    }

    @Override
    public void deleteAnnouncement(Long announcementId) {

        AnnouncementEntity myA = searchAnnouncement(announcementId);

        em.remove(myA);
    }

    @Override
    public StaffEntity searchStaff(String staffId) {
        Query q = em.createQuery("SELECT s FROM StaffEntity s WHERE s.staffId = :staffId");
        q.setParameter("staffId", staffId);
        List<StaffEntity> result = q.getResultList();
        return result.get(0);
    }

    @Override
    public ArrayList<AnnouncementEntity> getAnnouncementList() {
        ArrayList<AnnouncementEntity> announcementList = new ArrayList<AnnouncementEntity>();
        Query query = em.createQuery("SELECT a FROM AnnouncementEntity a WHERE a.recepientType='Staff Only' OR a.recepientType='Both' ORDER BY a.dateAndTime DESC");
        for (Object o : query.getResultList()) {
            announcementList.add((AnnouncementEntity) o);
        }
        return announcementList;
    }
    
    @Override
    public ArrayList<AnnouncementEntity> getPassengerAnnouncements() {
        ArrayList<AnnouncementEntity> announcements = new ArrayList<AnnouncementEntity>();
        Query query = em.createQuery("SELECT a FROM AnnouncementEntity a WHERE a.recepientType='Passenger Only' OR a.recepientType='Both' ORDER BY a.dateAndTime DESC");
        for (Object o : query.getResultList()) {
            announcements.add((AnnouncementEntity) o);
        }
        return announcements;
    }

    @Override
    public List<AnnouncementEntity> getIndividualAnnouncementList(String staffId) {
        //System.out.println("enter");
        Query q = em.createQuery("SELECT s FROM StaffEntity AS s WHERE s.staffId=:staffId");
        q.setParameter("staffId", staffId);
        List<StaffEntity> result = q.getResultList();

        Query q1 = em.createQuery("SELECT a FROM AnnouncementEntity AS a WHERE a.staff=:staff");
        q1.setParameter("staff", result.get(0));
        //System.out.println("staff "+ result.get(0).getFirstName());
        List<AnnouncementEntity> announcements = new ArrayList<AnnouncementEntity>();
        for (Object o : q1.getResultList()) {
            announcements.add((AnnouncementEntity) o);
        }
        //System.out.println("size "+announcements.size());
        return announcements;
    }

    @Override
    public AnnouncementEntity searchAnnouncement(Long announcementId) {
        AnnouncementEntity announcement = new AnnouncementEntity();
        Query q = em.createQuery("SELECT a FROM AnnouncementEntity AS a WHERE a.announcementId=:announcementId");
        q.setParameter("announcementId", announcementId);
        List<AnnouncementEntity> result = q.getResultList();
        announcement = result.get(0);
        return announcement;
    }
}
