package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
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
				View webButton = findViewById(R.id.website);
			    webButton.setOnClickListener(this);
			
	    }
	    
	 
		public void onClick(View v) {
			final String URL = "http://www.cinescope.be/fr/louvain-la-neuve/accueil/";
			switch (v.getId()) {
				case R.id.website:
					ExternalAppUtility.openBrowser(LoisirsDetails.this, URL);
					break;
			}
		}
	 
	 
}
