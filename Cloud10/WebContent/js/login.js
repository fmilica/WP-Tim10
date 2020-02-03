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
		// ne sem da bude tu inace ne ume da parsira -> glup je
		//dataType : "json",
		error : function(response) {
			alert(response.responseText)
		}
		//success : dodajih
	});
	// ucitavanje svih resursa na pocetku
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/discs/loadDiscs",
		/*error : function(response) {
			alert(response.responseText)
		}*/
	})
}
/*
//funkcija za ispis ucitanih korisnika ma konzolu (provera cisto)
function dodajih(data){
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	$.each(list, function(index, korisnik){
		console.log(korisnik.email);
	})
}
*/
//poziv funkcije kada se okine event za submit na stranici
//posto je jedini submit na stranici pisem ga bez drugog parametra posle 'submit'
$(document).on('submit', function(e) {
	e.preventDefault();
	console.log("prijavljujem korisnika...");
	var email = $(this).find("input[name=\"log\"]").val();
	var password = $(this).find("input[name=\"passw\"]").val();
	console.log(email);
	console.log(password);
	
	if(!email){// ovog
		$(this).find('#displayError1').html('Field must be filled!');
	}
	
	//nesto@nesto.nesto
	var odg = null
	var prvi = ""
	let drugi = ""
	let arr2 = ""
	if(email){
		prvi = email.split("@")
		if(prvi.length == 2){
			drugi = prvi.pop() //uzima poslednjeg dodatog
			arr2 = drugi.split(".")
			if(arr2.length >= 2){
				odg = "ok";
			}
			else{
				odg = null;
			}
		}
		else{
			odg = null;
		}
	}
	
	if(odg == null){
		$(this).find('#displayError1').html('Email must be aaa@bbb.ccc');
	}
	
	if(!password){
		$(this).find('#displayError2').html('Field must be filled!');

	}
	// (odg && password)
	if(email && odg && password){
		$.ajax({
			type : 'POST',
			url : rootURL + "/rest/users/login",
			contentType : 'application/json',
			dataType : "text",
			data : formToJSON(email, password),
			success : function(data) {
				window.location.href = "mainPage.html";
			},
			error : function(response) {
				alert(response.responseText)
				if (response.responseText.includes("No logged in user!")) {
					window.location.href = "login.html"
				}
				if (response.responseText.includes("doesn't exist")) {
					// slucaj kada je kombinacija nevalidna
					// ocisti tekstualna polja!
					$("input[name=\"log\"]").val("");
					$("input[name=\"passw\"]").val("");
				}
			}
		});
	}
});

$(document).on('input', '#login',  function(e){
	$(this).find('#displayError1').hide();
})


//funkcija zapravljenje json fajla od prosledjenih argumenata(Korisnik)
function formToJSON(email, password) {
	return JSON.stringify({
		"email" : email,
		"password" : password
	});
}