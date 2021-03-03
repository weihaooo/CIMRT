/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.ws;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import passenger.sessionbean.CalculatorSessionBeanLocal;
import passenger.sessionbean.TrainScheduleSessionBeanLocal;
import routefare.entity.TripStationScheduleEntity;

/**
 * REST Web Service
 *
 * @author FABIAN
 */
@Path("mobile")
public class MobileResource {

    @EJB
    private CalculatorSessionBeanLocal calculator;
    @EJB
    private TrainScheduleSessionBeanLocal train;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MobileResource
     */
    public MobileResource() {
    }
    
    @GET
    @Path("/nextTwoTrain")
    @Produces(MediaType.APPLICATION_JSON)
    public String calculateNextTwoTrains(@QueryParam("station") String station, @QueryParam("terminatingAt") String terminatingAt) {
        System.err.println("alculate: " + station + "; " + terminatingAt);
        Date date = new Date();
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        call.set(Calendar.HOUR_OF_DAY, 0);
        call.set(Calendar.MINUTE, 0);
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        date = call.getTime();
        Timestamp date1 = new java.sql.Timestamp(date.getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        
        String dayOfWeek;
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayOfWeek = "Sunday";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            dayOfWeek = "Saturday";
        } else {
            dayOfWeek = "Weekday";
        }
        
        return String.valueOf(train.calculateNextTwoTrains(date1, dayOfWeek, currentTime, terminatingAt, station));
        //throw new UnsupportedOperationException();
    }

    /**
     * Retrieves representation of an instance of mobile.ws.MobileResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/tripTime")
    @Produces(MediaType.APPLICATION_JSON)
    public String calculateTripTime(@QueryParam("startPt") String startPt, @QueryParam("endPt") String endPt) {
        System.err.println("alculate: " + startPt + "; " + endPt);
        String dayOfWeek="";
        Date date = new Date();
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        call.set(Calendar.HOUR_OF_DAY, 0);
        call.set(Calendar.MINUTE, 0);
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        date = call.getTime();
        Timestamp date1 = new java.sql.Timestamp(date.getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        
        Date dateSelect = new Date();
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateSelect);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dayOfWeek = "Sunday";
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                dayOfWeek = "Saturday";
            } else {
                dayOfWeek = "Weekday";
            }
        
        return String.valueOf(Math.round(calculator.calculateTripTime(date1, dayOfWeek, currentTime, startPt, endPt)));
        //throw new UnsupportedOperationException();
    }
   
    /**
     * Retrieves representation of an instance of mobile.ws.MobileResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/calculator")
    @Produces(MediaType.APPLICATION_JSON)
    public String calculateFare(@QueryParam("startPt") String startPt, @QueryParam("endPt") String endPt, 
            @QueryParam("passengerType") String passengerType) {
        
        String dayOfWeek="";
        System.err.println("alculate: " + startPt + "; " + endPt + " " + passengerType + " ");
        Date date = new Date();
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        call.set(Calendar.HOUR_OF_DAY, 0);
        call.set(Calendar.MINUTE, 0);
        call.set(Calendar.SECOND, 0);
        call.set(Calendar.MILLISECOND, 0);
        date = call.getTime();      
        Timestamp date1 = new java.sql.Timestamp(date.getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        DecimalFormat df2 = new DecimalFormat(".##");
        
        Date dateSelect = new Date();
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateSelect);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dayOfWeek = "Sunday";
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                dayOfWeek = "Saturday";
            } else {
                dayOfWeek = "Weekday";
            }
        
        return String.valueOf(df2.format(calculator.calculateTripFare(date1, dayOfWeek, currentTime, passengerType, startPt, endPt)));
        //throw new UnsupportedOperationException();
    }
       

    /**
     * PUT method for updating or creating an instance of MobileResource
     *
     * @param content representation for the resource
     */
//    @PUT
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.APPLICATION_XML)
//    public double calculateDistance(@FormParam("startPt") String startPt, @FormParam("endPt") String endPt) {
//        return calculatorSessionBeanLocal.calculateDistance(startPt, endPt);
//        
//    }
//
//    private CalculatorSessionBeanLocal lookupCalculatorSessionBeanLocal() {
//        try {
//            javax.naming.Context c = new InitialContext();
//            return (CalculatorSessionBeanLocal) c.lookup("java:global/CIMRT-ejb/CalculatorSessionBean!passenger.sessionbean.CalculatorSessionBeanLocal");
//        } catch (NamingException ne) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
//            throw new RuntimeException(ne);
//        }
//    }
}
