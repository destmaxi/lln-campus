package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.Map.Entry;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.external.SecurePreferences;


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
 * Activity to edit settings. Their values are encrypted.
 * Related with prefs.xml.
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements 
	OnSharedPreferenceChangeListener {
	/** Name of the username key */
	public final static String USERNAME = "username";
	/** Name of the password key*/
	public final static String PASSWORD = "password";
	/** Name of the courses notify key */
	public final static String COURSES_NOTIFY = "courses_notify";
	/** Name of the notify minute key */
	public final static String NOTIFY_MINUTE = "notify_minute";
	/** Name of the notify with gps key */
	public final static String NOTIFY_WITH_GPS = "notify_with_gps";
	/** Name of the notify speed move key */
	public final static String NOTIFY_SPEED_MOVE = "notify_speed_move";
	/** Name of the notify max distance key */
	public final static String NOTIFY_MAX_DISTANCE = "notify_max_distance";
	/** Name of the notify more time key */
	public final static String NOTIFY_MORE_TIME = "notify_more_time";
	
	
	private SharedPreferences mInsecurePrefs;
	private SharedPreferences mSecurePrefs;
	
	private EditTextPreference username;
	private EditTextPreference password;
	
	private CheckBoxPreference coursesNotify;
	private EditTextPreference notifyMinute;
	private CheckBoxPreference notifyWithGps;
	private EditTextPreference notifySpeedMove;
	private EditTextPreference notifyMaxDistance;
	private EditTextPreference notifyMoreTime;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        // Load the preferences from an XML resource.
        addPreferencesFromResource(R.xml.prefs);
        
        Preference pref = findPreference("username");
        EditTextPreference etp = (EditTextPreference) pref;
        pref.setSummary(etp.getText());
        
        mInsecurePrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSecurePrefs = new SecurePreferences(this);

        username = (EditTextPreference) findPreference(USERNAME);
        password = (EditTextPreference) findPreference(PASSWORD);
        
        coursesNotify = (CheckBoxPreference) findPreference(COURSES_NOTIFY);
        notifyMinute = (EditTextPreference) findPreference(NOTIFY_MINUTE);
        notifyWithGps = (CheckBoxPreference) findPreference(NOTIFY_WITH_GPS);
        notifySpeedMove = (EditTextPreference) findPreference(NOTIFY_SPEED_MOVE);
        notifyMaxDistance = (EditTextPreference) findPreference(NOTIFY_MAX_DISTANCE);
        notifyMoreTime = (EditTextPreference) findPreference(NOTIFY_MORE_TIME);
    }
    
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	// Decrypt relevant key/value pairs, if they exist.
    	for (Entry<String, ?> entry : mSecurePrefs.getAll().entrySet()) {
    		final String key = entry.getKey();
    		if (key == null) {
    			continue;
    		} else if (key.equals(USERNAME)) {
    			username.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals(PASSWORD)) {
    			password.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals(COURSES_NOTIFY)) {
    			coursesNotify.setChecked(mSecurePrefs.getBoolean(key, false));
    		} else if (key.equals(NOTIFY_MINUTE)) {
    			notifyMinute.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals(NOTIFY_WITH_GPS)) {
    			notifyWithGps.setChecked(mSecurePrefs.getBoolean(key, false));
    		} else if (key.equals(NOTIFY_SPEED_MOVE)) {
    			notifySpeedMove.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals(NOTIFY_MAX_DISTANCE)) {
    			notifyMaxDistance.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals(NOTIFY_MORE_TIME)) {
    			notifyMoreTime.setText(mSecurePrefs.getString(key, null));
    		}
    	}
    }
    
	@Override
    public void onStop() {
		// Replace unencrypted key/value pairs with encrypted ones.
    	LLNCampus.convertInsecureToSecurePreferences(mInsecurePrefs, mSecurePrefs);
    	super.onStop();
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (key.compareTo("password") == 0) // Don't show the password!!!
        	return;
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
        }
        if (pref instanceof RingtonePreference)
        {
        	RingtonePreference ring = (RingtonePreference) pref;
        	ring.setSummary(ring.getSharedPreferences().getString("notify_ringtone", "Silent"));
        }
    }
}