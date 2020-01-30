var rootURL = "../Cloud10"
var categories = null
var currentCat = null
var currentUser

window.onload = function() {
	// dobavljanje trenutno ulogovanog korisnika
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/users/checkCurrent",
		contentType : "application/json",
		success : setupUser,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown)
		}
	})
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// NE SME VRACATI SVE RESURSE												!!!!!
	// MORA DA IH FILTRIRA SKLADNO KORISNIKU NA SERVISU							!!!!!
	// PA TEK ONDA FILTRIRANE DA VRATI											!!!!!
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// -> PRE SLANJA SVIH NA SERVISU PROVERAVAMO KO JE ULOGOVANI I SALJEMO ADEKVATNO!
	// dobavljanje svih virtuelnih masina
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
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown)
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
		var row = $('<tr></tr>')
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
    } else {
        $('.user').show()
    }
}

$(document).ready(function() {

	// prikaz forme za dodavanje nove virtuelne masine
	$('#addNew').click(function() {
		// prikaz forme
		showForm()
	})

	// postavljanje vrednosti kategorije u tekst polja ispod
	$('#iCat').on("change", function() {
		// dobavljanje trenutne kategorije
		currentUser = $('#iCat').val()
		console.log(currentUser)
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
		// dobavljanje trenutne organizacije
		var currentOrgan = null
		// ako je admin
		if (currentUser.role == "Admin") {
			currentOrgan = currentUser.organisation
		} else {
			// ako je super admin
			// dobavljamo izabranu opciju
			currentOrgan = $('#iOrgan').val()
		}
		getFreeDiscsOptions(currentOrgan)
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
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("AJAX ERROR: " + errorThrown)
				}
			})
		}
	})
})

function showForm() {
	$('#addForm').show()

	if (currentUser.role == "SuperAdmin") {
		// dobavljanje organizacija
		$.ajax({
			type : "GET",
			url : rootURL + "/rest/organisations/getOrganisations",
			dataType : "json",
			success : addOrganOptions,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR: " + errorThrown)
			}
		})
	}

	// dobavljanje kateogrija
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/categories/getCategories",
		contentType : "application/json",
		success : addCatOptions,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown)
		}
	})
}

function addOrganOptions(allOrgans) {
	if (currentUser.role == "SuperAdmin") {
		var list = allOrgans == null ? [] : (allOrgans instanceof Array ? allOrgans : [allOrgans])
		$.each(list, function(index, organisation) {
			$('#iOrgan').append('<option value="' + organisation.name + '">' + 
								organisation.name + '</option>')
		})
	} else {
		$('#iOrgan').append('<option value="' + currentUser.organisation + '">' + 
							currentUser.organisation + '</option>')
	}
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
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown)
		}
	})
}
function addDiscOptions(allDiscs) {
	var list = allDiscs == null ? [] : (allDiscs instanceof Array ? allDiscs : [allDiscs])
	$.each(list, function(index, disc) {
		$('#iDiscs').append('<option value="' + disc.name + '">' + 
							disc.name + '</option>')
	})
}

function addCatOptions(allCats) {
	categories = allCats;
	var list = allCats == null ? [] : (allCats instanceof Array ? allCats : [allCats])
	$.each(list, function(index, category) {
		$('#iCat').append($("<option></option>").attr("value", category.name).text(category.name))
	})
	// da bi se namestile stavke kategorije u polja
	$('#iCat').trigger("change")
}
