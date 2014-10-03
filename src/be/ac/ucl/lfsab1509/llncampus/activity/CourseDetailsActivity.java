package be.ac.ucl.lfsab1509.llncampus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Class intended to show some details about a course activity and allows to
 * perform some actions with these details
 * Related with course_detail.xml
 */
public class CourseDetailsActivity extends LLNCampusActivity implements
		OnClickListener {
	
	// millis * sec * min * hours * days
	protected static long ONE_WEEK_MILLIS = 1000 * 60 * 60 * 24 * 7;
	protected static long ONE_HOUR_MILLIS = 1000 * 60 * 60 * 1;
	
	
	// Create an handler for the dialog box
	private final Handler handler = new Handler();
	
	
	
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
	
	/**
	 * Method that allow to suppress this of the DB and the others activities
	 * that have the same activity_name and same time of beginning and ending
	 * It can support up to 3 weeks with blanks (to support Easter Holidays)
	 * The DB delete the lines where the activity name and the time match
	 * (and the following weeks too)
	 * The Activity finish at the end of this method (if it success)
	 */
	protected void deleteConsecutiveEvents()
	{
		String activityName = getIntent().getStringExtra("ACTIVITY_NAME");
		long beginTime = getIntent().getLongExtra("BEGIN_TIME", -1);
		long endTime = getIntent().getLongExtra("END_TIME", -1);
		
		// Check if the data are coherent
		if (activityName == null || beginTime == -1 || endTime == -1)
			return;
		
		Database db = LLNCampus.getDatabase();
		String[] values = {activityName, Long.toString(beginTime), Long.toString(endTime)};
		int blankWeeks = 0;
		int error = 0;
		int otherHour = 0;
		
		while (blankWeeks < 5)
		{
			error = db.delete("Horaire", "ACTIVITY_NAME=? AND TIME_BEGIN=? AND TIME_END=?", values);
			if (error <= 0) // Delete no columns or error
			{
				// It can happen that we have the summer or winter change time!
				if (otherHour == 0)
				{
					values[1] = Long.toString(Long.parseLong(values[1]) - ONE_HOUR_MILLIS);
					values[2] = Long.toString(Long.parseLong(values[2]) - ONE_HOUR_MILLIS);
					error = db.delete("Horaire", "ACTIVITY_NAME=? AND TIME_BEGIN=? AND TIME_END=?", values);
					if (error <= 0)
					{
						// From one hour later to one hour in advance
						values[1] = Long.toString(Long.parseLong(values[1]) + 2 * ONE_HOUR_MILLIS);
						values[2] = Long.toString(Long.parseLong(values[2]) + 2 * ONE_HOUR_MILLIS);
						error = db.delete("Horaire", "ACTIVITY_NAME=? AND TIME_BEGIN=? AND TIME_END=?", values);
						if (error <= 0)
						{
							values[1] = Long.toString(Long.parseLong(values[1]) - ONE_HOUR_MILLIS);
							values[2] = Long.toString(Long.parseLong(values[2]) - ONE_HOUR_MILLIS);
							blankWeeks++;
						}
						else
						{
							blankWeeks = 0;
							otherHour = 1;
						}
					}
					else
					{
						blankWeeks = 0;
						otherHour = -1;
					}
				}
				else
				{
					if (otherHour == -1)
					{
						values[1] = Long.toString(Long.parseLong(values[1]) + ONE_HOUR_MILLIS);
						values[2] = Long.toString(Long.parseLong(values[2]) + ONE_HOUR_MILLIS);
					}
					else // otherHour == 1
					{
						values[1] = Long.toString(Long.parseLong(values[1]) - ONE_HOUR_MILLIS);
						values[2] = Long.toString(Long.parseLong(values[2]) - ONE_HOUR_MILLIS);
					}
					error = db.delete("Horaire", "ACTIVITY_NAME=? AND TIME_BEGIN=? AND TIME_END=?", values);
					if (error <= 0)
					{
						if (otherHour == 1)
						{
							values[1] = Long.toString(Long.parseLong(values[1]) + ONE_HOUR_MILLIS);
							values[2] = Long.toString(Long.parseLong(values[2]) + ONE_HOUR_MILLIS);
						}
						else // otherHour == -1
						{
							values[1] = Long.toString(Long.parseLong(values[1]) - ONE_HOUR_MILLIS);
							values[2] = Long.toString(Long.parseLong(values[2]) - ONE_HOUR_MILLIS);
						}
						blankWeeks++;
					}
					else
					{
						blankWeeks = 0;
						otherHour = 0;
					}
				}
			}
			else
			{
				blankWeeks = 0;
			}
			// For the next week...
			values[1] = Long.toString(Long.parseLong(values[1]) + ONE_WEEK_MILLIS);
			values[2] = Long.toString(Long.parseLong(values[2]) + ONE_WEEK_MILLIS);
		}
		finish();
	}

	@Override
	protected void editActionBar() {
		// Nothing to do because it's a dialog!
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_course_delete:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.delete_course_dialog_title)).setMessage(getString(R.string.delete_course_dialog_text));
            builder.setNeutralButton(getString(R.string.delete_course_one),
            		new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							handler.post(new Runnable(){
								public void run(){
									deleteOneEvent();
								}
							});
						}
					});
            builder.setPositiveButton(getString(R.string.delete_course_weekly),
            		new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							handler.post(new Runnable(){
								public void run(){
									deleteConsecutiveEvents();
								}
							});
						}
					});
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();
			break;
		
		case R.id.button_course_details_gps:
			
			ExternalAppUtility.startNavigation(
					getIntent().getDoubleExtra("LATITUDE", 0f), getIntent()
							.getDoubleExtra("LONGITUDE", 0f), this);
			break;
		}
	}
}
