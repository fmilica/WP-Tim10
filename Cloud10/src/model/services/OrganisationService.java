package model.services;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import model.Disc;
import model.Organisation;
import model.User;
import model.collections.Discs;
import model.collections.Organisations;
import model.enums.RoleType;
import model.wrappers.OrganisationWrapper;

@Path("/organisations")
public class OrganisationService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/getOrganisations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganisations() throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		User current = (User)ctx.getAttribute("currentUser");
		if(current.getEmail() == null || current.getRole() != RoleType.SuperAdmin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		Organisations orgs = (Organisations) ctx.getAttribute("organisations");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(orgs.getOrganisationsMap().values());
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/getOrganisation")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganisation() throws JsonIOException, JsonSyntaxException, FileNotFoundException, JsonProcessingException {
		User current = (User)ctx.getAttribute("currentUser");
		if(current.getEmail() == null || current.getRole() != RoleType.Admin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		Organisations o = getOrgs();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(o.getOrganisationsMap().values());
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/getFreeDiscs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFreeDiscs(Organisation o) throws JsonProcessingException {
		User current = (User) ctx.getAttribute("currentUser");
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() != RoleType.SuperAdmin) {
			if(!current.getOrganisation().getName().equals(o.getName()) && current.getRole() == RoleType.User) {
				return Response.status(Response.Status.FORBIDDEN).entity("Access denied! Users can't call this method!").build();
			}
			else if(!current.getOrganisation().getName().equals(o.getName()) && current.getRole() == RoleType.Admin) {
				return Response.status(Response.Status.FORBIDDEN).entity("Access denied! Admins can't call this method!").build();
			}
		}
		if(o == null || o.getName() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No Organisation sent!").build();
		}
		Organisations orgs = getOrgs();
		if(!orgs.getOrganisationsMap().containsKey(o.getName())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Organisation with specified name does't exists!").build();
		}
		ObjectMapper mapper = new ObjectMapper();
		String json;
		ArrayList<Disc> freeDiscs = new ArrayList<Disc>();
		Discs discs = (Discs)ctx.getAttribute("discs");
		// je iz te organizacije
		for (String rName : o.getResources()) {
			// je disk
			if (discs.getDiscsMap().containsKey(rName)) {
				Disc d = discs.getDiscsMap().get(rName);
				if(d.getVm() == null) {
					freeDiscs.add(d);
				}
			}
		}
		json = mapper.writeValueAsString(freeDiscs);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/addOrganisation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrganisation(Organisation o) throws JsonProcessingException {
		User current = (User) ctx.getAttribute("currentUser");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current.getEmail() == null || current.getRole() != RoleType.SuperAdmin) {
			System.out.println("nedozvoljen pristup");
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(o == null) {
			System.out.println("ima null");
			return Response.status(Response.Status.BAD_REQUEST).entity("No organisation sent!").build();
		}
		if(o.hasNull()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Organiation has null fields!").build();
		}
		Organisations orgs = getOrgs();
		if(orgs.checkOrg(o.getName())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Organisation with specified name already exists!").build();
		}
		o.setUsers(new ArrayList<String>());
		o.setResources(new ArrayList<String>());
		o.setLogo(o.getLogo());
		orgs.getOrganisationsMap().put(o.getName(), o);
		ctx.setAttribute("organisations", orgs);
		json = mapper.writeValueAsString(o);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/changeOrganisation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeOrganisation(OrganisationWrapper o) throws JsonProcessingException {
		System.out.println("izmena organizacije - validacija na serveru");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		User current = (User)ctx.getAttribute("currentUser");
		if(current.getEmail() == null || current.getRole() == RoleType.User) {
			System.out.println("nema pristup");
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(o == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		if(o.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.BAD_REQUEST).entity("User has null fields!").build();
		}
		Organisations orgs = new Organisations();
		if(current.getRole() == RoleType.SuperAdmin) {
			orgs = (Organisations) ctx.getAttribute("organisations");
		}
		else if(current.getRole() == RoleType.Admin) {
			ctx.setAttribute("organisation", current.getOrganisation());
			orgs.getOrganisationsMap().put(current.getOrganisation().getName(),current.getOrganisation());
		}
		if(!orgs.checkOrg(o.getOldName())) {
			System.out.println("pristup nepostojecem");
			return Response.status(Response.Status.BAD_REQUEST).entity("Can't edit organisation that doesn't exist!").build();
		}
		if(orgs.checkOrg(o.getName()) && !o.getOldName().equals(o.getName())) {
			System.out.println("postoji vec ovakav");
			return Response.status(Response.Status.BAD_REQUEST).entity("New organisation name already exists!").build();
		}
		System.out.println(o.getLogo());
		Organisation org = orgs.getOrganisationsMap().get(o.getOldName());
		System.out.println(org);
		org.setName(o.getName());
		org.setDescription(o.getDescription());
		org.setLogo(o.getLogo());
		ctx.setAttribute("organisations", orgs);
		json = mapper.writeValueAsString(org);
		return Response.ok(json).build();
	}
	
	private Organisations getOrgs() {
		User current = (User) ctx.getAttribute("currentUser");
		Organisations orgs = new Organisations();
		if(current.getRole() == RoleType.SuperAdmin) {
			orgs = (Organisations) ctx.getAttribute("organisations");
		}
		else if(current.getRole() == RoleType.Admin) {
			System.out.println(current.getOrganisation());
			ctx.setAttribute("organisation", current.getOrganisation());
			orgs.getOrganisationsMap().put(current.getOrganisation().getName(),current.getOrganisation());
		}
		return orgs;
	}
}
