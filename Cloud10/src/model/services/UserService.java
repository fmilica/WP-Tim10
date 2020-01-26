package model.services;

import java.io.FileNotFoundException;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
	public Collection<User> load() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("ucitaj korisnike");
		User current = getCurrent();
		if(current.getRole() == RoleType.Admin){
			return getAdminUsers().getUsersMap().values();
		}
		else if(current.getRole() == RoleType.User){
			return getCurrentUsers().getUsersMap().values();
		}
		else {
			return (getUsers()).getUsersMap().values();
		}
	}
	
	@GET
	@Path("/checkCurrent")
	@Produces(MediaType.APPLICATION_JSON)
	public User checkCurrent() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("---------validacija----------");
		return getCurrent();
	}
	
	
	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public String login(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("provera na serverskoj strani");
		User current = new User();
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
		}
		else {
			return "greska404";
		}
		System.out.println("TRENUTNO PRIJAVLJENI JE "+getCurrent().getEmail());
		return getCurrent().getEmail();
	}
	
	@POST
	@Path("/logout")
	public void logout() {
		setCurrent();
	}
	
	@POST
	@Path("/adduserSA")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public User adduserSA(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("dodavanje korisnika na serverskoj strani SuperAdmin");
		//super admin moze da popuni samo 
		//email, sifru, ime, prezime i organizaciju
		//za tip moze da odabere admin ili korisnik
		
		//provera validnosti podataka
		Users us = getUsers();
		if(!us.checkUser(p)) {
			return new User();
		}
		
		Organisation org = getOrganisations().getOrganisationsMap().get(p.getOrganisation().getName());
		p.setOrganisation(org);
		org.addUser(p);
		us.addUser(p);
		ctx.setAttribute("users", us);
		ctx.setAttribute("organisations", getOrganisations());
		for (Organisation o : getOrganisations().getOrganisationsMap().values()) {
			System.out.println(o);
		}
		return p;
	}
	
	@POST
	@Path("/adduserA")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public User adduserA(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("dodavanje korisnika na serverskoj strani SuperAdmin");
		//super admin moze da popuni samo 
		//email, sifru, ime, prezime
		//organizaciju dobija od admina koji je trenutno prijavljen
		//za tip moze da odabere admin ili korisnik
		
		//provera validnosti podataka
		Users us = getUsers();
		if(us.checkUser(p)) {
			return new User();
		}
		
		Organisation org = (Organisation)ctx.getAttribute("organisation");
		p.setOrganisation(org);
		org.addUser(p);
		us.addUser(p);
		ctx.setAttribute("users", us);
		ctx.setAttribute("organisation", org);
		System.out.println((Organisation)ctx.getAttribute("organisation"));
		return p;
	}
	
	@POST
	@Path("/changeuserSA")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public User changeuserSA(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("dodavanje korisnika na serverskoj strani SuperAdmin");
		//super admin moze da popuni samo 
		//sifru, ime, prezime i odabere tip za izmenu
		//EMAIL I ORGANISATION NE MOZE
		
		//provera validnosti podataka
		Users us = getUsers();
		if(!us.checkUser(p)) {
			return new User();
		}
		
		us.setUserValues(p);
		ctx.setAttribute("users", us);
		
		return p;
	}
	
	@POST
	@Path("/changeuserA")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public User changeuserA(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("dodavanje korisnika na serverskoj strani SuperAdmin");
		//admin moze da popuni samo korisnike u njegovoj organizaciji
		//sifru, ime, prezime i odabere tip za izmenu
		//EMAIL I ORGANISATION NE MOZE
		
		//provera validnosti podataka
		Users us = getUsers();
		if(!us.checkUser(p)) {
			return new User();
		}
		
		us.setUserValues(p);
		ctx.setAttribute("users", us);
		
		return p;
	}
	
	@POST
	@Path("/deleteuser")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public User deleteuser(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		//admin i super admin mogu da brisu korisnike
		//naravno one koji su njima vidljivi
		Users us = getUsers();
		if(us.checkUser(p)) {
			us.removeUser(p);
			ctx.setAttribute("users", us);
		}
		
		return new User();
	}
	
	@POST
	@Path("/editprofile")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public User editprofile(User p) {
		User current = getCurrent();
		current.setEmail(p.getEmail());
		current.setPassword(p.getPassword());
		current.setName(p.getName());
		current.setSurname(p.getSurname());
		ctx.setAttribute("curent", current);
		return current;
	}
	
	@GET
	@Path("/getOrgs")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Organisation> getOrgs() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		User current = getCurrent();
		Organisations orgs = new Organisations();
		if(current.getRole() == RoleType.SuperAdmin) {
			orgs = getOrganisations();
		}
		else {
			orgs.getOrganisationsMap().put(current.getOrganisation().getName(), current.getOrganisation());
		}
		return orgs.getOrganisationsMap().values();
	}
	
	@GET
	@Path("/getUserType")
	@Produces(MediaType.APPLICATION_JSON)
	public RoleType getUserType() {
		User current = getCurrent();
		return current.getRole();
	}
	
	private Users getUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null){
			users = new Users(ctx.getRealPath(""));
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
		}
		return users;
	}
	
	private Users getAdminUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null){
			User current = getCurrent();
			users = new Users(ctx.getRealPath(""),current);
			Organisations o = new Organisations(ctx.getRealPath(""));
			ctx.setAttribute("organisation", o.getOrganisationsMap().get(current.getOrganisation().getName()));
			current.setOrganisation((Organisation)ctx.getAttribute("organisation"));
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
		User u = new User();
		request.getSession().setAttribute("current", u);
	}
	
	private Organisations getOrganisations() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Organisations o = (Organisations) ctx.getAttribute("organisations");
		return o;
	}
}
