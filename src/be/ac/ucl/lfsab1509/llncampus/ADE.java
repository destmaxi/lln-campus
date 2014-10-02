package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import be.ac.ucl.lfsab1509.llncampus.activity.ScheduleActivity;
import be.ac.ucl.lfsab1509.llncampus.external.SecurePreferences;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
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
 */

/**
 * Manage the connection to and the fetching of information from ADE.
 */
public final class ADE {

	/** ADE server address */
	private static final String SERVER_URL = "http://horairev6.uclouvain.be";
	/** Page to get information */
	private static final String INFO_PATH = "/jsp/custom/modules/plannings/info.jsp"
			+ "?displayConfName=WEB&order=slot";
	/** Project number (depends of the year, e.g. 2012-2013: 9, 2013-2014: 16, 2014-2015-6). */
	private static final int PROJECT_ID = 6;
	/** ADE username. */
	private static final String USER = "etudiant";
	/** ADE password. */
	private static final String PASSWORD = "student";
	/** ID for notifications. */
	private static final Integer NOTIFY_ID = 1;

	/**
	 * Constructor (empty).
	 */
	private ADE() {

	}

	/**
	 * Establish the connection to ADE in specifying code of courses 
	 * in which information is requested
	 * 
	 * @param code
	 *            Code of courses to fetch, separated by comma (e.g. "lelec1530,lfsab1509").
	 * @param weeks
	 *            Week numbers, separated by comma (e.g. "0,1,2,3,42").
	 * @return true if connection succeeded, else false.
	 */
	private static boolean connectADE(final String code, final String weeks) {
		HttpClient client = ExternalAppUtility.getHttpClient();
		HttpGet request = new HttpGet((SERVER_URL
				+ "/jsp/custom/modules/plannings/direct_planning.jsp?weeks="
				+ weeks + "&code=" + code + "&login=" + USER + "&password="
				+ PASSWORD + "&projectId=" + PROJECT_ID + "&showTabDuration=true"
				+ "&showTabDate=true&showTab=true&showTabWeek=false&showTabDay=false" 
				+ "&showTabStage=false&showTabResources=false"
				+ "&showTabCategory6=false&showTabCategory7=false"
				+ "&showTabCategory8=false").replace(' ', '+'));
		try {
			client.execute(request);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Fetch information about courses which codes are given as parameter
	 * 
	 * @param code
	 *            Code of courses to fetch, separated by comma (e.g. "lelec1530,lfsab1509").
	 * @param weeks
	 *            Week numbers, separated by comma (e.g. "0,1,2,3,42").
	 * @return An ArrayList of Events or null if failure.
	 */
	public static ArrayList<Event> getInfo(final String code,
			final String weeks) {
		String html = ""; // A long string containing all the HTML
		ArrayList<Event> events = new ArrayList<Event>();
		if (!connectADE(code, weeks)) {
			return null;
		}
		try {
			HttpClient client = ExternalAppUtility.getHttpClient();
			HttpGet request = new HttpGet(SERVER_URL + INFO_PATH);
			HttpResponse response = client.execute(request);

			html = EntityUtils.toString(response.getEntity());

			String table = HTMLAnalyser.getBalisesContent(html, "table").get(0);
			ArrayList<String> lines = HTMLAnalyser.getBalisesContent(table, "tr");

			// Skip the 2 header lines.
			for (int i = 2; i < lines.size(); i++) {
				ArrayList<String> cells = HTMLAnalyser.getBalisesContent(lines.get(i), "td");

				String date = HTMLAnalyser.removeHTML(cells.get(0));
				String beginHour = HTMLAnalyser.removeHTML(cells.get(2));
				String duration = HTMLAnalyser.removeHTML(cells.get(3));
				
				Event event = new Event(date, beginHour, duration);
				event.addDetail("trainees",
						HTMLAnalyser.removeHTML(cells.get(6)));
				event.addDetail("trainers",
						HTMLAnalyser.removeHTML(cells.get(7)));
				event.addDetail("room",
						HTMLAnalyser.removeHTML(cells.get(8)));
				event.addDetail("course",
						HTMLAnalyser.removeHTML(cells.get(9)));
				event.addDetail("activity_name",
						HTMLAnalyser.removeHTML(cells.get(1)));
				events.add(event);
			}
		} catch (Exception e) {
			Log.e("ADE.java", "Error with connection or analysis of HTML : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return events;
	}

	/**
	 * Start information update from ADE.
	 * 
	 * @param sa
	 *            Activity that launches the update thread.
	 * @param updateRunnable
	 *            Runnable that launches the display update.
	 * @param handler
	 *            Handler to allow the display update at the end of the execution.
	 */
	public static void runUpdateADE(final ScheduleActivity sa,
			final Handler handler, final Runnable updateRunnable) {
		final Resources r = sa.getResources();

		final NotificationManager nm = (NotificationManager) sa
				.getSystemService(Context.NOTIFICATION_SERVICE);

		final NotificationCompat.Builder nb = new NotificationCompat.Builder(sa)
				.setSmallIcon(android.R.drawable.stat_sys_download)
				.setContentTitle(r.getString(R.string.download_from_ADE))
				.setContentText(r.getString(R.string.download_progress))
				.setAutoCancel(true);

		final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle(r.getString(R.string.download_from_ADE));

		nm.notify(NOTIFY_ID, nb.build());
		new Thread(new Runnable() {
			@Override
			public void run() {

				/*
				 * Fetching of code of courses to load.
				 */
				ArrayList<Course> courses = Course.getList();
							
				if (courses == null || courses.isEmpty()) {
					// Take the context of the ScheduleActivity
					SharedPreferences preferences = new SecurePreferences(sa);
					String username = preferences.getString("username", null);
					String password = preferences.getString("password", null);
					if(username != null && password != null){
						UCLouvain.downloadCoursesFromUCLouvain(sa, username, password, new Runnable(){
							public void run() {
								runUpdateADE(sa, handler, updateRunnable);
							}
						}, handler);
					}
					
				}

				/*
				 * Number of weeks. To download all the schedule, the numbers go from 0 to 51
				 * (0 = beginning of first semester, 51 = ending of the September exams session).
				 */
				String weeks = getWeeks();

				/*
				 * Fetching data from ADE and updating the database
				 */
				int nbError = 0;
				int i = 0;
				ArrayList<Event> events;
				for (Course course : courses) {
					i++;
					nb.setProgress(courses.size(), i, false);
					nb.setContentText(r.getString(R.string.download_for)
							+ " " + course.getCourseCode() + "...");
					nm.notify(NOTIFY_ID, nb.build());
					events = ADE.getInfo(course.getCourseCode(), weeks);
					if (events == null) {
						nbError++;
						inboxStyle.addLine(course.getCourseCode() + " : "
								+ r.getString(R.string.download_error));
					} else {
						// Removing old data
						LLNCampus.getDatabase().delete("Horaire", "COURSE = ?",
								new String[] {course.getCourseCode()});
						// Adding new data
						for (Event e : events) {
							ContentValues cv = e.toContentValues();
							cv.put("COURSE", course.getCourseCode());
							if (LLNCampus.getDatabase().insert("Horaire", cv) < 0) {
								nbError++;
							}
						}
						inboxStyle.addLine(course.getCourseCode() + " : "
								+ events.size() + " "
								+ r.getString(R.string.events));
						nb.setStyle(inboxStyle);
					}
				}

				nb.setProgress(courses.size(), courses.size(), false);

				if (nbError == 0) {
					nb.setContentText(r.getString(R.string.download_done));
					inboxStyle.setBigContentTitle(r
							.getString(R.string.download_done));
					nb.setSmallIcon(android.R.drawable.stat_sys_download_done);
					nb.setSound(RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
				} else {
					nb.setContentText(r.getString(R.string.download_done)
							+ ". " + r.getString(R.string.nb_error) + nbError
							+ " :/");
					inboxStyle.setBigContentTitle(r
							.getString(R.string.download_done)
							+ ". "
							+ r.getString(R.string.nb_error) + nbError + " :/");
					nb.setSmallIcon(android.R.drawable.stat_notify_error);
					nb.setSound(RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
				}
				nb.setStyle(inboxStyle);
				nm.notify(NOTIFY_ID, nb.build());

				handler.post(updateRunnable);

			}
		}).start();
	}

	/**
	 * Generate a full list of week numbers for ADE.
	 * 
	 * @return Week numbers separated by commas to fetch a full year for schelude (from 0 to 51).
	 */
	private static String getWeeks() {
		String weeks = "";
		for (int i = 0; i < 51; i++) {
			if (!weeks.isEmpty()) {
				weeks += ',';
			}
			weeks += i;
		}
		return weeks;
	}
}