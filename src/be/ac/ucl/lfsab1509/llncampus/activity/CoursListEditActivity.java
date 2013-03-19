package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Cours;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.Offre;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.UCLouvain;

/**
 * Activité pour la modification des cours.
 * @author Damien
 *
 */
public class CoursListEditActivity extends LLNCampusActivity implements OnClickListener {
	private static CoursListEditActivity context;
	
	private ProgressDialog mProgress;
    private Handler mHandler = new Handler();


		
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.course_list_edit);      
		findViewById(R.id.update_from_internet).setOnClickListener(this);
		
		EditText anac = (EditText) findViewById(R.id.anac);
		Time t = new Time();
		t.setToNow();
		int y = t.year;
		if (t.month < 9) { y--; }
		anac.setText(String.valueOf(y));
	}
	
	/**
	 * Lance le téléchargement de la liste des cours et la place dans la base de donnée.
	 * @param username Identifiant global UCL.
	 * @param password Mot de passe global UCL.
	 * @param anac Année académique.
	 * @author Damien
	 */
	private void runUpdateCourseList(final String username, final String password, 
			final int anac) {
		
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
        		
        		progress(20, "Récupération des informations sur les études suivies");
        		ArrayList<Offre> offres = uclouvain.getOffres(anac);
        		
        		if (offres == null || offres.isEmpty()) {
        			sendError("Impossible de récupérer vos études pour l'année académique " + anac);
        			return;
        		}
        		
        		int i = 40;
        		ArrayList<Cours> cours = new ArrayList<Cours>();
        		for (Offre o : offres) {
            		progress(i, "Récupération de la liste des cours pour " + o.offreName);
            		ArrayList<Cours> c = uclouvain.getCourses(o);
            		if (c != null && !c.isEmpty()){
            			cours.addAll(c);
            		} else {
            			Log.e("CoursListEditActivity", "Erreur : Aucun cours pour l'offre [" + o.offreCode + "] " + o.offreName);
            		}
        			i += (int) (30. / offres.size());
        		}
        		
        		if (cours.isEmpty()) {
        			sendError("Aucun cours n'a été trouvé.");
        			return;
        		}
        		
       			//Suppression des anciens cours
        		progress(70, "Nettoyage de la base de donnée  ");
       			LLNCampus.getDatabase().delete("Courses", "",null);
       			LLNCampus.getDatabase().delete("Horaire", "",null);
        		
       			// Ajout des nouvelles donnees
        		i = 80;
        		for (Cours c : cours) {
            		progress(i, "Ajout des nouveaux cours dans la base de donnée");
        			ContentValues cv = new ContentValues();
        			cv.put("CODE", c.coursCode);
        			cv.put("NAME", c.coursName);
        				
        			LLNCampus.getDatabase().insert("Courses", cv);
        			i += (int) (20. / cours.size());
        		}
        		
        		/*String weeks = ADE.getWeeks();
				
				ArrayList<Event> events;
				i = 80;
				for (Cours c : cours) {
            		progress(i, "Récupération de l'horaire pour " + c.coursCode + " : " + c.coursName);
					events = ADE.getInfos(c.coursCode, weeks);
					if (events == null) {
						Log.e("CoursListEditActivity", "Le contenu de " + c.coursCode + " n'a pu etre telecharge");
					} else {
						// Ajout des nouvelles donnees
						for (Event e : events) {
							ContentValues cv = e.toContentValues();
							cv.put("COURSE", c.coursCode);
							LLNCampus.getDatabase().insert("Horaire", cv);
						}
					}
					i += (int) (20. / cours.size());
				}*/
        		
        		progress(100, "Fin");
        		mProgress.cancel();
        		notify("Liste des cours correctement téléchargée.");
            }
        }).start();
	}
	
	
	@Override
	protected void editActionBar() {
		//Nothing to do !
	}
	
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_from_internet:
			EditText username = (EditText) findViewById(R.id.username);
			EditText password = (EditText) findViewById(R.id.password);
			EditText anac = (EditText) findViewById(R.id.anac);
			
			username.getText().toString();
			
			runUpdateCourseList(username.getText().toString(), 
					password.getText().toString(), 
					Integer.parseInt(anac.getText().toString()));
			break;
		}
	}
}
