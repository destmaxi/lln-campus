package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.LoisirsDetailsFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Class intended for showing some information about an Auditorium
 * @author Anh Tuan
 *
 */
public class LoisirsDetails extends LLNCampusActivity implements OnClickListener{
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.loisirs_details_fragment);
	        
	        String name = getIntent().getStringExtra("NAME");
	        
	        LoisirsDetailsFragment viewer = (LoisirsDetailsFragment) getFragmentManager().findFragmentById(R.id.loisirs_details_fragment);
	        viewer.updateLoisir(name);
	        
			setListeners();
	    }
	 
	 
	 private void setListeners() {
	      /*  View GPSButton = findViewById(R.id.button_auditorium_gps);
	        GPSButton.setOnClickListener(this);
	        View subButton = findViewById(R.id.button_subauditorium);
	        subButton.setOnClickListener(this); */
	    }
	    
	    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
		public void onClick(View v) {
			/* Intent intent;
			switch (v.getId()) {
			case R.id.button_auditorium_gps:
				intent = new Intent(android.content.Intent.ACTION_VIEW, 
							Uri.parse("http://maps.google.com/maps?daddr="+auditorium.getLatitude()+","+auditorium.getLongitude()+ "&dirflg=w"));
			    intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					            "com.google.android.maps.MapsActivity"));          
				startActivity(intent);
				//ExternalAppUtility.openBrowser(DetailsAuditorium.this, "google.navigation:dirflg=w&q="+auditorium.getLatitude()+","+auditorium.getLongitude());
				break;
			case R.id.button_subauditorium:
				intent = new Intent(this, SubAuditoriumActivity.class);
				startActivity(intent);
				break;			
			} */
		}
	 
	 
}
