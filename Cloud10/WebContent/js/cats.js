window.onload = function() {
    // dobavljanje diskova
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/discs/getAllDiscs",
		dataType : "json",
		success : fillContentTable,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown)
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

// popunjavanje tabele za prikaz diskova
function fillContentTable(allCats) {
	var list = allCats == null ? [] : (allCats instanceof Array ? allCats : [allCats])

	var table = $(document).find('#content')

	var header = $(document).find('#content thead')
	header.append('<th>' + "Name" + '</th>' + 
				  '<th>' + "Core Number" + '</th>' + 
				  '<th>' + "RAM" + '</th>' + 
				  '<th>' + "GPU" + '</th>')
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
    $('#submitAdd').hide()
    // prikaz forme
    $('#addForm').show();

    // postavljanje trenutnih vrednosti
    $('#iName').val(c.name)
    $('#iCoreNum').val(c.coreNum)
    $('#iRam').val(c.ram)
    $('#iGpu').val(c.gpu)
}