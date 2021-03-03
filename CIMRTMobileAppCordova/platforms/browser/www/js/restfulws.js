var storeObject = {
    venueId: null
}



var host = "192.168.1.95:8080";            
var urlPrefix = "/EventBookingSystem-war/EventBookingSystemRestfulWebServices/EventBookingResources/";
var url = "http://" + host + urlPrefix;



function loadEventsByVenueId(object)
{
    $('#eventstable tbody').html("");

    for(i = 0; i < object.events.event.length; i++)
    {   
        var startDateTime = String(object.events.event[i].startDateTime);
        var startDateTimeArray1 = startDateTime.split("T");
        var startDateTimeArray2 = startDateTimeArray1[1].split("+");
        var startDateTimeString = startDateTimeArray1[0] + " " + startDateTimeArray2[0].substring(0,5);

        var endDateTime = String(object.events.event[i].endDateTime);
        var endDateTimeArray1 = endDateTime.split("T");
        var endDateTimeArray2 = endDateTimeArray1[1].split("+");
        var endDateTimeString = endDateTimeArray1[0] + " " + endDateTimeArray2[0].substring(0,5);

        $('#eventstable tbody').append("<tr><td>" + object.events.event[i].eventId + "</td><td>" + object.events.event[i].eventName + "</td><td>" + startDateTimeString + "</td><td>" + endDateTimeString + "</td><td>" + object.events.event[i].venue.venueName + "</td><td>" + object.events.event[i].status + "</td></tr>");
    }          

    $('#eventstable').table("refresh");
}



function retrieveEventsByVenueId(venueId)
{       
    var request = "venueId=" + storeObject.venueId;

    $.ajax({ 
        type: "GET",
        url: url + "retrieveEventsByVenueId",
        dataType: "text/plain",
        contentType: "application/json",
        data: request, 
        complete: function(result){
            var object = jQuery.parseJSON(result.responseText);            
            
            loadEventsByVenueId(object);
        } 
    });    
}



function loadAllVenues(object)
{
    var output = "";
          
    for(i = 0; i < object.venues.venue.length; i++)
    {
        output += "<li><a href=\"#events\" onclick=\"storeObject.venueId='" + object.venues.venue[i].venueId + "';\">" + object.venues.venue[i].venueName + "</a></li>"
    }
    
    $('#venues').html(output).listview("refresh");
}



function retrieveAllVenues()
{       
    var request = "";
    
    $.ajax({ 
        type: "GET",
        url: url + "retrieveAllVenues",
        dataType: "text/plain",
        contentType: "application/json",
        data: request, 
        complete: function(result){
            var object = jQuery.parseJSON(result.responseText);            
            loadAllVenues(object);
        } 
    });    
}