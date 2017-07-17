package models;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class OffreOfficine extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date dateOffre;
	private Date dateLimit;
	@ManyToOne  
	private Utilisateur user;
	@ManyToOne
	private Fournisseur fournisseur;
	@ManyToMany(cascade=CascadeType.ALL) 
	private List<Proposition> propositions = new ArrayList<>();
	private int publie;
	private int complete;
	private int afficher;
	@ManyToOne 
	private GroupOfficine groupe;
	private int expiree;
	private float totalNonRemise;
	private float totalRemise;
	
	public OffreOfficine(Date dateOffre, Date dateLimit, Utilisateur user,
			Fournisseur fournisseur, List<Proposition> propositions, int publie) {
		super();
		this.dateOffre = dateOffre;
		this.dateLimit = dateLimit;
		this.user = user;
		this.fournisseur = fournisseur;
		this.propositions = propositions;
		this.publie = publie;
		this.complete=0;
	}
	public OffreOfficine() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateOffre() {
		return dateOffre;
	}
	public void setDateOffre(Date dateOffre) {
		this.dateOffre = dateOffre;
	}
	public Date getDateLimit() {
		return dateLimit;
	}
	public void setDateLimit(Date dateLimit) {
		this.dateLimit = dateLimit;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUtiliateur(Utilisateur user) {
		this.user = user;
	}
	public Fournisseur getFournisseur() {
		return fournisseur;
	}
	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}
	public List<Proposition> getPropositions() {
		return propositions;
	}
	public void setPropositions(List<Proposition> propositions) {
		this.propositions = propositions;
	}
	public int getPublie() {
		return publie;
	}
	public void setPublie(int publie) {
		this.publie = publie;
	}
	@Override
	public String toString() {
		return "OffreOfficine [id=" + id + ", dateOffre=" + dateOffre
				+ ", dateLimit=" + dateLimit + ", Utilisateur=" + user
				+ ", fournisseur=" + fournisseur + ", Propositions=" + propositions
				+ ", publie=" + publie + "]";
	}
	public int getComplete() {
		return complete;
	}
	public void setComplete(int complete) {
		this.complete = complete;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public GroupOfficine getGroupe() {
		return groupe;
	}
	public void setGroupe(GroupOfficine groupe) {
		this.groupe = groupe;
	}
	public int getAfficher() {
		return afficher;
	}
	public void setAfficher(int afficher) {
		this.afficher = afficher;
	}
	public int getExpiree() {
		return expiree;
	}
	public void setExpiree(int expiree) {
		this.expiree = expiree;
	}
	public float getTotalNonRemise() {
		return totalNonRemise;
	}
	public void setTotalNonRemise(float totalNonRemise) {
		this.totalNonRemise = totalNonRemise;
	}
	public float getTotalRemise() {
		return totalRemise;
	}
	public void setTotalRemise(float totalRemise) {
		this.totalRemise = totalRemise;
	}
	
	
	
	

}
