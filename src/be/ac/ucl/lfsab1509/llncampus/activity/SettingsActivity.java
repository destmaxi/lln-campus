package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
import android.util.Log;
import be.ac.ucl.lfsab1509.llncampus.R;

/**
 * Edit settings
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Make sure default values are applied.  In a real app, you would
        // want this in a shared function that is used to retrieve the
        // SharedPreferences wherever they are needed.
        /* PreferenceManager.setDefaultValues(getActivity(),
                R.xml.advanced_preferences, false);
	*/
        
     // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs);
        
        Preference pref = findPreference("username");
        EditTextPreference etp = (EditTextPreference) pref;
        pref.setSummary(etp.getText());
        
        //super.onCreate(savedInstanceState);

        
        
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