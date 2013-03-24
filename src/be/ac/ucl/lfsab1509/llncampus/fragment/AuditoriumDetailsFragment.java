package be.ac.ucl.lfsab1509.llncampus.fragment;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.os.Bundle;
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
		    text.setText(name);
		    
	        TextView address=(TextView) viewer.findViewById(R.id.auditorium_address);
	        
	        
	        ImageView image = (ImageView) viewer.findViewById(R.id.auditorium_picture);
	        image.setImageResource(auditorium.takePicture());
	        
	        address.setText(this.getString(R.string.auditorium_address_selected) +" "+ address_t);
	}
}
