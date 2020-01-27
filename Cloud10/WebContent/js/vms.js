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
		row.append('<th>' + vm.name + '</th>' + 
				   '<th>' + vm.category.name + '</th>' + 
				   '<th>' + vm.coreNum + '</th>' + 
				   '<th>' + vm.category.ram + '</th>' + 
				   '<th>' + vm.category.gpu + '</th>')
		table.append(row)
	})
}


$(document).ready(function() {

	// prikaz forme za dodavanje nove virtuelne masine
	$('.addVM').click(function() {
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
			dataType : "json",
			success : addCatOptions,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR: " + errorThrown)
			}
		})
	})

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
			var virtualMachine = {
				"name" : vmName,
				"category" : currentCat.name,
				// organisation : organisation
				"coreNum" : vmCoreNum,
				"ram" : vmRam,
				"gpu" : vmGpu
			}
			//var obj = { customer: complexObject };
			//var data2send = JSON.stringify(obj);
			// dodajemo u listu svih
			$.ajax({
				type : "POST",
				url : rootURL + "/rest/vms/addVM",
				contentType : "json",
				data : JSON.stringify({
					"name" : vmName,
					"category" : currentCat.name,
					// organisation : organisation
					"coreNum" : vmCoreNum,
					"ram" : vmRam,
					"gpu" : vmGpu
				}),
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("AJAX ERROR: " + errorThrown)
				}
			})
		}
	})
})

function addOrganOptions(allOrgans) {
	var list = allOrgans == null ? [] : (allOrgans instanceof Array ? allOrgans : [allOrgans])
	$.each(list, function(index, organisation) {
		$('#iOrgan').append('<option value="' + organisation.name + '">' + 
							organisation.name + '</option>')
	})
}

function addCatOptions(allCats) {
	categories = allCats;
	var list = allCats == null ? [] : (allCats instanceof Array ? allCats : [allCats])
	$.each(list, function(index, category) {
		$('#iCat').append($("<option></option>").attr("value", category.name).text(category.name))
	})
	$('#iCat').trigger("change");
}
