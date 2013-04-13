package be.ac.ucl.lfsab1509.llncampus;

/**
 * Représente une "offre" de l'UCL. Une "offre" correspond a un programme de
 * cours suivit pendant une année académique.
 */
public final class Offre {
	private final int anac;
	private final int numOffre;
	private final String offreCode;
	private final String offreName;

	/**
	 * Constructeur.
	 * 
	 * @param anac
	 *            Année académique (par exemple : 2012 pour l'année académique
	 *            2012-2013)
	 * @param numOffre
	 *            Numéro de l'offre (par exemple : 4932)
	 * @param offreCode
	 *            Code de l'offre (par exemple : FSA13BA)
	 * @param offreName
	 *            Nom de l'offre (par exemple : Troisième année de bachelier en
	 *            sciences de l'ingénieur, orientation ingénieur civil)
	 */
	public Offre(int anac, int numOffre, String offreCode, String offreName) {
		this.anac = anac;
		this.numOffre = numOffre;
		this.offreCode = offreCode;
		this.offreName = offreName;
	}

	/**
	 * Fournit l'année académique. Par exemple : 2012 pour l'année 2012-2013
	 * 
	 * @return Année académique.
	 */
	public int getAnac() {
		return anac;
	}

	/**
	 * Fournit le numéro de l'offre.
	 * 
	 * @return Numéro de l'offre
	 */
	public int getNumOffre() {
		return numOffre;
	}

	/**
	 * Fournit le code de l'offre. Par exemple : FSA13BA.
	 * 
	 * @return Code de l'offre.
	 */
	public String getOffreCode() {
		return offreCode;
	}

	/**
	 * Fournit le nom de l'offre. Par exemple : Troisième année de bachelier en
	 * sciences de l'ingénieur, orientation ingénieur civil).
	 * 
	 * @return Nom de l'offre
	 */
	public String getOffreName() {
		return offreName;
	}

	@Override
	public String toString() {
		return "anac : " + anac + " - num Offre : " + numOffre
				+ " - offreCode : " + offreCode + " - offreName : " + offreName;
	}

}