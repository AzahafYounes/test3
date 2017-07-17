package models;

import java.util.ArrayList;
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
public class GroupOfficine extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@ManyToMany(cascade=CascadeType.ALL) 
	private List<Officine> officines = new ArrayList<>();
	private int nbrOfficine;
	private String nomGroup;
	private String zone;
	private String observation;
	@ManyToOne
	private Utilisateur user;
	
	
	
	public List<Officine> getOfficines() {
		return officines;
	}
	public void setOfficines(List<Officine> officines) {
		this.officines = officines;
	}
	public int getNbrOfficine() {
		return nbrOfficine;
	}
	public void setNbrOfficine(int nbrOfficine) {
		this.nbrOfficine = nbrOfficine;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomGroup() {
		return nomGroup;
	}
	public void setNomGroup(String nomGroup) {
		this.nomGroup = nomGroup;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public GroupOfficine(List<Officine> officines, int nbrOfficine,
			String nomGroup, String zone, String observation) {
		super();
		this.officines = officines;
		this.nbrOfficine = nbrOfficine;
		this.nomGroup = nomGroup;
		this.zone = zone;
		this.observation = observation;
	}
	public String toString() {
		return "GroupOfficine [id=" + id + ", officines=" + officines
				+ ", nbrOfficine=" + nbrOfficine + ", nomGroup=" + nomGroup
				+ ", zone=" + zone + ", observation=" + observation + "]";
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public GroupOfficine() {
		super();
	}
	
	
	
	

}
