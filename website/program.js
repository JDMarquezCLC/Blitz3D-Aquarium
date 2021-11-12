var peces = {};

window.onload = function () {
        pecesBase();
    }

function mostrarPeces() {
	var html = ""/*  = '<div id="listaPeces">'; */
	
	for (i in peces) {
            html += "<p>" + peces[i].nombre + "<br><br>" + '<img src="' + peces[i].imagen + '" alt="' + peces[i].nombre + '">' + "</p>";
        }
        zonapeces.innerHTML = html;
            
	
	
	
	
	
	
	
	
}

function pecesBase() {
	peces["pez1"] = Object.create(Object.prototype);
    peces["pez1"].imagen = "pez1.png";
	peces["pez1"].nombre = "Pez Azul"
    peces["pez2"] = Object.create(Object.prototype);
    peces["pez2"].imagen = "pez2.png";
	peces["pez2"].nombre = "Pez Amarillo"
	peces["pez3"] = Object.create(Object.prototype);
    peces["pez3"].imagen = "pez3.png";
	peces["pez3"].nombre = "Tiburon"
}


document.getElementById("fsh").onclick = function() {mostrarPeces()};