package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * Gere la connexion a ADE et la recuperation des informations.  
 * @author damien
 *
 */
public class ADE {
	private static final String ServerUrl = "http://horaire.sgsi.ucl.ac.be:8080";
	private static final int projectId = 9;
	private static final String user = "etudiant";
	private static final String password = "student";

	
	/**
	 * Etablit la connexion a ADE en specifiant les codes des cours dont ont veut les informations.
	 * @param codes Code des cours a recuperer.
	 * @return true si la connexion a reussie, false sinon.
	 * @author Damien
	 */
	private static boolean connectADE(String codes){
		HttpClient client = ExternalAppUtility.getHttpClient();
		HttpGet request = new HttpGet(ServerUrl+"/ade/custom/modules/plannings/direct_planning.jsp?weeks=&code="+codes+"&login="+user+"&password="+password+"&projectId="+projectId+"");
		try {
			client.execute(request);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	/**
	 * Charge les informations a propos des cours dont le code est donne en argument. 
	 * @param codes : Liste des cours sous la forme d'un tableau de String
	 * @return Une liste d'evenement.
	 * @see getInfos(String codes)
	 * @author Damien
	 */
	public static ArrayList<Event> getInfos(String[] codes){
		String c = "";
		for(String code : codes){
			if(!c.isEmpty()){
				c += ",";
			}
			c += code;
		}
		return getInfos(c);
	}
	
	/**
	 * Charge les informations a propos des cours dont le code est donne en argument. 
	 * @param codes : codes de cours separes par une virgule.  
	 * @return Une liste d'evenement.
	 * @author Damien
	 */
	public static ArrayList<Event> getInfos(String codes){
		String html = ""; // A long string containing all the HTML
		ArrayList<Event> events = new ArrayList<Event>();
		if(!connectADE(codes)){ return null; }
		try {
			HttpClient client = ExternalAppUtility.getHttpClient();
			HttpGet request = new HttpGet(ServerUrl+"/ade/custom/modules/plannings/info.jsp?order=slot");
			HttpResponse response = client.execute(request);
			
			html = EntityUtils.toString(response.getEntity());
		
			String table = HTMLAnalyser.getBalisesContent(html,"table").get(0); 
			ArrayList<String> lignes = HTMLAnalyser.getBalisesContent(table,"tr");
						
			// On commence a 2 pour passer les 2 lignes d'en-tete.
			for(int i = 2; i<lignes.size() ; i++){
				ArrayList<String> cellules = HTMLAnalyser.getBalisesContent(lignes.get(i),"td");
				
				String date = HTMLAnalyser.removeHTML(cellules.get(0));
				String beginHour = HTMLAnalyser.removeHTML(cellules.get(4));
				String duration = HTMLAnalyser.removeHTML(cellules.get(5));
				
				Event event = new Event(date, beginHour, duration);
				event.addDetail("trainees", HTMLAnalyser.removeHTML(cellules.get(6)));
				event.addDetail("trainers", HTMLAnalyser.removeHTML(cellules.get(7)));
				event.addDetail("room", HTMLAnalyser.removeHTML(cellules.get(8)));
				event.addDetail("course", HTMLAnalyser.removeHTML(cellules.get(10)));
				event.addDetail("activity_name", HTMLAnalyser.removeHTML(cellules.get(1)));
				
				events.add(event);
			}
		} catch (Exception e) {
			Log.e("ADE.java", "Erreur lors de la connexion ou de l'analyse du code HTML : "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		return events;
	}
}
