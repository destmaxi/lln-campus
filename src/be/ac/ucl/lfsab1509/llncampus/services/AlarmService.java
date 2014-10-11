package be.ac.ucl.lfsab1509.llncampus.services;

import be.ac.ucl.lfsab1509.llncampus.Coordinates;
import be.ac.ucl.lfsab1509.llncampus.Course;
import be.ac.ucl.lfsab1509.llncampus.Event;
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
 * Class intended to create notifications for courses.
 */
public class AlarmService extends Service {
	
	/** Default time to notify before the event begins, in minutes. */
	private static final int DEFAULT_NOTIFY_MINUTE = 15;
	/** Default maximal distance of the event to notify, in meters. */
	private static final int DEFAULT_MAX_DISTANCE = 5000;
	/** Default speed of the user to compute time to reach the destination, in km/h. */
	private static final int DEFAULT_NOTIFY_SPEED = 5;
	/** Default time added to the computation of time to notify when user position is used. */
	private static final int DEFAULT_MORE_TIME = 5;
	/** Minimal distance of the event to notify, in meters. */
	private static final double MIN_DISTANCE = 30;
	/** Time period between potential notifications, in milliseconds. */
	private static final long TIME_TO_REPEAT = 60000;
	
	/** The next event that will occur. */
	private static Event nextEvent = null;
	
	private final IBinder mBinder = new LocalBinder();
	private AlarmManager am;

	@Override
	public void onCreate() {
		super.onCreate();
		// Define the Context here, otherwise no Context before launching the application.
		LLNCampus.setContext(getApplicationContext());
		am = (AlarmManager) LLNCampus.getContext().getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(LLNCampus.getContext(), AlarmService.class);
		PendingIntent pi = PendingIntent.getBroadcast(LLNCampus.getContext(), 0, i, 0);
		am.set(AlarmManager.ELAPSED_REALTIME, TIME_TO_REPEAT, pi);
	}

	/**
	 * Check if an event will occur and send a alert if needed.
	 */
	private void checkAlarm() {
		SharedPreferences preferences = new SecurePreferences(this);
		if (!preferences.getBoolean(getString(R.string.pref_courses_notify), false)) {
			Log.d("AlarmService", "Course notifications are disabled.");
			return;
		}

		if (Course.getList().size() != 0 && nextEvent == null) {
			loadNextEvent();
		}

		if (nextEvent != null) // If there is a next event, check if an alert should be sent.
		{

			int nbMin = LLNCampus.getIntPreference(getString(R.string.pref_notify_minute),
					DEFAULT_NOTIFY_MINUTE, this);

			// Added computations if notifications depend on the position of the user.
			if (preferences.getBoolean(getString(R.string.pref_notify_with_gps), false)) {
				Coordinates eventCoord = nextEvent.getCoordinates();
				if (eventCoord != null) {
					Coordinates currentCoord = LLNCampus.getGPS().getPosition();
					if (currentCoord != null)
					{
						double dist = eventCoord.getDistance(currentCoord);
						if (dist > MIN_DISTANCE && dist < LLNCampus
								.getIntPreference(getString(R.string.pref_notify_max_distance),
										DEFAULT_MAX_DISTANCE, this)) {
							int speedInKmPerHour = LLNCampus.getIntPreference(
									getString(R.string.pref_notify_speed_move), 
										DEFAULT_NOTIFY_SPEED, this);
							int speedMetersPerMinute = speedInKmPerHour * 1000 / 60;
							nbMin = (int) (dist / speedMetersPerMinute) 
									+ LLNCampus.getIntPreference(
											getString(R.string.pref_notify_more_time), 
											DEFAULT_MORE_TIME, this);
						} 
					}
				} 
			}
			Time currentTime = new Time();
			currentTime.setToNow();

			// Check if a notification should be sent.
			if (Course.getList().size() != 0 && nextEvent.getBeginTime().toMillis(false) 
					- nbMin * 60L * 1000L - currentTime.toMillis(false) < 0L) {
				sendAlert(nextEvent);
				loadNextEvent();
			}
		}
	}

	/**
	 * Set to null the next event.
	 */
	private static void resetNextEvent() {
		nextEvent = null;
	}

	/**
	 * Load the next event in the nextEvent variable.
	 */
	private static void loadNextEvent() {
		Log.d("AlarmService", "nextEvent update");
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
		Cursor c = LLNCampus.getDatabase()
				.sqlRawQuery("SELECT h.TIME_BEGIN, h.TIME_END, h.COURSE, h.ROOM, c.NAME "
						+ "FROM Horaire as h, Courses as c "
						+ "WHERE h.COURSE = c.CODE AND TIME_BEGIN > "
						+ precTime + " " + "ORDER BY TIME_BEGIN ASC LIMIT 1");
		c.moveToFirst();
		try{
			nextEvent = new Event(c.getLong(0), c.getLong(1));
			nextEvent.addDetail(Event.COURSE, c.getString(2));
			nextEvent.addDetail(Event.ROOM, c.getString(3));
			nextEvent.addDetail(Event.TITLE, c.getString(4));
			c.close();
		} catch(CursorIndexOutOfBoundsException e) // No event yet.
		{
			resetNextEvent();
		}
	}

	/**
	 * Send an alert to the user for the event e.
	 * 
	 * @param e
	 * 			Event to notify to the user.
	 */
	private void sendAlert(Event e) {
		final NotificationManager notificationManager = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		Time currentTime = new Time();
		currentTime.setToNow();

		long nbMin = e.getBeginTime().toMillis(false) / 60L / 1000L
				- currentTime.toMillis(false) / 60L / 1000L;

		final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(android.R.drawable.ic_dialog_alert)
				.setContentTitle(
						e.getDetail(Event.COURSE) + " " + getString(R.string.begins_in) + " "
								+ nbMin + " " + getString(R.string.minutes))
				.setContentText(
						e.getDetail(Event.ROOM) + " - " + e.getDetail(Event.TITLE))
				.setAutoCancel(true);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String ringtone = preferences.getString(getString(R.string.pref_notify_ringtone), null);
		if (ringtone == null) {
			notificationBuilder.setSound(RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		} else {
			notificationBuilder.setSound(Uri.parse(ringtone));
		}
		notificationManager.notify(1, notificationBuilder.build());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		android.util.Log.i("LocalService", "Received start id " + startId + ": " + intent);
		checkAlarm();
		return START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy() {
		// Tell the user we stopped.
		Toast.makeText(this, getString(R.string.notification_stop), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * A class to offer remote procedures for the AlarmService.
	 */
	public class LocalBinder extends Binder {
		
		/**
		 * Get the AlarmService.
		 * 
		 * @return AlarmService. 
		 */
		public AlarmService getService() {
			return AlarmService.this;
		}
	}
}