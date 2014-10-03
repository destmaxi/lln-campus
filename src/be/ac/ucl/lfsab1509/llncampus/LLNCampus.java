package be.ac.ucl.lfsab1509.llncampus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import be.ac.ucl.lfsab1509.llncampus.activity.SettingsActivity;
import be.ac.ucl.lfsab1509.llncampus.external.SecurePreferences;
import be.ac.ucl.lfsab1509.llncampus.services.AlarmService;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.Environment;
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
 * Base class of LLNCampus, provide some useful information for all other classes
 */

public class LLNCampus extends Application {
	private static Context APPLICATION_CONTEXT;
	private static Database DB;
	private static GPS gps = null;
	public static String LLNREPOSITORY = "LLNCampus";

	@Override
	public final void onCreate() {
		super.onCreate();
		//initialization for the application context here
		setContext(this);
	}
	
	/**
	 * Set the application context to the context c.
	 * 
	 * @param c
	 * 			The context.
	 */
	public static void setContext(Context c){
		APPLICATION_CONTEXT = c;
	}
	
	/**
	 * Close the database of LLNCampus.
	 */
	public final synchronized void close(){
		if (DB != null) {
			DB.close();
			DB = null;
		}
	}
	
	/**
	 * Get the GPS object used in the application (and create a new one if it does exist yet).
	 * 
	 * @return GPS object.
	 */
	public static GPS getGPS() {
		if (gps == null) {
			gps = new GPS();
		}
		return gps;
	}
	
	/**
	 * Stop the GPS service.
	 */
	public static void stopGPS() {
		if ( gps != null ) {
			gps.destroy();
			gps = null;
		}
	}
	
	/**
	 * Start the alarm service (for notifications of the student schedule).
	 */
	public static void startAlarmService(){
		Intent intent = new Intent(APPLICATION_CONTEXT, AlarmService.class);
		PendingIntent pintent = PendingIntent.getService(APPLICATION_CONTEXT, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) APPLICATION_CONTEXT.getSystemService(Context.ALARM_SERVICE);
		// Every minute
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 60*1000, pintent); 
	}
	
	/**
	 * Get a Database object to interact with the database (create a new object if it doesn't exist yet).
	 * 
	 * @return Database object.
	 */
	public static Database getDatabase() {
		if(DB == null) { 
			DB = new Database(APPLICATION_CONTEXT);
		}
		return DB;
	}
	
	/**
	 * Get the application context.
	 * @return Application context.
	 */
	public static Context getContext() {
		return APPLICATION_CONTEXT;
	}
	
	/**
	 * Get the value of a preference that is intended to contains an integer value.
	 * 
	 * @param key
	 * 			Key of the preference.
	 * @param defaultValue
	 * 			Default value if the key doesn't exist.
	 * @param context
	 * 			Application context.
	 * @return Integer value of the preference with key key if key exists, else defaultValue.
	 */
	public static int getIntPreference(String key, int defaultValue, Context context) {
		try {
			SharedPreferences preferences = new SecurePreferences(context);
			String str = preferences.getString(key, null);
			if(str == null){
				return defaultValue;
			}
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		} catch (NullPointerException e){
			return defaultValue;
		}
	}
	
	/**
	 * Convert non-encrypted preferences into encrypted ones, and delete non-encrypted data.
	 * @param mInsecurePrefs
	 * 					Non-encrypting SharedPreferences.
	 * @param mSecurePrefs
	 * 					Encrypting SharedPreferences.
	 */
    public static void convertInsecureToSecurePreferences(SharedPreferences mInsecurePrefs, 
    		SharedPreferences mSecurePrefs) {
    	Editor insecureEditor = mInsecurePrefs.edit();
    	Editor secureEditor = mSecurePrefs.edit();
    	
    	String key = SettingsActivity.USERNAME;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = SettingsActivity.PASSWORD;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = SettingsActivity.COURSES_NOTIFY;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putBoolean(key, mInsecurePrefs.getBoolean(key, false));
    		insecureEditor.remove(key);
    	}
    	key = SettingsActivity.NOTIFY_MINUTE;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = SettingsActivity.NOTIFY_WITH_GPS;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putBoolean(key, mInsecurePrefs.getBoolean(key, false));
    		insecureEditor.remove(key);
    	}
    	key = SettingsActivity.NOTIFY_SPEED_MOVE;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = SettingsActivity.NOTIFY_MAX_DISTANCE;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = SettingsActivity.NOTIFY_MORE_TIME;
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	
    	insecureEditor.commit();
    	secureEditor.commit();
    }
	
	/**
	 * Create and copy assets into folder sdcard/LLNREPOSITORY
	 */
	public static void copyAssets() {
	    AssetManager assetManager = getContext().getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("LLNCampus.java", "Failed to get asset file list.", e);
	    }
	    File f = new File("/" + Environment.getExternalStorageDirectory().getPath() + "/" + LLNREPOSITORY);
	    if (!f.exists()) {
	      f.mkdir();
	    }
	    for(String filename : files) {
	    	Log.d("FILE", filename);
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          out = new FileOutputStream("/" + Environment.getExternalStorageDirectory().getPath() + "/" + LLNREPOSITORY + "/" + filename);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(IOException e) {
	            Log.e("LLNCampus.java", "Failed to copy asset file: " + filename, e);
	        }       
	    }
	}
	
	/**
	 * Copy a binary stream from in to out.
	 * @param in
	 * 			Input stream.
	 * @param out
	 * 			Output stream
	 * @throws IOException
	 * 			If a read or a write throws an IOException.
	 */
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
}