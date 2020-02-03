package model.services;

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

import model.Disc;
import model.Organisation;
import model.User;
import model.VirtualMachine;
import model.collections.Discs;
import model.collections.Organisations;
import model.collections.VirtualMachines;
import model.enums.RoleType;
import model.wrappers.DiscWrapper;

@Path("/discs")
public class DiscsService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/loadDiscs")
	public void loadDiscs() {
		getDiscs();
	}
	
	@GET
	@Path("/getAllDiscs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDiscs() throws JsonProcessingException {
		User current = (User)request.getSession().getAttribute("current");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getRole() == RoleType.SuperAdmin) {
			json = mapper.writeValueAsString(getDiscs().getDiscsMap().values());
			return Response.ok(json).build();
		} else {
			Organisation o = current.getOrganisation();
			ArrayList<Disc> orgDiscs = new ArrayList<Disc>();
			for (Disc d : getDiscs().getDiscsMap().values()) {
				if (d.getOrganisation().equals(o.getName())) {
					orgDiscs.add(d);	
				}
			}
			json = mapper.writeValueAsString(orgDiscs);
			return Response.ok(json).build();
		}
	}
	
	@POST
	@Path("/getFreeOrganDiscs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFreeOrganDiscs(Organisation organisation) throws JsonProcessingException {
		User current = (User)request.getSession().getAttribute("current");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(organisation == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No Organisation sent!").build();
		}
		if(organisation.getName() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No Organisation sent!").build();
		}
		ArrayList<Disc> organDiscs = new ArrayList<Disc>();
		Discs discs = getDiscs();
		for (Disc d : discs.getDiscsMap().values()) {
			if (d.getOrganisation() != null) {
				if (d.getOrganisation().equals(organisation.getName())) {
					if (d.getVm() == null) {
						organDiscs.add(d);	
					}
				}
			}
		}
		json = mapper.writeValueAsString(organDiscs);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/addDisc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDisc(Disc d) throws JsonProcessingException {
		User current = (User)request.getSession().getAttribute("current");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		System.out.println(d);
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if(d == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No Disc sent!").build();
		}
		if(d.hasNull()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Some of Disc fields are empty!").build();
		}
		Discs discs = getDiscs();
		// validacija na serverskoj strani
		// da li postoji vec disk sa unetim imenom
		if(!discs.discNameFree(d.getName())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Disc with specified name already exists!").build();
		}
		discs.addDisc(d);
		// provera da li postoji odabrana organizacija
		Organisations orgs = (Organisations)ctx.getAttribute("organisations");
		if (!orgs.getOrganisationsMap().containsKey(d.getOrganisation())) {
			// ne postoji uneta organizacija
			// bad request
			return Response.status(Response.Status.BAD_REQUEST).entity("Organisation with specified name doesn't exist!").build();
		}
		// postoji
		// dodajemo joj disk u listu resursa
		orgs.getOrganisationsMap().get(d.getOrganisation()).getResources().add(d.getName());
		if (d.getVm() != null && !d.getVm().equals("")) {
			VirtualMachines vms = (VirtualMachines)ctx.getAttribute("vms");
			// proveravamo da li postoji odabrana virtuelna masina
			if (!vms.getVirtualMachinesMap().containsKey(d.getVm())) {
				// ne postoji
				// bad request
				return Response.status(Response.Status.BAD_REQUEST).entity("VM with specified name doesn't exist!").build();
			}
			// postoji
			// dodajemo joj disk
			VirtualMachine vm = vms.getVirtualMachinesMap().get(d.getVm());
			vm.getDiscs().add(d.getName());
			vms.writeVMs(ctx.getRealPath(""));
		}
		// ne zeli da doda disk nijednoj virtuelnoj
		// slobodan je
		d.setVm(null);
		discs.writeDiscs(ctx.getRealPath(""));
		orgs.writeOrganisations(ctx.getRealPath(""));
		json = mapper.writeValueAsString(d);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/editDisc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editDisc(DiscWrapper dw) throws JsonProcessingException {
		User current = (User) ctx.getAttribute("currentUser");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if(current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if(current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if(dw == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		if(dw.hasNull()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Some of Disc fields are empty!").build();
		}
		Discs discs = getDiscs();
		// validacija na serverskoj strani
		// da li vec postoji disk sa uneetim NOVIM imenom
		if (!dw.getOldName().equals(dw.getNewName())) {
			// uneto novo ime
			// da li je zauzeto novouneto ime
			if(!discs.discNameFree(dw.getNewName())) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Disc with specified name already exists!").build();
			}
		}
		// PROVERA DA LI ORGANIZACIJA POSTOJI
		// KAO I SVA OSTALA GOVNA DAL POSTOJE
		// KOJA I SALJEMO I NE SALJEMO [EOWGWEMEGO
		Disc oldDisc = discs.getDiscsMap().get(dw.getOldName());
		discs.deleteDisc(oldDisc.getName());
		oldDisc.setName(dw.getNewName());
		oldDisc.setCapacity(dw.getCapacity());
		oldDisc.setType(dw.getType());
		System.out.println(dw.getVm());
		if (dw.getVm() != null && dw.getVm().trim().length() != 0) {
			VirtualMachines vms = (VirtualMachines)ctx.getAttribute("vms");
			// da li postoji vm koja je prosledjena
			if (vms.getVirtualMachinesMap().containsKey(dw.getVm())) {
				// postoji
				// dodelimo joj disk
				oldDisc.setVm(dw.getVm());
				vms.getVirtualMachinesMap().get(dw.getVm()).addDisc(oldDisc.getName());
				vms.writeVMs(ctx.getRealPath(""));
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity("VM with specified name doesn't exist!").build();
			}
		} else {
			// nije odabrao vm
			// mozda je bio zakacen za neku, a sada vise nece
			oldDisc.setVm(null);
			VirtualMachines vms = (VirtualMachines)ctx.getAttribute("vms");
			// da li se nalazi u nekoj vm
			for (VirtualMachine vm : vms.getVirtualMachinesMap().values()) {
				if (vm.getDiscs().contains(dw.getOldName())) {
					// brisemo ga iz njenih resursa
					vm.getDiscs().remove(dw.getOldName());
				}
			}
		}
		discs.addDisc(oldDisc);
		json = mapper.writeValueAsString(dw);
		discs.writeDiscs(ctx.getRealPath(""));
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/removeDisc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeDisc(Disc d) throws JsonProcessingException {
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
		if(d == null || d.getName() == null || d.getName().trim().length()==0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No disc sent!").build();
		}
		Discs discs = getDiscs();
		// provera na serverskoj strani
		if (!discs.discNameFree(d.getName())) {
			// brise dobar
			if(d.getVm() != null) {
				VirtualMachines vms = (VirtualMachines)ctx.getAttribute("vms");
				// proverava da li postoji takva vm
				VirtualMachine vm = vms.getVirtualMachinesMap().get(d.getVm());
				if (vm == null) {
					// ne postoji takva vm
					return Response.status(Response.Status.BAD_REQUEST).entity("VM with specified name doesn't exist!").build();
				}
				// postoji takva vm
				vm.getDiscs().remove(d.getName());
				vms.writeVMs(ctx.getRealPath(""));
			}
			discs.deleteDisc(d.getName());
			// da li ima organizaciju
			if(d.getOrganisation() == null || d.getOrganisation().equals("")) {
				return Response.status(Response.Status.BAD_REQUEST).entity("No organisation specified!").build();
			}
			// da li je validna organizacija
			Organisations orgs = (Organisations)ctx.getAttribute("organisations");
			if(!orgs.getOrganisationsMap().containsKey(d.getOrganisation())) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Disc with specified organisation doesn't exist!").build();
			}
			Organisation organ = orgs.getOrganisationsMap().get(d.getOrganisation());
			organ.getResources().remove(d.getName());
			orgs.writeOrganisations(ctx.getRealPath(""));
			discs.writeDiscs(ctx.getRealPath(""));
			json = mapper.writeValueAsString(d);
			return Response.ok(json).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Disc with specified name doesn't exists!").build();
	}
	
	private Discs getDiscs() {
		Discs discs = (Discs)ctx.getAttribute("discs");
		if (discs == null) {
			discs = new Discs(ctx.getRealPath(""));
			ctx.setAttribute("discs", discs);
		}
		return discs;
	}
}
