	package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Ebean;

import models.FamilleTarif;
import models.Forme;
import models.Fournisseur;
import models.Produit;
import models.UserNotif;
import models.Utilisateur;
import play.api.mvc.MultipartFormData;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Results;

public class FournisseurAccount extends Controller {
	
	

	
	
	
	
	
	
	
	
	 public Result getAllFournisseurs(){
		 Utilisateur user = Authentification.getUser(session().get("user"));
		 try{
			 
		 	java.util.List<Fournisseur> allFournisseur = Ebean.find(Fournisseur.class).orderBy("id asc").findList();
		 	
		 	if (user.getRole().equals("Admin")){
		 		List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
		 		return Results.ok(views.html.fournisseurs.render(user, allFournisseur,notifications));
		 	}else 
				 return Results.ok(views.html.error_404.render());	
		 }
		catch (Exception Ex)
		{
			return Results.redirect("/");
		}
	 }
	 
	 
	 
		public Result addNewFournisseur() throws Exception {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		
		if(Ebean.find(Fournisseur.class).where().eq("nom", values.get("nom")[0]).findUnique()==null)
		{
		
		Fournisseur newFournisseur =new Fournisseur(values.get("nom")[0], values.get("email")[0], values.get("adress")[0], values.get("tel")[0], values.get("fix1")[0], values.get("fix2")[0], "", values.get("fax")[0]);
		
		Ebean.save(newFournisseur);
		
				  
					
			
		        
		return Results.ok();
		}
	else
	{
		return Results.badRequest();
	}
	
		}

		



		
		
		public Result updateFournisseur() {
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
				int id= Integer.parseInt(values.get("id")[0]);
				Fournisseur existantFournisseur =Ebean.find(Fournisseur.class,id);
				
				
				
				existantFournisseur.setNom(values.get("nom")[0]);
				existantFournisseur.setEmail(values.get("email")[0]);
				existantFournisseur.setAdress(values.get("adress")[0]);
				existantFournisseur.setTel(values.get("tel")[0]);
				existantFournisseur.setFix1(values.get("fix1")[0]);
				existantFournisseur.setFix2(values.get("fix2")[0]);
				existantFournisseur.setFix3("");
				existantFournisseur.setFax(values.get("fax")[0]);
				
				Ebean.update(existantFournisseur);
				return Results.ok(Json.toJson(existantFournisseur));	
			}
			catch(Exception Ex)
			{
				return Results.badRequest();
			}
			
		}

	


	public static Fournisseur getFournisseur(int id){
	 
		 Fournisseur fournisseur = null ;
		    
			if(Ebean.find(Fournisseur.class).where().eq("id",id).findUnique()!=null);		
			{
		    fournisseur = Ebean.find(Fournisseur.class).where().eq("id",id).findUnique();
			}
			return fournisseur;
	 }
	 
	
	public Result getFournisseurJson()
	{
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		
		Fournisseur fournisseur = getFournisseur(id);
		return Results.ok(Json.toJson(fournisseur));
	}
	
	
	 
	 	    	public Result deleteFournisseur() {
	    		    		
				try{
	    		final Map<String, String[]> values = request().body().asFormUrlEncoded();
	    		int id= Integer.parseInt(values.get("id")[0]);
	    		
	    		Fournisseur fournisseur = Ebean.find(Fournisseur.class,id);
	    		Ebean.delete(fournisseur);
	    		return ok();
				}
				catch (Exception Ex)
				{
					return Results.badRequest();
				}
	    	}
			
//Espace OFFICINE
	 	    	 public Result getAllFournisseursPh(){
	 	   		 Utilisateur user = Authentification.getUser(session().get("user"));
	 	   		 try{
	 	   			 
	 	   		 	java.util.List<Fournisseur> allFournisseur = Ebean.find(Fournisseur.class).orderBy("id asc").findList();
	 	   		 	
	 	   		 	if (user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
	 	   		 	java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	 	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	 	    		 
	 	   		 		return Results.ok(views.html.listFournisseurAdminPh.render("Officine",user, allFournisseur,notifications,notificationsLu));
	 	   		 		
	 	   		 	}
	 	   		 		
	 	   		 	else
	 	   				 return Results.ok(views.html.error_404.render());	
	 	   		 }
	 	   		catch (Exception Ex)
	 	   		{
	 	   			return Results.redirect("/");
	 	   		}
	 	   	 }
	 
	 	    	 
	 	    	public Result importFournisseurs(){
	 	   		
	 	   		try{
	 	   			
	 	   			DynamicForm form= Form.form().bindFromRequest();
	 	   			Utilisateur user = Authentification.getUser(session().get("user"));
	 	   			List<Fournisseur> fournisseurs =Ebean.find(Fournisseur.class).where().orderBy("nom asc").findList();
	 	   			Fournisseur fournisseur=new Fournisseur();
	 	   			String nom ,email,adress,tel,fix1,fix2,fix3,fax,FRCODE; 
	 	   			
	 	   			File file;
	 	   			play.mvc.Http.MultipartFormData<Object> body = Controller.request().body().asMultipartFormData();
	 	   			if(body.getFile("fichier")==null){
	 	   				return ok("noFichier");
	 	   			
	 	   			}else{
	 	   				FilePart fichier =body.getFile("fichier");
	 	   				file=(File) fichier.getFile();
	 	   				InputStream ips=new FileInputStream(file); 
	 	   				InputStreamReader ipsr=new InputStreamReader(ips);
	 	   				BufferedReader br=new BufferedReader(ipsr);
	 	   				String ligne;
	 	   				
	 	   				while ((ligne=br.readLine())!=null){
	 	   					System.out.println(ligne);
	 	   					String[] parts = ligne.split(";");
	 	   					 nom = parts[0].trim(); 
	 	   					 email=parts[1].trim();
	 	   					 adress=parts[2];
	 	   					 tel=parts[3].trim();
	 	   					 fix1 = parts[4].trim();
	 	   					 fix2=parts[5].trim();
	 	   					 fix3 = parts[6].trim(); 
	 	   					 fax = parts[7].trim();
	 	   				     FRCODE = parts[8].trim();
	 	   					 
	 	   					 
	 	   					 if(Ebean.find(Fournisseur.class).where().eq("nom", nom).findUnique()==null)
	 	   						{
	 	   						 
	 	   						 
	 	   						 Fournisseur newFournisseur =new Fournisseur(nom, email, adress, tel, fix1, fix2, fix3, fax);
	 	   						 
	 	   						 newFournisseur.setFRCODE(FRCODE);
	 	   						 System.out.println(newFournisseur);	
	 	   						 Ebean.save(newFournisseur);
	 	   						 fournisseurs.add(newFournisseur);
	 	   						 
	 	   						}
	 	   				}
	 	   				br.close(); 
	 	   				
	 	   			}
	 	   			
	 	   		List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
	 	   			return Results.ok(views.html.fournisseurs.render(user, fournisseurs,notifications));
	 	   		}
	 	   		catch(Exception Ex)
	 	   		{
	 	   			return Results.badRequest();
	 	   		}
	 	   		
	 	   	
	 	   	
	 	   		
	 	   	}
	 	   	
	 
}
