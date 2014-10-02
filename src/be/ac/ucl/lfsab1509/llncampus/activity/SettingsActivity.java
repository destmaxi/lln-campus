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
 * Edit settings
 * Related with prefs.xml
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

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
       
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs);
        
        Preference pref = findPreference("username");
        EditTextPreference etp = (EditTextPreference) pref;
        pref.setSummary(etp.getText());
        
        mInsecurePrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSecurePrefs = new SecurePreferences(this);

        username = (EditTextPreference) findPreference(getString(R.string.pref_username));
        password = (EditTextPreference) findPreference(getString(R.string.pref_password));
        
        coursesNotify = (CheckBoxPreference) findPreference(getString(R.string.pref_courses_notify));
        notifyMinute = (EditTextPreference) findPreference(getString(R.string.pref_notify_minute));
        notifyWithGps = (CheckBoxPreference) findPreference(getString(R.string.pref_notify_with_gps));
        notifySpeedMove = (EditTextPreference) findPreference(getString(R.string.pref_notify_speed_move));
        notifyMaxDistance = (EditTextPreference) findPreference(getString(R.string.pref_notify_max_distance));
        notifyMoreTime = (EditTextPreference) findPreference(getString(R.string.pref_notify_more_time));
        
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
    	// Decrypt relevant key/value pairs, if they exist
    	for (Entry<String, ?> entry : mSecurePrefs.getAll().entrySet()) {
    		final String key = entry.getKey();
    		if (key == null) {
    			continue;
    		} else if (key.equals("username")) {
    			username.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals("password")) {
    			password.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals("courses_notify")) {
    			coursesNotify.setChecked(mSecurePrefs.getBoolean(key, false));
    		} else if (key.equals("notify_minute")) {
    			notifyMinute.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals("notify_with_gps")) {
    			notifyWithGps.setChecked(mSecurePrefs.getBoolean(key, false));
    		} else if (key.equals("notify_speed_move")) {
    			notifySpeedMove.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals("notify_max_distance")) {
    			notifyMaxDistance.setText(mSecurePrefs.getString(key, null));
    		} else if (key.equals("notify_more_time")) {
    			notifyMoreTime.setText(mSecurePrefs.getString(key, null));
    		}
    	}
    }
    
	@Override
    public void onStop() {
    	super.onStop();
    	// Replace unencrypted key/value pairs with encrypted ones
    	LLNCampus.convertInsecureToSecurePreferences(mInsecurePrefs, mSecurePrefs);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
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