var rootURL = "../Cloud10"
var categories = null
var currentCat = null
var vmOldName = null
var currentUser
var change = false
var currentVM

window.onload = function() {
	// dobavljanje kateogrija
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/categories/getCategories",
		contentType : "application/json",
		success : addCatOptions,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	})

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
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/vms/getAllVms",
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

function fillContentTable(allVms) {
	var list = allVms == null ? [] : (allVms instanceof Array ? allVms : [allVms])

	var table = $(document).find('#content')

	var header = $(document).find('#content thead')
	header.append('<th>' + "Name" + '</th>' + 
				  '<th>' + "Category" + '</th>' + 
				  '<th>' + "Core Number" + '</th>' + 
				  '<th>' + "RAM Capacity" + '</th>' + 
				  '<th>' + "GPU number" + '</th>')
	// ako je superAdmin prikazujemo mu i iz koje je organizacije virtuelna masina
	if(currentUser.role == "SuperAdmin") {
		header.append('<th>' + "Organisation" + '</th>')
	}
	$.each(list, function(index, vm) {
		var row = $('<tr id="' + index + '" class="detailedView"></tr>')
		row.append('<td>' + vm.name + '</td>' + 
				   '<td>' + vm.category.name + '</td>' + 
				   '<td>' + vm.coreNum + '</td>' + 
				   '<td>' + vm.ram + '</td>' + 
				   '<td>' + vm.gpu + '</td>')
		// ako je superAdmin prikazujemo mu i iz koje je organizacije virtuelna masina
		if(currentUser.role == "SuperAdmin") {
			row.append('<td>' + vm.organisation + '</td>')
		}
		table.append(row)
	})

	// registrovanje za slusanje dogadjaja klika na red
	$('tr.detailedView').click(function(e){
		e.preventDefault();
		$.each(list, function(index, vm){
			if(index == $(e.target).parent()["0"].id){
				currentVM = vm
				vmOldName = vm.name
				editVM(vm)
			}
		})
	})
}

function setUserType(user) {
	$('#user').text(user.name + " " + user.surname)
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

function editVM(vm) {
	// postavljanje globalne promenljive da je rec o izmeni
	change = true
	// pretpostavljamo da je ugasena (poslednja aktivnost nema null)
	$('#on-off').prop("checked", false);
    // prilagodjavanje forme
	setUserType(currentUser)
	// sklanjanje detaljnog prikaza aktivnosti
	$('#editActivesForm').hide()
    $('#mainForm').text("Edit VM")
    $('#submitAdd').hide()

	// prikaz forme
	$('#addForm').show();

	// postavljanje trenutnih vrednosti
	$('#iName').val(vm.name)
	$('#iOrgan').append($("<option></option>").attr("value", vm.organisation).text(vm.organisation))
	$('#iOrgan').attr("disabled", "disabled")
	$('#iDiscs').empty()
	var list = vm.discs == null ? [] : (vm.discs instanceof Array ? vm.discs : [vm.discs])
	$.each(list, function(list, disc) {
		$('#iDiscs').append($("<option></option>").attr("value", disc).text(disc))
	})
	$('#lActives').show()
	if (currentUser.role == "SuperAdmin") {
		$('#editActives').show()
	}
	var list1 = vm.activities == null ? [] : (vm.activities instanceof Array ? vm.activities : [vm.activities])
	var activesTable = $(document).find('#actives')
	var header = $(document).find('#actives thead')
	activesTable.empty()
	header.empty()
	header.append('<td>' + "VM power on time" + '</td>' + 
				  '<td>' + "VM power off time" + '</td>')
	activesTable.append(header)
	$.each(list1, function(list1, activity) {
		var row = $('<tr></tr>')
		row.append($('<td>' + activity.on + '</td>'))
		var off = ""
		if (activity.off != null) {
			off = activity.off
		} else {
			// ipak je upaljena, poslednja aktivnost ima vrednost null
			$('#on-off').prop("checked", true);
		}
		row.append($('<td>' + off + '</td>'))
		activesTable.append(row)
	})
	addCatOptions(categories)
	$('#iCat').val(vm.category.name)
	$('#iCore').val(vm.coreNum)
	$('#iRam').val(vm.ram)
	$('#iGpu').val(vm.gpu)
	if (currentUser.role == "Admin") {
		$('#on-off').removeAttr("disabled")
	} else {
		$('#on-off').attr("disabled", "disabled")
	}
}

$(document).ready(function() {

	// prikaz forme za dodavanje nove virtuelne masine
	$('#addNew').click(function() {
		// postavljanje globalne promenljive
		change = false
		// prilagodjavanje forme
        $('#mainForm').text("Add VM")
        $('#submitChange').hide()
        $('#submitAbort').hide()
        $('#submitDelete').hide()
        // ciscenje popunjenih podataka
		$('#iName').val("")
		$('#iDiscs').empty()
		$('#lActives').hide()
		$('#actives').empty()
		$('#editActives').hide()
		// kada se dodaje uvek je ugasena
		$('#on-off').prop("checked", false);
		$('#on-off').attr("disabled", "disabled")

        // prikaz
        $('#submitAdd').show()
		// prikaz forme
		showForm()
	})

	// postavljanje vrednosti kategorije u tekst polja ispod
	$('#iCat').on("change", function() {
		// dobavljanje trenutne kategorije
		currentCat = jQuery.grep(categories, function(c) {
			return c.name == $('#iCat').val()
		})
		currentCat = currentCat["0"]
		$('#iCore').val(currentCat.coreNum)
		$('#iRam').val(currentCat.ram)
		$('#iGpu').val(currentCat.gpu)
	})

	// listanje vrednosti diskova specificnih za odabranu organizaciju
	$('#iOrgan').on("change", function() {
		// ako je dodavanje (nije izmena) dobavlja slobodne diskove za organizaciju
		if (change == false) {
			// dobavljanje trenutne organizacije
			var currentOrgan = null
			// ako je admin
			if (currentUser.role == "Admin") {
				currentOrgan = currentUser.organisation.name
			} else {
				// ako je super admin
				// dobavljamo izabranu opciju
				currentOrgan = $('#iOrgan').val()
			}
			getFreeDiscsOptions(currentOrgan)
		}
	})

	// otvaranje detaljnog prikaza aktivnosti
	$('#editActives').click(function(e) {
		$('#editActivesForm').show()
		var editActivesTable = $('#editActivesTable tbody')
		var list2 = currentVM.activities == null ? [] : (currentVM.activities instanceof Array ? currentVM.activities : [currentVM.activities])
		editActivesTable.empty()
		$.each(list2, function(index, activity) {
			var row = $('<tr id="' + index + '"></tr>')
			row.append($('<td><input id="on' + index + '" type="text" value="' + activity.on + '"></td>'))
			var off = ""
			if (activity.off != null) {
				off = activity.off
			}
			row.append($('<td><input id="off' + index + '" type="text" value="' + off + '"></td>'))
			// checkBox za brisanje aktivnosti
			row.append($('<td class="center"><input id="delete' + index + '" type="checkbox"></td>'))
			editActivesTable.append(row)
		})
	})

	// odustajanje od promene aktivnosti
	$('#submitAbortActives').click(function(e) {
		$('#editActivesForm').hide()
	})

	// potvrdjivanje promene aktivnosti
	$('#submitEditActives').click(function(e) {
		var activitiesNotDelete = []
		$('#editActivesTable tbody tr').each(function(index, row) {
			if (!($('#delete' + index).is(":checked"))) {
				var activityNew =  {on : $('#on' + index).val(), 
									off : $('#off' + index).val()}
				activitiesNotDelete.push(activityNew)
			}
		})
		// slanje svih aktivnosti na server
		var vmActivities = { vmName : currentVM.name,
						activities : activitiesNotDelete }
		$.ajax({
			type : "POST",
			url : rootURL + "/rest/vms/editVMActivities",
			data : JSON.stringify(vmActivities),
			contentType : "application/json",
			success : window.location.href="mainPage.html",
			error : function(response) {
				alert(response.responseText);
				if (response.responseText.includes("No logged in user!")) {
					window.location.href = "login.html"
				}
			}
		})
	})

	// dobavljanje unetih vrednosti sa forme
	$('#submitAdd').click(function(e) {
		var vmName = $('#iName').val()
		var organisation = $('#iOrgan').val()
		var vmDiscs = $('#iDiscs').val()
		var vmCoreNum = $('#iCore').val()
		var vmRam = $('#iRam').val()
		var vmGpu = $('#iGpu').val()

		if(!vmName) {
			alert("Name is required")
			e.preventDefault()
		}
		else {
			// JSON objekat koji se salje
			vm = {
				name : vmName,
				category : {
					name : currentCat.name,
					coreNum : currentCat.coreNum,
					ram : currentCat.ram,
					gpu : currentCat.gpu
				},
				organisation : organisation,
				coreNum : vmCoreNum,
				ram : vmRam,
				gpu : vmGpu,
				discs : vmDiscs
			}

			$.ajax({
				type : "POST",
				url : rootURL + "/rest/vms/addVM",
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify(vm),
				success : function(response){
					if(response == undefined) {
						alert("Virtual Machine with specified name already exists!")
						showForm()
					}
					else {
						window.location.href="mainPage.html"
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

	// izmena VM
	$('#submitChange').click(function(e) {
		var vmName = $('#iName').val()
		var vmOrganisation = $('#iOrgan').val()
		var vmDiscs = $('#iDiscs').val()
		// OMOGUCITI UZIMANJE IZ LISTE SLOBODNIH NOVI DISK
		// var newDiscs = $('#iNewDisk').val()
		// OMOGUCITI TABELU SA AKTIVNOSTIMA
		// PORED SVAKE AKTIVNOSTI OMOGUCITI (X) ZA BRISANJE
		// ZA SVAKU POCETNU/KRAJNJU OMOGUCITI POPAP DA BIRA DATUM I VREME
		// MOZE BITI I TABELICA SA UNOSOM STAVKI DATUMA I VREMENA
		
		// aktivnosti se ne menjaju tu, pa uzmemo sve
		var vmActives = $.map($('#iActives option') ,function(option) {
			return option.value
		})
		var vmCoreNum = $('#iCore').val()
		var vmRam = $('#iRam').val()
		var vmGpu = $('#iGpu').val()
		// da li ju je upalio/ugasio
		var vmStatus = $('#on-off').is(":checked")

		if(!vmName) {
			alert("Name is required")
			e.preventDefault()
		}
		else {
			// JSON objekat koji se salje
			vm = {
				name : vmName,
				organisation : vmOrganisation,
				category : {
					name : currentCat.name,
					coreNum : currentCat.coreNum,
					ram : currentCat.ram,
					gpu : currentCat.gpu
				},
				coreNum : vmCoreNum,
				ram : vmRam,
				gpu : vmGpu,
				discs : vmDiscs,
				activities : vmActives,
				oldName : vmOldName,
				status : vmStatus
			}

			$.ajax({
				type : "POST",
				url : rootURL + "/rest/vms/editVM",
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify(vm),
				success : function(response){
					if(response == undefined) {
						alert("Virtual Machine with specified name already exists!")
						showForm()
					}
					else {
						window.location.href="mainPage.html"
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

	// brisanje VM
	// AKO POSTOJE DISKOVI KOJI SU ZAKACENI ZA NJU -> DISKOVI SE OSLOBADJAJU
	$('#submitDelete').click(function(e) {
		var vmName = $('#iName').val()
		var vmDiscs = $.map($('#iDiscs option') ,function(option) {
			return option.value
		})
		if(!vmName) {
			alert("Name is required")
			e.preventDefault()
		}
		else {
			// JSON objekat koji se salje
			vm = {
				name : vmName,
				discs : vmDiscs
			}
			$.ajax({
				type : "POST",
				url : rootURL + "/rest/vms/removeVM",
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify(vm),
				success : function(response){
					if(response == undefined) {
						alert("VM with specified name doesn't exist!")
						showForm()
					}
					else {
						window.location.href="mainPage.html"
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

	// odustajanje od izmene VM
	$('#submitAbort').click(function(e) {
		// gasi se forma za izmenu i nista se ne menja
		$('#addForm').hide()
	})

	// filtriranje i pretraga VM !!!!!!!!!!!
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
		// da bi se namestili diskovi u polje
		$('#iOrgan').trigger("change")
	}
}

function addOrganOptionsSuper(allOrgans) {
	var list = allOrgans == null ? [] : (allOrgans instanceof Array ? allOrgans : [allOrgans])
	$('#iOrgan').empty()
	$.each(list, function(index, organ) {
		$('#iOrgan').append($("<option></option>").attr("value", organ.name).text(organ.name))
	})
	// da bi se namestili diskovi u polje
	$('#iOrgan').trigger("change")
}

function getFreeDiscsOptions(currentOrgan) {
	$.ajax({
		type : "POST",
		url : rootURL + "/rest/discs/getFreeOrganDiscs",
		contentType : "application/json",
		data : JSON.stringify(currentOrgan),
		dataType : "json",
		success : addDiscOptions,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
	})
}
function addDiscOptions(allDiscs) {
	var list = allDiscs == null ? [] : (allDiscs instanceof Array ? allDiscs : [allDiscs])
	$('#iDiscs').empty()
	$.each(list, function(index, disc) {
		$('#iDiscs').append($("<option></option>").attr("value", disc.name).text(disc.name))
	})
}

function addCatOptions(allCats) {
	categories = allCats;
	// ispraznimo postojece kategorije
	$('#iCat').empty()
	var list = allCats == null ? [] : (allCats instanceof Array ? allCats : [allCats])
	$.each(list, function(index, category) {
		$('#iCat').append($("<option></option>").attr("value", category.name).text(category.name))
	})
	// da bi se namestile stavke kategorije u polja
	$('#iCat').trigger("change")
}
