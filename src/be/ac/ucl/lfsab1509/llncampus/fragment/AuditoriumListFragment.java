package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.Bibliotheque;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.AuditoriumListAdapter;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.BibliothequesListAdapter;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class AuditoriumListFragment extends LLNCampusListFragment {
	
	private ArrayList<String> auditoriumsName = null;
	final String NAME = "NAME";
	private ArrayAdapter<String> adapter;
	private IAuditorium auditorium;
	private OnAuditoriumSelectedListener audSelectedListener;
	
	private AuditoriumListAdapter auditoriumListAdapter;
	private ArrayList<IAuditorium> auditoriumList;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		if (auditoriumsName == null) {
			String[] cols = {"NAME"};
			Cursor c = db.select("Poi", cols,"TYPE = 'auditoire'",null, null, null, "NAME ASC", null);
			
			this.auditoriumsName = new ArrayList<String>();
			while(c.moveToNext()){
				auditoriumsName.add(c.getString(0));
			}
			
			this.auditoriumList = new ArrayList<IAuditorium>();
			for (int i=0; i < auditoriumsName.size(); i++)
			{
				String[] cols1 = {"ID","NAME","LATITUDE", "LONGITUDE", "ADDRESS"};
				Cursor c1 = super.db.select("Poi", cols1, "NAME = "+ "'"+auditoriumsName.get(i)+"'", null, null, null, null, null);
				c1.moveToFirst();
				auditoriumList.add(new Auditorium(c1.getInt(0), c1.getString(1), c1.getDouble(2), c1.getDouble(3), c1.getString(4)));
				Log.d("VALEUR I", i+"");
			}
			c.close();
		}
		Log.d("TEST", "coucou");
		auditoriumListAdapter = new AuditoriumListAdapter(getActivity(),
				auditoriumList);
		setListAdapter(auditoriumListAdapter);
		Log.d("TEST", "coucou2");
	    
        setHasOptionsMenu(true);
        Log.d("TEST", "coucou3");
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            audSelectedListener = (OnAuditoriumSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAuditoriumSelectedListener");
        }
    }

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	    auditorium = auditoriumList.get(position);
	    
	   /* 
	    if(content !=null)
		{
			/* TODO passer le int et pas le nom 
			
		}*/
	    audSelectedListener.onAuditoriumSelected(auditorium);
	}
	
	
	public interface OnAuditoriumSelectedListener {
	    public void onAuditoriumSelected(IAuditorium aud);
	}
	
}
