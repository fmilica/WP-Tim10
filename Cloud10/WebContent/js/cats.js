var rooTURL = "../Cloud10"
	
var currentType= null
var currentName= null
window.onload = function() {
    // dobavljanje diskova
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/categories/getCategories",
		dataType : "json",
		success : fillContentTable,
		error : function(status, errorThrown) {
			alert("ERROR "+ status +": " + errorThrown)
		}
    })
    // dobavljanje trenutno ulogovanog korisnika
    $.ajax({
        type : "GET",
        url : rootURL + "/rest/users/checkCurrent",
        contentType : "application/json",
        success : setUserType,
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            alert("AJAX ERROR: " + errorThrown)
        }
    })
}

function setUserType(data){
	currentType = data;
	if(data == null){
		alert("Action not allowed!!!");
		window.location.href = "login.html";
	}
}

// popunjavanje tabele za prikaz diskova
function fillContentTable(allCats) {
	var list = allCats == null ? [] : (allCats instanceof Array ? allCats : [allCats])

	var table = $(document).find('#content')

	var header = $(document).find('#content thead')
	header.append('<th>' + "Name" + '</th>' + 
				  '<th>' + "Core Number" + '</th>' + 
				  '<th>' + "RAM" + '</th>' + 
				  '<th>' + "GPU" + '</th>')
	var br = 0;
	$.each(list, function(index, c) {
			var row = $('<tr id="' + index + '" class="detailedView"></tr>')
			row.append('<td>' + c.name + '</td>' + 
					   '<td>' + c.coreNum + '</td>' + 
					   '<td>' + c.ram + '</td>' + 
					   '<td>' + c.gpu + '</td>')
	        table.append(row)
    })
    
    // registrovanje za slusanje dogadjaja klika na red
    $('tr.detailedView').click(function(e){
        e.preventDefault();
        $.each(list, function(index, c){
            if(index == $(e.target).parent()["0"].id){
                editCatagory(c)
            }
        })
    })
}

function editCatagory(c) {
    // prilagodjavanje forme
    $('.card-title').text("Edit Category")
    $('.addBtn').hide()
    // prikaz forme
    $('#addForm').show();
    $('.editBtn').show();

    // postavljanje trenutnih vrednosti
    $('#iName').val(c.name)
    $('#iCoreNum').val(c.coreNum)
    $('#iRam').val(c.ram)
    $('#iGpu').val(c.gpu)
    
    currentName = c.name;
}

function addcat(){
	// prilagodjavanje forme
    $('.card-title').text("Add Category")
    $('.editBtn').hide()
    // prikaz forme
    $('#addForm').show();
    $('.addBtn').show();
    
    // postavljanje trenutnih vrednosti
    $('#iName').val("")
    $('#iCoreNum').val("")
    $('#iRam').val("")
    $('#iGpu').val("")
}

function addC(){
	var name = $('#iName').val()
    var coreNum = $('#iCoreNum').val()
    var ram = $('#iRam').val()
    var gpu = $('#iGpu').val()
    
    if(!name || !coreNum || !ram){
    	alert("All of the input boxes must be filled!")
    }
    
    if(!name){
    	var name = $('#iName').focus()
    }
    else if(!coreNum || !($.isNumeric(coreNum)) || parseInt(coreNum) < 0){
    	alert("Only positive numbers!")
    	$('#iCoreNum').focus();
    }
    else if(!ram || !($.isNumeric(ram)) || parseInt(ram) < 0){
    	alert("Only positive numbers!")
    	$('#iRam').focus();
    }
    else if(gpu){
    	if($.isNumeric(gpu)){
    		if(parseInt(gpu) < 0){
    			$('#iGpu').focus();
    		}
    	}
    	else{
    		$('#iGpu').focus();
    	}
    }
    else if(!gpu){
    	gpu = 0;
    }
    
    if(name && coreNum && ram){
    	$.ajax({
    		type: "POST",
    		url: rootURL + "/rest/categories/addCategory",
    		contentType : "application/json",
    		dataType : "json",
    		data : formJSONc(name, coreNum, ram, gpu),
    		success: function(data){
    			if(data.name == null){
    				alert("Category with name '"+name+"' already exists!");
    			}
    			else{
    				window.location.href = "catsPage.html";
    			}
    		},
    		error : function(status, errorThrown){
    			alert("ERROR "+status+": "+ errorThrown);
    		}
    	})
    }
}

function submitC(){
	var name = $('#iName').val()
    var coreNum = $('#iCoreNum').val()
    var ram = $('#iRam').val()
    var gpu = $('#iGpu').val()
    
    if(!name || !coreNum || !ram){
    	alert("All of the input boxes must be filled!")
    }
    
    if(!name){
    	var name = $('#iName').focus()
    }
    else if(!coreNum || !($.isNumeric(coreNum)) || parseInt(coreNum) < 0){
    	alert("Only positive numbers!")
    	$('#iCoreNum').focus();
    }
    else if(!ram || !($.isNumeric(ram)) || parseInt(ram) < 0){
    	alert("Only positive numbers!")
    	$('#iRam').focus();
    }
    else if(gpu){
    	if($.isNumeric(gpu)){
    		if(parseInt(gpu) < 0){
    			alert("Only positive numbers!")
    			$('#iGpu').focus();
    		}
    	}
    	else{
    		alert("Only positive numbers!")
    		$('#iGpu').focus();
    	}
    }
    else if(!gpu){
    	gpu = 0;
    }
    if(name && coreNum && ram && gpu){
        $.ajax({
            type : 'POST',
		    url : rootURL + "/rest/categories/changeCategory",
		    contentType : 'application/json',
		    dataType : "json",
		    data : formJSON(currentName, name, coreNum, ram, gpu),
		    success : function(data) {
				if(data.name == null){
					alert("Category with name '" + name +"' already exists!");
				}
				else{
					window.location.href = "catsPage.html";
				}
		    },
		    error : function(status, errorThrown) {
		    	alert("ERROR "+status+": "+ errorThrown);
		    }
        })
    }
}

function abortC(){
	// prilagodjavanje forme
    $('.editBtn').hide()
    // prikaz forme
    $('#addForm').hide();
    $('.addBtn').hide();
    
    // postavljanje trenutnih vrednosti
    $('#iName').val("")
    $('#iCoreNum').val("")
    $('#iRam').val("")
    $('#iGpu').val("")
}

function deleteC(){
	var name = $('#iName').val()
    var coreNum = $('#iCoreNum').val()
    var ram = $('#iRam').val()
    var gpu = $('#iGpu').val()
    
    if(!name || !coreNum || !ram){
    	alert("All of the input boxes must be filled!")
    }
    
    if(!name){
    	var name = $('#iName').focus()
    }
    else if(!coreNum || !($.isNumeric(coreNum)) || parseInt(coreNum) < 0){
    	alert("Only positive numbers!")
    	$('#iCoreNum').focus();
    }
    else if(!ram || !($.isNumeric(ram)) || parseInt(ram) < 0){
    	alert("Only positive numbers!")
    	$('#iRam').focus();
    }
    else if(gpu){
    	if($.isNumeric(gpu)){
    		if(parseInt(gpu) < 0){
    			$('#iGpu').focus();
    		}
    	}
    	else{
    		$('#iGpu').focus();
    	}
    }
    else if(!gpu){
    	gpu = 0;
    }
    
    if(name && coreNum && ram && gpu){
    	$.ajax({
    		type : 'POST',
    		url : rootURL + "/rest/categories/deleteCategory",
    		contentType : 'application/json',
    		dataType : "json",
    		data : formJSONc(name, coreNum, ram, gpu),
    		success : function(data) {
    			if(data.name == null){
    				alert("Category with name '" + name +"' deleted successfully!");
    			}
    			else{
    				window.location.href = "catsPage.html";
    			}
    		},
    		error : function(status, errorThrown){
    			alert("ERROR "+status+": "+ errorThrown);
    		}
    	})
    }
};

function formJSONc(name, coreNum, ram, gpu){
	return JSON.stringify({
		"name" : name,
		"coreNum" : coreNum,
		"ram" : ram,
		"gpu" : gpu
	});
}

function formJSON(currentName, name, coreNum, ram, gpu){
	return JSON.stringify({
		"oldName" : currentName,
		"name" : name,
		"coreNum" : coreNum,
		"ram" : ram,
		"gpu" : gpu
	});
}