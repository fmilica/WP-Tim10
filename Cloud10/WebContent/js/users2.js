var rootURL = "../Cloud10"

var currentType = null
var currentOrg = null
var retVal = null
//e.preventDefault();

//funkcija za proveru da li prilikom dolaska na mainPage postoji trenutni ulogovani
//situacija kada posle odjave pritisnemo back
//plus dobavljanje tipa trenutnog ulogovanog 
//za prikaz funkcionalnosti
$(window).on('load', function(){
    $.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/checkCurrent",
		dataType : "json",
		success : function(data){
            currentType = data.role;
            if(currentType == "SuperAdmin"){
                loadOrgs();
                //zato sto inace nece u tabeli prikazati kolonu organisations kada se ucita prvi put
                loadUsersToShow();
            }
            else if(currentType == "Admin"){
            	currentOrg = data.organisation.name
				$('.adminOnly').show()
            	loadUsersToShow();
            }
        },
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	});
})

function loadUsersToShow(){
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/load",
		// dataType : "json",
		success : showThem,
		error : function(response) {
			alert(response.responseText);
		}
    });
}

function loadOrgs(){
    $.ajax({
        type : 'GET',
		url : rootURL + "/rest/users/getOrgs",
		dataType : "json",
		success : function(data){
			/*if(data == null){
				alert("Action not allowed!!!");
				window.location.href = "login.html";
			}*/
            list = data == null ? [] : (data instanceof Array ? data : [data])
            $.each(list, function(index,org){
                $('#addSelect').append(new Option(org.name,org.name))
            })
        },
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
    })
}

function showThem(data){
	var list = data == null ? [] : (data instanceof Array ? data : [data]);
	
	var table = $(document).find('#usersTable tbody')
    var header = $(document).find('table thead')
    var h = '<tr>'+
            '<th>'+"Email"+'</th>'+
            '<th>'+"Name"+'</th>'+
            '<th>'+"Surname"+'</th>';

    if(currentType == "SuperAdmin"){
        h += '<th>'+"Organisation"+'</th>';
    }
	header.append(h + '</tr>');
	
	$.each(list, function(index,user){
		if(currentType == "Admin"){
			if(user.role != "SuperAdmin" && user.organisation.name == currentOrg){
				var tr = $('<tr id="'+index+'" class="edit"></tr>');
	            var row = '<td id="'+index+'">'+user.email+'</td>'+
	                        '<td id="'+index+'">'+user.name+'</td>'+
	                        '<td id="'+index+'">'+user.surname+'</td>';

	            tr.append(row)
				table.append(tr);
			}
		}
		else{
			if(user.role != "SuperAdmin"){
				var tr = $('<tr id="'+index+'" class="edit"></tr>');
	            var row = '<td id="'+index+'">'+user.email+'</td>'+
	                        '<td id="'+index+'">'+user.name+'</td>'+
	                        '<td id="'+index+'">'+user.surname+'</td>';
	            
	            
	            if(currentType == "SuperAdmin"){
	                row += '<td id="'+index+'">'+user.organisation.name+'</td>'
	            }
	            tr.append(row)
				table.append(tr);
			}
		}
		
	});
	
	$('tr.edit').click(function(e){
		e.preventDefault();
		$.each(list, function(index,user){
			if(index == e.target.id){
				editUser(user);
			}
		})
	})
}

function editUser(user){
	$(document).find('h3.card-title').html("Edit User");
	$(document).find('.addForm').show();
    if(currentType == "SuperAdmin"){
    	$(document).find('.superAdmin').show();
        $(document).find('.form-control superAdmin').show();
    }
    $(document).find('.addBtn').hide();
    $(document).find('.editBtn').show();
	
	//disable za email
	$(document).find('input[name="add_email"]').val(user.email)
	$(document).find('input[name="add_email"]').attr("readonly", true)
	
    $(document).find('input[name="add_pass"]').val(user.password)
    $(document).find('input[name="add_name"]').val(user.name)
    $(document).find('input[name="add_surn"]').val(user.surname)
	//disable za organisation, nije prakticno disable vec jedini izbor
	$('#addSelect').empty();
	$('#addSelect').append(new Option(user.organisation.name));
	$('#addSelect').attr("disabled", "disabled");
	$(document).find('select[name="selectType"]').val(user.role)
}

function submitU(){
	var email = $(document).find('input[name="add_email"]').val()
    var pass = $(document).find('input[name="add_pass"]').val()
    var name = $(document).find('input[name="add_name"]').val()
    var surn = $(document).find('input[name="add_surn"]').val()
    var org = "adminorg"
    if(currentType == "SuperAdmin"){
        org = $(document).find('select[name="selectAdd"]').val()
    }
	if(currentType == "Admin"){
		org = currentOrg
	}
    var type = $(document).find('select[name="selectType"]').val()
    if(!email || !pass || !name || !surn || !org || !type){
        alert("All of the input boxes must be filled!")
    }
    
    if(!email){
    	$(document).find('input[name="add_email"]').focus();
    }
    if(pass){
    	$(document).find('#spanPass').hide()
    }
    if(!pass){
    	$(document).find('input[name="add_pass"]').focus();
    	$(document).find('#spanPass').show()
    }
    if(name){
    	$(document).find('#spanName').hide()
    }
    if(!name){
    	$(document).find('input[name="add_name"]').focus();
    	$(document).find('#spanName').show()
    }
    if(surn){
    	$(document).find('#spanSurn').hide()
    }
    if(!surn){
    	$(document).find('input[name="add_surn"]').focus();
    	$(document).find('#spanSurn').show()
    }
    
    if(email && pass && name && surn && org && type){
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/users/changeUser",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(email, pass, name, surn, org, type),
		    success : function(data) {
				window.location.href = "usersPage.html";
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

function deleteU(){
	var email = $(document).find('input[name="add_email"]').val()
    var pass = $(document).find('input[name="add_pass"]').val()
    var name = $(document).find('input[name="add_name"]').val()
    var surn = $(document).find('input[name="add_surn"]').val()
    var org = "adminorg"
    if(currentType == "SuperAdmin"){
        org = $(document).find('select[name="selectAdd"]').val()
    }
    var type = $(document).find('select[name="selectType"]').val()
	
    if(email){
    	$(document).find('#spanEmail').hide();
    }
    if(!email){
    	alert("You need to enter user's email!")
    	$(document).find('input[name="add_email"]').focus();
    	$(document).find('#spanEmail').show();
    }
    if(email){
		$.ajax({
			type : 'POST',
			url : rootURL + "/rest/users/deleteUser",
			contentType : 'application/json',
			dataType : "json",
			data : formJSON(email, pass, name, surn, org, type),
			success : function(data) {
				// isto kao za brisanje kategorija
				/*
				if(data.email == null){
					alert("User with email '" + email +"' deleted successfully!");
				}
				else{*/
					window.location.href = "usersPage.html";
				//}
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

function discardU(){
	$(document).find('.addForm').hide();
    $(document).find('.addBtn').hide();
    $(document).find('.editBtn').hide();
    $(document).find('#spanEmail').hide();
    $(document).find('#spanPass').hide();
    $(document).find('#spanName').hide();
    $(document).find('#spanSurn').hide();
    $('#spanStar').hide();
}

function showForm(){
	$(document).find('h3.card-title').html("Add User");
	$(document).find('input[name="add_email"]').attr("readonly", false)
	$("#addSelect").removeAttr("disabled");
	if(currentType == "SuperAdmin"){
		$('#addSelect').empty();
		loadOrgs();
	}
    $(document).find('.addForm').show();
    if(currentType == "SuperAdmin"){
        $(document).find('.superAdmin').show();
        $(document).find('.form-control superAdmin').show();
    }
    $(document).find('input[name="add_email"]').val("")
    $(document).find('input[name="add_pass"]').val("")
    $(document).find('input[name="add_name"]').val("")
    $(document).find('input[name="add_surn"]').val("")
    
    $(document).find('.editBtn').hide();
    $(document).find('.addBtn').show();
    
	$(document).find('#spanEmail').hide()
	$(document).find('#spanPass').hide()
	$(document).find('#spanName').hide()
	$(document).find('#spanSurn').hide()
}

function add(){
	var email = $(document).find('input[name="add_email"]').val()
    var pass = $(document).find('input[name="add_pass"]').val()
    var name = $(document).find('input[name="add_name"]').val()
    var surn = $(document).find('input[name="add_surn"]').val()
    var org = "adminorg"
    if(currentType == "SuperAdmin"){
        org = $(document).find('select[name="selectAdd"]').val()
    }
    else if(currentType == "Admin"){
    	org = currentOrg
    }
    var type = $(document).find('select[name="selectType"]').val()
    
    if(!email || !pass || !name || !surn || !org || !type){
        alert("All of the input boxes must be filled!")
    }
    
    //nesto@nesto.nesto
	var odgEmail = null
	var prvi = ""
	let drugi = ""
	let arr2 = ""
	if(email){
		$('spanEmail').hide()
		$('#spanStar').hide();
		prvi = email.split("@")
		if(prvi.length == 2){
			drugi = prvi.pop() //uzima poslednjeg dodatog
			arr2 = drugi.split(".")
			if(arr2.length >= 2){
				odgEmail = "ok";
			}
			else{
				odgEmail = null;
			}
		}
		else{
			odgEmail = null;
		}
	}
	
	if(odgEmail == null){
		$('#spanEmail').html('Email must be aaa@bbb.ccc');
		$('#spanEmail').show()
		$('#spanStar').show();
	}
    if(!email){
    	$(document).find('input[name="add_email"]').focus();
    	$(document).find('#spanEmail').show();
    }
    if(pass){
    	$(document).find('#spanPass').hide()
    }
    if(!pass){
    	$(document).find('input[name="add_pass"]').focus();
    	$(document).find('#spanPass').show()
    }
    if(name){
    	$(document).find('#spanName').hide()
    }
    if(!name){
    	$(document).find('input[name="add_name"]').focus();
    	$(document).find('#spanName').show()
    }
    if(surn){
    	$(document).find('#spanSurn').hide()
    }
    if(!surn){
    	$(document).find('input[name="add_surn"]').focus();
    	$(document).find('#spanSurn').show()
    }
    
    if(email && odgEmail && pass && name && surn && org && type){
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/users/addUser",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(email, pass, name, surn, org, type),
		    success : function(data) {
		    	window.location.href = "usersPage.html"
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