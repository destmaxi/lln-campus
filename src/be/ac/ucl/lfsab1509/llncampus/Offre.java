package be.ac.ucl.lfsab1509.llncampus;

public class Offre {
	/** Année académique. */
	public int anac;
	/** Numéro de "l'offre" UCL. */
	public int numOffre;
	/** Code du programme */ 
	public String offreCode;
	/** Nom du programme */
	public String offreName;
	
	
	public String toString(){
		return "anac : " + anac
				+ " - num Offre : " + numOffre
				+ " - offreCode : " + offreCode
				+ " - offreName : " + offreName;
	}
	
	
}