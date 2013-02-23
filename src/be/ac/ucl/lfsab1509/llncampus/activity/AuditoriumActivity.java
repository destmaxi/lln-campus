package be.ac.ucl.lfsab1509.llncampus.activity;



import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class is intended to create a list of auditoriums in order to make a clickable list for the user.
 * @author Quentin
 * @version 19/02/2013
 *
 */
public class AuditoriumActivity extends ListActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium);
		
		/* FIXME : A reactiver une fois la db ok
		Database DB = LLNCampus.getDatabase();
		 
		String[] cols = {"NAME"};
		Cursor c = DB.select("poi", cols,"TYPE = 'auditoire'",null, null, null, null, null);
		
		ArrayList<String> values = new ArrayList<String>();
		while(c.moveToNext()){
			values.add(c.getString(0));
		}
		c.close();
		*/
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
		  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		  "Linux", "OS/2" };
		
		
		
		ListView listView = (ListView) findViewById(android.R.id.list);
		

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, values);


		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		listView.setClickable(true);
	}
	

    /*
     * Adding an item click listener to the list
     */
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, LoisirsTitle.class); //the intent is used to start a new activity
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
		startActivity(intent); //starts the activity denoted by this intent. 
    }
}
