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
		success : showTypePage,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
	validation();
})

function validation(){
	$.ajax({
        type : 'GET',
		url : rootURL + "/rest/users/checkCurrent",
		dataType : "json",
		success : function(data) {
				if(!data.email){
                    window.location.href = "login.html";
                }
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
    });
}

function showTypePage(type) {
	if (type == "Admin") {
		$(document).find('#cats').hide()
	}
	else if (type == "User") {
		$(document).find('#cats').hide()
		$(document).find('#users').hide()
		$(document).find('#organ').hide()
	}
}

//pozivanje funkcije za odjavljivanje
function logout(){
    console.log("odjavljivanje..");
    $.ajax({
        type : 'POST',
		url :  rootURL + "/rest/users/logout",
		success : function() {
				window.location.href = "login.html";
				
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
    });
}