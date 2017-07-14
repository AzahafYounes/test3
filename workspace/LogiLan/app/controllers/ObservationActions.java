package controllers;

import java.util.ArrayList
;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import models.Observation;

import models.UserNotif;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class ObservationActions extends Controller {
	
	
 public  Result getObservationPage(){
		 
		 try{
			 Utilisateur logedUser = Authentification.getUser(session().get("user"));
			 
			 session("user",logedUser.getIdentifiant());
			 java.util.List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
			 if(logedUser.getRole().equals("Admin"))
			 return Results.ok();
			 else return Results.ok(views.html.error_404.render());
			}
			catch (Exception Ex)
			{
				
				return Results.redirect("/");
			}
		 
	 }
 
 public  Result sendObservation(){
	 
	try{ 
	 final Map<String, String[]> values = request().body().asFormUrlEncoded();
	 String id = values.get("idPharm")[0];
	 String objet = values.get("objet")[0];
	 String message = values.get("message")[0];
	 Date date = new Date();
	 String str[] = id.split(",");
	 List<Utilisateur> users = new ArrayList<Utilisateur>(){}; 
	 Utilisateur admin = Authentification.getUser();
	 for(int i=0;i<str.length;i++)
	 {
		 Utilisateur user = Ebean.find(Utilisateur.class, Integer.parseInt(str[i]));
		 users.add(user);
		 
	 }
	 Observation obs = new Observation(objet, message, date);
	 obs.setAdmin(admin);
	 obs.setListPharmacien(users);
	 Ebean.save(obs);
	 for(Utilisateur pharmacie : users)
	 NotificationActions.addObservation(pharmacie.getId());
	 NotificationActions.GetNotificationAdmin();
	
	 return Results.ok();
 }
	catch(Exception Ex){
		return Results.badRequest();
	}
 }

}
