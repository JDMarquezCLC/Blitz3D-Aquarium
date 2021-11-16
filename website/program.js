var peces = {};
var devlog = document.getElementById("devlog");


window.onload = function () {
		devlog.style.visibility = "hidden";
		devlog.style.display = "none";
        pecesBase();
		
    }

function mostrarPeces() {
	var html = ""/*  = '<div id="listaPeces">'; */
	
	for (i in peces) {
            html += '<div class="pescaos">' + peces[i].nombre + "<br><br>" + '<img src="' + peces[i].imagen + '" alt="' + peces[i].nombre + '">' + "</div>";
        }
        zonapeces.innerHTML = html;
            
}

function mostrarDev() {
	/* var xhttp = new XMLHttpRequest();
	document.getElementById("zonapeces").innerHTML = this.responseText;
	xhttp.open("GET", "devlog.html", true);
	xhttp.send(); */
	//document.getElementById("devlog").style.visibility = "visible";
	zonapeces.innerHTML = devlog.innerHTML;
	zonapeces.style.visibility = "visible";
	zonapeces.style.display = "block";
}

function pecesBase() {
	peces["pez1"] = Object.create(Object.prototype);
    peces["pez1"].imagen = "psc1.gif";
	peces["pez1"].nombre = "Knobbed Porgy"
    peces["pez2"] = Object.create(Object.prototype);
    peces["pez2"].imagen = "psc2.gif";
	peces["pez2"].nombre = "Sweetlip Emperor"
	peces["pez3"] = Object.create(Object.prototype);
    peces["pez3"].imagen = "chark.gif";
	peces["pez3"].nombre = "Bull Shark"
}


document.getElementById("fsh").onclick = function() {mostrarPeces()};
document.getElementById("dvlg").onclick = function() {mostrarDev ()};