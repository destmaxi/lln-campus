package be.ac.ucl.lfsab1509.llncampus;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Class that defines the actions on the study title screen.
 * Related with the XML file study_title.xml.
 * @author Quentin
 *
 */
public class StudyTitle extends Activity implements OnClickListener{

	// Defining the URL used in the code
	public final String ICAMPUS_URL="https://www.uclouvain.be/cnx_icampus.html";
	public final String MOODLE_URL="https://www.uclouvain.be/cnx_moodle.html";
	public final String BUREAU_UCL_URL="http://www.uclouvain.be/onglet_bureau.html?";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_title);
        setListeners();
    }

 
    // Permet d'avoir des boutons "qui font quelque chose" (define the buttons in the XML file)
    private void setListeners() {
        View myVisitsButton = findViewById(R.id.button_horaire);
        myVisitsButton.setOnClickListener(this);
        View auditoiresButton = findViewById(R.id.auditoires);
        auditoiresButton.setOnClickListener(this);
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
		case R.id.button_horaire:
			intent = new Intent(this, LoisirsTitle.class); // A modifier
			startActivity(intent);
			break;
		case R.id.auditoires:
			intent = new Intent(this, LoisirsTitle.class); // A modifier
			startActivity(intent);
			break;
		case R.id.icampus:
			ExternalAppUtility.openBrowser(StudyTitle.this, ICAMPUS_URL);
			break;
		case R.id.moodle:
			ExternalAppUtility.openBrowser(StudyTitle.this, MOODLE_URL);
			break;
		case R.id.bureau:
			ExternalAppUtility.openBrowser(StudyTitle.this, BUREAU_UCL_URL);
			break;
		}
	}
}
