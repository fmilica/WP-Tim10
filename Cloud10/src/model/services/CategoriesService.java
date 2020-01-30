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

import model.Category;
import model.User;
import model.collections.Categories;
import model.enums.RoleType;

@Path("/categories")
public class CategoriesService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/getCategories")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCategories() throws JsonProcessingException {
		User current = getCurrent();
		// uh
		// ovo je zajebano
		// ja treba da pristupim kategorijama i kao admin
		// da ih loudujem
		// ali ne smem da pristupim izmeni, dodavanju ili slicno
		// ali kao admin moram da mogu da dodam virtuelnu masinu sa kategorijom
		// pa me moras spustiti
		// a onda ne znam kad da mu zabranjujes
		// hmmm...
		// ne znam doista
		/*
		if(current.getRole() != RoleType.SuperAdmin) {
			return Response.serverError().entity("Access denied!").build();
		}*/
		Categories cats = getCategories();
		if(cats == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Categories not found").build();
	    }
		ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(cats.getCategoriesMap().values());
	    System.out.println("load katrgorija\n" + json);
	    return Response.ok(json).build();
	}
	
	@POST
	@Path("/addCategory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCategory(Category c) throws JsonProcessingException {
		User current = getCurrent();
		System.out.println("trenutni je " + current);
		if(current.getRole() == RoleType.User) {
			System.out.println("nije super admin");
			return Response.serverError().entity("Access denied!").build();
		}
		if(c.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.NOT_FOUND).entity("Category has null fields!").build();
		}
		
		Categories cats = getCategories();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(cats.getCategoriesMap().containsKey(c.getName())) {
			System.out.println("kaze da sadrzi ovu kategoriju vec");
			json = mapper.writeValueAsString(new Category());
			System.out.println("ovo je json koji vrati "+json);
			return Response.ok(json).build();
		}
		cats.addItem(c);
		ctx.setAttribute("categories", cats);
		json = mapper.writeValueAsString(c);
		System.out.println("ovde bi trbalo da je sve okej");
		System.out.println("ovo je json koji vrati "+json);
	    return Response.ok(json).build();
	}
	
	private Categories getCategories() {
		Categories cats = (Categories)ctx.getAttribute("categories");
		if (cats == null) {
			cats = new Categories(ctx.getRealPath(""));
			ctx.setAttribute("categories", cats);
		}
		return cats;
	}
	
	private User getCurrent() {
		return (User)ctx.getAttribute("currentUser");
	}
}
