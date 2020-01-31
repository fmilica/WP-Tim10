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
	public Collection<Organisation> getOrganisations() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Organisations orgs = (Organisations) ctx.getAttribute("organisations");
		return orgs.getOrganisationsMap().values();
	}
	
	@GET
	@Path("/getOrganisation")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Organisation> getOrganisation() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Organisations o = getOrgs();
		for (Organisation org : o.getOrganisationsMap().values()) {
			System.out.println(org.getDescription() + " "+ org.getName());
		}
		return o.getOrganisationsMap().values();
	}
	
	@POST
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
		if(orgs.checkOrg(o.getName())) {
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
	public Organisation changeOrganisation(OrganisationWrapper o) {
		System.out.println("izmena organizacije - validacija na serveru");
		System.out.println(o);
		Organisations orgs = getOrgs();
		for (Organisation oo : orgs.getOrganisationsMap().values()) {
			System.out.println(oo.getName() + " - " + oo.getDescription());
		}
		if(!orgs.checkOrg(o.getOldName())) {
			System.out.println("pristup nepostojecem");
			return new Organisation();
		}
		if(orgs.checkOrg(o.getName()) && !o.getOldName().equals(o.getName())) {
			System.out.println("postoji vec ovakav");
			return new Organisation();
		}
		System.out.println("0");
		Organisation org = getOrgs().getOrganisationsMap().get(o.getOldName());
		System.out.println("0");
		org.setName(o.getName());
		System.out.println("0");
		org.setDescription(o.getDescription());
		System.out.println("0");
		org.setLogo(o.getLogo());
		System.out.println("0");
		ctx.setAttribute("organisations", orgs);
		System.out.println("0");
		System.out.println(org);
		return org;
	}
	
	private Organisations getOrgs() {
		User current = (User) ctx.getAttribute("currentUser");
		System.out.println(current + "    " + current.getOrganisation().getDescription());
		Organisations orgs = new Organisations();
		System.out.println(current.getOrganisation().getDescription());
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
