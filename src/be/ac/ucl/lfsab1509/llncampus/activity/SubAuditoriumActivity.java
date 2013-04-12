package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.SubAuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.SubAuditoriumListFragment;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;

public class SubAuditoriumActivity extends LLNCampusActivity implements SubAuditoriumListFragment.OnSubAuditoriumSelectedListener {
	ArrayList<String> values = null;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subauditorium_list_fragment);
				 
		View vue = findViewById(R.id.subauditorium_list_fragment);
		//vue.setBackgroundColor(getResources().getColor(R.color.Blue)); 
	}
	

    /*
     * Adding an item click listener to the list
     */

	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, DetailsSubAuditorium.class); //the intent is used to start a new activity
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
	
	
	public void onSubAuditoriumSelected(ISubAuditorium subauditorium){
		
		SubAuditoriumDetailsFragment viewer = (SubAuditoriumDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.subauditorium_details_fragment);
	    if (viewer == null || !viewer.isInLayout()) {
	    	Intent showContent = new Intent(getApplicationContext(),
					DetailsSubAuditorium.class);
	    	showContent.putExtra("ID_PARENT", subauditorium.getIDParent());
	    	showContent.putExtra("ID", subauditorium.getID());
			showContent.putExtra("NAME",subauditorium.getName());
			showContent.putExtra("NBPLACES",  subauditorium.getNbPlaces());
			showContent.putExtra("MOBILIER", subauditorium.getMobilier());
			showContent.putExtra("CABINE", subauditorium.hasCabine());
			showContent.putExtra("ECRAN", subauditorium.hasEcran());
			showContent.putExtra("RETRO", subauditorium.hasRetro());
			showContent.putExtra("SONO", subauditorium.hasSono());
			showContent.putExtra("DIA", subauditorium.hasDia());
			showContent.putExtra("VIDEO", subauditorium.getVideo());
			showContent.putExtra("NETWORK", subauditorium.hasNetwork());
			showContent.putExtra("ACCESS", subauditorium.hasAccess());
			
			startActivity(showContent);
	    } else {
	        viewer.updateSubAuditorium(subauditorium);
	       
	    }
	}

	
}
