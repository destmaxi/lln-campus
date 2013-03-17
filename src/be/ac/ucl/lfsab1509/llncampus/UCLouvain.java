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

import android.util.Log;
/**
 * Classe permettant la connexion à UCLouvain.be. 
 * @author Damien
 *
 */
public class UCLouvain {
	/** Adresse du serveur ADE. */
	private static final String SERVER_URL = "https://www.uclouvain.be";
	/** Page pour les infos. */
	private static final String LOGIN_PATH = "/page_login.html";
	private static final String BEGIN_HTML_NOTES_TABLE = 
			"<td class=\"composant-titre-inter\">Crédit</td>\n</tr>";
	private static final String END_HTML_NOTES_TABLE = 
			"</table>\n<p>\n</p>\n<table align=\"center\" border=\"0\" cellpadding=\"2\" cellspacing=\"2\">";
	
	private String cookies = null;
	private boolean connected = false;
	
	/**
	 * Création d'une connexion avec UCLouvain.
	 * @param user Identifiant global UCL
	 * @param password Mot de passe UCL
	 */
	public UCLouvain(final String user, final String password) {
		connectUCLouvain(user, password);
	}
	
	
	/**
	 * Établit la connexion avec UCLouvain.
	 * @param user Identifiant global UCL
	 * @param password Mot de passe. 
	 * @return true si la connexion a reussie, false sinon.
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
	
			//On établi la connexion.
			HttpResponse response = client.execute(request);
		
			// On enregistre les cookies reçu.
			this.cookies = "";
			for (Header hc : response.getHeaders("Set-Cookie")) {
				this.cookies += hc.getValue().substring(0, hc.getValue().indexOf(';') + 1);
			}
			if (!this.cookies.isEmpty()) {
				connected = true;
			}
			return true;
		} catch (Exception e) {
			Log.e("UCLouvain", e.getLocalizedMessage() + " - " + e.getMessage() + " - ");
			return false;
		}
	}
	
	/**
	 * Obtention de la liste des "offres"/"études" suivient par l'utilisateur.
	 * @return Liste des offres
	 */
	public ArrayList<Offre> getOffres() {
		if (!connected) { return null; }
		String html = ""; // A long string containing all the HTML
		ArrayList<Offre> offres = new ArrayList<Offre>();
		try {
			HttpClient client = ExternalAppUtility.getHttpClient();
			HttpGet request = new HttpGet(SERVER_URL + "/onglet_etudes.html");
			
			request.addHeader("Cookie", cookies);

			HttpResponse response = client.execute(request);
			
			html = EntityUtils.toString(response.getEntity());
		
			ArrayList<String> tables = HTMLAnalyser.getBalisesContent(html, "table");
			String table = tables.get(tables.size() - 1); // Dernier tableau de la page.
			ArrayList<String> lignes = HTMLAnalyser.getBalisesContent(table, "tr");
						
			// On commence a 2 pour passer les 2 lignes d'en-tete.
			for (int i = 2; i < lignes.size(); i++) {
				ArrayList<String> cellules = HTMLAnalyser.getBalisesContent(lignes.get(i), "td");
				Log.d("DEBUG UCLouvain", "Cellules : " + cellules);
				Offre o = new Offre();
				String anneeAnac = HTMLAnalyser.removeHTML(cellules.get(0));
				o.anac = Integer.parseInt(anneeAnac.substring(0, 4));
				
				String code = cellules.get(1);
				int j = code.indexOf("numOffre=");
				o.numOffre = Integer.parseInt(code.substring(j + 9, code.indexOf("&", j)));
				
				o.offreCode = HTMLAnalyser.removeHTML(code);
				
				o.offreName = HTMLAnalyser.removeHTML(cellules.get(2));
				
				offres.add(o);
			}
		} catch (Exception e) {
			Log.e("UCLouvain.java", 
					"Erreur lors de la connexion ou de l'analyse du code HTML : " 
							+ e.getMessage());
			return null;
		}
		return offres;
	}
	
	/**
	 * Retourne la liste des cours pour les offres passées en arguments.
	 * @param offres Liste des offres pour lesquels il faut récupérer les cours. 
	 * @return La liste des cours ou null en cas d'erreur. 
	 */
	public ArrayList<Cours> getCourses(final ArrayList<Offre> offres) {
		if (!connected) { return null; }

		ArrayList<Cours> cours = new ArrayList<Cours>();
		if (offres == null) {
			return null;
		}
		for (Offre o : offres) {
			cours.addAll(getCourses(o));
		}
		return cours;
	}
	/**
	 * Retourne la liste des cours pour une offre passé en argument.
	 * @param o L'offre a considerer
	 * @return Une liste de cours ou null en cas d'erreur
	 * @see getCourses
	 */
	public ArrayList<Cours> getCourses(final Offre o) {
		try {
			return getCourses(o.anac, o.numOffre);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Fournit la liste des offres de l'annee académique passé en argument. 
	 * @param anac année académique.
	 * @return 	La liste des offres pour l'année académique passé en argument
	 * 			ou null en cas d'erreur.
	 */
	public ArrayList<Offre> getOffres(final int anac) {
		if (!connected) { return null; }

		ArrayList<Offre> offres = new ArrayList<Offre>();
		for (Offre o : getOffres()) {
			if (o.anac == anac) {
				offres.add(o);
			}
		}
		return offres;
	}

	/**
	 * Retourne la liste des cours pour un numéro d'offre et une année académique.
	 * @param anac Année académique
	 * @param numOffre Numéro de l'offre
	 * @return Liste de cours pour l'année académique et le numéro de l'offre indiqué
	 * @throws ParseException
	 * @throws IOException
	 */
	private ArrayList<Cours> getCourses(int anac, int numOffre) 
			throws ParseException, IOException {
		HttpClient client = ExternalAppUtility.getHttpClient();
		HttpGet request = new HttpGet(SERVER_URL 
					+ "/onglet_etudes.html?cmp=cmp_formations.html&fct=notes&numOffre=" + numOffre 
					+ "&anac=" + anac);
		request.addHeader("Cookie", cookies);
		HttpResponse response = client.execute(request);
		String html = EntityUtils.toString(response.getEntity());
		
		
		int start = html.indexOf(BEGIN_HTML_NOTES_TABLE);
		if (start == -1) {
			return null;
		}
		int stop = html.indexOf(END_HTML_NOTES_TABLE, start);
		if (stop == -1) {
			return null;
		}
		
		ArrayList<Cours> cours = new ArrayList<Cours>();
		
		String notesTable = html.substring(start + BEGIN_HTML_NOTES_TABLE.length(), stop);

		ArrayList<String> lignes = HTMLAnalyser.getBalisesContent(notesTable, "tr");

		// On commence a 3 pour passer les 3 lignes d'en-tete.
		for (int i = 3; i < lignes.size(); i++) {
			ArrayList<String> cellules = HTMLAnalyser.getBalisesContent(lignes.get(i), "td");
			Log.d("UCLouvain", "Cellules : " + cellules);
			Cours c = new Cours();
				
			c.coursCode = HTMLAnalyser.removeHTML(
					cellules.get(0)).replaceAll("[^A-Za-z0-9éùàèê ]", "");
			c.coursName = HTMLAnalyser.removeHTML(
					cellules.get(1)).replaceAll("[^A-Za-z0-9éùàèê ]", "");
			if (!c.coursCode.isEmpty()) {
				cours.add(c);
			}
		}
		return cours;
	}
}
