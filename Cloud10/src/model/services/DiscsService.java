package model.services;

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
	public Collection<Disc> getAllDiscs() {
		User current = (User)request.getSession().getAttribute("current");
		if (current.getRole() == RoleType.SuperAdmin) {
			return getDiscs().getDiscsMap().values();
		} else {
			Organisation o = current.getOrganisation();
			ArrayList<Disc> orgDiscs = new ArrayList<Disc>();
			for (Disc d : getDiscs().getDiscsMap().values()) {
				if (d.getOrganisation().equals(o.getName())) {
					orgDiscs.add(d);	
				}
			}
			return orgDiscs;
		}
	}
	
	@POST
	@Path("/getFreeOrganDiscs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Disc> getFreeOrganDiscs(Organisation organisation) {
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
		return organDiscs;
	}
	
	@POST
	@Path("/addDisc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Disc addDisc(Disc d) {
		Discs discs = getDiscs();
		// validacija na serverskoj strani
		// da li postoji vec disk sa unetim imenom
		if(!discs.discNameFree(d.getName())) {
			return null;
		}
		discs.addDisc(d);
		// provera da li postoji odabrana organizacija
		Organisations orgs = (Organisations)ctx.getAttribute("organisations");
		if (!orgs.getOrganisationsMap().containsKey(d.getOrganisation())) {
			// ne postoji uneta organizacija
			// bad request
			return null;
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
				return null;
			}
			// postoji
			// dodajemo joj disk
			VirtualMachine vm = vms.getVirtualMachinesMap().get(d.getVm());
			vm.getDiscs().add(d.getName());
		}
		// ne zeli da doda disk nijednoj virtuelnoj
		// slobodan je
		d.setVm(null);
		return d;
	}
	
	@POST
	@Path("/editDisc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DiscWrapper editDisc(DiscWrapper dw) {
		Discs discs = getDiscs();
		// validacija na serverskoj strani
		// da li vec postoji disk sa uneetim NOVIM imenom
		if (!dw.getOldName().equals(dw.getNewName())) {
			// uneto novo ime
			// da li je zauzeto novouneto ime
			if(!discs.discNameFree(dw.getNewName())) {
				return null;
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
		if (dw.getVm() != null && !dw.getVm().equals("")) {
			VirtualMachines vms = (VirtualMachines)ctx.getAttribute("vms");
			// da li postoji vm koja je prosledjena
			if (vms.getVirtualMachinesMap().containsKey(dw.getVm())) {
				// postoji
				// dodelimo joj disk
				oldDisc.setVm(dw.getVm());
				vms.getVirtualMachinesMap().get(dw.getVm()).addDisc(oldDisc.getName());
			} else {
				return null;
			}
		}
		discs.addDisc(oldDisc);
		return dw;
	}
	
	@POST
	@Path("/removeDisc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Disc removeDisc(Disc d) {
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
					return null;
				}
				// postoji takva vm
				vm.getDiscs().remove(d.getName());
			}
			discs.deleteDisc(d.getName());
			return d;
		}
		return null;
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
