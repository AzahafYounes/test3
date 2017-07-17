package models;
import javax.persistence.Entity;




import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;




@Entity
public class Utilisateur extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
   protected  int id;
   protected  String nom;
   protected  String prenom;
   protected  String email;
   protected  String identifiant;
   protected  String password;
   protected  String role;
   protected  String tel;
   protected  String status;
   protected  String mac;
   protected  String photo;
   protected boolean isdeleted;
   
   
   public Utilisateur() {
	super();
	// TODO Auto-generated constructor stub
}

public Utilisateur(String nom ,String prenom,String email,String identifiant,String password,String role,String tel, String status){
	 
	   this.nom = nom;
	   this.prenom = prenom;
	   this.email = email ;
	   this.identifiant = identifiant;
	   this.password = password ;
	   this.role = role ;
	   this.tel = tel ;
	   this.status=status;
	   this.photo= "../../assets/admin/layout/profile/default.png";
	   this.isdeleted=false;
   }
   
   public void setId(int id) {
       this.id = id;
   }
   
   public int getId() {
       return id;
   }
   
   public void setMac(String mac) {
       this.mac = mac;
   }
   public String getMac() {
	return mac;
}
   
   public String getTel() {
	return tel;
}
   

public void setTel(String tel) {
	this.tel = tel;
}

public void setNom(String nom)
   {
	   this.nom = nom;
   }
   public void setPrenom(String prenom)
   {
	   this.prenom = prenom;
   }
   public void setEmail(String email)
   {
	   this.email = email ;
   }
   public void setIdentifiant(String identifiant)
   {
	   this.identifiant = identifiant;
   }
   public void setPassword(String password)
   {
	   this.password = password ;
   }
  
  
   public String getNom() {
       return nom;
   }
   public String getPrenom() {
       return prenom;
   }
   public String getEmail() {
       return email;
   }
   public String getIdentifiant() {
       return identifiant;
   }
   public String getPassword()
   {
	   return password;
   }
   public String getRole() {
       return role;
   }
    public void setRole(String role)
   {
	   this.role = role ;
   }
   public String getStatus() {
	return status;
  }

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String toString() {
		return "Utilisateur [id=" + id + ", nom=" + nom + ", prenom=" + prenom
				+ ", email=" + email + ", identifiant=" + identifiant
				+ ", password=" + password + ", role=" + role + ", tel=" + tel
				+ ", status=" + status + ", mac=" + mac + ", photo=" + photo
				+ ", isdeleted=" + isdeleted + "]";
	}


	
}
