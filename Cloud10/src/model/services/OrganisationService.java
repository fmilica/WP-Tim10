package model.services;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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

import model.Disc;
import model.Organisation;
import model.collections.Discs;
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
	
	@GET
	@Path("/getOrganisation")
	@Produces(MediaType.APPLICATION_JSON)
	public Organisation getOrganisation() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Organisation org = (Organisation) ctx.getAttribute("organisation");
		return org;
	}
	
	@GET
	@Path("/getFreeDiscs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Disc> getFreeDiscs(Organisation o) {
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
		return freeDiscs;
	}
	
	@POST
	@Path("/addOrganisation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Organisation addOrganisation(Organisation o) {
		
		Organisations orgs = getOrgs();
		if(orgs.checkOrg(o)) {
			return new Organisation();
		}
		o.setUsers(new ArrayList<String>());
		o.setResources(new ArrayList<String>());
		orgs.getOrganisationsMap().put(o.getName(), o);
		ctx.setAttribute("organisations", orgs);
		
		return o;
	}
	
	@POST
	@Path("/changeOrganisation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Organisation changeOrganisation(Organisation o) {
		
		Organisations orgs = getOrgs();
		if(!orgs.checkOrg(o)) {
			return new Organisation();
		}
		
		orgs.changeOrg(o);
		ctx.setAttribute("organisations", orgs);
		return o;
	}
	
	
	
	private Organisations getOrgs() {
		Organisations orgs = (Organisations) ctx.getAttribute("organisations");
		return orgs;
	}
}
