package be.ac.ucl.lfsab1509.llncampus.fragment;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CinemaFragment extends LLNCampusFragment{

	private View viewer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.cinema, container, false);   
	    return viewer;
	}
	
	public void update(){
		    TextView text = (TextView) viewer.findViewById(R.id.name);
		    text.setText("Cinema");
	}
}
