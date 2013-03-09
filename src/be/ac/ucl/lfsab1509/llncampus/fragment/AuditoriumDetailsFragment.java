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

public class AuditoriumDetailsFragment extends LLNCampusFragment{

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
	        image.setImageResource(this.takePicture(auditorium));
	        
			Log.d("ICI", Locale.getDefault().getDisplayLanguage());
			if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0){
				address.setText("Address: "+ address_t);
			}else{	
				address.setText("Adresse: "+ address_t);
			}	
		    
	}
	
	/**
	  * @return le chemin jusque l'image de l auditoire
	  * 
	  */
	private int takePicture(IAuditorium auditorium){
		  
		if(auditorium.getName().equals("Agora")) {return R.drawable.agora;}
		else if(auditorium.getName().equals("Coubertin")) {return R.drawable.coubertin;}
		else if(auditorium.getName().equals("Croix du Sud")) {return R.drawable.croixdusud;}
		else if(auditorium.getName().equals("Cyclotron")) {return R.drawable.cyclotron;}
		else if(auditorium.getName().equals("Descamps")) {return R.drawable.descamps;}
		else if(auditorium.getName().equals("Doyens")) {return R.drawable.doyens;}
		else if(auditorium.getName().equals("Dupriez")) {return R.drawable.dupriez;}
		else if(auditorium.getName().equals("Erasme")) {return R.drawable.erasme;}
		else if(auditorium.getName().equals("Lavoisier")) {return R.drawable.lavoisier;}
		else if(auditorium.getName().equals("Leclercq")) {return R.drawable.leclercq;}
		else if(auditorium.getName().equals("Marie Curie")) {return R.drawable.mariecurie;}
		else if(auditorium.getName().equals("Mercator")) {return R.drawable.mercator;}
		else if(auditorium.getName().equals("Montesquieu")) {return R.drawable.montesquieu;}
		else if(auditorium.getName().equals("Pierre Curie")) {return R.drawable.pierrecurie;}
		else if(auditorium.getName().equals("Sainte Barbe")) {return R.drawable.saintebarbe;}
		else if(auditorium.getName().equals("Sciences")) {return R.drawable.sciences;}
		else if(auditorium.getName().equals("Socrate")) {return R.drawable.socrate;}
		else if(auditorium.getName().equals("Studio Agora")) {return R.drawable.studioagora;}
		else if(auditorium.getName().equals("Thomas More")) {return R.drawable.thomasmore;}
		else if(auditorium.getName().equals("Van Helmont")) {return R.drawable.vanhelmont;}
		else {
			Log.e("DetailsAuditorium.java", "Ne trouve pas l'image vers l auditoire de Takepicture");
			return 0;
		}
		
	 }
	 
	
	
}
