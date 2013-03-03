package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.Locale;

import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AuditoriumDetailsFragment extends LLNCampusFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    Intent launchingIntent = getActivity().getIntent();
	    String name = launchingIntent.getStringExtra("NAME");
	    String address_t = launchingIntent.getStringExtra("ADDRESS");
	    RelativeLayout viewer = (RelativeLayout) inflater.inflate(R.layout.auditorium_details, container, false);
	    TextView text = (TextView) viewer.findViewById(R.id.auditorium_name);
	    
        TextView address=(TextView) viewer.findViewById(R.id.auditorium_address);
        text.setText(name);
		Log.d("ICI", Locale.getDefault().getDisplayLanguage());
		if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0){
			address.setText("Address: "+ address_t);
		}else{	
			address.setText("Adresse: "+ address_t);
		}	
	    
	    return viewer;
	}
}
