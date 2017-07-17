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
public class Brouillon extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date dateBrouillons;
	@ManyToOne  
	private Utilisateur user;
	@ManyToOne  
	private OffreOfficine offre;
	@ManyToMany(cascade=CascadeType.ALL) 
	private List<PropoBrouillon> propoBrouillons = new ArrayList<>();
	private int valide;
	private int isdeleted;
	private int isupdate;
	private float totalRemise;
	private float totalSansRemise;
	private float totalPPV;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateBrouillons() {
		return dateBrouillons;
	}
	public void setDateBrouillons(Date dateBrouillons) {
		this.dateBrouillons = dateBrouillons;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public OffreOfficine getOffre() {
		return offre;
	}
	public void setOffre(OffreOfficine offre) {
		this.offre = offre;
	}
	public List<PropoBrouillon> getPropoBrouillons() {
		return propoBrouillons;
	}
	public void setPropoBrouillons(List<PropoBrouillon> propoBrouillons) {
		this.propoBrouillons = propoBrouillons;
	}
	public int getValide() {
		return valide;
	}
	public void setValide(int valide) {
		this.valide = valide;
	}
	public Brouillon(Date dateBrouillons, Utilisateur user,
			OffreOfficine offre, List<PropoBrouillon> propoBrouillons,
			int valide) {
		super();
		this.dateBrouillons = dateBrouillons;
		this.user = user;
		this.offre = offre;
		this.propoBrouillons = propoBrouillons;
		this.valide = valide;
	}
	public Brouillon() {
		super();
	}
	@Override
	public String toString() {
		return "Brouillon [id=" + id + ", dateBrouillons=" + dateBrouillons
				+ ", user=" + user + ", offre=" + offre + ", propoBrouillons="
				+ propoBrouillons + ", valide=" + valide + "]";
	}
	public int getIsdeleted() {
		return isdeleted;
	}
	public void setIsdeleted(int isdeleted) {
		this.isdeleted = isdeleted;
	}
	public int getIsupdate() {
		return isupdate;
	}
	public void setIsupdate(int isupdate) {
		this.isupdate = isupdate;
	}
	public float getTotalRemise() {
		return totalRemise;
	}
	public void setTotalRemise(float totalRemise) {
		this.totalRemise = totalRemise;
	}
	public float getTotalSansRemise() {
		return totalSansRemise;
	}
	public void setTotalSansRemise(float totalSansRemise) {
		this.totalSansRemise = totalSansRemise;
	}
	public float getTotalPPV() {
		return totalPPV;
	}
	public void setTotalPPV(float totalPPV) {
		this.totalPPV = totalPPV;
	}
	
	
	

}
