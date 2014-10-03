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

import be.ac.ucl.lfsab1509.llncampus.activity.LLNCampusActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
	Copyright (C) 2014 Quentin De Coninck

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
 * Class that enables connection to UCLouvain.be.
 */
public class UCLouvain {
	/** UCLouvain web address. */
	private static final String SERVER_URL = "https://www.uclouvain.be";
	/** Login path for UCLouvain. */
	private static final String LOGIN_PATH = "/page_login.html";
	/** Path for notes table for UCLouvain. */
	private static final String FORMATION_PATH = "/cmp_formations.html";
	/** Beginning of the notes table in the HTML. */
	private static final String BEGIN_HTML_NOTES_TABLE = "<td class=\"composant-titre-inter\">"
			+ "Crédit</td>\n</tr>";
	/** Ending of the notes table in the HTML. */
	private static final String END_HTML_NOTES_TABLE = "</table>\n<p>\n</p>\n"
			+ "<table align=\"center\" border=\"0\" cellpadding=\"2\" cellspacing=\"2\">";

	private String cookies = null;
	private boolean connected = false;

	/**
	 * Constructor. Create a connection with UCLouvain.
	 * 
	 * @param user
	 *            UCL global user identifier.
	 * @param password
	 *            UCL password.
	 */
	public UCLouvain(final String user, final String password) {
		connectToUCLouvain(user, password);
	}

	/**
	 * Establish a connection with UCLouvain.
	 * 
	 * @param user
	 *            UCL global user identifier.
	 * @param password
	 *            UCL password.
	 * @return true if connection succeeded, else false.
	 */
	private boolean connectToUCLouvain(final String user, final String password) {
		HttpClient client = ExternalAppUtility.getHttpClient();

		HttpPost request = new HttpPost(SERVER_URL + LOGIN_PATH);
		request.addHeader("Referer", SERVER_URL + LOGIN_PATH);
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("username", user));
		postParameters.add(new BasicNameValuePair("password", password));
		try {
			request.setEntity(new UrlEncodedFormEntity(postParameters));

			// Establishment of the connection.
			HttpResponse response = client.execute(request);

			// Record received cookies.
			this.cookies = "";
			for (Header hc : response.getHeaders("Set-Cookie")) {
				this.cookies += hc.getValue().substring(0,
						hc.getValue().indexOf(';') + 1);
			}
			if (!this.cookies.isEmpty()) {
				connected = true;
			}
			return true;
		} catch (Exception e) {
			Log.e("UCLouvain", e.getLocalizedMessage() + " - " + e.getMessage()
					+ " - ");
			return false;
		}
	}

	/**
	 * Get the list of "Offers"/"Studies" followed by the user.
	 * 
	 * @return The offers list if connection succeeded, else null.
	 */
	public ArrayList<Offer> getOffers() {
		if (!connected) {
			Log.d("OFFER", "Not connected");
			return null;
		}
		String html = ""; // A long string containing all the HTML
		ArrayList<Offer> offers = new ArrayList<Offer>();
		try {
			HttpClient client = ExternalAppUtility.getHttpClient();
			HttpGet request = new HttpGet(SERVER_URL + FORMATION_PATH);
			request.addHeader("Cookie", cookies);
			HttpResponse response = client.execute(request);

			html = EntityUtils.toString(response.getEntity());
			ArrayList<String> tables = HTMLAnalyser.getTagsContent(html, "table");

			// Get the last table of the page.
			String table = tables.get(tables.size() - 1);
			ArrayList<String> lines = HTMLAnalyser.getTagsContent(table, "tr");

			int academicYear, offerNumber;
			String offerCode, offerName;
			// Skip the header lines.
			for (int i = 1; i < lines.size(); i++) {
				ArrayList<String> cellules = HTMLAnalyser.getTagsContent(lines.get(i), "td");

				String academicYearStr = HTMLAnalyser.removeHTMLTag(cellules.get(0));
				academicYear = Integer.parseInt(academicYearStr.substring(0, 4));
				String code = cellules.get(1);
				int indexOffer = code.indexOf("numOffre=");
				offerNumber = Integer.parseInt(code.substring(indexOffer + 9, 
						code.indexOf("&", indexOffer)));

				offerCode = HTMLAnalyser.removeHTMLTag(code);
				offerName = HTMLAnalyser.removeHTMLTag(cellules.get(2));
				offers.add(new Offer(academicYear, offerNumber, offerCode, offerName));
			}
		} catch (Exception e) {
			Log.e("UCLouvain.java", "Error when connecting or analyzing HTML : " + e.getMessage());
			return null;
		}
		return offers;
	}

	/**
	 * Get the offers list for the academic year given as argument.
	 * 
	 * @param academicYear
	 *            Academic year.
	 * @return The offers list for the academic year academicYear or null in case of failure.
	 */
	public final ArrayList<Offer> getOffers(final int academicYear) {
		if (!connected) {
			Log.d("OFFER", "Not connected");
			return null;
		}

		ArrayList<Offer> offers = new ArrayList<Offer>();
		ArrayList<Offer> allOffers = getOffers();
		if (allOffers == null || allOffers.isEmpty()) {
			Log.e("UCLouvain", "No offer fetched");
			return null;
		}
		for (Offer o : allOffers) {
			if (o.getAcademicYear() == academicYear) {
				offers.add(o);
			}
		}
		return offers;
	}

	/**
	 * Get the courses list for an offer given as argument.
	 * 
	 * @param offer
	 *            The offer.
	 * @return Courses list of the offer or null in case of failure.
	 * @see UCLouvain#getCourses(int, int)
	 */
	public final ArrayList<Course> getCourses(final Offer offer) {
		try {
			return getCourses(offer.getAcademicYear(), offer.getOfferNumber());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the courses list for offers given as arguments.
	 * 
	 * @param offers
	 *            Offers list for which courses are requested.
	 * @return Courses list of offers or null in case of failure.
	 * @see UCLouvain#getCourses(int, int)
	 */
	public final ArrayList<Course> getCourses(final ArrayList<Offer> offers) {
		if (!connected) {
			Log.d("OFFER", "Not connected");
			return null;
		}

		ArrayList<Course> course = new ArrayList<Course>();
		if (offers == null) {
			return null;
		}
		for (Offer o : offers) {
			course.addAll(getCourses(o));
		}
		return course;
	}

	/**
	 * Get the courses list for a given offer number and an academic year.
	 * 
	 * @param academicYear
	 *            Academic year.
	 * @param offerNumber
	 *            Offer number.
	 * @return Courses list for given academic year and offer number or null in case of failure.
	 * @throws ParseException
	 *             When there is an analysis error of the page.
	 * @throws IOException
	 *             When there is a read error of the page.
	 */
	private ArrayList<Course> getCourses(int academicYear, int offerNumber)
			throws ParseException, IOException {
		HttpClient client = ExternalAppUtility.getHttpClient();
		HttpGet request = new HttpGet(SERVER_URL + FORMATION_PATH + "?fct=notes&numOffre="
				+ offerNumber + "&anac=" + academicYear);
		request.addHeader("Cookie", cookies);
		HttpResponse response = client.execute(request);
		String html = EntityUtils.toString(response.getEntity());

		ArrayList<Course> course = new ArrayList<Course>();
		int start = html.indexOf(BEGIN_HTML_NOTES_TABLE);
		if (start == -1) {
			Log.e("UCLouvain",
					"Failed to find the beginning of the notes table");
			return null;
		}
		int stop = html.indexOf(END_HTML_NOTES_TABLE, start);
		if (stop == -1) {
			Log.e("UCLouvain",
					"Failed to find the ending of the notes table");
			return null;
		}
		String notesTable = html.substring(start + BEGIN_HTML_NOTES_TABLE.length(), stop);
		ArrayList<String> lines = HTMLAnalyser.getTagsContent(notesTable, "tr");

		for (int i = 0; i < lines.size(); i++) {
			ArrayList<String> cells = HTMLAnalyser.getTagsContent(lines.get(i), "td");
			String code = HTMLAnalyser.removeHTMLTag(cells.get(0)).replaceAll(
					"[^A-Za-z0-9éùàèê ]", "");
			// A course code has always a length superior to 6 (usually 7 or 8)
			if (code.length() > 6) {
				String name = HTMLAnalyser.removeHTMLTag(cells.get(1))
						.replaceAll("[^A-Za-z0-9éùàèê ]", "");
				Course c = new Course(code, name);
				course.add(c);
			}
		}
		return course;
	}

	/**
	 * Launch the download of the courses list and store them in the database.
	 * 
	 * @param context
	 * 			Application context.
	 * @param username
	 *          UCL global user identifier.
	 * @param password
	 *          UCL password.
	 * @param end
	 *          Runnable to be executed at the end of the download.
	 * @param mHandler
	 * 			Handler to manage messages and threads.
	 *          
	 */
	public static void downloadCoursesFromUCLouvain(final LLNCampusActivity context, 
			final String username, final String password,
			final Runnable end, final Handler mHandler) {
		Time t = new Time();
		t.setToNow();
		int year = t.year;
		// A new academic year begin in September (8th month on 0-based count).
		if (t.month < 8) {
			year--;
		}
		final int academicYear = year;

		mHandler.post(new Runnable(){

			public void run(){

				final ProgressDialog mProgress = new ProgressDialog(context);
				mProgress.setCancelable(false);
				mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgress.setMax(100);
				mProgress.setMessage(context.getString(R.string.connection));
				mProgress.show();

				new Thread(new Runnable() {
					/**
					 * Set the progress to the value n and show the message nextStep
					 * @param n
					 * 			The progress value.
					 * @param nextStep
					 * 			The message to show (indicate the next step to be processed).
					 */
					public void progress(final int n, final String nextStep) {
						mHandler.post(new Runnable() {
							public void run() {
								mProgress.setProgress(n);
								mProgress.setMessage(nextStep);
							}
						});
						Log.d("UCLouvain", nextStep);
					}

					/**
					 * Notify to the user an error.
					 * 
					 * @param msg
					 * 			The message to show to the user.
					 */
					public void sendError(String msg) {
						notify(context.getString(R.string.error) + " : " + msg);
						mProgress.cancel();
					}

					/**
					 * Notify to the user a message.
					 * 
					 * @param msg
					 * 			The message to show to the user.
					 */
					public void notify(final String msg) {
						mHandler.post(new Runnable() {
							public void run() {
								context.notify(msg);
							}
						});
					}

					public void run() {
						progress(0, context.getString(R.string.connection_UCL));
						UCLouvain uclouvain = new UCLouvain(username, password);

						progress(20, context.getString(R.string.fetch_info));
						ArrayList<Offer> offers = uclouvain.getOffers(academicYear);

						if (offers == null || offers.isEmpty()) {
							sendError(context.getString(R.string.error_academic_year)
									+ academicYear);
							return;
						}

						int i = 40;
						ArrayList<Course> courses = new ArrayList<Course>();
						for (Offer o : offers) {
							progress(i, context.getString(R.string.get_courses)+ o.getOfferName());
							ArrayList<Course> offerCourses = uclouvain.getCourses(o);
							if (offerCourses != null && !offerCourses.isEmpty()) {
								courses.addAll(offerCourses);
							} else {
								Log.e("CoursListEditActivity",
										"Error : No course for offer [" + o.getOfferCode() + "] "
												+ o.getOfferName());
							}
							i += (int) (30. / offers.size());
						}

						if (courses.isEmpty()) {
							sendError(context.getString(R.string.courses_empty));
							return;
						}

						// Remove old courses.
						progress(70, context.getString(R.string.cleaning_db));
						LLNCampus.getDatabase().delete("Courses", "", null);
						LLNCampus.getDatabase().delete("Horaire", "", null);

						// Add new data.
						i = 80;
						for (Course c : courses) {
							progress(i, context.getString(R.string.add_courses_db));
							ContentValues cv = new ContentValues();
							cv.put("CODE", c.getCourseCode());
							cv.put("NAME", c.getCoursName());

							LLNCampus.getDatabase().insert("Courses", cv);
							i += (int) (20. / courses.size());
						}

						progress(100, context.getString(R.string.end));
						mProgress.cancel();
						mHandler.post(end);
					}
				}).start();
			}
		});
	}
}
