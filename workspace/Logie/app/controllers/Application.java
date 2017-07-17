package controllers;

import java.util.List;





import models.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	int i = 1 ;
	
	public  Result index1() {
        return Results.ok(views.html.index.render());
	}

	public  Result getInscription() {
		return Results.ok(views.html.prix.render());
	}



    public  Result index() {
    	Utilisateur user = Authentification.getUser();
    	if(user==null)
    	{
    	return redirect(routes.Authentification.getAuthentification());
    	}
    	else
    	{
    		if(user.getRole().equals("Admin")){
    			List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
    			return Results.ok(main.render(user, notifications));
    		}
    		else if(user.getRole().equals("Pharmacien")){
    			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
        		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
        		 
    			return Results.ok(main1.render("Index Pharmaciens",user,notifications,notificationsLu));
    		} 
    		else{
    			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
        		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
        		 
    			return Results.ok(main2.render("Administrateur Officine",user,notifications,notificationsLu));
    		} 
    	}
    }   
    
}
