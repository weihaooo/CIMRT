navigator.notification.alert('here');


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

    }, 'Cordova Demo App');
}



function handleClnkExit()
{
    navigator.app.exitApp();
}



function handleBtnSubmitInput()
{
	navigator.notification.alert('handleBtnSubmitInput');
	
    var selectmanymenu = "";
    
    $('#select-many-menu option:selected').each(function () {
        selectmanymenu += $(this).val() + ",";
    });
    
    var message = "textbox=" + $('#textbox').val() +
            "\r\npassword=" + $('#password').val() +
            "\r\ntextarea=" + $('#textarea').val() +
            "\r\ncheckbox-1=" + $('#checkbox-1').prop('checked') +
            "\r\ncheckbox-2=" + $('#checkbox-2').prop('checked') +
            "\r\ncheckbox-3=" + $('#checkbox-3').prop('checked') +
            "\r\nradio-choice-1=" + $('#radio-choice-1').prop('checked') +
            "\r\nradio-choice-2=" + $('#radio-choice-2').prop('checked') +
            "\r\nradio-choice-3=" + $('#radio-choice-3').prop('checked') +
            "\r\nselect-one-menu=" + $('#select-one-menu').val() +
            "\r\nselect-many-menu=" + selectmanymenu +
            "\r\ndatepicker=" + $('#datepicker').val() +
            "\r\nslider=" + $('#slider').val() +
            "\r\nflipswitch=" + $('#flipswitch').val();
    
    navigator.notification.alert(message, function () {

    }, 'Input Values');
}



$(document).ready(function ()
{
	navigator.notification.alert('start');
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
	navigator.notification.alert('end');
});