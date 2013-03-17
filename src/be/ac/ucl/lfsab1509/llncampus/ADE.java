package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import be.ac.ucl.lfsab1509.llncampus.activity.HoraireActivity;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.format.Time;
import android.util.Log;

/**
 * Gere la connexion a ADE et la recuperation des informations.
 * @author damien
 */
public final class ADE {

	/** Adresse du serveur ADE. */
	private static final String SERVER_URL = "http://horaire.sgsi.ucl.ac.be:8080";
	/** Page pour les infos. */
	private static final String INFO_PATH = "/ade/custom/modules/plannings/info.jsp?order=slot";
	/** Numéro du projet (variable en fonction de l'année). */
	private static final int PROJECT_ID = 9;
	/** Nom d'utilisateur ADE. */
	private static final String USER = "etudiant";
	/** Mot de passe ADE. */
	private static final String PASSWORD = "student";
	/** ID pour les notifications. */
	private static final Integer NOTIFY_ID = 1;
	
	private ADE() {
		
	}
	/**
	 * Etablit la connexion a ADE en specifiant les codes des cours dont ont veut 
	 * les informations.
	 * @param code Code des cours a recuperer.
	 * @param weeks Numero des semaines.
	 * @return true si la connexion a reussie, false sinon.
	 * @author Damien
	 */
	private static boolean connectADE(final String code, final String weeks) {
		HttpClient client = ExternalAppUtility.getHttpClient();
		HttpGet request = new HttpGet(
					SERVER_URL
					+ "/ade/custom/modules/plannings/direct_planning.jsp?weeks=" + weeks
					+ "&code=" + code 
					+ "&login=" + USER
					+ "&password=" + PASSWORD 
					+ "&projectId=" + PROJECT_ID + ""
				);
		try {
			client.execute(request);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	/**
	 * Charge les informations a propos des cours dont le code est donne en argument.
	 * @param code Code du cours  
	 * @param weeks Numéro des semaines
	 * @return Une liste d'evenement ou null en cas d'echec.
	 * @author Damien
	 */
	public static ArrayList<Event> getInfos(final String code, final String weeks) {
		String html = ""; // A long string containing all the HTML
		ArrayList<Event> events = new ArrayList<Event>();
		if (!connectADE(code, weeks)) { return null; }
		try {
			HttpClient client = ExternalAppUtility.getHttpClient();
			HttpGet request = new HttpGet(SERVER_URL + INFO_PATH);
			HttpResponse response = client.execute(request);

			html = EntityUtils.toString(response.getEntity());

			String table = HTMLAnalyser.getBalisesContent(html, "table").get(0); 
			ArrayList<String> lignes = HTMLAnalyser.getBalisesContent(table, "tr");
						
			// On commence a 2 pour passer les 2 lignes d'en-tete.
			for (int i = 2; i < lignes.size(); i++) {
				ArrayList<String> cellules = HTMLAnalyser.getBalisesContent(lignes.get(i), "td");
				
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
			Log.e("ADE.java", 
					"Erreur lors de la connexion ou de l'analyse du code HTML : "
							+ e.getMessage());
			e.printStackTrace();
			return null;
		}
		return events;
	}
	/**
	 * Lance la mise à jour des infos depuis ADE.
	 * @param ha Activite qui lance le thread de mise à jour
	 * @author Damien
	 * @param updateRunnable 
	 * @param handler 
	 */
	public static void runUpdateADE(final HoraireActivity ha, final Handler handler, 
			final Runnable updateRunnable) {
		final NotificationManager nm = 
				(NotificationManager) ha.getSystemService(Context.NOTIFICATION_SERVICE);
		final Builder nb = new NotificationCompat.Builder(ha)
			.setContentTitle("Mise a jour de ADE")
			.setContentText("Mise a jour en cours")
			.setSmallIcon(android.R.drawable.stat_notify_sync);		
		nm.notify(NOTIFY_ID, nb.build());
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				/*
				 * Recuperation des codes des cours a charger 
				 */
				ArrayList<String> courses = new ArrayList<String>();
				Cursor c = 
						LLNCampus.getDatabase().select(
								"Courses", new String[]{"CODE"}, 
								null, null, null, null, null, null);
				while (c.moveToNext()) {
					courses.add(c.getString(0));
				}
				c.close();
				
				
				/*
				 * Numéro des semaines
				 * FIXME : Pour tout télécharger, les numéros vont de 0 à 51
				 * (0 = debut 1e quadri, 51 = fin 2e session d'examen) 
				 */
				String weeks = "";
				Time today = new Time(Time.getCurrentTimezone());
				today.setToNow();
				for (int i = 0; i  < 51; i++) {
					if (!weeks.isEmpty()) { weeks += ','; }
					weeks += i; 
					//weeks += (i + today.getWeekNumber() + 13); 
					//FIXME Trouver qqch de moins variable et qui fonctionne pr les 2 quadri... 
				}
				Log.d("ADE", "Weeks : " + weeks + "\n");

				/*
				 * Recuperation des donnees depuis ADE et mise a jour de la base de donnee
				 */
				int nbError = 0;
				ArrayList<Event> events;
				
				for (String courseCode : courses) {
					nb.setContentText("Téléchargement pour " + courseCode + "...");
					nm.notify(NOTIFY_ID, nb.build());
					events = ADE.getInfos(courseCode, weeks);
					if (events == null) {
						nb.setContentText("Le contenu de " + courseCode 
								+ " n'a pu etre telecharge");
						nm.notify(NOTIFY_ID, nb.build());
						Log.e("ADE", "Le contenu de " + courseCode + " n'a pu etre telecharge");
						nbError++;
					} else {
						// Suppression des anciennes donnees
						LLNCampus.getDatabase().delete("Horaire", "COURSE = ?", 
								new String[]{courseCode});
						// Ajout des nouvelles donnees
						for (Event e : events) {
							ContentValues cv = e.toContentValues();
							cv.put("COURSE", courseCode);
							if (LLNCampus.getDatabase().insert("Horaire", cv) < 0) {
								nbError++;
							}
						}
					}
				}
				nb.setContentText("Termine. Nombre d'erreurs : " + nbError);
				nm.notify(NOTIFY_ID, nb.build());
				
				handler.post(updateRunnable);

			}
		}).start();
	}

}