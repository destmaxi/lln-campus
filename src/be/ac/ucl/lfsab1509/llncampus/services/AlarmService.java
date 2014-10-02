package be.ac.ucl.lfsab1509.llncampus.services;

import be.ac.ucl.lfsab1509.llncampus.Coordinates;
import be.ac.ucl.lfsab1509.llncampus.Course;
import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.GPS;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.external.SecurePreferences;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

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
 * Class intended to create notifications for courses.
 */
public class AlarmService extends Service {
	private final IBinder mBinder = new LocalBinder();
	private final static int DEFAULT_NOTIFY_MINUTE = 15;
	private static final int DEFAULT_MAX_DISTANCE = 5000; // en m
	private static final int DEFAULT_NOTIFY_VITESSE = 5; // en km/h
	private static final double MIN_DISTANCE = 30; // en m
	private static Event nextEvent = null;
	private AlarmManager am;
	private static final long TIME_TO_REPEAT = 60000; //en millisecondes

	public class LocalBinder extends Binder {
		AlarmService getService() {
			return AlarmService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		/* Define the Context here, otherwise no Context before lauching the app */
		LLNCampus.setContext(getApplicationContext());
		am =(AlarmManager)LLNCampus.getContext().getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(LLNCampus.getContext(), AlarmService.class);
		PendingIntent pi = PendingIntent.getBroadcast(LLNCampus.getContext(), 0, i, 0);
		am.set(AlarmManager.ELAPSED_REALTIME, TIME_TO_REPEAT, pi);
	}

	private void verifAlarm() {
		SharedPreferences preferences = new SecurePreferences(this);
		if (!preferences.getBoolean(getString(R.string.pref_courses_notify), false)) {
			Log.d("AlarmService", "Les notifications de cours sont désactivées");
			return;
		}

		if (Course.getList().size() != 0 && nextEvent == null) {
			loadNextEvent();
		}

		if (nextEvent != null) // Sinon, peut planter!
		{

			int nbMin = LLNCampus.getIntPreference(getString(R.string.pref_notify_minute),
					DEFAULT_NOTIFY_MINUTE, this);

			if (preferences.getBoolean(getString(R.string.pref_notify_with_gps), false)) {
				Coordinates eventCoord = nextEvent.getCoordinates();
				if (eventCoord != null) {
					GPS gps = LLNCampus.getGPS();
					Coordinates currentCoord = gps.getPosition();
					if (currentCoord != null)
					{

						double dist = eventCoord.getDistance(currentCoord);
						if (dist > MIN_DISTANCE && dist < LLNCampus.getIntPreference(getString(R.string.pref_notify_max_distance),
								DEFAULT_MAX_DISTANCE, this)) {
							int vitesseKmH = LLNCampus.getIntPreference(getString(R.string.pref_notify_speed_move),DEFAULT_NOTIFY_VITESSE, this);
							int vitesseMMin = vitesseKmH*1000/60;

							nbMin = (int) (dist / vitesseMMin) + LLNCampus.getIntPreference(getString(R.string.pref_notify_more_time), 5, this);
						} 
					}
				} 
			}


			Time currentDate = new Time();
			currentDate.setToNow();

			if (Course.getList().size() != 0
					&& nextEvent.getBeginTime().toMillis(false) - nbMin * 60L
					* 1000L - currentDate.toMillis(false) < 0L) {
				sendAlert(nextEvent);
				loadNextEvent();
			}
		}
	}

	public static void resetNextEvent() {
		nextEvent = null;
	}

	private static void loadNextEvent() {
		Log.d("AlarmService", "NextEvent update");
		Database db = LLNCampus.getDatabase();
		long precTime;
		if (nextEvent == null) {
			Time currentDate = new Time();
			currentDate.setToNow();
			Log.d("AlarmService",
					"currentDate.toMilis= " + currentDate.toMillis(false));
			precTime = currentDate.toMillis(false);
		} else {
			precTime = nextEvent.getBeginTime().toMillis(false);
		}
		Cursor c = db
				.sqlRawQuery("SELECT h.TIME_BEGIN, h.TIME_END, h.COURSE, h.ROOM, c.NAME "
						+ "FROM Horaire as h, Courses as c "
						+ "WHERE h.COURSE = c.CODE AND TIME_BEGIN > "
						+ precTime + " " + "ORDER BY TIME_BEGIN ASC LIMIT 1");
		c.moveToFirst();
		
		try{
		nextEvent = new Event(c.getLong(0), c.getLong(1));
		nextEvent.addDetail("course", c.getString(2));
		nextEvent.addDetail("room", c.getString(3));
		nextEvent.addDetail("course_name", c.getString(4));
		c.close();
		} catch(CursorIndexOutOfBoundsException e) // No event yet
		{
			nextEvent = null;
		}
		
	}

	public void sendAlert(Event e) {
		final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Time currentDate = new Time();
		currentDate.setToNow();

		long nbMin = e.getBeginTime().toMillis(false) / 60L / 1000L
				- currentDate.toMillis(false) / 60L / 1000L;

		final NotificationCompat.Builder nb = new NotificationCompat.Builder(
				this)
				.setSmallIcon(android.R.drawable.ic_dialog_alert)
				.setContentTitle(
						"! " + e.getDetail("course") + " commence dans "
								+ nbMin + " minutes")
				.setContentText(
						e.getDetail("room") + " - "
								+ e.getDetail("course_name"))
				.setAutoCancel(true);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String ringtone = preferences.getString(getString(R.string.pref_notify_ringtone), null);
		if (ringtone == null) {
			nb.setSound(RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		} else {
			nb.setSound(Uri.parse(ringtone));
		}
		// nb.setStyle(inboxStyle);
		nm.notify(1, nb.build());

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		android.util.Log.i("LocalService", "Received start id " + startId
				+ ": " + intent);
		verifAlarm();
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		//return START_STICKY;
		return START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy() {

		// Tell the user we stopped.
		Toast.makeText(this,
				"Le service de notification de cours imminent a été arreté :(",
				Toast.LENGTH_SHORT).show();
	}
}