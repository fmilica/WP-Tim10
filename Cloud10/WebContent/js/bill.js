var rootURL = "../Cloud10"
var currentUser

window.onload = function() {
    // sakrivanje tabele za prikaz racuna
    $('#iBill').hide()
    // prikaz forme za odabir datuma
    $('#iDate').show()

    // dobavljanje trenutno ulogovanog
    $.ajax({
        type : "GET",
        url : rootURL + "/rest/users/checkCurrent",
        contentType : "application/json",
        success : setUserType,
		error : function(response) {
			alert(response.responseText);
			if (response.responseText.includes("No logged in user!")) {
				window.location.href = "login.html"
			}
		}
    })

    // pozivanje racunanja racuna
    $('#iCalculate').click(function(e){
        e.preventDefault()
        var dates = {start : $('#iStart').val(),
                    end : $('#iEnd').val()}
        $.ajax({
            type : "POST",
            url : rootURL + "/rest/organisations/calculateBill",
            contentType : "application/json",
            data : JSON.stringify(dates),
            success : fillContentTable,
            error : function(response) {
                alert(response.responseText);
                if (response.responseText.includes("No logged in user!")) {
                    window.location.href = "login.html"
                }
            }
        })
    })

    // vracanje forme za odabir datuma
    $('#iAnother').click(function(e) {
        e.preventDefault()
        // sakrivanje tabele za prikaz racuna
        $('#iBill').hide()
        // prikaz forme za odabir datuma
        $('#iDate').show()
        // ciscenje tabele
        $('#vmHead').empty()
        $('#discHead').empty()
        $('#vmBody').empty()
        $('#discBody').empty()
        $('#total').empty()
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

function fillContentTable(bill) {
    // prikaz tabele za prikaz racuna
    $('#iBill').show()
    // sakrivanje forme za unos datuma
    $('#iDate').hide()

    var vmsList = bill.vms == null ? [] : (bill.vms instanceof Array ? bill.vms : [bill.vms])
    var discsList = bill.discs == null ? [] : (bill.discs instanceof Array ? bill.discs : [bill.discs])

	var vmHeader = $('#vmHead')
	vmHeader.append('<th>' + "Virtual Machine" + '</th>' + 
                  '<th>' + "Price" + '</th>')
    
    var discHeader = $('#discHead')
    discHeader.append('<th>' + "Disc" + '</th>' + 
                    '<th>' + "Price" + '</th>')
    

    var totalHeader = $('#total')
    totalHeader.append('<th>Total</th>' + 
                    '<th>' + bill.total + '</th>')
    
    var vmBody = $('#vmBody')
    $.each(vmsList, function(index, vm) {
        var row = $('<tr></tr>')
        row.append('<td>' + vm.name + '</td>' + 
                    '<td>' + vm.price + '</td>')
        vmBody.append(row)
    })
    var discBody = $('#discBody')
    $.each(discsList, function(index, disc) {
        var row = $('<tr></tr>')
        row.append('<td>' + disc.name + '</td>' + 
                    '<td>' + disc.price + '</td>')
        discBody.append(row)
    })
}