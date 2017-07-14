
package controllers;






import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Ebean;





import models.UserNotif;
import models.Utilisateur;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;

public class Authentification extends Controller {
	static Pattern macpt = null;
	 public  Result authentif() throws UnknownHostException, SocketException
	 {
		 session().clear();
		 
		 DynamicForm form = Form.form().bindFromRequest();
		 Utilisateur logedUser= new Utilisateur();
		 String identifiant = form.get("Login");
	     String password = form.get("MotDePasse");
	     String password_valid = getPass(identifiant);
		 if(password_valid!="")
	     if(BCrypt.checkpw(password, password_valid))
	     {		
	    	
	    	 logedUser= (Utilisateur) Ebean.find(Utilisateur.class).where().eq("identifiant", identifiant).findUnique();
	    	 if(logedUser.getStatus().equalsIgnoreCase("Actif")){
	    		 
	    		 if(logedUser.getRole().equals("Admin"))
	    		 {
	    			 session("user",logedUser.getIdentifiant());
	    			 List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
	     			 return Results.ok(main.render(logedUser, notifications));
		    	     
	    		 }
	    		 else{ 
	    			 
	    			
	    			 if(logedUser.getRole().equals("Pharmacien"))
	    			  {
	    						 
		    			 session("user",logedUser.getIdentifiant());
		    			 java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		         		 java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		         		 
	    	    		 return Results.ok(main1.render("OFFICINE",logedUser,notifications,notificationsLu)); 
	    		 		}
	    			 else if(logedUser.getRole().equals("AdminOfficine")){
	    				 session("user",logedUser.getIdentifiant());
	    				 java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		         		 java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		    			  return Results.ok(main2.render("Administrateur des Officines",logedUser,notifications,notificationsLu));
	    				 
	    			 }
	    			 else return Results.badRequest(authentification.render(3));
	    		 	}
	    	 }
	    	 else if(logedUser.getStatus().equalsIgnoreCase("Inactif")){
	    			 return Results.badRequest(authentification.render(2));
	    	 }
	    	 	 
	     }
		return Results.badRequest(authentification.render(1));
	 }
     
	  public  Result getAuthentification(){
		  	session().clear();
		    return Results.ok(authentification.render(0));
	  	}
	  public  Result getLogiPharm(){
		  return Results.ok(views.html.logiPharm.render());
	  	}
	  public  Result getWinHosp(){
		  return Results.ok(views.html.winHosp.render());
	  	}
	  public  Result getWinDistr(){
		  return Results.ok(views.html.winDistr.render());
	  	}
	  public  Result getWinCom(){
		  return Results.ok(views.html.winCom.render());
	  	}
	  
	   public  String getPass(String login)
	 {
		try
		{
	    Utilisateur user = (Utilisateur) Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique();
	    return user.getPassword();
		}
		catch(Exception Ex){
			return "";
		}
		 
	 }
	 
	 
	 public static Utilisateur getUser(String login)
	 {
		    
			try{
		     Utilisateur user = (Utilisateur) Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique();
		     return user;
			}
			catch (Exception Ex)
			{ return null;}
			
	 }
	 
	 public  static Utilisateur getUser()
	 {
		    Utilisateur user = null ;
			String login = session().get("user");
		    
			if(Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique()!=null)
			{	   
		     user = (Utilisateur) Ebean.find(Utilisateur.class).where().eq("identifiant",login).findUnique();
			}
			return user;
	 }
	 
	 
	
	
}

