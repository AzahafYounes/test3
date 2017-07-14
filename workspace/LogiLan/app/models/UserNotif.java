package models;

import java.util.Date;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;



@Entity
public class UserNotif extends Model {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	@Column(name = "id_Notification", unique = true, nullable = false)
	private  Long id_Notification;
	private Date dateNotif;
	private String message;
	private Boolean lu = false ;
	private Boolean adminLu = false;
	private Boolean notifadminset = false ;
	private Boolean notifcommercset = false;
	private Boolean offre = false;
	@ManyToOne
	private OffreOfficine offreUser;
	
	@ManyToOne 
	Utilisateur user;
	@ManyToOne
	String type;
	public UserNotif() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserNotif(Date dateNotif, String message, Boolean lu) {
		super();
		this.dateNotif = dateNotif;
		this.message = message;
		this.lu = lu;
		this.adminLu= lu;
	}
	
	public Long getId_Notification() {
		return id_Notification;
	}
	public void setId_Notification(Long id_Notification) {
		this.id_Notification = id_Notification;
	}
	public Date getDateNotif() {
		return dateNotif;
	}
	public void setDateNotif(Date dateNotif) {
		this.dateNotif = dateNotif;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getLu() {
		return lu;
	}
	public void setLu(Boolean lu) {
		this.lu = lu;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	public Boolean getAdminLu() {
		return adminLu;
	}
	public void setAdminLu(Boolean adminLu) {
		this.adminLu = adminLu;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getNotifadminset() {
		return notifadminset;
	}
	public void setNotifadminset(Boolean notifadminset) {
		this.notifadminset = notifadminset;
	}
	public Boolean getNotifcommercset() {
		return notifcommercset;
	}
	public void setNotifcommercset(Boolean notifcommercset) {
		this.notifcommercset = notifcommercset;
	}
	

	
}
