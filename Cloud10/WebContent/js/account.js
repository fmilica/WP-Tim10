var rootURL = "../Cloud10"
var currentOrg = null;
$(window).on('load', function(){
	
    $.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/checkCurrent",
		dataType : "json",
		success : function(data){
			showUser(data)
        },
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	});
	
})

function showUser(user){
	$(document).find('input[name="add_email"]').val(user.email)
	$(document).find('input[name="add_pass"]').val(user.pass)
	$(document).find('input[name="add_repeat"]').val("")
	$(document).find('input[name="add_name"]').val(user.name)
	$(document).find('input[name="add_surn"]').val(user.surname)
	if(user.role != "SuperAdmin"){
		currentOrg = user.organisation.name
	}
	else{
		currentOrg = ""
	}
	//disable za organisation
	if(user.role != "SuperAdmin"){
		if (user.role == "Admin") {
			$('.adminOnly').show()
		}
		$('#addSelect').empty()
		$('#addSelect').append(new Option(user.organisation.name))
		$('#addSelect').attr("disabled", "disabled")
	}
	else{
		$('#addSelect').empty()
		$('#addSelect').attr("disabled", "disabled")
	}
	//disable za role
	$('#selectType').empty()
	$('#selectType').append(new Option(user.role))
	$('#selectType').attr("disabled", "disabled")
	
	//prikaz forme za izmenu
	$(document).find('.addForm').show();
    $(document).find('.form-control superAdmin').show();
	$(document).find('.addBtn').show();
}

function saveChanges(){
	var email = $(document).find('input[name="add_email"]').val()
    var pass = $(document).find('input[name="add_pass"]').val()
    var repeat = $(document).find('input[name="add_repeat"]').val()
    var name = $(document).find('input[name="add_name"]').val()
    var surn = $(document).find('input[name="add_surn"]').val()
    //nebitni su mi
	var org = "adminorg"
    var type = $(document).find('select[name="selectType"]').val()
	if(!email || !name || !surn || !org || !type){
        alert("All of the input boxes must be filled!")
    }
	else if(pass && repeat){
		$('#spanPass').hide()
		$('#spanRepeat').hide()
		if(pass != repeat){
			$(document).find('input[name="add_pass"]').focus()
			$('#spanPass').show()
			$(document).find('input[name="add_repeat"]').focus()
			$('#spanRepeat').show()
			alert("Password and Repeated password are not the same!");
		}
	}
	else{
		$('#spanPass').show()
		$('#spanRepeat').show()
	}
	
	if(!email){
    	$(document).find('input[name="add_email"]').focus();
    	$('spanEmail').show()
    }
	if(email){
		$('spanEmail').hide()
	}
    if(!name){
    	$(document).find('input[name="add_name"]').focus();
    	$('spanName').show()
    }
    if(name){
    	$('spanEmail').hide()
    }
    if(!surn){
    	$(document).find('input[name="add_surn"]').focus();
    	$('spanSurn').show()
    }
    if(surn){
    	$('spanSurn').hide()
    }
	if(email && name && surn && org && type){
		$.ajax({
            type : 'POST',
		    url : rootURL + "/rest/users/editProfile",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(email, pass, name, surn, currentOrg, type),
		    success : function(data) {
				window.location.href = "accountPage.html";
		    },
			error : function(response) {
				alert(response.responseText);
				if (response.responseText.includes("No logged in user!")) {
					window.location.href = "login.html"
				}
			}
        })
	}
}

function formJSON(email, pass, name, surn, org, type){
    return JSON.stringify({
        "email" : email,
        "password" : pass,
        "name" : name,
        "surname" : surn,
        "organisation" : org,
        "role" : type
	});
}