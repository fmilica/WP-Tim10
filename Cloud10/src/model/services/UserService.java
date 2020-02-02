package model.services;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Organisation;
import model.User;
import model.collections.Organisations;
import model.collections.Users;
import model.enums.RoleType;

@Path("/users")
public class UserService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	// always callable
	@GET
	@Path("/load")
	@Produces(MediaType.APPLICATION_JSON)
	public Response load() throws JsonProcessingException {
		System.out.println("ucitaj korisnike");
		getUsers().getUsersMap().values();
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		if(current == null) {
			//pa kad se ucitavaju prci put mora biti null
			// kada ne znamo jos kog je tipa korisnik, ne treba da vratimo nista
			// samo da ih ucitamo na kontekst
			System.out.println("JESTE NULL");
			getUsers();
			return Response.ok().build();
		}
		if(current.getRole() == RoleType.Admin){
			json = mapper.writeValueAsString(getAdminUsers().getUsersMap().values());
			return Response.ok(json).build();

		}
		else if(current.getRole() == RoleType.User){
			json = mapper.writeValueAsString(getCurrentUsers().getUsersMap().values());
			return Response.ok(json).build();
		}
		else{
			json = mapper.writeValueAsString(getUsers().getUsersMap().values());
			return Response.ok(json).build();
		}
	}
	
	// tested
	@GET
	@Path("/checkCurrent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCurrent() throws JsonProcessingException {
		System.out.println("checking current");
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		json = mapper.writeValueAsString(current);
		return Response.ok(json).build();
	}
	
	// tested
	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(User p) throws JsonProcessingException {
		System.out.println("login - provera na serverskoj strani");
		//current sme da bude null, tj mora biti
		User current = new User();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		//moze imati null podatke zato sto se tek loguje pa 
		//mu ne zanamo nista sem email i lozinke
		System.out.println(p);
		if (p == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		if(p.getEmail() == null || p.getPassword() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
		}
		if (p.getEmail().equals("") || p.getPassword().equals("")) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User has undefined fields!").build();
		}
		boolean ind = false;
		for (User k : getUsers().getUsersMap().values()) {
			if(k.getEmail().equals(p.getEmail()) && k.getPassword().equals(p.getPassword()))
			{
				ind = true;
				current = k;
				request.getSession().setAttribute("current", current);
				ctx.setAttribute("currentUser", current);
				break;
			}	
		}
		if(!ind) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User with entered email and password doesn't exist!").build();
		}
		System.out.println("TRENUTNO PRIJAVLJENI JE "+getCurrent().getEmail());
		json = mapper.writeValueAsString(getCurrent());
		return Response.ok(json).build();
	}
	
	// ok
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() {
		System.out.println("logout - podesavanje na serverskoj strani");
		if (getCurrent() == null) {
			// ne postoji niko ulogovan
			return Response.status(Response.Status.BAD_REQUEST).entity("You aren't logged in!").build();
		}
		setCurrent();
		return Response.ok().build();
	}
	
	// tested
	@POST
	@Path("/addUser")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response adduserSA(User p) throws JsonProcessingException {
		System.out.println("dodavanje korisnika na serverskoj strani");
		//super admin moze da popuni samo					admin moze da popuni samo 
		//email, sifru, ime, prezime i organizaciju			email, sifru, ime, prezime, organizaciju dobija od admina koji je trenutno prijavljen
		//za tip moze da odabere admin ili korisnik			za tip moze da odabere admin ili korisnik
		
		//provera validnosti podataka
		System.out.println(p);
		Users us = new Users();
		Organisation org = new Organisation();
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if (p == null) {
			// ako nista nije poslato -> jeste null
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		if(p.hasNull()) {
			System.out.println("ima kao neki null");
			// mislim da moramo da znamo koji field je null
			return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
		}
		if(current.getRole() == RoleType.Admin) {
			org = current.getOrganisation();
			// ako admin pokusava da doda ne za svoju organizacijuj
			if (!p.getOrganisation().getName().equals(org.getName())) {
				return Response.status(Response.Status.FORBIDDEN).entity("Adding users for organisations other than yours is forbidden!").build();
			}
			us = getAdminUsers();
		}
		else if(current.getRole() == RoleType.SuperAdmin){
			org = getOrganisations().getOrganisationsMap().get(p.getOrganisation().getName());
			us = getUsers();
		}
		else { //ako je user obican ne sme imati access ovoj metodi
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if(us.checkUser(p)) {
			// postoji korisnik sa unetim imenom
			return Response.status(Response.Status.BAD_REQUEST).entity("User with specified email already exists!").build();
		}
		
		p.setOrganisation(org);
		Organisations orgs = getOrganisations();
		orgs.getOrganisationsMap().get(org.getName()).addUser(p);
		us.addUser(p);
		
		ctx.setAttribute("users", us);
		if(current.getRole() == RoleType.SuperAdmin) {
			ctx.setAttribute("organisations", orgs);
		}
		else if(current.getRole() == RoleType.Admin) {
			ctx.setAttribute("organisation", org);
		}
		us.writeUsers(ctx.getRealPath(""));
		orgs.writeOrganisations(ctx.getRealPath(""));
		json = mapper.writeValueAsString(p);
		return Response.ok(json).build();
	}
	
	// ok
	@POST
	@Path("/changeUser")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUser(User p) throws JsonProcessingException {
		System.out.println("menjanje korisnika na serverskoj strani");
		//super admin moze da popuni samo 					admin moze da popuni samo korisnike u njegovoj organizaciji
		//sifru, ime, prezime i odabere tip za izmenu		sifru, ime, prezime i odabere tip za izmenu
		//EMAIL I ORGANISATION NE MOZE						EMAIL I ORGANISATION NE MOZE
		
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if (p == null) {
			// nije poslat objekat
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		if(p.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
		}
		Users us = new Users();
		if(current.getRole() == RoleType.Admin) {
			us = getAdminUsers();
		}
		else if(current.getRole() == RoleType.SuperAdmin){
			us = getUsers();
		}
		else { //ako je user obican ne sme imati access ovoj metodi
			return Response.status(Response.Status.FORBIDDEN).entity("403 - Access denied!").build();
		}
		if(us.userChanged(p)) {
			// mora da bude iz te organizacije
			return Response.status(Response.Status.FORBIDDEN).entity("Editing users for organisations other than yours is forbidden!").build();
		}
		//provera validnosti podataka
		if(us.checkUser(p)) {
			// postoji sa tim imenom
			// ne moze da menja ime pa mora biti isto
			json = mapper.writeValueAsString(new User());
			// da li onda ovde treba da se pozove da bi promenio vrednosti pre nego sto se okonca
			us.writeUsers(ctx.getRealPath(""));
			us.setUserValues(p);
			return Response.ok(json).build();
		}
		// ne moze mu dati novo ime
		// ne moze mu promeniti ime
		// zar ne bi trebalo da ovo ispod ne moze da se izvrsi?
		return Response.status(Response.Status.BAD_REQUEST).entity("Can't edit user that doesn't exist!").build();
		/*us.setUserValues(p);
		ctx.setAttribute("users", us);
		try {
			json = mapper.writeValueAsString(p);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(json).build();*/
	}
	
	// ok
	@POST
	@Path("/deleteUser")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(User p) throws JsonProcessingException {
		System.out.println("brisanje korisnika - provera na serverskoj strani");
		//admin i super admin mogu da brisu korisnike
		//naravno one koji su njima vidljivi
		// DA LI MORA SVE DA UNESE KADA BRISE ILI JE DOVOLJNO SAMO DA KAZE EMAIL?
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if (p == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		if(p.getEmail() == null) { 
			//ne treba da stoji hasNull vec da proveri da li je email null
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
		}
		Users us = new Users();
		if(current.getRole() == RoleType.Admin) {
			us = getAdminUsers();
		}
		else if (current.getRole() == RoleType.SuperAdmin) {
			us = getUsers();
		}
		else { //ako je user obican ne sme imati access ovoj metodi
			return Response.status(Response.Status.FORBIDDEN).entity("403 - Access denied!").build();
		}
		if(us.userChanged(p)) {
			return Response.status(Response.Status.FORBIDDEN).entity("Deleting users for organisations other than yours is forbidden!").build();
		}
		if(us.checkUser(p)) {
			// pokusava da brise nekog ko postoji
			json = mapper.writeValueAsString(new User());
			// da li ti ovo vrsi brisanje iz svih?
			// da li na kontekstu vise nece biti vidljiv obrisan user?
			Organisations orgs = getOrganisations();
			
			us.removeUser(p);
			orgs.removeUser(p);
			
			ctx.setAttribute("organisations", orgs);
			ctx.setAttribute("users", us);
			
			us.writeUsers(ctx.getRealPath(""));
			orgs.writeOrganisations(ctx.getRealPath(""));
			
			return Response.ok(json).build();
		}
		// pokusava da izbrise nepostojeceg
		return Response.status(Response.Status.BAD_REQUEST).entity("Can't edit user that doesn't exist!").build();
		/*us.removeUser(p);
		ctx.setAttribute("users", us);
		try {
			json = mapper.writeValueAsString(p);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(json).build();*/
	}
	
	// ok
	@POST
	@Path("/editProfile")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response editProfile(User p) throws JsonProcessingException {
		System.out.println("izmena profila - provera na serverskoj strani");
		//moze da se menja email, ime, prezime, sifra
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (p == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		if (p.getRole() != RoleType.SuperAdmin) {
			if(!(p.getPassword() == null || p.getPassword().trim().length() == 0)) {
				if(p.hasNullEdit()) {
					System.out.println("ima kao neki null");
					return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
				}
			}
		} else {
			// superadmin ima null za organizaciju
			// dodala sam ti novu funkciju hasNullSuperAdmin()
			// DAL IMA SMISLA DA SUPERADMIN MOZE DA SE PROMENI U DRUGI TIP
			// ONDA APLIKACIJA VISE NEMA SUPER ADMINA STO JE USLOV
			// MOZDA ZABRANITI TU PROMENU?
			if (p.hasNullSuperAdminEdit()) {
				return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
			}
		}
		Users us = getUsers();
		//validacija
		// ne sme da menja organizaciju
		// AKO JE IMA -> superAdmin je nema
		if (p.getRole() != RoleType.SuperAdmin) {
			if (!p.getOrganisation().getName().equals(current.getOrganisation().getName())) {
				// pokusao da menja organizaciju
				return Response.status(Response.Status.FORBIDDEN).entity("Changing one's organisaion is forbidden!").build();
			}	
		}
		// AKO ZELIS DA URADIMO ZABRANU PROMENE TIPA SUPERADMINA, EVO JE OVDE
		
		else {
			// superadmin je
			// zabrana da menja roleType
			if (p.getRole() != current.getRole()) {
				return Response.status(Response.Status.FORBIDDEN).entity("Changing role of SuperAdmin is forbidden!").build();
			}
		}
		if(!p.getEmail().equals(current.getEmail())) { // && !us.checkUser(p)
			// menja email
			if (us.checkUser(p)) {
				// postoji user sa unetim email-om
				return Response.status(Response.Status.BAD_REQUEST).entity("User with specified email already exists!").build();
			}
			// menja email i novouneti email ne postoji
			json = mapper.writeValueAsString(new User());
			// da li i ovde treba da dodelis izmenjene vrednosti currentu?
			us.removeUser(current);
			current.setEmail(p.getEmail());
			if(p.getPassword()==null || p.getPassword().trim().length() != 0) {
				current.setPassword(p.getPassword());
			}
			current.setName(p.getName());
			current.setSurname(p.getSurname());
			//us.changeUser(current,p);
			us.getUsersMap().put(current.getEmail(), current);
			ctx.setAttribute("curent", current);
			ctx.setAttribute("users", us);
			us.writeUsers(ctx.getRealPath(""));
			// ovo onda ovde?
			return Response.ok(json).build();
		} else {
			// ne menja email
			// dodeljuje sve vrednosti sem email?
			if(p.getPassword()==null || p.getPassword().trim().length() != 0) {
				current.setPassword(p.getPassword());
			}
			current.setName(p.getName());
			current.setSurname(p.getSurname());
			us.changeUser(current,p);
			ctx.setAttribute("curent", current);
			ctx.setAttribute("users", us);
			us.writeUsers(ctx.getRealPath(""));
			json = mapper.writeValueAsString(current);
			return Response.ok(json).build();
		}
		/*
		current.setEmail(p.getEmail());
		current.setPassword(p.getPassword());
		current.setName(p.getName());
		current.setSurname(p.getSurname());
		us.changeUser(current,p);
		ctx.setAttribute("curent", current);
		ctx.setAttribute("users", us);
		try {
			json = mapper.writeValueAsString(current);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.ok(json).build();*/
	}
	
	// ok
	@GET
	@Path("/getOrgs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrgs() throws JsonProcessingException {
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null || current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("403 - Access denied!").build();
		}
		Organisations orgs = new Organisations();
		if(current.getRole() == RoleType.SuperAdmin) {
			orgs = getOrganisations();
		}
		else {
			orgs.getOrganisationsMap().put(current.getOrganisation().getName(), current.getOrganisation());
		}
		json = mapper.writeValueAsString(orgs.getOrganisationsMap().values());
		return Response.ok(json).build();
	}
	
	// ok
	@GET
	@Path("/getUserType")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserType() throws JsonProcessingException {
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		json = mapper.writeValueAsString(current.getRole());
		return Response.ok(json).build();
	}
	
	private Users getUsers() {
		System.out.println("getUsers()");
		Users users = (Users) ctx.getAttribute("users");
		if(users == null) {
			users = new Users(ctx.getRealPath(""));
			if(users.getUsersMap().isEmpty()) {
				User superAdmin = new User("super@admin.com", "superadmin", "Super", "Admin", null, RoleType.SuperAdmin);
				users.getUsersMap().put(superAdmin.getEmail(), superAdmin);
			}
			Organisations o = new Organisations(ctx.getRealPath(""));
			ctx.setAttribute("organisations", o);
			for (Organisation org : o.getOrganisationsMap().values()) {
				for (String user : org.getUsers()) {
					if(users.getUsersMap().containsKey(user)) {
						users.setOrgForUser(user,org);
					}
				}
			}
			ctx.setAttribute("users",users);
			users.print();
		}
		return users;
	}
	
	private Users getAdminUsers() {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null) {
			users = new Users(ctx.getRealPath(""));
			User current = getCurrent();
			ctx.setAttribute("organisation", current.getOrganisation());
			System.out.println("ADMIN ORGANIZACIJA"+ctx.getAttribute("organisation"));
			Users retU = new Users();
			for (User user : users.getUsersMap().values()) {
				System.out.println(user);
				if(user.getRole() != RoleType.SuperAdmin) {
					System.out.println(user);
					retU.getUsersMap().put(user.getEmail(), user);
				}
			}
		}
		
		//request.getSession().setAttribute("current", current);
		return users;
	}
	
	private Users getCurrentUsers() {
		//pogresno radi funkcija, sreca pa je nikad ne pozivamo
		Users users = new Users();
		User current = getCurrent();
		Organisations o = new Organisations(ctx.getRealPath(""));
		ctx.setAttribute("organisation", o.getOrganisationsMap().get(current.getOrganisation().getName()));
		current.setOrganisation((Organisation)ctx.getAttribute("organisation"));
		
		users.getUsersMap().put(current.getEmail(), current);
		
		request.getSession().setAttribute("current", current);
		ctx.setAttribute("users",users);
		
		return users;
	}
	
	private User getCurrent() {
		User sc = (User) request.getSession().getAttribute("current");
		/*if (sc == null) {
			sc = new User();
			request.getSession().setAttribute("current", sc);
		}*/
		return sc;
	}
	
	private void setCurrent() {
		request.getSession().setAttribute("current", null);
		ctx.setAttribute("currentUser", null);
	}
	
	private Organisations getOrganisations() {
		Organisations o = (Organisations) ctx.getAttribute("organisations");
		return o;
	}
}
