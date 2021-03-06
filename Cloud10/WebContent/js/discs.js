var rootURL = "../Cloud10"
var currentUser = null
var disc = null
var oldName = null
var currentVm = null
var change = false

window.onload = function() {
    // dobavljanje trenutno ulogovanog korisnika
    $.ajax({
        type : "GET",
        url : rootURL + "/rest/users/checkCurrent",
        contentType : "application/json",
        success : setupUser,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
    })
}

function setupUser(user) {
	currentUser = user
	setUserType(user)
	getAndFillContentTable()
}

function getAndFillContentTable() {
    // dobavljanje diskova
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/discs/getAllDiscs",
		dataType : "json",
		success : fillContentTable,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
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
    // ako je superAdmin prikazujemo mu i iz koje je organizacije disk
	if(currentUser.role == "SuperAdmin") {
		header.append('<th>' + "Organisation" + '</th>')
	}
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
        // ako je superAdmin prikazujemo mu i iz koje je organizacije disk
		if(currentUser.role == "SuperAdmin") {
			row.append('<td>' + d.organisation + '</td>')
		}
        table.append(row)
    })
    
    // registrovanje za slusanje dogadjaja klika na red
    $('tr.detailedView').click(function(e){
        e.preventDefault();
        $.each(list, function(index, disc){
            if(index == $(e.target).parent()["0"].id){
                oldName = disc.name
                // e eva debilu jedan
                //oldName = $('#' + index + ' td:first-child').text()
                currentVm = disc.vm
                //currentVm = $('#' + index + ' td:last-child').text()
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
        $('.adminOnly').show()
    } else {
        $('.user').show()
    }
}

function editDisc(disc) {
    change = true
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
    $('#iOrgan').val(disc.organisation)
    $('#iVMs').val(disc.vm)
    showForm()
}

$(document).ready(function() {
    // prikaz forme za dodavanje novog diska
    $('#addNew').click(function() {
		// postavljanje globalne promenljive
        change = false
        // prilagodjavanje forme
        $('.card-title').text("Add Disk")
        $('#submitChange').hide()
        $('#submitAbort').hide()
        $('#submitDelete').hide()
        // ciscenje popunjenih podataka
        $('#iName').val("")
        $('#iCap').val("")

        // prikaz
        $('#submitAdd').show()
        showForm()
    })

	// listanje vrednosti virtuelnih masina specificnih za odabranu organizaciju
	$('#iOrgan').on("change", function() {
        var currentOrgan = null
			// ako je admin
			if (currentUser.role == "Admin") {
				currentOrgan = currentUser.organisation.name
			} else {
				// ako je super admin
				// dobavljamo izabranu opciju
				currentOrgan = $('#iOrgan').val()
			}
			getVMOptions(currentOrgan)
	})

    // dobavljanje unetih vrednosti sa forme
	$('#submitAdd').click(function(e) {
        var dName = $('#iName').val()
        var dType = $('#iType').val()
        var dCap = $('#iCap').val()
        var dOrgan = $('#iOrgan').val()
        var dVM = $('#iVMs').val()

        disc = {
            name : dName,
            type : dType,
            capacity : dCap,
            organisation : dOrgan,
            vm : dVM
        }

        if (checkInput(e, dName, dCap)) {
        	$('#spanName').hide()
        	$('#spanCap').hide()
            $.ajax({
				type : "POST",
				url : rootURL + "/rest/discs/addDisc",
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify(disc),
				success : function(response){
					if(response == undefined) {
                        alert("Disc with specified name already exists!")
                        showForm()
                    }
                    else {
                        window.location.href="discsPage.html"
                    }
				},
                error : function(response) {
                    alert(response.responseText);
                    if (response.responseText.includes("No logged in user!")) {
                        window.location.href = "login.html"
                    }
                }
            })
        }
    })
    
    // izmena diska
    $('#submitChange').click(function(e) {
        var dName = $('#iName').val()
        var dType = $('#iType').val()
        var dCap = $('#iCap').val()
        var dOrgan = $('#iOrgan').val()
        var dVM = $('#iVMs').val()

        disc = {
            newName : dName,
            type : dType,
            capacity : dCap,
            organisation : dOrgan,
            vm : dVM,
            oldName : oldName
        }

        if(checkInput(e, dName, dCap)) {
        	$('#spanName').hide()
        	$('#spanCap').hide()
            $.ajax({
                type : "POST",
                url : rootURL + "/rest/discs/editDisc",
                contentType : "application/json",
				dataType : "json",
                data : JSON.stringify(disc),
                success : function(response){
					if(response == undefined) {
                        alert("Disc with specified name already exists!")
                        showForm()
                    }
                    else {
                        window.location.href="discsPage.html"
                    }
                },
                error : function(response) {
                    alert(response.responseText);
                    if (response.responseText.includes("No logged in user!")) {
                        window.location.href = "login.html"
                    }
                }
            })
        }
    })

    // brisanje diska
    // AKO POSTOJI VM ZA KOJI JE ZAKACEN -> VM GUBI VEZU SA NJIM
    $('#submitDelete').click(function(e) {
        var name = $('#iName').val()
        var organisation = $('#iOrgan').val()

        var disc = {
            name : name,
            vm : currentVm,
            organisation : organisation
        }
        $.ajax({
            type : "POST",
            url : rootURL + "/rest/discs/removeDisc",
            contentType : "application/json",
            dataType : "json",
            data : JSON.stringify(disc),
            success : function(response){
                if(response == undefined) {
                    alert("Disc with specified name doesn't exist!")
                    showForm()
                }
                else {
                    window.location.href="discsPage.html"
                }
            },
            error : function(response) {
                alert(response.responseText);
                if (response.responseText.includes("No logged in user!")) {
                    window.location.href = "login.html"
                }
            }
        })
    })

    // odustajanje od promene diska
    $('#submitAbort').click(function(e) {
        // gasi se forma za izmenu i nista se ne menja
        $('#addForm').hide()
        $('#spanName').hide()
        $('#spanCap').hide()
    })

    function checkInput(e, dName, dCap) {
        if(!dName) {
            alert("Name is required!")
            $('#iName').focus()
            $('#spanName').show()
            e.preventDefault()
            return false
        } else if (!dCap) {
            alert("Capacity is required!")
            $('#iCap').focus()
            $('#spanCap').show()
            e.preventDefault()
            return false
        } else if (!$.isNumeric(dCap)) {
            alert("Capacity must be a number!")
            $('#iCap').focus()
            $('#spanCap').show()
            e.preventDefault()
            return false
        } else if (parseInt(dCap) <= 0) {
            alert("Capacity must be greater than 0!")
            $('#iCap').focus()
            $('#spanCap').show()
            e.preventDefault()
            return false
        }
        return true
    }
})

function showForm() {
	$('#addForm').show()
	
	if (currentUser.role == "SuperAdmin") {
		if (change == false) {
			// moze da bira organizaciju AKO NIJE IZMENA
			$('#iOrgan').removeAttr("disabled")
		} else {
			// ne moze da bira organizaciju
			$('#iOrgan').attr("disabled", "disabled")
		}
		// dobavljanje organizacija
		$.ajax({
			type : "GET",
			url : rootURL + "/rest/organisations/getOrganisations",
			dataType : "json",
			success : addOrganOptionsSuper,
            error : function(response) {
                alert(response.responseText);
                if (response.responseText.includes("No logged in user!")) {
                    window.location.href = "login.html"
                }
            }
		})
	} else {
		$('#iOrgan').append($("<option></option>").attr("value", currentUser.organisation.name)
							.attr("selected", "selected").text(currentUser.organisation.name))
		$('#iOrgan').attr("disabled", "disabled");
		// da bi se namestili virtuelne masine u polje
		$('#iOrgan').trigger("change")
	}
}

function addOrganOptionsSuper(allOrgans) {
	var list = allOrgans == null ? [] : (allOrgans instanceof Array ? allOrgans : [allOrgans])
    $('#iOrgan').empty()
	$.each(list, function(index, organ) {
		$('#iOrgan').append($("<option></option>").attr("value", organ.name).text(organ.name))
	})
	// da bi se namestili virtuelne masine u polje
	$('#iOrgan').trigger("change")
}

function getVMOptions(currentOrgan) {
	$.ajax({
		type : "POST",
		url : rootURL + "/rest/vms/getOrganVMs",
		contentType : "application/json",
		data : JSON.stringify(currentOrgan),
		dataType : "json",
		success : addVMOptions,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	})
}

function addVMOptions(allVMs) {
	var list = allVMs == null ? [] : (allVMs instanceof Array ? allVMs : [allVMs])
    $('#iVMs').empty()
    // dodavanje prazne opcije -> ako (super)admin ne zeli da zakaci disk za vm odmah
    $('#iVMs').append($("<option></option>").attr("value", null).text(""))
	$.each(list, function(index, vm) {
		$('#iVMs').append($("<option></option>").attr("value", vm.name).text(vm.name))
	})
}