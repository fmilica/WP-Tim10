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
import model.VirtualMachine;
import model.collections.Categories;
import model.collections.VirtualMachines;
import model.enums.RoleType;
import model.wrappers.CategoryWrapper;

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
		//RESENO sada???
		if(current.getEmail() == null) { //current.getRole() == RoleType.User ||  jer i njemu mora da se ucita getCategories valjda zbog js-a
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		Categories cats = getCategories();
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
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() != RoleType.SuperAdmin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!!").build();
		}
		if(c == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No category sent!").build();
		}
		if(c.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.BAD_REQUEST).entity("Category has null fields!").build();
		}
		
		Categories cats = getCategories();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(cats.getCategoriesMap().containsKey(c.getName())) {
			json = mapper.writeValueAsString(new Category());
			return Response.status(Response.Status.BAD_REQUEST).entity("Category with specified name already exists!").build();
		}
		cats.addItem(c);
		ctx.setAttribute("categories", cats);
		cats.writeCategories(ctx.getRealPath(""));
		json = mapper.writeValueAsString(c);
	    return Response.ok(json).build();
	}
	
	@POST
	@Path("/changeCategory")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeCategory(CategoryWrapper cw) throws JsonProcessingException {
		System.out.println("dodavanje kategorije na serverskoj strani ");
		User current = getCurrent();
		//samo admin ima pristup 
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() != RoleType.SuperAdmin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if(cw == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No category sent!").build();
		}
		Category c1 = new Category(cw);
		//ako mu je neki atribut null
		if(c1.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
		}
		
		Categories cats = getCategories();
		
		//ako mu staro ime ne postoji u kategorijama
		if(!cats.getCategoriesMap().containsKey(cw.getOldName())) {
			System.out.println("izmena nepostojeceg");
			return Response.status(Response.Status.BAD_REQUEST).entity("Can't edit category that doesn't exist!").build();
		}
		
		if(cats.getCategoriesMap().containsKey(cw.getName())) {
			System.out.println("izmena na postojeceg");
			if (!cw.getOldName().equals(cw.getName())) {
				// promeni ime
				// brisemo sa starim imenom iz mape
				if (cats.getCategoriesMap().containsKey(cw.getOldName())) {
					Category c = cats.getCategoriesMap().get(cw.getOldName());
					cats.getCategoriesMap().remove(cw.getOldName());
					c.setCoreNum(cw.getCoreNum());
					c.setGPU(cw.getGpu());
					c.setRAM(cw.getRam());
					c.setName(cw.getName());
					cats.getCategoriesMap().put(cw.getName(), c);
				}
				else {
					return Response.status(Response.Status.BAD_REQUEST).entity("Old category name doesn't exist!").build();
				}
			}
			// isto je staro i novo ime
			// menjamo vrednosti
			// ako postoji u mapi
			if (cats.getCategoriesMap().containsKey(cw.getName())) {
				Category c = cats.getCategoriesMap().get(cw.getName());
				c.setCoreNum(cw.getCoreNum());
				c.setGPU(cw.getGpu());
				c.setRAM(cw.getRam());
			}
			else {
				return Response.status(Response.Status.BAD_REQUEST).entity("Old category name doesn't exist!").build();
			}
			return Response.ok().build();
		}
		if (!cw.getOldName().equals(cw.getName())) {
			// promeni ime
			// brisemo sa starim imenom iz mape
			if (cats.getCategoriesMap().containsKey(cw.getOldName())) {
				Category c = cats.getCategoriesMap().get(cw.getOldName());
				cats.getCategoriesMap().remove(cw.getOldName());
				c.setCoreNum(cw.getCoreNum());
				c.setGPU(cw.getGpu());
				c.setRAM(cw.getRam());
				c.setName(cw.getName());
				cats.getCategoriesMap().put(cw.getName(), c);
			}
			else {
				return Response.status(Response.Status.BAD_REQUEST).entity("Old category name doesn't exist!").build();
			}
		}
		// isto je staro i novo ime
		// menjamo vrednosti
		// ako postoji u mapi
		if (cats.getCategoriesMap().containsKey(cw.getName())) {
			Category c = cats.getCategoriesMap().get(cw.getName());
			c.setCoreNum(cw.getCoreNum());
			c.setGPU(cw.getGpu());
			c.setRAM(cw.getRam());
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Old category name doesn't exist!").build();
		}
		ctx.setAttribute("categories", cats);
		cats.writeCategories(ctx.getRealPath(""));
		//json = mapper.writeValueAsString(c);
		return Response.ok().build();
	}
	
	
	@POST
	@Path("/deleteCategory")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCategory(Category c) throws JsonProcessingException {
		System.out.println("dodavanje kategorije na serverskoj strani ");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		User current = getCurrent();
		//samo admin ima pristup 
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() != RoleType.SuperAdmin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if(c == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No category sent!").build();
		}
		if(c.getName() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No category sent!").build();
		}
		//ako mu je neki atribut null
		if(c.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.BAD_REQUEST).entity("Category has null fields!").build();
		}
		
		VirtualMachines vms = (VirtualMachines) ctx.getAttribute("vms");
		if(vms != null) {
			for (VirtualMachine vm : vms.getVirtualMachinesMap().values()) {
				if(vm.getCategory().getName().equals(c.getName())) {
					return Response.status(Response.Status.BAD_REQUEST).entity("Category assigned to virtual machine can't be deleted!").build();
				}
			}
		}
		Categories cats = getCategories();
		
		//ako novo ime ne postoji u kategorijama
		if(!cats.getCategoriesMap().containsKey(c.getName())) {
			System.out.println("brisanje nepostojeceg");
			return Response.status(Response.Status.BAD_REQUEST).entity("Category with specified name doesn't exist!").build();
		}
		cats.remove(c);
		ctx.setAttribute("categories", cats);
		cats.writeCategories(ctx.getRealPath(""));
		json = mapper.writeValueAsString(c);
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
