package models;

public class CalculQte {
	private int id;
	private int idProduit;
	
	private int qteDispo;
	private int qteDemande;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdProduit() {
		return idProduit;
	}
	public void setIdProduit(int idProduit) {
		this.idProduit = idProduit;
	}
	
	public int getQteDispo() {
		return qteDispo;
	}
	public void setQteDispo(int qteDispo) {
		this.qteDispo = qteDispo;
	}
	public int getQteDemande() {
		return qteDemande;
	}
	public void setQteDemande(int qteDemande) {
		this.qteDemande = qteDemande;
	}
	public CalculQte(int id, int idProduit, int qteDispo,
			int qteDemande) {
		super();
		this.id = id;
		this.idProduit = idProduit;
		
		this.qteDispo = qteDispo;
		this.qteDemande = qteDemande;
	}
	@Override
	public String toString() {
		return "CalculQte [id=" + id + ", idProduit=" + idProduit
				+ ", qteBroui=" + qteDispo + ", qteDemande=" + qteDemande + "]";
	}
	
	

}
