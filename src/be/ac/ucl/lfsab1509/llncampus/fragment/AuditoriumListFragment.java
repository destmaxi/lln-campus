package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
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

public class AuditoriumListFragment extends LLNCampusListFragment {
	
	ArrayList<String> values = null;
	final String NAME = "NAME";
	private ArrayAdapter<String> adapter;
	private IAuditorium auditorium;
	private OnAuditoriumSelectedListener audSelectedListener;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	    String content = values.get(position);
	    
	    if(content !=null)
		{
			String nameAuditorium = content;
			Log.d("NAME", nameAuditorium);
			String[] cols = {"ID","NAME","LATITUDE", "LONGITUDE", "ADDRESS"};
			Cursor c = super.db.select("Poi", cols, "NAME = "+ "'"+nameAuditorium+"'", null, null, null, null, null);
			c.moveToFirst();
			auditorium = new Auditorium(c.getInt(0), c.getString(1), c.getDouble(2), c.getDouble(3), c.getString(4));
		}
	    audSelectedListener.onAuditoriumSelected(auditorium);
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

        setListAdapter(adapter);
        setHasOptionsMenu(true);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            audSelectedListener = (OnAuditoriumSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTutSelectedListener");
        }
    }

	public interface OnAuditoriumSelectedListener {
	    public void onAuditoriumSelected(IAuditorium aud);
	}
	
}
