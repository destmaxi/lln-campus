package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;

import android.os.Bundle;
import android.os.StrictMode;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Class that will be executed when starting the application.
 * Related with the XML main_title.xml
 * @author Quentin
 *
 */
public class MainTitle extends LLNCampusActivity implements OnClickListener{

	// Defining the URL used in the code
	public final String ICAMPUS_URL="https://www.uclouvain.be/cnx_icampus.html";
	public final String MOODLE_URL="https://www.uclouvain.be/cnx_moodle.html";
	public final String BUREAU_UCL_URL="http://www.uclouvain.be/onglet_bureau.html?";
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_title);
        setListeners();
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println(ADE.getInfos("LFSAB1509"));
    }

 
    // Permet d'avoir des boutons "qui font quelque chose" (define the buttons in the xml file)
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
    
    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_loisirs:
			intent = new Intent(this, LoisirsTitle.class);
			startActivity(intent);
			break;
		case R.id.button_horaire:
			intent = new Intent(this, LoisirsTitle.class);
			startActivity(intent);
			break;
		case R.id.button_auditoire:
			intent = new Intent(this, AuditoriumActivity.class);
			startActivity(intent);
			break;
		case R.id.button_library:
			intent = new Intent(this, LoisirsTitle.class);
			startActivity(intent);
			break;	
		case R.id.button_plan:
			intent = new Intent(this, LoisirsTitle.class);
			startActivity(intent);
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
}
