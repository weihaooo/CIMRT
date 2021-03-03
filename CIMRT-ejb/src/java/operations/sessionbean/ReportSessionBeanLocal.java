/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.sessionbean;

import commoninfra.entity.TeamEntity;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import operations.entity.IncidentReportEntity;
import operations.entity.InspectionReportEntity;
import operations.entity.MaintenanceRequestEntity;
import operations.entity.TripReportEntity;
/**
 *
 * @author Zhirong
 */

@Local
public interface ReportSessionBeanLocal {

    public boolean addInspectionReport(String reportType, Date todayDate, String rollingStock,String code, String remarks, String submitter, String driverCabinLight, String throttle,
            String brake, String reverser, String switchPanel, String horn, String microphone, String speaker, String ledDisplay, String wipers, String washers, String driverChair, String passCabinLight, String cabinBenches, String handGrips, String handRails,
            String doorSignals, String emergencyButtons, String emergencyExitDoors, String loudspeakers, String animatedTrainMaps, String windows, String trainDoors, String aircons);

    public boolean updateInspectionReport(String inspectReportId, String staffId, String signature);

    public ArrayList<InspectionReportEntity> getInspectionReports(String staffId);
    
    public ArrayList<InspectionReportEntity> getInspectionReports1(String nodeCode);

    public InspectionReportEntity searchInspectReport(String inspectReportId);

    public boolean addIncidentRep(Date date, String location, String subject, String content, String submitter);

    public boolean updateIncidentRep(String incidentRepId, Date date, String subject, String content, String location, String signature, String submitter, String staffId);

    public ArrayList<IncidentReportEntity> getIncidentReports(String staffId);
    
    public ArrayList<IncidentReportEntity> getIncidentReports1(String role,String nodeCode);

    public IncidentReportEntity searchIncidentRep(String incidentRepId);

    public TeamEntity searchTeam(String team);

    public List<TripReportEntity> getTripReports1(String nodeCode);

    public List<TripReportEntity> getTripReports(String staffId);

    public TripReportEntity searchTripReport(String tripReportId);

    public boolean addTripReport(String reportType, Date date, String rollingStock, String code, String remarks, String staffId, String cabinLight, String throttle, String brake, String reverser, String switchPanel, String horn,
               String microphone, String speaker,String ledDisplay,String wipers, String washers);
    
    public MaintenanceRequestEntity submitMaintenanceRequest(Date date, String faulty,String rollingStock,String staffId,String code);

}
