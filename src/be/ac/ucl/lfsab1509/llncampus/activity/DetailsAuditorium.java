package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumDetailsFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * This class is intended to shows information about an IAuditorium
 * Related with the xml file auditorium_details.xml
 * Caution: this class is called only if the device is NOT in landscape!
 * 			Check in the xml file (landscape and normal) for details.
 *
 */
public class DetailsAuditorium extends LLNCampusActivity implements OnClickListener {
	private Auditorium auditorium;

	// Create an local IAuditorium useful for the buttons
	// (fetch information given by AuditoriumListActivity)
	// Also manage the update query to the fragment
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium_details_fragment);

		String name = getIntent().getStringExtra("NAME");
		String address = getIntent().getStringExtra("ADDRESS");
		double []coord = getIntent().getDoubleArrayExtra("COORD");
		int id = getIntent().getIntExtra("ID", 0);
		auditorium = new Auditorium (id, name, coord[0], coord[1], address);

		AuditoriumDetailsFragment viewer = (AuditoriumDetailsFragment) getFragmentManager().findFragmentById(R.id.auditorium_details_fragment);
		viewer.updateAuditorium(auditorium);

		setListeners();
	}

	// Set the listeners
	private void setListeners() {
		View GPSButton = findViewById(R.id.button_auditorium_gps);
		GPSButton.setOnClickListener(this);
		View subButton = findViewById(R.id.button_subauditorium);
		subButton.setOnClickListener(this);
	}

	// Permet de définir l'action effectuée grâce Ã  l'appui sur un bouton
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_auditorium_gps:
			intent = new Intent(android.content.Intent.ACTION_VIEW, 
					Uri.parse("http://maps.google.com/maps?daddr="+auditorium.getLatitude()+","+auditorium.getLongitude()+ "&dirflg=w"));
			intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					"com.google.android.maps.MapsActivity"));          
			startActivity(intent);
			break;
		case R.id.button_subauditorium:
			intent = new Intent(this, SubAuditoriumActivity.class);
			intent.putExtra("IDPARENT", auditorium.getID());
			startActivity(intent);
			break;			
		}
	}


}
