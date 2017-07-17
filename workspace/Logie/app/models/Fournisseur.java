package models;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;


@Entity
public class Fournisseur extends Model{
	
	/**
	 * 
	 */
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
   protected  int id;
   protected  String nom;
   protected  String email;
   protected  String adress;
   protected  String tel;
   protected  String fix1;
   protected  String fix2;
   protected  String fix3;
   protected  String fax;
   protected String FRCODE;
   
   
   public Fournisseur() {
	   super();
   }


   public Fournisseur(int id, String nom, String email, String adress, String tel,
		   String fix1, String fix2, String fix3, String fax) {
	   super();
	   this.id = id;
	   this.nom = nom;
	   this.email = email;
	   this.adress = adress;
	   this.tel = tel;
	   this.fix1 = fix1;
	   this.fix2 = fix2;
	   this.fix3 = fix3;
	   this.fax = fax;
   }

   	

   public Fournisseur(String nom, String email, String adress, String tel,
		String fix1, String fix2, String fix3, String fax) {
	super();
	this.nom = nom;
	this.email = email;
	this.adress = adress;
	this.tel = tel;
	this.fix1 = fix1;
	this.fix2 = fix2;
	this.fix3 = fix3;
	this.fax = fax;
}


public int getId() {
	   return id;
   }


   public void setId(int id) {
	   this.id = id;
   }


   public String getNom() {
	   return nom;
   }


   public void setNom(String nom) {
	   this.nom = nom;
   }


   public String getEmail() {
	   return email;
   }


   public void setEmail(String email) {
	   this.email = email;
   }


   public String getAdress() {
	   return adress;
   }


   public void setAdress(String adress) {
	   this.adress = adress;
   }


   public String getTel() {
	   return tel;
   }


   public void setTel(String tel) {
	   this.tel = tel;
   }


   public String getFix1() {
	   return fix1;
   }


   public void setFix1(String fix1) {
	   this.fix1 = fix1;
   }


   public String getFix2() {
	   return fix2;
   }


   public void setFix2(String fix2) {
	   this.fix2 = fix2;
   }


   public String getFix3() {
	   return fix3;
   }


   public void setFix3(String fix3) {
	   this.fix3 = fix3;
   }


   public String getFax() {
	   return fax;
   }


   public void setFax(String fax) {
	   this.fax = fax;
   }


public String toString() {
	return "Fournisseur [id=" + id + ", nom=" + nom + ", email=" + email
			+ ", adress=" + adress + ", tel=" + tel + ", fix1=" + fix1
			+ ", fix2=" + fix2 + ", fix3=" + fix3 + ", fax=" + fax + "]";
}


public String getFRCODE() {
	return FRCODE;
}


public void setFRCODE(String fRCODE) {
	FRCODE = fRCODE;
}
   

}
