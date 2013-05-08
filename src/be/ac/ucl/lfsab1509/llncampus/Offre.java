package be.ac.ucl.lfsab1509.llncampus;


/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
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