var rootURL = "../Cloud10"

$(window).on('load', function(){
	
    $.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/checkCurrent",
		dataType : "json",
		success : function(data){
            if(data.role == null){
                alert("Action not allowed!!!");
				window.location.href = "login.html";
            }
			else{
				showUser(data)
			}
        },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
	
})

function showUser(user){
	$(document).find('input[name="add_email"]').val(user.email)
	$(document).find('input[name="add_pass"]').val(user.pass)
	$(document).find('input[name="add_repeat"]').val("")
	$(document).find('input[name="add_name"]').val(user.name)
	$(document).find('input[name="add_surn"]').val(user.surname)
	//disable za organisation
	if(user.role != "SuperAdmin"){
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
	else if(pass != repeat && pass && repeat){
		$(document).find('input[name="add_pass"]').focus()
		$(document).find('input[name="add_repeat"]').focus()
		alert("Password and Repeated password are not the same!");
	}
	
	if(!email){
    	$(document).find('input[name="add_email"]').focus();
    }
    else if(!name){
    	$(document).find('input[name="add_name"]').focus();
    }
    else if(!surn){
    	$(document).find('input[name="add_surn"]').focus();
    }
	
	if(email && pass && name && surn && org && type){
		$.ajax({
            type : 'POST',
		    url : rootURL + "/rest/users/editProfile",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(email, pass, name, surn, org, type),
		    success : function(data) {
				if(data.email == null){
					alert("User with email '" + email +"' already exists!");
				}
				else{
					window.location.href = "accountPage.html";
				}
		    },
		    error : function(XMLHttpRequest, textStatus, errorThrown) {
			    alert("AJAX ERROR: " + errorThrown);
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