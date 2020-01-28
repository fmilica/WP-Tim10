var rootURL = "../Cloud10"
var currentUser = null

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
function fillContentTable(allDiscs) {
	var list = allDiscs == null ? [] : (allDiscs instanceof Array ? allDiscs : [allDiscs])

	var table = $(document).find('#content')

	var header = $(document).find('#content thead')
	header.append('<th>' + "Name" + '</th>' + 
				  '<th>' + "Type" + '</th>' + 
				  '<th>' + "Capacity" + '</th>' + 
				  '<th>' + "Virtual Machine" + '</th>')
	$.each(list, function(index, d) {
        var row = $('<tr id="' + index + '" class="detailedView"></tr>')
        if(d.vm == null) {
            var vm = "Available"
        }else{
            vm = d.vm
        }
		row.append('<td>' + d.name + '</td>' + 
				   '<td>' + d.type + '</td>' + 
				   '<td>' + d.capacity + '</td>' + 
				   '<td>' + vm + '</td>')
        table.append(row)
    })
    
    // registrovanje za slusanje dogadjaja klika na red
    $('tr.detailedView').click(function(e){
        e.preventDefault();
        $.each(list, function(index, disc){
            if(index == $(e.target).parent()["0"].id){
                editDisc(disc)
            }
        })
    })
}

function setUserType(user) {
    currentUser = user
    if(user.role == "SuperAdmin") {
        $('.superAdmin').show()
        $('.admin').show()
        $('.user').show()
    } else if(user.role == "Admin") {
        $('.admin').show()
        $('.user').show()
    } else {
        $('.user').show()
    }
}

function editDisc(disc) {
    // prilagodjavanje forme
    setUserType(currentUser)
    $('.card-title').text("Edit Disk")
    $('#submitAdd').hide()
    // prikaz forme
    $('#addForm').show();

    // postavljanje trenutnih vrednosti
    $('#iName').val(disc.name)
    $('#iType').val(disc.type)
    $('#iCap').val(disc.capacity)
}

$(document).ready(function() {
    // prikaz forme za dodavanje novog diska
    $('#addNew').click(function() {
        // prilagodjavanje forme
        $('.card-title').text("Add Disk")
        $('#submitChange').hide()
        $('#submitAbort').hide()
        $('#submitDelete').hide()

        // prikaz
        $('#submitAdd').show()
        $('#addForm').show()
    })

    // dobavljanje unetih vrednosti sa forme
	$('#submitAdd').click(function(e) {
        var dName = $('#iName').val()
        var dType = $('#iType').val()
        var dCap = $('#iCap').val()

        if (checkInput(e, dName, dCap)) {
            $.ajax({
				type : "POST",
				url : rootURL + "/rest/discs/addDisc",
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify({
					"name" : dName,
                    "type" : dType,
                    "capacity" : dCap,
                    "vm" : null
				}),
				success : function(response){
					if(response == undefined) {
						alert("Disc with specified name already exists!")
                    }
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("AJAX ERROR: " + errorThrown)
                }
            })
        }
		/*if(!dName) {
            alert("Name is required!")
            e.preventDefault()
        } else if (!dCap) {
            alert("Capacity is required!")
            e.preventDefault()
        } else if (!$.isNumeric(dCap)) {
            alert("Capacity must be a number!")
            e.preventDefault()
        }
		else {
			$.ajax({
				type : "POST",
				url : rootURL + "/rest/discs/addDisc",
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify({
					"name" : dName,
                    "type" : dType,
                    "capacity" : dCap,
                    "vm" : null
				}),
				success : function(response){
					if(response == undefined) {
						alert("Disc with specified name already exists!")
                    }
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("AJAX ERROR: " + errorThrown)
				}
			})
		}*/
    })
    
    // izmena diska
    $('#submitChange').click(function(e) {
        var name = $('#iName').val()
        var type = $('#iType').val()
        var capacity = $('#iCap').val()

        if(checkInput(e, name, capacity)) {
            $.ajax({
                type : "POST",
                url : rootURL + "/rest/discs/editDisc",
                contentType : "json",
                dataType : "application/json",
                data : JSON.stringify({
                    "name" : name,
                    "type" : type,
                    "capacity" : capacity
                }),
                success : function(response){
					if(response == undefined) {
                        alert("Disc with specified name already exists!")
                        // KORISNIK OSTAJE NA FORMA ZA MENJANJE
                    }
                    // KORISNIKU SE GASI FORMA ZA MENJANJE
                    $('#addForm').hide()
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("AJAX ERROR: " + errorThrown)
				}
            })
        }
    })

    // brisanje diska
    // AKO POSTOJI VM ZA KOJI JE ZAKACEN -> VM GUBI VEZU SA NJIM
    $('#submitDelete').click(function(e) {
        var name = $('#iName').val()

        $.ajax({
            type : "POST",
            url : rootURL + "/rest/discs/removeDisc",
            contentType : "json",
            dataType : "application/json",
            data : JSON.stringify({
                "name" : name
            }),
            success : function(response){
                if(response == undefined) {
                    alert("Disc with specified name doesn't exist!")
                    // KORISNIK OSTAJE NA FORMA ZA BRISANJE
                }
                // KORISNIKU SE GASI FORMA ZA BRISANJE
                $('#addForm').hide()
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                alert("AJAX ERROR: " + errorThrown)
            }
        })
    })

    // odustajanje od promene diska
    $('submitAbort').click(function(e) {
        // gasi se forma za izmenu i nista se ne menja
        $('#addForm').hide()
    })

    function checkInput(e, dName, dCap) {
        if(!dName) {
            alert("Name is required!")
            e.preventDefault()
            return false
        } else if (!dCap) {
            alert("Capacity is required!")
            e.preventDefault()
            return false
        } else if (!$.isNumeric(dCap)) {
            alert("Capacity must be a number!")
            e.preventDefault()
            return false
        }
        return true
    }
})