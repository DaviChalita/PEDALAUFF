function trazMapa(obj) {

    for (var j = 1; j < obj.options.length; j++) {
        var btnValue = obj.options[j].value;
        document.getElementById(btnValue).hidden = true;
    }

    var btnValue = obj.options[obj.selectedIndex].value;
    document.getElementById(btnValue).hidden = false;

    return;

    // Do your thing here

    // reset 
    obj.selectedIndex = 0;
}