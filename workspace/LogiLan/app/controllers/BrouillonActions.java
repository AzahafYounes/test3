package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import akka.routing.Group;

import com.avaje.ebean.Expr;

import java.text.Normalizer.Form;
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
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;

import models.Brouillon;
import models.CalculQte;
import models.Fournisseur;
import models.GroupOfficine;
import models.Notification;
import models.Observation;
import models.Officine;
import models.OffreOfficine;
import models.Produit;
import models.PropoBrouillon;
import models.Proposition;
import models.UserNotif;
import models.Utilisateur;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;



public class BrouillonActions extends Controller {
	
	static List<SqlRow> ListRech = null;
	public Result getAllBrouillons() throws ParseException,IOException {

		Utilisateur user = Authentification.getUser();
		OffreOfficine offre=new OffreOfficine();
		
		if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
			String msg="";
			List <Brouillon> brouillons = Ebean.find(Brouillon.class).where().eq("user",user).findList();
			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
    		 
			return Results.ok(views.html.listBrouillonsAdminPh.render(msg,user,offre,brouillons,notifications,notificationsLu));
		
		}else if(user.getRole().equals("Admin")){
			String msg="";
			List <Brouillon> brouillons = Ebean.find(Brouillon.class).findList();
			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
    		 
			return Results.ok(views.html.listBrouillons.render(msg,user,offre,brouillons,notifications));
		}
	
		return Results.redirect("/");
			
	}
	
	
	
	
	
	
	
	
	
	public Result VerifierOffre(String id)throws ParseException,IOException {
		Utilisateur logedUser = Authentification.getUser();
		int idOffre=Integer.parseInt(id);
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		List<Brouillon> brouillon=Ebean.find(Brouillon.class).where().eq("offre", offre).findList();
		int complete=1;
		SqlRow qteDememde;
		int qte;
		for(int i=0;i<offre.getPropositions().size();i++){
			if(offre.getPropositions().get(i).getQteReste()!=0)
					complete=0;
				
		}
		if(complete==1){
			String objet = "Offre N° : "+idOffre;
			String message = "Commander ";
			Date date = new Date();
			List<Utilisateur> userDist=new ArrayList<>();
			String nomfichier;
			String path=new File("").getAbsolutePath(); 
			
			nomfichier=path+"\\public\\commande\\CO"+offre.getUser().getNom()+offre.getId()+".txt";
			
			PrintWriter writer =  new PrintWriter(new FileWriter(nomfichier));
			int repeat=0;
			for(int i=0;i<brouillon.size();i++){
				repeat=0;
				brouillon.get(i).setValide(2);
				Ebean.update(brouillon.get(i));
				if(brouillon.get(i).getUser().getId()!=logedUser.getId()){
					for(int j=0;j<userDist.size();j++){
						if(userDist.get(j).getId()==brouillon.get(i).getUser().getId()){
							repeat=1;
						}
					}
					if(repeat==0){
						userDist.add(brouillon.get(i).getUser());
					}
				}
					
					
					
				}
			for(int i=0;i<offre.getPropositions().size();i++){
				writer.println(offre.getPropositions().get(i).getProduit().getNom()+";"+offre.getPropositions().get(i).getProduit().getCodeBarre()+";"+offre.getPropositions().get(i).getQte());
			}
			writer.close();
			Observation obs = new Observation(objet, message, date);
			obs.setAdmin(logedUser);
			obs.setListPharmacien(userDist);
			Ebean.save(obs);
			for(Utilisateur commercial : userDist)
				NotificationActions.addObservation(commercial.getId());
			 NotificationActions.GetNotificationAdmin();
			 offre.setComplete(2);
			 Ebean.update(offre);
			 java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    	 java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    	 if(logedUser.getRole().equals("Admin")){
	    		 notifications=NotificationActions.GetNotificationAdmin();
	    		 return Results.ok(views.html.CommandeOffre.render(logedUser,offre,notifications));
	    	 }
	    	 
	    	 return Results.ok(views.html.CommandeOffrePh.render(logedUser,offre,notifications,notificationsLu));
			 
		}
		
			
	
		return Results.redirect("/offres");
			
	}
	
	
	
	public  Result updateValide()
	{
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		Long id = Long.parseLong(values.get("id")[0]);
		
		Brouillon brouillon = Ebean.find(Brouillon.class,id);
		if(brouillon.getValide()==0)
			brouillon.setValide(1);
		else brouillon.setValide(0);
		Ebean.update(brouillon);
		String objet = "Acceptation ";
		String message = "Validation pour : Offre N° : "+brouillon.getOffre().getId();
		Date date = new Date();
		List<Utilisateur> userDist=new ArrayList<>();
		userDist.add(brouillon.getUser());
		
		Observation obs = new Observation(objet, message, date);
		obs.setAdmin(brouillon.getOffre().getUser());
		obs.setListPharmacien(userDist);
		Ebean.save(obs);
		for(Utilisateur commercial : userDist)
			NotificationActions.addObservation(commercial.getId());
		 NotificationActions.GetNotificationAdmin();
		return Results.ok();
	}
	
	public Result AjouterNewBrouillons1(){
		DynamicForm f=play.data.Form.form().bindFromRequest();
		Map<String, String> data = f.data();
		System.out.println(data);
		int idOffre=Integer.parseInt(data.get("idOffre"));
		int idProduit;
		int qte;
		int valide=1;
		List<PropoBrouillon> propoList=new ArrayList<>();
		Utilisateur user= Authentification.getUser(session().get("user"));
		Produit produit=new Produit();
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		List<Brouillon> brouillons = Ebean.find(Brouillon.class).where().eq("offre",offre).findList();
		int formvalide=0;
		for(int i=0;i<offre.getPropositions().size();i++){
			if(data.get("qteSouhai["+i+"]") != null){
				qte=Integer.parseInt(data.get("qteSouhai["+i+"]"));
			}else{
				qte=0;
				
			}
			if(qte>offre.getPropositions().get(i).getQteReste()){
				formvalide=1;
			}
		}
		if(formvalide==1){
			String msg="erreur";
			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
    		return Results.ok(views.html.ParticiperOffreAdminPh.render(msg,user,offre,notifications,notificationsLu));
		}
		float total=0;
		float totalS=0;
		float totalR=0;
		int qteGra;
		for(int i=0;i<offre.getPropositions().size();i++){
			qte=Integer.parseInt(data.get("qteSouhai["+i+"]"));
			idProduit=Integer.parseInt(data.get("idProduit["+i+"]"));
			produit=Ebean.find(Produit.class).where().eq("id",idProduit ).findUnique();
			PropoBrouillon proBrouillon=new PropoBrouillon(produit, qte);
			
			
			if(offre.getPropositions().get(i).getTypeOffre()==1){
				proBrouillon.setTotalRemise((qte*offre.getPropositions().get(i).getProduit().getPph())*(1-(offre.getPropositions().get(i).getRemise()/100)));
				proBrouillon.setTotalSansRemise(qte*produit.getPph());
				total=total+(qte*produit.getPrix());
				proBrouillon.setTotalPPV(qte*produit.getPrix());
			}
			else if(offre.getPropositions().get(i).getTypeOffre()==2){
				proBrouillon.setTotalRemise(qte*produit.getPph());
				qteGra=(qte*offre.getPropositions().get(i).getUnitGratuit())/offre.getPropositions().get(i).getQte();
				proBrouillon.setTotalSansRemise((qte+qteGra)*produit.getPph());
				total=total+((qte+qteGra)*produit.getPrix());
				proBrouillon.setTotalPPV((qte+qteGra)*produit.getPrix());
			}else{
				proBrouillon.setTotalRemise(qte*produit.getPph());
				proBrouillon.setTotalSansRemise(qte*produit.getPph());
				total=total+(qte*produit.getPrix());
				proBrouillon.setTotalPPV(qte*produit.getPrix());
			}
			totalR=totalR+proBrouillon.getTotalRemise();
			totalS=totalS+proBrouillon.getTotalSansRemise();
			propoList.add(proBrouillon);	
			offre.getPropositions().get(i).setQteReste(offre.getPropositions().get(i).getQteReste()-qte);
			
		}
		if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
			
				Brouillon brouillon=new Brouillon(new Date(), user, offre, propoList, valide);
				brouillon.setTotalPPV(total);
				brouillon.setTotalRemise(totalR);
				brouillon.setTotalSansRemise(totalS);
				String msg="Enregistrement réussi";
				brouillons = Ebean.find(Brouillon.class).where().eq("user",user).findList();
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		    	java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		    	return Results.ok(views.html.ValiderParticiperOffreAdminPh.render(msg,user,offre,brouillon,notifications,notificationsLu));
		    		
		}
		if(user.getRole().equals("Admin")){
			int iduser=Integer.parseInt(data.get("officine"));
			
			Officine userOffre=Ebean.find(Officine.class).where().eq("id", iduser).findUnique();
			
			Brouillon brouillon=new Brouillon(new Date(), userOffre.getUser(), offre, propoList, valide);
			brouillon.setTotalPPV(total);
			brouillon.setTotalRemise(totalR);
			brouillon.setTotalSansRemise(totalS);
			String msg="Enregistrement réussi";
			Ebean.save(brouillon);
			Ebean.save(offre);
			
			
	    	return Results.redirect("/offres");
			
		}
		
		
		return Results.redirect("/");
	}
	
	
	public Result editNewBrouillon1(){
		DynamicForm f=play.data.Form.form().bindFromRequest();
		Map<String, String> data = f.data();
		
		int idBrouillon=Integer.parseInt(data.get("idBrouillon"));
		Brouillon br=Ebean.find(Brouillon.class).where().eq("id",idBrouillon).findUnique();
		int idOffre=br.getOffre().getId();
		int idProduit;
		int qte;
		
		int valide=1;
		List<PropoBrouillon> propoList=new ArrayList<>();
		Utilisateur user= Authentification.getUser(session().get("user"));
		Produit produit=new Produit();
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		List<Brouillon> brouillons = Ebean.find(Brouillon.class).where().eq("offre",offre).findList();
		int formvalide=0;
		for(int i=0;i<offre.getPropositions().size();i++){
			if(data.get("qteSouhai["+i+"]") != null){
				qte=Integer.parseInt(data.get("qteSouhai["+i+"]"));
			}else{
				qte=0;
				
			}
			for(int j=0;j<br.getPropoBrouillons().size();j++){
				if(br.getPropoBrouillons().get(j).getProduit().getId()==offre.getPropositions().get(i).getProduit().getId()){
					if(qte>offre.getPropositions().get(i).getQteReste()+br.getPropoBrouillons().get(j).getQte()){
						formvalide=1;
						
					}
				}
			}
			
		}
		if(formvalide==1){
			String msg="erreur";
			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
    		return Results.ok(views.html.ModifierParticipationOffreAdminPh.render(msg,user,br,notifications,notificationsLu));
		}
		float total=0;
		float totalS=0;
		float totalR=0;
		int qteGra;
		for(int i=0;i<offre.getPropositions().size();i++){
			qte=Integer.parseInt(data.get("qteSouhai["+i+"]"));
			idProduit=Integer.parseInt(data.get("idProduit["+i+"]"));
			produit=Ebean.find(Produit.class).where().eq("id",idProduit ).findUnique();
			offre.getPropositions().get(i).setQteReste((offre.getPropositions().get(i).getQteReste()+br.getPropoBrouillons().get(i).getQte())-qte);
			Ebean.update(offre.getPropositions().get(i));
			PropoBrouillon proBrouillon=new PropoBrouillon(produit, qte);
			
			
			if(offre.getPropositions().get(i).getTypeOffre()==1){
				proBrouillon.setTotalRemise((qte*offre.getPropositions().get(i).getProduit().getPph())*(1-(offre.getPropositions().get(i).getRemise()/100)));
				proBrouillon.setTotalSansRemise(qte*produit.getPph());
				total=total+(qte*produit.getPrix());
				proBrouillon.setTotalPPV(qte*produit.getPrix());
				
			}
			else if(offre.getPropositions().get(i).getTypeOffre()==2){
				proBrouillon.setTotalRemise(qte*produit.getPph());
				qteGra=(qte*offre.getPropositions().get(i).getUnitGratuit())/offre.getPropositions().get(i).getQte();
				proBrouillon.setTotalSansRemise((qte+qteGra)*produit.getPph());
				total=total+((qte+qteGra)*produit.getPrix());
				proBrouillon.setTotalPPV((qte+qteGra)*produit.getPrix());
			}else{
				proBrouillon.setTotalRemise(qte*produit.getPph());
				proBrouillon.setTotalSansRemise(qte*produit.getPph());
				total=total+(qte*produit.getPrix());
				proBrouillon.setTotalPPV(qte*produit.getPrix());
			}
			totalR=totalR+proBrouillon.getTotalRemise();
			totalS=totalS+proBrouillon.getTotalSansRemise();
			propoList.add(proBrouillon);	
			
		}
		if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
			
				Brouillon brouillon=new Brouillon(new Date(), user, offre, propoList, valide);
				br.setDateBrouillons(new Date());
				br.setPropoBrouillons(propoList);
				br.setTotalPPV(total);
				br.setTotalRemise(totalR);
				br.setTotalSansRemise(totalS);
				String msg="Enregistrement réussi";
				brouillons = Ebean.find(Brouillon.class).where().eq("user",user).findList();
				br.setIsupdate(3);
				Ebean.update(offre);
				Ebean.update(br);
				
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		    	java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		    	
		    	
		    	
		    	int complete=1;
				
				for(int i=0;i<offre.getPropositions().size();i++){
					if(offre.getPropositions().get(i).getQteReste()!=0){
						complete=0;
					}
					System.out.println(complete);
						
				}
				if(complete==1){
					List<Utilisateur> userDist=new ArrayList<>();
					brouillons=Ebean.find(Brouillon.class).where().eq("offre",br.getOffre()).findList();
					for(int i=0;i<brouillons.size();i++){
						brouillons.get(i).setValide(2);
						Ebean.update(brouillons.get(i));	
					}
					offre.setComplete(1);
					Ebean.update(offre);
					return Results.redirect("/brouillons");
					 
				}
				return Results.redirect("/brouillons");
		    			
		}
		if(user.getRole().equals("Admin") ){
			
			Brouillon brouillon=new Brouillon(new Date(), user, offre, propoList, valide);
			br.setDateBrouillons(new Date());
			br.setPropoBrouillons(propoList);
			br.setTotalPPV(total);
			br.setTotalRemise(totalR);
			br.setTotalSansRemise(totalS);
			String msg="Enregistrement réussi";
			brouillons = Ebean.find(Brouillon.class).where().eq("user",user).findList();
			br.setIsupdate(3);
			Ebean.update(offre);
			Ebean.update(br);
			
			java.util.List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
	    	
	    	int complete=1;
			
			for(int i=0;i<offre.getPropositions().size();i++){
				if(offre.getPropositions().get(i).getQteReste()!=0){
					complete=0;
				}
				System.out.println(complete);
					
			}
			if(complete==1){
				List<Utilisateur> userDist=new ArrayList<>();
				brouillons=Ebean.find(Brouillon.class).where().eq("offre",br.getOffre()).findList();
				for(int i=0;i<brouillons.size();i++){
					brouillons.get(i).setValide(2);
					Ebean.update(brouillons.get(i));	
				}
				offre.setComplete(1);
				Ebean.update(offre);
				return Results.redirect("/brouillons");
				 
			}
			return Results.redirect("/brouillons");
	    			
	}
		
		
		return Results.redirect("/");
	}
	
	
	public Result AjouterNewBrouillons(){
		DynamicForm f=play.data.Form.form().bindFromRequest();
		Map<String, String> data = f.data();
		int idOffre=Integer.parseInt(data.get("idOffre"));
		int idProduit;
		int qte;
		int valide=0;
		List<PropoBrouillon> propoList=new ArrayList<>();
		Utilisateur user= Authentification.getUser(session().get("user"));
		Produit produit=new Produit();
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		List<Brouillon> brouillons = Ebean.find(Brouillon.class).where().eq("offre",offre).findList();
		
		if(offre.getUser().getId()==user.getId()){
			valide=1;
		}
		float total=0;
		float totalS=0;
		float totalR=0;
		int qteGra;
		for(int i=0;i<offre.getPropositions().size();i++){
			if(data.get("qte["+i+"]") != null){
				qte=Integer.parseInt(data.get("qte["+i+"]"));
				 idProduit=Integer.parseInt(data.get("idProduit["+i+"]"));
				 produit=Ebean.find(Produit.class).where().eq("id",idProduit ).findUnique();
				 PropoBrouillon proBrouillon=new PropoBrouillon(produit, qte);
				 
				 offre.getPropositions().get(i).setQteReste(offre.getPropositions().get(i).getQteReste()-qte);
				 if(offre.getPropositions().get(i).getTypeOffre()==1){
						proBrouillon.setTotalRemise((qte*offre.getPropositions().get(i).getProduit().getPph())*(1-(offre.getPropositions().get(i).getRemise()/100)));
						proBrouillon.setTotalSansRemise(qte*produit.getPph());
						total=total+(qte*produit.getPrix());
						proBrouillon.setTotalPPV(qte*produit.getPrix());
					}
					else if(offre.getPropositions().get(i).getTypeOffre()==2){
						proBrouillon.setTotalRemise(qte*produit.getPph());
						qteGra=(qte*offre.getPropositions().get(i).getUnitGratuit())/offre.getPropositions().get(i).getQte();
						proBrouillon.setTotalSansRemise((qte+qteGra)*produit.getPph());
						total=total+((qte+qteGra)*produit.getPrix());
						proBrouillon.setTotalPPV((qte+qteGra)*produit.getPrix());
					}else{
						proBrouillon.setTotalRemise(qte*produit.getPph());
						proBrouillon.setTotalSansRemise(qte*produit.getPph());
						total=total+(qte*produit.getPrix());
						proBrouillon.setTotalPPV(qte*produit.getPrix());
					}
					totalR=totalR+proBrouillon.getTotalRemise();
					totalS=totalS+proBrouillon.getTotalSansRemise();
				 
				 Ebean.save(proBrouillon);
				 Ebean.save(offre.getPropositions().get(i));
				 
				 propoList.add(proBrouillon);
				 
			}
			 
		}
		if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
			
			if(propoList.size()!=0){
				Brouillon brouillon=new Brouillon(new Date(), user, offre, propoList, valide);
				brouillon.setTotalPPV(total);
				brouillon.setTotalRemise(totalR);
				brouillon.setTotalSansRemise(totalS);
				if(valide!=1){
					
					
					Officine officine= Ebean.find(Officine.class).where().eq("user",user).findUnique();
					String objet = officine.getNomOfficine()+" Participera à votre ";
					String message = "Offre N° : "+idOffre;
					Date date = new Date();
					List<Utilisateur> userDist=new ArrayList<>();
					userDist.add(offre.getUser());
					Observation obs = new Observation(objet, message, date);
					
					obs.setAdmin(user);
					obs.setListPharmacien(userDist);
					Ebean.save(obs);
					for(Utilisateur commercial : userDist)
						NotificationActions.addObservation(commercial.getId());
					    NotificationActions.GetNotificationAdmin();
					 
				}
				int complete=1;
				SqlRow qteDememde;
				
				Ebean.save(brouillon);
				for(int i=0;i<offre.getPropositions().size();i++){
					if(offre.getPropositions().get(i).getQteReste()!=0)
							complete=0;
						
				}
				if(complete==1){
					String objet = "Complet ";
					String message = "Offre N° : "+idOffre;
					Date date = new Date();
					List<Utilisateur> userDist=new ArrayList<>();
					userDist.add(offre.getUser());
					Observation obs = new Observation(objet, message, date);
					obs.setAdmin(user);
					obs.setListPharmacien(userDist);
					Ebean.save(obs);
					for(Utilisateur commercial : userDist)
						NotificationActions.addObservation(commercial.getId());
					 NotificationActions.GetNotificationAdmin();
					 
					offre.setComplete(1);
					 
				}
				
				
				
				 
				 brouillons = Ebean.find(Brouillon.class).where().eq("user",user).findList();
				 
		    		offre.setAfficher(1);
		    		
		    		Ebean.update(offre);
		    		return redirect("/brouillons");
				
			}else{
				return Results.redirect("/");
			}
		}
		
		
		return Results.redirect("/");
	}
	
	
	public Result PageaddNewBrouillon(String id){


		Utilisateur user = Authentification.getUser(session().get("user"));
		Integer idOffre =  Integer.parseInt(id);
		OffreOfficine offre = Ebean.find(OffreOfficine.class).where().eq("id",idOffre).findUnique();
		
		
		
		
		String msg="";
		try{
			if (user.getRole().equals("Admin")){
				List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
				List<GroupOfficine> groupes=Ebean.find(GroupOfficine.class).findList();
				List<Officine> officines=Ebean.find(Officine.class).findList();
				return Results.ok(views.html.ParticiperOffreAdmin.render(msg,user,groupes,offre,notifications));
			}
			else if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		 
	    			 return Results.ok(views.html.ParticiperOffreAdminPh.render(msg,user,offre,notifications,notificationsLu));
	    		
			}
			else	
				return Results.ok(views.html.error_404.render());	
		}
		catch (Exception Ex)
		{
			return Results.redirect("");
			
		}
	}
	
	
	
	
	
	
	public Result PartiperMonOffre(){


		Utilisateur user = Authentification.getUser(session().get("user"));
		int idOffre= Integer.parseInt(Controller.request().cookies().get("idOffre").value());
		OffreOfficine offre = Ebean.find(OffreOfficine.class).where().eq("id",idOffre).findUnique();
		String msg="";
		try{
			if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		
	    		
	    			return Results.ok(views.html.ParticiperOffreAdminPh.render(msg,user,offre,notifications,notificationsLu));
	    		
			}
			else	
				return Results.ok(views.html.error_404.render());	
		}
		catch (Exception Ex)
		{
			return Results.redirect("");
			
		}
	}

	
	
	public Result modifierBrouillonPh(String id){


		Utilisateur user = Authentification.getUser(session().get("user"));
		
		int idbr= Integer.parseInt(id);
		Brouillon brouillon = Ebean.find(Brouillon.class).where().eq("id",idbr).findUnique();
		String msg="";
		try{
			if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
	    		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
	    		
	    		return Results.ok(views.html.ModifierParticipationOffreAdminPh.render(msg,user,brouillon,notifications,notificationsLu));
	    		
			}
			else if(user.getRole().equals("Admin")){
				java.util.List<UserNotif> notifications=NotificationActions.GetNotificationAdmin();
	    		
	    		
	    		return Results.ok(views.html.ModifierParticipation.render(msg,user,brouillon,notifications));
			}
			else	
				return Results.ok(views.html.error_404.render());	
		}
		catch (Exception Ex)
		{
			return Results.redirect("");
			
		}
	}
	
	
	
	
	
	
	public Result PageModifierOffre(String id){

		int idOffre = Integer.parseInt(id);
		Utilisateur user = Authentification.getUser(session().get("user"));
		List<Produit> produits = Ebean.find(Produit.class).findList();
		List<Fournisseur> fournisseurs = Ebean.find(Fournisseur.class).findList();
		List<Proposition> propositions =new ArrayList<>();
		
		Controller.response().setCookie("idOffre", String.valueOf(idOffre));
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		try{
			if (user.getRole().equals("Admin"))
				return Results.ok(views.html.modifierOffreAdmin.render(user,produits,fournisseurs,propositions,offre));
			else if(user.getRole().equals("Pharmacien")){
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
		
		if(idOffre==0){
			List<Proposition> propositions = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			
			Date dateOffre=sdf.parse(values.get("dateOffre")[0]);
			Date dateLimit=sdf.parse(values.get("dateLimit")[0]);
			OffreOfficine offre= new OffreOfficine(dateOffre, dateLimit, user, fournisseur, propositions,Integer.parseInt(values.get("publier")[0]));
			
			
			
				Ebean.save(offre);
				Controller.response().setCookie("idOffre", String.valueOf(offre.getId()));
			}
		
		
		return Results.ok(Json.toJson(produits));
		
	}
	
	public Result addNewOffre() {
	
		return Results.ok();
			
	}
	
	public Result deleteOffre() {
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", Integer.parseInt(values.get("id")[0])).findUnique();
		Ebean.delete(offre);
	
		return Results.ok();
			
	}
	
	 
	public Result deleteBrouillon() {
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		Brouillon brouillon=Ebean.find(Brouillon.class).where().eq("id", Integer.parseInt(values.get("id")[0])).findUnique();
		OffreOfficine offre=brouillon.getOffre();
		for(int i=0;i<offre.getPropositions().size();i++){
			for(int j=0;j<brouillon.getPropoBrouillons().size();j++){
				if(offre.getPropositions().get(i).getProduit().getId()==brouillon.getPropoBrouillons().get(j).getProduit().getId()){
					offre.getPropositions().get(i).setQteReste(offre.getPropositions().get(i).getQteReste()+brouillon.getPropoBrouillons().get(j).getQte());
					
				}
			}
		}
		Ebean.update(offre);
		Ebean.delete(brouillon);
	
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
			
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id  WHERE us.id="+idUser+" AND four.id="+idFourniseur+" AND produit.id="+idProduit+" AND offre.publie="+publie)
			.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(offre));
			
		case 2:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE us.id="+idUser+" AND produit.id="+idProduit+" AND offre.publie="+publie)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 3:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE four.id="+idFourniseur+" AND produit.id="+idProduit+" AND offre.publie="+publie)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 4:
			 offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE us.id="+idUser+" AND four.id="+idFourniseur+" AND offre.publie="+publie)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
				
		case 5:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE us.id="+idUser+" AND offre.publie="+publie)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
			
		case 6:
			 offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE four.id="+idFourniseur+" AND offre.publie="+publie)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
		case 7:
			 offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE produit.id="+idProduit+" AND offre.publie="+publie)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
			 
		case 0:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE  offre.publie="+publie)
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
		
			
		switch (cas) {
		case 1:
			
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id INNER JOIN officine ON us.id=officine.user_id  WHERE officine.id="+idOfficine+" AND four.id="+idFourniseur+" AND produit.id="+idProduit)
			.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(offre));
			
		case 2:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id INNER JOIN officine ON us.id=officine.user_id WHERE officine.id="+idOfficine+" AND produit.id="+idProduit)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 3:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE four.id="+idFourniseur+" AND produit.id="+idProduit)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
		case 4:
			 offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN officine ON us.id=officine.user_id WHERE officine.id="+idOfficine+" AND four.id="+idFourniseur)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
				
		case 5:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN officine ON us.id=officine.user_id WHERE officine.id="+idOfficine)
					.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
			
			
		case 6:
			 offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id WHERE four.id="+idFourniseur)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
		case 7:
			 offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id INNER JOIN offre_officine_proposition ON offre.id=offre_officine_proposition.offre_officine_id INNER JOIN proposition ON proposition.id=offre_officine_proposition.proposition_id INNER JOIN produit ON produit.id=proposition.produit_id WHERE produit.id="+idProduit)
						.findList();
			 ListRech=offre;
			 return Results.ok(Json.toJson(ListRech));
			 
		case 0:
			offre= Ebean.createSqlQuery("Select offre.id , four.nom as nomFour , offre.date_Offre , offre.date_Limit ,offre.publie ,us.nom , us.prenom , us.id as iduser from offre_Officine as offre INNER JOIN Utilisateur as us ON offre.user_id = us.id INNER JOIN fournisseur as four ON offre.fournisseur_id = four.id ")
			.findList();
			ListRech=offre;
			return Results.ok(Json.toJson(ListRech));
		default:
			 break;
				}
		
		
	
		return Results.ok(Json.toJson(ListRech));
			
	}
	
	
	
	
	public static OffreOfficine getOffre(int id){
			
		OffreOfficine offre = null ;
			
		return offre;
	 }
	
	public static Brouillon getBrouillon(int id){
		 
		 Brouillon brouillon = null ;
		    
			if(Ebean.find(Brouillon.class).where().eq("id",id).findUnique()!=null);		
			{
				brouillon= Ebean.find(Brouillon.class).where().eq("id",id).findUnique();
			}
			return brouillon;
	 }
	
	
	
	public Result getBrouillonJson(){
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		
		Brouillon br = getBrouillon(id);
		return Results.ok(Json.toJson(br));
	}
	
	
	public Result getBrouillonsJson(){
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id",id).findUnique();
		List<Brouillon> br = Ebean.find(Brouillon.class).where().eq("offre", offre).findList();
		return Results.ok(Json.toJson(br));
	}
	
	
	
	
	public Result DemandeUpdateBrouillon(){
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		Brouillon br=Ebean.find(Brouillon.class).where().eq("id",id).findUnique();
		
		int value= Integer.parseInt(values.get("value")[0]);
		br.setIsupdate(value);
		Ebean.update(br);
		if(value==1){
			Officine officine=Ebean.find(Officine.class).where().eq("user", br.getUser()).findUnique();
			String objet = officine.getNomOfficine()+" veut modifidier ";
			String message = "Brouillon N° : "+br.getId();
			Date date = new Date();
			List<Utilisateur> userDist=new ArrayList<>();
			userDist.add(br.getOffre().getUser());
			
			Observation obs = new Observation(objet, message, date);
			obs.setAdmin(br.getUser());
			obs.setListPharmacien(userDist);
			Ebean.save(obs);
			for(Utilisateur commercial : userDist)
				NotificationActions.addObservation(commercial.getId());
			 NotificationActions.GetNotificationAdmin();
		}
		if(value==2){
			Officine officine=Ebean.find(Officine.class).where().eq("user", br.getOffre().getUser()).findUnique();
			String objet = officine.getNomOfficine()+" vous pouvez modifier ";
			String message = "Brouillon N° : "+br.getId();
			Date date = new Date();
			List<Utilisateur> userDist=new ArrayList<>();
			userDist.add(br.getOffre().getUser());
			
			Observation obs = new Observation(objet, message, date);
			obs.setAdmin(br.getUser());
			obs.setListPharmacien(userDist);
			Ebean.save(obs);
			for(Utilisateur commercial : userDist)
				NotificationActions.addObservation(commercial.getId());
			 NotificationActions.GetNotificationAdmin();
		}
		return Results.ok(Json.toJson(br));
	}
	public Result DemandeDeleteBrouillon(){
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		int value= Integer.parseInt(values.get("value")[0]);
		Brouillon br=Ebean.find(Brouillon.class).where().eq("id",id).findUnique();
		br.setIsdeleted(value);
		System.out.println(value);
		Ebean.update(br);
		
		if(value==1){
			Officine officine=Ebean.find(Officine.class).where().eq("user", br.getUser()).findUnique();
			String objet = officine.getNomOfficine()+" veut supprimer ";
			String message = "Brouillon N° : "+br.getId();
			Date date = new Date();
			List<Utilisateur> userDist=new ArrayList<>();
			userDist.add(br.getOffre().getUser());
			
			Observation obs = new Observation(objet, message, date);
			obs.setAdmin(br.getUser());
			obs.setListPharmacien(userDist);
			Ebean.save(obs);
			for(Utilisateur commercial : userDist)
				NotificationActions.addObservation(commercial.getId());
			 NotificationActions.GetNotificationAdmin();
		}
		if(value==2){
			Officine officine=Ebean.find(Officine.class).where().eq("user", br.getOffre().getUser()).findUnique();
			String objet = officine.getNomOfficine()+" vous pouvez supprimer ";
			String message = "Brouillon N° : "+br.getId();
			Date date = new Date();
			List<Utilisateur> userDist=new ArrayList<>();
			userDist.add(br.getOffre().getUser());
			
			Observation obs = new Observation(objet, message, date);
			obs.setAdmin(br.getUser());
			obs.setListPharmacien(userDist);
			Ebean.save(obs);
			for(Utilisateur commercial : userDist)
				NotificationActions.addObservation(commercial.getId());
			 NotificationActions.GetNotificationAdmin();
		}
		
		return Results.ok(Json.toJson(br));
	}
	
	
	
	
	
	
	public Result updateOffre() {
			
		return Results.badRequest();
			
	}
		
		
		

}
