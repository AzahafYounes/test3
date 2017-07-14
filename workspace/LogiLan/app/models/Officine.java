package models;


import javax.persistence.CascadeType;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import java.util.List;

import com.avaje.ebean.Model;

@Entity
public class Officine extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String nomOfficine;
	private String adress;
	private String inpe;
	private String fix;
	
	@ManyToOne  
	private Utilisateur user;
	private int manager;
	
    
	public Officine() {
		super();
	}
	public Officine(String nomOfficine, String adress, Utilisateur user,int manager) {
		super();
		this.nomOfficine = nomOfficine;
		this.adress = adress;
		this.user = user;
		this.manager=manager;
		
	}
	
	
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomOfficine() {
		return nomOfficine;
	}
	public void setNomOfficine(String nomOfficine) {
		this.nomOfficine = nomOfficine;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public int getManager() {
		return manager;
	}
	public void setManager(int manager) {
		this.manager = manager;
	}
	@Override
	public String toString() {
		return "Officine [id=" + id + ", nomOfficine=" + nomOfficine
				+ ", adress=" + adress + ", user=" + user + ", manager="
				+ manager + "]";
	}
	public String getInpe() {
		return inpe;
	}
	public void setInpe(String inpe) {
		this.inpe = inpe;
	}
	public String getFix() {
		return fix;
	}
	public void setFix(String fix) {
		this.fix = fix;
	}
	
	
	
	
	
	
	

}
