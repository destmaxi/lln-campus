package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.Cours;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.Offre;
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

	private ProgressDialog mProgress;
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
			finish();
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

	private void startDownloadActivity() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String username = preferences.getString("username", null);
		String password = preferences.getString("password", null);
		
		if (username == null || password == null) {
			notify(getString(R.string.username_notify));
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return ;
			// Proposer de modifier les options ou de revenir en arrière
		}
		onFirstPage = false;
		
		Time t = new Time();
		t.setToNow();
		int anac = t.year;
		if (t.month < 9) {
			anac--;
		}
		
		runUpdateCourseList(username,password,anac);
		loadCoursList();

	}

	private void startAddCoursActivity() {
		onFirstPage = false;
		setContentView(R.layout.cours_add);
		findViewById(R.id.cours_add_button).setOnClickListener(this);
	}

	/**
	 * Lance le téléchargement de la liste des cours et la place dans la base de
	 * donnée.
	 * 
	 * @param username
	 *            Identifiant global UCL.
	 * @param password
	 *            Mot de passe global UCL.
	 * @param anac
	 *            Année académique.
	 * @author Damien
	 */
	private void runUpdateCourseList(final String username,
			final String password, final int anac) {

		mProgress = new ProgressDialog(this);
		mProgress.setCancelable(false);
		mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgress.setMax(100);
		mProgress.setMessage(getString(R.string.connection));

		mProgress.show();

		new Thread(new Runnable() {
			public void progress(final int n, final String nextStep) {
				mHandler.post(new Runnable() {
					public void run() {
						mProgress.setProgress(n);
						mProgress.setMessage(nextStep);
					}
				});
				Log.d("CoursListEditActivity", nextStep);
			}

			public void sendError(String msg) {
				notify("Erreur : " + msg);
				mProgress.cancel();
			}

			public void notify(final String msg) {
				mHandler.post(new Runnable() {
					public void run() {
						context.notify(msg);
					}
				});
			}

			public void run() {
				progress(0, getString(R.string.connection_UCL));
				UCLouvain uclouvain = new UCLouvain(username, password);

				progress(20,
						getString(R.string.fetch_info));
				ArrayList<Offre> offres = uclouvain.getOffres(anac);

				if (offres == null || offres.isEmpty()) {
					sendError(getString(R.string.error_anac)
							+ anac);
					return;
				}

				int i = 40;
				ArrayList<Cours> cours = new ArrayList<Cours>();
				for (Offre o : offres) {
					progress(i, getString(R.string.get_courses)
							+ o.getOffreName());
					ArrayList<Cours> c = uclouvain.getCourses(o);
					if (c != null && !c.isEmpty()) {
						cours.addAll(c);
					} else {
						Log.e("CoursListEditActivity",
								"Erreur : Aucun cours pour l'offre ["
										+ o.getOffreCode() + "] " + o.getOffreName());
					}
					i += (int) (30. / offres.size());
				}

				if (cours.isEmpty()) {
					sendError(getString(R.string.courses_empty));
					return;
				}

				// Suppression des anciens cours
				progress(70, getString(R.string.cleaning_db));
				LLNCampus.getDatabase().delete("Courses", "", null);
				LLNCampus.getDatabase().delete("Horaire", "", null);

				// Ajout des nouvelles donnees
				i = 80;
				for (Cours c : cours) {
					progress(i,
							getString(R.string.add_courses_db));
					ContentValues cv = new ContentValues();
					cv.put("CODE", c.getCoursCode());
					cv.put("NAME", c.getCoursName());

					LLNCampus.getDatabase().insert("Courses", cv);
					i += (int) (20. / cours.size());
				}

				progress(100, getString(R.string.end));
				mProgress.cancel();
				notify(getString(R.string.courses_download_ok));
				mHandler.post(new Runnable() {
					public void run() {
						context.loadCoursList();
					}
				});
			}
		}).start();
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
		case R.id.update_from_internet:
			EditText username = (EditText) findViewById(R.id.username);
			EditText password = (EditText) findViewById(R.id.password);
			EditText anac = (EditText) findViewById(R.id.anac);

			runUpdateCourseList(username.getText().toString(), password
					.getText().toString(), Integer.parseInt(anac.getText()
					.toString()));
			loadCoursList();
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
