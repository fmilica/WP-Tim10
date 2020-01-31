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

import model.Organisation;
import model.User;
import model.VirtualMachine;
import model.collections.Discs;
import model.collections.VirtualMachines;
import model.enums.RoleType;
import model.wrappers.VirtualMachineWrapper;

@Path("/vms")
public class VirtualMachineService {

	@Context
	HttpServletRequest request;

	@Context
	ServletContext ctx;
	
	@GET
	@Path("/getAllVms")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<VirtualMachine> getAllVms() {
		User current = (User)request.getSession().getAttribute("current");
		if (current.getRole() == RoleType.SuperAdmin) {
			return getVMs().getVirtualMachinesMap().values();
		} else {
			Organisation o = current.getOrganisation();
			ArrayList<VirtualMachine> orgVms = new ArrayList<VirtualMachine>();
			for (VirtualMachine vm : getVMs().getVirtualMachinesMap().values()) {
				if (vm.getOrganisation().equals(o.getName())) {
					orgVms.add(vm);
				}
			}
			return orgVms;
		}
	}

	@POST
	@Path("/getOrganVMs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<VirtualMachine> getOrganVMs(Organisation organisation) {
		ArrayList<VirtualMachine> organVMs = new ArrayList<VirtualMachine>();
		VirtualMachines vms = getVMs();
		for (VirtualMachine vm : vms.getVirtualMachinesMap().values()) {
			if (vm.getOrganisation().equals(organisation.getName())) {
				organVMs.add(vm);
			}
		}
		return organVMs;
	}
	
	private VirtualMachines getVMs() {
		VirtualMachines vms = (VirtualMachines) ctx.getAttribute("vms");
		if (vms == null) {
			vms = new VirtualMachines(ctx.getRealPath(""));
			ctx.setAttribute("vms", vms);
		}
		return vms;
	}

	@POST
	@Path("/addVM")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public VirtualMachine addVM(VirtualMachine vm) {
		// validacija na serverskoj strani!
		VirtualMachines vms = getVMs();
		// jedinstvenost imena
		if (!vms.vmNameFree(vm.getName())) {
			return null;
		}
		// dodavanje diskovima reference na novonapravljenu virtuelnu masinu
		if (vm.getDiscs() != null) {
			Discs discs = (Discs)ctx.getAttribute("discs");
			Collection<String> vmDiscs = vm.getDiscs();
			for (String discName : vmDiscs) {
				if (discs.getDiscsMap().containsKey(discName)) {
					discs.getDiscsMap().get(discName).setVm(vm.getName());
				} else {
					// nepostojeci disk!
					return null;
				}
			}	
		}
		vms.addVM(vm);
		return vm;
	}

	@POST
	@Path("/editVM")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public VirtualMachineWrapper editVM(VirtualMachineWrapper vmw) {
		VirtualMachines vms = getVMs();
		// validacija na serverskoj strani
		// da li vec postoji vm sa uneetim NOVIM imenom
		if (!vmw.getOldName().equals(vmw.getName())) {
			// uneto novo ime
			// da li je zauzeto novouneto ime
			if(!vms.vmNameFree(vmw.getName())) {
				return null;
			}
		}
		VirtualMachine oldVm = vms.getVirtualMachinesMap().get(vmw.getOldName());
		vms.deleteVm(oldVm.getName());
		oldVm.setName(vmw.getName());
		oldVm.setCategory(vmw.getCategory());
		oldVm.setCoreNum(vmw.getCoreNum());
		oldVm.setRAM(vmw.getRam());
		oldVm.setGPU(vmw.getGpu());
		// dodavanje novih i starih diskova
		// NEOPHODNA PROVERA DA LI JE NULL BILO STA !
		//Collection<String> allDiscs = vmw.getDiscs();
		//allDiscs.addAll(vmw.getNewDiscs());
		// za sve diskove stare koje nije oznacio, namesti kod diska da je slobodan
		Collection<String> oldChosenDiscs = vmw.getDiscs();
		for (String old : oldVm.getDiscs()) {
			// iterira kroz stare diskove
			Discs discs = (Discs)ctx.getAttribute("discs");
			if (oldChosenDiscs == null) {
				// nije odabrao nijedan stari
				// oslobadjamo ih sve
				discs.getDiscsMap().get(old).setVm(null);
			}
			else if (!oldChosenDiscs.contains(old)) {
				// nije odabrao neki od starih diskova
				// oslobadjamo ga
				discs.getDiscsMap().get(old).setVm(null);
			}
		}
		if (vmw.getNewDiscs() != null) {
			Collection<String> newChosenDiscs = vmw.getNewDiscs();
			for (String newDisc : newChosenDiscs) {
				// odabrao je novi disk
				Discs discs = (Discs)ctx.getAttribute("discs");
				// zauzimamo ga
				discs.getDiscsMap().get(newDisc).setVm(oldVm.getName());
			}
		}
		// DODAJEMO UNIJU OBE LISTE
		oldVm.setDiscs(vmw.getDiscs());
		// oldVm.setActivities(vmw.getActivities());
		vms.addVM(oldVm);
		return vmw;
	}
	
	@POST
	@Path("/removeVM")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public VirtualMachine removeVM(VirtualMachine vm) {
		VirtualMachines vms = getVMs();
		// provera na serverskoj strani
		if (!vms.vmNameFree(vm.getName())) {
			// brise dobar
			if (vm.getDiscs() != null) {
				// ima diskova vezanih za sebe
				// oslobadja ih
				Discs discs = (Discs)ctx.getAttribute("discs");
				// proverava da li postoji takav disk
				for (String discName : vm.getDiscs()) {
					if (discs.getDiscsMap().containsKey(discName)) {
						// postoji
						// oslobadjamo ga
						discs.getDiscsMap().get(discName).setVm(null);
					}
				}
			}
			// brisemo ga
			vms.deleteVm(vm.getName());
			return vm;
		}
		// brise nepostojecu
		return null;
	}
}
