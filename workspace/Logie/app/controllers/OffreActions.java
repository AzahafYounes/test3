package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.avaje.ebean.Expr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;

import models.Brouillon;
import models.Fournisseur;
import models.GroupOfficine;
import models.Livraison;
import models.Observation;
import models.Officine;
import models.OffreOfficine;
import models.Produit;
import models.PropoLivree;
import models.Proposition;
import models.UserNotif;
import models.Utilisateur;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;



public class OffreActions extends Controller {
	private long CONST_DURATION_OF_DAY = 1000l * 60 * 60 * 24;
	static List<SqlRow> ListRech = null;
	public Result getAllOffres()throws ParseException,IOException {
		Utilisateur logedUser = Authentification.getUser();
		List<Produit> produits = Ebean.find(Produit.class).findList();
		List<Fournisseur> fournisseurs = Ebean.find(Fournisseur.class).findList();
		List<Utilisateur> users=Ebean.find(Utilisateur.class).findList();
		List<Officine> officines = Ebean.find(Officine.class).findList();
		List<GroupOfficine> groupe=Ebean.find(GroupOfficine.class).findList();
		List<OffreOfficine> offres=Ebean.find(OffreOfficine.class).orderBy("id desc").findList();
		List<OffreOfficine> offres2=new ArrayList<>();
		List<GroupOfficine> groupes = Ebean.find(GroupOfficine.class).findList();
		List<GroupOfficine> groupes2 = new ArrayList<>();
		GroupOfficine gr=Ebean.find(GroupOfficine.class).where().eq("user", logedUser).findUnique();
		if(logedUser.getRole().equals("Admin")){
			List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
			return Results.ok(views.html.listOffresAdmin.render(logedUser,produits,fournisseurs,users,officines,groupe,offres,notifications));
		}
		if(logedUser.getRole().equals("Pharmacien") || logedUser.getRole().equals("AdminOfficine")){
			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
    		Officine officine = Ebean.find(Officine.class).where().eq("user", logedUser).findUnique();
    		if(groupes!=null){
    			for(int i=0;i<groupes.size();i++){
    				for(int j=0;j<groupes.get(i).getOfficines().size();j++){
    					if(groupes.get(i).getOfficines().get(j).getId()==officine.getId()){
    						groupes2.add(groupes.get(i));
    					}
    				}
    			}
    		}
    		for(int i=0;i<groupes2.size();i++){
    			offres=Ebean.find(OffreOfficine.class).where().eq("groupe", groupes2.get(i)).orderBy("id desc").findList();
    			for(int j=0;j<offres.size();j++){
    				offres2.add(offres.get(j));
    			}
    			
    		}
    		Date date2=new Date();
    		for(int i=0;i<offres2.size();i++){
    			
    			long diff = offres2.get(i).getDateLimit().getTime() -date2.getTime() ;
        		long numberOfDay = (long)diff/CONST_DURATION_OF_DAY;
        		
        		if(numberOfDay<0){
        			offres2.get(i).setExpiree(1);
        			Ebean.update(offres2.get(i));
        		}else if(numberOfDay<10){
        			offres2.get(i).setExpiree(2);
        			Ebean.update(offres2.get(i));
        		}
    		}
    		
    		return Results.ok(views.html.listOffreAdminPh.render(logedUser,produits,fournisseurs,officines,offres2,notifications,notificationsLu));
		}
		
		return Results.redirect("/");
			
	}
	
	public Result livraisonOffre(String id)throws ParseException,IOException {
		Utilisateur logedUser = Authentification.getUser();
		int idOffre= Integer.parseInt(id);
		List<Livraison> livraisons=new ArrayList<>();
		Livraison livraison=new Livraison();
		if(Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique()!=null){
			OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
			List<Brouillon> brouillons=Ebean.find(Brouillon.class).where().eq("offre", offre).findList();
			if(logedUser.getRole().equals("Pharmacien") || logedUser.getRole().equals("AdminOfficine") && logedUser.getId()==offre.getUser().getId() ){
				for(int i=0;i<brouillons.size();i++){
					if(Ebean.find(Livraison.class).where().eq("brouillon", brouillons.get(i)).findUnique()!=null){
						livraison = Ebean.find(Livraison.class).where().eq("brouillon", brouillons.get(i)).findUnique();
						livraisons.add(livraison);
					}
				}
				
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		 
	    			 return Results.ok(views.html.LivraisonOffreAdminPh.render(logedUser,offre,brouillons,livraisons,notifications,notificationsLu));
	    		 
			}
			if(logedUser.getRole().equals("Admin")){
				List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
				return Results.ok(views.html.LivraisonOffre.render(logedUser,offre,brouillons,livraisons,notifications));
			}
		}
		
	
		return Results.redirect("/");
			
	}
	
	
	
	public Result livraisonBrouillonPh(String id)throws ParseException,IOException {
		Utilisateur logedUser = Authentification.getUser();
		int idbrouillon= Integer.parseInt(id);
		
		
		if(Ebean.find(Brouillon.class).where().eq("id", idbrouillon).findUnique()!=null){
			Brouillon brouillon=Ebean.find(Brouillon.class).where().eq("id", idbrouillon).findUnique();
			if(logedUser.getRole().equals("Pharmacien") || logedUser.getRole().equals("AdminOfficine") && logedUser.getId()==brouillon.getOffre().getUser().getId()){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		Controller.response().setCookie("idLivraison", String.valueOf(0));
	    			
	    			return Results.ok(views.html.LivrerOffreAdminPh.render(logedUser,brouillon,notifications,notificationsLu));
	    		
			}
			if(logedUser.getRole().equals("Admin")){
				List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
				return Results.ok(views.html.LivrerOffre.render(logedUser,brouillon,notifications));
			}
		}
		
	
		return Results.redirect("/");
			
	}
	
	
	
	
	public  Result updatePublie()
	{
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		Long idOffre = Long.parseLong(values.get("id")[0]);
		OffreOfficine offre = Ebean.find(OffreOfficine.class,idOffre);
		if(offre.getPublie()==0)
			offre.setPublie(1);
		else offre.setPublie(0);
		Ebean.update(offre);
		return Results.ok();
	}
	
	
	
	public Result livrer() throws ParseException,IOException {
		Utilisateur logedUser = Authentification.getUser();
		DynamicForm f=play.data.Form.form().bindFromRequest();
		int idLivraison= Integer.parseInt(Controller.request().cookies().get("idLivraison").value());
		
		Map<String, String> data = f.data();
		int idBrouillon= Integer.parseInt(data.get("idBrouillon"));
		int qte;
		String nomfichier;
		List<PropoLivree> propos=new ArrayList<>();
		Brouillon brouillon=Ebean.find(Brouillon.class).where().eq("id", idBrouillon).findUnique();
		Utilisateur user2=brouillon.getUser();
		Officine officine=Ebean.find(Officine.class).where().eq("user", logedUser).findUnique();
		Officine officine2=Ebean.find(Officine.class).where().eq("user", user2).findUnique();
		if(idLivraison!=0){
			Livraison livrer=Ebean.find(Livraison.class).where().eq("id",idLivraison).findUnique();
			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
    		
    		
			return Results.ok(views.html.BonLivraison.render(logedUser,officine,officine2,livrer,notifications,notificationsLu));
		}
		
		
		if(Ebean.find(Brouillon.class).where().eq("id", idBrouillon).findUnique()!=null){
			int qteGra;
			float somme=0;
			File monFichierTexte = new File(System.getProperty("user.dir") + "/");
			String path=new File("").getAbsolutePath(); 
			
			nomfichier=path+"\\public\\livraison\\LO"+brouillon.getUser().getNom()+brouillon.getId()+".txt";
			
			PrintWriter writer =  new PrintWriter(new FileWriter(nomfichier));
			
			
			OffreOfficine offre=brouillon.getOffre();
			if(logedUser.getRole().equals("Pharmacien") || logedUser.getRole().equals("AdminOfficine") && logedUser.getId()==brouillon.getOffre().getUser().getId()){
				float totalR=0;
				float totalS=0;
				float totalPPV=0;
				for(int i=0;i<brouillon.getPropoBrouillons().size();i++){
					
					if(data.get("qte["+i+"]") != null){
						qte=Integer.parseInt(data.get("qte["+i+"]"));
						
					}else{
						qte=0;
						
					}
					
					PropoLivree propoLivree= new PropoLivree(brouillon.getPropoBrouillons().get(i).getProduit(), qte);
					for(int j=0;j<brouillon.getOffre().getPropositions().size();j++){
						somme=0;
						if(brouillon.getPropoBrouillons().get(i).getProduit().getId()==brouillon.getOffre().getPropositions().get(j).getProduit().getId()){
							propoLivree.setRemise(brouillon.getOffre().getPropositions().get(j).getRemise());
							propoLivree.setTypeOffre(brouillon.getOffre().getPropositions().get(j).getTypeOffre());
							propoLivree.setUniteGratuit(brouillon.getOffre().getPropositions().get(j).getUnitGratuit());
							if(propoLivree.getTypeOffre()==1){
								propoLivree.setTotal((qte*propoLivree.getProduit().getPph())*(1-(propoLivree.getRemise()/100)));
								propoLivree.setTotalsanRemis(qte*propoLivree.getProduit().getPph());
								
								propoLivree.setTotalPPV(qte*propoLivree.getProduit().getPrix());
							}
							else if(offre.getPropositions().get(i).getTypeOffre()==2){
								propoLivree.setTotal(qte*propoLivree.getProduit().getPph());
								qteGra=(qte*offre.getPropositions().get(i).getUnitGratuit())/offre.getPropositions().get(i).getQte();
								propoLivree.setTotalsanRemis((qte+qteGra)*propoLivree.getProduit().getPph());
								
								propoLivree.setTotalPPV((qte+qteGra)*propoLivree.getProduit().getPrix());
							}else{
								propoLivree.setTotal(qte*propoLivree.getProduit().getPph());
								propoLivree.setTotalsanRemis(qte*propoLivree.getProduit().getPph());
								
								propoLivree.setTotalPPV(qte*propoLivree.getProduit().getPrix());
							}
							totalR=totalR+propoLivree.getTotal();
							totalS=totalS+propoLivree.getTotalsanRemis();
							totalPPV=totalPPV+propoLivree.getTotalPPV();
						
						}
					}
					
					propos.add(propoLivree);
					
					writer.println(propoLivree.getProduit().getNom()+";"+propoLivree.getProduit().getCodeBarre()+";"+qte);
					
				}
				writer.close();
				
				Livraison livraison=new Livraison(new Date(), logedUser, brouillon, propos, 1);
				livraison.setTotal(totalR);
				livraison.setTotalPPV(totalPPV);
				livraison.setTotalSansRemise(totalS);
				
				
				brouillon.setValide(3);
				String objet = "Offre N° : "+offre.getId();
				String message = "Livrer ";
				Date date = new Date();
				List<Utilisateur> userDist=new ArrayList<>();
				userDist.add(brouillon.getUser());
				Observation obs = new Observation(objet, message, date);
				obs.setAdmin(logedUser);
				obs.setListPharmacien(userDist);
				Ebean.save(obs);
				for(Utilisateur commercial : userDist)
					NotificationActions.addObservation(commercial.getId());
				 NotificationActions.GetNotificationAdmin();
				 Ebean.update(brouillon);
				 livraison.setUserDist(brouillon.getUser());
				 Ebean.save(livraison);
				 Controller.response().setCookie("idLivraison", String.valueOf(livraison.getId()));
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		List<Brouillon> brouillons=Ebean.find(Brouillon.class).where().eq("offre", offre).findList();
	    		List<Livraison> livraisons =new ArrayList<>();
	    		
	    		for(int i=0;i<brouillons.size();i++){
					if(Ebean.find(Livraison.class).where().eq("brouillon", brouillons.get(i)).findUnique()!=null){
						livraison = Ebean.find(Livraison.class).where().eq("brouillon", brouillons.get(i)).findUnique();
						livraisons.add(livraison);
						
					}
				}
	    		
	    		
	    		
	    		
	    		
	    		
				return Results.ok(views.html.BonLivraison.render(logedUser,officine,officine2,livraison,notifications,notificationsLu));
			}
		}
		
		
	
		return Results.redirect("/");
			
	}
	
	
	
	
	
	
	
	public Result PageAjouterOffre(){

		Utilisateur user = Authentification.getUser(session().get("user"));
		List<Produit> produits = Ebean.find(Produit.class).findList();
		List<Fournisseur> fournisseurs = Ebean.find(Fournisseur.class).findList();
		List<Proposition> propositions =new ArrayList<>();
		Controller.response().setCookie("idOffre", String.valueOf(0));
		List<GroupOfficine> groupes = Ebean.find(GroupOfficine.class).findList();
		List<GroupOfficine> groupes2 = new ArrayList<>();
		
		try{
			if (user.getRole().equals("Admin")){
				
				return Results.ok(views.html.AjoutOffreAdmin.render(user,produits,fournisseurs,propositions));
			}
				
			else if(user.getRole().equals("AdminOfficine") || user.getRole().equals("Pharmacien")){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		Officine officine = Ebean.find(Officine.class).where().eq("user", user).findUnique();
	    		if(groupes!=null){
	    			for(int i=0;i<groupes.size();i++){
	    				for(int j=0;j<groupes.get(i).getOfficines().size();j++){
	    					if(groupes.get(i).getOfficines().get(j).getId()==officine.getId()){
	    						groupes2.add(groupes.get(i));
	    					}
	    				}
	    			}
	    		}
	    		 
				return Results.ok(views.html.AjoutOffreAdminPh.render(user,produits,fournisseurs,propositions,groupes2,notifications,notificationsLu));
			}
			else	
				return Results.ok(views.html.error_404.render());	
		}
		catch (Exception Ex)
		{
			return Results.redirect("/");
		}
	}
	
	
	public Result PageModifierOffre(String id){
		int idOffre = Integer.parseInt(id);
		Utilisateur user = Authentification.getUser(session().get("user"));
		
		List<Fournisseur> fournisseurs = Ebean.find(Fournisseur.class).findList();
		List<Proposition> propositions =new ArrayList<>();
		
		Controller.response().setCookie("idOffre", String.valueOf(idOffre));
		
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		List<Produit> produits = Ebean.find(Produit.class).where().eq("fournisseur", offre.getFournisseur()).findList();
		try{
			if (user.getRole().equals("Admin"))
				return Results.ok(views.html.modifierOffreAdmin.render(user,produits,fournisseurs,propositions,offre));
			else if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
				if(offre.getUser().getId()==user.getId()){
					java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		    		
		    		 
					return Results.ok(views.html.modifierOffrePh.render(user,produits,fournisseurs,propositions,offre,notifications,notificationsLu));
				}
				else{
					return Results.ok(views.html.error_404.render());
				}
			}
			else
				return Results.ok(views.html.error_404.render());	
		}
		catch (Exception Ex)
		{
			return Results.redirect("/");
		}
	}
	
	
	public Result getProduitsByFournisseur() throws ParseException {
		int idOffre= Integer.parseInt(Controller.request().cookies().get("idOffre").value());
		
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idFournisseur = Integer.parseInt(values.get("fournisseur")[0]);
		Fournisseur fournisseur =Ebean.find(Fournisseur.class).where().eq("id",idFournisseur).findUnique();
		Utilisateur user = Authentification.getUser(session().get("user"));
		List<Produit> produits =Ebean.find(Produit.class).where().eq("fournisseur",fournisseur).orderBy("nom asc").findList();
		if(user.getRole().equals("Admin")){
			List<GroupOfficine> groupes =Ebean.find(GroupOfficine.class).findList();
			List<Proposition> propositions = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			
			Date dateOffre=sdf.parse(values.get("dateOffre")[0]);
			Date dateLimit=sdf.parse(values.get("dateLimit")[0]);
			OffreOfficine offre= new OffreOfficine(dateOffre, dateLimit, user, fournisseur, propositions,Integer.parseInt(values.get("publier")[0]));
			offre.setAfficher(0);
			for(int i=0;i<groupes.size();i++){
				offre.setGroupe(groupes.get(i));
				Ebean.save(offre);
			}
			
				Controller.response().setCookie("idOffre", String.valueOf(offre.getId()));
		}
			
		else{
		
		if(idOffre==0){
			int idGroupe = Integer.parseInt(values.get("groupe")[0]);
			GroupOfficine groupe =Ebean.find(GroupOfficine.class).where().eq("id",idGroupe).findUnique();
			List<Proposition> propositions = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			
			Date dateOffre=sdf.parse(values.get("dateOffre")[0]);
			Date dateLimit=sdf.parse(values.get("dateLimit")[0]);
			OffreOfficine offre= new OffreOfficine(dateOffre, dateLimit, user, fournisseur, propositions,Integer.parseInt(values.get("publier")[0]));
			offre.setAfficher(0);
			offre.setGroupe(groupe);
			
			
				Ebean.save(offre);
				
				Controller.response().setCookie("idOffre", String.valueOf(offre.getId()));
			}
		
		}
		
		return Results.ok(Json.toJson(produits));
		
	}
	
	public Result addNewOffre() {
	
		return Results.ok();
			
	}
	
	public Result deleteOffre() {
		List<SqlRow> req ;
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", Integer.parseInt(values.get("id")[0])).findUnique();
		Utilisateur user = Authentification.getUser(session().get("user"));
		int verifier=0;
		if(Ebean.find(Brouillon.class).where().eq("offre",offre).findList()!=null){
			List<Brouillon> brouillons=Ebean.find(Brouillon.class).where().eq("offre",offre).findList();
			for(int i=0;i<brouillons.size();i++){
				if(brouillons.get(i).getUser().getId()!=user.getId()){
					verifier=1;
				}
			}
			if(verifier==0){
				for(int i=0;i<brouillons.size();i++){
					if(brouillons.get(i).getUser().getId()==user.getId()){
						Ebean.delete(brouillons.get(i));
					}
				}
			}
		}
		if(user.getRole().equals("Admin")){
			List<Livraison> livraisons=new ArrayList<>();
			List<Brouillon> brouillons=Ebean.find(Brouillon.class).where().eq("offre", offre).findList();
			for(int i=0;i<brouillons.size();i++){
				System.out.println(brouillons.get(i));
				if(Ebean.find(Livraison.class).where().eq("brouillon", brouillons.get(i)).findUnique()!=null){
					Livraison livraison=Ebean.find(Livraison.class).where().eq("brouillon", brouillons.get(i)).findUnique();
					
					Ebean.delete(livraison);
					
				}
				Ebean.delete(brouillons.get(i));
			}
			
			
			Ebean.delete(offre);
			

			
			return Results.ok();
		}
		Ebean.delete(offre);
		Controller.response().setCookie("idOffre", String.valueOf(0));
		return Results.ok();
			
	}
	public Result deleteOffre2() {
		int idOffre= Integer.parseInt(Controller.request().cookies().get("idOffre").value());
		
		
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		Ebean.delete(offre);
		Controller.response().setCookie("idOffre", String.valueOf(0));
		return Results.ok();
			
	}
	public Result publier() {
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", Integer.parseInt(values.get("id")[0])).findUnique();
		offre.setPublie(1);
		Ebean.update(offre);
	
		return Results.ok();
			
	}
		
	public Result chercherOffre() throws ParseException {
		
		List<SqlRow> offre ;  ;
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idFourniseur=Integer.parseInt(values.get("fournisseur")[0]);
		int idProduit=Integer.parseInt(values.get("produit")[0]);
		int idUser=Integer.parseInt(values.get("user")[0]);
		
		int publie=Integer.parseInt(values.get("publier")[0]);
		Fournisseur fournisseur =new Fournisseur();
		Produit produit = new Produit();
		Utilisateur user = new Utilisateur();
		
		int cas = -1;
		if ( idFourniseur !=0 && idProduit!=0 && idUser !=0   ) {
			
			cas = 1;
		} else if (idProduit!=0 && idUser !=0) {
			cas = 2;
		} else if (idFourniseur !=0 && idProduit!=0) {
			cas = 3;
		} else if (idFourniseur !=0 && idUser !=0) {
			cas = 4;
		} else if (idUser !=0) {
			cas = 5;
		} else if (idFourniseur !=0) {
			cas = 6;
		} else if (idProduit!=0) {
			cas = 7;
		} else {
			cas = 0;
		}
		
			
		switch (cas) {
		case 1:
			
			offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id  WHERE us.id="+idUser+" AND four.id="+idFourniseur+" AND produit.id="+idProduit+" AND offre.publie="+publie)
			.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(offre));
			
		case 2:
			offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE us.id="+idUser+" AND produit.id="+idProduit+" AND offre.publie="+publie)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 3:
			offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE four.id="+idFourniseur+" AND produit.id="+idProduit+" AND offre.publie="+publie)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 4:
			 offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE us.id="+idUser+" AND four.id="+idFourniseur+" AND offre.publie="+publie)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
				
		case 5:
			offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE us.id="+idUser+" AND offre.publie="+publie)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
			
		case 6:
			 offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE four.id="+idFourniseur+" AND offre.publie="+publie)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
		case 7:
			 offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE produit.id="+idProduit+" AND offre.publie="+publie)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
			 
		case 0:
			offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE  offre.publie="+publie)
			.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
		default:
			 break;
				}
		
		
	
		return Results.ok(Json.toJson(ListRech));
			
	}
		
	
public Result chercherOffrePh() throws ParseException {
		
		List<SqlRow> offre ;  ;
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idFourniseur=Integer.parseInt(values.get("fournisseur")[0]);
		int idProduit=Integer.parseInt(values.get("produit")[0]);
		int idOfficine=Integer.parseInt(values.get("officine")[0]);
		
		Fournisseur fournisseur =new Fournisseur();
		Produit produit = new Produit();
		Utilisateur user = new Utilisateur();
		
		int cas = -1;
		if ( idFourniseur !=0 && idProduit!=0 && idOfficine !=0   ) {
			
			cas = 1;
		} else if (idProduit!=0 && idOfficine !=0) {
			cas = 2;
		} else if (idFourniseur !=0 && idProduit!=0) {
			cas = 3;
		} else if (idFourniseur !=0 && idOfficine !=0) {
			cas = 4;
		} else if (idOfficine !=0) {
			cas = 5;
		} else if (idFourniseur !=0) {
			cas = 6;
		} else if (idProduit!=0) {
			cas = 7;
		} else {
			cas = 0;
		}
		List<OffreOfficine> offres=Ebean.find(OffreOfficine.class).findList();
		List<OffreOfficine> offres2=new ArrayList<>();
		List<GroupOfficine> groupes = Ebean.find(GroupOfficine.class).findList();
		List<GroupOfficine> groupes2 = new ArrayList<>();
		Utilisateur logedUser = Authentification.getUser();
		Officine officine = Ebean.find(Officine.class).where().eq("user", logedUser).findUnique();
		String req="";
		if(groupes!=null){
			int trouve=0;
			
			for(int i=0;i<groupes.size();i++){
				for(int j=0;j<groupes.get(i).getOfficines().size();j++){
					if(groupes.get(i).getOfficines().get(j).getId()==officine.getId()){
						
						groupes2.add(groupes.get(i));
						trouve=1;
					}
				}
			}
			if(trouve==1){
				req=" AND ";
				for(int i=0;i<groupes2.size();i++){
					if(i!=groupes2.size()-1){
						req=req+" groupe.id="+groupes2.get(i).getId()+" OR ";
					}else{
						req=req+" groupe.id="+groupes2.get(i).getId();
					}
				}
				
			}
			
		}
		
		
		
		
		
		switch (cas) {
		case 1:
			
			offre= Ebean.createSqlQuery("Select offre.id ,groupe.nom_group, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id INNER JOIN officine ON us.id=officine.user_id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id WHERE officine.id="+idOfficine+" AND four.id="+idFourniseur+" AND produit.id="+idProduit+req)
			.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(offre));
			
		case 2:
			offre= Ebean.createSqlQuery("Select offre.id ,groupe.nom_group, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id INNER JOIN officine ON us.id=officine.user_id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id WHERE officine.id="+idOfficine+" AND produit.id="+idProduit+req)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 3:
			offre= Ebean.createSqlQuery("Select offre.id ,groupe.nom_group, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id WHERE four.id="+idFourniseur+" AND produit.id="+idProduit+req)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 4:
			 offre= Ebean.createSqlQuery("Select offre.id ,groupe.nom_group, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN officine ON us.id=officine.user_id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id WHERE officine.id="+idOfficine+" AND four.id="+idFourniseur+req)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
				
		case 5:
			offre= Ebean.createSqlQuery("Select offre.id ,groupe.nom_group, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN officine ON us.id=officine.user_id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id WHERE officine.id="+idOfficine+req)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
			
		case 6:
			 offre= Ebean.createSqlQuery("Select offre.id ,groupe.nom_group, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id WHERE four.id="+idFourniseur+req)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
		case 7:
			 offre= Ebean.createSqlQuery("Select offre.id ,groupe.nom_group, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id WHERE produit.id="+idProduit+req)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
			 
		case 0:
			offre= Ebean.createSqlQuery("Select offre.id ,offre.groupe_id, four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser ,offre.complete from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN group_officine AS groupe ON groupe.id=offre.groupe_id "+req)
			.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
		default:
			 break;
				}
		
		
	
		return Results.ok(Json.toJson(ListRech));
			
	}
	
	
	
	
	public static OffreOfficine getOffre(int id){
			
		OffreOfficine offre = Ebean.find(OffreOfficine.class).where().eq("id", id).findUnique() ;
			
		return offre;
	 }
	
	public Result getOffreJson(){
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		
		OffreOfficine offre = getOffre(id);
		return Results.ok(Json.toJson(offre));
	}
		
	public Result updateOffre() {
			
		try{
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			int id= Integer.parseInt(values.get("idOffre")[0]);
			
			OffreOfficine existantOffre =Ebean.find(OffreOfficine.class,id);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			
			Date dateOffre=sdf.parse(values.get("dateOffre")[0]);
			Date dateLimit=sdf.parse(values.get("dateLimit")[0]);
			
			
			existantOffre.setDateLimit(dateLimit);
			existantOffre.setDateOffre(dateOffre);
			long diff = existantOffre.getDateLimit().getTime() -date.getTime() ;
    		long numberOfDay = (long)diff/CONST_DURATION_OF_DAY;
    		System.out.println(numberOfDay);
    		if(numberOfDay<0){
    			existantOffre.setExpiree(1);
    			
    		}else if(numberOfDay<10){
    			existantOffre.setExpiree(2);
    			
    		}else{
    			existantOffre.setExpiree(0);
    		}
			
			Ebean.update(existantOffre);
			return Results.ok(Json.toJson(existantOffre));	
		}
		catch(Exception Ex)
		{
			return Results.badRequest();
		}
			
	}
		
		
		

}
