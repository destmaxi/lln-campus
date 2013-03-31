package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.Intent;
import android.os.Bundle;


/**
 * Class that will be executed when starting the application.
 * Related with the XML main_title.xml
 *
 */
public class Launcher extends LLNCampusActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Intent intent = new Intent(this, MainTitle.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
    }
}