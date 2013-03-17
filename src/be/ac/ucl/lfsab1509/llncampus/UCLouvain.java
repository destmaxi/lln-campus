package be.ac.ucl.lfsab1509.llncampus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.StrictMode;
import android.util.Log;

public class UCLouvain {
	/** Adresse du serveur ADE */
	private static final String SERVER_URL = "https://www.uclouvain.be";
	/** Page pour les infos */
	private static final String LOGIN_PATH = "/page_login.html";
	private static final String BEGIN_HTML_NOTES_TABLE = "<td class=\"composant-titre-inter\">Crédit</td>\n</tr>";
	private static final String END_HTML_NOTES_TABLE = "</table>\n<p>\n</p>\n<table align=\"center\" border=\"0\" cellpadding=\"2\" cellspacing=\"2\">";
	/** ID pour les notifications */
	//private static final Integer NOTIFY_ID = 2;
	
	private String cookies = null;
	private boolean connected = false;
	
	
	public UCLouvain(final String user, final String password){
		//TODO Remove this line
		//StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);

		connectUCLouvain(user, password);
	}
	
	
	/**
	 * Etablit la connexion a ADE en specifiant les codes des cours dont ont veut les informations.
	 * @param code Code des cours a recuperer.
	 * @param weeks Numero des semaines.
	 * @return true si la connexion a reussie, false sinon.
	 * @author Damien
	 */
	private boolean connectUCLouvain(final String user, final String password) {
		HttpClient client = ExternalAppUtility.getHttpClient();
		
		HttpPost request = new HttpPost(SERVER_URL + LOGIN_PATH);
		request.addHeader("Referer", SERVER_URL + LOGIN_PATH);
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("username", user));
        postParameters.add(new BasicNameValuePair("password", password));
        try {
			request.setEntity(new UrlEncodedFormEntity(postParameters));
	
			HttpResponse response = client.execute(request);//On se connecte
			
		//	String html = EntityUtils.toString(response.getEntity());
			//Log.d("DEBUG HTML", html);
			this.cookies = "";
			for(Header hc : response.getHeaders("Set-Cookie")){
				this.cookies += hc.getValue().substring(0,hc.getValue().indexOf(';')+1);
			}
			if(!this.cookies.isEmpty()){
				connected = true;
			}
			return true;
		} catch (Exception e) {
			Log.e("UCLouvain", e.getLocalizedMessage() + " - " + e.getMessage() + " - ");
			return false;
		}
	}
	
	
	public ArrayList<Offre> getOffres(){
		if(!connected){ return null; }
		String html = ""; // A long string containing all the HTML
		ArrayList<Offre> offres = new ArrayList<Offre>();
		try {
			HttpClient client = ExternalAppUtility.getHttpClient();
			HttpGet request = new HttpGet(SERVER_URL + "/onglet_etudes.html");
			
			request.addHeader("Cookie", cookies);

			HttpResponse response = client.execute(request);
			
			html = EntityUtils.toString(response.getEntity());
		
			ArrayList<String> tables = HTMLAnalyser.getBalisesContent(html, "table");
			String table = tables.get(tables.size()-1); // Dernier tableau de la page.
			ArrayList<String> lignes = HTMLAnalyser.getBalisesContent(table, "tr");
						
			// On commence a 2 pour passer les 2 lignes d'en-tete.
			for (int i = 2; i < lignes.size(); i++) {
				ArrayList<String> cellules = HTMLAnalyser.getBalisesContent(lignes.get(i), "td");
				Log.d("DEBUG UCLouvain", "Cellules : "+ cellules);
				Offre o = new Offre();
				String anneeAnac = HTMLAnalyser.removeHTML(cellules.get(0));
				o.anac = Integer.parseInt(anneeAnac.substring(0, 4));
				
				String code = cellules.get(1);
				int j = code.indexOf("numOffre=");
				o.numOffre = Integer.parseInt(code.substring(j+9, code.indexOf("&",j)));
				
				o.offreCode = HTMLAnalyser.removeHTML(code);
				
				o.offreName = HTMLAnalyser.removeHTML(cellules.get(2));
				
				offres.add(o);
			}
		} catch (Exception e) {
			Log.e("UCLouvain.java", "Erreur lors de la connexion ou de l'analyse du code HTML : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return offres;
	}
	
	/**
	 * Retourne la liste des cours de l'annee académique courante 
	 * @param anac
	 * @return
	 */
	public ArrayList<Cours> getCourses(final int anac){
		if(!connected){ return null; }

		ArrayList<Cours> cours = new ArrayList<Cours>();
		
		ArrayList<Offre> offres = getOffres();
		if (offres == null){
			return null;
		}
		for(Offre o : offres){
			if(o.anac == anac){
				Log.d("Trouvé Offre", o.toString());
				ArrayList<Cours> c;
				try {
					c = getCourses(anac, o.numOffre);
					if(c != null){
						cours.addAll(c);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		
		return cours;
		
	}


	private ArrayList<Cours> getCourses(int anac, int numOffre) throws ParseException, IOException {
		HttpClient client = ExternalAppUtility.getHttpClient();
		HttpGet request = new HttpGet(SERVER_URL + "/onglet_etudes.html?cmp=cmp_formations.html&fct=notes&numOffre="
					+ numOffre + "&anac=" + anac );
		request.addHeader("Cookie", cookies);
		HttpResponse response = client.execute(request);
		String html = EntityUtils.toString(response.getEntity());
		
		
		int start = html.indexOf(BEGIN_HTML_NOTES_TABLE);
		if (start == -1) {
			return null;
		}
		int stop = html.indexOf(END_HTML_NOTES_TABLE,start);
		if (stop == -1) {
			return null;
		}
		
		ArrayList<Cours> cours = new ArrayList<Cours>();
		
		String notesTable = html.substring(start + BEGIN_HTML_NOTES_TABLE.length(), stop);

		ArrayList<String> lignes = HTMLAnalyser.getBalisesContent(notesTable, "tr");

		// On commence a 3 pour passer les 3 lignes d'en-tete.
		for (int i = 3; i < lignes.size(); i++) {
			ArrayList<String> cellules = HTMLAnalyser.getBalisesContent(lignes.get(i), "td");
			Log.d("UCLouvain", "Cellules : "+ cellules);
			Cours c = new Cours();
				
			c.coursCode = HTMLAnalyser.removeHTML(cellules.get(0)).replaceAll("[^A-Za-z0-9éùàèê ]", "");
			c.coursName = HTMLAnalyser.removeHTML(cellules.get(1)).replaceAll("[^A-Za-z0-9éùàèê ]", "");
			if (!c.coursCode.isEmpty()) {
				cours.add(c);
			}
		}
		return cours;
	}
}
