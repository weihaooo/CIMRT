$(document).ready(function () {
    generateBreadcrumb()
    bindEventToNavigation();
});

var paths = {
    "Leasing space": "/BusinessPartnerSystem/viewAvailableLeasingSpace.xhtml"
}

function bindEventToNavigation() {

    $.each($(".active-menuitem > a > span:not(.menuitem-badge)"), function (index, element) {
        $(element).click(function (event) {
            generateBreadcrumb()
        });
    });
}

function generateBreadcrumb() {
    var spanList = $(".active-menuitem > a > span:not(.menuitem-badge)")
            var filteredList = []
    for (var i = 0; i < spanList.length; i++) {
        if ($(spanList[i]).text() !== "") {
            filteredList.push(spanList[i])
        }
    }
    for (var i = 0; i < filteredList.length; i++) {
        console.log($(filteredList[i]).text())
    }

    $("#breadcrumbList").html(generateBreadcrumbHTML(filteredList))
}

function generateBreadcrumbHTML(filteredList) {
    var html = '<div id="j_idt112" class="ui-breadcrumb ui-module ui-widget ui-widget-header ui-helper-clearfix ui-corner-all" role="menu"><ul>'

    // add home
    html += '<li role="menuitem"><a tabindex="-1" class="ui-menuitem-link ui-corner-all ui-icon ui-icon-home" href="/BusinessPartnerSystem/home.xhtml"><span class="ui-menuitem-text">Categories</span></a></li>'
    html += '<li class="ui-breadcrumb-chevron ui-icon ui-icon-triangle-1-e"></li>'

    for (var i = 0; i < filteredList.length; i++) {
        var path = "#"
        if (paths[$(filteredList[i]).text()] !== undefined) {
            path = paths[$(filteredList[i]).text()]
        }

        html += '<li role="menuitem"><a tabindex="-1" class="ui-menuitem-link ui-corner-all" href="' + path + '"><span class="ui-menuitem-text">'
        html += $(filteredList[i]).text() + '</span></a></li>'

        if (i !== filteredList.length - 1) {
            html += '<li class="ui-breadcrumb-chevron ui-icon ui-icon-triangle-1-e"></li>'
        }
    }

    html += "</ul></div>"
    return html
}

//remove a,  href="' + path + '">,         var path = "#"