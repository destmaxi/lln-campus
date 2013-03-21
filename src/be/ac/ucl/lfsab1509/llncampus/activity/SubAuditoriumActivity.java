package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;

public class SubAuditoriumActivity extends LLNCampusActivity {
	ArrayList<String> values = null;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium_list_fragment);
				 
		View vue = findViewById(R.id.auditorium_list_fragment);
		vue.setBackgroundColor(getResources().getColor(R.color.Blue)); 
	}
	

    /*
     * Adding an item click listener to the list
     */

	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, DetailsAuditorium.class); //the intent is used to start a new activity
    	/*
		 * before starting the new activity, we serialize the selected Poi from the list and add it as a parameter to the intent with the method 'putExtra'
		 * Note that non-serializable fields of the Poi (marked as 'transient') will not be included in the serialized object.
		 * This is the case for the data source of the Poi. Then, when de-serialized, the Poi should get another data source
		 */
    	// Here, give to the next activity some informations about what you did before (e.g. on which button you pressed)
    	/*
    	intent.putExtra(VISITABLE_ENTITY, cities.get(position));
    	intent.putExtra("Index", position);
    	*/
    	
    	intent.putExtra("NAME", values.get(position));
		startActivity(intent); //starts the activity denoted by this intent. 
    }
	
	
	public void onAuditoriumSelected(IAuditorium auditorium){
		
		
		AuditoriumDetailsFragment viewer = (AuditoriumDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.auditorium_details_fragment);
	    if (viewer == null || !viewer.isInLayout()) {
	    	Intent showContent = new Intent(getApplicationContext(),
					DetailsAuditorium.class);
			showContent.putExtra("NAME",auditorium.getName());
			showContent.putExtra("ADDRESS", auditorium.getAddress());
			double[] coord={ auditorium.getLatitude(), auditorium.getLongitude() };
			showContent.putExtra("COORD", coord);
			showContent.putExtra("ID", auditorium.getID());
			startActivity(showContent);
	    } else {
	        viewer.updateAuditorium(auditorium);
	       
	    }
	}
	
	/*
	 * Les deux methodes qui suivent ne seront utilisees quand dans le cas ou la tablette serait en
	 * paysage; gerer ce qu'aurait du faire DetailsAuditorium.
	 */
	
	    
	    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.button_auditorium_gps:
				
			    
				//ExternalAppUtility.openBrowser(DetailsAuditorium.this, "google.navigation:dirflg=w&q="+auditorium.getLatitude()+","+auditorium.getLongitude());
				break;
			case R.id.button_subauditorium:
				intent = new Intent(this, SubAuditoriumActivity.class);
				startActivity(intent);
				break;			
			}
		}
	 
}
