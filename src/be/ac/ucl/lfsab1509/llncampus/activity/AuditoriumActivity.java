package be.ac.ucl.lfsab1509.llncampus.activity;


import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.app.ListActivity;
import android.os.Bundle;
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
		
		/*
		LLNCampus llnCampus = new LLNCampus();
		Database db=null;
		if (llnCampus !=null){
			db = llnCampus.getDatabase();
		}
		else
		{
			Toast.makeText(this, "Bordel", Toast.LENGTH_LONG).show();
		}
		
		if (db != null)
		{
			Toast.makeText(this, "Youpie! =D", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this, "Fuck!", Toast.LENGTH_LONG).show();
		}
		*/
		ListView listView = (ListView) findViewById(android.R.id.list);
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
		  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		  "Linux", "OS/2" };

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, values);


		// Assign adapter to ListView
		listView.setAdapter(adapter); 
	}
}
