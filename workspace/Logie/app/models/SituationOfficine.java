package models;

import java.util.Date;
import java.util.List;

public class SituationOfficine {
	private Utilisateur user;
	private Utilisateur userDist;
	private List<Livraison> listEntree;
	private List<Livraison> listSortie;
	private float totalEntree;
	private float totalSortie;
	private float total;
	private int type;
	private Date date;
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
	public List<Livraison> getListEntree() {
		return listEntree;
	}
	public void setListEntree(List<Livraison> listEntree) {
		this.listEntree = listEntree;
	}
	public List<Livraison> getListSortie() {
		return listSortie;
	}
	public void setListSortie(List<Livraison> listSortie) {
		this.listSortie = listSortie;
	}
	public float getTotalEntree() {
		return totalEntree;
	}
	public void setTotalEntree(float totalEntree) {
		this.totalEntree = totalEntree;
	}
	public float getTotalSortie() {
		return totalSortie;
	}
	public void setTotalSortie(float totalSortie) {
		this.totalSortie = totalSortie;
	}
	@Override
	public String toString() {
		return "SituationOfficine [user=" + user + ", userDist=" + userDist
				+ ", listEntree=" + listEntree + ", listSortie=" + listSortie
				+ ", totalEntree=" + totalEntree + ", totalSortie="
				+ totalSortie + "]";
	}
	public SituationOfficine(Utilisateur user, Utilisateur userDist,
			List<Livraison> listEntree, List<Livraison> listSortie,
			float totalEntree, float totalSortie) {
		super();
		this.user = user;
		this.userDist = userDist;
		this.listEntree = listEntree;
		this.listSortie = listSortie;
		this.totalEntree = totalEntree;
		this.totalSortie = totalSortie;
	}
	public SituationOfficine() {
		
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	

}
