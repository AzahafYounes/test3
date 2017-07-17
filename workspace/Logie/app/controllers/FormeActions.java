package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;






import models.Forme;
import models.Fournisseur;
import models.UserNotif;
import models.Utilisateur;

import com.avaje.ebean.Ebean;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

public class FormeActions extends Controller{
	

	
	
	 public Result getAllFormes(){
		 Utilisateur user = Authentification.getUser(session().get("user"));
		 try{
			 
		 	java.util.List<Forme> allForme = Ebean.find(Forme.class).orderBy("idForme asc").findList();
		 	
		 	if (user.getRole().equals("Admin")){
		 		List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
		 		return Results.ok(views.html.formes.render(user, allForme,notifications));
		 	}else 
				 return Results.ok(views.html.error_404.render());	
		 }
		catch (Exception Ex)
		{
			return Results.redirect("/");
		}
	 }
	 
	 
	 
		public Result addNewForme() throws Exception {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		if(Ebean.find(Forme.class).where().eq("foDesig", values.get("foDesig")[0]).findUnique()==null)
		{
		
		Forme newForme =new Forme(values.get("foDesig")[0]);
		
		Ebean.save(newForme);
		
				  
					
			
		        
		return Results.ok();
		}
	else
	{
		return Results.badRequest();
	}
	
		}

		



		
		
		public Result updateForme() {
			try{
				final Map<String, String[]> values = request().body().asFormUrlEncoded();
				int id= Integer.parseInt(values.get("id")[0]);
				Forme existantForme =Ebean.find(Forme.class,id);
				
				
				
				existantForme.setFoDesig(values.get("foDesig")[0]);
				
				
				Ebean.update(existantForme);
				return Results.ok(Json.toJson(existantForme));	
			}
			catch(Exception Ex)
			{
				return Results.badRequest();
			}
			
		}

	


	public static Forme getForme(int id){
	 
		 Forme forme = null ;
		    
			if(Ebean.find(Forme.class).where().eq("idForme",id).findUnique()!=null);		
			{
		    forme = Ebean.find(Forme.class).where().eq("idForme",id).findUnique();
			}
			return forme;
	 }
	 
	
	public Result getFormeJson()
	{
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		
		Forme forme = getForme(id);
		return Results.ok(Json.toJson(forme));
	}
	
	
	 
	  public Result deleteForme() {
	    		    		
				try{
	    		final Map<String, String[]> values = request().body().asFormUrlEncoded();
	    		int id= Integer.parseInt(values.get("id")[0]);
	    		
	    		Forme forme = Ebean.find(Forme.class,id);
	    		Ebean.delete(forme);
	    		return ok();
				}
				catch (Exception Ex)
				{
					return Results.badRequest();
				}
	    	}
			
		
	  public Result importFormes(){
		 
	   		try{
	   			System.out.println("aaaaaaaaaaa");
	   			DynamicForm form= Form.form().bindFromRequest();
	   			Utilisateur user = Authentification.getUser(session().get("user"));
	   			List<Forme> formes =Ebean.find(Forme.class).findList();
	   			Forme forme=new Forme();
	   			String nom ,FOCODE; 
	   			
	   			File file;
	   			MultipartFormData body = Controller.request().body().asMultipartFormData();
	   			if(body.getFile("fichier")==null){
	   				return ok("noFichier");
	   			
	   			}else{
	   				System.out.println("bbbbbbbbbb");
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
	   					System.out.println(nom);
	   					 FOCODE=parts[1].trim();
	   					System.out.println(FOCODE);
	   					 
	   					 
	   					 
	   					 if(Ebean.find(Forme.class).where().eq("foDesig", nom).findUnique()==null)
	   						{
	   						 
	   						 
	   						 Forme newForme =new Forme(nom);
	   						 
	   						 newForme.setFOCODE(FOCODE);
	   						 System.out.println(newForme);
	   						 Ebean.save(newForme);
	   						 formes.add(newForme);
	   						 
	   						}
	   				}
	   				br.close(); 
	   				
	   			}
	   			
	   			List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
	   			return Results.ok(views.html.formes.render(user, formes,notifications));
	   		}
	   		catch(Exception Ex)
	   		{
	   			return Results.badRequest();
	   		}
	   		
	   	
	   	
	   		
	   	}
	   	
	 

}
