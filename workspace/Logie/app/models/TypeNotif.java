package models;

public class TypeNotif {
	
	static String rdv="Rendez-vous";
	static String echeance="Info";
	
	public static String getRdv() {
		return rdv;
	}
	public static void setRdv(String rdv) {
		TypeNotif.rdv = rdv;
	}
	public static String getEcheance() {
		return echeance;
	}
	public static void setEcheance(String echeance) {
		TypeNotif.echeance = echeance;
	}

}
