package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ILibrary;

public class LibraryDetailsFragment extends LLNCampusFragment {

	private View viewer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.library_details, container, false);  
	    return viewer;
	}
	
	public void updateLibrary(ILibrary library){
		    String name = library.getName();
		    String address_t = library.getAddress();
		    String schedule = library.getSchedule();
		    
		    TextView text = (TextView) viewer.findViewById(R.id.library_name);
		    
	        TextView address=(TextView) viewer.findViewById(R.id.library_address);
	        text.setText(name);
	        text.setTextSize(35);
	        ImageView image = (ImageView) viewer.findViewById(R.id.library_picture);
	        image.setImageResource(library.takePicture());
	        
	        TextView scheduleView = (TextView) viewer.findViewById(R.id.library_schedule);
	        
	        String stringSchedule = this.getString(R.string.library_schedule);
	        scheduleView.setText(stringSchedule + "\n" + schedule);
	        
	        
			Log.d("ICI", Locale.getDefault().getDisplayLanguage());
			if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0){
				address.setText("Address: "+ address_t);
			}else{	
				address.setText("Adresse: "+ address_t);
			}	
		    
	}

}
