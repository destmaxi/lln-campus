package be.ac.ucl.lfsab1509.llncampus.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import be.ac.ucl.lfsab1509.llncampus.R;

/**
 * Edit settings
 * @author Damien
 */
public class SettingsActivity extends PreferenceActivity {

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
        
        super.onCreate(savedInstanceState);



        
        
    }
    
    
    
}