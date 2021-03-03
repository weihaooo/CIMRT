var host = "137.132.194.85:8080";            
var urlPrefix = "/PassengerSystem/RESTful Web Services/MobileResource/";
var url = "http://" + host + urlPrefix;


function calculateDistance()
{       
    var request = "";
    
    $.ajax({ 
        type: "GET",
        url: url + "calculator",
        dataType: "json",
        contentType: "application/json",
        //data: request,
        complete: function(result){
            alert(result.responseText);
            //var object = jQuery.parseJSON(result.responseText);            
            //loadAllVenues(object);
        } 
    });   
}
