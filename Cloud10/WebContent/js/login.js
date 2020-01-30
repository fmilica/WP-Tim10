/**
 * 
 */

var rootURL = "../Cloud10"

ucitaj();

//funkcija za ucitavanje korisnika iz fajla
function ucitaj(){
	console.log('ucitavanje korisnika za prijavu.');
	$.ajax({
		type: 'GET',
		url: rootURL + "/rest/users/load",
		dataType : "json",
		success : dodajih
	});
	// ucitavanje svih resursa na pocetku
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/discs/loadDiscs",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown)
		}
	})
}

//funkcija za ispis ucitanih korisnika ma konzolu (provera cisto)
function dodajih(data){
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	$.each(list, function(index, korisnik){
		console.log(korisnik.email);
	})
}

//poziv funkcije kada se okine event za submit na stranici
//posto je jedini submit na stranici pisem ga bez drugog parametra posle 'submit'
$(document).on('submit', function(e) {
	e.preventDefault();
	console.log("prijavljujem korisnika...");
	var email = $(this).find("input[name=\"log\"]").val();
	var password = $(this).find("input[name=\"passw\"]").val();
	console.log(email);
	console.log(password);
	
	if(!email){
		$(this).find('#displayError1').html('Polje email mora biti popunjeno');
	}
	if(!password){
		$(this).find('#displayError2').html('Polje password mora biti popunjeno');

	}
	if(email && password){
		$.ajax({
			type : 'POST',
			url : rootURL + "/rest/users/login",
			contentType : 'application/json',
			dataType : "text",
			data : formToJSON(email, password),
			success : function(data) {
				if(data=="greska404"){
					alert("Nije dobra kombinacija email-a i lozinke!");
					//ocisti tekstualna polja!!
					$("input[name=\"log\"]").val("");
					$("input[name=\"passw\"]").val("");
				}else{
					window.location.href = "mainPage.html";
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR: " + errorThrown);
			}
		});
	}
});

//funkcija zapravljenje json fajla od prosledjenih argumenata(Korisnik)
function formToJSON(email, password) {
	return JSON.stringify({
		"email" : email,
		"password" : password
	});
}