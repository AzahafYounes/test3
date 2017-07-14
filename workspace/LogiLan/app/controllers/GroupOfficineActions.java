	package controllers;

import java.io.File



;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.Fournisseur;
import models.GroupOfficine;
import models.Officine;
import models.UserNotif;
import models.Utilisateur;
import play.api.mvc.MultipartFormData;
import play.data.DynamicForm;
import play.data.Form;

import com.avaje.ebean.Model;

import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Results;

public class GroupOfficineActions extends Controller {
	
		public Result addNewGroupOfficine() throws Exception {
			
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		if(Ebean.find(GroupOfficine.class).where().eq("nomGroup", values.get("nomGroup")[0]).findUnique()==null)
			{
			
			List<Officine> officines = null;
			GroupOfficine groupe =new GroupOfficine(officines, 0, values.get("nomGroup")[0], values.get("zone")[0], values.get("observation")[0]) ;
			Ebean.save(groupe);
			
				
				GroupOfficine groupe1= Ebean.find(GroupOfficine.class).where().eq("nomGroup", groupe.getNomGroup()).findUnique();
				Controller.response().setCookie("id", String.valueOf(groupe1.getId()));
				
			
			return Results.redirect("/updateGroupOfficine");
			}
		else
		{
			
			return Results.badRequest();
		}
	
	}
		public Result addMonGroupe() throws Exception {
			Utilisateur user = Authentification.getUser(session().get("user"));
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			if(Ebean.find(GroupOfficine.class).where().eq("nomGroup", values.get("nomGroupe")[0]).findUnique()==null)
				{
					
				List<Officine> officines = new ArrayList<>();
				if(Ebean.find(Officine.class).where().eq("user",user).findUnique()!=null){
					Officine officine=Ebean.find(Officine.class).where().eq("user",user).findUnique();
					officines.add(officine);
					
				}
				GroupOfficine groupe =new GroupOfficine(officines, 0, values.get("nomGroupe")[0], values.get("zone")[0], values.get("observation")[0]) ;
				groupe.setOfficines(officines);
				groupe.setUser(user);
				Ebean.save(groupe);
				
					
					
					
				
				return Results.ok(Json.toJson(groupe));
				}
			else
			{
				
				return Results.badRequest();
			}
		
		}
		
		
		public Result updatemongroupe() {
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
				int id= Integer.parseInt(values.get("id")[0]);
				GroupOfficine existantgroupe =Ebean.find(GroupOfficine.class,id);
				
				
				
				existantgroupe.setNomGroup(values.get("nomGroupe")[0]);
				existantgroupe.setZone(values.get("zone")[0]);
				existantgroupe.setObservation(values.get("observation")[0]);
				
				
				Ebean.update(existantgroupe);
				return Results.ok(Json.toJson(existantgroupe));	
			}
			catch(Exception Ex)
			{
				return Results.badRequest();
			}
			
		}
		
		public Result updateGroupOfficine() throws ParseException{
				
				int id= Integer.parseInt(Controller.request().cookies().get("id").value());
			
				GroupOfficine groupe= new Model.Finder<>(Integer.class, GroupOfficine.class).byId(id);
				
				 
				
				
				
				List<Officine> allOfficines= Ebean.find(Officine.class).where().orderBy("id asc").findList();
				
				Utilisateur logedUser= Authentification.getUser(session().get("user"));
				
				if(logedUser.getRole().equals("Admin")){
					List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
					return Results.ok(views.html.modifierGroupAdmin.render(logedUser,groupe,allOfficines,notifications));
				}
				Default: return Results.ok(views.html.error_404.render());
			
		}
		
		
		public Result addOfficineToGroup1() throws ParseException{
			
			Utilisateur user= Authentification.getUser(session().get("user"));
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			int idOfficine =Integer.parseInt(values.get("officine")[0]);
			
			
			Officine officine=Ebean.find(Officine.class).where().eq("id", idOfficine).findUnique();
			
			
			GroupOfficine groupe= Ebean.find(GroupOfficine.class).where().eq("user", user).findUnique();
			groupe.getOfficines().add(officine);
			
			Ebean.save(groupe);
			
			
			
			if(user.getRole().equals("AdminOfficine")){
				
				return Results.ok(Json.toJson(groupe));
			}
			Default: return Results.ok(views.html.error_404.render());
		
	}
		
		
		
		public Result addOfficineToGroup() throws ParseException{
			
			int idGroup= Integer.parseInt(Controller.request().cookies().get("id").value());
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			int idOfficine =Integer.parseInt(values.get("idOfficine")[0]);
			
			Utilisateur logedUser= Authentification.getUser(session().get("user"));
			Officine officine=Ebean.find(Officine.class).where().eq("id", idOfficine).findUnique();
			
			
			GroupOfficine groupe= new Model.Finder<>(Integer.class, GroupOfficine.class).byId(idGroup);
			groupe.getOfficines().add(officine);
			
			Ebean.save(groupe);
			
			
			
			if(logedUser.getRole().equals("Admin")){
				
				return Results.ok(Json.toJson(groupe));
			}
			Default: return Results.ok(views.html.error_404.render());
		
	}
		
		public Result getMonGroupe(){

			Utilisateur user = Authentification.getUser(session().get("user"));
			int exist=0;
			
			GroupOfficine mongroupe = null;
			try{
				
				
				if (user.getRole().equals("AdminOfficine")){
					
					if(Ebean.find(GroupOfficine.class).where().eq("user",user).findUnique()!=null){
						mongroupe=Ebean.find(GroupOfficine.class).where().eq("user",user).findUnique();
						exist=1;
					}
					System.out.println(exist);
					List<UserNotif> notifications=NotificationActions.GetNotificationCom();
        		 	List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
        		 	return Results.ok(views.html.mongroupe.render(user, mongroupe,exist,notifications,notificationsLu));
				}else 
					return Results.ok(views.html.error_404.render());
					
			}
			catch (Exception Ex)
			{
				return Results.redirect("/");
			}
		}
		
		
		
		
		
		
		
		public Result modifier() throws ParseException{
			DynamicForm form = Form.form().bindFromRequest();
			int id= Integer.parseInt(form.get("id"));
			String nomGroup = form.get("nomGroup");
			String zone = form.get("zone");
			String observation = form.get("observation");
			Utilisateur user = Authentification.getUser(session().get("user"));
			GroupOfficine groupe=null;
			if(Ebean.find(GroupOfficine.class).where().eq("nomGroup", groupe.getNomGroup()).findUnique()!=null){
				groupe = Ebean.find(GroupOfficine.class).where().eq("nomGroup", groupe.getNomGroup()).findUnique();
				Controller.response().setCookie("id", String.valueOf(groupe.getId()));
			}
			return Results.redirect("/updateGroupOfficine");
		
	}

		public static GroupOfficine getGroupOfficine(int id){
	 
			GroupOfficine groupe = null ;
		    if(Ebean.find(GroupOfficine.class).where().eq("id",id).findUnique()!=null);		
			{
				groupe = Ebean.find(GroupOfficine.class).where().eq("id",id).findUnique();
			}
			return groupe;
		}
		
		
		public static GroupOfficine getGroupOfficineByNom(String nom){
			 
			GroupOfficine groupe = null ;
		    if(Ebean.find(GroupOfficine.class).where().eq("nomGroup",nom).findUnique()!=null);		
			{
				groupe = Ebean.find(GroupOfficine.class).where().eq("nomGroup",nom).findUnique();
			}
			return groupe;
		}
		
		
	 
		public Result getGroupOfficineJson()

		{
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			int id= Integer.parseInt(values.get("id")[0]);
			GroupOfficine groupe = getGroupOfficine(id);
			return Results.ok(Json.toJson(groupe));
		}
	
		public Result getAllGroupOfficine(){

			Utilisateur user = Authentification.getUser(session().get("user"));
			try{
				java.util.List<GroupOfficine> groupes = Ebean.find(GroupOfficine.class).orderBy("nomGroup asc").findList();
				if (user.getRole().equals("Admin")){
					List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
					return Results.ok(views.html.Groupofficines.render(user, groupes,notifications));
				}else 
					return Results.ok(views.html.error_404.render());	
			}
			catch (Exception Ex)
			{
				return Results.redirect("/");
			}
		}
		
		
		public Result PageAjouterGroupOfficine(){

			Utilisateur user = Authentification.getUser(session().get("user"));
			try{
				java.util.List<Officine> AllOfficine = Ebean.find(Officine.class).orderBy("id asc").findList();
				java.util.List<Utilisateur> Allusers = Ebean.find(Utilisateur.class).orderBy("nom asc").findList();
				if (user.getRole().equals("Admin"))
					return Results.ok(views.html.ajoutGroup.render(user));
				else 
					return Results.ok(views.html.error_404.render());	
			}
			catch (Exception Ex)
			{
				return Results.redirect("/");
			}
		}
		
		
		
		
		
		public Result deleteGroupOfficine() {
	    	
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
		
		
		
			
			
			
		
		public Result deleteOfficinetoGroup() {
			Utilisateur user = Authentification.getUser(session().get("user"));
			GroupOfficine mongroupe=new GroupOfficine();
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
	    		int id= Integer.parseInt(values.get("id")[0]);
	    		if (user.getRole().equals("AdminOfficine")){
	    			if(Ebean.find(GroupOfficine.class).where().eq("user",user).findUnique()!=null){
						mongroupe=Ebean.find(GroupOfficine.class).where().eq("user",user).findUnique();
						for(int i=0;i<mongroupe.getOfficines().size();i++){
							if(mongroupe.getOfficines().get(i).getId()==id){
								mongroupe.getOfficines().remove(i);
							}
						}
						System.out.println(id);
						for(int i=0;i<mongroupe.getOfficines().size();i++){
							System.out.println(mongroupe.getOfficines().get(i));
						}
						
						Ebean.update(mongroupe);
					}
	    			
		    		
	    		}
	    		
	    		return ok();
			}
			catch (Exception Ex)
			{
				return Results.badRequest();
			}
	    }
		
		
		public Result modifierGroupOfficine(String id) throws ParseException{
			
			int idGroup=Integer.parseInt(id);
			
			GroupOfficine groupe=new Model.Finder<>(Integer.class,GroupOfficine.class).byId(idGroup);
			List<Officine> allOfficines= Ebean.find(Officine.class).where().orderBy("id asc").findList();
			
			Utilisateur logedUser= Authentification.getUser(session().get("user"));
			session("id",String.valueOf(idGroup));
			
			if(logedUser.getRole().equals("Admin")){
				Controller.response().setCookie("id", String.valueOf(groupe.getId()));
				List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
				return Results.ok(views.html.modifierGroupAdmin.render(logedUser,groupe,allOfficines,notifications));
			}
			Default: return Results.ok(views.html.error_404.render());
			
		}	
	 
	 
}
