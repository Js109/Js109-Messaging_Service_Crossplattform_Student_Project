var slider = document.getElementById("radiusRange");
var output = document.getElementById("radius-value");
output.innerHTML = slider.value + "km"; // Display the default slider value

// Update the current slider value (each time you drag the slider handle)
slider.oninput = function() {
    output.innerHTML = this.value + "km";
}