package controllers;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import models.FamilleTarif;
import models.Fournisseur;
import models.UserNotif;
import models.Utilisateur;

import com.avaje.ebean.Ebean;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class FamilleTarifAction extends Controller{

	public Result getAllFamilleTarifs(){
		 Utilisateur user = Authentification.getUser(session().get("user"));
		 
		 try{
			java.util.List<FamilleTarif> allfamilleTarifs = Ebean.find(FamilleTarif.class).orderBy("idFamilleT asc").findList();
		 	if (user.getRole().equals("Admin")){
		 		List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
		 		return Results.ok(views.html.familleTarifs.render(user, allfamilleTarifs,notifications));
		 	}else{ 
				 return Results.ok(views.html.error_404.render());	
		 	}
		 }
		catch (Exception Ex)
		{
			return Results.redirect("/");
		}
	 }
	
	public Result addNewFamilleTarif() throws Exception {
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			float tva=Float.parseFloat(values.get("tva")[0]);
			float marge=Float.parseFloat(values.get("marge")[0]);
			if(Ebean.find(FamilleTarif.class).where().eq("nom", values.get("nom")[0]).findUnique()==null){
				FamilleTarif newfamilleT =new FamilleTarif(values.get("nom")[0], tva, marge, values.get("type")[0]);
				Ebean.save(newfamilleT);
				return Results.ok();
			}
			else{
				return Results.badRequest();
			}
	
		}

	public Result updateFamilleTarif() {
			try{
				
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
				
				
				int id= Integer.parseInt(values.get("id")[0]);
				float tva=Float.parseFloat(values.get("tva")[0]);
				float marge=Float.parseFloat(values.get("marge")[0]);
				FamilleTarif existantFamilleTarif =Ebean.find(FamilleTarif.class,id);
				
				existantFamilleTarif.setNom(values.get("nom")[0]);
				existantFamilleTarif.setTva(tva);
				existantFamilleTarif.setMarge(marge);
				existantFamilleTarif.setType(values.get("type")[0]);
				
				Ebean.update(existantFamilleTarif);  
				return Results.ok(Json.toJson(existantFamilleTarif));	
			}
			catch(Exception Ex)
			{
				
				return Results.badRequest();
			}
			
		}

	public static FamilleTarif getFamilleTarif(int id){
	 
		 FamilleTarif familleTarif = null ;
		 if(Ebean.find(FamilleTarif.class).where().eq("idFamilleT",id).findUnique()!=null){
		    familleTarif = Ebean.find(FamilleTarif.class).where().eq("idFamilleT",id).findUnique();
		}
			return familleTarif;
	 }
	 
	
	public Result getFamilleTarifJson()
	{
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		
		FamilleTarif familleTarif = getFamilleTarif(id);
		return Results.ok(Json.toJson(familleTarif));
	}
	
	public Result deleteFamilleTarif() {
	    		    		
		try{
	    		final Map<String, String[]> values = request().body().asFormUrlEncoded();
	    		int id= Integer.parseInt(values.get("id")[0]);
	    		
	    		FamilleTarif familleTarif = Ebean.find(FamilleTarif.class,id);
	    		
	    		Ebean.delete(FamilleTarif.class,familleTarif.getIdFamilleT());
	    			
	    		return ok();
				}
				catch (Exception Ex)
				{
					return Results.badRequest();
				}
	    	}
	
}