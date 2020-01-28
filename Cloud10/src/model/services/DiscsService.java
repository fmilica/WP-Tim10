package model.services;

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
import model.collections.Discs;

@Path("/discs")
public class DiscsService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/getAllDiscs")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Disc> getAllDiscs() {
		return getDiscs().getDiscsMap().values();
	}
	
	@POST
	@Path("/addDisc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Disc addDisc(Disc d) {
		Discs discs = getDiscs();
		// validacija na serverskoj strani
		// da li postoji vec disk sa unetim imenom
		if(!discs.checkDiscName(d.getName())) {
			return null;
		}
		discs.addDisc(d);
		return d;
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
