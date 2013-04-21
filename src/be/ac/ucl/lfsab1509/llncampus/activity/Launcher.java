package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;


/**
 * Class that will be executed when starting the application.
 * Related with the XML main_title.xml
 * Useful for the name of the main screen
 *
 */
public class Launcher extends LLNCampusActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	// Compare the version number of the new version with this one; if different, then reload DB
    	try
    	{
    	    PackageManager manager = this.getPackageManager();
    	    PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
    	    int newCode = info.versionCode;
    	    String newName = info.versionName;
    	    
    	 // Compare with values on the server to see if there is a new version
    	    SharedPreferences prefs = this.getSharedPreferences(
    	    	      this.getPackageName(), Context.MODE_PRIVATE);
    	    
    	    String codeKey = this.getPackageName() + ".code";
    	    String nameKey = this.getPackageName() + ".name";

    	    // use a default value using new Date()
    	    int oldCode = prefs.getInt(codeKey, -1);
    	    String oldName = prefs.getString(nameKey, null);
    	    
    	    // First use, no update
    	    if (oldCode == -1 && oldName == null)
    	    {
    	    	// No need to reload the DB, just store the values
    	    	notify(this.getString(R.string.first_use));
    	    	
    	    	prefs.edit().putInt(codeKey, newCode).commit();
    	    	prefs.edit().putString(nameKey, newName).commit();
    	    }
    	    
    	    // If there is update, then reload the DB and store the values
    	    if (oldCode != newCode || oldName.compareTo(newName) != 0)
    	    {
    	    	// From LLNCampusActivity
    	    	notify(this.getString(R.string.update_app));
    	    	
    	    	db.close();
    	    	db.reset();
    	    	db.open();
    	    	
    	    	prefs.edit().putInt(codeKey, newCode).commit();
    	    	prefs.edit().putString(nameKey, newName).commit();
    	    }

    	    // If this version is equal to the other, then no need to reload the DB
    	}
    	catch(NameNotFoundException nnf)
    	{
    	    nnf.printStackTrace();
    	}
    	Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
    }
}