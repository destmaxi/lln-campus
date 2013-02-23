package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.os.Bundle;

/**
 * Class intended for showing some information about an Auditorium
 * @author Quentin
 *
 */
public class DetailsAuditorium extends LLNCampusActivity{
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.auditorium_details);
	    }
}
