package model.services;

import java.io.FileNotFoundException;

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

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

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
	
	@GET
	@Path("/load")
	@Produces(MediaType.APPLICATION_JSON)
	public Response load() throws JsonIOException, JsonSyntaxException, JsonProcessingException, FileNotFoundException{
		System.out.println("ucitaj korisnike");
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current.getEmail() == null) {
			//pa kad se ucitavaju prci put mora biti null
			System.out.println("JESTE NULL");
			json = mapper.writeValueAsString(getUsers().getUsersMap().values());
			return Response.ok(json).build();
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
	
	@GET
	@Path("/checkCurrent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCurrent() throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		System.out.println("---------validacija----------");
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.serverError().entity("Access denied!").build();
		}
		json = mapper.writeValueAsString(current);
		return Response.ok(json).build();
	}
	
	
	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		System.out.println("login - provera na serverskoj strani");
		//current sme da bude null, tj mora biti
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		//moze imati null podatke zato sto se tek loguje pa 
		//mu ne zanamo nista sem email i lozinke
		if(p.getEmail() == null || p.getPassword() == null) {
			return Response.serverError().entity("Access denied!").build();
		}
		boolean ind = false;
		for (User k : getUsers().getUsersMap().values()) {
			if(k.getEmail().equals(p.getEmail()) && k.getPassword().equals(p.getPassword()))
			{
				ind = true;
				current = k;
				break;
			}	
		}
		if(ind) {
			request.getSession().setAttribute("current", current);
			ctx.setAttribute("currentUser", current);
		}
		else {
	        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
		}
		System.out.println("TRENUTNO PRIJAVLJENI JE "+getCurrent().getEmail());
		json = mapper.writeValueAsString(getCurrent());
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/logout")
	public void logout() {
		System.out.println("logout - podesavanje na serverskoj strani");
		//nema response nije me briga
		setCurrent();
	}
	
	@POST
	@Path("/addUser")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response adduserSA(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		System.out.println("dodavanje korisnika na serverskoj strani");
		//super admin moze da popuni samo					admin moze da popuni samo 
		//email, sifru, ime, prezime i organizaciju			email, sifru, ime, prezime, organizaciju dobija od admina koji je trenutno prijavljen
		//za tip moze da odabere admin ili korisnik			za tip moze da odabere admin ili korisnik
		
		//provera validnosti podataka
		Users us = new Users();
		Organisation org = new Organisation();
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.serverError().entity("Access denied!").build();
		}
		if(p.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.NOT_FOUND).entity("User has null fields!").build();
		}
		if(current.getRole() == RoleType.Admin) {
			org = current.getOrganisation();
			us = getAdminUsers();
		}
		else if(current.getRole() == RoleType.SuperAdmin){
			org = getOrganisations().getOrganisationsMap().get(p.getOrganisation().getName());
			us = getUsers();
		}
		else { //ako je user obican ne sme imati access ovoj metodi
			return Response.serverError().entity("Access denied!").build();
		}
		if(us.checkUser(p)) {
			return null;
		}
		
		p.setOrganisation(org);
		getOrganisations().getOrganisationsMap().get(org.getName()).addUser(p);
		us.addUser(p);
		
		ctx.setAttribute("users", us);
		if(current.getRole() == RoleType.SuperAdmin) {
			ctx.setAttribute("organisations", getOrganisations());
		}
		else if(current.getRole() == RoleType.Admin) {
			ctx.setAttribute("organisation", org);
		}
		json = mapper.writeValueAsString(p);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/changeUser")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUser(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		System.out.println("dodavanje korisnika na serverskoj strani SuperAdmin");
		//super admin moze da popuni samo 					admin moze da popuni samo korisnike u njegovoj organizaciji
		//sifru, ime, prezime i odabere tip za izmenu		sifru, ime, prezime i odabere tip za izmenu
		//EMAIL I ORGANISATION NE MOZE						EMAIL I ORGANISATION NE MOZE
		
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null || current.getRole() == RoleType.User) {
			return Response.serverError().entity("Access denied!").build();
		}
		if(p.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.NOT_FOUND).entity("User has null fields!").build();
		}
		Users us = new Users();
		if(current.getRole() == RoleType.Admin) {
			us = getAdminUsers();
		}
		else if(current.getRole() == RoleType.SuperAdmin){
			us = getUsers();
		}
		else { //ako je user obican ne sme imati access ovoj metodi
			return Response.serverError().entity("Access denied!").build();
		}
		if(us.userChanged(p)) {
			return Response.serverError().entity("Access denied!").build();
		}
		//provera validnosti podataka
		if(!us.checkUser(p)) {
			json = mapper.writeValueAsString(new User());
			return Response.ok(json).build();
		}
		us.setUserValues(p);
		ctx.setAttribute("users", us);
		json = mapper.writeValueAsString(p);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/deleteUser")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		System.out.println("brisanje korisnika - provera na serverskoj strani");
		//admin i super admin mogu da brisu korisnike
		//naravno one koji su njima vidljivi
		
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null || current.getRole() == RoleType.User) {
			return Response.serverError().entity("Access denied!").build();
		}
		if(p.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.NOT_FOUND).entity("User has null fields!").build();
		}
		Users us = new Users();
		if(current.getRole() == RoleType.Admin) {
			us = getAdminUsers();
		}
		else {
			us = getUsers();
		}
		if(us.userChanged(p)) {
			return Response.serverError().entity("Access denied!").build();
		}
		if(!us.checkUser(p)) {
			json = mapper.writeValueAsString(new User());
			return Response.ok(json).build();
		}
		us.removeUser(p);
		ctx.setAttribute("users", us);
		json = mapper.writeValueAsString(p);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/editProfile")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response editProfile(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		System.out.println("izmena korisnika - provera na serverskoj strani");
		//moze da se menja email, ime, prezime, sifra
		
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.serverError().entity("Access denied!").build();
		}
		if(p.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.NOT_FOUND).entity("User has null fields!").build();
		}
		Users us = getUsers();
		//validacija
		if(!p.getEmail().equals(current.getEmail())) { // && !us.checkUser(p)
			json = mapper.writeValueAsString(new User());
			return Response.ok(json).build();
		}
		current.setEmail(p.getEmail());
		current.setPassword(p.getPassword());
		current.setName(p.getName());
		current.setSurname(p.getSurname());
		us.changeUser(current,p);
		ctx.setAttribute("curent", current);
		ctx.setAttribute("users", us);
		json = mapper.writeValueAsString(current);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/getOrgs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrgs() throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException{
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current.getEmail() == null || current.getRole() == RoleType.User) {
			return Response.serverError().entity("Access denied!").build();
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
	
	@GET
	@Path("/getUserType")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserType() throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		User current = getCurrent();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.serverError().entity("Access denied!").build();
		}
		json = mapper.writeValueAsString(current.getRole());
		return Response.ok(json).build();
	}
	
	private Users getUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("uslo u getUsers");
		Users users = (Users) ctx.getAttribute("users");
		if(users == null){
			System.out.println("mora biti null");
			users = new Users(ctx.getRealPath(""));
			users.print();
			Organisations o = new Organisations(ctx.getRealPath(""));
			System.out.println("sad organizacije");
			for (Organisation oo : o.getOrganisationsMap().values()) {
				System.out.println(oo.getName() + "    " + oo.getDescription());
			}
			ctx.setAttribute("organisations", o);
			for (Organisation org : o.getOrganisationsMap().values()) {
				System.out.println("iteracija org " + org.getDescription());
				for (String user : org.getUsers()) {
					System.out.println("ima users "+ user);
					if(users.getUsersMap().containsKey(user)) {
						System.out.println("sadrzimo tog usera " + user);
						users.setOrgForUser(user,org);
					}
				}
			}
			users.print();
			ctx.setAttribute("users",users);
			System.out.println("postavi sve usere na ctx");
			Users novi = (Users) ctx.getAttribute("users");
			novi.print();
		}
		return users;
	}
	
	private Users getAdminUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null){
			User current = getCurrent();
			ctx.setAttribute("organisation", current.getOrganisation());
			System.out.println("ADMIN ORGANIZACIJA"+ctx.getAttribute("organisation"));
			users = new Users(ctx.getRealPath(""),current);
			Organisations o = new Organisations();
			o.getOrganisationsMap().put(current.getOrganisation().getName(), current.getOrganisation());
			ctx.setAttribute("organiations", o);
			for (User user : users.getUsersMap().values()) {
				user.setOrganisation((Organisation)ctx.getAttribute("organisation"));
			}
			request.getSession().setAttribute("current", current);
			ctx.setAttribute("users",users);
		}
		return users;
	}
	
	private Users getCurrentUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null){
			User current = getCurrent();
			Organisations o = new Organisations(ctx.getRealPath(""));
			ctx.setAttribute("organisation", o.getOrganisationsMap().get(current.getOrganisation().getName()));
			current.setOrganisation((Organisation)ctx.getAttribute("organisation"));
			
			users = new Users();
			users.getUsersMap().put(current.getEmail(), current);
			
			request.getSession().setAttribute("current", current);
			ctx.setAttribute("users",users);
		}
		return users;
	}
	
	private User getCurrent() {
		User sc = (User) request.getSession().getAttribute("current");
		if (sc == null) {
			sc = new User();
			request.getSession().setAttribute("current", sc);
		} 
		return sc;
	}
	
	private void setCurrent() {
		request.getSession().setAttribute("current", null);
		ctx.setAttribute("currentUser", null);
	}
	
	private Organisations getOrganisations() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Organisations o = (Organisations) ctx.getAttribute("organisations");
		return o;
	}
}
