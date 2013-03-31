package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import be.ac.ucl.lfsab1509.llncampus.activity.HoraireActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
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
		HttpGet request = new HttpGet((
					SERVER_URL
					+ "/ade/custom/modules/plannings/direct_planning.jsp?weeks=" + weeks
					+ "&code=" + code 
					+ "&login=" + USER
					+ "&password=" + PASSWORD 
					+ "&projectId=" + PROJECT_ID + "").replace(' ', '+')
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

		final NotificationCompat.Builder nb = new NotificationCompat.Builder(ha)
	    .setSmallIcon(android.R.drawable.stat_sys_download)
	    .setContentTitle("Téléchargement de l'horaire depuis ADE")
	    .setContentText("Téléchargement en cours...")
	    .setAutoCancel(true);
		
		final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle("Téléchargement de l'horaire depuis ADE");
		
		nm.notify(NOTIFY_ID, nb.build());
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				/*
				 * Recuperation des codes des cours a charger 
				 */
				
				ArrayList<Cours> courses = Cours.getList();
				
				
				/*
				 * Numéro des semaines
				 * FIXME : Pour tout télécharger, les numéros vont de 0 à 51
				 * (0 = debut 1e quadri, 51 = fin 2e session d'examen) 
				 */
				String weeks = getWeeks();

				/*
				 * Recuperation des donnees depuis ADE et mise a jour de la base de donnee
				 */

				int nbError = 0;
				int i = 0;
				ArrayList<Event> events;
				for (Cours course : courses) {
					i++;
					nb.setProgress(courses.size(), i, false);
					nb.setContentText("Téléchargement pour " + course.coursCode + "...");
					nm.notify(NOTIFY_ID, nb.build());
					events = ADE.getInfos(course.coursCode, weeks);
					if (events == null) {
						nbError++;
						inboxStyle.addLine(course.coursCode + " : Erreur de téléchargement");
					} else {
						// Suppression des anciennes donnees
						LLNCampus.getDatabase().delete("Horaire", "COURSE = ?", 
								new String[]{course.coursCode});
						// Ajout des nouvelles donnees
						for (Event e : events) {
							ContentValues cv = e.toContentValues();
							cv.put("COURSE", course.coursCode);
							if (LLNCampus.getDatabase().insert("Horaire", cv) < 0) {
								nbError++;
							}
						}
						inboxStyle.addLine(course.coursCode + " : " + events.size() + " evenements");
						nb.setStyle(inboxStyle);
					}
				}

				nb.setProgress(courses.size(), courses.size(), false);

				if (nbError == 0) {
					nb.setContentText("Téléchargement terminé.");
					inboxStyle.setBigContentTitle("Téléchargement terminé.");
					nb.setSmallIcon(android.R.drawable.stat_sys_download_done);
					nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
				} else {
					nb.setContentText("Téléchargement terminé. Nombre d'erreur : " + nbError + " :/");
					inboxStyle.setBigContentTitle("Téléchargement terminé. Nombre d'erreur : " + nbError + " :/");
					nb.setSmallIcon(android.R.drawable.stat_notify_error);
					nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
				}
				nb.setStyle(inboxStyle);
				nm.notify(NOTIFY_ID, nb.build());
				
				handler.post(updateRunnable);

			}
		}).start();
	}
	public static String getWeeks() {
		String weeks = "";
		for (int i = 0; i  < 51; i++) {
			if (!weeks.isEmpty()) { weeks += ','; }
			weeks += i; 
		}
		return weeks;
	}

}