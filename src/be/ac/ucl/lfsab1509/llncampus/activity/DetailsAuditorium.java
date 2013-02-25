package be.ac.ucl.lfsab1509.llncampus.activity;


import java.util.Locale;

import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class intended for showing some information about an Auditorium
 * @author Anh Tuan
 *
 */
public class DetailsAuditorium extends LLNCampusActivity implements OnClickListener{
	private TextView name=null;
	private TextView address=null;
	private Auditorium auditorium;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.auditorium_details);
	        
	        
	        /*name of Auditorium*/
	        this.name = (TextView) findViewById(R.id.auditorium_name);
	        this.address=(TextView) findViewById(R.id.auditorium_address);
			Bundle extras = getIntent().getExtras(); 
			if(extras !=null)
			{
				/* TODO passer le int et pas le nom */
				String nameAuditorium = extras.getString("NAME");
				Log.d("NAME", nameAuditorium);
				String[] cols = {"ID","NAME","LATITUDE", "LONGITUDE", "ADDRESS"};
				Cursor c = super.db.select("Poi", cols, "NAME = "+ "'"+nameAuditorium+"'", null, null, null, null, null);
				c.moveToFirst();
				auditorium = new Auditorium(c.getInt(0), c.getString(1), c.getDouble(2), c.getDouble(3), c.getString(4));
			}
			this.name.setText(auditorium.getName());
			Log.d("ICI", Locale.getDefault().getDisplayLanguage());
			if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0){
				this.address.setText("Address: "+ auditorium.getAddress());
			}else{	
				this.address.setText("Adresse: "+ auditorium.getAddress());
			}	
			//this.getString(R.id.auditorium_address, auditorium.getAddress());
			setListeners();
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
