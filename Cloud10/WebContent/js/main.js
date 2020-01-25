var rootURL = "../Cloud10"

$(document).on('submit', '#login-form', function(e) {
	e.preventDefault();
	console.log("try login");

	var username = $("#username").val();
	var password = $("#password").val();

	$.ajax({
		type : 'POST',
		url : rootURL + "/rest/users/validateLogin",
		dataType : "json",
		data : {
			"username" : username,
			"password" : password
		},
		success : function(response) {
			if (response == undefined) {
				alert("Wrong username or password!")
			} else {
				$(location).attr('href', 'mainPage.html');
			}
		},
		error : function() {
			alert("")
		}
	})
})
$(document).ready(function(e) {
	$("#listUsers").click(function(e) {
		console.log("list users")

		$.ajax({
			type : 'GET',
			url : "rest/userServ/getUsers",
			dataType : "json",
			success : function(response){
				printUsers(response);
			},
			error : function() {
				alert("Error")
			}
		});
	});
});


function printUsers(users){
	var div = $("#changeable");
	div.empty();
	
	var forma = $("<form id=\"forma1\"></form>")
	
	var table = $("<table></table>")
	
	table.append("<tr>" +
			"<th>Email</th>" +
			"<th>Name</th>" +
			"<th>Surname</th>" +
			"<th>Organisation</th>" +
			"</tr>");
	
	$.each(users, function (key, value) {
		var row = $("<tr></tr>")
		row.append("<td>" + value.email + "</td>");
		row.append("<td>" + value.name + "</td>");
		row.append("<td>" + value.surname + "</td>");
		if(value.organisation == null){
			row.append("<td>" + "/"+ "</td>");
		}else{
			row.append("<td>" + value.organisation.name+ "</td>");
		}
		
		table.append(row)
	})
	
	table.append('<tr><td><input type="submit" id ="dodaj" value = "Add new user"></td></tr>')
	
	forma.append(table)
	
	div.append(forma)
	
	$("#dodaj").click(function(e){
		e.preventDefault();
		addNewUser()
	});
		
}

function addNewUser(){
	var div = $("#edit")
	
	div.empty();
	
	var forma2 = $("<form id=\"forma2\"></form>");
	
	let table = $("<table></table>");
	
	var row1 = $("<tr></tr>");
	var row2 = $("<tr></tr>");
	var row3 = $("<tr></tr>");
	var row4 = $("<tr></tr>");
	var row5 = $("<tr></tr>");
	var row6 = $("<tr></tr>");
	var row7 = $("<tr></tr>");
	
	row1.append("<td>Email</td>");
	row1.append("<td><input type=\"text\" name=\"email\" id=\"email\"></td>");
	
	row2.append("<td>Password</td>");
	row2.append("<td><input type=\"text\" name=\"password\" id=\"password\"></td>");
	
	row3.append("<td>Name</td>");
	row3.append("<td><input type=\"text\" name=\"name\" id=\"name\"></td>");
	
	row4.append("<td>Surname</td>");
	row4.append("<td><input type=\"text\" name=\"surname\" id=\"surname\"></td>");
	
	row5.append("<td>Organisation</td>");
	row5.append("<td><input type=\"text\" name=\"organisation\" id=\"organisation\"></td>");
	
	row6.append("<td>Role</td>")
	var select = $("<select name=\"role\" id=\"role\"></select>")
	var option1 = $("<option value=\"User\">User</option>")
	select.append(option1)
	var option2 = $("<option value=\"Admin\">Admin</option>")
	select.append(option2)
	row6.append(select);
	
	row7.append("<td><input type=\"submit\" value=\"Add\"></td>");
	
	table.append(row1);
	table.append(row2);
	table.append(row3);
	table.append(row4);
	table.append(row5);
	table.append(row6);
	table.append(row7);
	
	forma2.append(table);
	div.append(forma2);
	
	$("#forma2").submit(function(e){
		e.preventDefault()
		var email = $("#email").val()
		var password = $("#password").val()
		var name = $("#name").val()
		var surname = $("#surname").val()
		var organisation = $("#organisation").val()
		var role = $("#role").val()
		
		if (!email || !password || !name || !surname || !organisation) {
			alert("All fields must be filled !");
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "rest/userServ/addNewUser",
			dataType : "json",
			data : {
				"email" : email,
	        	"password" : password,
	        	"name" : name,
	        	"surname" : surname,
	        	"organisation" : organisation,
	        	"role" : role
			},
			success : function(response){
				if (response == undefined) {
					alert("User with given email already exists!");
				} else {
					$("#edit").empty();
					printUsers(response);
					
				}
			}
		});
	});
	
}

