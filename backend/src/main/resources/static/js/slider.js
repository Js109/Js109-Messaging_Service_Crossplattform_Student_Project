var slider = document.getElementById("radiusRange");
var output = document.getElementById("radius-value");
output.innerHTML = slider.value + "km"; // Display the default slider value

// Update the current slider value (each time you drag the slider handle)
slider.oninput = function() {
    output.innerHTML = this.value + "km";
}

// Disable slider until both coordinates have been entered
function onLatLngChange() {
    const lat = document.forms["message"]["lat"];
    const lng = document.forms["message"]["lng"];

    if(!(lat.value === "" && lng.value === "")){
        slider.disabled = false;
    } else {
        slider.disabled = true
        slider.value = 0;
        output.innerHTML = 0 + "km"
    }
}

// initial call to activate or deactivate slider on page load
onLatLngChange()