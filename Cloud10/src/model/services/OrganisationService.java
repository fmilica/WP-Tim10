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

import model.Activity;
import model.Disc;
import model.Organisation;
import model.User;
import model.VirtualMachine;
import model.collections.Discs;
import model.collections.Organisations;
import model.collections.VirtualMachines;
import model.enums.DiscType;
import model.enums.RoleType;
import model.wrappers.BillDates;
import model.wrappers.BillTypeWrapper;
import model.wrappers.BillWrapper;
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
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() != RoleType.SuperAdmin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
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
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() != RoleType.Admin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
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
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
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
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() != RoleType.SuperAdmin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if(o == null) {
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
		orgs.writeOrganisations(ctx.getRealPath(""));
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
		if(current == null) {
			System.out.println("nema pristup");
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			System.out.println("nema pristup");
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() == RoleType.User) {
			System.out.println("nema pristup");
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if(o == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No organisation sent!").build();
		}
		if(o.hasNull()) {
			System.out.println("ima kao neki null");
			return Response.status(Response.Status.BAD_REQUEST).entity("Organisation has null fields!").build();
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
		// izbacujemo iz organizacija onu sa starim imenom
		orgs.getOrganisationsMap().remove(o.getOldName());
		System.out.println(org);
		org.setName(o.getName());
		org.setDescription(o.getDescription());
		org.setLogo(o.getLogo());
		// ubacujemo ovu sa novim imenom
		orgs.getOrganisationsMap().put(org.getName(), org);
		ctx.setAttribute("organisations", orgs);
		orgs.writeOrganisations(ctx.getRealPath(""));
		json = mapper.writeValueAsString(org);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/calculateBill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calculateBill(BillDates bd) throws JsonProcessingException {
		User current = (User)ctx.getAttribute("currentUser");
		// pristup //
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getRole() != RoleType.Admin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		Organisations orgs = (Organisations)ctx.getAttribute("organisations");
		Organisation o = orgs.getOrganisationsMap().get(current.getOrganisation().getName());
		
		VirtualMachines vms = (VirtualMachines)ctx.getAttribute("vms");
		Discs discs = (Discs)ctx.getAttribute("discs");
		
		BillWrapper bw = new BillWrapper();
		
		// validacija datuma //
		if (!bd.formatDate()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Dates not formatted correctly!").build();
		}
		
		double total;
		double totalVM = 0;
		double totalDisc = 0;
		for (String resource : o.getResources()) {
			if (vms.getVirtualMachinesMap().containsKey(resource)) {
				// virtuelna masina je
				// racunanje //
				VirtualMachine vm = vms.getVirtualMachinesMap().get(resource);
				double hourPrice = 0;
				// 25e * coreNum
				double devide = 25.0 / 30.0 / 24.0;
				double cores = vm.getCoreNum() * devide;
				// 15e * 1GB RAM
				devide = 15.0 / 30.0 / 24.0;
				double rams = vm.getRAM() * devide;
				// 1e * GPUCore
				devide = 1.0 / 30.0 / 24.0;
				double gpus = vm.getGPU() * devide;
				hourPrice = cores + rams + gpus;
				
				// koliko sati je bila aktivna
				double hoursActive = 0;
				for (Activity a : vm.getActivities()) {
					if (a.getOff() != null) {
						// zavrsena aktivnost
						// samo njih uracunavamo
						if (a.getOnTime().after(bd.getStartDate())
								&& a.getOffTime().before(bd.getEndDate())) {
							// aktivnost se nalazi u zadatom opsegu
							long diffMili = a.getOffTime().getTime() - a.getOnTime().getTime();
							double diffHours = Math.ceil(diffMili / (60 * 60 * 1000));
							hoursActive += diffHours;	
						}
					}
				}
				// ukupno radjeno ikada
				double thisVM = Math.round((hourPrice * hoursActive) * 100.0) / 100.0;
				bw.getVms().add(new BillTypeWrapper(resource, thisVM));
				totalVM += thisVM;
			} else {
				// disk je
				Disc d = discs.getDiscsMap().get(resource);
				// racunanje //
				long diffMili = bd.getEndDate().getTime() - bd.getStartDate().getTime();
				double diffDays = Math.ceil(diffMili / (24 * 60 * 60 * 1000));
				
				double devide;
				double dayPrice;
				
				if (d.getType() == DiscType.HDD) {
					// 0.1e GB HDD
					devide = 0.1/30;
					dayPrice = d.getCapacity() * devide;
				} else {
					// 0.3e GB SSD
					devide = 0.3/30;
					dayPrice = d.getCapacity() * devide;
				}
				double thisDisc = Math.round((dayPrice * diffDays) * 100.0) / 100.0;
				bw.getDiscs().add(new BillTypeWrapper(resource, thisDisc));
				totalDisc += thisDisc;
			}
		}
		total = totalVM + totalDisc;
		total = Math.round((total) * 100.0) / 100.0;
		bw.setTotal(total);
		ObjectMapper mapper = new ObjectMapper();
		String bill = mapper.writeValueAsString(bw);
		return Response.ok(bill).build();
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
