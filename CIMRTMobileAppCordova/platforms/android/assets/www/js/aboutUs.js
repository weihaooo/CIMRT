function handleClnkAbout()
{
    navigator.notification.beep(1);

    var message = "Cordova Demo App:" +
            "\r\n\r\ndevice.cordova=" + device.cordova +
            "\r\ndevice.model=" + device.model +
            "\r\ndevice.platform=" + device.platform +
            "\r\ndevice.uuid=" + device.uuid +
            "\r\ndevice.version=" + device.version +
            "\r\ndevice.manufacturer=" + device.manufacturer +
            "\r\ndevice.isVirtual=" + device.isVirtual +
            "\r\ndevice.serial=" + device.serial;

    navigator.notification.alert(message, function () {

    }, 'IS4103-ES01 App');
}



function handleClnkExit()
{
    navigator.app.exitApp();
}





$(document).ready(function ()
{
    $(document).on('pagebeforeshow', '#output', retrieveAllVenues);
    $(document).on('pagebeforeshow', '#events', retrieveEventsByVenueId);
    
    $('#index #clnkAbout').click(handleClnkAbout);
    $('#index #clnkExit').click(handleClnkExit);
    $('#index #clnkPanelExit').click(handleClnkExit);    

    $('#input #clnkAbout').click(handleClnkAbout);
    $('#input #clnkExit').click(handleClnkExit);
    $("#input #btnSubmit").click(handleBtnSubmitInput);
    $('#input #clnkPanelExit').click(handleClnkExit);     
    
    $('#output #clnkAbout').click(handleClnkAbout);
    $('#output #clnkExit').click(handleClnkExit);
    $('#output #clnkPanelExit').click(handleClnkExit);
    
    $('#events #clnkAbout').click(handleClnkAbout);
    $('#events #clnkExit').click(handleClnkExit);
    $('#events #clnkPanelExit').click(handleClnkExit);
});