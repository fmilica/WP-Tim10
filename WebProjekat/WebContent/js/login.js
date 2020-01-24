/**
 * 
 */

var korisnici = "/rest/korisnici/ucitaj";
var prijava = "../services/rest/korisnici/prijavi";

ucitaj();

//findAll();

function ucitaj(){
	console.log('ucitavanje korisnika za prijavu.');
	$.ajax({
		type: 'GET',
		url: korisnici,
		dataType : "json",
		success : dodajih
	});
}

function dodajih(data){
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	$.each(list, function(index, korisnik){
		console.log(korisnik.korisnickoIme);
	})
}

$(document).on('submit', function(e) {
	e.preventDefault();
	console.log("prijavljujem korisnika...");
	var data = $('#forma').serialize(); //uzima podatke sa forme ali ja nemam formu pa vrv ne uzima nista
	var korisnickoIme = $(this).find("input[name=\"log\"]").val();
	var lozinka = $(this).find("input[name=\"passw\"]").val();
	console.log(korisnickoIme);
	console.log(lozinka);
	
	if(korisnickoIme && lozinka){
		alert("Sva polja moraju biti popunjena");
	}
	
	$.ajax({
		type : 'POST',
		url : prijava,
		contentType : 'application/json',
		dataType : "text",
		data : formToJSON(korisnickoIme, lozinka),
		success : function(data) {
			if(data=="greska404"){
				alert("Nije dobra kombinacija email-a i lozinke!");
				//ocisti tekstualna polja!!
				$(this).find("input[name=\"log\"]").clear();
				$(this).find("input[name=\"passw\"]").clear();
			}else{
				window.location.href = "druga.html";
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
});

function formToJSON(korisnickoIme, lozinka) {
	return JSON.stringify({
		"email" : korisnickoIme,
		"lozinka" : lozinka
	});
}
