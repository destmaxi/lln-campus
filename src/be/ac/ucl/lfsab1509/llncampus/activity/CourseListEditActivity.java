package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.Course;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.UCLouvain;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.CourseListAdapter;
import be.ac.ucl.lfsab1509.llncampus.external.SecurePreferences;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
    Copyright (C) 2014 Quentin De Coninck

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
 */

/**
 * Activity to show and modify courses.
 * Related with course_list_edit.xml
 * 
 */
public class CourseListEditActivity extends LLNCampusActivity implements
		OnClickListener, OnItemClickListener {
	/** The current class, to keep access for threads. */
	private static CourseListEditActivity context;

	private Handler mHandler = new Handler();
	
	private CourseListAdapter courseListAdapter;
	private ListView courseListView;
	private ArrayList<Course> courseList;

	/** This indicates if the user is on the courses list view. */
	private boolean onFirstPage = true;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		if (getIntent().getBooleanExtra(EXTRA_START_UCLOUVAIN, false)) {
			downloadCoursesFromUCLouvain();
		} else {
			loadCoursList();
		}
	}

	@Override
	public final void onPause() {
		super.onPause();
		// If adding a new course or after downloading courses from UCLouvain.
		if (!onFirstPage) {
			// TODO must check if a call of this.onResume() is better
			startActivity(new Intent(this, CourseListEditActivity.class));
		}
	}

	/**
	 * Prepare the view for the user to show the list of courses.
	 */
	private void loadCoursList() {
		onFirstPage = true;
		setContentView(R.layout.course_list_edit);
		findViewById(R.id.course_download).setOnClickListener(this);
		findViewById(R.id.course_add).setOnClickListener(this);

		courseList = Course.getList();
		courseListView = (ListView) findViewById(R.id.course_list);
		courseListAdapter = new CourseListAdapter(this, courseList);
		courseListView.setAdapter(courseListAdapter);
		courseListView.setOnItemClickListener(this);
	}

	/**
	 * Start the downloading of courses from UCLouvain and then show the list of courses 
	 * downloaded.
	 */
	public void downloadCoursesFromUCLouvain() {
		SharedPreferences preferences = new SecurePreferences(this);
		String username = preferences.getString(SettingsActivity.USERNAME, null);
		String password = preferences.getString(SettingsActivity.PASSWORD, null);
		onFirstPage = false;

		if (username == null || password == null) {
			notify(getString(R.string.username_notify));
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return;
		}

		UCLouvain.downloadCoursesFromUCLouvain(this, username, password,
				new Runnable() {
					public void run() {
						CourseListEditActivity.this.notify(context
								.getString(R.string.courses_download_ok));
						loadCoursList();
					}
				}, mHandler);

	}

	/** 
	 * Change the content of the view to add manually new courses.
	 */
	private void changeViewToAddCourses() {
		onFirstPage = false;
		setContentView(R.layout.course_add);
		findViewById(R.id.course_add_button).setOnClickListener(this);
	}

	@Override
	protected void enableActionBar() {
		// Nothing to do !
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.course_download:
			downloadCoursesFromUCLouvain();
			break;
		case R.id.course_add:
			changeViewToAddCourses();
			break;
		case R.id.course_add_button:
			EditText code = (EditText) findViewById(R.id.course_code_edit);
			EditText name = (EditText) findViewById(R.id.course_name_edit);

			if (Course.add(code.getText().toString(), name.getText().toString())) {
				super.notify(getString(R.string.add_course_ok));
			} else {
				super.notify(getString(R.string.add_course_error));
			}
			loadCoursList();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		final Course course = courseList.get(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.delete_course)).setMessage(
				getString(R.string.confirm_delete_course) + course.getCourseCode()
						+ " ?");
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					/**
					 * Show a notification message to the user.
					 * @param msg
					 * 		The message to show.
					 */
					public void notify(final String msg) {
						mHandler.post(new Runnable() {
							public void run() {
								context.notify(msg);
							}
						});
					}

					public void onClick(DialogInterface dialog, int id) {
						if (Course.remove(course)) {
							notify(getString(R.string.delete_course_ok));
							mHandler.post(new Runnable() {
								public void run() {
									context.loadCoursList();
								}
							});
						} else {
							notify(getString(R.string.delete_course_error));
						}

					}
				});
		builder.setNegativeButton(android.R.string.cancel, null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
