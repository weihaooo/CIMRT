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
import javax.ejb.Local;
import operations.entity.AnnouncementEntity;

/**
 *
 * @author zhuming
 */
@Local
public interface AnnouncementSessionBeanLocal {
    public void createAnnouncement(String title, String content, String type, Timestamp dateAndTime, String recepientType, String staffId);
    public void editAnnouncement(Long announcementId, String newTitle, String newContent, String newType, Timestamp newDateAndTime,String newRecipient);
    public void deleteAnnouncement(Long announcementId);
    public StaffEntity searchStaff(String staffId);
    public ArrayList<AnnouncementEntity> getAnnouncementList();
    public List<AnnouncementEntity> getIndividualAnnouncementList(String staffId);
    public AnnouncementEntity searchAnnouncement(Long announcementId);
    public ArrayList<AnnouncementEntity> getPassengerAnnouncements();
}
