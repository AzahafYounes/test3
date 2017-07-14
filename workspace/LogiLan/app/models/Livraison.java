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
public class Livraison extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date dateLivraison;
	@ManyToOne  
	private Utilisateur user;
	@ManyToOne  
	private Utilisateur userDist;
	@ManyToOne  
	private Brouillon brouillon;
	@ManyToMany(cascade=CascadeType.ALL) 
	private List<PropoLivree> propoLivree = new ArrayList<>();
	private int valide;
	private Float total;
	private float totalPPV;
	private float totalSansRemise;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateLivraison() {
		return dateLivraison;
	}
	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public Brouillon getBrouillon() {
		return brouillon;
	}
	public void setBrouillon(Brouillon brouillon) {
		this.brouillon = brouillon;
	}
	public List<PropoLivree> getPropoLivree() {
		return propoLivree;
	}
	public void setPropoLivree(List<PropoLivree> propoLivree) {
		this.propoLivree = propoLivree;
	}
	public int getValide() {
		return valide;
	}
	public void setValide(int valide) {
		this.valide = valide;
	}
	public Livraison(Date dateLivraison, Utilisateur user,
			Brouillon brouillon, List<PropoLivree> propoLivree,
			int valide) {
		super();
		this.dateLivraison = dateLivraison;
		this.user = user;
		this.brouillon = brouillon;
		this.propoLivree = propoLivree;
		this.valide = valide;
	}
	public Livraison() {
		super();
	}
	
	public Utilisateur getUserDist() {
		return userDist;
	}
	public void setUserDist(Utilisateur userDist) {
		this.userDist = userDist;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "Livraison [id=" + id + ", dateLivraison=" + dateLivraison
				+ ", user=" + user + ", userDist=" + userDist + ", brouillon="
				+ brouillon + ", propoLivree=" + propoLivree + ", valide="
				+ valide + ", total=" + total + "]";
	}
	public float getTotalPPV() {
		return totalPPV;
	}
	public void setTotalPPV(float totalPPV) {
		this.totalPPV = totalPPV;
	}
	public float getTotalSansRemise() {
		return totalSansRemise;
	}
	public void setTotalSansRemise(float totalSansRemise) {
		this.totalSansRemise = totalSansRemise;
	}
	
	
	
	
	

}
