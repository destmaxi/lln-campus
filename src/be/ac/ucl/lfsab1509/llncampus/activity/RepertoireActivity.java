package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import be.ac.ucl.lfsab1509.llncampus.R;

public class RepertoireActivity extends LLNCampusActivity implements
		OnClickListener {
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.repertoire);
		findViewById(R.id.repertoire_searchbtn).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.repertoire_searchbtn:
			Intent intent = new Intent(this, WebviewActivity.class);
			String nom = (((EditText) findViewById(R.id.repertoire_nom))
					.getText().toString().replace(' ', '+'));
			String prenom = (((EditText) findViewById(R.id.repertoire_prenom))
					.getText().toString().replace(' ', '+'));
			String entite = (((EditText) findViewById(R.id.repertoire_entite))
					.getText().toString().replace(' ', '+'));
			intent.putExtra("URL",
					"http://www.uclouvain.be/repertoire-personnel.html"
							+ "?nom=" + nom + "&prenom=" + prenom
							+ "&output=detail&sigle=" + entite
							+ "&Envoi=Effectuer+la+recherche");
			intent.putExtra("TITLE", getTitle());
			intent.putExtra("CSS", "#menu, #header{display:none;}");
			startActivity(intent);
			break;
		}
	}
	
}
