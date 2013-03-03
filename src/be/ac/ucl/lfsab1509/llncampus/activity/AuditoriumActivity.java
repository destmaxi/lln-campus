package be.ac.ucl.lfsab1509.llncampus.activity;



import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class is intended to create a list of auditoriums in order to make a clickable list for the user.
 * @author Quentin & Anh Tuan
 * @version 19/02/2013
 *
 */
public class AuditoriumActivity extends LLNCampusActivity{
	ArrayList<String> values = null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium_list_fragment);
		
				 
		
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
}
