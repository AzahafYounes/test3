package models;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;



import com.avaje.ebean.Model;

import java.util.Date;
import java.util.List;

  @Entity
public class Observation extends Model{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private  int     id;
	private  String objet;
    private  String  message;
    private  Date    dateObservation;
    
    @ManyToOne
    Utilisateur admin;
    @ManyToMany
    List<Utilisateur> listPharmacien;
    
	public Observation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Observation(String objet, String message, Date dateObservation) {
		super();
		this.objet = objet;
		this.message = message;
		this.dateObservation = dateObservation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDateObservation() {
		return dateObservation;
	}

	public void setDateObservation(Date dateObservation) {
		this.dateObservation = dateObservation;
	}

	public Utilisateur getAdmin() {
		return admin;
	}

	public void setAdmin(Utilisateur admin) {
		this.admin = admin;
	}

	public List<Utilisateur> getListPharmacien() {
		return listPharmacien;
	}

	public void setListPharmacien(List<Utilisateur> listPharmacien) {
		this.listPharmacien = listPharmacien;
	}
	
	
   
}
