package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_title);
        setListeners();
    }

 
    // Permet d'avoir des boutons "qui font quelque chose" (define the buttons in the xml file)
    private void setListeners() {
        View myVisitsButton = findViewById(R.id.button_loisirs);
        myVisitsButton.setOnClickListener(this);
        View studyButton = findViewById(R.id.button_etudes);
        studyButton.setOnClickListener(this);
    }
    
    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_loisirs:
			intent = new Intent(this, LoisirsTitle.class);
			startActivity(intent);
			break;
		case R.id.button_etudes:
			intent = new Intent(this, StudyTitle.class);
			startActivity(intent);
			break;			
		}
	}
}
