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

@Path("/organisations")
public class OrganisationService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/getOrganisations")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Organisation> getOrganisations() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Organisations orgs = (Organisations) ctx.getAttribute("organisations");
		return orgs.getOrganisationsMap().values();
	}
	
	@POST
	@Path("/load")
	@Consumes(MediaType.APPLICATION_JSON)
	public void load(User u) {
		
	}
	
}
