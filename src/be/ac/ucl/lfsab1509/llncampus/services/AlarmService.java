package be.ac.ucl.lfsab1509.llncampus.services;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends Service {
	private final IBinder mBinder = new LocalBinder();
	private final static int DEFAULT_NOTIFY_MINUTE = 15;
	private static Event nextEvent = null;

	public class LocalBinder extends Binder {
		AlarmService getService() {
			return AlarmService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	private void verifAlarm() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (!preferences.getBoolean("courses_notify", false)) {
			Log.d("AlarmService", "Les notifications de cours sont désactivées");
			return;
		}

		if (nextEvent == null) {
			loadNextEvent();
		}

		String nbMinStr = preferences.getString("notify_minute", null);
		int nbMin = 0;
		if (nbMinStr != null) {
			nbMin = Integer.valueOf(nbMinStr);
		}
		if (nbMin <= 0) {
			nbMin = DEFAULT_NOTIFY_MINUTE;
		}
		Time currentDate = new Time();
		currentDate.setToNow();

		if (nextEvent.getBeginTime().toMillis(false) - nbMin * 60L * 1000L
				- currentDate.toMillis(false) < 0L) {
			sendAlert(nextEvent);
			loadNextEvent();
		}
	}
	
	public static void resetNextEvent(){
		nextEvent = null;
	}

	private static void loadNextEvent() {
		Log.d("AlarmService", "NextEvent update");
		Database db = LLNCampus.getDatabase();
		long precTime;
		if (nextEvent == null) {
			Time currentDate = new Time();
			currentDate.setToNow();
			Log.d("AlarmService","currentDate.toMilis= " + currentDate.toMillis(false));
			precTime = currentDate.toMillis(false);
		} else {
			precTime = nextEvent.getBeginTime().toMillis(false);
		}
		Cursor c = db.sqlRawQuery("SELECT h.TIME_BEGIN, h.TIME_END, h.COURSE, h.ROOM, c.NAME "
						+ "FROM Horaire as h, Courses as c "
						+ "WHERE h.COURSE = c.CODE AND TIME_BEGIN > "
						+ precTime + " " + "ORDER BY TIME_BEGIN ASC LIMIT 1");
		Log.d("AlarmService", "Prec time : " + precTime);
		c.moveToFirst();
		nextEvent = new Event(c.getLong(0), c.getLong(1));
		nextEvent.addDetail("course", c.getString(2));
		nextEvent.addDetail("room", c.getString(3));
		nextEvent.addDetail("course_name", c.getString(4));
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
		String ringtone = preferences.getString("notify_ringtone", null);
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
		return START_STICKY;
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
