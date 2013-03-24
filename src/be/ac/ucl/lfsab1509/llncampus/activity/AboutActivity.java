package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.os.Bundle;

/*
* This activity just shows an about message
 */
public class AboutActivity extends LLNCampusActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.about);
	}
	
	@Override  
    protected void editActionBar(){
    	//Nothing to do because it's a dialog!
    }
}


