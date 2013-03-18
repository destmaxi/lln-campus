package be.ac.ucl.lfsab1509.llncampus.activity;



import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.LoisirListFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.LoisirsDetailsFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;


/**
 * This class is intended to create a list of auditoriums in order to make a clickable list for the user.
 * @author Quentin & Anh Tuan
 * @version 19/02/2013
 *
 */
public class LoisirsActivity extends LLNCampusActivity implements LoisirListFragment.OnCategorySelectedListener, OnClickListener {
	ArrayList<String> values = null;
	private String current_category;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loisirs_list_fragment);
				 
		View vue = findViewById(R.id.loisirs_list_fragment);
		vue.setBackgroundColor(getResources().getColor(R.color.Blue)); 
	}
	

    /*
     * Adding an item click listener to the list
     */

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
		
		if (current_category == "Cinema") {
			intent = new Intent(this, TodoActivity.class);
			intent.putExtra("NAME", current_category);
		}
		else {
			intent = new Intent(this, TodoActivity.class);
			intent.putExtra("Name", current_category);
		}
		startActivity(intent);
			
    }
	
	public void onCategorySelected(String cat){
		Intent intent;
		current_category = cat;
		
		if (current_category == "Sports") {
			intent = new Intent(this, TodoActivity.class);
			intent.putExtra("NAME", current_category);
			startActivity(intent);
		}
		else if (current_category == "Kaps") {
			intent = new Intent(this, TodoActivity.class);
			intent.putExtra("NAME", current_category);
			startActivity(intent);
		}
		else {
			LoisirsDetailsFragment viewer = (LoisirsDetailsFragment) getFragmentManager()
					.findFragmentById(R.id.loisirs_details_fragment);
			if (viewer == null || !viewer.isInLayout()) {
				intent = new Intent(getApplicationContext(), LoisirsDetails.class);
				intent.putExtra("NAME", current_category);
				startActivity(intent);
			} else {
				viewer.updateLoisir(current_category);
				setListeners();
			}
		}
	}
	
	/*
	 * Les deux methodes qui suivent ne seront utilisees quand dans le cas ou la tablette serait en
	 * paysage; gerer ce qu'aurait du faire DetailsAuditorium.
	 */
	
	 private void setListeners() {
	       /* View GPSButton = findViewById(R.id.button_auditorium_gps);
	        GPSButton.setOnClickListener(this);
	        View subButton = findViewById(R.id.button_subauditorium);
	        subButton.setOnClickListener(this); */
	    }
	    
	    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
		public void onClick(View v) {
			/*Intent intent;
			switch (v.getId()) {
			case R.id.button_auditorium_gps:
				intent = new Intent(android.content.Intent.ACTION_VIEW, 
							Uri.parse("http://maps.google.com/maps?daddr="+current_auditorium.getLatitude()+","+current_auditorium.getLongitude()+ "&dirflg=w"));
			    intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					            "com.google.android.maps.MapsActivity"));          
				startActivity(intent);
				//ExternalAppUtility.openBrowser(DetailsAuditorium.this, "google.navigation:dirflg=w&q="+auditorium.getLatitude()+","+auditorium.getLongitude());
				break;
			case R.id.button_subauditorium:
				intent = new Intent(this, SubAuditoriumActivity.class);
				startActivity(intent);
				break;			
			} */
		}
	 
}
