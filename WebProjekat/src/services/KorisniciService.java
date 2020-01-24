package services;
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

import model.Korisnik;
import model.kolekcije.Korisnici;

@Path("/korisnici")
public class KorisniciService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/ucitaj")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Korisnik> ucitaj() {
		System.out.println("ucitaj korisnike");
		return (getKorisnici()).getKorisnici().values();
	}
	
	@POST
	@Path("/prijavi")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public String prijavi(Korisnik p) {
		System.out.println("provera na serverskoj strani");
		System.out.println(p.getEmail() + "         " + p.getLozinka());
		boolean ind = false;
		for (Korisnik k : getKorisnici().getKorisnici().values()) {
			//System.out.println(pr.getName() + " " + pr.getPrice());
			System.out.println(k.getEmail() + "    " + k.getLozinka());
			if(k.getEmail().equals(p.getEmail()) && k.getLozinka().equals(p.getLozinka()))
			{
				ind = true;
				break;
			}	
		}
		System.out.println();
		if(ind) {
			request.getSession().setAttribute("trenutni", p);
		}
		else {
			return "greska404";
		}
		System.out.println("TRENUTNO PRIJAVLJENI JE "+getTrenutniPrijavljeni().getEmail());
		return getTrenutniPrijavljeni().getEmail();
	}
	
	@POST
	@Path("/odjavi")
	@Produces(MediaType.TEXT_PLAIN)
	public String odjavi() {
		Korisnik k = new Korisnik();
		ctx.setAttribute("trenutni", k);
		return "gotovo";
	}
	
	private Korisnici getKorisnici() {
		/*Products products = (Products) ctx.getAttribute("products");
		if (products == null) {
			products = new Products(ctx.getRealPath(""));
			ctx.setAttribute("products", products);
		} 
		return products;*/
		Korisnici korisnici = (Korisnici) ctx.getAttribute("korisnici");
		if(korisnici == null) {
			korisnici = new Korisnici();
			ctx.setAttribute("korisnici", korisnici);
		}
		return korisnici;
	}
	
	private Korisnik getTrenutniPrijavljeni() {
		Korisnik sc = (Korisnik) request.getSession().getAttribute("trenutni");
		if (sc == null) {
			sc = new Korisnik();
			request.getSession().setAttribute("trenutni", sc);
		} 
		return sc;
	}
}
