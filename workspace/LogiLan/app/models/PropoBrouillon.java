package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class PropoBrouillon extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@ManyToOne  
	private Produit produit;
	private int qte;
	private float totalRemise;
	private float totalSansRemise;
	private float totalPPV;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Produit getProduit() {
		return produit;
	}
	public void setProduit(Produit produit) {
		this.produit = produit;
	}
	public int getQte() {
		return qte;
	}
	public void setQte(int qte) {
		this.qte = qte;
	}
	@Override
	public String toString() {
		return "PropoBrouillon [id=" + id + ", produit=" + produit + ", qte="
				+ qte + "]";
	}
	public PropoBrouillon() {
		super();
	}
	public PropoBrouillon(Produit produit, int qte) {
		super();
		this.produit = produit;
		this.qte = qte;
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
