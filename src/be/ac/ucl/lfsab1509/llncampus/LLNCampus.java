package be.ac.ucl.lfsab1509.llncampus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

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
 * Basis class of LLNCampus, provide some useful information for all other classes
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
	public static void setContext(Context c){
		APPLICATION_CONTEXT = c;
	}
	
	public final synchronized void close(){
		if (DB != null) {
			DB.close();
			DB = null;
		}
	}
	
	public static GPS getGPS() {
		if (gps == null) {
			gps = new GPS();
		}
		return gps;
	}
	
	public static void stopGPS() {
		if ( gps != null ) {
			gps.destroy();
			gps = null;
		}
	}
	
	public static void startAlarmService(){
		Intent intent = new Intent(APPLICATION_CONTEXT, AlarmService.class);
		PendingIntent pintent = PendingIntent.getService(APPLICATION_CONTEXT, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) APPLICATION_CONTEXT.getSystemService(Context.ALARM_SERVICE);
		// Toutes les minutes
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 60*1000, pintent); 
	}
	
	public static Database getDatabase() {
		if(DB == null) { 
			DB = new Database(APPLICATION_CONTEXT);
		}
		return DB;
	}
	
	public static Context getContext() {
		return APPLICATION_CONTEXT;
	}
	
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
	
    public static void convertInsecureToSecurePreferences(SharedPreferences mInsecurePrefs, 
    		SharedPreferences mSecurePrefs) {
    	Editor insecureEditor = mInsecurePrefs.edit();
    	Editor secureEditor = mSecurePrefs.edit();
    	
    	String key = "username";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = "password";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = "courses_notify";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putBoolean(key, mInsecurePrefs.getBoolean(key, false));
    		insecureEditor.remove(key);
    	}
    	key = "notify_minute";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = "notify_with_gps";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putBoolean(key, mInsecurePrefs.getBoolean(key, false));
    		insecureEditor.remove(key);
    	}
    	key = "notify_speed_move";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = "notify_max_distance";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	key = "notify_more_time";
    	if (mInsecurePrefs.contains(key)) {
    		secureEditor.putString(key, mInsecurePrefs.getString(key, null));
    		insecureEditor.remove(key);
    	}
    	
    	insecureEditor.commit();
    	secureEditor.commit();
    }
	
	/**
	 * Cree et copie les assets dans le dossier sdcard/LLNCampus
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
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
}