package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
		onFirstPage = false;
		setContentView(R.layout.download_from_uclouvain);
		findViewById(R.id.update_from_internet).setOnClickListener(this);
		EditText anac = (EditText) findViewById(R.id.anac);
		Time t = new Time();
		t.setToNow();
		int y = t.year;
		if (t.month < 9) {
			y--;
		}
		anac.setText(String.valueOf(y));

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
		mProgress.setMessage("Connexion...");

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
				progress(0, "Connexion à UCLouvain.be");
				UCLouvain uclouvain = new UCLouvain(username, password);

				progress(20,
						"Récupération des informations sur les études suivies");
				ArrayList<Offre> offres = uclouvain.getOffres(anac);

				if (offres == null || offres.isEmpty()) {
					sendError("Impossible de récupérer vos études pour l'année académique "
							+ anac);
					return;
				}

				int i = 40;
				ArrayList<Cours> cours = new ArrayList<Cours>();
				for (Offre o : offres) {
					progress(i, "Récupération de la liste des cours pour "
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
					sendError("Aucun cours n'a été trouvé.");
					return;
				}

				// Suppression des anciens cours
				progress(70, "Nettoyage de la base de donnée  ");
				LLNCampus.getDatabase().delete("Courses", "", null);
				LLNCampus.getDatabase().delete("Horaire", "", null);

				// Ajout des nouvelles donnees
				i = 80;
				for (Cours c : cours) {
					progress(i,
							"Ajout des nouveaux cours dans la base de donnée");
					ContentValues cv = new ContentValues();
					cv.put("CODE", c.getCoursCode());
					cv.put("NAME", c.getCoursName());

					LLNCampus.getDatabase().insert("Courses", cv);
					i += (int) (20. / cours.size());
				}

				progress(100, "Fin");
				mProgress.cancel();
				notify("Liste des cours correctement téléchargée.");
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
				super.notify("Cours correctement ajouté");
			} else {
				super.notify("Echec de l'ajout du cours");
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
		builder.setTitle("Suppression d'un cours").setMessage(
				"Etes-vous sûr de vouloir supprimer le cours " + c.getCoursCode()
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
							notify("Cours supprimé!");
							mHandler.post(new Runnable() {
								public void run() {
									context.loadCoursList();
								}
							});
						} else {
							notify("Echec de la suppression :/");
						}

					}
				});
		builder.setNegativeButton(android.R.string.cancel, null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
