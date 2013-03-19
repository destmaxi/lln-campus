package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.Locale;

import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LoisirsDetailsFragment extends LLNCampusFragment{

	private View viewer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.loisirs_details, container, false);   
	    return viewer;
	}
	
	public void updateLoisir(String category){
		    String name = category;
		    TextView text = (TextView) viewer.findViewById(R.id.loisir_name);
		    text.setText(name);
	}
}
