package be.ac.ucl.lfsab1509.llncampus.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
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
		Button delete = (Button) findViewById(R.id.button_course_delete);
		delete.setOnClickListener(this);
	}
	
	/**
	 * Method that allow to suppress this of the DB
	 * The DB delete the line where the activity name and the time match
	 * The Activity finish at the end of this method (if it success)
	 */
	protected void deleteOneEvent()
	{
		String activityName = getIntent().getStringExtra("ACTIVITY_NAME");
		long beginTime = getIntent().getLongExtra("BEGIN_TIME", -1);
		long endTime = getIntent().getLongExtra("END_TIME", -1);
		
		// Check if the data are coherent
		if (activityName == null || beginTime == -1 || endTime == -1)
			return;
		
		Database db = LLNCampus.getDatabase();
		String[] values = {activityName, Long.toString(beginTime), Long.toString(endTime)};
		int error = db.delete("Horaire", "ACTIVITY_NAME=? AND TIME_BEGIN=? AND TIME_END=?", values);
		if (error == -1)
			Log.e("DELETE_EVENT", "Error deleting" + values[0] + values[1] + values[2]);
		else
		{
			finish();
		}
	}

	@Override
	protected void editActionBar() {
		// Nothing to do because it's a dialog!
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_course_delete:
			deleteOneEvent();
			break;
		
		case R.id.button_course_details_gps:
			
			ExternalAppUtility.startGPS(
					getIntent().getDoubleExtra("LATITUDE", 0f), getIntent()
							.getDoubleExtra("LONGITUDE", 0f), this);
			break;
		}
	}
}
