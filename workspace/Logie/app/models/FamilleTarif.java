package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;


@Entity
public class FamilleTarif extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idFamilleT;
	private String ftCode;
	private String nom;
	private float tva;
	private float marge;
	private String type;
	public FamilleTarif(int idFamilleT, String nom, float tva, float marge,
			String type) {
		super();
		this.idFamilleT = idFamilleT;
		this.nom = nom;
		this.tva = tva;
		this.marge = marge;
		this.type = type;
	}
	public FamilleTarif(String nom, float tva, float marge, String type) {
		super();
		this.nom = nom;
		this.tva = tva;
		this.marge = marge;
		this.type = type;
	}
	public FamilleTarif() {
		super();
	}
	public int getIdFamilleT() {
		return idFamilleT;
	}
	public void setIdFamilleT(int idFamilleT) {
		this.idFamilleT = idFamilleT;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public float getTva() {
		return tva;
	}
	public void setTva(float tva) {
		this.tva = tva;
	}
	public float getMarge() {
		return marge;
	}
	public void setMarge(float marge) {
		this.marge = marge;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String toString() {
		return "FamilleTarif [idFamilleT=" + idFamilleT + ", nom=" + nom
				+ ", tva=" + tva + ", marge=" + marge + ", type=" + type + "]";
	}
	public String getFtCode() {
		return ftCode;
	}
	public void setFtCode(String ftCode) {
		this.ftCode = ftCode;
	} 
	
	
	
	
	

}
