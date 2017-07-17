package models;

import java.text.SimpleDateFormat;

import java.util.Comparator;
import java.util.Date;


public class InitialisationComparateur implements Comparator<InitialisationSituation> {

	@Override
	
	public int compare(InitialisationSituation o1, InitialisationSituation o2) {
		int r=0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date1=o1.getDateReguelement();
		Date date2=o2.getDateReguelement();
		r=date1.compareTo(date2);
		return r;
		
	}
	

}
