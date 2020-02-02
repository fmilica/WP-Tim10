var rootURL = "../Cloud10"
	
var currentType = null
var currentName = null

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
        },
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	});
})

function loadO(){
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/organisations/getOrganisation",
		dataType : "json",
		success : showThem,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	});
	if(currentType == "Admin"){
		$(document).find("#sakrij").hide();
		$('.adminOnly').show()
	}
}

function loadOrgs(){
	$.ajax({
        type : 'GET',
		url : rootURL + "/rest/organisations/getOrganisations",
		dataType : "json",
		success : showThem,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
    })
}

function showOrgs(){
	$(document).find('h3.card-title').html("Add Organisation");
	$(document).find('input[name="add_name"]').val("")
	$(document).find('input[name="add_name"]').attr("readonly", false)
	$(document).find('input[name="add_desc"]').val("")
    $(document).find('input[name="add_logo"]').val("")
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
                    '<td id="'+index+'">'+org.description+'</td>';
        if(org.logo != null){
        	row += '<td id="'+index+'"><img src="'+org.logo+'" id="output" width="100"/></td>'
        }
        else {
        	row += '<td id="'+index+'"><img src=""/></td>'
        }
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
	$(document).find('input[name="add_name"]').attr("readonly", false)
    $(document).find('input[name="add_desc"]').val(org.description)
    //$(document).find('.imgUpload').val(org.logo)
	
    $(document).find('h3.card-title').html("Edit Organisation");
	$(document).find('.addForm').show();
    $(document).find('.addBtn').hide();
    $(document).find('.editBtn').show();
    
    currentName = org.name;
}

function addO(){
	var name = $(document).find('input[name="add_name"]').val()
    var desc = $(document).find('input[name="add_desc"]').val()
    var logo = $(document).find('input[name="add_logo"]').val()
    
    if(!name || !desc){
        alert("All of the input boxes must be filled!")
        event.preventDefault();
    }
	
	if(!name){
		$(document).find('input[name="add_name"]').focus()
	}
	else if(!desc){
		$(document).find('input[name="add_desc"]').focus()
	}
	
    if(name && desc){
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/organisations/addOrganisation",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(name, desc, logo),
		    success : function(data) {
		    	window.location.href = "organPage.html";
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

function submitO(){
	
	var name = $(document).find('input[name="add_name"]').val()
    var desc = $(document).find('input[name="add_desc"]').val()
    var logo = $(document).find('.imgUpload').val()
    console.log(logo)
    if(!name || !desc){
        alert("All of the input boxes must be filled!")
        event.preventDefault();
    }
	
	if(!name){
		$(document).find('input[name="add_name"]').focus()
	}
	else if(!desc){
		$(document).find('input[name="add_desc"]').focus()
	}
	
    if(name && desc){
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/organisations/changeOrganisation",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSONc(currentName, name, desc, logo),
		    success : function(data) {
				window.location.href = "organPage.html";
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

function discardO(){
	$(document).find('.addForm').hide();
    $(document).find('.addBtn').hide();
    $(document).find('.editBtn').hide();
}

function loadImage(event){
	var image = $(document).find("#output");
	image.src = event.target.files[0]
}

function formJSON(name, desc, logo){
    return JSON.stringify({
        "name" : name,
        "description" : desc,
        "logo" : logo
	});
}

function formJSONc(currentName, name, desc, logo){
    return JSON.stringify({
    	"oldName" : currentName,
        "name" : name,
        "description" : desc,
        "logo" : logo
	});
}