package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.*;

import com.avaje.ebean.CallableSql;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

public class ProduitActions extends Controller {
	
	public Result getAllProduit() {
	try{

		Utilisateur user = Authentification.getUser(session().get("user"));
		List<Produit> produits =Ebean.find(Produit.class).where().orderBy("nom asc").findList();
		List<Fournisseur> fournisseurs =Ebean.find(Fournisseur.class).where().orderBy("nom asc").findList();
		List<Forme> formes =Ebean.find(Forme.class).where().orderBy("foDesig asc").findList();
		List<FamilleTarif> familleTarifs =Ebean.find(FamilleTarif.class).where().orderBy("nom asc").findList();
		
		if(user.getRole().equals("Admin")){
			List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
			return Results.ok(views.html.lProduits.render(user, produits,fournisseurs,formes,familleTarifs,notifications));
		}else 
			 return Results.ok(views.html.error_404.render());
	}
	catch(Exception Ex)
		{
			return Results.redirect("/");
		}
	
	}
	
	public Result getByCaractere(String caractere) {
		try{
			System.out.println(caractere);
			Utilisateur user = Authentification.getUser(session().get("user"));
			List<Produit> produits =Ebean.find(Produit.class).where().like("nom", caractere+'%').orderBy("nom asc").findList();
			
			List<Fournisseur> fournisseurs =Ebean.find(Fournisseur.class).where().orderBy("nom asc").findList();
			List<Forme> formes =Ebean.find(Forme.class).where().orderBy("foDesig asc").findList();
			List<FamilleTarif> familleTarifs =Ebean.find(FamilleTarif.class).where().orderBy("nom asc").findList();
			
			if(user.getRole().equals("AdminOfficine") || user.getRole().equals("Pharmacien")){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		 
			
				return Results.ok(views.html.ProduitsAdminPh.render(user, produits,notifications,notificationsLu));
			}
			else if(user.getRole().equals("Admin")){
				List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
				return Results.ok(views.html.lProduits.render(user, produits,fournisseurs,formes,familleTarifs,notifications));
			}else 
				 return Results.ok(views.html.error_404.render());
		}
		catch(Exception Ex)
			{
				return Results.redirect("/");
			}
		
		}

	
	

	public Result addNewProduit() {
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idFournisseur = Integer.parseInt(values.get("fournisseur")[0]);
		float prix;
		String nom,codeBarre;
		int idforme =Integer.parseInt(values.get("forme")[0]);
		int idfamilleTarif =Integer.parseInt(values.get("familleTarif")[0]);
		Utilisateur user = Authentification.getUser(session().get("user"));		
		prix=Float.parseFloat(values.get("prix")[0]);
		
		nom=values.get("nom")[0];
		
		codeBarre=values.get("codeBarre")[0];
		Date actuelle = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		 String dat = dateFormat.format(actuelle);
		if(Ebean.find(Produit.class).where().eq("nom", nom).findUnique()==null)
		{
		
		Fournisseur fournisseur=Ebean.find(Fournisseur.class).where().eq("id",idFournisseur).findUnique();
		Forme forme=Ebean.find(Forme.class).where().eq("idForme",idforme).findUnique();
		FamilleTarif familleTarif=Ebean.find(FamilleTarif.class).where().eq("idFamilleT",idfamilleTarif).findUnique();
		
		Produit newProduit =new Produit(fournisseur,nom, prix,codeBarre,forme ,familleTarif,Integer.parseInt(values.get("isNew")[0]),actuelle,user);
		
		newProduit.setFamilleTarif(familleTarif);
		newProduit.setForme(forme);
		newProduit.setFournisseur(fournisseur);
		Ebean.save(newProduit);
		
		return Results.ok();
		
		}else
		{
			
			return Results.badRequest();
		}
		
		
		
		
	}
	
	

	public Result deleteProduit() {
		try{
			
    		final Map<String, String[]> values = request().body().asFormUrlEncoded();
    		int id= Integer.parseInt(values.get("id")[0]);
    		
    		Produit produit = Ebean.find(Produit.class,id);
    		
    		
    		Ebean.delete(produit);
    		return ok();
			}
			catch (Exception Ex)
			{
				return Results.badRequest();
			}
		
	}
	
	
	public static Produit getProduit(int id){
		
		
		 Produit produit = null ;
		    
			if(Ebean.find(Produit.class).where().eq("id",id).findUnique()!=null);		
			{
		    produit = Ebean.find(Produit.class).where().eq("id",id).findUnique();
			}
			return produit;
	 }
	
	
	public Result getByFiltre(String caractere)
	{
		
		System.out.println(caractere);
		List<Produit> produits =Ebean.find(Produit.class).where().like("nom", caractere+'%').orderBy("nom asc").findList();
		
		
		
		return Results.ok(Json.toJson(produits));
	}
	public Result getProduitJson()
	{
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		
		Produit produit = getProduit(id);
		
		return Results.ok(Json.toJson(produit));
	}
	
	
	public Result updateProduit() {
		try{
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			int id= Integer.parseInt(values.get("id")[0]);
			Produit existantProduit =Ebean.find(Produit.class,id);
			
			Fournisseur fournisseur = Ebean.find(Fournisseur.class,Integer.parseInt(values.get("fournisseur")[0]));
			Forme forme=Ebean.find(Forme.class).where().eq("idForme",Integer.parseInt(values.get("forme")[0])).findUnique();
			FamilleTarif familleTarif=Ebean.find(FamilleTarif.class).where().eq("idFamilleT",Integer.parseInt(values.get("familleTarif")[0])).findUnique();
			existantProduit.setNom(values.get("nom")[0]);
			
			existantProduit.setForme(forme);
			existantProduit.setPrix(Float.parseFloat(values.get("prix")[0]));
			existantProduit.setCodeBarre(values.get("codeBarre")[0]);
			existantProduit.setFournisseur(fournisseur);
			existantProduit.setFamilleTarif(familleTarif);
			
			Ebean.update(existantProduit);
			return Results.ok(Json.toJson(existantProduit));	
		}
		catch(Exception Ex)
		{
			return Results.badRequest();
		}
		
	}
	
	public Result importProduits(){
		
		try{
			
			DynamicForm form= Form.form().bindFromRequest();
			Utilisateur user = Authentification.getUser(session().get("user"));
			List<Produit> produits =Ebean.find(Produit.class).where().orderBy("nom asc").findList();
			List<Fournisseur> fournisseurs =Ebean.find(Fournisseur.class).where().orderBy("nom asc").findList();
			List<Forme> formes =Ebean.find(Forme.class).where().orderBy("foDesig asc").findList();
			List<FamilleTarif> familleTarifs =Ebean.find(FamilleTarif.class).where().orderBy("nom asc").findList();
			Produit produit=new Produit();
			Fournisseur fournisseur=new Fournisseur();
			Forme forme= new Forme();
			FamilleTarif familleTarif=new FamilleTarif();
			int isNew;
			Date actuelle=new Date();
			String nom ,codeBarre,prix,fournisseurS,formeS,familleTarifS,prixpph; 
			
			File file;
			MultipartFormData body = Controller.request().body().asMultipartFormData();
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
					 formeS=parts[1].trim();
					 fournisseurS=parts[2].trim();
					 familleTarifS=parts[3].trim();
					 prix = parts[4].trim();
					 prixpph=parts[5].trim();
					 codeBarre = parts[6].trim(); 
					 
					 
					 isNew=0;
					 if(Ebean.find(Produit.class).where().eq("nom", nom).findUnique()==null)
						{
						 if(fournisseurS!=""){
							 fournisseur= Ebean.find(Fournisseur.class).where().eq("FRCODE", fournisseurS).findUnique();
						 }
						 if(formeS!=""){
							 forme= Ebean.find(Forme.class).where().eq("FOCODE", formeS).findUnique();
						 }
						 if(familleTarifS!=""){
							 familleTarif = Ebean.find(FamilleTarif.class).where().eq("ftCode", familleTarifS).findUnique();
						 }
						 if(fournisseur==null){
							 fournisseur= Ebean.find(Fournisseur.class).where().eq("FRCODE", "Aucun").findUnique(); 
						 }
						 if(forme==null){
							 forme= Ebean.find(Forme.class).where().eq("FOCODE", "Aucun").findUnique();
						 }
						 if(familleTarif==null){
							 familleTarif = Ebean.find(FamilleTarif.class).where().eq("ftCode", "Aucun").findUnique();
						 }
						 
						 Produit newProduit =new Produit(fournisseur, nom, Float.parseFloat(prix), codeBarre, forme, familleTarif, isNew, actuelle, user);
						 
						 newProduit.setPph(Float.parseFloat(prixpph));
						 	
						 Ebean.save(newProduit);
						 produits.add(newProduit);
						 
						}
				}
				br.close(); 
				
			}
			
			List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
			return Results.redirect("/Produits/A");
		}
		catch(Exception Ex)
		{
			return Results.badRequest();
		}
		
	
	
		
	}
	//Produit Officine
	public Result getAllProduitPh() {
		try{

			Utilisateur user = Authentification.getUser(session().get("user"));
			List<Produit> produits =Ebean.find(Produit.class).where().orderBy("nom asc").findList();
			if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine") ){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		return Results.ok(views.html.ProduitsAdminPh.render(user, produits,notifications,notificationsLu));
			}
			else 
				 return Results.ok(views.html.error_404.render());
		}
		catch(Exception Ex)
			{
				return Results.redirect("/");
			}
		}


	
	
	
		 
}
