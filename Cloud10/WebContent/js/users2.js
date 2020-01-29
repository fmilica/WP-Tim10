var rootURL = "../Cloud10"

var currentType = null
var retVal = null
//e.preventDefault();

//funkcija za proveru da li prilikom dolaska na mainPage postoji trenutni ulogovani
//situacija kada posle odjave pritisnemo back
//plus dobavljanje tipa trenutnog ulogovanog 
//za prikaz funkcionalnosti
$(window).on('load', function(){
    $.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/getUserType",
		dataType : "json",
		success : function(data){
            currentType = data;
            if(currentType == "SuperAdmin"){
                loadOrgs();
                //zato sto inace nece u tabeli prikazati kolonu organisations kada se ucita prvi put
                loadUsersToShow();
            }
            else if(currentType == "Admin"){
            	loadUsersToShow();
            }
        },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
})

function loadUsersToShow(){
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/load",
		dataType : "json",
		success : showThem,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
    });
}

function loadOrgs(){
    $.ajax({
        type : 'GET',
		url : rootURL + "/rest/users/getOrgs",
		dataType : "json",
		success : function(data){
			if(data == null){
				alert("Action not allowed!!!");
				window.location.href = "login.html";
			}
            list = data == null ? [] : (data instanceof Array ? data : [data])
            $.each(list, function(index,org){
                $('#addSelect').append(new Option(org.name,org.name))
            })
        },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
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
    var type = $(document).find('select[name="selectType"]').val()
    if(!email || !pass || !name || !surn || !org || !type){
        alert("All of the input boxes must be filled!")
        event.preventDefault();
    }
    else{
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/users/changeUser",
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
	
    if(!email){
    	alert("You need to enter user's email!")
    	event.preventDefault();
    }
	$.ajax({
		type : 'POST',
		url : rootURL + "/rest/users/deleteUser",
		contentType : 'application/json',
		dataType : "json",
		data : formJSON(email, pass, name, surn, org, type),
		success : function(data) {
			if(data.email == null){
				alert("User with email '" + email +"' deleted successfully!");
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

function discardU(){
	$(document).find('.editDelete').hide();
    if(currentType == "SuperAdmin"){
        $(document).find('.superAdmin').hide();
        $(document).find('.form-control superAdmin').hide();
    }
	/* nema poente da ih brisem 
	$(document).find('input[name="add_email"]').clear()
    $(document).find('input[name="add_pass"]').clear()
    $(document).find('input[name="add_name"]').clear()
    $(document).find('input[name="add_surn"]').clear()
	*/
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
    $(document).find('.editBtn').hide();
    $(document).find('.addBtn').show();
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
    var type = $(document).find('select[name="selectType"]').val()
    
    if(!email || !pass || !name || !surn || !org || !type){
        alert("All of the input boxes must be filled!")
        event.preventDefault();
    }
    else{
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/users/addUser",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(email, pass, name, surn, org, type),
		    success : function(data) {
		    	if(data == null){
		    		alert("User with that email already exists!");
		    		event.preventDefault()
		    	}
		    	else{
		    		window.location.href = "usersPage.html"
		    		
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