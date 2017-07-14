package models;

import java.util.Date;




import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;



@Entity
public class Produit extends Model {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private  int    id;
	@ManyToOne  
	private Fournisseur fournisseur;
	private String nom;
	private float prix;
	private float pph;
	private String codeBarre;
	@ManyToOne
	private Forme forme;
	@ManyToOne  
	private FamilleTarif familleTarif;
	private int isNew;
	private Date dateAjout;
	@ManyToOne
	private Utilisateur user;
	
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Fournisseur getFournisseur() {
		return fournisseur;
	}
	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	public Forme getForme() {
		return forme;
	}
	public void setForme(Forme forme) {
		this.forme = forme;
	}
	public String getCodeBarre() {
		return codeBarre;
	}
	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}
	
	public Produit() {
		super();
	}
	public FamilleTarif getFamilleTarif() {
		return familleTarif;
	}
	public void setFamilleTarif(FamilleTarif familleTarif) {
		this.familleTarif = familleTarif;
	}
	
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	public Date getDateAjout() {
		return dateAjout;
	}
	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public Produit(Fournisseur fournisseur, String nom, float prix,
			String codeBarre, Forme forme, FamilleTarif familleTarif,
			int isNew, Date dateAjout, Utilisateur user) {
		super();
		this.fournisseur = fournisseur;
		this.nom = nom;
		this.prix = prix;
		this.codeBarre = codeBarre;
		this.forme = forme;
		this.familleTarif = familleTarif;
		this.isNew = isNew;
		this.dateAjout = dateAjout;
		this.user = user;
		this.pph=CalCulPPH1();
	}
	public float getPph() {
		return pph;
	}
	public void setPph(float pph) {
		this.pph = pph;
	}
	
	public float CalCulPPH1(){
		float prixHT,prixDepart,MontPPH;
		if(this.getFamilleTarif().getTva()!=0){
			prixHT=(this.prix/(1+(this.getFamilleTarif().getTva()/100)));
			if(this.getFamilleTarif().getFtCode().equals("A1") || this.getFamilleTarif().getFtCode().equals("A2")){
				prixDepart = prixHT/ (Float.parseFloat("1.11")+Float.parseFloat("0.47")) ;
				MontPPH=(prixHT-(prixDepart*Float.parseFloat("0.47"))*(1+(this.getFamilleTarif().getTva()/100)));
			}
			else if(this.getFamilleTarif().getFtCode().equals("A3") || this.getFamilleTarif().getFtCode().equals("A4")){
				prixDepart = (prixHT-300) / Float.parseFloat("1.02") ;
				MontPPH=(prixHT-300) *(1+(this.getFamilleTarif().getTva()/100));
			}
			else if(this.getFamilleTarif().getFtCode().equals("A5") || this.getFamilleTarif().getFtCode().equals("A6")){
				prixDepart = (prixHT-400) / Float.parseFloat("1.02") ;
				MontPPH=(prixHT-400) *(1+(this.getFamilleTarif().getTva()/100));
			}
			else{
				if(prixHT<=278.88){
					prixDepart = prixHT/(Float.parseFloat("1.11")+Float.parseFloat("0.57"));
					MontPPH = (float) ((prixHT-(prixDepart*0.57))*(1+(this.getFamilleTarif().getTva()/100)));
				}
				else if(prixHT>278.88 && prixHT<=929.05){
					prixDepart = prixHT/(Float.parseFloat("1.11")+Float.parseFloat("0.47"));
					MontPPH = (float) ((prixHT-(prixDepart*0.47))*(1+(this.getFamilleTarif().getTva()/100)));
				}
				else if(prixHT>929.05 && prixHT<=2101.32){
					prixDepart = (float) ((prixHT-300)/1.02);
					MontPPH = (float) ((prixHT-300)*(1+(this.getFamilleTarif().getTva()/100)));
				}else{
					prixDepart = (float) ((prixHT-400)/1.02);
					MontPPH = (float) ((prixHT-400)*(1+(this.getFamilleTarif().getTva()/100)));
				}
				
			}
		}else{
			MontPPH = this.prix *(1-(this.getFamilleTarif().getMarge()/100));
		}
			
			
			return MontPPH;
		
	}
	
			
			
		
		
		

	

	
	
	

}
