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
import model.VirtualMachine;
import model.collections.Discs;
import model.collections.VirtualMachines;
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
		return getDiscs().getDiscsMap().values();
	}
	
	@POST
	@Path("/getFreeOrganDiscs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Disc> getOrganDiscs(Organisation organisation) {
		ArrayList<Disc> organDiscs = new ArrayList<Disc>();
		Discs discs = (Discs)ctx.getAttribute("discs");
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
		Disc oldDisc = discs.getDiscsMap().get(dw.getOldName());
		discs.deleteDisc(oldDisc.getName());
		oldDisc.setName(dw.getNewName());
		oldDisc.setCapacity(dw.getCapacity());
		oldDisc.setType(dw.getType());
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
				VirtualMachine vm = vms.getVirtualMachinesMap().get(d.getVm());
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
