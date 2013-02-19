package be.ac.ucl.lfsab1509.llncampus;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainTitle extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_title);
        setListeners();
        /*
        Database db = new Database("database.sqlite");
        String result;
        if (db.open()){
        	result="Ok :)";
        }
        else{
        	result="Raté";
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_title, menu);
        return true;
    }
 
    // Permet d'avoir des boutons "qui font quelque chose"
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
