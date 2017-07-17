package models;
import javax.persistence.Entity
;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;


@Entity
public class Notification extends Model{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	   private  int id;
	   String type;
	   int delai;
	   private  String message;
	   private boolean isDeleted=false;
  
   
public Notification(String type, int delai, String message) {
	super();
	this.type = type;
	this.delai = delai;
	this.message = message;
}

public Notification() {
	super();
	// TODO Auto-generated constructor stub
}

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public int getDelai() {
	return delai;
}
public void setDelai(int delai) {
	this.delai = delai;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public boolean isDeleted() {
	return isDeleted;
}
public void setDeleted(boolean isDeleted) {
	this.isDeleted = isDeleted;
}
   

}
