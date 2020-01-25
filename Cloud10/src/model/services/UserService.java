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

import model.User;
import model.collections.Users;

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
		return (getUsers()).getUsersMap().values();
	}
	

	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public String login(User p) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("provera na serverskoj strani");
		System.out.println(p.getEmail() + "         " + p.getPassword());
		boolean ind = false;
		for (User k : getUsers().getUsersMap().values()) {
			//System.out.println(pr.getName() + " " + pr.getPrice());
			System.out.println(k.getEmail() + "    " + k.getPassword());
			if(k.getEmail().equals(p.getEmail()) && k.getPassword().equals(p.getPassword()))
			{
				ind = true;
				break;
			}	
		}
		System.out.println();
		if(ind) {
			request.getSession().setAttribute("trenutni", p);
		}
		else {
			return "greska404";
		}
		System.out.println("TRENUTNO PRIJAVLJENI JE "+getCurrent().getEmail());
		return getCurrent().getEmail();
	}
	
	private Users getUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null){
			users = new Users(ctx.getRealPath(""));
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
}
