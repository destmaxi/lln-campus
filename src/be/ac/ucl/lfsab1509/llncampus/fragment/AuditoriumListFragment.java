package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.AuditoriumActivity;
import be.ac.ucl.lfsab1509.llncampus.activity.DetailsAuditorium;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AuditoriumListFragment extends LLNCampusListFragment {
	
	ArrayList<String> values = null;
	final String NAME = "NAME";
	private ArrayAdapter<String> adapter;
	IAuditorium auditorium;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	    String content = values.get(position);
	    
	    if(content !=null)
		{
			/* TODO passer le int et pas le nom */
			String nameAuditorium = content;
			Log.d("NAME", nameAuditorium);
			String[] cols = {"ID","NAME","LATITUDE", "LONGITUDE", "ADDRESS"};
			Cursor c = super.db.select("Poi", cols, "NAME = "+ "'"+nameAuditorium+"'", null, null, null, null, null);
			c.moveToFirst();
			auditorium = new Auditorium(c.getInt(0), c.getString(1), c.getDouble(2), c.getDouble(3), c.getString(4));
		}
	    
	    Intent showContent = new Intent(getActivity().getApplicationContext(),
	            DetailsAuditorium.class);
	    showContent.putExtra("NAME",auditorium.getName());
	    showContent.putExtra("ADDRESS", auditorium.getAddress());
	    double[] coord={ auditorium.getLatitude(), auditorium.getLongitude() };
	    showContent.putExtra("COORD", coord);
	    showContent.putExtra("ID", auditorium.getID());
	    startActivity(showContent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		String[] cols = {"NAME"};
		Cursor c = db.select("Poi", cols,"TYPE = 'auditoire'",null, null, null, "NAME ASC", null);
		
		this.values = new ArrayList<String>();
		while(c.moveToNext()){
			values.add(c.getString(0));
		}
		c.close();
		
		
		
		//ListView listView = (ListView) findViewById(android.R.id.list);
		

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		

		
		
        
        

		adapter=new ArrayAdapter<String>(
	            this.getActivity(),android.R.layout.simple_list_item_1, values){

	        @Override
	        public View getView(int position, View convertView,
	                ViewGroup parent) {
	            View view =super.getView(position, convertView, parent);

	            TextView textView=(TextView) view.findViewById(android.R.id.text1);

	            //YOUR CHOICE OF COLOR
	            textView.setTextColor(Color.WHITE);

	            return view;
	        }
	    };
	        //SET THE ADAPTER TO LISTVIEW
	        setListAdapter(adapter);
		
		// Assign adapter to ListView
		//listView.setAdapter(adapter); 
		//listView.setClickable(true);
/*		setListAdapter(new ArrayAdapter<String>(
	            this,R.layout.auditorium ,R.id.list_content, values){
			
			
		});*/
		

        setListAdapter(adapter);
        setHasOptionsMenu(true);
	}

}
