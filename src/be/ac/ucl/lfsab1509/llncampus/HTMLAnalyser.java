package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import android.util.Log;

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
 * Classe permettant d'analyser du code HTML et d'y extraire du code.
 */
public final class HTMLAnalyser {

	/**
	 * Constructeur.
	 */
	private HTMLAnalyser() {
	}

	/**
	 * Extrait les parties entre <balise(...)> et </balise> d'un code HTML.
	 * 
	 * @param html
	 *            Code HTML
	 * @param balise
	 *            Nom de la balise
	 * @return Liste du contenu des balises
	 */
	
	static int count = 0;
	public static ArrayList<String> getBalisesContent(String html, String balise) {
		ArrayList<String> toReturn = new ArrayList<String>();
		int start, stop, endStart, i, nextStart;
		String theString = html;
		while ((start = theString.indexOf("<" + balise)) != -1
				&& (endStart = theString.indexOf('>', start)) != -1
				&& (stop = theString.indexOf("</" + balise + ">", endStart)) != -1) {
			i = endStart;

			/* Pour gérer les balises du même type imbriquée */
			while ((nextStart = theString.indexOf("<" + balise, i)) < stop
					&& nextStart > 0) {
				i = nextStart + balise.length() + 2;
				stop = theString.indexOf("</" + balise + ">", stop);
			}
			toReturn.add(theString.substring(endStart + 1, stop));

			// Appel recursif jusqu'à ce que la condition du if ne soit plus
			// respectee..
			Log.d("HTML", html);
			Log.d("SUB", html.substring(stop + 3 + balise.length()));
			Log.d("Balise", balise);
			count++;
			Log.d("Count", ""+count);
			theString = theString.substring(stop + 3 + balise.length());
		}
		return toReturn;
	}

	/**
	 * Supprime les balises HTML pour ne laisser que du texte.
	 * 
	 * @param html
	 *            Code HTML
	 * @return texte sans balise (x)HTML
	 */
	public static String removeHTML(String html) {
		int start, stop;
		// On cherche une ouverture de balise "<" et une fermeture de balise
		if ((start = html.indexOf('<')) != -1
				&& (stop = html.indexOf('>', start)) != -1) {
			String toReturn = "";
			if (start > 0) {
				toReturn += html.substring(0, start);
			}
			toReturn += html.substring(stop + 1);
			return removeHTML(toReturn);
		}
		// Aucune balise restante, on retourne le texte.
		return html;
	}
}