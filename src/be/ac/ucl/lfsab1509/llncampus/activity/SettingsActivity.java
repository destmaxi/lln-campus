package be.ac.ucl.lfsab1509.llncampus.activity;

import android.os.Bundle;
import be.ac.ucl.lfsab1509.llncampus.R;

/**
 * Edit settings
 * @author Damien
 */
public class SettingsActivity extends LLNCampusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        
    }
    
    @Override  
    protected void editActionBar(){
    	//Nothing to do because it's a dialog!
    }
    
}