package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class PropoLivree extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@ManyToOne  
	private Produit produit;
	private int qte;
	private int typeOffre;
	private float remise;
	private int uniteGratuit;
	private Float total;
	private float totalPPV;
	private float totalsanRemis;
	
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
	
	public PropoLivree() {
		super();
	}
	public PropoLivree(Produit produit, int qte) {
		super();
		this.produit = produit;
		this.qte = qte;
	}
	public int getTypeOffre() {
		return typeOffre;
	}
	public void setTypeOffre(int typeOffre) {
		this.typeOffre = typeOffre;
	}
	public float getRemise() {
		return remise;
	}
	public void setRemise(float remise) {
		this.remise = remise;
	}
	public int getUniteGratuit() {
		return uniteGratuit;
	}
	public void setUniteGratuit(int uniteGratuit) {
		this.uniteGratuit = uniteGratuit;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "PropoLivree [id=" + id + ", produit=" + produit + ", qte="
				+ qte + ", typeOffre=" + typeOffre + ", remise=" + remise
				+ ", uniteGratuit=" + uniteGratuit + ", total=" + total + "]";
	}
	public float getTotalPPV() {
		return totalPPV;
	}
	public void setTotalPPV(float totalPPV) {
		this.totalPPV = totalPPV;
	}
	public float getTotalsanRemis() {
		return totalsanRemis;
	}
	public void setTotalsanRemis(float totalsanRemis) {
		this.totalsanRemis = totalsanRemis;
	}
	
	
	
	
	
	
}
