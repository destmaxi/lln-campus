package be.ac.ucl.lfsab1509.llncampus.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;

public class CourseDetailsActivity extends LLNCampusActivity implements
		OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_details);
		TextView details = (TextView) findViewById(R.id.course_details);
		details.setText(getIntent().getStringExtra("DETAILS"));
		Button gps = (Button) findViewById(R.id.button_course_details_gps);
		gps.setOnClickListener(this);
		if (!getIntent().getBooleanExtra("COORDINATES", false)) {
			gps.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void editActionBar() {
		// Nothing to do because it's a dialog!
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_course_details_gps:
			
			ExternalAppUtility.startGPS(
					getIntent().getDoubleExtra("LATITUDE", 0f), getIntent()
							.getDoubleExtra("LONGITUDE", 0f), this);
			break;
		}
	}
}
