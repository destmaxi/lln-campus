package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;

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
import be.ac.ucl.lfsab1509.llncampus.Library;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ILibrary;

/**
 * This class is intended in order to control all the events that on Fragment of the Library List
 *
 */
public class LibraryListFragment extends LLNCampusListFragment {
	private ArrayList<String> librariesName = null;
	final String NAME = "NAME";
	private ArrayAdapter<String> adapter;
	private ILibrary library;
	private OnLibrarySelectedListener libSelectedListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		String[] cols = {"NAME"};
		Cursor c = db.select("Poi", cols,"TYPE = 'bibliotheque'",null, null, null, "NAME ASC", null);
		
		this.librariesName = new ArrayList<String>();
		while(c.moveToNext()){
			librariesName.add(c.getString(0));
		}
		c.close();
		

		adapter=new ArrayAdapter<String>(
	            this.getActivity(),android.R.layout.simple_list_item_1, librariesName){

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

        setListAdapter(adapter);
        setHasOptionsMenu(true);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	    String content = librariesName.get(position);
	    
	    if(content !=null) {
			String nameLibrary = content;
			Log.d("NAME", nameLibrary);
			String[] cols = {"ID","NAME","LATITUDE", "LONGITUDE", "ADDRESS"};
			Cursor c = super.db.select("Poi", cols, "NAME = "+ "'"+nameLibrary+"'", null, null, null, null, null);
			c.moveToFirst();
			library = new Library(c.getInt(0), c.getString(1), c.getDouble(2), c.getDouble(3), c.getString(4));
		}
	    libSelectedListener.onLibrarySelected(library);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            libSelectedListener = (OnLibrarySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTutSelectedListener");
        }
    }

	public interface OnLibrarySelectedListener {
	    public void onLibrarySelected(ILibrary lib);
	}

}
