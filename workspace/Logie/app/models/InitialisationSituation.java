package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;


@Entity
public class InitialisationSituation extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private  int     id;
	private Date dateReguelement;
	private float montant;
	@ManyToOne
	private Utilisateur user;
	@ManyToOne
	private Utilisateur userDist;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateReguelement() {
		return dateReguelement;
	}
	public void setDateReguelement(Date dateReguelement) {
		this.dateReguelement = dateReguelement;
	}
	public float getMontant() {
		return montant;
	}
	public void setMontant(float montant) {
		this.montant = montant;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public Utilisateur getUserDist() {
		return userDist;
	}
	public void setUserDist(Utilisateur userDist) {
		this.userDist = userDist;
	}
	public InitialisationSituation(Date dateReguelement, float montant,
			Utilisateur user, Utilisateur userDist) {
		super();
		this.dateReguelement = dateReguelement;
		this.montant = montant;
		this.user = user;
		this.userDist = userDist;
	}
	public InitialisationSituation() {
		super();
	}
	
	
	

}
