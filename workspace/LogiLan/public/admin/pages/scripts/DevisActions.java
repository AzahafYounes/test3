package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import models.*;
import com.avaje.ebean.Ebean;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class DevisActions extends Controller {
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	 
	 
		 
	//Commercial
	

	public static Result findMaxIdDevis(){
		int max= Ebean.find(DevisClient.class).findRowCount();
		max++;
		return Results.ok(Json.toJson(max));
		}

	public static Result addNewDvs() throws NumberFormatException, ParseException {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
				DevisClient dvs;
				Utilisateur loggedUser=Authentification.getUser(session().get("user"));
				ClientProspect cl=ClientPrsp.getClient(Integer.parseInt(session().get("id")));
				dvs=new DevisClient(values.get("num")[0], df.parse(values.get("date")[0]), values.get("titre")[0], values.get("descript")[0], Double.parseDouble(values.get("montant")[0]));
				dvs.setClient(cl);
				dvs.setUser(loggedUser);
				Ebean.save(dvs);
				Date today=new Date();
				Trace trace=new Trace("Ajout Devis",today);
		    	trace.setClient(ClientPrsp.getClient(Integer.parseInt(session().get("id"))));
		    	trace.setUser(Authentification.getUser(session().get("user")));
		    	Ebean.save(trace);
				return Results.ok(Json.toJson(dvs));
	}
	
	public static Result updateDvs() throws ParseException {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		System.out.println("test dvs"+values);
		DevisClient dvs =Ebean.find(DevisClient.class).where().eq("idDevisCl", Long.parseLong(values.get("id")[0])).findUnique();
		Date today=new Date();
		Date dateAv=dvs.getDate();
		String titreAv=dvs.getTitre();
		Double montantAv=dvs.getMontantDevis();
		String descAv=dvs.getDescription();
		Date dateAp=df.parse(values.get("date")[0]);
		String titreAp=values.get("titre")[0];
		Double montantAp=Double.parseDouble(values.get("montant")[0]);
		String descAp=values.get("descript")[0];
		
		dvs.setDate(df.parse(values.get("date")[0]));
		dvs.setNumDevis(values.get("num")[0]);
		dvs.setMontantDevis(Double.parseDouble(values.get("montant")[0]));
		dvs.setTitre(values.get("titre")[0]);
		dvs.setDescription(values.get("descript")[0]);
		Ebean.update(dvs);
		
		if(dateAp.getTime()!=dateAv.getTime()){
			Trace trace=new Trace("Modifier Devis",today);
			trace.setDataApres(df.format(dateAp));
			trace.setDataAvant(df.format(dateAv));
			trace.setDonneesChange("Date Devis");
			trace.setClient(ClientPrsp.getClient(Integer.parseInt(session().get("id"))));
	    	trace.setUser(Authentification.getUser(session().get("user")));
	    	Ebean.save(trace);
		}
		
		if(!titreAv.equals(titreAp)){
			Trace trace=new Trace("Modifier Devis",today);
			trace.setDataApres(titreAp);
			trace.setDataAvant(titreAv);
			trace.setDonneesChange("Titre Devis");
			trace.setClient(ClientPrsp.getClient(Integer.parseInt(session().get("id"))));
	    	trace.setUser(Authentification.getUser(session().get("user")));
	    	Ebean.save(trace);
		}
		
		if(montantAp!=montantAv){
			Trace trace=new Trace("Modifier Devis",today);
			trace.setDataApres(String.valueOf(montantAp));
			trace.setDataAvant(String.valueOf(montantAv));
			trace.setDonneesChange("Montant Devis");
			trace.setClient(ClientPrsp.getClient(Integer.parseInt(session().get("id"))));
	    	trace.setUser(Authentification.getUser(session().get("user")));
	    	Ebean.save(trace);
		}
		
		if(!descAp.equals(descAv)){
			Trace trace=new Trace("Modifier Devis",today);
			trace.setDataApres(descAp);
			trace.setDataAvant(descAv);
			trace.setDonneesChange("Description Devis");
			trace.setClient(ClientPrsp.getClient(Integer.parseInt(session().get("id"))));
	    	trace.setUser(Authentification.getUser(session().get("user")));
	    	Ebean.save(trace);
		}
		return Results.ok(Json.toJson(dvs));
	}
	
	public static Result deleteDvs() {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		DevisClient dvs= Ebean.find(DevisClient.class).where().eq("idDevisCl", Long.parseLong(values.get("id")[0])).findUnique();
	//dvs.delete();
		SqlUpdate down = Ebean.createSqlUpdate("delete from devis_client where idDevisCl="+Long.parseLong(values.get("id")[0]));
                       down.execute();
		Date today=new Date();
		Trace trace=new Trace("Suppression Devis",today);
    	trace.setClient(ClientPrsp.getClient(Integer.parseInt(session().get("id"))));
    	trace.setUser(Authentification.getUser(session().get("user")));
    	Ebean.save(trace);
		return ok();
	}

}
