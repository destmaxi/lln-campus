package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

/**
 * Classe permettant d'analyser du code HTML et d'y extraire du code.
 * @author damien
 *
 */
public class HTMLAnalyser {
	/**
	 * Extrait les parties entre <balise(...)> et </balise> d'un code HTML
	 * @param html
	 * @param balise
	 * @author Damien
	 */
	public static ArrayList<String> getBalisesContent(String html, String balise){
		ArrayList<String> toReturn = new ArrayList<String>();
		int start, stop, end_start;
		if(
			(start = html.indexOf("<"+balise)) != -1 && 
			(stop = html.indexOf("</"+balise+">")) != -1 &&
			(end_start = html.indexOf('>', start)) != -1 &&
			stop > end_start
		){
			toReturn.add(html.substring(end_start+1, stop));
			
			//Appel recursif jusqu'à ce que la condition du if ne soit plus respectee.. 
			toReturn.addAll(getBalisesContent(html.substring(stop+3+balise.length()),balise));
		}
		return toReturn;
	}
	
	/**
	 * Supprime les balises HTML pour ne laisser que du texte. 
	 * @param html
	 * @return texte sans balise (x)HTML
	 * @author damien
	 */
	public static String removeHTML(String html){
		int start, stop;
		//On cherche une ouverture de balise "<" et une fermeture de balise 
		if((start = html.indexOf('<')) != -1 &&  (stop = html.indexOf('>', start)) != -1){
			String toReturn = "";
			if(start > 0){
				toReturn += html.substring(0,start);
			}
			toReturn += html.substring(stop+1);
			return removeHTML(toReturn);
		}
		//Aucune balise restante, on retourne le texte.
		return html;
	}
}
