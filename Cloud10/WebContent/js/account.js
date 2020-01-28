var rootURL = "../Cloud10"

var currentUser = null;

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
				currentUser = data
			}
        },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
	$(document).find('input[name="add_email"]').val(currentUser.email)
	$(document).find('input[name="add_pass"]').val(currentUser.pass)
	$(document).find('input[name="add_repeat"]').val("")
	$(document).find('input[name="add_name"]').val(currentUser.name)
	$(document).find('input[name="add_surn"]').val(currentUser.surname)
	//disable za organisation
	$('#addSelect').empty()
	$('#addSelect').append(new Option(user.organisation.name))
	$('#addSelect').attr("disabled", "disabled")
	//disable za role
	$('#selectType').empty()
	$('#selectType').append(new Option(currentUser.role))
	$('#addSelect').attr("disabled", "disabled")
	//prikaz forme za izmenu
	$(document).find('.addForm').show();
    $(document).find('.superAdmin').show();
    $(document).find('.form-control superAdmin').show();
	$(document).find('.addBtn').show();
})


function saveChanges(){
	var email = $(document).find('input[name="add_email"]').val()
    var pass = $(document).find('input[name="add_pass"]').val()
    var repeat = $(document).find('input[name="add_repeat"]').val()
    var name = $(document).find('input[name="add_name"]').val()
    var surn = $(document).find('input[name="add_surn"]').val()
    //nebitni su mi
	var org = "adminorg"
    var type = $(document).find('select[name="selectType"]').val()
	if(!email || !pass || !name || !surn || !org || !type){
        alert("All of the input boxes must be filled!")
    }
	else if(pass != repeat){
		//fokus na te dve
		alert("Password and Repeated password are not the same!");
	}
	else{
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
					window.location.href = "usersPage.html";
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