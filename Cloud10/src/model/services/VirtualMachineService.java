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

import model.VirtualMachine;
import model.collections.VirtualMachines;

@Path("/vms")
public class VirtualMachineService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/getAllVms")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<VirtualMachine> getAllVms() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		return getVMs().getVirtualMachinesMap().values();
	}
	
	private VirtualMachines getVMs() {
		VirtualMachines vms = (VirtualMachines)ctx.getAttribute("vms");
		if (vms == null) {
			vms = new VirtualMachines(ctx.getRealPath(""));
			ctx.setAttribute("vms", vms);
		}
		return vms;
	}
	
	@POST
	@Path("/addVM")
	@Consumes(MediaType.APPLICATION_JSON)
	// MORA IMATI POVRATNU VREDNOST
	// AKO JE UNETA VM SA POSTOJECIM IMENOM
	public void addVM(VirtualMachine vm) {
		System.out.println("dodavanje virtuelne masine -- server");
		// validacija na serverskoj strani!
		
		VirtualMachines vms = getVMs();
		if(!vms.checkVMName(vm.getName())) {
			return;
		}
		
		vms.addVM(vm);
		System.out.println("dodali");
	}
}
