var rootURL = "../Cloud10"

verification();

//funkcija za proveru da li prilikom dolaska na mainPage postoji trenutni ulogovani
//situacija kada posle odjave pritisnemo back??
function verification(){
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
};

//pozivanje funkcije za odjavljivanje
$(document).on('submit', '.Logout', function(e){
    e.preventDefault();
    console.log("odjavljivanje..");
    $.ajax({
        type : 'POST',
		url : rootURL + "/rest/users/logout",
		success : function() {
				window.location.href = "login.html";
				
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
    });
})