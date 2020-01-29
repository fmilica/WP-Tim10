var rootURL = "../Cloud10"
var categories;
var currentCat;

window.onload = function() {
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
	$.each(list, function(index, vm) {
		var row = $('<tr></tr>')
		row.append('<td>' + vm.name + '</td>' + 
				   '<td>' + vm.category.name + '</td>' + 
				   '<td>' + vm.coreNum + '</td>' + 
				   '<td>' + vm.category.ram + '</td>' + 
				   '<td>' + vm.category.gpu + '</td>')
		table.append(row)
	})
}


$(document).ready(function() {

	// prikaz forme za dodavanje nove virtuelne masine
	$('#addNew').click(function() {
		// prikaz forme
		showForm()

		/*
		$('#addForm').show()
		/*
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
		*//*
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
		// podesavanje u zavisnosti od tipa korisnika
		// dobavljanje tip trenutno ulogovanog korisnika
		$.ajax({
			type : "GET",
			url : rootURL + "/rest/users/checkCurrent",
			contentType : "application/json",
			success : setOrganisation,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR: " + errorThrown)
			}
		})
		*/
	})
/*
	function setOrganisation(user) {
		if(user.role == "Admin") {
			$('<option>').val(user.organisation.name).text(user.organisation.name).appendTo('#iOrgan');
			$('#iOrgan').val(user.organisation.name)
			$('#iOrgan').attr('disabled','disabled');
			// dodavanje diskova za tu organizaciju iz koje je admin
			
			// NEMOGUCE AKO NE KORISTIMO DODATAN FAJL
			/*
			$.ajax({
				type : "GET",
				url : rootURL + "/rest/organisations/getFreeDiscs",
				contentType : "application/json",
				dataType : "json",
				success : addDiscsOptions,
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("AJAX ERROR: " + errorThrown)
				}
			})
			*//*
		}
		else if(user.role == "SuperAdmin") {
			console.log("popuni sve organizacije")
		}
	}
*/
	// postavljanje vrednosti kategorije
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

	// dobavljanje unetih vrednosti sa forme
	$('#submitAdd').click(function(e) {
		var vmName = $('#iName').val()
		var organisation = $('#iOrgan').val()
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
				// organisation : organisation
				coreNum : vmCoreNum,
				ram : vmRam,
				gpu : vmGpu
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
	/*
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
	*/
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
	// podesavanje u zavisnosti od tipa korisnika
	// dobavljanje tip trenutno ulogovanog korisnika
	$.ajax({
		type : "GET",
		url : rootURL + "/rest/users/checkCurrent",
		contentType : "application/json",
		success : setOrganisation,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown)
		}
	})
}

function setOrganisation(user) {
	if(user.role == "Admin") {
		$('<option>').val(user.organisation.name).text(user.organisation.name).appendTo('#iOrgan');
		$('#iOrgan').val(user.organisation.name)
		$('#iOrgan').attr('disabled','disabled');
		// dodavanje diskova za tu organizaciju iz koje je admin
		
		// NEMOGUCE AKO NE KORISTIMO DODATAN FAJL
		/*
		$.ajax({
			type : "GET",
			url : rootURL + "/rest/organisations/getFreeDiscs",
			contentType : "application/json",
			dataType : "json",
			success : addDiscsOptions,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR: " + errorThrown)
			}
		})
		*/
	}
	else if(user.role == "SuperAdmin") {
		console.log("popuni sve organizacije")
	}
}

function addDiscsOptions(allDiscs) {
	var list = allDiscs == null ? [] : (allDiscs instanceof Array ? allDiscs : [allDiscs])
	$each(list, function(index, disc) {
		$('#iDisc').append('<option value="' + disc.name + '">' + 
							disc.name + '</option>')
	})
	$('.multiple-discs').select2();
}

function addOrganOptions(allOrgans) {
	var list = allOrgans == null ? [] : (allOrgans instanceof Array ? allOrgans : [allOrgans])
	$.each(list, function(index, organisation) {
		$('#iOrgan').append('<option value="' + organisation.name + '">' + 
							organisation.name + '</option>')
	})
	// da bi se namestili diskovi u polje
	$('#iOrgan').trigger("change")
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
