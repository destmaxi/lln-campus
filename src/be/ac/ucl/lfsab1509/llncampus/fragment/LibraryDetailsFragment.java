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
		    text.setText(name + " (" + library.getSigle() + ")");
	        text.setTextSize(35);
		    
	        TextView address=(TextView) viewer.findViewById(R.id.library_address);
	        ImageView image = (ImageView) viewer.findViewById(R.id.library_picture);
	        image.setImageResource(library.takePicture());
	        
	        TextView scheduleView = (TextView) viewer.findViewById(R.id.library_schedule);
	        
	        String stringSchedule = this.getString(R.string.library_schedule);
	        scheduleView.setText(stringSchedule + "\n" + schedule);
	        
	        address.setText(this.getString(R.string.auditorium_address_selected) +" "+ address_t);
		    
	}

}
