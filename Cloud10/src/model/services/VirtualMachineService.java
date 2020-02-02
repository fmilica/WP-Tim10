package model.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import model.Activity;
import model.Organisation;
import model.User;
import model.VirtualMachine;
import model.collections.Discs;
import model.collections.Organisations;
import model.collections.VirtualMachines;
import model.enums.RoleType;
import model.wrappers.ActivityHelper;
import model.wrappers.VirtualMachineActivities;
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
	public Response getAllVms() throws JsonProcessingException {
		User current = (User) request.getSession().getAttribute("current");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getRole() == RoleType.SuperAdmin) {
			json = mapper.writeValueAsString(getVMs().getVirtualMachinesMap().values());
			return Response.ok(json).build();
		} else {
			Organisation o = current.getOrganisation();
			ArrayList<VirtualMachine> orgVms = new ArrayList<VirtualMachine>();
			for (VirtualMachine vm : getVMs().getVirtualMachinesMap().values()) {
				if (vm.getOrganisation().equals(o.getName())) {
					orgVms.add(vm);
				}
			}
			json = mapper.writeValueAsString(orgVms);
			return Response.ok(json).build();
		}
	}

	@POST
	@Path("/getOrganVMs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganVMs(Organisation organisation) throws JsonProcessingException {
		User current = (User) ctx.getAttribute("currentUser");
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		ArrayList<VirtualMachine> organVMs = new ArrayList<VirtualMachine>();
		VirtualMachines vms = getVMs();
		for (VirtualMachine vm : vms.getVirtualMachinesMap().values()) {
			if (vm.getOrganisation().equals(organisation.getName())) {
				organVMs.add(vm);
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(organVMs);
		return Response.ok(json).build();
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
	public Response addVM(VirtualMachine vm) throws JsonProcessingException {
		User current = (User) ctx.getAttribute("currentUser");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if (vm == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No VM sent!").build();
		}
		if (vm.hasNull()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Some of VM fields are empty!").build();
		}
		// validacija na serverskoj strani!
		VirtualMachines vms = getVMs();
		// jedinstvenost imena
		if (!vms.vmNameFree(vm.getName())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("VM with specified name already exists!")
					.build();
		}
		Organisations orgs = (Organisations) ctx.getAttribute("organisations");
		if (!orgs.getOrganisationsMap().containsKey(vm.getOrganisation())) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Organisation with specified name doesn't exist!").build();
		}
		Organisation org = orgs.getOrganisationsMap().get(vm.getOrganisation());
		org.addResource(vm.getName());
		// dodavanje diskovima reference na novonapravljenu virtuelnu masinu
		if (vm.getDiscs() != null) {
			Discs discs = (Discs) ctx.getAttribute("discs");
			Collection<String> vmDiscs = vm.getDiscs();
			for (String discName : vmDiscs) {
				if (discs.getDiscsMap().containsKey(discName)) {
					discs.getDiscsMap().get(discName).setVm(vm.getName());
				} else {
					// nepostojeci disk!
					return Response.status(Response.Status.BAD_REQUEST).entity("VM with specified name doesn't exist!")
							.build();
				}
			}
		}
		vms.addVM(vm);
		vms.writeVMs(ctx.getRealPath(""));
		orgs.writeOrganisations(ctx.getRealPath(""));
		json = mapper.writeValueAsString(vm);
		return Response.ok(json).build();
	}

	@POST
	@Path("/editVM")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editVM(VirtualMachineWrapper vmw) throws JsonProcessingException {
		User current = (User) ctx.getAttribute("currentUser");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if (vmw == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user sent!").build();
		}
		VirtualMachines vms = getVMs();
		// validacija na serverskoj strani
		// da li vec postoji vm sa uneetim NOVIM imenom
		if (!vmw.getOldName().equals(vmw.getName())) {
			// uneto novo ime
			// da li je zauzeto novouneto ime
			if (!vms.vmNameFree(vmw.getName())) {
				return Response.status(Response.Status.BAD_REQUEST).entity("VM with specified name already exists!")
						.build();
			} else {
				// promenilo se i slobodno je novo ime
				// menjamo u organizaciji u listi njenih resursa
				Organisations orgs = (Organisations) ctx.getAttribute("organisations");
				// ako postoji organizacija koja je prosledjena
				if (orgs.getOrganisationsMap().containsKey(vmw.getOrganisation())) {
					// postoji
					// brisemo staro ime i dodeljujemo novo u resurse
					Collection<String> resources = orgs.getOrganisationsMap().get(vmw.getOrganisation()).getResources();
					resources.remove(vmw.getOldName());
					resources.add(vmw.getName());
				}
				orgs.writeOrganisations(ctx.getRealPath(""));
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
		// Collection<String> allDiscs = vmw.getDiscs();
		// allDiscs.addAll(vmw.getNewDiscs());
		// za sve diskove stare koje nije oznacio, namesti kod diska da je slobodan
		Collection<String> oldChosenDiscs = vmw.getDiscs();
		if (oldChosenDiscs != null) {
			for (String old : oldVm.getDiscs()) {
				// iterira kroz stare diskove
				Discs discs = (Discs) ctx.getAttribute("discs");
				if (oldChosenDiscs == null) {
					// nije odabrao nijedan stari
					// oslobadjamo ih sve
					discs.getDiscsMap().get(old).setVm(null);
				} else if (!oldChosenDiscs.contains(old)) {
					// nije odabrao neki od starih diskova
					// oslobadjamo ga
					discs.getDiscsMap().get(old).setVm(null);
				}
			}
		}
		if (vmw.getNewDiscs() != null) {
			Collection<String> newChosenDiscs = vmw.getNewDiscs();
			for (String newDisc : newChosenDiscs) {
				// odabrao je novi disk
				Discs discs = (Discs) ctx.getAttribute("discs");
				// zauzimamo ga
				discs.getDiscsMap().get(newDisc).setVm(oldVm.getName());
			}
		}
		// DODAJEMO UNIJU OBE LISTE
		oldVm.setDiscs(vmw.getDiscs());
		// oldVm.setActivities(vmw.getActivities());
		Activity last = oldVm.getActivities().get(oldVm.getActivities().size() - 1);
		Date d = new Date();
		if (last.getOff() == null) {
			// bila je upaljena
			if (!vmw.isStatus()) {
				// poslao je da zeli da je ugasi
				last.setOffTime(d);
			}
		} else {
			// bila je ugasena
			if (vmw.isStatus()) {
				// poslao je da zeli da je upali
				Activity a = new Activity(d);
				oldVm.getActivities().add(a);
			}
		}
		vms.addVM(oldVm);
		vms.writeVMs(ctx.getRealPath(""));
		json = mapper.writeValueAsString(vmw);
		return Response.ok(json).build();
	}

	@POST
	@Path("/removeVM")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeVM(VirtualMachine vm) throws JsonProcessingException {
		User current = (User) ctx.getAttribute("currentUser");
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getRole() == RoleType.User) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}
		if (vm == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No VM sent!").build();
		}
		if (vm.getName() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No VM sent!").build();
		}
		VirtualMachines vms = getVMs();
		// provera na serverskoj strani
		if (!vms.vmNameFree(vm.getName())) {
			// brise dobar
			// brise vm iz resursa organizacije
			Organisations orgs = (Organisations) ctx.getAttribute("organisations");
			// da li postoji organizacija
			if (orgs.getOrganisationsMap().containsKey(vm.getOrganisation())) {
				// da li postoji u resursima
				Collection<String> resources = orgs.getOrganisationsMap().get(vm.getOrganisation()).getResources();
				resources.remove(vm.getName());
			}
			if (vm.getDiscs() != null) {
				// ima diskova vezanih za sebe
				// oslobadja ih
				Discs discs = (Discs) ctx.getAttribute("discs");
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
			vms.writeVMs(ctx.getRealPath(""));
			orgs.writeOrganisations(ctx.getRealPath(""));
			json = mapper.writeValueAsString(vm);
			return Response.ok(json).build();
		}
		// brise nepostojecu
		return Response.status(Response.Status.BAD_REQUEST).entity("VM with specified name doesn't exist!").build();
	}

	@POST
	@Path("/editVMActivities")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editVMActivities(VirtualMachineActivities vma) {
		User current = (User) ctx.getAttribute("currentUser");
		// pravo pristupa //
		// niko nije ulogovan
		if (current == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		if (current.getEmail() == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied! No logged in user!").build();
		}
		// samo superAdmin ima pristup ovoj metodi
		if (current.getRole() != RoleType.SuperAdmin) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();
		}

		// sadrzaj poziva //
		// nije poslao nista
		if (vma == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Nothing sent!").build();
		}
		// null virtuelna masina
		if (vma.getVmName() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No VM sent!").build();
		}
		// nepostojeca virtuelna masina
		VirtualMachines vms = getVMs();
		if (!vms.getVirtualMachinesMap().containsKey(vma.getVmName())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("VM with such name doesn't exist!").build();
		}
		VirtualMachine oldVm = vms.getVirtualMachinesMap().get(vma.getVmName());
		// obrisao je sve aktivnosti
		if (vma.getActivities() == null) {
			// okej poziv
			// kreiramo praznu listu aktivnosti i dodeljujemo je toj vm
			oldVm.setActivities(new ArrayList<Activity>());
			return Response.ok().build();
		}
		// postoji bar jedna aktivnost
		// validacija unetih datuma //
		ArrayList<Activity> newActivities = new ArrayList<Activity>();
		for (ActivityHelper a : vma.getActivities()) {
			// aktivnost mora imati pocetak da bi bila aktivnost
			// kraj moze i ne mora da ima
			if (a.getOn() == null || a.getOn().equals("")) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Activity must have start time!").build();
			}
			// ima pocetak
			if (Activity.checkDate(a.getOn())) {
				// ima validan pocetak
				if (a.getOff() != null && !a.getOff().equals("")) {
					// ima kraj
					if (Activity.checkDate(a.getOff())) {
						// ima validan kraj
						if (a.getOnDate().before(a.getOffDate())) {
							// kraj aktivnosti je posle pocetka
							newActivities.add(new Activity(a.getOn(), a.getOff()));
						} else {
							return Response.status(Response.Status.BAD_REQUEST)
									.entity("End time can't be before start time!").build();
						}
					} else {
						// nevalidan kraj
						return Response.status(Response.Status.BAD_REQUEST).entity("Incorrect end time!").build();
					}
				} else {
					// nema kraj
					newActivities.add(new Activity(a.getOn()));
				}
			} else {
				// nevalidan pocetak
				return Response.status(Response.Status.BAD_REQUEST).entity("Incorrect start time!").build();
			}
		}
		// kreirali smo sve aktivnosti
		oldVm.setActivities(newActivities);
		return Response.ok().build();
	}
}
