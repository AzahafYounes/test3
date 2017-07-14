package controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


import java.util.List;
import java.util.Map;




















import models.Brouillon;
import models.CalculQte;
import models.GroupOfficine;
import models.InitialisationComparateur;
import models.InitialisationSituation;
import models.Livraison;
import models.LivraisonComparateur;
import models.Officine;
import models.OffreOfficine;
import models.Produit;
import models.Proposition;
import models.SituationOfficine;
import models.UserNotif;
import models.Utilisateur;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;



















import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;

public class RegelementActions extends Controller {
	
	
	public Result getSituation() throws ParseException,IOException {

		Utilisateur user = Authentification.getUser();
		List<Livraison> livraisons=new ArrayList<>();
		List<Livraison> livraisons2=new ArrayList<>();
		java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
			livraisons=Ebean.find(Livraison.class).where().eq("user", user).findList();
			livraisons2=Ebean.find(Livraison.class).where().eq("userDist", user).findList();
			
    		List<SituationOfficine> situations = new ArrayList<>();
    		List<Utilisateur> users=new ArrayList<>();
    		for(int i=0;i<livraisons.size();i++){
    			Utilisateur u=Ebean.find(Utilisateur.class).where().eq("id",livraisons.get(i).getUserDist().getId()).findUnique();
    			
    			users.add(u);
    		}
    		
    		for(int i=0;i<livraisons2.size();i++){
    			Utilisateur u=Ebean.find(Utilisateur.class).where().eq("id",livraisons2.get(i).getUser().getId()).findUnique();
    			users.add(u);
    		}
    		//supprimer User reapeat
    		for(int i=0;i<users.size()-1;i++){
    			for(int j=i+1;j<users.size();j++){
    				if(users.get(i).getId()==users.get(j).getId()){
    					users.remove(j);
    					j--;
    				}
    			}
    		}
    		float total=0;
    		for(int i=0;i<users.size();i++){
    			
    			if(users.get(i).getId()!=user.getId()){
    				SituationOfficine situation = new SituationOfficine();
    				situation.setTotalEntree(0);
        			situation.setTotalSortie(0);
        			situation.setUser(user);
        			situation.setUserDist(users.get(i));
        			
        			situation.setListEntree(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", user),Expr.eq("user",users.get(i))).findList());
        			situation.setListSortie(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", users.get(i)),Expr.eq("user",user)).findList());
        			for(int j=0;j<situation.getListEntree().size();j++){
        				
        				situation.setTotalEntree(situation.getTotalEntree()+situation.getListEntree().get(j).getTotal());
        			}
        			
        			for(int j=0;j<situation.getListSortie().size();j++){
        				
        				situation.setTotalSortie(situation.getTotalSortie()+situation.getListSortie().get(j).getTotal());
        			}
        			
        			List<InitialisationSituation> inialisations = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("user", user),Expr.eq("userDist", users.get(i))).findList();
        			float reguelemntE=0;
        			
        			float reguelemntS=0;
        			float totalReguelement=0;
        			List<InitialisationSituation> inialisations2 = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("userDist", user),Expr.eq("user", users.get(i))).findList();
        			
        			for(int j=0;j<inialisations2.size();j++){
        				
        				reguelemntS=reguelemntS+Math.abs(inialisations2.get(j).getMontant());
                		
                	}
        			
        			for(int j=0;j<inialisations.size();j++){
        				
                		
                		reguelemntE=reguelemntE+Math.abs(inialisations.get(j).getMontant());
                		
                	}
        			situation.setTotalEntree(situation.getTotalEntree()-reguelemntS);
        			situation.setTotalSortie(situation.getTotalSortie()-reguelemntE);
        			//totalReguelement=reguelemntE-reguelemntS;
        			
        			if(situation.getTotalSortie()-situation.getTotalEntree()>=-1 && situation.getTotalSortie()-situation.getTotalEntree()<=1){
        				situation.setTotal(0);
        				
        			}else{
        				situation.setTotal(situation.getTotalSortie()-situation.getTotalEntree());
        				total= total + situation.getTotalSortie()-situation.getTotalEntree();
        			}
        			
        			
        			
        			
        			situations.add(situation);
        			
    			}
    		}
    		
    		//Float t=new Float(total);
    		
			return Results.ok(views.html.situation.render(user,situations,total,notifications,notificationsLu));
		
		}else if(user.getRole().equals("Admin")){
			
    		List<SituationOfficine> situations = new ArrayList<>();
    		List<Utilisateur> users=new ArrayList<>();
    		List<GroupOfficine> groupes=Ebean.find(GroupOfficine.class).findList();
    		List<Officine> officines=new ArrayList<>();
    		float total=0;
    		if(groupes.get(0)!=null){
    			officines=groupes.get(0).getOfficines();
    		}
    		Officine officine=new Officine();
    		if(officines.get(0)!=null){
    			livraisons=Ebean.find(Livraison.class).where().eq("user",officines.get(0).getUser()).findList();
    			livraisons2=Ebean.find(Livraison.class).where().eq("userDist", officines.get(0).getUser()).findList();
    			for(int i=0;i<livraisons.size();i++){
        			Utilisateur u=Ebean.find(Utilisateur.class).where().eq("id",livraisons.get(i).getUserDist().getId()).findUnique();
        			
        			users.add(u);
        		}
    			for(int i=0;i<livraisons2.size();i++){
        			Utilisateur u=Ebean.find(Utilisateur.class).where().eq("id",livraisons2.get(i).getUser().getId()).findUnique();
        			users.add(u);
        		}
    			for(int i=0;i<users.size()-1;i++){
        			for(int j=i+1;j<users.size();j++){
        				if(users.get(i).getId()==users.get(j).getId()){
        					users.remove(j);
        					j--;
        				}
        			}
        		}
    			
    			for(int i=0;i<users.size();i++){
        			
        			if(users.get(i).getId()!=officines.get(0).getUser().getId()){
        				SituationOfficine situation = new SituationOfficine();
        				situation.setTotalEntree(0);
            			situation.setTotalSortie(0);
            			situation.setUser(officines.get(0).getUser());
            			situation.setUserDist(users.get(i));
            			
            			situation.setListEntree(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", officines.get(0).getUser()),Expr.eq("user",users.get(i))).findList());
            			situation.setListSortie(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", users.get(i)),Expr.eq("user",officines.get(0).getUser())).findList());
            			for(int j=0;j<situation.getListEntree().size();j++){
            				
            				situation.setTotalEntree(situation.getTotalEntree()+situation.getListEntree().get(j).getTotal());
            			}
            			
            			for(int j=0;j<situation.getListSortie().size();j++){
            				
            				situation.setTotalSortie(situation.getTotalSortie()+situation.getListSortie().get(j).getTotal());
            			}
            			
            			List<InitialisationSituation> inialisations = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("user", officines.get(0).getUser()),Expr.eq("userDist", users.get(i))).findList();
            			float reguelemntE=0;
            			
            			float reguelemntS=0;
            			float totalReguelement=0;
            			List<InitialisationSituation> inialisations2 = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("userDist", officines.get(0).getUser()),Expr.eq("user", users.get(i))).findList();
            			
            			for(int j=0;j<inialisations2.size();j++){
            				
            				reguelemntS=reguelemntS+Math.abs(inialisations2.get(j).getMontant());
                    		
                    	}
            			
            			for(int j=0;j<inialisations.size();j++){
            				
                    		
                    		reguelemntE=reguelemntE+Math.abs(inialisations.get(j).getMontant());
                    		
                    	}
            			situation.setTotalEntree(situation.getTotalEntree()-reguelemntS);
            			situation.setTotalSortie(situation.getTotalSortie()-reguelemntE);
            			
            			
            			if(situation.getTotalSortie()-situation.getTotalEntree()>=-1 && situation.getTotalSortie()-situation.getTotalEntree()<=1){
            				situation.setTotal(0);
            				
            			}else{
            				situation.setTotal(situation.getTotalSortie()-situation.getTotalEntree());
            				total= total + situation.getTotalSortie()-situation.getTotalEntree();
            			}
            			
            			
            			
            			
            			situations.add(situation);
            			
        			}
        		}
        		
    			
    			
    			
    			
    			
    			
    		}
    		
    		
    		
    		
    		
    		
    		
    		
			
			return Results.ok(views.html.situationAdmin.render(user,situations,groupes,officines,total));
		}else{
			System.out.println("non exist");
		}
	
		return Results.redirect("/");
			
	}
	
	
	public Result getSituationOfficineJson() throws ParseException,IOException {

		Utilisateur user = Authentification.getUser();
		List<Livraison> livraisons=new ArrayList<>();
		List<Livraison> livraisons2=new ArrayList<>();
		List<SituationOfficine> situations = new ArrayList<>();
    	List<Utilisateur> users=new ArrayList<>();
    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
    	Officine officine=Ebean.find(Officine.class).where().eq("id", id).findUnique();
    	float total=0;
    	
    	if(officine!=null){
    		livraisons=Ebean.find(Livraison.class).where().eq("user",officine.getUser()).findList();
    		livraisons2=Ebean.find(Livraison.class).where().eq("userDist", officine.getUser()).findList();
    		for(int i=0;i<livraisons.size();i++){
        		Utilisateur u=Ebean.find(Utilisateur.class).where().eq("id",livraisons.get(i).getUserDist().getId()).findUnique();
        		users.add(u);
        	}
    		for(int i=0;i<livraisons2.size();i++){
        		Utilisateur u=Ebean.find(Utilisateur.class).where().eq("id",livraisons2.get(i).getUser().getId()).findUnique();
        		users.add(u);
        	}
    		for(int i=0;i<users.size()-1;i++){
        		for(int j=i+1;j<users.size();j++){
        			if(users.get(i).getId()==users.get(j).getId()){
        				users.remove(j);
        					j--;
        				}
        			}
        		}
    			
    		for(int i=0;i<users.size();i++){
        			
    			if(users.get(i).getId()!=officine.getUser().getId()){
        				SituationOfficine situation = new SituationOfficine();
        				situation.setTotalEntree(0);
            			situation.setTotalSortie(0);
            			situation.setUser(officine.getUser());
            			situation.setUserDist(users.get(i));
            			
            			situation.setListEntree(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", officine.getUser()),Expr.eq("user",users.get(i))).findList());
            			situation.setListSortie(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", users.get(i)),Expr.eq("user",officine.getUser())).findList());
            			for(int j=0;j<situation.getListEntree().size();j++){
            				situation.setTotalEntree(situation.getTotalEntree()+situation.getListEntree().get(j).getTotal());
            			}
            			
            			for(int j=0;j<situation.getListSortie().size();j++){
            				situation.setTotalSortie(situation.getTotalSortie()+situation.getListSortie().get(j).getTotal());
            			}
            			
            			List<InitialisationSituation> inialisations = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("user", officine.getUser()),Expr.eq("userDist", users.get(i))).findList();
            			float reguelemntE=0;
            			
            			float reguelemntS=0;
            			float totalReguelement=0;
            			List<InitialisationSituation> inialisations2 = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("userDist", officine.getUser()),Expr.eq("user", users.get(i))).findList();
            			
            			for(int j=0;j<inialisations2.size();j++){
            				
            				reguelemntS=reguelemntS+Math.abs(inialisations2.get(j).getMontant());
                    		
                    	}
            			
            			for(int j=0;j<inialisations.size();j++){
            				
                    		
                    		reguelemntE=reguelemntE+Math.abs(inialisations.get(j).getMontant());
                    		
                    	}
            			situation.setTotalEntree(situation.getTotalEntree()-reguelemntS);
            			situation.setTotalSortie(situation.getTotalSortie()-reguelemntE);
            			
            			
            			if(situation.getTotalSortie()-situation.getTotalEntree()>=-1 && situation.getTotalSortie()-situation.getTotalEntree()<=1){
            				situation.setTotal(0);
            				
            			}else{
            				situation.setTotal(situation.getTotalSortie()-situation.getTotalEntree());
            				total= total + situation.getTotalSortie()-situation.getTotalEntree();
            			}
            			
            			
            			
            			
            			situations.add(situation);
            			
        			}
    			
        		}
        		
    	}
    	return Results.ok(Json.toJson(situations));
			
}
	
	
	
	
	public Result initialisation() throws ParseException,IOException {

		Utilisateur user = Authentification.getUser();
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idUserDist= Integer.parseInt(values.get("id")[0]);
		Utilisateur userDist=Ebean.find(Utilisateur.class).where().eq("id", idUserDist).findUnique();
		List<Livraison> livraisons=new ArrayList<>();
		List<Livraison> livraisons2=new ArrayList<>();
		java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
			livraisons=Ebean.find(Livraison.class).where().and(Expr.eq("user", user),Expr.eq("userDist", userDist)).findList();
			livraisons2=Ebean.find(Livraison.class).where().and(Expr.eq("userDist", user),Expr.eq("user", userDist)).findList();
			List<InitialisationSituation> inialisations = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("user", user),Expr.eq("userDist", userDist)).findList();
			float reguelemntE=0;
			float reguelemntS=0;
			float totalReguelement=0;
			List<InitialisationSituation> inialisations2 = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("userDist", user),Expr.eq("user", userDist)).findList();
			livraisons.addAll(livraisons2);
			
			
			for(int j=0;j<inialisations.size();j++){
				
        		reguelemntE=reguelemntE+inialisations.get(j).getMontant();
        	}
			
			for(int j=0;j<inialisations2.size();j++){
				
        		reguelemntS=reguelemntS+inialisations2.get(j).getMontant();
        	}
			
			
    		float total=0;
    		
    		
    		SituationOfficine situation = new SituationOfficine();
    		situation.setTotalEntree(0);
        	situation.setTotalSortie(0);
        	situation.setUser(user);
        	situation.setUserDist(userDist);
        			
        	situation.setListEntree(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", user),Expr.eq("user",userDist)).findList());
        	situation.setListSortie(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", userDist),Expr.eq("user",user)).findList());
        	for(int j=0;j<situation.getListEntree().size();j++){
        		
        		situation.setTotalEntree(situation.getTotalEntree()+situation.getListEntree().get(j).getTotal());
        	}
        	for(int j=0;j<situation.getListSortie().size();j++){
        		
        		situation.setTotalSortie(situation.getTotalSortie()+situation.getListSortie().get(j).getTotal());
        	}
        	situation.setTotal(situation.getTotalSortie()-situation.getTotalEntree());
        	total= total + situation.getTotalSortie()-situation.getTotalEntree();
        	float totalini =(situation.getTotalSortie()+reguelemntS)-(situation.getTotalEntree()+reguelemntE);
        	
        	InitialisationSituation initial= new InitialisationSituation(new Date(), totalini, user, userDist);
        	
        	Ebean.save(initial);
        	return Results.redirect("/situation");
			
	}
		return Results.redirect("/");
		
}
	
	
	public Result DetailSituation(String id)throws ParseException,IOException {
		Utilisateur user = Authentification.getUser();
		int idUserDist=Integer.parseInt(id);
		Utilisateur userDist=Ebean.find(Utilisateur.class).where().eq("id", idUserDist).findUnique();
		List<Livraison> livraisons=new ArrayList<>();
		List<Livraison> livraisons2=new ArrayList<>();
		java.util.List<UserNotif> notifications=NotificationActions.GetNotificationCom();
		java.util.List<UserNotif> notificationsLu=NotificationActions.GetNotificationComLu();
		if(user.getRole().equals("Pharmacien") || user.getRole().equals("AdminOfficine")){
			livraisons=Ebean.find(Livraison.class).where().and(Expr.eq("user", user),Expr.eq("userDist", userDist)).findList();
			livraisons2=Ebean.find(Livraison.class).where().and(Expr.eq("userDist", user),Expr.eq("user", userDist)).findList();
			livraisons.addAll(livraisons2);
    		float total=0;
    		Collections.sort(livraisons,new LivraisonComparateur());
    		livraisons2=livraisons;
    		List<InitialisationSituation> inialisations = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("user", user),Expr.eq("userDist", userDist)).findList();
			
			List<InitialisationSituation> inialisations2 = Ebean.find(InitialisationSituation.class).where().and(Expr.eq("userDist", user),Expr.eq("user", userDist)).findList();
			
			
			
			
    		
    		SituationOfficine situation = new SituationOfficine();
    		situation.setTotalEntree(0);
        	situation.setTotalSortie(0);
        	situation.setUser(user);
        	situation.setUserDist(userDist);
        			
        	situation.setListEntree(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", user),Expr.eq("user",userDist)).findList());
        	situation.setListSortie(Ebean.find(Livraison.class).where().and(Expr.eq("userDist", userDist),Expr.eq("user",user)).findList());
        	
        	for(int j=0;j<inialisations.size();j++){
        		situation.setTotalEntree(situation.getTotalEntree()+inialisations.get(j).getMontant());
        	}
        	for(int j=0;j<inialisations2.size();j++){
        		situation.setTotalSortie(situation.getTotalSortie()+inialisations2.get(j).getMontant());
        	}
        	for(int j=0;j<situation.getListEntree().size();j++){
        		situation.setTotalEntree(situation.getTotalEntree()+situation.getListEntree().get(j).getTotal());
        	}
        	for(int j=0;j<situation.getListSortie().size();j++){
        		situation.setTotalSortie(situation.getTotalSortie()+situation.getListSortie().get(j).getTotal());
        	}
        	situation.setTotal(situation.getTotalSortie()-situation.getTotalEntree());
        	total= total + situation.getTotalSortie()-situation.getTotalEntree();
        	
        	return Results.ok(views.html.Detailsituation.render(user,situation,livraisons,inialisations,inialisations2,notifications,notificationsLu));
        			
    	}
    		
		
	
		return Results.redirect("/");
			
	}
	
	
	
	public Result getSituationJson(){

		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		
		Livraison livraison = getLivraison(id);
		return Results.ok(Json.toJson(livraison));
	}
	
	
	
	
	
	
	
	
	
	public static Livraison getLivraison(int id){
		 
		Livraison livraison = null ;
		    
			if(Ebean.find(Brouillon.class).where().eq("id",id).findUnique()!=null);		
			{
				livraison= Ebean.find(Livraison.class).where().eq("id",id).findUnique();
			}
			return livraison;
	 }
	
	
	

}
