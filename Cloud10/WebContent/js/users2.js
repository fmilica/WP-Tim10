var rootURL = "../Cloud10"

validation();
var currentType = null

//funkcija za proveru da li prilikom dolaska na mainPage postoji trenutni ulogovani
//situacija kada posle odjave pritisnemo back
//plus dobavljanje tipa trenutnog ulogovanog 
//za prikaz funkcionalnosti
$(window).on('load', function(){
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/load",
		dataType : "json",
		success : showThem,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
    });
    $.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/getUserType",
		dataType : "json",
		success : function(data){
            currentType = data;
            if(currentType == "SuperAdmin"){
                loadOrgs();
            }
        },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
    validation();
    
})

function loadOrgs(){
    $.ajax({
        type : 'GET',
		url : rootURL + "/rest/users/getOrgs",
		dataType : "json",
		success : function(data){
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
            var tr = $('<tr></tr>');
            var row = '<td>'+user.email+'</td>'+
                        '<td>'+user.name+'</td>'+
                        '<td>'+user.surname+'</td>';
            
            
            if(currentType == "SuperAdmin"){
                row += '<td>'+user.organisation.name+'</td>'
            }
            tr.append(row)
			table.append(tr);
		}
	})
}

$(document).ready(function(){
    
})
function addUser(){
    $(document).find('.addForm').show();
    if(currentType == "SuperAdmin"){
        $(document).find('.superAdmin').show();
        $(document).find('.form-control superAdmin').show();
    }
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
    }
    else{
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/users/addUser",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(email, pass, name, surn, org, type),
		    success : function(data) {
                addToTable(data)
		    },
		    error : function(XMLHttpRequest, textStatus, errorThrown) {
			    alert("AJAX ERROR: " + errorThrown);
		    }
        })
    }
}

function addToTable(user){
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
	
    var tr = $('<tr></tr>');
    var row = '<td>'+user.email+'</td>'+
                '<td>'+user.name+'</td>'+
                '<td>'+user.surname+'</td>';
    
    if(currentType == "SuperAdmin"){
        row += '<td>'+user.organisation.name+'</td>'
    }
    tr.append(row)
    table.append(tr);
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