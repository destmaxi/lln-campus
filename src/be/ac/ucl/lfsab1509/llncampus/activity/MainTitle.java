package be.ac.ucl.lfsab1509.llncampus.activity;


import java.io.File;

import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Class that will be executed when starting the application.
 * Related with the XML main_title.xml
 *
 */
public class MainTitle extends LLNCampusActivity implements OnClickListener {

	// Defining the URL used in the code
	public final String ICAMPUS_URL="https://www.uclouvain.be/cnx_icampus.html";
	public final String MOODLE_URL="https://www.uclouvain.be/cnx_moodle.html";
	public final String BUREAU_UCL_URL="http://www.uclouvain.be/onglet_bureau.html?";
	public final String MAP_NAME="plan_lln.pdf";
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_title);
        setListeners(); 
        new Thread(new Runnable() {
        	public void run(){
        		LLNCampus.copyAssets();
        	}
        }).start();
		getActionBar().setDisplayHomeAsUpEnabled(false);
    }

 
    //  buttons defined in the XML file
    private void setListeners() {
        View myVisitsButton = findViewById(R.id.button_loisirs);
        myVisitsButton.setOnClickListener(this);
        View studyButton = findViewById(R.id.button_horaire);
        studyButton.setOnClickListener(this);
        View auditButton = findViewById(R.id.button_auditoire);
        auditButton.setOnClickListener(this);
        View libraryButton = findViewById(R.id.button_library);
        libraryButton.setOnClickListener(this);
        View planButton = findViewById(R.id.button_plan);
        planButton.setOnClickListener(this);
        View iCampusButton = findViewById(R.id.icampus);
        iCampusButton.setOnClickListener(this);
        View moodleButton = findViewById(R.id.moodle);
        moodleButton.setOnClickListener(this);
        View bureauButton = findViewById(R.id.bureau);
        bureauButton.setOnClickListener(this);
    }
    
    // defines the action for each button
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_loisirs:
			intent = new Intent(this, LoisirsActivity.class);
			startActivity(intent);
			break;
		case R.id.button_horaire:
			intent = new Intent(this, HoraireActivity.class);
			startActivity(intent);
			break;
		case R.id.button_auditoire:
			intent = new Intent(this, AuditoriumActivity.class);
			startActivity(intent);
			break;
		case R.id.button_library:
			intent = new Intent(this, LibraryActivity.class);
			startActivity(intent);
			break;	
		case R.id.button_plan:
			/*
			intent = new Intent(Intent.ACTION_VIEW);
			Uri uri = Uri.parse("file:///android_asset/plan_2007recto2.png");
			intent.setData(uri);
			*/
			
			// Uses the tablet's default PDF reader. Starts MapActivity if none is found.
			try
			{
			 Intent intentUrl = new Intent(Intent.ACTION_VIEW);
			 
			 
			 Uri url = Uri.fromFile(new File("/" + Environment.getExternalStorageDirectory().getPath() + "/" + LLNCampus.LLNREPOSITORY + "/" +MAP_NAME));
			 intentUrl.setDataAndType(url, "application/pdf");
			 intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 startActivity(intentUrl);
			}
			catch (ActivityNotFoundException e)
			{
			 Toast.makeText(this, "Pas de lecteur de PDF installe: tentative de connection par le net...", Toast.LENGTH_LONG).show();
			 intent = new Intent(this, MapActivity.class);
			 startActivity(intent);
			}
			
			break;	
		case R.id.icampus:
			ExternalAppUtility.openBrowser(MainTitle.this, ICAMPUS_URL);
			break;
		case R.id.moodle:
			ExternalAppUtility.openBrowser(MainTitle.this, MOODLE_URL);
			break;
		case R.id.bureau:
			ExternalAppUtility.openBrowser(MainTitle.this, BUREAU_UCL_URL);
			break;						
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() == android.R.id.home) {
			//Nothing to do...
			return true;
		}
		return super.onOptionsItemSelected(menuItem);
	}
}
