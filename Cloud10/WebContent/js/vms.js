var rootURL = "../Cloud10"

window.onload = function() {
	$.ajax({
		type : 'GET',
		url : rootURL + "/rest/users/getUserType",
		dataType : "json",
		success : showTypePage,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	})
}

function showTypePage(type) {
	if (type == "Admin") {
		$(document).find('#cats').hide()
	}
	else if (type == "User") {
		$(document).find('#cats').hide()
		$(document).find('#users').hide()
		$(document).find('#organ').hide()
	}
}