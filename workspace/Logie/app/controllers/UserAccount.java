	package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import models.GroupOfficine;
import models.Officine;
import models.UserNotif;
import models.Utilisateur;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Results;

public class UserAccount extends Controller {
	static String url = "./public/admin/layout/profile/";
	static String htmlUrl ="../../assets/admin/layout/profile/";
	
	public  Result accountSettings(){
		try{
		Utilisateur user= Authentification.getUser(session().get("user"));
		 session("user",user.getIdentifiant());
		 
		 if(user.getRole().equals("Admin"))
		 {
			 List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
			 return Results.ok(views.html.editAccount.render(user,notifications));
		 }
		 else if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")) {
			 Officine officine=Ebean.find(Officine.class).where().eq("user",user).findUnique();
			 java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    	 java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
			 return Results.ok(views.html.editAccountAdminPh.render(user,officine,notifications,notificationsLu));
		 }
		 else{
			 return Results.redirect("/");
		 }
		}
		catch(Exception Ex)
		{
			return Results.redirect("/");
		}
	}

	public  Result updateGenInformation()
	{
	
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
	  
		   Utilisateur logedUser = Authentification.getUser();
		   logedUser.setIdentifiant(values.get("identifiant")[0]);
           logedUser.setEmail(values.get("email")[0]);
           logedUser.setTel(values.get("tel")[0]);
		   logedUser.setNom(values.get("nom")[0]);
		   logedUser.setPrenom(values.get("prenom")[0]);
           
           Ebean.update(logedUser);
			return Results.ok();
	}
	
	public  Result updateStatus()
	{
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		Long id = Long.parseLong(values.get("id")[0]);
		Utilisateur user = Ebean.find(Utilisateur.class,id);
		if(user.getStatus().equals("Actif"))
			user.setStatus("Inactif");
		else user.setStatus("Actif");
		Ebean.update(user);
		return Results.ok();
	}
	
	public  Result updatePass()
	{
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		Utilisateur user = Authentification.getUser();
		
		if(BCrypt.checkpw(values.get("password")[0], user.getPassword()))
		{
			user.setPassword(BCrypt.hashpw(values.get("newpassword")[0], BCrypt.gensalt()));
			Ebean.update(user);
			return Results.ok();
		}
		else 
			return Results.badRequest();		
	}

		public  Result addNewUser() throws Exception {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		if(Ebean.find(Utilisateur.class).where().eq("identifiant", values.get("login")[0]).findUnique()==null)
			{
			String hashPass= BCrypt.hashpw(values.get("pass")[0], BCrypt.gensalt());
			Utilisateur newUser =new Utilisateur(values.get("nom")[0], values.get("prenom")[0], values.get("email")[0],values.get("login")[0], hashPass, values.get("role")[0],values.get("tel")[0],"Inactif"); 
			newUser.setMac(values.get("mac")[0]);
			Ebean.save(newUser);
			String message="";
					  message = message+"<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' ><title>LOGIECHANGE</title>"
								+ "<style type='text/css'>html { -webkit-text-size-adjust:none; -ms-text-size-adjust: none;}"
								+ "@media only screen and (max-device-width: 680px), only screen and (max-width: 680px) {"
								+ "*[class='table_width_100'] {width: 96% !important;}*[class='border-right_mob'] {border-right: 1px solid #dddddd;}"
								+ "*[class='mob_100'] {width: 100% !important;}*[class='mob_center'] {text-align: center !important;padding: 0 !important;}"
								+ "*[class='mob_center_bl'] {float: none !important;display: block !important;margin: 0px auto;}	.iage_footer a {"
								+ "text-decoration: none;color: #929ca8;}img.mob_display_none {width: 0px !important;height: 0px !important;display: none !important;}"
								+ "img.mob_width_50 {width: 40% !important;height: auto !important;}img.mob_width_80 {width: 80% !important;height: auto !important;}"
								+ "img.mob_width_80_center {width: 80% !important;height: auto !important;margin: 0px auto;}.img_margin_bottom {font-size: 0;height: 25px;"
								+ "line-height: 25px;}}</style></head><body style='padding: 0px; margin: 0px;'><div id='mailsub' class='notification' align='center'>"
								+ "<table width='100%' border='0' cellspacing='0' cellpadding='0' style='min-width: 320px;'><tr><td align='center' bgcolor='#eff3f8'>"
								+ "<table border='0' cellspacing='0' cellpadding='0' class='table_width_100' width='100%' style='max-width: 680px; min-width: 300px;'>"
								+ "<tr><td>"
								+ "<div style='height: 80px; line-height: 80px; font-size: 10px;'>&nbsp;</div>"
								+ "</td></tr>"
								+ "<tr><td align='center' bgcolor='#ffffff'>"
								+ "<div style='height: 10px; line-height: 10px; font-size: 10px;'>&nbsp;</div>"
								+ "<table width='90%' border='0' cellspacing='0' cellpadding='0'>"
								+ "<tr><td align='left'>"
								+ "<div class='mob_center_bl' style='float: left; display: inline-block; width: 115px;'>"
								+ "<table class='mob_center' width='115' border='0' cellspacing='0' cellpadding='0' align='left' style='border-collapse: collapse;'>"
								+ "<tr><td align='left' valign='middle'>"
								+ "<div style='height: 20px; line-height: 20px; font-size: 10px;'>&nbsp;</div>"
								+ "<table width='115' border='0' cellspacing='0' cellpadding='0' >"
								+ "<tr><td align='left' valign='top' class='mob_center'>"
								+ "<a href='"+"/' target='_blank' style='color: #596167; font-family: Arial, Helvetica, sans-serif; font-size: 13px;'>"
								+ "<font face='Arial, Helvetica, sans-seri; font-size: 13px;' size='3' color='#596167'>"
								+ "<p><img src='https://img15.hostingpics.net/pics/686524logobigwhite.png' width='170' height='57' alt='LOGILAN' border='0' style='display: block;'  /></p></font></a>"
								+ "</td></tr>"
								+ "</table>"
								+ "</td></tr>"
								+ "</table></div>"
								+ "<div class='mob_center_bl' style='float: right; display: inline-block; width: 88px;'>"
								+ "<table width='88' border='0' cellspacing='0' cellpadding='0' align='right' style='border-collapse: collapse;'>"
								+ "<tr><td align='right' valign='middle'>"
								+ "<div style='height: 20px; line-height: 20px; font-size: 10px;'>&nbsp;</div>"
								+ "<table width='100%' border='0' cellspacing='0' cellpadding='0' >"
								+ "<tr><td align='right'>"
								+ "<div class='mob_center_bl' style='width: 88px;'>"
								+ "<table border='0' cellspacing='0' cellpadding='0'>"
								+ "<tr><td width='30' align='center' style='line-height: 19px;'>"
								+ "<a href='#' target='_blank' style='color: #596167; font-family: Arial, Helvetica, sans-serif; font-size: 12px;'>"
								+ "<font face='Arial, Helvetica, sans-serif' size='2' color='#596167'>"
								+ "<img src='cdn3.iconfinder.com/data/icons/free-social-icons/67/facebook_circle_color-48.png' width='10' height='19' alt='Facebook' border='0' style='display: block;' /></font></a>"
								+ "</td><td width='39' align='center' style='line-height: 19px;'>"
								+ "<a href='#' target='_blank' style='color: #596167; font-family: Arial, Helvetica, sans-serif; font-size: 12px;'>"
								+ "<font face='Arial, Helvetica, sans-serif' size='2' color='#596167'>"
								+ " <img src='cdn3.iconfinder.com/data/icons/free-social-icons/67/twitter_circle_color-48.png' width='19' height='16' alt='Twitter' border='0' style='display: block;' /></font></a>"
								+ "</td> </tr>"
								+ "</table>"
								+ "</div>"
								+ "</td></tr>"
								+ "</table>"
								+ "</td></tr>"
								+ "</table></div></td>"
								+ "</tr>"
								+ "</table>"
								+ "<div style='height: 30px; line-height: 30px; font-size: 10px;'>&nbsp;</div>"
								+ "</td></tr>"
								+ "<tr><td align='center' bgcolor='#f8f8f8'>"
								+ "<table width='90%' border='0' cellspacing='0' cellpadding='0'>"
								+ "<tr><td align='center'>"
								+ "<div style='height: 60px; line-height: 60px; font-size: 10px;'>&nbsp;</div>"
								+ "<div style='line-height: 44px;'>"
								+ "<font face='Arial, Helvetica, sans-serif' size='5' color='#6b6b6b' style='font-size: 34px;'>"
								+ "<span style='font-family: Arial, Helvetica, sans-serif; font-size: 34px; color: #6b6b6b;'>"
								+ "Hello <strong> "+" "+values.get("nom")[0]+"</strong>"
								+ "</span></font>"
								+ "</div>"
								+ "<div style='height: 20px; line-height: 20px; font-size: 10px;'>&nbsp;</div>"
								+ "</td></tr>"
								+ "<tr><td align='center'>"
								+ "<div style='line-height: 24px;'>"
								+ "<font face='Arial, Helvetica, sans-serif' size='4' color='#9c9c9c' style='font-size: 15px;'>"
								+ "<span style='font-family: Arial, Helvetica, sans-serif; font-size: 15px; color: #9c9c9c;'>"
								+ "<b>Nous vous remercierons infiniment pour votre registration, vous pouvez accéder à votre application LOGIECHANGE on clickon sur cette URL: </b>"+"<b>"+"<a href='localhost:9000/authentification'>LOGI-ECHANGE</a>"+"</b> "
								+ "<br/> Remarque: vous pouvez à tout moment modifier toutes vos informations."
								+ "</span></font>"
								+ "</div>"
								+ "<div style='height: 50px; line-height: 50px; font-size: 10px;'>&nbsp;</div>"
								+ "</td></tr>"
								+ "</table>"
								+ "</td></tr>"
								+ "<tr><td class='iage_footer' align='center' bgcolor='#ffffff'>"
								+ "<table width='100%' border='0' cellspacing='0' cellpadding='0'>"
								+ "<tr><td align='center'>"
								+ "<font face='Arial, Helvetica, sans-serif' size='3' color='#96a5b5' style='font-size: 13px;'><br/>"
								+ "<span style='font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #96a5b5;'>"
								+ "2016 &copy; LOGILAN MAROC. ALL Rights Reserved."
								+ "</span></font>"
								+ "</td></tr>"
								+ "</table>"
								+ "</td></tr>"
								+ "<tr><td>"
								+ "</td></tr>"
								+ "</table>"
								+ "</td></tr>"
								+ "</table>"
								+ "</div>"
								+ "</body>" + "</html>";
						int a=mailToModerator("Bienvenue chez LogiLan", message, "",values.get("email")[0]);
						int b=mailToModerator("Bienvenue chez LogiLan", message, "","logiechange@gmail.com");
			        
			return Results.ok(Json.toJson(newUser));
			}
		else
		{
			return Results.badRequest();
		}
	
	}
		  public  Result updateProfilePicture() throws IOException{
				 
			   String photo= uploadPicture("picture");
			    if(photo==""){
			    	photo=htmlUrl+"default.jpg";
			    } 
			    Utilisateur user = Authentification.getUser();
			    user.setPhoto(photo);
			    Ebean.update(user);
				 return Results.ok();
					
		  }
		
public  int mailToModerator(String sujet, String message,
			String src_joint, String to) throws InterruptedException {


		SimpleEmail email = new SimpleEmail();
		try {
			email.setHostName("smtp.googlemail.com");
			String encodingOptions = "text/html; charset=UTF-8";
			email.setSmtpPort(465);
			email.setSSLOnConnect(true);
			email.setFrom("logiechange@gmail.com");
			email.setAuthentication("LogiEchange", "A123456789++");
			email.addTo(to);
			email.setSubject(sujet);
			email.setMsg(message);
			email.addHeader("Content-Type", encodingOptions);
			email.send();
			return 1;
		} catch (EmailException e) {
			e.printStackTrace();
			return 0;
		}

	}


		public  String uploadPicture(String fileinput) throws IOException {


			play.mvc.Http.MultipartFormData body = Controller.request().body()
					.asMultipartFormData();
			if (body.getFile(fileinput) == null) {
				return htmlUrl+"default.jpg";
			} else {
				FilePart picture = body.getFile(fileinput);
				File file = (File) picture.getFile();
				String fullUrl= url+Authentification.getUser().getId()+".jpg";
				String htmlFullUrl= htmlUrl+Authentification.getUser().getId()+".jpg";
				FileInputStream imageInFile = new FileInputStream(file);
				FileOutputStream output = new FileOutputStream(fullUrl);
				byte imageDatap[] = new byte[(int) file.length()];
				imageInFile.read(imageDatap);
				output.write(imageDatap);
				output.close();
				imageInFile.close();
				return htmlFullUrl;
			}
		}
		
		public  Result updateUser() {
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
				int id= Integer.parseInt(values.get("id")[0]);
				Utilisateur existantUser =Ebean.find(Utilisateur.class,id);
				existantUser.setIdentifiant(values.get("login")[0]);
				existantUser.setNom(values.get("nom")[0]);
				existantUser.setPrenom(values.get("prenom")[0]);
				existantUser.setTel(values.get("tel")[0]);
				existantUser.setEmail(values.get("email")[0]);
				existantUser.setRole(values.get("role")[0]);
				if(!values.get("npass")[0].equals(""))
				{
					existantUser.setPassword(BCrypt.hashpw(values.get("npass")[0], BCrypt.gensalt()));
				}
				existantUser.setMac(values.get("mac")[0]);
				Ebean.update(existantUser);
				return Results.ok(Json.toJson(existantUser));	
			}
			catch(Exception Ex)
			{
				return Results.badRequest();
			}
			
		}

	


	public  Utilisateur getUser(int id){
	 //public static Utilisateur getUser(String nom,String prenom){
		 Utilisateur user = null ;
		    
			//if((Ebean.find(Utilisateur.class).where().eq("nom",nom).findUnique()!=null) && (Ebean.find(Utilisateur.class).where().eq("prenom",prenom).findUnique()!=null) )
		    if(Ebean.find(Utilisateur.class).where().eq("id",id).findUnique()!=null);		
			{
		    //user = Ebean.find(Utilisateur.class).where().eq("nom",nom).eq("prenom",prenom).findUnique();
		    user = Ebean.find(Utilisateur.class).where().eq("id",id).findUnique();
			}
			return user;
	 }
	 
	
	public  Result getUserJson()
	{
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		Utilisateur user = getUser(id);
		return Results.ok(Json.toJson(user));
	}
	
	 public  Result getAllUsers(){
		 Utilisateur user = Authentification.getUser(session().get("user"));
		 try{
			 
		 	List<Utilisateur> AllUser = Ebean.find(Utilisateur.class).orderBy("id asc").findList();
		 	
		 	if (user.getRole().equals("Admin")){
		 		 List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
		 		return Results.ok(views.html.listUsers.render(user, AllUser,notifications));
		 	}
		 	else if(user.getRole().equals("AdminOfficine")){
		 		
		 		List<Officine> officines=new ArrayList();
		 		GroupOfficine groupe=new GroupOfficine();
		 		if(Ebean.find(GroupOfficine.class).where().eq("user", user).findUnique()!=null){
		 			groupe= Ebean.find(GroupOfficine.class).where().eq("user", user).findUnique();
		 			if(groupe!=null){
			 			officines=groupe.getOfficines();
			 		}
		 		}
		 		
		 		List<Officine> allOfficine=Ebean.find(Officine.class).findList();
		 		
		 		
		 		java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		    	java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		 		return Results.ok(views.html.UsersAdminPh.render(user, officines,allOfficine,notifications,notificationsLu));
		 	}
			 else return Results.ok(views.html.error_404.render());	
		 }
		catch (Exception Ex)
		{
			return Results.redirect("/");
		}
	 }
	 
	 	    	public  Result deleteUser() {
	    		    		
				try{
	    		final Map<String, String[]> values = request().body().asFormUrlEncoded();
	    		int id= Integer.parseInt(values.get("id")[0]);
	    		
	    		Utilisateur user = Ebean.find(Utilisateur.class,id);
	    		Ebean.delete(user);
	    		return ok();
				}
				catch (Exception Ex)
				{
					return Results.badRequest();
				}
	    	}
			
			
	 
	 
}
