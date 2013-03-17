package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;
import java.util.Date;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Cours;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.UCLouvain;

public class CoursListEditActivity extends LLNCampusActivity implements OnClickListener {
	private static CoursListEditActivity context;
	private static final int NOTIFY_ID = 2;
	//create an handler
		private final Handler handler = new Handler();
		final Runnable updateRunnable = new Runnable() {
			public void run() {
				//call the activity method that updates the UI
				context.notify("FIN !");
			}
		};

		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.course_list_edit);      
		findViewById(R.id.update_from_internet).setOnClickListener(this);
		
		EditText anac = (EditText) findViewById(R.id.anac);
		Time t = new Time();
		t.setToNow();
		int y = t.year;
		if (t.month < 9){ y--; }
		anac.setText(String.valueOf(y));
	}
	
	/**
	 * Lance le téléchargement de la liste des cours et la place dans la base de donnée
	 * @param username Identifiant global UCL
	 * @param password Mot de passe global UCL
	 * @param anac Année académique
	 * @author Damien
	 */
	public void runUpdateCourseList(final String username, final String password, 
			final int anac) {
		final NotificationManager nm = 
				(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		final Builder nb = new NotificationCompat.Builder(this)
			.setContentTitle("Mise a jour de la liste des cours")
			.setContentText("Mise a jour en cours")
			.setSmallIcon(android.R.drawable.stat_notify_sync);		
		nm.notify(NOTIFY_ID, nb.build());
		new Thread(new Runnable() {
			@Override
			public void run() {
				int nbError = 0;

				UCLouvain uclouvain = new UCLouvain(username, password);
				ArrayList<Cours> cours = uclouvain.getCourses(anac);
				if (cours.isEmpty()) {
					//Suppression des anciens cours
					LLNCampus.getDatabase().delete("Courses", "",null);
					
					// Ajout des nouvelles donnees
					for (Cours c : cours) {
						ContentValues cv = new ContentValues();
						cv.put("CODE", c.coursCode);
						cv.put("NAME", c.coursName);
						
						if (LLNCampus.getDatabase().insert("Courses", cv) < 0) {
							nbError++;
						}
					}
					nb.setContentText("Termine. Nombre d'erreurs : " + nbError);
				} else {
					nb.setContentText("Erreur, la liste des cours n'a pu être mise à jour.");
				}
				nm.notify(NOTIFY_ID, nb.build());
				
				handler.post(updateRunnable);
			}
		}).start();
	}

	@Override
	protected void editActionBar(){
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
			super.notify("Téléchargement de la liste des cours... Merci de patienter");
			break;
		}
	}
}
