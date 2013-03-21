package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.SubAuditorium;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubAuditoriumListFragment extends LLNCampusListFragment {
	
	ArrayList<String> subAuditoriumsName = null;
	final String NAME = "NAME";
	private ArrayAdapter<String> adapter;
	private ISubAuditorium subauditorium;
	private OnSubAuditoriumSelectedListener subAudSelectedListener;
	private int idParent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		idParent = getActivity().getIntent().getIntExtra("IDPARENT", 0);
		
		
		String[] cols = {"AUDITORIUM_NAME"};
		Cursor c = db.select("Auditorium", cols,"BUILDING_ID = "+idParent,null, null, null, "AUDITORIUM_NAME ASC", null);
		
		this.subAuditoriumsName = new ArrayList<String>();
		while(c.moveToNext()){
			subAuditoriumsName.add(c.getString(0));
		}
		c.close();
		
		
		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

		adapter=new ArrayAdapter<String>(
	            this.getActivity(),android.R.layout.simple_list_item_1, subAuditoriumsName){

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
		
		// Assign adapter to ListView
		//listView.setAdapter(adapter); 
		//listView.setClickable(true);
/*		setListAdapter(new ArrayAdapter<String>(
	            this,R.layout.auditorium ,R.id.list_content, values){
			
			
		});*/
		
        setListAdapter(adapter);
        setHasOptionsMenu(true);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            subAudSelectedListener = (OnSubAuditoriumSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubAuditoriumSelectedListener");
        }
    }

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	    String content = subAuditoriumsName.get(position);
	    
	    if(content !=null)
		{
			/* TODO passer le int et pas le nom */
			String nameSubAuditorium = content;
			Log.d("NAME", nameSubAuditorium);
			String[] cols = {"AUDITORIUM_ID","AUDITORIUM_NAME","BUILDING_ID", "PLACE", "MOBILIER", "CABINE", "ECRAN", "SONO", "RETRO", "DIA", "VIDEO", "RESEAU", "ACCESS"};
			Cursor c = super.db.select("Auditorium", cols, "AUDITORIUM_NAME = "+ "'"+nameSubAuditorium+"'", null, null, null, null, null);
			c.moveToFirst();
			subauditorium = new SubAuditorium(idParent , c.getInt(0), c.getString(1), c.getInt(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10), c.getString(11), c.getString(12));
		}
	    subAudSelectedListener.onSubAuditoriumSelected(subauditorium);
	}
	
	
	public interface OnSubAuditoriumSelectedListener {
	    public void onSubAuditoriumSelected(ISubAuditorium subAud);
	}
	
}
