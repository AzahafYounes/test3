package controllers;

import java.util.Date;


import java.util.Map;





import models.OffreOfficine;
import models.Produit;
import models.Proposition;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;





import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

public class PropositionActions extends Controller {
	public  Result addNewProposition() throws Exception {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idOffre= Integer.parseInt(Controller.request().cookies().get("idOffre").value());
		
		Produit produit = Ebean.find(Produit.class).where().eq("id", values.get("idProduit")[0]).findUnique();
		int qte=Integer.parseInt(values.get("qte")[0]);
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		int verifier=0;
		for(int i=0;i<offre.getPropositions().size();i++){
			if(offre.getPropositions().get(i).getProduit().getId()==produit.getId()){
				verifier=1;
			}
		}
		Proposition newProp =new Proposition();
		float totalRemise;
		float totalSansRemise;
		float total;
		if(verifier==0){
			Date date =new Date();
			if(values.get("typeOffre")[0].equals("0")){
				newProp =new Proposition(date, produit, qte, 0, 0, 0);
				totalRemise=qte*produit.getPph();
				totalSansRemise=totalRemise;
				total=qte*produit.getPrix();
				newProp.setTotalPPV(total);
				newProp.setTotalRemise(totalRemise);
				newProp.setTotalSansRemise(totalSansRemise);
				
			}else if(values.get("typeOffre")[0].equals("1")){
				newProp =new Proposition(date, produit, qte, Float.parseFloat(values.get("remise")[0]), 0, 1);
				totalRemise=qte*(produit.getPph()*(1-(1/Float.parseFloat(values.get("remise")[0]))));
				totalSansRemise=qte*produit.getPph();
				total=qte*produit.getPrix();
				newProp.setTotalPPV(total);
				newProp.setTotalRemise(totalRemise);
				newProp.setTotalSansRemise(totalSansRemise);
				
			}else{
				newProp =new Proposition(date, produit, qte, 0, Integer.parseInt(values.get("unitGratuit")[0]), 2);
				totalRemise=qte*produit.getPph();
				totalSansRemise=(qte+Integer.parseInt(values.get("unitGratuit")[0]))*produit.getPph();
				total=(qte+Integer.parseInt(values.get("unitGratuit")[0]))*produit.getPrix();
				newProp.setTotalPPV(total);
				newProp.setTotalRemise(totalRemise);
				newProp.setTotalSansRemise(totalSansRemise);
			}
			newProp.setQteReste(qte);
			offre.setTotalNonRemise(offre.getTotalNonRemise()+totalSansRemise);
			offre.setTotalRemise(offre.getTotalRemise()+totalRemise);
			offre.getPropositions().add(newProp);
			Ebean.save(newProp);
			Ebean.update(offre);
		}
		
		       
		return Results.ok(Json.toJson(newProp));
		
	
	
		}
	
	
	
	public  Result addNewPropositionModifier() throws Exception {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idOffre= Integer.parseInt(values.get("idOffre")[0]);
		Produit produit = Ebean.find(Produit.class).where().eq("id", values.get("idProduit")[0]).findUnique();
		int qte=Integer.parseInt(values.get("qte")[0]);
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		Proposition newProp =new Proposition();
		int verifier=0;
		float totalRemise;
		float totalSansRemise;
		float total;
		for(int i=0;i<offre.getPropositions().size();i++){
			if(offre.getPropositions().get(i).getProduit().getId()==produit.getId()){
				verifier=1;
			}
		}
		if(verifier==0){
		Date date =new Date();
		if(values.get("typeOffre")[0].equals("0")){
			newProp =new Proposition(date, produit, qte, 0, 0, 0);
			totalRemise=qte*produit.getPph();
			totalSansRemise=totalRemise;
			total=qte*produit.getPrix();
			newProp.setTotalPPV(total);
			newProp.setTotalRemise(totalRemise);
			newProp.setTotalSansRemise(totalSansRemise);
			
		}else if(values.get("typeOffre")[0].equals("1")){
			newProp =new Proposition(date, produit, qte, Float.parseFloat(values.get("remise")[0]), 0, 1);
			totalRemise=qte*(produit.getPph()*(1-(1/Float.parseFloat(values.get("remise")[0]))));
			totalSansRemise=qte*produit.getPph();
			total=qte*produit.getPrix();
			newProp.setTotalPPV(total);
			newProp.setTotalRemise(totalRemise);
			newProp.setTotalSansRemise(totalSansRemise);
		}else{
			newProp =new Proposition(date, produit, qte, 0, Integer.parseInt(values.get("unitGratuit")[0]), 2);
			totalRemise=qte*produit.getPph();
			totalSansRemise=(qte+Integer.parseInt(values.get("unitGratuit")[0]))*produit.getPph();
			total=(qte+Integer.parseInt(values.get("unitGratuit")[0]))*produit.getPrix();
			newProp.setTotalPPV(total);
			newProp.setTotalRemise(totalRemise);
			newProp.setTotalSansRemise(totalSansRemise);
		}
		newProp.setQteReste(qte);
		offre.setTotalNonRemise(offre.getTotalNonRemise()+totalSansRemise);
		offre.setTotalRemise(offre.getTotalRemise()+totalRemise);
		
		offre.getPropositions().add(newProp);
		Ebean.save(newProp);
		Ebean.update(offre);
	}      
		return Results.ok(Json.toJson(newProp));
		
	
	
		
		
		
		
		
		
		
			
			
			
		
		
		       
		
		}
	public  Result deleteProposition() throws Exception {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int idProposition= Integer.parseInt(values.get("idProposition")[0]);
		Proposition prop = Ebean.find(Proposition.class).where().eq("id", idProposition).findUnique();
		int idOffre= Integer.parseInt(Controller.request().cookies().get("idOffre").value());
		OffreOfficine offre=Ebean.find(OffreOfficine.class).where().eq("id", idOffre).findUnique();
		offre.getPropositions().remove(prop);
		Ebean.update(offre);
		Ebean.delete(prop);
		       
		return Results.ok();
		
	
	
		}
	
	public Result calculePropo()
	{
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		int qtePr=Integer.parseInt(values.get("qtePr")[0]);
		int qteS=Integer.parseInt(values.get("qteS")[0]);
		
		float TPPV=Float.parseFloat((values.get("TPPV")[0]).replace(",", "."));
		float TR=Float.parseFloat(values.get("TR")[0].replace(",", "."));
		float TSR=Float.parseFloat(values.get("TSR")[0].replace(",", "."));
		Proposition propo=Ebean.find(Proposition.class).where().eq("id", id).findUnique();
		
		if(propo.getTypeOffre()==0){
			TPPV=TPPV+((qteS-qtePr)*propo.getProduit().getPrix());
			TSR=TSR+((qteS-qtePr)*propo.getProduit().getPph());
			TR=TR+((qteS-qtePr)*propo.getProduit().getPph());
			
		}else if(propo.getTypeOffre()==1){
			TPPV=TPPV+((qteS-qtePr)*propo.getProduit().getPrix());
			TSR=TSR+((qteS-qtePr)*propo.getProduit().getPph());
			TR=TR+(1-propo.getRemise()/100)*((qteS-qtePr)*propo.getProduit().getPph());
		}else{
			int qteGra=(qteS*propo.getUnitGratuit())/propo.getQte();
			TPPV=TPPV+((qteS-qtePr+qteGra)*propo.getProduit().getPrix());
			TSR=TSR+((qteS-qtePr+qteGra)*propo.getProduit().getPph());
			TR=TR+((qteS-qtePr)*propo.getProduit().getPph());
		}
		propo.setTotalPPV(TPPV);
		propo.setTotalRemise(TR);
		propo.setTotalSansRemise(TSR);
		return Results.ok(Json.toJson(propo));
	}
	
	
	
	
	public Result calculeProp()
	{
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		int id= Integer.parseInt(values.get("id")[0]);
		int qtePr=Integer.parseInt(values.get("qtePr")[0]);
		int qteS=Integer.parseInt(values.get("qteS")[0]);
		float TPPV=Float.parseFloat(values.get("TPPV")[0]);
		float TR=Float.parseFloat(values.get("TR")[0]);
		float TSR=Float.parseFloat(values.get("TSR")[0]);
		Proposition propo=Ebean.find(Proposition.class).where().eq("id", id).findUnique();
		
		if(propo.getTypeOffre()==0){
			TPPV=TPPV+((qteS-qtePr)*propo.getProduit().getPrix());
			TSR=TSR+((qteS-qtePr)*propo.getProduit().getPph());
			TR=TR+((qteS-qtePr)*propo.getProduit().getPph());
			
		}else if(propo.getTypeOffre()==1){
			TPPV=TPPV+((qteS-qtePr)*propo.getProduit().getPrix());
			TSR=TSR+((qteS-qtePr)*propo.getProduit().getPph());
			TR=TR+(1-propo.getRemise()/100)*((qteS-qtePr)*propo.getProduit().getPph());
		}else{
			int qteGra=(qteS*propo.getUnitGratuit())/propo.getQte();
			TPPV=TPPV+((qteS-qtePr+qteGra)*propo.getProduit().getPrix());
			TSR=TSR+((qteS-qtePr+qteGra)*propo.getProduit().getPph());
			TR=TR+((qteS-qtePr)*propo.getProduit().getPph());
		}
		propo.setTotalPPV(TPPV);
		propo.setTotalRemise(TR);
		propo.setTotalSansRemise(TSR);
		return Results.ok(Json.toJson(propo));
	}
	
	
	
	
	
	

}
