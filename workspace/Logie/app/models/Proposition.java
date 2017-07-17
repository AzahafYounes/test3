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
public class Proposition extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date dateProposition;
	@ManyToOne  
	private Produit produit;
	private int qte;
	private float remise;
	private int unitGratuit;
	private int typeOffre;
	private int qteReste;
	private float totalRemise;
	private float totalSansRemise;
	private float totalPPV;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateProposition() {
		return dateProposition;
	}
	public void setDateProposition(Date dateProposition) {
		this.dateProposition = dateProposition;
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
	
	public float getRemise() {
		return remise;
	}
	public void setRemise(float remise) {
		this.remise = remise;
	}
	public int getUnitGratuit() {
		return unitGratuit;
	}
	public void setUnitGratuit(int unitGratuit) {
		this.unitGratuit = unitGratuit;
	}
	public int getTypeOffre() {
		return typeOffre;
	}
	public void setTypeOffre(int typeOffre) {
		this.typeOffre = typeOffre;
	}
	public Proposition() {
		super();
	}
	public Proposition(Date dateProposition, Produit produit, int qte,
			float remise, int unitGratuit, int typeOffre) {
		super();
		this.dateProposition = dateProposition;
		this.produit = produit;
		this.qte = qte;
		this.remise = remise;
		this.unitGratuit = unitGratuit;
		this.typeOffre = typeOffre;
	}
	public int getQteReste() {
		return qteReste;
	}
	public void setQteReste(int qteReste) {
		this.qteReste = qteReste;
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
