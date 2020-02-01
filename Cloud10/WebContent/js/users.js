var rootURL = "../Cloud10"


//funkcija za proveru da li prilikom dolaska na mainPage postoji trenutni ulogovani
//situacija kada posle odjave pritisnemo back
//plus dobavljanje tipa trenutnog ulogovanog 
//za prikaz funkcionalnosti
$(window).on('load', function(){
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/getUserType",
		dataType : "json",
		success : function(data){
			validation()
			showTypePage(data)
		},
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	})
})

function validation(){
	$.ajax({
        type : 'GET',
		url : rootURL + "/rest/users/checkCurrent",
		dataType : "json",
		success : function(data) {
			$(document).find("a#current").text(data.name + " " + data.surname)
		},
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
    });
}

function showTypePage(type) {
	if (type == "SuperAdmin") {
		$(document).find('.superAdmin').show()
		$(document).find('.admin').show()
	} else if (type == "Admin") {
		$(document).find('.admin').show()
	}
}

//pozivanje funkcije za odjavljivanje
function logout(){
    console.log("odjavljivanje..");
    $.ajax({
        type : 'GET',
		url :  rootURL + "/rest/users/logout",
		success : function() {
			window.location.href = "login.html";
		},
		error : function(response) {
			alert(response.responseText);
		}
    });
}