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

public class AuditoriumDetailsFragment extends LLNCampusFragment {

	private View viewer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.auditorium_details, container, false);   
	    return viewer;
	}
	
	public void updateAuditorium(IAuditorium auditorium){
		    String name = auditorium.getName();
		    String address_t = auditorium.getAddress();
		    TextView text = (TextView) viewer.findViewById(R.id.auditorium_name);
		    
	        TextView address=(TextView) viewer.findViewById(R.id.auditorium_address);
	        text.setText(name);
	        
	        ImageView image = (ImageView) viewer.findViewById(R.id.auditorium_picture);
	        image.setImageResource(auditorium.takePicture());
	        
			Log.d("ICI", Locale.getDefault().getDisplayLanguage());
			if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0){
				address.setText("Address: "+ address_t);
			}else{	
				address.setText("Adresse: "+ address_t);
			}	
		    
	}
}
