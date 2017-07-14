package controllers;

import java.awt.event.ActionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class NotificationActions extends Controller {
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat daf = new SimpleDateFormat("dd/MM/yyyy");
	//admin
	 public  Result getNotifs()
	 {
		 try{
		 Utilisateur logedUser = Authentification.getUser(session().get("user"));
		 
		 session("user",logedUser.getIdentifiant());
		 if(logedUser.getRole().equals("Admin"))
		 {
			 UserNotif un = new UserNotif();
			 java.util.List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
			 System.out.println("size"+notifications.size());
				for (int i=0;i<notifications.size();i++){
					un= notifications.get(i);
					un.setAdminLu(true);
					un.setNotifadminset(true);
					Ebean.save(un);}
		 return Results.ok(views.html.notifs.render(logedUser, notifications));
		 }
		 else{
			UserNotif un = new UserNotif();
			 List<UserNotif> notifications=GetNotificationCom();
			 List<UserNotif> notificationsLu=GetNotificationComLu();
			 System.out.println("size"+notifications.size());
			for (int i=0;i<notifications.size();i++){
				un= notifications.get(i);
				un.setLu(true);
				Ebean.save(un);}
			//return Results.ok(views.html.main1.render("Notification",logedUser,views.html.listeNotif.render(notifications)));
			 return Results.ok(views.html.notifsPharm.render("Notification",logedUser,notifications,notificationsLu));
		 }	
		}
		catch (Exception Ex)
		{
			
			return Results.redirect("/");
		}
		
	 }
	 
	 public  Result getTypes(){
		 
		 String[] types = {TypeNotif.getRdv(),TypeNotif.getEcheance()};
		 return Results.ok(Json.toJson(types));
	 }
	 
	 public  Result getAllNotifs(){
		 try{
			 Utilisateur logedUser = Authentification.getUser(session().get("user"));
			 
			 session("user",logedUser.getIdentifiant());
			 List<Notification> Notifs = Ebean.find(Notification.class).where().eq("isDeleted", false).orderBy("id desc").findList();
			 java.util.List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
			 if(logedUser.getRole().equals("Admin"))
			 return Results.ok(views.html.listNotifs.render(logedUser,Notifs,notifications));
			 else return Results.ok(views.html.error_404.render());
		 }
			catch (Exception Ex)
			{
				
				return Results.redirect("/");
			}
		 
	 }
	 
	 public  Result addNewNotif() {
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
				int delai = Integer.parseInt(values.get("delai")[0]);
				List<Notification> notifications = Ebean.find(Notification.class).where().eq("isDeleted", false).ieq("type", values.get("type")[0]).findList();
				for(Notification ntf : notifications)
				{
					if(ntf.getDelai()==delai && ntf.isDeleted()==false){ 
						return Results.badRequest();
					}
				}
					Notification ntf = new Notification(values.get("type")[0] , delai, values.get("message")[0]);  
					Ebean.save(ntf);
					return Results.ok(Json.toJson(ntf));	
		}
		
		public  Result updateNotif() {
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			int id= Integer.parseInt(values.get("id")[0]);
			Notification notif =Ebean.find(Notification.class,id);
			notif.setType(values.get("type")[0]);
			notif.setDelai(Integer.parseInt(values.get("delai")[0]));
			notif.setMessage(values.get("message")[0]); 
				// update to base
			Ebean.update(notif);
			return Results.ok();
		}

		public  Result deleteNotif() {
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			Notification notif = Ebean.find(Notification.class,values.get("id")[0]);
			notif.setDeleted(true);
			Ebean.update(notif);
			return Results.ok();
		}
	 
		//Commercial

		public  Result createNotifs(){
			try{
			List<Utilisateur> users = Ebean.find(Utilisateur.class).orderBy("id asc").findList();
			List<Object> newNotifsUser = new ArrayList<Object>();
			List<Object> newNotifs = new ArrayList<Object>();
			for(Utilisateur user:users){
				newNotifsUser.add(addObservation(user.getId()));
				List<Notification> notifs = Ebean.find(Notification.class).findList();
				for(Notification notif : notifs){
				
						switch(notif.getType()){
							case "Rendez-vous" : {
								
								newNotifsUser.add(addRDV(notif.getId(), user.getId()));
							break;	
							}
							case "Info" : {
								
								newNotifsUser.add(addEcheance(notif.getId(),user.getId()));
								break;
								}				
						}
											
				}
				newNotifs.add(newNotifsUser);
				}
			return Results.ok();
		}
	catch(Exception ex)
			{
		return Results.badRequest();
			}
	}
		
		
		//Add Rendez-vous notif Based on msg
			public  Result addRDV(int idNotif, int idUser){
				Utilisateur user = Ebean.find(Utilisateur.class,idUser);
				Notification notif = Ebean.find(Notification.class,idNotif);
				List<UserNotif> newNotifsRdv = new ArrayList<UserNotif>();
				
				return Results.ok(Json.toJson(newNotifsRdv));
			}
			
			
			//Set notif false
			public  Result setNotifFalse()
			{
				try{
					final Map<String, String[]> values = request().body().asFormUrlEncoded();
					Utilisateur user = Authentification.getUser();
					Long id=Long.parseLong(values.get("id")[0]);
					UserNotif notif = Ebean.find(UserNotif.class,id);
					if(user.getRole().equals("Admin"))
					{
						notif.setNotifadminset(true);
					}
					else notif.setNotifcommercset(true);
					Ebean.update(notif);
					return Results.ok();
				   }
				catch(Exception Ex)
				{
					return Results.badRequest();
				}
				
			}
			
			
		//Add Echeance notif Based on msg
			public  Result addEcheance(int idNotif, int idUser)
			{
				Utilisateur user = Ebean.find(Utilisateur.class,idUser);
				Notification notif = Ebean.find(Notification.class,idNotif);
				List<OffreOfficine> rsks = Ebean.find(OffreOfficine.class).where().eq("user", user).ne("dateEcheance", null).findList();
				List<UserNotif> newNotifsRsk = new ArrayList<UserNotif>();
				for(OffreOfficine rsk : rsks){
					String message1 = notif.getMessage()+" : "+ rsk.getUser().getNom()+" "+rsk.getUser().getPrenom()+". Fournisseur : "+rsk.getFournisseur().getNom();
					int delai = (int)((rsk.getDateOffre().getTime()-new Date().getTime())/1000/60/60/24)+1;
					//System.out.println(delai);
					if(Ebean.find(UserNotif.class).where().eq("message",message1).findUnique()==null && delai== notif.getDelai())
						{
							UserNotif ntf = new UserNotif(new Date(), message1,false);
							ntf.setUser(user);
							ntf.setType("Echeance");
							Ebean.save(ntf);
							newNotifsRsk.add(ntf);
						}
					}	
				return Results.ok(Json.toJson(newNotifsRsk));
			}
		//Add Observation notif
			public static Result addObservation(int id)
			{
				Utilisateur user = Ebean.find(Utilisateur.class,id);
				List<Observation> observs = Ebean.find(Observation.class).findList();
				List<UserNotif> newNotifsObs = new ArrayList<UserNotif>();
				for(Observation observ : observs){
					if(observ.getListPharmacien().contains(user))
					{String message1="Observation - Objet : "+observ.getObjet()+". Message : "+observ.getMessage(); 
						if(Ebean.find(UserNotif.class).where().eq("message",message1).eq("user",user).findUnique()==null)
						{
							UserNotif ntf = new UserNotif(new Date(),message1,false);
							ntf.setUser(user);
							ntf.setType("Observation");
							
							Ebean.save(ntf);
							newNotifsObs.add(ntf);
						}
						
					}
				}
				return Results.ok(Json.toJson(newNotifsObs));
			}
		
		public  Result getNotifSize()
		{
			Utilisateur user = Authentification.getUser();
			int size;
			if(user.getRole().equals("Admin"))
			{
				size = Ebean.find(UserNotif.class).where().eq("adminLu", false).findRowCount();
				return Results.ok(Json.toJson(size));
			}
			else if (user.getRole().equals("Pharmacien"))
			{
				size = Ebean.find(UserNotif.class).where().eq("lu", false).eq("user", user).findRowCount();
				return Results.ok(Json.toJson(size));
			}
			return Results.badRequest();
			
		}
		
		public static List<UserNotif> GetNotificationCom(){
			Utilisateur user= Authentification.getUser();
			List<UserNotif> notifications= Ebean.find(UserNotif.class).where().eq("user", user).orderBy("id_Notification desc").findList();
			return notifications;
		}
		public  Result getNotification(){
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			Long id=Long.parseLong(values.get("id")[0]);
			UserNotif notif=Ebean.find(UserNotif.class).where().eq("id_Notification", id).findUnique();
			
			return Results.ok(Json.toJson(notif));
		}
		
		public static List<UserNotif> GetNotificationComLu(){
			List<UserNotif> notifications=new ArrayList<UserNotif>();
			notifications.clear();
			Utilisateur user= Authentification.getUser(session().get("user"));
			List<SqlRow> usernotifs=Ebean.createSqlQuery("Select * from user_notif where user_id="+user.getId()).findList();
			for(SqlRow usernotif:usernotifs){
				Long id=usernotif.getLong("id_notification");
				UserNotif notif=Ebean.find(UserNotif.class).where().eq("id_Notification", id).findUnique();
				if(!notif.getLu())
				notifications.add(notif);
				
			}
			return notifications;
		}
		
		
		public  Result setNotifLu(){
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			UserNotif notif=Ebean.find(UserNotif.class).where().eq("id_Notification",Long.parseLong(values.get("id")[0])).findUnique();
			Utilisateur user = Authentification.getUser();
			if(user.getRole().equals("Admin"))
				notif.setAdminLu(true);
			else
				notif.setLu(true);
			Ebean.update(notif);
			return Results.ok();
		}
		
		
		//notification admin
		
		public static List<UserNotif> GetNotificationAdmin(){
			
			List<UserNotif> notifAdmin = Ebean.find(UserNotif.class).orderBy("id_Notification desc").findList();
			return notifAdmin;
		}
		
		public  Result getNotifications(){
			
			Utilisateur user = Authentification.getUser();
			List<UserNotif> notifs;
			if(user.getRole().equals("Admin"))
				notifs = Ebean.find(UserNotif.class).orderBy("id_Notification desc").findList();
			else
				notifs = Ebean.find(UserNotif.class).where().eq("user",user).orderBy("id_Notification desc").findList();
			return Results.ok(Json.toJson(notifs));
		}
		
		
		
		
		
 		
}
