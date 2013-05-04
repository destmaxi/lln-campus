package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This activity just shows an about message
 */
public class AboutActivity extends LLNCampusActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		TextView text = (TextView) findViewById(R.id.about_text);
		String nameCode = "0";
		try{
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			nameCode = info.versionName;
		}
		catch(NameNotFoundException nnf)
		{
			nnf.printStackTrace();
		}
		text.setText(getString(R.string.about_text) + nameCode);
	}

	@Override
	protected void editActionBar() {
		// Nothing to do because it's a dialog!
	}
}
