package be.ac.ucl.lfsab1509.llncampus.activity;


import java.util.Locale;

import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumDetailsFragment;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * Class intended for showing some information about an Auditorium
 * @author Anh Tuan
 *
 */
public class DetailsAuditorium extends LLNCampusActivity implements OnClickListener{
	private Auditorium auditorium;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.auditorium_details_fragment);
	        
	        String name = getIntent().getStringExtra("NAME");
	        String address = getIntent().getStringExtra("ADDRESS");
	        double []coord = getIntent().getDoubleArrayExtra("COORD");
	        int id = getIntent().getIntExtra("ID", 0);
	        auditorium = new Auditorium (id, name, coord[0], coord[1], address);
	        ImageView image = (ImageView) findViewById(R.id.auditorium_picture);
	        image.setImageResource(this.takePicture());
	        
	        AuditoriumDetailsFragment viewer = (AuditoriumDetailsFragment) getFragmentManager().findFragmentById(R.id.auditorium_details_fragment);
	        viewer.updateAuditorium(auditorium);
	        
			setListeners();
	    }
	 
	 /**
	  * @return le chemin jusque l'image de l auditoire
	  * 
	  */
	private int takePicture(){
		  
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
		else if(auditorium.getName().equals("Montesqieu")) {return R.drawable.montesquieu;}
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
	 
	 
	 private void setListeners() {
	        View GPSButton = findViewById(R.id.button_auditorium_gps);
	        GPSButton.setOnClickListener(this);
	        View subButton = findViewById(R.id.button_subauditorium);
	        subButton.setOnClickListener(this);
	    }
	    
	    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.button_auditorium_gps:
				intent = new Intent(android.content.Intent.ACTION_VIEW, 
							Uri.parse("http://maps.google.com/maps?daddr="+auditorium.getLatitude()+","+auditorium.getLongitude()+ "&dirflg=w"));
			    intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					            "com.google.android.maps.MapsActivity"));          
				startActivity(intent);
				//ExternalAppUtility.openBrowser(DetailsAuditorium.this, "google.navigation:dirflg=w&q="+auditorium.getLatitude()+","+auditorium.getLongitude());
				break;
			case R.id.button_subauditorium:
				intent = new Intent(this, SubAuditoriumActivity.class);
				startActivity(intent);
				break;			
			}
		}
	 
	 
}
