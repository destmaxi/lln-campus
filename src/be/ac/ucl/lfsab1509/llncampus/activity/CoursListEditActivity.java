package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.Cours;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.UCLouvain;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.CoursListAdapter;

/**
 * Activité pour la modification des cours.
 * 
 * @author Damien
 * 
 */
public class CoursListEditActivity extends LLNCampusActivity implements
		OnClickListener, OnItemClickListener {
	private static CoursListEditActivity context;

	private Handler mHandler = new Handler();
	
	private CoursListAdapter coursListAdapter;
	private ListView coursListView;
	private ArrayList<Cours> coursList;

	private boolean onFirstPage = true;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		if (getIntent().getBooleanExtra("startUCLouvain", false)) {
			startDownloadActivity();
		} else {
			loadCoursList();
		}
	}

	@Override
	public final void onPause() {
		super.onPause();
		if (!onFirstPage) {
			startActivity(new Intent(this, CoursListEditActivity.class));
		}
	}

	private void loadCoursList() {
		onFirstPage = true;
		setContentView(R.layout.cours_list_edit);
		findViewById(R.id.cours_download).setOnClickListener(this);
		findViewById(R.id.cours_add).setOnClickListener(this);

		coursList = Cours.getList();
		coursListView = (ListView) findViewById(R.id.cours_list);
		coursListAdapter = new CoursListAdapter(this, coursList);
		coursListView.setAdapter(coursListAdapter);
		coursListView.setOnItemClickListener(this);
	}

	public void startDownloadActivity() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String username = preferences.getString("username", null);
		String password = preferences.getString("password", null);

		if (username == null || password == null) {
			notify(getString(R.string.username_notify));
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return;
			// Proposer de modifier les options ou de revenir en arrière
		}
		onFirstPage = false;

	

		UCLouvain.downloadCoursesFromUCLouvain(this, username, password,
				new Runnable() {
					public void run() {
						CoursListEditActivity.this.notify(context.getString(R.string.courses_download_ok));
						loadCoursList();
					}
				}, mHandler);

	}

	private void startAddCoursActivity() {
		onFirstPage = false;
		setContentView(R.layout.cours_add);
		findViewById(R.id.cours_add_button).setOnClickListener(this);
	}

	@Override
	protected void editActionBar() {
		// Nothing to do !
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cours_download:
			startDownloadActivity();
			break;
		
		case R.id.cours_add:
			startAddCoursActivity();
			break;
		case R.id.cours_add_button:
			EditText code = (EditText) findViewById(R.id.cours_code);
			EditText name = (EditText) findViewById(R.id.cours_name);

			if (Cours.add(code.getText().toString(), name.getText().toString())) {
				super.notify(getString(R.string.add_course_ok));
			} else {
				super.notify(getString(R.string.add_course_error));
			}
			loadCoursList();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		final Cours c = coursList.get(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.delete_course)).setMessage(
				getString(R.string.confirm_delete_course) + c.getCoursCode()
						+ " ?");
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void notify(final String s) {
						mHandler.post(new Runnable() {
							public void run() {
								context.notify(s);
							}
						});
					}

					public void onClick(DialogInterface dialog, int id) {
						if (Cours.remove(c)) {
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
