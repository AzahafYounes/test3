package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class Forme extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idForme;
	private String foDesig;
	private String FOCODE;
	public Forme(int idForme, String foDesig) {
		super();
		this.idForme = idForme;
		this.foDesig = foDesig;
	}
	public Forme(String foDesig) {
		super();
		this.foDesig = foDesig;
	}
	public Forme() {
		super();
	}
	public int getIdForme() {
		return idForme;
	}
	public void setIdForme(int idForme) {
		this.idForme = idForme;
	}
	public String getFoDesig() {
		return foDesig;
	}
	public void setFoDesig(String foDesig) {
		this.foDesig = foDesig;
	}
	public String toString() {
		return "Forme [idForme=" + idForme + ", foDesig=" + foDesig + "]";
	}
	public String getFOCODE() {
		return FOCODE;
	}
	public void setFOCODE(String fOCODE) {
		FOCODE = fOCODE;
	}
	
	
	
	

}
