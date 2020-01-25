/**
 * 
 */

var rootURL = "../Cloud10"

ucitaj();

//findAll();

function ucitaj(){
	console.log('ucitavanje korisnika za prijavu.');
	$.ajax({
		type: 'GET',
		url: rootURL + "/rest/users/load",
		dataType : "json",
		success : dodajih
	});
}

function dodajih(data){
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	$.each(list, function(index, korisnik){
		console.log(korisnik.email);
	})
}

$(document).on('submit', function(e) {
	e.preventDefault();
	console.log("prijavljujem korisnika...");
	var email = $(this).find("input[name=\"log\"]").val();
	var lozinka = $(this).find("input[name=\"passw\"]").val();
	console.log(email);
	console.log(lozinka);
	
	if(!email || !lozinka){
		alert("Sva polja moraju biti popunjena");
	}
	
	$.ajax({
		type : 'POST',
		url : rootURL + "/rest/users/login",
		contentType : 'application/json',
		dataType : "text",
		data : formToJSON(email, lozinka),
		success : function(data) {
			if(data=="greska404"){
				alert("Nije dobra kombinacija email-a i lozinke!");
				//ocisti tekstualna polja!!
				$(this).find("input[name=\"log\"]").clear();
				$(this).find("input[name=\"passw\"]").clear();
			}else{
				window.location.href = "mainPage.html";
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
});

function formToJSON(email, lozinka) {
	return JSON.stringify({
		"email" : email,
		"password" : lozinka
	});
}
