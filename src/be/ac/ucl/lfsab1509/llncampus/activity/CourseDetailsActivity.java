package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
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
		if (getIntent().getFloatExtra("LATITUDE", -42f) == -42f) {
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
					getIntent().getFloatExtra("LATITUDE", 0f), getIntent()
							.getFloatExtra("LONGITUDE", 0f), this);
			break;
		}
	}
}
