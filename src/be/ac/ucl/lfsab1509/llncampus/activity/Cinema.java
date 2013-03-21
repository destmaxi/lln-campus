package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.CinemaFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class Cinema extends LLNCampusActivity implements OnClickListener{
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.cinema_fragment);
	        
	        
	     CinemaFragment viewer = (CinemaFragment) getFragmentManager().findFragmentById(R.id.cinema_fragment);
	     viewer.update();
	        
	     setListeners();
	}
	 
	private void setListeners() {
		
        View webButton = findViewById(R.id.website);
        webButton.setOnClickListener(this);
		 
	      /*  View GPSButton = findViewById(R.id.button_auditorium_gps);
	        GPSButton.setOnClickListener(this);
	        View subButton = findViewById(R.id.button_subauditorium);
	        subButton.setOnClickListener(this); */
	}
	    
	    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.website:
				ExternalAppUtility.openBrowser(Cinema.this, "http://www.cinescope.be/fr/louvain-la-neuve/accueil/");
				break;
		
			}
		}
	 
	 
}
