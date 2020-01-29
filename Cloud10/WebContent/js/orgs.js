var rootURL = "../Cloud10"
var currentType = null

$(window).on('load', function(){
    $.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/getUserType",
		dataType : "json",
		success : function(data){
            currentType = data;
            if(currentType == "SuperAdmin"){
                loadOrgs();
            }
            else if(currentType == "Admin"){
            	loadO();
            }
            else if(currentType == null){
            	alert("Action not allowed!!!");
				window.location.href = "login.html";
            }
        },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
})

function loadO(){
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/organisations/getOrganisation",
		dataType : "json",
		success : function(data){
			console.log(data.name + " " + data.description)
            editOrg(data);   
        },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
}

function loadOrgs(){
	$.ajax({
        type : 'GET',
		url : rootURL + "/rest/organisations/getOrganisations",
		dataType : "json",
		success : showThem,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
    })
}

function showOrgs(){
	$(document).find('h3.card-title').html("Add Organisation");
	$(document).find('input[name="add_name"]').attr("readonly", false)
    $(document).find('.addForm').show();
    $(document).find('.editBtn').hide();
    $(document).find('.addBtn').show();
}

function showThem(data){
	list = data == null ? [] : (data instanceof Array ? data : [data])
	var table = $(document).find('#orgsTable tbody')
    var header = $(document).find('table thead')
    var h = '<tr>'+
            '<th>'+"Name"+'</th>'+
            '<th>'+"Description"+'</th>'+
            '<th>'+"Logo"+'</th>' + '</tr>';
	header.append(h);	
	$.each(list, function(index,org){
        var tr = $('<tr id="'+index+'" class="edit"></tr>');
        var row = '<td id="'+index+'">'+org.name+'</td>'+
                    '<td id="'+index+'">'+org.description+'</td>'+
                    '<td id="'+index+'">'+org.logo+'</td>';
        tr.append(row)
		table.append(tr);
	});
	
	$('tr.edit').click(function(e){
		e.preventDefault();
		$.each(list, function(index,org){
			if(index == e.target.id){
				editOrg(org);
			}
		})
	})
}

function editOrg(org){
	$(document).find('input[name="add_name"]').val(org.name)
	$(document).find('input[name="add_name"]').attr("readonly", true)
    $(document).find('input[name="add_desc"]').val(org.description)
    $(document).find('input[name="add_logo"]').val(org.logo)
	
    $(document).find('h3.card-title').html("Edit Organisation");
	$(document).find('.addForm').show();
    $(document).find('.addBtn').hide();
    $(document).find('.editBtn').show();
}

function addO(){
	var name = $(document).find('input[name="add_name"]').val()
    var desc = $(document).find('input[name="add_desc"]').val()
    var logo = $(document).find('input[name="add_logo"]').val()
    
    if(!name || !desc || !logo){
        alert("All of the input boxes must be filled!")
        event.preventDefault();
    }
    else{
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/organisations/addOrganisation",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(name, desc, logo),
		    success : function(data) {
		    	if(data == null){
		    		alert("Organisation with name '"+ name +"' already exists!");
		    		event.preventDefault();
		    	}
		    	else{
		    		window.location.href = "organPage.html";
		    	}
		    },
		    error : function(XMLHttpRequest, textStatus, errorThrown) {
			    alert("AJAX ERROR: " + errorThrown);
		    }
        })
    }
}

function submitO(){
	var name = $(document).find('input[name="add_name"]').val()
    var desc = $(document).find('input[name="add_desc"]').val()
    var logo = $(document).find('input[name="add_logo"]').val()
    
    if(!name || !desc || !logo){
        alert("All of the input boxes must be filled!")
        event.preventDefault();
    }
    else{
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/organisations/changeOrganisation",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(name, desc, logo),
		    success : function(data) {
				if(data.email == null){
					alert("Organisation with name '" + name +"' already exists!");
				}
				else{
					window.location.href = "organPage.html";
				}
		    },
		    error : function(XMLHttpRequest, textStatus, errorThrown) {
			    alert("AJAX ERROR: " + errorThrown);
		    }
        })
    }
}

function discardO(){
	loadO();
}

function formJSON(name, desc, logo){
    return JSON.stringify({
        "name" : name,
        "description" : desc,
        "logo" : logo
	});
}