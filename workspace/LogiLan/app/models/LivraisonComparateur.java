package models;

import java.text.SimpleDateFormat;

import java.util.Comparator;
import java.util.Date;


public class LivraisonComparateur implements Comparator<Livraison> {

	@Override
	
	public int compare(Livraison o1, Livraison o2) {
		int r=0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date1=o1.getDateLivraison();
		Date date2=o2.getDateLivraison();
		r=date1.compareTo(date2);
		return r;
		
	}
	

}
