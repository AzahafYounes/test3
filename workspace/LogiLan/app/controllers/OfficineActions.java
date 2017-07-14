	package controllers;

import java.io.File

;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import models.Officine;
import models.UserNotif;
import models.Utilisateur;
import play.api.mvc.MultipartFormData;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Results;

public class OfficineActions extends Controller {
	
		public Result addNewOfficine() throws Exception {
			
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		System.out.println(values.get("nomOfficine")[0]+values.get("user")[0]);
		if(Ebean.find(Officine.class).where().eq("nomOfficine", values.get("nomOfficine")[0]).findUnique()==null)
			{
			System.out.println(values.get("nomOfficine")[0]+values.get("user")[0]);
			Utilisateur user= Ebean.find(Utilisateur.class).where().eq("id", Integer.parseInt(values.get("user")[0])).findUnique();
			Officine newOfficine =new Officine(values.get("nomOfficine")[0], values.get("adress")[0], user,Integer.parseInt(values.get("manager")[0])); 
			newOfficine.setInpe(values.get("inpe")[0]);
			newOfficine.setFix(values.get("fix")[0]);
			Ebean.save(newOfficine);
			
			return Results.ok();
			}
		else
		{
			return Results.badRequest();
		}
	
	}
		
		public Result updateOfficine() {
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
				int id= Integer.parseInt(values.get("id")[0]);
				Officine existantOfficine =Ebean.find(Officine.class,id);
				existantOfficine.setNomOfficine(values.get("nomOfficine")[0]);
				existantOfficine.setAdress(values.get("adress")[0]);
				existantOfficine.setManager(Integer.parseInt(values.get("manager")[0]));
				Utilisateur user= Ebean.find(Utilisateur.class).where().eq("id", Integer.parseInt(values.get("user")[0])).findUnique();
				existantOfficine.setUser(user);
				
				Ebean.update(existantOfficine);
				return Results.ok(Json.toJson(existantOfficine));	
			}
			catch(Exception Ex)
			{
				return Results.badRequest();
			}
			
		}
		
		
		public Result updateOfficine1() {
			System.out.println("aaaaaaaaaa");
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
				Utilisateur user = Authentification.getUser(session().get("user"));
				
				System.out.println(user.getNom());
				Officine officine=Ebean.find(Officine.class).where().eq("user", user).findUnique();
				int id= officine.getId();
				
				Officine existantOfficine =Ebean.find(Officine.class,id);
				System.out.println(existantOfficine.getNomOfficine());
				existantOfficine.setNomOfficine(values.get("nomOfficine")[0]);
				System.out.println(values.get("fix")[0]+" "+values.get("inpe")[0]);
				existantOfficine.setFix(values.get("fix")[0]);
				existantOfficine.setInpe(values.get("inpe")[0]);
				
				
				Ebean.update(existantOfficine);
				return Results.ok(Json.toJson(existantOfficine));	
			}
			catch(Exception Ex)
			{
				return Results.badRequest();
			}
			
		}

		public static Officine getOfficine(int id){
	 
			Officine officine = null ;
		    if(Ebean.find(Officine.class).where().eq("id",id).findUnique()!=null);		
			{
				officine = Ebean.find(Officine.class).where().eq("id",id).findUnique();
				
			}
			return officine;
		}
	 
		public Result getOfficineJson()

		{
			System.out.println("aaaaaaaaaaaaa");
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			int id= Integer.parseInt(values.get("id")[0]);
			Officine officine = getOfficine(id);
			System.out.println(id);
			
			return Results.ok(Json.toJson(officine));
		}
		
		
		public Result getAllOfficineJSON(){

			Utilisateur user = Authentification.getUser(session().get("user"));
			
			try{
				java.util.List<Officine> AllOfficine = Ebean.find(Officine.class).orderBy("id asc").findList();
				java.util.List<Utilisateur> Allusers = Ebean.find(Utilisateur.class).orderBy("nom asc").findList();
				if (user.getRole().equals("Admin"))
					return Results.ok(Json.toJson(AllOfficine));
				else 
					return Results.ok(views.html.error_404.render());	
			}
			catch (Exception Ex)
			{
				return Results.redirect("/");
			}
		}
	
		public Result getAllOfficine(){

			Utilisateur user = Authentification.getUser(session().get("user"));
			try{
				java.util.List<Officine> AllOfficine = Ebean.find(Officine.class).orderBy("id asc").findList();
				java.util.List<Utilisateur> Allusers = Ebean.find(Utilisateur.class).orderBy("nom asc").findList();
				if (user.getRole().equals("Admin")){
					List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
					return Results.ok(views.html.officines.render(user, AllOfficine,Allusers,notifications));
				}else 
					return Results.ok(views.html.error_404.render());	
			}
			catch (Exception Ex)
			{
				return Results.redirect("/");
			}
		}
		
		public Result deleteOfficine() {
	    	
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
	    		int id= Integer.parseInt(values.get("id")[0]);
	    		Officine officine = Ebean.find(Officine.class,id);
	    		Ebean.delete(officine);
	    		return ok();
			}
			catch (Exception Ex)
			{
				return Results.badRequest();
			}
	    }
			
		
	 
	 
}
